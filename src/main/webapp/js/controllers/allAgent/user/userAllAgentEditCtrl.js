/**
 * 盟主编辑
 */
angular.module('inspinia').controller('userAllAgentEditCtrl',function($scope,$http,i18nService,$state,$stateParams){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文

    $scope.idCardNoStateSelect = [{text:"未完成认证",value:0},{text:"已完成认证",value:1}];
    $scope.idCardNoStateStr=angular.toJson($scope.idCardNoStateSelect);

    $scope.userTypeSelect = [{text:"全部",value:""},{text:"机构",value:1},{text:"大盟主",value:2},{text:"盟主",value:3}];
    $scope.userTypeStr=angular.toJson($scope.userTypeSelect);

    $scope.gradeSelect = [{text:"普通盟主",value:"0"},{text:"黄金盟主",value:"1"},
        {text:"铂金盟主",value:"2"},{text:"黑金盟主",value:"3"},{text:"钻石盟主",value:"4"}];

    $scope.agentShareLevel=[];
    for(var i=1;i<=20;i++){
        $scope.agentShareLevel.push({value:i,text:'Lv.'+i});
    }

    //清空
    $scope.clear=function(){
        $scope.info={};
    };
    $scope.clear();

    $http.post("userAllAgent/getUserAllAgent","id="+$stateParams.id,
        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function(data){
            if(data.status){
                $scope.info=data.user;
            }
        });

    $scope.submitting = false;
    //保存
    $scope.saveUserAllAgent=function () {
        if($scope.submitting){
            return;
        }
        $scope.submitting = true;
        $http.post("userAllAgent/saveUserAllAgent","info="+angular.toJson($scope.info),
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                $scope.submitting = false;
                if(data.status){
                    $scope.notice(data.msg);
                    $state.transitionTo('allAgent.allyManage',null,{reload:true});
                }else{
                    $scope.notice(data.msg);
                }
            })
            .error(function(data){
                $scope.submitting = false;
                $scope.notice(data.msg);
            });
    };
});