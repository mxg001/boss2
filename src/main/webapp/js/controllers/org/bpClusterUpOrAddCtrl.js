/*
 * 业务产品默认集群，修改和新增
 */
angular.module('inspinia').controller("bpClusterUpOrAddCtrl", function($scope, $http, $state, $stateParams, i18nService) {
	i18nService.setCurrentLang('zh-cn');
	$scope.info={startPc:false};
	$scope.status=[{text:true,value:1},{text:false,value:2}];
	$scope.productServices=[];
	$scope.productTypes=[];
	$scope.acqServices=[];
	$scope.acqOrgs=[];
	$scope.type = [];
	
	$scope.title="新增业务产品默认集群";
	//获取信息
	if($stateParams.id>-1){
		$scope.title="修改业务产品默认集群";
		$http.post('defTransRouteGroupAction/selectByParam',
				"ids="+angular.toJson($stateParams.id),
				 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		.success(function(data){
			if(!data.bols){
				$scope.notice(data.msg);
			}else{
				$scope.info=data.result;
//				if(data.result.startPc=="1"){
//					$scope.info.startPc=true;
//				}else{
//					$scope.info.startPc=false;
//				}
			}
			
		})
	}
	
	
	//业务产品
	$http.get('businessProductDefine/selectAllInfo.do')
	.success(function(largeLoad) {
		if(!largeLoad)
			return
		$scope.productTypes=largeLoad;
		if($stateParams.id==-1){
			$scope.info.productId=largeLoad[0].bpId;
		}
		$scope.merServiceType($scope.info.productId);
	});
	//收单机构
	 $http.post("acqOrgAction/selectBoxAllInfo")
	 .success(function(msg){
			//响应成功
	    	for(var i=0; i<msg.length; i++){
	    		$scope.acqOrgs.push({value:msg[i].id,text:msg[i].acqName});
	    	}
//	    	$scope.info.acqOrgId=msg[0].id;
	    	$scope.aceId(msg[0].id);
	});
	
	 $scope.aceId=function(id){
			//收单机构服务
			 $scope.acqServices=[];
			 $http.post("acqServiceAction/selectBoxAllInfo","ids="+angular.toJson(id),{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		 	 .success(function(msg){
		 			//响应成功
		 		 	if(msg.length>0){
		 		 		for(var i=0; i<msg.length; i++){
		 		 			$scope.acqServices.push({value:msg[i].serviceType,text:$scope.type[msg[i].serviceType]});
				    	}
//				    	$scope.info.serviceId=msg[0].serviceType;
		 		 	}
		 		 	
		 	});
		 }
	 $scope.checkChannel=function(id){
		//非直清模式，清除直清通道值
			if(id!=2){
				$scope.info.liquidationChannel = null;
			}
		 }
	 $scope.merServiceType=function(id){
		 $scope.productServices=[];
		 $http.post("businessProductInfoAction/selectBoxAllInfo","ids="+angular.toJson(id),{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
	 	 .success(function(msg){
	 			//响应成功
	 		$scope.productServices = msg;
	 		for(var i=0; i<$scope.productServices.length; i++){
	 			$scope.productServices[i].serviceId=parseInt($scope.productServices[i].serviceId);
	 		}
	 		
	 		if($stateParams.id==-1){
	 			$scope.info.serviceId=msg[0].serviceId;
			}else{
				if($scope.info.serviceI==null){
					$scope.info.serviceId=msg[0].serviceId;
				}
			}
//	 		 if(msg.length>0){
//	 			for(var i=0; i<msg.length; i++){
//	 				if(msg[i].serviceType!=4){
//	 					$scope.productServices.push({value:msg[i].serviceId,text:msg[i].serviceName});
//	 				}
//		    	}
//		    	$scope.info.serviceId=msg[0].serviceId;
//	 		 }
	 	});
	 }
	 
//	$scope.blurs=function(groupId){
//		$http.post('defTransRouteGroupAction/selectGroupNameByGroupId',
//				"ids="+angular.toJson(groupId),
//				 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
//		.success(function(data){
//			if(!data.bols){
//				$scope.notice(data.msg);
//			}else{
//				$scope.info.groupName=data.result.groupName;
//			}
//			
//		});
//		
//	}
	
	$scope.commit=function(){
		$scope.submitting = true;
//		if($scope.info.startPc==1){
//			delete $scope.info.acqOrgId;
//			delete $scope.info.acqServiceType;
//		}else if($scope.info.startPc==2){
//			$scope.info.startPc=2;
//		}
		if($scope.info.startPc==2){
			delete $scope.info.acqOrgId;
			delete $scope.info.acqServiceType;
		}
		angular.forEach($scope.productServices,function(data){
			if($scope.info.serviceId==data.serviceId){
				$scope.info.serviceType = data.serviceType;
			}
		})
		if($stateParams.id=="-1"){
			$http.post("defTransRouteGroupAction/addBpCluster","info="+angular.toJson($scope.info),{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			.success(function(data){
				if(data.bols){
					$scope.notice(data.msg);
					$state.go('org.bpClusterQuery');
					$scope.submitting = false;
				}else{
					$scope.notice(data.msg);
					$scope.submitting = false;
				}
			});
		}else{
			//bpName带“（）”js转换失败
			$scope.info.bpName="";
			$http.post("defTransRouteGroupAction/updateBpCluster","info="+angular.toJson($scope.info),{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			.success(function(data){
				if(data.bols){
					$scope.notice(data.msg);
					$state.go('org.bpClusterQuery');
					$scope.submitting = false;
				}else{
					$scope.notice(data.msg);
					$scope.submitting = false;
				}
			});
		}
	}
});