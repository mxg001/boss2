/**
 * 收单商户导入
 */
angular.module('inspinia',['angularFileUpload']).controller('acqMerchantimportCtrl',function($scope,$state,$http,$stateParams,FileUploader,$window){

	$scope.acqOrgs=[];
	//收单机构
	 $http.post("acqOrgAction/selectBoxAllInfo")
 	 .success(function(msg){
 			//响应成功
 	    	for(var i=0; i<msg.length; i++){
 	    		$scope.acqOrgs.push({value:msg[i].id,text:msg[i].acqName});
 	    	}
 	    	$scope.acqOrgId=msg[0].id
 	});
	
	var aa = [];
	//上传图片,定义控制器路径
	var opts = {acqOrg:""};
	if(_parameterName)
		opts[_parameterName] = _token;
    var uploader = $scope.uploader = new FileUploader({
        url: 'acqMerchantAction/importAcqMerchant',
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

     $scope.commit=function(){
    	 uploader.queue[0].formData[0].acqOrg=$scope.acqOrgId;
    	 uploader.uploadAll();//上传
    	 uploader.onSuccessItem = function(fileItem, response, status, headers) {//上传成功后的回调函数，在里面执行提交
		     if(response.result){
		    	 $scope.notice(response.msg);
		    	 $state.transitionTo('org.orgMer',null,{reload:true});
		     }else{
		    	$scope.notice(response.msg);
		     }
		 };	
		 return false;
     }
})