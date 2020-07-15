/**
 * 微创业商户详情
 */
angular.module('inspinia').controller('superPushMerchantDetailCtrl',function($scope,$http,$stateParams){

	//查询微创业商户详情
	$scope.query = function(){
		$http.get('superPush/superPushMerchantDetail?merchantNo='+$stateParams.merchantNo)
		.success(function(msg){
			if(msg.status){
				$scope.baseInfo = msg.baseInfo;
				$scope.bpList = msg.bpList;
				$scope.tiList = msg.tiPage.result;
				$scope.tiTotal = msg.tiPage.totalCount;
				$scope.merchantCardInfo = msg.merchantCardInfo;
			} else {
				$scope.notice(msg.msg);
			}
		});
	}
	$scope.query();

$scope.agentGrid={                           //配置表格
	    enableHorizontalScrollbar: 1,        //横向滚动条
	    enableVerticalScrollbar : 1,  		//纵向滚动条
	    columnDefs:[                           //表格数据
	       { field: 'index',displayName:'序号',cellTemplate: "<span class='checkbox'>{{rowRenderIndex + 1}}</span>"},
	       { field: 'agentName',displayName:'代理商名称'},
	       { field: 'agentNo',displayName:'代理商编号'},
	       { field: 'action',displayName:'操作',
	      	 cellTemplate:
	      		 '<a class="lh30" ng-click="grid.appScope.deleteAgent(row.entity)">删除</a>'
	       }
	    ],
		  onRegisterApi: function(gridApi) {                
		        $scope.agentGridApi = gridApi;
		  }
	};

});

