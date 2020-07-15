/**
 * 排行榜榜单详情
 */
angular.module('inspinia').controller('findRankingRecordDetailCtrl',function($scope, $http, $state,SweetAlert,$compile,$stateParams){

	 
	
    $scope.columnDefs = [
         {field: 'rankingIndex',displayName: '排名',width: 80,pinnable: false,sortable: false,cellTemplate:
     	 	'<div class="ui-grid-cell-contents ng-binding ng-scope" ng-style="{color:(row.entity.status == \'已移除\')?\'#c1c0c0\':\'#191a1b\'}">{{row.entity.rankingIndex}}</div>'},
		 {field: 'userName',displayName: '用户姓名',width: 200,pinnable: false,sortable: false,cellTemplate:
		 	 '<div class="ui-grid-cell-contents ng-binding ng-scope" ng-style="{color:(row.entity.status == \'已移除\')?\'#c1c0c0\':\'#191a1b\'}">{{row.entity.userName}}</div>'},
		 {field: 'nickName',displayName: '微信呢称',width: 150,pinnable: false,sortable: false,cellTemplate:
		 	 '<div class="ui-grid-cell-contents ng-binding ng-scope" ng-style="{color:(row.entity.status == \'已移除\')?\'#c1c0c0\':\'#191a1b\'}">{{row.entity.nickName}}</div>'},
		 {field: 'userCode',displayName: '用户iD',width: 150,pinnable: false,sortable: false,cellTemplate:
		 	 '<div class="ui-grid-cell-contents ng-binding ng-scope" ng-style="{color:(row.entity.status == \'已移除\')?\'#c1c0c0\':\'#191a1b\'}">{{row.entity.userCode}}</div>'},
		 {field: 'userTotalAmount',displayName: '统计总额',width: 150,pinnable: false,sortable: false,cellTemplate:
		 	 '<div class="ui-grid-cell-contents ng-binding ng-scope" ng-style="{color:(row.entity.status == \'已移除\')?\'#c1c0c0\':\'#191a1b\'}">{{row.entity.userTotalAmount}}</div>'},
		 {field: 'isRank',displayName: '是否获奖',width: 150,pinnable: false,sortable: false,cellTemplate:
		 	 '<div class="ui-grid-cell-contents ng-binding ng-scope" ng-style="{color:(row.entity.status == \'已移除\')?\'#c1c0c0\':\'#191a1b\'}">{{row.entity.isRank}}</div>'},
		 {field: 'rankingLevel',displayName: '获奖等级',width: 150,pinnable: false,sortable: false,cellTemplate:
		 	 '<div class="ui-grid-cell-contents ng-binding ng-scope" ng-style="{color:(row.entity.status == \'已移除\')?\'#c1c0c0\':\'#191a1b\'}">{{row.entity.rankingLevel}}</div>'},
		 {field: 'rankingAmount',displayName: '获奖金额',width: 150,pinnable: false,sortable: false,cellTemplate:
		 	 '<div class="ui-grid-cell-contents ng-binding ng-scope" ng-style="{color:(row.entity.status == \'已移除\')?\'#c1c0c0\':\'#191a1b\'}">{{row.entity.rankingAmount}}</div>'},
		 {field: 'status',displayName: '用户发放状态',width: 150,pinnable: false,sortable: false,cellTemplate:
		 	 '<div class="ui-grid-cell-contents ng-binding ng-scope" ng-style="{color:(row.entity.status == \'已移除\')?\'#c1c0c0\':\'#191a1b\'}">{{row.entity.status}}</div>'},
		 {field: 'removeTime',displayName: '移除时间',width: 150,pinnable: false,sortable: false,cellTemplate:
		 	 '<div class="ui-grid-cell-contents ng-binding ng-scope" ng-style="{color:(row.entity.status == \'已移除\')?\'#c1c0c0\':\'#191a1b\'}">{{row.entity.removeTime | date:"yyyy-MM-dd HH:mm:ss"}}</div>'},
		 {field: 'pushTime',displayName: '发放时间',width: 150,pinnable: false,sortable: false,cellTemplate:
		 	 '<div class="ui-grid-cell-contents ng-binding ng-scope" ng-style="{color:(row.entity.status == \'已移除\')?\'#c1c0c0\':\'#191a1b\'}">{{row.entity.pushTime | date:"yyyy-MM-dd HH:mm:ss"}}</div>'},
		 {field: 'remark',displayName: '说明',width: 150,pinnable: false,sortable: false,cellTemplate:
		 	 '<div class="ui-grid-cell-contents ng-binding ng-scope" ng-style="{color:(row.entity.status == \'已移除\')?\'#c1c0c0\':\'#191a1b\'}">{{row.entity.remark}}</div>'}
     ];

     $scope.orderGrid = {
         //paginationPageSize:10,                  //分页数量
         //paginationPageSizes: [10,20,50,100],	//切换每页记录数
         useExternalPagination: true,		  //开启拓展名
         enableHorizontalScrollbar: true,        //横向滚动条
         enableVerticalScrollbar : true,  		//纵向滚动条
         columnDefs: $scope.columnDefs,
         onRegisterApi: function(gridApi) {
             $scope.gridApi = gridApi;
             $scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                 $scope.paginationOptions.pageNo = 1;
                 $scope.paginationOptions.pageSize = pageSize;
                 $scope.query();
             });
         }
     };

     $scope.query = function () {
         $scope.pushSubmitting = true;
         $scope.loadImg = true;
         $http({
             url: 'superBank/findRankingRecordDetail?recordId='+$stateParams.recordId,
             method:"GET"
         }).success(function (result) {
             $scope.pushSubmitting = false;
             $scope.loadImg = false;
             if (!result.status){
                 $scope.notice(result.msg);
                 return;
             }
             $scope.orderGrid.data = result.data.page.result;
             $scope.orderGrid.totalItems = result.data.page.totalCount;
             $scope.baseInfo = result.data.sumOrder;
         }).error(function () {
             $scope.pushSubmitting = false;
             $scope.loadImg = false;
             $scope.notice('服务器异常,请稍后再试.');
         });
     };
    
     $scope.query();
    
     $scope.cancel = function(){
    	 history.back(-1);
     }
});