
angular.module('inspinia', ['angularFileUpload']).controller("leadInSensitiveWordsCtrl", function($scope, $http, $state, $stateParams,FileUploader) {
    var uploader = $scope.uploader = new FileUploader({
        url: 'sensitiveWords/addButchSensitiveWords',
        // formData:[opts],
        queueLimit: 1,   //文件个数
        removeAfterUpload: true,  //上传后删除文件
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    uploader.filters.push({
        name: 'isFile',
        fn: function(item, options) {
            if(item.size>10*1000*1024){
                $scope.notice("上传文件超过了10M限制，请调整后重新上传!");
                return false;
            }
            return this.queue.length < 1;
        }
    });


    $scope.clearItems = function(){  //重新选择文件时，清空队列，达到覆盖文件的效果
        uploader.clearQueue();
    }

    function newLineBySign(value){
        value=value.trim();
        var result='';
        if(value.indexOf("\\n")>0)
        { result=value.replace(/(\\n)/g,"$1\n");result=result.replace(/(\\n)/g,"");}
        return result;
    }//

    $scope.submitting=false;
    $scope.submit=function(){
        if(uploader.queue.length<=0){
            $scope.notice("请选择导入的文件");
            return;
        }
        $scope.submitting = true;
        $scope.loadImg = true;
        uploader.uploadAll();//上传
        uploader.onSuccessItem = function(fileItem, response, status, headers) {//上传成功后的回调函数，在里面执行提交
            if(response.status){
                //$scope.notice(response.msg);
                $scope.loadImg = false;
                $("#resultModel").modal("show");
                $scope.resultStr = newLineBySign(response.msg);
                $scope.successCount = response.successCount;
                $scope.failCount = response.failCount;
                $scope.submitting = false;
                //上传完成后跳转到哪个页面
                //$state.transitionTo('user.sensitive',null,{reload:true});
            }else{
                $scope.loadImg = false;
                $scope.notice(response.msg);

                //setTimeout(, 1000);
            }
            $scope.submitting = false;
        };
        return false;
    }

    $scope.cancel=function(){
        $('#resultModel').modal('hide');
    }
});
