/**
 * 分润查询
 */
angular.module('inspinia',['infinity.angular-chosen']).controller('shareQueryCtrl',function($scope,$http,$state,$stateParams,i18nService,$document,SweetAlert,$timeout){
	//数据源
	i18nService.setCurrentLang('zh-cn');

	$scope.paginationOptions = {pageNo : 1,pageSize : 10};
	$scope.orderTypeSelect = [{text:"会员服务费",value:'1'},{text:"其他",value:'2'}];
	$scope.enterStatusSelect = [{text:"未入账",value:'1'},{text:"已入账",value:'2'},{text:"入账失败",value:'3'}];

	var defatltSumAmount = {sumShareCash: "0.00", sumOrderCash: '0.00'};
	$scope.sumAmount = defatltSumAmount;

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
	//获取所有代理商-分润
	$scope.agentShare=[{value:"",text:"全部"}];
	$scope.getStatesShare = getStatesShare;
	var oldValueShare="";
	var timeoutShare="";
	function getStatesShare(value) {
		$scope.agenttt = [];
		var newValueShare=value;
		if(newValueShare != oldValueShare){
			if (timeoutShare) $timeout.cancel(timeoutShare);
			timeoutShare = $timeout(
				function(){
					$http.post('agentInfo/selectAllInfo','item=' + value,
							{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
						.then(function (response) {
							if(response.data.length==0) {
								$scope.agenttt.push({value: "", text: "全部"});
							}else{
								$scope.agenttt.push({value: "", text: "全部"});
								for(var i=0; i<response.data.length; i++){
									$scope.agenttt.push({value:response.data[i].agentNo,text:response.data[i].agentNo + " " + response.data[i].agentName});
								}
							}
							$scope.agentShare = $scope.agenttt;
							oldValueShare = value;
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
			$scope.agentShare.push({value:msg[i].agentNo, text:msg[i].agentNo + " " + msg[i].agentName});
		}
	});

	$scope.resetForm = function () {
		$scope.info = {sCreateDate:moment(new Date().getTime()-6*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
				eCreateDate:moment(new Date().getTime()).format('YYYY-MM-DD'+' 23:59:59'),
	            agentNode:"", contain:"1", shareAgentNo:""};
	}
	$scope.resetForm();

	$scope.columnDefs = [
		{field: 'createDate',displayName: '分润创建时间',width: 150, cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
		{field: 'shareCash',displayName: '分润金额',width: 150},
		{field: 'sharePercentage',displayName: '分润百分比',width: 150,cellTemplate:'<div class="lh30">{{row.entity.sharePercentage}}%</div>'},
		{field: 'enterStatus',displayName: '入账状态',width: 150, cellFilter:"formatDropping:" + angular.toJson($scope.enterStatusSelect)},
		{field: 'shareAgentName',displayName: '分润代理商名称',width: 160},
		{field: 'shareAgentNo',displayName: '分润代理商编号',width: 150},
		{field: 'relatedOrderNo',displayName: '关联订单号',width: 150},
		{field: 'orderCash',displayName: '订单金额',width: 150},
		{field: 'orderType',displayName: '订单类型',width: 150, cellFilter:"formatDropping:" + angular.toJson($scope.orderTypeSelect)},
		{field: 'userId',displayName: '用户ID',width: 150},
		{field: 'belongAgentNo',displayName: '所属代理商编号',width: 150},
		{field: 'belongAgentName',displayName: '所属代理商名称',width: 150},
		{field: 'enterDate',displayName: '入账时间',width: 150, cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'}
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
			url: 'cmShare/selectShareInfo?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
			data: $scope.info,
			method:'POST'
		}).success(function (msg) {
			$scope.loadImg = false;
			if (!msg.status){
				$scope.notice(msg.msg);
				$scope.sumAmount = defatltSumAmount;
				return;
			}
			$scope.myGrid.data = msg.page.result;
			$scope.myGrid.totalItems = msg.page.totalCount;
			$scope.sumAmount = msg.sumAmount;
		}).error(function (msg) {
			$scope.notice('服务器异常,请稍后再试.');
			$scope.loadImg = false;
		});
	};

	//导出
	$scope.exportInfo=function(){
		if($scope.info.sCreateDate == "" || $scope.info.eCreateDate == ""){
			$scope.notice("分润创建时间不能为空");
			return;
		}
		var stime = new Date($scope.info.sCreateDate).getTime();
		var etime = new Date($scope.info.eCreateDate).getTime();
		var sum = (etime - stime) / (24 * 60 * 60 * 1000);
		if(sum > 31){
			$scope.notice("分润创建时间的跨度不能超过一个月");
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
				location.href="cmShare/exportShareInfo?info="+encodeURI(angular.toJson($scope.info));
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