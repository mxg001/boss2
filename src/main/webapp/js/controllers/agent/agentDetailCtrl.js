angular.module('inspinia').controller("agentDetailCtrl", function($scope, $http, $state, $stateParams,uiGridConstants,i18nService,$log,$uibModal,$compile) {
	var self = this;
	$scope.merRateStr='[{text:"每笔固定金额",value:"1"},{text:"每笔扣率",value:"2"},{text:"每笔扣率带保底封顶",value:"3"},{text:"每笔扣率+固定金额",value:"4"},{text:"单笔阶梯扣率",value:"5"},{text:"扣率+封顶",value:"6"}]';
	$scope.statusStr = '[{text:"正常",value:1},{text:"关闭",value:0}]';
	$scope.type = [ {text : '个人',value :  1 }, {text : '个体商户',value :  2 }, {text : '企业商户',value :  3} ];
	$scope.checkStatus=[{text:"待审核",value:0},{text:"审核通过",value:1},{text:'审核不通过',value:2}];
	$scope.effectiveStatus = [{text:"否",value:0},{text:"是",value:1},{text:'否',value:2}];
	$scope.agent={};
	$scope.teamType=[];
	$scope.dpData=[];
	$scope.shareData=[];
	$scope.rateData=[];
	$scope.quotaData=[];
	$scope.teamId = $stateParams.teamId;

	$scope.teams=[];
	$http.get('teamInfo/queryTeamName.do').success(function(msg){
		for(var i=0;i<msg.teamInfo.length;i++){
			$scope.teams.push({text:msg.teamInfo[i].teamName,value:msg.teamInfo[i].teamId});
		}
	});

	$http.get('teamInfo/queryTeamName.do').success(function(msg){
		for(var i=0;i<msg.teamInfo.length;i++){
			$scope.teamType.push({text:msg.teamInfo[i].teamName,value:msg.teamInfo[i].teamId});
		}
		//2.2.3-1$scope.agent.teamId=msg.teamInfo[0].teamId;
	});
	$scope.isCostCapping=true;
	$http.post('agentInfo/queryAgentInfoDetail',{"agentNo":$stateParams.id,"teamId":$stateParams.teamId}).success(function(msg){
		$scope.agent = msg.agentInfo;
		$scope.dpData = msg.agentProducts;
		$scope.shareData = msg.agentShare;
		$scope.rateData = msg.agentRate;
		$scope.quotaData = msg.agentQuota;
		//$scope.happyBackGrid.data = msg.hbTypes;
		$scope.happyBackData = msg.hbTypes;
		if(msg.hbTypes==null||msg.hbTypes.length==0){
            $("#happyBack").hide();
		}
        $scope.newHappyBackGrid.data = msg.xhlfList;
        if(msg.xhlfList==null||msg.xhlfList.length==0){
            $("#newHappyBack").hide();
        }
		$scope.superHappyBackGrid.data = msg.superList;
		if(msg.superList==null||msg.superList.length==0){
			$("#superHappyBack").hide();
		}


		// 对shareData按teamId进行分组
		$scope.shareDataMap = [];
		angular.forEach($scope.shareData, function (data, index) {
			var teamId = data.teamId;
			if($scope.shareDataMap[teamId] === undefined){
				$scope.shareDataMap[teamId] = [];
			}
			$scope.shareDataMap[teamId].push(data);
			if(data.rateType=="6"&&$scope.isCostCapping){
				$scope.isCostCapping=false;
				$scope.shareListColumnDefs.splice($scope.shareListColumnDefs.length-4,0,{
					field: 'costCapping', displayName: '封顶手续费', width: 130});
			}
		});

		$scope.teamdIds = [];
		$scope.shareDataList = [];
		angular.forEach($scope.shareDataMap, function (data, index) {
			$scope.teamdIds.push(index);
			$scope.shareDataList[index] = {
				data: data,
				useExternalPagination: true,		  //开启拓展名
				columnDefs: $scope.shareListColumnDefs
			}
		});

		// 欢乐返活动分组显示
		$scope.HappyBackDataMap = [];
		angular.forEach($scope.happyBackData, function (e) {
			var teamId = e.teamId;
			if($scope.HappyBackDataMap[teamId] === undefined){
				$scope.HappyBackDataMap[teamId] = [];
			}
			$scope.HappyBackDataMap[teamId].push(e);
		});

		$scope.happyTeams = [];
		$scope.happyBackDataList = [];
		$scope.happyGridApi = [];
		angular.forEach($scope.HappyBackDataMap, function (data, index) {
			$scope.happyTeams.push(index);
			$scope.happyBackDataList[index] = {
				data: data,
				useExternalPagination: true,		  //开启拓展名
				columnDefs: $scope.happyBackColumnDefs
			}
		});



	}).error(function(){
	});

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

	$scope.shareListColumnDefs = [
		    {field: 'checkStatus',displayName:'审核状态',width:135,cellFilter:"formatDropping:"+angular.toJson($scope.checkStatus)},
		    {field: 'uncheckStatus',displayName: '未生效审核状态',width: 180},
		    {field: 'bpName',displayName: '业务产品名称',width: 150},
		    {field: 'serviceName',displayName: '服务名称',width: 150},
		    /*{field: 'serviceType',displayName: '服务种类',width: 150,cellTemplate:
				'<div class="lh30" ng-show="row.entity.serviceType!=10000&&row.entity.serviceType!=10001"><span ng-bind="row.entity.serviceType | serviceTypeFilter"/></div>'
				+'<div class="lh30" ng-show="row.entity.serviceType==10000||row.entity.serviceType==10001"><span ng-bind="row.entity.serviceType2 | serviceTypeFilter"/>-提现</div>'
				},*/
            {field: 'cardType',displayName: '银行卡种类',width: 150,cellFilter:"formatDropping:"+$scope.cardTypeStr},
            {field: 'holidaysMark',displayName: '节假日标志',width: 150,cellFilter:"formatDropping:"+$scope.holidaysStr},
            {field: 'profitType',displayName: '分润方式',width: 200,cellFilter:"formatDropping:"+$scope.frStr},
            {field: 'income',displayName: '代理商收益',width: 150},
            {field: 'cost',displayName: '代理商成本',width: 150},
            {field: 'shareProfitPercent',displayName: '代理商固定分润百分比',width: 180,cellTemplate:'<span class="checkbox" ng-show="row.entity.shareProfitPercent!=null">'+
            	'{{row.entity.shareProfitPercent}}%</span>'},
            {field: 'ladderRate',displayName: '阶梯分润比例',width: 300},
            {field: 'efficientDate',displayName: '生效日期',width: 200,cellFilter:'date:"yyyy-MM-dd"'},
            {field: 'checkStatus',displayName: '当前是否生效',width: 180,cellFilter:"formatDropping:" + angular.toJson($scope.effectiveStatus)}
		];
	
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

	$scope.happyBackColumnDefs =
		[
			{field: 'activityTypeNo',displayName: '欢乐返子类型编号',width: 180},
			{field: 'activityTypeName',displayName: '欢乐返子类型名称',width: 180},
			{field: 'functionName',displayName: '欢乐返类型'},
			{field: 'transAmount',displayName: '交易金额',width: 180,cellTemplate:'<div class="lh30">{{row.entity.transAmount}}元<div/>'},
			{field: 'cashBackAmount',displayName: '返现金额',width: 180,cellTemplate:'<div class="lh30">{{row.entity.cashBackAmount}}元<div/>'},
			{field: 'taxRate',displayName: '返现比例',width: 180,cellTemplate:'<div class="lh30">{{row.entity.taxRate*100}}%<div/>'},
            {field: 'repeatRegisterAmount',displayName: '重复注册返现金额',width: 180,cellTemplate:'<div class="lh30" ng-show="row.entity.repeatRegisterAmount!=null">{{row.entity.repeatRegisterAmount}}元<div/>'},
            {field: 'repeatRegisterRatio',displayName: '重复注册返现比例',width: 180,cellTemplate:'<div class="lh30">{{row.entity.repeatRegisterRatio*100}}%<div/>'},
            {field: 'emptyAmount',displayName: '首次注册不满扣N值',width: 180,cellTemplate:'<div class="lh30" ng-show="row.entity.emptyAmount!=null">{{row.entity.emptyAmount}}元<div/>'},
            {field: 'fullAmount',displayName: '首次注册满奖M值',width: 180,cellTemplate:'<div class="lh30" ng-show="row.entity.fullAmount!=null">{{row.entity.fullAmount}}元<div/>'},
            {field: 'repeatEmptyAmount',displayName: '重复注册不满扣N值',width: 180,cellTemplate:'<div class="lh30" ng-show="row.entity.repeatEmptyAmount!=null">{{row.entity.repeatEmptyAmount}}元<div/>'},
            {field: 'repeatFullAmount',displayName: '重复注册满奖M值',width: 180,cellTemplate:'<div class="lh30" ng-show="row.entity.repeatFullAmount!=null">{{row.entity.repeatFullAmount}}元<div/>'},
            {field: 'scanRewardAmount',displayName: '扫码交易满奖首次注册奖励金额',width: 180,cellTemplate:'<div class="lh30" ng-show="row.entity.scanRewardAmount!=null">{{row.entity.scanRewardAmount}}元<div/>'},
            {field: 'scanRepeatRewardAmount',displayName: '扫码交易满奖重复注册奖励金额',width: 180,cellTemplate:'<div class="lh30" ng-show="row.entity.scanRepeatRewardAmount!=null">{{row.entity.scanRepeatRewardAmount}}元<div/>'},
            {field: 'allRewardAmount',displayName: '全部交易满奖首次注册奖励金额',width: 180,cellTemplate:'<div class="lh30" ng-show="row.entity.allRewardAmount!=null">{{row.entity.allRewardAmount}}元<div/>'},
            {field: 'allRepeatRewardAmount',displayName: '全部交易满奖重复注册奖励金额',width: 180,cellTemplate:'<div class="lh30" ng-show="row.entity.allRepeatRewardAmount!=null">{{row.entity.allRepeatRewardAmount}}元<div/>'}

        ];

    $scope.newHappyBackGrid = {
        columnDefs: [
            {field: 'activityTypeNo',displayName: '欢乐返子类型编号',width:160},
            {field: 'activityTypeName',displayName: '欢乐返子类型名称',width:160},
            // {field: 'functionName',displayName: '欢乐返类型'},
            {field: 'transAmount',displayName: '交易金额（元）',width:160,cellTemplate:'<div class="lh30">{{row.entity.transAmount}}元<div/>'},
            {field: 'cashBackAmount',displayName: '返现金额（元）',width:160,cellTemplate:'<div class="lh30">{{row.entity.cashBackAmount}}元<div/>'},
            {field: 'repeatRegisterAmount',displayName: '重复注册返现金额（元）',width:200,cellTemplate:'<div class="lh30" ng-show="row.entity.repeatRegisterAmount!=null">{{row.entity.repeatRegisterAmount}}元<div/>'},
            {field: 'repeatRegisterAmount',displayName: '第1次考核奖励金额（元）',width:200,cellTemplate:'<div class="lh30" ng-show="row.entity.oneRewardAmount!=null">{{row.entity.oneRewardAmount}}元<div/>'},
            {field: 'repeatRegisterAmount',displayName: '第1次考核重复注册奖励金额（元）',width:260,cellTemplate:'<div class="lh30" ng-show="row.entity.oneRepeatRewardAmount!=null">{{row.entity.oneRepeatRewardAmount}}元<div/>'},
			{field: 'repeatRegisterAmount',displayName: '第1次子考核奖励金额（元）',width:260,cellTemplate:'<div class="lh30" ng-show="row.entity.oneSubRewardAmount!=null">{{row.entity.oneSubRewardAmount}}元<div/>'},
			{field: 'repeatRegisterAmount',displayName: '第1次子考核重复注册奖励金额（元）',width:260,cellTemplate:'<div class="lh30" ng-show="row.entity.oneSubRepeatReward!=null">{{row.entity.oneSubRepeatReward}}元<div/>'},
            {field: 'repeatRegisterAmount',displayName: '第2次考核奖励金额（元）',width:200,cellTemplate:'<div class="lh30" ng-show="row.entity.twoRewardAmount!=null">{{row.entity.twoRewardAmount}}元<div/>'},
            {field: 'repeatRegisterAmount',displayName: '第2次考核重复注册奖励金额（元）',width:260,cellTemplate:'<div class="lh30" ng-show="row.entity.twoRepeatRewardAmount!=null">{{row.entity.twoRepeatRewardAmount}}元<div/>'},
            {field: 'repeatRegisterAmount',displayName: '第3次考核奖励金额（元）',width:200,cellTemplate:'<div class="lh30" ng-show="row.entity.threeRewardAmount!=null">{{row.entity.threeRewardAmount}}元<div/>'},
            {field: 'repeatRegisterAmount',displayName: '第3次考核重复注册奖励金额（元）',width:260,cellTemplate:'<div class="lh30" ng-show="row.entity.threeRepeatRewardAmount!=null">{{row.entity.threeRepeatRewardAmount}}元<div/>'},
            {field: 'repeatRegisterAmount',displayName: '第4次考核奖励金额（元）',width:200,cellTemplate:'<div class="lh30" ng-show="row.entity.fourRewardAmount!=null">{{row.entity.fourRewardAmount}}元<div/>'},
            {field: 'repeatRegisterAmount',displayName: '第4次考核重复注册奖励金额（元）',width:280,cellTemplate:'<div class="lh30" ng-show="row.entity.fourRepeatRewardAmount!=null">{{row.entity.fourRepeatRewardAmount}}元<div/>'},
        ]
    };

	$scope.superHappyBackGrid = {
		columnDefs: [
			{field: 'activityTypeNo',displayName: '欢乐返子类型编号',width:160},
			{field: 'activityTypeName',displayName: '欢乐返子类型名称',width:160},
			// {field: 'functionName',displayName: '欢乐返类型'},
			{field: 'transAmount',displayName: '交易金额（元）',width:160,cellTemplate:'<div class="lh30">{{row.entity.transAmount}}元<div/>'},
			{field: 'cashBackAmount',displayName: '返现金额（元）',width:160,cellTemplate:'<div class="lh30">{{row.entity.cashBackAmount}}元<div/>'},
			{field: 'repeatRegisterAmount',displayName: '重复注册返现金额（元）',width:200,cellTemplate:'<div class="lh30" ng-show="row.entity.repeatRegisterAmount!=null">{{row.entity.repeatRegisterAmount}}元<div/>'},
			{field: 'repeatRegisterAmount',displayName: '第1次考核奖励金额（元）',width:200,cellTemplate:'<div class="lh30" ng-show="row.entity.oneRewardAmount!=null">{{row.entity.oneRewardAmount}}元<div/>'},
			{field: 'repeatRegisterAmount',displayName: '第1次考核重复注册奖励金额（元）',width:260,cellTemplate:'<div class="lh30" ng-show="row.entity.oneRepeatRewardAmount!=null">{{row.entity.oneRepeatRewardAmount}}元<div/>'},
			{field: 'repeatRegisterAmount',displayName: '第2次考核奖励金额（元）',width:200,cellTemplate:'<div class="lh30" ng-show="row.entity.twoRewardAmount!=null">{{row.entity.twoRewardAmount}}元<div/>'},
			{field: 'repeatRegisterAmount',displayName: '第2次考核重复注册奖励金额（元）',width:260,cellTemplate:'<div class="lh30" ng-show="row.entity.twoRepeatRewardAmount!=null">{{row.entity.twoRepeatRewardAmount}}元<div/>'},
			{field: 'repeatRegisterAmount',displayName: '第3次考核奖励金额（元）',width:200,cellTemplate:'<div class="lh30" ng-show="row.entity.threeRewardAmount!=null">{{row.entity.threeRewardAmount}}元<div/>'},
			{field: 'repeatRegisterAmount',displayName: '第3次考核重复注册奖励金额（元）',width:260,cellTemplate:'<div class="lh30" ng-show="row.entity.threeRepeatRewardAmount!=null">{{row.entity.threeRepeatRewardAmount}}元<div/>'},
			{field: 'repeatRegisterAmount',displayName: '第4次考核奖励金额（元）',width:200,cellTemplate:'<div class="lh30" ng-show="row.entity.fourRewardAmount!=null">{{row.entity.fourRewardAmount}}元<div/>'},
			{field: 'repeatRegisterAmount',displayName: '第4次考核重复注册奖励金额（元）',width:280,cellTemplate:'<div class="lh30" ng-show="row.entity.fourRepeatRewardAmount!=null">{{row.entity.fourRepeatRewardAmount}}元<div/>'},
		]
	};

	/**
	 * 获取敏感数据
	 */
	$scope.dataSta=true;
	$scope.getDataProcessing = function () {
		if($scope.dataSta){
			$http.post("agentInfo/getDataProcessing",{"agentNo":$stateParams.id,"teamId":$stateParams.teamId})
				.success(function(data) {
					if(data.status){
						$scope.agent.mobilephone = data.agentInfo.mobilephone;
						$scope.agent.idCardNo = data.agentInfo.idCardNo;
						$scope.agent.accountNo = data.agentInfo.accountNo;
						$scope.dataSta=false;
					}else{
						$scope.notice(data.msg);
					}
				});
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