/**
 * 代理商账户控制
 */
angular.module('inspinia',['uiSwitch']).controller('industrySwitchActivityCtrl',function($scope,$http,$state,$stateParams,$compile,$filter,$log,$uibModal,$timeout,SweetAlert,i18nService){
    i18nService.setCurrentLang('zh-cn');
    $scope.baseInfo={};
    $scope.submitting = false;
    
    $scope.merchantTypeList=[];
    $scope.industrySwitchList=[];
    $scope.query=function(){
    	$http.get('functionManager/industrySwitch').success(function(result){
    		if (result.status) {
    			$scope.baseInfo.industrySwitch=result.data.industrySwitch;
    			var sysDicts= result.data.sysDicts;
    			$scope.merchantTypeList=sysDicts;
    			$scope.industrySwitchList = result.data.industrySwitchList;
    			
    			
    		}
    	});
    }
    $scope.query();
    $scope.gridOptions={                           //配置表格
    		enableHorizontalScrollbar: 1,       //横向滚动条
    	    enableVerticalScrollbar : 1,  //纵向滚动条
    	    data:"industrySwitchList",
        columnDefs:[                           //表格数据
            { field: 'id',displayName:'ID',width:200},
            { field: 'acqMerchantTypeName',displayName:'商户类型',width:200},
            { field: 'startTime',displayName:'开始时间',width:200},
            { field: 'endTime',displayName:'结束时间',width:200,
            },
            { field: 'action',displayName:'操作',width:200,
                cellTemplate:
                '<a class="lh30" ng-show="grid.appScope.hasPermit(\'func.industrySwitchActivitySave\')" ng-click="grid.appScope.industrySwitchActivityEdit(row.entity)">修改</a>'+
                '<a class="lh30" ng-show="grid.appScope.hasPermit(\'func.industrySwitchActivityDelete\')" ng-click="grid.appScope.industrySwitchActivityDelete(row.entity)"> | 删除</a>'
            }
        ]
    };
    
    $scope.hardWardAddModel = function(){
        $("#hardWardAddModel").modal("show");
        $scope.info={};
    };
    
    $scope.industrySwitchActivityEdit = function (data){
    	  $("#hardWardAddModel").modal("show");
    	  $scope.info=data;
    }
    $scope.cancel= function(){
    	$("#hardWardAddModel").modal("hide");
    	$scope.info={};
    };
    
    $scope.industrySwitchActivitsave= function(){
    	// 校验参数
    	if($scope.info.acqMerchantType == null ||$scope.info.acqMerchantType == "" ){
    		$scope.notice("请选择商户类型");
    		return;
    	}
    	if($scope.info.startTime == null ||$scope.info.startTime == "" ){
    		$scope.notice("请选择开始时间");
    		return;
    	}
    	if($scope.info.endTime == null ||$scope.info.endTime == "" ){
    		$scope.notice("请选择结束时间");
    		return;
    	}
    	// 发送请求
    	$http.post("functionManager/industrySwitchSave",$scope.info).success(function(result){
    		if(result.status){
    			$scope.notice("保存成功");
    			$("#hardWardAddModel").modal("hide");
    			$scope.info={};
    		}else {
    			$scope.notice("时间区间已存在");
    		}
    		$scope.query();
    	});
     	
    }
    
    $scope.industrySwitchActivityDelete=function(entity){
    	// 发送请求
    	$http.post("functionManager/industrySwitchDelete",entity).success(function(result){
    		if(result.status){
    			$scope.notice("删除成功");
    		}
    		$scope.query();
    	});
    }
    
    $scope.industrySwitchUpdate=function (){
    	if($scope.baseInfo.industrySwitch ==1){
			$scope.serviceText = "确定开启？";
		} else {
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
 	                $http.post("functionManager/industrySwitchUpdate?industrySwitch="+$scope.baseInfo.industrySwitch)
 	            	.success(function(data){
 	            		if(data.status){
 	            			$scope.notice("操作成功！");
 	            		}else{
 	            			$scope.notice("操作失败！");
 	            			if($scope.baseInfo.industrySwitch ==1){
 	            				$scope.baseInfo.industrySwitch =0;
 	            			}else {
 	            				$scope.baseInfo.industrySwitch =1;
 	            			}
 	            		}
 	            	})
 	            	.error(function(data){
 	            		if($scope.baseInfo.industrySwitch ==1){
	            				$scope.baseInfo.industrySwitch =0;
	            			}else {
	            				$scope.baseInfo.industrySwitch =1;
	            			}
	            		$scope.notice("服务器异常")
	            	});
 	            } else {
 	            	if($scope.baseInfo.industrySwitch ==1){
         				$scope.baseInfo.industrySwitch =0;
         			}else {
         				$scope.baseInfo.industrySwitch =1;
         			}
 	            }
         });
    }
    
});



