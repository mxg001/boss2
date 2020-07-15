/**
 * 修改超级推商品
 */
angular.module('inspinia',['angularFileUpload']).controller("updateCjtGoods", function($scope, $http, $stateParams,$state,FileUploader) {
    $scope.mainImgName1 = "";
    $scope.mainImgName2 = "";
    $scope.mainImgName3 = "";

    $scope.descOldLength = 0;
    $scope.uploaderImgListMax = 20;//默认详情图片最多20张

    var getCjtHpList = function(){
        $http({
            method: "get",
            url: "cjtGoods/selectCjtHpList"
        }).success(function(result){
            if(result.status){
                $scope.cjtHpList = result.data;
            }
        });
    };
    getCjtHpList();

    //查询
    $scope.query = function(){
        $scope.submitting = true;
        $http({
            url:"cjtGoods/selectCjtGoods/" + $stateParams.id,
            method:"get"
        }).success(function(result){
            $scope.submitting = false;
            if (!result || !result.status){
                $scope.notice (result.msg);
                return;
            }
            $scope.baseInfo = result.data;
            if($scope.baseInfo.descImgUrl != null && $scope.baseInfo.descImgUrl != ""
                && $scope.baseInfo.descImgUrl != "undefined") {
                $scope.descImgUrlList = $scope.baseInfo.descImgUrl.split(",");
                $scope.descImgNameList = $scope.baseInfo.descImg.split(",");
                $scope.descImgList = [{}];
                angular.forEach($scope.descImgUrlList, function (item, index) {
                    if(item){
                        $scope.descImgList[index] = {descImgUrl: item, descImgName: $scope.descImgNameList[index]};
                    }
                });
                $scope.descOldLength = $scope.descImgList.length;
                $scope.uploaderImgListMax = $scope.uploaderImgListMax - $scope.descOldLength;
            }
        }).error(function(){
            $scope.submitting = false;
            $scope.notice("服务器异常");
        });
    };
    $scope.query();

    $scope.deleteMainImgUrl2 = function(){
        $scope.baseInfo.mainImgName2 = "";
    }
    $scope.deleteMainImgUrl3 = function(){
        $scope.baseInfo.mainImgName3 = "";
    }

    $scope.deleteThisLi = function($event){
        $event.target.parentNode.parentNode.remove();
        $scope.uploaderImgListMax = $scope.uploaderImgListMax + 1;
    }

    //提交
    $scope.submit = function(){
        if("uploading"===$scope.completeAllImgs){
            $scope.notice("还有图片未上传完成,请稍候再试!");
            return;
        }
        $scope.baseInfo.descImg = "";
        $("#imageUL li").each(function () {
            var yunName = $(this).attr("yun_name");
            if(yunName==="undefined" || typeof (yunName) ==="undefined"){
            }else{
                $scope.baseInfo.descImg += yunName+",";
            }
        });
        if($scope.uploaderImg1.queue.length==1){
            $scope.baseInfo.mainImgName1 = $scope.mainImgName1;
        }
        if($scope.uploaderImg2.queue.length==1){
            $scope.baseInfo.mainImgName2 = $scope.mainImgName2;
        }
        if($scope.uploaderImg3.queue.length==1){
            $scope.baseInfo.mainImgName3 = $scope.mainImgName3;
        }
        $scope.submitting = true;
        $http({
            url:"cjtGoods/updateCjtGoods",
            method:"post",
            data:$scope.baseInfo
        }).success(function(result){
            $scope.submitting = false;
            $scope.notice (result.msg);
            if(result.status){
                $state.transitionTo ('cjt.queryCjtGoods',null,{reload:true } );
            }
        }).error(function(){
            $scope.submitting = false;
            $scope.notice("服务器异常");
        });
    };

    //图1
    //上传图片,定义控制器路径
    $scope.uploaderImg1 = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数
        removeAfterUpload: false,  //上传后删除文件
        autoUpload:true,     //文件加入队列之后自动上传，默认是false
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤格式
    $scope.uploaderImg1.filters.push({
        name: 'imageFilter',
        fn: function(item,options) {
            var type = '|' + item.name.slice(item.name.lastIndexOf('.') + 1) + '|';
            return '|jpg|png|'.indexOf(type) !== -1;
        }
    });
    $scope.uploaderImg1.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            $scope.mainImgName1 = response.url;
        }
    };

    //图2
    $scope.uploaderImg2 = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数
        removeAfterUpload: false,  //上传后删除文件
        autoUpload:true,     //文件加入队列之后自动上传，默认是false
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤格式
    $scope.uploaderImg2.filters.push({
        name: 'imageFilter',
        fn: function(item,options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|'.indexOf(type) !== -1;
        }
    });
    $scope.uploaderImg2.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            $scope.mainImgName2 = response.url;
        }
    };

    //图3
    $scope.uploaderImg3 = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数
        removeAfterUpload: false,  //上传后删除文件
        autoUpload:true,     //文件加入队列之后自动上传，默认是false
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤格式
    $scope.uploaderImg3.filters.push({
        name: 'imageFilter',
        fn: function(item,options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|'.indexOf(type) !== -1;
        }
    });
    $scope.uploaderImg3.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            $scope.mainImgName3 = response.url;
        }
    };


    //批量上传
    $scope.uploaderImgList = new FileUploader({
        url: 'upload/fileUpload.do',
        removeAfterUpload: false,  //上传后删除文件
        autoUpload:true,     //文件加入队列之后自动上传，默认是false
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，限制最多上传个数
    $scope.uploaderImgList.filters.push({
        name: 'imageFilter',
        fn: function(item, options) {
            return this.queue.length < $scope.uploaderImgListMax;
        }
    });
    //过滤格式
    $scope.uploaderImgList.filters.push({
        name: 'imageFilter',
        fn: function(item,options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|'.indexOf(type) !== -1;
        }
    });

    $scope.uploaderImgList.onBeforeUploadItem = function(fileItem) {// 上传前的回调函数，在里面执行提交
        $scope.completeAllImgs = "uploading";
        console.log("图片开始上传[onBeforeUploadItem]");
        console.log("queue.length["+this.queue.length+"];uploaderImgListMax["+$scope.uploaderImgListMax+"]");
    };

    $scope.uploaderImgList.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            var itemIndex = this.getIndexOfItem(fileItem);
            $("#imageUL li[newImage='newImage']").eq(itemIndex).attr("yun_name",response.url);
            console.log("图片上传完成一项[onSuccessItem]" + response.url);
        }
    };

    $scope.completeAllImgs = "start";
    $scope.uploaderImgList.onBeforeUpload = function(){
        console.log("图片开始上传[onBeforeUpload]");
        $scope.completeAllImgs = "uploading";
    };
    $scope.uploaderImgList.onCompleteAll = function(){
        console.log("所有图片上传完成[onCompleteAll]");
        $scope.completeAllImgs = "end";
    };

});