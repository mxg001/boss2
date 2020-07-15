/**
 * 交易冻结
 */
angular.module('inspinia').controller('tradeFrozenCtrl',function($scope,$http,$state,$stateParams,$compile,$uibModal,$log,i18nService){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	$scope.settlementMethods=[{text:'T1',value:'1'},{text:'T0',value:'0'}];
	$scope.tradeInfoRecordData=[];
	 
	 $http.get('transInfoAction/queryInfoDetail?ids='+$stateParams.id)
	    .success(function(largeLoad) {
	    	if(!largeLoad.bols){
	    		$scope.notice(largeLoad.msg);
	    	}else{
	    		$scope.infoDetail=largeLoad.tt;
	    		$scope.pcb=largeLoad.pcb;
	    		$scope.pcb1=largeLoad.pcb1;
	    		$scope.tradeInfoRecordData=largeLoad.list;
	    		$scope.settleInfoRecordData=largeLoad.slist;
	    	}
	  });
	//表格
	 $scope.tradeInfoRecord={
		data:'tradeInfoRecordData',
		columnDefs:[
		      {field:'operTime',displayName:'时间',width:140,pinnable:false,sortable:false,
	       	     cellFilter: "date:'yyyy-MM-dd HH:mm:ss'"
	          },
	          {field:'operName',displayName:'操作人',width:110,pinnable:false,sortable:false}, 
	          {field:'operType',displayName:'操作内容',width:150,pinnable:false,sortable:false,
	        	  cellFilter:"formatDropping:[{text:'冻结',value:'0'},{text:'解冻',value:'1'}]"
	          }, 
	          {field:'operReason',displayName:'原因',width:200,pinnable:false,sortable:false}
			]
		};
	 
	//表格
	 $scope.settleInfoRecord={
		data:'settleInfoRecordData',
		columnDefs:[
			  {field:'id',displayName:'出款记录ID',width:120,pinnable:false,sortable:false},
			  {field:'createTime',displayName:'时间',width:110,pinnable:false,sortable:false,cellFilter: "date:'yyyy-MM-dd HH:mm:ss'"}, 
			  {field:'outAmount',displayName:'实际出款金额',width:150,pinnable:false,sortable:false,cellFilter:"currency:''"},
			  {field:'feeAmount',displayName:'出款手续费',width:130,pinnable:false,sortable:false,cellFilter:"currency:''"},
			  {field:'inBankName',displayName:'结算银行名称',width:150,pinnable:false,sortable:false},
			  {field:'outAccNo',displayName:'结算银行卡卡号',width:150,pinnable:false,sortable:false},
		      {field:'settleBank',displayName:'出款通道',width:110,pinnable:false,sortable:false}, 
		      {field:'outServiceId',displayName:'出款服务ID',width:140,pinnable:false,sortable:false},
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
	 
	 $scope.solutionModalOk=function(){
		//确认操作
		 if($scope.result==null||$scope.result==""){
     		$scope.notice("请填写冻结原因");
     	}else{
     		var data={"info":$scope.infoDetail,"result":$scope.result};
         	$http.post("transInfoAction/tradeFrozenInfo",angular.toJson(data))
    				 .success(function(msg){
		       	  		 if(msg.bols){
		       	  			 $scope.notice(msg.msg);
		       	  			 $state.go('trade.tradeQuery');
		       	  		 }else{
		       	  			 $scope.notice(msg.msg);
		       	  		 }
    				 })
     	}
	 }
})