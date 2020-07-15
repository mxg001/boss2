/**
 * 修改卡bin
 */
angular.module('inspinia').controller("editCardBinsCtrl", function($scope, $http,$state,$stateParams,i18nService) {
    //数据源
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.stateSelect=[{text:"打开",value:1},{text:"关闭",value:0}];
    $scope.stateStr=angular.toJson($scope.stateSelect);

    $scope.cardTypeSelect=[{text:"请选择",value:null},{text:"贷记卡",value:1},{text:"借记卡",value:2},{text:"借贷合一卡",value:3},{text:"其他",value:0}];
    $scope.cardTypeStr=angular.toJson($scope.cardTypeSelect);
    $scope.businessTypes=[{text:"境外卡",value:1},{text:"借贷合一卡bin白名单",value:2}];
    $scope.businessTypeStr=angular.toJson($scope.businessTypes);

    $scope.currencySelect=$scope.cardBinsCurrencysAdd;
    $scope.currencyStr=angular.toJson($scope.currencySelect);

    $scope.info = {cardNo:null,cardType:null,cardBank:null,state:1,currency:null,cardStyle:null,remarks:null,cardDigit:null};
    $scope.submitting = false;

    $http.post("cardBins/getCardBins","id=" +$stateParams.id,
        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function(data){
            if(data.status){
                $scope.info = data.card;
            }else{
                $scope.notice(data.msg);
            }
        });

    $scope.submit = function(){
        if($scope.submitting){
            return;
        }
        $scope.submitting = true;
        $http.post("cardBins/updateCardBins","info=" + angular.toJson($scope.info),
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.submitting = false;
                    $scope.notice(data.msg);
                    $state.transitionTo('risk.cardBins',null,{reload:true});
                }else{
                    $scope.notice(data.msg);
                    $scope.submitting = false;
                }
            })
            .error(function(data) {
                $scope.notice(data.msg);
                $scope.submitting = false;
            });
    };
});
