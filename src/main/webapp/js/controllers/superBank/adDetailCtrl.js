/**
 * banner广告新增
 */
angular.module('inspinia').controller('adDetailCtrl',function($scope, $http, $state,$stateParams){

	//返回上页
    $scope.goback = function () {
    	history.go(-1);
    };
    $scope.applyTypeList = [{value:"1",text:"App+公众号"},{value:"2",text:"App"},{value:"3",text:"公众号"}];
    $scope.postionTypeList = [{value:"1",text:"首页"},{value:"2",text:"办卡查询"},{value:"3",text:"贷款申请"},{value:"4",text:"信用卡列表位置"}];
    $scope.detailData = {};
    $http({
        url:"superBank/adDetail?id="+$stateParams.id+"&isUpd=0",
        method:"GET"
    }).success(function(result){
        if (result.status){
            $scope.detailData = result.data;
        } else {
           $scope.notice(result.msg);
        }
    }).error(function () {
        $scope.notice("服务器异常，请稍后再试");
    });
   
});