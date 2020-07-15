/**
 * 金额黑名单查询
 */
angular.module('inspinia').controller("queryBlacklistAmount", function($scope, $http, i18nService,$document, SweetAlert) {
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    //清空
    $scope.resetForm = function () {
        $scope.baseInfo = {};
    }
    $scope.resetForm();
    //查询
    $scope.query = function(){
        $scope.submitting = true;
        $scope.loadImg = true;
        $http({
            url:"blacklistAmount/selectPage?pageNo=" + $scope.paginationOptions.pageNo +  "&pageSize=" + $scope.paginationOptions.pageSize,
            method:"post",
            data:$scope.baseInfo
        }).success(function(result){
            $scope.submitting = false;
            $scope.loadImg = false;
            if (!result || !result.status){
                $scope.notice (result.msg);
                return;
            }
            $scope.blacklistAmountGrid.data = result.data.result;
            $scope.blacklistAmountGrid.totalItems = result.data.totalCount;
        }).error(function(){
            $scope.submitting = false;
            $scope.loadImg = false;
            $scope.notice("服务器异常");
        });
    };
    $scope.query();

    $scope.columnDefs = [
                    {field: 'jumpRuleId',displayName: '路由ID'},
                    {field: 'amount',displayName: '金额'},
                    {field: 'createTime',displayName: '创建时间',cellFilter:"date:'yyyy-MM-dd HH:mm:ss'"},
                    {field: 'operator',displayName: '操作人'},
                {field: 'action',displayName: '操作',width: 120,pinnedRight:true,sortable: false,editable:true,cellTemplate:
            '<a class="lh30"  ng-show="grid.appScope.hasPermit(\'blacklistAmount.delete\')" '
            + 'ng-click="grid.appScope.delete(row.entity.id)">删除</a>'
            }
    ];

    $scope.blacklistAmountGrid = {
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs: $scope.columnDefs,
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            $scope.gridApi.pagination.on.paginationChanged ($scope, function(newPage, pageSize) {
                $scope.paginationOptions.pageNo = newPage;
                $scope.paginationOptions.pageSize = pageSize;
                $scope.query();
            });
        }
    };

    $scope.delete = function (id) {
        SweetAlert.swal({
                title: "确定删除吗？",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http({
                        url: 'blacklistAmount/delete?id=' + id,
                        method:'get'
                    }).success(function (msg) {
                        $scope.notice(msg.msg);
                        if (msg.status){
                            $scope.query();
                        }
                    }).error(function (msg) {
                        $scope.notice('服务器异常,请稍后再试.');
                    });
                }
            });
    }

    $scope.addInfoModal = function(){
        $scope.addInfo = {};
        $('#addInfoModal').modal('show');
    }

    $scope.cancel = function(){
        $('#addInfoModal').modal('hide');
    }

    $scope.add = function () {
        $http({
            url: 'blacklistAmount/insert',
            data:$scope.addInfo,
            method:'post'
        }).success(function (msg) {
            $scope.notice(msg.msg);
            if (msg.status){
                $scope.cancel();
                $scope.query();
            }
        }).error(function (msg) {
            $scope.notice('服务器异常,请稍后再试.');
        });

    }



    //页面绑定回车事件
    $document.bind ("keypress", function(event) {
        $scope.$apply(function (){
            if(event.keyCode == 13){
                $scope.query();
            }
        })
    });

});