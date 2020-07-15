angular.module('inspinia',['uiSwitch']).controller("queryAgentCtrl", function($scope, $http, $state, $stateParams,i18nService,SweetAlert,$document) {
//	$scope.level = [{text:'全部',value:""},{text:'1',value:1},{text:'2',value:2},{text:'3',value:3},{text:'4',value:4},
//	                {text:'5',value:5},{text:'6',value:6},{text:'7',value:7},{text:'8',value:8}];
	$scope.checkStatus=[{text:"全部",value:""},{text:"待审核",value:0},{text:"审核通过",value:1},{text:'审核不通过',value:2}];
	$scope.lockStatus=[{text:"全部",value:""},{text:"锁定",value:1},{text:"未锁定",value:0}];
	$scope.baseInfo = {accountName:"",accountNo:"",idCardNo:"",shareCheck:"",shareLock:"",rateCheck:"",rateLock:"",hasAccount:"",quotaCheck:"",quotaLock:"",agentLevel:-1,teamId:"",hasSub:0,profitSwitch:2,promotionSwitch:2,cashBackSwitch:2};
	$scope.statusStr = '[{text:"关闭进件",value:"0"},{text:"正常",value:"1"},{text:"冻结",value:"2"}]';
	$scope.agentData = [];
	$scope.teamTypeStr=null;
	$scope.sysDictShare = {sysKey:"AGENT_WEB_SHARE_SWITCH",sysValue:""};
	$scope.sysDictSuperPush = {sysKey:"SUPER_PUSH_AGENT_SWITCH",sysValue:""};
	
	$http.get('teamInfo/queryTeamName.do').success(function(msg){
		$scope.teamType=[];
		$scope.teamType.push({text:'全部',value:""});
		for(var i=0;i<msg.teamInfo.length;i++){
			$scope.teamType.push({text:msg.teamInfo[i].teamName,value:msg.teamInfo[i].teamId});
		}
		$scope.teamTypeStr=angular.toJson($scope.teamType);
        $scope.agentGrid.columnDefs.splice($scope.agentGrid.columnDefs.length-4,0,{field: 'teamId',displayName: '所属组织',width:150,cellFilter:"formatDropping:" + $scope.teamTypeStr});
	});
	//查询代理商WEB分润显示开关
	$http.post('sysDict/getByKey.do',"sysKey=AGENT_WEB_SHARE_SWITCH", {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
	.success(function(msg){
		if(msg.status){
			$scope.sysDictShare = msg.sysDict;
			if($scope.sysDictShare.sysValue=='1'){
				$scope.sysDictShare.sysValue = 1;
			} else if($scope.sysDictShare.sysValue=='0'){
				$scope.sysDictShare.sysValue = 0;
			}
		} else {
			$scope.notice('代理商WEB分润显示开关字典未配置');
		}
	});
	//查询代理商推广开关总控制状态
	$http.post('sysDict/getByKey.do',"sysKey=SUPER_PUSH_AGENT_SWITCH", {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
	.success(function(msg){
		if(msg.status){
			$scope.sysDictSuperPush = msg.sysDict;
			if($scope.sysDictSuperPush.sysValue=='1'){
				$scope.sysDictSuperPush.sysValue = 1;
			} else if($scope.sysDictSuperPush.sysValue=='0'){
				$scope.sysDictSuperPush.sysValue = 0;
			}
		} else {
			$scope.notice('代理商推广开关总控制字典未配置');
		}
	});
	
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	$scope.agentGrid = {
		data : "agentData",
		paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],	//切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs: [
	         {field: 'agentNo',displayName: '代理商编号',width: 120},
	         {field: 'agentLevel',displayName: '代理商级别',width: 120},
	         {field: 'agentName',displayName: '代理商名称',width: 120},
	         {field: 'profitSwitch',displayName: '分润日结功能',width:150,cellTemplate:
	        	 '<span ng-show="grid.appScope.hasPermit(\'agent.switchProfitStatus\')"><switch class="switch switch-s" ng-model="row.entity.profitSwitch" ng-change="grid.appScope.switchProfitStatus(row)" /></span>'
	        	 +'<span class="lh30" ng-show="!grid.appScope.hasPermit(\'agent.switchProfitStatus\')"> <span ng-show="row.entity.profitSwitch==1">开启</span><span ng-show="row.entity.profitSwitch==0">关闭</span></span>'
	         },
	         {field: 'promotionSwitch',displayName: '代理商推广功能',width:150,cellTemplate:
	        	 '<span ng-show="grid.appScope.hasPermit(\'agent.agentPromotion\')"><switch class="switch switch-s" ng-model="row.entity.promotionSwitch" ng-change="grid.appScope.switchPromotionStatus(row)" /></span>'
	        	 +'<span class="lh30" ng-show="!grid.appScope.hasPermit(\'agent.agentPromotion\')"> <span ng-show="row.entity.promotionSwitch==1">开启</span><span ng-show="row.entity.promotionSwitch==0">关闭</span></span>'
	         },
	         {field: 'cashBackSwitch',displayName: '欢乐返返现开关',width:150,cellTemplate:
                 '<span ng-show="grid.appScope.hasPermit(\'agent.switchCashBackStatus\')"><switch class="switch switch-s" ng-model="row.entity.cashBackSwitch" ng-change="grid.appScope.switchCashBackStatus(row)" /></span>'
                 +'<span class="lh30" ng-show="!grid.appScope.hasPermit(\'agent.switchCashBackStatus\')"> <span ng-show="row.entity.cashBackSwitch==1">开启</span><span ng-show="row.entity.cashBackSwitch==0">关闭</span></span>'
             },
	         {field: 'parentId',displayName: '上级代理商编号',width: 150},
	         {field: 'parentAgentName',displayName: '上级代理商名称',width: 150},
	         {field: 'oneLevelId',displayName: '一级代理商编号',width:150},
	         {field: 'status',displayName: '状态',width:150,cellFilter:"formatDropping:" + $scope.statusStr},
	         {field: 'createDate',displayName: '创建时间',width:150,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
	         {field: 'hasAccount',displayName: '是否开户',width:150,cellFilter:"formatDropping:" + angular.toJson($scope.bool)},
	         {field: 'accountName',displayName: '开户名',width:150},
	         {field: 'accountNo',displayName: '开户账号',width:180},
	         {field: 'idCardNo',displayName: '身份证号',width:150},
	         {field: 'action',displayName: '操作',width: 180,pinnedRight:true,editable:true,cellTemplate:
	         	'<div class="lh30"><a target="_blank" ng-show="grid.appScope.hasPermit(\'agent.detail\')" ui-sref="agent.agentDetail({id:row.entity.agentNo,teamId:row.entity.teamId})">详情</a>'
	        	+'<span ng-show="grid.appScope.hasPermit(\'agent.edit\')&&row.entity.agentLevel==1"> | <a ui-sref="agent.editAgent({agentNo:row.entity.agentNo,teamId:row.entity.teamId})" >修改</a></span>'
	        	+'<span ng-show="grid.appScope.hasPermit(\'agent.edit\')&&row.entity.agentLevel!=1" style="color:gray;"> | 修改</span>'
	        	+'<span ng-show="grid.appScope.hasPermit(\'agent.openAccount\')&&row.entity.agentLevel==1&&row.entity.hasAccount==0"> | <a ng-click="grid.appScope.openAccount(row.entity.agentNo)" >开户</a></span></div>'}
	     ],
	     onRegisterApi: function(gridApi) {              
	         $scope.gridApi = gridApi;
	         $scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
	          	$scope.paginationOptions.pageNo = newPage;
	          	$scope.paginationOptions.pageSize = pageSize;
	            $scope.query();
	         });
	     }
	};

    //修改代理商推广功能开关
	$scope.switchProfitStatus=function(row){
		if(row.entity.profitSwitch){
			$scope.serviceTitle = "确定开启？";
			$scope.serviceText = "";
		} else {
			$scope.serviceTitle = "确定关闭？";
			$scope.serviceText = "该代理商及所有下级代理商欢乐返返现功能都会一起关闭！";
		}
        SweetAlert.swal({
            title: $scope.serviceTitle,
            text: $scope.serviceText,
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            closeOnConfirm: true,
            closeOnCancel: true },
	        function (isConfirm) {
	            if (isConfirm) {
	            	if(row.entity.profitSwitch==true){
	            		row.entity.profitSwitch=1;
	            	} else if(row.entity.profitSwitch==false){
	            		row.entity.profitSwitch=0;
	            	}
	            	var data={"profitSwitch":row.entity.profitSwitch,"agentNode":row.entity.agentNode};
	            	$http.post("agentInfo/switchProfitStatus.do",angular.toJson(data))
	            		.success(function(data){
	            			$scope.notice(data.msg);
	            			if(data.status){
	            				$scope.query();
	            			}else{
	            				row.entity.profitSwitch = !row.entity.profitSwitch;
	            			}
	            		})
	            		.error(function(data){
	            			row.entity.profitSwitch = !row.entity.profitSwitch;
	            			$scope.notice("服务器异常");
	            		});
	            } else {
	            	row.entity.profitSwitch = !row.entity.profitSwitch;
	            }
        });
    };


    //修改代理商欢乐返返现功能开关
	$scope.switchCashBackStatus=function(row){
		if (row.entity.agentLevel != 1) {	//boss系统只可以控制一级代理商,这里做判断
			$scope.notice("只能控制一级代理商,该代理商不是一级代理商");
			row.entity.cashBackSwitch = !row.entity.cashBackSwitch;
			return;
		}
    	if(row.entity.cashBackSwitch){
    		$scope.serviceTitle = "";
    		$scope.serviceText = "确定开启代理商(" + row.entity.agentNo + ")欢乐返返现功能？";
    	} else {
    		$scope.serviceTitle = "";
    		$scope.serviceText = "确定关闭代理商(" + row.entity.agentNo + ")欢乐返返现功能？关闭后，该代理商及所有下级代理商欢乐返返现功能都会一起关闭！";
    	}
    	SweetAlert.swal({
    		title: $scope.serviceTitle,
    		text: $scope.serviceText,
    		type: "warning",
    		showCancelButton: true,
    		confirmButtonColor: "#DD6B55",
    		confirmButtonText: "确定",
    		cancelButtonText: "取消",
    		closeOnConfirm: true,
    		closeOnCancel: true },
    		function (isConfirm) {
    			if (isConfirm) {
    				if(row.entity.cashBackSwitch==true){
    					row.entity.cashBackSwitch=1;
    				} else if(row.entity.cashBackSwitch==false){
    					row.entity.cashBackSwitch=0;
    				}
    				var data={"cashBackSwitch":row.entity.cashBackSwitch,"agentNode":row.entity.agentNode,"agentLevel":row.entity.agentLevel};
    				$http.post("agentInfo/switchCashBackStatus.do",angular.toJson(data))
    				.success(function(data){
    					$scope.notice(data.msg);
    					if(data.status){
    						$scope.query();
    					}else{
    						row.entity.cashBackSwitch = !row.entity.cashBackSwitch;
    					}
    				})
    				.error(function(data){
    					row.entity.cashBackSwitch = !row.entity.cashBackSwitch;
    					$scope.notice("服务器异常");
    				});
    			} else {
    				row.entity.cashBackSwitch = !row.entity.cashBackSwitch;
    			}
    		});
    };

    //修改代理商推广功能开关
    $scope.switchPromotionStatus=function(row){
        if (row.entity.agentLevel != 1) {	//boss系统只可以控制一级代理商,这里做判断
            $scope.notice("只能控制一级代理商,该代理商不是一级代理商");
            row.entity.promotionSwitch = !row.entity.promotionSwitch;
            return;
        }
        if(row.entity.promotionSwitch){
            $scope.serviceTitle = "";
            $scope.serviceText = "确定开启代理商(" + row.entity.agentNo + ")推广功能？";
        } else {
            $scope.serviceTitle = "";
            $scope.serviceText = "确定关闭代理商(" + row.entity.agentNo + ")推广功能？关闭后，该代理商及所有下级代理商推广功能都会一起关闭！";
        }
        SweetAlert.swal({
                title: $scope.serviceTitle,
                text: $scope.serviceText,
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    if(row.entity.promotionSwitch==true){
                        row.entity.promotionSwitch=1;
                    } else if(row.entity.promotionSwitch==false){
                        row.entity.promotionSwitch=0;
                    }
                    var data={"promotionSwitch":row.entity.promotionSwitch,"agentNode":row.entity.agentNode,"agentLevel":row.entity.agentLevel};
                    $http.post("agentInfo/switchPromotionStatus.do",angular.toJson(data))
                        .success(function(data){
                            $scope.notice(data.msg);
                            if(data.status){
                                $scope.query();
                            }else{
                                row.entity.promotionSwitch = !row.entity.promotionSwitch;
                            }
                        })
                        .error(function(data){
                            row.entity.promotionSwitch = !row.entity.promotionSwitch;
                            $scope.notice("服务器异常");
                        });
                } else {
                    row.entity.promotionSwitch = !row.entity.promotionSwitch;
                }
            });
    };



	//修改代理商WEB分润显示开关 和 代理商推广开关总控制
	$scope.updateSwitch = function(sysDict){
		SweetAlert.swal({
			title: sysDict.sysValue? "确定开启？" : "确定关闭？",
			type: "warning",
			showCancelButton: true,
			confirmButtonColor: "#DD6B55",
			confirmButtonText: "提交",
			cancelButtonText: "取消",
			closeOnConfirm: true,
			closeOnCancel: true },
			function (isConfirm) {
				if (isConfirm) {
					if(sysDict.sysValue==true){
						sysDict.sysValue=1;
					} else if(sysDict.sysValue==false){
						sysDict.sysValue=0;
					}
					$http.post("sysDict/updateSysValue.do",angular.toJson(sysDict))
				    	.success(function(data){
				    		$scope.notice(data.msg);
				    		if(!data.status){
				    			sysDict.sysValue = !sysDict.sysValue;
				    		}
				    	})
				    	.error(function(data){
				    		sysDict.sysValue = !sysDict.sysValue;
				    		$scope.notice("服务器异常")
				    	});
				}
				else {
					sysDict.sysValue = !sysDict.sysValue;
				}
			});
	}

	//删除代理商
	$scope.deleteAgent=function(agentNo){
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
	            	$http.post('agentInfo/delAgent?agentNo='+agentNo).success(function(msg){
	    				if(msg.status){
	    					$scope.notice(msg.msg);
	    					$scope.query();
	    				} else {
	    					$scope.notice(msg.msg);
	    				}
	    			}).error(function(){
	    			}); 
	            }
        });
    };
    
    //代理商开户
	$scope.openAccount=function(agentNo){
        SweetAlert.swal({
            title: "确认开户？",
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
	            	$http.get('agentInfo/openAccount?agentNo=' + agentNo).success(function(msg){
	    				if(msg.status){
	    					$scope.notice(msg.msg);
	    					$scope.query();
	    				} else {
	    					$scope.notice(msg.msg);
	    				}
	    			}).error(function(){
	    			}); 
	            }
        });
    };
    //批量修改代理商分润日结功能显示modal
    $scope.profitSwitchModal = function(){
    	$scope.switchStatus = null;
    	$scope.selectedAgent = $scope.gridApi.selection.getSelectedRows();
    	if($scope.selectedAgent!=null&&$scope.selectedAgent.length>0){
    		$('#profitSwitchModal').modal('show');
    	} else {
    		$scope.notice('请选择需要修改的代理商');
    	}
    }
    //隐藏modal
    $scope.cancel = function(){
    	$('#profitSwitchModal').modal('hide');
    	$('#accountModal').modal('hide');
    	$('#promotionSwitchModal').modal('hide');
    }

    //批量修改代理商推广功能显示modal
    $scope.promotionSwitchModal = function(){
    	$scope.switchStatus = null;
    	$scope.selectedAgent = $scope.gridApi.selection.getSelectedRows();
    	if($scope.selectedAgent!=null && $scope.selectedAgent.length>0){
    		for (var i = 0; i < $scope.selectedAgent.length; i++) {
    			if ($scope.selectedAgent[i].agentLevel != 1) {
    				$scope.notice("只能对一级代理商进行操作!");
    				return;
    			}
    		}
    		$('#promotionSwitchModal').modal('show');
    	} else {
    		$scope.notice('请选择需要修改的代理商');
    	}
    }

    //提交批量修改分润
    $scope.profitSwitchSubmit = function(){
    	var agentNodeList = [];
    	angular.forEach($scope.selectedAgent,function(data){
    		agentNodeList[agentNodeList.length] = data.agentNode;
    	});
    	var data = {agentNodeList:agentNodeList,switchStatus:$scope.switchStatus};
    	$http.post('agentInfo/updateProgitSwitchBatch',angular.toJson(data))
    	.success(function(msg){
    		if(msg.status){
    			$scope.notice(msg.msg);
    			$scope.cancel();
    	    	$scope.query();
    		} else {
    			$scope.notice(msg.msg);
    		}
    	})
    }
    //提交批量修改推广功能
    $scope.promotionSwitchSubmit = function(){
    	var agentNodeList = [];
    	angular.forEach($scope.selectedAgent,function(data){
    		agentNodeList[agentNodeList.length] = data.agentNode;
    	});
    	var data = {agentNodeList:agentNodeList,switchStatus:$scope.switchStatus};
    	$http.post('agentInfo/updatePromotionSwitchBatch',angular.toJson(data))
    	.success(function(msg){
    		if(msg.status){
    			$scope.notice(msg.msg);
    			$scope.cancel();
    			$scope.query();
    		} else {
    			$scope.notice(msg.msg);
    		}
    	})
    }

    //批量代理商开户modal
    $scope.accountModal = function(){
    	$scope.subjectNo = "";
    	$scope.selectedAgent = $scope.gridApi.selection.getSelectedRows();
    	if($scope.selectedAgent!=null&&$scope.selectedAgent.length>0){
    		$('#accountModal').modal('show');
    	} else {
    		$scope.notice('请选择需要开户的代理商');
    	}
    }
    //批量代理商开户提交
    $scope.accountSubmit = function(){
    	var agentNoList = [];
    	if($scope.selectedAgent.length>=5000){
    		$scope.notice('最多不能超过5000条');
    		return;
    	}
    	angular.forEach($scope.selectedAgent,function(data){
    		agentNoList[agentNoList.length] = data.agentNo;
    	});
    	var data = {agentNoList:agentNoList,subjectNo:$scope.subjectNo};
    	$http.post('agentInfo/openAccountBatch',angular.toJson(data))
    	.success(function(msg){
    		if(msg.status){
    			$scope.notice(msg.msg);
    			$scope.cancel();
    	    	$scope.query();
    		} else {
    			$scope.notice(msg.msg);
    		}
    	})
    }
	
	$scope.query = function(){
		$scope.loadImg = true;
		$http.post('agentInfo/queryAgentInfoList',"baseInfo=" + angular.toJson($scope.baseInfo) + "&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+
				$scope.paginationOptions.pageSize, {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
		).success(function(msg){
			$scope.loadImg = false;
			$scope.agentData = msg.result;
			$scope.agentGrid.totalItems = msg.totalCount;
		}).error(function(){
			$scope.loadImg = false;
		});
	}
//	$scope.query();//默认初始化加载
	//reset
	$scope.resetForm=function(){
		$scope.baseInfo = {shareCheck:"",shareLock:"",rateCheck:"",rateLock:"",hasAccount:"",quotaCheck:"",quotaLock:"",agentLevel:-1,teamId:"",hasSub:0,profitSwitch:2,promotionSwitch:2};
	}

	//页面绑定回车事件
	$document.bind("keypress", function(event) {
		$scope.$apply(function (){
			if(event.keyCode == 13){
				$scope.query();
			}
		})
	});

});