/**
 * 二维码详情
 */
angular.module('inspinia').controller('appInfoQueryDetailCtrl',function($scope,$http,$state,$stateParams,$compile,$filter,i18nService){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	$scope.info={};
	$http.post('appInfoAction/selectByParam?ids='+$stateParams.id)
	.success(function(largeLoad) {
		if(largeLoad.bols){
			$scope.info=largeLoad.data;
		}else{
			$scope.notice(largeLoad.msg);
		}
	});
	
})