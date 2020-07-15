/**
 * 查询进件
 */
angular.module('inspinia',[]).controller("queryRequireCtrl", function($scope, $http, $state, $stateParams,i18nService,$document) {
	$scope.requireName="";
	i18nService.setCurrentLang('zh-cn');
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
    $scope.query=function(){
		$http.post('require/queryRequireItem','requireName=' + $scope.requireName+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+
				$scope.paginationOptions.pageSize,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		.success(function(data){
			if(!data)
				return;
			$scope.requireGrid.data =data.result; 
			$scope.requireGrid.totalItems = data.totalCount;
		})
    }
    $scope.query();
    $scope.requireGrid = {
    		paginationPageSize:10,                  //分页数量
  	        paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
  	        useExternalPagination: true,		  //开启拓展名
  	        enableHorizontalScrollbar: true,        //横向滚动条
            enableVerticalScrollbar : true,  		//纵向滚动条
	        columnDefs: [
                {field: 'itemId',displayName: '进件要求项ID',pinnable: false,sortable: false},
                {field: 'itemName',displayName: '进件要求项名称',pinnable: false,sortable: false},
	            {field: 'action',displayName: '操作',pinnable: false,sortable: false,editable:true,cellTemplate:
	            	"<div class='lh30'><a ng-show='grid.appScope.hasPermit(\"require.detail\")' ui-sref='service.requireDetail({id:row.entity.itemId})'>详情</a><a  ng-show='grid.appScope.hasPermit(\"require.edit\")'  ui-sref='service.editRequireItem({id:row.entity.itemId})'> | 修改</a></div>"}
	        ],
	        onRegisterApi: function(gridApi) {      
	            $scope.gridApi = gridApi;
	            $scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
		          	$scope.paginationOptions.pageNo = newPage;
		          	$scope.paginationOptions.pageSize = pageSize;
		            $scope.query();
		     });
	        }
	 };
  //reset
	$scope.resetForm=function(){
		$scope.requireName="";
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