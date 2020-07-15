/**
 * 代理商提现限制
 */
angular.module('inspinia',['uiSwitch']).controller("agentWithdrawCtrl", function($scope, $http){

    $scope.baseInfo = {};

    $scope.query = function(){
        $http({
            url:"outAccountService/agentWithdraw",
            method:"get"
        }).success(function(result){
            if(result.status){
                $scope.baseInfo = result.data;
            } else {
                $scope.notice(result.msg);
            }
        });
    }
    $scope.query();


    $scope.submit = function(){
        $http({
            url:"outAccountService/saveAgentWithdraw",
            method:"post",
            data:$scope.baseInfo
        }).success(function(result){
            $scope.notice(result.msg);
        });
    }

    $scope.reset = function(){
        $scope.query();
    }
});

