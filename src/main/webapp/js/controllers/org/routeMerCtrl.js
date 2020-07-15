/**
 * 集群中普通商户
 */
angular.module('inspinia').controller("routeMerCtrl", function($scope, $http, $state, $stateParams, i18nService,$document) {
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	i18nService.setCurrentLang('zh-cn');
	
	$scope.info = {};
	$scope.query = function() {
		$http.post('orgRouteMer/queryOrgRouteMerList.do',
       		 "info="+angular.toJson($scope.info)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
       		 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
        ).success(function(data){
            $scope.routeMerDate = data.result;
			$scope.routeMerGrid.totalItems = data.totalCount;//总记录数
        }).error(function(){
        }); 
	};
	// $scope.query();
	
	$scope.reset = function() {
		$scope.info = {};
	};
	
	$scope.routeMerGrid = {
	        data: 'routeMerDate',
	        paginationPageSize:10,                  //分页数量
	        paginationPageSizes: [10,20,50,100],	  //切换每页记录数
	        useExternalPagination: true,		  //开启拓展名
	        enableHorizontalScrollbar: 0,        //去掉滚动条
	        enableVerticalScrollbar : 0, 
	        columnDefs: [
                {field: 'id',displayName: '序号',pinnable: false,sortable: false},
                {field: 'pos_merchant_no',displayName: '商户编号',pinnable: false,sortable: false},
                {field: 'merchant_name',displayName: '商户名称',pinnable: false,sortable: false},
                {field: 'service_type',displayName: '服务类型',pinnable: false,sortable: false,cellFilter:"formatDropping:" + angular.toJson($scope.serviceTypes)},
                {field: 'group_code',displayName: '集群编号',pinnable: false,sortable: false},
                {field: 'group_name',displayName: '集群名称',width:300,pinnable: false,sortable: false},
	            {name: 'action',displayName: '操作',pinnable: false,sortable: false,editable:true,cellTemplate:
	            	"<a ng-show='grid.appScope.hasPermit(\"orgRouteMer.delete\")' ng-click='grid.appScope.delConfirm(row.entity.id)' class='btn'>删除</a>"}
	        ],
	        onRegisterApi: function(gridApi) {	//选中行配置
	            $scope.gridApi = gridApi;
	            $scope.gridApi.selection.on.rowSelectionChanged($scope,function (row,event) {
	               if(row){
	                  $scope.testRow = row.entity;
	               }
	            })
	        },
	        onRegisterApi: function(gridApi) {                
	            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
	            	$scope.paginationOptions.pageNo = newPage;
	            	$scope.paginationOptions.pageSize = pageSize;
	            	$scope.query();
	            });
	        }
	};
	
	$scope.delConfirm = function(deleteId) {
		$('#myModal').modal('show');
		$scope.deleteId = deleteId;
	};
	
	$scope.confirm = function() {
		$http.post('orgRouteMer/delOrgRouteMer.do',
       		 angular.toJson({id:$scope.deleteId})
        ).success(function(msg){
        	$('#myModal').modal('hide');
        	$scope.notice(msg.msg);
        	if(msg.status)
        		$scope.query();
        }).error(function(){
        }); 
	}
	
	//当模态框隐藏时触发
	$('#myModal').on('hide.bs.modal', function () {
		$scope.deleteId = "";
	})

	//页面绑定回车事件
	$document.bind("keypress", function(event) {
		$scope.$apply(function (){
			if(event.keyCode == 13){
				$scope.query();
			}
		})
	});
	
});