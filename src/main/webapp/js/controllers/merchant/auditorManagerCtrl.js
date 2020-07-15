/**
 * 审核人员管理
 */
angular.module('inspinia',['uiSwitch']).controller('auditorManagerCtrl',function($scope,$http,$state,$stateParams,i18nService,SweetAlert,$document){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	//清空
	$scope.resetForm = function(){
		$scope.baseInfo={auditorId:-1,status:2,bpId:"-1"};
	}
	$scope.resetForm();
	
	//查询出拥有商户审核权限的人员
	$http.get('user/selectUserByMenuCode.do?menuCode='+"merchant.auditMer").success(function(msg){
		if(msg!=null){
			msg.unshift({"id":-1,"userName":"全部"});
			$scope.auditorList = msg;
		}
	});
	//查询出所有的业务产品
	$http.get('businessProductDefine/selectBpTeam.do').success(function(msg){
		if(msg!=null){
			msg.unshift({"bpId":"-1","bpName":"全部"});
			$scope.bpList = msg;
		}
	});
	
	$scope.auditorTable = {
			data: 'auditorData',
			paginationPageSize: 10,
			paginationPageSizes: [10, 20, 50, 100],
			useExternalPagination: true,		  	//开启拓展名
			columnDefs: [
	            {field: 'userName', displayName: '审核人'},
	            {field: 'bpName', displayName: '业务产品'},
	            {field: 'status', displayName: '状态',cellTemplate:
	            	'<span ng-show="grid.appScope.hasPermit(\'auditorManager.switch\')"><switch class="switch switch-s" ng-model="row.entity.status" ng-change="grid.appScope.open(row)" /></span>'
	            	+'<span ng-show="!grid.appScope.hasPermit(\'auditorManager.switch\')"> <span ng-show="row.entity.status==1">开启</span><span ng-show="row.entity.status==0">关闭</span></span>'
	            },
	            {field: 'options', displayName: '操作',pinnedRight:true, cellTemplate:
	               '<a class="lh30" ng-show="grid.appScope.hasPermit(\'auditorManager.delete\')" ng-click="grid.appScope.deleteData(row.entity.id)">删除</a></div>'
	            }
	        ],
	        onRegisterApi: function(gridApi){
	        	$scope.gridApi = gridApi;
	        	$scope.gridApi.pagination.on.paginationChanged($scope, function(newPage, pageSize){
	        		$scope.paginationOptions.pageNo = newPage;
	        		$scope.paginationOptions.pageSize = pageSize;
	        		$scope.query();
	        	});
	        }
		};
		
		//查询
		$scope.query = function(){
			$http.post('auditorManager/selectByCondition.do',"baseInfo="+angular.toJson($scope.baseInfo)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+
				$scope.paginationOptions.pageSize,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
					.success(function(page){
						if(!page){
							return;
						}
						$scope.auditorData = page.result;
						$scope.auditorTable.totalItems = page.totalCount;
					}).error(function(){
					});
		}
		$scope.query();
		
		$scope.open=function(row){
			if(row.entity.status){
				$scope.serviceText = "确定开启？";
			} else {
				$scope.serviceText = "确定关闭？";
			}
	        SweetAlert.swal({
	            title: $scope.serviceText,
//	            text: "服务状态为关闭后，不能正常交易!",
	            type: "warning",
	            showCancelButton: true,
	            confirmButtonColor: "#DD6B55",
	            confirmButtonText: "提交",
	            cancelButtonText: "取消",
	            closeOnConfirm: true,
	            closeOnCancel: true },
		        function (isConfirm) {
		            if (isConfirm) {
		            	if(row.entity.status==true){
		            		row.entity.status=1;
		            	} else if(row.entity.status==false){
		            		row.entity.status=0;
		            	}
		            	var data={"status":row.entity.status,"id":row.entity.id};
		                $http.post("auditorManager/updateStatus.do",angular.toJson(data))
		            	.success(function(data){
		            		if(data.status){
		            			$scope.notice("操作成功！");
		            		}else{
		            			if(row.entity.status==true){
		    	            		row.entity.status = false;
		    	            	} else {
		    	            		row.entity.status = true;
		    	            	}
		            			$scope.notice("操作失败！");
		            		}
		            	})
		            	.error(function(data){
		            		if(row.entity.status==true){
	    	            		row.entity.status = false;
	    	            	} else {
	    	            		row.entity.status = true;
	    	            	}
		            		$scope.notice("服务器异常")
		            	});
		            } else {
		            	if(row.entity.status==true){
		            		row.entity.status = false;
		            	} else {
		            		row.entity.status = true;
		            	}
		            }
	        });
	    	
	    };

    $scope.deleteData=function(id){
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
                    $http.get("auditorManager/deleteData.do?id="+id)
                        .success(function(msg){
                            $scope.notice(msg.msg);
                            $scope.query();
                        }).error(function(){
                    });
                }
            });
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