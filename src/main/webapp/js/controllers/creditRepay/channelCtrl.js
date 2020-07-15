/**
 * 行用卡通道管理
 */
angular.module('inspinia',['infinity.angular-chosen','uiSwitch']).controller('channelCtrl',function($scope,$http,$state,$stateParams,i18nService,$document,SweetAlert,$timeout){
	//数据源
	i18nService.setCurrentLang('zh-cn');

	$scope.paginationOptions = {pageNo : 1,pageSize : 10};
	$scope.channelList = [];
	$scope.statusSelect = [{text:"开启",value:'1'},{text:"关闭",value:'0'}];
    $scope.id='';

    //通道选项下拉框
    $scope.selectOptionList = function () {
        $http.get('channel/selectOptionList').success(
            function (data) {
                $scope.channelList = data.channelList;
            }
        );
    }

    $scope.columnDefs = [
        {field: 'channelCode',displayName: '通道编码',width: 150,pinnable: false,sortable: false},
        {field: 'channelName',displayName: '通道名称',width: 150,pinnable: false,sortable: false},
        {field: 'channelStatus',displayName: '通道开关',width: 160,pinnable: false,sortable: false,
            cellFilter:"formatDropping:"+ angular.toJson($scope.statusSelect)},
        {field: 'action',displayName: '操作',width: 200,pinnedRight:true,sortable: false,editable:true,cellTemplate:
            '<a class="lh30" ui-sref="creditRepay.channelDetail({id:row.entity.id})" target="_black">详情</a>&nbsp;&nbsp;&nbsp;&nbsp;' +
            '<a class="lh30" ui-sref="creditRepay.channelEdit({id:row.entity.id})" target="_black">修改</a>'}
    ];

    $scope.query = function () {
    	if($scope.id==null){
    		$scope.id="";
		}
        $http({
            url: 'channel/selectChannelList?id='+$scope.id+'&pageNo='+$scope.paginationOptions.pageNo
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