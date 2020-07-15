/**
 * 黑名单明细管理
 */
angular.module('inspinia').controller('blacklistDetailMagCtrl',function($scope, $http, $state, $stateParams, i18nService,$filter) {

	i18nService.setCurrentLang('zh-cn');
	$scope.blacklistTypes=[{text:"商户身份证",value:2},{text:"卡号",value:3},{text:"商户编号",value:1}];
	$scope.info={}
	$scope.blacklistDate=[]
	$scope.roll={rollNo:$stateParams.no}
	//查询
	$http.post('riskRollAction/selectRollDetail',
			"ids="+angular.toJson($stateParams.id),
			 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
	.success(function(data){
		if(!data.bols){
			$scope.notice(data.msg);
			return;
		}
		$scope.info=data.result;
	})
	//查询
	$scope.query=function(){
		$http.post('riskRollAction/selectRollListAllInfo',
				"roll="+angular.toJson($scope.roll)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
				 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		.success(function(data){
			if(!data.bols){
				$scope.notice("查询失败");
				return;
			}
			$scope.blacklistDate =data.page.result; 
			$scope.blacklistGrid.totalItems = data.page.totalCount;
		})
	}
	$scope.query();
	//清空
	$scope.reset = function() {
		$scope.roll={rollNo:$stateParams.no,rollNumber:""}
	};
	
	$scope.paginationOptions=angular.copy($scope.paginationOptions);	
	$scope.blacklistGrid = {
			data:"blacklistDate",
	        paginationPageSize:10,                  //分页数量
	        paginationPageSizes: [10,20,50,100],	  //切换每页记录数
	        useExternalPagination: true,		  //开启拓展名
	        columnDefs: [
                {field: 'id',displayName: '序号',width: 180,pinnable: false,sortable: false},
                {field: 'rollNumber',displayName: '黑名单号码',width: 250,pinnable: false,sortable: false},
                {field: 'createTime',displayName: '创建时间',width: 160,pinnable: false,sortable: false,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
	            {field: 'id',displayName: '操作',width: 320,pinnable: false,sortable: false,editable:true,cellTemplate:
	            	"<a ng-click='grid.appScope.openDelblacklist(row.entity.id)'>删除 </a>"
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
	
	//删除窗口
	$scope.openDelblacklist=function(id){
		$scope.listId=id;
		$('#delblacklist').modal('show');
	}
	
	//确认删除
	$scope.delblacklistNo=function(){
		for(var i =0;i<$scope.blacklistDate.length;i++){
			if($scope.blacklistDate[i].id==$scope.listId){
				$scope.blacklistDate.splice(i,1);
				break;
			}
		}
		$http.post('riskRollAction/deleteByid',
				"ids="+angular.toJson($scope.listId),
				 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		.success(function(data){
			if(!data.bols){
				$scope.notice(data.msg);
			}else{
				$scope.notice(data.msg);
			}
		})
		$('#delblacklist').modal('hide');
		
	}
	
	//打开添加名单窗口
	$scope.openAddblacklist=function(no){
		$scope.lists={rollNo:$scope.info.rollNo,rollType:$scope.info.rollType,rollNumber:""};
		$('#addblacklist').modal('show');
	}
	//提交添加名单
	$scope.addblacklistNo=function(){
		$scope.submitting = true;
		if($scope.lists.rollNo==""||$scope.lists.rollNo==null){
			$scope.notice("请填写完整的信息");
			$scope.submitting = false;
			return;
		}
		$http.post('riskRollAction/addRollListInfo',
				"info="+angular.toJson($scope.lists),
				 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		.success(function(data){
			if(!data.bols){
				$scope.notice(data.msg);
				$scope.submitting = false;
			}else{
				$scope.notice(data.msg);
				$scope.submitting = false;
				$scope.query();
				$('#addblacklist').modal('hide');
			}
		})
		
	}
})