/**
 * 新增贷款机构
 */
angular.module('inspinia',['angularFileUpload']).controller("addLoanInstitutionManagementCtrl", function($scope, $http, $state, $stateParams,FileUploader) {
    //数据源
    $scope.baseInfo = {};//奖励要求};
    $scope.rewardRequirementsList = [{text:"有效注册",value:"1"},{text:"授信成功",value:"3"},{text:"有效借款",value:"2"}];//奖励要求
    $scope.accessWayList = [{text:"H5",value:1},{text:"API",value:2}];//接入方式
    $scope.baseInfo={accessWay:1,profitType:'1',profitType:'1',rewardRequirements:'1'};
    //上传图片,定义控制器路径
    var uploaderImg = $scope.uploaderImg = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数
        removeAfterUpload: false,  //上传后删除文件
        autoUpload:true,     //文件加入队列之后自动上传，默认是false
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    uploaderImg.filters.push({
        name: 'imageFilter',
        fn: function(item, options) {
            return this.queue.length < 1;
        }
    });
    //过滤格式
    uploaderImg.filters.push({
        name: 'imageFilter',
        fn: function(item /*{File|FileLikeObject}*/, options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });
    $scope.uploaderImg.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            $scope.baseInfo.h5Link = response.url;
        }
    };

    //上传图片,定义控制器路径
    $scope.uploaderSpecial = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数
        removeAfterUpload: false,  //上传后删除文件
        autoUpload:true,     //文件加入队列之后自动上传，默认是false
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    $scope.uploaderSpecial.filters.push({
        name: 'imageFilter',
        fn: function(item, options) {
            return this.queue.length < 1;
        }
    });
    //过滤格式
    $scope.uploaderSpecial.filters.push({
        name: 'imageFilter',
        fn: function(item /*{File|FileLikeObject}*/, options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });
    $scope.uploaderSpecial.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            $scope.baseInfo.specialImage = response.url;
        }
    };

    //上传图片,定义控制器路径
    var uploaderLogo = $scope.uploaderLogo = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数
        removeAfterUpload: false,  //上传后删除文件
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    uploaderLogo.filters.push({
        name: 'imageFilter',
        fn: function(item, options) {
            return this.queue.length < 1;
        }
    });
    //过滤格式
    uploaderLogo.filters.push({
        name: 'imageFilter',
        fn: function(item /*{File|FileLikeObject}*/, options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });
    // $scope.uploaderLogo.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
    //     if (response.url != null) { // 回调参数url
    //         $scope.baseInfo.showLog = response.url;
    //     }
    // };

    //提交
    $scope.submit = function(){
        $scope.submitting = true;
        if($scope.baseInfo.showLogo != null){
        	//提交数据到后台
        	submitData()
        } else {
        	$scope.uploaderLogo.uploadAll();
        	$scope.uploaderLogo.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
                if (response.url != null) { // 回调参数url
                	$scope.baseInfo.showLogo = response.url; 
                }
                submitData()
        	}
            }
    }
    function submitData(){
    	$http({
            url: "loanInstitutionManagement/addLoan",
            method: "POST",
            data: $scope.baseInfo
        }).success(function(result){
            $scope.submitting = false;
            $scope.notice(result.msg);
            if(result.status){
                $state.transitionTo('superBank.loanInstitutionManagement',null,{reload:true});
            }
        })
    }
});
