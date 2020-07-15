/**
 * 代理商新增
 */
angular.module('inspinia').controller('addHappyReturnAgentActivityCtrl',function($scope,$http,$state,$stateParams,$compile,$filter,i18nService){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	
	$scope.activityId = $stateParams.activityId;
	
	$scope.info = {};
	$scope.getAgent = function(){
		$http.get("agentFunctionManager/findAgentInfoByAgentNo.do?agentNo="+$scope.info.agentNo)
		.success(function(data){
			if(!data){
				$scope.notice('代理商不存在');
			}else{
				if(data.agentLevel==1){
                    //设置回显
                    $scope.info.agentName = data.agentName;
				} else {
                    $scope.notice('代理商不是一级代理商');
				}
			}
			
		});
	}
	
	$scope.commit=function(){
		$scope.submitting = true;
        $scope.info.activityId=$scope.activityId;
		$http.post("activity/addHappyReturnAgentActivity",$scope.info)
		.success(function(data){
			if(data.status){
				$scope.notice(data.msg);
				$state.go('activity.selectHappyReturnAgentActivity',{"activityId":$scope.activityId});
			}else{
				$scope.notice(data.msg);
				$scope.submitting = false;
			}
		});
	}
})