/**
 * 查询公告
 */
angular.module('inspinia').controller("queryCreditRepayNoticeCtrl", function($scope, $http, $state, $stateParams,$filter,i18nService,SweetAlert,$document) {
	//数据源
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	$scope.statusTypes=[{text:"全部",value:0},{text:"待下发",value:1},{text:"正常",value:2}];
	$scope.baseInfo = {status:0,createTimeBegin:null,createTimeEnd:null,issuedTimeBegin:null,issuedTimeEnd:null};
	$scope.statusTypesStr = angular.toJson($scope.statusTypes);
	$scope.queryFunc = function(){
		$scope.result=[];
		if($scope.baseInfo.createTimeBegin!=null&& $scope.baseInfo.createTimeEnd != null &&
			$scope.baseInfo.createTimeBegin > $scope.baseInfo.createTimeEnd){
			$scope.notice("创建起始时间不能大于终止时间");
			return ;
		}
		if($scope.baseInfo.issuedTimeBegin!= null && $scope.baseInfo.issuedTimeEnd != null &&
			$scope.baseInfo.issuedTimeBegin > $scope.baseInfo.issuedTimeEnd){
			$scope.notice("下发起始时间不能大于终止时间");
			return ;
		}
		$http.post("creditRepayNotice/selectByParam.do","baseInfo=" + angular.toJson($scope.baseInfo)+"&pageNo="+
			$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
			{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			.success(function(data){
				$scope.result=data.result;
				$scope.noticeListGrid.totalItems = data.totalCount;
			});
	};
	$scope.queryFunc();
	$scope.noticeListGrid = {
		data: 'result',
		paginationPageSize:10,                  //分页数量
		paginationPageSizes: [10,20,50,100],	//切换每页记录数
		useExternalPagination: true,		    //开启拓展名
		enableHorizontalScrollbar: true,        //横向滚动条
		enableVerticalScrollbar : true,  		//纵向滚动条
		columnDefs: [
			{field: 'noticeNo',displayName: '通告编号',pinnable: false,sortable: false},
			{field: 'title',displayName: '标题',pinnable: false,sortable: false},
			{field: 'status',displayName: '状态',pinnable: false,sortable: false,cellFilter:"formatDropping:" + $scope.statusTypesStr},
			{field: 'createTime',displayName: '创建时间',width:180,pinnable: false,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
			{field: 'issuedTime',displayName: '下发时间',width: 150,pinnable: false,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
			{field: 'id',displayName: '操作',pinnedRight:true,width:180,cellTemplate:
			'<div class="lh30">' +
			'<a ui-sref="creditRepay.detailNotice({id:row.entity.id})" target="_black" >详情</a>' +

			'<span ng-switch on="row.entity.status">'+
				'<span ng-switch-when="1">'+
					'<a ng-show="grid.appScope.hasPermit(\'creditRepayNotice.modifyNotice\')" ui-sref="creditRepay.modifyNotice({id:row.entity.id})" target="_black"> | 修改</a>' +
					'<a ng-show="grid.appScope.hasPermit(\'creditRepayNotice.issueNotice\')" ui-sref="creditRepay.issueNotice({id:row.entity.id})" target="_black" > | 下发</a>'+
				'</span>'+
				'<span ng-switch-when="2">'+
					'<a ng-show="grid.appScope.hasPermit(\'creditRepayNotice.reclaimNotice\')" ng-click="grid.appScope.reclaimNotice(row.entity.id)"> | 收回</a>'+
				'</span>' +
			'</span>'+
			'<a ng-show="grid.appScope.hasPermit(\'creditRepayNotice.deleteNotice\')" ng-click="grid.appScope.deleteNotice(row.entity.id)"> | 删除</a>'+
			'</div>'}
		],
		onRegisterApi: function(gridApi) {
			$scope.gridApi = gridApi;
			gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
				$scope.paginationOptions.pageNo = newPage;
				$scope.paginationOptions.pageSize = pageSize;
				$scope.queryFunc();
			});
		}
	};
  	//清空
	$scope.resetForm=function(){
		$scope.baseInfo= {status:0,createTimeBegin:null,createTimeEnd:null,issuedTimeBegin:null,issuedTimeEnd:null};
	};

	//回收公告
	$scope.reclaimNotice = function(id){
		SweetAlert.swal({
				title: "确认收回？",
				type: "warning",
				showCancelButton: true,
				confirmButtonColor: "#DD6B55",
				confirmButtonText: "提交",
				cancelButtonText: "取消",
				closeOnConfirm: true,
				closeOnCancel: true },
			function (isConfirm) {
				if (isConfirm) {
					$http.get('creditRepayNotice/reclaimNotice/'+id)
						.success(function(msg){
							$scope.notice(msg.msg);
							$scope.queryFunc();
						}).error(function(msg){
							$scope.notice(msg.msg);
					});
				}
			});
	}

	//删除公告
	$scope.deleteNotice = function(id){
		SweetAlert.swal({
				title: "确认删除公告?",
				type: "warning",
				showCancelButton: true,
				confirmButtonColor: "#DD6B55",
				confirmButtonText: "提交",
				cancelButtonText: "取消",
				closeOnConfirm: true,
				closeOnCancel: true },
			function (isConfirm) {
				if (isConfirm) {
					$http.get('creditRepayNotice/deleteNotice/'+id)
						.success(function(msg){
							$scope.notice(msg.msg);
							$scope.queryFunc();
						}).error(function(msg){
						$scope.notice(msg.msg);
					});
				}
			});
	}
	//页面绑定回车事件
	$document.bind("keypress", function(event) {
		$scope.$apply(function (){
			if(event.keyCode == 13){
				$scope.queryFunc();
			}
		})
	});
});
