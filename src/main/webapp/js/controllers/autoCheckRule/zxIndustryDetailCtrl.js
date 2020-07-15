/**
 * 自选行业手续费修改
 */
angular.module('inspinia').controller('zxIndustryDetailCtrl',function($scope,$http,$stateParams){

    //自选行业手续费详情
    $http.get('couponActivity/zxIndustryDetail?activetiyCode='+$stateParams.activetiyCode)
        .success(function(result){
            if(result.status){
                $scope.baseInfo = result.data.baseInfo;
                $scope.list = result.data.config;
            } else {
                $scope.notice(result.msg);
            }
        });
});

