/**
 * 账单清单
 */
angular.module('inspinia').controller('billDetailCtrl',function($scope,$http,$state,$stateParams,i18nService,$document,SweetAlert){

	//数据源
	i18nService.setCurrentLang('zh-cn');

	$scope.paginationOptions = {pageNo : 1,pageSize : 10};
	$scope.transTypeSelect = [{text:"消费",value:'0'},{text:"还款",value:'1'}];

	$scope.resetForm = function () {
		$scope.info = {};
	}
	$scope.resetForm();

	$scope.columnDefs = [
		{field: 'id',displayName: '账单明细ID',width: 150},
		{field: 'refBillId',displayName: '账单ID',width: 150},
		{field: 'userNo',displayName: '用户ID',width: 150},
		{field: 'cardNo',displayName: '银行卡号',width: 150},
		{field: 'transDesc',displayName: '交易说明',width: 150},
		{field: 'transDate',displayName: '交易时间',width: 150,
			cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
		{field: 'transType',displayName: '交易类型',width: 150},
		{field: 'transAmt',displayName: '交易金额',width: 150},
		{field: 'transArea',displayName: '交易地',width: 150,cellTemplate:'<div class="col-sm-12 checkbox">-</div>'}//暂时留空
	];

	$scope.myGrid = {
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
		if ($scope.loadImg) {
			return;
		}
		$scope.loadImg = true;
		$scope.info.refBillId = $stateParams.id;
		$http({
			url: 'cmBill/selectBillDetail?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
			data: $scope.info,
			method:'POST'
		}).success(function (data) {
			$scope.loadImg = false;
			if (!data.status){
				$scope.notice(data.msg);
				return;
			}
			$scope.myGrid.data = data.page.result;
			$scope.myGrid.totalItems = data.page.totalCount;
		}).error(function () {
			$scope.notice('服务器异常,请稍后再试.');
			$scope.loadImg = false;
		});
	};
	$scope.query();

    //导出
    $scope.exportInfo=function(){
        SweetAlert.swal({
                title: "确认导出？",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true
            },
            function (isConfirm) {
                if (isConfirm) {
                    location.href="cmBill/exportBillDetailInfo?info="+encodeURI(angular.toJson($scope.info));
                }
            });
    }
	//页面绑定回车事件
	$document.bind("keypress", function(event) {
		$scope.$apply(function (){
			if(event.keyCode == 13){
				$scope.query();
			}
		})
	});
});