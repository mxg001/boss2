/**
 * 实名认证预警
 */

angular.module('inspinia').controller('verifiedWarningCtrl',function(i18nService,$scope,$http,$state,$stateParams,$compile,$filter,$log,$uibModal){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.authWarning={};
    $scope.hackWarning={};
    $scope.overtimeWarning={};
    $scope.toCheck={};
    $http.get('merchantBusinessProduct/selectVerifiedWarning')
        .success(function(data) {
            if(data.status){
                $scope.authWarning=data.authWarning;
                $scope.hackWarning=data.hackWarning;
                $scope.overtimeWarning=data.overtimeWarning;
                $scope.enterpriseWarning=data.enterpriseWarning;
                $scope.toCheck=data.toCheck;
            }else{
                $scope.notice(data.msg);
            }
        });

    $scope.updateVerifiedWarning=function(){
        var isNum=/^([1-9])\d{0,10}$/;
        if($scope.overtimeWarning.cycle==undefined||$scope.overtimeWarning.cycle==null){
            $scope.notice("BOSS系统至外放平台网络超时预警-周期不能为空!");
            return;
        }else{
            if($scope.overtimeWarning.cycle!=0&&!isNum.test($scope.overtimeWarning.cycle)){
                $scope.notice("BOSS系统至外放平台网络超时预警-周期格式不正确!");
                return;
            }
        }
        if($scope.overtimeWarning.num==undefined||$scope.overtimeWarning.num==null){
            $scope.notice("BOSS系统至外放平台网络超时预警-次数不能为空!");
            return;
        }else{
            if($scope.overtimeWarning.num!=0&&!isNum.test($scope.overtimeWarning.num)){
                $scope.notice("BOSS系统至外放平台网络超时预警-次数格式不正确!");
                return;
            }
        }

        if($scope.authWarning.cycle==undefined||$scope.authWarning.cycle==null){
            $scope.notice("鉴权认证失败预警-周期不能为空!");
            return;
        }else{
            if($scope.authWarning.cycle!=0&&!isNum.test($scope.authWarning.cycle)){
                $scope.notice("鉴权认证失败预警-周期格式不正确!");
                return;
            }
        }
        if($scope.authWarning.num==undefined||$scope.authWarning.num==null){
            $scope.notice("鉴权认证失败预警-次数不能为空!");
            return;
        }else{
            if($scope.authWarning.num!=0&&!isNum.test($scope.authWarning.num)){
                $scope.notice("鉴权认证失败预警-次数格式不正确!");
                return;
            }
        }

        if($scope.hackWarning.cycle==undefined||$scope.hackWarning.cycle==null){
            $scope.notice("活体认证-周期不能为空!");
            return;
        }else{
            if($scope.hackWarning.cycle!=0&&!isNum.test($scope.hackWarning.cycle)){
                $scope.notice("活体认证-周期格式不正确!");
                return;
            }
        }
        if($scope.hackWarning.num==undefined||$scope.hackWarning.num==null){
            $scope.notice("活体认证连续失败次数不能为空!");
            return;
        }else{
            if($scope.hackWarning.num!=0&&!isNum.test($scope.hackWarning.num)){
                $scope.notice("活体认证连续失败次数格式不正确!");
                return;
            }
        }

        if($scope.enterpriseWarning.num==undefined||$scope.enterpriseWarning.num==null){
            $scope.notice("企业对比-次数不能为空!");
            return;
        }else{
            if($scope.enterpriseWarning.num!=0&&!isNum.test($scope.enterpriseWarning.num)){
                $scope.notice("企业对比-次数格式不正确!");
                return;
            }
        }

        if($scope.overtimeWarning.phones==undefined||$scope.overtimeWarning.phones==null){
            $scope.notice("BOSS系统至外放平台网络超时预警-短信接收人不能为空!");
            return;
        }
        $scope.overtimeWarning.phones=$scope.overtimeWarning.phones.toLowerCase();

        if($scope.authWarning.phones==undefined||$scope.authWarning.phones==null){
            $scope.notice("鉴权认证失败预警-短信接收人不能为空!");
            return;
        }
        $scope.authWarning.phones=$scope.authWarning.phones.toLowerCase();

        if($scope.hackWarning.phones==undefined||$scope.hackWarning.phones==null){
            $scope.notice("活体认证-短信接收人不能为空!");
            return;
        }
        $scope.hackWarning.phones=$scope.hackWarning.phones.toLowerCase();

        if($scope.enterpriseWarning.phones==undefined||$scope.enterpriseWarning.phones==null){
            $scope.notice("企业对比-短信接收人不能为空!");
            return;
        }
        $scope.enterpriseWarning.phones=$scope.enterpriseWarning.phones.toLowerCase();
        if($scope.toCheck.num==undefined || $scope.toCheck.num==null){
            $scope.notice("审核预警-待审核商户预警数不能为空!");
            return;
        }
        $scope.toCheck.phones=$scope.toCheck.phones.toLowerCase();

        var data = {"overtimeWarning":$scope.overtimeWarning,"authWarning": $scope.authWarning, "hackWarning": $scope.hackWarning,
            "enterpriseWarning":$scope.enterpriseWarning,"toCheck":$scope.toCheck};
        $http.post("merchantBusinessProduct/updateVerifiedWarning", angular.toJson(data))
            .success(function (result) {
                if (result.status) {
                    $scope.notice("保存成功");
                    $state.transitionTo('merchant.auditMer',null,{reload:true});
                } else {
                    $scope.notice("保存失败");
                }

            });
    };
});