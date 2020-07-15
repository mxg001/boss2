/**
 * 信用卡管家-还款详情
 */
angular.module('inspinia').controller('repayDetailCtrl',function($scope, $http, $state,$stateParams){

	$scope.payWaySelect = [{text:"手工标记",value:'1'},{text:"超级还-分期",value:'2'},{text:"超级还-全额",value:'3'},{text:"超级还-完美",value:'4'}];
	$scope.billStatusSelect = [{text:"未还款",value:'0'},{text:"已还款",value:'1'}];

	$scope.info={};

	$http({
        url:"cmRepay/selectRepayInfoById?id="+$stateParams.id,
        method:"GET"
    }).success(function(data){
        if (data.status){
            $scope.info = data.info;
        } else {
        	$scope.notice(data.msg);
        }
    }).error(function () {
        $scope.notice("服务器异常，请稍后再试");
    });

    /**
     * 获取敏感数据
     */
    $scope.dataSta=true;
    $scope.getDataProcessing = function () {
        if($scope.dataSta){
            $http.get('cmRepay/getDataProcessing?id='+$stateParams.id)
                .success(function(data) {
                    if(data.status){
                        $scope.info.mobileNo = data.info.mobileNo;
                        $scope.dataSta=false;
                    }else{
                        $scope.notice(data.msg);
                    }
                });
        }
    };
});