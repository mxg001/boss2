/**
 * banner广告新增
 */
angular.module('inspinia',['angularFileUpload']).controller('addAdCtrl',function($scope, $http, $state,FileUploader){

	//返回上页
    $scope.goback = function () {
    	history.go(-1);
    };
    
    $scope.isDisabled = false;
    
    
    $scope.addAd = function () {
    	$http.post("superBank/addAd",angular.toJson($scope.ad))
  		.success(function(msg){
            $scope.notice(msg.msg);
            if(msg.status){
            	 $state.transitionTo('superBank.adManage',null,{reload:true});
            }else {
            	$scope.isDisabled = false;
            }
           
        }).error(function (msg) {
        	$scope.isDisabled = false;
            $scope.notice('服务器异常,请稍后再试.');
        });
    };

    //位置
    $scope.applyTypeList = [{value:"1",text:"App+公众号"},{value:"2",text:"App"},{value:"3",text:"公众号"}];
    $scope.postionTypeList = [{value:"1",text:"首页"},{value:"2",text:"办卡查询"},{value:"3",text:"贷款申请"},{value:"4",text:"信用卡列表位置"}];


    //组织列表
    $scope.orgs=[];
    $scope.getAllOrg = function(){
   	 $http({
            url:"superBank/getOrgInfoList",
            method:"POST"
        }).success(function(msg){
            if(msg.status){
                $scope.orgs = msg.data;
                $scope.orgs.unshift({orgId:'-1',orgName:"所有组织"});
                $scope.ad = {postionType:"1",orgId:"-1",applyType:"1"};
            }
        }).error(function(){
            $scope.notice("获取组织列表异常");
        });
   };
   $scope.getAllOrg();
  
   
   
   
 //上传图片,定义控制器路径
   $scope.uploaderImg = new FileUploader({
       url: 'upload/fileUpload.do',
       queueLimit: 1,   //文件个数
       removeAfterUpload: true,  //上传后删除文件
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

   //1.如果有图片先上传图片，将url赋值给orgLogo
   //2.提交所有数据，新增组织
   $scope.submit = function(){
       $scope.submitting = true;
       if($scope.uploaderImg.queue.length == 0){
    	   
    	   $scope.notice("请选择图片");
    	   return ;
           //$scope.addAd();
       } else {
    	   
    	   if($scope.ad.title==undefined || $scope.ad.title.trim().length==0){
    		   $scope.notice("请输入标题");
        	   return ;
    	   }
    	   
    	   if($scope.ad.showNo==undefined || $scope.ad.showNo.trim().length==0){
    		   $scope.notice("请输入权重");
        	   return ;
    	   }
    	   
    	   var showno = $scope.ad.showNo;
    	   var merch = /^\d+$/;
    	   if(!showno.match(merch)){
    		   $scope.notice("权重输入正整数或0");
    		   return ;
    	   }
    	   
    	   if($scope.ad.upDate==undefined || $scope.ad.upDate.trim().length==0 || $scope.ad.downDate==undefined || $scope.ad.downDate.trim().length==0){
    		   $scope.notice("请选择上下线时间");
        	   return ;
    	   }
    	   
    	   $scope.isDisabled = true;
    	   
           $scope.uploaderImg.uploadAll();// 上传消息图片
           $scope.uploaderImg.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
               
        	   if (response.url != null) { // 回调参数url
                   $scope.ad.imgUrl = response.url;
                   $scope.addAd();
               } else {
            	   $scope.isDisabled = false;
                   $scope.notice("上传图片失败");
                   $scope.submitting = false;
               }
           };
       }
   };
   
   
});