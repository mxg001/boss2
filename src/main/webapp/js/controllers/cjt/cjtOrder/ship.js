/**
 * 机具申领订单发货详情
 */
angular.module('inspinia').controller("ship", function($scope, $http,$stateParams,i18nService,$state) {
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    $scope.baseInfo = {orderNo: $stateParams.orderNo, logisticsCompany:"请选择"};
    $scope.resetForm = function(){
        $scope.terInfo = {type: null};
    }
    $scope.resetForm();

    var getCjtHpList = function(){
        $http({
            method: "get",
            url: "cjtGoods/selectCjtHpList"
        }).success(function(result){
            if(result.status){
                $scope.cjtHpList = result.data;
                $scope.cjtHpListAll = angular.copy($scope.cjtHpList);
                $scope.cjtHpListAll.unshift({typeName:"全部",hpId:null});
            }
        });
    };
    getCjtHpList();

    //查询
    $scope.query = function(){
        $scope.submitting = true;
        $http({
            url:"cjtOrder/selectCjtTerPage?pageNo=" + $scope.paginationOptions.pageNo +  "&pageSize=" + $scope.paginationOptions.pageSize,
            method:"post",
            data: $scope.terInfo
        }).success(function(result){
            $scope.submitting = false;
            if (!result || !result.status){
                $scope.notice (result.msg);
                return;
            }
            $scope.grid.data = result.data.result;
            $scope.grid.totalItems = result.data.totalCount;
        }).error(function(){
            $scope.submitting = false;
            $scope.notice("服务器异常");
        });
    };
    $scope.query();

    $scope.columnDefs = [
        {field: 'sn',displayName: 'SN号'},
        {field: 'typeName',displayName: '硬件产品种类'},
        {field: 'activityTypeName',displayName: '机具活动类型'}
    ];

    $scope.grid = {
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
            //全选
            $scope.gridApi.selection.on.rowSelectionChangedBatch($scope,function (rows) {
                if(rows[0].isSelected){
                    angular.forEach($scope.grid.data, function(item) {
                        $scope.addTer(item);
                    });
                }else{
                    angular.forEach($scope.grid.data, function(item) {
                        $scope.deleteTer(item);
                    });
                }
            });
            //单选
            $scope.gridApi.selection.on.rowSelectionChanged($scope,function (row) {
                if(row.isSelected){
                    $scope.addTer(row.entity);
                }else{
                    $scope.deleteTer(row.entity);
                }
            });
        },// 选中行
        isRowSelectable: function(row){
            angular.forEach($scope.selectGrid.data,function (item) {
                if(item.sn == row.entity.sn) {
                    row.grid.api.selection.selectRow(row.entity);
                    return false;
                }
            });
        }
    };

    $scope.selectColumnDefs = [
        {field: 'sn',displayName: 'SN号'},
        {field: 'typeName',displayName: '硬件产品种类'},
        {field: 'activityTypeName',displayName: '机具活动类型'},
        {field: 'action',displayName: '操作', cellTemplate:
            "<a class='checkbox' ng-click='grid.appScope.deleteTer(row.entity)'>移除</a>"
        },
    ];

    $scope.selectGrid = {
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: false,		  //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs: $scope.selectColumnDefs
    };

    $scope.addTer = function(entity) {
        var existsStatus = false;
        angular.forEach($scope.selectGrid.data, function(item) {
           if(item.sn == entity.sn) {
               existsStatus = true;
               return false;
           }
        });
        if(!existsStatus) {
            $scope.selectGrid.data[$scope.selectGrid.data.length] = entity;
        }
    }

    $scope.deleteTer = function(entity) {
        angular.forEach($scope.selectGrid.data, function(item, index) {
            if(item.sn == entity.sn) {
                $scope.selectGrid.data.splice(index, 1);
                return false;
            }
        });
    }

    $scope.submit = function(){
        var selectList = $scope.selectGrid.data;
        if(selectList == null || selectList.length < 1) {
            $scope.notice("请选择需要发货的机具");
            return;
        }
        $scope.baseInfo.sn = "";
        angular.forEach(selectList, function(item){
            $scope.baseInfo.sn += item.sn + ",";
        });
        $scope.submitting = true;
        $http({
            method:"post",
            url:"cjtOrder/ship",
            data: $scope.baseInfo
        }).success(function(result){
            $scope.notice(result.msg);
            $scope.submitting = false;
            if(result.status){
                $state.transitionTo ('cjt.queryCjtOrder',null,{reload:true } );
            }
        });
    }
});