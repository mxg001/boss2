/**
 * 商户收益查询
 */
angular.module('inspinia',['infinity.angular-chosen']).controller('merchantProfitQueryCtrl',function($scope,$http,$state,$stateParams,i18nService,$document,SweetAlert,$timeout){
	//数据源
	i18nService.setCurrentLang('zh-cn');

	$scope.activityCodeSelect = [{text:"云闪付",value:'5'},{text:"抽奖",value:'8'}];
	$scope.agentList = [];
	$scope.paginationOptions = {pageNo : 1,pageSize : 10};
    $scope.merchantProfitCount=0;
	
	$scope.resetForm = function () {
		$scope.baseInfo = {sTime:moment(new Date().getTime()-6*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
				eTime:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
				agentNo:'',activityCode:''};
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
	    {field: 'id',displayName: '编号',width: 80,pinnable: false,sortable: false},
		{field: 'merchantName',displayName: '商户名称',width: 150,pinnable: false,sortable: false},
		{field: 'merchantNo',displayName: '商户编号',width: 150,pinnable: false,sortable: false},
		{field: 'mobilephone',displayName: '商户手机号',width: 150,pinnable: false,sortable: false},
		{field: 'agentName',displayName: '直属代理商',width: 150,pinnable: false,sortable: false},
		{field: 'oneAgentName',displayName: '一级代理商',width: 140,pinnable: false,sortable: false},
		{field: 'transAmount',displayName: '交易金额',cellFilter:"currency:''",width: 140,pinnable: false,sortable: false},
		{field: 'transFee',displayName: '交易手续费',cellFilter:"currency:''",width: 140,pinnable: false,sortable: false},
		{field: 'discountFee',displayName: '计算优惠后手续费',cellFilter:"currency:''",width: 150,pinnable: false,sortable: false},
		{field: 'merchantProfit',displayName: '收益金额',cellFilter:"currency:''",width: 140,pinnable: false,sortable: false},
		{field: 'activityCode',displayName: '活动类型',width: 140,pinnable: false,sortable: false,
			cellFilter:"formatDropping:"+ angular.toJson($scope.activityCodeSelect)},
		{field: 'orderNo',displayName: '交易订单',width: 200,pinnable: false,sortable: false},
		{field: 'createTime',displayName: '时间',width: 200,pinnable: false,sortable: false,
			cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'}
	];

	$scope.merchantProfitGrid = {
		paginationPageSize:10,					//分页数量
		paginationPageSizes: [10,20,50,100],	//切换每页记录数
		useExternalPagination: true,			//开启拓展名
		enableHorizontalScrollbar: true,		//横向滚动条
		enableVerticalScrollbar : true,			//纵向滚动条
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

	$scope.query = function () {
		if ($scope.loadImg) {
			return;
		}
		$scope.loadImg = true;
		$http({
			url: 'cloudPay/selectCloudPayByParam?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
			data: $scope.baseInfo,
			method:'POST'
		}).success(function (msg) {
			$scope.loadImg = false;
			if (!msg.status){
				$scope.notice(msg.msg);
				return;
			}
            $scope.merchantProfitCount=msg.merchantProfitCount;
			$scope.merchantProfitGrid.data = msg.page.result;
			$scope.merchantProfitGrid.totalItems = msg.page.totalCount;
		}).error(function (msg) {
			$scope.loadImg = false;
			$scope.notice('服务器异常,请稍后再试.');
		});
	};

	// 导出
	$scope.exportInfo = function (id) {
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
					location.href="cloudPay/exportInfo?baseInfo="+angular.toJson($scope.baseInfo);
				}
			});
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