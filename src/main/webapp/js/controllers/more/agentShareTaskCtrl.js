angular.module('inspinia').controller("agentShareTaskCtrl", function($scope, $http, $state, $stateParams,uiGridConstants,i18nService,$log,$uibModal,$compile,SweetAlert) {
	$scope.updateAgentShareTaskSubmitting = false;
	$scope.updateAcqServiceRateSubmitting = false;
	$scope.updateOutAccountServiceTaskSubmitting = false;
	$scope.updateSettleStatusTaskSubmitting = false;
	//代理商分润
	$scope.updateAgentShareTask = function(){
		$scope.updateAgentShareTaskSubmitting = true;
		$http.post("refular/updateAgentShareTask")
		.success(function(msg){
			$scope.notice(msg.msg);
			$scope.updateAgentShareTaskSubmitting = false;
		}).error(function(){
			$scope.updateAgentShareTaskSubmitting = false;
		});
	}
	//收单服务
	$scope.updateAcqServiceRate = function(){
		$scope.updateAcqServiceRateSubmitting = true;
		$http.post("refular/updateAcqServiceRate")
		.success(function(msg){
			$scope.notice(msg.msg);
			$scope.updateAcqServiceRateSubmitting = false;
		}).error(function(){
			$scope.updateAcqServiceRateSubmitting = false;
		});
	}
	//出款服务
	$scope.updateOutAccountServiceTask = function(){
		$scope.updateOutAccountServiceTaskSubmitting = true;
		$http.post("refular/updateOutAccountServiceTask")
		.success(function(msg){
			$scope.notice(msg.msg);
			$scope.updateOutAccountServiceTaskSubmitting = false;
		}).error(function(){
			$scope.updateOutAccountServiceTaskSubmitting = false;
		});
	}
	//交易通道
	$scope.updateSettleStatusTask = function(){
		$scope.updateSettleStatusTaskSubmitting = true;
		$http.post("refular/updateSettleStatus")
		.success(function(msg){
			$scope.notice(msg.msg);
			$scope.updateSettleStatusTaskSubmitting = false;
		}).error(function(){
			$scope.updateSettleStatusTaskSubmitting = false;
		});
	}
	 /** 存量代理商批量开户*/
	$scope.batchCreateAcc = function(){
		$http.get('agentInfo/levelOneCreateAccByBacth').success(function(msg){
			if(msg.status){
				$scope.notice(msg.msg);
			} else {
				$scope.notice(msg.msg);
			}
		}).error(function(){
		}); 
	}
});