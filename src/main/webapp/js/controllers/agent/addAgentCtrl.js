angular.module('inspinia',['angularFileUpload','infinity.angular-chosen']).controller("addAgentCtrl", function($scope, $http, $state, $stateParams,uiGridConstants,i18nService,FileUploader,$log,$uibModal,$timeout) {
    i18nService.setCurrentLang('zh-cn');
	var self = this;
	$scope.merRateStr='[{text:"每笔固定金额",value:"1"},{text:"每笔扣率",value:"2"},{text:"每笔扣率带保底封顶",value:"3"},{text:"每笔扣率+固定金额",value:"4"},{text:"单笔阶梯扣率",value:"5"},{text:"扣率+封顶",value:"6"}]';
	$scope.teamType=[];
	$scope.agent={agentName:"",isOem:0,email:"",mobilephone:"",phone:"",linkName:"",teamId:"",saleName:"",invest:0,
			investAmount:0,agentArea:"",address:"",accountName:"",accountProvince:"",accountCity:"",accountType:2,bankName:"",accountNo:"",cnapsNo:"",isApprove:0,countLevel:-1,
        agentShareLevel:"",agentOem:"",agentType:"",setValue:""
	};
	$scope.type = [ {text : '个人',value :  1 }, {text : '个体商户',value :  2 }, {text : '企业商户',value :  3} ];
	$scope.shareBatchSet = true;
    $scope.agentShareLevel=[ {text : '请选择',value :  '' } ];
    for(var i=1;i<=20;i++){
        $scope.agentShareLevel.push({value:i,text:'Lv.'+i});
	}

	var uploadFlag = true;		//是否可以提交数据，默认是可以提交的，当有文件准备上传时，为false，上传完成后置为true
//	$http.get('teamInfo/queryTeamName.do').success(function(msg){
//		for(var i=0;i<msg.teamInfo.length;i++){
//			$scope.teamType.push({text:msg.teamInfo[i].teamName,value:msg.teamInfo[i].teamId});
//		}
//		$scope.agent.teamId=msg.teamInfo[msg.teamInfo.length-1].teamId;
//	});

	$scope.teams=[];
	$http.get('teamInfo/queryTeamName.do').success(function(msg){
		for(var i=0;i<msg.teamInfo.length;i++){
			$scope.teams.push({text:msg.teamInfo[i].teamName,value:msg.teamInfo[i].teamId});
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
			||$scope.agent.bankName==null || $scope.agent.bankName==""){
			$scope.accountNoFlag = true;
			return;
		}
		if($scope.agent.accountCity==null || $scope.agent.accountCity==""){
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
				}
			});
	}
//	   $scope.getUnionAccountNo=function(subBank){
//		   $scope.agent.cnapsNo=subBank;
//	   }

	$scope.getBankName=function(){
		$scope.agent.bankName = null;
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
    $scope.getPoscnapsNo= function(){
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
    };
	
	$scope.resetInvestAmount = function(){
		if($scope.agent.invest == 0){
			$scope.agent.investAmount = 0;
		}
	}
	//获取所有的业务产品信息
    $scope.queryBusinessProduct = function(){
        $http.post('businessProductDefine/selectAllInfoByBpId',"bpId="+$scope.product.productType,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
            ).success(function(data) {
                $scope.bpData = data;
            }).error(function(data, status, headers, config) {
            $scope.notice("获取所有的业务产品信息失败");
        });
    }
    $scope.queryBusinessProduct();
	
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
			$scope.agent.area = null;
		});
	}
	//县
	$scope.getAreas = function() {
		$scope.getAreaList($scope.agent.city,"",function(data){
			$scope.areaGroup = data;
		});
	}	
	
	$scope.bpData=[];
	$scope.shareData=[];
	$scope.rateData=[];
	$scope.quotaData=[];

    $scope.addbpData=[];
    var rowList = [];
    $scope.addbpList = {
        data: 'addbpData',
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs : [                           //表格数据
            {field: 'bpId',displayName: '业务产品ID',width: 200},
            {field: 'bpName',displayName: '业务产品名称',width: 200},
            {field: 'bpType',displayName: '类型',width: 200,cellFilter:"formatDropping:"+angular.toJson($scope.type)},
            {field: 'groupNo',displayName: '群组号',width: 200},
            {field: 'allowIndividualApply',displayName: '是否允许单独申请',width: 200,cellFilter:"formatDropping:"+angular.toJson($scope.bool)},
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
                            bpId:rowList[i].bpId,
                            bpName:rowList[i].bpName,
                            bpType:rowList[i].bpType,
                            groupNo:rowList[i].groupNo,
                            allowIndividualApply:rowList[i].allowIndividualApply
                        });
                        $scope.cancel();
                    }
                }
            }
        }
    };

    $scope.addData = function(row){
        if(row!=null&&row!=""){
            if($scope.checkData($scope.addbpData,row,null)){
                $scope.addbpData.push({
                    bpId:row.bpId,
                    bpName:row.bpName,
                    bpType:row.bpType,
                    groupNo:row.groupNo,
                    allowIndividualApply:row.allowIndividualApply
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
                    if(item.bpId==oldInfo.bpId){
                        continue;
                    }
                }
                if(item.bpId==info.bpId){
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
                if(row.bpId==dateItem.bpId){
                    $scope.addbpData.splice(j, 1);
                }
            }
            for(var j=0;j<$scope.bpData.length;j++){
                var dateItem=$scope.bpData[j];
                if(row.bpId==dateItem.bpId){
                    $scope.gridApiProduct.selection.unSelectRow($scope.bpData[j]);
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
			if(e.cost != null && e.cost != ""){
				e.shareProfitPercent = $scope.agent.setValue;
			}
		});
		$scope.agent.setValue = ""
	}

    $scope.bpList = {
        data: 'bpData',
        // enableHorizontalScrollbar: true,        //横向滚动条
        // enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs: [
             {field: 'bpId',displayName: '业务产品ID',width: 200},
             {field: 'bpName',displayName: '业务产品名称',width: 200},
             {field: 'bpType',displayName: '类型',width: 200,cellFilter:"formatDropping:"+angular.toJson($scope.type)},
             {field: 'groupNo',displayName: '群组号',width: 200},
             {field: 'allowIndividualApply',displayName: '是否允许单独申请',width: 200,cellFilter:"formatDropping:"+angular.toJson($scope.bool)},
        ],
		onRegisterApi : function(gridApi) {
			$scope.gridApiProduct = gridApi;
            //全选
            $scope.gridApiProduct.selection.on.rowSelectionChangedBatch($scope,function (rows) {
                if(rows[0].isSelected){
                    $scope.testRow = rows[0].entity;
                    for(var i=0;i<rows.length;i++){
                        rowList[rows[i].entity.bpId]=rows[i].entity;
                    }
                    $scope.addList(rowList);
                }else{
                    rowList={};
                    for(var i=0;i<rows.length;i++){
                        $scope.delteData(rows[i].entity);
                    }
                }
            })
			//单选的时候，将同组的业务产品做到：同时选中、同时取消
	         $scope.gridApiProduct.selection.on.rowSelectionChanged($scope,function (row) {
	            if(row.isSelected){
                    rowList[row.entity.bpId]=row.entity;
                    $scope.addData(row.entity);
	            	angular.forEach($scope.bpData,function(item){
	            		if(row.entity.groupNo!=null && item.groupNo==row.entity.groupNo
	            				&& item.allowIndividualApply==1){
	            			row.grid.api.selection.selectRow(item);
                            rowList[item.bpId]=item;
                            $scope.addData(item);
	            		}
		        	 });
	            }
	            else{
                    delete rowList[row.entity.bpId];
                    $scope.delteData(row.entity);
	            	angular.forEach(row.grid.rows,function(item){
	            		if(row.entity.groupNo!=null && item.entity.groupNo==row.entity.groupNo
	            				&& row.entity.allowIndividualApply==1){
	            			item.isSelected = false;
                            delete rowList[item.entity.bpId];
                            $scope.delteData(item.entity);
	            		}
		        	 });
	            }
	         })
		},
        isRowSelectable: function(row){ // 选中行
            if($scope.addbpData != null && $scope.addbpData.length>0){
                for(var i=0;i<$scope.addbpData.length;i++){
                    if(row.entity.bpId==$scope.addbpData[i].bpId){
                        row.grid.api.selection.selectRow(row.entity);
                    }
                }
            }
        }
    };
    $scope.fr=[{text:'固定分润比例',value:'5'},{text:'阶梯分润比例',value:'6'}];

	$scope.shareListColumnDefs = [
		{field: 'bpName', displayName: '业务产品名称', width: 200},
		{field: 'serviceName', displayName: '服务名称', width: 200},
		/*{
			field: 'serviceType', displayName: '服务种类', width: 150,
			cellTemplate:
				'<div class="lh30" ng-show="row.entity.serviceType!=10000&&row.entity.serviceType!=10001"><span ng-bind="row.entity.serviceType | serviceTypeFilter"/></div>'
				+ '<div class="lh30" ng-show="row.entity.serviceType==10000||row.entity.serviceType==10001"><span ng-bind="row.entity.serviceType2 | serviceTypeFilter"/>-提现</div>'

		},*/
		{field: 'cardType', displayName: '银行卡种类', width: 120, cellFilter: "formatDropping:" + $scope.cardTypeStr},
		{field: 'holidaysMark', displayName: '节假日标志', width: 120, cellFilter: "formatDropping:" + $scope.holidaysStr},
		{
			field: 'cost', displayName: '代理商成本', width: 130,
			cellTemplate:
			//如果不是体现服务，也就是交易服务，后面显示%
				'<div ng-show="row.entity.serviceType!=10000&&row.entity.serviceType!=10001" style="width:98%;height:98%;"> '
				+ '<input type="text" style="width:80%;height:98%;" class="ui-widget input" ng-readonly="false" ng-model="row.entity[col.field]"/>'
				+ '%'
				+ '</div>'
				//如果是体现服务，后面显示单位元
				+ '<div ng-show="row.entity.serviceType==10000||row.entity.serviceType==10001" style="width:98%;height:98%;"> '
				+ '<input type="text" style="width:80%;height:98%;" class="ui-widget input" ng-readonly="false" ng-model="row.entity[col.field]"/>'
				+ '元'
				+ '</div>'
		},
		{
			field: 'shareProfitPercent', displayName: '分润比例', width: 400,
			cellTemplate:
				'<div class="col-sm-10 checkbox" ng-show="row.entity.profitType==5" style="float:left;width:280px;">{{row.entity.shareProfitPercent}}%'
				+ '</div>'
				+ '<div class="col-sm-10 checkbox" ng-show="row.entity.profitType==6" style="width:280px; padding:4px;">{{row.entity.ladderRate}}'
				+ '</div>'
				+ '<button ng-click="grid.appScope.ladderFr(row)" class="btn " type="button" >修改分润比例</button>'
		},
	];



	
	//进入设置新分润的模块时
	$scope.ladderFr=function(row){
		$('#ladderFrModel').modal('show');
		$scope.info= {entity:angular.copy(row.entity),row:row,m1:0,m2:"",m3:"",m4:"",m5:"",m6:"",m7:"",m8:"",m9:"无穷大"};
		if(row.entity.ladderRate!=null){
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
	
	var rateRowsList={};
	$scope.rateList = {
		data: 'rateData',
		columnDefs: [
		     {field: 'isGlobal',displayName: '与公司管控费率相同',width: 180,
		    	 cellTemplate:
		    		 '<input type="checkbox" ng-model="row.entity.isGlobal"  ng-true-value="1" ng-false-value="0"/>'
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
							+'<p ng-bind="row.entity.merRate"></p>'
						+'</div>'
						+'<div ng-switch-when="0">'
							+'<input type="text" style="width:98%;height:98%;" class="ui-widget input" ng-model="row.entity.merRate"/>'
						+'</div>'
					+'</div>'
		     }
		]
	};
	
	var quotaIsGlobal='<div ng-switch="$eval(\'row.entity.isGlobal\')">'
		+'<div ng-switch-when="1">'
			+'<p ng-bind="row.entity[col.field]"></p>'
		+'</div>'
		+'<div ng-switch-when="0">'
			+'<input type="text" style="width:98%;height:98%;" class="ui-widget input" ng-model="row.entity[col.field]"/>'
		+'</div>'
	+'</div>';
	var quotaRowList={};
	$scope.quotaList = {
		data: 'quotaData',
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
            {field: 'singlMinAmount',displayName: '单笔最小交易额',width: 150,pinnable: false,sortable: false,editable:true,
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
		]
	};

	/*$scope.happyBackGrid = {
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
		}
	};*/

	$scope.happyBackColumnDefs = [
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


	//获取欢乐返子类型
	/*$http.post('agentInfo/selectHappyBackType')
		.success(function(data) {
			if (data.status) {
				$scope.happyBackGrid.data = data.list;
			} else {
				$scope.notice("获取所有的欢乐返子类型失败");
			}
		}).error(function() {  
			$scope.notice("获取所有的欢乐返子类型失败");
		});*/

	var teamFlag = true;
	$scope.changeTeamFlag = function(){
		teamFlag = true;
	}

	$scope.subDisable = true;
	var selectedProduct = []; 	//备份选中的业务产品，与下一次选中的业务产品做比较，不同则发送请求
	$scope.isCostCapping=true;
	//根据业务产品，获取对应的分润、费率、限额
	$scope.getShareList=function(){
		//如果没有勾选业务产品，则不能发送请求
		var products = $scope.addbpData;
		if (products.length == 0){
			$scope.bpTp = false;
			$scope.quotaData = [];
			$scope.rateData = [];
			$scope.shareData = [];
			//备份选中的业务产品，与下一次选中的业务产品做比较，不同则发送请求
			selectedProduct = angular.copy(products);
			return false;
		} else {
			$scope.subDisable = false;
			$scope.bpTp = true;
		}
		//如果选中的业务产品没有发生改变，就不会发送请求
		var bpIds = [];
		for(var i=0; i<products.length; i++){
			bpIds[i] = products[i].bpId;
		}
		var product2 = selectedProduct;
		var bpId2 = [];
		for(var i=0; i<product2.length; i++){
			bpId2[i] = product2[i].bpId;
		}	
		if(bpIds.toString()==bpId2.toString()){
			return false;
		}
		//备份选中的业务产品，与下一次选中的业务产品做比较，不同则发送请求
		selectedProduct = angular.copy(products);

		$http.post('agentInfo/getAgentServices',angular.toJson({bpIds:bpIds,agentNo:0}))
			.success(function(data, status, headers, config) {  
				$scope.quotaData.length= 0;
				$scope.rateData.length=0;
				$scope.shareData = [];
				$scope.rateTypeShow=true;
				angular.forEach(data.rates, function(data,index){
					$scope.rateData[index] = {id:data.id,isGlobal:1,serviceId:data.serviceId,serviceName:data.serviceName,cardType:data.cardType,holidaysMark:data.holidaysMark,
							merRate:data.merRate,rateType:data.rateType};
					if(data.allowIndividualApply==1){
						
						$scope.shareData[$scope.shareData.length] = {teamId:data.teamId,serviceId:data.serviceId,serviceName:data.serviceName,cardType:data.cardType,holidaysMark:data.holidaysMark,
								profitType:'5',bpId:data.bpId,bpName:data.bpName,serviceType:data.serviceType,serviceType2:data.serviceType2,shareProfitPercent:'100',rateType:data.rateType};
					}
					if(data.rateType=='6'){
						$scope.rateTypeShow=false;
						if($scope.isCostCapping){
							$scope.isCostCapping=false;
							$scope.shareListColumnDefs.splice($scope.shareListColumnDefs.length-1,0,{
								field: 'costCapping', displayName: '封顶手续费', width: 130,
								cellTemplate:
									'<div ng-show="row.entity.rateType==6" style="width:98%;height:98%;"> '
									+ '<input type="text" style="width:80%;height:98%;" class="ui-widget input" ng-readonly="false" ng-model="row.entity[col.field]"/>'
									+ '元'
									+ '</div>'
							});
						}
					}
				});
				if($scope.rateTypeShow&&!$scope.isCostCapping){
					$scope.isCostCapping=true;
					$scope.shareListColumnDefs.splice($scope.shareListColumnDefs.length-2,1)
				}
				angular.forEach(data.quotas, function(data,index){
					$scope.quotaData[index] = {id:data.id,serviceId:data.serviceId,serviceName:data.serviceName,cardType:data.cardType,holidaysMark:data.holidaysMark,
							profitType:data.profitType,merRate:data.merRate,isGlobal:1,singleMinAmount:data.singleMinAmount,singleCountAmount:data.singleCountAmount,
							singleDayAmount:data.singleDayAmount,singleDaycardAmount:data.singleDaycardAmount,singleDaycardCount:data.singleDaycardCount};
				});
				selectedProduct = angular.copy(products);
				// 对shareData按teamId进行分组
				$scope.shareDataMap = [];
				angular.forEach($scope.shareData, function (data, index) {
					var teamId = data.teamId;
					if($scope.shareDataMap[teamId] === undefined){
						$scope.shareDataMap[teamId] = [];
					}
					$scope.shareDataMap[teamId].push(data);
				});

				$scope.teamIds = [];
				$scope.shareDataList = [];
				angular.forEach($scope.shareDataMap, function (data, index) {
					$scope.teamIds.push(index);
					$scope.shareDataList[index] = {
						data: data,
						useExternalPagination: true,		  //开启拓展名
						columnDefs: $scope.shareListColumnDefs
					}
				});

			}).error(function(data, status, headers, config) {
			});
	}


	//获取欢乐返子类型
	$scope.happyGridApi = [];
	$scope.LastSelectBpId = [];
	$scope.selectFlag = false;
	$scope.getHappyBackType = function(){
		var bpIds = [];
		$scope.addbpData.forEach(function (e) {
			bpIds.push(e.bpId);
		});


		//如果业务产品没有变化，则不需要重新查询
		if($scope.LastSelectBpId.toString() == bpIds.toString() && $scope.selectFlag){
			return;
		}
		$scope.selectFlag = true;
		$scope.LastSelectBpId = bpIds;

		$http.post('agentInfo/selectHappyBackTypeWithTeamId?teamId=' + bpIds,
			{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			.success(function(data) {
				if (data.status) {
					$scope.happyBackData = data.list;
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
							columnDefs: $scope.happyBackColumnDefs,
							onRegisterApi: function(gridApi) {
								//$scope.happyGridApi = gridApi;
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

    $scope.newHappyBackGrid = {
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
            $scope.newHppyBackGridApi = gridApi;
        }
    };

	$scope.superHappyBackGrid = {
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
		}
	};
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
 	}
 	uploaderApp.removeFromQueue = function(value){
 		uploaderAppFlag = true;
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
 	}
 	uploaderWeb.removeFromQueue = function(value){
 		uploaderWebFlag = true;
 		var index = this.getIndexOfItem(value);
         var item = this.queue[index];
         if (item.isUploading) item.cancel();
         this.queue.splice(index, 1);
         item._destroy();
         this.progress = this._getTotalProgress();
 	}

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
 		//3.二维码没有，宣传图片有
		if(uploaderAppFlag && !uploaderWebFlag){
			//有上传宣传图片等待上传
			uploaderWeb.uploadAll();// 上传Web端图片
			uploaderWeb.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
				if (response.url != null) { // 回调参数url
					$scope.agent.managerLogo = response.url;
					$scope.submitData();
				}
	        }
		}
		//4.二维码和宣传图片都有
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
    	var bpIds = [];
    	if($scope.addbpData.length >0 ){
    		for(var i=0; i<$scope.addbpData.length; i++){
    			bpIds[i] = $scope.addbpData[i].bpId;
    		}
    	}
    	//获取选中的欢乐返子类型
    	//var happyBackTypes = $scope.happyGridApi.selection.getSelectedRows();

    	var happyBackTypes = [];
    	if($scope.happyTeams != undefined){
			$scope.happyTeams.forEach(function (e) {
				var temp = $scope.happyGridApi[e].selection.getSelectedRows();
				if(temp.length > 0){
					temp.forEach(function (data) {
						happyBackTypes.push(data);
					});
				}
			});
		}
        var newHappyBackTypes = $scope.newHppyBackGridApi.selection.getSelectedRows();
		var superHappyBackTypes = $scope.superHappyBackGridApi.selection.getSelectedRows();

    	var data = {"agentInfo":$scope.agent,"bpData":bpIds,"shareData":$scope.shareData,
    			"rateData":$scope.rateData,"quotaData":$scope.quotaData,"happyBackTypes":happyBackTypes,
            "newHappyBackTypes":newHappyBackTypes,"superHappyBackTypes":superHappyBackTypes};
    	$http.post('agentInfo/saveAgentInfo',angular.toJson(data)).success(function(msg){
    		$scope.subDisable = false;
    		if(msg.status){
    			$scope.notice(msg.msg);
    			$state.transitionTo('agent.addAgent',null,{reload:true});
    		}else{
    			$scope.noticeHtml(msg.msg);
    		}
    		$scope.subDisable = false;
    	}).error(function(){
    		$scope.subDisable = false;
    	});
    }
});
