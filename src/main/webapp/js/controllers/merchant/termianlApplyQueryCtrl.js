/**
 * 机具申请查询
 */
angular.module('inspinia').controller('termianlApplyQueryCtrl',function($scope,$state,$filter,$log,$http,$stateParams,$compile,i18nService,$document){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文

	$scope.info={status:"",merAccount:"",isBindParam:"",SNisNull:""};
	$scope.statuss=[{text:"全部",value:""},{text:"待直属处理",value:0},{text:"待一级处理",value:2},{text:"已处理",value:1}];
	$scope.merAccounts=[{text:"全部",value:""},{text:"是",value:1},{text:"否",value:0}];
	$scope.isBindParams=[{text:"全部",value:""},{text:"是",value:1},{text:"否",value:0}];
    $scope.SNisNullSelect=[{text:"全部",value:""},{text:"是",value:"1"},{text:"否",value:"0"}];
	$scope.paginationOptions=angular.copy($scope.paginationOptions);	
	//查询
	$scope.selectInfo=function(){
		if($scope.info.sTime>$scope.info.eTime){
			$scope.notice("起始时间不能大于结束时间");
			return;
		}
		$http.post(
			'terminalApplyAction/selectAllInfo',
			 "info="+angular.toJson($scope.info)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
			 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
		).success(function(result){
			if(!result){
				return;
			}
			//响应成功]
			$scope.gridOptions.data = result.page.result; 
			$scope.gridOptions.totalItems = result.page.totalCount;
		});
	}
	//清空
	$scope.clear=function(){
		$scope.info={merAccount:"",status:"",mobilephone:"",agentName:"",isBindParam:"",SNisNull:""};
	}
	$scope.selectInfo();
	 $scope.gridOptions={                           //配置表格
		      paginationPageSize:10,                  //分页数量
		      paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
		      useExternalPagination: true,
			  enableHorizontalScrollbar: 1,        //横向滚动条
			  enableVerticalScrollbar : 1,  		//纵向滚动条
		      columnDefs:[                           //表格数据
				  { field: 'merchantNo',displayName:'商户编号',width:160},
				  { field: 'merchantName',displayName:'商户名称' ,width:180},
				  { field: 'mobilephone',displayName:'商户手机号',width:130 },
				  { field: 'agentName',displayName:'代理商名称',width:155 },
				  { field: 'status',displayName:'状态',width:110,cellFilter:"statusss"},
		       	  { field: 'isBind',displayName:'是否绑定机具',width:140,cellFilter:"isBinds"},
		       	  { field: 'createTime',displayName:'申请时间',width:150,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
				  { field: 'sn',displayName:'机具SN号' ,width:140},
				  { field: 'sn',displayName:'机具是否强制下发',cellFilter:"snisnull",width:140},
				  { field: 'id',displayName:'操作',width:140,pinnedRight:true,
					  cellTemplate: '<a ui-sref="merchant.termianlApplyDetail({id:row.entity.id})">详情</a>'
				  }
		      ],
			  onRegisterApi: function(gridApi) {                
		          $scope.gridApi = gridApi;
		          gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
		          	$scope.paginationOptions.pageNo = newPage;
		          	$scope.paginationOptions.pageSize = pageSize;
		             $scope.selectInfo();
		          });
		      }
		};

	//页面绑定回车事件
	$document.bind("keypress", function(event) {
		$scope.$apply(function (){
			if(event.keyCode == 13){
				$scope.selectInfo();
			}
		})
	});
	
}).filter('statusss', function () {
	return function (value) {
		switch(value) {
			case "0" :
				return "待直属处理";
				break;
			case "1" :
				return "已处理";
				break;
			case "2" :
				return "待一级处理";
				break;
		}
	}
}).filter('isBinds', function () {
	return function (value) {
		switch(value) {
			case false :
				return "否";
				break;
			case true :
				return "是";
				break;
		}
	}
}).filter('snisnull', function () {
    return function (value) {
        if(value!=null&&""!=value){
			return "是";
		}else{
        	return "否";
		}
    }
})