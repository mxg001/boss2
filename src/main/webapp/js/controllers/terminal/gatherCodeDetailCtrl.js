/**
 * 收款码详情
*/
angular.module('inspinia').controller('gatherCodeDetail',function($scope,$http,$state,$stateParams,i18nService,SweetAlert){
	$scope.status = [{text:'未导出',value:0},{text:'已导出',value:1,},{text:'已使用',value:2},{text:'弃用',value:3}];
	$scope.statusAll = angular.copy($scope.status);
	$scope.statusAll.unshift({text:'全部',value:-1});
	//详情
	$scope.query = function(){
		$http.post('gatherCode/gatherCodeDetail/'+$stateParams.id)
				.success(function(msg){
					if(!msg){
						return;
					}
					$scope.data = msg;
				}).error(function(){
				});
	}
	$scope.query();
	
});