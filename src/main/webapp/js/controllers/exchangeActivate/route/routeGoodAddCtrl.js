/**
 * 渠道商品新增
 */
angular.module('inspinia',['infinity.angular-chosen']).controller('routeGoodAddCtrl',function($scope,$http,i18nService,$state,$stateParams,$timeout){

    i18nService.setCurrentLang('zh-cn');  //设置语言为中文

    $scope.goodModeSelect = [{text:"全部",value:""},{text:"文字报单",value:"1"},{text:"图片报单",value:"2"}];
    $scope.goodModeStr=angular.toJson($scope.goodModeSelect);

    //清空
    $scope.clear=function(){
        $scope.addInfo={channelNo:$stateParams.channelNo,goodMode:""};
    };
    $scope.clear();


    //获取产品
    $scope.getProductList=function (val) {
        $http.post("exchangeActivateProduct/productListSelect","val="+val,
            {headers: {'Content-Type':'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.productList=[];
                    $scope.productList.push({value:"",text:"全部"});
                    var list=data.list;
                    if(list!=null&&list.length>0){
                        for(var i=0; i<list.length; i++){
                            $scope.productList.push({value:list[i].id,text:"["+list[i].id+"]"+list[i].productName});
                        }
                    }
                }
            });
    }
    $scope.getProductList("");

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

    $scope.submitting = false;
    //提交
    $scope.commitInfo=function () {
        $scope.submitting = true;
        $http.post("excActRoute/addGood","info="+angular.toJson($scope.addInfo),
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