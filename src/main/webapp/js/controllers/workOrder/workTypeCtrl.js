/**
 * 超级nfc，激活码管理
 */
angular.module('inspinia',['infinity.angular-chosen','angularFileUpload']).controller('workTypeCtrl',function(SweetAlert,$scope,$http,$state,$stateParams,i18nService,$timeout,FileUploader){
	//数据源
	  i18nService.setCurrentLang('zh-cn');

    $scope.nameSelect = [];
    $scope.replyTypeSelect = [{text:"全部",value:""},{text:"一级和所属代理商均可回复",value:1},{text:"仅有一级代理商回复",value:2},{text:"仅有所属代理商回复",value:3}];

    // 重置按钮
    $scope.resetForm = function () {
        $scope.info = {name:"",replyType:""};
    };

    $scope.resetForm();
    // 查询分页
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    $http.get('workType/getAll')
      .success(function(result) {
          if(!result.status){
              return;
          }
          if(result.data.length==0) {
              $scope.nameSelect.push({value: "", text: "全部"});
          }else{
              $scope.nameSelect.push({value: "", text: "全部"});
              for(var i=0; i<result.data.length; i++){
                  $scope.nameSelect.push({value:result.data[i].id,text: result.data[i].name});
              }
          }
      });

	  $scope.workTypeGrid = {
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs: [
            {field: 'id',displayName: 'ID',pinnable: false,width: 80,sortable: false},
            {field: 'name',displayName: '类型名称',pinnable: false,width: 280,sortable: false},
            {field: 'dealProcessName',displayName: '处理流程',pinnable: false,width: 380,sortable: false},
          //{field: 'desc',displayName: '类型说明',pinnable: false,width: 280,sortable: false},
            {field: 'createTime',displayName: '创建日期',pinnable: false,width: 280,sortable: false,cellFilter : 'date:"yyyy-MM-dd HH:mm:ss"'},
            {field: 'action',displayName: '操作',width: 250,pinnedRight:true,sortable: false,editable:true,cellTemplate:
	         	'<div class="lh30">'
                  +'<a class="lh30" ng-show="grid.appScope.hasPermit(\'workType.detail\')" ui-sref="workOrder.workTypeDetail({id:row.entity.id})" >详情</a>'
                  +'<a class="lh30" ng-show="grid.appScope.hasPermit(\'workType.edit\')" ui-sref="workOrder.workTypeEdit({id:row.entity.id})" > | 修改</a>'
                  +'<a class="lh30" ng-show="grid.appScope.hasPermit(\'workType.del\')" ng-click="grid.appScope.del(row)" > | 删除</a>'
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
            url: 'workType/query?pageNo='+$scope.paginationOptions.pageNo +"&pageSize=" + $scope.paginationOptions.pageSize,
            method: 'POST',
            data:$scope.info
        }).success(function (result) {
            if(result.status){
              $scope.workTypeGrid.data =result.data.result;
              $scope.workTypeGrid.totalItems = result.data.totalCount;
            }else{
              $scope.notice(result.msg);
            }
            $scope.loadImg = false;
        });
    };

    $scope.query();


    //删除工单类型
    $scope.del = function(row){
      SweetAlert.swal({
          title: "确认删除？",
          showCancelButton: true,
          confirmButtonColor: "#DD6B55",
          confirmButtonText: "提交",
          cancelButtonText: "取消",
          closeOnConfirm: true,
          closeOnCancel: true
        },
        function (isConfirm) {
          if (isConfirm) {
            $http({
              url: 'workType/del?id='+row.entity.id,
              method: 'get'
            }).success(function (result) {
              if(result.status){
                $scope.query();
              }
              $scope.notice(result.msg);
            });
          }
        });

    }


});


