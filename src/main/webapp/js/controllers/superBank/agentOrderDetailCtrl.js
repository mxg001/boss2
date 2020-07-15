/**
 * 超级银行家代理授权订单详情
 */
angular.module('inspinia').controller('agentOrderDetailCtrl',function($scope, $http, $state,$stateParams){
    //获取代理授权订单详情
    $http({
        url:"superBank/orderDetail?orderNo=" + $stateParams.orderNo,
        method:"GET"
    }).success(function(result){
        if (result.status){
            $scope.baseInfo = result.data;
        } else {
           $scope.notice(result.msg);
        }
    }).error(function () {
        $scope.notice("服务器异常，请稍后再试");
    })


});