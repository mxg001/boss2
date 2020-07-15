angular.module('inspinia').controller("updateRateCtrl", function($scope, $http, $state, $stateParams, i18nService,SweetAlert,$uibModal,$log){
	
	i18nService.setCurrentLang('zh-cn');
	$scope.rateTypes = [{text:"每笔固定金额",value:1},{text:"每笔扣率",value:2},{text:"每笔扣率带保底封顶",value:3},{text:"每笔扣率+每笔固定金额",value:4},{text:"每月日均累计交易量阶梯扣率带保底",value:5}];
	$scope.costRateTypes = [{text:"每笔固定金额",value:1},{text:"每笔扣率",value:2},{text:"每笔扣率带保底封顶",value:3},{text:"每笔扣率+每笔固定金额",value:4},{text:"每月日均累计交易量阶梯扣率带保底",value:5}];
	$scope.bool = [{text:"是",value:1},{text:"否",value:2}];
	
	$scope.serviceType = $stateParams.serviceType;
	
	$http.post('outAccountService/getServiceDetail.do',
  		angular.toJson({serviceId:$stateParams.id})
    ).success(function(res){
    	$scope.serviceBaseInfo = res.serviceBaseInfo;
    	$scope.agentServiceRateData = res.agentServiceRateList;
    	$scope.costServiceRateData = res.costServiceRateList;
    }).error(function(){
    }); 
	
	$scope.agentServiceRateGrid = {
		data:"agentServiceRateData",
        columnDefs: [
            {field: 'id',displayName: 'ID',width: 150,pinnable: false,sortable: false},
            {field: 'agentRateType',displayName: '费率方式',width: 150,pinnable: false,sortable: false,
            	 cellFilter:"formatDropping:"+angular.toJson($scope.rateTypes)
            },
            {field: 'serviceRate',displayName: '费率',width: 150,pinnable: false,sortable: false},
            {field: 'effectiveDate',displayName: '生效日期',width: 150,pinnable: false,sortable: false,
            	cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'
            },
            {field: 'effectiveStatus',displayName: '当前是否生效',width: 180,pinnable: false,sortable: false,
            	 cellFilter:"formatDropping:"+angular.toJson($scope.bool)
            },
            {name: 'action',displayName: '操作',width: 150,pinnable: false,sortable: false,editable:true,cellTemplate:
        	"<a ng-click='grid.appScope.openUpdate(row.entity)' class='btn'>修改</a>"}
        ]
	};
	
	$scope.costServiceRateGrid = {
		data:"costServiceRateData",
        columnDefs: [
            {field: 'id',displayName: 'ID',width: 150,pinnable: false,sortable: false},
            {field: 'costRateType',displayName: '费率方式',width: 150,pinnable: false,sortable: false,
            	 cellFilter:"formatDropping:"+angular.toJson($scope.costRateTypes)
            },
            {field: 'serviceRate',displayName: '费率',width: 150,pinnable: false,sortable: false},
            {field: 'effectiveDate',displayName: '生效日期',width: 150,pinnable: false,sortable: false,
            	cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'
            },
            {field: 'effectiveStatus',displayName: '当前是否生效',width: 180,pinnable: false,sortable: false,
            	 cellFilter:"formatDropping:"+angular.toJson($scope.bool)
            },
            {name: 'action',displayName: '操作',width: 150,pinnable: false,sortable: false,editable:true,cellTemplate:
        	"<a ng-click='grid.appScope.openUpdate2(row.entity)' class='btn'>修改</a>"}
        ]
	};
	
	$scope.openUpdate = function(record) {
		$('#updateModal').modal('show');
		$scope.updateInfo = angular.copy(record);
		$scope.newInfo = {agentRateType:1};
		$scope.query(1,record.id);
	};
	$scope.openUpdate2 = function(record) {
		$('#updateModal2').modal('show');
		$scope.updateInfo = angular.copy(record);
		$scope.newInfo = {costRateType:1};
		$scope.query(2,record.id);
	};
	
	$scope.query = function (oper, serviceRateId) {
		$http.post('outAccountService/queryServiceRateLog.do',
	  		angular.toJson({serviceRateId:serviceRateId})
	    ).success(function(data){
	    	if(oper==1){
	    		var agentServiceRate = angular.copy($scope.updateInfo);
	    		$scope.serviceRateLogs = [agentServiceRate];
	    		for(var i=0; i<data.length; i++){
	    			$scope.serviceRateLogs.push(data[i]);
	    		}
	    	}else if(oper==2){
	    		var costServiceRate = angular.copy($scope.updateInfo);
	    		$scope.costServiceRateLogs = [costServiceRate];
	    		for(var i=0; i<data.length; i++){
	    			$scope.costServiceRateLogs.push(data[i]);
	    		}
	    	}
	    }).error(function(){
	    }); 
	}
	
	//当模态框隐藏时触发
	$('#updateModal').on('hide.bs.modal', function () {
		$scope.updateInfo = "";
	});
	
	$scope.serviceRateLogsGrid = {
		data:"serviceRateLogs",
		enableHorizontalScrollbar: 0,        //去掉滚动条
	    enableVerticalScrollbar : 0, 
        columnDefs: [
            {field: 'id',displayName: 'ID',width: 100,pinnable: false,sortable: false},
            {field: 'agentRateType',displayName: '费率方式',width: 150,pinnable: false,sortable: false,
            	 cellFilter:"formatDropping:"+angular.toJson($scope.rateTypes)
            },
            {field: 'serviceRate',displayName: '费率',width: 120,pinnable: false,sortable: false},
            {field: 'effectiveDate',displayName: '生效日期',width: 150,pinnable: false,sortable: false,
            	cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'
            },
            {field: 'effectiveStatus',displayName: '当前是否生效',width: 180,pinnable: false,sortable: false,
            	 cellFilter:"formatDropping:"+angular.toJson($scope.bool)
            },
            {name: 'action',displayName: '操作',width: 150,pinnable: false,sortable: false,editable:true,cellTemplate:
        	"<a ng-if='row.entity.effectiveDate>" + new Date().getTime() +"' class='btn' ng-click='grid.appScope.del(row.entity.id,1)'>删除</a>"}
        ]
	};
	if($scope.serviceType==2){
		$scope.costServiceRateLogsGrid = {
			data:"costServiceRateLogs",
			enableHorizontalScrollbar: 0,        //去掉滚动条
		    enableVerticalScrollbar : 0, 
	        columnDefs: [
	            {field: 'id',displayName: 'ID',width: 100,pinnable: false,sortable: false},
	            {field: 'costRateType',displayName: '费率方式',width: 150,pinnable: false,sortable: false,
	            	 cellFilter:"formatDropping:"+angular.toJson($scope.costRateTypes)
	            },
	            {field: 'serviceRate',displayName: '费率',width: 120,pinnable: false,sortable: false},
	            {field: 'effectiveDate',displayName: '生效日期',width: 150,pinnable: false,sortable: false,
	            	cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'
	            },
	            {field: 'effectiveStatus',displayName: '当前是否生效',width: 180,pinnable: false,sortable: false,
	            	 cellFilter:"formatDropping:"+angular.toJson($scope.bool)
	            },
	            {name: 'action',displayName: '操作',width: 150,pinnable: false,sortable: false,editable:true,cellTemplate:
	        	"<a ng-if='row.entity.effectiveDate>" + new Date().getTime() +"' ng-show='grid.appScope.hasPermit(\"managerService.delete\")' class='btn' ng-click='grid.appScope.del(row.entity.id,2)'>删除</a>"}
	        ]
		};
	}
	
	$scope.del=function(id, oper){
        SweetAlert.swal({
            title: "确认删除？",
//            text: "",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "提交",
            cancelButtonText: "取消",
            closeOnConfirm: true,
            closeOnCancel: true },
	        function (isConfirm) {
	            if (isConfirm) {
	            	$http.post('outAccountService/deleteServiceRateTask.do',
	           	  		 angular.toJson({"id":id})
	           	    ).success(function(msg){
	                       if(msg.status)
	                       	$scope.query(oper,$scope.updateInfo.id);
	           			$scope.notice(msg.msg);
	           	    }).error(function(){
	           	    });
	            }
        });
    };
//	$scope.del = function (id, oper){
//		$http.post('outAccountService/deleteServiceRateTask.do',
//	  		 angular.toJson({"id":id})
//	    ).success(function(msg){
//            if(msg.status)
//            	$scope.query(oper,$scope.updateInfo.id);
//			$scope.notice(msg.msg);
//	    }).error(function(){
//	    });
//	};
	$scope.info={m1:"0",m13:"无穷大"};
    $scope.bijiao=function(num1,num2){
		 if(isNaN(num1)||isNaN(num2)){
			 $scope.submitting = true;
			 $scope.notice("请输入正确的数值");
			 return;
		 }
		 if(parseFloat(num1)<0||parseFloat(num2)<0){
			 $scope.submitting = true;
			 $scope.notice("请输入正确的数值");
			 return;
		 }
		 if(parseFloat(num1)>=parseFloat(num2)){
			 $scope.submitting = true;
			 $scope.notice("后一个数值必须大于前一个数值");
			 return;
		 }
		 $scope.submitting = false;
	 }
	 
	 $scope.weikong=function(shu){
		 if(isNaN(shu)){
			 $scope.submitting = true;
			 $scope.notice("请输入正确的数值");
			 return;
		 }
		 if(parseFloat(shu)<0){
			 $scope.submitting = true;
			 $scope.notice("请输入正确的数值");
			 return;
		 }
		 $scope.submitting = false;
	 }
    
	$scope.update = function (oper){
		$scope.submitting = true;
		var effectiveDate = $scope.newInfo.effectiveDate;
		var nowDate = new Date();
		var nowMilliseconds =  nowDate.getTime()-nowDate.getHours()*3600000-nowDate.getMinutes()*60000-nowDate.getSeconds()*1000-nowDate.getMilliseconds();
		if(effectiveDate <nowMilliseconds){
	    	$scope.notice("生效日期必须大于等于当前日期！");
			$scope.submitting = false;
	    	return;
	    }
		$scope.newInfo.outAccountServiceRateId = $scope.updateInfo.id;
		$http.post('outAccountService/addServiceRateTask.do',
	  		angular.toJson($scope.newInfo)
	    ).success(function(msg){
			$scope.submitting = false;
	    	if(msg.status){
	    		if(oper==1){
	    			$('#updateModal').modal('hide');
					$scope.submitting = false;
	    		}else if(oper==2){
	    			$('#updateModal2').modal('hide');
					$scope.submitting = false;
	    		}
	    	}
			$scope.notice(msg.msg);
	    }).error(function(){
			$scope.submitting = false;
	    }); 
	}
	
});
