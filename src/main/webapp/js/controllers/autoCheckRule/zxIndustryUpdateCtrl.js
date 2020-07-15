/**
 * 自选行业手续费修改
 */
angular.module('inspinia',['uiSwitch']).controller('zxIndustryUpdateCtrl',function($scope,$http,$stateParams,$state){

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

    $scope.rateTypes=[{"text":"元","value":1},{"text":"%","value":2}];
    $scope.submit = function(){
        $scope.submitting = true;
        if($scope.baseInfo.statusInt == true || $scope.baseInfo.statusInt == 1 ){
            $scope.baseInfo.statusInt = 1;
        } else {
            $scope.baseInfo.statusInt = 0;
        }
        $http({
            method:'post',
            url:'couponActivity/zxIndustryUpdate',
            data:{"baseInfo":$scope.baseInfo,"list":$scope.list}
        }).success(function(result){
            $scope.notice(result.msg);
            $scope.submitting = false;
            if(result.status){
                $state.transitionTo('func.couponActivity',null,{reload:true});
            }
        });
    }

});

