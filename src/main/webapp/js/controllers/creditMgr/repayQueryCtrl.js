/**
 * 还款查询
 */
angular.module('inspinia',['infinity.angular-chosen']).controller('repayQueryCtrl',function($scope,$http,$state,$stateParams,i18nService,$document,SweetAlert,$timeout){
	//数据源
	i18nService.setCurrentLang('zh-cn');

	$scope.paginationOptions = {pageNo : 1,pageSize : 10};
	$scope.payWaySelect = [{text:"手工标记",value:'1'},{text:"超级还-分期",value:'2'},{text:"超级还-全额",value:'3'},{text:"超级还-完美",value:'4'}];
	$scope.billStatusSelect = [{text:"未还款",value:'0'},{text:"已还款",value:'1'}];

	$scope.resetForm = function () {
		$scope.info = {sPayDate:moment(new Date().getTime()-6*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
	            ePayDate:moment(new Date().getTime()).format('YYYY-MM-DD'+' 23:59:59')};
	}
	$scope.resetForm();

	$scope.columnDefs = [
		{field: 'id',displayName: '还款ID',width: 120},
		{field: 'orderId',displayName: '关联订单号',width: 150},
		{field: 'userNo',displayName: '用户ID',width: 150},
		{field: 'cardNo',displayName: '银行卡号',width: 150},
		{field: 'mobileNo',displayName: '手机号',width: 150},
		{field: 'userName',displayName: '姓名',width: 120},
		{field: 'orderMoney',displayName: '还款金额',width: 150},
		{field: 'billStatus',displayName: '还款状态',width: 130, cellFilter:"formatDropping:"+ angular.toJson($scope.billStatusSelect)},
		{field: 'payWay',displayName: '还款方式',width: 150, cellFilter:"formatDropping:"+ angular.toJson($scope.payWaySelect)},
		{field: 'payDate',displayName: '还款时间',width: 150, cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
		{field: 'action',displayName: '操作',width: 150,pinnedRight:true,editable:true,cellTemplate:
			'<a class="lh30" ui-sref="creditMgr.repayDetail({id:row.entity.id})" target="_black">详情</a>'}
	];

	$scope.myGrid = {
		paginationPageSize:10,                  //分页数量
		paginationPageSizes: [10,20,50,100],	//切换每页记录数
		useExternalPagination: true,		  //开启拓展名
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
			url: 'cmRepay/selectRepayInfo?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
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
		}).error(function (msg) {
			$scope.notice('服务器异常,请稍后再试.');
			$scope.loadImg = false;
		});
	};

	//导出
	$scope.exportInfo=function(){
		if($scope.info.sPayDate == "" || $scope.info.ePayDate == ""){
			$scope.notice("还款时间不能为空");
			return;
		}
		var stime = new Date($scope.info.sPayDate).getTime();
		var etime = new Date($scope.info.ePayDate).getTime();
		var sum = (etime - stime) / (24 * 60 * 60 * 1000);
		if(sum > 31){
			$scope.notice("还款时间的跨度不能超过一个月");
			return;
		}
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
				location.href="cmRepay/exportRepayInfo?info="+encodeURI(angular.toJson($scope.info));
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