/**
 * 修改banner
 */
angular.module('inspinia',['angularFileUpload']).controller("modifyBannerCtrl",function($scope, $http, $state, $stateParams,FileUploader) {
	//数据源
	$scope.team = [];
	$scope.baseInfo = {};//使用之前需要初始化
	$scope.imgFlagHide = true;	//“附件”，修改banner时，默认隐藏，当附件地址不为空时，显示
	$scope.removeImgFlag = false;	//是否删除图片，默认为false
	var uploadFlag = true;		//是否可以提交数据，默认是可以提交的，当有文件准备上传时，为false，上传完成后置为true
//	$http.get('banner/getAppInfo.do').success(function(msg) {
//    	if(msg!=null){
//    		$scope.appList = msg;
//    	}
//    	$scope.team.push({value:'0',text:'全部'});
//		for(var i=0; i<msg.allTeam.length; i++){
//			$scope.team.push({value:msg.allTeam[i].teamId,text:msg.allTeam[i].teamName});
//		}
//    	$scope.agent.push({value:'0',text:'全部'});
//		for(var i=0; i<msg.allAgent.length; i++){
//			$scope.agent.push({value:msg.allAgent[i].agentNo,text:msg.allAgent[i].agentName});
//		}
//    });
	//进入页面需要携带的banner相关数据
	$http.get('banner/modifyById/'+$stateParams.id).success(function(msg){
		if(msg.status){
	    	$scope.appList = msg.appList;
			$scope.baseInfo = msg.banner;
			if($scope.baseInfo.bannerAttachmentUrl != null){
				$scope.imgFlagHide = false;	
			}
			angular.forEach($scope.appList,function(data){
				if($scope.baseInfo.appNo == data.appNo){
					$scope.baseInfo.app = data;
				}
			})
		} else {
			$scope.notice('进入修改页面失败');
		}
	});
	
	//上传图片,定义控制器路径
    var uploader = $scope.uploader = new FileUploader({
        url: 'upload/fileUpload.do',
        removeAfterUpload: true,  //上传后删除文件
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    uploader.filters.push({
        name: 'imageFilter',
        fn: function(item, options) {
			if(item.size>500*1024){
				return false;
			}
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
    uploader.onAfterAddingFile = function(fileItem) {
		uploadFlag = false;
		$scope.imgFlagHide = true;
	}
	uploader.removeFromQueue = function(value){
		uploadFlag = true;
		if($scope.baseInfo.bannerAttachment != null){
			$scope.imgFlagHide = false;	
		}
		var index = this.getIndexOfItem(value);
        var item = this.queue[index];
        if (item.isUploading) item.cancel();
        this.queue.splice(index, 1);
        item._destroy();
        this.progress = this._getTotalProgress();
	}

	$scope.removeAnnex = function(){
		$scope.removeImgFlag = true;
		$scope.imgFlagHide = true;
	}
    
	 $scope.submit = function(){
		 $scope.submitting = true;
		 if($scope.baseInfo.onlineTime!=null && $scope.baseInfo.onlineTime!=""
			 && $scope.baseInfo.offlineTime!=null && $scope.baseInfo.offlineTime!=""
			 && $scope.baseInfo.onlineTime>$scope.baseInfo.offlineTime){
	 			$scope.notice("上线时间不能早于下线时间");
	 			return;
	 		}
		 if($scope.removeImgFlag){
			 delete $scope.baseInfo.bannerAttachment;
		 }
		 if(uploadFlag){
			$scope.submitData(); 
		 } else {
			//有上传附件等待上传
			uploader.uploadAll();// 上传宣传图片
			uploader.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
				if (response.url != null) { // 回调参数url
					$scope.baseInfo.bannerAttachment = response.url;
					$scope.submitData();
				} else {
					$scope.notice('上传失败');
				}
	        }
		 }
     };
     
     $scope.submitData = function(){
    	 $scope.baseInfo.appNo = $scope.baseInfo.app.appNo;
 		$scope.baseInfo.teamId = $scope.baseInfo.app.teamId;
    	 var data = {"banner":$scope.baseInfo};
		 
    	 $http.post('banner/saveBanner.do',angular.toJson(data)).success(function(msg) {
    		 if(msg.status){
    			 $scope.notice(msg.msg);
    			 $state.transitionTo('sys.queryBanner',null,{reload:true});
				 $scope.submitting = false;
    		 }else{
    			 $scope.notice(msg.msg);
				 $scope.submitting = false;
    		 }
         }).error(function(){
			 $scope.submitting = false;
         });
     };
});
