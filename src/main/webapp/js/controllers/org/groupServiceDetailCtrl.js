angular.module('inspinia').controller("groupServiceDetailCtrl", function($scope, $http, $state, $stateParams, i18nService){
	
	i18nService.setCurrentLang('zh-cn');
	$scope.rateTypes = [{text:"每笔固定金额",value:1},{text:"每笔扣率",value:2},{text:"每笔扣率带保底封顶",value:3},{text:"每笔扣率+每笔固定金额",value:4},{text:"单笔阶梯扣率",value:5},{text:"每月累计交易量阶梯",value:6}];
	$scope.boolAll = [{text:'是',value:1},{text:'否',value:2}];
	
	$http.post('groupService/serviceDetail.do',
  		 angular.toJson({id:$stateParams.id})
    ).success(function(data){
       $scope.serviceRateInfos = data.serviceRateInfos;
       $scope.serviceBaseInfo = data.serviceBaseInfo;
       $scope.serviceQuotaInfo = data.serviceQuotaInfo;
    }).error(function(){
    });
	
	$scope.serviceRateGrid = {
		data:"serviceRateInfos",
        columnDefs: [
            {field: 'id',displayName: 'ID',width: 150,pinnable: false,sortable: false},
            {field: 'cardRateType',displayName: '银行卡种类',width: 150,pinnable: false,sortable: false,
              	 cellFilter:"formatDropping:"+angular.toJson($scope.cardType)
            },
            {field: 'rateType',displayName: '费率方式',width: 150,pinnable: false,sortable: false,
            	 cellFilter:"formatDropping:"+angular.toJson($scope.rateTypes)
            },
            {field: 'serviceRate',displayName: '费率',width: 150,pinnable: false,sortable: false},
            {field: 'effectiveDate',displayName: '生效日期',width: 150,pinnable: false,sortable: false,
            	cellFilter: 'date:"yyyy-MM-dd"'
            },
            {field: 'effectiveStatus',displayName: '当前是否生效',width: 180,pinnable: false,sortable: false,
            	 cellFilter:"formatDropping:"+angular.toJson($scope.boolAll)
            }
        ]
	};
	
});