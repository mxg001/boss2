/**
 * 信用卡管家-banner详情
 */
angular.module('inspinia').controller('bannerDetailCtrl',function($scope, $http, $state,$stateParams){

	//返回上页
    $scope.goback = function () {
    	history.go(-1);
    };

    positionTypeSelect = [{text:"不限位置",value:'0'},{text:"H5",value:'1'}];

    $scope.ad = {};

    guoLv = function (select,value) {
    	for(var i = 0; i < select.length; i++) {
    		if(select[i].value == value) {
    			return select[i].text;
    		}
    	}
    }

    //获取所有组织
	org=[{value:"-1",text:"所有组织"}];
	$http.post("cmUserManage/selectOrgAllInfo")
		.success(function(msg){
			//响应成功
			for(var i=0; i<msg.length; i++){
				org.push({value:msg[i].orgId, text:msg[i].orgId + " " + msg[i].orgName});
			}
			query();
		}).error(function () {
	        $scope.notice("服务器异常，请稍后再试");
	    });

	query = function () {
		$http({
	        url:"cmBanner/queryBannerById?id="+$stateParams.id,
	        method:"GET"
	    }).success(function(data){
	        if (data.status){
	            $scope.ad = data.info;
	            $scope.ad.beginTime = moment(new Date($scope.ad.beginTime).getTime()).format('YYYY-MM-DD HH:mm:ss');
	            $scope.ad.endTime = moment(new Date($scope.ad.endTime).getTime()).format('YYYY-MM-DD HH:mm:ss');
	            $scope.ad.positionType = guoLv(positionTypeSelect,$scope.ad.positionType);
	            $scope.ad.orgId = guoLv(org,$scope.ad.orgId);
	        } else {
	        	$scope.notice(data.msg);
	        }
	    }).error(function () {
	        $scope.notice("服务器异常，请稍后再试");
	    });
	}

});