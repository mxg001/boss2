/**
 * 微创业商户分润详情
 */
angular.module('inspinia').controller('superPushShareDetailCtrl',function($scope,$http,$stateParams,i18nService,SweetAlert){
	i18nService.setCurrentLang('zh-cn');
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	$scope.baseInfo = {merchantNo:$stateParams.merchantNo,transTimeStart:"",transTimeEnd:""};
	
	//查询微创业商户分润详情
	$scope.query = function(){
		$http.post('superPush/superPushShareDetail',"baseInfo="+angular.toJson($scope.baseInfo)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+
				$scope.paginationOptions.pageSize,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		.success(function(msg){
			if(msg.status){
				$scope.superPushShareGrid.data = msg.page.result;
				$scope.superPushShareGrid.totalItems = msg.page.totalCount;
			}
		});
	}
	$scope.query();

	$scope.superPushShareGrid = {
	        paginationPageSize:10,                  //分页数量
	        paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
	        useExternalPagination: true,		  //开启拓展名
	        enableHorizontalScrollbar: true,        //横向滚动条
	        enableVerticalScrollbar : true,  		//纵向滚动条
	        columnDefs: [
	            {field: 'index',displayName: '序号',pinnable: false,width: 100,sortable: false,cellTemplate: "<span class='checkbox'>{{rowRenderIndex + 1}}</span>"},
	            {field: 'transTime',displayName: '统计日期',pinnable: false,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
	            {field: 'merchantNo',displayName: '下级商户编号',pinnable: false,sortable: false},
	            {field: 'merchantName',displayName: '下级商户名称',pinnable: false,sortable: false},
	            {field: 'level',displayName: '下级商户级别',pinnable: false,sortable: false},
	            {field: 'share',displayName: '贡献分润金额',pinnable: false,sortable: false},
	            {field: 'rule',displayName: '分润比例',pinnable: false,sortable: false},
	            {field: 'amount',displayName: '交易金额',pinnable: false,sortable: false}
	        ],
	        onRegisterApi: function(gridApi) {   
	            $scope.superPushShareGridApi = gridApi;
	            $scope.superPushShareGridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
		          	$scope.paginationOptions.pageNo = newPage;
		          	$scope.paginationOptions.pageSize = pageSize;
		            $scope.query();
		     });
	        }
		 };
	
	//导出
	 $scope.exportExcel=function(){
	        SweetAlert.swal({
	            title: "确认导出？",
//	            text: "",
//	            type: "warning",
	            showCancelButton: true,
	            confirmButtonColor: "#DD6B55",
	            confirmButtonText: "提交",
	            cancelButtonText: "取消",
	            closeOnConfirm: true,
	            closeOnCancel: true },
		        function (isConfirm) {
		            if (isConfirm) {
		            	if($scope.superPushShareGrid.data==null || $scope.superPushShareGrid.data.length==0){
		            		$scope.notice("没有可导出的数据");
		       			 	return;
		       		 	} else {
				       		 location.href="superPush/exportExcel.do?"
				       		 + "baseInfo="+angular.toJson($scope.baseInfo)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+
							 + $scope.superPushShareGrid.totalItems;
		       		 	}
		            }
	        });
	    };
	
	$scope.resetForm = function(){
		$scope.baseInfo = {merchantNo:$stateParams.merchantNo,transTimeStart:"",transTimeEnd:""};
	}

});

