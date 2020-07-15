/**
 * 风控事件记录详情
 */
angular.module('inspinia').controller('riskEventRecordDetailCtrl',function($scope, $http, $state, $stateParams, i18nService,$filter) {

	i18nService.setCurrentLang('zh-cn');
	
	$scope.info={};

    $scope.handleStatus=[{text:"全部",value:-1},{text:"已处理",value:1},{text:"未处理",value:0}];
	$scope.handleResults=[{text:"全部",value:-1},{text:"安全",value:1},{text:"可疑",value:2},{text:"风险",value:3}];
    $scope.moreTimelistDate=[];

	$http.post('riskEventRecordAction/findRiskEventRecordById',"ids="+angular.toJson($stateParams.id),
			 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
	.success(function(data){
		if(!data.bols){
			
		}else{
			$scope.info=data.result;
            $scope.moreTimelistDate = data.myDate;
		}
	});

    $scope.showMoreTime=function(rollNo){
        $scope.blackLogRollNo=rollNo;
        $("#moreTimeDiv").modal('show');
    };

    $scope.showMoreTimeGrid = {
        data:"moreTimelistDate",
        useExternalPagination: false,//是否使用分页按钮
        enablePagination: false,
        enableHorizontalScrollbar: 0,////表格的水平滚动条
        enableVerticalScrollbar : 1,//表格的垂直滚动条 (两个都是 1-显示,0-不显示)
        enablePaginationControls: false, //使用默认的底部分页
        columnDefs: [
            {field: 'id',displayName: '序号',width: 250,pinnable: false,sortable: false,enableColumnMenu: false,enableSorting: false},
            {field: 'createTime',displayName: '触发时间',width: 250,pinnable: false,sortable: false,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',enableColumnMenu: false,enableSorting: false}
        ]
    };
});