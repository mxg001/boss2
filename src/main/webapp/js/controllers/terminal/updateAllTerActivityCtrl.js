/**
 * 修改硬件种类及活动
 */

angular.module('inspinia').controller('updateAllTerActivityCtrl',function($scope,$state,$http,$stateParams){
	$scope.title='机具信息';
	if($stateParams.specialStatus==1){
		$scope.title='机具信息(特殊)';
	}
	$scope.info={};
	$scope.relationTypeList = [{text:"互斥",value:"0"},{text:"同组",value:"1"}];
    $scope.types = [{text:"欢乐返-循环送",value:"008"},{text:"欢乐返",value:"009"}];
    $scope.subTypeList = [{text:"全部",value:""},{text:"欢乐返",value:"1"},{text:"新欢乐送",value:"2"},{text:"超级返活动",value:"3"}];
    $scope.subType = "";
	//获取硬件产品列表
	$http.get('hardwareProduct/selectAllInfo.do')
    .success(function(msg) {
    	$scope.bpTypes=msg;
    	$scope.type=msg[0].hpId;
    });
	
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
                $scope.selectHlfActivityHardware($scope.type,'009');
            }else{
                $scope.selectHlfActivityHardware($scope.type,'008')
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
	
	//如果勾选的机具有已使用的“提示不能修改”
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
			activityTypeStr = activityTypeStr.substring(0,activityTypeStr.length-1);
			$scope.activityType = activityTypeStr;
		} else {
			$scope.activityType = null;
		}
		 if($scope.snStart > $scope.snEnd){  
		     $scope.notice("SN起始值不能大于结束值");
		     $scope.submitting = false;
		     return;
		}
		 if($scope.type==-1){
			 $scope.notice("请选择硬件产品种类");
			 $scope.submitting = false;
		     return;
		 }
		var data = {	
					"snStart":$scope.snStart,
					"snEnd":$scope.snEnd,
					"type":$scope.type,
					"activityType":$scope.activityType,
					"activityTypeNo":activityTypeNo
				};
		
		var url = "terminalInfo/updateAllTerActivity";
		//特殊处理
		if($stateParams.specialStatus==1){
			url = "terminalInfo/updateTerminalSpecial";
		}
		 $http.post(url,angular.toJson(data))
			.success(function(data){
				$scope.notice(data.msg);
				if(data.status){
					//刷新当前页面
					$state.transitionTo('updateAllTerActivity',{specialStatus:$stateParams.specialStatus},{reload:true});
				}
				$scope.submitting = false;
			}).error(function(){
				$scope.notice("系统异常");
				$scope.submitting = false;
			});
	}
    
});