/**
 * 银行详情
 */
angular.module('inspinia').controller("creditcardSourceDetailCtrl", function($scope, $http, $state, $stateParams) {
    // $scope.cardTypeList = [{text:"普通卡",value:"1"},{text:"校园卡",value:"2"}];
	$scope.accessMethodsList = [{text:"H5",value:"H5"},{text:"API",value:"API"}];
    //数据源
    $http({
        url:"creditcardSource/detail/" + $stateParams.id,
        method:"GET"
    }).success(function(result){
        if(result && result.status){
            $scope.baseInfo = result.data;
        } else {
            $scope.notice(result.msg);
        }
    });

});
