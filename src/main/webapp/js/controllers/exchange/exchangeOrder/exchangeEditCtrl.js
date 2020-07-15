/**
 * 积分兑换订单修改
 */
angular.module('inspinia',['infinity.angular-chosen','angularFileUpload']).controller('exchangeEditCtrl',function($scope,$http,i18nService,$stateParams,$state,$timeout,FileUploader){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文

    $scope.addinfo={orgCode:"",typeCode:"",productId:""};
    $scope.selectState={type:false,pro:false};
    $scope.oemList=[{value:"",text:"全部"}];
    //组织列表
    $http.post("exchangeOem/getOemList",
        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function(data){
            if(data.status){
                var list=data.list;
                if(list!=null&&list.length>0){
                    for(var i=0; i<list.length; i++){
                        $scope.oemList.push({value:list[i].oemNo,text:list[i].oemName});
                    }
                }
            }else{
                $scope.notice(data.msg);
            }
        });
    //获取机构
    $scope.orgList=[{value:"",text:"全部"}];
    $http.post("orgManagement/getOrgManagementList",
        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function(data){
            if(data.status){
                var list=data.list;
                if(list!=null&&list.length>0){
                    for(var i=0; i<list.length; i++){
                        $scope.orgList.push({value:list[i].orgCode,text:list[i].orgName});
                    }
                }
            }else{
                $scope.notice(data.msg);
            }
        });

    $scope.changeOrgCode=function () {
        if($scope.addinfo.orgCode!=null&&$scope.addinfo.orgCode!=""){
            $scope.selectState={type:true,pro:false};
            $scope.getProductTypeList($scope.addinfo.orgCode);
        }else{
            $scope.selectState={type:false,pro:false};
        }
        $scope.addinfo.typeCode="";
        $scope.addinfo.productId="";
    };

    $scope.typeList=[{value:"",text:"全部"}];
    //获取产品类别
    $scope.getProductTypeList=function (orgCode) {
        $http.post("productType/getProductTypeList","orgCode="+orgCode,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.typeList=[];
                    $scope.typeList.push({value:"",text:"全部"});
                    var list=data.list;
                    if(list!=null&&list.length>0){
                        for(var i=0; i<list.length; i++){
                            $scope.typeList.push({value:list[i].typeCode, text:list[i].typeName});
                        }
                    }
                }
            });
    };
    $scope.changeTypeCode=function () {
        if($scope.addinfo.typeCode!=null&&$scope.addinfo.typeCode!=""){
            $scope.selectState={type:true,pro:true};
            $scope.getProductList("");
            $scope.getAddProductType($scope.addinfo.typeCode);
        }else{
            $scope.selectState={type:true,pro:false};
        }
        $scope.addinfo.productId="";
    };
    //获取产品
    $scope.productList=[{value:"",text:"全部"}];
    $scope.getProductList=function (name) {
        if($scope.addinfo.typeCode!=null&&$scope.addinfo.typeCode!=""){
            $http.post("exchangeProduct/getProductList","name="+name+"&typeCode="+$scope.addinfo.typeCode,
                {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                .success(function(data){
                    if(data.status){
                        $scope.productList=[{value:"",text:"全部"}];
                        var list=data.list;
                        if(list!=null&&list.length>0){
                            for(var i=0; i<list.length; i++){
                                $scope.productList.push({value:list[i].id,text:list[i].productName});
                            }
                        }
                    }
                });
        }else{
            $scope.productList=[{value:"",text:"全部"}];
        }
    }
    //动态筛选产品
    $scope.getStates =getStates;
    var oldValue="";
    var timeout="";
    function getStates(value) {
        var newValue=value;
        if(newValue != oldValue){
            if (timeout) $timeout.cancel(timeout);
            timeout = $timeout(
                function(){
                    $scope.getProductList(value);
                },800);
        }
    };
    $scope.getAddProductType=function (typeCode) {
        $http.post("productType/getAddProductType","typeCode="+typeCode,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.mapConfig=data.mapConfig;
                }else{
                    $scope.notice(data.msg);
                }
            });
    };
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
        fn: function(item,options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });
    uploaderImg.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            $scope.addinfo.uploadImage= response.url;
        }
    };

    //获取用户
    $scope.userList=[{value:"",text:"全部"}];
    $scope.getUserManagementList=function (merchantNo) {
        $http.post("userManagement/getUserManagementList","merchantNo="+merchantNo,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.userList=[{value:"",text:"全部"}];
                    var list=data.list;
                    if(list!=null&&list.length>0){
                        for(var i=0; i<list.length; i++){
                            if(list[i].userName!=null){
                                $scope.userList.push({value:list[i].merchantNo,text:list[i].merchantNo+"("+list[i].userName+")"});
                            }else{
                                $scope.userList.push({value:list[i].merchantNo,text:list[i].merchantNo});
                            }
                        }
                    }
                }
            });
    }
    $scope.getUserManagementList("-1");

    //动态筛选产品
    $scope.getUserList =getUserList;
    var oldValueUser="";
    var timeoutUser="";
    function getUserList(value) {
        var newValue=value;
        if(newValue != oldValueUser){
            if (timeoutUser) $timeout.cancel(timeoutUser);
            timeoutUser = $timeout(
                function(){
                    $scope.getUserManagementList(value);
                },800);
        }
    };

    $http.post("exchangeOrder/getExchangeOrerEdit","id="+$stateParams.id,
        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function(data){
            $scope.submitting = false;
            if(data.status){
                $scope.addinfo=data.order;
                $scope.getProductTypeList($scope.addinfo.orgCode);//商品类型
                $scope.getProductList($scope.addinfo.productName);//商品列表
                $scope.getAddProductType($scope.addinfo.typeCode);//是否是预付卡
                $scope.getUserManagementList($scope.addinfo.merNo);//用户列表
                $scope.image=$scope.addinfo.uploadImage;
                $scope.addinfo.validityDateStart=$scope.addinfo.validityDateStart==null?null:moment($scope.addinfo.validityDateStart).format('YYYY-MM-DD');
                $scope.addinfo.validityDateEnd=$scope.addinfo.validityDateEnd==null?null:moment($scope.addinfo.validityDateEnd).format('YYYY-MM-DD');
            }else{
                $scope.notice(data.msg);
            }
        })

    //提交
    $scope.submitting = false;
    $scope.addExchangeOrder=function(){
        if ($scope.submitting) {
            return;
        }
        if($scope.addinfo.typeCode!=null&&$scope.addinfo.typeCode!=""){
            if($scope.mapConfig.need_logistics_info=="1"){
                if($scope.addinfo.logisticsInfo==null||$scope.addinfo.logisticsInfo==""){
                    $scope.notice("物流信息不能为空!");
                    return;
                }
            }
            if($scope.mapConfig.need_exec_num=="1"){
                if($scope.addinfo.execNum==null||$scope.addinfo.execNum==""){
                    $scope.notice("兑换数量不能为空!");
                    return;
                }
            }
            if($scope.mapConfig.need_redeem_code=="1"){
                if($scope.addinfo.redeemCode==null||$scope.addinfo.redeemCode==""){
                    $scope.notice("兑换码不能为空!");
                    return;
                }
            }
            if($scope.mapConfig.need_express_date=="1"){
                if($scope.addinfo.validityDateStart==null||$scope.addinfo.validityDateStart==""
                    ||$scope.addinfo.validityDateEnd==null||$scope.addinfo.validityDateEnd==""){
                    $scope.notice("有效期不能为空!");
                    return;
                }
            }
            if($scope.mapConfig.need_upload_screen=="1"){
                if($scope.image!=null){
                    if(uploaderImg.queue.length<1){
                        $scope.addinfo.uploadImage=null;
                    }
                }else{
                    if(uploaderImg.queue.length<1) {
                        $scope.notice("上传截图不能为空!");
                        return;
                    }
                }
            }else{
                if(uploaderImg.queue.length<1){
                    $scope.addinfo.uploadImage=null;
                }
            }
        }
        $scope.submitting = true;
        $http.post("exchangeOrder/saveExchangeOrder","info="+angular.toJson($scope.addinfo),
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.notice(data.msg);
                    $state.transitionTo('exchange.exchangeAudit',null,{reload:true});
                }else{
                    $scope.notice(data.msg);
                }
                $scope.submitting = false;
            })
            .error(function(data){
                $scope.notice(data.msg);
                $scope.submitting = false;
            });
    };
});