/**
 * 邀请有奖查询
 */
angular.module('inspinia',['infinity.angular-chosen']).controller('invitePrizesQueryCtrl',function($scope,$http,$state,$stateParams,i18nService,$document,SweetAlert,$timeout){
	//数据源
	i18nService.setCurrentLang('zh-cn');

	$scope.accountStatusSelect = [{text:"未入账",value:'0'},{text:"已入账",value:'1'},{text:"入账失败",value:'2'}];
	$scope.prizesTypeSelect = [{text:"商户",value:'1'},{text:"代理商",value:'2'}];
	$scope.agentList = [];
	$scope.paginationOptions = {pageNo : 1,pageSize : 10};
	var defatltCountInvitePrizesMerchant = {
		sumAmount: "0.00",
		unrecordedAmount: '0.00',
		recordedAmount: '0.00'
	};
	$scope.countInvitePrizesMerchant = defatltCountInvitePrizesMerchant;

	$scope.resetForm = function () {
		isVerifyTime = 1;
		$scope.baseInfo = {agentNode:'',containSub:'1',accountStatus:'',prizesType:'',prizesObject:'',
			startCreateTime:moment(new Date().getTime() - 24 * 3600 * 1000).format('YYYY-MM-DD'+' 00:00:00'),
			endCreateTime:moment(new Date().getTime()).format('YYYY-MM-DD'+' 23:59:59')};
	}
	$scope.resetForm();

	//获取所有代理商
	$scope.agent=[{value:"",text:"全部"}];
	$scope.getStates = getStates;
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
	//代理商
	$http.post("agentInfo/selectAllInfo")
		.success(function(msg){
			//响应成功
			for(var i=0; i<msg.length; i++){
				$scope.agent.push({value:msg[i].agentNode, text:msg[i].agentNo + " " + msg[i].agentName});
			}
		});

	$scope.columnDefs = [
	    {field: 'id',displayName: '编号',width: 80,pinnable: false,sortable: false},
		{field: 'merchantName',displayName: '邀请商户名称',width: 150,pinnable: false,sortable: false},
		{field: 'merchantNo',displayName: '邀请商户编号',width: 150,pinnable: false,sortable: false},
		{field: 'agentName',displayName: '所属代理商名称',width: 150,pinnable: false,sortable: false},
		{field: 'agentNo',displayName: '所属代理商编号',width: 140,pinnable: false,sortable: false},
		{field: 'prizesType',displayName: '奖励用户类型',width: 140,pinnable: false,sortable: false,
			cellFilter:"formatDropping:"+ angular.toJson($scope.prizesTypeSelect)},
		{field: 'prizesObject',displayName: '商户/一级代理商编号',width: 140,pinnable: false,sortable: false},
		{field: 'prizesAmount',displayName: '奖励金额',width: 120,pinnable: false,sortable: false},
		{field: 'createTime',displayName: '创建时间',width: 150,pinnable: false,sortable: false,
			cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
		{field: 'orderNo',displayName: '交易订单',width: 180,pinnable: false,sortable: false},
		{field: 'accountStatus',displayName: '入账状态',width: 120,pinnable: false,sortable: false,
			cellFilter:"formatDropping:"+ angular.toJson($scope.accountStatusSelect)},
		{field: 'accountTime',displayName: '入账时间',width: 150,pinnable: false,sortable: false,
			cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
		{field: 'realName',displayName: '操作人',width: 150,pinnable: false,sortable: false},
		{field: 'action',displayName: '操作',width: 120,pinnedRight:true,sortable: false,editable:true,cellTemplate:
			'<a class="lh30" ng-show="row.entity.accountStatus!=1 && grid.appScope.hasPermit(\'invitePrizes.recordAccount\')" '
			+ 'ng-click="grid.appScope.recorded(row.entity.id)">入账</a>'}
	];

	$scope.invitePrizesGrid = {
		paginationPageSize:10,                  //分页数量
		paginationPageSizes: [10,20,50,100],	//切换每页记录数
		useExternalPagination: true,		  //开启拓展名
		enableHorizontalScrollbar: true,        //横向滚动条
		enableVerticalScrollbar : true,  		//纵向滚动条
//		rowHeight:35,
		columnDefs: $scope.columnDefs,
		onRegisterApi: function(gridApi) {
			$scope.gridApi = gridApi;
			$scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
				$scope.paginationOptions.pageNo = newPage;
				$scope.paginationOptions.pageSize = pageSize;
				$scope.query();
			});
		}
	};

	//是否校验时间
	isVerifyTime = 1;//校验：1，不校验：0

	keyChange=function(){
		if ($scope.baseInfo.merchantNo||$scope.baseInfo.prizesObject) {
			isVerifyTime = 0;
		} else {
			isVerifyTime = 1;
		}
	}

	setBeginTime=function(setTime){
		$scope.baseInfo.startCreateTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
	}
	setEndTime=function(setTime){
		$scope.baseInfo.endCreateTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
	}

	$scope.query = function () {
		if ($scope.loadImg) {
			return;
		}
		if (!$scope.baseInfo.merchantNo&&!$scope.baseInfo.prizesObject) {
			if(!($scope.baseInfo.startCreateTime && $scope.baseInfo.endCreateTime)){
				$scope.notice("创建时间不能为空");
				return;
			}
			var stime = new Date($scope.baseInfo.startCreateTime).getTime();
			var etime = new Date($scope.baseInfo.endCreateTime).getTime();
			if ((etime - stime) > (31 * 24 * 60 * 60 * 1000)) {
				$scope.notice("创建时间范围不能超过31天");
				return;
			}
		}
		$scope.loadImg = true;
		$http({
			url: 'invitePrizes/selectInvitePrizesByParam?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
			data: $scope.baseInfo,
			method:'POST'
		}).success(function (msg) {
			$scope.loadImg = false;
			if (!msg.status){
				$scope.notice(msg.msg);
				return;
			}
			$scope.invitePrizesGrid.data = msg.page.result;
			$scope.invitePrizesGrid.totalItems = msg.page.totalCount;
		}).error(function (msg) {
			$scope.loadImg = false;
			$scope.notice('服务器异常,请稍后再试.');
		});
		$http({
			url: 'invitePrizes/countInvitePrizesMerchant',
			data: $scope.baseInfo,
			method:'POST'
		}).success(function (msg) {
			if (!msg.status){
				$scope.countInvitePrizesMerchant = defatltCountInvitePrizesMerchant;
				return;
			}
			$scope.countInvitePrizesMerchant =  msg.result;
		}).error(function (msg) {
			$scope.notice('服务器异常,请稍后再试.');
		});
	};

	// 导出
	$scope.exportInfo = function (id) {
		if (!$scope.baseInfo.merchantNo) {
			if(!($scope.baseInfo.startCreateTime && $scope.baseInfo.endCreateTime)){
				$scope.notice("创建时间不能为空");
				return;
			}
			var stime = new Date($scope.baseInfo.startCreateTime).getTime();
			var etime = new Date($scope.baseInfo.endCreateTime).getTime();
			if ((etime - stime) > (31 * 24 * 60 * 60 * 1000)) {
				$scope.notice("创建时间范围不能超过31天");
				return;
			}
		}
		SweetAlert.swal({
			title: "确定导出吗？",
			type: "warning",
			showCancelButton: true,
			confirmButtonColor: "#DD6B55",
			confirmButtonText: "确定",
			cancelButtonText: "取消",
			closeOnConfirm: true,
			closeOnCancel: true },
			function (isConfirm) {
				if (isConfirm) {
					location.href="invitePrizes/exportInfo?info="+angular.toJson($scope.baseInfo);
				}
			});
	};

	// 入账
	$scope.recorded = function (id) {
		SweetAlert.swal({
			title: "确定入账吗？",
			type: "warning",
			showCancelButton: true,
			confirmButtonColor: "#DD6B55",
			confirmButtonText: "确定",
			cancelButtonText: "取消",
			closeOnConfirm: true,
			closeOnCancel: true },
			function (isConfirm) {
				if (isConfirm) {
					$http({
						url: 'invitePrizes/recordAccount',
						data: id,
						method:'POST'
					}).success(function (msg) {
						$scope.notice(msg.msg);
						if (msg.status){
							$scope.query();
						}
					}).error(function (msg) {
						$scope.notice('服务器异常,请稍后再试.');
					});
				}
			});
	};

	// 批量入账
	$scope.batchRecorded = function (id) {
		selectedRows = $scope.gridApi.selection.getSelectedRows();
		if(selectedRows==null || selectedRows.length==0){
			$scope.notice('请选择要入账的条目');
			return;
    	}
		validIds = [];
		angular.forEach(selectedRows,function(data){
			if (data.accountStatus != '1') {
				validIds[validIds.length] = data.id;
			}
    	});
		if (validIds == null || validIds.length == 0) {
			$scope.notice('请选择可入账的条目');
			return;
		}
		SweetAlert.swal({
			title: "批量入账",
			text: "满足入账条件的数据有 " + validIds.length + " 条，是否确定入账？",
			type: "warning",
			showCancelButton: true,
			confirmButtonColor: "#DD6B55",
			confirmButtonText: "确定",
			cancelButtonText: "取消",
			closeOnConfirm: true,
			closeOnCancel: true },
			function (isConfirm) {
				if (isConfirm) {
					$http({
						url: 'invitePrizes/batchRecordAccount',
						data: validIds,
						method:'POST'
					}).success(function (msg) {
						$scope.notice(msg.msg);
						if (msg.status){
							$scope.query();
						}
					}).error(function (msg) {
						$scope.notice('服务器异常,请稍后再试.');
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