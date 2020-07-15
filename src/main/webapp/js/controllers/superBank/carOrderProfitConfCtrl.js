angular.module('inspinia').controller('carOrderProfitConfCtrl',function($scope,$http,i18nService,$document,$state,SweetAlert,$timeout){
   
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    $scope.query = function () {
        $scope.submitting = true;
        $scope.loadImg = true;
        $http({
            url: 'superBank/carOrderProfitConf',
            method:'POST'
        }).success(function (result) {
            $scope.submitting = false;
            $scope.loadImg = false;
            if (!result.status){
                $scope.notice(result.msg);
                return;
            }
            $scope.baseInfo = result.data;
        }).error(function () {
            $scope.submitting = false;
            $scope.loadImg = false;
            $scope.notice('服务器异常,请稍后再试.');
        });
    };
    
    //新增或更新
    $scope.addOrUpd = function () {
    	var flag = $scope.checkInput();
    	
    	if(!flag){
    		return ;
    	}
    	
    	$scope.submitting = true;
        $scope.loadImg = true;
        $http({
            url: 'superBank/updCarOrderProfitConf',
            data:$scope.baseInfo,
            method:'POST'
        }).success(function (result) {
            $scope.submitting = false;
            $scope.loadImg = false;
            $scope.notice(result.msg);
            if (!result.status){
                return;
            }
            $state.transitionTo('superBank.carOrderProfitConf',null,{reload:true});
        }).error(function () {
            $scope.submitting = false;
            $scope.loadImg = false;
            $scope.notice('服务器异常,请稍后再试.');
        });
    };
    
    $scope.agentShow = false;
    $scope.oemShow = false;
    
    $scope.checkInput = function (){
    	var regex = /^[0-9]+([.]{1}[0-9]{1,2}){0,1}$/;
    	var agentInput = $scope.baseInfo.orgPushAmount;
    	var oemInput = $scope.baseInfo.orgGainAmount;
    	
    	var ret = true;
    	
    	if(!regex.test(agentInput)){
    		$scope.agentShow = true;
    		ret = false;
    	}else {
    		$scope.agentShow = false;
    	}
    	
    	if(!regex.test(oemInput)){
    		$scope.oemShow = true;
    		ret = false;
    	}else{
    		$scope.oemShow = false;
    	}
    	
    	return ret;
    }
    
    $scope.query();
    
    //页面绑定回车事件
    $document.bind("keypress", function(event) {
        $scope.$apply(function (){
            if(event.keyCode == 13){
                $scope.query();
            }
        })
    });
});