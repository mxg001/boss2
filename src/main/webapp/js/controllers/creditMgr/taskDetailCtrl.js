/**
 * 信用卡管家-任务详情
 */
angular.module('inspinia').controller('taskDetailCtrl',function($scope, $http, $state,$stateParams,i18nService){

	i18nService.setCurrentLang('zh-cn');

	$scope.info={};

    $scope.taskGrid = {
		columnDefs: [
			{field: 'taskType',displayName: '序号'},
			{field: 'taskTitle',displayName: '内容'},
			{field: 'taskValue',displayName: '完成值'}
		]
	};

	$http({
        url:"cmTask/queryTaskDetail?id="+$stateParams.id,
        method:"GET"
    }).success(function(data){
        if (data.status){
            $scope.info = data.report;
            $scope.taskGrid.data = data.info;
        } else {
        	$scope.notice(data.msg);
        }
    }).error(function () {
        $scope.notice("服务器异常，请稍后再试");
    });

});