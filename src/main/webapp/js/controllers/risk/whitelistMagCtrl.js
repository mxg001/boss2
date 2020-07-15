/**
 * 白名单管理
 */
angular.module('inspinia',['uiSwitch']).controller('whitelistMagCtrl',function($scope, $http, $state, $stateParams, i18nService,$filter,SweetAlert,$document) {

	i18nService.setCurrentLang('zh-cn');
	$scope.info={rollType:-1,rollBelong:1,rollStatus:-1};
	$scope._rollStatus=[{text:"全部",value:-1},{text:"开启",value:1},{text:"关闭",value:0}];
	$scope.whitelistDate=[];
	//查询
	$scope.query=function(){
		if($scope.info.sdate>$scope.info.edate){
			$scope.notice("起始时间不能大于结束时间");
			return;
		}
		$http.post('riskRollAction/selectRollAllInfo',
				"info="+angular.toJson($scope.info)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
				 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		.success(function(data){
			if(!data.bols){
				$scope.notice("查询失败");
				return;
			}
			$scope.whitelistDate =data.page.result; 
			$scope.whitelistGrid.totalItems = data.page.totalCount;
		})
	}
	$scope.query();
	//清空
	$scope.reset = function() {
		$scope.info={rollType:-1,rollBelong:1,rollNo:"",rollStatus:-1,sdate:"",edate:""}
	};
	
	$scope.paginationOptions=angular.copy($scope.paginationOptions);	
	$scope.whitelistGrid = {
			data:"whitelistDate",
	        paginationPageSize:10,                  //分页数量
	        paginationPageSizes: [10,20,50,100],	//切换每页记录数
	        useExternalPagination: true,		  //开启拓展名
	        columnDefs: [
                {field: 'rollNo',displayName: '商户编号/身份证号',width: 200,pinnable: false,sortable: false},
                {field: 'rollTypeName',displayName: '白名单类型',width: 180,pinnable: false,sortable: false},
                {field: 'rollStatus',displayName: '状态',width: 130,pinnable: false,sortable: false,
                	cellTemplate:
                		'<span ng-show="grid.appScope.hasPermit(\'whiteList.switch\')"><switch class="switch switch-s" ng-model="row.entity.rollStatus" ng-change="grid.appScope.open(row)" /></span>'
        	            +'<span ng-show="!grid.appScope.hasPermit(\'whiteList.switch\')"> <span ng-show="row.entity.rollStatus==1">开启</span><span ng-show="row.entity.rollStatus==0">关闭</span></span>'
                
                },
                {field: 'createTime',displayName: '创建时间',width: 150,pinnable: false,sortable: false,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
                {field: 'userName',displayName: '创建人',width: 150,pinnable: false,sortable: false},
                {field: 'remark',displayName: '备注',width: 310,pinnable: false,sortable: false},
	            {field: 'id',displayName: '操作',width: 170,pinnedRight: true,pinnable: false,sortable: false,editable:true,cellTemplate:
	            	"<div class='lh30'>" +
	            	"  <a ng-show='grid.appScope.hasPermit(\"whiteList.update\")' ui-sref='risk.whitelistUp({id:row.entity.id})'>修改 </a>" +
	            	"<a ng-show='grid.appScope.hasPermit(\"whiteList.delete\")' ng-click='grid.appScope.deleteBlacklist(row.entity.id)'> | 删除 </a></div>"
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
	
	//全选
	$scope.selectAll = function() {
		if ($scope.tall) {
			for (var i=0; i<$scope.rulesList.length; i++) {
				$scope.rulesList[i].merWhiteRoll = 1;
			}
		} else {
			for (var i=0; i<$scope.rulesList.length; i++) {
				$scope.rulesList[i].merWhiteRoll = 0;
			}
		}
	}
	
	//确认规则配置
	$scope.confirm = function() {
		var riskRulesArr = [];
		var riskRules = null;
		
		for (var i=0; i<$scope.rulesList.length; i++) {
			riskRules = {rulesNo:-1,merWhiteRoll:0,realNameWhiteRoll:0};
			riskRules.rulesNo = $scope.rulesList[i].rulesNo;
			if ($scope.rulesList[i].merWhiteRoll == 1) {
				//选中状态
				riskRules.merWhiteRoll = 1;
			} else {
				riskRules.merWhiteRoll = 0;
			}
			
			if ($scope.rulesList[i].realNameWhiteRoll == 1) {
				//选中状态
				riskRules.realNameWhiteRoll = 1;
			} else {
				riskRules.realNameWhiteRoll = 0;
			}

			if ($scope.rulesList[i].walletWhiteRoll == 1) {
				//选中状态
				riskRules.walletWhiteRoll = 1;
			} else {
				riskRules.walletWhiteRoll = 0;
			}
			
			riskRulesArr.push(riskRules);
		}
		console.info(riskRulesArr);
			
		$http.post('riskRulesAction/rulesConfig',
				"riskRulesJson="+angular.toJson(riskRulesArr),
				 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		.success(function(data){
			if(!data.bols){
				$scope.notice(data.msg);
			}else{
				$scope.notice(data.msg);
				$('#addWhiteRuls').modal('hide');
				$scope.query();
			}
		})
	}
	
	//批量删除
	$scope.delBatch = function() {
		var ids = [];
		var rows = $scope.gridApi.selection.getSelectedRows();
		for (var i=0; i<rows.length; i++) {
			ids.push(rows[i].id);
		}
		ids = ids.join(",");
		if (ids == "") {
			$scope.notice("请在表格中选择要删除的白名单");
		} else {
			SweetAlert.swal({
	            title: "确认删除选中的白名单吗？",
	            type: "warning",
	            showCancelButton: true,
	            confirmButtonColor: "#DD6B55",
	            confirmButtonText: "确认",
	            cancelButtonText: "取消",
	            closeOnConfirm: true,
	            closeOnCancel: true },
		        function (isConfirm) {
		            if (isConfirm) {
		            	$http.post('riskRollAction/delBatch',
		        				"ids="+angular.toJson(ids),
		        				 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		        		.success(function(data){
		        			if(!data.bols){
		        				$scope.notice(data.msg);
		        			}else{
		        				$scope.notice(data.msg);
		        				$scope.query();
		        			}
		        		})
		            }
	        });
		}
	}
	
	$scope.open=function(row){
		if(row.entity.rollStatus){
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
	            	if(row.entity.rollStatus==true){
	            		row.entity.rollStatus=1;
	            	} else if(row.entity.rollStatus==false){
	            		row.entity.rollStatus=0;
	            	}
	                $http.post("riskRollAction/updateRollStatus",angular.toJson(row.entity))
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
    	
    };
    
    //删除白名单
	$scope.deleteBlacklist=function(id){
        SweetAlert.swal({
            title: "确认删除该条白名单吗？",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "确认",
            cancelButtonText: "取消",
            closeOnConfirm: true,
            closeOnCancel: true },
	        function (isConfirm) {
	            if (isConfirm) {
	            	$http.post('riskRollAction/deleteByid?ids='+id).success(function(msg){
	    				if(msg.bols){
	    					$scope.notice(msg.msg);
	    					$scope.query();
	    				} else {
	    					$scope.notice(msg.msg);
	    				}
	    			}).error(function(){
	    			}); 
	            }
        });
    };
	
	$scope.openModal=function(){
		//查询所有的规则
		$http.post('riskRulesAction/selectAll',
				 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		.success(function(data){
			if(!data.bols){
			}else{
				console.info(data.rulesList);
				$scope.rulesList = data.rulesList;
				$scope.roll={rollType:5};
				$('#addWhiteRuls').modal('show');
			}
		})
		
	}
	
	$scope.addwhiteistNo=function(){
		$scope.submitting = true;
		if($scope.roll.rollNumber==""||$scope.roll.rollNumber==null){
			$scope.notice("请填写完整的信息！！！！");
			$scope.submitting = false;
			return;
		}
		$http.post('riskRollAction/addRollListInfo',
				"info="+angular.toJson($scope.roll),
				 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		.success(function(data){
			if(!data.bols){
				$scope.notice(data.msg);
				$scope.submitting = false;
			}else{
				$scope.notice(data.msg);
				$scope.submitting = false;
				$('#addwhitelist').modal('hide');
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
	
})