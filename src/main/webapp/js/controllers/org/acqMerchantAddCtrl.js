/**
 * 收单机构商户新增
 */
angular.module('inspinia').controller('acqMerchantAddCtrl',function($scope, $http, $state, $stateParams, i18nService,$filter) {
	
	i18nService.setCurrentLang('zh-cn');
	
	$scope.agents=[];
	$scope.acqOrgs=[];
//	$scope.serviceTypes = [{text:"POS刷卡",value:1},{text:"扫码支付",value:2},{text:"快捷支付",value:3},{text:"账户提现",value:4}];
	$scope.acqServices=[];
	$scope.info={merchantServiceType:1,agentNos:"输入普通商户号后显示对应代理商",acqMerchantType:0,source:null,bind:0};
    $scope.specialSelect = [{text:"是",value:'1'},{text:"否",value:'0'}];
	$scope.shuzu=[{id:""}];
	$scope.num=0;
	$scope.sourceList = angular.copy($scope.acqMerSourceList);
	$scope.sourceList.unshift({text:"",value:null});
	//$scope.BusiProdDef=[{text:"全部",value:-1}];
	//代理商
//	 $http.post("agentInfo/selectAllInfo.do")
// 	 .success(function(msg){
// 			//响应成功
// 	    	for(var i=0; i<msg.length; i++){
// 	    		$scope.agents.push({value:msg[i].agentNo,text:msg[i].agentName});
// 	    	}
// 	    	$scope.info.agentNo=msg[0].agentNo;
// 	});
	
	//收单机构
	 $http.post("acqOrgAction/selectBoxAllInfo")
 	 .success(function(msg){
 			//响应成功
 	    	for(var i=0; i<msg.length; i++){
 	    		$scope.acqOrgs.push({value:msg[i].id,text:msg[i].acqName});
 	    	}
 	    	$scope.info.acqOrgId=msg[0].id;
 	    	$scope.aceId(msg[0].id);
 	});

	//业务产品
	/*$http.post("businessProductDefine/selectAllInfo.do")
		.success(function(msg){
			//响应成功
			for(var i=0; i<msg.length; i++){
				$scope.BusiProdDef.push({value:msg[i].bpId,text:msg[i].bpName});
			}
		});*/
	 
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
			    	$scope.info.acqServiceId=msg[0].id;
	 		 	}
	 		 	
	 	});
	 }
	 //终端操作
	$scope.addTermianl=function(){
		$scope.shuzu.push({id:""});
	}
	$scope.delTermianl=function(idx){
	    $scope.shuzu.splice(idx,1);
	}
	
	//鼠标离开事件
	/*$scope.mouseLeave=function(val){
		if(val==null||val==""){
			$scope.num=0;
			$scope.merchantServiceType=1;
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
	 		$scope.num=1;
 	    	$scope.info.merchantServiceType=msg.listStr[0];	
	 	});
	}*/
	
	//根据商户编号去查询一级代理商
	$scope.getOneAgentNo=function(){
		if(isNaN($scope.info.merchantNo)||$scope.info.merchantNo==""){
			return;
		}
		 $http.post("acqMerchantAction/selectOneAgentNo","merNo="+angular.toJson($scope.info.merchantNo)+"&type=1",{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
	 	 .success(function(msg){
	 			//响应成功
	 		 if(msg.bols){
	 			 $scope.info.agentNos = msg.ais.agentName;
	 			 $scope.info.agentNo = msg.ais.agentNo;
	 			 $scope.info.merchantName = msg.merchantName;
	 			 $scope.businesses = msg.businesses;
	 		 }else{
	 			$scope.info.agentNos="输入普通商户号后显示对应代理商";
	 			$scope.info.merchantNo = "";
	 			$scope.notice(msg.msg);
	 		 }
	 	});
	}


	$scope.openTip = function(){
		$('#tipModal').modal('show');
	}
	$scope.closeTip = function(){
		$('#tipModal').modal('hide');
	}

	
	
	//提交
	$scope.commit=function(){
		$scope.submitting = true;
		$scope.info.special == 1 ? $scope.info.special = 1 : $scope.info.special = 0;
		var data={"info":$scope.info,"list":[]};
		for(var i=0;i<$scope.shuzu.length;i++){
			if($scope.shuzu[i].id==""||$scope.shuzu[i].id==null){
				$scope.notice("终端不能为空！！！！");
				$scope.submitting = false;
				return;
			}
			data.list.push($scope.shuzu[i].id);
		}
		 $http.post("acqMerchantAction/addAcqMerchant",
				 "info="+angular.toJson(data),{headers: {'Content-Type':'application/x-www-form-urlencoded'}})
	 	 .success(function(msg){
	 		 if(!msg.bols){
	 			$scope.notice(msg.msg);
				 $scope.submitting = false;
	 		 }else{
	 		 	 $scope.notice("新增成功！");
				 $scope.closeTip();
				 $scope.submitting = false;
	 			 $state.transitionTo('org.orgMer',null,{reload:true});
	 		 }
	 	 });
		
	}

});