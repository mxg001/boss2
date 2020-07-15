angular.module('inspinia').controller("addGroupServiceCtrl", function($scope, $http, $state, $stateParams){
	
	$scope.rateTypes = [{text:"每笔固定金额",value:1},{text:"每笔扣率",value:2},{text:"每笔扣率带保底封顶",value:3},{text:"每笔扣率+每笔固定金额",value:4},{text:"单笔阶梯扣率",value:5},{text:"每月累计交易量阶梯",value:6}];
	$scope.boolAll = [{text:'是',value:1},{text:'否',value:2}];
	$scope.serviceBaseInfo = {serviceType:1,feeIsCard:2,quotaIsCard:2};
	$scope.serviceRateInfo = {};//借记卡
	$scope.serviceRateInfo2 = {};//信用卡
	$scope.serviceQuotaInfo = {};
	
	$http.post('groupService/acqOrgSelectBox.do'
	).success(function(data){
		$scope.acqOrgs = data;
	}).error(function(){
	}); 
	
	$scope.save = function(){
		$scope.submitting = true;
		if($scope.serviceBaseInfo.quotaIsCard == 1){
			//借记卡单笔交易验证
			if($scope.serviceQuotaInfo.savingsSingleMinAmount>$scope.serviceQuotaInfo.savingsSingleMaxAmount){
				$scope.notice("借记卡单笔交易限额设置有误！");
				$scope.submitting = false;
				return ;
			}
			if($scope.serviceQuotaInfo.savingsSingleMaxAmount>$scope.serviceQuotaInfo.savingsDayTotalAmount){
				$scope.notice("借记卡单笔交易限额不能大于日成功阀值！");
				$scope.submitting = false;
				return ;
			}
			//信用卡单笔交易验证
			if($scope.serviceQuotaInfo.creditSingleMinAmount>$scope.serviceQuotaInfo.creditSingleMaxAmount){
				$scope.notice("信用卡单笔交易限额设置有误！");
				$scope.submitting = false;
				return ;
			}
			if($scope.serviceQuotaInfo.creditSingleMaxAmount>$scope.serviceQuotaInfo.creditDayTotalAmount){
				$scope.notice("信用卡单笔交易限额不能大于日成功阀值！");
				$scope.submitting = false;
				return ;
			}
			//借记卡与单日总值验证
			if($scope.serviceQuotaInfo.savingsDayTotalAmount>$scope.serviceQuotaInfo.dayTotalAmount){
				$scope.notice("借记卡日成功交易总额阀值不能大于所有卡种日成功交易总额阀值！");
				$scope.submitting = false;
				return ;
			}
			//信用卡与单日总值验证
			if($scope.serviceQuotaInfo.creditDayTotalAmount>$scope.serviceQuotaInfo.dayTotalAmount){
				$scope.notice("信用卡日成功交易总额阀值不能大于所有卡种日成功交易总额阀值！");
				$scope.submitting = false;
				return ;
			}
			//合计验证
//			if(($scope.serviceQuotaInfo.creditDayTotalAmount+$scope.serviceQuotaInfo.savingsDayTotalAmount)>$scope.serviceQuotaInfo.dayTotalAmount){
//				$scope.notice("借记卡和信用卡日成功交易总额阀值不能大于所有卡种日成功交易总额阀值！");
//				$scope.submitting = false;
//				return ;
//			}
		} else {
			//信用卡与单日总值验证
			if($scope.serviceQuotaInfo.transLimitMinAmount>$scope.serviceQuotaInfo.transLimitMaxAmount){
				$scope.notice("单笔交易限额最小值不能大于最大值！");
				$scope.submitting = false;
				return ;
			}
			//信用卡与单日总值验证
			if($scope.serviceQuotaInfo.transLimitMaxAmount>$scope.serviceQuotaInfo.dayTotalAmount){
				$scope.notice("单笔交易不能大于日成功交易总额阀值！");
				$scope.submitting = false;
				return ;
			}
		}
		var data;
		if($scope.serviceBaseInfo.feeIsCard==1){
			$scope.serviceRateInfo.cardRateType=2;//借记卡的卡类型是2
			$scope.serviceRateInfo2.cardRateType=1;//信用卡的卡类型是1
			data = {"serviceBaseInfo":$scope.serviceBaseInfo,"serviceRateInfos":[$scope.serviceRateInfo,$scope.serviceRateInfo2],"serviceQuotaInfo":$scope.serviceQuotaInfo};
		}else{
			$scope.serviceRateInfo.cardRateType=0;
			data = {"serviceBaseInfo":$scope.serviceBaseInfo,"serviceRateInfos":[$scope.serviceRateInfo],"serviceQuotaInfo":$scope.serviceQuotaInfo};
		}
		$http.post('groupService/addAcqService.do',
       		 angular.toJson(data)
        ).success(function(msg){
            if(msg.status){
            	$scope.serviceBaseInfo = {serviceType:1,feeIsCard:2,quotaIsCard:2};
            	$scope.serviceRateInfo = {};
            	$scope.serviceRateInfo2 = {};
            	$scope.serviceQuotaInfo = {};
            	$state.transitionTo('org.addGroupService',null,{reload:true});
				$scope.submitting = false;
            }
			$scope.notice(msg.msg);
        }).error(function(){
			$scope.submitting = false;
        }); 
	}
	
});