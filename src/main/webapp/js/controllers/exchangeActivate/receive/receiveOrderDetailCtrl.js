/**
 * 收款订单详情
 */
angular.module('inspinia').controller('receiveOrderDetailCtrl',function($scope,$http,i18nService,$stateParams){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文

    $scope.orderStatusSelect = $scope.orderStatusExchange;
    $scope.orderStatusStr=angular.toJson($scope.orderStatusSelect);

    $scope.accStatusSelect = $scope.accStatusExchange;
    $scope.accStatusStr=angular.toJson($scope.accStatusSelect);

    $scope.payMethodSelect=[{text:"全部",value:''},{text:"POS",value:'1'},
        {text:"支付宝",value:'2'},{text:"微信",value:'3'},{text:"快捷",value:'4'}];
    $scope.payMethodStr=angular.toJson($scope.payMethodSelect);

    $scope.info={};

    $http.post("exchangeActivateReceiveOrder/getReceiveOrder","id="+$stateParams.id,
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
            $http.get('exchangeActivateReceiveOrder/getDataProcessing?id='+$stateParams.id)
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