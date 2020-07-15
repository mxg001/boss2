/**
 * 审核统计
 */
angular.module('inspinia').controller('auditorRecordCtrl',function($scope,$http,$state,$stateParams,i18nService,$compile,$uibModal,$log,SweetAlert,$document){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	$scope.paginationOptions=angular.copy($scope.paginationOptions);

	$scope.info={auditorId:"",eTime:moment(new Date().getTime()).format('YYYY-MM-DD') +' 00:00:00',
			sTime:moment(new Date().getTime()-30*24*60*60*1000).format('YYYY-MM-DD') +' 00:00:00',num:""};
	$scope.userNames=[]
	
	//平台审核人
	$http.get('merchantInfo/selectAllUserBox')
	.success(function(largeLoad) {
		$scope.userNames=largeLoad;
		$scope.userNames.splice(0,0,{id:"",userName:"全部"});
	});
	$scope.resetForm=function(){
		$scope.info={auditorId:"",eTime:moment(new Date().getTime()).format('YYYY-MM-DD') +' 00:00:00',
				sTime:moment(new Date().getTime()-30*24*60*60*1000).format('YYYY-MM-DD') +' 00:00:00',num:""};
	};
	$scope.infoBack=[];

	//模糊查询
	$scope.selectInfos=function(){
		if($scope.info.sTime>$scope.info.eTime){
			$scope.notice("起始时间不能大于结束时间");
			return;
		}
		$scope.infoBack=angular.copy($scope.info);
		$http.post(
			'auditorManager/selectAllRecord.do',
			 "info="+angular.toJson($scope.info)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
			 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
		).success(function(result){
			//响应成功
			if(result.status){
				$scope.gridOptions.data = result.page.result; 
				$scope.gridOptions.totalItems = result.page.totalCount;
			}else{
				$scope.notice(result.msg);
			}
		});
	};

	$scope.gridOptions={                           //配置表格
		enableSorting: true,
		paginationPageSize:10,                  //分页数量
		paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
		useExternalPagination: true,  
		columnDefs:[                           //表格数据
			{ field: 'id',displayName:'审核人员ID',width:180},
			{ field: 'userName',displayName:'审核人员',width:180},
			{ field: 'allCount',displayName:'审核数量',width:180},
			{ field: 'successNum',displayName:'审核成功',width:180 },
			{ field: 'successNum3',displayName:'企业成功',width:180 },
			{ field: 'successNum2',displayName:'个体成功',width:180 },
			{ field: 'successNum1',displayName:'小微成功',width:180 },

			{ field: 'failureNum',displayName:'审核失败',width:180 },
			{ field: 'failureNum3',displayName:'企业失败',width:180 },
			{ field: 'failureNum2',displayName:'个体失败',width:180 },
			{ field: 'failureNum1',displayName:'小微失败',width:180 },

			{ field: 'notAudited',displayName:'待审核',width:180 },
			{ field: 'notAudited3',displayName:'企业待审',width:180 },
			{ field: 'notAudited2',displayName:'个体待审',width:180 },
			{ field: 'notAudited1',displayName:'小微待审',width:180 },
			{ field: 'id',displayName:'操作',width:180,pinnedRight:true,
				cellTemplate:'<div  class="lh30"><a ng-show="grid.appScope.hasPermit(\'auditorRecord.detail1\')" ng-click="grid.appScope.auditorDetail1(row.entity)">详情1</a>  '
					+'<a ng-show="grid.appScope.hasPermit(\'auditorRecord.detail2\')"  ng-click="grid.appScope.auditorDetail2(row.entity)"> | 详情2</a>'
				+'<a ng-show="grid.appScope.hasPermit(\'auditorRecord.export\')"  ng-click="grid.appScope.exportInfo(row.entity)"> | 导出</a>' +
				'</div>'

			}
		],	
		onRegisterApi: function(gridApi) {                
			$scope.gridApi = gridApi;
			gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
				$scope.paginationOptions.pageNo = newPage;
				$scope.paginationOptions.pageSize = pageSize;
				$scope.selectInfos();
			});
		}
	};

	//导出信息//打开导出终端模板
	$scope.exportInfo=function(entity){
		$scope.addInfoBack=angular.copy($scope.infoBack);
		$scope.addInfoBack.auditorId=entity.id;
		var titleStr="确认导出"+entity.userName+"详情2列表?";
		SweetAlert.swal({
			title: titleStr,
			showCancelButton: true,
			confirmButtonColor: "#DD6B55",
			confirmButtonText: "提交",
			cancelButtonText: "取消",
			closeOnConfirm: true,
			closeOnCancel: true 
		},
		function (isConfirm) {
			if (isConfirm) {
				$scope.exportInfoClick("auditorManager/exportInfo.do",{"info":angular.toJson($scope.addInfoBack)});
			}
		});
	}

	//详情1
	$scope.auditorDetail1=function(entity){
		var modalScope = $scope.$new();
		modalScope.num=1;
		modalScope.name=entity.userName;
		modalScope.id=entity.id;
		modalScope.info=angular.copy($scope.infoBack);
		var modalInstance = $uibModal.open({
           templateUrl : 'views/merchant/auditorRecordDetail.html',  //指向上面创建的视图
           controller : 'auditorRecordDetailCtrl',// 初始化模态范围
           scope:modalScope,
           size:'lg'
        })
        modalScope.modalInstance=modalInstance;
        modalInstance.result.then(function(selectedItem){ 
        },function(){
           $log.info('取消: ' + new Date())
        })
	}
	
	//详情2
	$scope.auditorDetail2=function(entity){
		var modalScope = $scope.$new();
		modalScope.num=2;
		modalScope.name=entity.userName;
		modalScope.id=entity.id;
		modalScope.info=angular.copy($scope.infoBack);
		var modalInstance = $uibModal.open({
           templateUrl : 'views/merchant/auditorRecordDetail.html',  //指向上面创建的视图
           controller : 'auditorRecordDetailCtrl2',// 初始化模态范围
           scope:modalScope,
           size:'lg'
        })
        modalScope.modalInstance=modalInstance;
        modalInstance.result.then(function(selectedItem){ 
        },function(){
           $log.info('取消: ' + new Date())
        })
	}

	//页面绑定回车事件
	$document.bind("keypress", function(event) {
		$scope.$apply(function (){
			if(event.keyCode == 13){
				$scope.selectInfos();
			}
		})
	});
	 
});

//详情1
angular.module('inspinia').controller('auditorRecordDetailCtrl',function($scope,$http,$state,$stateParams,i18nService){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	$scope.paginationOptions=angular.copy($scope.paginationOptions);

	$scope.solutionModalClose=function(){
		$scope.modalInstance.dismiss();
	}
	 
    $scope.solutionModalOk=function(){
    	$scope.modalInstance.close($scope);
	}
    $scope.select=function(){
		$scope.info.auditorId=$scope.id;
		$http.post('auditorManager/selectAllRecordDetail.do',
			"info="+angular.toJson($scope.info)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
			{headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
    	).success(function(data){			//响应成功
    		$scope.gridOptions.data = data.page.result;
    		$scope.gridOptions.totalItems = data.page.totalCount;
    	});
    }
    $scope.select();
	$scope.gridOptions={                           //配置表格
		paginationPageSize:10,                  //分页数量
		paginationPageSizes: [10,20,50,100],	  //切换每页记录数
		  useExternalPagination: true,  
	      columnDefs:[
	      	  { field: 'bpId',displayName:'业务产品ID',width:150},
	          { field: 'bpName',displayName:'业务产品',width:150},
			  { field: 'allCount',displayName:'审核数量',width:180},
			  { field: 'successNum',displayName:'审核成功',width:180 },
			  { field: 'successNum3',displayName:'企业成功',width:180 },
			  { field: 'successNum2',displayName:'个体成功',width:180 },
			  { field: 'successNum1',displayName:'小微成功',width:180 },

			  { field: 'failureNum',displayName:'审核失败',width:180 },
			  { field: 'failureNum3',displayName:'企业失败',width:180 },
			  { field: 'failureNum2',displayName:'个体失败',width:180 },
			  { field: 'failureNum1',displayName:'小微失败',width:180 },

			  { field: 'notAudited',displayName:'待审核',width:180 },
			  { field: 'notAudited3',displayName:'企业待审',width:180 },
			  { field: 'notAudited2',displayName:'个体待审',width:180 },
			  { field: 'notAudited1',displayName:'小微待审',width:180 },
	      ],	
	      onRegisterApi: function(gridApi) {                
	          $scope.gridApi = gridApi;
	          gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
	          	$scope.paginationOptions.pageNo = newPage;
	          	$scope.paginationOptions.pageSize = pageSize;
	          	$scope.select();
	          });
	      }
	};
	
});

 //详情2
angular.module('inspinia').controller('auditorRecordDetailCtrl2',function($scope,$stateParams,$http,i18nService){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	$scope.paginationOptions=angular.copy($scope.paginationOptions);

	$scope.solutionModalClose=function(){
		$scope.modalInstance.dismiss();
	}
	 
	$scope.solutionModalOk=function(){
		$scope.modalInstance.close($scope);
	}
    $scope.select=function(){
		$scope.info.auditorId=$scope.id;
    	$http.post('auditorManager/selectAllRecordDetail2.do',
			"info="+angular.toJson($scope.info)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
			{headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
    	).success(function(data){		//响应成功
			$scope.gridOptions2.data = data.page.result;
			$scope.gridOptions2.totalItems = data.page.totalCount;
    	});
    }
    $scope.select();
	$scope.gridOptions2={                           //配置表格
		paginationPageSize:10,                  //分页数量
		paginationPageSizes: [10,20,50,100],	  //切换每页记录数
		  useExternalPagination: true,  
	      columnDefs:[                           //表格数据
			  { field: 'itemNo',displayName:'商户进件编号',width:150 },
	          { field: 'agentName',displayName:'所属代理商',width:150 },
	          { field: 'merchantName',displayName:'商户简称',width:150},
	          { field: 'createTime',displayName:'录入时间',width:150,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"' },
	          { field: 'auditorStatus',displayName:'审核状态',width:150,
	        	 cellFilter:"formatDropping:[{text:'审核成功',value:1},{text:'审核失败',value:2},{text:'复审退件',value:3}]"
	          },
	          { field: 'describes',displayName:'描述',width:150 },
	          { field: 'auditorTime',displayName:'审核时间',width:150,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"' }
	      ],	
	      onRegisterApi: function(gridApi) {                
	          $scope.gridApi = gridApi;
	          gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
	          	$scope.paginationOptions.pageNo = newPage;
	          	$scope.paginationOptions.pageSize = pageSize;
	          	$scope.select();
	          });
	      }
	};
	 
	 
});