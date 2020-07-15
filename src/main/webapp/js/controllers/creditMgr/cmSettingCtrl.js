/**
 * 信用卡管家设置
 */
angular.module('inspinia',['uiSwitch']).controller('cmSettingCtrl',function($scope,$http,$state,$document,SweetAlert){

	$scope.info = {vipCharge:1,cardLimit:1,agentComfig:0};

    $scope.query = function () {
        $http({
            url: 'cmSetting/selectVipConfigByType',
            method: 'GET'
        }).success(function (data) {
            if (data.status) {
                $scope.info = data.info;
            } else {
                $scope.notice(data.msg);
            }
        }).error(function (msg) {
            $scope.notice('服务器异常,请稍后再试.');
        });
    }
    $scope.query();

	$scope.update = function () {
		if ($scope.submitting) {
			return;
		}
		var zzs = /^[0-9]*[1-9][0-9]*$/;//判断正整数
		var zs = /^\d+(?=\.{0,1}\d+$|$)/;//判断正数
		if($scope.info.cardLimit){
			if(!zzs.test($scope.info.cardLimitNum)||$scope.info.cardLimitNum<1||$scope.info.cardLimitNum>20){
                $scope.notice("添卡数量上限必须大于0，小于20的整数");
                return;
			}
		}else{
            $scope.info.cardLimitNum=5;
		}
        if($scope.info.vipCharge==true){
            $scope.info.vipCharge=1;
        } else if($scope.info.vipCharge==false){
            $scope.info.vipCharge=0;
        }
        if($scope.info.cardLimit==true){
            $scope.info.cardLimit=1;
        } else if($scope.info.cardLimit==false){
            $scope.info.cardLimit=0;
        }
        if($scope.info.agentComfig==true){
            $scope.info.agentComfig=1;
        } else if($scope.info.agentComfig==false){
            $scope.info.agentComfig=0;
        }
	    if (!zs.test($scope.info.vipFee) || $scope.info.vipFee=="0") {
	    	$scope.notice("会员服务费必须为正数");
	    	return;
	    }
	    if (!zzs.test($scope.info.validPeriod)) {
	    	$scope.notice("有效期必须为正整数");
	    	return;
	    }
	    if (!zs.test($scope.info.agentShare)) {
	    	$scope.notice("代理商分润必须为正数");
	    	return;
	    }
		$scope.submitting = true;
		$http({
			url: 'cmSetting/updateVipConfig',
			data: $scope.info,
			method:'POST'
		}).success(function (data) {
			$scope.submitting = false;
			$scope.notice(data.msg);
            $scope.query();
		}).error(function (msg) {
			$scope.notice('服务器异常,请稍后再试.');
			$scope.submitting = false;
		});
	};
});