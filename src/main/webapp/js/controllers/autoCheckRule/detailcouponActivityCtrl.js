/**
 * 鼓励金详情
 */
angular.module('inspinia').controller('detailcouponActivityCtrl',function($scope,$http,$stateParams){

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
});

