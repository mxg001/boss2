/**
 *
 */
angular.module('inspinia',['']).controller('redProductControlCtrl',function($scope, $http, $state,$stateParams ){
    $scope.baseInfo = {"allow_org_profit":"1","conf_type":$stateParams.busType};

    //组织列表
    $scope.orgs=[];
    $scope.getAllOrg = function(){
        $http({
            url:"superBank/getOrgInfoList",
            method:"POST"
        }).success(function(msg){
            if(msg.status){
                $scope.orgs = msg.data;
            }
        }).error(function(){
            $scope.notice("获取组织列表异常");
        });
    };
    $scope.getAllOrg();

    $scope.redProducInfo = function () {
        $http({
            url:"red/redProductInfo",	
            data:$scope.baseInfo,
            method : "post"
        }).success(function (msg) {
            $scope.baseInfo = msg.data;
        }).error(function () {
            $scope.notice("查询异常")
        })
    }

    $scope.redProducInfo();
    $scope.confTypeName = "";
    $scope.typeName = function(){
    	
        for (var s in $scope.redBusTypes){
            if($scope.redBusTypes[s].value === $scope.baseInfo.conf_type){
                $scope.confTypeName = $scope.redBusTypes[s].text;
                return;
            }
        }

    }
    
    $scope.typeName()
    // $scope.redConfiInfo = function(){
    //     $http({
    //         url:"red/editRedConfig",
    //         method:"POST"
    //     }).success(function(msg){
    //         if(msg.status){
    //             $scope.orgs = msg.data;
    //         }
    //     }).error(function(){
    //         $scope.notice("获取红包配置信息异常");
    //     });
    // };
    $scope.getAllOrg();
    $scope.submit = function(){
    	var isNum=/^(([0-9][0-9]*)|(([0]\.\d{1,5}|[1-9][0-9]*\.\d{1,5})))$/;
    	
    	   if ($stateParams.busType==4) {
    		   if($scope.baseInfo.red_first_user==null || $scope.baseInfo.red_first_user==""){
                   $scope.notice("用户占比不能为空!");
                   return;
               }else{
                   if(!isNum.test($scope.baseInfo.red_first_user)){
                       $scope.notice("用户占比格式不正确!");
                       return;
                   }
             }
             
             if($scope.baseInfo.red_first_money==null || $scope.baseInfo.red_first_money==""){
                 $scope.notice("奖金占比不能为空!");
                 return;
             }else{
                 if(!isNum.test($scope.baseInfo.red_first_money)){
                     $scope.notice("奖金占比格式不正确!");
                     return;
                 }
           }
		}
        $http({
            url:"red/redProductInfoUpdate",
            data:$scope.baseInfo,
            method : "post"
        }).success(function (msg) {
            $scope.baseInfo = msg.data;
            $scope.notice("修改成功");

            $state.transitionTo('red.redControl',null,{reload:true});
        }).error(function () {
            $scope.notice("查询异常")
        })


    }

});