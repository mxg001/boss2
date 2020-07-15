/**
 * 收单机构商户
 */
angular.module('inspinia',['angularFileUpload','uiSwitch', 'infinity.angular-chosen']).controller('acqMerchantCtrl',function($scope, $http, $state,$timeout, $stateParams, i18nService,$filter,FileUploader,SweetAlert,$document) {

	i18nService.setCurrentLang('zh-cn');
	$scope.acqStatuss=[{text:"全部",value:-1},{text:"打开",value:1},{text:"关闭",value:0}];
	$scope.agent=[{value:-1,text:"全部"}];
	$scope.acqOrgs=[{value:-1,text:"全部"}]
	$scope.acqMerchantTypeString=angular.toJson($scope.acqMerchantTypes);
    $scope.specialSelect = [{text:"是",value:'1'},{text:"否",value:'0'}];

	$scope.sourceList = angular.copy($scope.acqMerSourceList);
	$scope.sourceList.unshift({text:"全部",value:null});

	$scope.sourceList1 = angular.copy($scope.acqMerSourceList);
	$scope.sourceList1.unshift({text:"",value:null});
	$scope.sourceStr=angular.toJson($scope.sourceList1);

	//清空
	$scope.reset = function() {
		$scope.info={acqStatus:-1,largeSmallFlag:-1,agentNo:-1,acqOrgId:-1,stratRemaimAmount:"",endRemaimAmount:"",acqMerchantType:-1,source:null};
	};
	$scope.reset();
	//代理商
	 $http.post("agentInfo/selectAllInfo.do")
  	 .success(function(msg){
  			//响应成功
  	    	for(var i=0; i<msg.length; i++){
  	    		$scope.agent.push({value:msg[i].agentNo,text:msg[i].agentName});
  	    	}
  	});
	
	//收单机构
	 $http.post("acqOrgAction/selectBoxAllInfo")
  	 .success(function(msg){
  			//响应成功
  	    	for(var i=0; i<msg.length; i++){
  	    		$scope.acqOrgs.push({value:msg[i].id,text:msg[i].acqName});
  	    	}
  	});
	
	//查询
	$scope.query=function(){
		$http.post('acqMerchantAction/selectAllInfo',
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
	// $scope.query();

	$scope.paginationOptions=angular.copy($scope.paginationOptions);	
	$scope.acqMerchantGrid = {
	        paginationPageSize:10,                  //分页数量
	        paginationPageSizes: [10,20,50,100],	  //切换每页记录数
	        useExternalPagination: true,		  //开启拓展名
	        columnDefs: [
                {field: 'id',displayName: '序号',width: 80,pinnable: false,sortable: true},
                {field: 'agentName',displayName: '代理商名称',width: 150,pinnable: false,sortable: false},
                {field: 'acqMerchantNo',displayName: '收单机构商户编号',width: 180,pinnable: false,sortable: false},
                {field: 'acqMerchantName',displayName: '收单机构商户名称',width: 160,pinnable: false,sortable: false},
				{field: 'source',displayName: '来源途径',width: 160,pinnable: false,sortable: false,
					cellFilter: "formatDropping:"+$scope.sourceStr
				},
				{field: 'acqName',displayName: '收单机构',width: 160,pinnable: false,sortable: false},
                {field: 'serviceName',displayName: '收单服务',width: 220,pinnable: false,sortable: false},
                {field: 'dayAmount',displayName: '今日收单金额',width: 220,pinnable: false,sortable: false,cellFilter:"currency:''"},
                {field: 'merchantNo',displayName: '商户编号',width: 160,pinnable: false,sortable: false},
                {field: 'merchantName',displayName: '商户名称',width: 160,pinnable: false,sortable: false},
                {field: 'acqMerchantCode',displayName: '收单机构对应收单商户进件编号',width: 160,pinnable: false,sortable: false},
                {field: 'repPay',displayName: '代付',width: 100,pinnable: false,sortable: true,
                	cellFilter:"formatDropping:[{text:'否',value:1},{text:'是',value:2}]"
                },
                {field: 'acqMerchantType',displayName: '类别',width: 160,pinnable: false,sortable: false,
                	cellFilter: "formatDropping:"+$scope.acqMerchantTypeString
                },
                {field: 'acqStatus',displayName: '状态',width: 150,pinnable: false,sortable: false,
                	cellTemplate:
                		/*'<switch class="switch" ng-model="row.entity.acqStatus"  ng-change="grid.appScope.open(row)" />'*/
                		'<span ng-show="grid.appScope.hasPermit(\'orgMer.switch\')"><switch class="switch switch-s" ng-model="row.entity.acqStatus" ng-change="grid.appScope.open(row)" /></span>'
        	            +'<span ng-show="!grid.appScope.hasPermit(\'orgMer.switch\')"> <span ng-show="row.entity.acqStatus==1">开启</span><span ng-show="row.entity.acqStatus==0">关闭</span></span>'
                
                },
                {field: 'createTime',displayName: '创建时间',width: 150,pinnable: false,sortable: false,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
	            {field: 'id',displayName: '操作',width: 200,pinnedRight:true,sortable: false,editable:true,cellTemplate:
	            	" <div class='lh30'> <a ng-show='grid.appScope.hasPermit(\"orgMer.detail\")' ui-sref='org.acqMerchantDetail({id:row.entity.id})'>详情 </a>" +
	            	" <a ng-show='grid.appScope.hasPermit(\"acqMer.update\")' ui-sref='org.acqMerchantUp({id:row.entity.id})'> | 修改 </a>" +
	            	" <a ng-show='grid.appScope.hasPermit(\"acqMer.addTermianl\")' ng-click='grid.appScope.openTerminalNo(row.entity.id,row.entity.acqMerchantNo)'> | 增加终端 </a></div>"
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
	
	//开通\关闭
	$scope.open=function(row){
		if(row.entity.acqStatus){
			$scope.serviceText = "确定开启？";
		} else {
			$scope.serviceText = "确定关闭？";
		}
        SweetAlert.swal({
            title: $scope.serviceText,
//            text: "服务状态为关闭后，不能正常交易!",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "提交",
            cancelButtonText: "取消",
            closeOnConfirm: true,
            closeOnCancel: true },
	        function (isConfirm) {
	            if (isConfirm) {
	            	if(row.entity.acqStatus==true){
	            		row.entity.acqStatus=1;
	            	} else if(row.entity.acqStatus==false){
	            		row.entity.acqStatus=0;
	            	}
	            	var data={"acqStatus":row.entity.acqStatus,"id":row.entity.id,"acqMerchantNo":row.entity.acqMerchantNo};
	                $http.post("acqMerchantAction/updateAcqStatus","info="+angular.toJson(data),{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
	            	.success(function(data){
	            		if(data.bols){
	            			$scope.notice("操作成功");
	            		}else{
	            			if(row.entity.acqStatus==true){
	    	            		row.entity.acqStatus = false;
	    	            	} else {
	    	            		row.entity.acqStatus = true;
	    	            	}
	            			$scope.notice(data.msg);
	            		}
	            	})
	            	.error(function(data){
	            		if(row.entity.acqStatus==true){
    	            		row.entity.acqStatus = false;
    	            	} else {
    	            		row.entity.acqStatus = true;
    	            	}
	            		$scope.notice("服务器异常")
	            	});
	            } else {
	            	if(row.entity.acqStatus==true){
	            		row.entity.acqStatus = false;
	            	} else {
	            		row.entity.acqStatus = true;
	            	}
	            }
        });
    	
    };
	
	//打开新增终端模板
	$scope.openTerminalNo=function(id,no){
		$scope.tn={acqMerchantNo:no,terminalNo:"",id:id};
		$('#myModal').modal('show');
	}
	//提交新增终端模板
	$scope.addTerminalNo=function(){
		var data={};
		if($scope.tn.terminalNo==""||$scope.tn.terminalNo==null){
			$scope.notice("收单机构终端不能为空~~~~");
			return;
		}
		for(var i =0;i<$scope.acqMerchantGrid.data.length;i++){
			if($scope.tn.id==$scope.acqMerchantGrid.data[i].id){
				data={acqTerminalNo:$scope.tn.terminalNo,acqOrgId:$scope.acqMerchantGrid.data[i].acqOrgId,acqMerchantNo:$scope.acqMerchantGrid.data[i].acqMerchantNo}
			}
		}
		$http.post('acqMerchantAction/addTermianlInfo',
				"info="+angular.toJson(data),{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		 .success(function(data){
			if(data.result){
				$scope.notice(data.msg);
			}else{
				$scope.notice(data.msg);
			}
		})
		
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
									$scope.agentt.push({value:response.data[i].agentNo,text:response.data[i].agentName});
								}
							}
							$scope.agent = $scope.agentt;
							oldValue = value;
						});
				},800);
		}

	}

	//导出信息
	$scope.exportInfo=function(){
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
					$scope.exportInfoClick("acqMerchantAction/importDetail",{"info":angular.toJson($scope.info)});
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