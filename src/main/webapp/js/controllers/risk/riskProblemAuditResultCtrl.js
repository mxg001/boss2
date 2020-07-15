/**
 * 风控问题审核结果
 */
angular.module('inspinia').controller('riskProblemAuditResultCtrl',function($scope, $http, $state, $stateParams, i18nService,$filter) {

	i18nService.setCurrentLang('zh-cn');
	$scope.problemTypes=[{text:"调单",value:1},{text:"风控规则",value:2},{text:"其他",value:3}];
	$scope.info={};
	$scope.auditInfo={auditPerson:_principal.realName,auditStatus: 2};
	$scope.recordGrid=[{"auditPerson":""}];
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
		$scope.recordGrid =data.record; 
	})
	$scope.riskProblemRecordGrid = {
			data:"recordGrid",
	        columnDefs: [
                {field: 'realName',displayName: '审核人员',width: 180,pinnable: false,sortable: false},
                {field: 'auditStatus',displayName: '审核结果',width: 150,pinnable: false,sortable: false,cellFilter:"auditResult"},
                {field: 'auditTime',displayName: '审核时间',width: 160,pinnable: false,sortable: false,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
                {field: 'auditOpinion',displayName: '审核意见',width: 160,pinnable: false,sortable: false},
	        ]
	};
	
	$scope.commit=function(){
		$scope.submitting = true;
		if($scope.auditInfo.auditStatus=="1"){
			$scope.info.status=3
		}else{
			$scope.info.status=4
		}
		$scope.auditInfo.problemId=$scope.info.problemId;
		var data={"info":$scope.info,"auditInfo":$scope.auditInfo};
		$http.post('riskProblemAction/addAuditInfo',
			"info="+angular.toJson(data),
			 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		.success(function(data){
			if(!data.bols){
				$scope.notice(data.msg);
				$scope.submitting = false;
			}else{
				$scope.notice(data.msg);
				$state.transitionTo('risk.riskProblemAudit',null,{reload:true});
				$scope.submitting = false;
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