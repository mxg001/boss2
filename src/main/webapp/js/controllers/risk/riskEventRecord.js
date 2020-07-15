/**
 * 风控事件记录
 */							
angular.module('inspinia',['uiSwitch']).controller('riskEventRecord',function($scope, $http, $state, $stateParams, i18nService,$filter,SweetAlert,$document) {

	i18nService.setCurrentLang('zh-cn');
	$scope._handleStatus=[{text:"全部",value:-1},{text:"已处理",value:1},{text:"未处理",value:0}];
	$scope._handleResults=[{text:"全部",value:-1},{text:"安全",value:1},{text:"可疑",value:2},{text:"风险",value:3}];
	$scope.ipDecisiones=[{text:"全部",value:""},{text:"IP境内",value:"ip境内"},{text:"IP境外",value:"ip境外"}];
	$scope.gnbDecisiones=[{text:"全部",value:""},{text:"基站境内",value:"基站境内"},{text:"基站境外",value:"基站境外"}];
	$scope.lbsDecisiones=[{text:"全部",value:""},{text:"经纬度境内",value:"经纬度境内"},{text:"经纬度境外",value:"经纬度境外"}];
	
	$scope.info={rulesInstruction:"-1",handleStatus:-1,handleResults:-1,ipDecision:"",gnbDecision:"",lbsDecision:""};
	
	$scope.riskEventRecordData=[]
	//查询
	$scope.query=function(){
		if($scope.info.screateTime>$scope.info.ecreateTime){
			$scope.notice("起始时间不能大于结束时间");
			return;
		}
		if($scope.info.shandleTime>$scope.info.ehandleTime){
			$scope.notice("起始时间不能大于结束时间");
			return;
		}
		$http.post('riskEventRecordAction/queryEventRecordList',
				"info="+angular.toJson($scope.info)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
				 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		.success(function(data){
			if(!data.bols){
				$scope.notice("查询失败");
				return;
			}
			 $scope.myVar = true;
			$scope.riskEventRecordData =data.page.result; 
			$scope.blacklistGrid.totalItems = data.page.totalCount;
		})
	}
	$scope.query();
	//清空
	$scope.reset = function() {
		$scope.info={rollNo:"",merchantNo:"",merchantName:"",agentNo:"",
				rulesInstruction:"-1",rulesNo:"",handleStatus:-1,handleResults:-1,handlePerson:"",sCreateTime:"",eCreateTime:"",rollStatus:"",ipDecision:"",gnbDecision:"",lbsDecision:""}
	};
	

	$scope.paginationOptions=angular.copy($scope.paginationOptions);	
	$scope.blacklistGrid = {
			data:"riskEventRecordData",
			paginationPageSize:10,                  //分页数量
		    paginationPageSizes: [10,20,50,100],	  //切换每页记录数
		    useExternalPagination: true,		  //开启拓展名
		    columnDefs: [
	                {field: 'id',displayName: '序号',width: 90,pinnable: false,sortable: false},
	                {field: 'createTime',displayName: '创建时间',width: 150,pinnable: false,sortable: false,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
	                {field: 'rollNo',displayName: '商户编号/身份证号/银行卡号',width: 200,pinnable: false,sortable: false},
	                {field: 'merchantName',displayName: '商户名称',width: 150,pinnable: false,sortable: false},
	                {field: 'agentNo',displayName: '代理商编号',width: 150,pinnable: false,sortable: false},
	                {field: 'rulesNo',displayName: '规则编号',width: 150,pinnable: false,sortable: false},
	                {field: 'ruleDesc',displayName: '规则引擎',width: 500,pinnable: false,sortable: false},
	                {field: 'rulesInstruction',displayName: '规则指令',width: 150,pinnable: false,sortable: false,cellFilter:"formatDropping:" + angular.toJson($scope.rulesInstructions)},
                    {field: 'orderNo',displayName: '交易订单号',width: 150,pinnable: false,sortable: false},
					{field: 'ip',displayName: 'IP',width: 150,pinnable: false,sortable: false},
					{field: 'ipArea',displayName: 'IP解析地址',width: 150,pinnable: false,sortable: false},
					{field: 'decision',displayName: '位置判断结果',width: 150,pinnable: false,sortable: false},
	                {field: 'handleStatus',displayName: '处理状态',width: 150,pinnable: false,sortable: false,cellFilter:"handleStatusNodes"},
	                {field: 'handleResults',displayName: '处理结果',width: 150,pinnable: false,sortable: false,cellFilter:"handleResultsNodes"},
	                {field: 'handleTime',displayName: '处理时间',width: 150,pinnable: false,sortable: false,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
	                {field: 'handlePerson',displayName: '处理人',width: 150,pinnable: false,sortable: false},
	                {field: 'handleRemark',displayName: '处理备注',width: 150,pinnable: false,sortable: false},
		            {field: 'rollNo',displayName: '操作',width: 120,pinnedRight: true,sortable: false,editable:true,cellTemplate:
		            	'<div class="lh30">'
				        		+"<a ng-show='grid.appScope.hasPermit(\"riskEvent.handle\")'  ng-click='grid.appScope.riskEventRecordHandle(row.entity.id)'> 处理 </a>" 
				            	+"<a target='_blank' ng-show='grid.appScope.hasPermit(\"riskEvent.detail\")'  ui-sref='risk.riskEventRecordDetail({id:row.entity.id})'> | 查看 </a>"
			        	+'</div>'
		            },
		            {field: 'rollStatus',displayName: '黑名单状态',width: 120,pinnable: false,sortable: false,
	                	cellTemplate:
	                		'<span ng-show="grid.appScope.hasPermit(\'riskRule.switch\')">' +
							'<span ng-show="row.entity.rollStatus==1 || row.entity.rollStatus==0">' +
							'<span ng-show="row.entity.rulesInstruction ==6 || row.entity.rulesInstruction ==7" class="lh30">无</span>' +
							'<span ng-show="row.entity.rulesInstruction !=6 && row.entity.rulesInstruction !=7" class="lh30"><switch class="switch switch-s" ng-model="row.entity.rollStatus" ng-change="grid.appScope.open(row)" /></span>' +
							'</span>' +
							'<span ng-show="row.entity.rollStatus!=1&&row.entity.rollStatus!=0" class="lh30">无</span>' +
							'</span>'
	        	            +'<span ng-show="!grid.appScope.hasPermit(\'riskRule.switch\')"> <span ng-show="row.entity.rollStatus==1">开启</span><span ng-show="row.entity.rollStatus==0">关闭</span></span>'
	                
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
	
	
	 //风控事件记录导出
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
							$scope.exportInfoClick("riskEventRecordAction/eventRecordExport.do",{"info":angular.toJson($scope.info)});
			            }
		        });
	 }
	 
	 //弹出框
	    $scope.handleResults1=[{text:"安全",value:1},{text:"可疑",value:2},{text:"风险",value:3}]
	    $scope.handleResults=1;
	    $scope.riskEventRecordHandle=function(id){
	    	$scope.id=id;
	    	$('#upHadleStatusModal').modal('show');
	    }
	    
	    $scope.updateHandleStatusColse=function(){
	    	$('#upHadleStatusModal').modal('hide');
	    }
	    
	    $scope.updateHandleStatusCommit=function(){
	    	var data={"id":$scope.id,"handleResults":$scope.handleResults,"handleRemark":$scope.handleRemark}
	    	$http.post("riskEventRecordAction/updateHandleStatus",angular.toJson(data))
			.success(function(msg){
				if(msg.status){
					$scope.notice(msg.msg);
					$('#upHadleStatusModal').modal('hide');
					$scope.query();
				}else{
					$scope.notice(msg.msg);
					$('#upHadleStatusModal').modal('hide');
					$scope.query();
				}
			}).error(function(){
			});
	    	
	    	
	    }


    $scope.handleResults=1;
    $scope.handleRemark='';
    $scope.rollNo='';
    $scope.rollType='';
    $scope.updateMyStatusCommit=function(){
        var data={"id":$scope.id,"rollStatus":$scope.handleResults,"remark":$scope.handleRemark,"rollNo":$scope.rollNo,"rollType":$scope.rollType};
        $http.post("riskRollAction/updateRollStatus",angular.toJson(data))
            .success(function(msg){
                if(msg.status){
                    $scope.notice(msg.msg);
                    $('#updateStatusModal').modal('hide');
                    $scope.query();
                }else{
                    $scope.notice(msg.msg);
                    $('#updateStatusModal').modal('hide');
                    $scope.query();
                }
            }).error(function(){
        });
    };

    $scope.updateMyStatusColse=function(){
        $('#updateStatusModal').modal('hide');
        $scope.query();
    };

       //开通 || 关闭
		$scope.open=function(row){
            var data={"rollNo":row.entity.rollNo,"rulesInstruction":row.entity.rulesInstruction};
            $http.post("riskEventRecordAction/getRollInfo",angular.toJson(data))
                .success(function(msg){
                    if(row.entity.rollStatus){
                        $scope.serviceText = "确定开启？";
                    } else {
                        $scope.serviceText = "确定关闭？";
                    }
                    $('#myTitle').html($scope.serviceText);
                    $scope.id=msg.rr.id;
                    if(row.entity.rollStatus==true){
                        $scope.handleResults=1;
                    } else if(row.entity.rollStatus==false){
                        $scope.handleResults=0;
                    }
                    $scope.handleRemark = msg.rr.remark;
                    $scope.rollNo=msg.rr.rollNo;
                    $scope.rollType=msg.rr.rollType;
                    $('#updateStatusModal').modal('show');
                }).error(function(e){

            });
			/*
            $http.post("riskEventRecordAction/updateRollStatus",angular.toJson(row.entity))
                .success(function(data){
                    if(data.bols){
                        if(row.entity.rollStatus){
                            $scope.serviceText = "确定开启？";
                        } else {
                            $scope.serviceText = "确定关闭？";
                        }
                        $('#myTitle').html($scope.serviceText);
                        $scope.id=row.entity.id;
                        if(row.entity.rollStatus==true){
                            $scope.handleResults=1;
                        } else if(row.entity.rollStatus==false){
                            $scope.handleResults=0;
                        }
                        $scope.handleRemark = row.entity.remark;
                        $scope.rollNo=row.entity.rollNo;
                        $scope.rollType=row.entity.rollType;
                        $('#updateStatusModal').modal('show');
                    }else{
                        if(row.entity.rollStatus==true){
                            row.entity.rollStatus = false;
                        } else {
                            row.entity.rollStatus = true;
                        }
                        SweetAlert.swal("提示", "操作失败！", "error");
                    }
                })
                .error(function(data){
                    if(row.entity.rollStatus==true){
                        row.entity.rollStatus = false;
                    } else {
                        row.entity.rollStatus = true;
                    }
                    $scope.notice("服务器异常")
                });
                */
/*
            if(row.entity.rollStatus){
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
		            	if(row.entity.rollStatus==true){
		            		row.entity.rollStatus=1;
		            	} else if(row.entity.rollStatus==false){
		            		row.entity.rollStatus=0;
		            	}
		                $http.post("riskEventRecordAction/updateRollStatus",angular.toJson(row.entity))
		            	.success(function(data){
		            		if(data.bols){
		            			SweetAlert.swal("提示", "操作成功！", "success");
		            		}else{
		            			if(row.entity.rollStatus==true){
		    	            		row.entity.rollStatus = false;
		    	            	} else {
		    	            		row.entity.rollStatus = true;
		    	            	}
		            			SweetAlert.swal("提示", "操作失败！", "error");
		            		}
		            	})
		            	.error(function(data){
		            		if(row.entity.rollStatus==true){
	    	            		row.entity.rollStatus = false;
	    	            	} else {
	    	            		row.entity.rollStatus = true;
	    	            	}
		            		$scope.notice("服务器异常")
		            	});
		            } else {
		            	if(row.entity.rollStatus==true){
		            		row.entity.rollStatus = false;
		            	} else {
		            		row.entity.rollStatus = true;
		            	}
		            }
	        });
	        */
	    };
})
.filter('handleStatusNodes', function () {
	return function (value) {
		switch(value) {
			case 0 :
				return "未处理";
				break;
			case 1 :
				return "已处理";
				break;
			
		}
	}
})
.filter('handleResultsNodes', function () {
	return function (value) {
		switch(value) {
			case 1 :
				return "安全";
				break;
			case 2 :
				return "可疑";
				break;
			case 3 :
				return "风险";
				break;
		}
	}
})

