/**
 * 欢乐返代理商奖励查询
 */
angular.module('inspinia',['infinity.angular-chosen']).controller('happyBackActivityAgentCtrl',function($scope,$http,$state,$stateParams,i18nService,$document,SweetAlert,$timeout){
	//数据源
	i18nService.setCurrentLang('zh-cn');
	$scope.disabledMerchantType = true;
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	$scope.baseInfo = {containsLower:"",scanTargetStatus:"",scanAccountStatus:"",allTargetStatus:"",allAccountStatus:"",merchantNo:"",agentNo:"",oneAgentNo:""};
	$scope.resetForm=function(){
        $scope.baseInfo = {containsLower:"",scanTargetStatus:"",scanAccountStatus:"",allTargetStatus:"",allAccountStatus:"",merchantNo:"",agentNo:"",oneAgentNo:""};
    };
	//扫码交易满奖达标状态
	$scope.scanTargetStatusList = [{text:'全部',value:''},{text:'考核中',value:'1'},{text:'未达标',value:'3'},{text:'已达标',value:'2'}];
	//扫码交易满奖入账状态
	$scope.scanRewardAccountStatusList = [{text:'全部',value:''},{text:'未入账',value:'0'},{text:'已入账',value:'1'}];
    //全部交易满奖达标状态
    $scope.allTargetStatusList = [{text:'全部',value:''},{text:'考核中',value:'1'},{text:'未达标',value:'3'},{text:'已达标',value:'2'}];
    //全部交易满奖入账状态
    $scope.allRewardAccountStatusList = [{text:'全部',value:''},{text:'未入账',value:'0'},{text:'已入账',value:'1'}];
	$scope.agent=[{value:"",text:"全部"}];
    $scope.agentGrades = [{text:'包含所有',value:''},{text:'仅包含直属',value:'2'},{text:'不包含',value:'1'}];
    //代理商
	$http.post("agentInfo/selectAllInfo")
		.success(function(msg){
			//响应成功
			for(var i=0; i<msg.length; i++){
				$scope.agent.push({value:msg[i].agentNode,text:msg[i].agentNo + " " + msg[i].agentName});
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
									$scope.agentt.push({value:response.data[i].agentNode,text:response.data[i].agentNo + " " + response.data[i].agentName});
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
		$http.post('happyBackActivityAgent/selectHappyBackActivityAgent',"baseInfo="+angular.toJson($scope.baseInfo)
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
		$http.post('happyBackActivityAgent/countMoney',"baseInfo="+angular.toJson($scope.baseInfo)
			,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			.success(function(data){
				$scope.rewardAmount = data.scanRewardAmount + data.allRewardAmount;//奖励已入账
				$scope.noRewardAmount = data.scanNoRewardAmount + data.allNoRewardAmount;//奖励未入账
				})
	};
	$scope.query();
	$scope.columnDefs = [
		{field: 'activeOrder',displayName: '激活订单号',pinnable: false,width: 180,sortable: false},
		{field: 'activeTime',displayName: '激活日期',pinnable: false,width: 180,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
		{field: 'scanTargetAmount',displayName: '扫码交易满奖达标条件',pinnable: false,width: 240,sortable: false,cellTemplate:
                '<div ng-show="row.entity.scanActivityDays!=null&&row.entity.scanActivityDays!=0" style="padding: 8px 5px;">激活后{{row.entity.scanActivityDays==null?0:row.entity.scanActivityDays}}天内，累计交易≥{{row.entity.scanTargetAmount==null?0:row.entity.scanTargetAmount}}元</div>'
        },
		{field: 'scanRewardAmount',displayName: '扫码交易满奖金额(元)',pinnable: false,width: 150,sortable: false,cellTemplate:
                '<div ng-show="row.entity.scanActivityDays!=null&&row.entity.scanActivityDays!=0&&row.entity.scanRewardAmount!=null" style="padding: 8px 5px;">{{row.entity.scanRewardAmount}}</div>'
        },
		{field: 'scanTargetStatus',displayName: '扫码交易满奖达标状态',pinnable: false,width: 150,sortable: false,cellFilter:"formatDropping:"+ angular.toJson($scope.scanTargetStatusList)},
		{field: 'scanTargetTime',displayName: '扫码交易满奖达标日期',pinnable: false,width: 180,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
		{field: 'scanRewardEndTime',displayName: '扫码交易满奖活动截止日期',pinnable: false,width: 180,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
		{field: 'scanAccountStatus',displayName: '扫码交易满奖入账状态',pinnable: false,width: 150,sortable: false,cellFilter:"formatDropping:"+ angular.toJson($scope.scanRewardAccountStatusList)},
		{field: 'scanAccountTime',displayName: '扫码交易满奖入账日期',pinnable: false,width: 180,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'allTargetAmount',displayName: '全部交易满奖达标条件',pinnable: false,width: 240,sortable: false,cellTemplate:
                '<div ng-show="row.entity.allActivityDays!=null&&row.entity.allActivityDays!=0" style="padding: 8px 5px;">激活后{{row.entity.allActivityDays==null?0:row.entity.allActivityDays}}天内，累计交易≥{{row.entity.allTargetAmount==null?0:row.entity.allTargetAmount}}元</div>'
        },
        {field: 'allRewardAmount',displayName: '全部交易满奖金额(元)',pinnable: false,width: 150,sortable: false,cellTemplate:
                '<div ng-show="row.entity.allActivityDays!=null&&row.entity.allActivityDays!=0&&row.entity.allRewardAmount!=null" style="padding: 8px 5px;">{{row.entity.allRewardAmount}}</div>'
        },
        {field: 'allTargetStatus',displayName: '全部交易满奖达标状态',pinnable: false,width: 150,sortable: false,cellFilter:"formatDropping:"+ angular.toJson($scope.allTargetStatusList)},
        {field: 'allTargetTime',displayName: '全部交易满奖达标日期',pinnable: false,width: 180,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'allRewardEndTime',displayName: '全部交易满奖活动截止日期',pinnable: false,width: 180,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'allAccountStatus',displayName: '全部交易满奖入账状态',pinnable: false,width: 150,sortable: false,cellFilter:"formatDropping:"+ angular.toJson($scope.scanRewardAccountStatusList)},
        {field: 'allAccountTime',displayName: '全部交易满奖入账日期',pinnable: false,width: 180,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
		{field: 'activityTypeNo',displayName: '欢乐返子类型编号',pinnable: false,width: 150,sortable: false},
		{field: 'merchantNo',displayName: '所属商户编号',pinnable: false,width: 150,sortable: false},
		{field: 'teamName',displayName: '所属组织',pinnable: false,width: 150,sortable: false},
		{field: 'teamEntryName',displayName: '所属子组织',pinnable: false,width: 150,sortable: false},
		{field: 'hardId',displayName: '硬件产品ID',pinnable: false,width: 150,sortable: false},
		{field: 'agentName',displayName: '所属代理商名称',pinnable: false,width: 150,sortable: false},
		{field: 'agentNo',displayName: '所属代理商编号',pinnable: false,width: 150,sortable: false},
		{field: 'oneAgentName',displayName: '一级代理商名称',pinnable: false,width: 180,sortable: false},
		{field: 'oneAgentNo',displayName: '一级代理商编号',pinnable: false,width: 180,sortable: false},
        {field: 'action',displayName: '操作',width: 150,pinnable:false,sortable: false,pinnedRight:true,cellTemplate:
            '<div class="lh30">'
            +'<a class="lh30" '
            +'ng-show="grid.appScope.hasPermit(\'happyBackActivityAgent.hlfAgentAwardDetail\')"'
            +'ng-click="grid.appScope.agentAwardDetail(row.entity.id)" target="_black">代理商明细</a>'
            +'</div>'
        }
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

    $scope.agentAwardDetail=function(id){
        $http.post("happyBackActivityAgent/agentAwardDetail",
            "id="+id,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(result){
                if (result.status) {
                    // 批量更改信息展示
                    $("#agentAwardDetailModel").modal("show");
                    $scope.agentAwardDetailList=result.data;
                }else {
                    $scope.notice(result.msg);
                }
            })

    }

    $scope.agentAwardDetailGrid = {
        data: 'agentAwardDetailList',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],      //切换每页记录数
        useExternalPagination: true,            //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,          //纵向滚动条
        columnDefs: [
            {field: 'agentName',displayName: '代理商名称',width:180},
            {field: 'agentNo',displayName: '代理商编号',width:180},
            {field: 'agentLevel',displayName: '代理商级别',width:180},
            {field: 'scanAmount',displayName: '扫码交易满奖金额',width:180},
            {field: 'scanAccountStatus',displayName: '扫码交易满奖入账状态',width:180,cellFilter:"formatDropping:"+ angular.toJson($scope.scanRewardAccountStatusList)},
            {field: 'scanAccountTime',displayName: '扫码交易满奖入账日期',width:180,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
            {field: 'scanRemark',displayName: '扫码入账备注',width:300},
			{field: 'allAmount',displayName: '全部交易满奖金额',width:180},
            {field: 'allAccountStatus',displayName: '全部交易满奖入账状态',width:180,cellFilter:"formatDropping:"+ angular.toJson($scope.allRewardAccountStatusList)},
            {field: 'allAccountTime',displayName: '全部交易满奖入账日期',width:180,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
            {field: 'allRemark',displayName: '全部入账备注',width:300}
        ]

    };

    $scope.closeAgentAwardDetail=function(){
        $("#agentAwardDetailModel").modal("hide");
    }

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
						$scope.exportInfoClick("happyBackActivityAgent/exportExcel.do",{"baseInfo" : angular.toJson($scope.baseInfo)});
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
				if((item.scanTargetStatus==2&&item.scanAccountStatus!=1)||
                    (item.allTargetStatus==2&&item.allAccountStatus!=1)){
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
					$http.post('happyBackActivityAgent/agentRewardAccountStatus',"ids="+ids,
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