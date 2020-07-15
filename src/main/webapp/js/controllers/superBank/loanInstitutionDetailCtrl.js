/**
 * 贷款机构详情
 */
angular.module('inspinia').controller("loanInstitutionDetailCtrl", function($scope, $http, $state, $stateParams) {
    //数据源
    $http({
        url:"loanInstitutionManagement/detail/" + $stateParams.id,
        method:"GET"
    }).success(function(result){
        if(result && result.status){
            $scope.baseInfo = result.data;
            $scope.rewardRequirementsList = [{text:"有效注册",value:"1"},{text:"授信成功",value:"3"},{text:"有效借款",value:"2"}];//奖励要求
            $scope.accessWayList = [{text:"H5",value:1},{text:"API",value:2}];//接入方式
        } else {
            $scope.notice(result.msg);
        }
    });

});
