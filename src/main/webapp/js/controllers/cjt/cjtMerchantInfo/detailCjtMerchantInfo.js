/**
 * 超级推商户详情
 */
angular.module('inspinia').controller('detailCjtMerchantInfo',function($scope,$http,$stateParams){
    // 商户类型:1-个人，2-个体商户，3-企业商户
    // $scope.merchantTypeList = [{text:"个人",value:"1"},{text:"个体商户",value:"2"},{text:"企业商户",value:"3"}];
    //查询超级推商户详情
    $scope.query = function(){
        $http.get('cjtMerchantInfo/detail?merchantNo='+$stateParams.merchantNo)
            .success(function(result){
                if(result.status){
                    $scope.baseInfo = result.data.baseInfo;
                    $scope.bpList = result.data.bpList;
                    $scope.tiList = result.data.tiPage.result;
                    $scope.tiTotal = result.data.tiPage.totalCount;
                    $scope.merchantCardInfo = result.data.merchantCardInfo;
                } else {
                    $scope.notice(result.msg);
                }
            });
    }
    $scope.query();


});

