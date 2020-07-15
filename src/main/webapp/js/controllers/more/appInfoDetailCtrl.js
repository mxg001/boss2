/**
 * 代理商APP软件设置详情
 */
angular.module('inspinia',[]).controller("appInfoDetailCtrl", function($scope, $http, $state, $stateParams) {
	$scope.imgFlagHide = true;	//“图片”，修改appInfo时，默认隐藏，当图片地址不为空时，显示
	$http.get('appInfo/selectDetail/'+$stateParams.id).success(function(msg){
		$scope.baseInfo = msg;
		if(msg.photo != null){
			$scope.imgFlagHide = false;
		}
	})
});