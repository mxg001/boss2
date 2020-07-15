/**
 * 业务组织管理
 */
angular.module('inspinia', ['angularFileUpload']).controller('redOrgCtrl', function($scope, $http,$stateParams,i18nService, FileUploader, SweetAlert){
    //数据源
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    $scope.resetForm = function () {
        $scope.baseInfo = {busCode:$stateParams.busCode,orgId:-1,busType:$stateParams.busType};
    }
    $scope.resetForm();

    $scope.columnDefs = [
        {field: 'orgId',displayName: '品牌组织ID',pinnable: false,sortable: false},
        {field: 'orgName',displayName: '组织名称',pinnable: false,sortable: false},
        {field: 'action',displayName: '操作',pinnedRight:true,sortable: false,editable:true,cellTemplate:
            '<a class="lh30" ng-show="grid.appScope.hasPermit(\'red.deleteRedOrg\')" '
            + 'ng-click="grid.appScope.deleteRedOrg(row.entity.id,row.entity.orgId)">删除</a>'}
    ];

    $scope.grid = {
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
//		rowHeight:35,
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
            url: 'red/redOrg?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
            data: $scope.baseInfo,
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

    $scope.deleteRedOrg = function(id,orgId){
        SweetAlert.swal({
                title: "删除关联组织将同步删除分类布局相同组织记录？",
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
                        url:"red/deleteRedOrg",
                        method:"POST",
                        data:id
                    }).success(function(result){
                        $scope.notice(result.msg);
                        if(result.status){
                            $scope.query();
                        }
                    });
                    
                    $http({
                        url:"red/deleteRedOrgSort",
                        method:"POST",
                        data:{orgIds:orgId,busCode:$scope.baseInfo.busCode}
                    }).success(function(result){
                    });
                }
            });
    }

    $scope.deleteBatch = function(){
        var redOrgId = "";
        var orgIds = "";
        $scope.selectedRedOrg = $scope.gridApi.selection.getSelectedRows();
        if($scope.selectedRedOrg!=null && $scope.selectedRedOrg.length>0){
            for (var i = 0; i < $scope.selectedRedOrg.length; i++) {
                redOrgId = redOrgId + $scope.selectedRedOrg[i].id + ',';
                orgIds = orgIds + $scope.selectedRedOrg[i].orgId + ',';
            }
        } else {
            $scope.notice('请选择需要删除的数据');
            return;
        }
        SweetAlert.swal({
                title: "删除关联组织将同步删除分类布局相同组织记录？",
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
                        url:"red/deleteRedOrg",
                        method:"POST",
                        data:redOrgId
                    }).success(function(result){
                        $scope.notice(result.msg);
                        if(result.status){
                            $scope.query();
                        }
                    });
                    
                    $http({
                        url:"red/deleteRedOrgSort",
                        method:"POST",
                        data:{orgIds:orgIds.replace(/,$/,""),busCode:$scope.baseInfo.busCode}
                    }).success(function(result){
                    });
                }
            });
    }

    //新增
    $scope.addRedOrg = function(){

        if(!$scope.addInfo.orgId){
            $scope.notice('组织不能为空');
            return;
        }
        $http({
            url:"red/addRedOrg",
            method:"POST",
            data:$scope.addInfo
        }).success(function(result){
            $scope.notice(result.msg);
            if(result.status){
                $scope.cancel();
                $scope.query();
            }
        });
    }

    //上传文件,定义控制器路径
    $scope.uploader = new FileUploader({
        url: 'red/importRedOrg',
        queueLimit: 1,   //文件个数
        removeAfterUpload: true,  //上传后删除文件
        headers : {'X-CSRF-TOKEN' : $scope.csrfData},
        formData:[{busCode:""}]
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

    $scope.cancel = function(){
        $('#importModal').modal('hide');
        $('#addModal').modal('hide');
    }
    //新增modal
    $scope.addModal = function(){
        $scope.addInfo = {busCode: $stateParams.busCode, orgId: -1};
        $('#addModal').modal('show');
    }

    //批量导入提交数据
    $scope.importStatus = false;
    $scope.importInfo = function(){
        $scope.importStatus = true;
        $scope.uploader.queue[0].formData[0].busCode = $stateParams.busCode;
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

    //获取所有的银行家组织
    $scope.orgInfoList = [{orgId:-1,orgName:"全部"}];
    $scope.getOrgInfoList = function () {
        $http({
            url:"superBank/getOrgInfoList",
            method:"POST"
        }).success(function(msg){
            if(msg.status){
                $scope.orgInfoList = msg.data;
                $scope.orgInfoList.unshift({orgId:-1,orgName:"全部"});
            } else {
                $scope.notice("获取组织信息异常");
            }
        }).error(function(){
            $scope.notice("获取组织信息异常");
        })
    };
    $scope.getOrgInfoList();
});
