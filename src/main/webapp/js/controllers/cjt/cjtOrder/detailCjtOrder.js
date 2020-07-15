/**
 * 机具申领订单详情
 */
angular.module('inspinia').controller("detailCjtOrder", function($scope, $http,$stateParams,i18nService) {
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    $scope.transTypeList = [{text:"全部", value:""},{text:"微信支付", value:"WX"},
        {text:"支付宝支付", value:"ZFB"},{text:"快捷支付", value:"KJ"}];

    $scope.goodOrderTypeStr = [{text:"无",value:null},{text:"付费购买",value:1},{text:"免费申领",value:2}];

    //查询
    $scope.query = function(){
        $scope.submitting = true;
        $http({
            url:"cjtOrder/orderDetail?orderNo=" + $stateParams.orderNo,
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