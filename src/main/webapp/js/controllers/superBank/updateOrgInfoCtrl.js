/**
 * 超级银行家组织修改
 */
angular.module('inspinia',['angularFileUpload']).controller('updateOrgInfoCtrl',function($scope, $http, $state,$stateParams, FileUploader,SweetAlert){
    //获取银行组织的初始信息
    $http({
        url:"superBank/orgInfoDetail?orgId=" + $stateParams.orgId,
        method:"GET"
    }).success(function(result){
        if (result.status){
            $scope.baseInfo = result.data;
            if($scope.baseInfo.indexStyle == '4' ){
                $scope.baseInfo.indexStyle == '4';
            }else{
                $scope.baseInfo.indexStyle = '3';
            }

            $scope.tutorModelList = angular.copy($scope.baseInfo.tutorModelList);
            $scope.bankModelList = angular.copy($scope.baseInfo.bankModelList);

            if($scope.baseInfo.tutorModelList != null && $scope.baseInfo.tutorModelList.length>0){
                $scope.tutorModelList=[
                    {uploaderTutor:$scope.uploaderTutor0,modulesImages:$scope.tutorModelList[0].modulesImages,modulesMatchUrl:$scope.tutorModelList[0].modulesMatchUrl,style:$scope.tutorModelList[0].style,order:$scope.tutorModelList[0].order,modulesImagesUrl:$scope.tutorModelList[0].modulesImagesUrl},
                    {uploaderTutor:$scope.uploaderTutor1,modulesImages:$scope.tutorModelList[1].modulesImages,modulesMatchUrl:$scope.tutorModelList[1].modulesMatchUrl,style:$scope.tutorModelList[1].style,order:$scope.tutorModelList[1].order,modulesImagesUrl:$scope.tutorModelList[1].modulesImagesUrl}
                ];
            }else{
                $scope.tutorModelList=[
                    {uploaderTutor:$scope.uploaderTutor0,modulesImages:'',modulesMatchUrl:'',style:1,order:1},
                    {uploaderTutor:$scope.uploaderTutor1,modulesImages:'',modulesMatchUrl:'',style:1,order:2}
                ];
            }

            if($scope.baseInfo.bankModelList != null && $scope.baseInfo.bankModelList.length>0){
                $scope.bankModelList=[
                    {uploaderBank:$scope.uploaderBank0,modulesImages:$scope.bankModelList[0].modulesImages,modulesMatchUrl:$scope.bankModelList[0].modulesMatchUrl,style:$scope.bankModelList[0].style,order:$scope.bankModelList[0].order,modulesImagesUrl:$scope.bankModelList[0].modulesImagesUrl},
                ];
            }else{
                $scope.bankModelList=[
                    {uploaderBank:$scope.uploaderBank0,modulesImages:'',modulesMatchUrl:'',style:2,order:1}
                ];
            }

        } else {
           $scope.notice(result.msg);
        }
    }).error(function () {
        $scope.notice("服务器异常，请稍后再试");
    });
    $scope.businessData=[];
    /*三大模块部分-start*/
    /////// 优秀导师模块
    tutorModelUpload(0);
    tutorModelUpload(1);
    function tutorModelUpload(index) {
        $scope['uploaderTutor' + index] = new FileUploader({
            url: 'upload/fileUpload.do',
            queueLimit: 1,   //文件个数
            removeAfterUpload: false,  //上传后删除文件
            autoUpload: true,     //文件加入队列之后自动上传，默认是false
            headers: {'X-CSRF-TOKEN': $scope.csrfData}
        });
        //过滤格式
        $scope['uploaderTutor' + index].filters.push({
            name: 'imageFilter',
            fn: function (item /*{File|FileLikeObject}*/, options) {
                var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
                return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
            }
        });
        $scope['uploaderTutor' + index].onSuccessItem = function (fileItem, response, status, headers) {// 上传成功后的回调函数，在里面执行提交
            if (response.url != null) { // 回调参数url
                $scope.tutorModelList[index].modulesImages = response.url;
            }
        };
        $scope['uploaderTutor' + index].removeFromQueue = function(value){
            var _index = this.getIndexOfItem(value);
            var item = this.queue[_index];
            if (item.isUploading) item.cancel();
            this.queue.splice(_index, 1);
            $scope.tutorModelList[index].modulesImages = "";
        };
    }

    /////// 银行家大学模块
    bankModelUpload(0);
    function bankModelUpload(index) {
        $scope['uploaderBank' + index] = new FileUploader({
            url: 'upload/fileUpload.do',
            queueLimit: 1,   //文件个数
            removeAfterUpload: false,  //上传后删除文件
            autoUpload: true,     //文件加入队列之后自动上传，默认是false
            headers: {'X-CSRF-TOKEN': $scope.csrfData}
        });
        //过滤格式
        $scope['uploaderBank' + index].filters.push({
            name: 'imageFilter',
            fn: function (item /*{File|FileLikeObject}*/, options) {
                var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
                return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
            }
        });
        $scope['uploaderBank' + index].onSuccessItem = function (fileItem, response, status, headers) {// 上传成功后的回调函数，在里面执行提交
            if (response.url != null) { // 回调参数url
                $scope.bankModelList[index].modulesImages = response.url;
            }
        };
        $scope['uploaderBank' + index].removeFromQueue = function(value){
            var _index = this.getIndexOfItem(value);
            var item = this.queue[_index];
            if (item.isUploading) item.cancel();
            this.queue.splice(_index, 1);
            $scope.bankModelList[index].modulesImages = "";
        };
    }

    /*三大模块部分-end*/




    //上传图片,定义控制器路径
    $scope.uploaderImg = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数
        removeAfterUpload: false,  //上传后删除文件
        autoUpload:true,     //文件加入队列之后自动上传，默认是false
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤格式
    $scope.uploaderImg.filters.push({
        name: 'imageFilter',
        fn: function(item /*{File|FileLikeObject}*/, options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });
    $scope.uploaderImg.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            $scope.baseInfo.orgLogo = response.url;
        }
    };
    //上传图片,定义控制器路径
    $scope.uploaderSeal = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数
        removeAfterUpload: false,  //上传后删除文件
        autoUpload:true,     //文件加入队列之后自动上传，默认是false
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤格式
    $scope.uploaderSeal.filters.push({
        name: 'imageFilter',
        fn: function(item /*{File|FileLikeObject}*/, options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });
    $scope.uploaderSeal.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            $scope.baseInfo.authorizedUnitSeal = response.url;
        }
    };
    //上传图片,定义控制器路径
    $scope.uploaderQr = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数
        removeAfterUpload: false,  //上传后删除文件
        autoUpload:true,     //文件加入队列之后自动上传，默认是false
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤格式
    $scope.uploaderQr.filters.push({
        name: 'imageFilter',
        fn: function(item /*{File|FileLikeObject}*/, options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });
    $scope.uploaderQr.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            $scope.baseInfo.publicQrCode = response.url;
        }
    };

    //APP下载二维码图片
    //上传图片,定义控制器路径
    $scope.uploaderAPPQR = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数
        removeAfterUpload: false,  //上传后删除文件
        autoUpload:true,     //文件加入队列之后自动上传，默认是false
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤格式
    $scope.uploaderAPPQR.filters.push({
        name: 'imageFilter',
        fn: function(item /*{File|FileLikeObject}*/, options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });
    $scope.uploaderAPPQR.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            $scope.baseInfo.appQrCode= response.url;
        }
    };
    $scope.uploaderAPPQR.removeFromQueue = function(value){
        var _index = this.getIndexOfItem(value);
        var item = this.queue[_index];
        if (item.isUploading) item.cancel();
        this.queue.splice(_index, 1);
        $scope.baseInfo.appQrCode = "";
    };
    //上传图片,定义控制器路径
    $scope.uploaderMemeberLogo = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数
        removeAfterUpload: false,  //上传后删除文件
        headers : {'X-CSRF-TOKEN' : $scope.csrfData},
        autoUpload:true     //文件加入队列之后自动上传，默认是false
    });
    //过滤格式
    $scope.uploaderMemeberLogo.filters.push({
        name: 'imageFilter',
        fn: function(item /*{File|FileLikeObject}*/, options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });
    $scope.uploaderMemeberLogo.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            $scope.baseInfo.memberCenterLogo = response.url;
        }
    };
    //上传图片,定义控制器路径
    $scope.uploaderStartPage = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数
        removeAfterUpload: false,  //上传后删除文件
        autoUpload:true,     //文件加入队列之后自动上传，默认是false
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤格式
    $scope.uploaderStartPage.filters.push({
        name: 'imageFilter',
        fn: function(item /*{File|FileLikeObject}*/, options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });
    $scope.uploaderStartPage.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            $scope.baseInfo.startPage = response.url;
        }
    };
    //上传图片,定义控制器路径
    $scope.uploaderAppLogo = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数
        removeAfterUpload: false,  //上传后删除文件
        autoUpload:true,     //文件加入队列之后自动上传，默认是false
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤格式
    $scope.uploaderAppLogo.filters.push({
        name: 'imageFilter',
        fn: function(item /*{File|FileLikeObject}*/, options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });
    $scope.uploaderAppLogo.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            $scope.baseInfo.appLogo = response.url;
        }
    };
    //上传图片,定义控制器路径
    $scope.uploaderShareMessage = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数
        removeAfterUpload: false,  //上传后删除文件
        autoUpload:true,     //文件加入队列之后自动上传，默认是false
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤格式
    $scope.uploaderShareMessage.filters.push({
        name: 'imageFilter',
        fn: function(item /*{File|FileLikeObject}*/, options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });
    $scope.uploaderShareMessage.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            $scope.baseInfo.shareMessageLogo = response.url;
        }
    };
    //上传图片,定义控制器路径
    $scope.uploaderShare1 = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数
        removeAfterUpload: false,  //上传后删除文件
        autoUpload:true,     //文件加入队列之后自动上传，默认是false
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤格式
    $scope.uploaderShare1.filters.push({
        name: 'imageFilter',
        fn: function(item /*{File|FileLikeObject}*/, options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });
    $scope.uploaderShare1.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            $scope.baseInfo.shareTemplateImage1 = response.url;
        }
    };
    //上传图片,定义控制器路径
    $scope.uploaderShare2 = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数
        removeAfterUpload: false,  //上传后删除文件
        autoUpload:true,     //文件加入队列之后自动上传，默认是false
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤格式
    $scope.uploaderShare2.filters.push({
        name: 'imageFilter',
        fn: function(item /*{File|FileLikeObject}*/, options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });
    $scope.uploaderShare2.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            $scope.baseInfo.shareTemplateImage2 = response.url;
        }
    };
    $scope.uploaderShare2.removeFromQueue = function(value){
        var _index = this.getIndexOfItem(value);
        var item = this.queue[_index];
        if (item.isUploading) item.cancel();
        this.queue.splice(_index, 1);
        $scope.baseInfo.shareTemplateImage2 = "";
    };
    //上传图片,定义控制器路径
    $scope.uploaderShare3 = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数
        removeAfterUpload: false,  //上传后删除文件
        autoUpload:true,     //文件加入队列之后自动上传，默认是false
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤格式
    $scope.uploaderShare3.filters.push({
        name: 'imageFilter',
        fn: function(item /*{File|FileLikeObject}*/, options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });
    $scope.uploaderShare3.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            $scope.baseInfo.shareTemplateImage3 = response.url;
        }
    };
    $scope.uploaderShare3.removeFromQueue = function(value){
        var _index = this.getIndexOfItem(value);
        var item = this.queue[_index];
        if (item.isUploading) item.cancel();
        this.queue.splice(_index, 1);
        $scope.baseInfo.shareTemplateImage3 = "";
    };
    
    $scope.copyFunction = function(value){
    	var oInput = document.createElement('input');
        oInput.value = value;
        document.body.appendChild(oInput);
        oInput.select(); // 选择对象
        document.execCommand("Copy"); // 执行浏览器复制命令
        oInput.className = 'oInput';
        oInput.style.display='none';
//        alert('复制成功');
    }

    //上传首页背景,定义控制器路径
    $scope.uploaderHomeBackground = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数
        removeAfterUpload: false,  //上传后删除文件
        autoUpload:true,     //文件加入队列之后自动上传，默认是false
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤格式
    $scope.uploaderHomeBackground.filters.push({
        name: 'imageFilter',
        fn: function(item /*{File|FileLikeObject}*/, options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });
    $scope.uploaderHomeBackground.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            $scope.baseInfo.homeBackground = response.url;
        }
    };

    $scope.businessList = {
        data: 'businessData',
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs: [
            {field: 'isCheck',displayName: '选择',width: 180,
                cellTemplate:
                    '<input type="checkbox" ng-model="row.entity.isCheck" ng-true-value="1" ng-false-value="0"/>'
            },
            {field: 'businessName',displayName: '业务名称',width: 250},
            {field: 'id',displayName: '业务ID',width: 250}
        ],
        onRegisterApi: function(gridApi) {                //选中行配置
            $scope.rateGridApi = gridApi;

        }
    };

    $http({
        url : "superBank/getBusinessConfList?orgId="+$stateParams.orgId,
        method : "POST"
    }).success(function(data){
        if(data.status){
            $scope.businessData = data.data;
        } else {
            $scope.notice(data.msg);
        }
    }).error(function(){
        $scope.notice("服务器异常，请稍候再试");
        $scope.submitting = false;
    });

    $scope.switchStatus=function(){
        SweetAlert.swal({
                title: "",
                text:  "请确认是否立即重置本商户密钥。重置后原密钥会自动失效，无法上送请求。",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确定重置",
                cancelButtonText: "取消重置",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                	 $http({
                         url : "superBank/resetSecretKey",
                         method : "POST",
                     }).success(function(data){
                         if(data.status){
                        	 $scope.baseInfo.openMerchantKey=data.data;
                         } else {
                         }
                     }).error(function(){
                         $scope.notice("服务器异常，请稍候再试");
                         $scope.submitting = false;
                     });
                    	
                } 
            });
    };
    $scope.submit = function(){

        //校验三大模块的链接是否有效
        if($scope.baseInfo.indexStyle=="3"){
            $scope.baseInfo.tutorModelList = angular.copy($scope.tutorModelList);
            $scope.baseInfo.bankModelList = angular.copy($scope.bankModelList);
            var reg=/(http|ftp|https):\/\/[\w\-_]+(\.[\w\-_]+)+([\w\-\.,@?^=%&:/~\+#]*[\w\-\@?^=%&/~\+#])?/;

            for (var i = 0; i < $scope.baseInfo.tutorModelList.length; i++) {
                var tutorUrl = $scope.baseInfo.tutorModelList[i].modulesMatchUrl;
                if(($scope.baseInfo.indexStyle=="3") && (tutorUrl != '') && (!reg.test(tutorUrl))){
                    $scope.notice("请输入正确的链接");
                    $scope.submitting = false;
                    return;
                }
                $scope.baseInfo.tutorModelList[i].uploaderTutor=null;
            }
            for (var i = 0; i < $scope.baseInfo.bankModelList.length; i++) {
                var bankUrl = $scope.baseInfo.bankModelList[i].modulesMatchUrl;
                if(($scope.baseInfo.indexStyle=="3") && (bankUrl != '') && (!reg.test(bankUrl))){
                    $scope.notice("请输入正确的链接");
                    $scope.submitting = false;
                    return;
                }
                $scope.baseInfo.bankModelList[i].uploaderBank=null;
            }
        }
        var ids = "";
        angular.forEach($scope.businessData,function(data,index){
            if(data.isCheck == true && !data.disabled){
                if(ids!=""){
                    ids = ids+","+data.id;
                }else {
                    ids = data.id;
                }
            }
        });

        if(ids==""){
            $scope.notice("请选择升级方案业务");
            return false;
        }
        $scope.baseInfo.checkIds = ids;

        if($scope.baseInfo.upMemberNeedpay!=1 && $scope.baseInfo.upMemberNeedperfect!=1  && $scope.baseInfo.upMemberNeedlock!=1){
            $scope.notice("升级专员条件必须至少填写一项");
            return false;
        }else{

            if($scope.baseInfo.upMemberNeedlock==1 && ($scope.baseInfo.upMemberLocknum===''||$scope.baseInfo.upMemberLocknum===null)){
                $scope.notice("升级专员条件 勾选锁粉人数后需填写人数数量");
                return false;
            }
            if($scope.baseInfo.upMemberNeedlock==1 && $scope.baseInfo.upMemberLocknum<=0){
                $scope.notice("升级专员条件 勾选锁粉人数后需填写人数数量 请检查");
                return false;
            }
        }
        if(!($scope.baseInfo.upMemberMposnum===''||$scope.baseInfo.upMemberMposnum===null) && $scope.baseInfo.upMemberMposnum<=0){
            $scope.notice("升级专员条件 采购mpos机数量请填写大于零的值");
            return false;
        }
        if(($scope.baseInfo.upManagerNum===''||$scope.baseInfo.upManagerNum===null) && ($scope.baseInfo.upManagerCardnum===''||$scope.baseInfo.upManagerCardnum===null) && ($scope.baseInfo.upManagerLocknum===''||$scope.baseInfo.upManagerLocknum===null) ){
            $scope.notice("升级经理条件必须至少填写一项");
            return false;
        }

        if($scope.baseInfo.upManagerNum <= 0){         //升级经理条件 第一项等于-1  判断其他两项
            if($scope.baseInfo.upManagerCardnum<= 0||($scope.baseInfo.upManagerCardnum===''||$scope.baseInfo.upManagerCardnum===null)){
                if($scope.baseInfo.upManagerLocknum<= 0||($scope.baseInfo.upManagerLocknum===''||$scope.baseInfo.upManagerLocknum===null)){
                    $scope.notice("升级条件不能为空，请检查你的经理升级条件 ！");
                    return false;
                }
            }
            if($scope.baseInfo.upManagerLocknum<= 0||($scope.baseInfo.upManagerLocknum===''||$scope.baseInfo.upManagerLocknum===null)){
                if($scope.baseInfo.upManagerCardnum<= 0||($scope.baseInfo.upManagerCardnum===''||$scope.baseInfo.upManagerCardnum===null)){
                    $scope.notice("升级条件不能为空，请检查你的经理升级条件 ！");
                    return false;
                }
            }
        }
        if($scope.baseInfo.upManagerCardnum<= 0) {     //升级经理条件 第二项等于-1  判断其他两项
            if($scope.baseInfo.upManagerNum<= 0||($scope.baseInfo.upManagerNum===''||$scope.baseInfo.upManagerNum===null)){
                if($scope.baseInfo.upManagerLocknum<= 0||($scope.baseInfo.upManagerLocknum===''||$scope.baseInfo.upManagerLocknum===null)){
                    $scope.notice("升级条件不能为空，请检查你的经理升级条件 ！");
                    return false;
                }
            }
            if($scope.baseInfo.upManagerLocknum<= 0||($scope.baseInfo.upManagerLocknum===''||$scope.baseInfo.upManagerLocknum===null)){
                if($scope.baseInfo.upManagerNum<= 0||($scope.baseInfo.upManagerNum===''||$scope.baseInfo.upManagerNum===null)){
                    $scope.notice("升级条件不能为空，请检查你的经理升级条件 ！");
                    return false;
                }
            }

        }
        if($scope.baseInfo.upManagerLocknum<= 0) {    //升级经理条件 第三项等于-1  判断其他两项
            if($scope.baseInfo.upManagerNum<= 0||($scope.baseInfo.upManagerNum===''||$scope.baseInfo.upManagerNum===null)){
                if($scope.baseInfo.upManagerCardnum<= 0||($scope.baseInfo.upManagerCardnum===''||$scope.baseInfo.upManagerCardnum===null)){
                    $scope.notice("升级条件不能为空，请检查你的经理升级条件 ！");
                    return false;
                }
            }
            if($scope.baseInfo.upManagerCardnum<= 0||($scope.baseInfo.upManagerCardnum===''||$scope.baseInfo.upManagerCardnum===null)){
                if($scope.baseInfo.upManagerNum<= 0||($scope.baseInfo.upManagerNum===''||$scope.baseInfo.upManagerNum===null)){
                    $scope.notice("升级条件不能为空，请检查你的经理升级条件 ！");
                    return false;
                }
            }

        }
        if(!($scope.baseInfo.upManagerMposnum===''||$scope.baseInfo.upManagerMposnum===null) && $scope.baseInfo.upManagerMposnum<=0){
            $scope.notice("升级经理条件 采购mpos机数量请填写大于零的值");
            return false;
        }

        if(($scope.baseInfo.upBankerNum===''||$scope.baseInfo.upBankerNum===null) && ($scope.baseInfo.upBankerCardnum===''||$scope.baseInfo.upBankerCardnum===null) && ($scope.baseInfo.upBankerLocknum===''||$scope.baseInfo.upBankerLocknum===null) ){
            $scope.notice("升级银行家条件必须至少填写一项");
            return false;
        }

        if($scope.baseInfo.upBankerNum<= 0){       //升级银行家条件第一项 判断其他两项
            if($scope.baseInfo.upBankerCardnum<= 0||$scope.baseInfo.upBankerCardnum===''||$scope.baseInfo.upBankerCardnum===null){
                if($scope.baseInfo.upBankerLocknum<= 0||$scope.baseInfo.upBankerLocknum===''||$scope.baseInfo.upBankerLocknum===null){
                    $scope.notice("升级条件不能为空，请检查你的银行家升级条件 ！");
                    return false;
                }
            }

            if($scope.baseInfo.upBankerLocknum<= 0||$scope.baseInfo.upBankerLocknum===''||$scope.baseInfo.upBankerLocknum===null){
                if($scope.baseInfo.upBankerCardnum<= 0||$scope.baseInfo.upBankerCardnum===''||$scope.baseInfo.upBankerCardnum===null){
                    $scope.notice("升级条件不能为空，请检查你的银行家升级条件 ！");
                    return false;
                }
            }
        }
        if($scope.baseInfo.upBankerCardnum<= 0){      //升级银行家条件第二项 判断其他两项
            if($scope.baseInfo.upBankerNum<= 0||$scope.baseInfo.upBankerNum===''||$scope.baseInfo.upBankerNum===null){
                if($scope.baseInfo.upBankerLocknum<= 0||$scope.baseInfo.upBankerLocknum===''||$scope.baseInfo.upBankerLocknum===null){
                    $scope.notice("升级条件不能为空，请检查你的银行家升级条件 ！");
                    return false;
                }
            }

            if($scope.baseInfo.upBankerLocknum<= 0||$scope.baseInfo.upBankerLocknum===''||$scope.baseInfo.upBankerLocknum===null){
                if($scope.baseInfo.upBankerNum<= 0||$scope.baseInfo.upBankerNum===''||$scope.baseInfo.upBankerNum===null){
                    $scope.notice("升级条件不能为空，请检查你的银行家升级条件 ！");
                    return false;
                }
            }

        }
        if($scope.baseInfo.upBankerLocknum<= 0){      //升级银行家条件第三项 判断其他两项
            if($scope.baseInfo.upBankerNum<= 0||$scope.baseInfo.upBankerNum===''||$scope.baseInfo.upBankerNum===null){
                if($scope.baseInfo.upBankerCardnum<= 0||$scope.baseInfo.upBankerCardnum===''||$scope.baseInfo.upBankerCardnum===null){
                    $scope.notice("升级条件不能为空，请检查你的银行家升级条件 ！");
                    return false;
                }
            }

            if($scope.baseInfo.upBankerCardnum<= 0||$scope.baseInfo.upBankerCardnum===''||$scope.baseInfo.upBankerCardnum===null){
                if($scope.baseInfo.upBankerNum<= 0||$scope.baseInfo.upBankerNum===''||$scope.baseInfo.upBankerNum===null){
                    $scope.notice("升级条件不能为空，请检查你的银行家升级条件 ！");
                    return false;
                }
            }
        }
        if(!($scope.baseInfo.upBankerMposnum===''||$scope.baseInfo.upBankerMposnum===null) && $scope.baseInfo.upBankerMposnum<=0){
            $scope.notice("升级银行家条件 采购mpos机数量请填写大于零的值");
            return false;
        }




        console.log($scope.baseInfo);
        $scope.submitting = true;
        $http({
            url : "superBank/updateOrgInfo",
            method : "POST",
            data : $scope.baseInfo
        }).success(function(data){
            if(data.status){
                $scope.notice(data.msg);
                $state.transitionTo('superBank.orgInfoManager',null,{reload:true});
            } else {
                $scope.notice(data.msg);
                $scope.submitting = false;
            }
        }).error(function(){
            $scope.notice("服务器异常，请稍候再试");
            $scope.submitting = false;
        });
    }

});