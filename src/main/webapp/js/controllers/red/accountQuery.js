/**
 * 平台红包账户查询
 */
angular.module('inspinia').controller('accountQueryCtrl', function($scope, $http,$stateParams,i18nService, SweetAlert){
    //数据源
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    //(0平台、1品牌商、2个人)
    $scope.userType= [{text:"平台",value:"0"},{text:"品牌商",value:"1"},{text:"个人",value:"2"}];
    $scope.userTypeStr=angular.toJson($scope.userType);
    
    $scope.resetForm = function () {
        $scope.baseInfo = {
        		relationId:"",type:"0"};
    }
    $scope.resetForm();
    
    $scope.columnDefs = [
        {field: 'id',displayName: '',pinnable: false,sortable: false,width:0},
        {field: 'accountName',displayName: '账户名称',pinnable: false,sortable: false},
        {field: 'type',displayName: '用户类型',pinnable: false,sortable: false,cellFilter:"formatDropping:"+$scope.userTypeStr},
        {field: 'tempId',displayName: '用户ID／组织编号',pinnable: false,sortable: false},
        {field: 'userName',displayName: '用户/组织名称',pinnable: false,sortable: false},
        {field: 'totalAmount',displayName: '可用金额',pinnable: false,sortable: false},
        {field: 'transAmountStr',displayName: '操作',pinnable: false,sortable: false,cellTemplate:
            '<div class="lh30">' +
            " <a ui-sref='red.plateAccount({id:row.entity.id})'>收支明细</a>"}
    ];

    $scope.grid = {
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
        $scope.submitting = true;
        $scope.loadImg = true;
         $http.post("manor/redAccountDetail","baseInfo=" + angular.toJson($scope.baseInfo)+"&pageNo="+
            $scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function (result) {
            $scope.submitting = false;
            $scope.loadImg = false;
            if (!result.status){
                $scope.notice(result.msg);
                return;
            }
            $scope.grid.data = result.data.page.result;
            $scope.grid.totalItems = result.data.page.totalCount;
            $scope.totalInfo = result.data.totalInfo;
        }).error(function () {
            $scope.submitting = false;
            $scope.loadImg = false;
            $scope.notice('服务器异常,请稍后再试.');
        });
    };
    // $scope.query();

    $scope.exportInfo=function(){
        SweetAlert.swal({
                title: "确认导出？",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true
            },
            function (isConfirm) {
                if (isConfirm) {
                    location.href="manor/exportAccountQuery?baseInfo="+encodeURI(angular.toJson($scope.baseInfo));
                }
            });
    };
});
