/**
 * 代理商新增
 */
angular.module('inspinia').controller('addAgentInvitePrizesCtrl',function($scope,$http,$state,$stateParams,$compile,$filter,i18nService){
	
	$scope.info = {};
	$scope.getAgent = function(){
		$http.get("agentFunctionManager/findAgentInfoByAgentNo.do?agentNo="+$scope.info.agentNo)
		.success(function(data){
			if(!data){
				$scope.notice('代理商不存在');
			}else{
				//设置回显
				$scope.info.agentName = data.agentName;
			}
			
		});
	}
	
	$scope.commit=function(){
		$scope.submitting = true;
//		$http.post("agentFunctionManager/addAgentFunctionManager",angular.toJson($scope.info))
		$http.post("agentFunctionManager/addAgentFunctionManager",angular.toJson({"info":$scope.info,"functionNumber":$stateParams.functionNumber}))
		.success(function(data){
			if(data.bols){
				$scope.notice(data.msg);
				$state.go('func.switchSet',{"functionNumber":$scope.functionNumber});
			}else{
				$scope.notice(data.msg);
				$scope.submitting = false;
			}
		});
	}
})