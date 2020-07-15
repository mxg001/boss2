/**
 * 产品编辑、详情
 */
angular.module('inspinia').controller('exchangeActivateProductEditCtrl',function($scope,$http,i18nService,$stateParams,$state){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.addinfo={orgCode:"",typeCode:""};
    $http.post("exchangeActivateProduct/getExchangeProduct","id="+$stateParams.id,
        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function(data){
            if(data.status){
                $scope.addinfo=data.pro;
                $scope.getProductTypeList($scope.addinfo.orgCode);
            }
        });

    $scope.orgList=[];
    //获取机构
    $http.post("exchangeActivateOrg/getOrgManagementList")
        .success(function(data){
            if(data.status){
                $scope.orgList.push({value:"",text:"全部"});
                var orgList=data.list;
                if(orgList!=null&&orgList.length>0){
                    for(var i=0; i<orgList.length; i++){
                        $scope.orgList.push({value:orgList[i].orgCode, text:orgList[i].orgName});
                    }
                }
            }
        });
    //获取产品类别
    $scope.getProductTypeList=function (orgCode) {
        $http.post("productTypeActivate/getProductTypeList","orgCode="+orgCode,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.typeList=[];
                    $scope.typeList.push({value:"",text:"全部"});
                    var typeList=data.list;
                    if(typeList!=null&&typeList.length>0){
                        for(var i=0; i<typeList.length; i++){
                            $scope.typeList.push({value:typeList[i].typeCode, text:typeList[i].typeName});
                        }
                    }
                }
            });
    }
    //机构变更事件
    $scope.changeOrgCode=function () {
        if($scope.addinfo.orgCode!=null&&$scope.addinfo.orgCode!=""){
            $scope.getProductTypeList($scope.addinfo.orgCode);
            $scope.addinfo.typeCode="";
        }
    };

    $scope.submitting = false;
    //保存
    $scope.saveProduct=function () {
        if($scope.submitting){
            return;
        }
        $scope.submitting = true;
        $http.post("exchangeActivateProduct/updateExchangeProduct","info="+angular.toJson($scope.addinfo),
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                $scope.submitting = false;
                if(data.status){
                    $scope.notice(data.msg);
                    $state.transitionTo('exchangeActivate.product',null,{reload:true});
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