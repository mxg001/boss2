/**
 * 填加服务
 */
angular.module('inspinia').controller("detailServiceCtrl", function($scope, $http, $state, $stateParams) {
	var self = this;
	$scope.queryDetail=function(dat){
		$http.post("service/queryServiceDetail",dat,
				{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
    	.success(function(data){
    		if(data!=null){
    			$scope.baseInfo=data;
    			if(data.rates!=undefined&&data.rates.length>0){
    				$scope.rates=data.rates;
    				$scope.status.rate.check=data.rates[0].checkStatus;
    				$scope.status.rate.lock=data.rates[0].lockStatus;
    			}
    			if(data.quotas!=undefined&&data.quotas.length>0){
    				$scope.quotas=data.quotas;
    				$scope.status.quota.check=data.quotas[0].checkStatus;
    				$scope.status.quota.lock=data.quotas[0].lockStatus;
    			}	
    		}
    	})
    	.error(function(data){
    	});
	};
	
	//数据源
	$scope.fixedMark=[{text:"固定",value:1},{text:"不固定",value:0}];
	$scope.checkStatus=[{text:"已审核",value:1},{text:"未审核",value:0}];
	$scope.lockStatus=[{text:"已锁定",value:1},{text:"未锁定",value:0}];
	$scope.rateType=[{text:"每笔固定金额",value:1},{text:"每笔扣率",value:2},{text:"每笔扣率带保底封顶",value:3},
	                 {text:"每笔扣率+每笔固定金额",value:4},{text:"单笔阶梯扣率",value:5},{text:"扣率+封顶",value:6}];
	$scope.tFlags=[{text:"不涉及",value:0},{text:"只允许T0",value:1},{text:"只允许T1",value:2},{text:"允许T0和T1",value:3}];
	$scope.baseInfo={};
	$scope.rates=[];
	$scope.quotas=[];
	$scope.status={rate:{},quota:{}};
    $scope.serviceRateList = {
        data: 'rates',
        columnDefs: [
            {field: 'cardType',displayName: '银行卡种类',width: 220,pinnable: false,sortable: false,cellFilter: "formatDropping:cardType"},
            {field: 'holidaysMark',displayName: '节假日标志',width: 220,pinnable: false,sortable: false,cellFilter: "formatDropping:holidays"},
            {field: 'rateType',displayName: '费率类型',width: 220,pinnable: false,sortable: false,cellFilter: "formatDropping:rateType"},
            {field: 'merRate',displayName: '商户费率',width: 320,pinnable: false,sortable: false}
        ]    
    };
	
    $scope.serviceQuotaList = {
        data: 'quotas',
        columnDefs: [
            {field: 'cardType',displayName: '银行卡种类',width: 120,pinnable: false,sortable: false,cellFilter: "formatDropping:cardType"},
            {field: 'holidaysMark',displayName: '节假日标志',width: 120,pinnable: false,sortable: false,cellFilter: "formatDropping:holidays"},
            {field: 'singleDayAmount',displayName: '单日最大交易金额',width: 150,pinnable: false,sortable: false},
            {field: 'singleMinAmount',displayName: '单笔最小交易额',width: 150,pinnable: false,sortable: false},
            {field: 'singleCountAmount',displayName: '单笔最大交易额',width: 150,pinnable: false,sortable: false},
            {field: 'singleDaycardAmount',displayName: '单日单卡最大交易额',width: 150,pinnable: false,sortable: false},
            {field: 'singleDaycardCount',displayName: '单日单卡最大交易笔数',width: 150,pinnable: false,sortable: false}
        ]    
    };
    $scope.queryDetail("serviceId="+$stateParams.serviceId);
});

