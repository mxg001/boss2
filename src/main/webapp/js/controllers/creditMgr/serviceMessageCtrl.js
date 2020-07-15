/**
 * 服务消息
 */
angular.module('inspinia').controller('serviceMessageCtrl',function($scope,$http,$state,$stateParams,i18nService,$document,SweetAlert){
	//数据源
	i18nService.setCurrentLang('zh-cn');

	$scope.paginationOptions = {pageNo : 1,pageSize : 10};
	$scope.msgStatusSelect = [{text:"未读",value:'0'},{text:"已读",value:'1'},{text:"关闭",value:'2'}];
	$scope.msgTypeSelect = [{text:"账单类",value:"bill"},{text:"用卡计划类",value:"plan"},{text:"提额任务类",value:"task"},{text:"其他类",value:"other"}];

	$scope.resetForm = function () {
		$scope.info = {sCreateTime:moment(new Date().getTime()-6*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
	            eCreateTime:moment(new Date().getTime()).format('YYYY-MM-DD'+' 23:59:59')};
	}
	$scope.resetForm();

	$scope.columnDefs = [
		{field: 'id',displayName: '消息ID',width: 150},
		{field: 'cardNo',displayName: '银行卡号',width: 150},
		{field: 'msgType',displayName: '消息类型',width: 150,cellFilter:"formatDropping:"+ angular.toJson($scope.msgTypeSelect)},
		{field: 'msgTitle',displayName: '消息标题',width: 200},
		{field: 'createTime',displayName: '发送时间',width: 150,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
		{field: 'msgStatus',displayName: '状态',width: 150},
		{field: 'userNo',displayName: '用户ID',width: 150},
		{field: 'action',displayName: '操作',width: 150,pinnedRight:true,sortable: false,editable:true,cellTemplate:
			'<a class="lh30" ui-sref="creditMgr.messageDetail({id:row.entity.id})" target="_black">详情</a>'
			+'<a class="lh30" ng-show="grid.appScope.hasPermit(\'creditMgr.recoverMessage\')" ng-click="grid.appScope.updateIsDel(row.entity)"> | 回收</a>'}
	];

	$scope.myGrid = {
		paginationPageSize:10,                  //分页数量
		paginationPageSizes: [10,20,50,100],	//切换每页记录数
		useExternalPagination: true,		  //开启拓展名
		enableHorizontalScrollbar: true,        //横向滚动条
		enableVerticalScrollbar : true,  		//纵向滚动条
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
			url: 'cmServiceMsg/selectMsgInfo?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
			data: $scope.info,
			method:'POST'
		}).success(function (data) {
			$scope.loadImg = false;
			if (!data.status){
				$scope.notice(data.msg);
				return;
			}
			$scope.myGrid.data = data.page.result;
			$scope.myGrid.totalItems = data.page.totalCount;
		}).error(function (msg) {
			$scope.notice('服务器异常,请稍后再试.');
			$scope.loadImg = false;
		});
	};

	$scope.updateIsDel = function(entity){
		SweetAlert.swal({
			title: "确认回收？",
			showCancelButton: true,
			confirmButtonColor: "#DD6B55",
			confirmButtonText: "确定",
			cancelButtonText: "取消",
			closeOnConfirm: true,
			closeOnCancel: true },
			function (isConfirm) {
				if (isConfirm) {
					$http.post("cmServiceMsg/updateMsgIsDelById?id=" + entity.id)
						.success(function(data){
							$scope.notice(data.msg);
							if(data.status){
								$scope.query();
							}
						});
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
});