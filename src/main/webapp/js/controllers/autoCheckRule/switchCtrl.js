angular.module('inspinia',['uiSwitch']).controller('switchCtrl',function(i18nService,$scope,$http,$state,$stateParams,$compile,$filter,SweetAlert){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	$scope.paginationOptions=angular.copy($scope.paginationOptions);

	
	$scope.switchTable = {
			data: 'functionManagerData',
			paginationPageSize: 10,
			paginationPageSizes: [10, 20, 50, 100],
			useExternalPagination: true,		  	//开启拓展名
			columnDefs: [
	            {field: 'id', displayName: '序号'},
	            {field: 'functionNumber', displayName: '功能编号'},
	            {field: 'functionName', displayName: '功能名称'},
	            {field: 'remark', displayName: '功能说明'},
	            {field: 'functionSwitch', displayName: '功能是否开启',cellTemplate:
					'<div class="lh30">'+
	            		'<span ><switch class="switch switch-s" ng-true-value="1" ng-false-value="0" ng-model="row.entity.functionSwitch" ng-change="grid.appScope.open1(row)" /></span>' +
					'</div>'
	            },
	            {field: 'agentControl', displayName: '是否开启代理商控制',cellTemplate:
					'<div class="lh30">'+
	            		'<span ng-show="row.entity.agentIsControl==1"><switch class="switch switch-s" ng-true-value="1" ng-false-value="0" ng-model="row.entity.agentControl" ng-change="grid.appScope.open2(row)" /></span>' +
					'</div>'
		        },
	            {field: 'id', displayName: '代理商管理',cellTemplate:
					'<div class="lh30">'+
	            		'<span ng-show="grid.appScope.hasPermit(\'func.update\')&&row.entity.functionSwitch==1&&row.entity.agentControl==1"><a  ui-sref="func.switchSet({functionNumber:row.entity.functionNumber})">设置</a></span>' +
					'</div>'
	            },
		        {field: 'id', displayName: '黑名单管理',cellTemplate:
					'<div class="lh30">'+
	            		'<span ng-show="grid.appScope.hasPermit(\'func.update\')&&row.entity.blacklist == 1"><a  ui-sref="func.switchSet({functionNumber:row.entity.functionNumber,blacklist:1})">设置</a></span>' +
					'</div>'
	            },
                {field: 'id', displayName: '商户黑名单',cellTemplate:
                    '<div class="lh30">'+
                    '<span ng-show="grid.appScope.hasPermit(\'func.update\')&&row.entity.merBlackList == 1"><a  ui-sref="func.switchMerSet({functionNumber:row.entity.functionNumber,merList:1})">设置</a></span>' +
                    '</div>'
                },
				{field: 'setting', displayName: '业务设置',cellTemplate:
						'<div class="lh30" ng-show="grid.appScope.hasPermit(\'func.update\') && row.entity.setting == 1">'+
							'<div ng-switch="row.entity.functionNumber">'+
								'<span ng-switch-when="062">' +
									'<a  ui-sref="func.setting062({functionNumber:row.entity.functionNumber,functionName:row.entity.functionName})">设置</a>' +
								'</span>'+
                                '<span ng-switch-when="064">' +
                                    '<a  ui-sref="func.setting064({functionNumber:row.entity.functionNumber,functionName:row.entity.functionName})">设置</a>' +
                                '</span>'+
								'<span ng-switch-when="065">' +
									'<a  ui-sref="func.switchSpecialSet065({functionNumber:row.entity.functionNumber})">设置</a>' +
								'</span>'+
								'<span ng-switch-when="066">' +
									'<a  ui-sref="func.switchSpecialSet066({functionNumber:row.entity.functionNumber})">设置</a>' +
								'</span>'+
								'<span ng-switch-when="069">' +
									'<a  ui-sref="func.switchSpecialSet069({functionNumber:row.entity.functionNumber})">设置</a>' +
								'</span>'+
								'<span ng-switch-when="072">' +
									'<a  ui-sref="func.switchSpecialSet072({functionNumber:row.entity.functionNumber})">设置</a>' +
								'</span>'+
								'<span ng-switch-when="073">' +
									'<a  ui-sref="func.switchSpecialSet073({functionNumber:row.entity.functionNumber})">设置</a>' +
								'</span>'+
								'<span ng-switch-when="074">' +
									'<a  ui-sref="func.switchSpecialSet074({functionNumber:row.entity.functionNumber})">设置</a>' +
								'</span>'+
								'<span ng-switch-when="070">' +
                        			'<a  ui-sref="func.switchSpecialSet070({functionNumber:row.entity.functionNumber,functionName:row.entity.functionName})">设置</a>' +
								'</span>'+
								'<span ng-switch-when="071">' +
									'<a  ui-sref="func.switchSpecialSet070({functionNumber:row.entity.functionNumber,functionName:row.entity.functionName})">设置</a>' +
                        		'</span>'+
								'<span ng-switch-default>' +
									'<a  ui-sref="func.setting({functionNumber:row.entity.functionNumber,functionName:row.entity.functionName})">设置</a>' +
								'</span>'+
							'</div>'+
						'</div>'
				}
	        ]
		};
	//查询
	$scope.query = function(){
		$http.post('functionManager/selectFunctionManagers.do',"baseInfo="+angular.toJson($scope.baseInfo)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+
			$scope.paginationOptions.pageSize,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
				.success(function(page){
					if(!page){
						return;
					}
					$scope.functionManagerData = page;
					$scope.switchTable.totalItems = page.totalCount;
				}).error(function(){
				});
	}
	$scope.query();
	
	$scope.open1=function(row){
		if(row.entity.functionSwitch){
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
	            	if(row.entity.functionSwitch==true){
	            		row.entity.functionSwitch=1;
	            	} else if(row.entity.functionSwitch==false){
	            		row.entity.functionSwitch=0;
	            	}
	            	var data={"functionSwitch":row.entity.functionSwitch,"id":row.entity.id};
	                $http.post("functionManager/updateFunctionSwitch.do",angular.toJson(data))
	            	.success(function(data){
	            		if(data.status){
	            			$scope.notice("操作成功！");
	            		}else{
	            			if(row.entity.functionSwitch==true){
	    	            		row.entity.functionSwitch = false;
	    	            	} else {
	    	            		row.entity.functionSwitch = true;
	    	            	}
	            			$scope.notice("操作失败！");
	            		}
	            	})
	            	.error(function(data){
	            		if(row.entity.functionSwitch==true){
    	            		row.entity.functionSwitch = false;
    	            	} else {
    	            		row.entity.functionSwitch = true;
    	            	}
	            		$scope.notice("服务器异常")
	            	});
	            } else {
	            	if(row.entity.functionSwitch==true){
	            		row.entity.functionSwitch = false;
	            	} else {
	            		row.entity.functionSwitch = true;
	            	}
	            }
        });
    	
    };
    $scope.open2=function(row){
    	if(row.entity.agentControl){
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
    				if(row.entity.agentControl==true){
    					row.entity.agentControl=1;
    				} else if(row.entity.agentControl==false){
    					row.entity.agentControl=0;
    				}
    				var data={"agentControl":row.entity.agentControl,"id":row.entity.id};
    				$http.post("functionManager/updateAgentControl.do",angular.toJson(data))
    				.success(function(data){
    					if(data.status){
    						$scope.notice("操作成功！");
    					}else{
    						if(row.entity.agentControl==true){
    							row.entity.agentControl = false;
    						} else {
    							row.entity.agentControl = true;
    						}
    						$scope.notice("操作失败！");
    					}
    				})
    				.error(function(data){
    					if(row.entity.agentControl==true){
    						row.entity.agentControl = false;
    					} else {
    						row.entity.agentControl = true;
    					}
    					$scope.notice("服务器异常")
    				});
    			} else {
    				if(row.entity.agentControl==true){
    					row.entity.agentControl = false;
    				} else {
    					row.entity.agentControl = true;
    				}
    			}
    		});
    	
    };
	
});