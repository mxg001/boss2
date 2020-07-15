/**
 * 红包业务/分类布局管理
 */
angular.module('inspinia', ['angularFileUpload']).controller('redOrgSortCtrl', function($scope, $http,$stateParams,i18nService, FileUploader, SweetAlert,$state){
    //数据源
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    $scope.providerSysKey = "REDBUSINESSCATEGORY";// 红包业务分类系统字典表对应的KEY

    $scope.baseInfo = {	"id":$stateParams.id,
			"busCode":$stateParams.busCode,
			"busType":$stateParams.busType,
			"orgStatus":$stateParams.orgStatus};
    
    $scope.resetForm = function () {
        $scope.paramInfo = {busCode:$stateParams.busCode,orgId:"",busType:$stateParams.busType,category:""};
    }
    $scope.resetForm();

    $scope.columnDefs = [
        {field: 'orgId',displayName: '品牌组织ID',pinnable: false,sortable: false},
        {field: 'orgName',displayName: '组织名称',pinnable: false,sortable: false,cellTemplate:'<span>{{ (grid.appScope.orgInfoList | filter : {"orgId":row.entity.orgId})[0].orgName }}</span>'},
        {field: 'category', displayName: '所属分类',width: 120,pinnable: false,sortable: false,cellTemplate:'<span>{{ (grid.appScope.sysDictList | filter : {"code":row.entity.category})[0].name }}</span>'},
        {field: 'sortNum', displayName: '排序顺序',width: 120,pinnable: false,sortable: false},
        {field: 'action',displayName: '操作',pinnedRight:true,sortable: false,editable:true,cellTemplate:
            '<a class="lh30" ng-click="grid.appScope.editRedOrgSort(row.entity.id,row.entity.orgId,row.entity.category,row.entity.sortNum)">修改</a>'
        	+'<a class="lh30" ng-click="grid.appScope.deleteRedOrgSort(row.entity.id)"> | 删除</a>'
        	}
    ];

    $scope.grid = {
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs: $scope.columnDefs,
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            $scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.paginationOptions.pageNo = newPage;
                $scope.paginationOptions.pageSize = pageSize;
                $scope.query();
            });
        }
    };

    $scope.query = function () {
        $scope.submitting = true;
        $scope.loadImg = true;
        $http({
            url: 'red/redOrgSort?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
            data: $scope.paramInfo,
            method:'POST'
        }).success(function (result) {
            $scope.submitting = false;
            $scope.loadImg = false;
            if (!result.status){
                $scope.notice(result.msg);
                return;
            }
            $scope.grid.data = result.data.result;
            $scope.grid.totalItems = result.data.totalCount;
        });
    };
    $scope.query();

    $scope.deleteRedOrgSort = function(id){
        SweetAlert.swal({
                title: "确定删除？",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http({
                        url:"red/deleteRedOrgSort",
                        method:"POST",
                        data:{ids:id}
                    }).success(function(result){
                        $scope.notice(result.msg);
                        if(result.status){
                            $scope.query();
                        }
                    });
                }
            });
    }

    // 批量删除
    $scope.deleteBatch = function(){
        var ids = "";
        $scope.selectedRedOrg = $scope.gridApi.selection.getSelectedRows();
        if($scope.selectedRedOrg!=null && $scope.selectedRedOrg.length>0){
            for (var i = 0; i < $scope.selectedRedOrg.length; i++) {
            	ids = ids + $scope.selectedRedOrg[i].id + ',';
            }
        } else {
            $scope.notice('请选择需要删除的数据');
            return;
        }
        SweetAlert.swal({
                title: "确定删除？",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http({
                        url:"red/deleteRedOrgSort",
                        method:"POST",
                        data:{ids:ids.replace(/,$/,"")}
                    }).success(function(result){
                        $scope.notice(result.msg);
                        if(result.status){
                            $scope.query();
                        }
                    });
                }
            });
    }

    //上传文件,定义控制器路径
    $scope.uploader = new FileUploader({
        url: 'red/importRedOrgSort',
        queueLimit: 1,   //文件个数
        removeAfterUpload: true,  //上传后删除文件
        headers : {'X-CSRF-TOKEN' : $scope.csrfData},
        formData:[{busCode:"",orgStatus:0}]
    });
    //过滤格式
    $scope.uploader.filters.push({
        name: 'fileFilter',
        fn: function(item /*{File|FileLikeObject}*/, options) {
            var type = '|' + item.name.slice(item.name.lastIndexOf('.') + 1) + '|';
            return '|xlsx|xls|'.indexOf(type) !== -1;
        }
    });
    //批量导入modal
    $scope.addBatchModal = function(){
        $('#importModal').modal('show');
    }

    //批量导入提交数据
    $scope.importStatus = false;
    $scope.importInfo = function(){
        $scope.importStatus = true;
        $scope.uploader.queue[0].formData[0].busCode = $stateParams.busCode;
        $scope.uploader.queue[0].formData[0].orgStatus = $stateParams.orgStatus;
        if($scope.uploader.queue.length > 0){
            $scope.uploader.uploadAll();
            $scope.uploader.onSuccessItem = function(fileItem, response, status, headers) {//上传成功后的回调函数，在里面执行提交
                $scope.importStatus = false;
                $scope.notice(response.msg);
                $scope.cancel();
                $scope.query();
            };
        }
    }

    $scope.cancel = function(){
    	$("#importModal").modal('hide');
    }
    
    // 修改红包业务组织分类
    $scope.editRedOrgSort = function(id,orgId,category,sortNum){
    	$scope.baseInfo.id = id;
    	$scope.baseInfo.orgId = orgId;
    	$scope.baseInfo.category = category;
    	$scope.baseInfo.sortNum = sortNum;
    	$scope.baseInfo.flag = "edit";
    	$state.go("red.redOrgSortControl",$scope.baseInfo);
    }

    // 获取组织列表
    $scope.orgInfoList=[{orgId:"",orgName:"全部"},{orgId:"-1",orgName:"默认"}];
    $scope.getAllOrg = function(){
    	var data = {};
    	var httpUrl = "superBank/getOrgInfoList";
    	if($scope.baseInfo.orgStatus == 1){
    		httpUrl = "red/redOrgListAll";
    		data = {
    			orgId : "-1",
    			busCode : $scope.baseInfo.busCode,
    			busType : $scope.baseInfo.busType
    		};
    	}
    	
        $http({
            url:httpUrl,
            method:"POST",
            data : data
        }).success(function(msg){
            if(msg.status){
            	$scope.orgInfoList.push.apply($scope.orgInfoList,msg.data);
            }
        }).error(function(){
            $scope.notice("获取组织列表异常");
        });
    };
    $scope.getAllOrg();
    
    
    $scope.sysDictList = [{code:"",name:"全部"}];
    $scope.getSysDict = function(){
      $http({
          url:"sysOption/selectSysOption",
          data:{optionGroupCode:$scope.providerSysKey},
          method:"POST"
      }).success(function(data){
    	  $scope.sysDictList.push.apply($scope.sysDictList,data.data);
      }).error(function(){
          $scope.notice("获取红包业务类别异常!");
      });
    };
    $scope.getSysDict();
});
