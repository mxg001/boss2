/**
 * 超级推商品详情
 */
angular.module('inspinia').controller("detailCjtGoods", function($scope, $http,$stateParams) {
    //查询
    $scope.descImgUrlList = [];

    $scope.query = function(){
        $scope.submitting = true;
        $http({
            url:"cjtGoods/selectCjtGoods/" + $stateParams.id,
            method:"get"
        }).success(function(result){
            $scope.submitting = false;
            if (!result || !result.status){
                $scope.notice (result.msg);
                return;
            }
            $scope.baseInfo = result.data;
            if($scope.baseInfo.descImgUrl != null && $scope.baseInfo.descImgUrl != ""
             && $scope.baseInfo.descImgUrl != "undefined") {
                $scope.descImgUrlList = $scope.baseInfo.descImgUrl.split(",");
            }
        }).error(function(){
            $scope.submitting = false;
            $scope.notice("服务器异常");
        });
    };
    $scope.query();
});