/**
 * 排行榜榜单明细
 */
angular.module('inspinia').controller('rankingRecordDetailCtrl',function($scope, $http, $state,SweetAlert,$compile,$stateParams){

	 
	
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
         {field: 'pushTime',displayName: '发放时间',width: 150,pinnable: false,sortable: false,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',cellTemplate:
         	 '<div class="ui-grid-cell-contents ng-binding ng-scope" ng-style="{color:(row.entity.status == \'已移除\')?\'#c1c0c0\':\'#191a1b\'}">{{row.entity.pushTime | date:"yyyy-MM-dd HH:mm:ss"}}</div>'},
         {field: 'remark',displayName: '说明',width: 150,pinnable: false,sortable: false,cellTemplate:
         	 '<div class="ui-grid-cell-contents ng-binding ng-scope" ng-style="{color:(row.entity.status == \'已移除\')?\'#c1c0c0\':\'#191a1b\'}">{{row.entity.remark}}</div>'},
	     {field: 'action',displayName: '操作',width: 180,pinnedRight:true,sortable: false,editable:true,cellTemplate:
	     '<a class="lh30" target="_blank" ng-show="row.entity.status!=\'已移除\' && row.entity.isRank==\'获奖\' " ng-click="grid.appScope.removeRanking({detailId:row.entity.id})">移除</a>' + 
	     ' <a class="lh30" target="_blank" ng-show="row.entity.status == \'已移除\'  && row.entity.isRank==\'获奖\' " '
	     + ' ng-click="grid.appScope.cancelRemoveRanking({detailId:row.entity.id})">取消移除</a>'
	     }
         
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
             url: 'superBank/rankingRecordDetail?recordId='+$stateParams.recordId,
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
    
     //发放奖金
     $scope.pushBonus = function(){
    	 $scope.pushSubmitting = true;
    	 $http({
             url: 'superBank/pushRankingBonus?recordId='+$scope.baseInfo.recordId,
             method:"GET"
         }).success(function (result) {
             $scope.pushSubmitting = true;
             $scope.loadImg = false;
             if (!result.status){
                 $scope.notice(result.msg);
                 return;
             }
             $scope.notice(result.msg);
             $state.transitionTo('superBank.rankingRecord',null,{reload:true});
         }).error(function () {
             $scope.pushSubmitting = false;
             $scope.loadImg = false;
             $scope.notice('服务器异常,请稍后再试.');
         });
     }
    
     $scope.removeRanking = function(data){
		 SweetAlert.swal({
             title: "确定移除排名吗？",
             type: "warning",
             showCancelButton: true,
             confirmButtonColor: "#DD6B55",
             confirmButtonText: "确定",
             cancelButtonText: "取消",
             closeOnConfirm: true,
             closeOnCancel: true },
         function (isConfirm) {
             if (isConfirm) {
                 //alert(data.detailId);
            	 $scope.detailInfo = {"id":data.detailId};
            	 $("#removeRanking").modal("show");
             }
         });
     };
     
     
     $scope.cancelRemoveRanking = function(data){
		 SweetAlert.swal({
             title: "确定取消移除吗？",
             type: "warning",
             showCancelButton: true,
             confirmButtonColor: "#DD6B55",
             confirmButtonText: "确定",
             cancelButtonText: "取消",
             closeOnConfirm: true,
             closeOnCancel: true },
         function (isConfirm) {
             if (isConfirm) {
            	 $http({
                     url: 'superBank/cancelRemoveRankingDetail?detailId='+data.detailId,
                     method:'GET'
                 }).success(function (result) {
                     $scope.submitting = false;
                     $scope.loadImg = false;
                     if (!result.status){
                         $scope.notice(result.msg);
                         return;
                     }
                     $scope.notice(result.msg);
                     $scope.query();
                 }).error(function () {
                     $scope.submitting = false;
                     $scope.loadImg = false;
                     $scope.notice('服务器异常,请稍后再试.');
                 });
             }
         });
     };
     
    $scope.cancel = function () {
    	$("#removeRanking").modal("hide");
    };
    
    $scope.remove = function () {
    	$http({
            url: 'superBank/removeRankingDetail',
            data: $scope.detailInfo,
            method:'POST'
        }).success(function (result) {
            $scope.submitting = false;
            $scope.loadImg = false;
            if (!result.status){
                $scope.notice(result.msg);
                $("#removeRanking").modal("hide");
                return;
            }
            $scope.notice(result.msg);
            $("#removeRanking").modal("hide");
            $scope.query();
        }).error(function () {
            $scope.submitting = false;
            $scope.loadImg = false;
            $scope.notice('服务器异常,请稍后再试.');
        });
    };
    
    $scope.cancelRemove = function () {
    	$http({
            url: 'superBank/cancelRemoveRankingDetail?detailId='+$scope.detailInfo.id,
            method:'GET'
        }).success(function (result) {
            $scope.submitting = false;
            $scope.loadImg = false;
            if (!result.status){
                $scope.notice(result.msg);
                $("#removeRanking").modal("hide");
                return;
            }
            $scope.notice(result.msg);
            $("#removeRanking").modal("hide");
            $scope.query();
        }).error(function () {
            $scope.submitting = false;
            $scope.loadImg = false;
            $scope.notice('服务器异常,请稍后再试.');
        });
    };
    
});