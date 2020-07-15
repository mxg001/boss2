/**
 * 结算订单管理
 */
angular.module('inspinia',['infinity.angular-chosen']).controller('settleOrderCtrl',function($scope,$http,$state,$stateParams,i18nService,$document,SweetAlert,$timeout){
	//数据源
	i18nService.setCurrentLang('zh-cn');

	$scope.statusSelect = [{text:"未结算",value:'0'},{text:"结算中",value:'1'},{text:"已结算",value:'2'},{text:"结算失败",value:'3'}];
	$scope.serviceSelect = [{text:"计划还款",value:'repayPlan'},{text:"用户提现",value:'withdraw'}];
	$scope.acqCodeSelect = [{text:"中茂",value:'ZM'},{text:"合利宝",value:'HLB'},{text:"开放平台",value:'openPay'}];
	$scope.paginationOptions = {pageNo : 1,pageSize : 10};
	$scope.baseInfo = {status:'', service:'',
			sCreateTime:moment(new Date().getTime()-6*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
			eCreateTime:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'};
	$scope.sumAmount = '';

	//获取出款渠道
	$scope.listAcqCode = function () {
		$http.post('repaySettleOrder/listAcqCode')
			.success(function(data){
				if(data.status){
					$scope.acqCodeSelect = data.acqCodes;
				}else{
					$scope.acqCodeSelect = [{text:"中茂",value:'ZM'},{text:"合利宝",value:'HLB'},{text:"开放平台",value:'openPay'}];
				}
			});
	}
	$scope.listAcqCode();

	$scope.columnDefs = [
		{field: 'orderNo',displayName: '订单ID',width: 150},
		{field: 'serviceOrderNo',displayName: '业务订单ID',width: 150},
		{field: 'merNo',displayName: '用户编号',width: 150},
		{field: 'channelName',displayName: '出款渠道',width: 120},
		{field: 'outAccNo',displayName: '上游商户号',width: 120},
		{field: 'nickname',displayName: '昵称',width: 150},
		{field: 'accName',displayName: '姓名',width: 150},
		{field: 'mobileNo',displayName: '手机号',width: 150},
		{field: 'accNo',displayName: '银行卡号',width: 150},
		{field: 'status',displayName: '结算状态',width: 120,
			cellFilter:"formatDropping:"+ angular.toJson($scope.statusSelect)},
		{field: 'service',displayName: '订单类型',width: 120,
			cellFilter:"formatDropping:"+ angular.toJson($scope.serviceSelect)},
		{field: 'amount',displayName: '出款金额',cellFilter:"currency:''",width: 150},
		{field: 'fee',displayName: '手续费',cellFilter:"currency:''",width: 150},
		{field: 'bankOrderNo',displayName: '上游订单号',width: 150},
		{field: 'createTime',displayName: '时间',width: 150,
			cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
		{field: 'action',displayName: '操作',width: 120,pinnedRight:true,editable:true,cellTemplate:
			'<a class="lh30" ng-show="row.entity.status==3 && row.entity.service==\'withdraw\' && grid.appScope.hasPermit(\'repaySettleOrder.againPayment\')" '
			+ 'ng-click="grid.appScope.againPayment(row.entity.orderNo)">再次出款</a>'}
	];

	$scope.myGrid = {
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
		if ($scope.loadImg) {
			return;
		}
		$scope.loadImg = true;
		$http({
			url: 'repaySettleOrder/selectSettleOrderByParam?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
			data: $scope.baseInfo,
			method:'POST'
		}).success(function (msg) {
			if (!msg.status){
				$scope.notice(msg.msg);
				return;
			}
			$scope.myGrid.data = msg.page.result;
			$scope.myGrid.totalItems = msg.page.totalCount;
			$scope.sumAmount = msg.sumAmount;
			$scope.loadImg = false;
		}).error(function (msg) {
			$scope.notice('服务器异常,请稍后再试.');
			$scope.loadImg = false;
		});
	};

	// 再次出款
	$scope.againPayment = function (orderNo) {
		SweetAlert.swal({
			title: "确定出款吗？",
			type: "warning",
			showCancelButton: true,
			confirmButtonColor: "#DD6B55",
			confirmButtonText: "确定",
			cancelButtonText: "取消",
			closeOnConfirm: true,
			closeOnCancel: true },
			function (isConfirm) {
				if (isConfirm) {
					$http({
						url: 'repaySettleOrder/againPayment',
						data: orderNo,
						method:'POST'
					}).success(function (msg) {
						$scope.notice(msg.msg);
						if (msg.status){
							$scope.query();
						}
					}).error(function (msg) {
						$scope.notice('服务器异常,请稍后再试.');
					});
				}
			});
	}

	// 导出
	$scope.exportInfo = function () {
		SweetAlert.swal({
			title: "确定导出吗？",
			type: "warning",
			showCancelButton: true,
			confirmButtonColor: "#DD6B55",
			confirmButtonText: "确定",
			cancelButtonText: "取消",
			closeOnConfirm: true,
			closeOnCancel: true },
			function (isConfirm) {
				if (isConfirm) {
					location.href="repaySettleOrder/exportInfo?baseInfo="+encodeURI(angular.toJson($scope.baseInfo));
				}
			});
	}

	$scope.resetForm = function () {
		$scope.baseInfo = {status:'', service:'',
				sCreateTime:moment(new Date().getTime()-6*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
				eCreateTime:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'};
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