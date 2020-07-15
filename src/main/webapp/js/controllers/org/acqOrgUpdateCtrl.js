/**
 * 收单机构管理的修改
 */
angular.module('inspinia').controller('acqOrgUpdateCtrl',function($scope, $http, $state, $stateParams, i18nService,$filter) {

	i18nService.setCurrentLang('zh-cn');
	$scope.isBool=[{text:"否",value:1},{text:"是",value:2}];
//	$scope.rey=[{text:"代付",value:1}];
	$scope.acqOrgs=[];
	$scope.settleAccount=[];
	$scope.info={};
	
	//收单机构
	$http.post("acqOrgAction/selectBoxAllInfo")
 	 .success(function(msg){
 			//响应成功
 		 if(msg==null){
 			 return;
 		 }
 		 if(msg!=null && msg.length>0){
 			for(var i=0; i<msg.length; i++){
 		    	$scope.acqOrgs.push({value:msg[i].id,text:msg[i].acqName});
 		    }
 		 }
 	});
	
	//分润结算账户数据
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
	/**
	 * 结算方式选择“直清”，默认选择本收单机构的交易只能在本机构出款为“是”且则限制用户不能更改
	 */
	$scope.checkTransHaveOut = function(id){
		if(id == 2){
			$scope.info.acqTransHaveOut = 1;
		}
	}
	//查询
	$http.post('acqOrgAction/selectByParam',"ids="+angular.toJson($stateParams.id),
			 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
	.success(function(data){
		if(!data.bols){
			$scope.notice(data.msg);
		}else{
			$scope.info=data.result;
			if(data.result.acqTransHaveOut==1){
				$scope.info.acqTransHaveOut=true;
			}else{
				$scope.info.acqTransHaveOut=false;
			}
		}
	})
	$scope.commit=function(){
		$scope.submitting = true;
		if($scope.info.acqSuccessAmount==""||$scope.info.acqSuccessAmount==null ||$scope.info.dayAlteredTime==""||$scope.info.dayAlteredTime==null){
			$scope.notice("请填写完整的信息~~~~~~~~");
			$scope.submitting = false;
			return;
		}
		if($scope.info.acqTransHaveOut){
			$scope.info.acqTransHaveOut=1
		}else{
			$scope.info.acqTransHaveOut=2
		}
		$http.post('acqOrgAction/updateAcqOrgInfo',"info="+angular.toJson($scope.info),
				 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		.success(function(data){
			if(!data.bols){
				$scope.notice(data.msg);
				$scope.submitting = false;
			}else{
				$scope.notice(data.msg);
				$state.transitionTo('org.acqOrg',null,{reload:true});
				$scope.submitting = false;
			}
		})
	}
	
})