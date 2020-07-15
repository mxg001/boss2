/**
 * 保险公司详情
 */
angular.module('inspinia').controller('insuranceCompanyDetailCtrl',function($scope, $http, $state,$stateParams){
    $http({
        url:"insuranceCompany/detail/" + $stateParams.id,
        method:"GET"
    }).success(function(result){
        if(result && result.status){
            $scope.baseInfo = result.data;
        } else {
            $scope.notice(result.msg);
        }
    });


});