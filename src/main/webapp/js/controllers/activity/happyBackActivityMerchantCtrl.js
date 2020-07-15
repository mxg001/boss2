/**
 * 欢乐返活跃商户活动查询
 */
angular.module('inspinia',['infinity.angular-chosen']).controller('happyBackActivityMerchantCtrl',function($scope,$http,$state,$stateParams,i18nService,$document,SweetAlert,$timeout){
	//数据源
	i18nService.setCurrentLang('zh-cn');
	$scope.disabledMerchantType = true;
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	$scope.baseInfo = {targetStatus:"",rewardAccountStatus:"",deductStatus:"",agentNo:"",oneAgentNo:"",repeatStatus:""};
	$scope.resetForm=function(){
		$scope.baseInfo = {targetStatus:"",rewardAccountStatus:"",deductStatus:"",agentNo:"",oneAgentNo:"",repeatStatus:""};
	};
	//达标状态
	$scope.targetStatusList = [{text:'全部',value:''},{text:'未达标',value:'0'},{text:'已达标',value:'1'}];
	//奖励入账状态
	$scope.rewardAccountStatusList = [{text:'全部',value:''},{text:'未入账',value:'0'},{text:'已入账',value:'1'}];
	//扣款状态
	$scope.deductStatusList = [{text:'全部',value:''},{text:'未扣款',value:'0'},{text:'已扣款',value:'1'},{text:'已发起预调账',value:'2'},{text:'待扣款',value:'3'}];

	$scope.agent=[{value:"",text:"全部"}];
	//代理商
	$http.post("agentInfo/selectAllInfo")
		.success(function(msg){
			//响应成功
			for(var i=0; i<msg.length; i++){
				$scope.agent.push({value:msg[i].agentNo,text:msg[i].agentNo + " " + msg[i].agentName});
			}
		});
	//动态代理商
	$scope.getStates =getStates;
	var oldValue="";
	var timeout="";
	function getStates(value) {
		$scope.agentt = [];
		var newValue=value;
		if(newValue != oldValue){
			if (timeout) $timeout.cancel(timeout);
			timeout = $timeout(
				function(){
					$http.post('agentInfo/selectAllInfo','item=' + value,
						{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
						.then(function (response) {
							if(response.data.length==0) {
								$scope.agentt.push({value: "", text: "全部"});
							}else{
								$scope.agentt.push({value: "", text: "全部"});
								for(var i=0; i<response.data.length; i++){
									$scope.agentt.push({value:response.data[i].agentNo,text:response.data[i].agentNo + " " + response.data[i].agentName});
								}
							}
							$scope.agent = $scope.agentt;
							oldValue = value;
						});
				},800);
		}
	};

	//一级代理商
	$http.post("agentInfo/selectAllOneInfo")
		.success(function(msg){
			//响应成功
			for(var i=0; i<msg.length; i++){
				$scope.oneAgent.push({value:msg[i].agentNo,text:msg[i].agentNo + " " + msg[i].agentName});
			}
		});
	//条件查询一级代理商
	$scope.oneAgent=[{value:"",text:"全部"}];
	$scope.getStatesOne =getStatesOne;
	var oldValueOne="";
	var timeoutOne="";
	function getStatesOne(value) {
		$scope.agenttOne = [];
		var newValueOne=value;
		if(newValueOne != oldValueOne){
			if (timeoutOne) $timeout.cancel(timeoutOne);
			timeoutOne = $timeout(
				function(){
					$http.post('agentInfo/selectAllOneInfo','item=' + value,
						{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
						.then(function (response) {
							if(response.data.length==0) {
								$scope.agenttOne.push({value: "", text: "全部"});
							}else{
								$scope.agenttOne.push({value: "", text: "全部"});
								for(var i=0; i<response.data.length; i++){
									$scope.agenttOne.push({value:response.data[i].agentNo,text:response.data[i].agentNo + " " + response.data[i].agentName});
								}
							}
							$scope.oneAgent = $scope.agenttOne;
							oldValueOne = value;
						});
				},800);
		}
	};

	//查询
	$scope.query=function(){
		$http.post('happyBackActivityMerchant/selectHappyBackActivityMerchant',"baseInfo="+angular.toJson($scope.baseInfo)
			+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
			{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			.success(function(data){
				if(!data){
					$scope.notice("没有查询到数据");
					return;
				}else{
					$scope.activityGrid.data = data.result;
					$scope.activityGrid.totalItems = data.totalCount;
				}
			})
		$http.post('happyBackActivityMerchant/countMoney',"baseInfo="+angular.toJson($scope.baseInfo)
			,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			.success(function(data){
				$scope.rewardAmountTotalEd = data.rewardAmountTotalEd == null ? "0.00" : data.rewardAmountTotalEd;//奖励已入账
				$scope.rewardAmountTotal = data.rewardAmountTotal == null ? "0.00" : data.rewardAmountTotal;//奖励未入账
				$scope.deductAmountTotalEd = data.deductAmountTotalEd == null ? "0.00" : data.deductAmountTotalEd;//已扣款
				$scope.deductAmountTotal = data.deductAmountTotal == null ? "0.00" : data.deductAmountTotal;//待扣款
				$scope.deductAmountTotalAdvance = data.deductAmountTotalAdvance == null ? "0.00" : data.deductAmountTotalAdvance;//发起预调账
			})
	};
	$scope.query();
	$scope.columnDefs = [
		{field: 'activeOrder',displayName: '激活订单号',pinnable: false,width: 180,sortable: false},
		{field: 'activeTime',displayName: '激活日期',pinnable: false,width: 180,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
		{field: 'targetStatus',displayName: '奖励达标状态',pinnable: false,width: 150,sortable: false,cellFilter:"formatDropping:"+ angular.toJson($scope.targetStatusList)},
		{field: 'rewardAmount',displayName: '奖励金额(元)',pinnable: false,width: 180,sortable: false},
		{field: 'rewardAccountStatus',displayName: '奖励入账状态',pinnable: false,width: 150,sortable: false,cellFilter:"formatDropping:"+ angular.toJson($scope.rewardAccountStatusList)},
		{field: 'targetTime',displayName: '奖励达标日期',pinnable: false,width: 180,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
		{field: 'rewardAccountTime',displayName: '奖励入账日期',pinnable: false,width: 180,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
		{field: 'deductAmount',displayName: '扣款金额(元)',pinnable: false,width: 180,sortable: false},
		{field: 'deductStatus',displayName: '扣款状态',pinnable: false,width: 150,sortable: false,cellFilter:"formatDropping:"+ angular.toJson($scope.deductStatusList)},
		{field: 'deductTime',displayName: '扣款/调账日期',pinnable: false,width: 180,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
		{field: 'repeatStatus',displayName: '是否重复注册',pinnable: false,width: 150,sortable: false,cellFilter:"formatDropping:"+ angular.toJson($scope.bool)},
		{field: 'activityTypeNo',displayName: '欢乐返子类型编号',pinnable: false,width: 150,sortable: false},
		{field: 'merchantNo',displayName: '所属商户编号',pinnable: false,width: 150,sortable: false},
		{field: 'teamName',displayName: '所属组织',pinnable: false,width: 150,sortable: false},
		{field: 'teamEntryName',displayName: '所属子组织',pinnable: false,width: 150,sortable: false},
		{field: 'hardId',displayName: '硬件产品ID',pinnable: false,width: 150,sortable: false},
		{field: 'agentName',displayName: '所属代理商名称',pinnable: false,width: 150,sortable: false},
		{field: 'agentNo',displayName: '所属代理商编号',pinnable: false,width: 150,sortable: false},
		{field: 'oneAgentName',displayName: '所属一级代理商名称',pinnable: false,width: 180,sortable: false},
		{field: 'oneAgentNo',displayName: '所属一级代理商编号',pinnable: false,width: 180,sortable: false},
	];
	$scope.activityGrid = {
		paginationPageSize:10,                  //分页数量
		paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
		useExternalPagination: true,		  //开启拓展名
		enableHorizontalScrollbar: true,        //横向滚动条
		enableVerticalScrollbar : true,  		//纵向滚动条
		columnDefs: $scope.columnDefs,
		onRegisterApi: function(gridApi) {
			$scope.gridApi = gridApi;
			gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
				$scope.paginationOptions.pageNo = newPage;
				$scope.paginationOptions.pageSize = pageSize;
				$scope.query();
			});
		}
	};

	$scope.changeAgentNode = function () {
		$scope.disabledMerchantType = !$scope.baseInfo.agentNode;
	};

	//导出
	$scope.exportExcel=function(){
		SweetAlert.swal({
				title: "确认导出？",
				showCancelButton: true,
				confirmButtonColor: "#DD6B55",
				confirmButtonText: "提交",
				cancelButtonText: "取消",
				closeOnConfirm: true,
				closeOnCancel: true },
			function (isConfirm) {
				if (isConfirm) {
					if($scope.activityGrid.data==null || $scope.activityGrid.data.length==0){
						$scope.notice("没有可导出的数据");
						return;
					} else {
						$scope.exportInfoClick("happyBackActivityMerchant/exportExcel.do",{"baseInfo" : angular.toJson($scope.baseInfo)});
					}
				}
			});
	};

	var result=false;
	//批量奖励入账
	$scope.merRewardAccountStatus=function () {
		var ids = "";
		var selectList=$scope.gridApi.selection.getSelectedRows();
		if(selectList==null||selectList.length==0){
			$scope.notice("请选择需要奖励入账的记录");
			return false;
		}
		if(selectList!=null&&selectList.length>0){
			for(var i=0;i<selectList.length;i++){
				var item=selectList[i];
				if(item.targetStatus==1&&item.rewardAccountStatus!=1){
					ids = ids + item.id + ",";
				}
			}
		}
		if(ids==""){
			$scope.notice("请选择需要奖励入账的记录");
			return false;
		}
		ids=ids.substring(0,ids.length-1);
		if(result){
			return ;
		}
		result=true;
		SweetAlert.swal({
				title: "确认批量奖励入账？",
				showCancelButton: true,
				confirmButtonColor: "#DD6B55",
				confirmButtonText: "提交",
				cancelButtonText: "取消",
				closeOnConfirm: true,
				closeOnCancel: true },
			function (isConfirm) {
				if (isConfirm) {
					$http.post('happyBackActivityMerchant/merRewardAccountStatus',"ids="+ids,
						{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
						.success(function(msg){
							$scope.notice(msg.msg);
							if(msg.status){
								$scope.resetForm();
								$scope.query();
							}
							result=false;
						}).error(function(){
						$scope.notice('系统异常');
						result=false;
					});
				}
			});
	}

	//页面绑定回车事件
	$document.bind("keypress", function(event) {
		$scope.$apply(function (){
			if(event.keyCode == 13){
				$scope.query();
			}
		})
	});
});