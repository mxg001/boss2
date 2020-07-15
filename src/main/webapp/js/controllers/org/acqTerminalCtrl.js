/**
 * 收单机构终端
 */
angular.module('inspinia',['uiSwitch','infinity.angular-chosen']).controller('acqTerminalCtrl',function($scope, $http, $state,$timeout, $stateParams, i18nService,$filter,SweetAlert,$document) {

	i18nService.setCurrentLang('zh-cn');
	$scope.agents=[{value:-1,text:"全部"}];
	$scope.largeSmallFlags=[{text:"全部",value:-1},{text:"不可套",value:1},{text:"可套",value:2}];
	$scope.lockeds=[{text:"全部",value:-1},{text:"正常",value:0},{text:"锁定",value:1},{text:"废弃",value:2}];
	$scope.acqOrgs=[{value:-1,text:"全部"}]
	$scope.info={locked:-1,largeSmallFlag:-1,agentNo:-1,acqOrgId:-1};
	//代理商
	 $http.post("agentInfo/selectAllInfo.do")
 	 .success(function(msg){
 			//响应成功
 	    	for(var i=0; i<msg.length; i++){
 	    		$scope.agents.push({value:msg[i].agentNo,text:msg[i].agentName});
 	    	}
 	});
	 
	//收单机构
	 $http.post("acqOrgAction/selectBoxAllInfo")
  	 .success(function(msg){
  			//响应成功
  	    	for(var i=0; i<msg.length; i++){
  	    		$scope.acqOrgs.push({value:msg[i].id,text:msg[i].acqName});
  	    	}
  	});
	 
	//清空
	$scope.reset = function() {
		$scope.info={locked:-1,largeSmallFlag:-1,agentNo:-1,acqOrgId:-1};
	};
	
	//查询
	$scope.query=function(){
		$http.post('acqTerminalAction/selectAllInfo',
				"info="+angular.toJson($scope.info)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
				 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		.success(function(data){
			if(!data.bols){
				$scope.notice("查询失败");
				return;
			}
			$scope.acqTermianlGrid.data =data.page.result; 
			$scope.acqTermianlGrid.totalItems = data.page.totalCount;
		})
	}
	// $scope.query();
//	$scope.ssrs=[{id:222,locked:0,status:1}]
	$scope.paginationOptions=angular.copy($scope.paginationOptions);	
	$scope.acqTermianlGrid = {
//			data:"ssrs",
	        paginationPageSize:10,                  //分页数量
	        paginationPageSizes: [10,20,50,100],	  //切换每页记录数
	        useExternalPagination: true,		  //开启拓展名
	        columnDefs: [
                {field: 'id',displayName: '序号',width: 80,pinnable: false,sortable: false},
                {field: 'acqMerchantNo',displayName: '收单机构商户编号',width: 180,pinnable: false,sortable: false},
                {field: 'acqMerchantName',displayName: '收单机构商户名称',width: 160,pinnable: false,sortable: false},
                {field: 'acqTerminalNo',displayName: '收单机构终端编号',width: 160,pinnable: false,sortable: false},
                {field: 'batchNo',displayName: '批次号',width: 160,pinnable: false,sortable: false},
                {field: 'serialNo',displayName: '流水号',width: 160,pinnable: false,sortable: false},
                {field: 'locked',displayName: '锁定状态',width: 120,pinnable: false,sortable: false,
                	cellFilter:"formatDropping:[{text:'正常',value:0},{text:'锁定',value:1},{text:'废弃',value:2}]"
                },
                {field: 'status',displayName: '状态',width: 150,pinnable: false,sortable: false,
                	cellTemplate:
//                		'<switch class="switch" ng-model="row.entity.status"  ng-change="grid.appScope.open(row)" />'
                		'<span ng-show="grid.appScope.hasPermit(\'orgTerminal.switch\')"><switch class="switch switch-s" ng-model="row.entity.status" ng-change="grid.appScope.open(row)" /></span>'
        	            +'<span ng-show="!grid.appScope.hasPermit(\'orgTerminal.switch\')"> <span ng-show="row.entity.status==1">开启</span><span ng-show="row.entity.status==0">关闭</span></span>'
                },
                {field: 'createTime',displayName: '创建时间',width: 150,pinnable: false,sortable: false,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
	            {field: 'id',displayName: '操作',width: 200,pinnedRight: true,sortable: false,editable:true,cellTemplate:
	            	"<div class='lh30'> <a ng-show='grid.appScope.hasPermit(\"orgTerminal.detail\")' ui-sref='org.orgTerminalDetail({id:row.entity.id})'>详情 </a>" +
	            	"<a ng-show='grid.appScope.hasPermit(\"orgTerminal.update\")' ui-sref='org.acqTerminalUp({id:row.entity.id})'> | 修改 </a>" +
//	            	" |  <a ng-click='grid.appScope.openLargeSmallFlag(row.entity)'> 大小套 </a>" +
	            	"<a ng-show='grid.appScope.hasPermit(\"orgTerminal.delete\")' ng-click='grid.appScope.openDelModel(row.entity.id,row.entity.acqTerminalNo)'> | 删除 </a></div>"
	            	
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
	$scope.open=function(row){
		if(row.entity.status){
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
	            	if(row.entity.status==true){
	            		row.entity.status=1;
	            	} else if(row.entity.status==false){
	            		row.entity.status=0;
	            	}
	            	var data={"status":row.entity.status,"id":row.entity.id};
	                $http.post("acqTerminalAction/updateTermianlStatus","info="+angular.toJson(data),{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
	            	.success(function(data){
	            		if(data.bols){
	            			$scope.notice("操作成功");
	            		}else{
	            			if(row.entity.status==true){
	    	            		row.entity.status = false;
	    	            	} else {
	    	            		row.entity.status = true;
	    	            	}
	            			$scope.notice("操作失败");
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
	
	//打开删除终端模板
	$scope.openDelModel=function(id,no){
		$scope.no=no;
		$scope.id=id;
		$('#myModal').modal('show');
	}
	
	//提交删除终端模板
	$scope.commitDelModel=function(){
		for(var i=0;i<$scope.acqTermianlGrid.data.length;i++){
			if($scope.acqTermianlGrid.data[i].id==$scope.id){
				$scope.acqTermianlGrid.data.splice(i,1);
				break;
			}
		}
		$http.post('acqTerminalAction/deleteByid',
				"ids="+angular.toJson($scope.id),{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		.success(function(data){
			if(!data.bols){
				$scope.notice("删除失败");
			}else{
				$scope.notice("删除成功");
			}
		})
		$('#myModal').modal('hide');
	}

	$scope.getStates =getStates;
	var oldValue="";
	var timeout="";
	function getStates(value) {
		$scope.agentt = [];
		var newValue=value;
		if(newValue != oldValue){
			if (timeout) $timeout.cancel(timeout);
			timeout = $timeout(
				function(){
					$http.post('agentInfo/selectAllInfo','item=' + value,
						{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
						.then(function (response) {
							if(response.data.length==0) {
								$scope.agentt.push({value: "", text: ""});
							}else{
								for(var i=0; i<response.data.length; i++){
									$scope.agentt.push({value:response.data[i].agentNo,text:response.data[i].agentName});
								}
							}
							$scope.agents = $scope.agentt;
							oldValue = value;
						});
				},800);
		}
	}
	
	//页面绑定回车事件
	$document.bind("keypress", function(event) {
		$scope.$apply(function (){
			if(event.keyCode == 13){
				$scope.query();
			}
		})
	});
//	//打开大小套的模板
//	$scope.openLargeSmallFlag=function(entity){
//		$scope.terminalStr=entity;
//		$('#largeSmallFlagModal').modal('show');
//	}
//	
//	//提交大小套的模板
//	$scope.commitlargeSmallFlagModal=function(){
//		$scope.terminalStr.acqMerchantNo=$scope.merNo;
//		$scope.terminalStr.acqTerminalNo=$scope.terNo;
//		$http.post('acqTerminalAction/addTermianlInfo',
//		"info="+angular.toJson($scope.terminalStr),{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
//		 .success(function(data){
//			if(data.result){
//				$scope.notice(data.msg);
//			}else{
//				$scope.notice(data.msg);
//			}
//		})
//		$('#largeSmallFlagModal').modal('hide');
//	}
});