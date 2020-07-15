angular.module('inspinia', ['infinity.angular-chosen']).controller('billShareReportCtrl',function($scope,$http,$state,$stateParams,$compile,$uibModal,$timeout,$log,i18nService,SweetAlert,$document){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.accountStatusList = [{text:"全部",value:"0"},{text:"已入账",value:"1"},{text:"入账失败",value:"2"},{text:"未入账",value:"3"}];
    $scope.accountStatusStr = angular.toJson($scope.accountStatusList);
    
    $scope.agentTypeAll = initData.AGENT_TYPE;
	$scope.agentTypeAll.unshift({text:"全部",value:""});
	$scope.agentTypeStr = angular.toJson($scope.agentTypeAll);
    /**
     * 清空
     */
    $scope.clear=function(){
        $scope.info={
        		batchNo:"",
        		oneAgentNo:"",
        		oneAgentName:"",
        		sShareAmount:"",
        		eShareAmount:"",
        		billMonth:moment(new Date().getTime()).format('YYYYMM'),
        		sAccountTime:"",
        		eAccountTime:"",
        		accountStatus:"0",
        		reportMonth:"",
        		agentType:""
        }
    };
    $scope.clear();
    /**
     * 
     */
    $scope.reportGrid={                           //配置表格
            paginationPageSize:10,                  //分页数量
            paginationPageSizes: [10,20,50,100],	//切换每页记录数
            useExternalPagination: true,		    //开启拓展名
            enableHorizontalScrollbar: true,        //横向滚动条
            enableVerticalScrollbar : true,  		//纵向滚动条
            columnDefs:[                           //表格数据
                { field: 'batchNo',displayName:'汇总批次号',width:150 },
                { field: 'createTime',displayName:'汇总时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180 },
                { field: 'billMonth',displayName:'保单创建月份',width:120 },
                { field: 'oneAgentNo',displayName:'一级代理商编号',width:170 },
                { field: 'oneAgentName',displayName:'一级代理商名称',width:180},
                { field: 'agentType',displayName:'代理商类型',width:120,cellFilter:"formatDropping:" +  $scope.agentTypeStr  },
                { field: 'totalAmount',displayName:'保费总金额',width:170 },
                { field: 'totalCount',displayName:'保单数',width:140 },
                { field: 'shareRate',displayName: '代理商分润百分比',width: 180,cellTemplate:"<div class='ui-grid-cell-contents ng-binding ng-scope'>{{row.entity.shareRate}}%</div>"},
                { field: 'shareAmount',displayName:'代理商分润金额',width:180 },
                { field: 'accountStatus',displayName:'分润入账状态',width:120,cellFilter:"formatDropping:" +  $scope.accountStatusStr},
                { field: 'accountTime',displayName:'入账时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:150},
                { field: 'id',displayName:'操作',width:180,pinnedRight:true, cellTemplate:
                '<div class="lh30">'+
                '<a target="_blank" ng-show="grid.appScope.hasPermit(\'shareReport.account\') && row.entity.accountStatus!=1"  ng-click="grid.appScope.initAccount(row.entity.id)">入账</a> ' +
                '</div>'
                }
            ],
            onRegisterApi: function(gridApi) {
                $scope.gridApi = gridApi;
                gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                    $scope.paginationOptions.pageNo = newPage;
                    $scope.paginationOptions.pageSize = pageSize;
                    $scope.query();
                });
            }
        };
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    /**
     * 
     */
	$scope.query = function(){
		if($scope.loadImg){
				return;
		}
		var stime = new Date($scope.info.sAccountTime).getTime();
		var etime = new Date($scope.info.eAccountTime).getTime();
		if ((etime - stime) > (90 * 24 * 60 * 60 * 1000)) {
			$scope.notice("查询时间范围不能超过90天");
			return;
		}
		$scope.loadImg = true;
		$http.post("shareReporAction/getAllInfo","info="+angular.toJson($scope.info)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
				{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			.success(function(msg){
				$scope.loadImg = false;
				debugger;
				if(msg.bols){
					$scope.reportGrid.data = msg.page.result; 
					$scope.reportGrid.totalItems = msg.page.totalCount;
					$scope.znum = msg.page.totalCount;
				}else{
					$scope.notice("查询出错");
				}
			}).error(function(){
				$scope.loadImg = false;
			});
		$http.post("shareReporAction/getTotalAmount","info="+angular.toJson($scope.info),
				{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			.success(function(msg){
				if(msg.bols){
					$scope.totalInfo = msg.totalInfo;
				}else{
				$scope.notice("查询汇总出错");
			}
		});
	}
	
    /**
     * 打开汇总模态框
     */
    $scope.showReportModal = function () {
        $("#initReportModal").modal("show");
    };
    /**
     * /关闭汇总模态框
     */
    $scope.cancel = function () {
        $("#initReportModal").modal("hide");
        $scope.info.reportMonth="";

    };
    /**
     * 汇总
     */
    $scope.initShareReport = function () {
    	$http.post("shareReporAction/initShareReport","reportMonth="+$scope.info.reportMonth+"&proCode=150007",
				{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			.success(function(msg){
				if(msg.bols){
					$scope.notice("汇总成功");
					$scope.cancel();
					$scope.query();
				}else{
					$scope.notice(msg.msg);
				}
		});
    	 
    };
	$scope.settleList = [];
	/**
	 * 单一入账
	 */
    $scope.initAccount = function (id) {
    	$scope.settleList = [];
		SweetAlert.swal({
			title: "确定入账吗？",
			type: "warning",
	        showCancelButton: true,
	        confirmButtonColor: "#DD6B55",
	        confirmButtonText: "确定",
	        cancelButtonText: "取消",
	        closeOnConfirm: true,
	        closeOnCancel: true 
	      },
		function (isConfirm) {
	    	  if (isConfirm) {
	    		  $scope.loadImg = true;
	    	      $scope.settleList[0] = id;
	    	      $scope.initBatchAccount();
		        }
	        });

    };
    /**
     * 批量入账
     */
    $scope.batchAccount = function () {
    	$scope.settleList = [];
    	$scope.list = $scope.gridApi.selection.getSelectedRows();
		angular.forEach($scope.list,function(item){
			if(item.accountStatus != 1){
				$scope.settleList[$scope.settleList.length] = item.id;
			}
		});
		 if($scope.settleList.length < 2){
			 $scope.notice("批量入账最少选中两条");
			 return;
		 }
	   SweetAlert.swal({
			title: "批量入账",
			text: "满足入账条件的数据有"+$scope.settleList.length+"条,是否确定入账？",
			type: "warning",
	        showCancelButton: true,
	        confirmButtonColor: "#DD6B55",
	        confirmButtonText: "确定",
	        cancelButtonText: "取消",
	        closeOnConfirm: true,
	        closeOnCancel: true 
	      },
		function (isConfirm) {
	    	  if (isConfirm) {
	    		  $scope.loadImg = true;
	    		  $scope.initBatchAccount();
		        }
	        });
    };
    
    /**
     * 批量记账处理
     */
    $scope.initBatchAccount = function () {
    	$http.post("shareReporAction/batchAccount","ids="+angular.toJson($scope.settleList),
				{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			.success(function(msg){
				if(msg.bols){
					$scope.notice("入账成功");
					$scope.loadImg = false;
					$scope.query();
				}else{
					$scope.notice(msg.msg);
					$scope.loadImg = false;
				}
		});
    	 
    };
	// 导出
	$scope.exportInfo = function () {
		var stime = new Date($scope.info.sAccountTime).getTime();
		var etime = new Date($scope.info.eAccountTime).getTime();
		if ((etime - stime) > (90 * 24 * 60 * 60 * 1000)) {
			$scope.notice("查询时间范围不能超过90天");
			return;
		}
		SweetAlert.swal({
				title: "确认导出？",
				showCancelButton: true,
				confirmButtonColor: "#DD6B55",
				confirmButtonText: "提交",
				cancelButtonText: "取消",
				closeOnConfirm: true,
				closeOnCancel: true
			},
			function (isConfirm) {
				if (isConfirm) {
					location.href="shareReporAction/importDetail?info="+encodeURI(angular.toJson($scope.info));
				}
			});
	};
    
});

