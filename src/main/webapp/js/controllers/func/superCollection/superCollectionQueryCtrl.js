/**
 * 查询公告
 */
angular.module('inspinia').controller("superCollectionQueryCtrl",function($scope,$http,$state,i18nService,SweetAlert) {
    //数据源
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.info = {startTime:null,endTime:null,dayLines:null};
    $scope.modifyInfo={startTime:null,endTime:null,dayLines:null};
    $scope.sumlines=0;
    $scope.submitting=false;
    //获取数据
    $scope.getData=function () {
        $http.get('superCollection/selectInfo')
            .success(function(msg){
                if(msg.status){
                    $scope.info = msg.info;
                    $scope.sumlines=msg.sumlines;
                }else{
                    $scope.notice(msg.msg);
                }
            })
            .error(function () {
                $scope.info = {startTime:null,endTime:null,dayLines:null};
                $scope.notice(msg.msg);
            });
    }
    $scope.getData();


    //修改
    $scope.hardWardAddModel = function(){
        $scope.modifyInfo.startTime=$scope.info.startTime;
        $scope.modifyInfo.endTime=$scope.info.endTime;
        $scope.modifyInfo.dayLines=$scope.info.dayLines;
        $("#hardWardAddModel").modal("show");
    }
    $scope.cancel=function(){
        $('#hardWardAddModel').modal('hide');
    }

    //修改交易控制
    $scope.saveInfo = function(){
        if($scope.modifyInfo.startTime==null||$scope.modifyInfo.startTime==""||$scope.modifyInfo.endTime==null||$scope.modifyInfo.endTime==""){
            $scope.notice("交易时间区间不能为空!");
            return;
        }
        if($scope.modifyInfo.endTime<$scope.modifyInfo.startTime){
            $scope.notice("交易时间结束时间不能小于开始时间!");
            return;
        }
        if($scope.modifyInfo.dayLines==null||$scope.modifyInfo.dayLines==""){
            $scope.notice("每日额度不能为空!");
            return;
        }
        if($scope.submitting){
           return;
        }
        $scope.submitting = true;
        $http.post("superCollection/saveInfo","info="+angular.toJson($scope.modifyInfo),
            {headers: {'Content-Type':'application/x-www-form-urlencoded'}})
            .success(function(msg){
                if(msg.status){
                    $scope.info.startTime=$scope.modifyInfo.startTime;
                    $scope.info.endTime=$scope.modifyInfo.endTime;
                    $scope.info.dayLines=$scope.modifyInfo.dayLines;
                    $scope.notice(msg.msg);
                }else{
                    $scope.notice(msg.msg);
                }
                $scope.submitting=false;
                $scope.cancel();
            })
            .error(function () {
                $scope.notice(msg.msg);
                $scope.submitting=false;
                $scope.cancel();
            });
    }
});
