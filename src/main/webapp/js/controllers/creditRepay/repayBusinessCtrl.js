/**
 * 信用卡业务管理
 */
angular.module('inspinia').controller('repayBusinessCtrl',function($scope,$http,$state,$stateParams,i18nService,$timeout){
	//数据源
	i18nService.setCurrentLang('zh-cn');
    //
    // $scope.showUpdateRepayBusinessModal = function () {
    //     $("#updateRepayBusinessModal").modal('show');
    // };
    // $scope.hideAllModel = function () {
    //     $("#updateRepayBusinessModal").modal('hide');
    //     $scope.listRepayBusiness();
    // };
    //
    // $scope.updateRepayBusiness = function () {
		// $http({
		// 	url: 'repayBusinessAction/updateRepayBusiness',
		// 	data: $scope.repayBusiness,
		// 	method: 'POST'
		// }).success(function (data) {
		// 	if(data.result){
		// 		$scope.notice("操作成功");
    //             $scope.hideAllModel();
		// 	}else{
    //             $scope.notice(data.message);
		// 	}
    //     })
    // };
    //
    // $scope.listRepayBusiness = function () {
    //     $http({
    //         url: 'repayBusinessAction/listRepayBusiness',
    //         method: 'POST'
    //     }).success(function (data) {
    //         $scope.repayBusiness = data.data;
    //         $scope.repayBusiness.rateAndSingleAmount = $scope.repayBusiness.rate + "%+" + $scope.repayBusiness.singleAmount;
    //     })
    // };
    // $scope.listRepayBusiness();


    // ============= start 变量定义 ==================
    $scope.page = {
        pageNo: 1,
        pageSize: 10
    };
    $scope.repayBusiness = {};

    $scope.resetForm = function () {
        $scope.info = {};
    };
    $scope.resetForm();

    $scope.businessGrid = {
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs: [
            {field: 'agentNo',displayName: '所属服务商',
                cellTemplate: '<span>{{row.entity.agentNo === "default" ? "默认" : row.entity.agentNo}}</span>'},
            {field: 'agentName',displayName: '所属服务商名称',
                cellTemplate: '<span>{{row.entity.agentNo === "default" ? "默认" : row.entity.agentName}}</span>'},
            {field: 'rate',displayName: '费率',cellTemplate:'<span ng-show="row.entity.rate !== null && row.entity.singleAmount !== null">{{row.entity.rate}}%+{{row.entity.singleAmount}}</span>'},
            {field: 'fullRepayRate',displayName: '全额还款费率',cellTemplate:'<span ng-show="row.entity.fullRepayRate !== null && row.entity.fullRepaySingleAmount !== null">{{row.entity.fullRepayRate}}%+{{row.entity.fullRepaySingleAmount}}</span>'},
            {field: 'perfectRate',displayName: '完美还款费率',cellTemplate:'<span ng-show="row.entity.perfectRate !== null && row.entity.perfectSingleAmount !== null">{{row.entity.perfectRate}}%+{{row.entity.perfectSingleAmount}}</span>'},
            {field: 'action',displayName: '操作',width: 250,pinnedRight:true,editable:true,cellTemplate:
            '<div class="lh30">'
            +'<a class="lh30" ' +
            // 'ng-show="hasPermit(\'business.updateCost\')" ' +
            'ng-click="grid.appScope.showUpdateRepayBusinessModal(row.entity)">修改</a>'
            +'</div>'
            }
        ],
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            $scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.page.pageNo = newPage;
                $scope.page.pageSize = pageSize;
                $scope.listRepayBusiness();
            });
            $scope.listRepayBusiness();
        }
    };
    // ============= end 变量定义 ==================

    // ============= start 事件定义 ==================
    // 查询按钮
    $scope.listRepayBusiness = function () {
    	if ($scope.loadImg) {
    		return;
    	}
    	$scope.loadImg = true;
        $http({
            url: 'repayBusinessAction/listRepayBusiness?pageNo=' + $scope.page.pageNo + "&pageSize=" + $scope.page.pageSize,
            method: 'POST',
            data: $scope.info
        }).success(function (data) {
            $scope.businessGrid.data =data.data;
            $scope.businessGrid.totalItems = data.count;
            $scope.loadImg = false;
        })
    };
    // ============= end 事件定义 ==================

    $scope.showUpdateRepayBusinessModal = function (data) {
        $scope.repayBusiness = data;
        $scope.repayBusiness.rateAndSingleAmount = $scope.repayBusiness.rate + "%+" + $scope.repayBusiness.singleAmount;
        $scope.repayBusiness.fullRepayRateAndSingleAmount = $scope.repayBusiness.fullRepayRate + "%+" + $scope.repayBusiness.fullRepaySingleAmount;
        $scope.repayBusiness.perfectRateAndSingleAmount = $scope.repayBusiness.perfectRate + "%+" + $scope.repayBusiness.perfectSingleAmount;
        $("#updateRepayBusinessModal").modal('show');
    };
    $scope.hideAllModel = function () {
        $("#updateRepayBusinessModal").modal('hide');
        $scope.listRepayBusiness();
    };
    $scope.updateRepayBusiness = function () {
        $http({
            url: 'repayBusinessAction/updateRepayBusiness',
            data: $scope.repayBusiness,
            method: 'POST'
        }).success(function (data) {
            if(data.result){
                $scope.notice("操作成功");
                    $scope.hideAllModel();
            }else{
                    $scope.notice(data.message);
            }
        })
    };

});
