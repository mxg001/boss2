/**
 * 机具申请详情
 */
angular.module('inspinia').controller('termianlApplyDetailCtrl',function($scope,$state,$filter,$log,$http,$stateParams,$compile,i18nService){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	$scope.info={};
	$scope.statuss=[{text:"待直属处理",value:0},{text:"已处理",value:1},{text:"待一级处理",value:2}];
	$http.get('terminalApplyAction/selectDetail?ids='+$stateParams.id)
	.success(function(largeLoad) {
		if(largeLoad.bols){
			$scope.info=largeLoad.result;
		}else{
			$scope.notice("查询出错");
		}
	});

//	$scope.commit=function(){
//		if($scope.info.status==1){
//			$scope.notice("已经是处理状态不能修改");
//			return;
//		}
//		$http.get('terminalApplyAction/updateInfo?ids='+$stateParams.id)
//		.success(function(largeLoad) {
//			if(largeLoad.bols){
//				$scope.notice(largeLoad.msg);
//				$state.go('merchant.termianlApplyQuery');
//			}else{
//				$scope.notice(largeLoad.msg);
//			}
//		});
//	}
}).filter('snisnull', function () {
    return function (value) {
        if(value!=null&&""!=value){
            return "是";
        }else{
            return "否";
        }
    }
})