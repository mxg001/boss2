/**
 * 订单类型详情查看
 */
angular.module('inspinia').controller('typeDetailCtrl',function($scope,$http,$state,$stateParams){

	//数据源
	$scope.repayType={};
    $scope.paginationOptions = {pageNo : 1,pageSize : 10};

    $scope.query = function () {
        $http.get('repayType/queryTypeDetailById?id='+$stateParams.id)
            .success(function(data) {
                if(data.status){
                    $scope.repayType = data.repayType;
                }else{
                    $scope.notice(data.msg);
                }
            });
    }
    $scope.query();

});