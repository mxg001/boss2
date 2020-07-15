/**
 * APP二维码新增
 */
angular.module('inspinia',['angularFileUpload']).controller('appInfoQueryAddCtrl',function($scope,$http,$state,$stateParams,$compile,$filter,i18nService,FileUploader){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	$scope.appNames=[];
	$scope.parenNames=[];
	$scope.isNull=0;
	$scope.info={};
	$scope.teams=[];
	$http.get('teamInfo/queryTeamName.do').success(function(msg){
		for(var i=0;i<msg.teamInfo.length;i++){
			$scope.teams.push({text:msg.teamInfo[i].teamName,value:msg.teamInfo[i].teamId});
		}
		$scope.info.teamId=msg.teamInfo[0].teamId;
	});
	$scope.selectBox=function(id){
		$http.post('appInfoAction/selectInfoBox?id='+id)
		 .success(function(msg) {
			 if(msg.bols){
				 if(msg.d==0){
					 $scope.appNames=msg.data; 
					 $scope.info.parentId=$scope.appNames[0].id;
					 $scope.selectBox($scope.info.parentId);
				 }else{
					 $scope.parenNames=msg.data;
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
	
     $scope.commit=function(){
         $scope.submitting = true;
         if(uploader.queue.length==0){
             var data = {"info":$scope.info};
             $http.post('appInfoAction/addAppInfo',angular.toJson(data))
                 .success(function(msg) {
                     $scope.submitting = false;
                     $scope.notice(msg.msg);
                     if(msg.result){
                         $state.transitionTo('sys.appInfo',null,{reload:true});
                     }
                 });
             }else{
				 uploader.uploadAll();//上传
				 uploader.onSuccessItem = function(fileItem, response, status, headers) {//上传成功后的回调函数，在里面执行提交
					 if(response.url != null){
						$scope.info.codeUrl = response.url;
						 var data = {info:$scope.info};
						 $http.post('appInfoAction/addAppInfo',angular.toJson(data))
							 .success(function(msg) {
								 $scope.submitting = false;
								 $scope.notice(msg.msg);
								 if(msg.result){
									 $state.transitionTo('sys.appInfo',null,{reload:true});
								 }
							 }).error(function(){
							 $scope.submitting = false;
						 });
					 }
             	};
		 }
     }
	
})