/**
 * banner详情
 */
angular.module('inspinia',[]).controller("bannerDetailCtrl", function($scope, $http, $state, $stateParams) {
	$http.get('banner/bannerDetail/'+$stateParams.id).success(function(msg){
		$scope.baseInfo = msg;
	})

});