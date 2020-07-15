/**
 * 添加公告
 */
angular.module('inspinia').controller("addCreditRepayNoticeCtrl", function($scope, $http,$state,i18nService) {
    //数据源
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.noticeInfo = {title:null,link:null};

    $scope.submit = function(){
        $scope.submitting = true;
        var data = {"notice":$scope.noticeInfo};
        $http.post("creditRepayNotice/saveNotice.do",angular.toJson(data))
            .success(function(msg){
                if(msg.status){
                    $scope.notice(msg.msg);
                    $state.transitionTo('creditRepay.addNotice',null,{reload:true});
                    $scope.submitting = false;
                } else {
                    $scope.notice(msg.msg);
                    $scope.submitting = false;
                }
            })
            .error(function() {
                $scope.notice(msg.msg);
                $scope.submitting = false;
            }
        );
    };
});
