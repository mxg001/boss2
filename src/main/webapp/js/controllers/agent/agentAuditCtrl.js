angular.module('inspinia').controller("agentAuditCtrl", function($scope, $http, $state, $stateParams,uiGridConstants,i18nService,$log,$uibModal,$compile) {
	var self = this;
	$scope.merRateStr='[{text:"每笔固定金额",value:"1"},{text:"每笔扣率",value:"2"},{text:"每笔扣率带保底封顶",value:"3"},{text:"每笔扣率+固定金额",value:"4"},{text:"单笔阶梯扣率",value:"5"},{text:"扣率+封顶",value:"6"}]';
	$scope.statusStr = '[{text:"正常",value:1},{text:"关闭",value:0}]';
	$scope.type = [ {text : '个人',value :  1 }, {text : '个体商户',value :  2 }, {text : '企业商户',value :  3} ];
	$scope.effectiveStatus = [{text:"否",value:0},{text:"是",value:1},{text:'否',value:2}];
	$scope.agent={};
	$scope.teamType=[];
	$scope.bpData=[];
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
	//备份待审核的分润信息，切换的时候使用
	$scope.shareList = [];
	$scope.shareAllList = [];
	$http.post('agentInfo/queryAgentInfoAudit',{"agentNo":$stateParams.id,"teamId":$stateParams.teamId}).success(function(msg){
		$scope.agent = msg.agentInfo;
		$scope.bpData = msg.agentProducts;
		angular.forEach(msg.agentShare,function(data){
			if(data.checkStatus==1){
				data.lock=1;
			}
		})
		$scope.shareData = msg.agentShare;
		$scope.shareList = msg.agentShare;
		$scope.rateData = msg.agentRate;
		$scope.quotaData = msg.agentQuota;
	}).error(function(){
	});
	
	//获取所有的分润
	$scope.getAllShare = function(){
		if($scope.shareAllList.length==0){
			$http.post('agentInfo/getAllShare',{"agentNo":$stateParams.id,"teamId":$stateParams.teamId}).success(function(msg){
				angular.forEach(msg,function(data){
					if(data.checkStatus==1){
						data.lock=1;
					}
				})
				$scope.shareData = msg;
				$scope.shareAllList = msg;
			}).error(function(){
			});
		} else {
			$scope.shareData = $scope.shareAllList;
		}
	}
	//获取待审核的分润
	$scope.getShare = function(){
		$scope.shareData = $scope.shareList;
	}
	//全部通过
	$scope.allPass = function(){
		angular.forEach($scope.shareData,function(item){
			item.checkStatus = 1;
		})
	}
	//全部不通过
	$scope.allNotPass = function(){
		angular.forEach($scope.shareData,function(item){
			item.checkStatus = 2;
		})
	}
	var rowList={};
    $scope.bpList = {
        data: 'bpData',
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
		    {field:'checkStatus',displayName:'审核',width:150,cellTemplate:'<div ng-show="row.entity.lock==1" style="line-height:60px;margin-right:21px">审核通过</div><div ng-show="row.entity.lock!=1"><div class="dr-ov"><div class="d-radio"><label><input type="radio" name="{{row.entity.id}}" ng-model="row.entity.checkStatus" value="1" />审核通过</label></div>'
		    	+'<div class="d-radio"><label><input type="radio" ng-model="row.entity.checkStatus" name="{{row.entity.id}}" value="2" />审核不通过</label></div></div></div>'},
            {field: 'serviceName',displayName: '服务名称',width: 210},
            {field: 'cardType',displayName: '银行卡种类',width: 150,cellFilter:"formatDropping:"+$scope.cardTypeStr},
            {field: 'holidaysMark',displayName: '节假日标志',width: 150,cellFilter:"formatDropping:"+$scope.holidaysStr},
            {field: 'efficientDate',displayName: '生效日期',width: 200,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
            {field: 'effectiveStatus',displayName: '当前是否生效',width: 180,cellFilter:"formatDropping:"+angular.toJson($scope.effectiveStatus)},
            {field: 'profitType',displayName: '分润方式',width: 270,cellFilter:"formatDropping:"+$scope.frStr},
            {field: 'income',displayName: '代理商收益',width: 150},
            {field: 'cost',displayName: '代理商成本',width: 150},
            {field: 'costCapping',displayName: '封顶手续费',width: 150},
            {field: 'shareProfitPercent',displayName: '代理商固定分润百分比',width: 180,cellTemplate:'<div style="line-height:62px" ng-show="row.entity.shareProfitPercent!=null">'+
            	'{{row.entity.shareProfitPercent}}%</div>'},
            {field: 'ladderRate',displayName: '阶梯分润比例',width: 300}
            
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
	
	$scope.submit = function(){
		var submitData = [];
		angular.forEach($scope.shareData,function(data){
			var item = {id:data.id,shareId:data.shareId,checkStatus:data.checkStatus,efficientDate:data.efficientDate};
			submitData.push(item);
		})
		var data = {"shareData":submitData};
    	$http.post('agentInfo/updateAgentShare',angular.toJson(data)).success(function(msg){
			$scope.notice(msg.msg);
			if(msg.status){
				$state.transitionTo('agent.auditQuery',null,{reload:true});
			}
		});
	}
	
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