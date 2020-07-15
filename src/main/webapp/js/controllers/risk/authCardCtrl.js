/**
 * 实名认证
 */							
angular.module('inspinia',['uiSwitch']).controller('authCardCtrl',function($scope, $http, $state, $stateParams, i18nService,$filter,SweetAlert,$document) {

	i18nService.setCurrentLang('zh-cn');
	$scope.info={rollType:-1,rollBelong:2,rollStatus:-1};
	$scope._rollStatus=[{text:"全部",value:-1},{text:"开启",value:1},{text:"关闭",value:0}];
	$scope.blacklistDate=[]
	//查询
	$scope.query=function(){
		if($scope.info.sdate>$scope.info.edate){
			$scope.notice("起始时间不能大于结束时间");
			return;
		}
		$http.post('authCardAction/queryAuthCardList',
				"info="+angular.toJson($scope.info)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
				 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		.success(function(data){
			if(!data.bols){
				$scope.notice("查询失败");
				return;
			}
			 $scope.myVar = true;
			$scope.blacklistDate =data.page.result; 
			$scope.blacklistGrid.totalItems = data.page.totalCount;
		})
	}
	$scope.query();
	//清空
	$scope.reset = function() {
		$scope.info={rollType:-1,rollBelong:2,rollNo:"",rollName:"",rollStatus:-1,sdate:"",edate:""}
	};
	
	$scope.paginationOptions=angular.copy($scope.paginationOptions);	
	$scope.blacklistGrid = {
			data:"blacklistDate",
	        paginationPageSize:10,                  //分页数量
	        paginationPageSizes: [10,20,50,100],	  //切换每页记录数
	        useExternalPagination: true,		  //开启拓展名
	        columnDefs: [
	            {field: 'id',displayName: 'ID',width: 100,pinnable: false,sortable: false},
	            {field: 'cardNo',displayName: '银行卡号',width: 220,pinnable: false,sortable: false},
	            {field: 'userName',displayName: '持卡人',width: 130,pinnable: false,sortable: false},
	            {field: 'merchantNo',displayName: '商户编号',width: 200,pinnable: false,sortable: false},
                {field: 'merchantName',displayName: '商户名称',width: 120,pinnable: false,sortable: false},
                {field: 'idCard',displayName: '身份证',width: 200,pinnable: false,sortable: false},
                {field: 'mobile',displayName: '手机号',width: 160,pinnable: false,sortable: false},
                {field: 'status',displayName: '认证状态',width: 160,pinnable: false,sortable: false,cellFilter:"statusNodes"},
                
//                {field: 'rollStatus',displayName: '状态',width: 130,pinnable: false,sortable: false,
//                	cellTemplate:
//                		'<span ng-show="grid.appScope.hasPermit(\'blackList.switch\')"><switch class="switch switch-s" ng-model="row.entity.rollStatus" ng-change="grid.appScope.open(row)" /></span>'
//        	            +'<span ng-show="!grid.appScope.hasPermit(\'blackList.switch\')"> <span ng-show="row.entity.rollStatus==1">开启</span><span ng-show="row.entity.rollStatus==0">关闭</span></span>'
//                },
                
                {field: 'createTime',displayName: '创建时间',width: 150,pinnable: false,sortable: false,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
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
	
	
	 //实名认证导出
	 $scope.exportInfo=function(){
			 SweetAlert.swal({
		            title: "确认导出？",
		            showCancelButton: true,
		            confirmButtonColor: "#DD6B55",
		            confirmButtonText: "提交",
		            cancelButtonText: "取消",
		            closeOnConfirm: true,
		            closeOnCancel: true 
		            },
			        function (isConfirm) {
			            if (isConfirm) {
					       	location.href="authCardAction/authCardExport.do?info="+angular.toJson($scope.info);
			            }
		        });
	 }
	 
	    $scope.myVar = true;
//	    $scope.myVar1 = false;
	    $scope.toggle = function() {	  
			$http.post('authCardAction/authCardTotal',
					"info="+angular.toJson($scope.info),
					 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			.success(function(data){
				/*if(!data.bols){
					$scope.notice(data.count);
					return;
				}*/
				$scope.totalCount = data.page.count;
	    		$scope.sucCount = data.page.sucCount;
	    		$scope.myVar = !$scope.myVar;
//	    		$scope.myVar1 = !$scope.myVar1;
			})
	    }
})
.filter('statusNodes', function () {
	return function (value) {
		switch(value) {
			case '0' :
				return "失败";
				break;
			case '1' :
				return "成功";
				break;
			case '2' :
				return "失效";
				break;
		}
	}
})
