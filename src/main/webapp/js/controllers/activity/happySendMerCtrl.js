/**
 * 新欢乐送商户奖励
 */
angular.module('inspinia',['angularFileUpload','infinity.angular-chosen']).controller('happySendMerCtrl',function($scope,$http,$state,$stateParams,i18nService,$document,SweetAlert,FileUploader,$timeout){
	//数据源
	i18nService.setCurrentLang('zh-cn');
	$scope.paginationOptions=angular.copy($scope.paginationOptions);

	$scope.resetForm=function(){
		$scope.baseInfo = {
			activeOrder:"",activityTargetStatus:"",rewardAccountStatus:"",
			merchantNo:"",
			agentGrade:"",
            agentN:"",
            minRewardAccountTime:"",
            maxRewardAccountTime:"",
			minTargetTime:"",
            maxTargetTime:"",
            minRewardEndTime:"",
            maxRewardEndTime:"",
            minActiveTime:"",
            maxActiveTime:""};

	}
	$scope.resetForm();
	//活动类型
    $scope.activityTargetStatuss = [{text:'全部',value:''},{text:'考核中',value:'0'},{text:'已达标',value:'1'},{text:'未达标',value:'2'}];
	$scope.rewardAccountStatuss = [{text:'全部',value:''},{text:'未入账',value:'0'},{text:'已入账',value:'1'}];
	$scope.agentGrades = [{text:'包含所有',value:''},{text:'不包含',value:'1'},{text:'仅包含直属',value:'2'}];
	$scope.totalData = {
        totalRewardAmount: '0.00',
        totalNotRewardAmount: '0.00'
	};
	$scope.accountCheckTotal = 0;
	$scope.liquidationTotal = 0;
	$scope.rewardIsBookedTotal = 0;


	//当前页
	$scope.pageAllClick=function () {
		if($scope.baseInfo.pageAll){
			$scope.baseInfo.countAll=false;
		}else{
			$scope.baseInfo.countAll=true;
		}
		$scope.gridApi.selection.clearSelectedRows();
	}
	//所有页
	$scope.countAllClick=function(){
		if($scope.baseInfo.countAll){
			$scope.baseInfo.pageAll=false;
		}else{
			$scope.baseInfo.pageAll=true;
		}
		$scope.gridApi.selection.clearSelectedRows();

	}



	//代理商
	$http.post("agentInfo/selectAllInfo")
		.success(function(msg){
			//响应成功
			for(var i=0; i<msg.length; i++){
				$scope.agent.push({value:msg[i].agentNode,text:msg[i].agentNo + " " + msg[i].agentName});
				$scope.oneAgent.push({value:msg[i].agentNo,text:msg[i].agentNo + " " + msg[i].agentName});
			}
		});
	//动态代理商
	$scope.agent=[{value:"",text:"全部"}];
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
					$http.post('agentInfo/selectAllInfo','item=' + value,
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

	//是否校验时间
	isVerifyTime = 1;//校验：1，不校验：0

	keyChange=function(){
		if ($scope.baseInfo.activeOrder || $scope.baseInfo.merchantN
			|| ($(d5666).val() && $(d5667).val())) {
			isVerifyTime = 0;
		} else {
			isVerifyTime = 1;
		}
	}

	setBeginTime=function(setTime){
		$scope.baseInfo.activeTimeStart = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
	}
	setEndTime=function(setTime){
		$scope.baseInfo.activeTimeEnd = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
	}




	//查询
	$scope.query=function(){
		/*if (!($scope.baseInfo.activeOrder || $scope.baseInfo.merchantN
			|| ($(d5666).val() && $(d5667).val()))) {
			if(!($scope.baseInfo.enterTimeStart && $scope.baseInfo.enterTimeEnd)){
				if(!($scope.baseInfo.activeTimeStart && $scope.baseInfo.activeTimeEnd)){
					$scope.notice("激活日期不能为空");
					return;
				}

			}

			if(($scope.baseInfo.activeTimeStart && $scope.baseInfo.activeTimeEnd)){
				var stime = new Date($scope.baseInfo.activeTimeStart).getTime();
				var etime = new Date($scope.baseInfo.activeTimeEnd).getTime();
				if ((etime - stime) > (365 * 24 * 60 * 60 * 1000)) {
					$scope.notice("激活日期范围不能超过365天");
					return;
				}
			}

			if(($scope.baseInfo.enterTimeStart && $scope.baseInfo.enterTimeEnd)){
				var stime = new Date($scope.baseInfo.enterTimeStart).getTime();
				var etime = new Date($scope.baseInfo.enterTimeEnd).getTime();
				if ((etime - stime) > (31 * 24 * 60 * 60 * 1000)) {
					$scope.notice("进件日期范围不能超过31天");
					return;
				}
			}
		}*/
		$scope.baseInfo.checkIds=null;
		$scope.loadImg = true;
		$scope.submitting = true;
		$http.post('activityDetail/selectHappySendOrderDetail.do',"baseInfo="+angular.toJson($scope.baseInfo)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+
			$scope.paginationOptions.pageSize,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			.success(function(msg){
				$scope.loadImg = false;
				$scope.submitting = false;
				if(!msg)
					return;
				if(msg.status){
					$scope.activityGrid.data = msg.page.result;
					$scope.activityGrid.totalItems = msg.page.totalCount;
					$scope.totalData = msg.totalData;

					$scope.accountCheckTotal = msg.accountCheckTotal;
					$scope.liquidationTotal = msg.liquidationTotal;
					$scope.rewardIsBookedTotal = msg.rewardIsBookedTotal;
				}
			}).error(function(){
			$scope.notice("系统异常");
			$scope.loadImg = false;
			$scope.submitting = false;
		})
	}
	$scope.query();
	$scope.activityGrid = {
		paginationPageSize:10,                  //分页数量
		paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
		useExternalPagination: true,		  //开启拓展名
		enableHorizontalScrollbar: true,        //横向滚动条
		enableVerticalScrollbar : true,  		//纵向滚动条
		columnDefs: [
			{field: 'id',displayName: '序号',pinnable: false,width: 180,sortable: false},
			{field: 'activeOrder',displayName: '激活订单号',pinnable: false,width: 180,sortable: false},
			{field: 'activeTime',displayName: '激活日期',pinnable: false,width: 180,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
			{field: 'targetAmount',displayName: '达标条件(元)',headerTooltip: '累计交易金额仅统计激活后商户的银行卡交易(刷卡、闪付等)',pinnable: false,width: 180,sortable: false},
			{field: 'rewardAmount',displayName: '奖励金额(元)',pinnable: false,width: 180,sortable: false},
			{field: 'activityTargetStatus',displayName: '达标状态',pinnable: false,width: 180,sortable: false,cellFilter:"formatDropping:"+ angular.toJson($scope.activityTargetStatuss)},
			{field: 'activityTargetTime',displayName: '达标日期',pinnable: false,width: 180,sortable: false,cellFilter: "date:'yyyy-MM-dd HH:mm:ss'"},
			{field: 'rewardEndTime',displayName: '活动截止日期',pinnable: false,width: 180,sortable: false,cellFilter: "date:'yyyy-MM-dd HH:mm:ss'"},
            {field: 'rewardAccountStatus',displayName: '奖励入账状态',pinnable: false,width: 180,sortable: false,cellFilter:"formatDropping:[{text:'已入账',value:'1'},{text:'未入账',value:'0'}]"},
            {field: 'rewardAccountTime',displayName: '入账日期',pinnable: false,width: 180,sortable: false,cellFilter: "date:'yyyy-MM-dd HH:mm:ss'"},

			{field: 'activityTypeNo',displayName: '欢乐返子类型编号',pinnable: false,width: 150,sortable: false},
			{field: 'merchantNo',displayName: '商户编号',pinnable: false,width: 150,sortable: false},
			{field: 'teamName',displayName: '所属组织',pinnable: false,width: 150,sortable: false},
			{field: 'teamEntryName',displayName: '所属子组织',pinnable: false,width: 150,sortable: false},
			{field: 'hardId',displayName: '硬件产品ID',pinnable: false,width: 150,sortable: false},

            {field: 'agentName',displayName: '所属代理商名称',pinnable: false,width: 180,sortable: false},
            {field: 'agentNo',displayName: '所属代理商编号',pinnable: false,width: 180,sortable: false},
            {field: 'oneAgentName',displayName: '一级代理商名称',pinnable: false,width: 180,sortable: false},
            {field: 'oneAgentNo',displayName: '一级代理商编号',pinnable: false,width: 180,sortable: false}
		],
		onRegisterApi: function(gridApi) {
			$scope.gridApi = gridApi;
			$scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
				$scope.paginationOptions.pageNo = newPage;
				$scope.paginationOptions.pageSize = pageSize;
				$scope.query();
			});
			//行选中事件
			$scope.gridApi.selection.on.rowSelectionChanged($scope,function(row,event){
				if(row.isSelected){
					$scope.baseInfo.pageAll=false;
					$scope.baseInfo.countAll=false;
				}
			});
			//全选事件
			$scope.gridApi.selection.on.rowSelectionChangedBatch($scope,function(row,event){
				if(row){
					if(row[0].isSelected){
						$scope.baseInfo.pageAll=false;
						$scope.baseInfo.countAll=false;
					}
				}
			});
		}
	};

	$scope.rewardResulGrid = {
		data: 'rewardResultList',
		enableVerticalScrollbar : true,  		//纵向滚动条
		columnDefs: [
			{field: 'activeOrder',displayName: '激活订单号',width:250},
			{field: 'billingStatusText',displayName: '入账状态',width:250},
			{field: 'billingMsg',displayName: '入账信息',width:250}
		]
	};

	var result=false;
	//批量奖励入账
	$scope.rewardIsBooked=function () {
		var ids = "";
			var selectList=$scope.gridApi.selection.getSelectedRows();
			if(selectList==null||selectList.length==0){
				$scope.notice("请选择需要奖励入账的记录");
				return false;
			}
			if(selectList!=null&&selectList.length>0){
				for(var i=0;i<selectList.length;i++){
					var item=selectList[i];
					if(item.rewardAccountStatus==0 && item.activityTargetStatus==1){
						ids = ids + item.id + ",";
					}
				}
			}
			if(ids==""){
				$scope.notice("没有符合入账的记录");
				return false;
			}
			$scope.baseInfo.checkIds=ids.substring(0,ids.length-1);

		if(result){
			return;
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
					$http.post('activityDetail/happySendNewMerRewardIsBooked',"baseInfo="+angular.toJson($scope.baseInfo) + "&pageNo=" + $scope.paginationOptions.pageNo + "&pageSize="+$scope.paginationOptions.pageSize,
						{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
						.success(function(msg){
							$scope.notice(msg.msg);
							if(msg.status){
								$scope.cancel();
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





	$scope.cancel = function(){
		$scope.baseInfo.checkIds=null;
		$scope.liquidationStatus = 1;
		$scope.accountCheckStatus = 1;
		$('#liquidationModal').modal('hide');
		$('#accountCheckModal').modal('hide');
		$('#rewardResultModel').modal('hide');
	}

	$scope.reward_cancel = function(){
		$scope.baseInfo.checkIds=null;
		$scope.liquidationStatus = 1;
		$scope.accountCheckStatus = 1;
		$('#liquidationModal').modal('hide');
		$('#accountCheckModal').modal('hide');
		$('#rewardResultModel').modal('hide');
		$scope.query();
	}

	//导出
	$scope.exportExcel=function(){

        SweetAlert.swal({
                title: "确认导出数据？",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true
            },
            function (isConfirm) {
                if (isConfirm) {
                    // location.href = "activityDetail/exportHappySendOrder.do?baseInfo=" + encodeURI(angular.toJson($scope.baseInfo)) + "&pageNo=" + $scope.paginationOptions.pageNo + "&pageSize=" + +$scope.paginationOptions.pageSize;
                    $scope.exportInfoClick("activityDetail/exportHappySendOrder.do",{"baseInfo":angular.toJson($scope.baseInfo)})
                }
            });



		/*if (!($scope.baseInfo.activeOrder || $scope.baseInfo.merchantN
			|| ($(d5666).val() && $(d5667).val()))) {
			if(!($scope.baseInfo.activeTimeStart && $scope.baseInfo.activeTimeEnd)){
				$scope.notice("激活日期不能为空");
				return;
			}
			var stime = new Date($scope.baseInfo.activeTimeStart).getTime();
			var etime = new Date($scope.baseInfo.activeTimeEnd).getTime();
			if ((etime - stime) > (365 * 24 * 60 * 60 * 1000)) {
				$scope.notice("激活日期范围不能超过365天");
				return;
			}
		}*/
		/*if($scope.baseInfo.countAll||$scope.baseInfo.pageAll){
			//当前页
			if($scope.baseInfo.pageAll){
				$scope.baseInfo.checkIds=null;
				SweetAlert.swal({
						title: "确认导出当前页所有的数据？",
						showCancelButton: true,
						confirmButtonColor: "#DD6B55",
						confirmButtonText: "提交",
						cancelButtonText: "取消",
						closeOnConfirm: true,
						closeOnCancel: true
					},
					function (isConfirm) {
						if (isConfirm) {
							location.href = "activityDetail/exportHappySendOrder.do?baseInfo=" + encodeURI(angular.toJson($scope.baseInfo)) + "&pageNo=" + $scope.paginationOptions.pageNo + "&pageSize=" + +$scope.paginationOptions.pageSize;
						}
					});
			}
			//全部数据
			if($scope.baseInfo.countAll){
				$scope.baseInfo.checkIds=null;
				SweetAlert.swal({
						title: "确认导出所有的数据？",
						showCancelButton: true,
						confirmButtonColor: "#DD6B55",
						confirmButtonText: "提交",
						cancelButtonText: "取消",
						closeOnConfirm: true,
						closeOnCancel: true
					},
					function (isConfirm) {
						if (isConfirm) {
							location.href = "activityDetail/exportHappySendOrder.do?baseInfo=" + encodeURI(angular.toJson($scope.baseInfo)) + "&pageNo=" + $scope.paginationOptions.pageNo + "&pageSize=" + +$scope.paginationOptions.pageSize;
						}
					});
			}
		}else {
			var selectList = $scope.gridApi.selection.getSelectedRows();
			if(selectList!=null&&selectList.length>0){
				var ids = "";
				for(var i=0;i<selectList.length;i++){
					if(i<selectList.length-1){
						ids = ids + selectList[i].id + ",";
					}else{
						ids = ids + selectList[i].id;
					}
				}
				$scope.baseInfo.checkIds=ids;
			}
			SweetAlert.swal({
					title: "确认导出选中的数据？",
					showCancelButton: true,
					confirmButtonColor: "#DD6B55",
					confirmButtonText: "提交",
					cancelButtonText: "取消",
					closeOnConfirm: true,
					closeOnCancel: true
				},
				function (isConfirm) {
					if (isConfirm) {
						location.href = "activityDetail/exportHappySendOrder.do?baseInfo=" + encodeURI(angular.toJson($scope.baseInfo)) + "&pageNo=" + $scope.paginationOptions.pageNo + "&pageSize=" + +$scope.paginationOptions.pageSize;
					}
				});
		}*/
	};

	//代理商名称/编号  改变时调用
	$scope.changeAgentNode = function () {
		$scope.disabledMerchantType = !$scope.baseInfo.agentN;
	};

	//条件显示问题
	$scope.mtxt="全部条件";
	$scope.visible= false;
	$scope.toggle = function(){
		if($scope.visible == false){
			$scope.mtxt="收起";
			$scope.visible=true;
		}else{
			$scope.mtxt="全部条件";
			$scope.visible=false;
		}
	};
	//页面绑定回车事件
	$document.bind("keypress", function(event) {
		$scope.$apply(function (){
			if(event.keyCode == 13){
				$scope.query();
			}
		})
	});

});