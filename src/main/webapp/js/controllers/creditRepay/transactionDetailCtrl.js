/**
 * 交易详情
 */
angular.module('inspinia').controller('transactionDetailCtrl',function($scope,$http,$state,$stateParams,$compile,$filter,$log,$uibModal){

	//数据源
	$scope.orderNo = $stateParams.orderNo;
	$scope.info={};
	$scope.transTypeSelect = [{text:"快捷",value:'quickPay'}];
	$scope.transStatusSelect = [{text:"初始化",value:'0'},{text:"交易中",value:'1'},{text:"交易成功",value:'2'},{text:"交易失败",value:'3'},{text:"未知",value:'4'}];
	$scope.cardTypeSelect = [{text:"借记卡",value:'DEBIT'},{text:"信用卡",value:'CREDIT'},{text:"零钱",value:'CFT'}];

	$http.get('repaySettleOrder/selectTradeOrderDetail?orderNo='+$stateParams.orderNo)
		.success(function(data) {
			if(data.status){
				$scope.info = data.info;
			}else{
				$scope.notice(data.msg);
			}
		});

});