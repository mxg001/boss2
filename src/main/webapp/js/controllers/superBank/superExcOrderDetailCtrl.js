/**
 * 超级银行家保险订单详情
 */
angular.module('inspinia').controller('superExcOrderDetailCtrl',function($scope, $http, $state,$stateParams){
    $http({
        url:"superExcOrder/superExcOrderDetail?orderNo=" + $stateParams.orderNo,
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