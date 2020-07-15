/**
 * 信用卡管家-订单详情
 */
angular.module('inspinia').controller('orderDetailCtrl',function($scope, $http, $state,$stateParams){

	$scope.transTypeSelect = [{text:"支付宝",value:'alipay'},{text:"微信",value:'weixin'}];
	$scope.transStatusSelect = [{text:"待付款",value:'WAIT'},{text:"已付款",value:'SUCCESS'},{text:"已关闭",value:'CLOSED'}];

	$scope.info={};

	$http({
        url:"cmOrder/queryOrderInfoById?tradeNo="+$stateParams.tradeNo,
        method:"GET"
    }).success(function(data){
        if (data.status){
            $scope.info = data.info;
        } else {
        	$scope.notice(data.msg);
        }
    }).error(function () {
        $scope.notice("服务器异常，请稍后再试");
    });

});