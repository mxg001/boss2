angular.module('inspinia').controller('blacklistAddCtrl',function($scope, $http, $state, $stateParams, i18nService,$filter) {

	i18nService.setCurrentLang('zh-cn');
	$scope.info={rollType:1,rollBelong:2,rollMsg:"",rollUser:"1"};
	$scope.name="商户编号";
//	$scope.blacklistTypes=[{text:"商户身份证",value:2},{text:"卡号",value:3},{text:"商户编号",value:1}]
	
	$scope.commit=function(){
		$scope.submitting = true;
		if($scope.info.rollNo==""||$scope.info.rollNo==null){
			$scope.notice("请填写完整的信息");
			$scope.submitting = false;
			return;
		}
		$http.post('riskRollAction/addRollInfo',
				"info="+angular.toJson($scope.info),
				 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		.success(function(data){
			if(!data.bols){
				$scope.notice(data.msg);
				$scope.submitting = false;
			}else{
				$scope.notice(data.msg);
				$state.transitionTo('risk.blacklistQuery',null,{reload:true});
				$scope.submitting = false;
			}
		})
	}


	//查询代理商提示语
	$scope.queryAgentTips = function(){
		$http.post('msg/queryAgentTips',null,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			.success(function(result){
				if(result.status){
					$scope.agentTips = result.data;
					$scope.agentTips.unshift({msgCode:"",userMsg:"请选择"});
				}else{
					$scope.notice(result.msg);
				}
			}).error(function(){
		});
	}

	$scope.queryAgentTips();
	
	$scope.rollTypeChange = function(x) {

		if(x==8 || x==9 || x==10){
			$scope.info.rollUser = "2";
		}else{
			$scope.info.rollUser = "1";
		}

		if (x == 1 || x == 4) {
			$scope.name = "商户编号";
		} else if (x == 2) {
			$scope.name = "身份证号";
		} else if (x == 3) {
			$scope.name = "银行卡号";
		} else if (x == 7){
			$scope.name = "商户编号";
		}else if (x == 8){
			$scope.name = "代理商身份证号";
		}else if (x == 9){
			$scope.name = "代理商结算卡号";
		}else if (x == 10){
			$scope.name = "代理商手机号";
		}
		
	}
})