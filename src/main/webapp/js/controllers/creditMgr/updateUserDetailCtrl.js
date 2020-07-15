/**
 * 信用卡管家-修改用户
 */
angular.module('inspinia').controller('updateUserDetailCtrl',function($scope, $http, $state,$stateParams,i18nService){

	i18nService.setCurrentLang('zh-cn');

	$scope.info={};

    $scope.userTypeSelect = [{text:"普通",value:'0'},{text:"月付费",value:'1'}];
    $scope.cardStatusSelect = [{text:"已删除",value:'0'},{text:"正常",value:'1'}];
    $scope.daySelect = [];
    for(var i = 1; i <= 28; i++){
		$scope.daySelect.push({value:i + "", text:"每月" + i + "日"});
	}

    $scope.myGrid = {
		columnDefs: [
			{field: 'id',displayName: '卡片ID',width: 150},
			{field: 'cardNo',displayName: '卡号',width: 150},
			{field: 'bankName',displayName: '银行名称',width: 150},
			{field: 'userName',displayName: '姓名',width: 150},
			{field: 'mobileNo',displayName: '预留手机',width: 150},
			{field: 'totalAmount',displayName: '总额度',width: 150},
			{field: 'statementDate',displayName: '账单日',width: 150,cellFilter:"formatDropping:"+ angular.toJson($scope.daySelect)},
			{field: 'repaymentDate',displayName: '还款日',width: 150,cellFilter:"formatDropping:"+ angular.toJson($scope.daySelect)},
			{field: 'remindTime',displayName: '还款提醒时间',width: 150,cellFilter:"formatDropping:"+ angular.toJson($scope.daySelect)},
			{field: 'cardStatus',displayName: '状态',width: 120,cellFilter:"formatDropping:"+angular.toJson($scope.cardStatusSelect)}
		]
	};

	$http({
        url:"cmUserManage/selectUserDetailEdit?userNo="+$stateParams.userNo,
        method:"GET"
    }).success(function(data){
        if (data.status){
            $scope.info = data.info;
            $scope.myGrid.data = data.cardList;
        } else {
        	$scope.notice(data.msg);
        }
    }).error(function () {
        $scope.notice("服务器异常，请稍后再试");
    });

	$scope.submit = function () {
		$http({
	        url:"cmUserManage/updateUserInfo?userNo="+$stateParams.userNo+"&mobileNo="+$scope.info.mobileNo,
	        method:"GET"
	    }).success(function(data){
	    	$scope.notice(data.msg);
	        if (data.status){
	        	$scope.goback();
	        }
	    }).error(function () {
	        $scope.notice("服务器异常，请稍后再试");
	    });
    };

});