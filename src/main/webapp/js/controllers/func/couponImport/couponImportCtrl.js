/**
 * 发行券查询/批量新增
 */
angular.module('inspinia',['angularFileUpload']).controller('routerOrgBatchDeleteCtrl',function($scope,$http,i18nService,SweetAlert,FileUploader){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文

    $scope.addCoupon={couponCode:0,activityEntityId:null,addNum:null};
    $scope.couponCodeSelect=[{text:'全部',value:0},{text:'注册返',value:1},{text:'签到返',value:2},
        {text:'充值返',value:3},{text:'购买鼓励金',value:6},{text:'购买鼓励金活动福利',value:15},{text:'抵用金活动福利',value:16},{text:'会员系统',value:17}];

    $scope.actEntityList=[];
    $scope.addNumSta=true;
    $scope.actEntityChange=function () {
        if($scope.addCoupon.couponCode==0||$scope.addCoupon.couponCode==1||$scope.addCoupon.couponCode==2||$scope.addCoupon.couponCode==15||$scope.addCoupon.couponCode==16 || $scope.addCoupon.couponCode==17){
            $scope.addNumSta=true;
            $scope.addCoupon.addNum=null;
        }else{
            $scope.addNumSta=false;
            $scope.addCoupon.addNum=1;
        }
        $http.post("couponActivity/actEntityList","couponCode="+$scope.addCoupon.couponCode,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function (data) {
                $scope.actEntityList=data;
                if(data.length > 0){
                    $scope.addCoupon.activityEntityId=data[0].id;
                }
            });
    };

    //上传图片,定义控制器路径
    var uploader = $scope.uploader = new FileUploader({
        url: 'couponActivity/addCouponImport',
        queueLimit: 1,   //文件个数
        removeAfterUpload: true,  //上传后删除文件
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    uploader.filters.push({
        name: 'isFile',
        fn: function(item, options) {
            return this.queue.length < 1;
        }
    });
    $scope.clearItems = function(){  //重新选择文件时，清空队列，达到覆盖文件的效果
        uploader.clearQueue();
    };

    $scope.importMsgShow = function(){
        $('#importMsg').modal('show');
    };
    $scope.importMsgHide = function(){
        $('#importMsg').modal('hide');
    };

    $scope.submitting = false;
    $scope.addImport=function(){
        if($scope.addCoupon.couponCode==null||$scope.addCoupon.couponCode==0){
            $scope.notice("请选择参与活动!");
            return;
        }
        if($scope.addCoupon.activityEntityId==null||$scope.addCoupon.activityEntityId==0){
            $scope.notice("请选择赠送券类型!");
            return;
        }
        if($scope.addCoupon.addNum==null||$scope.addCoupon.addNum==0){
            $scope.notice("请填写赠送数量!");
            return;
        }else{
            if(isNaN($scope.addCoupon.addNum)){
                $scope.notice("赠送数量只能是数字!");
                return;
            }
            if(Number($scope.addCoupon.addNum)<=0){
                $scope.notice("赠送数量不能小于1!");
                return;
            }
        }
        if($scope.submitting){
            return
        }
        $scope.submitting = true;

        //上传文件前，修改参数上传
        uploader.onBeforeUploadItem = function(item) {
            formData = [{"info":angular.toJson($scope.addCoupon)}];
            Array.prototype.push.apply(item.formData, formData);
        };
        uploader.uploadAll();//上传
        uploader.onSuccessItem = function(fileItem, response, status, headers) {//上传成功后的回调函数，在里面执行提交
            //处理返回数据
            $scope.mgsInfo=response.msg+"\n";
            var errorList=response.errorList;
            if(errorList!=null&&errorList.length>0){
                for(var i=0;i<errorList.length;i++){
                    $scope.mgsInfo=$scope.mgsInfo+errorList[i].msg+"\n";
                }
            }
            $scope.importMsgShow();
            $scope.submitting = false;
        };
    };

});