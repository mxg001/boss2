/**
 * 进件详情
 */
angular.module('inspinia',[]).controller("requireDetailCtrl", function($scope, $http, $state, $stateParams) {
	$scope.photo=[{text:"只允许拍照",value:'1'},{text:"拍照和相册",value:'2'}];
//	$scope.exampleType=[{text:"图片",value:1},{text:"文件",value:2},{text:"文字",value:3}];
	$scope.bool=[{text:"是",value:'1'},{text:'否',value:'2'}];
	$http.get('require/requireDetailCtrl/'+$stateParams.id).success(function(largeLoad){
		$scope.requireItem = largeLoad;
	})

});