/**
 * 账单清单
 */
angular.module('inspinia',['infinity.angular-chosen']).controller('billInventoryCtrl',function($scope,$http,$state,$stateParams,i18nService,$document,SweetAlert,$timeout){
	//数据源
	i18nService.setCurrentLang('zh-cn');

	$scope.paginationOptions = {pageNo : 1,pageSize : 10};
	$scope.methodSelect = [{text:"邮箱导入",value:'1'},{text:"手工导入",value:'2'},{text:"网银导入",value:'3'}];

	$scope.resetForm = function () {
		$scope.info = {};
	}
	$scope.resetForm();

	$scope.columnDefs = [
		{field: 'id',displayName: '账单ID',width: 150},
		{field: 'userNo',displayName: '用户ID',width: 150},
		{field: 'cardNo',displayName: '银行卡号',width: 150},
		{field: 'bankName',displayName: '银行名称',width: 150},
		{field: 'userName',displayName: '姓名',width: 150},
		{field: 'repayment',displayName: '账单金额',width: 150},
		{field: 'statementDate',displayName: '账单日',width: 150,cellTemplate:
			'<div class="col-sm-12 checkbox">每月{{row.entity.statementDate}}日</div>'},
		{field: 'repaymentDate',displayName: '还款日',width: 150,cellTemplate:
			'<div class="col-sm-12 checkbox">每月{{row.entity.repaymentDate}}日</div>'},
        {field: 'createTime',displayName: '账单导入时间',width: 180, cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
		{field: 'method',displayName: '账单类型',width: 150,
			cellFilter:"formatDropping:"+ angular.toJson($scope.methodSelect)},
		{field: 'reviewTime',displayName: '评测时间',width: 180, cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
		{field: 'billHealth',displayName: '用卡健康度',width: 120,cellTemplate:
			'<a class="lh30" ui-sref="creditMgr.reviewsReport({billId:row.entity.id})" target="_black">{{row.entity.billHealth=="-1" ? "" : row.entity.billHealth}}</a>'},
		{field: 'withrawScore',displayName: '提额指数',width: 120,cellTemplate:
			'<a class="lh30" ui-sref="creditMgr.reviewsReport({billId:row.entity.id})" target="_black">{{row.entity.withrawScore}}</a>'},
		{field: 'action',displayName: '账单明细',width: 150,pinnedRight:true,editable:true,cellTemplate:
			'<a class="lh30" ui-sref="creditMgr.billDetail({id:row.entity.id})" target="_black">查看</a>'}
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
		$http({
			url: 'cmBill/selectBillInfo?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
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
				location.href="cmBill/exportBillInfo?info="+encodeURI(angular.toJson($scope.info));
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