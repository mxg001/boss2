/*
 * 新增交易跳转集群配置
 */
angular.module('inspinia').controller('jumpRouteAddCtrl',function($scope, $http, $state){
	
	$scope.groupList = [];//集群集合
	$scope.acqList = [];//收单机构集合
	$scope.baseInfo = {status:1, cardType:1, bpType:1,serviceTypeSelect:0, provinceType:0,monday:0,
			tuesday:1,wednesday:2,thursday:3,friday:4,saturday:5,sunday:6,cardBinType:0,acqMerchantState:0,acqMerchantShowState:0,
        	effectiveDateType:1,relationActivity:0};
	// $scope.bpTypeList = [{text:'不限',value:0},{text:'指定业务产品',value:1}];
	$scope.bpTypeList = [{text:'指定业务产品',value:1}];
	$scope.provinceTypeList = [{text:'不限',value:0},{text:'指定省份',value:1}];
	$scope.cardBinList = [{text:'不限',value:0},{text:'指定发卡行',value:1}];
	$scope.serviceTypeSelectList = [{text:'不限',value:0},{text:'指定服务类型',value:1}];
	$scope.acqMerchantList = [{text:'不限',value:0},{text:'指定行业',value:1}];
	$scope.effectiveDateTypeList = [{text:'每天',value:1},{text:'周一至周五',value:2},{text:'法定工作日',value:3},{text:'自定义',value:4}];
    $scope.statusList = [{text:"生效",value:1},{text:"失效",value:0}];

    //是否关联活动
	$scope.relationActivitySelect = [{text:"否",value:0},{text:"是",value:1}];
	$scope.relationActivityStr=angular.toJson($scope.relationActivitySelect);

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
	//$scope.changeBp();
	
	//获取收单机构集合
	$http.post('routeGroup/acqOrgSelectBox.do'
	).success(function(data){
		$scope.acqList = data;
		for(var i=0;i<data.length;i++){
			if(data[i].acqName=="YS_ZQ"){
				$scope.acqMerchantShowStateId=data[i].id;
				break;
			}
		}
	});

	//获取所有的组织
    $http.get('teamInfo/queryTeamName.do').success(function(msg){
    	if(msg.status) {
            $scope.teamGrid.data = msg.teamInfo;
		}
    });
	
	//原来是‘获取所有包含POS刷卡服务的业务产品’，20171219修改为‘获取所有业务产品’
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
    $scope.getBpList();

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

	$scope.changeCardType = function(){
		if($scope.baseInfo.cardBinType=='0'){
			 $scope.acqMerchantClass = "col-sm-4";
		}else{
			$scope.getCardBinList();
			$scope.acqMerchantClass = "col-sm-1";
		}
	}
	$scope.changeCardType();

	$scope.setAcqMerchantShowState=function(){
		if($scope.baseInfo.acqId==$scope.acqMerchantShowStateId){
			$scope.baseInfo.acqMerchantShowState=1;
		}else{
			$scope.baseInfo.acqMerchantShowState=0;
		}
	}
	//获取所有省份
	$scope.getAreaList=function(name,type,callback){
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
	$scope.getAreaList(0,"p",function(data){
		$scope.provinceGrid.data = data;
	});
	
	$scope.getCardBinList = function(){
		$http.post('jumpRoute/getCarBinList','cardType='+$scope.baseInfo.cardType,
			{headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
		).success(function(data){
			$scope.cardBinGrid.data = data;
		}).error(function(){
		});
	}

	//获取收单机构
	$http.post('jumpRoute/getSysDictList',
		{headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
	).success(function(data){
		$scope.acqMerchantGrid.data = data;
	});

	//获取商户服务类型为POS刷卡类的路由集群
	$http.get("routeGroup/getGroupByServiceType").success(function(data){
		if(data.status){
			angular.forEach(data.groupList,function(item){
				item.groupName = item.groupCode + " " + item.groupName;
			})
			$scope.groupList = data.groupList;
		}
	});

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
			}
	};

	$scope.findCity=function(){
		$http.post("areaInfo/getAreaCityByParentId",angular.toJson({provinceList:$scope.provinceGridApi.selection.getSelectedRows()})).success(function(data){
			$scope.cityGrid.data = data;
		});
	}

	//商户市表格
	$scope.cityGrid = {
		columnDefs: [
			{field: 'name',displayName:'市'},
		],
		onRegisterApi : function(gridApi) {
			$scope.cityGridApi = gridApi;
		}
	};
	
	// TODO 服务类型
	$scope.bpIds=[];
	$scope.changeServiceTypeSelect = function(){
		$scope.bpIds=[];
		angular.forEach($scope.bpGridApi.selection.getSelectedRows(),function(item,index){
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
		}	
	};
	
	//发卡行
	$scope.cardBinGrid = {
	        columnDefs: [
	             {field: 'bank_name',displayName: '银行名称'},
	        ],
			onRegisterApi : function(gridApi) {
				$scope.cardBinGridApi = gridApi;
			}
	};

	//行业
	$scope.acqMerchantGrid = {
		columnDefs: [
			{field: 'sysName',displayName: '行业'},
		],
		onRegisterApi : function(gridApi) {
			$scope.acqMerchantGridApi = gridApi;
		}
	};
	
	$scope.serviceTypes=[];
	$scope.submit = function(){
		//判断日期大小、金额大小
		if($scope.baseInfo.startDate>$scope.baseInfo.endDate){
			$scope.notice("开始生效日期不能大于截止生效日期");
			return false;
		}
		if($scope.baseInfo.startTime>$scope.baseInfo.endTime){
			$scope.notice("每天生效时间不能大于每天截止时间");	
			return false;
		}
		if(Number($scope.baseInfo.minTransAmount)>Number($scope.baseInfo.maxTransAmount)){
			$scope.notice("交易最小金额不能大于最大金额");
			return false;
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
		}
		$scope.baseInfo.weekDays = $scope.baseInfo.weekList.toString();
		//取到所有需要提交的数据
		angular.forEach($scope.serviceTypeGridApi.selection.getSelectedRows(),function(item,index){
			$scope.serviceTypes[index] = item.sysValue;
		})
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