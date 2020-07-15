/**
 * 交易查询
 */
angular.module('inspinia',['infinity.angular-chosen']).controller('saleQueryTradeCtrl',function($scope,$http,$state,$stateParams,$compile,$timeout,$log,i18nService,$document){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	//数据源
	$scope.bools = [{text:"全部",value:""},{text:'否',value:'0'},{text:'是',value:'1'}];

	$scope.frozenStatus = [{text:'全部',value:""},{text:'正常',value:'0'},{text:'已冻结',value:'1'}];
	$scope.settleStatuss=[{text:'全部',value:""},{text:'已结算',value:"1"},{text:'未结算',value:"0"},{text:'结算中',value:"2"},{text:'结算失败',value:"3"}]
	$scope.settlementMethods=[{text:'全部',value:""},{text:'T1',value:'1'},{text:'T0',value:'0'}];
	$scope.accounts=[{text:'全部',value:""},{text:'未记账',value:'0'},{text:'记账成功',value:'1'},{text:'记账失败',value:'2'}]
	$scope.dtOptions={}
	$scope.acqOrgs=[{text:"全部",value:""}];
	$scope.agent=[{text:"全部",value:""}];
	$scope.BusiProdDef=[{text:"全部",value:-1}];
	//机具类型
	$scope.termianlTypes=[];
	
	$scope.payMethodTypeStr = "[";
	angular.forEach($scope.payMethodType,function(data,index){
		$scope.payMethodTypeStr = $scope.payMethodTypeStr+'{text:"'+data.text+'",'+'value:'+data.value+'},';
	});
	$scope.payMethodTypeStr = $scope.payMethodTypeStr.substring(0,$scope.payMethodTypeStr.length-1);
	$scope.payMethodTypeStr = $scope.payMethodTypeStr+"]";

	//清空
	$scope.clear=function(){
		isVerifyTime = 1;
		$scope.info={account:"",id:"",accountSerialNo:"",agentNo:"",settleStatus:"",orderNo:"",transStatus:"",freezeStatus:"",acqOrgId:"",merchantNo:"",mobilephone:"",bool:"",
				terType:"",payMethod:"",businessProductId:-1,serviceType:"",accountNo:"",cardType:"",settlementMethod:"",
				smoney:"",emoney:"",acqMerchantNo:"",acqReferenceNo:"",
				sdate:moment(new Date().getTime()).format('YYYY-MM-DD')+' 00:00:00',edate:moment(new Date().getTime()).format('YYYY-MM-DD'+' 23:59:59'),
				transTimeStart:moment(new Date().getTime()-24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',transTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'};
	}
	$scope.clear();
	
	//收单机构、机具类型、服务类型，需要从数据库加载数据，按照以上格式赋值
	
	//baseInfo 在以后真正开发时，进行确定里面包含的内容
	//获取硬件产品列表
	$http.get('hardwareProduct/selectAllInfo.do')
	.success(function(result){
		if(!result)
			return;
		$scope.termianlTypes=result;
		$scope.termianlTypes.splice(0,0,{hpId:"",typeName:"全部"});
	})
	
	//收单机构
	 $http.post("acqOrgAction/selectBoxAllInfo")
  	 .success(function(msg){
  		//响应成功
  		 if(msg==null){
  			 return;
  		 }
  	    for(var i=0; i<msg.length; i++){
  	    	$scope.acqOrgs.push({value:msg[i].id,text:msg[i].acqName});
  	    }
  	});
	
	//代理商
	 $http.post("agentInfo/selectAllInfoSale")
  	 .success(function(msg){
  			//响应成功"
  	   	for(var i=0; i<msg.length; i++){
  	   		$scope.agent.push({value:msg[i].agentNo,text:msg[i].agentName});
  	   	}
  	});
	 
	//业务产品
	 $http.post("businessProductDefine/selectAllInfo.do")
  	 .success(function(msg){
  			//响应成功
  	   	for(var i=0; i<msg.length; i++){
  	   		$scope.BusiProdDef.push({value:msg[i].bpId,text:msg[i].bpName});
  	   	}
  	});

	//是否校验时间
	isVerifyTime = 1;//校验：1，不校验：0

	keyChange=function(){
		if ($scope.info.orderNo || $scope.info.merchantNo || $scope.info.mobilephone
				|| $scope.info.acqReferenceNo || $scope.info.accountNo) {
			isVerifyTime = 0;
		} else {
			isVerifyTime = 1;
		}
	}

	setBeginTime=function(setTime){
		$scope.info.sdate = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
	}
	setEndTime=function(setTime){
		$scope.info.edate = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
	}

	$scope.query=function(){
		if ($scope.loadImg) {
			return;
		}
		if (!($scope.info.orderNo || $scope.info.merchantNo || $scope.info.mobilephone
				|| $scope.info.acqReferenceNo || $scope.info.accountNo)) {
			if(!($scope.info.sdate && $scope.info.edate)){
				$scope.notice("创建时间不能为空");
				return;
			}
			var stime = new Date($scope.info.sdate).getTime();
			var etime = new Date($scope.info.edate).getTime();
			if ((etime - stime) > (31 * 24 * 60 * 60 * 1000)) {
				$scope.notice("创建时间范围不能超过31天");
				return;
			}
		}
		$scope.loadImg = true;
		$http.post("transInfoAction/getAllInfoSale","info="+angular.toJson($scope.info)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
				{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			.success(function(msg){
				if(msg.bols){
					if(msg.money==null){
						$scope.zmoney=0;
					}else{
						$scope.zmoney=msg.money;
					}
					$scope.gridOptions.data = msg.page.result; 
					$scope.gridOptions.totalItems = msg.page.totalCount;
					$scope.znum=msg.page.totalCount;
				}else{
					$scope.notice("查询出错");
				}
				$scope.loadImg = false;
		});
	}
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	
	$scope.gridOptions={                           //配置表格
		 	  paginationPageSize:10,                  //分页数量
	          paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
	          useExternalPagination: true,                //分页数量
		      columnDefs:[                           //表格数据
		         { field: 'id',displayName:'交易流水',width:170},
		         { field: 'orderNo',displayName:'订单号',width:170},
		         { field: 'settlementMethod',displayName:'结算周期',width:120,cellFilter:"settlementMethods"},
		         { field: 'merchantName',displayName:'商户简称',width:150 },
		         { field: 'merchantNo',displayName:'商户编号',width:180 },
		         { field: 'payMethod',displayName:'交易方式',cellFilter:"formatDropping:"+$scope.payMethodTypeStr,width:150 },
		         { field: 'cardType',displayName:'卡种',width:150,cellFilter:"formatDropping:"+$scope.cardTypeStr
		         },
		         { field: 'accountNo',displayName:'交易卡号',width:180 },
		         { field: 'transAmount',displayName:'金额（元）',cellFilter:"currency:''",width:180 },
		         { field: 'transStatus',displayName:'交易状态',cellFilter:"formatDropping:"+$scope.transStatusStr,width:150 },
		         { field: 'freezeStatus',displayName:'冻结状态',cellFilter:"freezeStatuss",width:150 },
		         { field: 'settleStatus',displayName:'结算状态',cellFilter:"settleStatuss",width:150},
		         { field: 'account',displayName:'交易记账',cellFilter:"accountss",width:150},
		         { field: 'transTime',displayName:'交易时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:150},
		         { field: 'id',displayName:'操作',pinnedRight:true,width:200,
		        	 cellTemplate:'<a class="lh30" target="_blank" ng-show="grid.appScope.hasPermit(\'transSelect.detail\')" ui-sref="sale.tradeQueryDetail({id:row.entity.orderNo,val:0})">详情</a>'
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

	$scope.getStates =getStates;
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
								$scope.agentt.push({value: "", text: ""});
							}else{
								for(var i=0; i<response.data.length; i++){
									$scope.agentt.push({value:response.data[i].agentNo,text:response.data[i].agentName});
								}
							}
							$scope.agent = $scope.agentt;
							oldValue = value;
						});
				},800);
		}

	}

	//页面绑定回车事件
	$document.bind("keypress", function(event) {
		$scope.$apply(function (){
			if(event.keyCode == 13){
				$scope.query();
			}
		})
	});

	
}).filter('accountss', function () {
	return function (value) {
		switch(value) {
			case "0" :
				return "未记账";
				break;
			case "1" :
				return "记账成功";
				break;
			case "2" :
				return "记账失败";
				break;
		}
	}
}).filter('settlementMethods', function () {
	return function (value) {
		switch(value) {
			case "0" :
				return "T0";
				break;
			case "1" :
				return "T1";
				break;
		}
	}
}).filter('settlementMethods', function () {
	return function (value) {
		switch(value) {
			case "0" :
				return "T0";
				break;
			case "1" :
				return "T1";
				break;
		}
	}
}).filter('freezeStatuss', function () {
	return function (value) {
		switch(value) {
			case "0" :
				return "正常";
				break;
			case "1" :
				return "已冻结";
				break;
		}
	}
}).filter('settleStatuss', function () {
	return function (value) {
		switch(value) {
			case "0" :
				return "未结算";
				break;
			case "1" :
				return "已结算 ";
				break;
			case "2" :
				return "结算中";
				break;
			case "3" :
				return "结算失败";
				break;
			default:
				return "未结算";
				break;
		}
	}
});

