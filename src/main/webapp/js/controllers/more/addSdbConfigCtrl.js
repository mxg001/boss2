/**
 * 盛代宝配置
 */
angular.module('inspinia',['angularFileUpload']).controller('addSdbConfigCtrl',function($scope,$http,i18nService,FileUploader,SweetAlert,$stateParams,$state){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.addInfo={};
    $scope.img="";
    $scope.img2="";
    $scope.img3="";
    $scope.imgUrl="";
    $scope.imgUrl2="";
    $scope.imgUrl3="";
    $scope.getSdbConfig=function () {
        $http.post("sdbConfig/getSdbConfig","team_id=999",
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.img=data.img;
                    $scope.img2=data.img2;
                    $scope.img3=data.img3;
                    $scope.imgUrl=data.imgUrl;
                    $scope.imgUrl2=data.imgUrl2;
                    $scope.imgUrl3=data.imgUrl3;
                }
            });
    };
    $scope.getSdbConfig();

    $scope.deleteImg=function (sta) {
        var title="";
        if(sta==2){
            title="确认删除图2?";
        }else if(sta==3){
            title="确认删除图3?";
        }
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
                    $http.post("sdbConfig/deleteSdbConfigImg","team_id=999&status="+sta,
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function(data){
                            if(data.status){
                                $scope.notice(data.msg);
                            }else{
                                $scope.notice(data.msg);
                            }
                            $scope.getSdbConfig();
                        });
                }
            });

    };

    $scope.submitting = false;
    //保存
    $scope.saveProduct=function () {
        if($scope.submitting){
            return;
        }
        $scope.subInfo=angular.copy($scope.addInfo);
        var team_ad_url='';
        if($scope.uploaderImg1.queue.length<1){
            $scope.subInfo.img=null;
            if($scope.img!=''){
                team_ad_url+=$scope.img+',';
            }
        }else{
            team_ad_url+=$scope.subInfo.img+',';
        }
        if($scope.uploaderImg2.queue.length<1){
            $scope.subInfo.img2=null;
            if($scope.img2!=''){
                team_ad_url+=$scope.img2+',';
            }
        }else{
            team_ad_url+=$scope.subInfo.img2+',';
        }
        if($scope.uploaderImg3.queue.length<1){
            $scope.subInfo.img3=null;
            if($scope.img3!=''){
                team_ad_url+=$scope.img3+',';
            }
        }else{
            team_ad_url+=$scope.subInfo.img3+',';
        }
        if(team_ad_url!=''){
            team_ad_url=team_ad_url.substr(0,team_ad_url.length-1)
        }else {
            $scope.notice("最少需上传1张图片");
            return;
        }
        $scope.submitting = true;
        $http.post("sdbConfig/saveSdbConfigImg","team_id=999&team_ad_url="+team_ad_url,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                $scope.submitting = false;
                if(data.status){
                    $scope.notice(data.msg);
                    $scope.getSdbConfig();
                }else{
                    $scope.notice(data.msg);
                }
            })
            .error(function(data){
                $scope.submitting = false;
                $scope.notice(data.msg);
            });
    };

    //图1
    //上传图片,定义控制器路径
    var uploaderImg1 = $scope.uploaderImg1 = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数
        removeAfterUpload: false,  //上传后删除文件
        autoUpload:true,     //文件加入队列之后自动上传，默认是false
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    uploaderImg1.filters.push({
        name: 'imageFilter',
        fn: function(item, options) {
            return this.queue.length < 1;
        }
    });
    //过滤格式
    uploaderImg1.filters.push({
        name: 'imageFilter',
        fn: function(item,options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });
    uploaderImg1.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            $scope.addInfo.img= response.url;
        }
    };

    //图2
    var uploaderImg2 = $scope.uploaderImg2 = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数
        removeAfterUpload: false,  //上传后删除文件
        autoUpload:true,     //文件加入队列之后自动上传，默认是false
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    uploaderImg2.filters.push({
        name: 'imageFilter',
        fn: function(item, options) {
            return this.queue.length < 1;
        }
    });
    //过滤格式
    uploaderImg2.filters.push({
        name: 'imageFilter',
        fn: function(item,options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });
    uploaderImg2.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            $scope.addInfo.img2= response.url;
        }
    };

    //图3
    var uploaderImg3 = $scope.uploaderImg3 = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数
        removeAfterUpload: false,  //上传后删除文件
        autoUpload:true,     //文件加入队列之后自动上传，默认是false
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    uploaderImg3.filters.push({
        name: 'imageFilter',
        fn: function(item, options) {
            return this.queue.length < 1;
        }
    });
    //过滤格式
    uploaderImg3.filters.push({
        name: 'imageFilter',
        fn: function(item,options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });
    uploaderImg3.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            $scope.addInfo.img3= response.url;
        }
    };

});