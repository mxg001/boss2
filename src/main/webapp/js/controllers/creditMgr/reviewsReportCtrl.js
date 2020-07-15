/**
 * 信用卡管家-评测报告
 */
angular.module('inspinia').controller('reviewsReportCtrl',function($scope, $http, $state,$stateParams,i18nService){

	i18nService.setCurrentLang('zh-cn');

	$scope.reviewsReport={};

    $scope.amountGrid = {
		columnDefs: [
			{field: 't1',displayName: '0-500元',cellTemplate:'<div class="col-sm-12 checkbox">{{row.entity.t1}}笔</div>'},
			{field: 't2',displayName: '501-2000元',cellTemplate:'<div class="col-sm-12 checkbox">{{row.entity.t2}}笔</div>'},
			{field: 't3',displayName: '2001-5000元',cellTemplate:'<div class="col-sm-12 checkbox">{{row.entity.t3}}笔</div>'},
			{field: 't4',displayName: '5001-10000元',cellTemplate:'<div class="col-sm-12 checkbox">{{row.entity.t4}}笔</div>'},
			{field: 't5',displayName: '10001-50000元',cellTemplate:'<div class="col-sm-12 checkbox">{{row.entity.t5}}笔</div>'}
		]
	};

    $scope.timeGrid = {
		columnDefs: [
			{field: 't1',displayName: '0:00-4:00',cellTemplate:'<div class="col-sm-12 checkbox">{{row.entity.t1}}笔</div>'},
			{field: 't2',displayName: '4:00-8:00',cellTemplate:'<div class="col-sm-12 checkbox">{{row.entity.t2}}笔</div>'},
			{field: 't3',displayName: '8:00-12:00',cellTemplate:'<div class="col-sm-12 checkbox">{{row.entity.t3}}笔</div>'},
			{field: 't4',displayName: '12:00-16:00',cellTemplate:'<div class="col-sm-12 checkbox">{{row.entity.t4}}笔</div>'},
			{field: 't5',displayName: '16:00-20:00',cellTemplate:'<div class="col-sm-12 checkbox">{{row.entity.t5}}笔</div>'},
			{field: 't6',displayName: '20:00-24:00',cellTemplate:'<div class="col-sm-12 checkbox">{{row.entity.t6}}笔</div>'}
		]
    };

	$http({
        url:"cmBill/queryReviewsReport?billId="+$stateParams.billId,
        method:"GET"
    }).success(function(data){
        if (data.status){
            $scope.reviewsReport = data.reviewsReport;
            $scope.amountGrid.data = [data.amountCount];
            $scope.timeGrid.data = [data.timeCount];
        } else {
        	$scope.notice(data.msg);
        }
    }).error(function () {
        $scope.notice("服务器异常，请稍后再试");
    });

});