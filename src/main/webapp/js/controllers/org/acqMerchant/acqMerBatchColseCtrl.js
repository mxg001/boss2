/**
 * 收单机构商户/批量关闭
 */
angular.module('inspinia',['angularFileUpload']).controller('acqMerBatchColseCtrl',function($scope,$http,i18nService,SweetAlert,FileUploader){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文

    //上传图片,定义控制器路径
    var uploader = $scope.uploader = new FileUploader({
        url: 'acqMerchantAction/acqMerBatchColseimport',
        queueLimit: 1,   //文件个数
        removeAfterUpload: true,  //上传后删除文件
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    uploader.filters.push({
        name: 'isFile',
        fn: function(item, options) {
            return this.queue.length < 1;
        }
    });
    $scope.clearItems = function(){  //重新选择文件时，清空队列，达到覆盖文件的效果
        uploader.clearQueue();
    };

    $scope.importMsgShow = function(){
        $('#importMsg').modal('show');
    };
    $scope.importMsgHide = function(){
        $('#importMsg').modal('hide');
    };

    $scope.submitting = false;
    $scope.acqMerBatchColseimport=function(){
        if($scope.submitting){
            return
        }
        $scope.submitting = true;
        uploader.uploadAll();//上传
        uploader.onSuccessItem = function(fileItem, response, status, headers) {//上传成功后的回调函数，在里面执行提交
            //处理返回数据
            $scope.mgsInfo=response.msg+"\n";
            var errorList=response.errorList;
            if(errorList!=null&&errorList.length>0){
                for(var i=0;i<errorList.length;i++){
                    $scope.mgsInfo=$scope.mgsInfo+errorList[i].msg+"\n";
                }
            }
            $scope.importMsgShow();
            $scope.submitting = false;
        };
    };

});