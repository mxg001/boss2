/**
 * 修改服务
 */
angular.module('inspinia').controller("editServiceCtrl", function($scope, $http, $state,$timeout, $stateParams) {
	var self = this;
	$scope.baseInfo={};
	$scope.linkServices = [];
	$scope.editService=1;
	$scope.queryDetail=function(dat){
		$http.post("service/queryServiceDetail",dat,
				{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
    	.success(function(data){
    		if(data!=null){
    			$scope.baseInfo=data;
    			if(data.linkService!=null){
    				$scope.linkServices.unshift({value:data.linkService,text:data.linkServiceName});
    			}
    			if(data.rates!=undefined&&data.rates.length>0){
    				$scope.rates=data.rates;
//    				$scope.status.rate.check=data.rates[0].checkStatus;
//    				$scope.status.rate.lock=data.rates[0].lockStatus;
//    				delete data["rates"];
    			}
    			if(data.quotas!=undefined&&data.quotas.length>0){
    				$scope.quotas=data.quotas;
//    				$scope.status.quota.check=data.quotas[0].checkStatus;
//    				$scope.status.quota.lock=data.quotas[0].lockStatus;
//    				delete data["quotas"];
    			}	
    		}
    	})
    	.error(function(data){
    	});
	};
	//数据源
	$scope.checkStatus=[{text:"已审核",value:"1"},{text:"未审核",value:"0"}];
	$scope.lockStatus=[{text:"已锁定",value:"1"},{text:"未锁定",value:"0"}];
	$scope.rateType=[{text:"每笔固定金额",value:"1"},{text:"每笔扣率",value:"2"},{text:"每笔扣率带保底封顶",value:"3"},
	                 {text:"每笔扣率+每笔固定金额",value:"4"},{text:"单笔阶梯扣率",value:"5"},{text:"扣率+封顶",value:6}];
	$scope.tFlags=[{text:"不涉及",value:0},{text:"只允许T0",value:1},{text:"只允许T1",value:2},{text:"允许T0和T1",value:3}];
	$scope.rates=[];
	$scope.quotas=[];
//	$scope.status={rate:{},quota:{}};
    $scope.serviceRateList = {
        data: 'rates',
            columnDefs: [
             {field: 'cardType',displayName: '银行卡种类',pinnable: false,sortable: false,cellFilter: "formatDropping:"+$scope.cardTypeStr},
             {field: 'holidaysMark',displayName: '节假日标志',pinnable: false,sortable: false,cellFilter: "formatDropping:"+$scope.holidaysStr},
             {field: 'merRate',displayName: '商户费率',pinnable: false,sortable: false,
             	cellTemplate: '<input style="width:98%;height:98%;" class="ui-widget input" ng-readonly="false" ng-model="row.entity[col.field]"/>'}
         ]    
    };
	
    $scope.serviceQuotaList = {
        data: 'quotas',        columnDefs: [
             {field: 'cardType',displayName: '银行卡种类',pinnable: false,sortable: false,cellFilter: "formatDropping:"+$scope.cardTypeStr},
             {field: 'holidaysMark',displayName: '节假日标志',pinnable: false,sortable: false,cellFilter: "formatDropping:"+$scope.holidaysStr},
             {field: 'singleDayAmount',displayName: '单日最大交易金额',pinnable: false,sortable: false,
            	 cellTemplate: '<input   style="width:98%;height:98%;" class="ui-widget input" ng-readonly="false" ng-model="row.entity[col.field]"/>'},
         	{field: 'singleMinAmount',displayName: '单笔最小交易额',width: "20%",pinnable: false,sortable: false,editable:true,
            	cellTemplate: '<input type="number" style="width:98%;height:98%;" class="ui-widget input" ng-readonly="false" ng-model="row.entity[col.field]"/>'},
         	{field: 'singleCountAmount',displayName: '单笔最大交易额',pinnable: false,sortable: false,
            		cellTemplate: '<input   style="width:98%;height:98%;" class="ui-widget input" ng-readonly="false" ng-model="row.entity[col.field]"/>'},
             {field: 'singleDaycardAmount',displayName: '单日单卡最大交易额',pinnable: false,sortable: false,
             	cellTemplate: '<input   style="width:98%;height:98%;" class="ui-widget input" ng-readonly="false" ng-model="row.entity[col.field]"/>'},
             {field: 'singleDaycardCount',displayName: '单日单卡最大交易笔数',pinnable: false,sortable: false,editable:true,
             	cellTemplate: '<input  style="width:98%;height:98%;" class="ui-widget input" ng-readonly="false" ng-model="row.entity[col.field]"/>'}
         ]  
    };
    
//    $scope.moneyVald=function(money){
//    	var t=/^[0-9]*[1-9][0-9]*$/;  
//    	if(!t.test(money)){
//    		if(money>0){
//    			$scope.notice("请输入整数~~~~~"); 
//    		}else{
//    			$scope.notice("请输入交易金额必须大于0~~~~~"); 
//    		}
//    	}
//    }
//    
//    $scope.numVald=function(num){
//    	var t=/^[0-9]*[1-9][0-9]*$/;  
//    	if(!t.test(num)){
//    		if(num>0){
//    			$scope.notice("请输入整数~~~~~"); 
//    		}else{
//    			$scope.notice("请输入交易次数必须大于0~~~~~"); 
//    		}
//    	}
//    	
//    }
    
//    $scope.linkServicesBak = angular.copy($scope.linkServices);
  //获取可以关联的所有服务
    $http.post("service/getLinkServices")
	.success(function(result){
		if(result){
			angular.forEach(result,function(data,index){
				$scope.linkServices.push({text:data.serviceName,value:data.serviceId});
			})
		}
	})
	.error(function(result){
	});
    
    $scope.changeServiceType = function(){
    	if($scope.baseInfo.serviceType == 10001){
    		$scope.allLinkServices = $scope.linkServicesBak;
    	} else {
    		$scope.allLinkServices = $scope.linkServices;
    	}
    }
    $scope.changeServiceType();
    $scope.updateRates=function(){
    	card=$scope.baseInfo.rateCard;
    	holiday=$scope.baseInfo.rateHolidays;
    	$scope.rates.length=0;
    	if(card==0){
    		if(holiday==0){
    			$scope.rates.push({"cardType":"0","holidaysMark":"0","merRate":""});
    		}else{
    			$scope.rates.push({"cardType":"0","holidaysMark":"1","merRate":""});
    			$scope.rates.push({"cardType":"0","holidaysMark":"2","merRate":""});
    		}
    	}else{
    		if(holiday==0){
    			$scope.rates.push({"cardType":"1","holidaysMark":"0","merRate":""});
    			$scope.rates.push({"cardType":"2","holidaysMark":"0","merRate":""});
    		}else{
    			$scope.rates.push({"cardType":"1","holidaysMark":"1","merRate":""});
    			$scope.rates.push({"cardType":"1","holidaysMark":"2","merRate":""});
    			$scope.rates.push({"cardType":"2","holidaysMark":"1","merRate":""});
    			$scope.rates.push({"cardType":"2","holidaysMark":"2","merRate":""});
    		}
    	}
    };
    $scope.updateQuotas=function(){
    	card=$scope.baseInfo.quotaCard;
    	holiday=$scope.baseInfo.quotaHolidays;
    	$scope.quotas.length=0;
    	if(card==0){
    		if(holiday==0){
    			$scope.quotas.push({"cardType":"0","holidaysMark":"0","singleDayAmount":0,"singleMinAmount":0,"singleCountAmount":0,"singleDaycardAmount":0,"singleDaycardCount":0});
    		}else{
    			$scope.quotas.push({"cardType":"0","holidaysMark":"1","singleDayAmount":0,"singleMinAmount":0,"singleCountAmount":0,"singleDaycardAmount":0,"singleDaycardCount":0});
    			$scope.quotas.push({"cardType":"0","holidaysMark":"2","singleDayAmount":0,"singleMinAmount":0,"singleCountAmount":0,"singleDaycardAmount":0,"singleDaycardCount":0});
    		}
    	}else{
    		if(holiday==0){
    			$scope.quotas.push({"cardType":"1","holidaysMark":"0","singleDayAmount":0,"singleMinAmount":0,"singleCountAmount":0,"singleDaycardAmount":0,"singleDaycardCount":0});
    			$scope.quotas.push({"cardType":"2","holidaysMark":"0","singleDayAmount":0,"singleMinAmount":0,"singleCountAmount":0,"singleDaycardAmount":0,"singleDaycardCount":0});
    		}else{
    			$scope.quotas.push({"cardType":"1","holidaysMark":"1","singleDayAmount":0,"singleMinAmount":0,"singleCountAmount":0,"singleDaycardAmount":0,"singleDaycardCount":0});
    			$scope.quotas.push({"cardType":"1","holidaysMark":"2","singleDayAmount":0,"singleMinAmount":0,"singleCountAmount":0,"singleDaycardAmount":0,"singleDaycardCount":0});
    			$scope.quotas.push({"cardType":"2","holidaysMark":"1","singleDayAmount":0,"singleMinAmount":0,"singleCountAmount":0,"singleDaycardAmount":0,"singleDaycardCount":0});
    			$scope.quotas.push({"cardType":"2","holidaysMark":"2","singleDayAmount":0,"singleMinAmount":0,"singleCountAmount":0,"singleDaycardAmount":0,"singleDaycardCount":0});
    		}
    	}
    };
    
  //服务名称是否存在
    $scope.existFlag = false;
    $scope.existServiceName = function(){
    	$http.post("service/existServiceName",angular.toJson({"baseInfo":$scope.baseInfo}))
    	.success(function(msg){
    		if(msg.status){
    			$scope.existFlag = true;
    		} else {
    			$scope.existFlag = false;
    		}
    	});
    }

	//代理商展示名称是否存在
	$scope.agentShowNameFlag = false;
	$scope.existAgentShowName = function(){
		$http.post("service/existAgentShowName",{"baseInfo":$scope.baseInfo})
			.success(function(msg){
				if(msg.status){
					$scope.agentShowNameFlag = true;
				} else {
					$scope.agentShowNameFlag = false;
				}
			});
	}
    
    $scope.commitData=function(){
		$scope.submitting = true;
//    	var timestamp2 = Date.parse(new Date($scope.baseInfo.useStarttime));
//    	if(timestamp2>$scope.baseInfo.useEndtime){
//    		$scope.notice("使用的起始时间不能大于结束时间");
//			$scope.submitting = false;
//    		return;
//    	}
//    	if($scope.baseInfo.tradStart>$scope.baseInfo.tradEnd){
//    		$scope.notice("每日交易起始时间不能大于结束时间");
//			$scope.submitting = false;
//    		return;
//    	}
    	var data={"baseInfo":$scope.baseInfo,"rates":$scope.rates,"quotas":$scope.quotas};  
	 	$http.post("service/updateServiceDetail",data)
		   	.success(function(data){
		   		if(data.status){
		   			$scope.notice("修改服务成功！"); 
	    			$state.transitionTo('service.queryService',null,{reload:true});
					$scope.submitting = false;
	    		}else
	    			$scope.notice(data.msg,{classes:"alert-danger"});
					$scope.submitting = false;
		   	})
		   	.error(function(data){
		   		$scope.notice("修改服务失败！",{classes:"alert-danger"});
				$scope.submitting = false;
		   	});
    };
    $scope.queryDetail("serviceId="+$stateParams.serviceId);
});

