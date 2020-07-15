angular.module('inspinia').controller("addServiceCtrl", function($scope, $http, $state, $stateParams,$uibModal,$log,i18nService){
	
//	$scope.serviceTypes = [{text:"单笔代付-自有资金",value:1},{text:"单笔代付-垫资",value:2},{text:"批量代付",value:3}];
	$scope.priorities = [{text:"A级",value:"1"},{text:"B级",value:"2"},{text:"C级",value:"3"},{text:"D级",value:"4"},{text:"E级",value:"5"}];
	$scope.rateTypes = [{text:"每笔固定金额",value:1},{text:"每笔扣率",value:2},{text:"每笔扣率带保底封顶",value:3},{text:"每笔扣率+每笔固定金额",value:4},{text:"每月日均累计交易量阶梯扣率带保底",value:5}];
	$scope.costRateTypes = [{text:"每笔固定金额",value:1},{text:"每笔扣率",value:2},{text:"每笔扣率带保底封顶",value:3},{text:"每笔扣率+每笔固定金额",value:4},{text:"每月日均累计交易量阶梯扣率带保底",value:5}];
	
	$http.post('groupService/acqOrgSelectBox.do'
	).success(function(data){
		$scope.acqOrgs = data;
	}).error(function(){
	}); 
	
	$scope.reset = function(){
		$scope.serviceBaseInfo = {serviceType:1,level:"1",dayResetLimit:1};
		$scope.serviceRateInfo = {agentRateType:1};
		$scope.serviceRateInfo2 = {costRateType:1};
	}
	$scope.reset();
	
	$scope.getRun=function(num){
		$scope.serviceRateInfo.serviceRate="";
		if(num==5){
			$scope.ladderFr();
		}
	}
	$scope.getRun1=function(num){
		$scope.serviceRateInfo2.serviceRate="";
		if(num==5){
			$scope.ladderFr1();
		}
	}
	
	//代付服务费率分润
	$scope.ladderFr=function(){
		var modalScope = $scope.$new();
		modalScope.info={m1:"0",m2:"",m3:"",m4:"",m5:"",m6:"",m7:"",m8:"",m9:"",m9:"",m10:"",m11:"",m12:"",m13:"无穷大"};
		var modalInstance = $uibModal.open({
           templateUrl : 'views/money/ladderFrModal.html',  //指向上面创建的视图
           controller : 'outLadderModalCtr',// 初始化模态范围
           scope:modalScope,
           size:'lg'
       })
       modalScope.modalInstance=modalInstance;
       modalInstance.result.then(function(selectedItem){ 
		   var info=selectedItem.info;
		   $scope.serviceRateInfo.serviceRate=selectedItem.info.m1+"万元<"+
		   selectedItem.info.m2+"元~"+selectedItem.info.m3+"%<="+selectedItem.info.m4+"万元<"+
		   selectedItem.info.m5+"元~"+selectedItem.info.m6+"%<="+selectedItem.info.m7+"万元<"+
		   selectedItem.info.m8+"元~"+selectedItem.info.m9+"%<="+selectedItem.info.m10+"万元<"+
		   selectedItem.info.m11+"元~"+selectedItem.info.m12+"%<="+selectedItem.info.m13;
		   $scope.serviceRateInfo.ladder1SafeLine = selectedItem.info.m2;	
		   $scope.serviceRateInfo.ladder1Rate = selectedItem.info.m3;
		   $scope.serviceRateInfo.ladder1Max = selectedItem.info.m4;
		   
		   $scope.serviceRateInfo.ladder2SafeLine = selectedItem.info.m5;	
		   $scope.serviceRateInfo.ladder2Rate = selectedItem.info.m6;
		   $scope.serviceRateInfo.ladder2Max = selectedItem.info.m7;
		   
		   $scope.serviceRateInfo.ladder3Max = selectedItem.info.m10;
		   $scope.serviceRateInfo.ladder3SafeLine = selectedItem.info.m8;	
		   $scope.serviceRateInfo.ladder3Rate = selectedItem.info.m9;
		   
		   $scope.serviceRateInfo.ladder4SafeLine = selectedItem.info.m11;	
		   $scope.serviceRateInfo.ladder4Rate = selectedItem.info.m12;
		   $scope.serviceRateInfo.ladder4Max = null;
		   
		   
       },function(){
           $log.info('取消: ' + new Date())
       })
		
	}
	
	//垫资资金成本费率分润
	$scope.ladderFr1=function(){
		var modalScope = $scope.$new();
		modalScope.info={m1:"0",m2:"",m3:"",m4:"",m5:"",m6:"",m7:"",m8:"",m9:"",m9:"",m10:"",m11:"",m12:"",m13:"无穷大"};
		var modalInstance = $uibModal.open({
           templateUrl : 'views/money/ladderFrModal.html',  //指向上面创建的视图
           controller : 'outLadderModalCtr',// 初始化模态范围
           scope:modalScope,
           size:'lg'
       })
       modalScope.modalInstance=modalInstance;
       modalInstance.result.then(function(selectedItem){ 
		   var info=selectedItem.info;
		   $scope.serviceRateInfo2.serviceRate=selectedItem.info.m1+"万元<"+
		   selectedItem.info.m2+"元~"+selectedItem.info.m3+"%<="+selectedItem.info.m4+"万元<"+
		   selectedItem.info.m5+"元~"+selectedItem.info.m6+"%<="+selectedItem.info.m7+"万元<"+
		   selectedItem.info.m8+"元~"+selectedItem.info.m9+"%<="+selectedItem.info.m10+"万元<"+
		   selectedItem.info.m11+"元~"+selectedItem.info.m12+"%<="+selectedItem.info.m13;
		   
		   $scope.serviceRateInfo2.ladder1SafeLine = selectedItem.info.m2;	
		   $scope.serviceRateInfo2.ladder1Rate = selectedItem.info.m3;
		   $scope.serviceRateInfo2.ladder1Max = selectedItem.info.m4;
		   
		   $scope.serviceRateInfo2.ladder2SafeLine = selectedItem.info.m5;	
		   $scope.serviceRateInfo2.ladder2Rate = selectedItem.info.m6;
		   $scope.serviceRateInfo2.ladder2Max = selectedItem.info.m7;
		   
		  
		   $scope.serviceRateInfo2.ladder3SafeLine = selectedItem.info.m8;	
		   $scope.serviceRateInfo2.ladder3Rate = selectedItem.info.m9;
		   $scope.serviceRateInfo2.ladder3Max = selectedItem.info.m10;
		   
		   $scope.serviceRateInfo2.ladder4SafeLine = selectedItem.info.m11;	
		   $scope.serviceRateInfo2.ladder4Rate = selectedItem.info.m12;
		   $scope.serviceRateInfo2.ladder4Max = null;
		   
		   
       },function(){
           $log.info('取消: ' + new Date())
       })
		
	}
	
	
	$scope.save = function(){
		$scope.submitting = true;
		if($scope.serviceBaseInfo.outAccountMinAmount > $scope.serviceBaseInfo.outAccountMaxAmount){
			$scope.notice("每笔出款限额最小值不能大于最大值");
			$scope.submitting = false;
			return;
		}
		if($scope.serviceBaseInfo.outAccountMaxAmount > $scope.serviceBaseInfo.dayOutAccountAmount){
			$scope.notice("每笔出款限额不能大于每日限额");
			$scope.submitting = false;
			return;
		}
		
		var data;
		if($scope.serviceBaseInfo.serviceType==2){
			data = {"serviceBaseInfo":$scope.serviceBaseInfo,"serviceRateInfos":[$scope.serviceRateInfo,$scope.serviceRateInfo2]};
		}else{
			data = {"serviceBaseInfo":$scope.serviceBaseInfo,"serviceRateInfos":[$scope.serviceRateInfo]};
		}
		$http.post('outAccountService/addService.do',
       		 angular.toJson(data)
        ).success(function(msg){
            if(msg.status){
            	$scope.reset();
            	$state.transitionTo('money.addService',null,{reload:true});
				$scope.submitting = false;
            }
			$scope.submitting = false;
			$scope.notice(msg.msg);
        }).error(function(){
			$scope.submitting = false;
        }); 
	}
	
});

angular.module('inspinia').controller('outLadderModalCtr',function($scope,$stateParams,$http){
	 $scope.solutionModalClose=function(){
		 $scope.modalInstance.dismiss();
	 }
	 
	 $scope.solutionModalOk=function(){
		 $scope.modalInstance.close($scope);
	 }
	 $scope.bijiao=function(num1,num2){
		 if(isNaN(num1)||isNaN(num2)){
			 $scope.submitting = true;
			 $scope.notice("请输入正确的数值");
			 return;
		 }
		 if(parseFloat(num1)<0||parseFloat(num2)<0){
			 $scope.submitting = true;
			 $scope.notice("请输入正确的数值");
			 return;
		 }
		 if(parseFloat(num1)>=parseFloat(num2)){
			 $scope.submitting = true;
			 $scope.notice("后一个数值必须大于前一个数值");
			 return;
		 }
		 $scope.submitting = false;
	 }
	 
	 $scope.weikong=function(shu){
		 if(isNaN(shu)){
			 $scope.submitting = true;
			 $scope.notice("请输入正确的数值");
			 return;
		 }
		 if(parseFloat(shu)<0){
			 $scope.submitting = true;
			 $scope.notice("请输入正确的数值");
			 return;
		 }
		 $scope.submitting = false;
	 }
	 
});