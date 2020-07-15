/**
 * 收单机构管理的详情
 */
angular.module('inspinia').controller('acqOrgDetailCtrl',function($scope, $http, $state, $stateParams, i18nService,$filter) {

	i18nService.setCurrentLang('zh-cn');
	
	$scope.isBool=[{text:"否",value:1},{text:"是",value:2}]
	
	$scope.info={};
	$scope.settleAccount = [];
	$http.post('acqOrgAction/selectByParam',"ids="+angular.toJson($stateParams.id),
			 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
	.success(function(data){
		if(!data.bols){
			
		}else{
			$scope.info=data.result;
			if(data.result.acqTransHaveOut==1){
				$scope.info.acqTransHaveOut=true;
			}
			if(data.accountData==null){
	 			 return;
	 		 }
	 		if(data.accountData.data!=null && data.accountData.data.length>0){
	 			angular.forEach(data.accountData.data,function(data,index){
	 				$scope.settleAccount.push({value:data.id,text:data.bankName+"-"+data.accountName+"-"+data.accountNo});
	 			});
	 		}
		}
		
	})
})