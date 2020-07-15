/**
 * APP二维码
 */
angular.module('inspinia').controller('appInfoCtrl',function($scope,$http,$state,$stateParams,$compile,$filter,i18nService,$document){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	
	$scope.info={appName:""};
	
	//查询
	$scope.selectInfo=function(){
		$http.post(
				'appInfoAction/selectAllInfo',
				 "info="+angular.toJson($scope.info)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
				 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
			).success(function(result){
				//响应成功
				$scope.appInfoOptions.data = result.page.result; 
				$scope.appInfoOptions.totalItems = result.page.totalCount;
			});
	}
	$scope.selectInfo();
	//清空
	$scope.clear=function(){
		$scope.info={appName:""};
	}
	
	$scope.paginationOptions=angular.copy($scope.paginationOptions);	
	
	$scope.appInfoOptions={                           //配置表格
		      paginationPageSize:10,                  //分页数量
		      paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
		      useExternalPagination: true,
		      columnDefs:[                           //表格数据
		         { field: 'appName',displayName:'APP名称'},
		         { field: 'parenName',displayName:'代理商APP名称'},
		         { field: 'teamName',displayName:'所属组织'},
		         { field: 'protocolVer',displayName:'隐私政策版本号'},
		         { field: 'id',displayName:'操作',
		        	 cellTemplate:'<a class="lh30" ng-show="grid.appScope.hasPermit(\'appQrcode.detail\')" ui-sref="sys.appInfoQueryDetail({id:row.entity.id})">详情</a> '
		        		 +'<a class="lh30" ng-show="grid.appScope.hasPermit(\'appQrcode.update\')" ui-sref="sys.appInfoQueryUp({id:row.entity.id})"> | 修改</a>'
		        		 +'<a class="lh30" ui-sref="sys.appMobileVerInfo({id:row.entity.appNo})"> | 历史记录</a>'
		         }
		      ],
			  onRegisterApi: function(gridApi) {                
		          $scope.gridApi = gridApi;
		          gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
		          	$scope.paginationOptions.pageNo = newPage;
		          	$scope.paginationOptions.pageSize = pageSize;
		             $scope.selectInfo();
		          });
		      }
		};
	//页面绑定回车事件
	$document.bind("keypress", function(event) {
		$scope.$apply(function (){
			if(event.keyCode == 13){
				$scope.selectInfo();
			}
		})
	});
})