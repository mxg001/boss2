/**
 * 信用卡管家-用户详情
 */
angular.module('inspinia').controller('queryCardAndRewardDetailCtrl',function($scope, $http, $state,$stateParams,i18nService,SweetAlert){

	i18nService.setCurrentLang('zh-cn');

	
	$scope.riskStatusaa=[{text:"全部",value:""},{text:"已赠送",value:1},{text:"赠送失败",value:2},{text:"未赠送",value:3}]
	$scope.givenTypeaa=[{text:"全部",value:""},{text:"鼓励金",value:1},{text:"积分",value:2},{text:"刷卡金",value:3}]
	$scope.orderStatus=[{text:"信用卡办理",value:2},{text:"贷款",value:6}]
	
	$scope.info={};


    $scope.myGrid = {
		columnDefs: [
		             { field: 'id',displayName:'编号',width:150},
		 			{ field: 'username',displayName:'商户名称',width:150},
		 			{ field: 'phone',displayName:'手机号码',width:150},
		 			{ field: 'orgName',displayName:'机构名称',width:150},
		 			{ field: 'mechName',displayName:'组织名称',width:150},
		 			{ field: 'orderType',displayName:'订单类型',width:150,cellFilter:"formatDropping:"+angular.toJson($scope.orderStatus)},
		 			{ field: 'orderNo',displayName:'订单号',width:150},
		 			{ field: 'statusName',displayName:'订单状态',width:150},
		 			{ field: 'transAmount',displayName:'交易金额',width:150},
		 			{field: 'transTime',displayName: '交易时间',width: 150,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
		 			{ field: 'givenChannelName',displayName:'赠送渠道',width:150},
		 			{ field: 'givenType',displayName:'赠送类型',width:150,cellFilter:"formatDropping:"+angular.toJson($scope.givenTypeaa)},
		 			{ field: 'couponAmount',displayName:'赠送面值',width:150},
		 			{ field: 'effectiveDays',displayName:'有效期' ,width:150},
		 			{ field: 'givenStatus',displayName:'赠送状态',width:150,cellFilter:"formatDropping:"+angular.toJson($scope.riskStatusaa)},
		 			{ field: 'operUsername',displayName:'操作人',width:150 },
		 			{field: 'updateTime',displayName: '操作时间',width: 150,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
		 			{field: 'successTime',displayName: '赠送成功时间',width: 150,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
		 			{field: 'successTime',displayName: '鼓励金生效时间',width: 150,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
		 			{field: 'operTime',displayName: '鼓励金到期时间',width: 150,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'}
		]
	};

	$http({
        url:"cardAndReward/selectCardLoanHeartenLog?id="+$stateParams.userNo,
        method:"GET"
    }).success(function(data){
        if (data.status){
            $scope.info = data.info;
            $scope.myGrid.data = data.cardList;
        } else {
        	$scope.notice(data.msg);
        }
    }).error(function () {
    });

	//导出
	$scope.exportInfo=function(){
		SweetAlert.swal({
			title: "确认导出？",
			showCancelButton: true,
			confirmButtonColor: "#DD6B55",
			confirmButtonText: "确定",
			cancelButtonText: "取消",
			closeOnConfirm: true,
			closeOnCancel: true 
		},
		function (isConfirm) {
			if (isConfirm) {
				location.href="cardAndReward/exportSendList?id="+$stateParams.userNo;
			}
		});
	}
	
});