/**
 * 添加代理商AppInfo的软件设置信息
 */
angular.module('inspinia',['angularFileUpload']).controller("modifyAppInfoCtrl",function($scope, $http, $state, $stateParams,FileUploader) {
	// 数据源
	$scope.team = [];
	$scope.agent = [];
	$scope.baseInfo = {};// 使用之前需要初始化
	$scope.imgFlagHide = true;	//“图片”，修改appInfo时，默认隐藏，当图片地址不为空时，显示
	var removeImgFlag = false;	//是否删除图片，默认为false
	var uploadFlag = true;		//是否可以提交数据，默认是可以提交的，当有文件准备上传时，为false，上传完成后置为true
	$http.get('appInfo/modifyAppInfo/' + $stateParams.id).success(function(msg) {
				$scope.team.push({
					value : '0',
					text : '全部'
				});
				for (var i = 0; i < msg.allTeam.length; i++) {
					$scope.team.push({
						value : msg.allTeam[i].teamId+"",
						text : msg.allTeam[i].teamName
					});
				}
				$scope.agent.push({
					value : '0',
					text : '全部'
				});
				for (var i = 0; i < msg.allAgent.length; i++) {
					$scope.agent.push({
						value : msg.allAgent[i].agentNo+"",
						text : msg.allAgent[i].agentName
					});
				}
				$scope.baseInfo = msg.appInfo;
				if($scope.baseInfo.photo != null){
					$scope.imgFlagHide = false;	
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
		removeImgFlag = false;
	}
	uploader.removeFromQueue = function(value){
		uploadFlag = true;
		if($scope.baseInfo.photo != null){
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
		removeImgFlag = true;
		$scope.imgFlagHide = true;
	}
    
	 $scope.submit = function(){
		 if(uploadFlag){
			$scope.submitData(); 
		 } else {
			//有上传附件等待上传
			uploader.uploadAll();// 上传宣传图片
			uploader.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
				if (response.url != null) { // 回调参数url
					$scope.baseInfo.photo = response.url;
					$scope.submitData();
				} else {
					$scope.notice('上传失败');
				}
	        }
		 }
     };
     
     $scope.submitData = function(){
    	 if(removeImgFlag){
			 delete $scope.baseInfo.photo;
		 }
    	 if($scope.baseInfo.teamId!='200010'){
 			 $scope.baseInfo.agentNo = '0';
 		 }
    	 var data = {"appInfo":$scope.baseInfo};
		 $scope.submitting = true;
    	 $http.post('appInfo/saveAppInfo.do',angular.toJson(data)).success(function(msg) {
    		 if(msg.status){
    			 $scope.notice(msg.msg);
    			 $state.transitionTo('sys.queryAppInfo',null,{reload:true});
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