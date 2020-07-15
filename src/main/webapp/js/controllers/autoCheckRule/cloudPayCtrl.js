/**
 * 云闪付活动详情
 */
angular.module('inspinia',['uiSwitch']).controller('cloudPayCtrl',function(i18nService,$scope,$http,$state,$stateParams,$compile,$filter,SweetAlert){

	//云闪付活动详情
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
		$http.post('couponActivity/cloudPaySave',
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
	
	if("edit"==$stateParams.edit){
		$scope.couponEdit=true;
	}else{
		$scope.couponEdit=false;
	}

});

