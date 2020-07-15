/**
 * 定时任务监控编辑/详情
 */
angular.module('inspinia').controller('detailTimedTaskCtrl',function($scope,$http,$stateParams,i18nService,$state){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.taskStatusSelect=[{text:"全部",value:''},{text:"不存在",value:'NONE'},{text:"正常",value:'NORMAL'},
        {text:"暂停",value:'PAUSED'},{text:"完成",value:'COMPLETE'},{text:"错误",value:'ERROR'},{text:"阻塞",value:'BLOCKED'}];
    $scope.enabledStateSelect=[{text:"全部",value:-1},{text:"未启动",value:0},{text:"启动",value:1}];
    $scope.warningStateSelect=[{text:"全部",value:-1},{text:"否",value:0},{text:"是",value:1}];
    $scope.taskStatusStr=angular.toJson($scope.taskStatusSelect);
    $scope.enabledStateStr=angular.toJson($scope.enabledStateSelect);
    $scope.warningStateStr=angular.toJson($scope.warningStateSelect);

    $scope.info={};

    $scope.query=function () {
        $http.post("timedTask/detailTimedTask","id=" + $stateParams.id,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.info=data.info;
                }else{
                    $scope.notice(data.msg);
                }
            });
    }
    $scope.query();

    $scope.submit=function () {
        if($scope.info.warningState==1){
            if($scope.info.errorWarningState!=1&&$scope.info.overtimeWarningState!=1){
                $scope.notice("请选择预警条件!");
                return;
            }
            if($scope.info.overtimeWarningState==1){
                if($scope.info.earlyWarningThreshold==null||$scope.info.earlyWarningThreshold==""){
                    $scope.notice("超时阀值不能为空!");
                    return;
                }
            }
        }
        $http.post("timedTask/editSubmit","info="+angular.toJson($scope.info),
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.notice(data.msg);
                    $state.transitionTo('sys.timedTask',null,{reload:true});
                }else{
                    $scope.notice(data.msg);
                }
            });
    }
})