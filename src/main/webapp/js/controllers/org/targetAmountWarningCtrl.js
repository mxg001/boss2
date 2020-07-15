/**
 * 目标金额预警
 */

angular.module('inspinia').controller('targetAmountWarningCtrl',function(i18nService,$scope,$http,$state,$stateParams,$compile,$filter,$log,$uibModal){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.yesAmountWarning={};
    $scope.noAmountWarning={};
    $http.get('jumpRoute/selectTargetAmountWarning')
        .success(function(data) {
            if(data.status){
                $scope.yesAmountWarning=data.yesAmountWarning;
                $scope.noAmountWarning=data.noAmountWarning;
                $scope.yesAmountWarning.content=$scope.yesAmountWarning.content.replace("date","XX:XX")
                $scope.noAmountWarning.content=$scope.noAmountWarning.content.replace("date","XX:XX")
            }else{
                $scope.notice(data.msg);
            }
        });

    $scope.updateTargetAmountWarning=function(){
        if($scope.yesAmountWarning.phones==undefined||$scope.yesAmountWarning.phones==null){
            $scope.notice("已达到目标金额短信接收人不能为空!");
            return;
        }
        $scope.yesAmountWarning.phones=$scope.yesAmountWarning.phones.toLowerCase();
        if($scope.noAmountWarning.phones==undefined||$scope.noAmountWarning.phones==null){
            $scope.notice("未达到目标金额短信接收人不能为空!");
            return;
        }
        $scope.noAmountWarning.phones=$scope.noAmountWarning.phones.toLowerCase();
        var data = {"yesAmountWarning": $scope.yesAmountWarning, "noAmountWarning": $scope.noAmountWarning};
        $http.post("jumpRoute/updateTargetAmountWarning", angular.toJson(data))
            .success(function (result) {
                if (result.status) {
                    $scope.notice("保存成功");
                    $state.transitionTo('org.jumpRoute',null,{reload:true});
                } else {
                    $scope.notice("保存失败");
                }

            });
    };
});