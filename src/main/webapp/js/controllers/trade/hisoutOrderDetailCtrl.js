/**
 * 出款订单详情
 */
angular.module('inspinia').controller('hisoutOrderDetailCtrl',function($scope,$http,$state,$stateParams,$compile,$log,i18nService){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文

//	$scope.settleTypes=[{text:"T0交易",value:'1'},{text:"手工提现",value:'2'},{text:"T1线上代付",value:'3'},{text:"T1线下代付",value:'4'}];
	$scope.settleUserTypes=[{text:"全部",value:''},{text:"商户",value:'M'},{text:"代理商",value:'A'},{text:"用户",value:'U'}];//M商户；A代理商'
	// $scope.sourceSystems=[{text:"全部",value:''},{text:"交易系统",value:'core'},{text:"账户系统",value:'account'},{text:"运营系统",value:'boss'}
	// 					,{text:"代理商app",value:'agentapp'},{text:"车管家系统",value:'car'},{text:"代理商web",value:"agentweb"}];
	$scope.synStatuss=[{text:"未提交",value:'1'},{text:"已提交",value:'2'}];
	$scope.settleOrderStatuss=[{text:"未确认",value:'0'},{text:"已确认",value:'1'},{text:"确认失败",value:'2'}];
	 $http.get('settleOrderInfoHistoryAction/selectInfoDetail?ids='+$stateParams.id)
	    .success(function(largeLoad) {
	    	if(!largeLoad.bols){
	    		$scope.notice(largeLoad.msg);
	    	}else{
	    		$scope.infoDetail=largeLoad.soi;
	    		$scope.settleInfoRecordData=largeLoad.slist;
	    	}
	  });

	//表格
	 $scope.settleInfoRecord={
		data:'settleInfoRecordData',
		columnDefs:[
			  {field:'id',displayName:'出款记录ID',width:120,pinnable:false,sortable:false},
			  {field:'accountSerialNo',displayName:'出款流水号',width:120,pinnable:false,sortable:false},
			  {field:'createTime',displayName:'时间',width:110,pinnable:false,sortable:false,cellFilter: "date:'yyyy-MM-dd HH:mm:ss'"}, 
			  {field:'outAmount',displayName:'实际出款金额',width:150,pinnable:false,sortable:false,cellFilter:"currency:''"},
			  {field:'feeAmount',displayName:'出款手续费',width:130,pinnable:false,sortable:false,cellFilter:"currency:''"},
			  {field:'inBankName',displayName:'结算银行名称',width:150,pinnable:false,sortable:false},
			  {field:'inAccNo',displayName:'结算银行卡卡号',width:150,pinnable:false,sortable:false},
			  {field:'inAccName',displayName:'收款账户名称',width:150,pinnable:false,sortable:false},
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
	          }
			]
		};
})