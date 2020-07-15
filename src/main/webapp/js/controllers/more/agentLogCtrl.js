/**
 * 日志管理
 */
angular.module('inspinia').controller('agentLogCtrl',function($scope,$http,$state,$stateParams,i18nService,$document){
	i18nService.setCurrentLang('zh-cn');
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	$scope.resetForm = function(){
		$scope.baseInfo = {
			agent_no: '', agent_name: '', method_desc: ''
			, oper_start_time: moment(new Date().getTime() - 6 * 24 * 60 * 60 * 1000).format('YYYY-MM-DD') + ' 00:00:00'
			, oper_end_time: moment(new Date().getTime()).format('YYYY-MM-DD') + ' 23:59:59'
			, ip: ''
		};
	}
	$scope.resetForm();
	$scope.logData=[];

	$scope.logGrid = {
		data: 'logData',
		paginationPageSize: 10,
		paginationPageSizes: [10, 20, 50, 100],
		useExternalPagination: true,		  	//开启拓展名
		columnDefs: [
		    {field: 'id',displayName:'日志编号'},
            {field: 'agent_no',  displayName: '代理商编号'},
            {field: 'agent_name',  displayName: '代理商名称'},
            {field: 'request_method', width:300, displayName: '请求方法'},
            {field: 'method_desc', displayName: '方法描述'},
            {field: 'request_params', displayName: '请求参数'},
            {field: 'return_result', displayName: '响应结果'},
            {field: 'oper_ip', displayName: 'IP'},
            {field: 'oper_time', displayName: '操作时间', cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
            {field: 'options',  displayName: '操作',pinnedRight:true, cellTemplate:
            	'<div class="lh30"><a ng-click="grid.appScope.logDetail(row.entity)">详情</a></div>'
            }
        ],
        onRegisterApi: function(gridApi){
        	$scope.gridApi = gridApi;
        	$scope.gridApi.pagination.on.paginationChanged($scope, function(newPage, pageSize){
        		$scope.paginationOptions.pageNo = newPage;
        		$scope.paginationOptions.pageSize = pageSize;
        		$scope.query();
        	});
        }
	};
	
	//查询
	$scope.query = function(){
		$scope.loadImg = true;
		$scope.submitting = true;
		$http.post('agentLog/queryByCondition',"baseInfo="+angular.toJson($scope.baseInfo)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+
			$scope.paginationOptions.pageSize,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
				.success(function(msg){
					$scope.loadImg = false;
					$scope.submitting = false;
					if(msg.status){
						$scope.logData = msg.page.result;
						$scope.logGrid.totalItems = msg.page.totalCount;
					} else {
						$scope.notice(msg.msg);
					}
				}).error(function(){
					$scope.loadImg = false;
					$scope.submitting = false;
				});
	}

	//详情
	$scope.logInfo = {};
	$scope.logDetail = function(msg){
		$scope.logInfo = msg;
		$('#logDetailModal').modal('show');
	}
	$scope.logDetail2 = function(id){
		$scope.logInfo = {};
		$('#logDetailModal').modal('show');
		$http.get('agentLog/queryDetail/' + id).success(function(msg){
			$scope.logInfo = msg;
		});
	}
	
	$scope.cancel = function(){
		$('#logDetailModal').modal('hide');
	}

	//页面绑定回车事件
	$document.bind("keypress", function(event) {
		$scope.$apply(function (){
			if(event.keyCode == 13){
				$scope.query();
			}
		})
	});
	
});