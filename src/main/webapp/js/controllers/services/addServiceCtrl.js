/**
 * 填加服务
 */
angular.module('inspinia').controller("addServiceCtrl", function($scope, $http, $state, $stateParams) {
	var self = this;
	$scope.editService=0;
	//数据源
	$scope.tFlags=[{text:"不涉及",value:0},{text:"只允许T0",value:1},{text:"只允许T1",value:2},{text:"允许T0和T1",value:3}];
	$scope.baseInfo={bankCard:0,serviceName:"",agentShowName:"",serviceType:4,rateCard:0,rateHolidays:0,quotaHolidays:0,quotaCard:0,tFlag:0,cashSubject:"",linkService:null,
			useStarttime:moment(new Date().getTime()).format('YYYY-MM-DD'),useEndtime:"2099-12-30",hardwareIs:0,remark:"",tradStart:"00:00",tradEnd:"23:59",fixedRate:1,fixedQuota:1,
			loanMergeShow:1};
	$scope.rates=[{"cardType":0,"holidaysMark":0,"merRate":0,"fixedMark":0}];
	$scope.quotas=[{"cardType":0,"holidaysMark":0,"singleDayAmount":0,"singleMinAmount":0,"singleCountAmount":0,"singleDaycardAmount":0,"singleDaycardCount":0,"fixedMark":0}];
    $scope.serviceRateList = {
        data: 'rates',
        columnDefs: [
            {field: 'cardType',displayName: '银行卡种类',width: "30%",pinnable: false,sortable: false,cellFilter: "formatDropping:"+$scope.cardTypeStr},
            {field: 'holidaysMark',displayName: '节假日标志',width: "30%",pinnable: false,sortable: false,cellFilter: "formatDropping:"+$scope.holidaysStr},
            {field: 'merRate',displayName: '商户费率',width: "30%",pinnable: false,sortable: false,editable:true,
            	cellTemplate: '<input style="width:98%;height:98%;" class="ui-widget input" ng-readonly="false" ng-model="row.entity[col.field]"/>'}
        ]    
    };
	
    $scope.serviceQuotaList = {
        data: 'quotas',
        columnDefs: [
            {field: 'cardType',displayName: '银行卡种类',width:"12%",pinnable: false,sortable: false,cellFilter: "formatDropping:"+$scope.cardTypeStr},
            {field: 'holidaysMark',displayName: '节假日标志',width:"12%",pinnable: false,sortable: false,cellFilter: "formatDropping:"+$scope.holidaysStr},
            {field: 'singleDayAmount',displayName: '单日最大交易金额',width: "15%",pinnable: false,sortable: false,editable:true,
            	cellTemplate: '<input type="number" style="width:98%;height:98%;" class="ui-widget input" ng-readonly="false" ng-model="row.entity[col.field]"/>'},
        	{field: 'singleMinAmount',displayName: '单笔最小交易额',width: "15%",pinnable: false,sortable: false,editable:true,
            	cellTemplate: '<input type="number" style="width:98%;height:98%;" class="ui-widget input" ng-readonly="false" ng-model="row.entity[col.field]"/>'},
            {field: 'singleCountAmount',displayName: '单笔最大交易额',width: "15%",pinnable: false,sortable: false,editable:true,
            	cellTemplate: '<input type="number" style="width:98%;height:98%;" class="ui-widget input" ng-readonly="false" ng-model="row.entity[col.field]"/>'},
            {field: 'singleDaycardAmount',displayName: '单日单卡最大交易额',width: "15%",pinnable: false,sortable: false,editable:true,
            	cellTemplate: '<input type="number" style="width:98%;height:98%;" class="ui-widget input" ng-readonly="false" ng-model="row.entity[col.field]"/>'},
            {field: 'singleDaycardCount',displayName: '单日单卡最大交易笔数',width: "15%",pinnable: false,sortable: false,editable:true,
            	cellTemplate: '<input  type="number" style="width:98%;height:98%;" class="ui-widget input" ng-readonly="false" ng-model="row.entity[col.field]"/>'}
        ]    
    };
    
    $scope.linkServices = [];
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
    			$scope.rates.push({"cardType":0,"holidaysMark":0,"merRate":0});
    		}else{
    			$scope.rates.push({"cardType":0,"holidaysMark":1,"merRate":0});
    			$scope.rates.push({"cardType":0,"holidaysMark":2,"merRate":0});
    		}
    	}else{
    		if(holiday==0){
    			$scope.rates.push({"cardType":1,"holidaysMark":0,"merRate":0});
    			$scope.rates.push({"cardType":2,"holidaysMark":0,"merRate":0});
    		}else{
    			$scope.rates.push({"cardType":1,"holidaysMark":1,"merRate":0});
    			$scope.rates.push({"cardType":1,"holidaysMark":2,"merRate":0});
    			$scope.rates.push({"cardType":2,"holidaysMark":1,"merRate":0});
    			$scope.rates.push({"cardType":2,"holidaysMark":2,"merRate":0});
    		}
    	}
    };
    $scope.updateQuotas=function(){
    	card=$scope.baseInfo.quotaCard;
    	holiday=$scope.baseInfo.quotaHolidays;
    	$scope.quotas.length=0;
    	if(card==0){
    		if(holiday==0){
    			$scope.quotas.push({"cardType":0,"holidaysMark":0,"singleDayAmount":0,"singleMinAmount":0,"singleCountAmount":0,"singleDaycardAmount":0,"singleDaycardCount":0});
    		}else{
    			$scope.quotas.push({"cardType":0,"holidaysMark":1,"singleDayAmount":0,"singleMinAmount":0,"singleCountAmount":0,"singleDaycardAmount":0,"singleDaycardCount":0});
    			$scope.quotas.push({"cardType":0,"holidaysMark":2,"singleDayAmount":0,"singleMinAmount":0,"singleCountAmount":0,"singleDaycardAmount":0,"singleDaycardCount":0});
    		}
    	}else{
    		if(holiday==0){
    			$scope.quotas.push({"cardType":1,"holidaysMark":0,"singleDayAmount":0,"singleMinAmount":0,"singleCountAmount":0,"singleDaycardAmount":0,"singleDaycardCount":0});
    			$scope.quotas.push({"cardType":2,"holidaysMark":0,"singleDayAmount":0,"singleMinAmount":0,"singleCountAmount":0,"singleDaycardAmount":0,"singleDaycardCount":0});
    		}else{
    			$scope.quotas.push({"cardType":1,"holidaysMark":1,"singleDayAmount":0,"singleMinAmount":0,"singleCountAmount":0,"singleDaycardAmount":0,"singleDaycardCount":0});
    			$scope.quotas.push({"cardType":1,"holidaysMark":2,"singleDayAmount":0,"singleMinAmount":0,"singleCountAmount":0,"singleDaycardAmount":0,"singleDaycardCount":0});
    			$scope.quotas.push({"cardType":2,"holidaysMark":1,"singleDayAmount":0,"singleMinAmount":0,"singleCountAmount":0,"singleDaycardAmount":0,"singleDaycardCount":0});
    			$scope.quotas.push({"cardType":2,"holidaysMark":2,"singleDayAmount":0,"singleMinAmount":0,"singleCountAmount":0,"singleDaycardAmount":0,"singleDaycardCount":0});
    		}
    	}
    };
    //服务名称是否存在
    $scope.existFlag = false;
    $scope.existServiceName = function(){
    	$http.post("service/existServiceName",{"baseInfo":$scope.baseInfo})
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
//    	if($scope.baseInfo.useStarttime>$scope.baseInfo.useEndtime){
//    		$scope.notice("起始时间不能大于结束时间");
//			$scope.submitting = false;
//    		return;
//    	}
//    	if($scope.baseInfo.tradStart>$scope.baseInfo.tradEnd){
//    		$scope.notice("每日交易起始时间不能大于结束时间");
//			$scope.submitting = false;
//    		return;
//    	}
    	var t=/^([1-9]\d*|[0]{1,1})$/;  
    	for(var i=0;i<$scope.quotas.length;i++){
			if(!t.test($scope.quotas[i].singleDaycardCount)){
				$scope.notice("服务管控额度的单日单卡笔数必须为整数");
				$scope.submitting = false;
				return;
			}
    	};
    	var data={"baseInfo":$scope.baseInfo,"rates":$scope.rates,"quotas":$scope.quotas};
    	$http.post("service/addServiceType",angular.toJson(data))
    	.success(function(data){
    		if(data.status){
    			$state.transitionTo('service.addService',null,{reload:true});
    			$scope.notice("添加服务成功！");
				$scope.submitting = false;
    		}else
    			$scope.notice(data.msg);
				$scope.submitting = false;
    	})
    	.error(function(data){
			$scope.submitting = false;
    	});
    };


	

});

