/**
 * 商户查询
 */

angular.module('inspinia', ['infinity.angular-chosen']).controller('merchantQueryCtrl',function($scope,$http,$state,$timeout,$stateParams,$compile,$filter,i18nService,SweetAlert,$document){
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
	$scope.specials=[{text:"全部",value:""},{text:"是",value:"1"},{text:"否",value:"0"}];

	$scope.tradeType=[{text:"全部",value:""},{text:"集群模式",value:"0"},{text:"直清模式",value:"1"}];
	$scope.syncStatus=[{text:"全部",value:""},{text:"初始化",value:"0"},{text:"同步成功",value:"1"},{text:"同步失败",value:"2"},{text:"审核中",value:"3"}];
	$scope.riskStatusList=[{text:"全部",value:""},{text:"正常",value:"1"},{text:"只进不出",value:"2"},{text:"不进不出",value:"3"}];
	// $scope.recommendedSources=[{text:"全部",value:""},{text:"正常注册",value:"0"},{text:"微创业",value:"1"},{text:"代理商分享",value:"2"},{text:"超级盟主",value:"3"}];
    $scope.bindStatusList = [{text:"绑定",value:"1"},{text:"已解绑",value:"0"}];

	$scope.itemSourceSelect= angular.copy($scope.merItemSourceList);
	$scope.itemSourceStr=angular.toJson($scope.itemSourceSelect);

	$scope.team=[{text:"全部",value:""}];
	$scope.provinceList=[{text:"全部",value:""}];
	$scope.cityList=[{text:"全部",value:""}];

	$scope.authChannels = [{text:"全部",value:""}];;
	$scope.huoTiChannels = [{text:"全部",value:""}];

	//代理商推广
	$scope.sourceSysStaSelect=[{text:'全部',value:null},{text:'是',value:1},{text:'否',value:2}];


	$http.get('merchantBusinessProduct/queryHuoTiChannels').success(function(data){
		for(var i=0;i<data.length;i++){
			$scope.huoTiChannels.push({text:data[i].channel_name,value:data[i].channel_code});
		}
	});
	$http.get('merchantBusinessProduct/queryAuthChannels').success(function(res){
		for(var i=0;i<res.data.length;i++){
			$scope.authChannels.push({text:res.data[i].sys_name,value:res.data[i].sys_value});
		}
	});
	$http.get('teamInfo/queryTeamName.do').success(function(msg){
		for(var i=0;i<msg.teamInfo.length;i++){
			$scope.team.push({text:msg.teamInfo[i].teamName,value:msg.teamInfo[i].teamId});
		}
	});
	$scope.subTeamMap = [];
	$scope.subTeams = [];
	$scope.allSubTeams = [];

	$http.get('teamInfo/querySubTeams').success(function(result){
		$scope.subTeamMap = result.subTeamMap;
		angular.forEach($scope.subTeamMap, function (x) {
			angular.forEach(x, function (y) {
				$scope.allSubTeams.push({text:y.teamEntryName,value:y.teamEntryId});
			});
		});
		$scope.subTeams = $scope.allSubTeams;
	});
	//所属组织关联硬件产品
	$scope.teamIdChange = function(teamId){
		$http.post('hardwareProduct/selectAllInfoByTeamId.do',"teamId="+teamId,
			{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			.success(function(result){
				if(!result)
					return;
				$scope.termianlTypes=result;
				$scope.termianlTypes.splice(0,0,{hpId:"-1",typeName:"全部"});
			})

	}
	//所属组织关联业务产品
	$scope.productChange = function(teamId){
		$http.post('businessProductDefine/selectAllInfoByTeamId.do',"teamId="+teamId,
			{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			.success(function(result){
				if(!result)
					return;
				$scope.productTypes=result;
				$scope.productTypes.splice(0,0,{bpId:"-1",bpName:"全部"});
			})
	}

	$scope.hasSubTeam = function(teamId){
		$scope.teamIdChange(teamId);
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
			eTime:moment(new Date().getTime()).format('YYYY-MM-DD'+' 23:59:59'),openStatus:"",specialMerchant:"",
			autoCheckTimes:"",riskStatus:"",merAccount:"",acqOrgMerNo:"",cardId:"",mbpId:"",merchantNo:"",
			merchantExamineState:-1,agentName:"",agentNode:-1,productType:"-1",termianlType:"-1",
			mobilephone:"",activityType:"",preFrozenMoney1:"",preFrozenMoney2:"",accountName:"",
			merStatus:"",tradeType:"",syncStatus:"",recommendedSource:"",teamId:"",province:"",city:"",
			sourceSysSta:null,itemSource:"",merchantType:"",authChannel:"",autoMbpChannel:""
		};
		$scope.cityShow=false;
		isVerifyTime = 1;
	}
	$scope.clear();
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
				|| $scope.info.cardId || $scope.info.accountName || $scope.info.accountNo)) {
			if(!($scope.info.sTime && $scope.info.eTime)){
    			$scope.notice("创建时间不能为空");
    			return;
    		}
			var stime = new Date($scope.info.sTime).getTime();
			var etime = new Date($scope.info.eTime).getTime();
			if ((etime - stime) > (31 * 24 * 60 * 60 * 1000)) {
				$scope.notice("创建时间范围不能超过31天");
				return;
			}
		}
		$scope.loadImg = true;
		$http.post(
				'merchantBusinessProduct/selectByParam.do',
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
	}

	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	//初始化
	//323tgh进入页面不用查询
//	$http.post('merchantBusinessProduct/selectAllInfo.do')
//	.success(function(data){
//		if(!data)
//			return;
//		$scope.gridOptions.data =data.result ;
//		$scope.gridOptions.totalItems = data.totalCount;
//	})

	$scope.gridOptions={                           //配置表格
		paginationPageSize:10,                  //分页数量
		paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
		useExternalPagination: true,
		enableHorizontalScrollbar: 1,        //横向滚动条
		enableVerticalScrollbar : 1,  		//纵向滚动条
		columnDefs:[                           //表格数据
			{ field: 'id',displayName:'商户进件编号',width:150},
			{ field: 'teamName',displayName:'所属组织',width:150},
			{ field: 'teamEntryName',displayName:'所属子组织',width:150},
			{ field: 'merchantNo',displayName:'商户编号',width:150},
			{ field: 'merchantType',displayName:'商户类型',width:150,
                cellFilter:"formatDropping:"+ angular.toJson($scope.merchantTypeLists)
			},
			{ field: 'tradeType',displayName:'交易模式',width:150,
				cellFilter:"formatDropping:[{text:'集群模式',value:0},{text:'直清模式',value:1}]"
			},
			{ field: 'merchantName',displayName:'商户名称' ,width:150},
			{ field: 'mobilePhone',displayName:'商户手机号',width:150},
			{ field: 'merCreditCardStatus',displayName:'绑定快捷支付卡',width:150,
				cellFilter:"formatDropping:[{text:'是',value:1},{text:'否',value:0}]"
			},
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
			{ field: 'merStatus',displayName:'商户状态',width:150,
				cellFilter:"formatDropping:[{text:'关闭',value:0},{text:'正常',value:1}]"
			},
			{ field: 'status',displayName:'业务产品状态',width:150,
				cellFilter:"formatDropping:[{text:'待一审',value:1},{text:'待平台审核',value:2},{text:'审核失败',value:3},{text:'正常',value:4}]"
			},
			{ field: 'realAuthChannelName',displayName:'鉴权通道',width:150
			},
			{ field: 'huoTiChannel',displayName:'自动审核活体通道',width:150
			},
			{ field: 'riskStatus',displayName:'商户冻结状态',width:150,
				cellFilter:"formatDropping:[{text:'正常',value:1},{text:'只进不出',value:2},{text:'不进不出',value:3}]"
			},
			{ field: 'recommendedSource',displayName:'推广来源',width:150,
				cellFilter:"formatDropping:" + angular.toJson($scope.recommendedSources)
			},
			{ field: 'sourceSysSta',displayName: '代理商推广',width:180,cellFilter:"formatDropping:"+ angular.toJson($scope.sourceSysStaSelect)},
			{ field: 'itemSource',displayName:'进件来源',width:120,cellFilter:"formatDropping:" + $scope.itemSourceStr},
			{ field: 'id',displayName:'操作',width:380,pinnedRight:true,
				cellTemplate:
                '<div  class="lh30">'
                +'<a ng-show="grid.appScope.hasPermit(\'merchant.detail\')" ui-sref="merchant.queryMerDetail({mertId:row.entity.id})" target="_black">详情</a>'
                +'<a ng-show="grid.appScope.hasPermit(\'merchant.restPwd\')" ng-click="grid.appScope.restPwd(row.entity.merchantNo,row.entity.teamId)"> | 密码重置</a>'
                +'<a ng-show="grid.appScope.hasPermit(\'merchant.openAccount\')&&row.entity.merAccount==0" ng-click="grid.appScope.createAccount(row.entity.merchantNo)"> | 开户</a>'
                +'<a ng-show="grid.appScope.hasPermit(\'merchant.preFrozen\')&&!(row.entity.status==2&&row.entity.merAccount==1)"  ng-click="grid.appScope.preFrozen(row.entity.merchantNo,row.entity.preFrozenAmount)"> | 预冻结</a>'
                +'<a ng-show="grid.appScope.hasPermit(\'merchant.update\')&&row.entity.status!=1&&row.entity.status!=2" ui-sref="merchant.MerUpdate({mertId:row.entity.id})" target="_black"> | 修改</a>'
                +'<a ng-show="grid.appScope.hasPermit(\'merchantBusinessProduct.selectMerchantCreditcard\')" ng-click="grid.appScope.bindCreditCard(row.entity.merchantNo)"> | 绑定信用卡</a>'
                +'</div>'
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

    $scope.creditcardGrid = {
        columnDefs: [
            {field: 'accountNo',displayName: '银行卡号',width:209,cellTemplate:
                "<div class='lh30'>{{row.entity.accountNo}} "
                + "<button class='btn btn-sm' type='button' ng-show='row.entity.encryptStatus&&grid.appScope.hasPermit(\"merchantBusinessProduct.selectMerchantCreditcardDetail\")'"
                + " ng-click='grid.appScope.selectMerchantCreditcardDetail(row)'>显示</button>"
                + "</div>"},
            {field: 'bankName',displayName: '所属银行',width:209},
            {field: 'bindStatus',displayName: '绑定状态',width:209,cellFilter:"formatDropping:" + angular.toJson($scope.bindStatusList)},
            {field: 'createTime',displayName: '首次绑定日期',width:209,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'}
        ],
        onRegisterApi: function(gridApi) {
            $scope.creditcardGridApi = gridApi;
        }
    };

    $scope.bindCreditCard = function(merchantNo){
        $http({
            url:"merchantBusinessProduct/selectMerchantCreditcard?merchantNo=" + merchantNo,
            method:"post"
        }).success(function(result){
            if(result.status) {
                $("#bindCreditCardModal").modal("show");
                $scope.creditcardGrid.data = result.data;
                angular.forEach($scope.creditcardGrid.data, function(item) {
                    item.encryptStatus = 1;
                });
            } else {
                $scope.notice(result.msg);
            }
        });
    }

    $scope.selectMerchantCreditcardDetail = function(row){
        $http({
            url:"merchantBusinessProduct/selectMerchantCreditcardDetail?id=" + row.entity.id,
            method:"post"
        }).success(function(result){
            if(result.status) {
                row.entity = result.data;
            } else {
                $scope.notice(result.msg);
            }
        });
    }

	$scope.preFrozen=function(merNo,preFrozenAmount,preFrozenNote){
		$("#preFrozenMsg").hide();
		//判断 该商户是否存在  未出款的交易，请谨慎操作预冻结！
		var data={"merchantNo":merNo};
		$http.post('merchantBusinessProduct/judgeIsExistTrade.do',angular.toJson(data))
				.success(function(req){
					if(req.bols){
						$("#preFrozenMsg").show();
						$("#preFrozenModal").modal("show");
						$scope.preFrozen.preFrozenMerchant = merNo;
						$scope.preFrozen.preFrozenMoney = preFrozenAmount;
						$scope.preFrozen.preFrozenNote = preFrozenNote;
					}else{
						if(req.msg){
							$scope.notice(req.msg);
						}else{
							$("#preFrozenModal").modal("show");
							$scope.preFrozen.preFrozenMerchant = merNo;
							$scope.preFrozen.preFrozenMoney = preFrozenAmount;
							$scope.preFrozen.preFrozenNote = preFrozenNote;
							$("#preFrozenMsg").hide();
						}
					}
				}).error(function(){
			$scope.notice("请求出错！");
		});
	}

	$scope.savePreFrozen=function(){
		var data={"merchantNo":$scope.preFrozen.preFrozenMerchant,"preFrozenAmount":$scope.preFrozen.preFrozenMoney,"preFrozenNote":$scope.preFrozen.preFrozenNote};
		$http.post('merchantBusinessProduct/preFrozen.do',angular.toJson(data))
				.success(function(req){
					if(req.bols){
						$scope.notice(req.msg);
						$scope.selectInfo();
						$('#preFrozenModal').modal('hide');
					}else{
						$scope.notice(req.msg);
						$('#preFrozenModal').modal('hide');
					}
				}).error(function(){
			$scope.notice("请求出错！");
		});
	}

	$scope.cancel = function(){
		$("#preFrozenModal").modal("hide");
        $("#bindCreditCardModal").modal("hide");
	}

	$scope.createAccount=function(merNo){
		var data={"merNo":merNo};
		$http.post('merchantInfo/createAccount',angular.toJson(data))
				.success(function(data){
					if(data.bols){
						$scope.notice(data.msg);
						$scope.selectInfo();
					}else{
						$scope.notice(data.msg);
					}
				})
	}

	$scope.createAccountInfo=function(){
		var merchantSelectedList = $scope.gridApi.selection.getSelectedRows();
		var merchantNoList = [];
		angular.forEach(merchantSelectedList, function(item){
			merchantNoList[merchantNoList.length] = item.merchantNo;
		});
		$http.post('merchantInfo/createAllAccount',angular.toJson(merchantNoList))
				.success(function(data){
					if(data.bols){
						$scope.notice(data.msg);
						$scope.selectInfo();
					}else{
						$scope.notice(data.msg);
					}
				})
	}

	//密码重置
	$scope.restPwd=function(merchantNo,teamId){
		SweetAlert.swal({
					title: "确认密码重置？",
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
						var data={"merchantNo":merchantNo,"teamId":teamId};
						$http.post("merchantInfo/restPwd.do",angular.toJson(data))
								.success(function(msg){
									$scope.notice(msg.msg);
									$scope.selectInfo();
								}).error(function(){
						});
					}
				});
	};
	$scope.riskStatusa1=[{text:"正常",value:"1"},{text:"只进不出",value:"2"},{text:"不进不出",value:"3"}]
	$scope.upRiskStatusOpen=function(merId,statusa){
		$scope.merRiskStatus=statusa;
		$scope.merId=merId;
		$('#upRiskStatus').modal('show');
	}

	$scope.upRiskStatusColse=function(){
		$('#upRiskStatus').modal('hide');
	}

	$scope.upRiskStatusCommit=function(){
		var data={"merId":$scope.merId,"merRiskStatus":$scope.merRiskStatus}
		$http.post("merchantInfo/updatRiskStatus",angular.toJson(data))
				.success(function(msg){
					if(msg.status){
						$scope.notice(msg.msg);
						for(var i =0;i<$scope.gridOptions.data.length;i++){
							if($scope.gridOptions.data[i].merchantNo==$scope.merId){
								$scope.gridOptions.data[i].riskStatus=$scope.merRiskStatus;
							}
						}
						$('#upRiskStatus').modal('hide');
					}else{
						$scope.notice(msg.msg);
						$('#upRiskStatus').modal('hide');
					}
				}).error(function(){
		});


	}
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

	//-----商户名称/编号 下拉框查询 start -----//
//	$scope.merchantSelect = [{text:"全部",value:""}];
//	//获取少量的商户
//	$http.post("merchantBusinessProduct/getMerchantFew",
//		{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
//		.success(function(msg){
//			//响应成功
//			for(var i=0; i<msg.length; i++){
//				$scope.merchantSelect.push({value:msg[i].merchantNo,text:msg[i].merchantNo+"("+msg[i].merchantName+")"});
//			}
//		});
//
//	$scope.getMerchantSelect =getMerchantSelect;
//	var oldValueMer="";
//	var timeoutMer="";
//	function getMerchantSelect(value) {
//		$scope.agenttMer = [];
//		var newValueMer=value;
//		if(newValueMer != oldValueMer){
//			if (timeoutMer) $timeout.cancel(timeoutMer);
//			timeoutMer = $timeout(
//				function(){
//					$http.post('merchantBusinessProduct/getMerchantFew','item='+value,
//						{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
//						.success(function (msg) {
//							if(msg.length==0) {
//								$scope.agenttMer.push({value: "", text:"全部"});
//							}else{
//								for(var i=0; i<msg.length; i++){
//									$scope.agenttMer.push({value:msg[i].merchantNo,text:msg[i].merchantNo+"("+msg[i].merchantName+")"});
//								}
//							}
//							$scope.merchantSelect = $scope.agenttMer;
//							oldValueMer = value;
//						});
//				},800);
//		}
//	}
	//-----商户名称/编号 下拉框查询 end -----//

	$scope.overFormat = function overFormat(){
		var v = $scope.preFrozen.preFrozenMoney;
		if(v === ''){
			v = '0.00';
		}else if(v === '0'){
			v = '0.00';
		}else if(v === '0.'){
			v = '0.00';
		}else if(/^[-]?0+\d+\.?\d*.*$/.test(v)){
			v = v.replace(/^[-]?0+(\d+\.?\d*).*$/, '$1');
			v = inp.getRightPriceFormat(v).val;
		}else if(/^[-]?0\.\d$/.test(v)){
			v = v + '0';
		}else if(!/^[-]?\d+\.\d{2}$/.test(v)){
			if(/^[-]?\d+\.\d{2}.+/.test(v)){
				v = v.replace(/^([-]?\d+\.\d{2}).*$/, '$1');console.log(v);
			}else if(/^[-]?\d+$/.test(v)){
				v = v + '.00';
			}else if(/^[-]?\d+\.$/.test(v)){
				v = v + '00';
			}else if(/^[-]?\d+\.\d$/.test(v)){
				v = v + '0';
			}else if(/^[-]?[^\d]+\d+\.?\d*$/.test(v)){
				v = v.replace(/^[-]?[^\d]+(\d+\.?\d*)$/, '$1');
			}else if(/[-]?\d+/.test(v)){
				v = v.replace(/^[-]?[^\d]*(\d+\.?\d*).*$/, '$1');
				ty = false;
			}else if(/^[-]?0+\d+\.?\d*$/.test(v)){
				v = v.replace(/^[-]?0+(\d+\.?\d*)$/, '$1');
				ty = false;
			}else{
				v = '0.00';
			}
		}
		$scope.preFrozen.preFrozenMoney = v;
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