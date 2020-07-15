/**
 * 风控问题审核管理
 */
angular.module('inspinia').controller('riskProblemAuditCtrl',function($scope, $http, $state, $stateParams, i18nService,$document,$filter) {

	i18nService.setCurrentLang('zh-cn');
	$scope.info={problemType:-1};
	$scope.problemTypes=[{text:"全部",value:-1},{text:"调单",value:1},{text:"风控规则",value:2},{text:"其他",value:3}];
	$scope.riskProblemAuditDate=[]
	
	//查询
	$scope.query=function(){
		$http.post('riskProblemAction/selectAuditAllInfo',
				"info="+angular.toJson($scope.info)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
				 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		.success(function(data){
			if(!data.bols){
				$scope.notice("查询失败");
				return;
			}
			$scope.riskProblemAuditDate =data.page.result; 
			$scope.riskProblemAuditGrid.totalItems = data.page.totalCount;
		})
	}
	$scope.query();
	//清空
	$scope.reset = function() {
		$scope.info={problemType:-1,problemTitle:"",problemId:"",dealPerson:""}
	};
	
	$scope.paginationOptions=angular.copy($scope.paginationOptions);	
	$scope.riskProblemAuditGrid = {
			data:"riskProblemAuditDate",
	        paginationPageSize:10,                  //分页数量
	        paginationPageSizes: [10,20,50,100],	  //切换每页记录数
	        useExternalPagination: true,		  //开启拓展名
	        columnDefs: [
                {field: 'problemId',displayName: '问题编号',pinnable: false,sortable: false},
                {field: 'problemType',displayName: '问题分类',pinnable: false,sortable: false,
                	cellFilter:"problemTypeRisk"
                },
                {field: 'riskRulesNo',displayName: '风控规则ＩＤ',pinnable: false,sortable: false},
                {field: 'problemTitle',displayName: '问题标题',pinnable: false,sortable: false},
	            {field: 'problemId',displayName: '操作',pinnable: false,sortable: false,editable:true,
                	cellTemplate:
                		"<a class='lh30' ng-show='grid.appScope.hasPermit(\"riskProblem.insert\")'  ui-sref='risk.riskProblemAuditResult({id:row.entity.problemId})'> 审核</a>"
	            }
	        ],
			onRegisterApi: function(gridApi) {                
		         $scope.gridApi = gridApi;
		         gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
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
}).filter('problemTypeRisk', function () {
	return function (value) {
		switch(value) {
			case 1 :
				return "调单";
				break;
			case 2 :
				return "风控规则";
				break;
			case 3 :
				return "其他";
				break;
		}
	}
})