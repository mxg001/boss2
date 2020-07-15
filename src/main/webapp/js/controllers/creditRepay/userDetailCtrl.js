/**
 * 商户详情查看
 */
angular.module('inspinia').controller('userDetailCtrl',function($scope,$http,$state,$stateParams,SweetAlert,$compile,$filter,$log,$uibModal){

	//数据源
	$scope.info={};
	$scope.imageList={};
	$scope.debitCardList={};
	$scope.creditCardList={};
    $scope.reportStatusSelect = [{text:"未同步",value:'0'},{text:"同步成功",value:'1'},{text:"同步失败",value:'2'},{text:"审核中",value:'3'}];
    $scope.effectiveStatusSelect = [{text:"不生效",value:'0'},{text:"生效",value:'1'}];
    $scope.operateTypeSelect = [{text:"初始报备",value:'report'},{text:"修改同步",value:'sync'}];
    $scope.operateResultSelect = [{text:"失败",value:'0'},{text:"成功",value:'1'},{text:"未知",value:'2'}];
    $scope.paginationOptions = {pageNo : 1,pageSize : 10};

	$http.get('repayMerchant/queryMerchantDetailByMerchantNo?merchantNo='+$stateParams.merchantNo)
		.success(function(data) {
			if(data.status){
				$scope.info = data.info;
				$scope.imageList = data.imageList;
				$scope.debitCardList = data.debitCardList;
				$scope.creditCardList = data.creditCardList;
			}else{
				$scope.notice(data.msg);
			}
		});

	$http.get('repayMerchant/queryAccountAmount?merchantNo='+$stateParams.merchantNo)
		.success(function(data) {
			if(data.status){
				$scope.accountBalance.data = data.accountInfo;
			}else{
				$scope.notice(data.msg);
			}
		});

	$scope.accountBalance={	//配置表格
//		data:'accountInfo',
		columnDefs:[                           //表格数据
			{ field: 'balanceNo',displayName:'账号'},
			{ field: 'balance',displayName:'余额',cellFilter:"currency:''"},
			{ field: 'freezeAmount',displayName:'冻结余额',cellFilter:"currency:''"}
		]
	};


    $scope.channelSyn={	//配置表格
//		data:'accountInfo',
        columnDefs:[                           //表格数据
            { field: 'merchantNo',displayName:'商户编号'},
            { field: 'channelName',displayName:'同步通道'},
           /* { field: 'channelCode',displayName:'通道编号'},*/
            { field: 'reportStatus',displayName:'同步状态',cellFilter:"formatDropping:"+ angular.toJson($scope.reportStatusSelect)},
            { field: 'effectiveStatus',displayName:'是否生效',cellFilter:"formatDropping:"+ angular.toJson($scope.effectiveStatusSelect)},
            { field: 'zqMerchantNo',displayName:'上游子商户号'},
            {field: 'action',displayName: '操作',width: 120,pinnedRight:true,sortable: false,editable:true,cellTemplate:
                '<a class="lh30" ng-show="row.entity.reportStatus==0 || row.entity.reportStatus==2" '
                + 'ng-click="grid.appScope.toChannelSyn(row.entity.channelCode)">同步</a>'}
        ]
    };


    // 同步
    $scope.toChannelSyn = function (channelCode) {
        SweetAlert.swal({
                title: "确定同步吗？",
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
                        url: 'repayMerchant/toChannelSyn?merchantNo='+$stateParams.merchantNo+'&channelCode='+channelCode,
                        /*data: channelCode,*/
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

    $scope.query = function () {
        $http.get('repayMerchant/querychannelSyn?merchantNo='+$stateParams.merchantNo)
            .success(function(data) {
                if(data.status){
                    $scope.channelSyn.data = data.channelSynInfo;
                }else{
                    $scope.notice(data.msg);
                }
            });

        $http.get('repayMerchant/querychannelSynLog?merchantNo='+$stateParams.merchantNo)
            .success(function(data) {
                if(data.status){
                    $scope.channelSynLog.data = data.channelSynLogInfo;
                }else{
                    $scope.notice(data.msg);
                }
            });
    }

    $scope.query();



    $scope.channelSynLog={	//配置表格
//		data:'accountInfo',
        columnDefs:[                           //表格数据

            { field: 'channelName',displayName:'同步通道'},
            { field: 'operateType',displayName:'同步要素',cellFilter:"formatDropping:"+ angular.toJson($scope.operateTypeSelect)},
            { field: 'operateResult',displayName:'同步状态',cellFilter:"formatDropping:"+ angular.toJson($scope.operateResultSelect)},
            { field: 'createTime',displayName:'同步时间',cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
            { field: 'operator',displayName:'操作人'}
        ]
    };

    //打开/关闭用户备注日志表格
    $scope.columnDefs = [
        { field: 'index',displayName:'序号',width: 40,cellTemplate: "<span class='checkbox'>{{rowRenderIndex + 1}}</span>"},
        { field: 'operateDetail',displayName:'打开/关闭用户备注',width: 600,cellFilter:"formatDropping:"+ angular.toJson($scope.operateTypeSelect)},
        { field: 'operator',displayName:'操作人',width: 150},
        { field: 'operateTime',displayName:'操作时间',width: 200,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'}
    ];


    $scope.queryLog = function(){
        //打开/关闭用户备注日志
        $http.get('repayMerchant/queryAllMerStatusLog?merchantNo='+$stateParams.merchantNo
            +'&pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize)
            .success(function(data) {
                if(data.status){
                    // $scope.merStatusChangeLog.data = data.page.result;
                    $scope.merStatusChangeLog.data = data.page.result;
                    $scope.merStatusChangeLog.totalItems = data.page.totalCount;
                }else{
                    $scope.notice(data.msg);
                }
            });
    }
    $scope.queryLog();

    $scope.merStatusChangeLog = {
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
                $scope.queryLog();
            });
        }
    };

    /**
     * 获取敏感数据
     */
    $scope.dataSta=true;
    $scope.getDataProcessing = function () {
        if($scope.dataSta){
            $http.get('repayMerchant/getDataProcessing?merchantNo='+$stateParams.merchantNo)
                .success(function(data) {
                    if(data.status){
                        $scope.info.mobileNo = data.info.mobileNo;
                        $scope.info.idCardNo = data.info.idCardNo;
                        $scope.debitCardList = data.debitCardList;
                        $scope.creditCardList = data.creditCardList;
                        $scope.dataSta=false;
                    }else{
                        $scope.notice(data.msg);
                    }
                });
        }
    };
});