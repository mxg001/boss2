/**
 * 商户黑名单新增
 */
angular.module('inspinia').controller('addMerFunctionManagerCtrl',function($scope,$http,$state,$stateParams,$compile,$filter,i18nService){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	
	$scope.functionNumber = $stateParams.functionNumber;
	
	$scope.info = {functionNumber:$scope.functionNumber};
	$scope.getMer = function(){
		$http.get("merFunctionManager/findMerInfoByMerNo.do?merNo="+$scope.info.merchantNo)
		.success(function(data){
			if(!data){
				$scope.notice('商户不存在');
                $scope.info.merchantName = "";
			}else{
				//设置回显
				$scope.info.merchantName = data.merchantName;
			}
			
		});
	}
	
	$scope.commit=function(){
		//$scope.submitting = true;
		$http.post("merFunctionManager/addMerFunctionManager",angular.toJson({"info":$scope.info}))
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