/**
 * 修改交易跳转路由集群配置
 */
angular.module('inspinia').controller('jumpRouteUpdateCtrl',function($scope, $http, $stateParams, $state){
	
	$scope.groupList = [];//集群集合
	$scope.acqList = [];//收单机构集合
	$scope.acqDetailList = [];//详情页-收单机构集合
	$scope.groupDetailList = [];//详情页-路由集群集合
	// $scope.bpTypeList = [{text:'不限',value:0},{text:'指定业务产品',value:1}];
	$scope.bpTypeList = [{text:'指定业务产品',value:1}];
	$scope.provinceTypeList = [{text:'不限',value:0},{text:'指定省份',value:1}];
	$scope.cardBinList = [{text:'不限',value:0},{text:'指定发卡行',value:1}];
	//$scope.baseInfo = {cardType:1,bpType:1,provinceType:1,cardBinType:1,acqMerchantState:1,acqMerchantShowState:1};

	$scope.acqMerchantList = [{text:'不限',value:0},{text:'指定行业',value:1}];
	$scope.serviceTypeSelectList = [{text:'不限',value:0},{text:'指定服务类型',value:1}];
    $scope.effectiveDateTypeList = [{text:'每天',value:1},{text:'周一至周五',value:2},{text:'法定工作日',value:3},{text:'自定义',value:4}];
    $scope.statusList = [{text:"生效",value:1},{text:"失效",value:0}];

	//是否关联活动
	$scope.relationActivitySelect = [{text:"否",value:0},{text:"是",value:1}];
	$scope.relationActivityStr=angular.toJson($scope.relationActivitySelect);

    //获取所有的组织
    $http.get('teamInfo/queryTeamName.do').success(function(msg){
        if(msg.status) {
            $scope.teamGrid.data = msg.teamInfo;
        }
    });

    //当前配置
	$http.get("jumpRoute/update/" + $stateParams.id).success(function(data){
		if(data.status){
			$scope.baseInfo = data.baseInfo;
			if($scope.baseInfo!=null&&$scope.baseInfo.weekDays!=null
				&&$scope.baseInfo.weekDays!=""){
				$scope.baseInfo.monday = -1;
				$scope.baseInfo.tuesday = -1;
				$scope.baseInfo.wednesday = -1;
				$scope.baseInfo.thursday = -1;
				$scope.baseInfo.friday = -1;
				$scope.baseInfo.saturday = -1;
				$scope.baseInfo.sunday = -1;
				if($scope.baseInfo.weekDays.indexOf("0")>-1){
					$scope.baseInfo.monday = 0;
				}
				if($scope.baseInfo.weekDays.indexOf("1")>-1){
					$scope.baseInfo.tuesday = 1;
				}
				if($scope.baseInfo.weekDays.indexOf("2")>-1){
					$scope.baseInfo.wednesday = 2;
				}
				if($scope.baseInfo.weekDays.indexOf("3")>-1){
					$scope.baseInfo.thursday = 3
				}
				if($scope.baseInfo.weekDays.indexOf("4")>-1){
					$scope.baseInfo.friday = 4;
				}
				if($scope.baseInfo.weekDays.indexOf("5")>-1){
					$scope.baseInfo.saturday = 5;
				}
				if($scope.baseInfo.weekDays.indexOf("6")>-1){
					$scope.baseInfo.sunday = 6;
				}
			}
			// $scope.changeBp();
			$scope.changeCardType();
			$scope.getAcqMerchantAllList();
			//$scope.baseInfo.serviceTypeSelect=0;

			$http.post('jumpRoute/getCarBinList','cardType='+$scope.baseInfo.cardType,
				{headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
			).success(function(data){
				$scope.cardBinGrid.data = data;
			}).error(function(){
			});

			//获取所有包含POS刷卡服务的业务产品
			$http.get("businessProductDefine/getProduct").success(function(data) {
				if (data.status) {
					// 将data.bpList里面，$scope.baseInfo.bpIds放在前面
					if($scope.baseInfo.bpIds && data.bpList && data.bpList.length > 0) {
						angular.forEach(data.bpList, function(item){
							if($scope.baseInfo.bpIds.indexOf(item.bpId) > -1) {
                                $scope.bpGrid.data.push(item);
							}
						});
					}
					//修改页面需要展示所有的业务产品，详情页只需要展示选中的
					if($stateParams.type == "update") {
                        if(data.bpList && data.bpList.length > 0) {
                            angular.forEach(data.bpList, function (item) {
                                if(!existsBp(item.bpId, $scope.bpGrid.data)) {
                                    $scope.bpGrid.data.push(item);
                                }
                            })
                        }
                    }

					if($scope.baseInfo.bpType==0){
						$scope.baseInfo.serviceTypeSelect=0;
					}else if($scope.baseInfo.serviceTypes == null || $scope.baseInfo.serviceTypes == ''){
							$scope.baseInfo.serviceTypeSelect=0;
						}else{
							$scope.baseInfo.serviceTypeSelect=1;
						}
					//$scope.changeServiceTypeSelect();
					angular.forEach($scope.baseInfo.bpList,function(item,index){
						$scope.bpIds[index] = item;
					})
					$http.post('jumpRoute/getServiceTypeSelect','bqIds='+angular.toJson($scope.bpIds),
							{headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
						).success(function(data){
							$scope.serviceTypeGrid.data=data;
						});
				}
			});
			//获取省份数据
			$scope.getAreaList(0,"p",function(data){
				$scope.provinceGrid.data = data;
			});
			//获取收单机构
			$http.post('jumpRoute/getSysDictList',
				{headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
			).success(function(data){
				$scope.acqMerchantGrid.data = data;
			});

		}
	});
	
	//获取收单机构集合
	$scope.acqListStr = [];
	$http.post('routeGroup/acqOrgSelectBox.do'
	).success(function(data){
		$scope.acqList = data;
		angular.forEach(data,function(item){
			$scope.acqDetailList.push({"text":item.acqName,"value":item.id});
		})
	}); 
	


	//改变业务产品时，需要切换省份表格的class，保证在切换业务产品的时候省份表格不挪位
	// $scope.changeBp = function(){
	// 	if($scope.baseInfo.bpType==0){
	// 		$scope.provinceClass = "col-sm-4";
	// 		angular.forEach($scope.bpGridApi.selection.getSelectedRows(),function(item,index){
	// 			$scope.bpGridApi.selection.unSelectRow(item);
	// 		})
	// 		$scope.baseInfo.serviceTypeSelect=0;
	// 		$scope.changeServiceTypeSelect();
	// 	} else {
	// 		$scope.provinceClass = "col-sm-1";
	// 	}
	// }

	//切换指定行业样式
	$scope.changeCardType = function(){
		if($scope.baseInfo.cardBinType=='0'){
			$scope.acqMerchantClass = "col-sm-4";
		}else{
			$scope.acqMerchantClass = "col-sm-1";
		}
	}
	
	$scope.getAcqMerchantAllList=function () {
		$http.post('routeGroup/acqOrgSelectBox.do'
		).success(function(data){
			$scope.acqList = data;
			for(var i=0;i<data.length;i++){
				if(data[i].acqName=="YS_ZQ"){
					$scope.acqMerchantShowStateId=data[i].id;
					break;
				}
			}
			angular.forEach(data,function(item){
				$scope.acqDetailList.push({"text":item.acqName,"value":item.id});
			})
		});
	}


	
	//获取所有省份
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

	$scope.setAcqMerchantShowState=function(){
		if($scope.baseInfo.acqId==$scope.acqMerchantShowStateId){
			$scope.baseInfo.acqMerchantShowState=1;
			$scope.baseInfo.acqMerchantState=0;
		}else{
			$scope.baseInfo.acqMerchantShowState=0;
			$scope.baseInfo.acqMerchantState=0;
		}
	}

	//获取商户服务类型为POS刷卡类的路由集群
	$http.get("routeGroup/getGroupByServiceType").success(function(data){
		if(data.status){
			angular.forEach(data.groupList,function(item){
				item.groupName = item.groupCode + " " + item.groupName;
				$scope.groupDetailList.push({"text":item.groupName,"value":item.groupCode});
			})
			$scope.groupList = data.groupList;
		}
	})

	$scope.findCity=function(){
		$http.post("areaInfo/getAreaCityByParentId",angular.toJson({provinceList:$scope.provinceGridApi.selection.getSelectedRows()})).success(function(data){
			$scope.cityGrid.data = data;
		});
	}

    $scope.getBpList = function() {
        var teamId = "";
        if($scope.teamGridApi) {
            var selectTeamList = $scope.teamGridApi.selection.getSelectedRows();
            if(selectTeamList != null && selectTeamList.length > 0) {
                angular.forEach(selectTeamList, function(item) {
                    teamId = teamId + item.teamId + ",";
                });
            }
        }
        $http.get("businessProductDefine/getProductByTeam?teamId=" + teamId).success(function(data){
            if(data.status){
                $scope.bpGrid.data = [];
                $scope.bpGrid.data = $scope.bpGridApi.selection.getSelectedRows();
                angular.forEach(data.bpList, function(item) {
                    if(!existsBp(item.bpId, $scope.bpGrid.data)) {
                        $scope.bpGrid.data.push(item);
                    }
                });
            }
        })
    };
    // $scope.getBpList();

    /**
     * 如果bpList包含bpId，返回true
     * @param bpId
     * @param bpList
     * @returns {boolean}
     */
    function existsBp(bpId, bpList){
        var existsStatus = false;
        if(bpList == null || bpList.length < 1) {
            return existsStatus;
        }
        angular.forEach(bpList, function(item) {
            if(item.bpId == bpId) {
                existsStatus = true;
                return;
            }
        });
        return existsStatus;
    }

    //组织
    $scope.teamGrid = {
        columnDefs: [
            {field: 'teamName',displayName: '所属组织'},
        ],
        onRegisterApi : function(gridApi) {
            $scope.teamGridApi = gridApi;
            //行选中事件
            $scope.teamGridApi.selection.on.rowSelectionChanged($scope,function(row,event){
                $scope.getBpList();
            });
            //全选事件
            $scope.teamGridApi.selection.on.rowSelectionChangedBatch($scope,function(row,event){
                $scope.getBpList();
            });
        }
    };

	//业务产品（包含POS刷卡服务）表格
	$scope.bpGrid = {
	        columnDefs: [
	             {field: 'bpName',displayName: '业务产品'},
	        ],
			onRegisterApi : function(gridApi) {
				$scope.bpGridApi = gridApi;
                $scope.bpGridApi.selection.on.rowSelectionChanged($scope,function(row,event){
                    //行选中事件
                    if(row.isSelected && $scope.baseInfo.bpList.indexOf(row.entity.bpId) == -1){
                        $scope.baseInfo.bpList.push(row.entity.bpId);
                    }
					if(!row.isSelected && $scope.baseInfo.bpList.indexOf(row.entity.bpId) > -1) {
                        delete $scope.baseInfo.bpList[$scope.baseInfo.bpList.indexOf(row.entity.bpId)];
					}
                });
                $scope.bpGridApi.selection.on.rowSelectionChangedBatch($scope,function(row,event){
                    //全选事件
                    $scope.baseInfo.bpList = [];
                    if(row[0].isSelected){
                        angular.forEach($scope.bpGrid.data, function(item) {
                            $scope.baseInfo.bpList.push(item.bpId);
                        });
                    }
                });
			},
            isRowSelectable: function(row){ // 选中行
				if($scope.baseInfo.bpType != null && $scope.baseInfo.bpType != 0){
					if($scope.baseInfo.bpList){
						for(var i=0;i<$scope.baseInfo.bpList.length;i++){
							 if(row.entity.bpId==$scope.baseInfo.bpList[i]){
								 row.grid.api.selection.selectRow(row.entity);
							 }
						}
					}
				}
	        }
	};

	// TODO
	$scope.bpIds=[];
	$scope.changeServiceTypeSelect = function(){
		$scope.bpIds=[];
		$scope.baseInfo.serviceTypes='';
		// console.debug($scope.bpGridApi.selection);
		//angular.forEach($scope.baseInfo.bpList,function(item,index){
		angular.forEach($scope.bpGridApi.selection.getSelectedRows(),function(item,index){
			//$scope.bpIds[index] = item;
			$scope.bpIds[index] = item.bpId;
		})
		
		$http.post('jumpRoute/getServiceTypeSelect','bqIds='+angular.toJson($scope.bpIds),
				{headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
			).success(function(data){
				$scope.serviceTypeGrid.data=data;
			});
	};
	
	// 服务类型
	$scope.serviceTypeGrid = {
		columnDefs: [
		    {field: 'sysName',displayName: '服务类型'},
	    ],
		onRegisterApi : function(gridApi) {
			$scope.serviceTypeGridApi = gridApi;
		},
		isRowSelectable: function(row){ // 选中行
			if($scope.baseInfo.serviceTypes != null && $scope.baseInfo.serviceTypes != ''){
				 if($scope.baseInfo.serviceTypes.indexOf(row.entity.sysValue) != -1){
					 row.grid.api.selection.selectRow(row.entity);
				 }
			}
        }	
	};
	
	//商户省份表格
	$scope.provinceGrid = {
	        columnDefs: [
	             {field: 'name',displayName: '省份'},
	        ],
			onRegisterApi : function(gridApi) {
				$scope.provinceGridApi = gridApi;
				$scope.provinceGridApi.selection.on.rowSelectionChanged($scope,function(row,event){
					//行选中事件
					$scope.findCity();
				});
				$scope.provinceGridApi.selection.on.rowSelectionChangedBatch($scope,function(row,event){
					//全选事件
					$scope.findCity();
				});
			},
			isRowSelectable: function(row){ // 选中行 
				if($scope.baseInfo.provinceType != null && $scope.baseInfo.provinceType != 0){
					if($scope.baseInfo.provinceList){
						for(var i=0;i<$scope.baseInfo.provinceList.length;i++){
							 if(row.entity.name==$scope.baseInfo.provinceList[i]){
								 row.grid.api.selection.selectRow(row.entity);
								 break;
							 }
						}
					}
				}
	        }
	};

	//商户市表格
	$scope.cityGrid = {
		columnDefs: [
			{field: 'name',displayName:'市'},
		],
		onRegisterApi : function(gridApi) {
			$scope.cityGridApi = gridApi;
		},
		isRowSelectable: function(row){ // 选中行
			if($scope.baseInfo.provinceType != null && $scope.baseInfo.provinceType != 0){
				if($scope.baseInfo.cityList){
					for(var i=0;i<$scope.baseInfo.cityList.length;i++){
						if(row.entity.name==$scope.baseInfo.cityList[i]){
							row.grid.api.selection.selectRow(row.entity);
							break;
						}
					}
				}
			}
		}
	};

	//发卡行
	$scope.cardBinGrid = {
	        columnDefs: [
	             {field: 'bank_name',displayName: '银行名称'},
	        ],
			onRegisterApi : function(gridApi) {
				$scope.cardBinGridApi = gridApi;
			},
			isRowSelectable: function(row){ // 选中行 
				if($scope.baseInfo.cardBinType != null && $scope.baseInfo.cardBinType != 0){
					if($scope.baseInfo.cardBinList){
						for(var i=0;i<$scope.baseInfo.cardBinList.length;i++){
							 if(row.entity.id==$scope.baseInfo.cardBinList[i]){
								 row.grid.api.selection.selectRow(row.entity);
								 break;
							 }
						}
					}
				}
	        }
	};

	//行业
	$scope.acqMerchantGrid = {
		columnDefs: [
			{field: 'sysName',displayName: '行业'},
		],
		onRegisterApi : function(gridApi) {
			$scope.acqMerchantGridApi = gridApi;
		},
		isRowSelectable: function(row){ // 选中行
			if($scope.baseInfo.acqMerchantState != null && $scope.baseInfo.acqMerchantState != 0){
				if($scope.baseInfo.acqMerchantList){
					for(var i=0;i<$scope.baseInfo.acqMerchantList.length;i++){
						if(row.entity.sysValue==$scope.baseInfo.acqMerchantList[i]){
							row.grid.api.selection.selectRow(row.entity);
							break;
						}
					}
				}
			}
		}
	};
	$scope.serviceTypes=[];
$scope.submit = function(){
		
		//判断日期大小、金额大小
		if($scope.baseInfo.startDate>$scope.baseInfo.endDate){
			$scope.notice("开始生效日期不能大于截止生效日期");
			return;
		}
		if($scope.baseInfo.startTime>$scope.baseInfo.endTime){
			$scope.notice("每天生效时间不能大于每天截止时间");	
			return;
		}
		if(Number($scope.baseInfo.minTransAmount)>Number($scope.baseInfo.maxTransAmount)){
			$scope.notice("交易最小金额不能大于最大金额");
			return;
		}
		$scope.baseInfo.weekList = [];
		addWeekDays($scope.baseInfo.weekList,$scope.baseInfo.monday);
		addWeekDays($scope.baseInfo.weekList,$scope.baseInfo.tuesday);
		addWeekDays($scope.baseInfo.weekList,$scope.baseInfo.wednesday);
		addWeekDays($scope.baseInfo.weekList,$scope.baseInfo.thursday);
		addWeekDays($scope.baseInfo.weekList,$scope.baseInfo.friday);
		addWeekDays($scope.baseInfo.weekList,$scope.baseInfo.saturday);
		addWeekDays($scope.baseInfo.weekList,$scope.baseInfo.sunday);
		if($scope.baseInfo.weekList.length==0){
			$scope.notice("请设置每周重复");
			return;
		}
		$scope.baseInfo.weekDays = $scope.baseInfo.weekList.toString();
		angular.forEach($scope.serviceTypeGridApi.selection.getSelectedRows(),function(item,index){
			$scope.serviceTypes[index] = item.sysValue;
		})
		//取到所有需要提交的数据
		$scope.submitData = {
				baseInfo:$scope.baseInfo,
				bpList:$scope.bpGridApi.selection.getSelectedRows(),
				provinceList:$scope.provinceGridApi.selection.getSelectedRows(),
				cardBinList:$scope.cardBinGridApi.selection.getSelectedRows(),
				acqMerchantList:$scope.acqMerchantGridApi.selection.getSelectedRows(),
				cityList:$scope.cityGridApi.selection.getSelectedRows(),
				serviceTypes:$scope.serviceTypes
		};
		$scope.submitting = true;
		$http.post("jumpRoute/save.do",angular.toJson($scope.submitData)).success(function(data){
			if(data.status){
				$scope.notice(data.msg);
				$state.transitionTo('org.jumpRoute',null,{reload:true});
			} else {
				$scope.notice(data.msg);
				$scope.submitting = false;
			}
		});
	}
	function addWeekDays(weekList,day){
		if(day!=null&&day!=-1){
			weekList.push(day);
		}
		return weekList;
	}

	
});