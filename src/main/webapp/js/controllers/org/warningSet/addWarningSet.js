/**
 * 新增预警阀值设置
 */
angular.module('inspinia').controller('addWarningSetCtrl',function($scope, $http, $state) {

    $scope.warnStatus = [{text:"打开",value:1},{text:"关闭",value:0}];
    $scope.warnTimeTypes = [{text:"全天",value:1},{text:"个性化",value:2}];

    $scope.baseInfo = {};
    $scope.resetForm = function(){
        $scope.baseInfo.serviceId = null;
        $scope.baseInfo.acqId = "";
        $scope.baseInfo.serviceName = null;
        $scope.baseInfo.serviceType = null;
        $scope.baseInfo.warnTimeType = 1;
        $scope.baseInfo.warnStartTime = null;
        $scope.baseInfo.warnEndTime = null;
        $scope.baseInfo.warnStatus = 1;
        // $scope.baseInfo.exceptionNumber = null;
    }
    $scope.resetForm();

    $scope.acqOrgs=[{value:null,text:""}];
    //收单机构
    $http.post("acqOrgAction/selectBoxAllInfo")
        .success(function(msg){
            //响应成功
            for(var i=0; i<msg.length; i++){
                $scope.acqOrgs.push({value:msg[i].id,text:msg[i].acqName});
            }
        });

    $http.get("warningSet/getTransCycle")
        .success(function(result){
            if(result.status){
                $scope.baseInfo.waringCycle = result.data.cycle;
                $scope.baseInfo.exceptionNumber = result.data.exceptionNumber;
            }else {
                $scope.notice(result.msg);
            }
        });

    $scope.getServiceInfo = function(){
        if($scope.baseInfo.serviceId == null || $scope.baseInfo.serviceId == ''){
            return;
        }
        $http({
            url: 'warningSet/getServiceInfo?serviceId=' + $scope.baseInfo.serviceId,
            method:'get'
        }).success(function (result) {
            if (result.status){
                $scope.baseInfo.acqId = result.data.acqId;
                $scope.baseInfo.serviceName = result.data.serviceName;
                $scope.baseInfo.serviceType = parseInt(result.data.serviceType);

                return;
            } else {
                $scope.notice(result.msg);
            }
        }).error(function () {
            $scope.submitting = false;
            $scope.notice('服务器异常,请稍后再试.');
        });
    }


    $scope.submit = function(){
        $scope.submitting = true;
        $http.post('warningSet/updateWaringInfo', "info=" + angular.toJson($scope.baseInfo),
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function (result) {
            $scope.submitting = false;
            $scope.notice(result.msg);
            if (result.status){
                $state.transitionTo('org.addWarningSet',null,{reload:true});
                // $scope.resetForm();
                return;
            }
        }).error(function () {
            $scope.submitting = false;
            $scope.notice('服务器异常,请稍后再试.');
        });
    }

});