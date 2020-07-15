/**
 * 服务商分润订单查询
 */
angular.module('inspinia',['infinity.angular-chosen']).controller('providerShareCtrl',function($scope,$http,$state,$stateParams,i18nService,$document,SweetAlert,$timeout){
	//数据源
	i18nService.setCurrentLang('zh-cn');

	$scope.statusSelect=[{text:"全部",value:''},{text:"初始化",value:'0'},{text:"未执行",value:'1'},{text:"还款中",value:'2'},{text:"还款成功",value:'3'},{text:"还款失败",value:'4'},{text:"挂起",value:'5'},{text:"终止",value:'6'}];
	$scope.profitTypeSelect=[{text:"分期还款",value:'1'},{text:"全额还款",value:'2'},{text:"保证金",value:'3'},{text:"完美还款",value:'4'}];
	$scope.agentList = [];
	$scope.info = {profitMerNo:'', containSub:'1', status:'',
		sTransTime:moment(new Date().getTime()-6*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
		eTransTime:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'};
	$scope.pageNo = 1;
	$scope.pageSize = 10;
	$scope.sunProfitDate = {
		shareAmount : '0.00',
		actualPayFee : '0.00',
		actualWithdrawFee : '0.00'
	};

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
        {field: 'profitNo',displayName: '分润流水号',width: 180,pinnable: false,sortable: false},
        {field: 'profitMerNo',displayName: '服务商编号',width: 120,pinnable: false,sortable: false},
        {field: 'agentName',displayName: '服务商名称',width: 120,pinnable: false,sortable: false},
        {field: 'profitType',displayName: '订单类型',width: 120,pinnable: false,sortable: false,
        	cellFilter:"formatDropping:"+angular.toJson($scope.profitTypeSelect)},
        {field: 'shareAmount',displayName: '分润',width: 120,pinnable: false,sortable: false},
		{field: 'toProfitAmount',displayName: '产生分润金额',width: 120,pinnable: false,sortable: false},
        {field: 'repayAmount',displayName: '任务金额',width: 120,pinnable: false,sortable: false},
        {field: 'ensureAmount',displayName: '保证金',width: 120,pinnable: false,sortable: false},
        {field: 'repayFee',displayName: '服务费',width: 120,pinnable: false,sortable: false},
        {field: 'successPayAmount',displayName: '已消费总额',width: 120,pinnable: false,sortable: false},
        {field: 'successRepayAmount',displayName: '已还款总额',width: 120,pinnable: false,sortable: false},
        {field: 'actualPayFee',displayName: '实际消费手续费',width: 150,pinnable: false,sortable: false},
        {field: 'actualWithdrawFee',displayName: '实际还款手续费',width: 150,pinnable: false,sortable: false},
        {field: 'orderNo',displayName: '关联还款订单',width: 180,pinnable: false,sortable: false},
        {field: 'merchantNo',displayName: '订单用户',width: 150,pinnable: false,sortable: false},
        {field: 'status',displayName: '订单状态',width: 120,pinnable: false,sortable: false,
        	cellFilter:"formatDropping:"+angular.toJson($scope.statusSelect)},
        {field: 'transTime',displayName: '终态时间',width: 200,pinnable: false,sortable: false,
        	cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'}
    ];
    $scope.profitGrid = {
        data : "profitDate",
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],	//切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
//        rowHeight:40,
        columnDefs: $scope.columnDefs,
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            $scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.pageNo = newPage;
                $scope.pageSize = pageSize;
                $scope.listRepayProfitDetail();
            });
        }
    };

    $scope.clear = function () {
        $scope.info = {profitMerNo:'', containSub:'1',  status:'',
			sTransTime:moment(new Date().getTime()-6*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
			eTransTime:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'};
    };

    $scope.listRepayProfitDetail = function () {
    	if ($scope.loadImg) {
			return;
		}
		if ($scope.info.minShareAmount && $scope.info.maxShareAmount
				&& ($scope.info.maxShareAmount - $scope.info.minShareAmount) < 0){
			$scope.notice("最小分润金额不能大于最大分润金额");
			return;
		}
		$scope.loadImg = true;
		$http({
			url: 'providerAction/listRepayProfitDetail?pageNo=' + $scope.pageNo + '&pageSize=' + $scope.pageSize,
			method: 'POST',
			data: $scope.info
    	}).success(function (data) {
			if (data.status){
                $scope.profitDate = data.page.result;
                $scope.profitGrid.totalItems = data.page.totalCount;
                $scope.sunProfitDate = data.sunProfitDate;
			}else{
                $scope.notice(data.msg);
			}
			$scope.loadImg = false;
        }).error(function (data) {
            $scope.notice("服务器异常,请稍后再试");
            $scope.loadImg = false;
        })
    };

    $scope.exportRepayProfitDetail = function () {
        SweetAlert.swal({
            title: "确定导出？",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            closeOnConfirm: true,
            closeOnCancel: true
        },
        function (isConfirm) {
            if (isConfirm) {
				location.href="providerAction/exportRepayProfitDetail?info="+encodeURI(angular.toJson($scope.info));
			}
        });
    };
});