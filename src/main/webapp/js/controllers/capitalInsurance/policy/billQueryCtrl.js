angular.module('inspinia', ['infinity.angular-chosen']).controller('billQueryCtrl',function($scope,$http,$state,$stateParams,$compile,$uibModal,$timeout,$log,i18nService,SweetAlert,$document){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    //订单类型
    $scope.orderTypeList = [{text:"全部",value:"0"},{text:"投保订单",value:"1"},{text:"退保订单",value:"2"}];
    $scope.orderTypeStr = angular.toJson($scope.orderTypeList);
    
    // 对账结果
    $scope.checkStatusList = [{text:"全部",value:"0"},{text:"对账成功",value:"1"},{text:"对账失败",value:"2"}];
    $scope.checkStatusStr = angular.toJson($scope.checkStatusList);
    
    $scope.insurerStr = angular.toJson($scope.insurerList);
    /**
     * 清空
     */
    $scope.clear=function(){
        $scope.info={
        		batchNo:"",
        		insurer:"0",
        		orderType:"0",
        		createTime:moment(new Date().getTime()-24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
        		createTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD'+' 23:59:59'),
        		checkStatus:"0"
        }
    };
    $scope.clear();
    /**
     * 
     */
    $scope.billGrid={                           //配置表格
            paginationPageSize:10,                  //分页数量
            paginationPageSizes: [10,20,50,100],	//切换每页记录数
            useExternalPagination: true,		    //开启拓展名
            enableHorizontalScrollbar: true,        //横向滚动条
            enableVerticalScrollbar : true,  		//纵向滚动条
            columnDefs:[                           //表格数据
                { field: 'batchNo',displayName:'对账批次号',width:180 },
                { field: 'insurer',displayName:'承保单位',width:120,cellFilter:"formatDropping:" +  $scope.insurerStr},
                { field: 'orderType',displayName:'订单类型',width:120,cellFilter:"formatDropping:" +  $scope.orderTypeStr},
                { field: 'acqTotalAmount',displayName:'上游含税保费总金额',width:180 },
                { field: 'acqTotalCount',displayName:'上游对账文件总笔数',width:180 },
                { field: 'acqSuccessCount',displayName:'上游对账文件成功笔数',width:180 },
                { field: 'acqFailCount',displayName:'上游对账文件失败笔数',width:180 },
                { field: 'sysTotalAmount',displayName:'平台含税保费总金额-成本价',width:180 },
                { field: 'sysTotalCount',displayName:'平台交易总笔数',width:180 },
                { field: 'sysSuccessCount',displayName:'平台对账成功总笔数',width:180 },
                { field: 'sysFailCount',displayName:'平台对账失败总笔数',width:180 },
                { field: 'checkStatus',displayName:'对账结果',width:120,cellFilter:"formatDropping:" +  $scope.checkStatusStr},
                { field: 'fileDate',displayName:'对账文件日期',width:180},
                { field: 'checkTime',displayName:'对账时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
                { field: 'fileName',displayName:'对账文件名称',width:180},
                { field: 'createPerson',displayName:'操作员',width:180},
                { field: 'remark',displayName:'备注',width:180},
                { field: 'createTime',displayName:'创建时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
                { field: 'id',displayName:'操作',width:180,pinnedRight:true, cellTemplate:
                '<div class="lh30">'+
                '<a target="_blank" ng-show="grid.appScope.hasPermit(\'policy.billEneryQuery\')" ui-sref="policy.billEneryQuery({batchNo:row.entity.batchNo})">详情</a> ' +
                '<a target="_blank" ng-show="grid.appScope.hasPermit(\'checkBill.clean\')" ng-click="grid.appScope.cleanCheckBill(row.entity.batchNo)">清除</a> ' +
                '</div>'
                }
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
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    /**
     * 
     */
	$scope.query = function(){
		if($scope.loadImg){
				return;
		}
		var stime = new Date($scope.info.createTimeTime).getTime();
		var etime = new Date($scope.info.createTimeEnd).getTime();
		if ((etime - stime) > (90 * 24 * 60 * 60 * 1000)) {
			$scope.notice("查询时间范围不能超过90天");
			return;
		}
		$scope.loadImg = true;
		$http.post("checkBillAction/getBillAllInfo","info="+angular.toJson($scope.info)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
				{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			.success(function(msg){
				$scope.loadImg = false;
				if(msg.bols){
					$scope.billGrid.data = msg.page.result; 
					$scope.billGrid.totalItems = msg.page.totalCount;
					$scope.znum = msg.page.totalCount;
				}else{
					$scope.notice("查询出错");
				}
			}).error(function(){
				$scope.loadImg = false;
			});
	}
	/**
	 * 清除对账数据
	 */
	$scope.cleanCheckBill = function(batchNo){
		if($scope.loadImg){
				return;
		}
		SweetAlert.swal({
			title: "确认清除该批次所有对账数据？",
			type: "warning",
	        showCancelButton: true,
	        confirmButtonColor: "#DD6B55",
	        confirmButtonText: "确定",
	        cancelButtonText: "取消",
	        closeOnConfirm: true,
	        closeOnCancel: true 
	      },
		function (isConfirm) {
	    	  if (isConfirm) {
	    		  $scope.loadImg = true;
		          $http.post("checkBillAction/cleanCheckBill","batchNo="+batchNo,
		        		{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		        	   .success(function(msg){
		        		   $scope.loadImg = false;
		        		   if(msg.bols){
		        			   $scope.notice("清除成功");
		        			   $scope.query();
		        				}else{
		        					$scope.notice(msg.msg);
		        				}
		        		   	$scope.loadImg = false;
		        			}).error(function(){
		        				$scope.loadImg = false;
		        		});
		            }
	        });
	}
	
	
});

