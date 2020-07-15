/**
 */
angular.module('inspinia').controller('msgAddCtrl',function($scope, $http, $state, $stateParams, i18nService,$filter) {

	i18nService.setCurrentLang('zh-cn');

	  //清空查询条件
    $scope.resetForm = function(){
    	$scope.info={msgType:"1",moduleName:"A",status:"1",sourceOrg:""};
    }

    $scope.resetForm();
	
	
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

    
    //收单机构
    $http.post('groupService/acqOrgSelectBox.do'
	).success(function(data){
		$scope.acqOrgs = data;
		$scope.acqOrgs.splice(0,0,{"acqName":"全部","acqEnname":""});
	}).error(function(){
	}); 
    
	$scope.commit=function(){
		$scope.submitting = true;
		$http.post('msg/addMsg',
				"info="+angular.toJson($scope.info),
				 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		.success(function(data){
			if(!data.status){
				$scope.notice(data.msg);
               // $state.transitionTo('sys.msgAdd',null,{reload:true});
				$scope.submitting = false;
			}else{
				$scope.notice(data.msg);
				$scope.submitting = false;
				$state.transitionTo('sys.msgAdd',null,{reload:true});
				
			}
		})
	}
	
})