/**
 * 商户迁移
 */
angular.module('inspinia', ['infinity.angular-chosen']).controller('migrateCheckCtrl',function(i18nService,$scope,$http,$timeout,$state,$stateParams,$compile,$filter){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	$scope.info={remark:"",id:$stateParams.id};
	$scope.check = function(checkStatus){
		if(checkStatus == "3" && "" == $scope.info.remark){
			$scope.notice("请输入审核失败意见!");
		}
		var data = {
				"id" : $scope.info.id,
				"checkStatus" : checkStatus,
				"remark" : $scope.info.remark
			};
		$http.post('merchantMigrate/checkMigrateModify',angular.toJson(data)).success(function(result){
			if(result.status=="1"){
				 $state.transitionTo('merchant.migrate',null,{reload:true});
			}
			$scope.notice(result.msg);
		}).error(function(){
			$scope.notice("提交请求失败,请联系管理员");
		});
	}
	
	$scope.query = function(){
		$http.get("merchantMigrate/migrateCheck/"+$stateParams.id).success(function(msg){
			$scope.info={oaNo:msg.merchantMigrate.oaNo,id:msg.merchantMigrate.id,agentNo:msg.merchantMigrate.agentNo+'['+msg.merchantMigrate.oneAgentName+']',
	 				agentNode:msg.merchantMigrate.nodeAgentNo+'['+msg.merchantMigrate.nodeAgentName+']',createPerson:msg.merchantMigrate.createPerson,
	 				createTime:msg.merchantMigrate.createTime,checkRemark:msg.merchantMigrate.checkRemark};
	 		$scope.gridOptions.data = msg.page.result;  
			$scope.gridOptions.totalItems = msg.page.totalCount;
	 	});
	}
	
	$scope.query();
	 
	 $scope.paginationOptions=angular.copy($scope.paginationOptions);
	 $scope.gridOptions={                           //配置表格
			  paginationPageSize:10,                  //分页数量
			  paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
			  useExternalPagination: true,  
		      columnDefs:[                           //表格数据
                     { field: 'merchantNo',displayName:'商户编号',width:150},
    		         { field: 'agentLevel',displayName:'原直属代理商级别',width:150},
    		         { field: 'nodeAgentName',displayName:'原直属代理商',width:150},
    		         { field: 'oneAgentName',displayName:'原一级代理商',width:150},
		      ],
		      onRegisterApi: function(gridApi) {                
		          $scope.gridApi = gridApi;
		          gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
		          	$scope.paginationOptions.pageNo = newPage;
		          	$scope.paginationOptions.pageSize = pageSize;
		          	$scope.query();
		          });
		      }
		};
	
})