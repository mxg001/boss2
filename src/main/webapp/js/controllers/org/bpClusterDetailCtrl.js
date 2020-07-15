/**
 * 业务产品默认路由集群详情
 */
angular.module('inspinia').controller('bpClusterDetailCtrl',function($scope, $http, $state, $stateParams, i18nService,$filter) {

	i18nService.setCurrentLang('zh-cn');
	$scope.status = [{text:"是",value:1},{text:"否",value:2}];
	$scope.info={};
	
	//查询
	$http.post('defTransRouteGroupAction/selectByParam',
			"ids="+angular.toJson($stateParams.id),
			 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
	.success(function(data){
		if(!data.bols){
			$scope.notice(data.msg);
		}else{
			$scope.info=data.result;
		}
		
	})
});