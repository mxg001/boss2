/**
 * 公告详情
 */
angular.module('inspinia').controller('cmNoticeDetailCtrl',function($scope,$http,$state,$stateParams){

	//获取所有组织
 	$http.post("cmUserManage/selectOrgAllInfo")
 		.success(function(msg){
 			$scope.orgInfoList = msg;
 			angular.forEach($scope.orgInfoList, function(item){
 				item.checkStatus = 0;
 			});
 			$scope.getBaseInfo();
 		}).error(function(){
 			$scope.notice("获取组织信息异常");
 		});

    $scope.getBaseInfo = function(){
        $http({
            url:"cmNotice/queryNoticeById?id=" + $stateParams.id,
            method:"GET"
        }).success(function(data){
            $scope.baseInfo = data.info;
            if($scope.baseInfo.orgId != null){
                if($scope.baseInfo.orgId == -1){
                    $scope.orgAll = 1;
                } else {
                    angular.forEach($scope.orgInfoList, function(item){
                        if($scope.baseInfo.orgId.indexOf(item.orgId)>-1){
                            item.checkStatus = 1;
                        }
                    });
                }
            }
        });
    }
});