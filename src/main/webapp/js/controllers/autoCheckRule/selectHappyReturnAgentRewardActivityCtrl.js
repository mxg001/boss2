angular.module('inspinia',['uiSwitch']).controller('selectHappyReturnAgentRewardActivityCtrl',function(i18nService,$scope,$http,$state,$stateParams,$compile,$filter,SweetAlert){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文


	$scope.activityId = $stateParams.activityId;

	$scope.info={agentNo:"",agentName:"",teamId:"",activityId:""};
	$scope.info.activityId = $scope.activityId;//添加查询条件
	
	$scope.clear=function(){
		$scope.info={agentNo:"",agentName:""};
	}
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	
	//根据agentNo查询代理商
	$scope.selectByParam=function(){
        $http({
            url: 'activity/selectHappyReturnAgentActivity?pageNo=' + $scope.paginationOptions.pageNo + "&pageSize=" + $scope.paginationOptions.pageSize,
            data: $scope.info,
            method: 'POST'
        }).success(function (data) {
			//响应成功]
			$scope.gridOptions.data = data.page.result;
			$scope.gridOptions.totalItems = data.page.totalCount;
		});
	}

	var rowList={};
	var num=0;
	$scope.selectByParam();
	$scope.gridOptions = {
//			data: 'switchSetData',
			paginationPageSize: 10,
			paginationPageSizes: [10, 20, 50, 100],
			useExternalPagination: true,		  	//开启拓展名
			enableHorizontalScrollbar: 1,        //横向滚动条
			enableVerticalScrollbar : 1,  		//纵向滚动条
			columnDefs: [
	            {field: 'agentNo', displayName: '代理商编号'},
	            {field: 'agentName', displayName: '代理商名称'},
	            {field: 'id', displayName: '操作',cellTemplate:
	            	'<a class="lh30" ng-show="grid.appScope.hasPermit(\'activity.deleteHappyReturnAgentActivity\')" ng-click="grid.appScope.deleteByAgentNo(row.entity.id)">删除</a></div>'
		        }
	        ],
	        onRegisterApi: function(gridApi) {
		          $scope.gridApi = gridApi;
		          //全选
			      $scope.gridApi.selection.on.rowSelectionChangedBatch($scope,function (rows) {
			            if(rows[0].isSelected){
			               $scope.testRow = rows[0].entity;
			               for(var i=0;i<rows.length;i++){
			            	   rowList[rows[i].entity.agentNo]=rows[i].entity;
			            	   num++;
			               }
			            }else{
			            	rowList={};
			            	num=0;
			            }
			         })
			         //单选
			         $scope.gridApi.selection.on.rowSelectionChanged($scope,function (row) {
			            if(row.isSelected){
			               $scope.testRow = row.entity;
			               rowList[row.entity.agentNo]=row.entity;
			               num++;
			            }else{
			            	delete rowList[row.entity.agentNo];
			            	num--;
			            	if(num<0){
			            		num=0;
			            	}
			            }
			         })
		          gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
		          	$scope.paginationOptions.pageNo = newPage;
		          	$scope.paginationOptions.pageSize = pageSize;
		             $scope.selectByParam();
		          });
		      }

		};
	 //删除单个
	 $scope.deleteByAgentNo=function(id){
	        SweetAlert.swal({
	            title: "确认删除？",
//	            text: "",
	            type: "warning",
	            showCancelButton: true,
	            confirmButtonColor: "#DD6B55",
	            confirmButtonText: "提交",
	            cancelButtonText: "取消",
	            closeOnConfirm: true,
	            closeOnCancel: true },
		        function (isConfirm) {
		            if (isConfirm) {
		            	$http.post('activity/deleteHappyReturnAgentActivity',id)
		   	   		 	.success(function(datas){
		            		$scope.notice(datas.msg);
		    				$scope.selectByParam();
		    			}).error(function(){
		    				$socpe.notice("删除失败");
		    			})
		            }
	        });
	    };
});