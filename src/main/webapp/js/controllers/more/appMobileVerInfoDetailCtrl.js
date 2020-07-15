/**
 * 二维码子表详情
 */
angular.module('inspinia').controller('appMobileVerInfoDetailCtrl',function($scope,$http,$state,$stateParams,$compile,$filter,i18nService){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	$scope.info={};
	$scope.platforms=[{text:'android',value:0},{text:'IOS',value:1}];
	$scope.downFlags=[{text:'不需要',value:0},{text:'需要更新',value:1},{text:'需要强制下载',value:2}];
	$http.post('appInfoAction/findChildDetailInfo?ids='+$stateParams.id)
	.success(function(largeLoad) {
		if(largeLoad.bols){
			$scope.info=largeLoad.data;
		}else{
			$scope.notice(largeLoad.msg);
		}
	});
	
})