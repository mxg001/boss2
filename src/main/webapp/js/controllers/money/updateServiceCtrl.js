angular.module('inspinia').controller("updateServiceCtrl", function($scope, $http, $state, $stateParams){
	
//	$scope.serviceTypes = [{text:"单笔代付-自有资金",value:1},{text:"单笔代付-垫资",value:2},{text:"批量代付",value:3}];
	$scope.priorities = [{text:"A级",value:"1"},{text:"B级",value:"2"},{text:"C级",value:"3"},{text:"D级",value:"4"},{text:"E级",value:"5"}];
	
	$http.post('groupService/acqOrgSelectBox.do'
	).success(function(data){
		$scope.acqOrgs = data;
	}).error(function(){
	});
	
	$http.post('outAccountService/getServiceDetail.do',
			angular.toJson({serviceId:$stateParams.id})
	).success(function(res){
		$scope.serviceBaseInfo = res.serviceBaseInfo;
		$scope.serviceBaseInfo.addAmount = 0;
		$scope.serviceBaseInfo.isOverride = 0;
	}).error(function(){
	});
	
	$scope.save = function(){
		$scope.submitting =true;
		if($scope.serviceBaseInfo.isOverride==0){
			$scope.serviceBaseInfo.dayOutAccountAmount = new Number($scope.serviceBaseInfo.dayOutAccountAmount) + new Number($scope.serviceBaseInfo.addAmount);
		}else if($scope.serviceBaseInfo.isOverride==1){
			$scope.serviceBaseInfo.dayOutAccountAmount = $scope.serviceBaseInfo.addAmount;
		}
		$http.post('outAccountService/updateService.do',
				angular.toJson($scope.serviceBaseInfo)
		).success(function(msg){
			$scope.notice(msg.msg);
			$state.transitionTo('money.managerService',null,{reload:true});
			$scope.submitting =false;
		}).error(function(){
			$scope.submitting =false;
		});
	}
	
});