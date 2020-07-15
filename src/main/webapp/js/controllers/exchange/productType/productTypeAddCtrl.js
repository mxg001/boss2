/**
 * 产品类别新增
 */
angular.module('inspinia',['angularFileUpload']).controller('productTypeAddCtrl',function($scope,$http,i18nService,$state,FileUploader){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文

    $scope.declaraTypeSelect = [{text:"全部",value:""},{text:"微信报单",value:"1"},{text:"自助报单",value:"2"}];
    $scope.declaraTypeStr=angular.toJson($scope.declaraTypeSelect);

    $scope.orgList=[];
    //清空
    $scope.clear=function(){
        $scope.info={orgCode:"",declaraType:""};
    };
    $scope.clear();

    //获取机构
    $http.post("orgManagement/getOrgManagementList")
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
    //获取默认字段
    $http.post("productType/getaddOrgDefault",
        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function(data){
            if(data.status){
                $scope.checkboxListDefault=data.checkboxListDefault;
                if($scope.checkboxListDefault!=null&&$scope.checkboxListDefault.length>0){
                    for(var i=0;i<$scope.checkboxListDefault.length;i++){
                        var item=$scope.checkboxListDefault[i];
                        if(item.propertyDefaultValue=="1"){
                            item.propertyDefaultValue=true;
                        }else{
                            item.propertyDefaultValue=false;
                        }
                    }
                }
                $scope.checkboxList = angular.copy($scope.checkboxListDefault);
                $scope.fileListDefault=data.fileListDefault;
                $scope.fileList = angular.copy($scope.fileListDefault);
                $scope.textListDefault=data.textListDefault;
                $scope.textList = angular.copy($scope.textListDefault);
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
                        $scope.fileList[i].propertyDefaultValue= response.url;
                    }
                };
                $scope.fileList[i].uploaderImg=uploaderImg;
            }
        }

    };

    $scope.submitting = false;
    //开启
    $scope.savetype = function(){
        if($scope.submitting){
            return;
        }
        if($scope.checkboxList!=null&&$scope.checkboxList.length>0){
            for(var i=0;i<$scope.checkboxList.length;i++){
                var item=$scope.checkboxList[i];
                if(item.propertyDefaultValue=="1"||item.propertyDefaultValue==true){
                    for(var j=0;j<$scope.fileList.length;j++){
                        if(item.propertyCode.indexOf($scope.fileList[j].propertyCode)>0){
                            if($scope.fileList[j].uploaderImg.queue.length<1){
                                $scope.notice($scope.fileList[j].propertyDesc+"不能为空!");
                                return;
                            }
                        }
                    }
                    for(var k=0;k<$scope.textList.length;k++){
                        if(item.propertyCode.indexOf($scope.textList[k].propertyCode)>0){
                            if($scope.textList[k].propertyDefaultValue==null||$scope.textList[k].propertyDefaultValue==""){
                                $scope.notice($scope.textList[k].propertyDesc+"不能为空!");
                                return;
                            }
                        }
                    }
                    item.propertyDefaultValue="1";
                }else{
                    item.propertyDefaultValue="0";
                }
            }
        }
        if($scope.fileList!=null&&$scope.fileList.length>0){
            $scope.saveFileList = angular.copy($scope.fileList);
            for(var i=0;i<$scope.saveFileList.length;i++){
                if($scope.saveFileList[i].uploaderImg.queue.length<1){
                    $scope.saveFileList[i].propertyDefaultValue=null;
                }
                delete $scope.saveFileList[i].uploaderImg;
            }
        }
        $scope.infoSub = angular.copy($scope.info);
        if($scope.infoSub.videoUrl!=null){
            $scope.infoSub.videoUrl=encodeURIComponent($scope.infoSub.videoUrl);
        }
        if($scope.infoSub.courseUrl!=null){
            $scope.infoSub.courseUrl=encodeURIComponent($scope.infoSub.courseUrl);
        }
        if($scope.infoSub.bankUrl!=null){
            $scope.infoSub.bankUrl=encodeURIComponent($scope.infoSub.bankUrl);
        }
        $scope.submitting = true;
        $http.post("productType/addProductType",
            "info="+angular.toJson($scope.infoSub)+"&checkboxList="+angular.toJson($scope.checkboxList)+
            "&fileList="+angular.toJson($scope.saveFileList)+"&textList="+angular.toJson($scope.textList),
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                $scope.submitting = false;
                if(data.status){
                    $scope.notice(data.msg);
                    $state.transitionTo('exchange.productType',null,{reload:true});
                }else{
                    $scope.notice(data.msg);
                    $scope.backState();
                }
            })
            .error(function(data){
                $scope.submitting = false;
                $scope.notice(data.msg);
                $scope.backState();
            });
    };
    $scope.backState=function () {
        if($scope.checkboxList!=null&&$scope.checkboxList.length>0){
            for(var i=0;i<$scope.checkboxList.length;i++){
                var item=$scope.checkboxList[i];
                if(item.propertyDefaultValue=="1"){
                    item.propertyDefaultValue=true;
                }else{
                    item.propertyDefaultValue=false;
                }
            }
        }
    };

});