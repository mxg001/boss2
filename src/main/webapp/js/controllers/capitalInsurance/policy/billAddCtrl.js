
angular.module('inspinia',['angularFileUpload', 'infinity.angular-chosen']).controller('billAddCtrl',function($scope, $http, $state, $timeout, $stateParams, i18nService, SweetAlert, FileUploader){
    $scope.uploadInsurerList=[];
    $scope.uploadInsurerList=$scope.uploadInsurerList.concat($scope.insurerList);
    $scope.uploadInsurerList.splice(0,1);

    $scope.clean = function () { 
		$scope.info = {
			orderType: "1",
			insurer: "yilian"
		};
    $scope.clearItems();
    };


	var opts = {orderType: null,insurer:null};
	if (_parameterName){
        opts[_parameterName] = _token;
	}
    var uploader = $scope.uploader = new FileUploader({
        url: 'checkBillAction/importInsure.do',
        headers: {'X-CSRF-TOKEN': $scope.csrfData},
        formData: [opts],
        queueLimit: 1,   //文件个数
        removeAfterUpload: true  //上传后删除文件
    });
    //过滤长度，只能上传一个
    uploader.filters.push({
        name: 'isFile',
        fn: function (item, options) {
            return this.queue.length < 1;
        }
    });
    uploader.removeFromQueue = function (value) {
        var index = this.getIndexOfItem(value);
        if(index >= 0){
            var item = this.queue[index];
            if (item.isUploading) item.cancel();
            this.queue.splice(index, 1);
            item._destroy();
            this.progress = this._getTotalProgress();
        }
    };

    $scope.clearItems = function () {  //重新选择文件时，清空队列，达到覆盖文件的效果
        uploader.clearQueue();
    };
    
	  //下载导入模板
    $scope.downloadBillTemplate = function () {
    	 var orderType = $scope.info.orderType;
         if (orderType == null || orderType == "") {
             $scope.submitting = false;
             $scope.notice("请先选择订单类型");
             return;
         }
         var insurer = $scope.info.insurer;
         if (insurer == null || insurer == "") {
             $scope.submitting = false;
             $scope.notice("请先选择承保单位");
             return;
         }
         if(orderType == "2"&&insurer == "zhlh"){
             $scope.submitting = false;
             $scope.notice("此承保单位无退保模板");
             return;
         }

         location.href = "checkBillAction/downloadBillTemplate.do?orderType="+orderType+"&insurer="+insurer;
    };
    
    //提交导入的文件
    $scope.importInsure = function () {
        var insurer = $scope.info.insurer;
        if (insurer == null || insurer == ""  || insurer == "0") {
            $scope.submitting = false;
            $scope.notice("请先选择承保单位");
            return;
        }
	   	 var orderType = $scope.info.orderType;
	     if (orderType == null || orderType == "") {
	         $scope.submitting = false;
	         $scope.notice("请先选择订单类型");
	         return;
	     }
        var fileNum = uploader.queue.length;
        if (fileNum < 1) {
            $scope.submitting = false;
            $scope.notice("上传的文件不能为空");
            return;
        }
        $scope.submitting = true;
        $scope.loadImg = true;
        uploader.queue[0].formData[0].orderType = $scope.info.orderType;
        uploader.queue[0].formData[0].insurer = $scope.info.insurer;
        uploader.uploadAll();//上传
        uploader.onSuccessItem = function (fileItem, response, status, headers) {//上传成功后的回调函数，在里面执行提交
        	 $scope.loadImg = false;
        	 $scope.submitting = false;
        	if (response.result) {
                var batchNo = response.batchNo;
                var title, msgText,confirmButtonText;
                    title = "文件对账中...";
                    msgText = "对账批次号："+batchNo+",请稍后在【对账信息查询】菜单查看对账结果";
                    confirmButtonText = "确定";
                swal({
                    title: title,
                    text: msgText,
                    type: "success",
                    showCancelButton: false,
                    closeOnConfirm: true,
                    confirmButtonText: confirmButtonText,
                    confirmButtonColor: "#5CB85C"
                }, function () {
                    return;
                });
            } else {
                swal("", response.msg, "error");
            }
            $scope.cancel();
        };
        $scope.loadImg = false;
        $scope.submitting = false;
        return false;
    };
    $scope.clean();
});

