/**
 * 订单类型管理
 */
angular.module('inspinia',['infinity.angular-chosen','uiSwitch']).controller('repayTypeManagerCtrl',function($scope,$http,$state,$stateParams,i18nService,$document,SweetAlert,$timeout){
	//数据源
	i18nService.setCurrentLang('zh-cn');

	$scope.paginationOptions = {pageNo : 1,pageSize : 10};
	$scope.typesSelect = [{text:"分期还款",value:'1'},{text:"全额还款",value:'2'},{text:"完美还款",value:'3'}];
	$scope.statusSelect = [{text:"关闭",value:'0'},{text:"开启",value:'1'}];
    $scope.planType='';

    $scope.columnDefs = [
        {field: 'planType',displayName: '订单类型',width: 160,pinnable: false,sortable: false,
            cellFilter:"formatDropping:"+ angular.toJson($scope.typesSelect)},
        {field: 'status',displayName: '服务开关',width: 160,pinnable: false,sortable: false,
            cellFilter:"formatDropping:"+ angular.toJson($scope.statusSelect)},
        {field: 'action',displayName: '操作',width: 200,pinnedRight:true,sortable: false,editable:true,cellTemplate:
            '<a class="lh30" ui-sref="creditRepay.typeDetail({id:row.entity.id})" target="_black">详情</a>&nbsp;&nbsp;&nbsp;&nbsp;' +
            '<a class="lh30" ui-sref="creditRepay.typeEdit({id:row.entity.id})" target="_black">修改</a>'}
    ];


    $scope.query = function () {
    	if($scope.planType==null){
    		$scope.planType="";
		}
        $http({
            url: 'repayType/selectRepayTypeList?planType='+$scope.planType+'&pageNo='+$scope.paginationOptions.pageNo
            +'&pageSize='+$scope.paginationOptions.pageSize,
            method:'POST'
        }).success(function (msg) {
            if (!msg.status){
                $scope.notice(msg.msg);
                return;
            }
            $scope.myGrid.data = msg.page.result;
            $scope.myGrid.totalItems = msg.page.totalCount;
        }).error(function (msg) {
            $scope.notice('服务器异常,请稍后再试.');
        });
    };
    //$scope.query();


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


	//页面绑定回车事件
	$document.bind("keypress", function(event) {
		$scope.$apply(function (){
			if(event.keyCode == 13){
				$scope.query();
			}
		})
	});
});