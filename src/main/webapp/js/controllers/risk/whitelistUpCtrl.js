/**
 * 白名单修改
 */
angular.module('inspinia').controller('whitelistUpCtrl',function($scope, $http, $state, $stateParams, i18nService,$filter) {

	i18nService.setCurrentLang('zh-cn');
//	$scope.whitelistTypes=[{text:"商户身份证",value:2},{text:"卡号",value:3},{text:"商户编号",value:1}]
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
	
	$scope.commit=function(){
		$scope.submitting = true;
//		if($scope.info.rollName==""||$scope.info.rollName==null){
//			$scope.notice("请填写完整的信息！！！！");
//			$scope.submitting = false;
//			return;
//		}
		$http.post('riskRollAction/updateRollInfo',
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