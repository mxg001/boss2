/**
 * 对账详情
 */
angular.module('inspinia').controller('billEneryDetailCtrl',function($scope,$http,$state,$stateParams,$compile,$filter,$log,$uibModal){
	//数据源
	$scope.id = $stateParams.id;
	$scope.info={};

	//订单类型
    $scope.orderTypeList = [{text:"全部",value:"0"},{text:"投保订单",value:"1"},{text:"退保订单",value:"2"}];
    $scope.insureStatusList = [{text:"投保成功",value:"SUCCESS"},{text:"投保失败",value:"FAILED"},{text:"初始化",value:"INIT"},{text:"已退保",value:"OVERLIMIT"},{text:'退保失败',value:'RECEDEFAILED'}];
    $scope.checkStatusList = [{text:"全部",value:"0"},{text:"核对成功",value:"1"},{text:"上游单边",value:"2"},{text:"平台单边",value:"3"},{text:"金额不符",value:"4"},{text:"未核对",value:"5"}];
	/**
	 * 
	 */
	$http.get('billEneryAction/billEneryDetail?id='+$scope.id)
		.success(function(data) {
			if(data.status){
				$scope.info = data.info;
			}else{
				$scope.notice(data.msg);
			}
		});
});