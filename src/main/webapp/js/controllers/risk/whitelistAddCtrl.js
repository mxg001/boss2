/**
 * 白名单新增
 */
angular.module('inspinia').controller('whitelistAddCtrl',function($scope, $http, $state, $stateParams, i18nService,$filter) {

	i18nService.setCurrentLang('zh-cn');
	$scope.info={rollType:5,rollBelong:1}
	
	$scope.commit=function(){
		$scope.submitting = true;
		if($scope.info.rollNo==""||$scope.info.rollNo==null){
			$scope.notice("请填写完整的信息");
			$scope.submitting = false;
			return;
		}
		$http.post('riskRollAction/addRollInfo',
				"info="+angular.toJson($scope.info),
				 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		.success(function(data){
			if(!data.bols){
				$scope.notice(data.msg);
				$scope.submitting = false;
			}else{
				$scope.notice(data.msg);
				$state.transitionTo('risk.whitelistMag',null,{reload:true});
				$scope.submitting = false;
			}
		})
		
	}
})