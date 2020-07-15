/**
 * 机具详情
 */
angular.module('inspinia').controller('terminalDetailCtrl',function($scope,$state,$http,$stateParams,$filter){
	$scope.terminalStates=[{text:"全部",value:"-1"},{text:"已入库",value:"0"},{text:"已分配",value:"1"},{text:"已使用",value:"2"},{text:"申请中",value:"3"}];
	$scope.relationTypeList = [{text:"互斥",value:"0"},{text:"同组",value:"1"}];
    $scope.types = [{text:"欢乐返-循环送",value:"008"},{text:"欢乐返",value:"009"}];
	
	$http.get('terminalInfo/selectObjInfo.do?ids='+$stateParams.termId)
    .success(function(msg) {
    	if(msg.status){
    		$scope.data=msg.terminalInfo;
    		//活动类型表数据
    		$scope.activityTypeGrid.data = msg.activityTypeList;
    		//勾选已有的活动
	    	if($scope.data.activityType){
	    		var strArr = $scope.data.activityType.split(',');
                if((strArr.indexOf("7")>=0||strArr.indexOf("8")>=0)){
                    $scope.isActivityHardware=true;
                    if(strArr.indexOf("7")>=0){
                        $scope.selectHlfActivityHardware($scope.data.type,'009');
                    }else if(strArr.indexOf("8")>=0){
                        $scope.selectHlfActivityHardware($scope.data.type,'008');
                    }
                }
				for(var i=0; i<strArr.length; i++){
					angular.forEach($scope.activityTypeGrid.data,function(item){
						//比较sys_value相等，就勾上
						if(item.sys_value == strArr[i]){
							item.selectStatus = true;
						}
					});
				}
	    	}
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
                    if($scope.data.activityTypeNo!=null){
                        angular.forEach($scope.activityHlfGrid.data,function(item){
                            //比较相等，就勾上
                            if(item.activityTypeNo == $scope.data.activityTypeNo){
                                item.selectStatus = true;
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
	
	$scope.activityTypeGrid = {
	        columnDefs: [
	             {field: 'selectStatus',displayName: '',width: '30',cellTemplate:
	        		 '<input type="checkbox" ng-disabled="true" ng-change="grid.appScope.selectedActivityType(row.entity)" ng-model="row.entity.selectStatus" ng-checked="row.entity.selectStatus"/>'},
	             {field: 'sys_name',displayName: '活动类型',width: 180},
	             {field: 'group_no',displayName: '群组',width: 180},
	             {field: 'relation_type',displayName: '关系',width: 180,cellFilter:"formatDropping:"+angular.toJson($scope.relationTypeList)},
	        ]
	    };

    $scope.activityHlfGrid = {
        columnDefs: [
            {field: 'activityTypeNo',displayName: '',width: '30',cellTemplate:
                    '<input type="checkbox" ng-disabled="true" ng-change="grid.appScope.selectedActivityTypeNo(row.entity)" ng-model="row.entity.selectStatus" ng-checked="row.entity.selectStatus" />'},
            {field: 'activityCode',displayName: '欢乐返类型',width: 180,cellFilter:"formatDropping:"+angular.toJson($scope.types)},
            {field: 'activityTypeName',displayName: '欢乐返子类型名称',width: 180},
            {field: 'transAmount',displayName: '交易金额',width: 180},
            {field: 'cashBackAmount',displayName: '返现金额',width: 180},
            {field: 'repeatRegisterAmount',displayName: '重复注册返现金额',width: 180},
        ]
    };
});

