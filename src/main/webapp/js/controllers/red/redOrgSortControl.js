/**
 *
 */
angular.module('inspinia',['']).controller('redOrgSortControl',function(i18nService,$scope, $http, $state,$stateParams ){
	i18nService.setCurrentLang('zh-cn'); // 设置语言为中文
	$scope.baseInfo = {	"id":$stateParams.id,
    					"busCode":$stateParams.busCode,
    					"busType":$stateParams.busType,
    					"sortNum":$stateParams.sortNum,
    					"orgId":$stateParams.orgId != "" ?parseInt($stateParams.orgId):$stateParams.orgId,
    					"category":$stateParams.category,
    					"orgStatus":$stateParams.orgStatus,
    					"flag":$stateParams.flag};
    $scope.flagText = $scope.baseInfo.flag == "add"?"新增":"修改";
    
    $scope.providerSysKey = "REDBUSINESSCATEGORY";// 红包业务分类系统字典表对应的KEY
    
    $scope.getSysDict = function(){
   	 $http({
            url:"sysOption/selectSysOption",
            data:{optionGroupCode:$scope.providerSysKey},
            method:"POST"
        }).success(function(data){
       	 $scope.sysDictList = data.data;
        }).error(function(){
            $scope.notice("获取红包业务类别异常!");
        });
    };
    $scope.getSysDict();
    
    // 获取组织列表
    $scope.orgs=[{orgId:-1,orgName:"默认"}];
    $scope.getAllOrg = function(){
    	var data = {};
    	var httpUrl = "superBank/getOrgInfoList";
    	if($scope.baseInfo.orgStatus == 1){
    		httpUrl = "red/redOrgListAll";
    		data = {orgId : "-1",
    				busCode : $scope.baseInfo.busCode,
    				busType : $scope.baseInfo.busType};
    	}
    	
        $http({
            url:httpUrl,
            method:"POST",
            data : data
        }).success(function(msg){
            if(msg.status){
                $scope.orgs.push.apply($scope.orgs,msg.data);
            }
        }).error(function(){
            $scope.notice("获取组织列表异常");
        });
    };
    $scope.getAllOrg();


    $scope.confTypeName = "";
    $scope.typeName = function(){
        for (var s in $scope.redBusTypes){
            if($scope.redBusTypes[s].value === $scope.baseInfo.busType){
                $scope.confTypeName = $scope.redBusTypes[s].text;
                return;
            }
        }
    }
    $scope.typeName();
    

    $scope.submit = function(){
    	var isNum=/^-?\d{1,3}$/;
    	if($scope.baseInfo.orgId == null || $scope.baseInfo.orgId == "" || $("#orgId").find("option:selected").text() == ""){
    		$scope.notice("请选择对应组织!");
    		return;
    	}else if($scope.baseInfo.category == null || $scope.baseInfo.category == ""){
    		$scope.notice("请选择分类模块!");
    		return;
    	}else if($scope.baseInfo.sortNum == null || $scope.baseInfo.sortNum == ""){
    		$scope.notice("排列顺序不能为空!");
    		return;
    	}else if(!isNum.test($scope.baseInfo.sortNum)){
    		$scope.notice("排列顺序只能填写3位以内整数!");
    		return;
    	}

    	var url = "red/editRedOrgSort";
    	if($scope.baseInfo.flag == "add"){
    		url = "red/addRedOrgSort";
    	}
        $http({
            url:url,
            data:$scope.baseInfo,
            method : "post"
        }).success(function (result) {
            $scope.notice(result.msg);
            if(result.status){
                $state.transitionTo('red.redOrgSortCtrl',$scope.baseInfo,{reload:true});
            }
        }).error(function () {
            $scope.notice("操作异常,请联系管理员!");
        })
    }

});