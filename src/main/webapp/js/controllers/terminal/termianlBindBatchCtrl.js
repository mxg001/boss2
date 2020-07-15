/**
3 * 机具批量绑定
 */
angular.module('inspinia',['angularFileUpload','infinity.angular-chosen']).controller('termianlBindBatchCtrl',function($scope,$state,$http,$stateParams,FileUploader,$window,$timeout,SweetAlert){
	
	$scope.agent=[{text:"全部",value:""}];
	$scope.info={};
	//业务产品
	$http.get('hardwareProduct/selectAllInfo.do')
	.success(function(result){
		if(!result)
			return;
		$scope.termianlTypes=result;
	})
	
	//所有的一级代理商
	$http.get('agentInfo/selectByLevelOne.do')
	.success(function(largeLoad) {
		if(!largeLoad){
			return;
		}
		$scope.agentList = largeLoad;
		angular.forEach($scope.agentList,function(item){
			item.agentName = item.agentNo + " " + item.agentName;
		})
	});
	$scope.getStates =getStates;
	var oldValue="";
	var timeout="";
	function getStates(value) {
//		$scope.agentt = [];
		var newValue=value;
		if(newValue != oldValue){
			if (timeout) $timeout.cancel(timeout);
			timeout = $timeout(
				function(){
					$http.post('agentInfo/selectAllInfo','item=' + value,
						{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
						.then(function (response) {
							$scope.agentList = response.data;
							angular.forEach($scope.agentList,function(item){
								item.agentName = item.agentNo + " " + item.agentName;
							});
							oldValue = value;
						});
				},800);
		}
	}
	
	//根据代理商获取对应的商户
	$scope.getMerchantList = function(){
		$scope.info.bpId = null;
		$scope.info.merchantNo = null;
		$scope.bpList = [];
		$scope.merchantList = [];
		if($scope.info.agentNo==null){
			return;
		}
		$http.post('merchantInfo/selectAllInfo.do','agentNo='+$scope.info.agentNo,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		 .success(function(data){
			 $scope.merchantList = data;
		 });
	}
	
	//根据商户获取对应的业务产品
	$scope.getBpList = function(){
		if($scope.info.merchantNo==null){
			return;
		}
		$http.post('terminalInfo/getMbpByMerId','merId='+$scope.info.merchantNo,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		 .success(function(msg){
			 if(msg.result){
				 $scope.bpList = msg.list;
				 if($scope.bpList.length>0){
					 $scope.info.bpId = $scope.bpList[0].bpId;
				 }
			 }
		 });
	}
    var uploader = $scope.uploader = new FileUploader({
        url: 'terminalInfo/terminalBindBatch',
        queueLimit: 1,   //文件个数 
        formData:[{agentNo:"",merchantNo:"",bpId:""}],
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
    	 $scope.submitting = true;
    	 uploader.queue[0].formData[0].agentNo = $scope.info.agentNo;
    	 uploader.queue[0].formData[0].merchantNo = $scope.info.merchantNo;
    	 uploader.queue[0].formData[0].bpId = $scope.info.bpId;
    	 uploader.uploadAll();//上传
    	 uploader.onSuccessItem = function(fileItem, response, status, headers) {//上传成功后的回调函数，在里面执行提交
    		 $scope.submitting = false;
		     if(response.result){
		    	 SweetAlert.swal({
		             title: response.msg,
		             type: "success",
		             showCancelButton: true,
		             confirmButtonColor: "#DD6B55",
		             confirmButtonText: "确定",
		             showCancelButton: false,
		             closeOnConfirm: true
		         });
		    	 $state.transitionTo('termianlBindBatch',null,{reload:true});
		     }else{
		    	 SweetAlert.swal({
		             title: response.msg,
		             type: "warning",
		             showCancelButton: true,
		             confirmButtonColor: "#DD6B55",
		             confirmButtonText: "确定",
		             showCancelButton: false,
		             closeOnConfirm: true
		          });
		     }
		 };	
     }
});