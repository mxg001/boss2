/**
 * 添加公告
 */
angular.module('inspinia').controller("issueCreditRepayNoticeCtrl", function($scope, $http,$state,$stateParams,i18nService) {
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


    $scope.submit = function(){
        $scope.submitting = true;
        var data = {"notice":$scope.noticeInfo};
        $http.post("creditRepayNotice/issueNotice.do",angular.toJson(data))
            .success(function(msg){
                if(msg.status){
                    $scope.notice(msg.msg);
                    $state.transitionTo('creditRepay.notice',null,{reload:true});
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
