/**
 * 交易查询
 */
angular.module('inspinia', ['infinity.angular-chosen']).controller('queryTradeCtrl',function($scope,$http,$state,$stateParams,$compile,$uibModal,$timeout,$log,i18nService,SweetAlert,$document){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	//数据源
	$scope.bools = [{text:"全部",value:""},{text:'否',value:'0'},{text:'是',value:'1'}];
	$scope.deliveries = [{text:'新模式',value:'1'},{text:'旧模式',value:'0'}];
    $scope.authCodes = [{text:'否',value:''},{text:'是',value:'1'}];
    // $scope.recommendedSources=[{text:"全部",value:""},{text:"正常注册",value:"0"},{text:"微创业",value:"1"},{text:"代理商分享",value:"2"},{text:"超级盟主",value:"3"}];
	$scope.frozenStatus = [{text:'全部',value:""},{text:'正常',value:'0'},{text:'风控冻结',value:'1'},{text:'活动冻结',value:'2'},{text:'财务冻结',value:'3'}];
	$scope.settlementMethods=[{text:'全部',value:""},{text:'T1',value:'1'},{text:'T0',value:'0'}];
	$scope.accounts=[{text:'全部',value:""},{text:'未记账',value:'0'},{text:'记账成功',value:'1'},{text:'记账失败',value:'2'}];
	$scope.dtOptions={}
	$scope.acqOrgs=[{text:"全部",value:""}];
	$scope.agent=[{text:"全部",value:""}];
	$scope.BusiProdDef=[{text:"全部",value:-1}];
	$scope.totalInfo = {};
	$scope.settleChangeStatus=[{text:"转T1结算",value:'4'},{text:"不结算",value:'5'},{text:"已结算",value:'1'}];//变更状态
	$scope.profitTypes=[{text:"普通交易分润",value:'0'},{text:"封顶交易分润",value:'1'}];
	//机具类型
	$scope.termianlTypes=[];

	$scope.subTeamMap = [];
	$scope.subTeams = [];
	$scope.allSubTeams = [];

	//代理商推广
	$scope.sourceSysStaSelect=[{text:'全部',value:null},{text:'是',value:1},{text:'否',value:2}];

	$http.get('teamInfo/querySubTeams').success(function(result){
		$scope.subTeamMap = result.subTeamMap;
		angular.forEach($scope.subTeamMap, function (x) {
			angular.forEach(x, function (y) {
				$scope.allSubTeams.push({text:y.teamEntryName,value:y.teamEntryId});
			});
		});
		$scope.subTeams = $scope.allSubTeams;
	});

	$scope.hasSubTeam = function(teamId){
		$scope.productChange(teamId);
		if(teamId == "" || teamId == null){
			$scope.subTeams = $scope.allSubTeams;
		}else {
			$scope.subTeams = [];
			var temp = $scope.subTeamMap[teamId];
			if(null != temp && temp != undefined){
				angular.forEach(temp, function (e) {
					$scope.subTeams.push({text:e.teamEntryName,value:e.teamEntryId});
				});
			}
		}
	}

	$scope.payMethodTypeStr = angular.toJson($scope.payMethodType);
	$scope.showTotalInfo = false;//是否展示汇总数据

	//条件显示问题
	$scope.mtxt="全部条件";
	$scope.visible= false;
	$scope.toggle = function(){
		if($scope.visible == false){
			$scope.mtxt="收起";
			$scope.visible=true;
		}else{
			$scope.mtxt="全部条件";
			$scope.visible=false;
		}
	}
	$scope.cancel = function(){
		$('#settleAccounts').modal('hide');
	}

	//提交状态变更信息
	$scope.submitSettleStatus = function(){
		if($scope.info.changeSettleStatusReason==null || $scope.info.changeSettleStatusReason=="" || $scope.info.changeSettleStatusReason==undefined){
			$scope.notice('请填写结算状态变更原因！');
			return;
		}
		$http.post("transInfoAction/changeSettleStatus","currSettleStatus="+$scope.info.currSettleStatus+
			"&changeSettleStatusReason="+$scope.info.changeSettleStatusReason+"&ids="+$scope.info.settleOrderList+"&oldSettleStatus="+$scope.info.oldSettleStatus+"&orderOrigin=0",
			{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			.success(function(data){
				if(data!=null && data!=""){
					$scope.notice(data.msg);
					$scope.settleChangeStatus = $scope.oldSettleChangeStatusArr;
					$scope.cancel();
					$scope.query();
				}else{
					$scope.notice("请求数据失败!");
				}
			})
			.error(function(data){
				$scope.notice("请求数据失败!");
			});
	}

	//弹出状态更改窗口
	$scope.settleAccounts = function(){
		$scope.settleChangeStatus=[{text:"转T1结算",value:'4'},{text:"不结算",value:'5'},{text:"已结算",value:'1'}];//变更状态
		$scope.info.changeSettleStatusReason = "";//变更原因
		$scope.info.currSettleStatus = "";//变更后的状态
		$scope.info.settleOrderList= [];//订单id
		$scope.info.settleOrderListStr = "";//订单id字符串
		$scope.info.oldSettleStatus = "";//变更前的状态

		var selectedRecordRows = $scope.gridApi.selection.getSelectedRows();
		if(selectedRecordRows==null || selectedRecordRows	.length<=0){
			$scope.notice('请选择交易明细订单');
			return;
		}
		$scope.info.currSettleStatus = selectedRecordRows[0].settleStatus;


		var canChangeSettleStatus = true;
		angular.forEach(selectedRecordRows,function(item){
			if(item.settleStatus!=$scope.info.currSettleStatus){
				canChangeSettleStatus = false;
			}
			$scope.info.settleOrderList[$scope.info.settleOrderList.length] = item.orderNo;
			$scope.info.settleOrderListStr+=item.orderNo+"、";
		});
		
		if($scope.info.settleOrderList.length>10) {
			$scope.info.settleOrderListStr = "";
			angular.forEach(selectedRecordRows,function(item,index){
				if(index>9){
					$scope.info.settleOrderListStr = $scope.info.settleOrderListStr.substr(0, $scope.info.settleOrderListStr.length - 1)+"···";
				}else{
					$scope.info.settleOrderListStr+=item.orderNo+"、";
				}
			});
		}else{
			$scope.info.settleOrderListStr = $scope.info.settleOrderListStr.substr(0, $scope.info.settleOrderListStr.length - 1);
		}
		
		if(!canChangeSettleStatus){
			$scope.notice('请选择结算状态一致的订单！');
			return;
		}
		$scope.info.oldSettleStatus = $scope.info.currSettleStatus;
		//判断所选订单状态是否已经被修改过
		checkCanChangeSettleStatus($scope.info.settleOrderList);
	}

	//判断所选订单状态是否已经被修改过
	function checkCanChangeSettleStatus(ids){
		$http.post("settleOrderInfoAction/checkCanChangeSettleStatus","ids="+ids+"&orderOrigin=0",
			{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			.success(function(data){
				if(data!=null || data!=""){
					if(!data.checkCanChangeSettleStatusFlag){
						$scope.notice('存在已变更结算状态的交易明细订单！');
					}else{
						//删除当前状态下拉选项
						$scope.oldSettleChangeStatusArr = $scope.settleChangeStatus;
						$scope.settleChangeStatus = [];
						angular.forEach($scope.oldSettleChangeStatusArr,function(item,index){
							if(item.value!=$scope.info.oldSettleStatus){
								$scope.settleChangeStatus.push(item);
								$scope.info.currSettleStatus = item.value;
							}
						})
						$('#settleAccounts').modal('show');
					}
				}else{
					$scope.notice("请求数据异常!");
				}
			})
			.error(function(data){
				$scope.notice("请求数据异常!");
			});
	}

	//清空
	$scope.clear=function(){
		isVerifyTime = 1;
		$scope.info={account:"",id:"",accountSerialNo:"",agentNo:"",settleStatus:"",orderNo:"",transStatus:"",freezeStatus:"",acqOrgId:"",merchantNo:"",mobilephone:"",bool:"",
				terType:"",payMethod:"",businessProductId:-1,serviceType:"",accountNo:"",cardType:"",settlementMethod:"",activityType:"",settleType:"",settleOrder:"",
				smoney:"",emoney:"",acqMerchantNo:"",acqReferenceNo:"", acqSerialNo:"",superPushStatus:"",sourceSysSta:null,
				sdate:moment(new Date().getTime()-24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',edate:moment(new Date().getTime()).format('YYYY-MM-DD'+' 23:59:59'),
				orderType:"",zxStatus:"",vasRate:"",recommendedSource:"",teamId:"",merchantType:"",authCodeStatus:"",queryTotalStatus:0,profitType:""};

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
	 $http.post("agentInfo/selectAllInfo")
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

	//所属组织关联业务产品
	$scope.productChange = function(teamId){
		$http.post('businessProductDefine/selectAllInfoByTeamId.do',"teamId="+teamId,
			{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			.success(function(result){
				if(!result)
					return;
				$scope.BusiProdDef=[{text:"全部",value:-1}];
				for(var i=0; i<result.length; i++){
					$scope.BusiProdDef.push({value:result[i].bpId,text:result[i].bpName});
				}
			})
	}
	 //组织
    $http.get('teamInfo/queryTeamName.do').success(function(msg){
        $scope.teamType=[];
        $scope.teamType.push({text:'全部',value:""});
        for(var i=0;i<msg.teamInfo.length;i++){
            $scope.teamType.push({text:msg.teamInfo[i].teamName,value:msg.teamInfo[i].teamId});
        }
    });

	//是否校验时间
	isVerifyTime = 1;//校验：1，不校验：0

	keyChange=function(){
		if ($scope.info.merchantNo || $scope.info.mobilephone || $scope.info.orderNo
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

	//校验查询条件
	function queryCheck(){
        if($scope.info.acqSerialNo!= null&&$scope.info.acqSerialNo!=""){
            if($scope.info.acqMerchantNo==null||$scope.info.acqMerchantNo==""){
                $scope.notice("收单机构凭证号查询时,收单商户号不能为空!");
                return false;
            }
        }
        if (!($scope.info.merchantNo || $scope.info.mobilephone || $scope.info.orderNo
                || $scope.info.acqReferenceNo || $scope.info.accountNo)) {
            if(!($scope.info.sdate && $scope.info.edate)){
                $scope.notice("创建时间不能为空");
                return false;
            }
            var stime = new Date($scope.info.sdate).getTime();
            var etime = new Date($scope.info.edate).getTime();
            if ((etime - stime) > (31 * 24 * 60 * 60 * 1000)) {
                $scope.notice("创建时间范围不能超过31天");
                return false;
            }
        }
        return true;
	}

	$scope.count = function(){

	}

	$scope.query=function(sta){
		if($scope.loadImg){
			return;
		}

		var orderNo=$scope.info.orderNo;
		if(orderNo!=null&&orderNo!=""){
			if(orderNo.indexOf("，") != -1){
				orderNo=orderNo.replace(/，/ig,',');
			}
			if(orderNo.indexOf(",") != -1){
				var strs=orderNo.split(",");
				if(strs!=null&&strs.length>100){
					$scope.notice("最多只能输入100个订单号,超出" + (strs.length-100) +  "个!");
					return;
				}
			}
		}

		if(!queryCheck()) {
			return ;
		}

		$scope.info.queryTotalStatus = 0;
        //是否展示统计信息
        $scope.showTotalInfo = false;
		if(sta==1){
			$http.post("transInfoAction/getTotalNumAndTotalMoney","info="+angular.toJson($scope.info),
				{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
				.success(function(msg){
					if(msg.bols){
						$scope.gridOptions.totalItems = msg.totalInfo.tradeCountNum;
                        $scope.totalInfo = msg.totalInfo;

					}else{
						$scope.notice("查询汇总出错");
					}
				});
		}
        queryList();
	}

	//查询列表数据
	function queryList(){
        $scope.loadImg = true;
        $http.post("transInfoAction/getAllInfo","info="+angular.toJson($scope.info)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(msg){
                $scope.loadImg = false;
                if(msg.bols){
                    $scope.gridOptions.data = msg.page.result;
                }else{
                    $scope.notice("查询出错");
                }
            }).error(function(){
            $scope.loadImg = false;
        });
	}

	$scope.queryTotal = function(){
        if(!queryCheck()) {
            return ;
        }
        queryList();
        $scope.info.queryTotalStatus = 1;
		$http.post("transInfoAction/getTotalNumAndTotalMoney","info="+angular.toJson($scope.info),
			{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			.success(function(msg){
				if(msg.bols){
					$scope.totalInfo = msg.totalInfo;
                    $scope.gridOptions.totalItems = msg.totalInfo.tradeCountNum;
                    //是否展示统计信息
                    $scope.showTotalInfo = true;
				}else{
					$scope.notice("查询汇总出错");
				}
			});
	}

	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	//$scope.query();手动点查询
	var rowList={};
	var num=0;
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
                  { field: 'merchantType',displayName:'商户类型',width:150,
                      cellFilter:"formatDropping:"+ angular.toJson($scope.merchantTypeLists)
                  },
                  { field: 'recommendedSource',displayName: '推广来源',width:180,cellFilter:"formatDropping:"+ angular.toJson($scope.recommendedSources)},
				  { field: 'sourceSysSta',displayName: '代理商推广',width:180,cellFilter:"formatDropping:"+ angular.toJson($scope.sourceSysStaSelect)},
				  { field: 'unionpayMerNo',displayName:'银联报备商户编号',width:180},
		          { field: 'payMethod',displayName:'交易方式',cellFilter:"formatDropping:"+$scope.payMethodTypeStr,width:150 },
				  { field: 'delivery',displayName: '交易模式',width:180,cellFilter:"formatDropping:"+ angular.toJson($scope.deliveries)},
		          { field: 'orderType',displayName:'订单类型',cellFilter:"formatDropping:"+angular.toJson($scope.orderType),width:150 },
		          { field: 'zxRate',displayName:'是否优享(一)收费',width:150 },
		          { field: 'serviceType',displayName:'收款类型',cellFilter:"formatDropping:"+angular.toJson($scope.serviceTypeAll),width:150 },
		          { field: 'cardType',displayName:'卡种',width:150,cellFilter:"formatDropping:"+$scope.cardTypeStr},
		          { field: 'accountNo',displayName:'交易卡号',width:180 },
		          { field: 'transAmount',displayName:'金额（元）',cellFilter:"currency:''",width:180 },
				  { field: 'nPrm',displayName:'保费',cellFilter:"currency:''",width:180 },
				  { field: 'merchantRate',displayName:'商户费率',width:130 },
				  { field: 'vasRate',displayName:'服务费',width:130 },
		          { field: 'merchantFee',displayName:'交易手续费（元）',cellFilter:"currency:''",width:180 },
		          { field: 'deductionFee',displayName:'抵扣交易手续费（元）',cellFilter:"currency:''",width:180 },
		          { field: 'actualFee',displayName:'实际交易手续费（元）',cellFilter:"currency:''",width:180 },
		          { field: 'amount',displayName:'出款金额（元）',cellFilter:"currency:''",width:180 },
		          { field: 'outAmount',displayName:'到账金额（元）',cellFilter:"currency:''",width:180 },
		          { field: 'feeAmount',displayName:'出款手续费（元）',cellFilter:"currency:''",width:180 },
		          { field: 'outActualFee',displayName:'实际出款手续费（元）',cellFilter:"currency:''",width:180 },
		         { field: 'acqMerchantFee',displayName:'营销费用（元）',cellFilter:"currency:''",width:180 },
				  { field: 'deductionMerFee',displayName:'抵扣优享(二)手续费（元）',cellFilter:"currency:''",width:180 },
				  { field: 'actualMerchantPrice',displayName:'实际优享(二)手续费（元）',cellFilter:"currency:''",width:180 },
				  { field: 'transStatus',displayName:'交易状态',cellFilter:"formatDropping:"+$scope.transStatusStr,width:150 },
				  { field: 'profitType',displayName: '分润类型',width:150,cellFilter:"formatDropping:"+ angular.toJson($scope.profitTypes)},
		          { field: 'freezeStatus',displayName:'冻结状态',cellFilter:"freezeStatuss",width:150 },
		          { field: 'settleStatus',displayName:'结算状态',cellFilter:"formatDropping:"+$scope.settleStatusStr,width:150},
		          { field: 'account',displayName:'交易记账',cellFilter:"accountss",width:150},
		          { field: 'settleType',displayName:'出款类型',
		          	 cellFilter:"formatDropping:"+ angular.toJson($scope.settleTypeAll),width:150},
		          { field: 'settleOrder',displayName:'出款订单ID',width:150},
		          { field: 'createTime',displayName:'创建时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:150},
		          { field: 'transTime',displayName:'交易时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:150},
				  /*{ field: 'address',displayName:'经营地址',width:150},
				  { field: 'mobilephone',displayName:'商户手机号',width:150},
				  { field: 'saleName',displayName:'一级代理商所属销售',width:150},*/
		          { field: 'id',displayName:'操作',pinnedRight:true,width:200,
		        	 cellTemplate:
		        		   '<div class="lh30" ng-switch on="$eval(\'row.entity.freezeStatus\')">'
						 		+'<div ng-switch-when=1>'
						 			+'<div ng-show="row.entity.isSettleMethod==1">'
							 			+'<a target="_blank" ng-show="grid.appScope.hasPermit(\'trade.detail\')" ui-sref="trade.tradeQueryDetail({id:row.entity.orderNo,val:0})">详情</a>'
							 			+'<a ng-show="grid.appScope.hasPermit(\'trade.ticket\')" ng-click="grid.appScope.tradeInvoiceInfo(row.entity.orderNo)"> | 小票</a> '
								 		+'<a target="_blank" ng-show="grid.appScope.hasPermit(\'trade.thaw\')" ui-sref="trade.tradeQueryDetail({id:row.entity.orderNo,val:1})"> | 解冻</a>'
								 		+'<a ng-show="grid.appScope.hasPermit(\'trade.settleTransInfo\')&&row.entity.acqEnname!=\'BeiD\'" ng-click="grid.appScope.tradeSettle(row.entity.orderNo)"> | 结算</a> '
						 			+'</div>'
						 			+'<div ng-show="row.entity.isSettleMethod==0">'
							 			+'<a ng-show="grid.appScope.hasPermit(\'trade.detail\')" target="_blank" ui-sref="trade.tradeQueryDetail({id:row.entity.orderNo,val:0})">详情</a>'
							 			+'<a ng-show="grid.appScope.hasPermit(\'trade.ticket\')" ng-click="grid.appScope.tradeInvoiceInfo(row.entity.orderNo)"> | 小票</a> '
								 		+'<a target="_blank" ng-show="grid.appScope.hasPermit(\'trade.thaw\')" ui-sref="trade.tradeQueryDetail({id:row.entity.orderNo,val:1})"> | 解冻</a>'
						 			+'</div>'
						 		+'</div>'
						 		+'<div ng-switch-when=0>'
						 			+'<div ng-show="row.entity.payMethod!=null">'
								 		+'<div ng-show="row.entity.isSettleMethod==1" style="display:inline-block;">'
								 			+'<a target="_blank" ng-show="grid.appScope.hasPermit(\'trade.detail\')" ui-sref="trade.tradeQueryDetail({id:row.entity.orderNo,val:0})">详情</a>'
								 			+'<a ng-show="grid.appScope.hasPermit(\'trade.ticket\')" ng-click="grid.appScope.tradeInvoiceInfo(row.entity.orderNo)"> | 小票</a> '
									 		//+'<a target="_blank" ng-show="grid.appScope.hasPermit(\'trade.frozen\')" ui-sref="trade.tradeFrozen({id:row.entity.orderNo})"> | 冻结</a>'
									 		+'<a ng-show="grid.appScope.hasPermit(\'trade.settleTransInfo\')&&row.entity.acqEnname!=\'BeiD\'" ng-click="grid.appScope.tradeSettle(row.entity.orderNo)"> | 结算</a> '
							 			+'</div>'
							 			+'<div ng-show="row.entity.isSettleMethod==0" style="display:inline-block;">'
								 			+'<a target="_blank" ng-show="grid.appScope.hasPermit(\'trade.detail\')" ui-sref="trade.tradeQueryDetail({id:row.entity.orderNo,val:0})">详情</a>'
								 			+'<a ng-show="grid.appScope.hasPermit(\'trade.ticket\')" ng-click="grid.appScope.tradeInvoiceInfo(row.entity.orderNo)"> | 小票</a> '
									 		//+'<a target="_blank" ng-show="grid.appScope.hasPermit(\'trade.frozen\')" ui-sref="trade.tradeFrozen({id:row.entity.orderNo})"> | 冻结</a>'
							 			+'</div>'
							 			+'<div style="display:inline-block;" ng-show="row.entity.transStatus==\'SUCCESS\' && row.entity.settleStatus!=1 && row.entity.account==1">'
							 			    +'<a target="_blank" ng-show="grid.appScope.hasPermit(\'trade.frozen\')" ui-sref="trade.tradeFrozen({id:row.entity.orderNo})"> | 冻结</a>'
							 			+'</div>'
							 		+'</div>'
							 		+'<div ng-show="row.entity.payMethod==null">'
							 			+'<a target="_blank" ng-show="grid.appScope.hasPermit(\'trade.detail\')" ui-sref="trade.tradeQueryDetail({id:row.entity.orderNo,val:0})">详情</a>'
							 			+'<a ng-show="grid.appScope.hasPermit(\'trade.ticket\')" ng-click="grid.appScope.tradeInvoiceInfo(row.entity.orderNo)"> | 小票</a> '
							 		+'</div>'
						 		+'</div>'
						 		+'<div ng-switch-default>'
							 		+'<div ng-show="row.entity.isSettleMethod==1">'
							 			+'<a target="_blank" ng-show="grid.appScope.hasPermit(\'trade.detail\')" ui-sref="trade.tradeQueryDetail({id:row.entity.orderNo,val:0})">详情</a>'
							 			+'<a ng-show="grid.appScope.hasPermit(\'trade.settleTransInfo\')&&row.entity.acqEnname!=\'BeiD\'" ng-click="grid.appScope.tradeSettle(row.entity.orderNo)"> | 结算</a> '
						 			+'</div>'
						 			+'<div ng-show="row.entity.isSettleMethod==0">'
						 				+'<a target="_blank" ng-show="grid.appScope.hasPermit(\'trade.detail\')" ui-sref="trade.tradeQueryDetail({id:row.entity.orderNo,val:0})">详情</a> '
						 			+'</div>'
			    				+'</div>'
						 	+'</div>'
		         }
		      ],
			  onRegisterApi: function(gridApi) {                
		          $scope.gridApi = gridApi;
		          //全选
			      $scope.gridApi.selection.on.rowSelectionChangedBatch($scope,function (rows) {
			            if(rows[0].isSelected){
			               $scope.testRow = rows[0].entity;
			               for(var i=0;i<rows.length;i++){
			            	   rowList[rows[i].entity.agentNo]=rows[i].entity;
			            	   num++;
			               }
			            }else{
			            	rowList={};
			            	num=0;
			            }
			         })
			         //单选
			         $scope.gridApi.selection.on.rowSelectionChanged($scope,function (row) {
			            if(row.isSelected){
			               $scope.testRow = row.entity;
			               rowList[row.entity.agentNo]=row.entity;
			               num++;
			            }else{
			            	delete rowList[row.entity.agentNo];
			            	num--;
			            	if(num<0){
			            		num=0;
			            	}
			            }
			         })
		          gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
		          	$scope.paginationOptions.pageNo = newPage;
		          	$scope.paginationOptions.pageSize = pageSize;
		             $scope.query(0);
		          });
		      }
		};
	
	//结算
	$scope.tradeSettle=function(id){
		SweetAlert.swal({
            title: "确认结算？",
//            text: "",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "提交",
            cancelButtonText: "取消",
            closeOnConfirm: true,
            closeOnCancel: true },
	        function (isConfirm) {
	            if (isConfirm) {
	            	$http.post("transInfoAction/settleTransInfo?id="+id)
	        		.success(function(msg){
	        			 if(!msg.bols){
	        				 $scope.notice(msg.msg);
	        			 }else{
	        				 $scope.notice(msg.msg);
	        				 $scope.query(1);
	        			 }
	        		});
	            }
        });
	}
	
	//========批量手工结算 by sober==========================================================================
	$scope.settleButch=function(type){
		//直接猎取到所有选中的行
		$scope.list = $scope.gridApi.selection.getSelectedRows();
		$scope.settleList = [];
		angular.forEach($scope.list,function(item){
			if(item.isSettleMethod==1){
				$scope.settleList[$scope.settleList.length] = item;
			}
		})
		 if($scope.settleList.length<2){
			 $scope.notice("批量结算最少选中两条");
			 return;
		 }
		 var list=$scope.list;
		 SweetAlert.swal({
	            title: "确认批量手工结算？",
	            showCancelButton: true,
	            confirmButtonColor: "#DD6B55",
	            confirmButtonText: "确认",
	            cancelButtonText: "取消",
	            closeOnConfirm: true,
	            closeOnCancel: true 
	            },
		        function (isConfirm) {
		            if (isConfirm) {
		            	$http.post("transInfoAction/settleButch?type=" + type,angular.toJson($scope.settleList))
		   	   		 	.success(function(datas){
			   	   			 if(datas.bols){
			   	   				$scope.settleMsg = datas;
			   	   				$('#settleModal').modal('show');
			   	   				$scope.query(1);
			   	   			 }else{
			   	   				 $scope.notice(datas.msg);
			   	   			 }
			   	   			 rowList={};
			   	   			 num=0;
		   	   		 	});
		            }else{
		            	 rowList={};
		   	   			 num=0;
		            }
	        });
	 }
	
	
	//订单数据同步
	$scope.syncOrder=function(){
		//直接猎取到所有选中的行
		$scope.list = $scope.gridApi.selection.getSelectedRows();
		$scope.settleList = [];
		angular.forEach($scope.list,function(item){
			if(item.transStatus=='INIT'){
				$scope.settleList[$scope.settleList.length] = item;
			}
		})
		 if($scope.settleList.length<1){
			 $scope.notice("请选择需要同步的数据");
			 return;
		 }
		 var list=$scope.list;
		 SweetAlert.swal({
	            title: "确认同步订单状态？",
	            showCancelButton: true,
	            confirmButtonColor: "#DD6B55",
	            confirmButtonText: "确认",
	            cancelButtonText: "取消",
	            closeOnConfirm: true,
	            closeOnCancel: true 
	            },
		        function (isConfirm) {
		            if (isConfirm) {
		            	$http.post("transInfoAction/syncOrder",angular.toJson($scope.settleList))
		   	   		 	.success(function(datas){
			   	   			 if(datas.bols){
			   	   				$scope.settleMsg = datas;
			   	   				$('#syncOrderModal').modal('show');
			   	   				$scope.query(1);
			   	   			 }else{
			   	   				 $scope.notice(datas.msg);
			   	   			 }
			   	   			 rowList={};
			   	   			 num=0;
		   	   		 	});
		            }else{
		            	 rowList={};
		   	   			 num=0;
		            }
	        });
	 }
	//========================================================================================
	
	//隐藏出款提示窗口
	$scope.settleModalHide = function(){
		$('#settleModal').modal('hide');
	}
	
	$scope.syncOrderModalHide = function(){
		$('#syncOrderModal').modal('hide');
	}
	
	//发票
	$scope.tradeInvoiceInfo = function(id){
		var modalScope = $scope.$new(); 	//相当于$scope
		modalScope.id = id;
		var modalInstance = $uibModal.open({
            templateUrl : 'views/trade/tradeInvoiceModalCtr.html?v='+new Date().getTime(),  //指向上面创建的视图
            controller : 'tradeInvoiceModalCtr',// 初始化模态范围
            scope:modalScope,
            size:'lg'
        })
        modalScope.modalInstance = modalInstance;
        modalInstance.result.then(function(selectedItem){ 
        	//确认操作\
        	//$state.go('trade');
        },function(){
            $log.info('取消: ' + new Date())
        })
	
	}
	 //导出信息//打开导出终端模板
	 $scope.exportInfo=function(){
		 if($scope.loadImg){
			 return;
		 }

		 var orderNo=$scope.info.orderNo;
		 if(orderNo!=null&&orderNo!=""){
			 if(orderNo.indexOf("，") != -1){
				 orderNo=orderNo.replace(/，/ig,',');
			 }
			 if(orderNo.indexOf(",") != -1){
				 var strs=orderNo.split(",");
				 if(strs!=null&&strs.length>100){
					 $scope.notice("最多只能输入100个订单号,超出" + (strs.length-100) +  "个!");
					 return;
				 }
			 }
		 }

         if(!queryCheck()) {
             return ;
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
                    	if($scope.hasPermit('trade.showMobile')){
                            $scope.info.editState=1;
						}else{
                            $scope.info.editState=0;
						}
                        $http.post("transInfoAction/exportTransInfo","info="+angular.toJson($scope.info),
                            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                            .success(function(data){
                                $scope.notice(data.msg);
                            });
                    }
            });
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

	//同步交易状态
	$scope.syncTransStatus=function(){
		SweetAlert.swal({
            title: "确定同步交易状态？",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            closeOnConfirm: true,
            closeOnCancel: true 
        },
        function (isConfirm) {
            if (isConfirm) {
            	$http.post("transInfoAction/syncTransStatus")
    			.success(function(msg){
    				if (msg.status) {
    					$scope.notice(msg.result.header.errMsg);
    				} else {
    					$scope.notice(msg.msg);
    				}
    			}).error(function(){
    				$scope.notice("同步订单状态失败");
    			});
            }
        });
	}

	//批量记账
	$scope.accountRecordBatch=function(){
		var selectList = $scope.gridApi.selection.getSelectedRows();
		if(selectList==null||selectList.length==0){
			$scope.notice("请选中要批量记账的数据!");
			return false;
		}
		var ids="";
		if(selectList!=null&&selectList.length>0){
			for(var i=0;i<selectList.length;i++){
				ids = ids + selectList[i].id + ",";
			}
		}
		ids=ids.substring(0,ids.length-1);
		SweetAlert.swal({
				title: "确定批量记账？",
				showCancelButton: true,
				confirmButtonColor: "#DD6B55",
				confirmButtonText: "确定",
				cancelButtonText: "取消",
				closeOnConfirm: true,
				closeOnCancel: true
			},
			function (isConfirm) {
				if (isConfirm) {
					$http.post("transInfoAction/accountRecordBatch","ids="+ids,
						{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
						.success(function(data){
							if(data.status){
								$scope.notice(data.msg);
								$scope.query(1);
							}else{
								$scope.notice(data.msg);
							}
						})
						.error(function(data){
							$scope.notice(data.msg);
						});
				}
			});
	}
	//页面绑定回车事件
	$document.bind("keypress", function(event) {
		$scope.$apply(function (){
			if(event.keyCode == 13){
				$scope.query(1);
			}
		})
	});
	
	
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
}).filter('freezeStatuss', function () {
	return function (value) {
		switch(value) {
			case "0" :
				return "正常";
				break;
			case "1" :
				return "风控冻结";
			case "2" :
				return "活动冻结";	
			case "3" :
				return "财务冻结";		
				break;
		}
	}
})

angular.module('inspinia').controller('tradeInvoiceModalCtr',function($scope,$http,$state,$stateParams,$compile,$uibModal,$log,i18nService){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文 
	$scope.solutionModalClose=function(){
		 $scope.modalInstance.dismiss();
	 }

	//默认隐藏
	$scope.merchantAddress = true;

	/*
	 * 保存小票（转换成.jpg图片输出）
	 */
	$scope.out_save_to_img = function(){
		var token = $("meta[name='_csrf']").attr("content");  
		var header = $("meta[name='_csrf_header']").attr("content");
		$("#token").val(token);
		$("#header").val(header);
		if($("#trade_preview_sign_prt").html().length > 10){
	    	  //保存小票2
			 var json = getFormJson(true);
			 $("#inputInfo").val(json);
			 $("#saveForm").attr("action","transInfoAction/download");
			 $("#saveForm").submit();
	      }else{
				var json = getFormJson(false);
				 $("#inputInfo").val(json);
				 $("#saveForm").attr("action","transInfoAction/download_one");
				 $("#saveForm").submit();
	      }
	 }
	//组装form请求JSON数据
	function getFormJson(isFmtAmount){
		 var json = "{";
		    var transTime = document.getElementById('transTime').value;
	        var transAmount = document.getElementById('transAmount').value;
	        if(isFmtAmount){
	   		    transAmount =  fmoney(transAmount,2);
	        }else{
	        	 transAmount =  "RMB "+transAmount;
	        }
	        var bankName = document.getElementById('bankName').value;
	        var card_no = $scope.infoDetail.accountNo;
	        json +='"transTime":"'+transTime+'",';
	        json +='"transAmount":"'+transAmount+'",';
	        json +='"bankName":"'+bankName+'",';
	        json +='"card_no":"'+card_no+'",';
			if(!$scope.myCheck){
				json +='"myCheck":"1",';
	           }
	           if(!$scope.acqSerialNoCheck){
	            json +='"acqSerialNoCheck":"1",';
	           }
	          if(!$scope.merchantAddress){
	        	  json +='"merchantAddress":"1",';
	            }
	          if(!$scope.showAcqTerminalNo){
	        	  json +='"showAcqTerminalNo":"1",';
	          }
	          if(!$scope.showYsTerminalNo){
	        	  json +='"showYsTerminalNo":"1",';
	          }
	          if($scope.infoDetail.signImg != null){
	        		  json +='"issignImg":"1",';
	          }
	          json +='"acqMerchantName":"'+($scope.infoDetail.acqMerchantName || '')+'",';
	          json +='"operatorNo":"'+($scope.operatorNo || '')+'",';
	          json +='"acqEnname":"'+($scope.infoDetail.acqEnname || '')+'",';
	          json +='"acqMerchantNo":"'+($scope.infoDetail.acqMerchantNo || '')+'",';
	          json +='"acqTerminalNo":"'+($scope.infoDetail.acqTerminalNo || '')+'",';
	          json +='"ysTerminalNo":"'+($scope.ysTerminalNo || '')+'",';
	          json +='"acqAuthNo":"'+($scope.infoDetail.acqAuthNo || '')+'",';
	          json +='"acqReferenceNo":"'+($scope.infoDetail.acqReferenceNo || '')+'",';
	          json +='"acqSerialNo":"'+($scope.infoDetail.acqSerialNo || '')+'",';
	          json +='"batchNo":"'+($scope.infoDetail.acqBatchNo || '')+'",';
	          json +='"address":"'+($scope.infoDetail.address || '')+'",';
	          if(($scope.infoDetail.teamId=='100090'||$scope.infoDetail.teamId=='100070')&&$scope.infoDetail.transAmount<=1000){
                  json +='"isDisplay":"1",';
                  json +='"readCard":"'+$scope.infoDetail.readCard+'",';
                  json +='"orderType":"'+$scope.infoDetail.orderType+'",';
              }else{
                  json +='"isDisplay":"0",';
			  }
	          json +='"teamId":"'+$scope.infoDetail.teamId +'",';
	          json +='"signImg":"'+$scope.infoDetail.signImg+'"';
	          json +='}';
	          return json;
	}
	 /**
	  * 小票打印(有签名)
	  */
	 $scope.solutionModalOk = function(){
		
	      if($("#trade_preview_sign_prt").html().length > 10){
	    	  var str =  $("#trade_preview_sign_prt").html();
	    	  var  oPop = window.open('','oPop'); 
		      oPop.document.write(str);
		      setTimeout(txtPrint(), 1000);
		      setTimeout(function () {
		    	  oPop.print();  
		    	  oPop.close();   
		    	  }, 1000);
		     
	      }else{
	    	  var str =  $("#trade_preview_sign").html();
	    	  var oPop = window.open('','oPop'); 
		      oPop.document.write(str);  
		      setTimeout(function () {
		    	  oPop.print();  
		    	  oPop.close();   
		    	  }, 1000);
	      }
	 }
	 /**
	  * 小票打印(无签名)
	  */
	 $scope.no_sign_print = function(){
		 if($("#trade_preview_prt").html().length > 10){
			 var oPop = window.open('','oPop'); 
		      var str =  $("#trade_preview_prt").html();
		      oPop.document.write(str);  
		      oPop.print();  
		      oPop.close();   
	      }else{
	    	  var oPop = window.open('','oPop'); 
		      var str =  $("#trade_preview").html();
		      oPop.document.write(str);  
		      oPop.print();  
		      oPop.close();   
	      }
	 }
	 /**
	  * 生成小票1（有签名）
	  */
	 $scope.createInvoic_one = function(){
	        var transTime = document.getElementById('transTime').value;
	        var transAmount = document.getElementById('transAmount').value;
	        var bankName = document.getElementById('bankName').value;
	        var card_no = $scope.infoDetail.accountNo;
	        var str = '<!DOCTYPE html>'  
	           str +='<html>'  
	           str +='<head>'  
	           str +='<meta charset="utf-8">' 
	           str +='<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">'  
	           str+='<style>';  
               str+='body{ font-size:9px;     font-family:"宋体";}h5{ margin: 0; padding: 10px 0;}.form-d{ overflow: hidden;padding-right:5px;}.form-d label{ float: left;font-weight:normal;font-size:9px}.form-d div{font-size:9px}</style></head>'; 
	           str +='<body>'
	           str +='<div><div class="form-d"><h5 style="text-align:center; font-size:20px; margin-bottom:10px;">POS签购单</h5></div><div class="form-d" style="border-bottom:1px solid #bbb; border-top:1px solid #bbb; margin-bottom:10px; line-height:18px;"><span style="float: left; padding:0；font-size:x-small;font-size:9px; font-weight: normal; padding:0">商户存根</span><span style="padding:0;float: right;font-weight: normal;font-size: 9px;float:right; padding-right:5px;">MERCHANT COPY</span></div>'
	           str +='<div><div class="form-d"><label>商户名称：</label><div>'+($scope.infoDetail.acqMerchantName || '')+'</div></div>'
	           str +='<div class="form-d"><label>商户编号：</label><div>'+($scope.infoDetail.acqMerchantNo || '')+'</div></div>'
	           if(!$scope.showAcqTerminalNo){
	        	   str +='<div class="form-d"><label>终端编号：</label><div>'+($scope.infoDetail.acqTerminalNo || '')+'</div></div>'
	           }
	           if(!$scope.showYsTerminalNo){
	        	   str +='<div class="form-d"><label>终端号：</label><div>'+($scope.ysTerminalNo|| '')+'</div></div>'
	           }
	           str +='<div class="form-d"><label>操作员号：</label><div>'+($scope.operatorNo || '')+'</div></div>'
	           str +='<div class="form-d"><label>卡号： </label><div><strong style="font-size: 14px;">'+card_no+'</strong></div></div>'
	           str +='<div class="form-d"><label>发卡行： </label><div>'+bankName+'</div></div>'
	           if(!$scope.myCheck){
	        	   str +='<div class="form-d"><label>收单行： </label><div>'+($scope.infoDetail.acqEnname || '')+'</div></div>'
	           }
	           str +=' <div class="form-d"><label>交易类型：</label><div>消费</div></div>'
	           if(!$scope.acqSerialNoCheck){
	        	   str +='<div class="form-d"><label>凭证号：</label><div>'+($scope.infoDetail.acqSerialNo || '')+'</div></div>'
	           }
	          str +='<div class="form-d"><label>批次号：</label><div>'+($scope.infoDetail.acqBatchNo || '')+'</div></div>'
	          str +='<div class="form-d"><label>检索参考号：</label><div>'+($scope.infoDetail.acqReferenceNo || '')+'</div></div>'
	          str +='<div class="form-d"><label>授权号码：</label><div>'+($scope.infoDetail.acqAuthNo || '')+'</div></div>'
	          str +='<div class="form-d"><label>时间：</label><div>'+transTime+'</div></div>'
	          if(!$scope.merchantAddress){
	        	  str +='<div class="form-d"><label>交易地址：</label><div>'+($scope.infoDetail.address || '')+'</div></div>'
	          }
	          str +='<div class="form-d"><label style="line-height:25px;">金额：</label><div><strong style="font-size: 18px;line-height:25px;">RMB '+transAmount+'</strong></div></div>'
	          str +='<div class="form-d"><label>备注：</label><div></div></div>'
	          str +='<div class="form-d" style="border-top:1px solid #bbb; margin-top:3px; padding-top:3px;"><label >持卡人签名：</label><div style="width:100%;">'
	          if($scope.infoDetail.signImg != null){
	        	  str +='<img ng-src="'+$scope.infoDetail.signImg+'" src="'+$scope.infoDetail.signImg+'" width="150px">'
	          }
	          str +='</div></div>'
	          str +='<div style="margin-bottom:5px; overflow:hidden; font-size:5px;border-bottom:1px solid #bbb; padding-bottom:5px;">'
              if(($scope.infoDetail.teamId=='100090'||$scope.infoDetail.teamId=='100070')&&$scope.infoDetail.transAmount<=1000){
                  if($scope.infoDetail.readCard=='quickPass'&&$scope.infoDetail.orderType==5){
                      str +='<label>交易金额未超1000.00元，免密免签</label><br/>'
                  }else if($scope.infoDetail.readCard=='ic'||$scope.infoDetail.readCard=='track'){
                      str +='<label>交易金额未超1000.00元，免签</label><br/>'
                  }
			  }
              str +='<label >本人确认以上交易，同意将其计入本卡账号</label><br/>'
	          str +='<label>I ACKNOWLEDGE SATISFACTORY RECEIPT OF RELATIVE GOODS/SERVICES</label></div>'
	          str +='<div style="margin-bottom:30px;"><h3 style="font-family:Microsoft Yahei; font-size:16px; font-weight:300">支付随心&nbsp;&nbsp;&nbsp;畅享随行</h3></div><div style="text-align:center">-------由此线撕开-------</div></div></div>'
	          str +='</body>'  
	          str +='</html>'  
	          transTime=document.getElementById('trade_preview_sign').innerHTML = str;
	          $("#modal-body").hide();
	          $("#modal-footer_preview").hide();
	          $("#modal-footer_print").show();
	          $("#trade_preview_sign").show();
	          //同时生成无签名小票1
	          $scope.createInvoic_one_no_sign();
	 }
	
	/**
	 * 生成小票1（无签名）
	 */
	 $scope.createInvoic_one_no_sign = function(){
	        var transTime = document.getElementById('transTime').value;
	        var transAmount = document.getElementById('transAmount').value;
	        var bankName = document.getElementById('bankName').value;
	        //打印页面
	        var card_no = $scope.infoDetail.accountNo;
	        var str = '<!DOCTYPE html>'  
	            str +='<html>'  
	            str +='<head>'  
	            str +='<meta charset="utf-8">' 
	            str +='<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">'  
	            str+='<style>';  
                str+='body{ font-size: 9px;}h5{ margin: 0; padding: 10px 0;}.form-d{ overflow: hidden;padding-right:5px;}.form-d label{ float: left}</style></head>'; 
	            str +='<body>'
	            str +='<div><div class="form-d"><h5 style="text-align:center; font-size:20px; margin-bottom:10px;">POS签购单</h5></div><div class="form-d" style="border-bottom:1px solid #bbb; border-top:1px solid #bbb; margin-bottom:10px; line-height:18px;"><h5 style="float: left; padding:0">商户存根&nbsp;&nbsp</h5><h5 style="padding:0;float: right; padding-right:5px;">MERCHANT COPY</h5></div>'
	            str +='<div><div class="form-d"><label>商户名称：</label><div>'+($scope.infoDetail.acqMerchantName || '')+'</div></div>'
	            str +='<div class="form-d"><label>商户编号：</label><div>'+($scope.infoDetail.acqMerchantNo || '')+'</div></div>'
	            if(!$scope.showAcqTerminalNo){
	            	str +='<div class="form-d"><label>终端编号：</label><div>'+($scope.infoDetail.acqTerminalNo || '')+'</div></div>'
	            }
	            if(!$scope.showYsTerminalNo){
	            	str +='<div class="form-d"><label>终端号：</label><div>'+($scope.ysTerminalNo || '')+'</div></div>'
	            }
	            str +='<div class="form-d"><label>操作员号：</label><div>'+($scope.operatorNo || '')+'</div></div>'
	            str +='<div class="form-d"><label>卡号： </label><div><strong style="font-size: 14px;">'+card_no+'</strong></div></div>'
	            str +='<div class="form-d"><label>发卡行： </label><div>'+bankName+'</div></div>'
	            if(!$scope.myCheck){
	            	 str +='<div class="form-d"><label>收单行： </label><div>'+($scope.infoDetail.acqEnname || '')+'</div></div>'
	            }
	            str +=' <div class="form-d"><label>交易类型：</label><div>消费</div></div>'
	            if(!$scope.acqSerialNoCheck){
	            	str +='<div class="form-d"><label>凭证号：</label><div>'+($scope.infoDetail.acqSerialNo || '')+'</div></div>'
	            }
	            str +='<div class="form-d"><label>批次号：</label><div>'+($scope.infoDetail.acqBatchNo || '')+'</div></div>'
	            str +='<div class="form-d"><label>检索参考号：</label><div>'+($scope.infoDetail.acqReferenceNo || '')+'</div></div>'
	            str +='<div class="form-d"><label>授权号码：</label><div>'+($scope.infoDetail.acqAuthNo || '')+'</div></div>'
	            if(!$scope.merchantAddress){
	            	str +='<div class="form-d"><label>交易地址：</label><div>'+($scope.infoDetail.address || '')+'</div></div>'
	            }
	            str +='<div class="form-d"><label>时间：</label><div>'+transTime+'</div></div>'
	            str +='<div class="form-d"><label style="line-height:25px;">金额：</label><div><strong style="font-size: 18px;line-height:25px;">RMB '+transAmount+'</strong></div></div>'
	            str +='<div class="form-d"><label>备注：</label><div></div></div>'
	            str +='<div class="form-d" style="border-top:1px solid #bbb; margin-top:3px; padding-top:3px;"><label >持卡人签名：</label><div style="width:100%; height:60px"></div></div>'
	            str +='<div style="margin-bottom:5px; overflow:hidden; font-size:5px;border-bottom:1px solid #bbb; padding-bottom:5px;">'
         		if(($scope.infoDetail.teamId=='100090'||$scope.infoDetail.teamId=='100070')&&$scope.infoDetail.transAmount<=1000){
                    if($scope.infoDetail.readCard=='quickPass'&&$scope.infoDetail.orderType==5){
                        str +='<label>交易金额未超1000.00元，免密免签</label><br/>'
                    }else if($scope.infoDetail.readCard=='ic'||$scope.infoDetail.readCard=='track'){
                        str +='<label>交易金额未超1000.00元，免签</label><br/>'
                    }
         		}
         		str +='<label >本人确认以上交易，同意将其计入本卡账号</label><br/>'
	            str +='<label>I ACKNOWLEDGE SATISFACTORY RECEIPT OF RELATIVE GOODS/SERVICES</label></div>'
	            str +='<div style="margin-bottom:30px;"><h3 style="font-family:Microsoft Yahei; font-size:16px; font-weight:300">支付随心&nbsp;&nbsp;&nbsp;畅享随行</h3></div><div style="text-align:center">-------由此线撕开-------</div></div></div>'
                str +='</body>'  
	            str +='</html>'
	         transTime=document.getElementById('trade_preview').innerHTML = str;

	 }
	 /**
	  * 生成小票2（有签名）
	  */
	 $scope.createInvoic_two = function(){
		 var transTime = document.getElementById('transTime').value;
		 var transAmount = document.getElementById('transAmount').value;
		 transAmount =  fmoney(transAmount,2);
		 var bankName = document.getElementById('bankName').value;
		 var card_no = $scope.infoDetail.accountNo;
		 var str ='<!DOCTYPE html>'
			str +='<html lang="zh_CN">'
			str +='<head>'
			str +='<meta charset="UTF-8">'
			str +='<meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">'
			str +='<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate"/>'
			str +='<meta http-equiv="Pragma" content="no-cache"/>'
			str +='<meta http-equiv="Expires" content="0"/>'
		 	str +='<title>签购单</title>'
		 	str +='<style>'
		 	str +='body{ margin: 0; padding: 0;}'
		 	str +='.main{ margin: 0 auto; padding: 10px 0}'
		 	str +='.top{ overflow: hidden; padding-bottom: 15px;}'
		 	str +='.top span{float: left}'
		 	str +='.top em{float: right; text-align: right; font-style: normal; font-size: 14px; color: #999; padding-top: 22px;}'
		 	str +='.top h2{ margin: 0 auto; width: 120px; font-size: 36px; color:#474747; font-weight: normal; line-height: 62px;}'
		 	str +='.con{color:#999; line-height: 34px; overflow: hidden; padding: 20px 0; font-size: 14px; border-bottom: 2px solid #d0d0d0}'
		 	str +='.con2{color:#999; line-height: 34px; overflow: hidden; padding: 20px 0; font-size: 14px;}'
		 	str +='.con p{overflow: hidden; margin: 0}'
		 	str +='.con2 p{overflow: hidden; margin: 0}'
		 	str +='.con2 p span{display: block}'
		 	str +='.con p span{float: left;}'
		 	str +='.con p i{float: right; font-style: normal; color: #0099cc}'
		 	str +='</style>'
		 	str +='</head>'
		 	str +='<body>'
		 	str +='<div class="main">'
		 	str +='<div class="top"><span><img src="img/unionpay.png"></span><em>商户存根<br>MERCHANT COPY</em><h2>签购单</h2>'
		 	str +='</div>'
		 	str +='<div class="con">'
		 	str +='<p><span>商户名称(MERCHANT):</span><i>'+($scope.infoDetail.acqMerchantName || '')+'</i></p>'
		 	str +='<p><span>交易金额(AMOUNT):</span><i style="font-size: 26px;">'+transAmount+'</i></p>'
		 	str +='<p><span>操作员(OPEATOR):</span><i>'+($scope.operatorNo || '')+'</i></p>'
		 	str +='<p><span>交易类型(TRANTS.TYPE):</span><i>消费</i></p>'
		 	str +='<p><span>卡号(CARD NUMBER)</span><i style="font-size: 18px;">'+card_no+'</i></p>'
		    str +='<p><span>发卡行(CARD ISSURE):</span><i>'+bankName+'</i></p>'
		 	if(!$scope.myCheck){
		 		str +='<p><span>收单机构(ACQ NO.):</span><i>'+($scope.infoDetail.acqEnname || '')+'</i></p>'
		 	}
		 	str +='</div>'
		 	str +='<div class="con">'
		 	str +='<p><span>商户编号(MERCHANT NO.):</span><i>'+($scope.infoDetail.acqMerchantNo || '')+'</i></p>'
		 	if(!$scope.showAcqTerminalNo){
		 		str +='<p><span>终端编号(TERMINAL ID.):</span><i>'+($scope.infoDetail.acqTerminalNo || '')+'</i></p>'
		 	}
		 	if(!$scope.showYsTerminalNo){
		 		str +='<p><span>终端号(TERMINAL ID.):</span><i>'+($scope.ysTerminalNo || '')+'</i></p>'
		 	}
		 	str +='<p><span>授权号(AUTH CODE):</span><i>'+($scope.infoDetail.acqAuthNo || '')+'</i></p>'
		 	str +='<p><span>参考号(REF.NO.):</span><i>'+($scope.infoDetail.acqReferenceNo || '')+'</i></p>'
		 	if(!$scope.acqSerialNoCheck){
		 		str +='<p><span>凭证号(VOUCHER NO.):</span><i>'+($scope.infoDetail.acqSerialNo || '')+'</i></p>'
		 	}
		 	str +='<p><span>批次号(BATCH NO.):</span><i>'+($scope.infoDetail.acqBatchNo || '')+'</i></p>'
		 	
		 	str +='<p><span>交易时间(TRANS.TIME):</span><i>'+transTime+'</i></p>'
		 	if(!$scope.merchantAddress){
	 			str +='<p><span>交易地址(LOCATION):</span><i>'+($scope.infoDetail.address || '')+'</i></p>'
	 		}
		 	str +='<p><span>备注:</span><i></i></p>'
		 	str +='</div>'
		 	str +='<div class="con2">'
		 	str +='<p><span>持卡人签名(SIGNATURE):</span>'
		 	str +='<div style="display: block; margin: 0 auto; text-align: center">'
		 	if($scope.infoDetail.signImg != null){
			   str +='<img src="'+$scope.infoDetail.signImg+'" height="120px;">'   
		 	}
		 	str +='</div></p>'
		 	str +='<p style="text-align: center; line-height: 24px;">'
            if(($scope.infoDetail.teamId=='100090'||$scope.infoDetail.teamId=='100070')&&$scope.infoDetail.transAmount<=1000){
                if($scope.infoDetail.readCard=='quickPass'&&$scope.infoDetail.orderType==5){
                    str +='交易金额未超1000.00元，免密免签<br/>'
                }else if($scope.infoDetail.readCard=='ic'||$scope.infoDetail.readCard=='track'){
                    str +='交易金额未超1000.00元，免签<br/>'
                }

            }
            str +='本人确认以上交易，同意将其计入本卡账户<br>I ACKNOWLEDGE SATISFACTORY RECEIPT OF RELATIVE GOODS/SERVICES</p>'
		 	str +='</div></div></body></html>'
		document.getElementById('trade_preview_sign').innerHTML = str;
         $("#modal-body").hide();
         $("#modal-footer_preview").hide();
         $("#modal-footer_print").show();
         $("#trade_preview_sign").show();
         //添加打印样式
         var strPrt =  '<!DOCTYPE html>'
	    	  strPrt +='<html lang="zh_CN">'
	    		  strPrt +='<head>'
	    			  strPrt +='<meta charset="UTF-8">'
	    				  strPrt +='<meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">'
	    					  strPrt +='<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate"/>'
	      	strPrt +='<meta http-equiv="Pragma" content="no-cache"/>'
	      	strPrt +='<meta http-equiv="Expires" content="0"/>'
	      	strPrt +='<title>签购单</title>'
	      	strPrt +='<style>'
	      		strPrt +='body{ margin: 0; padding: 0; color:#000;}'
	      		strPrt +='.main{ margin: 0 auto; padding: 10px 0}'
	      		strPrt +='.top{ overflow: hidden; padding-bottom: 15px;}'
	      		strPrt +='.top span{float: left}'
	      		strPrt +='.top_em{float: right; text-align: right;  font-size: 10px; margin-right:10px;margin-top:-40px;}'
	      		strPrt +='.top h2{font-size: 16px;  font-weight: normal;margin-top:5;}'
	      		strPrt +=' p{overflow: hidden; margin: 0;font-size: 10px;padding-right:5px;}'
	      		strPrt +=' p span{display: block;font-size: 10px;}'
	      		strPrt +=' p span{float: left;font-size: 10px;}'
	      		strPrt +=' p i{float: left;font-size: 10px;padding-left:5px;font-style: normal;}'
	      	strPrt +='</style>'
	      strPrt +='</head>'
	      strPrt +='<body>'
	      	strPrt +='<div class="main">'
	      		strPrt +='<div class="top">'
	      			strPrt +='<span><img src="img/unionpay.png" width="60px;"></span>'
	      			strPrt +='<h2>签购单</h2>'
	      		strPrt +='</div>'
	      			strPrt +='<div class="top_em">商户存根<br>MERCHANT COPY</div>'
	      			strPrt +='<p>'
	      				strPrt +='<span>商户名称: '+($scope.infoDetail.acqMerchantName || '')+'</span>'
	      			strPrt +='</p>'
	      			strPrt +='<p>'
	      				strPrt +='<span>交易金额:</span>'
	      				strPrt +='<i>'+transAmount+'</i>'
	      			strPrt +='</p>'
	      			strPrt +='<p>'
	      				strPrt +='<span>操作员:</span>'
	      				strPrt +='<i>'+($scope.operatorNo || '')+'</i>'
	      			strPrt +='</p>'
	      			strPrt +='<p>'
	      				strPrt +='<span>交易类型:</span>'
	      				strPrt +='<i>消费</i>'
	      			strPrt +='</p>'
	      			strPrt +='<p>'
	      				strPrt +='<span>卡号:</span>'
	      				strPrt +='<i>'+card_no+'</i>'
	      			strPrt +='</p>'
	      			strPrt +='<p>'
	      				strPrt +='<span>发卡行:</span>'
	      				strPrt +='<i>'+bankName+'</i>'
	      			strPrt +='</p>'
	      				if(!$scope.myCheck){
	      			strPrt +='<p>'
	      				strPrt +='<span>收单机构:</span>'
	      				strPrt +='<i>'+($scope.infoDetail.acqEnname || '')+'</i>'
	      					strPrt +='</p>'
	      				}
	      						strPrt +='<p>'
	      							strPrt +='<span>商户编号:</span>'
	      								strPrt +='<i>'+($scope.infoDetail.acqMerchantNo || '')+'</i>'
	      									strPrt +='</p>'
	      										strPrt +='<p>'
	      				if(!$scope.showAcqTerminalNo){
		      				strPrt +='<span>终端编号:</span>'
		      				strPrt +='<i>'+($scope.infoDetail.acqTerminalNo || '')+'</i>'
	      				}
  						if(!$scope.showYsTerminalNo){
  							strPrt +='<span>终端号:</span>'
  								strPrt +='<i>'+($scope.ysTerminalNo || '')+'</i>'
  						}
	      			strPrt +='</p>'
	      			strPrt +='<p>'
	      				strPrt +='<span>授权号:</span>'
	      				strPrt +='<i>'+($scope.infoDetail.acqAuthNo || '')+'</i>'
	      			strPrt +='</p>'
	      			strPrt +='<p>'
	      				strPrt +='<span>参考号:</span>'
	      				strPrt +='<i>'+($scope.infoDetail.acqReferenceNo || '')+'</i>'
	      			strPrt +='</p>'
	      				if(!$scope.acqSerialNoCheck){
	      			strPrt +='<p>'
	      				strPrt +='<span>凭证号:</span>'
	      				strPrt +='<i>'+($scope.infoDetail.acqSerialNo || '')+'</i>'
	      			strPrt +='</p>'
	      				}
	      			strPrt +='<p>'
	      				strPrt +='<span>批次号:</span>'
	      				strPrt +='<i>'+($scope.infoDetail.acqBatchNo || '')+'</i>'
	      			strPrt +='</p>'
	      				
	      			strPrt +='<p>'
	      				strPrt +='<span>交易时间:</span>'
	      				strPrt +='<i>'+transTime+'</i>'
	      			strPrt +='</p>'
	      				if(!$scope.merchantAddress){
	      					strPrt +='<p><span>交易地址:</span><i>'+($scope.infoDetail.address || '')+'</i></p>'
	    		 		}
	      			strPrt +='<p>'
	      				strPrt +='<span>备注:</span>'
	      				strPrt +='<i>'+($scope.infoDetail.remark || '')+'</i>'
	      			strPrt +='</p>'
	      				strPrt +='<p>'
	      				strPrt +='<span>持卡人签名(SIGNATURE):</span>'
	      					strPrt +='<div style="display: block; margin: 0 auto; text-align: center;">'
	      						if($scope.infoDetail.signImg != null){
	      						   strPrt +='<img src="'+$scope.infoDetail.signImg+'" width="140px; display:block;margin：0 auto">'   
	      					 	}
	      							strPrt +='</div>'
	      								strPrt +='</p>'
	      									strPrt +='<p style="line-height: 16px;margin-right: 10px; padding-bottom:20px; border-bottom:1px solid #ccc;">'
         									if(($scope.infoDetail.teamId=='100090'||$scope.infoDetail.teamId=='100070')&&$scope.infoDetail.transAmount<=1000){
                                                if($scope.infoDetail.readCard=='quickPass'&&$scope.infoDetail.orderType==5){
                                                    strPrt +='交易金额未超1000.00元，免密免签<br/>'
                                                }else if($scope.infoDetail.readCard=='ic'||$scope.infoDetail.readCard=='track'){
                                                    strPrt +='交易金额未超1000.00元，免签<br/>'
                                                }
											}
         									strPrt +='本人确认以上交易，同意将其计入本卡账户<br>I ACKNOWLEDGE SATISFACTORY RECEIPT OF RELATIVE GOODS/SERVICES</p>'
	      										strPrt +='</div>'
	      											strPrt +=' </body>'
	      												strPrt +='</html>';
	      							document.getElementById('trade_preview_sign_prt').innerHTML = strPrt;
         //同时生成无签名小票2
         $scope.createInvoic_two_no_sign();
	 }
	 /**
	  * 生成小票2（无签名）
	  */
	 $scope.createInvoic_two_no_sign = function(){
		    var transTime = document.getElementById('transTime').value;
	        var transAmount = document.getElementById('transAmount').value;
	        transAmount =  fmoney(transAmount,2);
	        var bankName = document.getElementById('bankName').value;
	        var card_no = $scope.infoDetail.accountNo;
		    var str ='<!DOCTYPE html>'
			    str +='<html lang="zh_CN">'
				str +='<head>'
				str +='<meta charset="UTF-8">'
			    str +='<meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">'
			    str +='<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate"/>'
			    str +='<meta http-equiv="Pragma" content="no-cache"/>'
			    str +='<meta http-equiv="Expires" content="0"/>'
		 	    str +='<title>签购单</title>'
		 	    str +='<style>'
		 	    str +='body{ margin: 0; padding: 0; }'
		 		str +='.main{  margin: 0 auto; padding: 10px 0}'
		 		str +='.top{ overflow: hidden; padding-bottom: 15px;}'
		 		str +='.top span{float: left}'
		 		str +='.top em{float: right; text-align: right; font-style: normal; font-size: 14px; color: #999; padding-top: 22px;}'
		 		str +='.top h2{ margin: 0 auto; width: 120px; font-size: 36px; color:#474747; font-weight: normal; line-height: 62px;}'
		 		str +='.con{color:#999; line-height: 34px; overflow: hidden; padding: 20px 0; font-size: 14px; border-bottom: 2px solid #d0d0d0}'
		 		str +='.con2{color:#999; line-height: 34px; overflow: hidden; padding: 20px 0; font-size: 14px;}'
		 		str +='.con p{overflow: hidden; margin: 0}'
		 		str +='.con2 p{overflow: hidden; margin: 0}'
		 		str +='.con2 p span{display: block}'
		 		str +='.con p span{float: left;}'
		 		str +='.con p i{float: right; font-style: normal; color: #0099cc}'
		 		str +='</style>'
		 		str +='</head>'
		 		str +='<body>'
		 		str +='<div class="main">'
		 		str +='<div class="top">'
		 		str +='<span><img src="img/unionpay.png"></span><em>商户存根<br>MERCHANT COPY</em><h2>签购单</h2>'
		 		str +='</div>'
		 		str +='<div class="con">'
		 		str +='<p><span>商户名称(MERCHANT):</span><i>'+($scope.infoDetail.acqMerchantName || '')+'</i></p>'
		 		str +='<p><span>交易金额(AMOUNT):</span><i style="font-size: 26px;">'+transAmount+'</i></p>'
		 		str +='<p><span>操作员(OPEATOR):</span><i>'+($scope.operatorNo || '')+'</i></p>'
		 		str +='<p><span>交易类型(TRANTS.TYPE):</span><i>消费</i></p>'
		 		str +='<p><span>卡号(CARD NUMBER)</span><i style="font-size: 18px;">'+card_no+'</i></p>'
		 		str +='<p><span>发卡行(CARD ISSURE):</span><i>'+bankName+'</i></p>'
		 		if(!$scope.myCheck){
		 			str +='<p><span>收单机构(ACQ NO.):</span><i>'+($scope.infoDetail.acqEnname || '')+'</i></p>'
		 		}
		 		str +='</div>'
		 		str +='<div class="con">'
		 		str +='<p><span>商户编号(MERCHANT NO.):</span><i>'+($scope.infoDetail.acqMerchantNo || '')+'</i></p>'
		 		str +='<p><span>终端号(TERMINAL ID.):</span><i>'+($scope.infoDetail.acqTerminalNo || '')+'</i></p>'
		 		str +='<p><span>授权号(AUTH CODE):</span><i>'+($scope.infoDetail.acqAuthNo || '')+'</i></p>'
		 		str +='<p><span>参考号(REF.NO.):</span><i>'+($scope.infoDetail.acqReferenceNo || '')+'</i></p>'
		 		if(!$scope.acqSerialNoCheck){
		 			str +='<p><span>凭证号(VOUCHER NO.):</span><i>'+($scope.infoDetail.acqSerialNo || '')+'</i></p>'
		 		}
		 		str +='<p><span>批次号(BATCH NO.):</span><i>'+($scope.infoDetail.acqBatchNo || '')+'</i></p>'
		 		
		 		str +='<p><span>交易时间(TRANS.TIME):</span><i>'+transTime+'</i></p>'
		 		if(!$scope.merchantAddress){
		 			str +='<p><span>交易地址(LOCATION):</span><i>'+($scope.infoDetail.address || '')+'</i></p>'
		 		}
		 		str +='<p><span>备注:</span><i></i></p>'
		 		str +='</div>'
		 		str +='<div class="con2">'
		 		str +='<p><span>持卡人签名(SIGNATURE):</span><div style="display: block; margin: 0 auto; text-align: center">'
		 		str +='</div></p>'

		 		str +='<p style="text-align: center; line-height: 24px;">'
         		if(($scope.infoDetail.teamId=='100090'||$scope.infoDetail.teamId=='100070')&&$scope.infoDetail.transAmount<=1000){
                    if($scope.infoDetail.readCard=='quickPass'&&$scope.infoDetail.orderType==5){
                        str +='交易金额未超1000.00元，免密免签<br/>'
                    }else if($scope.infoDetail.readCard=='ic'||$scope.infoDetail.readCard=='track'){
                        str +='交易金额未超1000.00元，免签<br/>'
                    }
         		}
         		str +='本人确认以上交易，同意将其计入本卡账户<br>I ACKNOWLEDGE SATISFACTORY RECEIPT OF RELATIVE GOODS/SERVICES</p>'
		 		str +='</div></div></body></html>'
		 	document.getElementById('trade_preview').innerHTML = str;
		 		//添加打印样式
		         var strPrt =  '<!DOCTYPE html>'
			    	 strPrt +='<html lang="zh_CN">'
			    	 strPrt +='<head>'
			    	 strPrt +='<meta charset="UTF-8">'
			    	 strPrt +='<meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">'
			    	strPrt +='<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate"/>'
			      	strPrt +='<meta http-equiv="Pragma" content="no-cache"/>'
			      	strPrt +='<meta http-equiv="Expires" content="0"/>'
			      	strPrt +='<title>签购单</title>'
			      	strPrt +='<style>'
			      		strPrt +='body{ margin: 0; padding: 0; color:#000;}'
			      		strPrt +='.main{ margin: 0 auto; padding: 10px 0}'
			      		strPrt +='.top{ overflow: hidden; padding-bottom: 15px;}'
			      		strPrt +='.top span{float: left}'
			      		strPrt +='.top_em{float: right; text-align: right;  font-size: 10px; margin-right:10px;margin-top:-40px;}'
			      		strPrt +='.top h2{font-size: 16px;  font-weight: normal;margin-top:5;}'
			      		strPrt +=' p{overflow: hidden; margin: 0;font-size: 10px;padding-right:5px;}'
			      		strPrt +=' p span{display: block;font-size: 10px;}'
			      		strPrt +=' p span{float: left;font-size: 10px;}'
			      		strPrt +=' p i{float: left;font-size: 10px;padding-left:5px;font-style: normal;}'
			      			strPrt +='</style>'
			      			strPrt +='</head>'
			      			strPrt +='<body>'
			      			strPrt +='<div class="main">'
			      			strPrt +='<div class="top">'
			      			strPrt +='<span><img src="img/unionpay.png" width="60px;"></span>'
			      			strPrt +='<h2>签购单</h2>'
			      			strPrt +='</div>'
			      			strPrt +='<div class="top_em">商户存根<br>MERCHANT COPY</div>'
			      			strPrt +='<p>'
			      			strPrt +='<span>商户名称: '+($scope.infoDetail.acqMerchantName || '')+'</span><i></i>'
			      			strPrt +='</p>'
			      			strPrt +='<p>'
			      			strPrt +='<span>交易金额:</span>'
			      			strPrt +='<i>'+transAmount+'</i>'
			      			strPrt +='</p>'
			      			strPrt +='<p>'
			      			strPrt +='<span>操作员:</span>'
			      			strPrt +='<i>'+($scope.operatorNo || '')+'</i>'
			      			strPrt +='</p>'
			      			strPrt +='<p>'
			      			strPrt +='<span>交易类型:</span>'
			      			strPrt +='<i>消费</i>'
			      			strPrt +='</p>'
			      			strPrt +='<p>'
			      			strPrt +='<span>卡号:</span>'
			      			strPrt +='<i>'+card_no+'</i>'
			      			strPrt +='</p>'
			      			strPrt +='<p>'
			      			strPrt +='<span>发卡行:</span>'
			      			strPrt +='<i>'+bankName+'</i>'
			      			strPrt +='</p>'
			      				if(!$scope.myCheck){
			      					strPrt +='<p>'
						      			strPrt +='<span>收单机构:</span>'
						      			strPrt +='<i>'+($scope.infoDetail.acqEnname || '')+'</i>'
						      			strPrt +='</p>'
			      				}
			      			
			      			strPrt +='<p>'
			     			strPrt +='<span>商户编号:</span>'
			      			strPrt +='<i>'+($scope.infoDetail.acqMerchantNo || '')+'</i>'
			      			strPrt +='</p>'
			      			strPrt +='<p>'
			      			if(!$scope.showAcqTerminalNo){
				      			strPrt +='<span>终端编号:</span>'
				      			strPrt +='<i>'+($scope.infoDetail.acqTerminalNo || '')+'</i>'
			      			}
			      			if(!$scope.showYsTerminalNo){
			      				strPrt +='<span>终端号:</span>'
			      				strPrt +='<i>'+($scope.ysTerminalNo || '')+'</i>'
			      			}
			      			strPrt +='</p>'
			      			strPrt +='<p>'
			      			strPrt +='<span>授权号:</span>'
			      			strPrt +='<i>'+($scope.infoDetail.acqAuthNo || '')+'</i>'
			      			strPrt +='</p>'
			      			strPrt +='<p>'
			      			strPrt +='<span>参考号:</span>'
			    			strPrt +='<i>'+($scope.infoDetail.acqReferenceNo || '')+'</i>'
			      			strPrt +='</p>'
			      				if(!$scope.acqSerialNoCheck){
			      					strPrt +='<p>'
						      			strPrt +='<span>凭证号:</span>'
						      			strPrt +='<i>'+($scope.infoDetail.acqSerialNo || '')+'</i>'
						      			strPrt +='</p>'
			      				}
			      			
			      			strPrt +='<p>'
			      			strPrt +='<span>批次号:</span>'
			      			strPrt +='<i>'+($scope.infoDetail.acqBatchNo || '')+'</i>'
			      			strPrt +='</p>'
			      			strPrt +='<p>'
			      			strPrt +='<span>交易时间:</span>'
			      			strPrt +='<i>'+transTime+'</i>'
			      			strPrt +='</p>'
			      				if(!$scope.merchantAddress){
			      					strPrt +='<p><span>交易地址:</span><i>'+($scope.infoDetail.address || '')+'</i></p>'
			    		 		}
			      			strPrt +='<p>'
			      			strPrt +='<span>备注:</span>'
			      			strPrt +='<i>'+($scope.infoDetail.remark || '')+'</i>'
			      			strPrt +='</p>'
			      			strPrt +='<p>'
			      			strPrt +='<span>持卡人签名(SIGNATURE):</span>'
			      			strPrt +='<div style="display: block; margin: 0 auto; text-align: center;">'
			      			strPrt +='</div>'
			      			strPrt +='</p>'
			      			strPrt +='<div style="height:40px;"></div>'
			      			strPrt +='<p style=" line-height: 16px;margin-right: 10px; padding-bottom:20px; border-bottom:1px solid #ccc; ">'
         					if(($scope.infoDetail.teamId=='100090'||$scope.infoDetail.teamId=='100070')&&$scope.infoDetail.transAmount<=1000){
                                if($scope.infoDetail.readCard=='quickPass'&&$scope.infoDetail.orderType==5){
                                    strPrt +='交易金额未超1000.00元，免密免签<br/>'
                                }else if($scope.infoDetail.readCard=='ic'||$scope.infoDetail.readCard=='track'){
                                    strPrt +='交易金额未超1000.00元，免签<br/>'
                                }
							}
         					strPrt +='本人确认以上交易，同意将其计入本卡账户<br>I ACKNOWLEDGE SATISFACTORY RECEIPT OF RELATIVE GOODS/SERVICES</p>'
			      			strPrt +='</div>'
			      			strPrt +=' </body>'
			      			strPrt +='</html>';
			      							document.getElementById('trade_preview_prt').innerHTML = strPrt;
	 }
	 /**格式化输出银行卡信息（只显示前6位和最后4位，其余*代替）*/
	 function format_card_no(accountNo){
		 if(accountNo == null){
			 return "";
		 }
		 var accountArr = accountNo.split("/");
         var card_no = "";
         for(var index = 0;index < accountArr.length;index++){
        	 if(index == 0){
        		 var no_arr = accountArr[index].split("");
        		 for (var int = 0; int < no_arr.length; int++) {
     				if(int <= 5 || (int >= no_arr.length - 4 && int <= no_arr.length - 1)){
     					card_no += no_arr[int];
     				}else{
     					card_no += "*";
     				}
     			}
        	 }else{
        		 card_no += "/"+accountArr[index];
        	 }
         }
         return card_no;
	 }
	 /**
	  * 格式化金额没3位以，分割
	  */
	 function fmoney(s, n)   
	 {   
	    n = n > 0 && n <= 20 ? n : 2;   
	    s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";   
	    var l = s.split(".")[0].split("").reverse(),   
	    r = s.split(".")[1];   
	    t = "";   
	    for(i = 0; i < l.length; i ++ )   
	    {   
	       t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");   
	    }   
	    return t.split("").reverse().join("") + "." + r;   
	 }
	 
	 $http.get('transInfoAction/queryInfoDetail?ids='+$scope.id)
	    .success(function(largeLoad) {
	    	if(!largeLoad.bols){
	    		$scope.notice(largeLoad.msg);
	    	}else{
	    		$scope.infoDetail=largeLoad.tt;
	    		if($scope.infoDetail.accountNo){
	    			$scope.infoDetail.accountNo=($scope.infoDetail.accountNo || '')+"/"
	    			if($scope.infoDetail.readCard == 'quickPass'){//非接
	    				$scope.infoDetail.accountNo=($scope.infoDetail.accountNo || '')+"C"
	    			}
	    			if($scope.infoDetail.isIccard){
	    				$scope.infoDetail.accountNo=($scope.infoDetail.accountNo || '')+"I"
	    			}else{
	    				$scope.infoDetail.accountNo=($scope.infoDetail.accountNo || '')+"S"
	    			}
	    			$scope.infoDetail.accountNo=format_card_no($scope.infoDetail.accountNo);
	    		}
		        if($scope.infoDetail.transAmount){
		        	$scope.infoDetail.transAmount=$scope.infoDetail.transAmount.toFixed(2);
		        }
		        $scope.infoDetail.transType="消费";
		        $scope.operatorNo="01";
	    		$scope.pcb=largeLoad.pcb;
	    		$scope.pcb1=largeLoad.pcb1;
	    		$scope.ysTerminalNo=largeLoad.ysTerminalNo;

	    		//终端号为空则隐藏终端号
	    		if ($scope.ysTerminalNo == null || $scope.ysTerminalNo == '') {
	    			$scope.showYsTerminalNo = true;
	    		} else {
	    			$scope.showAcqTerminalNo = true;
	    		}
	    	}
	    });

	$scope.changeTerminalNo = function(type){
		if (type == 1) {
			$scope.showYsTerminalNo = !$scope.showYsTerminalNo;
		} else {
			$scope.showAcqTerminalNo = !$scope.showAcqTerminalNo;
		}
	}

});
