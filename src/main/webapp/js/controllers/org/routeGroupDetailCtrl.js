angular.module('inspinia').controller("routeGroupDetailCtrl", function($scope, $http, $state, $stateParams){
	
	$scope.routeTypes = [{text:'销售专用',value:1},{text:'公司自建',value:2},{text:'技术测试',value:3}];
	$scope.statusAll = [{text:'正常',value:0},{text:'停用',value:1}];
	
	$http.post('routeGroup/getRouteGroup.do',
			angular.toJson({id:$stateParams.id})
	).success(function(data){
		if(data.agentNo == null || data.agentNo == ""){
			data.agentName = "全部";
		}
		$scope.info = data;
		$http.post('routeGroup/mapGroupSelect.do'
		).success(function(result){
	        if(result.status){
	        	 angular.forEach(result.data ,function(data,index,array){  
	        		 if($scope.info.mapGroupId==data.id){    
		                	$scope.searchField=data.group_code+data.group_name;
		                }     
	             });
	        }
	    }).error(function(){
	    });
//		$scope.getCities();
	}).error(function(){
	}); 

});