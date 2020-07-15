/**
 * 实名认证预警
 */

angular.module('inspinia').controller('tradeWarningCtrl',function(i18nService,$scope,$http,$state,$stateParams,$compile,$filter,$log,$uibModal){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.info={};
    $scope.tradeWarning=[];
    $scope.overtimeWarning={};
    $scope.delIds=[];
    $http.get('transInfoAction/selectTradeWarning')
        .success(function(data) {
            if(data.status){
                $scope.info=data.info;
                $scope.tradeWarning=data.list;
            }else{
                $scope.notice(data.msg);
            }
        });

    $scope.delrule = function (val,item) {
        if ( $scope.tradeWarning.length===1){
            $scope.notice("最后一条交易预警不允许删除");
            return;
        }
        if(item.id){
            $scope.delIds.push(item.id)
        }
        $scope.tradeWarning.splice(val,1);
    }

    $scope.addrule = function () {
        let ruleCon = {"cycle":'',"num":'',"err_code":'',"id":''};
        $scope.tradeWarning.push(ruleCon)
    }

    //重复错误码判断
    function isRepeat(arr){
        var hash = {};
        for(var i in arr) {
            if(hash[arr[i].err_code])
                return true;
            hash[arr[i].err_code] = true;
        }
        return false;
    }

    $scope.updatetradeWarning=function(){
        if (isRepeat( $scope.tradeWarning)){
            $scope.notice("该刷卡交易错误预警已添加");
            return;
        }
        var data = {"info":$scope.info,"tradeWarning": $scope.tradeWarning,"delIds":$scope.delIds};
        $http.post("transInfoAction/updateTradeWarning", angular.toJson(data))
            .success(function (result) {
                if (result.status) {
                    $scope.notice("保存成功");
                    $state.transitionTo('trade.tradeQuery',null,{reload:true});
                } else {
                    $scope.notice("保存失败");
                }

            });
    };
});