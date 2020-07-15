/**
 * 增加进件
 */
angular.module('inspinia', ['angularFileUpload']).controller("addRequireCtrl", function($scope, $http, $state, $stateParams,FileUploader) {
	//定义数据源
	$scope.photo=[{text:"只允许拍照",value:'1'},{text:"拍照和相册",value:'2'}];
//	$scope.exampleType=[{text:"图片",value:1},{text:"文件",value:2},{text:"文字",value:3}];
	$scope.bool=[{text:"是",value:'1'},{text:'否',value:'2'}];
	$scope.baseInfo={itemName:null,photo:'1',exampleType:1,example:null,remark:null,photoAddress:null,
			checkStatus:'1',checkMsg:null};
	$scope.imgFlag = true;
	$scope.fileFlag = true;
	
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


	//定义获取剩余字数的方法
	$scope.getCount = function () {
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


	// 过滤格式
	uploaderImg.filters
			.push({
				name : 'imageFilter',
				fn : function(item /* {File|FileLikeObject} */,options) {
					var type = '|'+ item.type.slice(item.type.lastIndexOf('/') + 1)+ '|';
					return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
				}
	});
	
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
				fn : function(item  {File|FileLikeObject} ,
						options) {
					var type = '|'
							+ item.type.slice(item.type
									.lastIndexOf('/') + 1)
							+ '|';
					return '|jpg|png|jpeg|bmp|gif|'
							.indexOf(type) !== -1;
				}
			});*/
	$scope.subDisable = false;
	$scope.addRequire = function(){
		$scope.subDisable = true;
		if($scope.baseInfo.exampleType==1){
			if(uploaderImg.queue.length>0){
				uploaderImg.uploadAll();//上传图片
				uploaderImg.onSuccessItem = function(fileItem, response, status, headers) {//上传成功后的回调函数，在里面执行提交
					if(response.url != null){ //回调参数url
						$scope.baseInfo.photoAddress = response.url;
						delete $scope.baseInfo.example;
						$scope.submitData();
					}
				};
			}else{
				$scope.submitData();
			}
		}
		else if($scope.baseInfo.exampleType==2){
			uploaderFile.uploadAll();//上传文件
			uploaderFile.onSuccessItem = function(fileItem, response, status, headers) {//上传成功后的回调函数，在里面执行提交
				if(response.url != null){ //回调参数url
			    	 $scope.baseInfo.photoAddress = response.url;
			    	 delete $scope.baseInfo.example;
			    	 $scope.submitData();
				}
			}
		}
		else if($scope.baseInfo.exampleType==3){
			delete $scope.baseInfo.photoAddress;
			delete $scope.baseInfo.photo;
			$scope.submitData();
		}
	}
	$scope.submitData = function(){
		var data={"baseInfo":$scope.baseInfo};
		$http.post('require/addRequire',
				angular.toJson(data)
		).success(function(msg){
			$scope.subDisable = false;
			if(msg.status){
				$scope.notice('新增进件要求项成功');
				$state.transitionTo('service.addRequireItem',null,{reload:true});
			} else {
				$scope.notice('新增进件要求项失败');
			}
		}).error(function(){
			$scope.subDisable = false;
		});
	}
});