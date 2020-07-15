/**
 * 核销渠道新增
 */
angular.module('inspinia').controller('routeAddCtrl',function($scope,$http,i18nService,$state){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文

    $scope.prioritySelect = [{text:"全部",value:""},{text:"A",value:"A"},{text:"B",value:"B"},
        {text:"C",value:"C"},{text:"D",value:"D"},{text:"E",value:"E"},{text:"F",value:"F"},{text:"G",value:"G"}];
    $scope.prioritySelectStr=angular.toJson($scope.prioritySelect);

    //清空
    $scope.clear=function(){
        $scope.addInfo={priority:""};
    };
    $scope.clear();

    $scope.submitting = false;
    //新增
    $scope.commitInfo=function () {
        $scope.submitting = true;
        $http.post("excActRoute/addRoute","info="+angular.toJson($scope.addInfo),
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                $scope.submitting = false;
                if(data.status){
                    $scope.notice(data.msg);
                    $state.transitionTo('exchangeActivate.route',null,{reload:true});
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