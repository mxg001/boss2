/**
 * 行用卡通道管理
 */
angular.module('inspinia',['infinity.angular-chosen','uiSwitch']).controller('dealTemplateCtrl',function($scope,$http,$state,$stateParams,i18nService,$document,SweetAlert){
	//数据源
	i18nService.setCurrentLang('zh-cn');

	$scope.paginationOptions = {pageNo : 1,pageSize : 10};
	$scope.channelList = [];
	$scope.statusSelect = [{text:"开启",value:'1'},{text:"关闭",value:'0'}];
    $scope.templateNo='';
    $scope.info={};

    $scope.query = function () {
    	if($scope.templateNo==null){
    		$scope.templateNo="";
		}
        $http({
            url: 'dealTemplate/selectTemplateList?templateNo='+$scope.templateNo+'&pageNo='+$scope.paginationOptions.pageNo
            +'&pageSize='+$scope.paginationOptions.pageSize,
            method:'POST'
        }).success(function (result) {
            if (!result.status){
                $scope.notice(result.msg);
                return;
            }
            $scope.myGrid.data = result.page.result;
            $scope.myGrid.totalItems = result.page.totalCount;
        }).error(function (msg) {
            $scope.notice('服务器异常,请稍后再试.');
        });
    };
    $scope.query();

    $scope.columnDefs = [
        {field: 'templateNo',displayName: '模板编码',width: 150,pinnable: false,sortable: false},
        {field: 'templateContent',displayName: '模板内容',pinnable: false,sortable: false},
        {field: 'creator',displayName: '操作人',width: 150,pinnable: false,sortable: false},
        {field: 'action',displayName: '操作',width: 150,pinnedRight:true,sortable: false,editable:true,cellTemplate:
            '<a class="lh30" ' +
            'ng-show="row.entity.templateNo != 101 && row.entity.templateNo != 102 && row.entity.templateNo != 103 && grid.appScope.hasPermit(\'dealTemplate.update\')" ' +
            'ng-click="grid.appScope.openEditModal(row.entity.id)">修改</a>&nbsp;&nbsp;&nbsp;&nbsp;' +
            '<a class="lh30" ' +
            'ng-show="row.entity.templateNo != 101 && row.entity.templateNo != 102 && row.entity.templateNo != 103 && grid.appScope.hasPermit(\'dealTemplate.delete\')" ' +
            'ng-click="grid.appScope.delete(row.entity.id)">删除</a>'}
    ];

    $scope.myGrid = {
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        enableHorizontalScrollbar: 0,        //横向滚动条
        enableVerticalScrollbar : 0,  		//纵向滚动条
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

	$scope.add = function () {
        $scope.submitting = true;
        SweetAlert.swal({
                title: "是否提交",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post('dealTemplate/add', $scope.info).success(
                        function (data) {
                            if(data.status){
                                $scope.notice("添加成功");
                                $scope.closeAddModal();
                                $scope.query();
                            }else{
                                $scope.notice("添加失败");
                            }
                        });
                }
            });
        $scope.submitting = false;
    }


	$scope.clear = function () {
        $scope.templateNo = "";
    }

    $scope.openAddModal = function () {
        $scope.title = "新增模板";
        $scope.show = true;
        $("#addModal").modal("show");
    }

    $scope.closeAddModal = function () {
        $("#addModal").modal("hide");
        $scope.info={};
    }

    $scope.openEditModal = function (id) {
	    $http.get("dealTemplate/selectById?id="+id)
            .success(function (result) {
                if(result.status){
                   $scope.info = result.template;
                }
            });
        $scope.title = "修改模板";
        $scope.show = false;
        $("#addModal").modal("show");
    }
    
    $scope.edit = function () {
        SweetAlert.swal({
                title: "是否修改",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "修改",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post('dealTemplate/edit', $scope.info).success(
                        function (data) {
                            if(data.status){
                                $scope.notice("修改成功");
                                $scope.closeAddModal();
                                $scope.info={};
                                $scope.query();
                            }else{
                                $scope.notice("修改失败");
                            }
                        });
                }
            });
    }

    $scope.delete = function (id) {
        SweetAlert.swal({
                title: "是否删除",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "删除",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post('dealTemplate/delete?id='+id).success(
                        function (data) {
                            if(data.status){
                                $scope.notice("删除成功");
                                $scope.query();
                            }else{
                                $scope.notice("删除失败");
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