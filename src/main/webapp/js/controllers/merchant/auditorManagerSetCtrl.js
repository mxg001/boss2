/**
 * 审核人员管理:设置规则	
 */
angular.module('inspinia',['uiSwitch']).controller('auditorManagerSetCtrl',function($scope,$http,$state,$stateParams,i18nService,SweetAlert){
	//清空
	$scope.resetForm = function(){
		$scope.baseInfo={auditorId:-1};
	}
	$scope.resetForm();
	
	//查询出拥有商户审核权限的人员
	$http.get('user/selectUserByMenuCode.do?menuCode='+"merchant.auditMer").success(function(msg){
		if(msg!=null){
			msg.unshift({"id":-1,"userName":"请选择"});
			$scope.auditorList = msg;
		}
	});
	//查询出所有的业务产品
	$scope.getBpList = function(){
		$http.get('businessProductDefine/selectBpTeam.do').success(function(msg){
			if(msg!=null){
				$scope.bpData = msg;
				angular.forEach($scope.bpData,function(data){
					data.status=1;
				});
			}
		});
	}
	$scope.getBpList();
	//根据审核人，查询他可以审核的业务产品
	//如果审核人为空，则查询出所有的业务产品
	$scope.getBpByAuditor = function(){
		if($scope.baseInfo.auditorId==-1){
			$scope.bpList=null;
			$scope.getBpList();
			return;
		}
		$http.get('auditorManager/getBpByAuditor.do?auditorId='+$scope.baseInfo.auditorId).success(function(msg){
			if(msg!=null){
				$scope.bpList = msg;
			}
		});
		$scope.getBpList();
	}
	$scope.getBpByAuditor();
	
	$scope.auditorTable = {
			data: 'bpData',
			useExternalPagination: true,		  	//开启拓展名
			columnDefs: [
			    {field: 'bpId',width: 200, displayName: '业务产品ID'},
	            {field: 'bpName',width: 200, displayName: '业务产品名称'},
	            {field: 'status',width: 200, displayName: '状态',
	            	cellTemplate:'<switch class="switch" ng-model="row.entity.status"  ng-true-value="1" ng-false-value="0" />'},
	        ],
	        onRegisterApi: function(gridApi){
	        	$scope.gridApi = gridApi;
	        },
	        isRowSelectable: function(row){ // 选中行 
				if($scope.bpList != null){
					for(var i=0;i<$scope.bpList.length;i++){
						 if(row.entity.bpId==$scope.bpList[i].bpId){
							 row.grid.api.selection.selectRow(row.entity);
							 row.entity.status = $scope.bpList[i].status;
						 }
					}
				}
	        }
		};
	
	$scope.list = [];
	//提交规则
	$scope.submit = function(){	
		$scope.subDisable = true;
		angular.forEach($scope.gridApi.selection.getSelectedRows(),function(data){
			$scope.list.push({"bpId":data.bpId,"status":data.status,"auditorId":$scope.baseInfo.auditorId});
		})
		var data = {
				"auditorId" : $scope.baseInfo.auditorId,
				"auditorList" : $scope.list
			};
		$http.post('auditorManager/saveBatch',angular.toJson(data)).success(function(msg) {
			$scope.subDisable = false;
			if (msg.status) {
				$scope.notice(msg.msg);
				$state.transitionTo('merchant.auditorManager',null,{reload:true});
			} else {
				$scope.subDisable = false;
				$scope.notice(msg.msg);
			}
		}).error(function() {
			$scope.subDisable = false;
			$scope.notice("系统错误");
			
		});
	}
		
});