/**
 * 核销渠道编辑
 */
angular.module('inspinia').controller('excRouteEditCtrl',function($scope,$http,i18nService,$state,$stateParams){

    i18nService.setCurrentLang('zh-cn');  //设置语言为中文

    $scope.prioritySelect = [{text:"全部",value:""},{text:"A",value:"A"},{text:"B",value:"B"},
        {text:"C",value:"C"},{text:"D",value:"D"},{text:"E",value:"E"},{text:"F",value:"F"},{text:"G",value:"G"}];
    $scope.prioritySelectStr=angular.toJson($scope.prioritySelect);

    //清空
    $scope.clear=function(){
        $scope.addInfo={priority:""};
    };
    $scope.clear();

    $http.post("excRoute/getRoute","id="+$stateParams.id,
        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function(data){
            if(data.status){
                $scope.addInfo=data.info;
            }
        });

    $scope.submitting = false;
    //提交
    $scope.commitInfo=function () {
        $scope.submitting = true;
        $http.post("excRoute/updateRoute","info="+angular.toJson($scope.addInfo),
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                $scope.submitting = false;
                if(data.status){
                    $scope.notice(data.msg);
                    $state.transitionTo('exchange.route',null,{reload:true});
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