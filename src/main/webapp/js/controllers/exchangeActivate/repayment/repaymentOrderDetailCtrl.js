/**
 * 还款订单详情
 */
angular.module('inspinia').controller('repaymentOrderDetailCtrl',function($scope,$http,i18nService,$stateParams){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文

    $scope.orderStatusSelect = $scope.orderStatusExchange;
    $scope.orderStatusStr=angular.toJson($scope.orderStatusSelect);

    $scope.accStatusSelect = $scope.accStatusExchange;
    $scope.accStatusStr=angular.toJson($scope.accStatusSelect);

    $scope.repayStatusSelect=[{text:"全部",value:''},{text:"还款成功",value:'3'},
        {text:"还款失败",value:'4'},{text:"终止",value:'6'}];
    $scope.repayStatusStr=angular.toJson($scope.repayStatusSelect);

    $scope.payTypeSelect=[{text:"全部",value:''},{text:"分期还款",value:'1'},
        {text:"全额还款",value:'2'},{text:"完美还款",value:'3'}];
    $scope.payTypeStr=angular.toJson($scope.payTypeSelect);

    $scope.info={};

    $http.post("exchangeActivateRepaymentOrder/getRepaymentOrder","id="+$stateParams.id,
        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function(data){
            if(data.status){
                $scope.info=data.order;
            }
        });

    /**
     * 获取敏感数据
     */
    $scope.dataSta=true;
    $scope.getDataProcessing = function () {
        if($scope.dataSta){
            $http.get('exchangeActivateRepaymentOrder/getDataProcessing?id='+$stateParams.id)
                .success(function(data) {
                    if(data.status){
                        $scope.info.mobileUsername = data.order.mobileUsername;
                        $scope.info.idCardNo = data.order.idCardNo;
                        $scope.dataSta=false;
                    }else{
                        $scope.notice(data.msg);
                    }
                });
        }
    };
});