/**
 * 代理商新增
 */
angular.module('inspinia').controller('addAgentFunctionManagerCtrl',function($scope,$http,$state,$stateParams,$compile,$filter,i18nService){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	
	$scope.functionNumber = $stateParams.functionNumber;
	$scope.blacklist = $stateParams.blacklist;
	
	
	$scope.containsLowerJson=[{text:"包含",value:1},{text:"不包含",value:0}];
	
	$scope.info = {containsLower:1,blacklist:$scope.blacklist,functionNumber:$scope.functionNumber};
	$scope.getAgent = function(){
		$http.get("agentFunctionManager/findAgentInfoByAgentNo.do?blacklist="+$scope.blacklist+"&agentNo="+$scope.info.agentNo)
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
		//$scope.submitting = true;
//		$http.post("agentFunctionManager/addAgentFunctionManager",angular.toJson($scope.info))
		$http.post("agentFunctionManager/addAgentFunctionManager",angular.toJson({"info":$scope.info}))
		.success(function(data){
			if(data.bols){
				$scope.notice(data.msg);
				//$state.go('func.switchSet',{"functionNumber":$scope.functionNumber,"blacklist":$scope.blacklist});
			}else{
				$scope.notice(data.msg);
				$scope.submitting = false;
			}
		});
	}
})