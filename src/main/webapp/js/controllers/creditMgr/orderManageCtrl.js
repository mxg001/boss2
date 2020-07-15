/**
 * 订单管理
 */
angular.module('inspinia',['infinity.angular-chosen']).controller('orderManageCtrl',function($scope,$http,$state,$stateParams,i18nService,$document,SweetAlert,$timeout){
	//数据源
	i18nService.setCurrentLang('zh-cn');

	$scope.paginationOptions = {pageNo : 1,pageSize : 10};
	$scope.transTypeSelect = [{text:"支付宝",value:'alipay'},{text:"微信",value:'weixin'}];
	$scope.transStatusSelect = [{text:"待付款",value:'WAIT'},{text:"已付款",value:'SUCCESS'},{text:"已关闭",value:'CLOSED'}];

	$scope.sumAmount = "0.00";
	
	$scope.resetForm = function () {
		$scope.info = {sExpireTime:moment(new Date().getTime()-6*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
				eExpireTime:moment(new Date().getTime()).format('YYYY-MM-DD'+' 23:59:59'),
				orgId:"", agentNo:""};
	}
	$scope.resetForm();

	//获取所有组织
	$scope.org=[{value:"",text:"全部"}];
	$http.post("cmUserManage/selectOrgAllInfo")
		.success(function(msg){
			//响应成功
			for(var i=0; i<msg.length; i++){
				$scope.org.push({value:msg[i].orgId, text:msg[i].orgId + " " + msg[i].orgName});
			}
		});

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
										$scope.agentt.push({value:response.data[i].agentNo,text:response.data[i].agentNo + " " + response.data[i].agentName});
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
				$scope.agent.push({value:msg[i].agentNo, text:msg[i].agentNo + " " + msg[i].agentName});
			}
		});

	$scope.columnDefs = [
		{field: 'tradeNo',displayName: '订单ID',width: 180},
		{field: 'orgName',displayName: '组织名称',width: 150},
		{field: 'orgId',displayName: '组织编号',width: 150},
		{field: 'userNo',displayName: '用户ID',width: 150},
		{field: 'transSubject',displayName: '订单类型',width: 150},
		{field: 'transStatus',displayName: '订单状态',width: 150, cellFilter:"formatDropping:"+ angular.toJson($scope.transStatusSelect)},
		{field: 'transAmount',displayName: '订单金额',width: 150},
		{field: 'transType',displayName: '支付方式',width: 150, cellFilter:"formatDropping:"+ angular.toJson($scope.transTypeSelect)},
		{field: 'thirdTradeNo',displayName: '关联支付单号',width: 150},
		{field: 'createTime',displayName: '创建时间',width: 150, cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
		{field: 'paymentTime',displayName: '付款时间',width: 150, cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
		{field: 'expireTime',displayName: '会员到期时间',width: 150, cellFilter:'date:"yyyy-MM-dd"'},
		{field: 'action',displayName: '操作',width: 150,pinnedRight:true,editable:true,cellTemplate:
			'<a class="lh30" ui-sref="creditMgr.orderDetail({tradeNo:row.entity.tradeNo})" target="_black">详情</a>'}
	];

	$scope.myGrid = {
		paginationPageSize:10,                  //分页数量
		paginationPageSizes: [10,20,50,100],	//切换每页记录数
		useExternalPagination: true,		  //开启拓展名
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

	$scope.query = function () {
		if ($scope.loadImg) {
			return;
		}
		$scope.loadImg = true;
		$http({
			url: 'cmOrder/selectOrderInfo?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
			data: $scope.info,
			method:'POST'
		}).success(function (data) {
			$scope.loadImg = false;
			if (!data.status){
				$scope.notice(data.msg);
				$scope.sumAmount = "0.00";
				return;
			}
			$scope.myGrid.data = data.page.result;
			$scope.myGrid.totalItems = data.page.totalCount;
			$scope.sumAmount = data.sumAmount;
		}).error(function (msg) {
			$scope.notice('服务器异常,请稍后再试.');
			$scope.loadImg = false;
		});
	};

	//导出
	$scope.exportInfo=function(){
		if($scope.info.sExpireTime == "" || $scope.info.eExpireTime == ""){
			$scope.notice("会员过期时间不能为空");
			return;
		}
		var stime = new Date($scope.info.sExpireTime).getTime();
		var etime = new Date($scope.info.eExpireTime).getTime();
		var sum = (etime - stime) / (24 * 60 * 60 * 1000);
		if(sum > 31){
			$scope.notice("会员过期时间的跨度不能超过一个月");
			return;
		}
		SweetAlert.swal({
			title: "确认导出？",
			showCancelButton: true,
			confirmButtonColor: "#DD6B55",
			confirmButtonText: "确定",
			cancelButtonText: "取消",
			closeOnConfirm: true,
			closeOnCancel: true 
		},
		function (isConfirm) {
			if (isConfirm) {
				location.href="cmOrder/exportOrderInfo?info="+encodeURI(angular.toJson($scope.info));
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