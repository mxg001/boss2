/**
 * 超级银行家组织新增
 */
angular.module('inspinia',['angularFileUpload']).controller('redConfigDetailCtrl',function($scope, $http,$stateParams, $state, FileUploader){
    $scope.baseInfo = {"allow_org_profit":"1"};
    $scope.baseInfo.id = $stateParams.id;
    //组织列表
    $scope.orgs=[];

    $scope.getAllOrg = function(){
        $http({
            url:"superBank/getOrgInfoList",
            method:"POST"
        }).success(function(msg){
            if(msg.status){
                $scope.orgs = msg.data;
                $scope.orgs.unshift({orgId:0,orgName:"平台"});
            }
        }).error(function(){
            $scope.notice("获取组织列表异常");
        });
    };
    $scope.getAllOrg();

    $scope.redConfiInfo = function(){
        $http({
            url:"red/editRedConfig",
            method:"POST",
            data : $scope.baseInfo,
        }).success(function(msg){
            if(msg.status){
                $scope.baseInfo = msg.data;
                $scope.changeBusType();
            }
        }).error(function(){
            $scope.notice("获取红包配置信息异常");
        });
    };
    $scope.redConfiInfo();

    $scope.is_alow =false;
    $scope.changeBusType = function () {
        if($scope.baseInfo.bus_type === '2' ||  $scope.baseInfo.bus_type === '3'){
            $scope.is_alow = true;
        }else {
            $scope.is_alow  =   false
        }
    }

});