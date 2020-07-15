/**
 * 风控问题修改
 */
angular.module('inspinia').controller('riskProblemUpCtrl',function($scope, $http, $state, $stateParams, i18nService,$filter) {

	i18nService.setCurrentLang('zh-cn');
	$scope.problemTypes=[{text:"调单",value:1},{text:"风控规则",value:2},{text:"其他",value:3}];
	$scope.info={statusRsik:1}
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
	
	$scope.riskProblemRecordGrid = {
			data:"recordDate",
	        columnDefs: [
                {field: 'realName',displayName: '审核人员',width: 180,pinnable: false,sortable: false},
                {field: 'auditStatus',displayName: '审核结果',width: 150,pinnable: false,sortable: false,cellFilter:"auditResult"},
                {field: 'auditTime',displayName: '审核时间',width: 160,pinnable: false,sortable: false,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
                {field: 'auditOpinion',displayName: '审核意见',width: 160,pinnable: false,sortable: false},
	        ]
	};
	
	
	$scope.commit=function(){
		$scope.submitting= true;
		$http.post('riskProblemAction/updateInfo',
			"info="+angular.toJson($scope.info),
			 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		.success(function(data){
			if(!data.bols){
				$scope.notice(data.msg);
				$scope.submitting=  false;
			}else{
				$scope.notice(data.msg);
				$state.transitionTo('risk.riskProblemMag',null,{reload:true});
				$scope.submitting= false;
			}
		})
		
	}
	$scope.commitAudit=function(){
		$scope.info.status=2;
		$http.post('riskProblemAction/updateInfo',
			"info="+angular.toJson($scope.info),
			 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		.success(function(data){
			if(!data.bols){
				$scope.notice(data.msg);
			}else{
				$scope.notice(data.msg);
				$state.transitionTo('risk.riskProblemMag',null,{reload:true});
			}
		})
	}
	
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