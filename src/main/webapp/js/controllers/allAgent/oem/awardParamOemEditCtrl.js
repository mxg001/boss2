/**
 * oem配置
 */
angular.module('inspinia',['angularFileUpload']).controller("awardParamOemEditCtrl", function($scope, $http, $state, $stateParams,FileUploader,i18nService,SweetAlert) {
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    //数据源
    $scope.info = {brandCode:''};
    $scope.agentBgi='';
    $scope.merBgi='';
    $scope.ownerImg='';
    $scope.merImg='';
    $scope.leaderboardBgi='';
    var ownerImgs = ['','','','','',''];
    var merImgs = ['','','','','',''];
    var leaImgs = ['','',''];
    var ownerImgsMap = ['','','','','',''];
    var merImgsMap = ['','','','','',''];
    var leaImgsMap = ['','',''];

    $scope.getMap=function(){
        if($scope.info.ownerImgs!=null&&$scope.info.ownerImgs!=''){
            var owner = $scope.info.ownerImgs.split(",");
            for(var i=0;i<owner.length;i++){
                ownerImgsMap[i]=owner[i];
            }
        }
        if($scope.info.merImgs!=null&&$scope.info.merImgs!=''){
            var mer = $scope.info.merImgs.split(",");
            for(var i=0;i<mer.length;i++){
                merImgsMap[i]=mer[i];
            }
        }
        if($scope.info.leaderboardBgi!=null&&$scope.info.leaderboardBgi!=''){
            var lea = $scope.info.leaderboardBgi.split(",");
            for(var i=0;i<lea.length;i++){
                leaImgsMap[i]=lea[i];
            }
        }
    }

    $scope.getOem=function(){
        $http.post("awardParam/getAwardParamOem","id="+$stateParams.id,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.info=data.oem;
                    $scope.getMap();
                }
            });
    }
    $scope.getOem();

    //上传图片,定义控制器路径
    var uploaderAgentBgiImg = $scope.uploaderAgentBgiImg = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数
        removeAfterUpload: false,  //上传后删除文件
        autoUpload:true,     //文件加入队列之后自动上传，默认是false
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    uploaderAgentBgiImg.filters.push({
        name: 'imageFilter',
        fn: function(item, options) {
            return this.queue.length < 1;
        }
    });
    //过滤格式
    uploaderAgentBgiImg.filters.push({
        name: 'imageFilter',
        fn: function(item,options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });
    uploaderAgentBgiImg.removeFromQueue = function(value){
        $scope.agentBgi= "";
        var index = this.getIndexOfItem(value);
        var item = this.queue[index];
        if (item.isUploading) item.cancel();
        this.queue.splice(index, 1);
        item._destroy();
        this.progress = this._getTotalProgress();
    }
    uploaderAgentBgiImg.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            $scope.agentBgi= response.url;
        }
    };

    //上传图片,定义控制器路径
    var uploaderMerBgiImg = $scope.uploaderMerBgiImg = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数
        removeAfterUpload: false,  //上传后删除文件
        autoUpload:true,     //文件加入队列之后自动上传，默认是false
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    uploaderMerBgiImg.filters.push({
        name: 'imageFilter',
        fn: function(item, options) {
            return this.queue.length < 1;
        }
    });
    //过滤格式
    uploaderMerBgiImg.filters.push({
        name: 'imageFilter',
        fn: function(item,options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });
    uploaderMerBgiImg.removeFromQueue = function(value){
        $scope.merBgi= "";
        var index = this.getIndexOfItem(value);
        var item = this.queue[index];
        if (item.isUploading) item.cancel();
        this.queue.splice(index, 1);
        item._destroy();
        this.progress = this._getTotalProgress();
    }
    uploaderMerBgiImg.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            $scope.merBgi= response.url;
        }
    };

    //提交图片
    $scope.submit = function(){
        $scope.updateInfo={};
        $scope.updateInfo.brandCode=$scope.info.brandCode;
        if($scope.info.agentBgi==null||$scope.info.agentBgi==''){
            if($scope.agentBgi==null||$scope.agentBgi==''){
                $scope.notice("邀请盟主分享海报图片不能为空!");
                return false;
            }else{
                $scope.updateInfo.agentBgi= $scope.agentBgi;
            }
        }else {
            if($scope.agentBgi!=null&&$scope.agentBgi!=''){
                $scope.updateInfo.agentBgi= $scope.agentBgi;
            }
        }
        if($scope.info.merBgi==null||$scope.info.merBgi==''){
            if($scope.merBgi==null||$scope.merBgi==''){
                $scope.notice("拓展商户分享海报图片不能为空!");
                return false;
            }else {
                $scope.updateInfo.merBgi= $scope.merBgi;
            }
        }else {
            if($scope.merBgi!=null&&$scope.merBgi!=''){
                $scope.updateInfo.merBgi= $scope.merBgi;
            }
        }
        if($scope.info.aboutUs==null||$scope.info.aboutUs==''){
            $scope.notice("关于我们的内容不能为空!");
            return false;
        }else{
            $scope.updateInfo.aboutUs= $scope.info.aboutUs;
        }
        $scope.updateInfo.ownerImgs='';
        for(var i=0;i<ownerImgs.length;i++){
            if(ownerImgs[i]!=null&&ownerImgs[i]!=''){
                ownerImgsMap[i]=ownerImgs[i];
            }
        }
        for(var i=0;i<ownerImgsMap.length;i++){
            if(ownerImgsMap[i]!=null&&ownerImgsMap[i]!=''){
                $scope.updateInfo.ownerImgs+=ownerImgsMap[i]+",";
            }
        }
        if($scope.updateInfo.ownerImgs==null||$scope.updateInfo.ownerImgs==''){
            $scope.notice("邀请盟主H5介绍页面至少上传一张!");
            getMap();
            return false;
        }else{
            $scope.updateInfo.ownerImgs=$scope.updateInfo.ownerImgs.substr(0,$scope.updateInfo.ownerImgs.length-1);
        }
        if($scope.info.ownerImg==null||$scope.info.ownerImg==''){
            if($scope.ownerImg==null||$scope.ownerImg==''){
                $scope.notice("邀请盟主H5注册页面宣传图不能为空!");
                return false;
            }else{
                $scope.updateInfo.ownerImg= $scope.ownerImg;
            }
        }else{
            if($scope.ownerImg!=null&&$scope.ownerImg!=''){
                $scope.updateInfo.ownerImg= $scope.ownerImg;
            }
        }
        $scope.updateInfo.merImgs='';
        for(var i=0;i<merImgs.length;i++){
            if(merImgs[i]!=null&&merImgs[i]!=''){
                merImgsMap[i]=merImgs[i];
            }
        }
        for(var i=0;i<merImgsMap.length;i++){
            if(merImgsMap[i]!=null&&merImgsMap[i]!=''){
                $scope.updateInfo.merImgs+=merImgsMap[i]+",";
            }
        }
        if($scope.updateInfo.merImgs==null||$scope.updateInfo.merImgs==''){
            $scope.notice("拓展商户H5介绍页面至少上传一张!");
            $scope.getMap();
            return false;
        }else{
            $scope.updateInfo.merImgs=$scope.updateInfo.merImgs.substr(0,$scope.updateInfo.merImgs.length-1);
        }
        if($scope.info.merImg==null||$scope.info.merImg==''){
            if($scope.merImg==null||$scope.merImg==''){
                $scope.notice("拓展商户H5注册页面宣传图!");
                return false;
            }else{
                $scope.updateInfo.merImg=$scope.merImg;
            }
        }else {
            if($scope.merImg!=null&&$scope.merImg!=''){
                $scope.updateInfo.merImg=$scope.merImg;
            }
        }
        // if($scope.info.merContent==null||$scope.info.merContent==''){
        //     $scope.notice("介绍文案不能为空!");
        //     return false;
        // }else{
        //     $scope.updateInfo.merContent=$scope.info.merContent;
        // }
        if($scope.info.merApp==null||$scope.info.merApp==''){
            $scope.notice("商户APP下载H5页面链接地址不能为空!");
            return false;
        }else{
            $scope.updateInfo.merApp=$scope.info.merApp;
        }

        $scope.updateInfo.leaderboardBgi='';
        for(var i=0;i<leaImgs.length;i++){
            if(leaImgs[i]!=null&&leaImgs[i]!=''){
                leaImgsMap[i]=leaImgs[i];
            }
        }
        for(var i=0;i<leaImgsMap.length;i++){
            if(leaImgsMap[i]!=null&&leaImgsMap[i]!=''){
                $scope.updateInfo.leaderboardBgi+=leaImgsMap[i]+",";
            }
        }
        if($scope.updateInfo.leaderboardBgi==null||$scope.updateInfo.leaderboardBgi==''){
            $scope.notice("排行榜分享海报至少上传一张!");
            $scope.getMap();
            return false;
        }else{
            $scope.updateInfo.leaderboardBgi=$scope.updateInfo.leaderboardBgi.substr(0,$scope.updateInfo.leaderboardBgi.length-1);
        }
        $scope.submitting = true;
        $http.post("awardParam/updateAwardParamOem","info="+angular.toJson($scope.updateInfo),
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(msg){
                if(msg.status){
                    $scope.notice(msg.msg);
                    $state.transitionTo('allAgent.awardParam',null,{reload:true});
                    $scope.submitting = false;
                } else {
                    $scope.notice(msg.msg);
                    $scope.submitting = false;
                }
            }).error(function(){
            $scope.submitting = false;
        });
    }

    //图1
    //上传图片,定义控制器路径
    var uploaderOwnerImg1 = $scope.uploaderOwnerImg1 = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数
        removeAfterUpload: false,  //上传后删除文件
        autoUpload:true,     //文件加入队列之后自动上传，默认是false
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    uploaderOwnerImg1.filters.push({
        name: 'imageFilter',
        fn: function(item, options) {
            return this.queue.length < 1;
        }
    });
    //过滤格式
    uploaderOwnerImg1.filters.push({
        name: 'imageFilter',
        fn: function(item,options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });
    uploaderOwnerImg1.removeFromQueue = function(value){
        ownerImgs[0]= "";
        var index = this.getIndexOfItem(value);
        var item = this.queue[index];
        if (item.isUploading) item.cancel();
        this.queue.splice(index, 1);
        item._destroy();
        this.progress = this._getTotalProgress();
    }
    uploaderOwnerImg1.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            ownerImgs[0]= response.url;
        }
    };

    //图2
    var uploaderOwnerImg2 = $scope.uploaderOwnerImg2 = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数
        removeAfterUpload: false,  //上传后删除文件
        autoUpload:true,     //文件加入队列之后自动上传，默认是false
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    uploaderOwnerImg2.filters.push({
        name: 'imageFilter',
        fn: function(item, options) {
            return this.queue.length < 1;
        }
    });
    //过滤格式
    uploaderOwnerImg2.filters.push({
        name: 'imageFilter',
        fn: function(item,options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });
    uploaderOwnerImg2.removeFromQueue = function(value){
        ownerImgs[1]= "";
        var index = this.getIndexOfItem(value);
        var item = this.queue[index];
        if (item.isUploading) item.cancel();
        this.queue.splice(index, 1);
        item._destroy();
        this.progress = this._getTotalProgress();
    }
    uploaderOwnerImg2.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            ownerImgs[1]= response.url;
        }
    };

    //图3
    var uploaderOwnerImg3 = $scope.uploaderOwnerImg3 = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数
        removeAfterUpload: false,  //上传后删除文件
        autoUpload:true,     //文件加入队列之后自动上传，默认是false
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    uploaderOwnerImg3.filters.push({
        name: 'imageFilter',
        fn: function(item, options) {
            return this.queue.length < 1;
        }
    });
    //过滤格式
    uploaderOwnerImg3.filters.push({
        name: 'imageFilter',
        fn: function(item,options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });
    uploaderOwnerImg3.removeFromQueue = function(value){
        ownerImgs[2]= "";
        var index = this.getIndexOfItem(value);
        var item = this.queue[index];
        if (item.isUploading) item.cancel();
        this.queue.splice(index, 1);
        item._destroy();
        this.progress = this._getTotalProgress();
    }
    uploaderOwnerImg3.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            ownerImgs[2]= response.url;
        }
    };

    //图4
    var uploaderOwnerImg4 = $scope.uploaderOwnerImg4 = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数
        removeAfterUpload: false,  //上传后删除文件
        autoUpload:true,     //文件加入队列之后自动上传，默认是false
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    uploaderOwnerImg4.filters.push({
        name: 'imageFilter',
        fn: function(item, options) {
            return this.queue.length < 1;
        }
    });
    //过滤格式
    uploaderOwnerImg4.filters.push({
        name: 'imageFilter',
        fn: function(item,options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });
    uploaderOwnerImg4.removeFromQueue = function(value){
        ownerImgs[3]= "";
        var index = this.getIndexOfItem(value);
        var item = this.queue[index];
        if (item.isUploading) item.cancel();
        this.queue.splice(index, 1);
        item._destroy();
        this.progress = this._getTotalProgress();
    }
    uploaderOwnerImg4.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            ownerImgs[3]= response.url;
        }
    };

    //图5
    var uploaderOwnerImg5 = $scope.uploaderOwnerImg5 = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数
        removeAfterUpload: false,  //上传后删除文件
        autoUpload:true,     //文件加入队列之后自动上传，默认是false
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    uploaderOwnerImg5.filters.push({
        name: 'imageFilter',
        fn: function(item, options) {
            return this.queue.length < 1;
        }
    });
    //过滤格式
    uploaderOwnerImg5.filters.push({
        name: 'imageFilter',
        fn: function(item,options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });
    uploaderOwnerImg5.removeFromQueue = function(value){
        ownerImgs[4]= "";
        var index = this.getIndexOfItem(value);
        var item = this.queue[index];
        if (item.isUploading) item.cancel();
        this.queue.splice(index, 1);
        item._destroy();
        this.progress = this._getTotalProgress();
    }
    uploaderOwnerImg5.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            ownerImgs[4]= response.url;
        }
    };

    //图6
    var uploaderOwnerImg6 = $scope.uploaderOwnerImg6 = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数
        removeAfterUpload: false,  //上传后删除文件
        autoUpload:true,     //文件加入队列之后自动上传，默认是false
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    uploaderOwnerImg6.filters.push({
        name: 'imageFilter',
        fn: function(item, options) {
            return this.queue.length < 1;
        }
    });
    //过滤格式
    uploaderOwnerImg6.filters.push({
        name: 'imageFilter',
        fn: function(item,options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });
    uploaderOwnerImg6.removeFromQueue = function(value){
        ownerImgs[5]= "";
        var index = this.getIndexOfItem(value);
        var item = this.queue[index];
        if (item.isUploading) item.cancel();
        this.queue.splice(index, 1);
        item._destroy();
        this.progress = this._getTotalProgress();
    }
    uploaderOwnerImg6.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            ownerImgs[5]= response.url;
        }
    };

    //图uploaderMerImg
    var uploaderOwnerImg = $scope.uploaderOwnerImg = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数
        removeAfterUpload: false,  //上传后删除文件
        autoUpload:true,     //文件加入队列之后自动上传，默认是false
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    uploaderOwnerImg.filters.push({
        name: 'imageFilter',
        fn: function(item, options) {
            return this.queue.length < 1;
        }
    });
    //过滤格式
    uploaderOwnerImg.filters.push({
        name: 'imageFilter',
        fn: function(item,options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });
    uploaderOwnerImg.removeFromQueue = function(value){
        $scope.ownerImg= "";
        var index = this.getIndexOfItem(value);
        var item = this.queue[index];
        if (item.isUploading) item.cancel();
        this.queue.splice(index, 1);
        item._destroy();
        this.progress = this._getTotalProgress();
    }
    uploaderOwnerImg.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            $scope.ownerImg= response.url;
        }
    };

    //图1
    //上传图片,定义控制器路径
    var uploaderMerImg1 = $scope.uploaderMerImg1 = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数
        removeAfterUpload: false,  //上传后删除文件
        autoUpload:true,     //文件加入队列之后自动上传，默认是false
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    uploaderMerImg1.filters.push({
        name: 'imageFilter',
        fn: function(item, options) {
            return this.queue.length < 1;
        }
    });
    //过滤格式
    uploaderMerImg1.filters.push({
        name: 'imageFilter',
        fn: function(item,options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });
    uploaderMerImg1.removeFromQueue = function(value){
        merImgs[0]= "";
        var index = this.getIndexOfItem(value);
        var item = this.queue[index];
        if (item.isUploading) item.cancel();
        this.queue.splice(index, 1);
        item._destroy();
        this.progress = this._getTotalProgress();
    }
    uploaderMerImg1.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            merImgs[0]= response.url;
        }
    };

    //图2
    var uploaderMerImg2 = $scope.uploaderMerImg2 = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数
        removeAfterUpload: false,  //上传后删除文件
        autoUpload:true,     //文件加入队列之后自动上传，默认是false
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    uploaderMerImg2.filters.push({
        name: 'imageFilter',
        fn: function(item, options) {
            return this.queue.length < 1;
        }
    });
    //过滤格式
    uploaderMerImg2.filters.push({
        name: 'imageFilter',
        fn: function(item,options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });
    uploaderMerImg2.removeFromQueue = function(value){
        merImgs[1]= "";
        var index = this.getIndexOfItem(value);
        var item = this.queue[index];
        if (item.isUploading) item.cancel();
        this.queue.splice(index, 1);
        item._destroy();
        this.progress = this._getTotalProgress();
    }
    uploaderMerImg2.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            merImgs[1]= response.url;
        }
    };

    //图3
    var uploaderMerImg3 = $scope.uploaderMerImg3 = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数
        removeAfterUpload: false,  //上传后删除文件
        autoUpload:true,     //文件加入队列之后自动上传，默认是false
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    uploaderMerImg3.filters.push({
        name: 'imageFilter',
        fn: function(item, options) {
            return this.queue.length < 1;
        }
    });
    //过滤格式
    uploaderMerImg3.filters.push({
        name: 'imageFilter',
        fn: function(item,options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });
    uploaderMerImg3.removeFromQueue = function(value){
        merImgs[2]= "";
        var index = this.getIndexOfItem(value);
        var item = this.queue[index];
        if (item.isUploading) item.cancel();
        this.queue.splice(index, 1);
        item._destroy();
        this.progress = this._getTotalProgress();
    }
    uploaderMerImg3.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            merImgs[2]= response.url;
        }
    };

    //图4
    var uploaderMerImg4 = $scope.uploaderMerImg4 = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数
        removeAfterUpload: false,  //上传后删除文件
        autoUpload:true,     //文件加入队列之后自动上传，默认是false
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    uploaderMerImg4.filters.push({
        name: 'imageFilter',
        fn: function(item, options) {
            return this.queue.length < 1;
        }
    });
    //过滤格式
    uploaderMerImg4.filters.push({
        name: 'imageFilter',
        fn: function(item,options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });
    uploaderMerImg4.removeFromQueue = function(value){
        merImgs[3]= "";
        var index = this.getIndexOfItem(value);
        var item = this.queue[index];
        if (item.isUploading) item.cancel();
        this.queue.splice(index, 1);
        item._destroy();
        this.progress = this._getTotalProgress();
    }
    uploaderMerImg4.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            merImgs[3]= response.url;
        }
    };

    //图5
    var uploaderMerImg5 = $scope.uploaderMerImg5 = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数
        removeAfterUpload: false,  //上传后删除文件
        autoUpload:true,     //文件加入队列之后自动上传，默认是false
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    uploaderMerImg5.filters.push({
        name: 'imageFilter',
        fn: function(item, options) {
            return this.queue.length < 1;
        }
    });
    //过滤格式
    uploaderMerImg5.filters.push({
        name: 'imageFilter',
        fn: function(item,options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });
    uploaderMerImg5.removeFromQueue = function(value){
        merImgs[4]= "";
        var index = this.getIndexOfItem(value);
        var item = this.queue[index];
        if (item.isUploading) item.cancel();
        this.queue.splice(index, 1);
        item._destroy();
        this.progress = this._getTotalProgress();
    }
    uploaderMerImg5.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            merImgs[4]= response.url;
        }
    };

    //图6
    var uploaderMerImg6 = $scope.uploaderMerImg6 = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数
        removeAfterUpload: false,  //上传后删除文件
        autoUpload:true,     //文件加入队列之后自动上传，默认是false
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    uploaderMerImg6.filters.push({
        name: 'imageFilter',
        fn: function(item, options) {
            return this.queue.length < 1;
        }
    });
    //过滤格式
    uploaderMerImg6.filters.push({
        name: 'imageFilter',
        fn: function(item,options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });
    uploaderMerImg6.removeFromQueue = function(value){
        merImgs[5]= "";
        var index = this.getIndexOfItem(value);
        var item = this.queue[index];
        if (item.isUploading) item.cancel();
        this.queue.splice(index, 1);
        item._destroy();
        this.progress = this._getTotalProgress();
    }
    uploaderMerImg6.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            merImgs[5]= response.url;
        }
    };

    //图uploaderMerImg
    var uploaderMerImg = $scope.uploaderMerImg = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数
        removeAfterUpload: false,  //上传后删除文件
        autoUpload:true,     //文件加入队列之后自动上传，默认是false
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    uploaderMerImg.filters.push({
        name: 'imageFilter',
        fn: function(item, options) {
            return this.queue.length < 1;
        }
    });
    //过滤格式
    uploaderMerImg.filters.push({
        name: 'imageFilter',
        fn: function(item,options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });
    uploaderMerImg.removeFromQueue = function(value){
        $scope.merImg= "";
        var index = this.getIndexOfItem(value);
        var item = this.queue[index];
        if (item.isUploading) item.cancel();
        this.queue.splice(index, 1);
        item._destroy();
        this.progress = this._getTotalProgress();
    }
    uploaderMerImg.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            $scope.merImg= response.url;
        }
    };

    $scope.deleteOwnerImg=function (sta) {
        var title="确认删除图"+sta+"?";
        SweetAlert.swal({
                title:title,
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确认",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true
            },
            function (isConfirm) {
                if (isConfirm) {
                    ownerImgsMap[sta-1]=null;
                    $scope.ownerImgs='';
                    for(var i=0;i<ownerImgsMap.length;i++){
                        if(ownerImgsMap[i]!=null&&ownerImgsMap[i]!=''){
                            $scope.ownerImgs+=ownerImgsMap[i]+",";
                        }
                    }
                    $scope.ownerImgs=$scope.ownerImgs.substr(0,$scope.ownerImgs.length-1);
                    $http.post("awardParam/deleteOwnerImgs","id="+$stateParams.id+"&ownerImgs="+$scope.ownerImgs,
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function(data){
                            if(data.status){
                                $scope.notice(data.msg);
                            }else{
                                $scope.notice(data.msg);
                            }
                            $scope.getOem();
                        });
                }
            });

    };

    $scope.deleteMerImg=function (sta) {
        var title="确认删除图"+sta+"?";
        SweetAlert.swal({
                title:title,
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确认",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true
            },
            function (isConfirm) {
                if (isConfirm) {
                    merImgsMap[sta-1]=null;
                    $scope.merImgs='';
                    for(var i=0;i<merImgsMap.length;i++){
                        if(merImgsMap[i]!=null&&merImgsMap[i]!=''){
                            $scope.merImgs+=merImgsMap[i]+",";
                        }
                    }
                    $scope.merImgs=$scope.merImgs.substr(0,$scope.merImgs.length-1);
                    $http.post("awardParam/deleteMerImgs","id="+$stateParams.id+"&merImgs="+$scope.merImgs,
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function(data){
                            if(data.status){
                                $scope.notice(data.msg);
                            }else{
                                $scope.notice(data.msg);
                            }
                            $scope.getOem();
                        });
                }
            });

    };

    //图1
    //上传图片,定义控制器路径
    var uploaderLeaImg1 = $scope.uploaderLeaImg1 = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数
        removeAfterUpload: false,  //上传后删除文件
        autoUpload:true,     //文件加入队列之后自动上传，默认是false
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    uploaderLeaImg1.filters.push({
        name: 'imageFilter',
        fn: function(item, options) {
            return this.queue.length < 1;
        }
    });
    //过滤格式
    uploaderLeaImg1.filters.push({
        name: 'imageFilter',
        fn: function(item,options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });
    uploaderLeaImg1.removeFromQueue = function(value){
        leaImgs[0]= "";
        var index = this.getIndexOfItem(value);
        var item = this.queue[index];
        if (item.isUploading) item.cancel();
        this.queue.splice(index, 1);
        item._destroy();
        this.progress = this._getTotalProgress();
    }
    uploaderLeaImg1.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            leaImgs[0]= response.url;
        }
    };

    //图2
    var uploaderLeaImg2 = $scope.uploaderLeaImg2 = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数
        removeAfterUpload: false,  //上传后删除文件
        autoUpload:true,     //文件加入队列之后自动上传，默认是false
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    uploaderLeaImg2.filters.push({
        name: 'imageFilter',
        fn: function(item, options) {
            return this.queue.length < 1;
        }
    });
    //过滤格式
    uploaderLeaImg2.filters.push({
        name: 'imageFilter',
        fn: function(item,options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });
    uploaderLeaImg2.removeFromQueue = function(value){
        leaImgs[1]= "";
        var index = this.getIndexOfItem(value);
        var item = this.queue[index];
        if (item.isUploading) item.cancel();
        this.queue.splice(index, 1);
        item._destroy();
        this.progress = this._getTotalProgress();
    }
    uploaderLeaImg2.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            leaImgs[1]= response.url;
        }
    };

    //图3
    var uploaderLeaImg3 = $scope.uploaderLeaImg3 = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数
        removeAfterUpload: false,  //上传后删除文件
        autoUpload:true,     //文件加入队列之后自动上传，默认是false
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    uploaderLeaImg3.filters.push({
        name: 'imageFilter',
        fn: function(item, options) {
            return this.queue.length < 1;
        }
    });
    //过滤格式
    uploaderLeaImg3.filters.push({
        name: 'imageFilter',
        fn: function(item,options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });
    uploaderLeaImg3.removeFromQueue = function(value){
        leaImgs[2]= "";
        var index = this.getIndexOfItem(value);
        var item = this.queue[index];
        if (item.isUploading) item.cancel();
        this.queue.splice(index, 1);
        item._destroy();
        this.progress = this._getTotalProgress();
    }
    uploaderLeaImg3.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            leaImgs[2]= response.url;
        }
    };

    $scope.deleteLeaImg=function (sta) {
        var title="确认删除?";
        SweetAlert.swal({
                title:title,
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确认",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true
            },
            function (isConfirm) {
                if (isConfirm) {
                    leaImgsMap[sta-1]=null;
                    $scope.leaImgs='';
                    for(var i=0;i<leaImgsMap.length;i++){
                        if(leaImgsMap[i]!=null&&leaImgsMap[i]!=''){
                            $scope.leaImgs+=leaImgsMap[i]+",";
                        }
                    }
                    $scope.leaImgs=$scope.leaImgs.substr(0,$scope.leaImgs.length-1);
                    $http.post("awardParam/deleteLeaImgs","id="+$stateParams.id+"&leaderboardBgi="+$scope.leaImgs,
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function(data){
                            if(data.status){
                                $scope.notice(data.msg);
                            }else{
                                $scope.notice(data.msg);
                            }
                            $scope.getOem();
                        });
                }
            });
    };
});
