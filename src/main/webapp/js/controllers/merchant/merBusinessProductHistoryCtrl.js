angular.module('inspinia').controller('merBusinessProductHistoryCtrl',function($scope,$http,$state,$stateParams,$compile,$filter,$log,$uibModal){

$scope.operationPersonTypeList=[{text:"全部",value:"-1"},{text:"运营",value:"1"},{text:"商户",value:"2"}];
$scope.operationTypeList=[{text:"全部",value:"-1"},{text:"关闭",value:"1"},{text:"更换",value:"2"}];
$scope.sourceProductTypeList = [];
$scope.newProductTypeList = [];

$scope.info={merchantNo:"",mobilephone:"",operationType:"-1",operationPersonType:"-1",operationPerson:"",sourceProductType:"-1",newProductType:"-1"};


//业务产品
$http.get('merchantBusinessProduct/selectProductOrNewProduct.do')
.success(function(largeLoad) {
	if(!largeLoad)
		return
		$scope.sourceProductTypeList=largeLoad.sourceProduct;
		$scope.sourceProductTypeList.splice(0,0,{bpId:"-1",bpName:"全部"});		
		$scope.newProductTypeList=largeLoad.newProduct;
		$scope.newProductTypeList.splice(0,0,{bpId:"-1",bpName:"全部"});
});


//模糊查询
$scope.selectInfos=function(){
	if($scope.info.sTime>$scope.info.eTime){
		$scope.notice("起始时间不能大于结束时间");
		return;
	}
	$http.post(
		 'merchantBusinessProduct/selectMerBpHistoryList',
		 "info="+angular.toJson($scope.info)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
		 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
	).success(function(result){
		//响应成功
		$scope.gridOptions.data = result.result; 
		$scope.gridOptions.totalItems = result.totalCount;
	});
}

//清空
$scope.clear=function(){
	$scope.info={auditorId:"",cardId:"",mbpId:"",merchantNo:"",merchantExamineState:2,agentName:"",agentNode:-1,productType:"-1",termianlType:"-1",mobilephone:""};
}
$scope.gridOptions={                           //配置表格
		  paginationPageSize:10,                  //分页数量
		  paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
		  useExternalPagination: true,  
	      columnDefs:[                           //表格数据
	         { field: 'merchantNo',displayName:'商户编号',width:150},
	         { field: 'createTime',displayName:'操作时间',width:150,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'
	         },
	         { field: 'operationPersonType',displayName:'操作人类型',width:120,
	        	 cellFilter:"formatDropping:[{text:'运营',value:1},{text:'商户',value:2}]"
	         },
	         { field: 'operationPerson',displayName:'操作人',width:120,
	         },
	         { field: 'operationType',displayName:'操作类型',width:120,
	        	 cellFilter:"formatDropping:[{text:'关闭',value:1},{text:'更换',value:2}]"
	         },
	         { field: 'bpName1',displayName:'原业务产品',width:120,
	        	 
	         },
	         { field: 'bpName2',displayName:'新业务产品',width:120,
	         },
	     
	        
	      ],
	      onRegisterApi: function(gridApi) {                
	          $scope.gridApi = gridApi;
	          gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
	          	$scope.paginationOptions.pageNo = newPage;
	          	$scope.paginationOptions.pageSize = pageSize;
	             $scope.selectInfos();
	          });
	      }
	};


});

