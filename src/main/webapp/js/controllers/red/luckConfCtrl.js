/**
 * 红包幸运值配置
 */
angular.module('inspinia').controller('luckConfCtrl',function($scope, $http,$stateParams, $state) {
    //获取配置信息
    $scope.getInfo = function () {
        $http({
            url: "red/getLuckConf",
            method: "GET"
        }).success(function (result) {
            if (result.status) {
                $scope.baseInfo = result.data;
            } else {
                $scope.notice(result.msg);
            }
        });
    };
    $scope.getInfo();

    //提交配置信息
    $scope.submit = function () {
        $scope.submitting = true;
        $http({
            url: "red/updateLuckConf",
            method: "POST",
            data: $scope.baseInfo
        }).success(function (result) {
            $scope.submitting = false;
            $scope.notice(result.msg);
            if(result.status){
                $scope.getInfo();
            }
        });
    };
});