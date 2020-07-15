angular.module('inspinia',['angularFileUpload']).controller('blacklistimportCtrl',function($scope,$state,$http,$stateParams,FileUploader,$window){

	$scope.acqOrgs=[];
	$scope.resultStr="";
	var aa = [];
	//上传图片,定义控制器路径
	var opts = {acqOrg:""};
	if(_parameterName)
		opts[_parameterName] = _token;
    var uploader = $scope.uploader = new FileUploader({
        url: 'riskRollAction/importBlacklist',
        formData:[opts],
        queueLimit: 1,   //文件个数 
        removeAfterUpload: true,  //上传后删除文件
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    uploader.filters.push({
        name: 'isFile',
        fn: function(item, options) {
            if(item.size>10*1000*1024){
                $scope.notice("上传文件超过了10M限制，请调整后重新上传!");
                return false;
            }
            return this.queue.length < 1;
        }
    });
    
     $scope.clearItems = function(){  //重新选择文件时，清空队列，达到覆盖文件的效果
    	 uploader.clearQueue();
     }

    function newLineBySign(value){
     	value=value.trim();
     	var result='';
     	if(value.indexOf("\\n")>0)
     	{ result=value.replace(/(\\n)/g,"$1\n");result=result.replace(/(\\n)/g,"");}
     	return result;
    }
     $scope.commit=function(){
    	 
    	 if(uploader.queue.length==0){
    		 $scope.notice("请选择上传文件");
    		 return ;
    	 };
    	 
    	 $scope.submitting = true;
         $scope.loadImg = true;

         uploader.uploadAll();//上传
    	 uploader.onSuccessItem = function(fileItem, response, status, headers) {//上传成功后的回调函数，在里面执行提交
             if(response.result){
                 $scope.loadImg = false;
                 $("#resultModel").modal("show");
		    	 $scope.resultStr = newLineBySign(response.msg);
		    	 $scope.successCount = response.successCount;
		    	 $scope.failCount = response.failCount;
		    	 $scope.submitting = false;
		    	 //$state.transitionTo('risk.blacklistQuery',null,{reload:true});
		     }else{
                 $scope.loadImg = false;
                 $("#resultModel").modal("show");
		    	 $scope.resultStr = newLineBySign(response.msg);
                 $scope.successCount = response.successCount;
                 $scope.failCount = response.failCount;
		    	 $scope.submitting = false;
		    	 //$scope.notice(response.msg);
		     }
		 };	
		 return false;
     }
     
     $scope.cancel=function(){
    		$scope.hardWare={hardId:"",price:"",targetAmout:""};
    		$('#resultModel').modal('hide');
    	}
     
})