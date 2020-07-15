/**
 * 用户反馈问题详情
 */
angular.module('inspinia').controller("userFeedbackDetailCtrl", function($scope, $http, $state, $stateParams,$filter,i18nService) {

	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	$scope.userTypes=[{text:"代理商",value:1},{text:"商户",value:2}]
	
	//查询
	$http.post('userFeedbackProblemAction/selectDetail',
			"ids="+angular.toJson($stateParams.id),
			 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
	.success(function(data){
		if(!data.bols){
			return;
		}
		$scope.info=data.result;
		$scope.tup=data.tt;
	})

	//提交处理
	$scope.submit = function(){
		$http.post('userFeedbackProblemAction/dealResult',
			"info="+angular.toJson($scope.info),
			{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			.success(function(data){
				if(data.status){
					$state.transitionTo('sys.userFeedback',null,{reload:false});
					$scope.notice(data.msg);
				}
			})
	}

})