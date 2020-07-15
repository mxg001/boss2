/**
 * 提额任务
 */
angular.module('inspinia',['infinity.angular-chosen']).controller('quotaTaskCtrl',function($scope,$http,$state,$stateParams,i18nService,$document,SweetAlert,$timeout){
	//数据源
	i18nService.setCurrentLang('zh-cn');

	$scope.paginationOptions = {pageNo : 1,pageSize : 10};
	$scope.bankNameSelect = [];

	$http({
		url: 'cmTask/selectBankName',
		method:'GET'
	}).success(function (data) {
		if (data.status){
			$scope.bankNameSelect = data.bankNames;
		} else {
			$scope.notice(data.msg);
		}
	}).error(function (data) {
		$scope.notice('服务器异常,请稍后再试.');
		$scope.loadImg = false;
	});

	$scope.resetForm = function () {
		$scope.info = {startTime:moment(new Date().getTime()-60*24*60*60*1000).format('YYYY-MM-DD'),
				endTime:moment(new Date().getTime()).format('YYYY-MM-DD')};
	}
	$scope.resetForm();

	$scope.columnDefs = [
		{field: 'id',displayName: '提额任务ID',width: 150},
		{field: 'cardNo',displayName: '银行卡号',width: 150},
		{field: 'bankName',displayName: '银行名称',width: 150},
		{field: 'userNo',displayName: '用户ID',width: 150},
		{field: 'userName',displayName: '姓名',width: 120},
		{field: 'cardHealth',displayName: '卡健康度',width: 120,cellTemplate:
			'<div class="lh30">{{row.entity.cardHealth}}分</div>'},
		{field: 'increasePossible',displayName: '提额指数',width: 120,cellTemplate:
			'<div class="lh30">{{row.entity.increasePossible}}%</div>'},
		{field: 'taskHaveComplete',displayName: '计划完成度',width: 120,cellTemplate:
			'<div class="lh30">{{row.entity.taskHaveComplete}}%</div>'},
		{field: 'time',displayName: '提额任务时间',width: 240,cellTemplate:
			'<div class="lh30">{{row.entity.startTime}} — {{row.entity.endTime}}</div>'},
		{field: 'action',displayName: '操作',width: 120,pinnedRight:true,sortable: false,editable:true,cellTemplate:
			'<a class="lh30" ui-sref="creditMgr.taskDetail({id:row.entity.id})" target="_black">详情</a>'}
	];

	$scope.myGrid = {
		paginationPageSize:10,                  //分页数量
		paginationPageSizes: [10,20,50,100],	//切换每页记录数
		useExternalPagination: true,
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
			url: 'cmTask/selectTaskInfo?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
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
                    location.href="cmTask/exportTaskInfo?info="+encodeURI(angular.toJson($scope.info));
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