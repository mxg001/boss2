/**
 * 黑名单修改
 */
angular.module('inspinia').controller('blacklistUpCtrl',function($scope, $http, $state, $stateParams, i18nService,$filter) {

	i18nService.setCurrentLang('zh-cn');
//	$scope.blacklistTypes=[{text:"商户身份证",value:2},{text:"卡号",value:3},{text:"商户编号",value:1}]
	$scope.info={};
	$scope.name = "商户编号";
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
		var _rollType = $scope.info.rollType;
		if (_rollType == 1 || _rollType == 4) {
			$scope.name = "商户编号";
		} else if (_rollType == 2) {
			$scope.name = "身份证号";
		} else if (_rollType == 3) {
			$scope.name = "银行卡号";
		}
	})

	//查询代理商提示语
	$scope.queryAgentTips = function(){
		$http.post('msg/queryAgentTips',null,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			.success(function(result){
				if(result.status){
					$scope.agentTips = result.data;
					$scope.agentTips.unshift({msgCode:"",userMsg:"请选择"});
				}else{
					$scope.notice(result.msg);
				}
			}).error(function(){
		});
	}

	$scope.queryAgentTips();

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
				$scope.submitting = false;
				$state.transitionTo('risk.blacklistQuery',null,{reload:true});
			}
		})
	}
})