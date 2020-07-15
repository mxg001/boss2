/**
 * 收单机构管理
 */
angular.module('inspinia',['uiSwitch']).controller('acqOrgCtrl',function($scope, $http, $state, $stateParams, i18nService,$filter,SweetAlert,$document) {
	
	i18nService.setCurrentLang('zh-cn');
	
	$scope.status=[{text:"--请选择--",value:"-1"},{text:"开启",value:"1"},{text:"关闭",value:"0"}];
	$scope.info={acqStatus:"-1"};
	$scope.dars=[];
	
	//清空
	$scope.reset = function() {
		$scope.info={acqStatus:"-1"};
	};
	
	//查询
	$scope.query=function(){
		var info=$scope.info;
		$http.post('acqOrgAction/selectAllInfo',
				"info="+angular.toJson(info)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
				 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		.success(function(data){
			if(!data.bols){
				$scope.notice("查询失败");
				return;
			}
			$scope.dars =data.page.result; 
			$scope.acqOrgGrid.totalItems = data.page.totalCount;
		})
	}
	$scope.query();
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	$scope.acqOrgGrid = {
			data:"dars",
	        paginationPageSize:10,                  //分页数量
	        paginationPageSizes: [10,20,50,100],	  //切换每页记录数
	        useExternalPagination: true,		  //开启拓展名
	        columnDefs: [
                {field: 'id',displayName: '收单机构编号',width: 160},
                {field: 'acqName',displayName: '收单机构名称',width: 160},
                {field: 'acqStatus',displayName: '当日T１状态',width: 120,
                		cellTemplate:
                			/*'<switch class="switch" ng-model="row.entity.acqStatus"  ng-change="grid.appScope.open(row)" />'*/
                		'<span ng-show="grid.appScope.hasPermit(\'acqOrq.switch\')"><switch class="switch switch-s" ng-model="row.entity.acqStatus" ng-change="grid.appScope.open(row)" /></span>'
        	            +'<span ng-show="!grid.appScope.hasPermit(\'acqOrq.switch\')"> <span ng-show="row.entity.acqStatus==1">开启</span><span ng-show="row.entity.acqStatus==0">关闭</span></span>'
                },
                {field: 'channelStatus',displayName: '交易转集群',width: 120,
            		cellTemplate:
            		'<span ng-show="row.entity.settleType==2"><switch class="switch switch-s" ng-model="row.entity.channelStatus" ng-change="grid.appScope.openChannel(row)" /></span>'
    	            +'<span ng-show="row.entity.settleType==2"> <span ng-show="row.entity.channelStatus==1"></span><span ng-show="row.entity.channelStatus==0"></span></span>'
               },
                {field: 't0AdvanceMoney',displayName: 'T0垫资额度（元）',width: 180,
                	cellFilter:"currency:''"
                },
                {field: 't0OwnMoney',displayName: 'T0自有额度（元）',width: 180,
                	cellFilter:"currency:''"
                },
                {field: 'valvesAmount',displayName: '冲量提醒阀值(T1-T0)（元）',width: 210,
                	cellFilter:"currency:''"
                },
                {field: 't1TransAmount',displayName: '日T1交易总额（元）',width: 180,
                	cellFilter:"currency:''"
                },
                {field: 't0TransAdvanceAmount',displayName: '日T0垫资交易总额（元）',width: 200,
                	cellFilter:"currency:''"
                },
                {field: 't0TransOwnAmount',displayName: '日T0自资交易总额（元）',width: 200,
                	cellFilter:"currency:''"
                },
                {field: 'id',displayName: '离冲量阈值还差（元）',width: 200,
                	cellTemplate:'<div class="lh30" ng-bind="row.entity.t1TransAmount-row.entity.t0TransAdvanceAmount-row.entity.t0TransOwnAmount-row.entity.valvesAmount |currency:\'\'"></div >'
                },
                {field: 'acqCloseTips',displayName: '提示语',width: 200},
	            {field: 'id',displayName: '操作',width:320,pinnedRight:true,
                	cellTemplate:"<div class='lh30'><a ng-show='grid.appScope.hasPermit(\"acqOrg.whiteList\")' ng-click='grid.appScope.openWhiteList(row.entity.id)'>白名单管理</a>" +
                			" <a ng-show='grid.appScope.hasPermit(\"acqOrg.insertWhite\")' ng-click='grid.appScope.openAddWhiteList(row.entity.id)'> | 添加白名单</a>" +
                			" <a ng-show='grid.appScope.hasPermit(\"acqOrg.update\")' ui-sref='org.updateConfigura({id:row.entity.id})'> | 修改配置</a>" +
                			" <a ng-show='grid.appScope.hasPermit(\"acqOrg.detail\")' ui-sref='org.configuraDetail({id:row.entity.id})'> | 配置详情</a></div>"
                		
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
	//开通\关闭
	$scope.mhide = true;
	$scope.mdshow=true;
	$scope.open=function(row){
		if(row.entity.acqStatus){
			$scope.serviceText = "确定开启？";
		} else {
			$(".modal-backdrop").show().addClass("in");
			$('#closeChannel').modal('show');
			$scope.mdshow=false;
			$scope.mmid=row.entity.id;
			$scope.names=row.entity.acqName;
			return;
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
	            	if(row.entity.acqStatus==true){
	            		row.entity.acqStatus=1;
	            	} else if(row.entity.acqStatus==false){
	            		row.entity.acqStatus=0;
	            	}
	            	var data={"acqStatus":row.entity.acqStatus,"id":row.entity.id};
	                $http.post("acqOrgAction/updateAcqStatus","info="+angular.toJson(data),{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
	            	.success(function(data){
	            		if(data.bols){
	            			$scope.notice("操作成功");
	            		}else{
	            			if(row.entity.acqStatus==true){
	    	            		row.entity.acqStatus = false;
	    	            	} else {
	    	            		row.entity.acqStatus = true;
	    	            	}
	            			$scope.notice("操作失败");
	            		}
	            	})
	            	.error(function(data){
	            		if(row.entity.acqStatus==true){
    	            		row.entity.acqStatus = false;
    	            	} else {
    	            		row.entity.acqStatus = true;
    	            	}
	            		$scope.notice("服务异常");
	            	});
	            } else {
//	            	row.entity.acqStatus==true?false:true;
	            	if(row.entity.acqStatus==true){
	            		row.entity.acqStatus = false;
	            	} else {
	            		row.entity.acqStatus = true;
	            	}
	            }
        });
    	
    };
    //交易转集群开关
	$scope.openChannel=function(row){
		if(row.entity.channelStatus){
			$scope.serviceText = "确定开启？";
		} else{
			$scope.serviceText = "确定关闭？";
		}
	     SweetAlert.swal({
	            title: $scope.serviceText,
	            type: "warning",
	            showCancelButton: true,
	            confirmButtonColor: "#DD6B55",
	            confirmButtonText: "提交",
	            cancelButtonText: "取消",
	            closeOnConfirm: true,
	            closeOnCancel: true },
		        function (isConfirm) {
		            if (isConfirm) {
		            	var data={"channelStatus":row.entity.channelStatus,"id":row.entity.id};
		                $http.post("acqOrgAction/updatechannelStatus","info="+angular.toJson(data),{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		            	.success(function(data){
		            		if(data.bols){
		            			$scope.notice("操作成功");
		            		}else{
		            			if(row.entity.channelStatus==true){
		    	            		row.entity.channelStatus = false;
		    	            	} else {
		    	            		row.entity.channelStatus = true;
		    	            	}
		            			$scope.notice("操作失败");
		            		}
		            	})
		            	.error(function(data){
		            		if(row.entity.channelStatus==true){
	    	            		row.entity.channelStatus = false;
	    	            	} else {
	    	            		row.entity.channelStatus = true;
	    	            	}
		            		$scope.notice("服务异常");
		            	});
		            } else {
		            	if(row.entity.channelStatus==true){
		            		row.entity.channelStatus = false;
		            	} else {
		            		row.entity.channelStatus = true;
		            	}
		            }
	        });
    	
    };
	$scope.aChannel = function(){
		$(".modal-backdrop").removeClass("in").hide();
		$("#closeChannel").removeClass("in").hide();
		$scope.mdshow=true;
		$scope.query();
	}
	$scope.whiteListDate=[{id:"111"},{id:"222"},{id:"333"},{id:"444"}];
	$scope.whiteListMgrGrid={
			data:'whiteListDate',
			columnDefs:[
		           {field:'merchantNo',displayName:'商户编号',width:200,pinnable:false,sortable:false},
		           {field:'id',displayName:'操作',width:150,pinnable:false,sortable:false,
		        	   cellTemplate:'<a class="lh30" ng-show="grid.appScope.hasPermit(\'acqOrg.deleteWhite\')" ng-click="grid.appScope.delWhiteList(row.entity.id)">删除</a>'
		           }
				]
	};
	//打开白名单管理模板
	$scope.openWhiteList=function(id){
		$http.post('acqOrgAction/selectAllWlInfo',
				"ids="+angular.toJson(id),{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		.success(function(data){
			if(!data.bols){
				$scope.notice(data.msg);
			}else{
				$scope.whiteListDate =data.result; 
				$('#myModal2').modal('show');
			}
		})
	}
	
	//删除白名单
	$scope.delWhiteList=function(id){
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
	            	$http.post('acqOrgAction/deleteWlInfo',
	        				"ids="+angular.toJson(id),{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
	        		.success(function(data){
	        			if(!data.bols){
	        				$scope.notice(data.msg);
	        			}else{
	        				$scope.notice(data.msg);
	        				for(var i=0;i<$scope.whiteListDate.length;i++){
	        					if($scope.whiteListDate[i].id==id){
	        						$scope.whiteListDate.splice(i,1);
	        						break;
	        					}
	        				}
	        			}
	        		});
	            }
        });
		
	}
	
	
	//打开白名单模板
	$scope.openAddWhiteList=function(id){
		$scope.roll={merchantNo:"",acqId:id};
		$('#myModal1').modal('show');
	}
	//添加白名单
	$scope.addWhiteList=function(id){
		$scope.submitting = true;
		$http.post('acqOrgAction/addWlInfo',
				"info="+angular.toJson($scope.roll),{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		.success(function(data){
			if(!data.bols){
				$scope.notice(data.msg);
				$scope.submitting = false;
			}else{
				$scope.notice(data.msg);
				$('#myModal1').modal('hide');
				$scope.submitting = false;
			}
		})
	}
	
	//提交关闭通道模板
	$scope.commitChannel=function(){
		if($scope.mminfo==""||$scope.mminfo==null){
			$scope.notice("请填写完整的信息~~~~~");
			return;
		}
		
		var data={"acqStatus":0,"id":$scope.mmid,closeTips:$scope.mminfo};
		$http.post('acqOrgAction/updateAcqStatus',
				"info="+angular.toJson(data),{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		 .success(function(data){
			if(data.bols){
				$scope.notice(data.msg);
				$scope.query();
				$(".modal-backdrop").removeClass("in").hide();
				$("#closeChannel").removeClass("in").hide();
				$scope.mdshow=true;
			}else{
				$scope.notice("失败~~~~");
			}
		})
	}

	//页面绑定回车事件
	$document.bind("keypress", function(event) {
		$scope.$apply(function (){
			if(event.keyCode == 13){
				$scope.query();
			}
		})
	});

	
});


