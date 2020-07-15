/**
 * 风控问题新增
 */
angular.module('inspinia').controller('riskProblemAddCtrl',function($scope, $http, $state, $stateParams, i18nService,$filter) {

	i18nService.setCurrentLang('zh-cn');
	$scope.problemTypes=[{text:"调单",value:1},{text:"风控规则",value:2},{text:"其他",value:3}];
	$scope.dealPersons=[];
	$scope.info={}
	
	//处理人员
	$http.post('riskProblemAction/selectAllUserInfo')
	.success(function(data){
		if(!data)
			return
		$scope.dealPersons=data;
	})
	
	
	$scope.commit=function(){
		$scope.submitting= true;
		$http.post('riskProblemAction/addProblemInfo',"info="+angular.toJson($scope.info),
				{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		.success(function(data){
			if(data.bols){
				$scope.notice(data.msg);
				$state.transitionTo('risk.riskProblemMag',null,{reload:true});
				$scope.submitting = false;
			}else{
				$scope.notice(data.msg);
				$scope.submitting = false;
			}
		})
	}
	
});