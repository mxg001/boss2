
angular.module('inspinia',['infinity.angular-chosen','uiSwitch','angularFileUpload']).controller('userManagerCtrl',function($scope,$http,$state,$stateParams,SweetAlert,i18nService,$timeout,FileUploader){
	  //数据源
	  i18nService.setCurrentLang('zh-cn');

	  $scope.statusSelect = [{value:"",text:"全部"},{value:"0",text:"关闭"},{value:"1",text:"开启"}];
	  $scope.roleSelect = [{value:"",text:"全部"},{value:"1",text:"普通"},{value:"2",text:"管理员"}];
	  $scope.deptSelect = angular.copy(initData.DEPT_LIST);
    $scope.deptSelect.unshift({value:"",text:"全部"});

    // 查询分页
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    // 查询条件
    $scope.info  = {
        bossUserName:"",deptNo:"",roleType:"",status:""
    };


	  $scope.userManagerGrid = {
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs: [
            {field: 'bossUserName',displayName: 'boss账号',pinnable: false,width: 140,sortable: false},
            {field: 'bossRealName',displayName: '姓名',pinnable: false,width: 140,sortable: false},
            {field: 'deptName',displayName: '所属部门',pinnable: false,width: 140,sortable: false},
            {field: 'roleType',displayName: '人员角色',pinnable: false,width: 140,sortable: false,cellFilter:"formatDropping:"+angular.toJson($scope.roleSelect)},
            {field: 'status',displayName: '状态',width:150,cellTemplate:
                  '<span ng-show="grid.appScope.hasPermit(\'workOrderUser.updateStatus\')"><switch class="switch switch-s" ng-model="row.entity.status" ng-change="grid.appScope.updateStatus(row)" /></span>'
                  +'<span class="lh30" ng-show="!grid.appScope.hasPermit(\'workOrderUser.updateStatus\')"> <span ng-show="row.entity.status==1">开启</span><span ng-show="row.entity.status==0">关闭</span></span>'
            },

            {field: 'dutySale',displayName: '负责销售',pinnable: false,width: 280,sortable: false},
            {field: 'dutyWorkType',displayName: '负责工单类型',pinnable: false,width: 280,sortable: false},
            {field: 'createTime',displayName: '创建日期',pinnable: false,width: 280,sortable: false,cellFilter : 'date:"yyyy-MM-dd HH:mm:ss"'},
            {field: 'action',displayName: '操作',width: 250,pinnedRight:true,sortable: false,editable:true,cellTemplate:
	         	'<div class="lh30">'
	            +'<a class="lh30" ng-show="grid.appScope.hasPermit(\'workOrderUser.edit\')" ui-sref="workOrder.userManagerEdit({id:row.entity.id})" >修改</a>'
	            +'<a class="lh30" ng-show="grid.appScope.hasPermit(\'workOrderUser.del\')" ng-click="grid.appScope.del(row)" > | 删除</a>'
            	+'</div>'
            }
        ],
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            $scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
	          	$scope.paginationOptions.pageNo = newPage;
	          	$scope.paginationOptions.pageSize = pageSize;
	            $scope.query();
	        });
        }
	 };
    // 查询按钮
    $scope.query = function () {
        if ($scope.loadImg) {
            return;
        }
        $scope.loadImg = true;
        $http({
            url: 'workOrderUser/query?pageNo='+$scope.paginationOptions.pageNo +"&pageSize=" + $scope.paginationOptions.pageSize,
            method: 'POST',
            data:$scope.info
        }).success(function (data) {
            $scope.userManagerGrid.data =data.result;
            $scope.userManagerGrid.totalItems = data.totalCount;
            $scope.loadImg = false;
        });
    };

    $scope.query();

  $scope.del=function(row){

    SweetAlert.swal({
        title: "确定删除？",
        text: $scope.serviceText,
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        closeOnConfirm: true,
        closeOnCancel: true },
      function (isConfirm) {
        if (isConfirm) {
          if(row.entity.status==true){
            row.entity.status=1;
          } else if(row.entity.status==false){
            row.entity.status=0;
          }
          $http.get("workOrderUser/del?id="+row.entity.id)
            .success(function(data){
              $scope.notice(data.msg);
              if(data.status){
                $scope.query();
              }
            })
            .error(function(data){
              $scope.notice("服务器异常");
            });
        }
      });
  };


  $scope.updateStatus=function(row){
    if(row.entity.status){
      $scope.serviceText = "确定开启？";
    } else {
      $scope.serviceText = "确定关闭？";
    }
    SweetAlert.swal({
        title: $scope.serviceText,
//            text: "服务状态为关闭后，不能正常交易!",
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "提交",
        cancelButtonText: "取消",
        closeOnConfirm: true,
        closeOnCancel: true },
      function (isConfirm) {
        if (isConfirm) {
          if(row.entity.status==true){
            row.entity.status=1;
          } else if(row.entity.status==false){
            row.entity.status=0;
          }
          var data={"status":row.entity.status,"id":row.entity.id};
          $http.post("workOrderUser/updateStatus",angular.toJson(data))
            .success(function(data){
              if(data.status){
                $scope.notice("操作成功！");
              }else{
                if(row.entity.status==true){
                  row.entity.status = false;
                } else {
                  row.entity.status = true;
                }
                $scope.notice("操作失败！");
              }
            })
            .error(function(data){
              if(row.entity.status==true){
                row.entity.status = false;
              } else {
                row.entity.status = true;
              }
              $scope.notice("服务器异常")
            });
        } else {
          if(row.entity.status==true){
            row.entity.status = false;
          } else {
            row.entity.status = true;
          }
        }
      });

  };



  // 重置按钮
	  $scope.resetForm = function () {
      $scope.info  = {
          bossUserName:"",deptNo:"",roleType:"",status:""
      };
    };

});


