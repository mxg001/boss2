/**
 * 信用卡管家-消息详情
 */
angular.module('inspinia').controller('messageDetailCtrl',function($scope, $http, $state,$stateParams,i18nService){

	i18nService.setCurrentLang('zh-cn');

	$scope.msgTypeSelect = [{text:"账单类",value:"bill"},{text:"用卡计划类",value:"plan"},{text:"提额任务类",value:"task"},{text:"其他类",value:"other"}];

	$scope.info={};

	$http({
        url:"cmServiceMsg/selectMsgInfoById?id="+$stateParams.id,
        method:"GET"
    }).success(function(data){
        if (data.status){
            $scope.info = data.info;
        } else {
        	$scope.notice(data.msg);
        }
    }).error(function () {
        $scope.notice("服务器异常，请稍后再试");
    });

});