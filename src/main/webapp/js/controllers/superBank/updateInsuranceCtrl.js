/**
 * 修改保险公司
 */
angular.module('inspinia',['angularFileUpload']).controller("updateInsuranceCtrl", function($scope, $http, $state, $stateParams,FileUploader) {
    //数据源
    $scope.createOrderTypeList = [{text:"实际回调创建",value:1}
        ,{text:"批量导入创建",value:2}];
    $http({
        url:"insuranceCompany/detail/" + $stateParams.id,
        method:"GET"
    }).success(function(result){
        if(result && result.status){
            $scope.baseInfo = result.data;
        } else {
            $scope.notice(result.msg);
        }
    });


    //上传图片,定义控制器路径
    var uploaderLogo = $scope.uploaderLogo = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数
        removeAfterUpload: false,  //上传后删除文件
        autoUpload:true,     //文件加入队列之后自动上传，默认是false
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
    $scope.uploaderLogo.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            $scope.baseInfo.showLogo = response.url;
        }
    };

    //提交
    $scope.submit = function(){
        var showOrder=$scope.baseInfo.showOrder
        if(!validate(showOrder)){
            $scope.notice("顺序输入不合法")
            return
        }
        $scope.submitting = true;
        $http({
            url: "insuranceCompany/updateCompany",
            method: "POST",
            data: $scope.baseInfo
        }).success(function(result){
            $scope.submitting = false;
            $scope.notice(result.msg);
            if(result.status){
                $state.transitionTo('superBank.insuranceCompany',null,{reload:true});
            }
        })
    }
    function validate(num)
    {
        var reg = /^\d+(?=\.{0,1}\d+$|$)/
        if(reg.test(num)) return true;
        return false ;
    }

});
