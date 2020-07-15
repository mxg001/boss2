/**
 * 机具的新增和修改
 */

angular.module('inspinia').controller('terminalUpdateOrAddCtrl',function($scope,$state,$http,$stateParams){
	
	$scope.info={activityType:"",channel:""};
	$scope.title="新增机具";
	$scope.relationTypeList = [{text:"互斥",value:"0"},{text:"同组",value:"1"}];
    $scope.types = [{text:"欢乐返-循环送",value:"008"},{text:"欢乐返",value:"009"}];
    $scope.subTypeList = [{text:"全部",value:""},{text:"欢乐返",value:"1"},{text:"新欢乐送",value:"2"},{text:"超级返活动",value:"3"}];
    $scope.subType = "";
	//获取硬件产品列表
	$http.get('hardwareProduct/selectAllInfo.do')
    .success(function(largeLoad) {
    	$scope.bpTypes=largeLoad;
    	$scope.info.type=largeLoad[0].hpId;
    });
	//获取机具信息
	if($stateParams.termId>-1){
		$scope.title="修改机具";
		$http.get('terminalInfo/selectObjInfo.do?ids='+$stateParams.termId)
	    .success(function(msg) {
	    	if(msg.status){
	    		$scope.info= msg.terminalInfo;
	    		$scope.info.type=parseInt($scope.info.type);
	    		//活动类型表数据
	    		$scope.activityTypeGrid.data = msg.activityTypeList;
	    		//勾选已有的活动
    	    	if($scope.info.activityType){
    	    		var strArr = $scope.info.activityType.split(',');
                    if((strArr.indexOf("7")>=0||strArr.indexOf("8")>=0)){
                        $scope.isActivityHardware=true;
                        if(strArr.indexOf("7")>=0){
                            $scope.selectHlfActivityHardware($scope.info.type,'009');
                        }else if(strArr.indexOf("8")>=0){
                            $scope.selectHlfActivityHardware($scope.info.type,'008');
                        }
                    }
    				for(var i=0; i<strArr.length; i++){
    					angular.forEach($scope.activityTypeGrid.data,function(item){
    						//比较sys_value相等，就勾上
    						if(item.sys_value == strArr[i]){
    							item.selectStatus = true;
    							if(item.group_no){
    								angular.forEach($scope.activityTypeGrid.data,function(groupItem){
    									if(groupItem.group_no 
    											&& groupItem.group_no == item.group_no
    											&& groupItem.id != item.id){
    										groupItem.disabledStatus = true;
    									}
    								});
    							}
    						}
    					});
    				}
    	    	}
	    	}
	    });
    }
	
	if($stateParams.termId==-1){
		//获取活动类型（包含群组关系）
		$http.post('activityGroup/getAllActivityGroup')
		.success(function(msg){
			if(msg.status){
				$scope.activityTypeGrid.data = msg.activityTypeList;
			} else {
				$scope.notice(msg.msg);
			}
		}).error(function(){
			$scope.notice("服务异常");
		});
	}
	
	$scope.activityTypeGrid = {
        columnDefs: [
            {field: 'selectStatus',displayName: '',width: '30',cellTemplate:
                    '<input type="checkbox" ng-disabled="row.entity.disabledStatus" ng-change="grid.appScope.selectedActivityType(row.entity)" ng-model="row.entity.selectStatus" ng-checked="row.entity.selectStatus"/>'},
            {field: 'sys_name',displayName: '活动类型',width: 180},
            {field: 'group_no',displayName: '群组',width: 180},
            {field: 'relation_type',displayName: '关系',width: 180,cellFilter:"formatDropping:"+angular.toJson($scope.relationTypeList)},
        ]
    };

    $scope.activityHlfGrid = {
        columnDefs: [
            {field: 'activityTypeNo',displayName: '',width: '30',cellTemplate:
                    '<input type="checkbox" ng-disabled="row.entity.disabledStatus" ng-change="grid.appScope.selectedActivityTypeNo(row.entity)" ng-model="row.entity.selectStatus" ng-checked="row.entity.selectStatus" />'},
            {field: 'activityCode',displayName: '欢乐返类型',width: 180,cellFilter:"formatDropping:"+angular.toJson($scope.types)},
            {field: 'activityTypeName',displayName: '欢乐返子类型名称',width: 180},
            {field: 'transAmount',displayName: '交易金额',width: 180},
            {field: 'cashBackAmount',displayName: '返现金额',width: 180},
            {field: 'repeatRegisterAmount',displayName: '重复注册返现金额',width: 180},
        ]
    };

	//机具通道
	$scope.channelList=[{value:"",text:"全部"}];
	$http.post("sysDict/getListByKey.do?sysKey=JJTD")
	.success(function(data){
		//响应成功
	    for(var i=0; i<data.length; i++){
	    	 $scope.channelList.push({value:data[i].sysValue,text:data[i].sysName});
		}
	});

    $scope.isActivityHardware=false;

    $scope.selectHlfActivityHardware=function(hardId,activityCode){
        $scope.queryInfo={hardId:hardId,activityCode:activityCode}
        var queryInfo = {"queryInfo" : $scope.queryInfo};
        $http.post('activity/selectHlfActivityHardwareList',angular.toJson(queryInfo))
            .success(function(data){
                if(data.status){
                    $scope.activityHlfGrid.data = data.page;
                    $scope.hlfTypeAllList = angular.copy(data.page);
                    if($scope.info.activityTypeNo!=null){
                        angular.forEach($scope.activityHlfGrid.data,function(item){
                            //比较相等，就勾上
                            if(item.activityTypeNo == $scope.info.activityTypeNo){
                                item.selectStatus = true;
                            }else if($scope.info.activityTypeNo!=null&&$scope.info.activityTypeNo!=""){
                                item.disabledStatus = true;
							}else{
                                item.disabledStatus = false;
							}
                        });
                    }else{
                        angular.forEach($scope.activityHlfGrid.data,function(item){
                            item.disabledStatus = false;
                            item.selectStatus=0;
                        })
					}
                }else{
                    $scope.notice(data.msg);
                }
            })
            .error(function(){
            });
    }

    $scope.changeSubTypeList = function(){
        var itemHlfGridData = [];
        if($scope.subType) {
            angular.forEach($scope.hlfTypeAllList, function(item) {
                if(item.subType == $scope.subType) {
                    itemHlfGridData[itemHlfGridData.length] = item;
                }
            });
            $scope.activityHlfGrid.data = itemHlfGridData;
        } else {
            $scope.activityHlfGrid.data = $scope.hlfTypeAllList;
        }
        angular.forEach($scope.activityHlfGrid.data,function(item){
            item.disabledStatus = false;
            item.selectStatus=0;
        })
    }

    $scope.changeClear= function(){
        $scope.isActivityHardware=false;
        $scope.activityHlfGrid.data=[];
        $scope.info.activityTypeNo="";
        angular.forEach($scope.activityTypeGrid.data,function(item){
            item.disabledStatus = false;
            item.selectStatus=0;
        })
        angular.forEach($scope.activityHlfGrid.data,function(item){
            item.disabledStatus = false;
            item.selectStatus=0;
        })
    }
	
	//勾选活动类型时
	$scope.selectedActivityType = function(entity){
        if((entity.sys_value==7||entity.sys_value==8)&&entity.selectStatus){
            $scope.isActivityHardware=true;
            if(entity.sys_value==7){
                $scope.info.activityTypeNo="";
                $scope.selectHlfActivityHardware($scope.info.type,'009');
            }else{
                $scope.info.activityTypeNo="";
                $scope.selectHlfActivityHardware($scope.info.type,'008')
            }
        }else if((entity.sys_value==7||entity.sys_value==8)&&!entity.selectStatus){
            $scope.isActivityHardware=false;
            $scope.activityHlfGrid.data=[];
        }
		angular.forEach($scope.activityTypeGrid.data,function(item){
			if(entity.group_no!=null && item.group_no!=null 
					&& entity.relation_type!=null  && item.relation_type!=null 
					&& item.group_no==entity.group_no
					&& item.group_no==entity.group_no
					&& entity.relation_type=='0'
					&& item.id!= entity.id){
				if(entity.selectStatus){
					item.disabledStatus = true;
				} else {
					item.disabledStatus = false;
				}
			}
			if(entity.group_no!=null  && item.group_no!=null 
					&& entity.relation_type!=null  && item.relation_type!=null 
					&& item.group_no==entity.group_no
					&& item.group_no==entity.group_no
					&& entity.relation_type=='1'
					&& item.id!= entity.id){
				if(entity.selectStatus){
					item.selectStatus = true;
				} else {
					item.selectStatus = false;
				}
			}
		});
	}

    //勾选活动类型时
    $scope.selectedActivityTypeNo = function(entity){
        angular.forEach($scope.activityHlfGrid.data,function(item){
            if(entity.selectStatus&&item.activityTypeNo != entity.activityTypeNo){
                item.disabledStatus = true;
            } else {
                item.disabledStatus = false;
            }
        });
    }
	
	$scope.commitDate=function(){
        var activityTypeNo="";
        angular.forEach($scope.activityHlfGrid.data,function(item){
            if(item.selectStatus){
                activityTypeNo=item.activityTypeNo;
            }
        });
        if($scope.isActivityHardware){
            if($scope.activityHlfGrid.data.length==0){
                $scope.notice("该硬件产品还没有建欢乐返子类型，请先建后重试");
                return;
            }
            if(activityTypeNo == undefined || activityTypeNo == null || activityTypeNo==""){
            	$scope.notice("欢乐返子类型不能为空");
                return;
            }
		}
        $scope.info.activityTypeNo=activityTypeNo;
		$scope.submitting = true;
		var activityTypeStr = "";
		var needAdd = true;
		angular.forEach($scope.activityTypeGrid.data,function(item){
			if(item.selectStatus){
				//去重
				var strArr = activityTypeStr.split(',');
				for(var i=0; i<strArr.length; i++){
					if(strArr[i]==item.sys_value){
						needAdd = false;
						break;
					}
				}
				if(needAdd){
					activityTypeStr = activityTypeStr + item.sys_value + ",";
				}
			}
		});
		//去掉最后一个逗号
		if(activityTypeStr.length>0){
			activityTypeStr=activityTypeStr.substring(0,activityTypeStr.length-1);
			$scope.info.activityType = activityTypeStr;
		} else {
			$scope.info.activityType = null;
		}
		//修改
		if($stateParams.termId>-1){
			$http.post("terminalInfo/updateTerminalInfo.do",angular.toJson($scope.info))
			.success(function(data){
				if(data.result){
					$scope.notice("操作成功");
					$state.go('terminal.terminalQuery');
					$scope.submitting = false;
				}else{
					$scope.notice("操作失败");
					$scope.submitting = false;
				}
			});
		}else{
			//新增
			$http.post("terminalInfo/addTerminalInfo.do",angular.toJson($scope.info))
			.success(function(data){
				if(data.result){
					$scope.notice("操作成功");
					$state.go('terminal.terminalQuery');
					$scope.submitting = false;
				}else{
					$scope.notice(data.message);
					$scope.submitting = false;
				}
			});
		}
	}
	
	$scope.creatActiveCode = function(){
		if(!($scope.info.type==13 || $scope.info.type==19 || $scope.info.type==123)){
			$scope.notice('请选择硬件产品为“扫码的硬件类型”');
		}else {
			$scope.codeNumber = 0;
			$("#activeCode").modal("show");
		}
	}
	$scope.cancel = function(){
		$("#activeCode").modal("hide");
	}
	
	//生成激活码
	$scope.submitActiveCode = function(){
		if($scope.codeNumber<=0){
			$scope.notice("数量不能小于0");
			return false;
		}
		if($scope.codeNumber>10000000){
			$scope.notice("数量不能大于一千万");
			return false;
		}
		$scope.submitting = true;
		$http.get("terminalInfo/createActiveCode.do?codeNumber=" + $scope.codeNumber
				+"&hpId="+ $scope.info.type)
		.success(function(msg){
			if(msg.status){
				$scope.notice(msg.msg);
				$("#activeCode").modal("hide");
			} else {
				$scope.notice(msg.msg);
			}
			$scope.submitting = false;
		}).error(function(){
			$scope.notice("服务异常");
			$scope.submitting = false;
		})
	}
	
    
});