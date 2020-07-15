angular.module('inspinia').controller("serviceDetailCtrl", function($scope, $http, $state, $stateParams, i18nService){
	
	i18nService.setCurrentLang('zh-cn');
//	$scope.serviceTypes = [{text:"单笔代付-自有资金",value:1},{text:"单笔代付-垫资",value:2},{text:"批量代付",value:3}];
	$scope.priorities = [{text:"A级",value:"1"},{text:"B级",value:"2"},{text:"C级",value:"3"},{text:"D级",value:"4"},{text:"E级",value:"5"}];
	$scope.rateTypes = [{text:"每笔固定金额",value:1},{text:"每笔扣率",value:2},{text:"每笔扣率带保底封顶",value:3},{text:"每笔扣率+每笔固定金额",value:4},{text:"每月日均累计交易量阶梯扣率带保底",value:5}];
	$scope.costRateTypes = [{text:"每笔固定金额",value:1},{text:"每笔扣率",value:2},{text:"每笔扣率带保底",value:3},{text:"每笔扣率+每笔固定金额",value:4},{text:"每月日均累计交易量阶梯扣率带保底",value:5}];
	$scope.bool = [{text:"是",value:1},{text:"否",value:2}];
	
	$scope.serviceType = $stateParams.serviceType;
	$scope.btnIsHide1 = false;
	$scope.btnIsHide2 = false;
	
	$http.post('outAccountService/getServiceDetail.do',
  		angular.toJson({serviceId:$stateParams.id})
    ).success(function(res){
    	$scope.serviceBaseInfo = res.serviceBaseInfo;
    	$scope.agentServiceRateData = res.agentServiceRateList;
    	$scope.costServiceRateData = res.costServiceRateList;
    }).error(function(){
    }); 
	
	$scope.agentServiceRateGrid = {
		data:"agentServiceRateData",
        columnDefs: [
            {field: 'id',displayName: 'ID',width: 150,pinnable: false,sortable: false},
            {field: 'agentRateType',displayName: '费率方式',width: 150,pinnable: false,sortable: false,
            	 cellFilter:"formatDropping:"+angular.toJson($scope.rateTypes)
            },
            {field: 'serviceRate',displayName: '费率',width: 150,pinnable: false,sortable: false},
            {field: 'effectiveDate',displayName: '生效日期',width: 150,pinnable: false,sortable: false,
            	cellFilter: 'date:"yyyy-MM-dd"'
            },
            {field: 'effectiveStatus',displayName: '当前是否生效',width: 180,pinnable: false,sortable: false,
            	 cellFilter:"formatDropping:"+angular.toJson($scope.bool)
            }
        ]
	};
	
	$scope.queryAgentServiceRate = function(){
		$scope.query(1,$scope.agentServiceRateData[0].id);
		$scope.btnIsHide1 = true;
	}
	
	$scope.costServiceRateGrid = {
		data:"costServiceRateData",
        columnDefs: [
            {field: 'id',displayName: 'ID',width: 150,pinnable: false,sortable: false},
            {field: 'costRateType',displayName: '费率方式',width: 150,pinnable: false,sortable: false,
            	 cellFilter:"formatDropping:"+angular.toJson($scope.costRateTypes)
            },
            {field: 'serviceRate',displayName: '费率',width: 150,pinnable: false,sortable: false},
            {field: 'effectiveDate',displayName: '生效日期',width: 150,pinnable: false,sortable: false,
            	cellFilter: 'date:"yyyy-MM-dd"'
            },
            {field: 'effectiveStatus',displayName: '当前是否生效',width: 180,pinnable: false,sortable: false,
            	 cellFilter:"formatDropping:"+angular.toJson($scope.bool)
            }
        ]
	};
	
	$scope.queryCostServiceRate = function(){
		$scope.query(2,$scope.costServiceRateData[0].id);
		$scope.btnIsHide2 = true;
	}
	
	$scope.query = function (oper, serviceRateId) {
		$http.post('outAccountService/queryServiceRateLog.do',
	  		angular.toJson({serviceRateId:serviceRateId})
	    ).success(function(data){
	    	if(oper==1){
	    		for(var i=0; i<data.length; i++){
	    			$scope.agentServiceRateData.push(data[i]);
	    		}
	    	}else if(oper==2){
	    		for(var i=0; i<data.length; i++){
	    			$scope.costServiceRateData.push(data[i]);
	    		}
	    	}
	    }).error(function(){
	    }); 
	}
	
});