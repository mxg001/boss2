/**
 * Created by Administrator on 2017/10/26/026.
 */
/**
 * 公告详情
 */
angular.module('inspinia').controller("detailCreditRepayNoticeCtrl", function($scope, $http,$state,$stateParams,i18nService) {
    //数据源
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.noticeInfo = {title:null,link:null};


    $http.get('creditRepayNotice/selectById/'+$stateParams.id)
        .success(function(msg){
            $scope.noticeInfo = msg.notice;
        })
        .error(function () {
            $scope.noticeInfo ={title:null,link:null};
            $scope.notice(msg.msg);
        });

});

