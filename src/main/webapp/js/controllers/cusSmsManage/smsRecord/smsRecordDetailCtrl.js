/**
 * 发送记录详情
 */
angular.module('inspinia').controller('smsRecordDetailCtrl',function($scope,$http,i18nService,$state,$stateParams){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文

    //业务类型
    $scope.typeSelect =angular.copy($scope.smsBusinessTypeList);
    $scope.typeSelect.unshift({text:"全部",value:""});
    $scope.typeStr=angular.toJson($scope.typeSelect);

    $scope.getSmsRecordInfo=function(){
        $http.post("cusSmsRecord/getCusSmsRecordInfo",
            "id="+$stateParams.id,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.info=data.info;
                }
            });
    };
    $scope.getSmsRecordInfo();

    /**
     * 获取敏感数据
     */
    $scope.dataSta=true;
    $scope.getDataProcessing = function () {
        if($scope.dataSta){
            $http.post("cusSmsRecord/getDataProcessing",
                "id="+$stateParams.id,
                {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                .success(function(data){
                    if(data.status){
                        $scope.info.mobilePhone=data.info.mobilePhone;
                        $scope.dataSta=false;
                    }
                });
        }
    };
});