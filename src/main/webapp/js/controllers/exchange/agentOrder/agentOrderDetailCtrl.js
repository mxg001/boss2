/**
 * 产品编辑、详情
 */
angular.module('inspinia').controller('agentOrderDetailCtrl',function($scope,$http,i18nService,$stateParams){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文

    $scope.orderStatusSelect =$scope.orderStatusExchange;
    $scope.orderStatusStr=angular.toJson($scope.orderStatusSelect);

    $scope.accStatusSelect = $scope.accStatusExchange;
    $scope.accStatusStr=angular.toJson($scope.accStatusSelect);

    $scope.merCapaSelect=$scope.merCapaExchange;
    $scope.merCapaStr=angular.toJson($scope.merCapaSelect);
    $scope.info={};

    $http.post("agentOrder/getAgentOrder","id="+$stateParams.id,
        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function(data){
            if(data.status){
                $scope.info=data.order;
            }
        });
});