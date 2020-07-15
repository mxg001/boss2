/**
 * 银行详情
 */
angular.module('inspinia').controller("insuranceProductDetailCtrl", function($scope, $http, $state, $stateParams) {
    //数据源
    $http({
        url:"insuranceProduct/detail/" + $stateParams.id,
        method:"GET"
    }).success(function(result){
        if(result && result.status){
            $scope.baseInfo = result.data;
        } else {
            $scope.notice(result.msg);
        }
    });

});
