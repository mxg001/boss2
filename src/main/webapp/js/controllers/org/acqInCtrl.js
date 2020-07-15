/**
 * 收单商户进件
 */
angular.module('inspinia',['angularFileUpload','uiSwitch', 'infinity.angular-chosen']).controller('acqInCtrl',function($scope, $http, $state,$timeout, $stateParams, i18nService,$filter,FileUploader,SweetAlert,$document) {

	i18nService.setCurrentLang('zh-cn');
	
	// `merchant_type` int(11) NOT NULL COMMENT '进件类型:1个体收单商户，2-企业收单商户',
	$scope.merchantTypes=[{text:"全部",value:-1},{text:"个体收单商户",value:1},{text:"企业收单商户",value:2}];
	// `audit_status` int(11) NOT NULL COMMENT '审核状态 1.正常 2.审核通过 3 审核不通过',
	$scope.auditStatuss=[{text:"全部",value:-1},{text:"待审核",value:1},{text:"审核通过",value:2},{text:"审核不通过",value:3}];
	

	
	$scope.info={acqStatus:-1,largeSmallFlag:-1,agentNo:-1,acqOrgId:-1,stratRemaimAmount:"",endRemaimAmount:"",acqMerchantType:-1};
	$scope.agent=[{value:-1,text:"全部"}];
	$scope.agentone=[{value:-1,text:"全部"}];
	
	$scope.acqMerchantTypeString=angular.toJson($scope.acqMerchantTypes);
	
	
	//进件来源
	$scope.itemSourceSelect=[{text:"全部",value:""}];
	    $http.post("sysDict/getListByKey.do?sysKey=ACQ_MER_INTO_SOURCE")
	        .success(function(data){
	            //响应成功
	            for(var i=0; i<data.length; i++){
	                $scope.itemSourceSelect.push({text:data[i].sysName,value:data[i].sysValue});
	            }
	});
	    
	
	    
	
	
	//代理商
	 $http.post("agentInfo/selectAllInfo.do")
 	 .success(function(msg){
 			//响应成功
 	    	for(var i=0; i<msg.length; i++){
 	    		$scope.agent.push({value:msg[i].agentNo,text:msg[i].agentName+"-"+msg[i].agentNo});
 	    	}
 	});
	 
	//一级代理商
	 $http.post("agentInfo/selectAllOneInfo.do")
 	 .success(function(msg){
 			//响应成功
 	    	for(var i=0; i<msg.length; i++){
 	    		$scope.agentone.push({value:msg[i].agentNo,text:msg[i].agentName+"-"+msg[i].agentNo});
 	    	}
 	});
	
	//查询
	$scope.query=function(){
		$http.post('acqInMerchantAction/selectAllInfo',
				"info="+angular.toJson($scope.info)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
				 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		.success(function(data){
			if(!data.bols){
				$scope.notice("查询失败");
				return;
			}
			
			$scope.acqMerchantGrid.data =data.page.result; 
			$scope.acqMerchantGrid.totalItems = data.page.totalCount;
		})
	}
	 $scope.query();
	//清空
	$scope.reset = function() {
		$scope.info={agentNo:-1,oneAgentNo:-1,intoSource:"",auditStatus:-1,merchantType:-1};
	};
	
	$scope.reset();
	
	$scope.paginationOptions=angular.copy($scope.paginationOptions);	
	$scope.acqMerchantGrid = {
	        paginationPageSize:10,                  //分页数量
	        paginationPageSizes: [10,20,50,100],	  //切换每页记录数
	        useExternalPagination: true,		  //开启拓展名
	        columnDefs: [
                {field: 'acqIntoNo',displayName: '收单进件编号',width: 80,pinnable: false,sortable: true},
               /* {field: 'merchantName',displayName: '商户名称',width: 150,pinnable: false,sortable: false},*/
                {field: 'merchantType',displayName: '商户类型',width: 180,pinnable: false,sortable: false,cellFilter:"formatDropping:"+angular.toJson($scope.merchantTypes)},
                {field: 'auditStatus',displayName: '审核状态',width: 160,pinnable: false,sortable: false,cellFilter:"formatDropping:"+angular.toJson($scope.auditStatuss)},
                {field: 'mcc',displayName: 'MCC码',width: 160,pinnable: false,sortable: false},
                {field: 'legalPerson',displayName: '法人姓名',width: 220,pinnable: false,sortable: false},
                {field: 'agentNo',displayName: '所属代理商编号',width: 220,pinnable: false,sortable: false},
                {field: 'agentName',displayName: '所属代理商名称',width: 160,pinnable: false,sortable: false},
                {field: 'oneAgentNo',displayName: '所属一级代理商编号',width: 160,pinnable: false,sortable: false},
                {field: 'oneAgentName',displayName: '所属一级代理商名称',width: 160,pinnable: false,sortable: false},
                {field: 'intoSource',displayName: '进件来源',width: 160,pinnable: false,sortable: false},
                {field: 'createTime',displayName: '进件日期',width: 150,pinnable: false,sortable: false,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
                {field: 'auditTime',displayName: '审核日期',width: 150,pinnable: false,sortable: false,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
	            {field: 'id',displayName: '操作',width: 200,pinnedRight:true,sortable: false,editable:true,cellTemplate:
	            	" <div class='lh30'> <a ng-show='grid.appScope.hasPermit(\"acqIn.detail\") && row.entity.auditStatus!=1' ui-sref='org.acqInDetail({id:row.entity.id,flag:1})'>详情 </a>" +
	            	" <a ng-show='grid.appScope.hasPermit(\"acqIn.audit\") && row.entity.auditStatus==1' ui-sref='org.acqInDetail({id:row.entity.id,flag:2})'>  审核 </a>" +
	            	" <a ng-show='grid.appScope.hasPermit(\"acqIn.mcc\") && row.entity.auditStatus==2 && row.entity.mcc==null' ng-click='grid.appScope.openTerminalNo(row.entity.id,row.entity.mcc)'> | 添加MCC </a></div>"
	            }
	        ],
			onRegisterApi: function(gridApi) {                
		         $scope.gridApi = gridApi;
		         gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
		         	$scope.paginationOptions.pageNo = newPage;
		         	$scope.paginationOptions.pageSize = pageSize;
		            $scope.query();
	             });
	      }
	};
	
	$scope.openTerminalNo=function(id,no){
		if(null !=no && no!=""){
			$scope.info.mcc = no;
		}
		$scope.tn={id:id};
		$('#myModal').modal('show');
	}
	
	$scope.cancelMcc = function(){
		$('#myModal').modal('hide');
	}
	
	$scope.addTerminalNo=function(){
		$http.post('acqInMerchantAction/updateMcc',
				"id="+$scope.tn.id+"&mcc="+$scope.info.mcc,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		 .success(function(data){
			if(data.status){
				$scope.notice(data.msg);
				$scope.query();
			}else{
				$scope.notice(data.msg);
			}
		})
		
		$scope.info.mcc="";
		$('#myModal').modal('hide');
	}
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
								$scope.agentt.push({value: "", text: ""});
							}else{
								for(var i=0; i<response.data.length; i++){
									$scope.agentt.push({value:response.data[i].agentNo,text:response.data[i].agentName+"-"+response.data[i].agentNo});
								}
							}
							$scope.agent = $scope.agentt;
							oldValue = value;
						});
				},800);
		}

	}
	
	$scope.getOneStates =getOneStates;
	var oldOneValue="";
	var timeout="";
	function getOneStates(value) {
		$scope.agentt = [];
		var newValue=value;
		if(newValue != oldOneValue){
			if (timeout) $timeout.cancel(timeout);
			timeout = $timeout(
				function(){
					$http.post('agentInfo/selectAllOneInfo','item=' + value,
						{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
						.then(function (response) {
							if(response.data.length==0) {
								$scope.agentt.push({value: "", text: ""});
							}else{
								for(var i=0; i<response.data.length; i++){
									$scope.agentt.push({value:response.data[i].agentNo,text:response.data[i].agentName+"-"+response.data[i].agentNo});
								}
							}
							$scope.agentone = $scope.agentt;
							oldOneValue = value;
						});
				},800);
		}

	}
	
	 $scope.import=function(){
	        SweetAlert.swal({
	                title: "确认导出？",
	                showCancelButton: true,
	                confirmButtonColor: "#DD6B55",
	                confirmButtonText: "提交",
	                cancelButtonText: "取消",
	                closeOnConfirm: true,
	                closeOnCancel: true
	            },
	            function (isConfirm) {
	                if (isConfirm) {
	                    location.href="acqInMerchantAction/exportInfo?info="+encodeURI(encodeURI(angular.toJson($scope.info)));
	                }
	            });
	    };
	

	//页面绑定回车事件
	$document.bind("keypress", function(event) {
		$scope.$apply(function (){
			if(event.keyCode == 13){
				$scope.query();
			}
		})
	});
	
})