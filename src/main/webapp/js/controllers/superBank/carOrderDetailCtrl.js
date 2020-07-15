/**
 * 超级银行家贷款订单详情
 */
angular.module('inspinia').controller('carOrderDetailCtrl',function($scope, $http, $state,$stateParams){
    $http({
        url:"superBank/carOrderDetail?orderNo=" + $stateParams.orderNo,
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