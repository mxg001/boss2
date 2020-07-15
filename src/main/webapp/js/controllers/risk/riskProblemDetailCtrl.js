/**
 * 风控问题详情
 */
angular.module('inspinia').controller('riskProblemDetailCtrl',function($scope, $http, $state, $stateParams, i18nService,$filter) {

	i18nService.setCurrentLang('zh-cn');
	$scope.statusRsiks=[{text:"待处理",value:1},{text:"待审核",value:2},{text:"审核通过",value:3},{text:"审核不通过",value:4}];
	$scope.problemTypes=[{text:"调单",value:1},{text:"风控规则",value:2},{text:"其他",value:3}];
	$scope.info={}
	$scope.recordDate=[{"auditPerson":""}];
	
	//查询
	$http.post('riskProblemAction/selectInfo',
			"ids="+angular.toJson($stateParams.id),
			 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
	.success(function(data){
		if(!data.bols){
			$scope.notice(data.msg);
			return;
		}
		$scope.info=data.result;
		$scope.recordDate=data.record;
	})
	
	$scope.riskProblemRecordGrids = {
			data:"recordDate",
			paginationPageSize:10,                  //分页数量
		    paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
		    useExternalPagination: true,
	        columnDefs: [
                {field: 'realName',displayName: '审核人员',width: 180,pinnable: false,sortable: false},
                {field: 'auditStatus',displayName: '审核结果',width: 150,pinnable: false,sortable: false,cellFilter:"auditResult"},
                {field: 'auditTime',displayName: '审核时间',width: 160,pinnable: false,sortable: false,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
                {field: 'auditOpinion',displayName: '审核意见',width: 160,pinnable: false,sortable: false},
	        ]
	};
	
}).filter('auditResult', function () {
	return function (value) {
		switch(value) {
			case 1 :
				return "通过";
				break;
			case 2 :
				return "不通过";
				break;
		}
	}
})