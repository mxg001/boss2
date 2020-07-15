
angular.module('inspinia', ['angularFileUpload']).controller("leadInSensitiveWordsCtrl", function($scope, $http, $state, $stateParams,FileUploader) {
    var uploader = $scope.uploader = new FileUploader({
        url: 'superSensitiveWords/addButchSensitiveWords',
        // formData:[opts],
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
    }

    $scope.submitting=false;
    $scope.submit=function(){
        if(uploader.queue.length<=0){
            $scope.notice("请选择导入的文件");
            return;
        }
        $scope.submitting = true;
        uploader.uploadAll();//上传
        uploader.onSuccessItem = function(fileItem, response, status, headers) {//上传成功后的回调函数，在里面执行提交
            if(response.status){
                $scope.notice(response.msg);
                //上传完成后跳转到哪个页面
                $state.transitionTo('superBank.sensitive',null,{reload:true});
            }else{
                $scope.notice(response.msg);
            }
            $scope.submitting = false;
        };
        return false;
    }
});
