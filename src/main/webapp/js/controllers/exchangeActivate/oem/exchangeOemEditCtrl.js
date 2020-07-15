/**
 * oem新增
 */
angular.module('inspinia',['angularFileUpload','infinity.angular-chosen']).controller('exchangeActivateOemEditCtrl',function($scope,$http,i18nService,$state,FileUploader,$timeout,$stateParams){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文

    //清空
    $scope.clear=function(){
        $scope.addinfo={};
        $scope.agentOem={agentNo:""};
    };
    $scope.clear();

    //获取默认字段
    $http.post("exchangeActivateOem/getExchangeOem", "id="+$stateParams.id,
        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function(data){
            if(data.status){
                $scope.addinfo=data.oem;
                $scope.agentOem=data.agentOem;
                $scope.fileListDefault=data.fileListDefault;
                $scope.fileList = angular.copy($scope.fileListDefault);
                $scope.shareList=data.shareListDefault;
                $scope.addUploaderList();
            }else{
                $scope.notice(data.msg);
            }
        })
        .error(function(data){
            $scope.notice(data.msg);
        });

    $scope.addUploaderList=function(){
        if($scope.fileList!=null&&$scope.fileList.length>0){
            for(let i=0;i<$scope.fileList.length;i++){
                //上传图片,定义控制器路径
                var uploaderImg = new FileUploader({
                    url: 'upload/fileUpload.do',
                    queueLimit: 1,   //文件个数
                    removeAfterUpload: false,  //上传后删除文件
                    autoUpload:true,     //文件加入队列之后自动上传，默认是false
                    headers : {'X-CSRF-TOKEN' : $scope.csrfData}
                });
                //过滤长度，只能上传一个
                uploaderImg.filters.push({
                    name: 'imageFilter',
                    fn: function(item, options) {
                        return this.queue.length < 1;
                    }
                });
                //过滤格式
                uploaderImg.filters.push({
                    name: 'imageFilter',
                    fn: function(item,options) {
                        var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
                        return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
                    }
                });
                uploaderImg.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
                    if (response.url != null) { // 回调参数url
                        $scope.fileList[i].configValue= response.url;
                    }
                };
                $scope.fileList[i].uploaderImg=uploaderImg;
            }
        }

    };

    $scope.submitting = false;
    //编辑
    $scope.savetype = function(){
        if($scope.submitting){
            return;
        }
        if($scope.checkPrice()){
            return;
        }
        if($scope.fileList!=null&&$scope.fileList.length>0){
            $scope.saveFileList = angular.copy($scope.fileList);
            for(var i=0;i<$scope.saveFileList.length;i++){
                if($scope.saveFileList[i].uploaderImg.queue.length<1){
                    $scope.saveFileList[i].configValue=null;
                }
                delete $scope.saveFileList[i].uploaderImg;
            }
        }
        $scope.shareListSub = angular.copy($scope.shareList);
        if($scope.shareListSub!=null&&$scope.shareListSub.length>0){
            for(var i=0;i<$scope.shareListSub.length;i++){
                var item=$scope.shareListSub[i];
                if(item.propertyRemark!=null){
                    item.propertyRemark=encodeURIComponent(item.propertyRemark);
                }
            }
        }
        $scope.addinfoSub = angular.copy($scope.addinfo);
        if($scope.addinfoSub.agreement!=null){
            $scope.addinfoSub.agreement=encodeURIComponent($scope.addinfoSub.agreement);//协议 转码
        }
        $scope.submitting = true;
        $http.post("exchangeActivateOem/updateExchangeOem",
            "info="+angular.toJson($scope.addinfoSub)+
            "&agentOem="+angular.toJson($scope.agentOem)+
            "&fileList="+angular.toJson($scope.saveFileList)+
            "&shareList="+angular.toJson($scope.shareListSub),
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                $scope.submitting = false;
                if(data.status){
                    $scope.notice(data.msg);
                    $state.transitionTo('exchangeActivate.exchangeOem',null,{reload:true});
                }else{
                    $scope.notice(data.msg);
                }
            })
            .error(function(data){
                $scope.submitting = false;
                $scope.notice(data.msg);
            });
    };
    $scope.checkPrice=function () {
        //校验成本价
        if($scope.shareList!=null&&$scope.shareList.length>0){
            var price1=0;
            var price2=0;
            var price3=0;
            var price4=0;
            var desc1="";
            var desc2="";
            var desc3="";
            var desc4="";
            for(var i=0;i<$scope.shareList.length;i++){
                var item=$scope.shareList[i];
                if(item.propertyCode=="pro_share"){
                    if(item.configValue!=null&&item.configValue!=""){
                        price1=item.configValue;
                        desc1=item.propertyDesc;
                    }
                }
                if(item.propertyCode=="one_agent_share"){
                    if(item.configValue!=null&&item.configValue!=""){
                        price2=item.configValue;
                        desc2=item.propertyDesc;
                    }
                }
                if(item.propertyCode=="platform_fee"){
                    if(item.configValue!=null&&item.configValue!=""){
                        price3=item.configValue;
                        desc3=item.propertyDesc;
                    }
                }
                if(item.propertyCode=="oem_fee"){
                    if(item.configValue!=null&&item.configValue!=""){
                        price4=item.configValue;
                        desc4=item.propertyDesc;
                    }
                }
            }
            if(Number(price1)+Number(price2)>100){
                $scope.notice(desc1+"+"+desc2+"不能大于100%");
                return true;
            }
            if(Number(price3)<Number(price4)){
                $scope.notice(desc4+"不能大于"+desc3);
                return true;
            }
        }
        return false;
    };
});