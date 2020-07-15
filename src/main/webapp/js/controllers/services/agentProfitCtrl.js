/**
 * 代理商分润设置默认值
 */
angular.module('inspinia').controller("agentProfitCtrl", function($scope, $http, $state,$timeout, $stateParams,$uibModal) {
	
	$scope.fr=[{text:'每笔固定收益金额',value:1},{text:'每笔收益率',value:2},{text:'每笔收益率带保底封顶',value:3},{text:'每笔收益率+每笔固定收益金额',value:4},
	    		{text:'成本价+固定分润比',value:5},{text:'成本价+阶梯分润比',value:6}];
	$scope.ladderRate =[];
	$scope.linkServices = [];
	$scope.serviceInfo=[];
	$scope.serviceInfo1=[];
	$scope.queryDetail=function(serviceId){
		$http.post("service/queryAgentProfit",serviceId, {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		.success(function(data){
    		if(data!=null){
    			$scope.serviceInfo = data;
    		}
    	})
    	.error(function(data){
    	});
	};
    $scope.agentProfitList = {
        data: 'serviceInfo',
        columnDefs: [
         			{field: 'serviceName',displayName: '服务名称',width: 150},
         			{field: 'cardType',displayName: '银行卡种类',width: 150,cellFilter:"formatDropping:"+$scope.cardTypeStr},
         			{field: 'holidaysMark',displayName: '节假日标志',width: 150,cellFilter:"formatDropping:"+$scope.holidaysStr},
         			{field: 'profitType',displayName: '分润方式',width: 300,
         				cellTemplate: '<select class="input-sm form-control input-s-sm inline" style="width:98%;height:98%; margin:0 1%;" ng-model="row.entity.profitType" ng-options="x.value as x.text for x in grid.appScope.fr"/>'
         			},
         			{field: 'income',displayName: '代理商收益',width: 150,cellTemplate:
         				'<div ng-switch="$eval(\'row.entity.profitType\')" style="height:98%;">'
         				+'<div ng-show="row.entity.profitType==1"  ng-switch-when="1" style="height:98%;">'
         				+'<input type="text" style="width:98%;height:98%; margin:0 1%;" class="ui-widget input" ng-readonly="false" ng-model="row.entity[col.field]"/>'
         				+'</div>'
         				+'<div ng-show="row.entity.profitType==2" ng-switch-when="2" style="height:98%;">'
         				+'<input type="text" style="width:98%;height:98%; margin:0 1%;" class="ui-widget input" ng-readonly="false" ng-model="row.entity[col.field]"/>'
         				+'</div>'
         				+'<div ng-switch-when="3" style="height:98%;">'
         				+'<input type="text" style="width:98%;height:98%; margin:0 1%;" class="ui-widget input" ng-readonly="false" ng-model="row.entity[col.field]"/>'
         				+'</div>'
         				+'<div ng-switch-when="4" style="height:98%;">'
         				+'<input type="text" style="width:98%;height:98%; margin:0 1%;" class="ui-widget input" ng-readonly="false" ng-model="row.entity[col.field]"/>'
         				+'</div>'
         				+'</div>'
         			},
         			{field: 'cost',displayName: '代理商成本',width: 130,
         				cellTemplate:'<div ng-switch="$eval(\'row.entity.profitType\')" style="height:98%;">'
         				+'<div ng-switch-when="5">'
         				+'<input type="text" style="width:98%;height:98%;" class="ui-widget input" ng-readonly="false" ng-model="row.entity[col.field]"/>'
         				+'</div>'
         				+'<div ng-switch-when="6">'
         				+'<input type="text" style="width:98%;height:98%;" class="ui-widget input" ng-readonly="false" ng-model="row.entity[col.field]"/>'
         				+'</div>'
         				+'</div>'
         			},
         			{field: 'shareProfitPercent',displayName: '代理商固定分润百分比',width: 180,
         				cellTemplate:'<div ng-switch="$eval(\'row.entity.profitType\')" style="height:98%;">'
         				+'<div ng-switch-when="5">'
         				+'<input type="text" style="width:90%;height:98%;" class="ui-widget input" ng-readonly="false" ng-model="row.entity[col.field]"/>%'
         				+'</div>'
         				+'</div>'
         			},
         			{field: 'aaa',displayName: '阶梯分润设置',width: 130,
         				cellTemplate: '<div ng-switch="$eval(\'row.entity.profitType\')">'
         				+'<div ng-switch-when="6">'
         				+'<button ng-click="grid.appScope.ladderFr(row)" class="btn btn-primary" type="button" >阶梯分润</button>'
         				+'</div>'
         				+'</div>'
         			},
         			{field: 'ladderRate',displayName: '阶梯分润比例',width: 250}
         		]
    };
	
    $scope.serviceInfo=[];
    $scope.commitData=function(){
    	//获取选中业务产品的ID数组
//    	var products = $scope.gridApiProduct.selection.getSelectedRows();
//    	var bpIds = [];
//    	if(products.length >0 ){
//    		for(var i=0; i<products.length; i++){
//    			bpIds[i] = products[i].bpId;
//    		}
//    	}
    	var data = {"serviceInfo":$scope.serviceInfo};
    	$http.post('service/saveAgentProfit',angular.toJson(data)).success(function(msg){
    		$scope.subDisable = false;
    		if(msg.status){
    			$scope.notice(msg.msg);
    			$state.transitionTo('service.queryService',null,{reload:true});
    		}else{
    			$scope.notice(msg.msg,{classes:"alert-danger"}); 
    		}
    		$scope.subDisable = false;
    	}).error(function(){
    		$scope.notice("代理商分润设置失败！",{classes:"alert-danger"});
    		$scope.subDisable = false;
    	});
    	
    };
    $scope.queryDetail("serviceId="+$stateParams.serviceId);
    
    var ladderRateDate = {m1:0,m2:"",m3:"",m4:"",m5:"",m6:"",m7:"",m8:"",m9:"无穷大"};
    $scope.solutionModalClose=function(){
		 $scope.modalInstance.dismiss();
	 }
	 
	 $scope.solutionModalOk=function(){
		 $scope.modalInstance.close($scope);
	 }
	$scope.ladderFr=function(row){
		var modalScope = $scope.$new();
		modalScope.info=ladderRateDate;
		var modalInstance = $uibModal.open({
           templateUrl : 'views/agent/ladderFrModal.html',  //指向上面创建的视图
           controller : 'agentProfitCtrl',// 初始化模态范围
           scope:modalScope,
           size:'lg'
       })
       modalScope.modalInstance=modalInstance;
       modalInstance.result.then(function(selectedItem){ 
		   var info=selectedItem.info;
		   ladderRateDate = selectedItem.info;
		   row.entity.ladderRate=info.m2+"%<"+info.m3+"<"+info.m4+"%<"+info.m5+"<"+info.m6+"%<"+info.m7+"<"+info.m8+"%";
       },function(){
           $log.info('取消: ' + new Date())
       })
		
	}
});

