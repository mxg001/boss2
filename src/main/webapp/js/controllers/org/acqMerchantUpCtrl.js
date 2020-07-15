/**
 * 收单机构商户修改
 */
angular.module('inspinia').controller('acqMerchantUpCtrl',function($scope, $http, $state, $stateParams, i18nService,$filter) {

	i18nService.setCurrentLang('zh-cn');
	
	$scope.agents=[];
	$scope.acqOrgs=[];
	$scope.acqServices=[];
	$scope.serviceTypes=[];
    $scope.specialSelect = [{text:"是",value:'1'},{text:"否",value:'0'}];
	$scope.info={};

	$scope.sourceList = angular.copy($scope.acqMerSourceList);
	$scope.sourceList.unshift({text:"",value:null});

	//代理商
//	 $http.post("agentInfo/selectAllInfo.do")
// 	 .success(function(msg){
// 			//响应成功
// 	    	for(var i=0; i<msg.length; i++){
// 	    		$scope.agents.push({value:msg[i].agentNo,text:msg[i].agentName});
// 	    	}
// 	});
	
	//收单机构
	 $http.post("acqOrgAction/selectBoxAllInfo")
 	 .success(function(msg){
 			//响应成功
 	    	for(var i=0; i<msg.length; i++){
 	    		$scope.acqOrgs.push({value:msg[i].id,text:msg[i].acqName});
 	    	}
 	});
	 
	 $scope.aceId=function(id){
			//收单机构服务
		 	$scope.acqServices=[];
			$http.post("acqServiceAction/selectBox","ids="+angular.toJson(id),{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		 	 .success(function(msg){
		 			//响应成功
		 		if(msg.length>0){
	 		 		for(var i=0; i<msg.length; i++){
			    		$scope.acqServices.push({value:msg[i].id,text:msg[i].serviceName});
			    	}
	 		 	}
		 	});
		 }
	 
	//查询
	$http.post('acqMerchantAction/selectByParam',
			"ids="+angular.toJson($stateParams.id),
			 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
	.success(function(data){
		if(!data.bols)
			return;
		/*for(var i=0; i<data.list.length; i++){
 	    	if(data.list[i]=="1")
 	    		$scope.serviceTypes.push({value:data.list[i],text:"POS刷卡"});
 	    	if(data.list[i]=="2")
 	    		$scope.serviceTypes.push({value:data.list[i],text:"扫码支付"});
 	    	if(data.list[i]=="3")
 	    		$scope.serviceTypes.push({value:data.list[i],text:"快捷支付"});
 	    	if(data.list[i]=="4")
 	    		$scope.serviceTypes.push({value:data.list[i],text:"账户提现"});
 	    }*/
		$scope.info=data.result;
		$scope.info.oldMerchantNo = $scope.info.merchantNo;
		$scope.info.oldAcqMerchantNo = $scope.info.acqMerchantNo;
		$scope.aceId(data.result.acqOrgId);
	})
	
	
	//根据商户编号去查询一级代理商
	$scope.getOneAgentNo=function(){
		if($scope.info.merchantNo==null){
			return;
		}
		 $http.post("acqMerchantAction/selectOneAgentNo","merNo="+angular.toJson($scope.info.merchantNo)+"&type=2",{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
	 	 .success(function(msg){
	 			//响应成功
	 		 if(msg.bols){
	 			 $scope.info.agentName=msg.ais.agentName;
	 			 $scope.info.agentNo=msg.ais.agentNo;
	 		 }else{
	 			$scope.info.agentNos="输入普通商户号后显示对应代理商";
	 			$scope.notice(msg.msg);
	 		 }
	 	});
	}
	
/*	//鼠标离开事件
	$scope.mouseLeave=function(val){
		if(val==null||val==""){
			return;
		}
		var data={id:val};
		 $http.post("acqMerchantAction/selectServiceType","info="+angular.toJson(data),{headers: {'Content-Type':'application/x-www-form-urlencoded'}})
	 	 .success(function(msg){
	 			//响应成功
	 		 if(!msg.bols)
	 			 return;
	 		$scope.serviceTypes=[];
	 		for(var i=0; i<msg.listStr.length; i++){
	 	    	if(msg.listStr[i]=="1")
	 	    		$scope.serviceTypes.push({value:msg.listStr[i],text:"POS刷卡"});
	 	    	if(msg.listStr[i]=="2")
	 	    		$scope.serviceTypes.push({value:msg.listStr[i],text:"扫码支付"});
	 	    	if(msg.listStr[i]=="3")
	 	    		$scope.serviceTypes.push({value:msg.listStr[i],text:"快捷支付"});
	 	    	if(msg.listStr[i]=="4")
	 	    		$scope.serviceTypes.push({value:msg.listStr[i],text:"账户提现"});
	 	    }
 	    	$scope.info.merchantServiceType=msg.listStr[0];	
	 	});
	}*/
	
	
	//修改提交
	$scope.commit=function(){
		$scope.submitting = true;
		var data={"info":$scope.info,"id":$stateParams.id};
		$http.post("acqMerchantAction/updateAcqMerchant",
				 "info="+angular.toJson(data),{headers: {'Content-Type':'application/x-www-form-urlencoded'}})
	 	 .success(function(msg){
	 		 if(!msg.bols){
	 			$scope.notice(msg.msg);
				 $scope.submitting = false;
	 		 }else{
	 			$scope.notice("修改成功！");
	 			$state.transitionTo('org.orgMer',null,{reload:true});
				 $scope.submitting = false;
	 		 }
	 	 });
	}
});