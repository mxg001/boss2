/**
 * 增加路由集群
 */
angular.module('inspinia').controller("addRouteGroupCtrl", function($scope, $http, $state, $stateParams){
	
	$scope.routeTypes = [{text:'销售专用',value:1},{text:'公司自建',value:2},{text:'技术测试',value:3}];
	
	$scope.reset = function(){
		$scope.info = {"mySettle":0,"routeType":1,"agentNo":"","groupProvince":"全部","groupCity":"不限"};
	}
	$scope.reset();
	$scope.newServiceTypes = [];
	angular.forEach($scope.serviceTypes,function(data){
		if(data.value!=10000&&data.value!=10001){
			$scope.newServiceTypes.push(data);
		}
	})
	$http.post('routeGroup/acqOrgSelectBox.do'
	).success(function(data){
		$scope.acqOrgs = data;
	}).error(function(){
	}); 
	
	$scope.queryByService = function() {
		$http.post('routeGroup/acqServiceSelectBox.do',
				angular.toJson({"acqId":$scope.info.acqId})
		).success(function(data){
			$scope.acqServices = data;
		}).error(function(){
		}); 
		angular.forEach($scope.acqOrgs,function(data){
			if($scope.info.acqId==data.id){
				$scope.info.defAcqDayAmount = data.acqDefDayamount;
			}
		});
	}
	
	$http.post('routeGroup/oneLevelAgentSelectBox.do'
	).success(function(data){
		$scope.agentInfos = data;
		$scope.agentInfos.splice(0,0,{"agentName":"全部","agentNo":""});
	}).error(function(){
	}); 
	
	$http.post('areaInfo/getAreaByName.do',"type=p&&name=''",{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			.success(function(data){
		$scope.provinceInfos = data;
		$scope.provinceInfos.splice(0,0,{"name":"全部"});
		$scope.getCities();
	}).error(function(){
	}); 
	
	$scope.getCities = function() {
		if($scope.info.groupProvince=='全部'){
			$scope.cities = [{"name":"不限"}];
			$scope.info.groupCity = '不限';
			return ;
		}
		$http.post('areaInfo/getAreaByName.do',"type=''&&name="+$scope.info.groupProvince,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		.success(function(data){
			$scope.cities = data;
			$scope.cities.splice(0,0,{"name":"不限"});
		}).error(function(){
		});
	}
	
	$scope.save = function() {
		$scope.submitting = true;
		if($scope.info.allowTransStartTime>$scope.info.allowTransEndTime){
			$scope.notice("允许交易的起始时间要小于终止时间")
			$scope.submitting = false;
			return;
		}
		angular.forEach($scope.acqServices,function(data){
			if($scope.info.acqServiceId==data.id){
				$scope.info.acqServiceType = data.serviceType;
			}
		})
		angular.forEach($scope.acqServiceTypes,function(data,index){
			if($scope.info.acqServiceType == data.value){
				$scope.info.serviceName = data.text;
				$scope.submitting = false;
				return;
			}
		});
		$http.post('routeGroup/addRouteGroup.do',
       		 angular.toJson($scope.info)
        ).success(function(msg){
            if(msg.status){
            	$state.transitionTo('org.addRouteGroup',null,{reload:true});
				$scope.submitting = false;
            }
			$scope.notice(msg.msg);
			$scope.submitting = false;
        }).error(function(){
			$scope.submitting = false;
        }); 
	};
	$scope.datas=[];
	$http.post('routeGroup/mapGroupSelect.do'
	).success(function(result){
        if(result.status){
        	$scope.datas=result.data;
        	$scope.tempdatas = $scope.datas; //下拉框选项副本    
        }
    }).error(function(){
    }); 
	
    $scope.closeGroupSelect=true;//选择框是否隐藏    
    $scope.searchField='';//文本框数据    
    
    $scope.closeSelect=function(){
    	if($scope.closeGroupSelect){
    		$scope.closeGroupSelect=false;
    	}else{
    		$scope.closeGroupSelect=true;
    	}
    }
    //将下拉选的数据值赋值给文本框    
    $scope.click=function(x){  
        $scope.searchField=x; 
        document.getElementById("searchField").value=x; 
        var forData = '';
        angular.forEach($scope.tempdatas ,function(data,index,array){  
        	forData = data.group_code+data.group_name;
            if(forData==x){    
            	$scope.info.mapGroupId=data.id;
            }    
        });
        $scope.changeKeyValue(x);
        $scope.closeGroupSelect=true;    
    }    
    //获取的数据值与下拉选逐个比较，如果包含则放在临时变量副本，并用临时变量副本替换下拉选原先的数值，如果数据为空或找不到，就用初始下拉选项副本替换    
    $scope.changeKeyValue=function(v){  
        var newData=[];  //临时下拉选副本    
        //如果包含就添加    
        var forData = '';
        angular.forEach($scope.tempdatas ,function(data,index,array){  
        	forData = data.group_code+data.group_name;
            if(forData.indexOf(v)>=0){    
            	newData.unshift(data);    
            }    
        });    
        //用下拉选副本替换原来的数据    
        $scope.datas=newData;    
        //下拉选展示    
        //如果不包含或者输入的是空字符串则用初始变量副本做替换    
        if($scope.datas.length==0 || ''==v){    
            $scope.datas=$scope.tempdatas;    
        }
        $scope.closeGroupSelect=false;
        
    }    
});