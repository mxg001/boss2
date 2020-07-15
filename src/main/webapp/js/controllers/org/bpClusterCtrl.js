/**
 * 默认路由集群管理
 */
angular.module('inspinia').controller("bpClusterCtrl", function($scope, $http, $state, $stateParams, i18nService,$document) {
	
	i18nService.setCurrentLang('zh-cn');
	$scope.merService=[];
	$scope.info={productId:-1,serviceId:-1};
	//业务产品
	$http.get('businessProductDefine/selectAllInfo.do')
	.success(function(largeLoad) {
		if(!largeLoad)
			return
		$scope.productTypes=largeLoad;
		$scope.productTypes.splice(0,0,{bpId:-1,bpName:"全部"});
		$scope.serType(-1)
	});
	
	$scope.serType=function(id){
		//商户服务
	 	$scope.merService=[];
	 	$http.post("businessProductInfoAction/selectBoxAllInfo","ids="+angular.toJson(id),{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		 .success(function(msg){
				//响应成功
			if(msg.length>0){
		 		for(var i=0; i<msg.length; i++){
		 			$scope.merService.push({value:msg[i].serviceId,text:msg[i].serviceName});
		    	}
		 		$scope.merService.splice(0,0,{value:-1,text:"全部"});
		    	$scope.info.serviceId=-1;
		 	}
	 	});
		
	}
	
	//查询
	$scope.query=function(){
		$http.post('defTransRouteGroupAction/selectAllInfo',
				"info="+angular.toJson($scope.info)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
				 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		.success(function(data){
			if(!data.bols){
				$scope.notice(data.msg);
			}else{
				$scope.bpClusterGrid.data =data.page.result ; 
				$scope.bpClusterGrid.totalItems = data.page.totalCount;
			}
		})
	}
	$scope.query();
	//清空
	$scope.reset = function() {
		$scope.info={productId:-1,serviceId:-1};
	};
	$scope.paginationOptions=angular.copy($scope.paginationOptions);	
	$scope.bpClusterGrid = {
	        paginationPageSize:10,                  //分页数量
	        paginationPageSizes: [10,20,50,100],	  //切换每页记录数
	        useExternalPagination: true,		  //开启拓展名
	        enableHorizontalScrollbar: 0,        //去掉滚动条
	        enableVerticalScrollbar : 0, 
	        columnDefs: [
                {field: 'id',displayName: '序号',width: 120,pinnable: false,sortable: false},
                {field: 'bpName',displayName: '业务产品',width: 200,pinnable: false,sortable: false},
                {field: 'serviceName',displayName: '商户服务',pinnable: false,sortable: false},
                {field: 'defGroupCode',displayName: '集群编号',width: 120,pinnable: false,sortable: false},
                {field: 'groupName',displayName: '集群名称',pinnable: false,sortable: false},
	            {field: 'id',displayName: '操作',pinnable: false,sortable: false,editable:true,cellTemplate:
	            	"<div  class='lh30'><a ng-show='grid.appScope.hasPermit(\"bpCluster.update\")' ui-sref='org.bpClusterUpOrAdd({id:row.entity.id})'>修改</a>" +
	            	"<a ng-show='grid.appScope.hasPermit(\"bpCluster.detail\")' ui-sref='org.bpClusterDetail({id:row.entity.id})'> | 详情</a></div>"}
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

	//页面绑定回车事件
	$document.bind("keypress", function(event) {
		$scope.$apply(function (){
			if(event.keyCode == 13){
				$scope.query();
			}
		})
	});
	
});