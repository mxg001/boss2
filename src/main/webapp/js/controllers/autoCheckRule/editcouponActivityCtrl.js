/**
 * 鼓励金详情
 */
angular.module('inspinia').controller('editcouponActivityCtrl',function($scope,$http,$stateParams,$state){

	//鼓励金详情
	$scope.query = function(){
		$http.get('couponActivity/rewardDetail?actId='+$stateParams.actId)
		.success(function(msg){
			if(msg.status){
				$scope.info = msg.info;
			} else {
				$scope.notice(msg.msg);
			}
		});
	}
	$scope.query();
	
	$scope.commit = function(){
		$scope.subDisable = true;
		$http.post('couponActivity/rewardSave',
				"info="+angular.toJson($scope.info),
				 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		.success(function(data){
			if(data.status){
				$scope.notice(data.msg);
				$state.transitionTo('func.couponActivity',null,{reload:true});
				$scope.subDisable = false;
			}else{
				$scope.notice(data.msg);
				$scope.subDisable = false;
			}
		});
	}
	
	
	//赠送金额格式
	$scope.isBigdecimal = function(obj,attr){
		var exp = /^([1-9][\d]{0,7}|0)(\.[\d]{1,2})?$/;
		var num = obj[attr];
		if(!exp.test(num)){
			$scope.notice("赠送金额格式不正确");
			obj[attr] = null;
		   }
		}
	//可获取数量格式
	$scope.isNum = function(obj,attr){
		var exp = /^[1-9]\d*$/;
		var num = obj[attr];
		if(!exp.test(num) && num != -1){
			$scope.notice("可获取数量格式不对");
			obj[attr] = null;
		   }
		}
	//奖励有效期天数格式
	$scope.isDays = function(obj,attr){
		var exp = /^[1-9]\d*$/;
		var num = obj[attr];
		if(!exp.test(num)){
			$scope.notice("奖励有效期天数格式不对");
			obj[attr] = null;
		   }
		}
});

