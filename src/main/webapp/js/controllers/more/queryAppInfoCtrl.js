/**
 * 查询banner
 */
angular.module('inspinia').controller("queryAppInfoCtrl",function($scope, $http, $state, $stateParams,$filter,i18nService,$document) {
	$scope.team = [];
	$scope.agent = [];
	$scope.query = {teamId : '0',agentNo : '0'};
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	// 数据源
	$http.get('teamInfo/queryTeamAndOneAgent.do').success(function(msg) {
		$scope.team.push({value : '0',text : '全部'});
		for (var i = 0; i < msg.teamInfo.length; i++) {
			$scope.team.push({value : msg.teamInfo[i].teamId,text : msg.teamInfo[i].teamName});
		}
		$scope.agent.push({value : '0',text : '全部'});
		for (var i = 0; i < msg.allAgent.length; i++) {
			$scope.agent.push({value : msg.allAgent[i].agentNo,text : msg.allAgent[i].agentName});
		}
	}).error(function() {
	});

	$scope.queryFunc = function() {
		$scope.query.pageNo = $scope.paginationOptions.pageNo;
		$scope.query.pageSize = $scope.paginationOptions.pageSize;
		$http.post('appInfo/selectByCondition.do', {"query":$scope.query}).success(function(msg) {
			$scope.appInfo = msg.result;
			$scope.appInfoGrid.totalItems = msg.totalCount;
		}).error(function() {
		})
	};
	
	$scope.appInfoGrid = {
        data: 'appInfo',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	  //切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs: [
            {field: 'id',displayName: 'ID',pinnable: false,sortable: false},
            {field: 'teamName',displayName: '组织名称',pinnable: false,sortable: false},
            {field: 'agentName',displayName: '一级代理商名称',pinnable: false,sortable: false},
            {field: 'action',displayName: '操作',pinnable: false,sortable: false,editable:true,cellTemplate:
            	"<div class='lh30'><a ng-show='grid.appScope.hasPermit(\"appInfo.detail\")'  ui-sref='sys.appInfoDetail({id:row.entity.id})'>详情</a>" +
            	"<a ng-show='grid.appScope.hasPermit(\"appInfo.update\")' ui-sref='sys.modifyAppInfo({id:row.entity.id})'> | 修改</a></div>"}
        ],
        onRegisterApi: function(gridApi) {                
            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
            	$scope.paginationOptions.pageNo = newPage;
            	$scope.paginationOptions.pageSize = pageSize;
            	$scope.queryFunc();
            });
        }
	 };
	$scope.queryFunc();
	//reset
	$scope.resetForm=function(){
		$scope.query= {teamId : '0',agentNo : '0'};
	}

	//页面绑定回车事件
	$document.bind("keypress", function(event) {
		$scope.$apply(function (){
			if(event.keyCode == 13){
				$scope.queryFunc();
			}
		})
	});
});
