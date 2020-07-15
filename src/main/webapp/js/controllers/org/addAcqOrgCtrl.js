/**
 * 收单机构管理  新增
 */
angular.module('inspinia').controller('addAcqOrgCtrl',function($scope,$http,$state,$stateParams,$compile,$filter,i18nService){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	
//	$scope.settleTypes = [{text:"代付",value:1}];
//	$scope.info={settleType:1}
	
	$scope.acqTransHaveOut=[{text:"是",value:1},{text:"否",value:0}];
	
	$scope.info = {};
	$scope.commit=function(){
		$scope.submitting = true;
		$http.post("acqOrgAction/addAcqOrg",angular.toJson($scope.info))
		.success(function(data){
			if(data.bols){
				$scope.notice(data.msg);
				$state.go('org.acqOrg');
			}else{
				$scope.notice(data.msg);
				$scope.submitting = false;
			}
		});
	}
	/**
	 * 结算方式选择“直清”，默认选择本收单机构的交易只能在本机构出款为“是”且则限制用户不能更改
	 */
	$scope.checkTransHaveOut = function(id){
		if(id == 2){
			$scope.info.acqTransHaveOut = 1;
		}
	}
	//分润结算账户数据
	$scope.settleAccount=[];
	$http.post("acqOrgAction/selectAccountDate")
	.success(function(msg){
 		 if(msg.accountData==null){
 			 return;
 		 }
 		if(msg.accountData.data!=null && msg.accountData.data.length>0){
 			angular.forEach(msg.accountData.data,function(data,index){
 				$scope.settleAccount.push({value:data.id,text:data.bankName+"-"+data.accountName+"-"+data.accountNo});
 			});
 		}
	});
})