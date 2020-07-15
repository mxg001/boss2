/**
 * 超级银行家组织新增
 */
angular.module('inspinia',['angularFileUpload','uiSwitch']).controller('addRedConfigCtrl',function($scope, $http, $state, FileUploader){
    $scope.baseInfo = {"allow_org_profit":"1","bus_type":"1","push_type":"1",
                        "receive_type":"0","push_org_id":0,"push_area":"3"};
    $scope.is_alow =false;
    var busType = [];
    angular.forEach($scope.redBusType, function(item){
        if(item.value!="0"){
            busType[busType.length] = item;
        }
    });
    $scope.redBusType = busType;
    $scope.changeBusType = function () {
        if($scope.baseInfo.bus_type == '2'||$scope.baseInfo.bus_type == '3'){
            $scope.is_alow = true;
        }else {
            $scope.is_alow = false
        }
    }

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


    //上传图片,定义控制器路径
    $scope.uploaderImg = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 9,   //文件个数
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
    // $scope.uploaderImg.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
    //     if (response.url != null) { // 回调参数url
    //         $scope.baseInfo.img_url = response.url;
    //     }
    // };

    //提交按钮
    $scope.submit = function(){
        $scope.submitting = true;
        if($scope.uploaderImg.queue.length > 0){
            $scope.baseInfo.img_url = "";
            $scope.uploaderImg.uploadAll();
            $scope.uploaderImg.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
                if (response.url != null) { // 回调参数url
                    $scope.baseInfo.img_url = $scope.baseInfo.img_url + response.url + ";";
                }
            }
            $scope.uploaderImg.onCompleteAll = function(){
                $scope.baseInfo.img_url = $scope.baseInfo.img_url.substring(0, $scope.baseInfo.img_url.length-1);
                submitData();
            }
        } else {
            submitData();
        }
    }
    //提交数据
    function submitData() {
        if($scope.baseInfo.bus_type != "2" && $scope.baseInfo.bus_type != "3"){
            $scope.baseInfo.allow_org_profit = null;
        } else {
            if ($scope.baseInfo.allow_org_profit) {
                $scope.baseInfo.allow_org_profit = "1";
            } else {
                $scope.baseInfo.allow_org_profit = "0";
            }
        }
        
        /*if($scope.baseInfo.bus_type == "15"){
        	$scope.submitting = false;
        	var isNum=/^(([0-9][0-9]*)|(([0]\.\d{1,5}|[1-9][0-9]*\.\d{1,5})))$/;
        	var isNum2=/^(([1-9][0-9]*))$/;
       		   if($scope.baseInfo.manormoney_traderpremium==null || $scope.baseInfo.manormoney_traderpremium==""){
                      $scope.notice("领地红包总金额占领地交易溢价比例不能为空!");
                      return;
                  }else{
                      if(!isNum.test($scope.baseInfo.manormoney_traderpremium)){
                          $scope.notice("领地红包总金额占领地交易溢价比例占比格式不正确!");
                          return;
                      }
                  }
       		   if($scope.baseInfo.oneladder_people_num==null || $scope.baseInfo.oneladder_people_num==""){
       			   $scope.notice("红包领取人数不能为空!");
       			   return;
       		   }else{
       			   if(!isNum2.test($scope.baseInfo.oneladder_people_num)){
       				   $scope.notice("红包领取人数不正确!");
       				   return;
       			   }
       		   }
        }*/
        
        $http({url: "red/addRedConfig", data: $scope.baseInfo, method: "post"}).success(function (data) {
            if (data.status) {
                $scope.notice(data.msg);
                $state.transitionTo('red.redConfigs', null, {reload: true});
            } else {
                $scope.notice(data.msg);
                $scope.submitting = false;
            }
        }).error(function () {
            $scope.notice("服务器异常，请稍候再试");
            $scope.submitting = false;
        });
    }
});