/**
 * 贷款理财配置新增
 */
angular.module('inspinia',['angularFileUpload']).controller('loanFinancingAddCtrl',function($scope,$http,i18nService,$state,FileUploader,SweetAlert){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文

    $scope.addInfo={};

    //上传图片,定义控制器路径
    var uploaderImg = $scope.uploaderImg = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数
        removeAfterUpload: true,  //上传后删除文件
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    uploaderImg.filters.push({
        name: 'imageFilter',
        fn: function(item, options) {
            if(item.size>40*1024){
                $scope.notice("图片大小超过40KB,请重新上传!");
                return false;
            }
            return this.queue.length < 1;
        }
    });
    //过滤格式
    uploaderImg.filters.push({
        name: 'imageFilter',
        fn: function(item,options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|png|'.indexOf(type) !== -1;
        }
    });
    uploaderImg.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            $scope.subAddInfoPost(response.url);
        }
    };
    //新增
    $scope.submitting = false;
    $scope.subAddInfo= function(){
        if($scope.submitting){
            return;
        }
        if(uploaderImg.queue.length<1){
            $scope.notice("产品logo不能为空!");
            return;
        }
        SweetAlert.swal({
                title: "您正在进行提交操作,是否继续该操作?",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $scope.submitting = true;
                    uploaderImg.uploadAll();// 上传消息图片
                }
            });
    };

    $scope.subAddInfoPost=function (url) {
        $scope.infoSub = angular.copy($scope.addInfo);
        $scope.infoSub.logImg=url;
        var data={
            info:angular.toJson($scope.infoSub),
        };
        var postCfg = {
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            transformRequest: function (data) {
                return $.param(data);
            }
        };
        $http.post("loanFinancing/addLoanFinancing",data,postCfg)
            .success(function(data){
                $scope.submitting = false;
                if(data.status){
                    $scope.notice(data.msg);
                    $state.transitionTo('func.loanFinancing',null,{reload:true});
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