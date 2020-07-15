/**
 * 调单新增
 */
angular.module('inspinia',['angularFileUpload']).controller('surveyOrderAddCtrl',function($scope,$http,i18nService,$state,FileUploader){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文

    $scope.clear=function(){
        $scope.addInfo={orderServiceCode:"",orderTypeCode:"",dataSta:0,orderSta:0,replyEndTime:""};
        $scope.tall="";
    };
    $scope.clear();


    //上传,定义控制器路径
    var uploader = $scope.uploader = new FileUploader({
        url: 'upload/fileUploadAll.do',
        queueLimit: 6,   //文件个数
        removeAfterUpload: false,  //上传后删除文件
        autoUpload:false,     //文件加入队列之后自动上传，默认是false
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    uploader.filters.push({
        name: 'isFile',
        fn: function(item, options) {
            return this.queue.length < 6;
        }
    });
    //过滤格式
    uploader.filters.push({
        name: 'imageFilter',
        fn: function(item,options) {
            if(item.size>20*1024*1024){
                return false;
            }
            var itemName=item.name.substring(0,item.name.lastIndexOf("."));
            if(itemName.length>50){
                return false;
            }
            // var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            // return '|jpg|png|jpeg|doc|gif|'.indexOf(type) !== -1;
            return true;
        }
    });
    $scope.clearItems = function(){  //重新选择文件时，清空队列，达到覆盖文件的效果
        uploader.clearQueue();
    };


    $scope.submitting = false;

    //新增info
    $scope.commitInfo = function(){
        if($scope.submitting){
            return;
        }
        if($scope.addInfo.replyEndTime==null||$scope.addInfo.replyEndTime==""){
            $scope.notice("回复截止时间不能为空!");
            return;
        }
        $scope.submitting = true;
        $scope.tall="";
        uploader.onSuccessItem = function(fileItem,response, status, headers) {
            // 上传成功后的回调函数，在里面执行提交
            if (response.url != null) { // 回调参数url
                if($scope.tall!=null){
                    if($scope.tall==""){
                        $scope.tall=response.url+",";
                    }else{
                        $scope.tall=$scope.tall+response.url+",";
                    }
                }
            }
        };
        uploader.onCompleteAll = function() {
            $scope.commitData();
        };
        if($scope.uploader.queue.length<1){
            $scope.commitData();
        }else{
            $scope.uploader.uploadAll();
        }
    };
    $scope.commitData=function () {
        $scope.addInfo.templateFilesName=$scope.tall.substring(0,$scope.tall.length-1);
        $http.post("surveyOrder/addSurveyOrder","info="+angular.toJson($scope.addInfo),
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                $scope.submitting = false;
                if(data.addsta=='1'){
                    $scope.notice(data.msg);
                    $state.transitionTo('risk.surveyOrder',null,{reload:true});
                    $scope.uploader.clearQueue();
                }else if(data.addsta=='2'){
                    $scope.uploader.clearQueue();
                    $scope.titleModal(data.msg);
                }else{
                    $scope.uploader.clearQueue();
                    $scope.notice(data.msg);
                }
            })
            .error(function(data){
                $scope.submitting = false;
                $scope.uploader.clearQueue();
                $scope.notice(data.msg);
            });
    };
    $scope.titleModal = function(errormsg){
        $scope.error=errormsg;
        $('#addBankModal').modal('show');
    };
    $scope.cancel = function(){
        $('#addBankModal').modal('hide');
    };
});