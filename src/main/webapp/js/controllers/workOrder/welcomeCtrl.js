/**
 * 首页详情
 */
angular.module('inspinia').controller("welcomeCtrl", function($scope, $http, $location, $stateParams,$window) {

	$http.get('workOrder/getToDo').success(function(result){
	    if(result.status){
	        $scope.todo = result.data;
      }else{
					$scope.status = false;
	        //$scope.notice(result.msg)
      }
	})

});