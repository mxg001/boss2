/**
 * 短信模板发送
 */
angular.module('inspinia').controller('smsTemplateSendCtrl',function($scope,$http,i18nService,$state,$stateParams){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文

    //发送类型
    $scope.sendTypeSelect =[{text:"手机号码",value:"1"},{text:"商户编号",value:"2"}];
    $scope.sendTypeStr=angular.toJson($scope.sendTypeSelect);

    $scope.addInfo={sendType:"1",templateId:$stateParams.id};

    $scope.getSmsRecordInfo=function(){
        $http.post("cusSmsTemplate/getSmsTemplateInfo",
            "id="+$stateParams.id,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.info=data.info;
                }
            });
    };
    $scope.getSmsRecordInfo();

    $scope.resultSta=false;
    $scope.resultShow=function (sta) {
        $scope.resultSta=sta;
    };
    $scope.submitCheck=function(){
        var sendStr=$scope.addInfo.sendStr;
        if(sendStr!=null&&sendStr!=""){
            if(sendStr.indexOf("，") != -1){
                sendStr=sendStr.replace(/，/ig,',');
            }
            if(sendStr.indexOf(",") != -1){
                var strs=sendStr.split(",");
                if(strs!=null&&strs.length>100){
                    $scope.notice("最多只能输入100个手机号码/商户编号,超出" + (strs.length-100) +  "个!");
                    return false;
                }
            }
        }
        return true;
    };
    $scope.submitting=false;
    $scope.submitInfo=function () {
        if($scope.submitting){
            return;
        }
        //校验
        if(!$scope.submitCheck()) {
            return;
        }
        $scope.submitting=true;
        $scope.subInfo=angular.copy($scope.addInfo);
        if($scope.addInfo.sendStr.indexOf("，") != -1){
            $scope.subInfo.sendStr=$scope.addInfo.sendStr.replace(/，/ig,',');
        }
        var data={
            info:angular.toJson($scope.subInfo),
        };
        var postCfg = {
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            transformRequest: function (data) {
                return $.param(data);
            }
        };
        $http.post("cusSmsTemplate/sendTemplate",data,postCfg)
            .success(function(data){
                if(data.status){
                    $scope.notice(data.msg);
                    if(data.errorMsg!=null&&data.errorMsg!=""){
                        var reg=new RegExp("<br>","g");
                        $scope.errorMsg=data.errorMsg.replace(reg,"\r\n");
                    }
                    $scope.resultShow(true);
                }else{
                    $scope.notice(data.msg);
                }
                $scope.submitting=false;
            })
            .error(function(data){
                $scope.notice(data.msg);
                $scope.submitting=false;
            });
    };
});