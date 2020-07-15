/**
 * 欢乐返商户查询
 */
angular.module('inspinia',['angularFileUpload','infinity.angular-chosen']).controller('happyBackCtrl',function($scope,$http,$state,$stateParams,i18nService,$document,SweetAlert,FileUploader,$timeout){
	//数据源
	i18nService.setCurrentLang('zh-cn');
	$scope.acqOrgs=[{text:"全部",value:""}];
	$scope.paginationOptions=angular.copy($scope.paginationOptions);

	$scope.resetForm=function(){
		$scope.baseInfo = {merchantN:"",agentNode:"",status:"",discountStatus:-1,checkStatus:"",oneAgentNo:"",
			activeTimeStart:moment(new Date().getTime() - 24 * 3600 * 1000).format('YYYY-MM-DD'+' 00:00:00'),
			activeTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD'+' 23:59:59'),
			billingTimeStart:"",
			billingTimeEnd:"",
			billingStatus:"",
			enterTimeStart:"",
			enterTimeEnd:"",recommendedSource:"",
			activityCode:"",liquidationStatus:"",accountCheckStatus:"",merchantType:"1",agentN:'',
			isStandard: '',repeatRegister:null,
			minCumulateTransAmount:'', maxCumulateTransAmount:'',
			minTransTotal:'', maxTransTotal:'',
			minFullAmount:'', maxFullAmount:'',
			minEmptyAmount:'', maxEmptyAmount:'',
			minStandardTime:'', maxStandardTime:'',
			minMinusAmountTime:'', maxMinusAmountTime:'',
			minAddAmountTime:'', maxAddAmountTime:'',acqOrgId:'',countAll:false,pageAll:false,checkIds:null,isExclusion:-1};
		$scope.disabledMerchantType=true;
		isVerifyTime = 1;
	}
	$scope.resetForm();
	//活动类型
	$scope.subjectTypes = [{text:'全部',value:''},{text:'欢乐返-循环送',value:'008'},{text:'欢乐返',value:'009'}];
	//$scope.subjectTypes = [{text:'全部',value:''},{text:'欢乐返-循环送',value:'008'},{text:'欢乐返',value:'009'},{text:'欢乐返128',value:'021'}];
	$scope.settleStatus = [{text:'全部',value:''},{text:'同意',value:'1'},{text:'不同意',value:'2'},{text:'未核算',value:'3'}];
	$scope.activityStatus = [{text:'全部',value:''},{text:'未激活',value:'1'},{text:'已激活',value:'2'},{text:'已返鼓励金',value:'6'},{text:'已扣款',value:'7'},{text:'预调账已发起',value:'8'},{text:'已奖励',value:'9'}];
	$scope.isStandards = [{text:'全部',value:''},{text:'未达标',value:'0'},{text:'已达标',value:'1'}];
	// $scope.recommendedSources=[{text:"全部",value:""},{text:"正常注册",value:"0"},{text:"微创业",value:"1"},{text:"代理商分享",value:"2"},{text:"超级盟主",value:"3"}];
	$scope.repeatRegisters = [{text:'全部',value:null},{text:'否',value:'0'},{text:'是',value:'1'}];
	$scope.billingStatuss = [{text:'全部',value:''},{text:'未入账',value:'0'},{text:'已入账',value:'1'}];
    $scope.exclusionStatuss = [{text:'全部',value:-1},{text:'不互斥',value:1},{text:'互斥',value:0}];
    $scope.totalData = {
		totalTransTotal: '0.00',
		totalEmptyAmount: '0.00',
		totalAdjustmentAmount: '0.00',
		totalFullAmount: '0.00',
		cashBackAmountHavePay: '0.00',
		cashBackAmountNotPay: '0.00'
	};
	$scope.accountCheckTotal = 0;
	$scope.liquidationTotal = 0;
	$scope.rewardIsBookedTotal = 0;

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
	};

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

	//当前页
	$scope.pageAllClick=function () {
		if($scope.baseInfo.pageAll){
			$scope.baseInfo.countAll=false;
		}else{
			$scope.baseInfo.countAll=true;
		}
		$scope.gridApi.selection.clearSelectedRows();
	}

    $scope.countTradeLists=[];
	$scope.checkActivityCode2 = function(){
		$scope.typeNos2=[];
		$http.post("activity/queryByactivityTypeNoList","009").success(function (data) {
			if(data.status){
                $scope.countTradeLists=data.info;
			}
		})
	};
	$scope.checkActivityCode2();

	//欢乐返活动子类型
	$scope.checkActivityCode = function(activityCode){
		$scope.getTypeNoList(activityCode,null,false);
	};
	//获取欢乐返子类型
	$scope.getTypeNoList=function(activityCode,value,timeSta){
		$scope.typeNos=[{value: "", text: "全部"}];
		if(activityCode==null||activityCode==""){
			return;
		}
		var data={
			activityCode:activityCode,
			str:value
		};
		var postCfg = {
			headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
			transformRequest: function (data) {
				return $.param(data);
			}
		};
		$http.post("activity/getHlfActivityTypeList",data,postCfg)
			.success(function(data){
				$scope.typeNost=[{value: "", text: "全部"}];
				if(data.status){
					for(var i=0; i<data.info.length; i++){
						$scope.typeNost.push({value:data.info[i].activityTypeNo,text:data.info[i].activityTypeNo +" "+data.info[i].activityTypeName});
					}
				}else{
					$scope.notice(data.msg);
				}
				$scope.typeNos = $scope.typeNost;
				if(timeSta){
					oldValueType = value;
				}
			});
	};

	//欢乐返子类型动态
	$scope.typeNos=[{value:"",text:"全部"}];
	$scope.getStatesType =getStatesType;
	var oldValueType="";
	var timeoutType="";
	function getStatesType(value) {
		var newValue=value;
		if(newValue != oldValueType){
			if (timeoutType) $timeout.cancel(timeoutType);
			timeoutType = $timeout(
				function(){
					$scope.getTypeNoList($scope.baseInfo.activityCode,value,true);
				},800);
		}
	};

    $scope.countTradeColumnDefs = [
        {field: 'activityTypeName',displayName: '欢乐返子类型名称',pinnable: false,width: 180,sortable: false}
    ];
    $scope.payMethodLists=[];
    $scope.checkMap = {};
    $scope.clickCheck= function($event,row){
		var id=row.entity.id;
		var countTradeScope=row.entity.countTradeScope;
		var checkValue=$event.target.value;
		var oldScope= $scope.checkMap[id];
        var newCountTradeScope="";
		if(oldScope){
            newCountTradeScope=oldScope;
		}else{
            if(countTradeScope){
                newCountTradeScope=countTradeScope;
            }
		}
        if($event.target.checked){
        	if(newCountTradeScope.indexOf(","+checkValue+",")!=-1){

			}else{
                newCountTradeScope=newCountTradeScope+checkValue+","
			}
		}else{
            if(newCountTradeScope.indexOf(","+checkValue+",")!=-1){
                newCountTradeScope=newCountTradeScope.replace(checkValue+",","");
            }
		}
        $scope.checkMap[id]=newCountTradeScope;
    }

    $scope.payMethodCheck= function(){
		if(angular.toJson($scope.checkMap)=="{}"){
			$scope.notice("无变更设置");
			return;
		}
		$http.post("activity/setCountTradeScope","countTradeScope="+ angular.toJson($scope.checkMap),
			{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			.success(function (data) {
				if(data.status){
					$scope.notice(data.msg);
				}
			});
    };

	//代理商
	$http.post("agentInfo/selectAllInfo")
		.success(function(msg){
			//响应成功
			for(var i=0; i<msg.length; i++){
				$scope.agent.push({value:msg[i].agentNode,text:msg[i].agentNo + " " + msg[i].agentName});
				$scope.oneAgent.push({value:msg[i].agentNo,text:msg[i].agentNo + " " + msg[i].agentName});
			}
		});
	//动态代理商
	$scope.agent=[{value:"",text:"全部"}];
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
								$scope.agentt.push({value: "", text: "全部"});
							}else{
								$scope.agentt.push({value: "", text: "全部"});
								for(var i=0; i<response.data.length; i++){
									$scope.agentt.push({value:response.data[i].agentNode,text:response.data[i].agentNo + " " + response.data[i].agentName});
								}
							}
							$scope.agent = $scope.agentt;
							oldValue = value;
						});
				},800);
		}
	};

	//条件查询一级代理商
	$scope.oneAgent=[{value:"",text:"全部"}];
	$scope.getStatesOne =getStatesOne;
	var oldValueOne="";
	var timeoutOne="";
	function getStatesOne(value) {
		$scope.agenttOne = [];
		var newValueOne=value;
		if(newValueOne != oldValueOne){
			if (timeoutOne) $timeout.cancel(timeoutOne);
			timeoutOne = $timeout(
				function(){
					$http.post('agentInfo/selectAllInfo','item=' + value,
						{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
						.then(function (response) {
							if(response.data.length==0) {
								$scope.agenttOne.push({value: "", text: "全部"});
							}else{
								$scope.agenttOne.push({value: "", text: "全部"});
								for(var i=0; i<response.data.length; i++){
									$scope.agenttOne.push({value:response.data[i].agentNo,text:response.data[i].agentNo + " " + response.data[i].agentName});
								}
							}
							$scope.oneAgent = $scope.agenttOne;
							oldValueOne = value;
						});
				},800);
		}
	};

	//是否校验时间
	isVerifyTime = 1;//校验：1，不校验：0

	keyChange=function(){
		if ($scope.baseInfo.activeOrder || $scope.baseInfo.merchantN
			|| ($(d5666).val() && $(d5667).val())) {
			isVerifyTime = 0;
		} else {
			isVerifyTime = 1;
		}
	}

	setBeginTime=function(setTime){
		$scope.baseInfo.activeTimeStart = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
	}
	setEndTime=function(setTime){
		$scope.baseInfo.activeTimeEnd = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
	}

	//收单机构
	$http.post("activityDetail/selectHappyTixianSwitch")
		.success(function(msg){
			//响应成功
			if(msg==null){
				return;
			}
			if(msg.status){
				$scope.happyTixianSwitch =msg.happyTixianSwitch;
			}
		});


	//查询
	$scope.query=function(){
		if (!($scope.baseInfo.activeOrder || $scope.baseInfo.merchantN
			|| ($(d5666).val() && $(d5667).val()))) {
			if(!($scope.baseInfo.enterTimeStart && $scope.baseInfo.enterTimeEnd)){
				if(!($scope.baseInfo.activeTimeStart && $scope.baseInfo.activeTimeEnd)){
					$scope.notice("激活日期不能为空");
					return;
				}

			}

			if(($scope.baseInfo.activeTimeStart && $scope.baseInfo.activeTimeEnd)){
				var stime = new Date($scope.baseInfo.activeTimeStart).getTime();
				var etime = new Date($scope.baseInfo.activeTimeEnd).getTime();
				if ((etime - stime) > (365 * 24 * 60 * 60 * 1000)) {
					$scope.notice("激活日期范围不能超过365天");
					return;
				}
			}

			if(($scope.baseInfo.enterTimeStart && $scope.baseInfo.enterTimeEnd)){
				var stime = new Date($scope.baseInfo.enterTimeStart).getTime();
				var etime = new Date($scope.baseInfo.enterTimeEnd).getTime();
				if ((etime - stime) > (31 * 24 * 60 * 60 * 1000)) {
					$scope.notice("进件日期范围不能超过31天");
					return;
				}
			}
		}
		$scope.baseInfo.checkIds=null;
		$scope.loadImg = true;
		$scope.submitting = true;
		$http.post('activityDetail/selectHappyBackDetail.do',"baseInfo="+angular.toJson($scope.baseInfo)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+
			$scope.paginationOptions.pageSize,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			.success(function(msg){
				$scope.loadImg = false;
				$scope.submitting = false;
				if(!msg)
					return;
				if(msg.status){
					$scope.activityGrid.data = msg.page.result;
					$scope.activityGrid.totalItems = msg.page.totalCount;
					$scope.totalData = msg.totalData;

					$scope.accountCheckTotal = msg.accountCheckTotal;
					$scope.liquidationTotal = msg.liquidationTotal;
					$scope.rewardIsBookedTotal = msg.rewardIsBookedTotal;
				}
			}).error(function(){
			$scope.notice("系统异常");
			$scope.loadImg = false;
			$scope.submitting = false;
		})
	}
//	$scope.query();
	$scope.activityGrid = {
		paginationPageSize:10,                  //分页数量
		paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
		useExternalPagination: true,		  //开启拓展名
		enableHorizontalScrollbar: true,        //横向滚动条
		enableVerticalScrollbar : true,  		//纵向滚动条
		columnDefs: [
			{field: 'id',displayName: '序号',pinnable: false,width: 180,sortable: false},
			{field: 'activeOrder',displayName: '激活订单号',pinnable: false,width: 180,sortable: false},
			{field: 'activeTime',displayName: '激活时间',pinnable: false,width: 180,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
			{field: 'activityCode',displayName: '活动类型',pinnable: false,width: 180,sortable: false,cellFilter:"formatDropping:"+ angular.toJson($scope.subjectTypes)},
			{field: 'activityTypeNo',displayName: '欢乐返子类型编号',pinnable: false,width: 180,sortable: false},
			{field: 'activityTypeName',displayName: '欢乐返子类型',pinnable: false,width: 180,sortable: false},
			{field: 'merchantName',displayName: '商户名称',pinnable: false,width: 180,sortable: false},
			{field: 'merchantNo',displayName: '商户编号',pinnable: false,width: 180,sortable: false},
			{field: 'teamName',displayName: '所属组织',pinnable: false,width: 180,sortable: false},
			{field: 'teamEntryName',displayName: '所属子组织',pinnable: false,width: 180,sortable: false},
			{field: 'hardId',displayName: '硬件产品ID',pinnable: false,width: 180,sortable: false},
			{field: 'recommendedSource',displayName: '推广来源',pinnable: false,width: 180,sortable: false,cellFilter:"formatDropping:"+ angular.toJson($scope.recommendedSources)},
			{field: 'repeatRegister',displayName: '是否重复注册',pinnable: false,width: 180,sortable: false,cellFilter:"formatDropping:"+ angular.toJson($scope.repeatRegisters)},
			{field: 'enterTime',displayName: '进件时间',pinnable: false,width: 180,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
			{field: 'acqEnname',displayName: '收单机构',pinnable: false,width: 180,sortable: false},
			{field: 'transTotal',displayName: '交易金额',pinnable: false,width: 180,sortable: false},
			{field: 'acqMerchantFee',displayName: '上游手续费',pinnable: false,width: 180,sortable: false},
			{field: 'cumulateTransAmount',displayName: '累计交易金额',pinnable: false,width: 180,sortable: false},
			{field: 'minOverdueTime',displayName: '交易奖励截止累计日期',pinnable: false,width: 180,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
			{field: 'overdueTime',displayName: '交易扣款截止累计日期',pinnable: false,width: 180,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
			{field: 'cumulateAmountMinus',displayName: '累计交易（扣）',pinnable: false,width: 180,sortable: false},
			{field: 'cumulateAmountAdd',displayName: '累计交易（奖）',pinnable: false,width: 180,sortable: false},
			{field: 'cashBackAmount',displayName: '返现金额',pinnable: false,width: 180,sortable: false},
			{field: 'emptyAmount',displayName: '未满扣N元',pinnable: false,width: 180,sortable: false},
			{field: 'fullAmount',displayName: '满奖M元',pinnable: false,width: 180,sortable: false},
            {field: 'isExclusion',displayName: '互斥不补贴',pinnable: false,width: 180,sortable: false,cellFilter:"formatDropping:"+ angular.toJson($scope.exclusionStatuss)},
            {field: 'status',displayName: '活动状态',pinnable: false,width: 180,sortable: false,cellFilter:"formatDropping:" + angular.toJson($scope.activityStatus)},
			{field: 'isStandard',displayName: '奖励是否达标',pinnable: false,width: 180,sortable: false, cellFilter:"formatDropping:" + angular.toJson($scope.isStandards)},
			{field: 'standardTime',displayName: '奖励达标时间',pinnable: false,width: 180,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
			{field: 'minusAmountTime',displayName: '扣款时间',pinnable: false,width: 180,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
			{field: 'addAmountTime',displayName: '奖励时间',pinnable: false,width: 180,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
			{field: 'agentName',displayName: '所属代理商名称',pinnable: false,width: 180,sortable: false},
			{field: 'agentNo',displayName: '所属代理商编号',pinnable: false,width: 180,sortable: false},
			{field: 'oneAgentName',displayName: '一级代理商名称',pinnable: false,width: 180,sortable: false},
			{field: 'oneAgentNo',displayName: '一级代理商编号',pinnable: false,width: 180,sortable: false},
			{field: 'liquidationStatus',displayName: '清算核算状态',pinnable: false,width: 180,sortable: false,cellFilter:"formatDropping:[{text:'同意',value:'1'},{text:'不同意',value:'2'},{text:'未核算',value:'3'}]"},
			{field: 'liquidationOperator',displayName: '清算核算操作人',pinnable: false,width: 180,sortable: false},
			{field: 'liquidationTime',displayName: '清算操作时间',pinnable: false,width: 180,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
			{field: 'accountCheckStatus',displayName: '账务核算状态',pinnable: false,width: 180,sortable: false,cellFilter:"formatDropping:[{text:'同意',value:'1'},{text:'不同意',value:'2'},{text:'未核算',value:'3'}]"},
			{field: 'accountCheckOperator',displayName: '财务核算操作人',pinnable: false,width: 180,sortable: false},
			{field: 'accountCheckTime',displayName: '账务操作时间',pinnable: false,width: 180,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
			{field: 'billingStatus',displayName: '入账状态',pinnable: false,width: 180,sortable: false,cellFilter:"formatDropping:[{text:'已入账',value:'1'},{text:'未入账',value:'0'}]"},
			{field: 'billingTime',displayName: '入账时间',pinnable: false,width: 180,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
			{field: 'billingMsg',displayName: '入账信息',pinnable: false,width: 180,sortable: false},
			{field: 'action',displayName: '操作',width: 250,pinnedRight:true,sortable: false,editable:true,cellTemplate:
					'<div class="lh30">'
					+'<a class="lh30" '
					+'ui-sref="activity.happyBackDetail({hId:row.entity.id})" target="_black">详情</a>'
					+'<a class="lh30" ng-click="grid.appScope.oneRewardIsBooked(row.entity.id)" ' +
					' ng-show="row.entity.isStandard==\'1\'&& (row.entity.status==6||row.entity.status==7||row.entity.status==8)&&row.entity.addAmountTime==null && row.entity.fullAmount!=0 && grid.appScope.hasPermit(\'activity.oneRewardIsBooked\')">奖励入账</a>'
					+'</div>'
			}
		],
		onRegisterApi: function(gridApi) {
			$scope.gridApi = gridApi;
			$scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
				$scope.paginationOptions.pageNo = newPage;
				$scope.paginationOptions.pageSize = pageSize;
				$scope.query();
			});
			//行选中事件
			$scope.gridApi.selection.on.rowSelectionChanged($scope,function(row,event){
				if(row.isSelected){
					$scope.baseInfo.pageAll=false;
					$scope.baseInfo.countAll=false;
				}
			});
			//全选事件
			$scope.gridApi.selection.on.rowSelectionChangedBatch($scope,function(row,event){
				if(row){
					if(row[0].isSelected){
						$scope.baseInfo.pageAll=false;
						$scope.baseInfo.countAll=false;
					}
				}
			});
		}
	};

	$scope.rewardResulGrid = {
		data: 'rewardResultList',
		enableVerticalScrollbar : true,  		//纵向滚动条
		columnDefs: [
			{field: 'activeOrder',displayName: '激活订单号',width:250},
			{field: 'billingStatusText',displayName: '入账状态',width:250},
			{field: 'billingMsg',displayName: '入账信息',width:250}
		]
	};







	var result=false;
	//奖励入账
	$scope.oneRewardIsBooked=function (id) {
		if(result){
			result;
		}
		result=true;
		SweetAlert.swal({
				title: "确认奖励入账？",
				showCancelButton: true,
				confirmButtonColor: "#DD6B55",
				confirmButtonText: "提交",
				cancelButtonText: "取消",
				closeOnConfirm: true,
				closeOnCancel: true },
			function (isConfirm) {
				if (isConfirm) {
					$http.post('activityDetail/oneRewardIsBooked',"id="+id,
						{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
						.success(function(msg){
							$scope.notice(msg.msg);
							if(msg.status){
								$scope.cancel();
								$scope.query();
							}
							result=false;
						}).error(function(){
						$scope.notice('系统异常');
						result=false;
					});
				}
			});
	}

	//欢乐返奖励批量入账
	$scope.joyToAccount=function () {
		var ids = "";
		if($scope.baseInfo.countAll||$scope.baseInfo.pageAll){
		}else {
			var selectList=$scope.gridApi.selection.getSelectedRows();
			if(selectList==null||selectList.length==0){
				$scope.notice("请选择需要欢乐返入账的记录");
				return false;
			}
			if(selectList!=null&&selectList.length>0){
				for(var i=0;i<selectList.length;i++){
					var item=selectList[i];
					ids = ids + item.id + ",";
				}
			}
			if(ids==""){
				$scope.notice("请选择需要欢乐返入账的记录");
				return false;
			}
			$scope.baseInfo.checkIds=ids.substring(0,ids.length-1);
		}
		if(result){
			result;
		}
		result = true;
		SweetAlert.swal({
				title: "确认批量欢乐返入账？",
				showCancelButton: true,
				confirmButtonColor: "#DD6B55",
				confirmButtonText: "提交",
				cancelButtonText: "取消",
				closeOnConfirm: true,
				closeOnCancel: true },
			function (isConfirm) {
				if (isConfirm) {
					$http.post('activityDetail/joyToAccount',"baseInfo="+angular.toJson($scope.baseInfo) + "&pageNo=" + $scope.paginationOptions.pageNo + "&pageSize="+$scope.paginationOptions.pageSize,
						{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
						.success(function(msg){
							if(msg.status){
								$('#rewardResultModel').modal('show');
								$scope.rewardResultList = msg.rewardResultList;
								$scope.cashBackAmountHaveCount = msg.cashBackAmountHaveCount;
								$scope.cashBackAmountNotCount = msg.cashBackAmountNotCount
							}else{
								$scope.notice(msg.msg);
							}
							result = false;
						}).error(function(){
						$scope.notice('系统异常');
						result=false;
					});
				}
			});
	}

	//批量奖励入账
	$scope.rewardIsBooked=function () {
		if (!($scope.baseInfo.activeOrder || $scope.baseInfo.merchantN
			|| ($(d5666).val() && $(d5667).val()))) {
			if(!($scope.baseInfo.activeTimeStart && $scope.baseInfo.activeTimeEnd)){
				$scope.notice("激活日期不能为空");
				return;
			}
			var stime = new Date($scope.baseInfo.activeTimeStart).getTime();
			var etime = new Date($scope.baseInfo.activeTimeEnd).getTime();
			if ((etime - stime) > (365 * 24 * 60 * 60 * 1000)) {
				$scope.notice("激活日期范围不能超过365天");
				return;
			}
		}
		var ids = "";
		$scope.baseInfo.batchOrOne="2";
		if($scope.baseInfo.countAll||$scope.baseInfo.pageAll){

		}else {
			var selectList=$scope.gridApi.selection.getSelectedRows();
			if(selectList==null||selectList.length==0){
				$scope.notice("请选择需要奖励入账的记录");
				return false;
			}
			if(selectList!=null&&selectList.length>0){
				for(var i=0;i<selectList.length;i++){
					var item=selectList[i];
					if((item.status==6||item.status==7||item.status==8) && item.isStandard==1&& item.addAmountTime==null&& item.fullAmount!=0){
						ids = ids + item.id + ",";
					}
				}
			}
			if(ids==""){
				$scope.notice("请选择需要奖励入账的记录");
				return false;
			}
			$scope.baseInfo.checkIds=ids.substring(0,ids.length-1);
		}
		if(result){
			result;
		}
		result=true;
		SweetAlert.swal({
				title: "确认批量奖励入账？",
				showCancelButton: true,
				confirmButtonColor: "#DD6B55",
				confirmButtonText: "提交",
				cancelButtonText: "取消",
				closeOnConfirm: true,
				closeOnCancel: true },
			function (isConfirm) {
				if (isConfirm) {
					$http.post('activityDetail/rewardIsBooked',"baseInfo="+angular.toJson($scope.baseInfo) + "&pageNo=" + $scope.paginationOptions.pageNo + "&pageSize="+$scope.paginationOptions.pageSize,
						{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
						.success(function(msg){
							$scope.notice(msg.msg);
							if(msg.status){
								$scope.cancel();
								$scope.query();
							}
							result=false;
						}).error(function(){
						$scope.notice('系统异常');
						result=false;
					});
				}
			});
	}
	//清算核算
	$scope.liquidation=function(entity){
		//检查是否符合清算核算条件
		if(entity.status!=2 || entity.liquidationStatus==1){
			$scope.notice("不符合清算核算条件");
			return;
		} else {
			$scope.liquidationStatus = 1;
			$scope.baseInfo.checkIds=entity.id;
			$scope.baseInfo.batchOrOne="1";
			$('#liquidationModal').modal('show');
		}
	};
	//批量清算核算
	$scope.liquidationBatch = function(){
		if (!($scope.baseInfo.activeOrder || $scope.baseInfo.merchantN
			|| ($(d5666).val() && $(d5667).val()))) {
			if(!($scope.baseInfo.activeTimeStart && $scope.baseInfo.activeTimeEnd)){
				$scope.notice("激活日期不能为空");
				return;
			}
			var stime = new Date($scope.baseInfo.activeTimeStart).getTime();
			var etime = new Date($scope.baseInfo.activeTimeEnd).getTime();
			if ((etime - stime) > (365 * 24 * 60 * 60 * 1000)) {
				$scope.notice("激活日期范围不能超过365天");
				return;
			}
		}
		var ids = "";
		$scope.baseInfo.batchOrOne="2";
		if($scope.baseInfo.countAll||$scope.baseInfo.pageAll){
			$scope.liquidationStatus = 1;
			$scope.baseInfo.checkIds=null;
			$('#liquidationModal').modal('show');
		}else{
			$scope.selectNums = 0;
			var selectList = $scope.gridApi.selection.getSelectedRows();
			if(selectList==null||selectList.length==0){
				$scope.notice("请选择需要清算核算的记录");
				return false;
			}
			if(selectList!=null&&selectList.length>0){
				for(var i=0;i<selectList.length;i++){
					var item=selectList[i];
					if(item.status==2 && item.liquidationStatus!=1){
						ids = ids + item.id + ",";
					}
				}
			}
			if(ids==""){
				$scope.notice("请选择需要清算核算的记录");
				return false;
			}
			$scope.baseInfo.checkIds=ids.substring(0,ids.length-1);
			$scope.liquidationStatus = 1;
			$('#liquidationModal').modal('show');
		}
	}
	//财务核算
	$scope.accountCheck=function(entity){
		//检查是否符合清算核算条件
		if(entity.status!=2 || entity.liquidationStatus!=1 || entity.activityCode!='009' || entity.accountCheckStatus==1){
			$scope.notice("不符合财务核算条件");
			return;
		} else {
			$scope.accountCheckStatus = 1;
			$scope.baseInfo.checkIds=entity.id;
			$scope.baseInfo.batchOrOne="1";
			$('#accountCheckModal').modal('show');
		}
	};
	//批量财务核算
	$scope.accountCheckBatch = function(){
		if (!($scope.baseInfo.activeOrder || $scope.baseInfo.merchantN
			|| ($(d5666).val() && $(d5667).val()))) {
			if(!($scope.baseInfo.activeTimeStart && $scope.baseInfo.activeTimeEnd)){
				$scope.notice("激活日期不能为空");
				return;
			}
			var stime = new Date($scope.baseInfo.activeTimeStart).getTime();
			var etime = new Date($scope.baseInfo.activeTimeEnd).getTime();
			if ((etime - stime) > (365 * 24 * 60 * 60 * 1000)) {
				$scope.notice("激活日期范围不能超过365天");
				return;
			}
		}
		var ids = "";
		$scope.baseInfo.batchOrOne="2";
		if($scope.baseInfo.countAll||$scope.baseInfo.pageAll){
			$scope.accountCheckStatus = 1;
			$scope.baseInfo.checkIds=null;
			$('#accountCheckModal').modal('show');
		}else{
			$scope.selectNums = 0;
			var selectList = $scope.gridApi.selection.getSelectedRows();
			if(selectList==null||selectList.length==0){
				$scope.notice("请选择需要财务核算的记录");
				return false;
			}
			if(selectList!=null&&selectList.length>0){
				for(var i=0;i<selectList.length;i++){
					var item=selectList[i];
					if(item.status==2 && item.activityCode=="009" && item.liquidationStatus==1 && item.accountCheckStatus!=1){
						ids = ids + item.id + ",";
					}
				}
			}
			if(ids==""){
				$scope.notice("请选择需要财务核算的记录");
				return false;
			}
			$scope.baseInfo.checkIds=ids.substring(0,ids.length-1);
			$scope.accountCheckStatus = 1;
			$('#accountCheckModal').modal('show');
		}
	}
	//提交清算核算
	$scope.submitClear = function(){
		$scope.submitting = true;
		$http.post('activityDetail/liquidation.do',"baseInfo="+angular.toJson($scope.baseInfo) + "&pageNo=" + $scope.paginationOptions.pageNo + "&pageSize="+$scope.paginationOptions.pageSize+"&liquidationStatus="+$scope.liquidationStatus,
			{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			.success(function(msg){
				$scope.submitting = false;
				$scope.notice(msg.msg);
				if(msg.status){
					$scope.cancel();
					$scope.query();
				}
			}).error(function(){
			$scope.submitting = false;
			$scope.notice('系统异常');
		});
	};
	//提交财务核算
	$scope.submitCheck = function(){
		$scope.submitting = true;
		$http.post('activityDetail/accountCheck.do',"baseInfo="+angular.toJson($scope.baseInfo) + "&pageNo=" + $scope.paginationOptions.pageNo + "&pageSize="+$scope.paginationOptions.pageSize+"&accountCheckStatus="+$scope.accountCheckStatus,
			{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			.success(function(msg){
				$scope.submitting = false;
				$scope.notice(msg.msg);
				if(msg.status){
					$scope.cancel();
					$scope.query();
				}
			}).error(function(){
			$scope.submitting = false;
			$scope.notice('系统异常');
		});
	};

	$scope.cancel = function(){
		$scope.baseInfo.checkIds=null;
		$scope.liquidationStatus = 1;
		$scope.accountCheckStatus = 1;
		$('#liquidationModal').modal('hide');
		$('#accountCheckModal').modal('hide');
		$('#rewardResultModel').modal('hide');
        $('#tradeCountSetBatchModal').modal('hide');
        $scope.query();
	}

	$scope.reward_cancel = function(){
		$scope.baseInfo.checkIds=null;
		$scope.liquidationStatus = 1;
		$scope.accountCheckStatus = 1;
		$('#liquidationModal').modal('hide');
		$('#accountCheckModal').modal('hide');
		$('#rewardResultModel').modal('hide');
        $('#tradeCountSetBatchModal').modal('hide');
		$scope.query();
	}

	//导出
	$scope.exportExcel=function(){
		if (!($scope.baseInfo.activeOrder || $scope.baseInfo.merchantN
			|| ($(d5666).val() && $(d5667).val()))) {
			if(!($scope.baseInfo.activeTimeStart && $scope.baseInfo.activeTimeEnd)){
				$scope.notice("激活日期不能为空");
				return;
			}
			var stime = new Date($scope.baseInfo.activeTimeStart).getTime();
			var etime = new Date($scope.baseInfo.activeTimeEnd).getTime();
			if ((etime - stime) > (365 * 24 * 60 * 60 * 1000)) {
				$scope.notice("激活日期范围不能超过365天");
				return;
			}
		}
		if($scope.baseInfo.countAll||$scope.baseInfo.pageAll){
			//当前页
			if($scope.baseInfo.pageAll){
				$scope.baseInfo.checkIds=null;
				SweetAlert.swal({
						title: "确认导出当前页所有的数据？",
						showCancelButton: true,
						confirmButtonColor: "#DD6B55",
						confirmButtonText: "提交",
						cancelButtonText: "取消",
						closeOnConfirm: true,
						closeOnCancel: true
					},
					function (isConfirm) {
						if (isConfirm) {
							location.href = "activityDetail/exportHappyBack.do?baseInfo=" + encodeURI(angular.toJson($scope.baseInfo)) + "&pageNo=" + $scope.paginationOptions.pageNo + "&pageSize=" + +$scope.paginationOptions.pageSize;
						}
					});
			}
			//全部数据
			if($scope.baseInfo.countAll){
				$scope.baseInfo.checkIds=null;
				SweetAlert.swal({
						title: "确认导出所有的数据？",
						showCancelButton: true,
						confirmButtonColor: "#DD6B55",
						confirmButtonText: "提交",
						cancelButtonText: "取消",
						closeOnConfirm: true,
						closeOnCancel: true
					},
					function (isConfirm) {
						if (isConfirm) {
							location.href = "activityDetail/exportHappyBack.do?baseInfo=" + encodeURI(angular.toJson($scope.baseInfo)) + "&pageNo=" + $scope.paginationOptions.pageNo + "&pageSize=" + +$scope.paginationOptions.pageSize;
						}
					});
			}
		}else {
			var selectList = $scope.gridApi.selection.getSelectedRows();
			if(selectList!=null&&selectList.length>0){
				var ids = "";
				for(var i=0;i<selectList.length;i++){
					if(i<selectList.length-1){
						ids = ids + selectList[i].id + ",";
					}else{
						ids = ids + selectList[i].id;
					}
				}
				$scope.baseInfo.checkIds=ids;
			}
			SweetAlert.swal({
					title: "确认导出选中的数据？",
					showCancelButton: true,
					confirmButtonColor: "#DD6B55",
					confirmButtonText: "提交",
					cancelButtonText: "取消",
					closeOnConfirm: true,
					closeOnCancel: true
				},
				function (isConfirm) {
					if (isConfirm) {
						location.href = "activityDetail/exportHappyBack.do?baseInfo=" + encodeURI(angular.toJson($scope.baseInfo)) + "&pageNo=" + $scope.paginationOptions.pageNo + "&pageSize=" + +$scope.paginationOptions.pageSize;
					}
				});
		}
	};
    $scope.countTradeGrid ={};
    // $('#tradeCountSetBatchModal').modal('show');
    //交易累计设置modal
    $scope.tradeCountSetBatch = function(){

        $scope.countTradeColumnDefs = [
            {field: 'activityTypeName',displayName: '欢乐返子类型名称',pinnable: false,width: 180,sortable: false}
        ];
        $scope.checkMap = {};
        $scope.payMethodLists=[];
        $http.post("sysDict/getListByKey.do?sysKey=PAY_METHOD_TYPE")
            .success(function (data) {
                //响应成功
                for(var i=0; i<data.length; i++){
                    $scope.payMethodLists.push({text:data[i].sysName,value:data[i].sysValue});
                    $scope.countTradeColumnDefs.push({field: 'countTradeScope',displayName: data[i].sysName,pinnable: true,width: 120,sortable: true,cellTemplate:
                        '<div>' +
                        '<input type="checkbox" ng-click="grid.appScope.clickCheck($event,row)" ng-checked="row.entity.countTradeScope!=null&&row.entity.countTradeScope.indexOf(\','+data[i].sysValue+',\')!=-1" name="paymethodCheck" value="'+data[i].sysValue+'"/>'+
                        '</div>'});
                    $scope.countTradeGrid = {
                        enableHorizontalScrollbar: true,        //横向滚动条
                        enableVerticalScrollbar : true 		//纵向滚动条

                    };
                    $scope.countTradeGrid.columnDefs=$scope.countTradeColumnDefs;
                    $http.post("activity/queryByactivityTypeNoList","009").success(function (data) {
                        if(data.status){
                            $scope.countTradeLists=data.info;
                            for(var i2=0; i2< $scope.countTradeLists.length; i2++){
                            	if( $scope.countTradeLists[i2].countTradeScope!=null&& $scope.countTradeLists[i2].countTradeScope!=""){
                            		var oldScope=$scope.countTradeLists[i2].countTradeScope;
                                    $scope.countTradeLists[i2].countTradeScope=","+oldScope+",";
								}

                            }
                            $scope.countTradeGrid.data = $scope.countTradeLists;
                            $('#tradeCountSetBatchModal').modal('show');
                            $('#tradeCountSetBatchModal').css("height","100%");
                        }
                    })


                }
            })
    }


	//代理商名称/编号  改变时调用
	$scope.changeAgentNode = function () {
		$scope.disabledMerchantType = !$scope.baseInfo.agentN;
	};
	//页面绑定回车事件
	$document.bind("keypress", function(event) {
		$scope.$apply(function (){
			if(event.keyCode == 13){
				$scope.query();
			}
		})
	});

});