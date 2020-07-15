/**
 * APP的历史记录
 */
angular.module('inspinia').controller('appMobileVerInfoCtrl',function($scope,$http,$state,$stateParams,$compile,$filter,i18nService){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	$scope.info={};
	$http.post('appInfoAction/findAppInfo?ids='+$stateParams.id)
	.success(function(largeLoad) {
		if(largeLoad.bols){
			$scope.info=largeLoad.data;
		}else{
			$scope.notice(largeLoad.msg);
		}
	});
	
	//查询
	$scope.selectInfo=function(){
		$http.post(
				'appInfoAction/selectChildAllInfo',
				 "ids="+angular.toJson($stateParams.id)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
				 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
			).success(function(result){
				//响应成功
				$scope.appInfoOptions.data = result.page.result; 
				$scope.appInfoOptions.totalItems = result.page.totalCount;
			});
	}
	$scope.selectInfo();
	
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	$scope.appInfoOptions={                           //配置表格
		      paginationPageSize:10,                  //分页数量
		      paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
		      useExternalPagination: true,
		      columnDefs:[                           //表格数据
		         { field: 'appType',displayName:'APP编号',width:150},
		         { field: 'platform',displayName:'所属客户端',width:150,
		        	 cellFilter:"formatDropping:[{text:'android',value:0},{text:'IOS',value:1}]"
		         },
		         { field: 'version',displayName:'版本号',width:150},
		         { field: 'appUrl',displayName:'连接',width:150},
		         { field: 'url',displayName:'二维码路径',width:150},
		         { field: 'downFlag',displayName:'更新状态',width:150,
		        	 cellFilter:"formatDropping:[{text:'不需要',value:0},{text:'需要更新',value:1},{text:'需要强制下载',value:2}]"
		         },
		         { field: 'verDesc',displayName:'说明',width:150},
		         { field: 'lowestVersion',displayName:'最低版本',width:150},
		         { field: 'appLogo',displayName:'LOGO',width:150},
		         { field: 'createTime',displayName:'创建时间',width:150,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
		         { field: 'id',displayName:'操作',width:180,pinnedRight:true,
		        	 cellTemplate:'<a class="lh30" ui-sref="sys.appMobileVerInfoDetail({id:row.entity.id})">详情</a> '
		        		 +'<a class="lh30" ui-sref="sys.appMobileVerInfoAddOrUp({id:row.entity.id,appNo:row.entity.appType,appName:row.entity.appName})"> | 修改</a>'
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
	
});