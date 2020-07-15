/**
 * 出款服务管理
 */
angular.module('inspinia',['uiSwitch']).controller("managerServiceCtrl", function($scope, $http, $state, $stateParams, i18nService,SweetAlert,$document){
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	i18nService.setCurrentLang('zh-cn');
	
	$scope.onAndOff = [{text:"开启",value:1},{text:"关闭",value:0}];
	$scope.priorities = [{text:"A级",value:"1"},{text:"B级",value:"2"},{text:"C级",value:"3"},{text:"D级",value:"4"},{text:"E级",value:"5"}];
	$scope.updateLogDate = [];
	$scope.reset = function () {
		$scope.info = {acqOrgId:"",serviceType:""};
	}
	$scope.reset();
	
	$http.post('outAccountService/queryFunction.do'
	).success(function(data){
		$scope.functionInfo = data;
	}).error(function(){
	}); 
	$scope.openModal = function(oper){
		$('#myModal').modal('show');
		$scope.oper = oper;
		if(oper == 1){
			$('#myModal .modal-title').html('修改关闭提现预警手机号');
			$('#myModal #promptMsg').html('修改关闭提现预警手机号(如有多个，请用英文逗号,隔开)：');
			$scope.functionInfo.mobile = $scope.functionInfo.closeAdvanceMobile;
		}else if(oper == 2){
			$('#myModal .modal-title').html('修改打款预警手机号');
			$('#myModal #promptMsg').html('修改打款预警手机号(如有多个，请用英文逗号,隔开)：');
			$scope.functionInfo.mobile = $scope.functionInfo.outAccountMobile;
		}else if(oper == 3){
			$('#myModal .modal-title').html('修改跳转通道预警手机号');
			$('#myModal #promptMsg').html('修改跳转通道预警手机号(如有多个，请用英文逗号,隔开)：');
			$scope.functionInfo.mobile = $scope.functionInfo.skipChannelMobile;
		}else if(oper == 4){
			$('#myModal .modal-title').html('修改出款失败预警手机号');
			$('#myModal #promptMsg').html('修改出款失败预警手机号(如有多个，请用英文逗号,隔开,最多三个)：');
			$scope.functionInfo.mobile = $scope.functionInfo.outAccountFailure;
		}
	}
	//当模态框隐藏时触发
	$('#myModal').on('hide.bs.modal', function () {
		$scope.oper = "";
		$scope.functionInfo.mobile = "";
	});
	$scope.confirm = function(){
		var msg = $scope.functionInfo.mobile;
		if(msg!=""){
			if($scope.oper==1){
				$scope.functionInfo.closeAdvanceMobile = msg;
			}else if($scope.oper==2){
				$scope.functionInfo.outAccountMobile = msg;
			}else if($scope.oper==3){
				$scope.functionInfo.skipChannelMobile = msg;
			}else if($scope.oper==4){
				$scope.functionInfo.outAccountFailure = msg;
			}
			$scope.save();
			$('#myModal').modal('hide');
		}
	}
	$scope.save = function(){
		$scope.submitting = true;
		$http.post('outAccountService/saveFunction.do',
       		 angular.toJson($scope.functionInfo)
        ).success(function(msg){
            if(msg.status){
            	$scope.functionInfo.id = msg.data;
				$scope.submitting = false;
            }
			$scope.notice(msg.msg);
        }).error(function(){
			$scope.submitting = false;
        }); 
	}
	
	$http.post('groupService/acqOrgSelectBox.do'
	).success(function(data){
		$scope.acqOrgs = data;
		$scope.acqOrgs.splice(0,0,{"acqName":"全部","id":""});
	}).error(function(){
	}); 
	
	$scope.query = function() {
		$http.post('outAccountService/queryService.do',
       		 "info="+angular.toJson($scope.info)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
       		 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
        ).success(function(data){
            $scope.moneyServiceDate = data.result;
			$scope.moneyServiceGrid.totalItems = data.totalCount;//总记录数
        }).error(function(){
        }); 
	};
	$scope.query();
	
	$scope.moneyServiceGrid = {
	        data: 'moneyServiceDate',
	        paginationPageSize:10,                  //分页数量
	        paginationPageSizes: [10,20,50,100],	//切换每页记录数
	        useExternalPagination: true,		  //开启拓展名
//	        enableHorizontalScrollbar: 0,        //去掉滚动条
//	        enableVerticalScrollbar : 0, 
	        columnDefs: [
                {field: 'id',displayName: '出款服务ID',width: 100,pinnable: false,sortable: false},
                {field: 'serviceName',displayName: '服务名称',width: 180,pinnable: false,sortable: false},
                {field: 'acqOrgName',displayName: '收单机构',width: 200,pinnable: false,sortable: false},
                {field: 'serviceType',displayName: '服务类型',width: 200,pinnable: false,sortable: false,cellFilter:"formatDropping:"+angular.toJson($scope.moneyServiceType)},
                {field: 'outAccountStatus',displayName: '状态',width: 180,pinnable: false,sortable: false,
					cellTemplate:
						'<span ng-show="grid.appScope.hasPermit(\'managerService.switch\')"><switch class="switch switch-s" ng-model="row.entity.outAccountStatus" ng-change="grid.appScope.open(row)" /></span>'
        	            +'<span ng-show="!grid.appScope.hasPermit(\'managerService.switch\')"> <span ng-show="row.entity.outAccountStatus==1">开启</span><span ng-show="row.entity.outAccountStatus==0">关闭</span></span>'
                },
                {field: 'level',displayName: '优先级',width: 200,pinnable: false,sortable: false,cellFilter:"formatDropping:"+angular.toJson($scope.priorities)},
                {field: 'dayTotalAmount',displayName: '今日累计已出款额度(元)',width: 200,pinnable: false,sortable: false,cellFilter:"number:2"},
                {field: 'lastAmount',displayName: '剩余额度(元)',width: 200,pinnable: false,sortable: false,cellFilter:"number:2"},
                {field: 'userBalance',displayName: '上游账户余额(元)',width: 200,pinnable: false,sortable: false,cellFilter:"number:2"},
				{field: 'remark',displayName: '备注',width: 300,pinnable: false,sortable: false},
	             {name: 'action',displayName: '操作',width: 240,pinnedRight: true,sortable: false,editable:true,cellTemplate:
	            	"<a class='lh30' target='_blank' ng-show='grid.appScope.hasPermit(\"managerService.detail\")' ui-sref='money.serviceDetail({id:row.entity.id,serviceType:row.entity.serviceType})'>详情</a>" +
	            	"<a class='lh30' ng-show='grid.appScope.hasPermit(\"managerService.update\")' ui-sref='money.updateService({id:row.entity.id})'> | 修改</a>" +
	            	"<a class='lh30' ng-show='grid.appScope.hasPermit(\"managerService.updateRate\")' ui-sref='money.updateRate({id:row.entity.id,serviceType:row.entity.serviceType})'> | 修改费率</a>"}
	        ],
	        onRegisterApi: function(gridApi) {                
	            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
	            	$scope.paginationOptions.pageNo = newPage;
	            	$scope.paginationOptions.pageSize = pageSize;
	            	$scope.query();
	            });
	        }
	};
	
	$scope.open = function (id, outAccountStatus) {
		if(outAccountStatus==1){
			outAccountStatus=1;
		}else{
			outAccountStatus=0;
		}
		$http.post('outAccountService/updateServiceStatus.do',
	       		 angular.toJson({id:id,outAccountStatus:outAccountStatus})
	        ).success(function(msg){
	            if(msg.status){
	            	$scope.query();
	            }
	            $scope.notice(msg.msg);
	    }).error(function(){
	    }); 		
	}
	
	//开通\关闭
	$scope.open=function(row){
		if(row.entity.outAccountStatus){
			$scope.serviceText = "确定开启？";
		} else {
			$scope.serviceText = "确定关闭？";
		}
        SweetAlert.swal({
            title: $scope.serviceText,
//            text: "服务状态为关闭后，不能正常交易!",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "提交",
            cancelButtonText: "取消",
            closeOnConfirm: true,
            closeOnCancel: true },
	        function (isConfirm) {
	            if (isConfirm) {
	            	if(row.entity.outAccountStatus==true){
	            		row.entity.outAccountStatus=1;
	            	} else if(row.entity.outAccountStatus==false){
	            		row.entity.outAccountStatus=0;
	            	}
	            	var data={"acqStatus":row.entity.outAccountStatus,"id":row.entity.id};
	                $http.post("outAccountService/updateServiceStatus.do",angular.toJson({id:row.entity.id,outAccountStatus:row.entity.outAccountStatus}))
	            	.success(function(data){
	            		if(data.status){
	            			$scope.notice("操作成功");
	            		}else{
	            			if(row.entity.outAccountStatus==true){
	    	            		row.entity.outAccountStatus = false;
	    	            	} else {
	    	            		row.entity.outAccountStatus = true;
	    	            	}
	            			$scope.notice("操作失败");
	            		}
	            	})
	            	.error(function(data){
	            		if(row.entity.outAccountStatus==true){
    	            		row.entity.outAccountStatus = false;
    	            	} else {
    	            		row.entity.outAccountStatus = true;
    	            	}
	            		$scope.notice("服务器异常")
	            	});
	            } else {
	            	if(row.entity.outAccountStatus==true){
	            		row.entity.outAccountStatus = false;
	            	} else {
	            		row.entity.outAccountStatus = true;
	            	}
	            }
        });
    	
    };
	
	//$scope.updateLogDate =[{id:1},{id:2}];
	$scope.updateLogGrid = {
	        data: 'updateLogDate',
	        columnDefs: [
                {field: 'id',displayName: '序号',width: 100,pinnable: false,sortable: false},
                {field: 'id',displayName: '修改人',width: 120,pinnable: false,sortable: false},
                {field: 'id',displayName: '修改内容',width: 200,pinnable: false,sortable: false},
                {field: 'updateTime',displayName: '修改时间',width: 200,pinnable: false,sortable: false,cellFilter: "date:'yyyy-MM-dd HH:mm:ss'"}
	        ]
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