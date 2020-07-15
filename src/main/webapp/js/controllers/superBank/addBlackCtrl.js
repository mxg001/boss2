/**
 * 新增黑名单
 */
angular.module('inspinia',['angularFileUpload']).controller("addBlackCtrl", function($scope, $http, $state, $stateParams,FileUploader) {
    //数据源
    $scope.typeList = [{text:"抢红包黑名单",value:"0"},{text:"购买领地黑名单",value:"1"},{text:"办理信用卡黑名单",value:"2"},{text:"提现黑名单",value:"3"}];
    $scope.baseInfo={};
    $scope.baseInfo={type:"0"};
    //提交
    $scope.submit = function(){
        if(!$scope.baseInfo.type){
                $scope.notice("黑名单类型不能为空");
                return;
        }
        $scope.submitting = true;
        $http({
            url: "superBank/addUserBlack",
            method: "POST",
            data: $scope.baseInfo
        }).success(function(result){
            $scope.submitting = false;
            $scope.notice(result.msg);
            if(result.status){
                $state.transitionTo('superBank.blacklist',null,{reload:true});
            }
        })
    }

  
});
