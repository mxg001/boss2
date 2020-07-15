/**
 * 新增银行
 */
angular.module('inspinia',['angularFileUpload']).controller("addInsuranceProductCtrl", function($scope, $http, $state, $stateParams,FileUploader) {
    //数据源
    $scope.baseInfo = {productType:"1",bonusSettleTime:"1",bonusType:2,recommendStatus:0};
    $scope.productTypeList = [{text:"意外险",value:"1"},{text:"健康医疗险",value:"2"} ,{text:"家财险",value:"3"} ,{text:"医疗意外险",value:"4"}];
    $scope.bonusSettleTimeList=[{text:"实时",value:"1"},{text:"周结",value:"2"},{text:"月结",value:"3"}];
    //上传图片,定义控制器路径
    $scope.uploaderLogo = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数
        removeAfterUpload: false,  //上传后删除文件
        autoUpload:true,     //文件加入队列之后自动上传，默认是false
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    $scope.uploaderLogo.filters.push({
        name: 'imageFilter',
        fn: function(item, options) {
            return this.queue.length < 1;
        }
    });
    //过滤格式
    $scope.uploaderLogo.filters.push({
        name: 'imageFilter',
        fn: function(item /*{File|FileLikeObject}*/, options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });
    $scope.uploaderLogo.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            $scope.baseInfo.productImage = response.url;
        }
    };

    //获取保险公司
    $scope.getInsuranceCompany = function(){
        $http({
            url:"insuranceCompany/getCompanyList",
            method:"POST"
        }).success(function(msg){
            if(msg.status){
                $scope.companyListAll = msg.data;
            }
        }).error(function(){
            $scope.notice("获取组织列表异常");
        });
    };
    $scope.getInsuranceCompany();

    //提交
    $scope.submit = function(){
        var price=$scope.baseInfo.productPrice
        if(!validate(price)){
                $scope.notice("价格输入不合法")
                return
        }
        var showOrder=$scope.baseInfo.showOrder
        if(!validate(showOrder)){
            $scope.notice("顺序输入不合法")
            return
        }
        $scope.submitting = true;
        $http({
            url: "insuranceProduct/addProduct",
            method: "POST",
            data: $scope.baseInfo
        }).success(function(result){
            $scope.submitting = false;
            $scope.notice(result.msg);
            if(result.status){
                $state.transitionTo('superBank.insuranceProduct',null,{reload:true});
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
