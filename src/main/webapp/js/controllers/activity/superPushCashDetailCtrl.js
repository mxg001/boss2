/**
 * 微创业提现详情
 */

angular.module('inspinia').controller('superPushCashDetailCtrl',function($scope,$http,$stateParams,i18nService){
	i18nService.setCurrentLang('zh-cn');
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	//数据源
	$scope.cashDetail = [];
	$http.get('superPush/superPushCashDetail?merchantNo='+$stateParams.merchantNo)
	.success(function(msg) {
		if(msg.status){
			$scope.baseInfo = msg.baseInfo;
			$scope.merchantCardInfo = msg.merchantCardInfo;
		}
	});
	//提现流水
	$scope.query = function(){
		$http.post('superPush/getCashPage',"merchantNo="+$stateParams.merchantNo+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+
			$scope.paginationOptions.pageSize,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			.success(function(msg){
				if(msg.status){
					$scope.cashGrid.data = msg.page.result;
					$scope.cashGrid.totalItems = msg.page.totalCount;
				}
		})
	}
	$scope.query();
	
	$scope.cashGrid={
		paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],	//切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
		rowHeight:40,
		columnDefs: [
 	         {field: 'settleOrder',displayName: '申请编号',pinnable: false,sortable: false},
 	         {field: 'createTime',displayName: '申请时间',pinnable: false,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
 	         {field: 'settleAmount',displayName: '申请金额',pinnable: false,sortable: false},
 	         {field: 'settleStatus',displayName: '状态',pinnable: false,sortable: false,cellFilter:'formatDropping:'+angular.toJson($scope.settleStatus)},
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
});
