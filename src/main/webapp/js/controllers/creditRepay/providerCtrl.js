/**
 * 服务商查询
 */
angular.module('inspinia',['infinity.angular-chosen']).controller('providerCtrl',function($scope,$http,$state,$stateParams,i18nService,$timeout){
	//数据源
	i18nService.setCurrentLang('zh-cn');

	// ============= start 变量定义 ==================
    // 查询分页
    $scope.page = {pageNo : 1, pageSize: 10};
    // 查询条件
    $scope.info  = {};
    $scope.updateServiceCostData = {};
    
    //代理商级别,目前系统规定最多20级
    $scope.agentLevel=[{text:"一级",value:"1"},{text:"二级",value:"2"},{text:"三级",value:"3"},
    {text:"四级",value:"4"},{text:"五级",value:"5"},{text:"六级",value:"6"},{text:"七级",value:"7"},{text:"八级",value:"8"},
    {text:"九级",value:"9"},{text:"十级",value:"10"},{text:"十一级",value:"11"},{text:"十二级",value:"12"},{text:"十三级",value:"13"},
    {text:"十四级",value:"14"},{text:"十五级",value:"15"},{text:"十六级",value:"16"},{text:"十七级",value:"17"},{text:"十八级",value:"18"},
    {text:"十九级",value:"19"},{text:"二十级",value:"20"}];
    
	$scope.providerGrid = {
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs: [
            {field: 'agentNo',displayName: '服务商编号'},
            {field: 'agentLevel',displayName: '级别'},
            {field: 'agentName',displayName: '服务商名称'},
            {field: 'oneLevelId',displayName: '一级代理商编号'},
            {field: 'rate',displayName: '成本',cellTemplate:'<span ng-show="row.entity.rate !== null && row.entity.singleAmount !== null">{{row.entity.rate}}%+{{row.entity.singleAmount}}</span>'},
            {field: 'fullRepayRate',displayName: '全额还款成本',
            	cellTemplate:'<span ng-show="row.entity.fullRepayRate !== null && row.entity.fullRepaySingleAmount !== null">{{row.entity.fullRepayRate}}%+{{row.entity.fullRepaySingleAmount}}</span>'},
        	{field: 'perfectRepayRate',displayName: '完美还款成本',
        		cellTemplate:'<span ng-show="row.entity.perfectRepayRate !== null && row.entity.perfectRepaySingleAmount !== null">{{row.entity.perfectRepayRate}}%+{{row.entity.perfectRepaySingleAmount}}</span>'},
            {field: 'accountRatio',displayName: '入账比例',
                cellTemplate:'<span ng-show="row.entity.accountRatio">{{row.entity.accountRatio}} %</span>'},
            {field: 'action',displayName: '操作',width: 150,pinnedRight:true,editable:true,cellTemplate:
	         	'<div class="lh30">'
	            +'<a class="lh30" ng-show="row.entity.agentLevel==1 && row.entity.rate!==null && row.entity.singleAmount!==null && grid.appScope.hasPermit(\'provider.updateCost\')" ng-click="grid.appScope.showUpdateServiceCostModal(row.entity)">修改</a>'
            	+'</div>'
            }
        ],
        onRegisterApi: function(gridApi) {                
            $scope.gridApi = gridApi;
            $scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
	          	$scope.page.pageNo = newPage;
	          	$scope.page.pageSize = pageSize;
	            $scope.listProvider();
	        });
            $scope.listProvider();
        }
	 };
    // ============= end 变量定义 ==================

    // ============= start 事件定义 ==================
    // 查询按钮
	$scope.listProvider = function () {
        $http({
            url: 'providerAction/listProvider?pageNo='+$scope.page.pageNo +"&pageSize=" + $scope.page.pageSize,
            method: 'POST',
            data: $scope.info
        }).success(function (data) {
            $scope.providerGrid.data =data.data;
            $scope.providerGrid.totalItems = data.count;
        });
    };

	// 重置按钮
	$scope.resetForm = function () {
        $scope.info = {};
    };

	$scope.openSuperRepayment = function () {
	    console.log("---> %o" ,  $scope.gridApi.selection.getSelectedRows());
        var selectedRows = $scope.gridApi.selection.getSelectedRows();
        if (selectedRows && selectedRows.length === 0){
            $scope.notice("必须选择需要开通的服务商.");
            return;
        }
        var agentNoList = [];
        angular.forEach(selectedRows, function (item) {
            agentNoList.push(item.agentNo);
        });
        $http({
            url: 'providerAction/openSuperRepayment',
            method: 'POST',
            data: agentNoList
        }).success(function (data) {
            if (data.result){
                $scope.notice("操作成功");
                $scope.listProvider();
            }else{
                $scope.notice(data.message);
            }
        });
    };
    $scope.showUpdateServiceCostModal = function (data) {
        if(data.rate === null || data.singleAmount === null){
            $scope.notice("该服务商未开通超级还款,不能修改.");
            return;
        }
        if(data.agentLevel != '1'){
        	$scope.notice("只有一级代理商才允许修改");
            return;
        }
        $scope.updateServiceCostData = {
        	agentLevel:data.agentLevel,
            agentNo: data.agentNo,
            agentName: data.agentName,
            accountRatio: data.accountRatio,
            cost: data.rate+ "%+" + data.singleAmount,
            fullRepayCost: data.fullRepayRate+ "%+" + data.fullRepaySingleAmount,
            perfectRepayCost: data.perfectRepayRate+ "%+" + data.perfectRepaySingleAmount
        };
        $("#updateServiceCostModal").modal('show');
    };

    $scope.updateServiceCost = function () {
        if (!/^\d+(\.\d{0,6})?%\+\d+(\.\d{0,2})?$/.test($scope.updateServiceCostData.cost)){
            $scope.notice("修改后的成本不合法(例:0.63%+2)");
            return;
        }
        if (!/^\d+(\.\d{0,6})?%\+\d+(\.\d{0,2})?$/.test($scope.updateServiceCostData.fullRepayCost)){
        	$scope.notice("修改后的全额还款成本不合法(例:0.63%+2)");
        	return;
        }
        if (!/^\d+(\.\d{0,6})?%\+\d+(\.\d{0,2})?$/.test($scope.updateServiceCostData.perfectRepayCost)){
        	$scope.notice("修改后的完美还款成本不合法(例:0.63%+2)");
        	return;
        }
        $http({
            url: 'providerAction/updateServiceCost',
            method: 'POST',
            data: $scope.updateServiceCostData
        }).success(function (data) {
            if (data.result){
                $scope.notice("操作成功");
                $scope.hideAllModel();
                $scope.listProvider();
            }else{
                $scope.notice(data.message);
            }
        });
    };
    $scope.hideAllModel = function () {
        $scope.updateServiceCostData = {};
        $("#updateServiceCostModal").modal('hide');
    };

    // ============= end 事件定义 ==================
});
