/**
 * 系统红包配置修改
 */
angular.module('inspinia',['angularFileUpload','uiSwitch']).controller('editRedConfigCtrl',function($scope, $http,$stateParams, $state, FileUploader){
    $scope.baseInfo = {};
    $scope.changeBusType = function () {
        if($scope.baseInfo.bus_type == '2'||$scope.baseInfo.bus_type == '3'){
            $scope.is_alow = true;
        }else {
            $scope.is_alow = false
        }
    }
    $scope.baseInfo.id = $stateParams.id;
    var busType = [];
    angular.forEach($scope.redBusType, function(item){
        if(item.value!="0"){
            busType[busType.length] = item;
        }
    });
    $scope.redBusType = busType;
    //组织列表
    $scope.orgs=[];
    $scope.getAllOrg = function(){
        $http({
            url:"superBank/getOrgInfoList",
            method:"POST"
        }).success(function(msg){
            if(msg.status){
                $scope.orgs = msg.data;
                $scope.orgs.unshift({orgId:0,orgName:"平台"});
            }
        }).error(function(){
            $scope.notice("获取组织列表异常");
        });
    };
    $scope.getAllOrg();

    $scope.imgLength = 0;
    $scope.redConfiInfo = function(){
        $http({
            url:"red/editRedConfig",
            method:"POST",
            data : $scope.baseInfo,
        }).success(function(msg){
            if(msg.status){
                $scope.baseInfo = msg.data;
                $scope.imgLength = $scope.baseInfo.urlList.length;
                $scope.changeBusType();
            }
        }).error(function(){
            $scope.notice("获取红包配置信息异常");
        });
    };
    $scope.redConfiInfo();

    //上传图片,定义控制器路径
    $scope.uploaderImg = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 9-$scope.imgLength,   //文件个数
        removeAfterUpload: true,  //上传后删除文件
        // autoUpload:true,     //文件加入队列之后自动上传，默认是false
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


    $scope.submit = function(){
        $scope.submitting = true;
        $scope.baseInfo.img_url = "";
        if($scope.baseInfo.imgList.length > 0){
            if($scope.baseInfo.imgList.length + $scope.uploaderImg.queue.length > 9){
                $scope.notice("宣传图片最多不能超过9张");
                return;
            }
            angular.forEach($scope.baseInfo.imgList, function(item){
                $scope.baseInfo.img_url = $scope.baseInfo.img_url + item + ";";
            });
        }
        if($scope.uploaderImg.queue.length > 0){
            $scope.uploaderImg.uploadAll();
            $scope.uploaderImg.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
                if (response.url != null) { // 回调参数url
                    $scope.baseInfo.img_url = $scope.baseInfo.img_url + response.url + ";";
                }
            }
            $scope.uploaderImg.onCompleteAll = function(){
                submitData();
            }
        } else {
            submitData();
        }
    }
    //提交数据
    function submitData (){
        if($scope.baseInfo.allow_org_profit){
            $scope.baseInfo.allow_org_profit = "1";
        }else {
            $scope.baseInfo.allow_org_profit = "0";
        }
        $http({url:"red/editRedConfigSub",data : $scope.baseInfo,method :"post"}).success(
            function(msg){
                $scope.notice(msg.msg);
                if(msg.status){
                    $state.transitionTo('red.redConfigs',null,{reload:true});
                }
            }
        );
    }
    Array.prototype.indexOf = function (val) {
        for (var i = 0; i < this.length; i++) {
            if (this[i] == val) return i;
        }
        return -1;
    };

    Array.prototype.remove = function (val) {
        var index = this.indexOf(val);
        if (index > -1) {
            this.splice(index, 1);
        }
    };
    Array.prototype.removeIndex = function (index) {
        if (index > -1) {
            this.splice(index, 1);
        }
    };

    //删除存量的图片，删掉aliyun url集合对应的元素，以及img集合
    $scope.deleteImg = function(img){
        var index = $scope.baseInfo.urlList.indexOf(img);
        $scope.baseInfo.imgList.removeIndex(index);
        $scope.baseInfo.urlList.remove(img);
        $scope.imgLength = $scope.baseInfo.urlList.length;
    }

    $scope.is_alow =false;
    $scope.changeBusType = function () {
        if($scope.baseInfo.bus_type === '2' ||  $scope.baseInfo.bus_type === '3'){
            $scope.is_alow = true;
        }else {
            $scope.is_alow  =   false
        }
    }

});