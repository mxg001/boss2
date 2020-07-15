/**
 * oem产品修改
 */
angular.module('inspinia',['infinity.angular-chosen']).controller('exchangeActivateOemProductEditCtrl',function($scope,$http,i18nService,$state,$stateParams,$timeout){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.oemNoOne=$stateParams.oemNo;
    $scope.addinfo={oemNo:"",orgCode:"",typeCode:"",pId:""};
    $scope.selectState={type:true,pro:true};
    $scope.oemList=[{value:"",text:"全部"}];
    //组织列表
    $http.post("exchangeActivateOem/getOemList",
        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function(data){
            if(data.status){
                var list=data.list;
                if(list!=null&&list.length>0){
                    for(var i=0; i<list.length; i++){
                        $scope.oemList.push({value:list[i].oemNo,text:list[i].oemName});
                    }
                }
            }else{
                $scope.notice(data.msg);
            }
        });
    //获取机构
    $scope.orgList=[{value:"",text:"全部"}];
    $http.post("exchangeActivateOrg/getOrgManagementList",
        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function(data){
            if(data.status){
                var list=data.list;
                if(list!=null&&list.length>0){
                    for(var i=0; i<list.length; i++){
                        $scope.orgList.push({value:list[i].orgCode,text:list[i].orgName});
                    }
                }
            }else{
                $scope.notice(data.msg);
            }
        });

    $scope.changeOrgCode=function () {
        if($scope.addinfo.orgCode!=null&&$scope.addinfo.orgCode!=""){
            $scope.selectState={type:true,pro:false};
            $scope.getProductTypeList($scope.addinfo.orgCode);
        }else{
            $scope.selectState={type:false,pro:false};
        }
        $scope.addinfo.typeCode="";
        $scope.addinfo.pId="";
    }

    $scope.typeList=[{value:"",text:"全部"}];
    //获取产品类别
    $scope.getProductTypeList=function (orgCode) {
        $http.post("productTypeActivate/getProductTypeList","orgCode="+orgCode,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.typeList=[];
                    $scope.typeList.push({value:"",text:"全部"});
                    var list=data.list;
                    if(list!=null&&list.length>0){
                        for(var i=0; i<list.length; i++){
                            $scope.typeList.push({value:list[i].typeCode, text:list[i].typeName});
                        }
                    }
                }
            });
    };
    $scope.changeTypeCode=function () {
        if($scope.addinfo.typeCode!=null&&$scope.addinfo.typeCode!=""){
            $scope.selectState={type:true,pro:true};
            $scope.getProductList("");
        }else{
            $scope.selectState={type:true,pro:false};
        }
        $scope.addinfo.pId="";
    };
    //获取产品
    $scope.productList=[{value:"",text:"全部"}];
    $scope.getProductList=function (name) {
        if($scope.addinfo.typeCode!=null&&$scope.addinfo.typeCode!=""){
            $http.post("exchangeActivateProduct/getProductList","name="+name+"&typeCode="+$scope.addinfo.typeCode,
                {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                .success(function(data){
                    if(data.status){
                        $scope.productList=[{value:"",text:"全部"}];
                        var list=data.list;
                        if(list!=null&&list.length>0){
                            for(var i=0; i<list.length; i++){
                                $scope.productList.push({value:list[i].id,text:list[i].productName});
                            }
                        }
                    }
                });
        }else{
            $scope.productList=[{value:"",text:"全部"}];
        }
    };
    //动态筛选产品
    $scope.getStates =getStates;
    var oldValue="";
    var timeout="";
    function getStates(value) {
        var newValue=value;
        if(newValue != oldValue){
            if (timeout) $timeout.cancel(timeout);
            timeout = $timeout(
                function(){
                    $scope.getProductList(value);
                },800);
        }
    };
    $scope.productChange=function () {
        if($scope.addinfo.pId!=null&&$scope.addinfo.pId!=null){
            $http.post("exchangeActivateProduct/getExchangeProduct","id="+$scope.addinfo.pId,
                {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                .success(function(data){
                    if(data.status){
                        $scope.proInfo=data.pro;
                    }else{
                        $scope.notice(data.msg);
                    }
                });
        }
    };
    $http.post("exchangeActivateOem/getProductOemDetail","id="+$stateParams.id,
        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function(data){
            $scope.submitting = false;
            if(data.status){
                $scope.addinfo=data.proOem;
                $scope.getProductTypeList($scope.addinfo.orgCode);
                $scope.getProductList($scope.addinfo.productName);
                $scope.productChange();
            }else{
                $scope.notice(data.msg);
            }
        })

    $scope.submitting = false;
    //保存
    $scope.saveOemProduct=function () {
        if($scope.submitting){
            return;
        }
        $scope.submitting = true;
        $http.post("exchangeActivateOem/updateProductOem","info="+angular.toJson($scope.addinfo),
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                $scope.submitting = false;
                if(data.status){
                    $scope.notice(data.msg);
                    $state.transitionTo('exchangeActivate.productOem',{oemNo:$stateParams.oemNo},{reload:true});
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