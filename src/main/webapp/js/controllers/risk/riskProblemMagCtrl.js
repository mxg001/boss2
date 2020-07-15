/**
 * 风控问题管理
 */
angular.module('inspinia').controller('riskProblemMagCtrl',function($scope, $http, $state, $stateParams, i18nService,$document,$filter) {

	i18nService.setCurrentLang('zh-cn');
	$scope.info={status:-1,problemType:-1,dealPerson:""}
	$scope.statusRsiks=[{text:"全部",value:-1},{text:"待处理",value:1},{text:"待审核",value:2},{text:"审核通过",value:3},{text:"审核不通过",value:4}];
	$scope.problemTypes=[{text:"全部",value:-1},{text:"调单",value:1},{text:"风控规则",value:2},{text:"其他",value:3}];
	$scope.riskProblemMagDate=[]
	$scope.dealPersons=[];
	
	$http.get('user/findUserBox.do')
	.success(function(largeLoad) {
		if(!largeLoad)
			return
		$scope.dealPersons=largeLoad;
		$scope.dealPersons.splice(0,0,{id:"",realName:"全部"});
	});
	
	//查询
	$scope.query=function(){
		$http.post('riskProblemAction/selectAllInfo',
				"info="+angular.toJson($scope.info)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
				 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		.success(function(data){
			if(!data.bols){
				$scope.notice("查询失败");
				return;
			}
			$scope.riskProblemMagDate =data.page.result; 
			$scope.riskProblemMagGrid.totalItems = data.page.totalCount;
		})
	}
	$scope.query();
	//清空
	$scope.reset = function() {
		$scope.info={status:-1,problemType:-1,problemTitle:"",problemId:"",dealPerson:""}
	};
	
	$scope.paginationOptions=angular.copy($scope.paginationOptions);	
	$scope.riskProblemMagGrid = {
			data:"riskProblemMagDate",
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
                {field: 'status',displayName: '状态',pinnable: false,sortable: false,
                	cellFilter:"stutasTypeRisk"
                },
	            {field: 'problemId',displayName: '操作',width: 250,pinnable: false,sortable: false,editable:true,
                	cellTemplate:
                		"<div class='lh30' ng-show='row.entity.status==1 || row.entity.status==4'>" +
                			"<a ng-show='grid.appScope.hasPermit(\"riskProblem.detail\")'  ui-sref='risk.riskProblemDetail({id:row.entity.problemId})'> 详情  |  </a>" +
                			"<a ng-show='grid.appScope.hasPermit(\"riskProblem.update\")'  ui-sref='risk.riskProblemUp({id:row.entity.problemId})'> 修改  | </a>" +
                			"<a ng-show='grid.appScope.hasPermit(\"riskProblem.result\")'  ui-sref='risk.riskFeedback({id:row.entity.problemId})'> 处理结果反馈 </a>" +
                		"</div>"+
                		"<div  class='lh30' ng-show='row.entity.status!=1 && row.entity.status!=4'>" +
	            			"<a ng-show='grid.appScope.hasPermit(\"riskProblem.detail\")'  ui-sref='risk.riskProblemDetail({id:row.entity.problemId})'> 详情</a>" +
            			"</div>"
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
}).filter('stutasTypeRisk', function () {
	return function (value) {
		switch(value) {
			case 1 :
				return "待处理";
				break;
			case 3 :
				return "审核通过";
				break;
			case 2 :
				return "待审核";
				break;
			case 4 :
				return "审核不通过";
				break;
		}
	}
})