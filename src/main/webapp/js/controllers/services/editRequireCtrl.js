/**
 * 修改进件要求
 */
angular.module('inspinia',['angularFileUpload']).controller("editRequireCtrl", function($scope, $http, $state, $stateParams,FileUploader) {
	//定义数据源
	$scope.photo=[{text:"只允许拍照",value:'1'},{text:"拍照和相册",value:'2'}];
//	$scope.exampleType=[{text:"图片",value:1},{text:"文件",value:2},{text:"文字",value:3}];
	$scope.bool=[{text:"是",value:'1'},{text:'否',value:'2'}];
	var exampleType = null;	//进件项类型
	$http.get('require/requireDetailCtrl/'+$stateParams.id).success(function(largeLoad){
		$scope.baseInfo=largeLoad;
		exampleType = largeLoad.exampleType;
		if($scope.baseInfo.exampleType==1){
			$scope.imgFlag = false;
			$scope.fileFlag = true;
		}
		if($scope.baseInfo.exampleType==2){
			$scope.imgFlag = true;
			$scope.fileFlag = false;
		}
		if($scope.baseInfo.exampleType==3){
			$scope.imgFlag = true;
			$scope.fileFlag = true;
		}
	});

	//定义获取剩余字数的方法
	$scope.getCount = function () {
		if($scope.baseInfo==null){
			return;
		}
		if($scope.baseInfo.remark==null){
			$scope.baseInfo.remark = "";
		}
		//判断输入数据的长度
		if($scope.baseInfo.remark.length > 60){
			$scope.baseInfo.remark = $scope.baseInfo.remark.slice(0, 60);
		}
		//返回剩余字数的个数
		return $scope.baseInfo.remark.length;
	};

	var uploaderImgFlag = true;	//是否可以提交图片数据
	var uploaderFileFlag = true;//是否可以提交文件数据
	// 上传二维码,定义控制器路径
	var uploaderImg = $scope.uploaderImg = new FileUploader({
		url : 'upload/fileUpload.do',
		removeAfterUpload: true,  //上传后删除文件
	    headers : {'X-CSRF-TOKEN' : $scope.csrfData}
	});
	// 过滤长度，只能上传一个
	uploaderImg.filters.push({
		name : 'imageFilter',
		fn : function(item, options) {
			return this.queue.length < 1;
		}
	});
	// 过滤格式
	uploaderImg.filters.push({
				name : 'imageFilter',
				fn : function(item /* {File|FileLikeObject} */,options) {
					var type = '|'+ item.type.slice(item.type.lastIndexOf('/') + 1)+ '|';
					return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
				}
	});
	//remove后，显示进入页面
    uploaderImg.removeFromQueue = function(value){
    	if(exampleType==1){
    		$scope.imgFlag = false;
    	}
    	uploaderImgFlag = true;
		var index = this.getIndexOfItem(value);
        var item = this.queue[index];
        if (item.isUploading) item.cancel();
        this.queue.splice(index, 1);
        item._destroy();
        this.progress = this._getTotalProgress();
	}
   
	// 上传宣传图片,定义控制器路径
	var uploaderFile = $scope.uploaderFile = new FileUploader({
		url : 'upload/fileUpload.do',
		removeAfterUpload: true,  //上传后删除文件
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
	});
	// 过滤长度，只能上传一个
	uploaderFile.filters.push({
		name : 'imageFilter',
		fn : function(item, options) {
			return this.queue.length < 1;
		}
	});
	// 过滤格式，文件格式的过滤
	/*uploaderFile.filters.push({
				name : 'imageFilter',
				fn : function(item  {File|FileLikeObject} ,options) {
					var type = '|'+ item.type.slice(item.type.lastIndexOf('/') + 1)+ '|';
					return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
				}
			});*/
	uploaderFile.removeFromQueue = function(value){
		if(exampleType==2){
    		$scope.fileFlag = false;
    	}
    	uploaderFileFlag = true;
		var index = this.getIndexOfItem(value);
        var item = this.queue[index];
        if (item.isUploading) item.cancel();
        this.queue.splice(index, 1);
        item._destroy();
        this.progress = this._getTotalProgress();
	}
	
	uploaderImg.onAfterAddingFile = function(fileItem) {
		$scope.imgFlag = true;//隐藏图片
		uploaderImgFlag = false;
	}
	uploaderFile.onAfterAddingFile = function(fileItem) {
		$scope.fileFlag = true;//隐藏图片
		uploaderFileFlag = false;
	}
	$scope.addRequire = function(){
		if($scope.baseInfo.exampleType==1){
			if(!uploaderImgFlag){
				uploaderImg.uploadAll();//上传图片
				uploaderImg.onSuccessItem = function(fileItem, response, status, headers) {//上传成功后的回调函数，在里面执行提交
				     if(response.url != null){ //回调参数url
				    	 $scope.baseInfo.photoAddress = response.url;
				    	 $scope.submitData();
				     }
				}
			} else {
				$scope.submitData();
			}
		}
		if($scope.baseInfo.exampleType==2){
			if(!uploaderFileFlag){
				uploaderFile.uploadAll();//上传文件
				uploaderFile.onSuccessItem = function(fileItem, response, status, headers) {//上传成功后的回调函数，在里面执行提交
					if(response.url != null){ //回调参数url
				    	 $scope.baseInfo.photoAddress = response.url;
				    	 $scope.submitData();
					}
				};
			}else{
				$scope.submitData();
			}
			
		}
		if($scope.baseInfo.exampleType==3){
			$scope.baseInfo.photoAddress = null;
			$scope.submitData();
		}
	}
	$scope.submitData = function(){
		$scope.submitting = true;
		var data={"baseInfo":$scope.baseInfo};
		$http.post('require/addRequire',angular.toJson(data)).success(function(msg){
			if(msg.status){
				$scope.notice('修改进件要求项成功');
				$state.transitionTo('service.queryRequireItem',null,{reload:true});
				$scope.submitting = false;
			} else {
				$scope.notice('修改进件要求项成功');
				$scope.submitting = false;
			}
		}).error(function(){
			$scope.submitting = false;
		});
	}
});
