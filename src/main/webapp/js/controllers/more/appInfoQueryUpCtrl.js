/**
 * 二维码修改
 */
angular.module('inspinia',['angularFileUpload']).controller('appInfoQueryUpCtrl',function($scope,$http,$state,$stateParams,$compile,$filter,i18nService,FileUploader){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	$scope.appNames=[];
	$scope.parenNames=[];
	$scope.isNull=0;
	$scope.info={};
	$http.post('appInfoAction/selectByParam?ids='+$stateParams.id)
	 .success(function(msg) {
		 if(msg.bols){
			 $scope.info=msg.data;
			 $scope.info.parentId=parseInt(msg.data.parentId);
		 }else{
	     	$scope.notice("查询失败");
		 }
	 }).error(function(){
		 $scope.notice("操作失败");
	 });
	
	$scope.selectBox=function(id){
		$http.post('appInfoAction/selectInfoBox?id='+id)
		 .success(function(msg) {
			 if(msg.bols){
				 if(msg.d==0){
					 $scope.appNames=msg.data; 
					 if($scope.info.parentId==0){
						 for(var i =0;i<$scope.appNames.length;i++){
							 if($scope.info.appName==$scope.appNames[i].appName){
								 $scope.info.parentId=$scope.appNames[i].id;
							 }
						 }
					 }
					 $scope.selectBox($scope.info.parentId);
				 }else{
					 if(!msg.isnull){
						 $scope.isNull=1;
						 $scope.parenNames=msg.data;
					 }else{
						 $scope.isNull=0;
					 }
				 }
			 }else{
		     	$scope.notice("查询失败");
			 }
		 }).error(function(){
			 $scope.notice("操作失败");
		 });
	}
	$scope.selectBox("");
	//上传图片,定义控制器路径
    var uploader = $scope.uploader = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数 
        removeAfterUpload: true,  //上传后删除文件
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    uploader.filters.push({
        name: 'imageFilter',
        fn: function(item, options) {
            return this.queue.length < 1;
        }
    });
    //过滤格式
     uploader.filters.push({
         name: 'imageFilter',
         fn: function(item /*{File|FileLikeObject}*/, options) {
             var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
             return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
         }
     });
     $scope.clearItems = function(){  //重新选择文件时，清空队列，达到覆盖文件的效果
    		    uploader.clearQueue();
     }
     $scope.fileSelect = null;
     $scope.uploader = uploader;
     $scope.commit=function(){
    	 $scope.submitting = true;
    	if($scope.uploader.queue.length==0){
    		 var data = {"img":$scope.info.logUrl,"info":$scope.info};
				 $http.post('appInfoAction/upAppInfo',angular.toJson(data))
				 .success(function(msg) {
					 if(msg.result){
	    				 $scope.notice("操作成功"); 
	    				 $state.transitionTo('sys.appInfo',null,{reload:true});
					 }else{
						 $scope.notice("操作失败");
						 $scope.submitting = false;
					 }
				 }).error(function(){
					 $scope.submitting = false;
				 });
    	}
    	uploader.uploadAll();//上传
 		uploader.onSuccessItem = function(fileItem, response, status, headers) {//上传成功后的回调函数，在里面执行提交
 		     if(response.url != null){
 		    	 var data = {"img":response.url,"info":$scope.info};
 				 $http.post('appInfoAction/upAppInfo',angular.toJson(data))
 				 .success(function(msg) {
 					 if(msg.result){
	    				$scope.notice("操作成功"); 
	    				 $state.transitionTo('sys.appInfo',null,{reload:true});
 					 }else{
	    		     	$scope.notice("操作失败");
	    		     	$scope.submitting = false;
 					 }
 				 }).error(function(){
 					 $scope.submitting = false;
 				 });
 		     }
 		};	 
    	 
     }
	
})