angular.module('inspinia').controller('faceRecognitionCtrl',function($scope, $http, $state, $stateParams, i18nService,$filter) {

	i18nService.setCurrentLang('zh-cn');
	$scope.baseInfo={};
	//查询
	$http.get('riskRulesAction/faceRecognition')
	.success(function(data){
		if(!data.bols){
			$scope.notice(data.msg);
			return;
		}
		$scope.baseInfo.faceJs=data.faceJs;
		$scope.baseInfo.faceXs=data.faceXs;
	})

	$scope.commit=function(){
		$scope.submitting = true;
		$http.post('riskRulesAction/updateFaceRecognition',
			"baseInfo="+angular.toJson($scope.baseInfo),
			 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		.success(function(data){
			if(!data.bols){
				$scope.notice(data.msg);
				$scope.submitting = false;
			}else{
				$scope.notice(data.msg);
				$scope.submitting = false;
			}
		})
		
	}
})