/**
 * 出款明细详情
 */
angular.module('inspinia').controller('outDetailQueryDetailCtrl',function($scope,$http,$state,$stateParams,$compile,$uibModal,$log,i18nService){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	$scope.settleUserTypes=[{text:"商户",value:'M'},{text:"代理商",value:'A'},{text:"用户",value:'U'}];//M商户；A代理商'
    // $scope.sourceSystems=[{text:"全部",value:''},{text:"交易系统",value:'core'},{text:"账户系统",value:'account'},{text:"运营系统",value:'boss'}
    //     ,{text:"代理商app",value:'agentapp'},{text:"车管家系统",value:'car'},{text:"代理商web",value:"agentweb"}
    //     ,{text:"超级银行家",value:'banker'}];
    $scope.synStatuss=[{text:"未提交",value:'1'},{text:"已提交",value:'2'}];
	$scope.settleOrderStatuss=[{text:"未确认",value:'0'},{text:"已确认",value:'1'},{text:"确认失败",value:'2'}];
	$scope.accounts=[{text:'未记账',value:'1'},{text:'记账成功',value:'2'},{text:'记账失败',value:'3'}]
	$scope.accountStr=[{text:'未记账',value:'0'},{text:'记账成功',value:'1'},{text:'记账失败',value:'2'}]
	$scope.corrections=[{text:'未冲正',value:'0'},{text:'已冲正',value:'1'}]
	$scope.settleInfo={};
	$http.get('settleOrderInfoAction/selectOutInfoDetail?ids='+$stateParams.id)
    .success(function(largeLoad) {
    	if(!largeLoad.bols){
    		$scope.notice(largeLoad.msg);
    	}else{
    		$scope.info=largeLoad.soi;
    		$scope.infoDetail=largeLoad.st;
    	}
  });

	$scope.dataSta=true;
	$scope.getDetailShow = function(){
		if($scope.dataSta){
			$http.get('settleOrderInfoAction/getDetailShow?ids='+$stateParams.id)
				.success(function(largeLoad) {
					if(!largeLoad.bols){
						$scope.notice(largeLoad.msg);
					}else{
						$scope.infoDetail=largeLoad.st;
						$scope.dataSta=false;
					}
				});
		}
	}
})