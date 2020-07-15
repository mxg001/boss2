
angular.module('inspinia',['infinity.angular-chosen','angularFileUpload']).controller('workOrderDetailCtrl',function($scope,$http,$state,$stateParams,i18nService,$timeout,FileUploader){
    //数据源
    i18nService.setCurrentLang('zh-cn');

    //回显工单信息
    $http.get('workOrder/getWorkOrderDetailById?id='+$stateParams.id)
      .success(function(result) {
          if(!result.status){
              $scope.notice(result.msg);
              return;
          }
          $scope.info = result.data;

      });



    $scope.showAddImgModal = function(){
        $('#imgModal').modal('show');
    }
    $scope.hideAddImgModal = function(){
        $('#imgModal').modal('hide');
    }

    $scope.reply = function(){
        window.open('welcome.do#/workOrder/workOrder/workOrderReply/1/'+$scope.info.id, '_self');
    }
    $scope.reject = function(){
        window.open('welcome.do#/workOrder/workOrder/workOrderReject/1/'+$scope.info.id, '_self');
    }


    $scope.add= function(){
        $scope.submitting = true;
        $http({
            url: 'workOrderItem/reply',
            method: 'POST',
            data: $scope.info
        }).success(function (result) {
            if(result.status){
                $scope.addInfo = null;
                $scope.submitting = false;
                $state.transitionTo('workOrder.workOrderManager',null,{reload:false});
                $scope.notice(result.msg);
            }else{
                $scope.submitting = false;
                $scope.notice(result.msg);
            }
        });
    }

    //上传,定义控制器路径
    var uploader = $scope.uploader = new FileUploader({
        url: 'upload/fileUploadAll.do',
        queueLimit: 15,   //文件个数
        removeAfterUpload: false,  //上传后删除文件
        autoUpload:false,     //文件加入队列之后自动上传，默认是false
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度
    uploader.filters.push({
        name: 'isFile',
        fn: function(item, options) {
            return this.queue.length < 15;
        }
    });
    //过滤格式
    uploader.filters.push({
        name: 'imageFilter',
        fn: function(item,options) {
            if(item.size>10*1024*1024){
                return false;
            }
            return true;
        }
    });
    $scope.clearItems = function(){  //重新选择文件时，清空队列，达到覆盖文件的效果
        uploader.clearQueue();
    };

    //新增info
    $scope.commitInfo = function(){
        if($scope.submitting){
            return;
        }
        $scope.submitting = true;
        $scope.info.workFileInfos = [];
        uploader.onSuccessItem = function(fileItem,response, status, headers) {
            // 上传成功后的回调函数，在里面执行提交
            if (response.url != null) { // 回调参数url
                $scope.info.workFileInfos.push({fileName:fileItem.file.name,fileUrl:response.url,fileType:fileItem.file.type});
            }
        };
        uploader.onCompleteAll = function() {
            $scope.add();
        };
        if($scope.uploader.queue.length<1){
            $scope.add();
        }else{
            $scope.uploader.uploadAll();
        }
    };


}).filter('trustHtml', function ($sce) {
    return function (input) {
        return $sce.trustAsHtml(input);
    }
});


