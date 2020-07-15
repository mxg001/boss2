/**
 * 收单机构终端修改
 */
angular.module('inspinia').controller('acqTerminalUpCtrl',function($scope, $http, $state, $stateParams, i18nService,$filter) {

	i18nService.setCurrentLang('zh-cn');
	$scope.lockeds=[{text:"正常",value:0},{text:"锁定",value:1},{text:"废弃",value:2}];
	$scope.info={};
	
	//查询
	$http.post('acqTerminalAction/selectByParam',
			"ids="+angular.toJson($stateParams.id),
			 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
	.success(function(data){
		if(!data.bols)
			return;
		$scope.info=data.result;
	})
	
	//修改提交
	$scope.commit=function(){
		var data={"id":$stateParams.id,"locked":$scope.info.locked};
		$http.post('acqTerminalAction/updateLockedById',
				"info="+angular.toJson(data),{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		.success(function(data){
			if(!data.bols){
				$scope.notice("修改失败！");
			}else{
				$scope.notice("修改成功！");
				$state.transitionTo('org.orgTerminal',null,{reload:true});
			}
		})
		
	}
	
})