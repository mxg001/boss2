/**
 * 导入卡bin
 */
angular.module('inspinia',['angularFileUpload']).controller("importCardBinsCtrl", function($scope, $http,$state,FileUploader,i18nService) {
    //数据源
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.businessTypes=[{text:"境外卡",value:1},{text:"借贷合一卡bin白名单",value:2}];
    $scope.businessType=2;
    $scope.submitting = false;

    //上传图片,定义控制器路径
    var uploader = $scope.uploader = new FileUploader({
        url: 'cardBins/importCardBins',
        formData:[{businessType:null}],
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
    //导入
    $scope.import=function(){
        if($scope.submitting){
            return;
        }
        $scope.submitting = true;
        uploader.queue[0].formData[0].businessType = $scope.businessType;
        uploader.uploadAll();//上传
        uploader.onSuccessItem = function(fileItem, response, status, headers) {//上传成功后的回调函数，在里面执行提交
            if(response.status){
                $scope.notice(response.msg);
                $scope.submitting = false;
                $state.transitionTo('risk.cardBins',null,{reload:true});
            }else{
                $scope.notice(response.msg);
                $scope.submitting = false;
            }
        };
    }
});
