/**
 * 公告详情
 */
angular.module('inspinia').controller('msgDetailCtrl',function($scope,$http,$state,$stateParams){
	$scope.statusType = [{text:"失效",value:"0"},{text:"生效",value:"1"}];
	
	
	 //模块名
    $scope.modelTypeList=[];
    $http.post("sysDict/getListByKey.do?sysKey=MODELTYPE")
        .success(function(data){
            //响应成功
            for(var i=0; i<data.length; i++){
                $scope.modelTypeList.push({value:data[i].sysValue,text:data[i].sysName});
            }
        });

    $scope.msgTypeList=[];
    $http.post("sysDict/getListByKey.do?sysKey=MSGTYPE")
        .success(function(data){
            //响应成功
            for(var i=0; i<data.length; i++){
                $scope.msgTypeList.push({value:data[i].sysValue,text:data[i].sysName});
            }
        });
	
	
	$http.post("msg/msgDetail","id="+$stateParams.id,
        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function(data){
            if(data.status){
                $scope.info=data.info;
            }else{
            	$scope.notice(data.msg);
            }
        });
});