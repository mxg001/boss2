/**
 * 风控规则管理
 */
angular.module('inspinia',['uiSwitch']).controller('riskRulesMgrCtrl',function($scope, $http, $state, $stateParams, i18nService,$filter,SweetAlert,$document) {

	i18nService.setCurrentLang('zh-cn');
	$scope.types=[{text:"全部",value:-1},{text:"开启",value:1},{text:"关闭",value:"2"}]
	$scope.info={status:-1,rulesInstruction:"-1"};
	$scope.riskRulesGridData=[{"rulesNo":1,"status":1,"rollType":123},{"rulesNo":2,"status":2,"rollType":123}];
	//查询
	$scope.query=function(){
		$http.post('riskRulesAction/selectAllInfo',
				"info="+angular.toJson($scope.info)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
				 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		.success(function(data){
			if(!data.bols){
				$scope.notice("查询失败");
				return;
			}
			$scope.riskRulesGridData =data.page.result; 
			$scope.riskRulesGrid.totalItems = data.page.totalCount;
		})
	}
	$scope.query();
	//清空
	$scope.reset = function() {
		$scope.info={status:-1,rulesInstruction:"-1",rulesNo:""};
	};
	
	$scope.paginationOptions=angular.copy($scope.paginationOptions);	
	$scope.riskRulesGrid = {
			data:"riskRulesGridData",
	        paginationPageSize:10,                  //分页数量
	        paginationPageSizes: [10,20,50,100],	  //切换每页记录数
	        useExternalPagination: true,		  //开启拓展名
	        columnDefs: [
                {field: 'rulesNo',displayName: '序号',width: 80,pinnable: false,sortable: false,
                	cellTemplate:'<span class="lh30">{{rowRenderIndex +1}}</span>'
                },
                {field: 'rulesNo',displayName: '规则编号',width: 100,pinnable: false,sortable: false},
                {field: 'rulesEngine',displayName: '规则引擎',width: 500,pinnable: false,sortable: false},
                /*{field: 'efficientNodeNo',displayName: '生效节点',width: 100,pinnable: false,sortable: false,cellFilter:"nodes"},*/
                {field: 'rulesInstruction',displayName: '规则指令',width: 120,pinnable: false,sortable: false,cellFilter:"formatDropping:" + angular.toJson($scope.rulesInstructions)},
                {field: 'status',displayName: '状态',width: 100,pinnable: false,sortable: false,
                	cellTemplate:
                		'<span ng-show="grid.appScope.hasPermit(\'riskRule.switch\')"><switch class="switch switch-s" ng-model="row.entity.status" ng-change="grid.appScope.open(row)" /></span>'
        	            +'<span ng-show="!grid.appScope.hasPermit(\'riskRule.switch\')"> <span ng-show="row.entity.status==1">开启</span><span ng-show="row.entity.status==0">关闭</span></span>'
                
                },
                {field: 'createTime',displayName: '创建时间',width: 150,pinnable: false,sortable: false,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
                {field: 'remark',displayName: '备注',width: 100,pinnable: false,sortable: false},
	            {field: 'rulesNo',displayName: '操作',width: 120,pinnedRight: true,sortable: false,editable:true,cellTemplate:
	            	'<div class="lh30">'
			        		+"<a ng-show='grid.appScope.hasPermit(\"riskRule.detail\")'  ui-sref='risk.riskRulesDetail({id:row.entity.rulesNo})'> 详情 </a>" 
			            	+"<a ng-show='grid.appScope.hasPermit(\"riskRule.update\") && row.entity.rulesNo==102'  ui-sref='risk.riskRulesSet102({id:row.entity.rulesNo})'> | 设置 </a>"
			            	+"<a ng-show='grid.appScope.hasPermit(\"riskRule.update\") && row.entity.rulesNo==103'  ui-sref='risk.riskRulesSet103({id:row.entity.rulesNo})'> | 设置 </a>" 
			            	+"<a ng-show='grid.appScope.hasPermit(\"riskRule.update\") && row.entity.rulesNo==104'  ui-sref='risk.riskRulesSet104({id:row.entity.rulesNo})'> | 设置 </a>" 
			            	+"<a ng-show='grid.appScope.hasPermit(\"riskRule.update\") && row.entity.rulesNo==105'  ui-sref='risk.riskRulesSet105({id:row.entity.rulesNo})'> | 设置 </a>" 
			            	+"<a ng-show='grid.appScope.hasPermit(\"riskRule.update\") && row.entity.rulesNo==106'  ui-sref='risk.riskRulesSet106({id:row.entity.rulesNo})'> | 设置 </a>"
                            +"<a ng-show='grid.appScope.hasPermit(\"riskRule.update\") && row.entity.rulesNo==107'  ui-sref='risk.riskRulesSet107({id:row.entity.rulesNo})'> | 设置 </a>"
                            +"<a ng-show='grid.appScope.hasPermit(\"riskRule.update\") && row.entity.rulesNo==108'  ui-sref='risk.riskRulesSet108({id:row.entity.rulesNo})'> | 设置 </a>"
                            +"<a ng-show='grid.appScope.hasPermit(\"riskRule.update\") && row.entity.rulesNo==109'  ui-sref='risk.riskRulesSet109({id:row.entity.rulesNo})'> | 设置 </a>"
                            +"<a ng-show='grid.appScope.hasPermit(\"riskRule.update\") && row.entity.rulesNo==110'  ui-sref='risk.riskRulesSet110({id:row.entity.rulesNo})'> | 设置 </a>"
                            +"<a ng-show='grid.appScope.hasPermit(\"riskRule.update\") && row.entity.rulesNo==111'  ui-sref='risk.riskRulesSet111({id:row.entity.rulesNo})'> | 设置 </a>"
                            +"<a ng-show='grid.appScope.hasPermit(\"riskRule.update\") && row.entity.rulesNo==112'  ui-sref='risk.riskRulesSet112({id:row.entity.rulesNo})'> | 设置 </a>"
                            +"<a ng-show='grid.appScope.hasPermit(\"riskRule.update\") && row.entity.rulesNo==113'  ui-sref='risk.riskRulesSet113({id:row.entity.rulesNo})'> | 设置 </a>"
                    		+"<a ng-show='grid.appScope.hasPermit(\"riskRule.update\") && row.entity.rulesNo==114'  ui-sref='risk.riskRulesSet114({id:row.entity.rulesNo})'> | 设置 </a>"
                    		+"<a ng-show='grid.appScope.hasPermit(\"riskRule.update\") && row.entity.rulesNo==115'  ui-sref='risk.riskRulesSet115({id:row.entity.rulesNo})'> | 设置 </a>"

							+"<a ng-show='grid.appScope.hasPermit(\"riskRule.update\") && row.entity.rulesNo==116'  ui-sref='risk.riskRulesSet116({id:row.entity.rulesNo})'> | 设置 </a>"
							+"<a ng-show='grid.appScope.hasPermit(\"riskRule.update\") && row.entity.rulesNo==117'  ui-sref='risk.riskRulesSet117({id:row.entity.rulesNo})'> | 设置 </a>"
							+"<a ng-show='grid.appScope.hasPermit(\"riskRule.update\") && row.entity.rulesNo==118'  ui-sref='risk.riskRulesSet118({id:row.entity.rulesNo})'> | 设置 </a>"

							+"<a ng-show='grid.appScope.hasPermit(\"riskRule.update\") && row.entity.rulesNo==119'  ui-sref='risk.riskRulesSet119({id:row.entity.rulesNo})'> | 设置 </a>"
							+"<a ng-show='grid.appScope.hasPermit(\"riskRule.update\") && row.entity.rulesNo==120'  ui-sref='risk.riskRulesSet120({id:row.entity.rulesNo})'> | 设置 </a>"
							+"<a ng-show='grid.appScope.hasPermit(\"riskRule.update\") && row.entity.rulesNo==121'  ui-sref='risk.riskRulesSet121({id:row.entity.rulesNo})'> | 设置 </a>"
							+"<a ng-show='grid.appScope.hasPermit(\"riskRule.update\") && row.entity.rulesNo==122'  ui-sref='risk.riskRulesSet122({id:row.entity.rulesNo})'> | 设置 </a>"
							+"<a ng-show='grid.appScope.hasPermit(\"riskRule.update\") && row.entity.rulesNo==123'  ui-sref='risk.riskRulesSet123({id:row.entity.rulesNo})'> | 设置 </a>"
							+"<a ng-show='grid.appScope.hasPermit(\"riskRule.update\") && row.entity.rulesNo==124'  ui-sref='risk.riskRulesSet124({id:row.entity.rulesNo})'> | 设置 </a>"
							+"<a ng-show='grid.appScope.hasPermit(\"riskRule.update\") && row.entity.rulesNo==125'  ui-sref='risk.riskRulesSet125({id:row.entity.rulesNo})'> | 设置 </a>"
							+"<a ng-show='grid.appScope.hasPermit(\"riskRule.update\") && row.entity.rulesNo==126'  ui-sref='risk.riskRulesSet102({id:row.entity.rulesNo})'> | 设置 </a>"
							+"<a ng-show='grid.appScope.hasPermit(\"riskRule.update\") && row.entity.rulesNo==127'  ui-sref='risk.riskRulesSet127({id:row.entity.rulesNo})'> | 设置 </a>"
							+"<a ng-show='grid.appScope.hasPermit(\"riskRule.update\") && row.entity.rulesNo==128'  ui-sref='risk.riskRulesSet128({id:row.entity.rulesNo})'> | 设置 </a>"
							+"<a ng-show='grid.appScope.hasPermit(\"riskRule.update\") && row.entity.rulesNo==129'  ui-sref='risk.riskRulesSet129({id:row.entity.rulesNo})'> | 设置 </a>"
							+"<a ng-show='grid.appScope.hasPermit(\"riskRule.update\") && row.entity.rulesNo==130'  ui-sref='risk.riskRulesSet130({id:row.entity.rulesNo})'> | 设置 </a>"

							+"<a ng-show='grid.appScope.hasPermit(\"riskRule.update\") && row.entity.rulesNo!=124 " +
									"&& row.entity.rulesNo!=125 && row.entity.rulesNo!=126 && row.entity.rulesNo!=127 && row.entity.rulesNo!=128 " +
									"&& row.entity.rulesNo!=122 && row.entity.rulesNo!=123 && row.entity.rulesNo!=102 && row.entity.rulesNo!=103 " +
									"&& row.entity.rulesNo!=104 && row.entity.rulesNo!=105 && row.entity.rulesNo!=106 && row.entity.rulesNo!=107" +
                            		"&& row.entity.rulesNo!=108 && row.entity.rulesNo!=109 && row.entity.rulesNo!=110 && row.entity.rulesNo!=111 " +
									"&& row.entity.rulesNo!=112 && row.entity.rulesNo!=113 && row.entity.rulesNo!=114 && row.entity.rulesNo!=115" +
									"&& row.entity.rulesNo!=116 && row.entity.rulesNo!=117 && row.entity.rulesNo!=118 && row.entity.rulesNo!=129" +
									"&& row.entity.rulesNo!=119 && row.entity.rulesNo!=120 && row.entity.rulesNo!=121 && row.entity.rulesNo!=130' " +
								"ui-sref='risk.riskRulesSet({id:row.entity.rulesNo})'> | 设置 </a>"
		        	+'</div>'
	            	
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
	            	var data={"status":row.entity.status,"rulesNo":row.entity.rulesNo};
	                $http.post("riskRulesAction/updateStatusInfo","info="+angular.toJson(data),{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
	            	.success(function(data){
	            		if(data.bols){
	            			SweetAlert.swal("提示", "操作成功！", "success");
	            		}else{
	            			if(row.entity.status==true){
	    	            		row.entity.status = false;
	    	            	} else {
	    	            		row.entity.status = true;
	    	            	}
	            			SweetAlert.swal("提示", "操作失败！", "error");
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

	//页面绑定回车事件
	$document.bind("keypress", function(event) {
		$scope.$apply(function (){
			if(event.keyCode == 13){
				$scope.query();
			}
		})
	});

})
.filter('nodes', function () {
	return function (value) {
		switch(value) {
			case 1 :
				return "交易";
				break;
			case 2 :
				return "审件";
				break;
			case 3 :
				return "提现";
				break;
			case 4 :
				return "实名认证";
				break;
		}
	}
})
.filter('statusStr', function () {
	return function (value) {
		switch(value) {
			case 1 :
				return "开启";
				break;
			case 2 :
				return "关闭";
				break;
		}
	}
})