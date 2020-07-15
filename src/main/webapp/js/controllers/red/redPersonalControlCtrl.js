/**
 * 个人发红包
 */
angular.module('inspinia').controller('redPersonalControlCtrl',function($scope, $http, $state){
    $http({
        url:"red/getRedUserConf",
        method:"POST"
    }).success(function(result){
        if(result.status){
            $scope.baseInfo = result.data;
        } else {
            $scope.notice(result.msg);
        }
    }).error(function(){
        $scope.notice("获取红包配置信息异常");
    });

    $scope.submit = function () {
        $scope.submitting = true;
        $http({
            url:"red/updateRedUserConf",
            method:"POST",
            data: $scope.baseInfo
        }).success(function(result){
            $scope.notice(result.msg);
           if(result.status){
               $state.transitionTo('red.redControl',null,{reload:true});
           }
        });
    }

});