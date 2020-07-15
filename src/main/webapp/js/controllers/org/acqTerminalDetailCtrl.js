/**
 * 收单机构终端详情
 */
angular.module('inspinia').controller('acqTerminalDetailCtrl',function($scope, $http, $state, $stateParams, i18nService,$filter) {

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
	
})