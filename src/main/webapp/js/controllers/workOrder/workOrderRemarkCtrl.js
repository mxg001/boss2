
angular.module('inspinia',['infinity.angular-chosen','angularFileUpload']).controller('workOrderRemarkCtrl',function($scope,$http,$state,$stateParams,i18nService,$timeout,FileUploader){
	  //数据源
	  i18nService.setCurrentLang('zh-cn');
    $scope.info = {agentShow:""};
	  $scope.agentShowSelect = [{text:"请选择",value:""},{text:"是",value:"1"},{text:"否",value:"0"}]

    $scope.info.orderId = $stateParams.id;
    $scope.showAgentShowStatus = false;

    //回显备注信息
    $http.get('workOrder/getWorkOrderToRemarkById?id='+$stateParams.id)
      .success(function(result) {
        if(result.status){
            $scope.showAgentShowStatus =  result.data;
        }else{
            $scope.notice(result.msg);
        }
      });


    $scope.showAddImgModal = function(){
        $('#imgModal').modal('show');
    }
    $scope.hideAddImgModal = function(){
        $('#imgModal').modal('hide');
    }


    $scope.remark= function(){
        $scope.submitting = true;
        $http({
            url: 'workOrderItem/remark',
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
            $scope.remark();
        };
        if($scope.uploader.queue.length<1){
            $scope.remark();
        }else{
            $scope.uploader.uploadAll();
        }
    };

}).filter('trustHtml', function ($sce) {
    return function (input) {
        return $sce.trustAsHtml(input);
    }
});


