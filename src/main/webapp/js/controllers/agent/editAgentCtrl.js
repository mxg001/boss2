angular.module('inspinia',['angularFileUpload','uiSwitch','infinity.angular-chosen'], [ 'ui.bootstrap']).controller("editAgentCtrl", function($scope, $http, $state, $stateParams,uiGridConstants,i18nService,FileUploader,$log,$uibModal,$compile,SweetAlert,$timeout) {
	var self = this;
	$scope.merRateStr='[{text:"每笔固定金额",value:"1"},{text:"每笔扣率",value:"2"},{text:"每笔扣率带保底封顶",value:"3"},{text:"每笔扣率+固定金额",value:"4"},{text:"单笔阶梯扣率",value:"5"},{text:"扣率+封顶",value:"6"}]';
	$scope.statusStr = '[{text:"正常",value:1},{text:"关闭",value:1}]';
	$scope.type = [ {text : '个人',value :  1 }, {text : '个体商户',value :  2 }, {text : '企业商户',value :  3} ];
	$scope.checkStatus=[{text:"待审核",value:0},{text:"审核通过",value:1},{text:'审核不通过',value:2}];
	$scope.effectiveStatus = [{text:"否",value:0},{text:"是",value:1},{text:'否',value:2}];
	$scope.editAgentFlag = true;	//修改操作时，有的输入框禁止改动，可以用此标志位控制
	$scope.teamType=[];
	$scope.agent={};
	$scope.appLogoHide = true;		//修改页面，默认不显示图片
	$scope.webLogoHide = true;		
	var removeAppLogoFlag = false;
	var removeWebLogoFlag = false;
	$scope.bpData=[];
	$scope.shareData=[];
	$scope.rateData=[];
	$scope.quotaData=[];
	var shareLength = 0;	//备份分润数据的长度，最后提交时，只提交新的分润信息
	var rateLength = 0;		
	var quotaLength = 0;	
	$scope.shareModal = 1;
	$scope.joinHbTypes=[];//已参加的欢乐返子类型
	$scope.shareBatchSet = true;
	$scope.teams=[];

	$http.get('teamInfo/queryTeamName.do').success(function(msg){
		for(var i=0;i<msg.teamInfo.length;i++){
			$scope.teams.push({text:msg.teamInfo[i].teamName,value:msg.teamInfo[i].teamId});
		}
	});
    $scope.agentShareLevel=[ {text : '请选择',value :  '' } ];
    for(var i=1;i<=20;i++){
        $scope.agentShareLevel.push({value:i+'',text:'Lv.'+i});
    }

	$http.get('teamInfo/queryTeamName.do').success(function(msg){
		for(var i=0;i<msg.teamInfo.length;i++){
			$scope.teamType.push({text:msg.teamInfo[i].teamName,value:msg.teamInfo[i].teamId});
		}
	});

    $scope.product={productType:""};
    $scope.productTypes=[];
    //业务产品
    $http.get('businessProductDefine/selectAllInfo.do')
        .success(function(largeLoad) {
            if(!largeLoad){
                return;
            }
            if(largeLoad.length==0) {
                $scope.productTypes.push({bpId: "", bpName: "全部"});
            }else{
                $scope.productTypes.push({bpId: "", bpName: "全部"});
                for(var i=0; i<largeLoad.length; i++){
                    $scope.productTypes.push({bpId:largeLoad[i].bpId,bpName:largeLoad[i].bpId + " " + largeLoad[i].bpName});
                }
            }
        });

    $scope.getStates =getStates;
    var oldValue="";
    var timeout="";
    function getStates(value) {
        $scope.productTypess = [];
        var newValue=value;
        if(newValue != oldValue){
            if (timeout) $timeout.cancel(timeout);
            timeout = $timeout(
                function(){
                    $http.post('businessProductDefine/selectAllInfoByName','bpId=' + value,
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .then(function (response) {
                            if(response.data.length==0) {
                                $scope.productTypess.push({bpId: "", bpName: "全部"});
                            }else{
                                $scope.productTypess.push({bpId: "", bpName: "全部"});
                                for(var i=0; i<response.data.length; i++){
                                    $scope.productTypess.push({bpId:response.data[i].bpId,bpName:response.data[i].bpId + " " + response.data[i].bpName});
                                }
                            }
                            $scope.productTypes = $scope.productTypess;
                            oldValue = value;
                        });
                },800);
        }
    };


	//post查询
	$scope.getAreaList=function(name,type,callback){
		if(name == null || name=="undefine"){
			return;
		}
		$http.post('areaInfo/getAreaByName.do','name='+name+'&&type='+type,
			{headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
		).success(function(data){
			callback(data);
		}).error(function(){
		});
	}
	//省
	$scope.getAreaList(0,"p",function(data){
		$scope.provinceGroup = data;
	});
	//市
	$scope.getCities = function() {
		$scope.getAreaList($scope.agent.province,"",function(data){
			$scope.cityGroup = data;
		});
	}
	//县
	$scope.getAreas = function() {
		$scope.getAreaList($scope.agent.city,"",function(data){
			$scope.areaGroup = data;
		});
	}

	$http.post('agentInfo/editAgentInfoDetail',angular.toJson({"agentNo":$stateParams.agentNo,"teamId":$stateParams.teamId})).success(function(msg){
		//只有一级代理商才能修改
		if(msg.agentInfo.agentLevel != 1){
			return false;
		}
		if(msg.agentInfo.clientLogo != null){
			$scope.appLogoHide = false;
		}
		if(msg.agentInfo.managerLogo != null){
			$scope.webLogoHide = false;
		}
		$scope.agent = msg.agentInfo;
		$scope.getCities();
		$scope.getAreas();
		$scope.getCitiess();
		$scope.getBankName();
		// $scope.getPosCnaps();

		$scope.shareData = msg.agentShare;
		angular.forEach($scope.shareData, function(data,index){
			$scope.shareData[index].isOldShare = true;
		});
		$scope.quotaData = msg.agentQuota;
		$scope.rateData = msg.agentRate;
		if(msg.agentShare != null){
			shareLength = msg.agentShare.length;
		}
		if(msg.agentQuota != null){
			quotaLength = msg.agentQuota.length;
		}
		if(msg.agentRate != null){
			rateLength = msg.agentRate.length;
		}
		for(var i=0; i<msg.hbTypes.length; i++){ //保存已参加的欢乐返子类型
			$scope.joinHbTypes[i] = msg.hbTypes[i].activityTypeNo;
		}
        for(var i=0; i<msg.xhlfList.length; i++){ //保存已参加的新欢乐送子类型
            $scope.joinHbTypes[$scope.joinHbTypes.length] = msg.xhlfList[i].activityTypeNo;
        }
		for(var i=0; i<msg.superList.length; i++){ //保存已参加的超级返活动子类型
			$scope.joinHbTypes[$scope.joinHbTypes.length] = msg.superList[i].activityTypeNo;
		}
	}).error(function(){
	});

	$scope.getAreaLists=function(name,type,callback){
		if(name == null || name=="undefine"){
			return;
		}
		$http.post('areaInfo/getAreaByName','name='+name+'&&type='+type,
			{headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
		).success(function(data){
			callback(data);
		}).error(function(){
		});
	}

	//省，加载页面时自动触发
	$scope.getAreaLists(0,"p",function(data){
		$scope.provinceGroups = data;
	});
	//市，页面上ng-change生时触发
	$scope.getCitiess = function() {
		$scope.areaGroups=[];
		$scope.getAreaLists($scope.agent.accountProvince,"",function(data){
			$scope.cityGroups = data;
		});
		$scope.bankNames = [];
	}


	$scope.bankNames=[];
	$scope.accountNoFlag = false;
	//支行
	$scope.getPosCnaps = function() {
		if($scope.agent.accountType == 1){	// 如果是对公帐号,则不校验
			$scope.accountNoFlag = false;
			return;
		}
		if($scope.agent.accountNo==null || $scope.agent.accountNo==""
			||$scope.agent.bankName==null || $scope.agent.bankName==""
			||$scope.agent.accountCity==null || $scope.agent.accountCity==""){
			$scope.accountNoFlag = true;
			return;
		}
		$scope.accountNoFlag = false;

		var data={"pris":$scope.agent.accountProvince,"cityName":$scope.agent.accountCity,"backName":$scope.agent.bankName}
		$http.post("merchantInfo/selectCnaps",angular.toJson(data))
			.success(function(data) {
				if(!data.bols){
					$scope.notice(data.msg);
				}else{
					$scope.bankNames=data.list;
					$scope.bankNames.forEach(function (e) {
						e.cnapsNo = e.cnapsNo+"";
					})
				}
			});
	}

	$scope.getBankName=function(){
		if($scope.agent.accountType == 1){	// 如果是对公帐号,则不校验
			$scope.accountNoFlag = false;
			return;
		}
		if($scope.agent.accountNo==null || $scope.agent.accountNo==""){
			$scope.accountNoFlag = true;
			return;
		}
		$scope.accountNoFlag = false;
		var data={"accountNo":$scope.agent.accountNo}
		$http.post("merchantInfo/getBackName",angular.toJson(data))
			.success(function(msg) {
				if(msg.bols){
					$scope.accountList=msg.lists;
					$scope.accountNoFlag = false;
					$scope.agent.bankName = msg.lists[0].bankName;
					if($scope.agent.accountProvince != null && $scope.agent.accountProvince !="" && $scope.agent.accountCity != null && $scope.agent.accountCity !="" ){
						$scope.getPosCnaps();
					}
				} else {
					$scope.accountNoFlag = true;
				}
			});
	}

    //查找联行行号
    /*$scope.getPoscnapsNo= function(){
        $http.post('agentInfo/getPoscnapsNo',"bankName="+$scope.agent.bankName,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
        ).success(function(msg){
            if(msg.status){
                $scope.notice(msg.msg);
                $scope.agent.cnapsNo=msg.cnapsNo;
            } else {
                $scope.notice(msg.msg);
            }
        }).error(function(){
        });
    };*/

	//是否投资为“否”，清空投资金额
	$scope.resetInvestAmount = function(){
		if($scope.agent.invest == 0){
			$scope.agent.investAmount = 0;
		}
	}

    //获取所有的业务产品信息
	//原有的业务产品ID
	$scope.oldBpIds = [];
    $scope.queryBusinessProduct = function(){
        $http.post('agentInfo/editAgentInfoProducts',angular.toJson({"agentNo":$stateParams.agentNo,"teamId":$stateParams.teamId,"bpId":$scope.product.productType}))
            .success(function(msg){
                $scope.bpData = msg.parentProducts;
                $scope.agentProducts = msg.agentProducts;
                for(var i=0; i<msg.parentProducts.length; i++){
                    if(msg.agentProducts != null){
                        for(var j=0; j<msg.agentProducts.length; j++){
                            if(msg.parentProducts[i].key1==msg.agentProducts[j].key1){
                                $scope.bpData[i].aaa = true;//是否勾选
                                $scope.bpData[i].bbb = true;//是否显示状态开关
                                $scope.bpData[i].disabled = true;//已经有的业务产品，不让去掉
                                $scope.bpData[i].key2 = $scope.agentProducts[j].key2;
                                $scope.oldBpIds.push( $scope.bpData[i].key1);
                                break;
                            } else {
                                $scope.bpData[i].aaa = false;
                            }
                        }
                    }
                    for(var j=0;j<$scope.addbpData.length;j++){
                        if(msg.parentProducts[i].key1==$scope.addbpData[j].key1){
                            $scope.bpData[i].aaa = true;
                        }
                    }
                }
            })
    }
    $scope.queryBusinessProduct();


	//勾选业务产品
	$scope.selectedBp = function(entity){
		$scope.getShareList();
		//1.勾选业务产品时，如果勾选了队员，必须勾上队长
		if(entity.aaa==true){
            rowList[entity.key1]=entity;
            $scope.addData(entity);
            if(entity.key6!=null && entity.key7==0){
                angular.forEach($scope.bpData,function(data){
                    if(data.key6!=null && data.key6==entity.key6 && data.key7==1){
                        data.aaa = true;
                        rowList[data.key1]=data;
                        $scope.addData(data);
                    }
                });
			}
		}
		//2.取消业务产品时，如果取消队长，队员也要全部取消,禁用的业务产品(代理商已代理)不能取消
		if(entity.aaa==false ){
            delete rowList[entity.key1];
            $scope.delteData(entity);
            if(entity.key6!=null && entity.key7==1){
                angular.forEach($scope.bpData,function(data){
                    if(data.key6!=null && data.key6==entity.key6 && data.key7==0 && data.bbb!=true){
                        data.aaa = false;
						delete rowList[data.key1];
						$scope.delteData(data);
                    }
                });
			}
		}
	}

    $scope.addbpData=[];
    var rowList = [];
    $scope.addbpList = {
        data: 'addbpData',
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs : [                           //表格数据
            {field: 'key1',displayName: '业务产品ID',width: 180},
            {field: 'key3',displayName: '业务产品名称',width: 180},
            {field: 'key5',displayName: '类型',width: 180,cellFilter:"formatDropping:"+angular.toJson($scope.type)},
            {field: 'key6',displayName: '群组号',width: 200},
            {field: 'key7',displayName: '是否允许单独申请',width: 200,cellFilter:"formatDropping:"+angular.toJson($scope.bool)},
            {field: 'action',displayName: '操作',width:80,cellTemplate:
                    '<a class="lh30" ng-click="grid.appScope.delteData(row.entity)">移除</a> '}],
        onRegisterApi: function(gridApi) {
            $scope.addbpListGridApi = gridApi;
        },
    };

    $scope.addList = function(rowList){
        if(rowList!=null){
            for(var i in rowList){
                if(rowList[i]!=null&&rowList[i]!=""){
                    if($scope.checkData($scope.addbpData,rowList[i],null)){
                        $scope.addbpData.push({
                            key1:rowList[i].key1,
                            key3:rowList[i].key3,
                            key5:rowList[i].key5,
                            key6:rowList[i].key6,
                            key7:rowList[i].key7
                        });
                        $scope.cancel();
                    }
                }
            }
        }
    };

    $scope.addData = function(row){
    	//如果已经存在，则不加入addbpData
		var exists = true;
		$scope.oldBpIds.forEach(function (e) {
			if(e == row.key1){
				exists = false;
			}
		});
        if(row!=null&&row!="" && exists){
            if($scope.checkData($scope.addbpData,row,null)){
                $scope.addbpData.push({
                    key1:row.key1,
                    key3:row.key3,
                    key5:row.key5,
                    key6:row.key6,
                    key7:row.key7
                });
                $scope.cancel();
            }
        }
    };

    $scope.checkData = function(dataList,info,oldInfo){
        if(dataList!=null&&dataList.length>0){
            for(var i=0;i<dataList.length;i++){
                var item=dataList[i];
                if(oldInfo!=null){
                    if(item.key1==oldInfo.key1){
                        continue;
                    }
                }
                if(item.key1==info.key1){
                    return false;
                }
            }
        }
        return true;
    };

    $scope.delteData = function(row){
        if(row!=null&&row!=""){
            for(var j=0;j<$scope.addbpData.length;j++){
                var dateItem=$scope.addbpData[j];
                if(row.key1==dateItem.key1){
                    $scope.addbpData.splice(j, 1);
                }
            }
            for(var j=0;j<$scope.bpData.length;j++){
                if(row.key1==$scope.bpData[j].key1){
                    $scope.bpData[j].aaa=false;
                    $scope.getShareList();
                }
            }
        }
    };

	$scope.batchSetShareProfitPercent = function(){
		if($scope.agent.setValue == "" || $scope.agent.setValue == undefined){
			$scope.notice("请输入设定值！");
			return;
		}
		$scope.shareData.forEach(function (e) {
			if(!e.isOldShare){
				if(e.cost != null && e.cost != ""){
					e.shareProfitPercent = $scope.agent.setValue;
				}
			}
		});
		$scope.agent.setValue = ""
	}

	$scope.bpList = {
        data: 'bpData',
        columnDefs: [
             {field: 'aaa',displayName: '',width: '30',cellTemplate:
        		 '<input type="checkbox" ng-disabled="row.entity.disabled" ng-change="grid.appScope.selectedBp(row.entity)" ng-model="row.entity.aaa" ng-checked="row.entity[col.field]"/>'},
             {field: 'key1',displayName: '业务产品ID',width: 180},
             {field: 'key3',displayName: '业务产品名称',width: 180},
             {field: 'key5',displayName: '类型',width: 180,cellFilter:"formatDropping:"+angular.toJson($scope.type)},
             {field: 'key2',displayName: '状态',width: 180,
            	 cellTemplate:
            		 '<span ng-show="row.entity.bbb&&grid.appScope.hasPermit(\'agent.switchProStatus\')"><switch class="switch switch-s" ng-model="row.entity.key2" ng-change="grid.appScope.switchProduct(row)" /></span>'
 	            	+'<span ng-show="row.entity.bbb&&!grid.appScope.hasPermit(\'agent.switchProStatus\')"> <span ng-show="row.entity.key2==1">开启</span><span ng-show="row.entity.key2==0">关闭</span></span>'
            		/*+ '<switch ng-show="row.entity.bbb"  class="switch switch-s" ng-model="row.entity.key2" ng-change="grid.appScope.switchProduct(row)"/>'*/
             },
             {field: 'key6',displayName: '群组号',width: 200},
             {field: 'key7',displayName: '是否允许单独申请',width: 200,cellFilter:"formatDropping:"+angular.toJson($scope.bool)},
        ]
    };

	/*$scope.happyBackGrid = {
		enableSelectAll : false,//取消表头的全选与全不选
		columnDefs: [
			{field: 'activityTypeNo',displayName: '欢乐返子类型编号'},
			{field: 'activityTypeName',displayName: '欢乐返子类型名称'},
			{field: 'functionName',displayName: '欢乐返类型'},
			{field: 'transAmount',displayName: '交易金额',cellTemplate:'<div class="lh30">{{row.entity.transAmount}}元<div/>'},
			{field: 'cashBackAmount',displayName: '返现金额',cellTemplate:'<div class="lh30">{{row.entity.cashBackAmount}}元<div/>'},
            {field: 'repeatRegisterAmount',displayName: '重复注册返现金额',cellTemplate:'<div class="lh30" ng-show="row.entity.repeatRegisterAmount!=null">{{row.entity.repeatRegisterAmount}}元<div/>'},
            {field: 'emptyAmount',displayName: '首次注册不满扣N值',cellTemplate:'<div class="lh30" ng-show="row.entity.emptyAmount!=null">{{row.entity.emptyAmount}}元<div/>'},
            {field: 'fullAmount',displayName: '首次注册满奖M值',cellTemplate:'<div class="lh30" ng-show="row.entity.fullAmount!=null">{{row.entity.fullAmount}}元<div/>'},
            {field: 'repeatEmptyAmount',displayName: '重复注册不满扣N值',cellTemplate:'<div class="lh30" ng-show="row.entity.repeatEmptyAmount!=null">{{row.entity.repeatEmptyAmount}}元<div/>'},
            {field: 'repeatFullAmount',displayName: '重复注册满奖M值',cellTemplate:'<div class="lh30" ng-show="row.entity.repeatFullAmount!=null">{{row.entity.repeatFullAmount}}元<div/>'}
		],
		onRegisterApi: function(gridApi) {
			$scope.happyGridApi = gridApi;
			$scope.happyGridApi.selection.on.rowSelectionChanged($scope,function (row) {
				//取消勾选的时候校验一下是否为已参加的子类型，若是，则把勾打上，不给取消
				if (!row.isSelected){
					for(var i=0; i<$scope.joinHbTypes.length; i++){
						if(row.entity.activityTypeNo == $scope.joinHbTypes[i]){
							row.isSelected = true;
							break;
						}
					}
				}
			})
		}
	};*/

	$scope.happyBackColumnDefs =
		[
			{field: 'select',displayName: '',width: '30',cellTemplate:
					'<input type="checkbox" ng-disabled="row.entity.disabled" ng-model="row.entity.select" ng-checked="row.entity.select"/>'},
			{field: 'activityTypeNo',displayName: '欢乐返子类型编号',width: 180},
			{field: 'activityTypeName',displayName: '欢乐返子类型名称',width: 180},
			{field: 'functionName',displayName: '欢乐返类型',width: 180},
			{field: 'transAmount',displayName: '交易金额',width: 180,cellTemplate:'<div class="lh30">{{row.entity.transAmount}}元<div/>'},
			{field: 'cashBackAmount',displayName: '返现金额',width: 180,cellTemplate:'<div class="lh30">{{row.entity.cashBackAmount}}元<div/>'},
            {field: 'repeatRegisterAmount',displayName: '重复注册返现金额',width: 180,cellTemplate:'<div class="lh30" ng-show="row.entity.repeatRegisterAmount!=null">{{row.entity.repeatRegisterAmount}}元<div/>'},
            {field: 'emptyAmount',displayName: '首次注册不满扣N值',width: 180,cellTemplate:'<div class="lh30" ng-show="row.entity.emptyAmount!=null">{{row.entity.emptyAmount}}元<div/>'},
            {field: 'fullAmount',displayName: '首次注册满奖M值',width: 180,cellTemplate:'<div class="lh30" ng-show="row.entity.fullAmount!=null">{{row.entity.fullAmount}}元<div/>'},
            {field: 'repeatEmptyAmount',displayName: '重复注册不满扣N值',width: 180,cellTemplate:'<div class="lh30" ng-show="row.entity.repeatEmptyAmount!=null">{{row.entity.repeatEmptyAmount}}元<div/>'},
            {field: 'repeatFullAmount',displayName: '重复注册满奖M值',width: 180,cellTemplate:'<div class="lh30" ng-show="row.entity.repeatFullAmount!=null">{{row.entity.repeatFullAmount}}元<div/>'},
            {field: 'scanRewardAmount',displayName: '扫码交易满奖首次注册奖励金额',width: 180,cellTemplate:'<div class="lh30" ng-show="row.entity.scanRewardAmount!=null">{{row.entity.scanRewardAmount}}元<div/>'},
            {field: 'scanRepeatRewardAmount',displayName: '扫码交易满奖重复注册奖励金额',width: 180,cellTemplate:'<div class="lh30" ng-show="row.entity.scanRepeatRewardAmount!=null">{{row.entity.scanRepeatRewardAmount}}元<div/>'},
            {field: 'allRewardAmount',displayName: '全部交易满奖首次注册奖励金额',width: 180,cellTemplate:'<div class="lh30" ng-show="row.entity.allRewardAmount!=null">{{row.entity.allRewardAmount}}元<div/>'},
            {field: 'allRepeatRewardAmount',displayName: '全部交易满奖重复注册奖励金额',width: 180,cellTemplate:'<div class="lh30" ng-show="row.entity.allRepeatRewardAmount!=null">{{row.entity.allRepeatRewardAmount}}元<div/>'}


        ];

    $scope.newHappyBackGrid = {
        enableSelectAll : false,//取消表头的全选与全不选
        columnDefs: [
            {field: 'activityTypeNo',displayName: '欢乐返子类型编号',width:160},
            {field: 'activityTypeName',displayName: '欢乐返子类型名称',width:160},
            // {field: 'functionName',displayName: '欢乐返类型'},
            {field: 'transAmount',displayName: '交易金额（元）',width:160,cellTemplate:'<div class="lh30">{{row.entity.transAmount}}元<div/>'},
            {field: 'cashBackAmount',displayName: '返现金额（元）',width:160,cellTemplate:'<div class="lh30">{{row.entity.cashBackAmount}}元<div/>'},
            {field: 'repeatRegisterAmount',displayName: '重复注册返现金额（元）',width:200,cellTemplate:'<div class="lh30" ng-show="row.entity.repeatRegisterAmount!=null">{{row.entity.repeatRegisterAmount}}元<div/>'},
            {field: 'repeatRegisterAmount',displayName: '第1次考核奖励金额（元）',width:200,cellTemplate:'<div class="lh30" ng-show="row.entity.oneRewardAmount!=null">{{row.entity.oneRewardAmount}}元<div/>'},
            {field: 'repeatRegisterAmount',displayName: '第1次考核重复注册奖励金额（元）',width:260,cellTemplate:'<div class="lh30" ng-show="row.entity.oneRepeatRewardAmount!=null">{{row.entity.oneRepeatRewardAmount}}元<div/>'},
			{field: 'repeatRegisterAmount',displayName: '第1次子考核奖励金额（元）',width:260,cellTemplate:'<div class="lh30" ng-show="row.entity.oneSubRewardAmount!=null">{{row.entity.oneSubRewardAmount}}元<div/>'},
			{field: 'repeatRegisterAmount',displayName: '第1次子考核重复注册奖励金额（元）',width:260,cellTemplate:'<div class="lh30" ng-show="row.entity.oneSubRepeatReward!=null">{{row.entity.oneSubRepeatReward}}元<div/>'},
            {field: 'repeatRegisterAmount',displayName: '第2次考核奖励金额（元）',width:200,cellTemplate:'<div class="lh30" ng-show="row.entity.twoRewardAmount!=null">{{row.entity.twoRewardAmount}}元<div/>'},
            {field: 'repeatRegisterAmount',displayName: '第2次考核重复注册奖励金额（元）',width:260,cellTemplate:'<div class="lh30" ng-show="row.entity.twoRepeatRewardAmount!=null">{{row.entity.twoRepeatRewardAmount}}元<div/>'},
            {field: 'repeatRegisterAmount',displayName: '第3次考核奖励金额（元）',width:200,cellTemplate:'<div class="lh30" ng-show="row.entity.threeRewardAmount!=null">{{row.entity.threeRewardAmount}}元<div/>'},
            {field: 'repeatRegisterAmount',displayName: '第3次考核重复注册奖励金额（元）',width:260,cellTemplate:'<div class="lh30" ng-show="row.entity.threeRepeatRewardAmount!=null">{{row.entity.threeRepeatRewardAmount}}元<div/>'},
            {field: 'repeatRegisterAmount',displayName: '第4次考核奖励金额（元）',width:200,cellTemplate:'<div class="lh30" ng-show="row.entity.fourRewardAmount!=null">{{row.entity.fourRewardAmount}}元<div/>'},
            {field: 'repeatRegisterAmount',displayName: '第4次考核重复注册奖励金额（元）',width:280,cellTemplate:'<div class="lh30" ng-show="row.entity.fourRepeatRewardAmount!=null">{{row.entity.fourRepeatRewardAmount}}元<div/>'},
        ],
        onRegisterApi: function(gridApi) {
            $scope.newHappyBackGridApi = gridApi;
            $scope.newHappyBackGridApi.selection.on.rowSelectionChanged($scope,function (row) {
                //取消勾选的时候校验一下是否为已参加的子类型，若是，则把勾打上，不给取消
                if (!row.isSelected){
                    for(var i=0; i<$scope.joinHbTypes.length; i++){
                        if(row.entity.activityTypeNo == $scope.joinHbTypes[i]){
                            row.isSelected = true;
                            break;
                        }
                    }
                }
            })
        }
    };

    //超级返活动
	$scope.superHappyBackGrid = {
		enableSelectAll : false,//取消表头的全选与全不选
		columnDefs: [
			{field: 'activityTypeNo',displayName: '欢乐返子类型编号',width:160},
			{field: 'activityTypeName',displayName: '欢乐返子类型名称',width:160},
			{field: 'transAmount',displayName: '交易金额（元）',width:160,cellTemplate:'<div class="lh30">{{row.entity.transAmount}}元<div/>'},
			{field: 'cashBackAmount',displayName: '返现金额（元）',width:160,cellTemplate:'<div class="lh30">{{row.entity.cashBackAmount}}元<div/>'},
			{field: 'repeatRegisterAmount',displayName: '重复注册返现金额（元）',width:200,cellTemplate:'<div class="lh30" ng-show="row.entity.repeatRegisterAmount!=null">{{row.entity.repeatRegisterAmount}}元<div/>'},
			{field: 'repeatRegisterAmount',displayName: '第1次考核奖励金额（元）',width:200,cellTemplate:'<div class="lh30" ng-show="row.entity.oneRewardAmount!=null">{{row.entity.oneRewardAmount}}元<div/>'},
			{field: 'repeatRegisterAmount',displayName: '第1次考核重复注册奖励金额（元）',width:260,cellTemplate:'<div class="lh30" ng-show="row.entity.oneRepeatRewardAmount!=null">{{row.entity.oneRepeatRewardAmount}}元<div/>'},
			{field: 'repeatRegisterAmount',displayName: '第2次考核奖励金额（元）',width:200,cellTemplate:'<div class="lh30" ng-show="row.entity.twoRewardAmount!=null">{{row.entity.twoRewardAmount}}元<div/>'},
			{field: 'repeatRegisterAmount',displayName: '第2次考核重复注册奖励金额（元）',width:260,cellTemplate:'<div class="lh30" ng-show="row.entity.twoRepeatRewardAmount!=null">{{row.entity.twoRepeatRewardAmount}}元<div/>'},
			{field: 'repeatRegisterAmount',displayName: '第3次考核奖励金额（元）',width:200,cellTemplate:'<div class="lh30" ng-show="row.entity.threeRewardAmount!=null">{{row.entity.threeRewardAmount}}元<div/>'},
			{field: 'repeatRegisterAmount',displayName: '第3次考核重复注册奖励金额（元）',width:260,cellTemplate:'<div class="lh30" ng-show="row.entity.threeRepeatRewardAmount!=null">{{row.entity.threeRepeatRewardAmount}}元<div/>'},
			{field: 'repeatRegisterAmount',displayName: '第4次考核奖励金额（元）',width:200,cellTemplate:'<div class="lh30" ng-show="row.entity.fourRewardAmount!=null">{{row.entity.fourRewardAmount}}元<div/>'},
			{field: 'repeatRegisterAmount',displayName: '第4次考核重复注册奖励金额（元）',width:280,cellTemplate:'<div class="lh30" ng-show="row.entity.fourRepeatRewardAmount!=null">{{row.entity.fourRepeatRewardAmount}}元<div/>'},
		],
		onRegisterApi: function(gridApi) {
			$scope.superHappyBackGridApi = gridApi;
			$scope.superHappyBackGridApi.selection.on.rowSelectionChanged($scope,function (row) {
				//取消勾选的时候校验一下是否为已参加的子类型，若是，则把勾打上，不给取消
				if (!row.isSelected){
					for(var i=0; i<$scope.joinHbTypes.length; i++){
						if(row.entity.activityTypeNo == $scope.joinHbTypes[i]){
							row.isSelected = true;
							break;
						}
					}
				}
			})
		}
	};

	//获取欢乐返子类型
	$scope.happyGridApi = [];
	$scope.LastSelectBpId = [];
	$scope.selectFlag = false;
	$scope.getHappyBackType = function(){
		var bpIds2 = [];
		//var bpIds = $scope.oldBpIds;
		var bpIds = [];
		$scope.oldBpIds.forEach(function (e) {
			bpIds.push(e);
		});
		//新增的业务产品
		$scope.addbpData.forEach(function (e) {
			bpIds.push(e.key1);
			bpIds2.push(e.key1);
		});

		//如果业务产品没有变化，则不需要重新查询
		if($scope.LastSelectBpId.toString() == bpIds2.toString() && $scope.selectFlag){
			return;
		}
		$scope.selectFlag = true;
		$scope.LastSelectBpId = bpIds2;


		$http.post('agentInfo/selectHappyBackTypeWithTeamId?teamId=' + bpIds,
			{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			.success(function(data) {
				if (data.status) {
					$scope.happyBackData = data.list;
					//勾选原本已选中的
					if($scope.joinHbTypes.length > 0){
						$scope.happyBackData.forEach(function (e) {
							for(var i=0; i<$scope.joinHbTypes.length; i++){
								if(e.activityTypeNo == $scope.joinHbTypes[i]){
									e.select = true;
									e.disabled = true;
									break;
								}
							}
						});
					}

					$scope.showTitle = true;
					if(bpIds.length < 1){//默认展示全部，不分组
						$scope.showTitle = false;
						$scope.happyBackData.forEach(function (e) {
							e.teamId = "110";
						});
					}

					$scope.HappyBackDataMap = [];
					angular.forEach($scope.happyBackData, function (e) {
						var teamId = e.teamId;
						if($scope.HappyBackDataMap[teamId] === undefined){
							$scope.HappyBackDataMap[teamId] = [];
						}
						$scope.HappyBackDataMap[teamId].push(e);
					});

					$scope.happyTeams = [];
					$scope.happyBackDataList = [];
					angular.forEach($scope.HappyBackDataMap, function (data, index) {
						$scope.happyTeams.push(index);
						$scope.happyBackDataList[index] = {
							data: data,
							useExternalPagination: true,		  //开启拓展名
							enableSelectAll : false,//取消表头的全选与全不选
							columnDefs: $scope.happyBackColumnDefs,
							onRegisterApi: function(gridApi) {
								$scope.happyGridApi[index] = gridApi;
							}
						}

					});

				} else {
					$scope.notice("获取所有的欢乐返子类型失败");
				}
			}).error(function() {
			$scope.notice("获取所有的欢乐返子类型失败");
		});
	}

	//获取欢乐返子类型
	$http.post('agentInfo/selectHappyBackType')
		.success(function(data) {
			if (data.status) {
                $scope.newHappyBackGrid.data = data.xhlfList;
				$scope.superHappyBackGrid.data = data.superList;
			} else {
				$scope.notice("获取所有的欢乐返子类型失败");
			}
		}).error(function() {
			$scope.notice("获取所有的欢乐返子类型失败");
		});

	//将代理商已参加的欢乐返子类型打勾
	$scope.getJoinHbTypes = function(){
		if($scope.joinHbTypes.length == 0){
			return;
		}
        angular.forEach($scope.newHappyBackGridApi.grid.rows,function(row){
            for(var i=0; i<$scope.joinHbTypes.length; i++){
                if(row.entity.activityTypeNo == $scope.joinHbTypes[i]){
                    row.isSelected = true;
                    break;
                }
            }
        });
	}
	//将代理商已参加的超级欢乐返活动子类型打勾
	$scope.getSuperTypes = function(){
		if($scope.joinHbTypes.length == 0){
			return;
		}
		angular.forEach($scope.superHappyBackGridApi.grid.rows,function(row){
			for(var i=0; i<$scope.joinHbTypes.length; i++){
				if(row.entity.activityTypeNo == $scope.joinHbTypes[i]){
					row.isSelected = true;
					break;
				}
			}
		});
	}

	$scope.switchProduct=function(row){
		if(row.entity.key2){
			$scope.serviceText = "确定开启？";
		} else {
			$scope.serviceText = "确定关闭？所有的下级也会一起关闭！";
		}
        SweetAlert.swal({
            title: $scope.serviceText,
//            text: "服务状态为关闭后，不能正常交易!",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "提交",
            cancelButtonText: "取消",
            closeOnConfirm: true,
            closeOnCancel: true },
	        function (isConfirm) {
	            if (isConfirm) {
	            	if(row.entity.key2==true){
	            		row.entity.key2=1;
	            	} else if(row.entity.key2==false){
	            		row.entity.key2=0;
	            	}
	            	var data={"bpId":row.entity.key1,"status":row.entity.key2,"agentNo":$stateParams.agentNo};
	            	$http.post('agentInfo/updateAgentProStatus',angular.toJson(data)).success(function(msg){
	            		if(msg.status){
	            			$scope.notice(msg.msg);
	            		}else{
	            			if(row.entity.key2==true){
	    	            		row.entity.key2 = false;
	    	            	} else {
	    	            		row.entity.key2 = true;
	    	            	}
	            			$scope.notice(msg.msg);
	            		}
	            	})
	            	.error(function(data){
	            		if(row.entity.key2==true){
    	            		row.entity.key2 = false;
    	            	} else {
    	            		row.entity.key2 = true;
    	            	}
	            		$scope.notice("服务器异常")
	            	});
	            } else {
	            	if(row.entity.key2==true){
	            		row.entity.key2 = false;
	            	} else {
	            		row.entity.key2 = true;
	            	}
	            }
        });
    };
	
    $scope.fr=[{text:'成本价+固定分润比',value:5},{text:'成本价+阶梯分润比',value:6}];

	/*$scope.shareList = {
		data: 'shareData',
		enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        useExternalPagination: true,		  //开启拓展名
		columnDefs: [*/
	$scope.shareListColumnDefs = [
            {field: 'index',displayName: '序号',width: 100,cellTemplate: "<span class='checkbox' ng-style='{color: row.entity.red ? \"red\": \"black\"}'>{{rowRenderIndex + 1}}</span>"},
		    {field: 'checkStatus',displayName:'审核状态',width:135,cellFilter:"formatDropping:"+angular.toJson($scope.checkStatus)},
		    {field: 'uncheckStatus',displayName: '未生效审核状态',width: 180},
		    {field: 'bpName',displayName: '业务产品名称',width: 150},
		    {field: 'serviceName',displayName: '服务名称',width: 150},
		    /*{field: 'serviceType',displayName: '服务种类',width: 150,cellTemplate:
				'<div class="lh30" ng-show="row.entity.serviceType!=10000&&row.entity.serviceType!=10001"><span ng-bind="row.entity.serviceType | serviceTypeFilter"/></div>'
				+'<div class="lh30" ng-show="row.entity.serviceType==10000||row.entity.serviceType==10001"><span ng-bind="row.entity.serviceType2 | serviceTypeFilter"/>-提现</div>'
				},*/
//			{field: 'serviceName',displayName: '服务名称',width: 150},
			{field: 'cardType',displayName: '银行卡种类',width: 150,cellFilter:"formatDropping:"+$scope.cardTypeStr},
			{field: 'holidaysMark',displayName: '节假日标志',width: 150,cellFilter:"formatDropping:"+$scope.holidaysStr},
			{field: 'cost',displayName: '代理商成本',width: 130,cellTemplate:
				'<div class="lh30" ng-show="row.entity.isOldShare">{{row.entity[col.field]}}</div>'
				//如果不是体现服务，也就是交易服务，后面显示%
				+'<div ng-show="row.entity.serviceType!=10000&&row.entity.serviceType!=10001" style="width:98%;height:98%;"> '
				+'<input type="text" style="width:80%;height:98%;" class="ui-widget input" ng-change="grid.appScope.colorIndex(row)" ng-readonly="false" ng-model="row.entity[col.field]"/>'
				+'%'
				+'</div>'
				//如果是体现服务，后面显示单位元
				+'<div ng-show="row.entity.serviceType==10000||row.entity.serviceType==10001" style="width:98%;height:98%;"> '
				+'<input type="text" style="width:80%;height:98%;" class="ui-widget input" ng-change="grid.appScope.colorIndex(row)" ng-readonly="false" ng-model="row.entity[col.field]"/>'
				+'元'
				+'</div>'
			},
			{field: 'shareProfitPercent',displayName: '代理商固定分润百分比',width: 400,cellTemplate:
				'<div class="lh30" ng-show="row.entity.isOldShare"><span ng-show="row.entity.shareProfitPercent!=null">{{row.entity.shareProfitPercent}}%</div>'+
				'<div ng-show="row.entity.isNewShare">'+
				'<div class="col-sm-10 checkbox" ng-show="row.entity.profitType==5" style="float:left;width:280px;">{{row.entity.shareProfitPercent}}%'
				+'</div>'
				+'<div class="col-sm-10 checkbox" ng-show="row.entity.profitType==6" style="width:280px; padding:4px;">{{row.entity.ladderRate}}'
				+'</div>'
				+'<button ng-click="grid.appScope.ladderFr(row)" class="btn " type="button" >修改分润比例</button>'
				+'</div>'
			},
			{field: 'aaa',displayName: '操作',width: 130,pinnedLeft:true,cellTemplate:
        		'<div class="lh30" ng-show="row.entity.isOldShare&&grid.appScope.hasPermit(\'agent.updateShare\')">'+
        			'<a ng-click="grid.appScope.editShare(row.entity)" >修改分润</a>'+
        		'</div>'
            }
		];
	//};

    $scope.colorIndex = function(row){
        row.entity.red = true;
    }
	//进入修改旧分润模块时，需要携带数据：服务的基本信息、原分润的历史记录
	$scope.editShare=function(record){
		$scope.shareModal = 1;
		$('#editShareModel').modal('show');
		$scope.shareInfo = record;
		$scope.shareInfo.cardTypeStr = $scope.shareInfo.cardType;
		$scope.newShareInfo = {profitType:5,shareProfitPercent:100};
		$scope.oldShareInfo = {};
		$scope.ladderInfo1 = {m1:0,m2:"",m3:"",m4:"",m5:"",m6:"",m7:"",m8:"",m9:"无穷大"};
		$scope.queryShare(record);
	}
	//点击确定时，发送请求到后台，提交新的分润的信息
	$scope.submitShare = function(record){
		var efficientDate = $scope.newShareInfo.efficientDate;
		var nowDate = new Date();
        var min=nowDate.getMinutes();
        nowDate.setMinutes(min+5);
		var nowMilliseconds =  nowDate.getTime();
		if(efficientDate <nowMilliseconds){
	    	$scope.notice("生效日期必须大于等于当前日期加五分钟！");
	    	return;
	    }
		var i = 0;
		angular.forEach($scope.shareHistoryInfo,function(data){
			if(data.checkStatus==0){
				i++;
			}
		});
		if(i>0){
			$scope.notice("待审核的分润记录只能有一条");
			return;
		}
	    $scope.newShareInfo.shareId = record.id;
	    $scope.newShareInfo.serviceId = record.serviceId;
	    $scope.newShareInfo.serviceType = record.serviceType;
		$scope.newShareInfo.rateType = record.rateType;
	    $scope.newShareInfo.ladderRate = $scope.ladderInfo1.m2+"%<"+$scope.ladderInfo1.m3+"<"+$scope.ladderInfo1.m4+"%<"+$scope.ladderInfo1.m5+"<"+
	    	$scope.ladderInfo1.m6+"%<"+$scope.ladderInfo1.m7+"<"+$scope.ladderInfo1.m8+"%";
	    $http.post('agentInfo/addNewShare',angular.toJson($scope.newShareInfo)).success(function(msg){
	    	if(msg.status){
	    		$scope.notice(msg.msg);
	    		$scope.queryShare(record);
	    		$scope.oldShareInfo = {profitType:1};
	    	} else {
	    		$scope.notice(msg.msg);
	    	}
	    }).error(function(){
	    	$scope.notice("新增分润失败!");
	    })
	}
	//进入设置新分润的模块时
	$scope.ladderFr=function(row){
		$('#ladderFrModel').modal('show');
		$scope.info= {entity:angular.copy(row.entity),row:row,m1:0,m2:"",m3:"",m4:"",m5:"",m6:"",m7:"",m8:"",m9:"无穷大"};
//		console.log(row.entity.ladderRate)
		if(row.entity.ladderRate!=null){
//			var strArr = row.entity.ladderRate.toString().spilt("<");
			var strArr = row.entity.ladderRate.toString().split("<");
			$scope.info.m2 = parseInt(strArr[0].split("%")[0]);
			$scope.info.m3 = parseInt(strArr[1].split("%")[0]);
			$scope.info.m4 = parseInt(strArr[2].split("%")[0]);
			$scope.info.m5 = parseInt(strArr[3].split("%")[0]);
			$scope.info.m6 = parseInt(strArr[4].split("%")[0]);
			$scope.info.m7 = parseInt(strArr[5].split("%")[0]);
			$scope.info.m8 = parseInt(strArr[6].split("%")[0]);
		}
	}
	//点击确定时,保存阶梯分润并在表格显示
	$scope.addShare = function(row){
		$('#ladderFrModel').modal('hide');
		row.entity.profitType = $scope.info.entity.profitType;
		row.entity.shareProfitPercent = $scope.info.entity.shareProfitPercent;
		row.entity.ladderRate=$scope.info.m2+"%<"+$scope.info.m3+"<"+$scope.info.m4+"%<"+$scope.info.m5+"<"+
			$scope.info.m6+"%<"+$scope.info.m7+"<"+$scope.info.m8+"%";
	}
	$scope.shareHistoryInfo = [];
	//查询分润的历史记录
	$scope.shareHistoryCappingShow=true;
	$scope.queryShare = function(record){
		$scope.shareHistoryInfo = [];
		$http.post('agentInfo/queryNewShareList',record.id).success(function(msg){
			angular.forEach(msg,function(data,index){
				$scope.shareHistoryInfo[$scope.shareHistoryInfo.length]={
					checkStatus:data.checkStatus,	
					efficientDate:data.efficientDate,
					profitType:data.profitType,
					income:data.income,
					cost:data.cost,
					shareProfitPercent:data.shareProfitPercent,
					ladderRate:data.ladderRate,
					id:data.id,
					effectiveStatus:data.effectiveStatus,
					mainStatus:data.mainStatus,
					costRateType:data.costRateType,
					costCapping:data.costCapping,
					rateType:data.rateType
				}
			});
			if(record.costRateType==5&&$scope.shareHistoryCappingShow){
				$scope.shareHistoryCappingShow=false;
				$scope.shareHistory.columnDefs.splice($scope.shareHistory.columnDefs.length-2,0,{
					field: 'costCapping', displayName: '封顶手续费', width: 135});
			}
			if(record.costRateType!=5&&!$scope.shareHistoryCappingShow){
				$scope.shareHistoryCappingShow=true;
				$scope.shareHistory.columnDefs.splice($scope.shareHistory.columnDefs.length-3, 1);
			}
		}).error(function(){
		})
	}
	
	//删除分润
	$scope.deleteShare=function(record){
        SweetAlert.swal({
            title: "确认删除？",
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
//	            	var efficientDate = record.efficientDate;
//	        	    if(efficientDate <= new Date().getTime()){
//	        	    	$scope.notice("生效日期必须大于当前日期！");
//	        	    	return;
//	        	    }
	            	$http.post('agentInfo/delNewShare',record.id).success(function(msg){
	    	    		if(msg.status){
	    	    			$scope.notice("操作成功");
	    	    			$scope.queryShare($scope.shareInfo);
	    	    		}else{
	    	    			$scope.notice("操作失败");
	    	    		}
	    			}).error(function(){
	    				$scope.notice("操作失败");
	    			});
	            }
        });
    };
    
    //修改分润
    $scope.editShareInfo = function(entity){
    	$scope.shareModal = 0;
    	$scope.oldShareInfo = angular.copy(entity);
    	$scope.oldShareInfo.cost = $scope.oldShareInfo.cost.split('%')[0];
    }
    //提交旧的分润
    $scope.submitOldShare = function(record){
		var efficientDate = $scope.oldShareInfo.efficientDate;
		var nowDate = new Date();
        var min=nowDate.getMinutes();
        nowDate.setMinutes(min+5);
		var nowMilliseconds =  nowDate.getTime();
		if(efficientDate <nowMilliseconds){
	    	$scope.notice("生效日期必须大于等于当前日期加五分钟！");
	    	return;
	    }
		var i = 0;
		angular.forEach($scope.shareHistoryInfo,function(data){
			if(data.checkStatus==0&&data.id!=$scope.oldShareInfo.id){
				i++;
			}
		});
		if(i>0){
			$scope.notice("待审核的分润记录只能有一条");
			return;
		}
	    $scope.oldShareInfo.shareId = record.id;
	    $scope.oldShareInfo.serviceType = record.serviceType;
		$scope.oldShareInfo.rateType = record.rateType;
	    if($scope.oldShareInfo==6){
	    	$scope.oldShareInfo.ladderRate = $scope.ladderInfo1.m2+"%<"+$scope.ladderInfo1.m3+"<"+$scope.ladderInfo1.m4+"%<"+$scope.ladderInfo1.m5+"<"+
	    	$scope.ladderInfo1.m6+"%<"+$scope.ladderInfo1.m7+"<"+$scope.ladderInfo1.m8+"%";
	    }
	    
	    $http.post('agentInfo/updateShare',angular.toJson($scope.oldShareInfo)).success(function(msg){
	    	if(msg.status){
	    		$scope.notice(msg.msg);
	    		if(record.id==$scope.oldShareInfo.id){
	    			record = $scope.oldShareInfo;
	    		}
	    		$scope.queryShare(record);
//	    		$scope.oldShareInfo = {profitType:1};
	    		$scope.shareModal = 1;
	    	} else {
	    		$scope.notice(msg.msg);
	    	}
	    }).error(function(){
	    	$scope.notice("新的分润添加失败!");
	    })
	}
    
	
	//分润的历史记录
	$scope.shareHistory = {
		data: "shareHistoryInfo",
		enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        useExternalPagination: true,		  //开启拓展名
        columnDefs: [
			{field: 'id',displayName: '操作', width:135, 
		 	cellTemplate: '<a ng-click="grid.appScope.editShareInfo(row.entity)" ng-show="grid.appScope.hasPermit(\'agent.delNewShare\')&&row.entity.checkStatus!=1">修改 </a>'
		 	 +	'<a ng-click="grid.appScope.deleteShare(row.entity)" ng-show="grid.appScope.hasPermit(\'agent.delNewShare\')&&row.entity.checkStatus!=1'
		 	 + ' &&row.entity.mainStatus!=1">删除</a>'
			},
			 {field: 'effectiveStatus',displayName: '当前是否生效', width:135, cellFilter:"formatDropping:"+angular.toJson($scope.effectiveStatus)},
             {field: 'checkStatus',displayName:'审核状态',width:135,cellFilter:"formatDropping:"+angular.toJson($scope.checkStatus)},
             {field: 'efficientDate',displayName: '生效日期', width:135, cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
             {field: 'profitType',displayName: '分润方式', width:135, cellFilter:"formatDropping:"+angular.toJson($scope.fr)},
 			 {field: 'income',displayName: '代理商收益', width:135},
 			 {field: 'cost',displayName: '代理商成本', width:135},
 			 {field: 'shareProfitPercent',displayName: '代理商固定分润百分比', width:200, cellTemplate:'<span class="checkbox" ng-show="(row.entity.profitType==5||row.entity.profitType==6)'
 				+' &&row.entity.shareProfitPercent!=null">'
 				+'{{row.entity.shareProfitPercent}}%</span>'},
 			 {field: 'ladderRate',displayName: '阶梯分润比例' ,width:135 }
 			 
 		]
	}
	
	var rateRowsList={};
	$scope.rateList = {
		data: 'rateData',
		enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
		columnDefs: [
		     {field: 'isGlobal',displayName: '与公司管控费率相同',width: 180,
		    	 cellTemplate:
		    		 '<input type="checkbox" ng-model="row.entity.isGlobal" ng-true-value="1" ng-false-value="0"/>'
		     },
             {field: 'serviceName',displayName: '服务名称',width: 150},
             {field: 'cardType',displayName: '银行卡种类',width: 150,cellFilter:"formatDropping:"+$scope.cardTypeStr},
             {field: 'holidaysMark',displayName: '节假日标志',width: 150,cellFilter:"formatDropping:"+$scope.holidaysStr},
		     {field: 'rateType',displayName: '费率方式',width: 200,
            	 cellFilter:'formatDropping:'+$scope.merRateStr	 
		     },
		     {field: 'merRate',displayName: '商户费率',width: 150,
		    	 cellTemplate:
		    		 '<div ng-switch="$eval(\'row.entity.isGlobal\')">'
						+'<div ng-switch-when="1">'
							+'<p ng-bind="row.entity[col.field]"></p>'
						+'</div>'
						+'<div ng-switch-when="0">'
							+'<input type="text" style="width:98%;height:34px;" class="ui-widget input" ng-model="row.entity[col.field]"/>'
						+'</div>'
					+'</div>'
		     }
		],
		onRegisterApi: function(gridApi) {                //选中行配置
	        $scope.rateGridApi = gridApi;
		}
	};
	
	var quotaIsGlobal='<div ng-switch="$eval(\'row.entity.isGlobal\')">'
		+'<div ng-switch-when="1">'
			+'<p ng-bind="row.entity[col.field]"></p>'
		+'</div>'
		+'<div ng-switch-when="0">'
			+'<input type="text" style="width:98%;height:34px;" class="ui-widget input" ng-model="row.entity[col.field]"/>'
		+'</div>'
	+'</div>';
	var quotaRowList={};
	$scope.quotaList = {
		data: 'quotaData',
		enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
		columnDefs: [
            {field: 'isGlobal',displayName: '与公司管控限额相同',width: 180,
            	 cellTemplate:
		    		 '<input type="checkbox" ng-model="row.entity.isGlobal" ng-true-value="1" ng-false-value="0"/>'
            },
            {field: 'serviceName',displayName: '服务名称',width: 150},
            {field: 'cardType',displayName: '银行卡种类',width: 150,cellFilter:"formatDropping:"+$scope.cardTypeStr},
            {field: 'holidaysMark',displayName: '节假日标志',width: 150,cellFilter:"formatDropping:"+$scope.holidaysStr},
            {field: 'singleDayAmount',displayName: '单日最大交易金额',width: 160,pinnable: false,sortable: false,editable:true,
            	 cellTemplate:quotaIsGlobal
            },
            {field: 'singleMinAmount',displayName: '单笔最小交易额',width: 150,pinnable: false,sortable: false,editable:true,
           	 cellTemplate:quotaIsGlobal
           },
            {field: 'singleCountAmount',displayName: '单笔最大交易额',width: 150,pinnable: false,sortable: false,editable:true,
            	 cellTemplate:quotaIsGlobal
            },
            {field: 'singleDaycardAmount',displayName: '单日单卡最大交易额',width: 180,pinnable: false,sortable: false,editable:true,
            	 cellTemplate:quotaIsGlobal
            },
            {field: 'singleDaycardCount',displayName: '单日单卡最大交易笔数',width: 240,pinnable: false,sortable: false,editable:true,
            	 cellTemplate:quotaIsGlobal
            }
		],
		onRegisterApi: function(gridApi) {                //选中行配置
	        $scope.quotaGridApi = gridApi;
    	}
	};

	var bpId2 = [];//备份选中的业务产品，与下一次选中的业务产品做比较，不同则发送请求
	$scope.getShareList=function(){
		//如果没有勾选业务产品，则不能发送请求
		var parentProducts = $scope.bpData;
		var agentProducts = $scope.agentProducts;
		if (parentProducts.length == 0){
			$scope.bpTp = false;
			$scope.quotaData = [];
			$scope.rateData = [];
			$scope.shareData = [];
			return false;
		} else {
			$scope.bpTp = true;
		}
		//如果选中的业务产品没有发生改变，就不会发送请求
		var bpIds = [];
		var j=0;
		for(var i= 0; i<$scope.addbpData.length; i++){
            bpIds[i] = $scope.addbpData[i].key1;
			/*if(parentProducts[i].aaa && !parentProducts[i].disabled){
				bpIds[j] = parentProducts[i].key1;
				j++;
			}*/
		}
		if(bpIds.toString()==bpId2.toString()){
			// 分组
			$scope.sortByTeamId();
			return false;
		}
		
		$http.post('agentInfo/getAgentServices',angular.toJson({"bpIds":bpIds,"agentNo":0}))
			.success(function(data, status, headers, config) {
				console.log(data)
				$scope.quotaData.length = quotaLength;
				$scope.rateData.length = rateLength;
				$scope.shareData.length = shareLength;
				angular.forEach(data.rates, function(data,index){
					$scope.rateData[rateLength+index] = {
						id:data.id,
						isGlobal:data.isGlobal,
						serviceId:data.serviceId,
						serviceName:data.serviceName,
						cardType:data.cardType,
						holidaysMark:data.holidaysMark,
						merRate:data.merRate,
						rateType:data.rateType,
						checkStatus:data.checkStatus,
						lockStatus:data.lockStatus
					};
					if(data.allowIndividualApply==1){
						$scope.shareData[$scope.shareData.length] = {
								teamId:data.teamId,
								serviceId:data.serviceId,
								serviceName:data.serviceName,
								cardType:data.cardType,
								holidaysMark:data.holidaysMark,
								id:data.id,
								checkStatus:data.checkStatus,
								lockStatus:data.lockStatus,
								profitType:5,
								bpId:data.bpId,
								bpName:data.bpName,
								serviceType:data.serviceType,
								serviceType2:data.serviceType2,
								shareProfitPercent:'100',
								isNewShare:true,
								rateType:data.rateType
						};
					}
				});
				angular.forEach(data.quotas, function(data,index){
					$scope.quotaData[quotaLength+index] = {
						id:data.id,
						serviceId:data.serviceId,
						serviceName:data.serviceName,
						cardType:data.cardType,
						holidaysMark:data.holidaysMark,
						profitType:data.profitType,
						merRate:data.merRate,
						isGlobal:data.isGlobal,
						singleMinAmount:data.singleMinAmount,
						singleCountAmount:data.singleCountAmount,
						checkStatus:data.checkStatus,
						lockStatus:data.lockStatus,
						singleDayAmount:data.singleDayAmount,
						singleDaycardAmount:data.singleDaycardAmount,
						singleDaycardCount:data.singleDaycardCount
					};
				});
				bpId2 = bpIds;	//备份此时选中的业务产品

				//分组
				$scope.sortByTeamId();

			}).error(function(data, status, headers, config) {
			});
	}

	$scope.isCostCapping=true;
	$scope.sortByTeamId = function () {
		$scope.shareDataMap = [];
		angular.forEach($scope.shareData, function (data, index) {
			var teamId = data.teamId;
			if($scope.shareDataMap[teamId] === undefined){
				$scope.shareDataMap[teamId] = [];
			}
			$scope.shareDataMap[teamId].push(data);
			if(data.rateType=="6"&&$scope.isCostCapping){
				$scope.isCostCapping=false;
				$scope.shareListColumnDefs.splice($scope.shareListColumnDefs.length-2,0,{
					field: 'costCapping', displayName: '封顶手续费', width: 130,
					cellTemplate:
						'<div class="lh30" ng-show="row.entity.isOldShare">{{row.entity[col.field]}}</div>'
						+ '<div ng-show="row.entity.isNewShare&&row.entity.rateType==6" style="width:98%;height:98%;"> '
						+ '<input type="text" style="width:80%;height:98%;" class="ui-widget input" ng-readonly="false" ng-model="row.entity[col.field]"/>'
						+ '元'
						+ '</div>'
				});
			}
		});
		$scope.teamdIds = [];
		$scope.shareDataList = [];
		angular.forEach($scope.shareDataMap, function (data, index) {
			$scope.teamdIds.push(index);
			$scope.shareDataList[index] = {
				data: data,
				useExternalPagination: true,		  //开启拓展名
				columnDefs: $scope.shareListColumnDefs
			}
		});
	}
	
	//上传App图片,定义控制器路径
	var uploaderAppFlag = true;
    var uploaderApp = $scope.uploaderApp = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数 
        removeAfterUpload: true,  //上传后删除文件
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    uploaderApp.filters.push({
        name: 'imageFilter',
        fn: function(item, options) {
            return this.queue.length < 1;
        }
    });
    //过滤格式
     uploaderApp.filters.push({
         name: 'imageFilter',
         fn: function(item /*{File|FileLikeObject}*/, options) {
             var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
             return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
         }
     });
     uploaderApp.onAfterAddingFile = function(fileItem) {
    	 uploaderAppFlag = false;
    	 $scope.appLogoHide = true;
    	 removeAppLogoFlag = false;
 	}
 	uploaderApp.removeFromQueue = function(value){
 		uploaderAppFlag = true;
 		if($scope.agent.clientLogo){
 			$scope.appLogoHide = false;
 		}
 		var index = this.getIndexOfItem(value);
         var item = this.queue[index];
         if (item.isUploading) item.cancel();
         this.queue.splice(index, 1);
         item._destroy();
         this.progress = this._getTotalProgress();
 	}
 	//上传Web图片,定义控制器路径
 	var uploaderWebFlag = true;
    var uploaderWeb = $scope.uploaderWeb = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数 
        removeAfterUpload: true,  //上传后删除文件
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    uploaderWeb.filters.push({
        name: 'imageFilter',
        fn: function(item, options) {
            return this.queue.length < 1;
        }
    });
    //过滤格式
     uploaderWeb.filters.push({
         name: 'imageFilter',
         fn: function(item /*{File|FileLikeObject}*/, options) {
             var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
             return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
         }
     });
     uploaderWeb.onAfterAddingFile = function(fileItem) {
    	 uploaderWebFlag = false;
    	 $scope.webLogoHide = true;
 	}
 	uploaderWeb.removeFromQueue = function(value){
 		uploaderWebFlag = true;
 		if($scope.agent.managerLogo != null){
			$scope.webLogoHide = false;
		}
 		var index = this.getIndexOfItem(value);
        var item = this.queue[index];
        if (item.isUploading) item.cancel();
        this.queue.splice(index, 1);
        item._destroy();
        this.progress = this._getTotalProgress();
 	}
 	
 	$scope.removeAppLogo = function(){
 		$scope.appLogoHide = true;
 		removeAppLogoFlag = true;
 	}
 	
 	$scope.removeWebLogo = function(){
 		$scope.webLogoHide = true;
 		removeWebLogoFlag = true;
 	}

 	$scope.subDisable = false;
 	$scope.submit = function(){

		if($scope.agent.agentType=="11"){
			if($scope.agent.countLevel!=1&&$scope.agent.countLevel!=2){
				$scope.notice("超级盟主的代理商,代理商层级链条长度限制只能为1或者2!");
				return;
			}
		}

 		$scope.subDisable = true;
 		//1.没有等待上传的
 		if(uploaderAppFlag && uploaderWebFlag){
 			$scope.submitData();
 		}
 		//2.有AppLOGO等待上传
 		if(!uploaderAppFlag && uploaderWebFlag){
 			uploaderApp.uploadAll();// 上传App端图片
 			uploaderApp.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
				if (response.url != null) { // 回调参数url
					$scope.agent.clientLogo = response.url;
					$scope.submitData();
				}
	        }
 		}
 		//3.appLogo没有，webLogo有
		if(uploaderAppFlag && !uploaderWebFlag){
			uploaderWeb.uploadAll();// 上传WebLogo端图片
			uploaderWeb.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
				if (response.url != null) { // 回调参数url
					$scope.agent.managerLogo = response.url;
					$scope.submitData();
				}
	        }
		}
		//4.appLogo和webLogo都有
		if(!uploaderAppFlag && !uploaderWebFlag){
			uploaderApp.uploadAll();// 上传App端图片
			uploaderApp.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
				if (response.url != null) { // 回调参数url
					$scope.agent.clientLogo = response.url;
					uploaderWeb.uploadAll();// 上传Web端图片
					uploaderWeb.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
						if (response.url != null) { // 回调参数url
							$scope.agent.managerLogo = response.url;
							$scope.submitData();
						}
					}
				}
	        }
		}
 	}
 	
    $scope.submitData = function(){
    	if(removeAppLogoFlag){
			delete $scope.agent.clientLogo;
		}
		if(removeWebLogoFlag){
			delete $scope.agent.managerLogo;
		}
    	//获取选中业务产品的ID数组
    	var bpIds = [];
    	var j = 0;
    	debugger;
        for(var i= 0; i<$scope.addbpData.length; i++){
            bpIds[i] = $scope.addbpData[i].key1;
        }

    	//获取选中的欢乐返子类型
    	//var happyBackTypes = $scope.happyGridApi.selection.getSelectedRows();
		var happyBackList = [];
        var happyBackHas = [];
        if($scope.happyBackData != undefined){
			$scope.happyBackData.forEach(function (e) {
				if(e.select){
					var activityTpyeNo = e.activityTypeNo;
					if(happyBackHas[activityTpyeNo] == "" || null == happyBackHas[activityTpyeNo]){// 去重
						happyBackList.push(e);
						happyBackHas[activityTpyeNo]=activityTpyeNo;
					}
				}
			});
		}
        var newHappyBackTypes = $scope.newHappyBackGridApi.selection.getSelectedRows();
		var superHappyBackTypes = $scope.superHappyBackGridApi.selection.getSelectedRows();

		var data = {"agentInfo":$scope.agent,"bpData":bpIds,"shareData":$scope.shareData.slice(shareLength),
    			"rateData":$scope.rateData,"quotaData":$scope.quotaData,"happyBackTypes":happyBackList,
            	"newHappyBackTypes":newHappyBackTypes,"superHappyBackTypes":superHappyBackTypes};
    	$http.post('agentInfo/updateAgent',angular.toJson(data)).success(function(msg){
    		$scope.subDisable = false;
    		if(msg.status){
    			$scope.notice(msg.msg);
    			$state.transitionTo('agent.queryAgent',null,{reload:true});
    		}else{
    			$scope.noticeHtml(msg.msg);
    		}
    	}).error(function(){
    		$scope.subDisable = false;
    	});
    }
    $scope.returnPage = function(){
    	history.go(-1)
    }
    
    //===sober====================
    var ladderRateDate = {m1:0,m2:"",m3:"",m4:"",m5:"",m6:"",m7:"",m8:"",m9:"无穷大"};
    $scope.solutionModalClose=function(){
		 $scope.modalInstance.dismiss();
	 }
	 
	 $scope.solutionModalOk=function(){
		 $scope.modalInstance.close($scope);
	 }
//	$scope.ladderFr=function(row){
//		var modalScope = $scope.$new();
//		modalScope.info=ladderRateDate;
//		var modalInstance = $uibModal.open({
//           templateUrl : 'views/agent/ladderFrModal.html',  //指向上面创建的视图
//           controller : 'editAgentCtrl',// 初始化模态范围
//           scope:modalScope,
//           size:'lg'
//       })
//       modalScope.modalInstance=modalInstance;
//       modalInstance.result.then(function(selectedItem){ 
//		   var info=selectedItem.info;
//		   ladderRateDate = selectedItem.info;
//		   row.entity.ladderRate=info.m2+"%<"+info.m3+"<"+info.m4+"%<"+info.m5+"<"+info.m6+"%<"+info.m7+"<"+info.m8+"%";
//       },function(){
//           $log.info('取消: ' + new Date())
//       })
//		
//	}
});
//.filter('serviceTypeFilter', function () {
//	return function (value) {
//		switch(value) {
//			case 4 :
//				return "POS刷卡-标准类";
//				break;
//			case 9 :
//				return "POS刷卡-优惠类";
//				break;
//			case 10000 :
//				return "账户提现";
//				break;
//			case 10001 :
//				return "关联提现服务";
//				break;
//			case 10002 :
//				return "支付宝扫码支付";
//				break;
//			case 10003 :
//				return "微信扫码支付";
//				break;
//			case 10004 :
//				return "快捷支付";
//				break;
//			case 10005 :
//				return "微信主扫"
//				break;
//			case 10006 :
//				return "支付宝主扫"
//				break;
//		}
//	}
//});
