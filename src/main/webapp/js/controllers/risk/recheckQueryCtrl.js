/**
 * 商户查询
 */

angular.module('inspinia', ['infinity.angular-chosen']).controller('recheckQueryCtrl',function($scope,$http,$state,$timeout,$stateParams,$compile,$filter,i18nService,SweetAlert,$document){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	//数据源
	$scope.IsYN=[{text:"全部",value:-1},{text:"是",value:1},{text:"否",value:0}];
	$scope.merchantStates=[{text:"全部",value:-1},{text:"待一审",value:1},{text:"待平台审核",value:2},{text:"审核失败",value:3},{text:"正常",value:4}];
	$scope.termianlTypes=[{text:"全部",value:0}];
	// $scope.info={sTime:moment(new Date().getTime()-6*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',eTime:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',openStatus:"",autoCheckTimes:"",riskStatus:"",merAccount:"",acqOrgMerNo:"",cardId:"",mbpId:"",merchantNo:"",merchantExamineState:-1,agentName:"",agentNode:-1,productType:"-1",termianlType:"-1",mobilephone:"",activityType:"",preFrozenMoney1:"",preFrozenMoney2:"",accountName:"",merStatus:"",tradeType:"",syncStatus:"",recommendedSource:""};
	$scope.agent = [{text:"全部",value:""}];
	$scope.merAccounts=[{text:"全部",value:""},{text:"是",value:1},{text:"否",value:0}]
	$scope.openStatuss=[{text:"全部",value:""},{text:"通过",value:1},{text:"不通过",value:2}]
	$scope.riskStatusaa=[{text:"全部",value:""},{text:"正常",value:1},{text:"不进不出",value:3},{text:"只进不出",value:2}]
	$scope.activityTypes=[{text:"全部",value:""},{text:"是",value:"1"},{text:"否",value:"0"}];
	$scope.merStatusList=[{text:"全部",value:""},{text:"正常",value:"1"},{text:"关闭",value:"0"}];
	$scope.tradeType=[{text:"全部",value:""},{text:"集群模式",value:"0"},{text:"直清模式",value:"1"}];
	$scope.syncStatus=[{text:"全部",value:""},{text:"初始化",value:"0"},{text:"同步成功",value:"1"},{text:"同步失败",value:"2"},{text:"审核中",value:"3"}];
	$scope.riskStatusList=[{text:"全部",value:""},{text:"正常",value:"1"},{text:"只进不出",value:"2"},{text:"不进不出",value:"3"}];
	// $scope.recommendedSources=[{text:"全部",value:""},{text:"正常注册",value:"0"},{text:"微创业",value:"1"},{text:"代理商分享",value:"2"},{text:"超级盟主",value:"3"}];
	$scope.team=[{text:"全部",value:""}];
	$scope.provinceList=[{text:"全部",value:""}];
	$scope.cityList=[{text:"全部",value:""}];
	
	$scope.userNames=[];
	$scope.allNum = {init:0,total:0,disagree:0,agree:0,refund:0};
	
	$scope.reexamineStatus=[{text:"全部",value:""},{text:"通过",value:1},{text:"不通过",value:2}
	,{text:"退件",value:3},{text:"未复审",value:0}
	]
	
	$scope.reexamineStatusList=angular.toJson($scope.reexamineStatus);
    $scope.recheckUsers=initData.RECHECK_USERS;

	$http.get('teamInfo/queryTeamName.do').success(function(msg){
		for(var i=0;i<msg.teamInfo.length;i++){
			$scope.team.push({text:msg.teamInfo[i].teamName,value:msg.teamInfo[i].teamId});
		}
	});
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
	//清空
	$scope.clear=function(){
		$scope.info={sTime:moment(new Date().getTime()-6*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
			eTime:moment(new Date().getTime()).format('YYYY-MM-DD'+' 23:59:59'),
			openStatus:"",reexamineOperator:"",
			autoCheckTimes:"",riskStatus:"",merAccount:"",acqOrgMerNo:"",cardId:"",mbpId:"",merchantNo:"",
			merchantExamineState:-1,agentName:"",agentNode:-1,productType:"-1",termianlType:"-1",
			mobilephone:"",activityType:"",preFrozenMoney1:"",preFrozenMoney2:"",accountName:"",reexamineStatus:0,
			merStatus:"",tradeType:"",syncStatus:"",recommendedSource:"",teamId:"",province:"",city:"",merchantType:""};
		$scope.cityShow=false;
		isVerifyTime = 1;
	}
	$scope.clear();
	
	//平台审核人
	$http.get('merchantInfo/selectAllUserBox')
	.success(function(largeLoad) {
		$scope.userNames=largeLoad;
	});
	
	//业务产品
	$http.get('businessProductDefine/selectAllInfo.do')
			.success(function(largeLoad) {
				if(!largeLoad)
					return
				$scope.productTypes=largeLoad;
				$scope.productTypes.splice(0,0,{bpId:"-1",bpName:"全部"});
			});

	//机具类型
	$http.get('hardwareProduct/selectAllInfo.do')
			.success(function(result){
				if(!result)
					return;
				$scope.termianlTypes=result;
				$scope.termianlTypes.splice(0,0,{hpId:"-1",typeName:"全部"});
			})

	//代理商
	$http.post("agentInfo/selectAllInfo")
			.success(function(msg){
				//响应成功
				for(var i=0; i<msg.length; i++){
					$scope.agent.push({value:msg[i].agentNo,text:msg[i].agentName});
				}
			});

	//获取所有省份
	$http.post('areaInfo/getAreaByName','name=0&&type=p',
		{headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
	).success(function(msg){
		for(var i=0; i<msg.length; i++){
			$scope.provinceList.push({value:msg[i].id,text:msg[i].name});
		}
	});
	$scope.changeProvince=function(){
		if($scope.info.province!=null&&$scope.info.province!=""){
			$scope.cityShow=true;
			$http.post('areaInfo/getAreaByParentId','id='+$scope.info.province,
				{headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
			).success(function(msg){
				$scope.cityList=[{text:"全部",value:""}];
				$scope.info.city="";
				for(var i=0; i<msg.length; i++){
					$scope.cityList.push({value:msg[i].name,text:msg[i].name});
				}
			});
		}else{
			$scope.cityShow=false;
			$scope.info.city="";
		}
	}

	$scope.changeTradeType = function(){
		var tradeType = $scope.info.tradeType;
		$scope.info.syncStatus = "";
		if("1" == tradeType){
			$("#syncLabel").css("display", "block");
			$("#syncDiv").css("display", "block");
		}else{
			$("#syncLabel").css("display", "none");
			$("#syncDiv").css("display", "none");
		}
	};

	//是否校验时间
	isVerifyTime = 1;//校验：1，不校验：0

	keyChange=function(){
		if ($scope.info.merchantNo || $scope.info.mobilephone
				|| $scope.info.cardId || $scope.info.accountName) {
			isVerifyTime = 0;
		} else {
			isVerifyTime = 1;
		}
	}

	setBeginTime=function(setTime){
		$scope.info.sTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
	}
	setEndTime=function(setTime){
		$scope.info.eTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
	}

	//查询
	$scope.selectInfo=function(){
		if($scope.loadImg){
			return;
		}
		if (!($scope.info.mbpId || $scope.info.merchantNo || $scope.info.mobilephone
				|| $scope.info.cardId || $scope.info.accountName)) {
			if(!($scope.info.sTime && $scope.info.eTime)){
    			$scope.notice("创建时间不能为空");
    			return;
    		}
			var stime = new Date($scope.info.sTime).getTime();
			var etime = new Date($scope.info.eTime).getTime();
			if ((etime - stime) > (93 * 24 * 60 * 60 * 1000)) {
				$scope.notice("创建时间范围不能超过三个月");
				return;
			}
		}
		
		$scope.loadImg = true;
		$http.post(
				'merchantBusinessProduct/examineQueryByParam.do',
				"info="+angular.toJson($scope.info)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
				{headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
		).success(function(data){
			//响应成功
			if(data.status){
				$scope.gridOptions.data = data.page.result;
				$scope.gridOptions.totalItems = data.page.totalCount;
			}else{
				$scope.notice(data.msg);
			}
			$scope.loadImg = false;
		}).error(function(result){
			$scope.loadImg = false;
			$scope.notice("系统异常!");
		});
        //汇总
        $http.post(
            'merchantBusinessProduct/examineTotalByParam.do',
            "info="+angular.toJson($scope.info),
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
        ).success(function(data){
            if(data.status){
                $scope.allNum = data.result;
            }
        }).error(function(result){
            $scope.notice("汇总异常!");
        });
	}

	
	
	
	$scope.paginationOptions=angular.copy($scope.paginationOptions);

	$scope.gridOptions={                           //配置表格
		paginationPageSize:10,                  //分页数量
		paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
		useExternalPagination: true,
		enableHorizontalScrollbar: 1,        //横向滚动条
		enableVerticalScrollbar : 1,  		//纵向滚动条
		columnDefs:[                           //表格数据
			{ field: 'id',displayName:'商户进件编号',width:150},
			{ field: 'teamName',displayName:'所属组织',width:150
			},
			{ field: 'merchantNo',displayName:'商户编号',width:150},
            { field: 'merchantType',displayName:'商户类型',width:150,
                cellFilter:"formatDropping:"+ angular.toJson($scope.merchantTypeLists)
            },
			{ field: 'tradeType',displayName:'交易模式',width:150,
				cellFilter:"formatDropping:[{text:'集群模式',value:0},{text:'直清模式',value:1}]"
			},
			{ field: 'merchantName',displayName:'商户名称' ,width:150},
			{ field: 'mobilePhone',displayName:'商户手机号',width:150},
			{ field: 'bpName',displayName:'业务产品',width:150 },
			{ field: 'agentName',displayName:'代理商名称',width:150 },
			{ field: 'activityCode',displayName:'是否参加欢乐送',width:150,
				cellFilter:"formatDropping:[{text:'是',value:1},{text:'否',value:0}]"
			},
			{ field: 'merAccount',displayName:'已开户',width:150,
				cellFilter:"formatDropping:[{text:'否',value:0},{text:'是',value:1}]"
			},
			{ field: 'syncStatus',displayName:'直清同步状态',width:150,
				cellFilter:"formatDropping:[{text:'初始化',value:0},{text:'同步成功',value:1},{text:'同步失败',value:2},{text:'审核中',value:3},{text:'-',value:'-'}]"
			},
			{ field: 'controlAmount',displayName:'冻结金额',cellFilter:"currency:''",width:150 },
			{ field: 'preFrozenAmount',displayName:'预冻结金额',cellFilter:"currency:''",width:150 },
			
			{ field: 'merCreateTime',displayName:'创建时间',width:150,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'
			},
			
			{ field: 'reexamineTime',displayName:'复审时间',width:150,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'
			},
			{ field: 'merStatus',displayName:'商户状态',width:150,
				cellFilter:"formatDropping:[{text:'关闭',value:0},{text:'正常',value:1}]"
			},
			{ field: 'reexamineStatus',displayName:'复审状态',width:150,
				cellFilter:"formatDropping:"+$scope.reexamineStatusList
			},
			{ field: 'reexamineOperator',displayName:'复审人',width:150 },
			{ field: 'status',displayName:'业务产品状态',width:150,
				cellFilter:"formatDropping:[{text:'待一审',value:1},{text:'待平台审核',value:2},{text:'审核失败',value:3},{text:'正常',value:4}]"
			},
			{ field: 'riskStatus',displayName:'商户冻结状态',width:150,
				cellFilter:"formatDropping:[{text:'正常',value:1},{text:'只进不出',value:2},{text:'不进不出',value:3}]"
			},
			{ field: 'recommendedSource',displayName:'推广来源',width:150,
				cellFilter:"formatDropping:" + angular.toJson($scope.recommendedSources)
			},
			{ field: 'id',displayName:'操作',width:270,pinnedRight:true,
	               cellTemplate:'<div  class="lh30"><a ng-show="grid.appScope.hasPermit(\'risk.examineRcored\') && row.entity.status==4" ng-click="grid.appScope.merSelect(row)"  ui-sref="risk.MerExamineDetail({mertId:row.entity.id})"  target="_black" >复审</a></div>'
			}
		],
		onRegisterApi: function(gridApi) {
			$scope.gridApi = gridApi;
			gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
				$scope.paginationOptions.pageNo = newPage;
				$scope.paginationOptions.pageSize = pageSize;
				$scope.selectInfo();
			});
		}
	};


	$scope.riskStatusa1=[{text:"正常",value:"1"},{text:"只进不出",value:"2"},{text:"不进不出",value:"3"}]

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
									console.log($scope.agent);
									oldValue = value;
								});
					},800);
		}

	}

	//导出信息//打开导出终端模板
	$scope.exportInfo=function(){
		if (!($scope.info.mbpId || $scope.info.merchantNo || $scope.info.mobilephone
				|| $scope.info.cardId || $scope.info.accountName)) {
			if(!($scope.info.sTime && $scope.info.eTime)){
    			$scope.notice("创建时间不能为空");
    			return;
    		}
			var stime = new Date($scope.info.sTime).getTime();
			var etime = new Date($scope.info.eTime).getTime();
			if ((etime - stime) > (93 * 24 * 60 * 60 * 1000)) {
				$scope.notice("创建时间范围不能超过三个月");
				return;
			}
		}
		SweetAlert.swal({
			title: "确认导出？",
//		    text: "",
//		    type: "warning",
			showCancelButton: true,
			confirmButtonColor: "#DD6B55",
			confirmButtonText: "提交",
			cancelButtonText: "取消",
			closeOnConfirm: true,
			closeOnCancel: true 
		},
		function (isConfirm) {
			if (isConfirm) {
                var token = $("meta[name='_csrf']").attr("content");
                var header = $("meta[name='_csrf_header']").attr("content");
                $("#token").val(token);
                $("#header").val(header);
                $("#inputInfo").val(angular.toJson($scope.info));
                $("#exportForm").attr("action","merchantBusinessProduct/exportExamine.do");
                $("#exportForm").submit();
			}
		});
	}
	//页面绑定回车事件
	$document.bind("keypress", function(event) {
		$scope.$apply(function (){
			if(event.keyCode == 13){
				$scope.selectInfo();
			}
		})
	});

})