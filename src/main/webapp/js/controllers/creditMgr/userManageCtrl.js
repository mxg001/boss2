/**
 * 用户管理
 */
angular.module('inspinia',['infinity.angular-chosen']).controller('cmUserManageCtrl',function($scope,$http,$state,$stateParams,i18nService,$document,SweetAlert,$timeout){
	//数据源
	i18nService.setCurrentLang('zh-cn');

	$scope.paginationOptions = {pageNo : 1,pageSize : 10};
	$scope.userTypeSelect = [{text:"普通",value:'0'},{text:"月付费",value:'1'}];

	//获取所有组织
	$scope.org=[{value:"",text:"全部"}];
	$http.post("cmUserManage/selectOrgAllInfo")
		.success(function(msg){
			//响应成功
			for(var i=0; i<msg.length; i++){
				$scope.org.push({value:msg[i].orgId, text:msg[i].orgId + " " + msg[i].orgName});
			}
		});

	//获取所有代理商
	$scope.agent=[{value:"",text:"全部"}];
	$scope.getStates = getStates;
	var oldValue="";
	var timeout="";
	function getStates(value) {
		$scope.agentt = [];
			var newValue=value;
			if(newValue != oldValue){
				if (timeout) $timeout.cancel(timeout);
				timeout = $timeout(
					function(){
						$http.post('agentInfo/selectAllInfo','item=' + value,
								{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
							.then(function (response) {
								if(response.data.length==0) {
									$scope.agentt.push({value: "", text: "全部"});
								}else{
									$scope.agentt.push({value: "", text: "全部"});
									for(var i=0; i<response.data.length; i++){
										$scope.agentt.push({value:response.data[i].agentNode,text:response.data[i].agentNo + " " + response.data[i].agentName});
									}
								}
								$scope.agent = $scope.agentt;
								oldValue = value;
							});
					},800);
			}
	};
	//代理商
	$http.post("agentInfo/selectAllInfo")
		.success(function(msg){
			//响应成功
			for(var i=0; i<msg.length; i++){
				$scope.agent.push({value:msg[i].agentNode, text:msg[i].agentNo + " " + msg[i].agentName});
			}
		});

	$scope.resetForm = function () {
		$scope.info = {sCreateTime:moment(new Date().getTime()-6*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
	            eCreateTime:moment(new Date().getTime()).format('YYYY-MM-DD'+' 23:59:59'),
	            srcOrgId:"", agentNode:"", contain:"1"};
	}
	$scope.resetForm();

	$scope.columnDefs = [
		{field: 'userNo',displayName: '用户ID',width: 150},
		{field: 'userName',displayName: '姓名',width: 150},
		{field: 'mobileNo',displayName: '手机号',width: 150},
		{field: 'srcOrgId',displayName: '来源组织ID',width: 150},
		{field: 'srcUserId',displayName: '来源用户/商户编号',width: 160},
		{field: 'orgName',displayName: '来源组织名称',width: 150},
		{field: 'agentName',displayName: '所属代理商名称',width: 150},
		{field: 'oneAgentName',displayName: '一级代理商名称',width: 150},
		{field: 'createTime',displayName: '入驻时间',width: 150,
			cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
		{field: 'userType',displayName: '会员类型',width: 150,cellTemplate:'<div class="lh30">{{ row.entity | formatDropping111 : grid.appScope.userTypeSelect}}</div>'},
		{field: 'memberExpire',displayName: '会员到期时间',width: 150,
			cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
		{field: 'action',displayName: '操作',width: 150,pinnedRight:true,editable:true,cellTemplate:
			'<a class="lh30" ui-sref="creditMgr.userDetail({userNo:row.entity.userNo})" target="_black">详情</a>'
			+ '<a class="lh30" ng-show="grid.appScope.hasPermit(\'creditMgr.updateUser\')" ui-sref="creditMgr.updateUserDetail({userNo:row.entity.userNo})" target="_black"> | 修改</a>'}
	];

	$scope.myGrid = {
		paginationPageSize:10,                  //分页数量
		paginationPageSizes: [10,20,50,100],	//切换每页记录数
		useExternalPagination: true,		  //开启拓展名
		enableHorizontalScrollbar: true,        //横向滚动条
		enableVerticalScrollbar : true,  		//纵向滚动条
//		rowHeight:35,
		columnDefs: $scope.columnDefs,
		onRegisterApi: function(gridApi) {
			$scope.gridApi = gridApi;
			$scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
				$scope.paginationOptions.pageNo = newPage;
				$scope.paginationOptions.pageSize = pageSize;
				$scope.query();
			});
		}
	};

	$scope.query = function () {
		if ($scope.loadImg) {
			return;
		}
		$scope.loadImg = true;
		$http({
			url: 'cmUserManage/selectUserInfo?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
			data: $scope.info,
			method:'POST'
		}).success(function (msg) {
			$scope.loadImg = false;
			if (!msg.status){
				$scope.notice(msg.msg);
				return;
			}
			$scope.myGrid.data = msg.page.result;
			$scope.myGrid.totalItems = msg.page.totalCount;
		}).error(function (msg) {
			$scope.notice('服务器异常,请稍后再试.');
			$scope.loadImg = false;
		});
	};

	//导出
	$scope.exportInfo=function(){
		if($scope.info.sCreateTime == "" || $scope.info.eCreateTime == ""){
			$scope.notice("入驻时间不能为空");
			return;
		}
		var stime = new Date($scope.info.sCreateTime).getTime();
		var etime = new Date($scope.info.eCreateTime).getTime();
		var sum = (etime - stime) / (24 * 60 * 60 * 1000);
		if(sum > 31){
			$scope.notice("入驻时间的跨度不能超过一个月");
			return;
		}
		SweetAlert.swal({
			title: "确认导出？",
			showCancelButton: true,
			confirmButtonColor: "#DD6B55",
			confirmButtonText: "确定",
			cancelButtonText: "取消",
			closeOnConfirm: true,
			closeOnCancel: true 
		},
		function (isConfirm) {
			if (isConfirm) {
				location.href="cmUserManage/exportCmUser?info="+encodeURI(angular.toJson($scope.info));
			}
		});
	}

	//页面绑定回车事件
	$document.bind("keypress", function(event) {
		$scope.$apply(function (){
			if(event.keyCode == 13){
				$scope.query();
			}
		})
	});
}).filter("formatDropping111",function(){
	return function(input, userTypeSelect){
		if (input.isVip == "1"){
			//会员没过期，取它的userType，转换成中文
			for(var i=0; i<userTypeSelect.length; i++){
				if (userTypeSelect[i].value == input.userType) {
					return userTypeSelect[i].text;
				}
			}
			return input.userType;
		} else {//会员过期了
			return "普通";
		}
	};
});