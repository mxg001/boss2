/**
 * 行用卡通道管理
 */
angular.module('inspinia',['infinity.angular-chosen','uiSwitch']).controller('failWarnCtrl',function($scope,$http,$state,$stateParams,i18nService,$document){
	//数据源
	i18nService.setCurrentLang('zh-cn');

	$scope.statuses = [{text:"开启",value:'1'},{text:"关闭",value:'0'}];
    $scope.info={};

    //显示
    $scope.query = function () {
        $http.get('warn/selectWarnInfoByCode?code=channelFailWarn').success(
            function (data) {
                if(data.status){
                    $scope.info = data.info;
                }else {
                    $scope.notice("获取配置信息失败");
                }
        });
    };
    $scope.query();

    //修改
    $scope.update = function () {
        if(!isPositiveNum($scope.info.warnTriggerValue)){
            $scope.notice("失败上限条数必须是正整数");
            return;
        }
        if(!isPositiveNum($scope.info.warnCountTime)){
            $scope.notice("失败统计间隔分钟数必须是正整数");
            return;
        }
        if(isBlank($scope.info.warnPhone)){
            $scope.notice("短信告知人必须填写");
            return;
        }
        $http.post('warn/update', $scope.info).success(function (data) {
            if(data.status){
                $scope.notice("提交成功");
            }else {
                $scope.notice(data.msg);
            }

        });
    }

    //验证是否为正整数
    isPositiveNum = function (s) {
        var re = /^[0-9]+$/;
        if(re.test(s)){
            return parseInt(s)>0;
        }
        return false;
    }

    //参数非空判断
    isBlank = function (param) {
        if(param=="" || param==null ){
            return true;
        }else{
            return false;
        }
    }


});