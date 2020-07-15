/**
 * 收单机构商户详情
 */
angular.module('inspinia').controller('acqMerchantDetailCtrl',function($scope, $http, $state, $stateParams, i18nService,$filter) {

	i18nService.setCurrentLang('zh-cn');
	$scope.info={};

	$scope.sourceList = angular.copy($scope.acqMerSourceList);
	$scope.sourceList.unshift({text:"",value:null});

	//查询
	$http.post('acqMerchantAction/selectByParam',
			"ids="+angular.toJson($stateParams.id),
			 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
	.success(function(data){
		if(!data.bols)
			return;
		$scope.info=data.result;
		for(var i in $scope.acqMerchantTypes){
			if($scope.info.acqMerchantType==$scope.acqMerchantTypes[i].value){
				$scope.info.acqMerchantType=$scope.acqMerchantTypes[i].text;
				break;
			}
		}
	})
})