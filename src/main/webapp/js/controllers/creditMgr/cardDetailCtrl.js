/**
 * 信用卡管家-卡片详情
 */
angular.module('inspinia').controller('cardDetailCtrl',function($scope, $http, $state,$stateParams,i18nService){

	i18nService.setCurrentLang('zh-cn');

	$scope.info={};

	$http({
        url:"cmCard/queryCardInfoById?id="+$stateParams.id,
        method:"GET"
    }).success(function(data){
        if (data.status){
            $scope.info = data.info;
            if (!$scope.info.creditScore) {
            	$scope.info.creditScore = '---';
			}
        } else {
        	$scope.notice(data.msg);
        }
    }).error(function () {
        $scope.notice("服务器异常，请稍后再试");
    });

});