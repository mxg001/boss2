/**
 * 风控问题反馈
 */
angular.module('inspinia').controller('riskFeedbackCtrl',function($scope, $http, $state, $stateParams, i18nService,$filter) {

	i18nService.setCurrentLang('zh-cn');
	$scope.problemTypes=[{text:"调单",value:1},{text:"风控规则",value:2},{text:"其他",value:3}];
	$scope.info={};
	
	//查询
	$http.post('riskProblemAction/selectFeedbackInfo',
			"ids="+angular.toJson($stateParams.id),
			 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
	.success(function(data){
		if(!data.bols){
			$scope.notice(data.msg);
			return;
		}
		$scope.info=data.result;
	})
	
	//提交反馈措施
	$scope.commit=function(){
		$scope.submitting = true;
		if($scope.info.dealMeasures==null||$scope.info.dealMeasures==""){
			$scope.notice("请填写完整信息");
			$scope.submitting = false;
			return;
		}
		$http.post('riskProblemAction/updateFeedback',
			"info="+angular.toJson($scope.info),
			 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		.success(function(data){
			if(!data.bols){
				$scope.notice(data.msg);
				$scope.submitting = false;
			}else{
				$scope.notice(data.msg);
				$scope.submitting = false;
				$state.transitionTo('risk.riskProblemMag',null,{reload:true});
			}
		})
	}
})