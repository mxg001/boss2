/**
 * 新增商户迁移
 */
angular.module('inspinia', ['infinity.angular-chosen','angularFileUpload']).controller('addMerchantMigrateCtrl',function(i18nService,$scope,$http,$timeout,$state,$stateParams,FileUploader,$window){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	$scope.info={agentNo:"",agentNode:"",oaNo:""};

	//一级代理商
	$scope.oneAgentList=[{text:"全部",value:""}];
	$scope.getOneAgentList=function(value,oldValueSta){
		$http.post('agentInfo/selectAllOneInfo','item=' + value,
			{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			.then(function (response) {
				$scope.agentItem=[{text:"全部",value:""}];
				if(response.data.length>0) {
					for(var i=0; i<response.data.length; i++){
						$scope.agentItem.push({value:response.data[i].agentNo,
							text:response.data[i].agentNo+" "+response.data[i].agentName});
					}
				}
				$scope.oneAgentList=$scope.agentItem;
				if(oldValueSta){
					oldValueOneAgent=value;
				}
			});
	};
	$scope.getOneAgentList("",false);


	//一级代理商动态查询
	$scope.getOneAgentStates =getOneAgentStates;
	var oldValueOneAgent="";
	var timeoutOneAgent="";
	function getOneAgentStates(value) {
		var newValue=value;
		if(newValue != $scope.oldValueOneAgent){
			if (timeoutOneAgent) $timeout.cancel(timeoutOneAgent);
			timeoutOneAgent = $timeout(
				function(){
					$scope.getOneAgentList(value,true);
					},800);
		}
	};

	//代理商
	$scope.agentList=[{text:"全部",value:""}];
	$scope.getAgentList=function(parentAgentNo,value,oldValueSta){
		$scope.agentList=[{text:"全部",value:""}];
		if(parentAgentNo==null||""==parentAgentNo){
			return;
		}
		$http.post('agentInfo/getAllAgentListByParent','item=' + value+'&parentAgentNo=' + parentAgentNo,
			{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			.then(function (response) {
				$scope.agentItem=[{text:"全部",value:""}];
				if(response.data.length>0) {
					for(var i=0; i<response.data.length; i++){
						$scope.agentItem.push({value:response.data[i].agentNo,
							text:response.data[i].agentNo+" "+response.data[i].agentName});
					}
				}
				$scope.agentList=$scope.agentItem;
				if(oldValueSta){
					oldValueAgent=value;
				}
			});
	};
	$scope.getAgentList("","",false);
	//一级代理商动态查询
	$scope.getAgentStates =getAgentStates;
	var oldValueAgent="";
	var timeoutAgent="";
	function getAgentStates(value) {
		var newValue=value;
		if(newValue != $scope.oldValueAgent){
			if (timeoutAgent) $timeout.cancel(timeoutAgent);
			timeoutAgent = $timeout(
				function(){
					$scope.getAgentList($scope.info.agentNo,value,true);
				},800);
		}
	};

	$scope.getNodeAgent=function(){
		$scope.getAgentList($scope.info.agentNo,"",false);
		$scope.info.agentNode="";
	};

	var opts = {agentNo:"",agentNode:"",goSn:"2",oaNo:""};
	var uploader = $scope.uploader = new FileUploader({
		url: 'merchantMigrate/addMigrate',
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

	$scope.addMerchantMigrate = function(){
		if($scope.info.agentNo=="-1"){
			$scope.notice("请选择目标一级代理商"+$scope.info.agentNo);
			return;
		}
		
		if($scope.info.agentNode == "-1"){
			$scope.notice("请选择目标直属代理商"+$scope.info.agentNode);
			return;
		}
		
		if($scope.info.oaNo!="" && $scope.info.oaNo.length>30){
			$scope.notice("关联OA单号超过系统限制30位，本项非必填项");
			return;
		}
		
		if($scope.uploader.queue[0] == null){
			$scope.notice("你若上传附件，我必感激涕零");
			return;
		}
		
		var fileNmaes = uploader.queue[0].file.name.substring(uploader.queue[0].file.name.lastIndexOf("."));
		if(fileNmaes != ".xls" && fileNmaes != ".xlsx"){
			$scope.notice(fileNmaes+"请上传后缀名为 .xls 或 .xlsx 的有效Excel文件!");
			return false;
		}
		$scope.uploader.queue[0].formData[0].agentNo=$scope.info.agentNo;
		$scope.uploader.queue[0].formData[0].agentNode=$scope.info.agentNode;
		$scope.uploader.queue[0].formData[0].oaNo=$scope.info.oaNo;
		$scope.uploader.uploadAll();//上传
		$scope.uploader.onSuccessItem = function(fileItem, response, status, headers) {//上传成功后的回调函数，在里面执行提交
	   		$scope.notice(response.addMsg);
		};	
		return false;
	}
	
	
	
})