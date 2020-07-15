angular.module('inspinia').controller("updateServiceRateCtrl", function($scope, $http, $state, $stateParams, i18nService,SweetAlert){
	i18nService.setCurrentLang('zh-cn');
	
	$scope.rateTypes = [{text:"每笔固定金额",value:1},{text:"每笔扣率",value:2},{text:"每笔扣率带保底封顶",value:3},{text:"每笔扣率+每笔固定金额",value:4},{text:"单笔阶梯扣率",value:5},{text:"每月累计交易量阶梯",value:6}];
	$scope.boolAll = [{text:'是',value:1},{text:'否',value:2}];
//	$scope.bankCardTypes = [{text:'不限',value:0},{text:'储蓄卡',value:1},{text:'信用卡',value:2}];
	
	$scope.search = function () {
		$http.post('groupService/listEffectiveServiceRate.do',
	  		 angular.toJson({id:$stateParams.id})
	    ).success(function(res){
	       $scope.serviceRateInfos = res.data;
	    }).error(function(){
	    });
	}
	$scope.search();
	
	$scope.serviceRateGrid = {
			data:"serviceRateInfos",
	        columnDefs: [
                {field: 'id',displayName: 'ID',width: 150,pinnable: false,sortable: false},
	            {field: 'cardRateType',displayName: '银行卡种类',width: 150,pinnable: false,sortable: false,
	              	 cellFilter:"formatDropping:"+$scope.cardTypeStr
	            },
	            {field: 'rateType',displayName: '费率方式',width: 150,pinnable: false,sortable: false,
	            	 cellFilter:"formatDropping:"+angular.toJson($scope.rateTypes)
	            },
	            {field: 'serviceRate',displayName: '费率',width: 150,pinnable: false,sortable: false},
	            {field: 'effectiveDate',displayName: '生效日期',width: 150,pinnable: false,sortable: false,
	            	cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'
	            },
	            {field: 'effectiveStatus',displayName: '当前是否生效',width: 180,pinnable: false,sortable: false,
	            	 cellFilter:"formatDropping:"+angular.toJson($scope.boolAll)
	            },
                {name: 'action',displayName: '操作',width: 150,pinnable: false,sortable: false,editable:true,cellTemplate:
            	"<a ng-click='grid.appScope.openUpdate(row.entity)' class='btn'>修改</a>"}
	        ]
	};
	
	$scope.openUpdate = function(record) {
		$('#updateModal').modal('show');
		$scope.updateInfo = record;
		$scope.newInfo = angular.copy($scope.updateInfo);
		$scope.newInfo.rateType = 1;
		$scope.newInfo.serviceRate = 0;
		$scope.newInfo.effectiveDate = new Date();
		$scope.query();
	};
	
	$scope.query = function () {
		$http.post('groupService/listServiceRateLog.do',
	  		 angular.toJson({"rateId":$scope.updateInfo.id,"cardType":$scope.updateInfo.cardRateType})
	    ).success(function(res){
	       $scope.serviceRateLogs = res.data;
	    }).error(function(){
	    });
	}
	
	//当模态框隐藏时触发
	$('#updateModal').on('hide.bs.modal', function () {
		$scope.newInfo = {};
	});
	
	$scope.serviceRateLogGrid = {
		data:"serviceRateLogs",
		enableHorizontalScrollbar: 0,        //去掉滚动条
	    enableVerticalScrollbar : 0, 
        columnDefs: [
            {field: 'id',displayName: 'ID',width: 80,pinnable: false,sortable: false},
            {field: 'cardRateType',displayName: '银行卡种类',width: 120,pinnable: false,sortable: false,
              	 cellFilter:"formatDropping:"+$scope.cardTypeStr
            },
            {field: 'rateType',displayName: '费率方式',width: 150,pinnable: false,sortable: false,
            	 cellFilter:"formatDropping:"+angular.toJson($scope.rateTypes)
            },
            {field: 'serviceRate',displayName: '费率',width: 130,pinnable: false,sortable: false},
            {field: 'effectiveDate',displayName: '生效日期',width: 150,pinnable: false,sortable: false,
            	cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'
            },
            {field: 'effectiveStatus',displayName: '当前是否生效',width: 130,pinnable: false,sortable: false,
            	 cellFilter:"formatDropping:"+angular.toJson($scope.boolAll)
            },
            {name: 'action',displayName: '操作',width: 120,pinnable: false,sortable: false,editable:true,cellTemplate:
        	"<a ng-if='row.entity.effectiveDate>" + new Date().getTime() +"' class='btn' ng-click='grid.appScope.del(row.entity)'>删除</a>"}
        ]
	};
	$scope.del=function(record){
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
	            	$http.post('groupService/deleteServiceRate.do',
	           	  		 angular.toJson({"id":record.id})
	           	    ).success(function(msg){
	                       if(msg.status)
	                       	$scope.query();
	           			$scope.notice(msg.msg);
	           	    }).error(function(){
	           	    });
	            }
        });
    };
	
	$scope.update = function (){
		$scope.submitting = true;
		var effectiveDate = $scope.newInfo.effectiveDate;
	    var nowDate = new Date();
		var nowMilliseconds =  nowDate.getTime();
		if(effectiveDate <nowMilliseconds){
	    	$scope.notice("生效时间必须大于等于当前时间！");
			$scope.submitting = false;
	    	return;
	    }
		$scope.newInfo.acqServiceRateId = $scope.updateInfo.id;
		delete $scope.newInfo.effectiveStatus;
		$http.post('groupService/updateServiceRate.do',
	  		 angular.toJson($scope.newInfo)
	    ).success(function(msg){
            if(msg.status){
            	$('#updateModal').modal('hide');
            	$scope.search();
            }
			$scope.notice(msg.msg);
			$scope.submitting = false;
	    }).error(function(){
			$scope.submitting = false;
	    });
	}
	
});