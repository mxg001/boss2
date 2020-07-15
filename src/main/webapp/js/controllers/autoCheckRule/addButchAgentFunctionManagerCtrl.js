
angular.module('inspinia', ['angularFileUpload']).controller("addButchAgentFunctionManagerCtrl", function($scope, $http, $state, $stateParams,FileUploader) {
	
	$scope.functionNumber = $stateParams.functionNumber;
	$scope.blacklist = $stateParams.blacklist;
	$scope.containsLowerJson=[{text:"包含",value:1},{text:"不包含",value:0}];
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
     $scope.updateActivityResultButchGrid = {
    	        data: 'errorlist',
    	        paginationPageSize:10,                  //分页数量
    	        paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
    	        useExternalPagination: true,		    //开启拓展名
    	        enableHorizontalScrollbar: true,        //横向滚动条
    	        enableVerticalScrollbar : true,  		//纵向滚动条
    	        columnDefs: [
    	            {field: 'agentNo',displayName: '代理商编号',width:150},
    	            {field: 'containsLower',displayName: '是否包含下级',width:150,cellFilter:"formatDropping:"+angular.toJson($scope.containsLowerJson)},
    	            {field: 'handle',displayName: '操作',width:100},
    	            {field: 'errMsg',displayName: '失败原因',width:200}
    	        ]
    		 };
     
	 $scope.submit=function(){
		 uploader.queue[0].formData[0].functionNumber = $scope.functionNumber;
		 uploader.queue[0].formData[0].blacklist = $scope.blacklist;
    	 uploader.uploadAll();//上传
    	 $scope.loadImg = true;
    	 uploader.onSuccessItem = function(fileItem, response, status, headers) {//上传成功后的回调函数，在里面执行提交
		     if(response.status){
		    	 //$scope.notice(response.msg);
		    	 //上传完成后跳转到哪个页面
		    	 //$state.transitionTo('func.switchSet',{"functionNumber":$scope.functionNumber,"blacklist":$scope.blacklist},{reload:true});
		    	 $("#updateActivityResultButchModel").modal("show");
		    	 $scope.successCount = response.data.successCount;
		    	 $scope.errCount = response.data.errCount;
		    	 $scope.errorlist =  response.data.list;
		    	 $scope.loadImg = false;
		     }else{
		    	$scope.notice(response.msg);
		    	$scope.loadImg = false;
		     }
		 };	
		 return false;
     }
	 
	 $scope.confirmButchModel=function(){
		 $("#updateActivityResultButchModel").modal('hide');
		 $scope.loadImg = false;
	 }
});
