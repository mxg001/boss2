/**
 * 微创业活动设置
 */
angular.module('inspinia',['uiSwitch']).controller('superPushConfigCtrl',function($scope,$http,$state,$stateParams,$compile,$filter,$log,$uibModal,$timeout,SweetAlert){

//查询微创业的相关配置信息
	$scope.query = function(){
		$http.get('superPushConfig/getInfo')
		.success(function(msg){
			if(msg.status){
				$scope.baseInfo = msg.info;
				$scope.shareRuleGrid.data = msg.shareRuleList;
				angular.forEach($scope.shareRuleGrid.data,function(data){
					data.action = 1;
				})
//				$scope.agentGrid.data = msg.agentList;
			} else {
				$scope.notice(msg.msg);
			}
		});
	}
	$scope.query();

	$scope.shareRuleGrid={                  //配置表格
	    enableHorizontalScrollbar: 1,       //横向滚动条
	    enableVerticalScrollbar : 1,  		//纵向滚动条
	    columnDefs:[                        //表格数据
	       { field: 'bpName',displayName:'业务产品'},
	       { field: 'profit',displayName:'一级代理商分润',cellTemplate:
	    	   '<div class="col-sm-12 checkbox" ng-show="row.entity.action==1">'
	    	  +' <div ng-show="row.entity.oneAgentProfitType==1">{{row.entity.oneAgentPerFixIncome}}'
	    	  +' </div>'
	    	  +' <div ng-show="row.entity.oneAgentProfitType==2">{{row.entity.oneAgentPerFixInrate}}%'
	    	  +' </div>'
	    	  +'</div>'
	    	  +' <div class="col-sm-12 checkbox" ng-show="row.entity.action==2">'
	    	  +' <div ng-show="row.entity.oneAgentProfitType==1" style="padding-left:30px;"><input style="width:80px;float:left" type="number" ng-model="row.entity.oneAgentPerFixIncome"/>'
	    	  +' </div>'
	    	  +' <div ng-show="row.entity.oneAgentProfitType==2" style="padding-left:30px;"><input type="number" ng-model="row.entity.oneAgentPerFixInrate" style="width:80px;float:left"/><span style="padding-left:5px;float:left">%</span>'
	    	  +' </div>'
	    	  +'</div>'},
	    	  { field: 'profit',displayName:'直属代理商分润',cellTemplate:
	       	   '<div class="col-sm-12 checkbox" ng-show="row.entity.action==1">'
	       	  +' <div ng-show="row.entity.agentProfitType==1">{{row.entity.agentPerFixIncome}}'
	       	  +' </div>'
	       	  +' <div ng-show="row.entity.agentProfitType==2">{{row.entity.agentPerFixInrate}}%'
	       	  +' </div>'
	       	  +'</div>'
	       	  +' <div class="col-sm-12 checkbox" ng-show="row.entity.action==2">'
	       	  +' <div ng-show="row.entity.agentProfitType==1" style="padding-left:30px;"><input style="width:80px;float:left" type="number" ng-model="row.entity.agentPerFixIncome"/>'
	       	  +' </div>'
	       	  +' <div ng-show="row.entity.agentProfitType==2" style="padding-left:30px;"><input type="number" ng-model="row.entity.agentPerFixInrate" style="width:80px;float:left"/><span style="padding-left:5px;float:left">%</span>'
	       	  +' </div>'
	       	  +'</div>'},
	       { field: 'profit1',displayName:'一级分润',cellTemplate:
	    	   '<div class="col-sm-12 checkbox" ng-show="row.entity.action==1">'
	     	  +' <div ng-show="row.entity.profitType1==1">{{row.entity.perFixIncome1}}'
	     	  +' </div>'
	     	  +' <div ng-show="row.entity.profitType1==2">{{row.entity.perFixInrate1}}%'
	     	  +' </div>'
	     	  +'</div>'
	     	  +' <div class="col-sm-12 checkbox" ng-show="row.entity.action==2">'
	     	  +' <div ng-show="row.entity.profitType1==1" style="padding-left:30px;"><input type="number" style="width:80px;float:left" ng-model="row.entity.perFixIncome1" />'
	     	  +' </div>'
	     	  +' <div ng-show="row.entity.profitType1==2" style="padding-left:30px;"><input type="number" ng-model="row.entity.perFixInrate1" style="width:80px;float:left"/><span style="padding-left:5px;float:left">%</span>'
	     	  +' </div>'
	     	  +'</div>'
	       },
	       { field: 'profit2',displayName:'二级分润',cellTemplate:
	    	   '<div class="col-sm-12 checkbox" ng-show="row.entity.action==1">'
	     	  +' <div ng-show="row.entity.profitType2==1">{{row.entity.perFixIncome2}}'
	     	  +' </div>'
	     	  +' <div ng-show="row.entity.profitType2==2">{{row.entity.perFixInrate2}}%'
	     	  +' </div>'
	     	  +'</div>'
	     	  +' <div class="col-sm-12 checkbox" ng-show="row.entity.action==2">'
	     	  +' <div ng-show="row.entity.profitType2==1" style="padding-left:30px;"><input type="number" style="width:80px;float:left" ng-model="row.entity.perFixIncome2" />'
	     	  +' </div>'
	     	  +' <div ng-show="row.entity.profitType2==2"  style="padding-left:30px;"><input type="number" ng-model="row.entity.perFixInrate2" style="width:80px;float:left"/><span style="padding-left:5px;float:left">%</span>'
	     	  +' </div>'
	     	  +'</div>'},
	       { field: 'profit3',displayName:'三级分润',cellTemplate:
	    	   '<div class="col-sm-12 checkbox" ng-show="row.entity.action==1">'
	     	  +' <div ng-show="row.entity.profitType3==1">{{row.entity.perFixIncome3}}'
	     	  +' </div>'
	     	  +' <div ng-show="row.entity.profitType3==2">{{row.entity.perFixInrate3}}%'
	     	  +' </div>'
	     	  +'</div>'
	     	  +' <div class="col-sm-12 checkbox" ng-show="row.entity.action==2">'
	     	  +' <div ng-show="row.entity.profitType3==1" style="padding-left:30px;"><input type="number" style="width:80px;float:left" ng-model="row.entity.perFixIncome3" />'
	     	  +' </div>'
	     	  +' <div ng-show="row.entity.profitType3==2"  style="padding-left:30px;"><input type="number" ng-model="row.entity.perFixInrate3" style="width:80px;float:left"/><span style="padding-left:5px;float:left">%</span>'
	     	  +' </div>'
	     	  +'</div>'},
	       { field: 'action',displayName:'操作',
	      	 cellTemplate:
	      		 '<a  class="lh30" ng-show="row.entity.action==1 && grid.appScope.hasPermit(\'superPushConfig.saveShareRule\')" ng-click="grid.appScope.editShareRule(row.entity)"><input type="hidden" ng-model="row.entity.action" />修改</a>'+
	      		 '<a  class="lh30" ng-show="row.entity.action==2 && grid.appScope.hasPermit(\'superPushConfig.saveShareRule\')" ng-click="grid.appScope.saveShareRule(row.entity)">保存</a>'
	       }
	    ]
	};
	
	//修改分润
	$scope.editShareRule = function(entity){
		entity.action = 2;
	}
    // //分享奖励开关
    // $scope.rewardPointSwitchSelect = function() {
    //    if(!$scope.baseInfo.rewardPointSwitch){
    //        $scope.baseInfo.prizesAmount=0;
	 //   }
    // };
	//保存分润
	$scope.saveShareRule = function(entity){
		$scope.copyEntity = angular.copy(entity);
		SweetAlert.swal({
	        title: "是否保存",
	//        text: "服务状态为关闭后，不能正常交易!",
	        type: "warning",
	        showCancelButton: true,
	        confirmButtonColor: "#DD6B55",
	        confirmButtonText: "提交",
	        cancelButtonText: "取消",
	        closeOnConfirm: true,
	        closeOnCancel: true },
	        function (isConfirm) {
	            if (isConfirm) {
	            	$http.post('superPushConfig/saveShareRule',angular.toJson(entity))
	            	.success(function(msg){
	            		if(msg.status){
	            			entity.id = msg.id;
	            			entity.action = 1;
	            		}
	            		$scope.notice(msg.msg);
	            	}).error(function(){
	            		$scope.notice('服务异常');
	            	})
	            } else {
	            	entity = $scope.copyEntity;
	            }
	        }
		);
	}
	
	//提交微创业配置信息
	$scope.submit = function(){
		$scope.submitting = true;
		var data = {
				"baseInfo" : $scope.baseInfo
	//			"agentList":$scope.agentGrid.data,
		};
		 $http.post("superPushConfig/saveConfig",angular.toJson(data))
			.success(function(data){
				if(data.status){
					$scope.notice(data.msg);
					$scope.query();
				}else{
					$scope.notice(data.msg);
				}
				$scope.submitting = false;
			}).error(function(){
				$scope.notice('服务异常');
				$scope.submitting = false;
			});
	}
	
	//取消提交，并还原正在编辑的配置信息
	$scope.submitCancel=function(){
		$scope.query();
	}
});

//$scope.agentGrid={                           //配置表格
//	    enableHorizontalScrollbar: 1,        //横向滚动条
//	    enableVerticalScrollbar : 1,  		//纵向滚动条
//	    columnDefs:[                           //表格数据
//	       { field: 'index',displayName:'序号',cellTemplate: "<span class='checkbox'>{{rowRenderIndex + 1}}</span>"},
//	       { field: 'agentName',displayName:'代理商名称'},
//	       { field: 'agentNo',displayName:'代理商编号'},
//	       { field: 'action',displayName:'操作',
//	      	 cellTemplate:
//	      		 '<a class="lh30" ng-click="grid.appScope.deleteAgent(row.entity)">删除</a>'
//	       }
//	    ],
//		  onRegisterApi: function(gridApi) {                
//		        $scope.agentGridApi = gridApi;
//		  }
//	};

//$scope.addAgentGrid={                       //配置表格
//	    enableHorizontalScrollbar: 1,       //横向滚动条
//	    enableVerticalScrollbar : 1,  		//纵向滚动条
//	    columnDefs:[                        //表格数据
//	       { field: 'index',displayName:'序号',width:120,cellTemplate: "<span class='checkbox'>{{rowRenderIndex + 1}}</span>"},
//	       { field: 'agentName',displayName:'代理商名称',width:200},
//	       { field: 'agentNo',displayName:'代理商编号',width:150}
////	       { field: 'action',displayName:'操作',width:120,pinnedRight:true,
////	      	 cellTemplate:
////	      		 '<div  class="lh30" <a ng-click="grid.appScope.deleteAgent(row.entity)">删除</a></div>'
////	       }
//	    ],
//		  onRegisterApi: function(gridApi) {                
//		        $scope.addAgentGridApi = gridApi;
//		  },
//		  isRowSelectable: function(row){ // 选中行 
//				if($scope.agentGrid.data != null){
//					for(var i=0;i<$scope.agentGrid.data.length;i++){
//						 if(row.entity.agentNo==$scope.agentGrid.data[i].agentNo){
//							 row.grid.api.selection.selectRow(row.entity);
//						 }
//					}
//				}
//	        }
//	};

//$scope.deleteAgent = function(entity){
//	SweetAlert.swal({
//        title: "是否删除",
////        text: "服务状态为关闭后，不能正常交易!",
//        type: "warning",
//        showCancelButton: true,
//        confirmButtonColor: "#DD6B55",
//        confirmButtonText: "提交",
//        cancelButtonText: "取消",
//        closeOnConfirm: true,
//        closeOnCancel: true },
//        function (isConfirm) {
//            if (isConfirm) {
//            	var index = $scope.agentGrid.data.indexOf(entity);
//            	if (index > -1) {
//            		$scope.agentGrid.data.splice(index, 1);
//            	}
//            }
//        });
//}
//获取所有的一级代理商
//$scope.agent=[{value:"",text:"全部"}];
//$scope.getStates =getStates;
//var oldValue="";
//var timeout="";
//function getStates(value) {
//	$scope.agentt = [];
//		var newValue=value;
//		if(newValue != oldValue){
//			if (timeout) $timeout.cancel(timeout);
//			timeout = $timeout(
//				function(){
//					$http.post('agentInfo/selectAllInfo','item=' + value,
//							{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
//						.then(function (response) {
//							$scope.addAgentGrid.data = response.data;
//							if(response.data.length==0) {
//								$scope.agentt.push({value: "", text: "全部"});
//							}else{
//								$scope.agentt.push({value: "", text: "全部"});
//								for(var i=0; i<response.data.length; i++){
//									$scope.agentt.push({value:response.data[i].agentNode,text:response.data[i].agentNo + " " + response.data[i].agentName});
//								}
//							}
//							$scope.agent = $scope.agentt;
//							oldValue = value;
//						});
//				},800);
//		}
//};
////获取所有的一级代理商(limit 50)
//$scope.getAllAgent = function(){
//	 $http.post("agentInfo/selectAllInfo")
//	 .success(function(msg){
//			//响应成功
//		 $scope.addAgentGrid.data = msg;
//	});
//}
//添加代理商的modal
//$scope.addAgentModal = function(){
//	$("#addAgentModal").modal("show");
//	$scope.baseInfo.addAgentNo = null;
//	$scope.getAllAgent();
//}
//添加代理商,将agentGrid没有的代理商添加进来
//$scope.addAgentList = function(){
//	angular.forEach($scope.addAgentGridApi.selection.getSelectedRows(),function(data){
//		if(!contains($scope.agentGrid.data,data)){
//			$scope.agentGrid.data[$scope.agentGrid.data.length] = data;
//		}
//	});
//	$('#addAgentModal').modal('hide');
//}
//function contains(arr, obj) {  
//    var i = arr.length;
//    while (i--) {  
//        if (arr[i].agentNo == obj.agentNo) {  
//            return true;  
//        }  
//    }  
//    return false;  
//}

//取消，隐藏modal
//$scope.cancel=function(){
//	$('#addAgentModal').modal('hide');
//}


