angular.module('inspinia').controller('switchSpecialSet069Ctrl',function(i18nService,$scope,$http,$state,$stateParams,$compile,$filter,SweetAlert){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	$scope.paginationOptions=angular.copy($scope.paginationOptions);

	$scope.addInfo={};
	//查询开关设置相关信息
	$scope.getFunInfo = function(){
		$http.get('functionManager/getFunctionManagerInfo?functionNumber=' + $stateParams.functionNumber)
			.success(function(data){
				if(data.status){
					$scope.baseInfo = data.info;
					$scope.titleName = $scope.baseInfo.functionName;
				} else {
					$scope.notice(data.msg);
				}
			});
	};
	$scope.getFunInfo();

	//查询开关设置相关信息
	$scope.query = function(){
		$http.get('functionManager/getFunctionManagerTeamList?functionNumber=' + $stateParams.functionNumber)
			.success(function(data){
				if(data.status){
					$scope.result = data.list;
					if($scope.result.length==1){
						$scope.addInfo=$scope.result[0];
					}
				} else {
					$scope.notice(data.msg);
				}
			});
	};
	$scope.query();

	$scope.submitting = false;
	//保存开关基本信息
	$scope.submit = function(){
		if($scope.submitting){
			return;
		}
		var isNum=/^\d*$/;
		if(!isNum.test($scope.addInfo.cycle)){
			$scope.notice("弹窗周期格式不正确!");
			return;
		}

		$scope.submitting = true;
		$http({
			method: "post",
			url: "functionManager/updateBaseInfo",
			data: $scope.baseInfo
		}).success(function(result){
			$scope.teamAdd();//调用个性配置存储
			$scope.notice(result.msg);
			$scope.submitting = false;
		}).error(function(){
			$scope.notice('服务异常');
			$scope.submitting = false;
		});
	};

	//新增
	$scope.teamAdd=function () {
		//校验
		$scope.submittingMode=true;
		$scope.subInfo=angular.copy($scope.addInfo);
		$scope.subInfo.functionNumber=$stateParams.functionNumber;

		var data={
			info:angular.toJson($scope.subInfo),
		};
		var postCfg = {
			headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
			transformRequest: function (data) {
				return $.param(data);
			}
		};
		$http.post("functionManager/saveFunctionConfigure",data,postCfg)
			.success(function(data){
				if(data.status){

				}else{
					$scope.notice(data.msg);
				}
			})
			.error(function(data){
				$scope.notice(data.msg);
			});
	};
});