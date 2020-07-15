/**
 * 机具申领订单发货详情
 */
angular.module('inspinia').controller("shipDetail", function($scope, $http,$stateParams,i18nService) {
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    //查询
    $scope.query = function(){
        $scope.submitting = true;
        $http({
            url:"cjtOrder/shipDetail?orderNo=" + $stateParams.orderNo,
            method:"get"
        }).success(function(result){
            $scope.submitting = false;
            if (!result || !result.status){
                $scope.notice (result.msg);
                return;
            }
            $scope.baseInfo = result.data.baseInfo;
            $scope.grid.data = result.data.snList;
            $scope.grid.totalItems = result.data.snList.length;
        }).error(function(){
            $scope.submitting = false;
            $scope.notice("服务器异常");
        });
    };
    $scope.query();

    $scope.columnDefs = [
        {field: 'sn',displayName: 'SN号'},
        {field: 'hpName',displayName: '硬件产品种类'}
    ];

    $scope.grid = {
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: false,		  //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs: $scope.columnDefs,
        // onRegisterApi: function(gridApi) {
        //     $scope.gridApi = gridApi;
        //     $scope.gridApi.pagination.on.paginationChanged ($scope, function(newPage, pageSize) {
        //         $scope.paginationOptions.pageNo = newPage;
        //         $scope.paginationOptions.pageSize = pageSize;
        //         $scope.query();
        //     });
        // }
    };
});