/**
 * 二维码历史记录修改和新增
 */
angular.module('inspinia',['angularFileUpload']).controller('appMobileVerInfoAddOrUpCtrl',function($scope,$http,$state,$stateParams,$compile,$filter,i18nService,FileUploader){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	$scope.info={platform:0,downFlag:0};
	$scope.platforms=[{text:'android',value:0},{text:'IOS',value:1}];
	$scope.downFlags=[{text:'不需要',value:0},{text:'需要更新',value:1},{text:'需要强制下载',value:2}];
	$scope.appNo=$stateParams.appNo;
	$scope.appName=$stateParams.appName;
	$scope.name="二维码历史记录新增";
	if($stateParams.id!=0){
		$scope.name="二维码历史记录修改";
		$http.post('appInfoAction/findChildDetailInfo?ids='+$stateParams.id)
		 .success(function(msg) {
			 if(msg.bols){
				 $scope.info=msg.data;
			 }else{
		     	$scope.notice("查询失败");
			 }
		 }).error(function(){
			 $scope.notice("操作失败");
		 });
		
	}
	
	//上传二维码图片,定义控制器路径
	var uploaderImgFlag = true;
    var uploaderImg = $scope.uploaderImg = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数 
        removeAfterUpload: true,  //上传后删除文件
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
     uploaderImg.onAfterAddingFile = function(fileItem) {
    	 uploaderImgFlag = false;
 	}
 	uploaderImg.removeFromQueue = function(value){
 		uploaderImgFlag = true;
 		var index = this.getIndexOfItem(value);
         var item = this.queue[index];
         if (item.isUploading) item.cancel();
         this.queue.splice(index, 1);
         item._destroy();
         this.progress = this._getTotalProgress();
 	}
 	
 	
 	
 // 上传LOGO,定义控制器路径
 	var uploaderFileFlag = true;
 	  var uploaderFile = $scope.uploaderFile = new FileUploader({
 	        url: 'upload/fileUpload.do',
 	        queueLimit: 1,   //文件个数 
 	        removeAfterUpload: true,  //上传后删除文件
	        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
 	    });
 	    //过滤长度，只能上传一个
 	    uploaderFile.filters.push({
 	        name: 'imageFilter',
 	        fn: function(item, options) {
 	            return this.queue.length < 1;
 	        }
 	    });
 	    //过滤格式
 	     uploaderFile.filters.push({
 	         name: 'imageFilter',
 	         fn: function(item /*{File|FileLikeObject}*/, options) {
 	             var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
 	             return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
 	         }
 	     });
 	     uploaderFile.onAfterAddingFile = function(fileItem) {
 	    	 uploaderFileFlag = false;
 	 	}
 	 	uploaderFile.removeFromQueue = function(value){
 	 		uploaderFileFlag = true;
 	 		var index = this.getIndexOfItem(value);
 	         var item = this.queue[index];
 	         if (item.isUploading) item.cancel();
 	         this.queue.splice(index, 1);
 	         item._destroy();
 	         this.progress = this._getTotalProgress();
 	 	}
 	 	 $scope.commit = function(){
 	 		 $scope.submitting = true;
 	 		//1.没有等待上传的
 	 		if(uploaderImgFlag && uploaderFileFlag){
 	 			$scope.submitData();
 	 		}
 	 		//2.有消息图片
 	 		if(!uploaderImgFlag && uploaderFileFlag){
 	 			uploaderImg.uploadAll();// 上传消息图片
 	 			uploaderImg.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
 					if (response.url != null) { // 回调参数url
 						$scope.info.url= response.url;
 						$scope.submitData();
 					}
 		        }
 	 		}
 	 		//3.消息图片没有，附件有
 			if(uploaderImgFlag && !uploaderFileFlag){
 				//有上传消息图片等待上传
 				uploaderFile.uploadAll();// 上传附件
 				uploaderFile.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
 					if (response.url != null) { // 回调参数url
 						$scope.info.appLogo = response.url;
 						$scope.submitData();
 					}
 		        }
 			}
 			//4.消息图片有，附件有
 			if(!uploaderImgFlag && !uploaderFileFlag){
 				uploaderImg.uploadAll();// 上传消息图片
 				uploaderImg.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
 					if (response.url != null) { // 回调参数url
 						$scope.info.url= response.url;
 						uploaderFile.uploadAll();// 上传附件
 						uploaderFile.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
 							if (response.url != null) { // 回调参数url
 								$scope.info.appLogo = response.url;
 								$scope.submitData();
 							}
 						}
 					}
 		        }
 			}
 	 		 
 	 	 }
 	 	 
 	 	$scope.submitData = function(){
 	 		$scope.info.appType=$scope.appNo;
 	 		if(uploaderImgFlag){
 	 			$scope.info.url=$scope.info.logUrl;
 	 		}
 	 		if(uploaderFileFlag){
 	 			$scope.info.appLogo=$scope.info.logAppLogo;
 	 		}
 	 		var data={"info":$scope.info,"isn":$stateParams.id}
 	    	$http.post("appInfoAction/addOrUpAppChildInfo",angular.toJson(data))
 			.success(function(msg){
 				if(msg.result){
 					$scope.notice("操作成功");
 					$state.transitionTo('sys.appMobileVerInfo',{id:$scope.appNo},{reload:true});
 					$scope.submitting = false;
 				} else {
 					$scope.notice("操作失败");
 					$scope.submitting = false;
 				}
 			}).error(function(){
 				$scope.submitting = false;
 			});
 	 		
 	 	}
	
})