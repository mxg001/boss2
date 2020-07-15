/**
 * 黑名单详情
 */
angular.module('inspinia').controller('blacklistDetailCtrl',function($scope, $http, $state, $stateParams, i18nService,$filter) {

	i18nService.setCurrentLang('zh-cn');
//	$scope.blacklistTypes=[{text:"商户身份证",value:2},{text:"卡号",value:3},{text:"商户编号",value:1}]
	$scope.info={};
	
	//查询
	$http.post('riskRollAction/selectRollDetail',
			"ids="+angular.toJson($stateParams.id),
			 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
	.success(function(data){
		if(!data.bols){
			$scope.notice(data.msg);
			return;
		}
		$scope.info=data.result;
	})
})