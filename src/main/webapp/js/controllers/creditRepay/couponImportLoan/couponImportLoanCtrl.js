/**
 * 贷款送鼓励金/导入赠送
 */
angular.module('inspinia',['angularFileUpload']).controller('couponImportLoanCtrl',function($scope,$http,i18nService,SweetAlert,FileUploader){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文

    //获取所有赠送渠道
    $scope.sendChannelList=[];
    $http.post("sysDict/getListByKey.do?sysKey=SENDCHANNEL")
        .success(function(data){
            //响应成功
            for(var i=0; i<data.length; i++){
                $scope.sendChannelList.push({value:data[i].sysValue,text:data[i].sysName});
            }
            $scope.addCoupon.sendChannel="1";
        });

    $scope.addCoupon={sendChannel:"",sendTypeId:"",addNum:null,couponCode:13};

    //获取所有赠送类型
    $scope.sendTypeList=[];
    $http.get('cardAndReward/getSendTypeList?type='+$scope.addCoupon.couponCode)
        .success(function(data){
            //响应成功
            for(var i=0; i<data.list.length; i++){
                $scope.sendTypeList.push({value:data.list[i].id+"",text:"鼓励金金额"+data.list[i].couponAmount+",有效期"+data.list[i].effectiveDays+"天"});
            }
            $scope.addCoupon.sendTypeId=data.sendTypeId+"";
        });

    //上传图片,定义控制器路径
    var uploader = $scope.uploader = new FileUploader({
        url: 'cardAndReward/couponImportLoan',
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
        if($scope.addCoupon.sendChannel==null||$scope.addCoupon.sendChannel==""){
            $scope.notice("请选择赠送渠道！");
            return;
        }
        if($scope.addCoupon.sendTypeId==null||$scope.addCoupon.sendTypeId==""){
            $scope.notice("请选择赠送券类型！");
            return;
        }
        if($scope.submitting){
            return;
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