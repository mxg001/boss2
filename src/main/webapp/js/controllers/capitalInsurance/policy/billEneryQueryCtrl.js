angular.module('inspinia', ['infinity.angular-chosen']).controller('billEneryQueryCtrl',function($scope,$http,$state,$stateParams,$timeout,i18nService,SweetAlert){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    //订单类型
    $scope.orderTypeList = [{text:"全部",value:"0"},{text:"投保订单",value:"1"},{text:"退保订单",value:"2"}];
    $scope.orderTypeStr = angular.toJson($scope.orderTypeList);
   // 对账状态
    $scope.checkStatusList = [{text:"全部",value:"0"},{text:"核对成功",value:"1"},{text:"上游单边",value:"2"},{text:"平台单边",value:"3"},{text:"金额不符",value:"4"},{text:"未核对",value:"5"}];
    $scope.checkStatusStr = angular.toJson($scope.checkStatusList);
    
    // 汇总状态
    $scope.reportStatusList = [{text:"全部",value:"0"},{text:"未汇总",value:"2"},{text:"已汇总",value:"1"}];
    $scope.reportStatusStr = angular.toJson($scope.reportStatusList);
    
    $scope.insurerStr = angular.toJson($scope.insurerList);
    
    $scope.insureStatusList = [{text:"投保成功",value:"SUCCESS"},{text:"投保失败",value:"FAILED"},{text:"初始化",value:"INIT"},{text:"已退保",value:"OVERLIMIT"},{text:'退保失败',value:'RECEDEFAILED'}];
    $scope.insureStatusStr = angular.toJson($scope.insureStatusList);
    /**
     * 清空
     */
    $scope.clear=function(){
        $scope.info={
				oneAgentNo:"",
        		batchNo:"",
        		createTime:moment(new Date().getTime()-24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
        		createTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD'+' 23:59:59'),
        		insurer:"0",
        		checkStatus:"0",
        		orderType:"0",
        		acqOrderNo:"",
        		sysOrderNo:"",
        		reportStatus:"0",
        		insureTime:moment(new Date().getTime()-24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
        		insureTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD'+' 23:59:59'),
				billTimeBegin:"",
				billTimeEnd:""
        }
    };
    $scope.clear();

	//一级代理商
	$scope.oneAgentList=[];
	$http.post("agentInfo/selectAllOneInfo")
		.success(function(msg){
			$scope.oneAgentList=[{value:"",text:"全部"}];
			for(var i=0; i<msg.length; i++){
				$scope.oneAgentList.push({value:msg[i].agentNo,text:msg[i].agentNo + " " + msg[i].agentName});
			}
			$scope.aa="";
		});

	//一级代理商
	$scope.getOneStates =getOneStates;
	var oldValueOne="";
	var timeoutOne="";
	function getOneStates(value) {
		var newValueOne=value;
		if(newValueOne != oldValueOne){
			if (timeoutOne) $timeout.cancel(timeoutOne);
			timeoutOne = $timeout(
				function(){
					$http.post('agentInfo/selectAllOneInfo','item=' + value,
						{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
						.then(function (response) {
							if(response.data.length==0) {
								$scope.agenttOne =[{value: "", text: "全部"}];
							}else{
								$scope.agenttOne =[{value: "", text: "全部"}];
								for(var i=0; i<response.data.length; i++){
									$scope.agenttOne.push({value:response.data[i].agentNo,text:response.data[i].agentNo + " " + response.data[i].agentName});
								}
							}
							$scope.oneAgentList = $scope.agenttOne;
							oldValueOne = value;
						});
				},800);
		}
	};
    /**
     * 
     */
    $scope.billEneryGrid={                           //配置表格
            paginationPageSize:10,                  //分页数量
            paginationPageSizes: [10,20,50,100],	//切换每页记录数
            useExternalPagination: true,		    //开启拓展名
            enableHorizontalScrollbar: true,        //横向滚动条
            enableVerticalScrollbar : true,  		//纵向滚动条
            columnDefs:[                           //表格数据
                { field: 'id',displayName:'ID',width:100 },
                { field: 'batchNo',displayName:'对账批次号',width:180 },
                { field: 'insurer',displayName:'上游渠道',width:120,cellFilter:"formatDropping:" +  $scope.insurerStr},
                { field: 'orderType',displayName:'订单类型',width:120,cellFilter:"formatDropping:" +  $scope.orderTypeStr},
                { field: 'acqOrderNo',displayName:'上游保险订单号',width:180 },
                { field: 'acqAmount',displayName:'上游渠道含税保费',width:180 },
                { field: 'holder',displayName:'被保险人',width:180 },
				{ field: 'oneAgentNo',displayName:'一级代理商编号',width:180 },
				{ field: 'oneAgentName',displayName:'一级代理商名称',width:180 },
                { field: 'sysOrderNo',displayName:'平台保险订单号',width:180 },
                { field: 'sysBillNo',displayName:'保单号',width:180 },
                { field: 'sysAmount',displayName:'平台含税保费',width:180 },
                { field: 'billAmount',displayName:'平台售价',width:180 },
                { field: 'insureStatus',displayName:'平台投保状态',width:120,cellFilter:"formatDropping:" +  $scope.insureStatusStr},
                { field: 'insureTime',displayName:'投保时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180 },
                { field: 'effectiveStime',displayName:'保险起期',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180 },
                { field: 'effectiveEtime',displayName:'保险止期',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180 },
                { field: 'checkStatus',displayName:'对账状态',width:120,cellFilter:"formatDropping:" +  $scope.checkStatusStr},
                { field: 'reportStatus',displayName:'汇总状态',width:120,cellFilter:"formatDropping:" +  $scope.reportStatusStr},
                { field: 'id',displayName:'操作',width:180,pinnedRight:true, cellTemplate:
                '<div class="lh30">'+
                '<a target="_blank" ui-sref="policy.billEneryDetail({id:row.entity.id})">对账详情</a> ' +
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
    
    
    $scope.queryByParam = function(){
		if($scope.loadImg){
			return;
		}
    	// 对账汇总详情链接数据,只根据批次查询批次
    	if($scope.batchNo != null && $scope.batchNo != ""){
    		$scope.batchNo = null;
    	}else{
    		if(!($scope.info.insureTime && $scope.info.insureTimeEnd)){
    			$scope.notice("投保时间不能为空");
    			return;
    		}
    		var stime = new Date($scope.info.insureTime).getTime();
    		var etime = new Date($scope.info.insureTimeEnd).getTime();
    		if ((etime - stime) > (90 * 24 * 60 * 60 * 1000)) {
    			$scope.notice("投保时间范围不能超过90天");
    			return;
    		}
    		var cstime = new Date($scope.info.createTime).getTime();
    		var cetime = new Date($scope.info.createTimeEnd).getTime();
    		if ((cetime - cstime) > (90 * 24 * 60 * 60 * 1000)) {
    			$scope.notice("创建时间范围不能超过90天");
    			return;
    		}
    		if($scope.info.billTimeBegin!=null&&$scope.info.billTimeBegin!=""
				&&$scope.info.billTimeEnd!=null&&$scope.info.billTimeEnd!=""){
				var billTimeBegin = new Date($scope.info.billTimeBegin).getTime();
				var billTimeEnd = new Date($scope.info.billTimeEnd).getTime();
				if ((billTimeEnd - billTimeBegin) > (90 * 24 * 60 * 60 * 1000)) {
					$scope.notice("保单生成时间不能超过90天");
					return;
				}
			}
    	}
    	$scope.query();
    }
    /**
     * 
     */
	$scope.query = function(){
		$scope.loadImg = true;
		$http.post("billEneryAction/getEntryAllInfo","info="+angular.toJson($scope.info)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
				{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			.success(function(msg){
				$scope.loadImg = false;
				if(msg.bols){
					$scope.billEneryGrid.data = msg.page.result; 
					$scope.billEneryGrid.totalItems = msg.page.totalCount;
					$scope.znum = msg.page.totalCount;
				}else{
					$scope.notice("查询出错");
				}
			}).error(function(){
				$scope.loadImg = false;
			});
	}
	
	 /**
	  * 导出信息
	  */
	 $scope.exportInfo=function(){
		 if($scope.loadImg){
			 return;
		 }
		 if(!($scope.info.insureTime && $scope.info.insureTimeEnd)){
			 $scope.notice("投保时间不能为空");
			 return;
		 }
		 var stime = new Date($scope.info.insureTime).getTime();
		 var etime = new Date($scope.info.insureTimeEnd).getTime();
		 if ((etime - stime) > (90 * 24 * 60 * 60 * 1000)) {
			 $scope.notice("投保时间范围不能超过90天");
			 return;
		 }
		 var cstime = new Date($scope.info.createTime).getTime();
		 var cetime = new Date($scope.info.createTimeEnd).getTime();
		 if ((cetime - cstime) > (90 * 24 * 60 * 60 * 1000)) {
			 $scope.notice("创建时间范围不能超过90天");
			 return;
		 }
		 if($scope.info.billTimeBegin!=null&&$scope.info.billTimeBegin!=""
			 &&$scope.info.billTimeEnd!=null&&$scope.info.billTimeEnd!=""){
			 var billTimeBegin = new Date($scope.info.billTimeBegin).getTime();
			 var billTimeEnd = new Date($scope.info.billTimeEnd).getTime();
			 if ((billTimeEnd - billTimeBegin) > (90 * 24 * 60 * 60 * 1000)) {
				 $scope.notice("保单生成时间不能超过90天");
				 return;
			 }
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
					       	location.href="billEneryAction/exportTransInfo?info="+encodeURI(angular.toJson($scope.info));
			            }
		        });
	 }
	 
	  /**
	   * 对账汇总详情链接数据
	   */
		$scope.batchNo = $stateParams.batchNo;	
	    if($scope.batchNo != null && $scope.batchNo != ""){
	    	 $scope.info.batchNo= $scope.batchNo;
	    	 $scope.info.insureTime= "";
	    	 $scope.info.insureTimeEnd= "";
	    	 $scope.info.createTime= "";
	    	 $scope.info.createTimeEnd= "";
	    	 $scope.query();
	    }

});

