/**
 * 交易查询
 */
angular.module('inspinia',['infinity.angular-chosen']).controller('transactionQueryCtrl',function($scope,$http,$state,$stateParams,i18nService,$document,SweetAlert,$timeout){
	i18nService.setCurrentLang('zh-cn');

	$scope.resetForm = function () {
		isVerifyTime = 1;
		$scope.baseInfo = {sCreateTime:moment(new Date().getTime()-6*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
	            eCreateTime:moment(new Date().getTime()).format('YYYY-MM-DD'+' 23:59:59')};
	}
	$scope.resetForm();

	$scope.paginationOptions = {pageNo : 1,pageSize : 10};
	$scope.serviceSelect = [{text:"还款计划消费",value:'repayPlan'},{text:"保证金",value:'ensure'},{text:"全额还款手续费",value:'fullRepayFee'},{text:"完美还款消费",value:'perfectPlan'}];
	$scope.transTypeSelect = [{text:"快捷",value:'quickPay'}];
	$scope.transStatusSelect = [{text:"初始化",value:'0'},{text:"交易中",value:'1'},{text:"交易成功",value:'2'},{text:"交易失败",value:'3'},{text:"未知",value:'4'}];
	$scope.recordStatusSelect = [{text:"未记账",value:'0'},{text:"记账中",value:'1'},{text:"记账成功",value:'2'},{text:"记账失败",value:'3'}];
	$scope.acqCodeSelect = [{text:"中茂",value:'ZM'},{text:"合利宝",value:'HLB'},{text:"开放平台",value:'openPay'}];

	var defatltCountAmount = {sumAmount: "0.00", sumFeeAmount: '0.00'};
	$scope.countAmount = defatltCountAmount;

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

	//是否校验时间
    isVerifyTime = 1;//校验：1，不校验：0

    keyChange=function(){
    	if ($scope.baseInfo.orderNo || $scope.baseInfo.merchantNo || $scope.baseInfo.serviceOrderNo) {
    		isVerifyTime = 0;
    	} else {
    		isVerifyTime = 1;
    	}
    }

	$scope.columnDefs = [
		{field: 'orderNo',displayName: '订单ID',width: 150},
		{field: 'merchantNo',displayName: '用户编号',width: 150},
		{field: 'nickname',displayName: '昵称',width: 150},
		{field: 'mobileNo',displayName: '手机号',width: 150},
		{field: 'service',displayName: '订单类型',width: 150,
			cellFilter:"formatDropping:"+ angular.toJson($scope.serviceSelect)},
		{field: 'transType',displayName: '交易方式',width: 150,
			cellFilter:"formatDropping:"+ angular.toJson($scope.transTypeSelect)},
		{field: 'transStatus',displayName: '交易状态',width: 150,
			cellFilter:"formatDropping:"+ angular.toJson($scope.transStatusSelect)},
		{field: 'transAmount',displayName: '交易金额',cellFilter:"currency:''",width: 150},
		{field: 'transFee',displayName: '交易手续费',cellFilter:"currency:''",width: 150},
		{field: 'transFeeRate',displayName: '交易费率',width: 150},
		{field: 'serviceOrderNo',displayName: '关联业务订单',width: 150},
		{field: 'recordStatus',displayName: '记账状态',width: 150,
			cellFilter:"formatDropping:"+ angular.toJson($scope.recordStatusSelect)},
		{field: 'acqCode',displayName: '交易通道',width: 150},
		{field: 'acqMerchantNo',displayName: '上游商户号',width: 150},
		{field: 'accountNo',displayName: '银行卡号',width: 150},
		{field: 'resMsg',displayName: '错误详情',width: 200},
		{field: 'createTime',displayName: '创建时间',width: 150,
			cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
		{field: 'transTime',displayName: '交易时间',width: 150,
			cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
		{field: 'action',displayName: '操作',width: 120,pinnedRight:true,editable:true,cellTemplate:
			'<a class="lh30" ui-sref="creditRepay.transactionDetail({orderNo:row.entity.orderNo})" target="_black">详情</a>'}
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
		if (!($scope.baseInfo.orderNo || $scope.baseInfo.merchantNo || $scope.baseInfo.serviceOrderNo)) {
			if(!($scope.baseInfo.sCreateTime && $scope.baseInfo.eCreateTime)){
    			$scope.notice("创建时间不能为空");
    			return;
    		}
        	var stime = new Date($scope.baseInfo.sCreateTime).getTime();
        	var etime = new Date($scope.baseInfo.eCreateTime).getTime();
        	if ((etime - stime) > (31 * 24 * 60 * 60 * 1000)) {
        		$scope.notice("创建时间范围不能超过31天");
                return;
        	}
    	}
		$scope.loadImg = true;
		$http({
			url: 'repaySettleOrder/selectRepayTradeOrder?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
			data: $scope.baseInfo,
			method:'POST'
		}).success(function (msg) {
			if (msg.status){
				$scope.myGrid.data = msg.page.result;
				$scope.myGrid.totalItems = msg.page.totalCount;
				$scope.countAmount = msg.count;
				angular.forEach($scope.myGrid.data,function(item) {
					item.acqCode = format(item.acqCode);
				});
			} else {
				$scope.notice(msg.msg);
				$scope.countAmount = defatltCountAmount;
			}
			$scope.loadImg = false;
		}).error(function (msg) {
			$scope.notice('服务器异常,请稍后再试.');
			$scope.loadImg = false;
		});
	};

	format = function(data){
		for (var i = 0; i < $scope.acqCodeSelect.length; i++) {
			if ($scope.acqCodeSelect[i].value == data) {
				return $scope.acqCodeSelect[i].text;
			}
		}
		return data;
	}

	//导出
	$scope.exportInfo=function(){
		if($scope.baseInfo.sCreateTime == "" || $scope.baseInfo.eCreateTime == ""){
			$scope.notice("创建时间不能为空");
			return;
		}
		var stime = new Date($scope.baseInfo.sCreateTime).getTime();
		var etime = new Date($scope.baseInfo.eCreateTime).getTime();
		var sum = (etime - stime) / (24 * 60 * 60 * 1000);
		if(sum > 7){
			$scope.notice("创建时间的跨度不能超过七天");
			return;
		}
		SweetAlert.swal({
			title: "确认导出？",
//		    text: "温馨提示：数据量越大需要时间越多，请耐心等待",
//		    type: "warning",
			showCancelButton: true,
			confirmButtonColor: "#DD6B55",
			confirmButtonText: "确定",
			cancelButtonText: "取消",
			closeOnConfirm: true,
			closeOnCancel: true 
		},
		function (isConfirm) {
			if (isConfirm) {
				location.href="repaySettleOrder/exportRepayTradeOrder.do?baseInfo="+encodeURI(angular.toJson($scope.baseInfo));
			}
		});
	}

	setBeginTime=function(setTime){
    	$scope.baseInfo.sCreateTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
    }

    setEndTime=function(setTime){
    	$scope.baseInfo.eCreateTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
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