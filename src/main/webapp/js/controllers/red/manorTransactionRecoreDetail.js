/**
 * 领地业务管理
 */
angular.module('inspinia').controller('manorTransactionRecoreDetailCtrl',function(SweetAlert,i18nService,$scope,$http,$state,$stateParams,$compile,$filter,$log,$uibModal){
i18nService.setCurrentLang('zh-cn');

//'订单状态:1已创建；2待支付；3:待审核 4已授权 5订单成功 6订单失败 7已办理过  9已关闭'
$scope.statusList = [{text:"全部",value:""},{text:"已创建",value:"1"},{text:"待支付",value:"2"},
                     {text:"待审核",value:"3"},{text:"已授权",value:"4"},{text:"订单成功",value:"5"},
                     {text:"订单失败",value:"6"},{text:"已办理过",value:"7"},{text:"已关闭",value:"9"},{text:"已支付",value:"18"},{text:"已退款",value:"19"}];//订单状态
//'记账状态;0待入账；1已记账；2记账失败',
$scope.accountStatusList = [{text:"全部",value:""},{text:"待入账",value:"0"},{text:"已记账",value:"1"},{text:"记账失败",value:"2"}];
//'支付方式：1 微信，2 支付宝，3 快捷'
$scope.payMethodList = [{text:"全部",value:""},{text:"微信",value:"1"},{text:"支付宝",value:"2"},{text:"快捷支付",value:"3"},{text:"红包账户",value:"4"},{text:"分润账户",value:"5"}];

//收款通道
$scope.payChannelStatusList = [{text:"全部",value:""},{text:"微信官方",value:"wx"},{text:"支付宝官方",value:"ali"},{text:"中钢银通",value:"kj"}];//通道名称

$scope.infoDetail={};
$http.post("manor/transactionRecoreOne","orderId="+$stateParams.orderId,
        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function(data){
            if(data.status){
            	$scope.infoDetail = data.data.infoDetail;
            }else{
                $scope.notice(data.msg);
            }
});

});
