/**
 * 广告新增
 */
angular.module('inspinia').controller('exchangeActivateHelpCenterEditCtrl',function($scope,$http,i18nService,$state,$stateParams){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文

    $scope.categorySelect = [{text:"全部",value:""},{text:"使用教程",value:"1"},{text:"常见问题",value:"2"}];
    $scope.categoryStr=angular.toJson($scope.categorySelect);

    $scope.modelSelect = [{text:"全部",value:""},{text:"兑换视频教程",value:"1"},{text:"图文教程",value:"2"},
        {text:"常见问题",value:"3"}];

    $scope.addInfo={category:"",model:""};

    $scope.modelShow=true;
    $scope.changeCategory=function () {
        if($scope.addInfo.category!=null&&$scope.addInfo.category!=null){
            if($scope.addInfo.category=="1"){
                $scope.modelSelect=[{text:"全部",value:""},{text:"兑换视频教程",value:"1"},{text:"图文教程",value:"2"}];
            }else if($scope.addInfo.category=="2"){
                $scope.modelSelect=[{text:"全部",value:""},{text:"常见问题",value:"3"}];
            }
            $scope.modelShow=false;
        }else{
            $scope.modelSelect = [{text:"全部",value:""},{text:"兑换视频教程",value:"1"},{text:"图文教程",value:"2"},
                {text:"常见问题",value:"3"}];
            $scope.modelShow=true;
        }
    };

    $http.post("exchangeActivateHelpCenter/getHelpCenter","id="+$stateParams.id,
        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function(data){
            if(data.status){
                $scope.addInfo=data.help;
                $scope.changeCategory();
            }
        });

    $scope.submitting = false;
    //新增banner
    $scope.addData = function(){
        if($scope.submitting){
            return;
        }
        $scope.submitting = true;
        $scope.subAddInfo = angular.copy($scope.addInfo);
        if($scope.subAddInfo.link!=null&&$scope.subAddInfo.link!=""){
            $scope.subAddInfo.link=encodeURIComponent($scope.subAddInfo.link);
        }
        $http.post("exchangeActivateHelpCenter/updateHelpCenter",
            "info="+angular.toJson($scope.subAddInfo),
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                $scope.submitting = false;
                if(data.status){
                    $scope.notice(data.msg);
                    $state.transitionTo('exchangeActivate.helpCenter',null,{reload:true});
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