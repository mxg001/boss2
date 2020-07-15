/**
 * 修改收单服务限额
 */
angular.module('inspinia').controller("updateServiceQuotaCtrl", function($scope, $http, $state, $stateParams){
	$scope.quotaIsCard = $stateParams.quotaIsCard;
	
	$http.post('groupService/getAcqServiceTransRule.do',
   		angular.toJson({"acqServiceId":$stateParams.id})
    ).success(function(data){
        $scope.serviceQuotaInfo = data;
    }).error(function(){
    }); 
	
	$scope.update = function () {
		$scope.submitting = true;
		if($scope.quotaIsCard == 1){
			//借记卡单笔交易验证
			if($scope.serviceQuotaInfo.savingsSingleMinAmount>$scope.serviceQuotaInfo.savingsSingleMaxAmount){
				$scope.notice("借记卡单笔交易限额最小值不能大于最大值！");
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
				$scope.notice("信用卡单笔交易限额最小值不能大于最大值！");
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
		$http.post('groupService/updateAcqServiceTransRule.do',
	   		angular.toJson($scope.serviceQuotaInfo)
	    ).success(function(msg){
			$scope.notice(msg.msg);
			$scope.submitting = false;
			$state.transitionTo('org.groupService',null,{reload:true});
	    }).error(function(){
			$scope.submitting = false;
	    }); 	
	}
	
});