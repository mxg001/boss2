/**
3 * 密钥生成
 */
angular.module('inspinia').controller('terminalSecretCtrl',function(SweetAlert,$scope,$state,$http,$stateParams,$window){
	
	
	$scope.info={};
	//业务产品
	$http.get('hardwareProduct/selectAllInfo.do')
	.success(function(result){
		if(!result)
			return;
		$scope.termianlTypes=result;
	});

	/*$scope.getKeys=function(){
		$scope.submitting = true;
		$http.post("terminalInfo/addSecret.do",angular.toJson($scope.info))
		.success(function(data){
			if(data.status){
				$scope.notice("操作成功");
//				$state.go('addSecret');
				$scope.submitting = false;
			}else{
				$scope.notice("操作失败");
				$scope.submitting = false;
			}
		});
	};*/
	
	//导出
	 $scope.getKeys=function(){
	        SweetAlert.swal({
	            title: "确认导出？",
//	            text: "",
//	            type: "warning",
	            showCancelButton: true,
	            confirmButtonColor: "#DD6B55",
	            confirmButtonText: "提交",
	            cancelButtonText: "取消",
	            closeOnConfirm: true,
	            closeOnCancel: true },
		        function (isConfirm) {
		            if (isConfirm) {
				       	 location.href="terminalInfo/addSecret.do?terminalCount="+$scope.info.terminalCount+"&termianlType="+$scope.info.termianlType;
		            }
	            });
	    };
});