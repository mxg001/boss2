/**
 * 超级银行家贷款订单详情
 */
angular.module('inspinia').controller('inquiryOrderDetailCtrl',function($scope, $http, $state,$stateParams,$sce){
    $http({
        url:"superBank/orderInquiryDetail?orderNo=" + $stateParams.orderNo,
        method:"GET"
    }).success(function(result){
        if (result.status){
            $scope.baseInfo = result.data;
            var reportUrl=result.data.reportUrl;
            if(reportUrl!=null||reportUrl == "" ){
                $scope.someUrl = $sce.trustAsResourceUrl(reportUrl);
            }else{
                document.getElementById("report").style.display="none";
            }
        } else {
           $scope.notice(result.msg);
        }
    }).error(function () {
        $scope.notice("服务器异常，请稍后再试");
    })
});