/**
 * 交易查询详情
 */
angular.module('inspinia').controller('queryTradeDetailCtrl',function($scope,$http,$state,$stateParams,$compile,$uibModal,$log,i18nService){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	$scope.settlementMethods=[{text:'T1',value:'1'},{text:'T0',value:'0'}];
//	$scope.settleStatusaa=[{text:'未结算',value:'0'},{text:'未结算',value:''},{text:'已结算',value:'1'},{text:'结算中',value:'2'},{text:'结算失败',value:'3'}]
	$scope.tradeInfoRecordData=[];
	$scope.val=$stateParams.val;
	$scope.name="交易详情";
	$scope.coupNo="";
	if($stateParams.val=="0"){
		$scope.name="交易详情";
	}else{
		$scope.name="交易解冻";
	}
	 
	 $http.get('transInfoAction/queryInfoDetail?ids='+$stateParams.id)
	    .success(function(largeLoad) {
	    	if(!largeLoad.bols){
	    		$scope.notice(largeLoad.msg);
	    	}else{
	    		$scope.infoDetail=largeLoad.tt;
	    		$scope.pcb=largeLoad.pcb;
	    		$scope.coupNo=largeLoad.coupNo;
	    		$scope.pcb1=largeLoad.pcb1;
	    		$scope.tradeInfoRecordData=largeLoad.list;
	    		$scope.settleInfoRecordData=largeLoad.slist;
				$scope.orderEventList=largeLoad.orderEventList;
	    	}
	  });
	//表格
	 $scope.tradeInfoRecord={
		data:'tradeInfoRecordData',
		columnDefs:[
		      {field:'operTime',displayName:'时间',pinnable:false,sortable:false,
	       	     cellFilter: "date:'yyyy-MM-dd HH:mm:ss'"
	          },
	          {field:'operName',displayName:'操作人',pinnable:false,sortable:false},
	          {field:'operType',displayName:'操作内容',pinnable:false,sortable:false,
	        	  cellFilter:"formatDropping:[{text:'冻结',value:'0'},{text:'解冻',value:'1'},{text:'转T1结算',value:'2'},{text:'定时任务发起T1交易出款',value:'3'}]"
	          }, 
	          {field:'operReason',displayName:'原因',pinnable:false,sortable:false}
			]
		};
	 
	//表格
	 $scope.settleInfoRecord={
		data:'settleInfoRecordData',
		columnDefs:[
			  {field:'id',displayName:'出款记录ID',width:120,pinnable:false,sortable:false},
			  {field:'accountSerialNo',displayName:'记账流水号',width:120,pinnable:false,sortable:false},
			  {field:'createTime',displayName:'时间',width:110,pinnable:false,sortable:false,cellFilter: "date:'yyyy-MM-dd HH:mm:ss'"}, 
			  {field:'outAmount',displayName:'实际出款金额',width:150,pinnable:false,sortable:false,cellFilter:"currency:''"},
			  {field:'feeAmount',displayName:'出款手续费',width:130,pinnable:false,sortable:false,cellFilter:"currency:''"},
			  {field:'inBankName',displayName:'结算银行名称',width:150,pinnable:false,sortable:false},
			  {field:'inAccNo',displayName:'结算银行卡卡号',width:150,pinnable:false,sortable:false},
		      {field:'settleBank',displayName:'出款通道',width:110,pinnable:false,sortable:false}, 
		      {field:'serviceName',displayName:'出款服务名称',width:140,pinnable:false,sortable:false},
	          {field:'status',displayName:'状态',width:110,pinnable:false,sortable:false,
		    	  cellFilter:"formatDropping:[{text:'未提交',value:'0'},{text:'已提交',value:'1'},{text:'提交失败',value:'2'},{text:'超时',value:'3'},{text:'交易成功',value:'4'},{text:'交易失败',value:'5'},{text:'未知',value:'6'}]"
	          }, 
	          {field:'errMsg',displayName:'错误信息',width:150,pinnable:false,sortable:false}, 
	          {field:'synAccountStatus',displayName:'记账状态',width:150,pinnable:false,sortable:false,
	        	  cellFilter:"formatDropping:[{text:'未记账',value:'1'},{text:'已记账',value:'2'},{text:'记账失败',value:'3'}]"
	          }, 
	          {field:'correction',displayName:'记账冲正',width:150,pinnable:false,sortable:false,
	        	  cellFilter:"formatDropping:[{text:'未冲正',value:'0'},{text:'已冲正',value:'1'}]"
	          },
	          {field:'userName',displayName:'结算人',width:140,pinnable:false,sortable:false}
			]
		};

	$scope.orderEventGrid={
		data:'orderEventList',
		columnDefs:[
			{field:'createTime',displayName:'时间',pinnable:false,sortable:false, cellFilter: "date:'yyyy-MM-dd HH:mm:ss'"},
			{field:'activityName',displayName:'名称',pinnable:false,sortable:false},
			{field:'eventRemark',displayName:'操作内容',pinnable:false,sortable:false},
			{field:'amountRemark',displayName:'结果',pinnable:false,sortable:false}
		]
	};

	 $scope.solutionModalOk=function(){
		//确认操作
     	var data={"info":$scope.infoDetail};
     	$http.post("transInfoAction/tradeUnfreezeInfo",angular.toJson(data))
		.success(function(msg){
	       	  	if(msg.bols){
	       	  		$scope.notice(msg.msg);
	       	  		$state.go('trade.tradeQuery');
	       	  	}else{
	       	  		$scope.notice(msg.msg);
	       	  	}
		 })
	 }

    $scope.merchantTypes=function(data){
        for(var i =0;i<$scope.merchantTypeLists.length;i++){
            if($scope.merchantTypeLists[i].value==data){
                return $scope.merchantTypeLists[i].text;
            }

        }
    };

	$scope.dataSta=true;
	$scope.getDetailShow = function(){
		if($scope.dataSta){
			$http.get('transInfoAction/getDetailShow?ids='+$stateParams.id)
				.success(function(largeLoad) {
					if(!largeLoad.bols){
						$scope.notice(largeLoad.msg);
					}else{
						$scope.infoDetail=largeLoad.tt;
						$scope.pcb1=largeLoad.pcb1;
						$scope.dataSta=false;
					}
				});
		}
	}

})