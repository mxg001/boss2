
angular.module('inspinia', ['angularFileUpload']).controller("addButchAgentInvitePrizesCtrl", function($scope, $http, $state, $stateParams,FileUploader) {
	
	$scope.functionNumber = $stateParams.functionNumber;
	
	var opts = {functionNumber:""};
	if(_parameterName)
		opts[_parameterName] = _token;
	
	var uploader = $scope.uploader = new FileUploader({
		url: 'agentFunctionManager/addButchAgentFunctionManager',
		formData:[opts],
        queueLimit: 1,   //文件个数 
        removeAfterUpload: true,  //上传后删除文件
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
	//过滤长度，只能上传一个
    uploader.filters.push({
        name: 'isFile',
        fn: function(item, options) {
            return this.queue.length < 1;
        }
    });
    

     $scope.clearItems = function(){  //重新选择文件时，清空队列，达到覆盖文件的效果
    	 uploader.clearQueue();
     }
     
	 $scope.submit=function(){
		 uploader.queue[0].formData[0].functionNumber = $scope.functionNumber;
    	 uploader.uploadAll();//上传
    	 uploader.onSuccessItem = function(fileItem, response, status, headers) {//上传成功后的回调函数，在里面执行提交
		     if(!response.result){
		    	 $scope.notice(response.msg);
		    	 //上传完成后跳转到哪个页面
		    	 $state.transitionTo('func.switchSet',{"functionNumber":$scope.functionNumber},{reload:true});
		     }else{
		    	$scope.notice(response.msg);
		     }
		 };	
		 return false;
     }
});
