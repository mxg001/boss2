/**
 * 超级银行家保险订单详情
 */
angular.module('inspinia').controller('insuranceOrderDetailCtrl',function($scope, $http, $state,$stateParams){
    $http({
        url:"insuranceOrder/insuranceOrderDetail?orderNo=" + $stateParams.orderNo,
        method:"GET"
    }).success(function(result){
        if (result.status){
            $scope.baseInfo = result.data;
            if (result.data.orgBonusConf != null) {
            	$scope.baseInfo.orgBonusConf = result.data.orgBonusConf + "%";
			}
        } else {
           $scope.notice(result.msg);
        }
    }).error(function () {
        $scope.notice("服务器异常，请稍后再试");
    })


});