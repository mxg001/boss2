angular.module('inspinia').controller("agentDetailCtrl", function($scope, $http, $state, $stateParams,uiGridConstants,i18nService,$log,$uibModal,$compile) {
	var self = this;
	$scope.merRateStr='[{text:"每笔固定金额",value:"1"},{text:"每笔扣率",value:"2"},{text:"每笔扣率带保底封顶",value:"3"},{text:"每笔扣率+固定金额",value:"4"},{text:"单笔阶梯扣率",value:"5"}]';
	$scope.statusStr = '[{text:"正常",value:1},{text:"关闭",value:0}]';
	$scope.type = [ {text : '个人',value :  1 }, {text : '个体商户',value :  2 }, {text : '企业商户',value :  3} ];
	$scope.agent={};
	$scope.teamType=[];
	$scope.dpData=[];
	$scope.shareData=[];
	$scope.rateData=[];
	$scope.quotaData=[];
	$scope.teamId = $stateParams.teamId;
	$http.get('teamInfo/queryTeamName.do').success(function(msg){
		for(var i=0;i<msg.teamInfo.length;i++){
			$scope.teamType.push({text:msg.teamInfo[i].teamName,value:msg.teamInfo[i].teamId});
		}
		$scope.agent.teamId=msg.teamInfo[0].teamId;
	});
	$http.post('agentInfo/queryAgentInfoDetail',{"agentNo":$stateParams.id,"teamId":$stateParams.teamId}).success(function(msg){
		$scope.agent = msg.agentInfo;
		$scope.dpData = msg.agentProducts;
		$scope.shareData = msg.agentShare;
		$scope.rateData = msg.agentRate;
		$scope.quotaData = msg.agentQuota;
	}).error(function(){
	});
	/**
	 * 获取敏感数据
	 */
	$scope.dataSta=true;
	$scope.getDataProcessing = function () {
		if($scope.dataSta){
			$http.post("agentInfo/dataProcessingTwo",{"agentNo":$stateParams.id,"teamId":$stateParams.teamId})
				.success(function(data) {
					if(data.status){
						$scope.agent.mobilephone = data.agentInfo.mobilephone;
						$scope.dataSta=false;
					}else{
						$scope.notice(data.msg);
					}
				});
		}
	};
	
	
	var rowList={};
    $scope.bpList = {
        data: 'dpData',
        columnDefs: [
             {field: 'key1',displayName: '业务产品ID',width: 150},
             {field: 'key3',displayName: '业务产品名称',width: 150},
             {field: 'key5',displayName: '类型',width: 150,cellFilter:"formatDropping:"+angular.toJson($scope.type)},
             {field: 'key2',displayName: '状态',width: 150,cellFilter:"formatDropping:"+$scope.statusStr}
        ],
		onRegisterApi : function(gridApi) {
			$scope.gridApiProduct = gridApi;
		}
    };
    $scope.frStr='[{text:"每笔固定收益金额",value:"1"},{text:"每笔收益率",value:"2"},{text:"每笔收益率带保底封顶",value:"3"},{text:"每笔收益率+每笔固定收益金额",value:"4"},'+
		'{text:"成本价+固定分润比",value:"5"},{text:"成本价+阶梯分润比",value:"6"}]';
	$scope.shareList = {
		data: 'shareData',
		columnDefs: [
            {field: 'serviceName',displayName: '服务名称',width: 150},
            {field: 'cardType',displayName: '银行卡种类',width: 150,cellFilter:"formatDropping:"+$scope.cardTypeStr},
            {field: 'holidaysMark',displayName: '节假日标志',width: 150,cellFilter:"formatDropping:"+$scope.holidaysStr},
            {field: 'profitType',displayName: '分润方式',width: 200,cellFilter:"formatDropping:"+$scope.frStr},
            {field: 'income',displayName: '代理商收益',width: 150},
            {field: 'cost',displayName: '代理商成本',width: 150},
            {field: 'shareProfitPercent',displayName: '代理商固定分润百分比',width: 180,cellTemplate:'<span ng-show="row.entity.shareProfitPercent!=null">'+
            	'{{row.entity.shareProfitPercent}}%</span>'},
            {field: 'ladderRate',displayName: '阶梯分润比例',width: 300},
            {field: 'efficientDate',displayName: '生效日期',width: 200,cellFilter:'date:"yyyy-MM-dd"'},
            {field: 'efficientDate',displayName: '当前是否生效',width: 180,cellFilter:"compareDateFilter"}
		]
	};
	
	var rateRowsList={};
	$scope.rateList = {
		data: 'rateData',
		columnDefs: [
		     {field: 'isGlobal',displayName: '与公司管控费率相同',width: 180,
		    	 cellTemplate:
		    		 '<input type="checkbox" disabled="true" ng-model="row.entity.isGlobal" ng-true-value="1" ng-false-value="0"/>'
		     },
             {field: 'serviceName',displayName: '服务名称',width: 150},
             {field: 'cardType',displayName: '银行卡种类',width: 150,cellFilter:"formatDropping:"+$scope.cardTypeStr},
             {field: 'holidaysMark',displayName: '节假日标志',width: 150,cellFilter:"formatDropping:"+$scope.holidaysStr},
		     {field: 'rateType',displayName: '费率方式',width: 200,cellFilter:'formatDropping:'+$scope.merRateStr},
		     {field: 'merRate',displayName: '商户费率',width: 300}
		],
		onRegisterApi: function(gridApi) {                //选中行配置
	        $scope.rateGridApi = gridApi;
		}
	};
	
	$scope.quotaList = {
		data: 'quotaData',
		columnDefs: [
            {field: 'isGlobal',displayName: '与公司管控费率相同',width: 180,
            	 cellTemplate:
		    		 '<input type="checkbox" disabled="true" ng-model="row.entity.isGlobal" ng-true-value="1" ng-false-value="0"/>'
            },
            {field: 'serviceName',displayName: '服务名称',width: 150},
            {field: 'cardType',displayName: '银行卡种类',width: 150,cellFilter:"formatDropping:"+$scope.cardTypeStr},
            {field: 'holidaysMark',displayName: '节假日标志',width: 150,cellFilter:"formatDropping:"+$scope.holidaysStr},
            {field: 'singleDayAmount',displayName: '单日最大交易金额',width: 180,pinnable: false,sortable: false,editable:true,
            },
            {field: 'singleMinAmount',displayName: '单笔最小交易额',width: 180,pinnable: false,sortable: false,editable:true,
            },
            {field: 'singleCountAmount',displayName: '单笔最大交易额',width: 180,pinnable: false,sortable: false,editable:true,
            },
            {field: 'singleDaycardAmount',displayName: '单日单卡最大交易额',width: 200,pinnable: false,sortable: false,editable:true,
            },
            {field: 'singleDaycardCount',displayName: '单日单卡最大交易笔数',width: 250,pinnable: false,sortable: false,editable:true,
            }
		],
		onRegisterApi: function(gridApi) {                //选中行配置
	        $scope.quotaGridApi = gridApi;
    	}
	};
	
}).filter('compareDateFilter', function () {
	return function (value) {
		var date = new Date().getTime();
		if(date < value){
			return "否";
		} 
		if(date >= value ){
			return "是";
		}
	}
});