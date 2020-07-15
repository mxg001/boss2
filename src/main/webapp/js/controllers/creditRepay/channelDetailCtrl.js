/**
 * 通道详情
 */
angular.module('inspinia').controller('channelDetailCtrl',function($scope,$http,$state,$stateParams,i18nService){

	//数据源
    i18nService.setCurrentLang('zh-cn');

    $scope.info = {};
    $scope.execludeCardList = [];

    $scope.query = function () {
        $http.get('channel/selectById?id='+$stateParams.id)
            .success(function(data) {
                if(data.status){
                    $scope.info = data.channel.repayChannel;
                    $scope.execludeCardList = data.channel.excludeCardList;
                }else{
                    $scope.notice(data.msg);
                }
            });
    }

});