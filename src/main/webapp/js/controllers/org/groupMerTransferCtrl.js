/**
 * 集群中普通商户转移
 */
angular.module('inspinia',['infinity.angular-chosen']).controller('groupMerTransfer',function($scope,$http,i18nService,$document,$timeout){
	i18nService.setCurrentLang('zh-cn');
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	//清空
	$scope.reset = function() {
		$scope.info={status:"",agentNode:"",serviceType:"",groupProvince:"全部",groupCity:"全部"};
	};
	$scope.reset();
	$scope.merData = [];
	$scope.agent=[{value:"",text:"全部"}];
	$scope.statusAll = [{text:'全部',value:''},{text:'正常',value:0},{text:'停用',value:1}];
	$scope.newServiceTypes = [{text:'全部',value:''}];
	angular.forEach($scope.serviceTypes,function(data){
		if(data.value!=10000&&data.value!=10001){
			$scope.newServiceTypes.push(data);
		}
	})
	//获取所有的收单机构
	$http.post('routeGroup/acqOrgSelectBox.do'
	).success(function(data){
		$scope.acqOrgList = data;
		// $scope.acqOrgList.splice(0,0,{"acqName":"-请选择-","id":""});
		if(data != null && data.length > 0){
            $scope.info.acqId = data[0].id;
		}
		$scope.getAcqServiceList();
	}).error(function(){
		 
	}); 
	//获取收单机构对应的收单服务
	
	$scope.getAcqServiceList = function(){
		$scope.acqServiceList = [{"serviceName":"-请选择-",id:""}];
		if($scope.info.acqId!=""){
			$http.post('routeGroup/acqServiceSelectBox.do',
					angular.toJson({"acqId":$scope.info.acqId})
			).success(function(data){
				$scope.acqServiceList = data;
				$scope.acqServiceList.unshift({"serviceName":"-请选择-",id:""});
				if(data != null && data.length > 0){
					$scope.info.acqServiceId = data[0].id;
				}
			}).error(function(){
				 
			}); 
		}
	}
	//获取所有的一级代理商
	$scope.getStates =getStates;
	var oldValue="";
	var timeout="";
	function getStates(value) {
		$scope.agentt = [];
			var newValue=value;
			if(newValue != oldValue){
				if (timeout) $timeout.cancel(timeout);
				timeout = $timeout(
					function(){
						$http.post('agentInfo/selectAllInfo','item=' + value,
								{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
							.then(function (response) {
								if(response.data.length==0) {
									$scope.agentt.push({value: "", text: "全部"});
								}else{
									$scope.agentt.push({value: "", text: "全部"});
									for(var i=0; i<response.data.length; i++){
										$scope.agentt.push({value:response.data[i].agentNode,text:response.data[i].agentName});
									}
								}
								$scope.agent = $scope.agentt;
								oldValue = value;
							});
					},800);
			}
	};
	//代理商
	 $http.post("agentInfo/selectAllInfo")
  	 .success(function(msg){
  			//响应成功
  	   	for(var i=0; i<msg.length; i++){
  	   		$scope.agent.push({value:msg[i].agentNode,text:msg[i].agentName});
  	   	}
  	});
	//获取省份
	$http.post('areaInfo/getAreaByName.do',"type=p&&name=''",{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
	.success(function(data){
		$scope.provinceList = data;
		$scope.provinceList.splice(0,0,{"name":"全部"});
		$scope.getCities();
		}).error(function(){
		 
	});
		
	$scope.getCities = function() {
		if($scope.info.groupProvince=='全部'){
			$scope.cityList = [{"name":"全部"}];
			$scope.info.groupCity = '全部';
			return ;
		}
		$http.post('areaInfo/getAreaByName.do',"type=''&&name="+$scope.info.groupProvince,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		.success(function(data){
			$scope.cityList = data;
			$scope.cityList.splice(0,0,{"name":"全部"});
		}).error(function(){
		});
		}
	//查询
	$scope.query=function(){
		if($scope.info.acqId == ""){
			$scope.notice("请选择收单机构");
			return;
		}
        if($scope.info.acqServiceId == ""){
            $scope.notice("请选择收单机构服务");
            return;
        }
		$scope.submitting = true;
        $scope.loadImg = true;
		var info=$scope.info;
		$http.post('groupMerTransferAction/selectByParam',
				"info="+angular.toJson(info)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
				 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		.success(function(result){
            $scope.submitting = false;
            $scope.loadImg = false;
            if(result.status){
                $scope.merData = result.data.result;
                $scope.merGrid.totalItems = result.data.totalCount;
			} else {
				$scope.notice(result.msg);
			}
		})
	}
	// $scope.query();
	$scope.merGrid = {
			data:"merData",
			paginationPageSize:10,                  //分页数量
	        paginationPageSizes: [10,50,100,500,1000],	  //切换每页记录数
	        useExternalPagination: true,		  //开启拓展名
	        columnDefs: [
	                     {field:'merchantNo',displayName:'商户编号'},
	                     {field:'merchantName',displayName:'商户名称'},
	                     {field:'serviceType',displayName:'商户服务类型',cellFilter:'formatDropping:'+angular.toJson($scope.serviceTypes)},
	                     {field:'agentName',displayName:'代理商名称'},
	                     {field:'groupCode',displayName:'集群编号'},
	                     {field:'groupName',displayName:'集群名称'},
	                     {field:'province',displayName:'商户所属省份'},
	                     {field:'city',displayName:'商户所属城市'}
	                     ],
	         onRegisterApi: function(gridApi) {                
		         $scope.gridApi = gridApi;
		         gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
		         	$scope.paginationOptions.pageNo = newPage;
		         	$scope.paginationOptions.pageSize = pageSize;
		            $scope.query();
	             });
	      }
	}
	
	//转移集群modal
	$scope.transferGroup = function(){
//		$scope.merData = $scope.merData;
		if($scope.merData.length<1){
			$scope.notice("请选择需要转移的商户");
			return;
		}
		var selectList = $scope.gridApi.selection.getSelectedRows();
		if(selectList.length>=1){
			$scope.allData =selectList;
			$scope.merSum = selectList.length;
		}else{
			var info=$scope.info;
			$http.post('groupMerTransferAction/selectByParam',
				"info="+angular.toJson(info)+"&pageNo="+1+"&pageSize="+99999999,
				{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
				.success(function(data){
					$scope.allData = data.result;
					$scope.merSum = data.totalCount;
				});
		}
		$scope.groupNameShow = false;
		$scope.groupNameMsgShow = false;
		$scope.groupCode = null;
		$scope.groupName = null;
		$('#transferModal').modal('show');
	}
	//获取集群名称，根据集群编码
	$scope.getGroupName = function(){
		$scope.groupNameShow = false;
		$scope.groupNameMsgShow = false;
		$scope.groupName = null;
		$http.get("routeGroup/getGroupByCode?groupCode=" + $scope.groupCode).success(function(msg){
			if(msg!=null && msg!=""){
				$scope.groupNameShow = true;
				$scope.groupName = msg.groupName;
			} else {
				$scope.groupNameMsgShow = true;
				$scope.groupNameMsg = "集群不存在,请重新输入";
			}
			
		});
	}
	//将当前查询条件下的所有数据，提交转移集群
	$scope.transferGroupSubmit = function(){
		$scope.submitting = true;
		var data = {"groupCode":$scope.groupCode,"merList":$scope.allData};
		$http.post("groupMerTransferAction/transferMer",angular.toJson(data)).success(function(msg){
			if(msg.status){
				$scope.cancel();
			}
			$scope.notice(msg.msg);
			$scope.submitting = false;
		});
	}
	//取消
	$scope.cancel = function(){
		$('#transferModal').modal('hide');
	}

	//页面绑定回车事件
	$document.bind("keypress", function(event) {
		$scope.$apply(function (){
			if(event.keyCode == 13){
				$scope.query();
			}
		})
	});
	
});