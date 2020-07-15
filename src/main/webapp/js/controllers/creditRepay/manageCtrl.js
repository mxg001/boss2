/**
 * 用户管理
 */
angular.module('inspinia',['infinity.angular-chosen','uiSwitch']).controller('manageCtrl',function($scope,$http,$state,$stateParams,i18nService,$document,SweetAlert,$timeout){
	//数据源
	i18nService.setCurrentLang('zh-cn');

	$scope.paginationOptions = {pageNo : 1,pageSize : 10};
	$scope.baseInfo = {agentNo:''};
	$scope.merAccountSelect = [{text:"未开户",value:'0'},{text:"已开户",value:'1'}];
	$scope.statusSelect = [{text:"正常",value:'0'},{text:"已关闭",value:'1'}];
	$scope.sendData = {operateDetail:'', merchantNo:""};

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
		{field: 'merchantNo',displayName: '用户编号',width: 150,pinnable: false,sortable: false},
		{field: 'nickname',displayName: '昵称',width: 150,pinnable: false,sortable: false},
		{field: 'mobileNo',displayName: '手机号',width: 150,pinnable: false,sortable: false},
		{field: 'userName',displayName: '姓名',width: 150,pinnable: false,sortable: false},
		{field: 'proMerNo',displayName: '收款商户号',width: 150,pinnable: false,sortable: false},
		{field: 'agentName',displayName: '服务商名称',width: 150,pinnable: false,sortable: false},
		{field: 'agentNo',displayName: '服务商编号',width: 150,pinnable: false,sortable: false},
		{field: 'oneAgentName',displayName: '一级服务商名称',width: 150,pinnable: false,sortable: false},
		{field: 'oneAgentNo',displayName: '一级服务商编号',width: 150,pinnable: false,sortable: false},
		{field: 'merAccount',displayName: '开户状态',width: 150,pinnable: false,sortable: false,
			cellFilter:"formatDropping:"+ angular.toJson($scope.merAccountSelect)},
		{field: 'status',displayName: '用户状态',width: 150,pinnable: false,sortable: false,
			cellFilter:"formatDropping:"+ angular.toJson($scope.statusSelect)},
		{field: 'enterTime',displayName: '入驻时间',width: 150,pinnable: false,sortable: false,
			cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
		{field: 'createTime',displayName: '激活时间',width: 150,pinnable: false,sortable: false,
			cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'statusValue',displayName: '关闭/打开用户交易',width: 160,pinnable: false,sortable: false,cellTemplate:
            '<span ng-show="grid.appScope.hasPermit(\'merStatus\')">' +
				'<switch class="switch switch-s" ng-model="row.entity.statusValue" ng-change="grid.appScope.switchStatus(row)" />' +
			'</span>'
            +'<span ng-show="!grid.appScope.hasPermit(\'merStatus\')"> ' +
				'<span class="lh30" ng-show="row.entity.status==1">开启</span>' +
				'<span class="lh30" ng-show="row.entity.status==0">关闭</span>' +
			'</span>'
        },
		{field: 'action',displayName: '操作',width: 120,pinnedRight:true,sortable: false,editable:true,cellTemplate:
			'<a class="lh30" ui-sref="creditRepay.userDetail({merchantNo:row.entity.merchantNo})" target="_black">详情</a>'}
	];


    //修改状态
    $scope.switchStatus = function(row){
        $('#importModal').modal('show');
        $scope.sendData.operateDetail = "";
        $scope.sendData.merchantNo = row.entity.merchantNo;

        //打开/关闭用户备注日志
        $http.get('repayMerchant/queryMerStatusChangeLog?merchantNo='+$scope.sendData.merchantNo)
            .success(function(data) {
                if(data.status){
                    $scope.merStatusChangeLog.data = data.operateLogs;
                }else{
                    $scope.notice(data.msg);
                }
            });
    }


    //打开/关闭用户备注日志表格
    $scope.merStatusChangeLog={
        columnDefs:[
            { field: 'index',displayName:'序号',width: 40,cellTemplate: "<span class='checkbox'>{{rowRenderIndex + 1}}</span>"},
            { field: 'operateDetail',displayName:'打开/关闭用户备注',width: 350,cellFilter:"formatDropping:"+ angular.toJson($scope.operateTypeSelect)},
            { field: 'operator',displayName:'操作人',width: 100},
            { field: 'operateTime',displayName:'操作时间',width: 160,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'}

        ]
    };


	$scope.myGrid = {
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

	$scope.query = function () {
		if ($scope.loadImg) {
			return;
		}
		$scope.loadImg = true;
		$http({
			url: 'repayMerchant/selectRepayMerchantByParam?pageNo='+$scope.paginationOptions.pageNo
			+'&pageSize='+$scope.paginationOptions.pageSize,
			data: $scope.baseInfo,
			method:'POST'
		}).success(function (msg) {
			if (!msg.status){
				$scope.notice(msg.msg);
				return;
			}
			$scope.myGrid.data = msg.page.result;
			$scope.myGrid.totalItems = msg.page.totalCount;
			$scope.loadImg = false;
		}).error(function (msg) {
			$scope.notice('服务器异常,请稍后再试.');
			$scope.loadImg = false;
		});
	};

    $scope.changeStatus = function(){
        if($scope.sendData.operateDetail.length == 0){
            alert("请填写备注信息");
        }else {
            var data = {"merchantNo":$scope.sendData.merchantNo,"operateDetail":$scope.sendData.operateDetail};
            $http.post('repayMerchant/changeStatus', angular.toJson(data))
                .success(function (result) {
                    $scope.notice(result.msg);
                    setTimeout(function(){
                        $('#importModal').modal('hide');
                        $scope.query();
                    }, 1000)
                }).error(function () {
                $scope.notice(result.msg);
                setTimeout(function(){
                    $('#importModal').modal('hide');
                    $scope.query();
                }, 1000)
            });
        }
    }

    $scope.cancel = function(){
        $('#importModal').modal('hide');
        $scope.query();
    }

	// 批量入账
	$scope.batchOpenAccount = function () {
		selectedRows = $scope.gridApi.selection.getSelectedRows();
		if(selectedRows==null || selectedRows.length==0){
			$scope.notice('请选择要开户的条目');
			return;
    	}
		validIds = [];
		angular.forEach(selectedRows,function(data){
			if (data.merAccount == '0') {
				validIds[validIds.length] = data.merchantNo;
			}
    	});
		if (validIds == null || validIds.length == 0) {
			$scope.notice('请选择未开户的条目');
			return;
		}
		SweetAlert.swal({
			title: "批量开户",
			text: "选中条目中未开户的数据有 " + validIds.length + " 条，是否确定开户？",
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
						url: 'repayMerchant/batchOpenAccount',
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

	$scope.resetForm = function () {
		$scope.baseInfo = {agentNo:''};
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