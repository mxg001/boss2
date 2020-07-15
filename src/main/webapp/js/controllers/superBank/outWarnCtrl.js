/**
 * 出款余额不足预警
 **/
angular.module('inspinia').controller('outWarnCtrl',function($scope,$http,i18nService){
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    i18nService.setCurrentLang('zh-cn');
    $scope.baseInfo = {};//出款预警基本信息
    $scope.resetForm = function(){
        $scope.record = {
            createDateStart:moment(new Date().getTime()-7*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
            createDateEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'};//充值记录
    }
    $scope.resetForm();

    //获取出款预警基本信息
    $scope.getWarnInfo = function(){
        $http({
            url:'superBank/getOutWarnInfo',
            method:'GET'
        }).success(function(result){
            if(result.status){
                $scope.baseInfo.warnPhone = result.data.warnPhone;
                $scope.baseInfo.warnAmount = result.data.warnAmount;
            }
        });
    }
    //获取出款账户余额
    $scope.getWarnAccount = function(){
        $scope.getWarnAccountStatus = true;
        $http({
            url:'superBank/getOutWarnAccount',
            method:'GET'
        }).success(function(result){
            $scope.getWarnAccountStatus = false;
            if(result.status){
                $scope.baseInfo.accountBalance = result.data;
            }
        });
    }
    //获取出款账户充值记录
    $scope.getRechargeList = function(){
        $scope.submitting = true;
        $scope.loadImg = true;
        $http({
            url:'superBank/getRechargeList?pageNo=' + $scope.paginationOptions.pageNo + '&pageSize=' + $scope.paginationOptions.pageSize,
            method:'POST',
            data: $scope.record
        }).success(function(result){
            $scope.submitting = false;
            $scope.loadImg = false;
            if(result.status){
                $scope.rechargeGrid.data = result.data.result;
                $scope.rechargeGrid.totalItems = result.data.totalCount;
            }
        });
    }

    $scope.getWarnInfo();
    $scope.getWarnAccount();
    $scope.getRechargeList();

    $scope.columnDefs = [
        {field: 'createDate',displayName: '充值时间',pinnable: false,sortable: false,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'amountBalance',displayName: '充值前余额',pinnable: false,sortable: false},
        {field: 'rechargeAmount',displayName: '充值金额',pinnable: false,sortable: false},
        {field: 'userName',displayName: '操作人',pinnable: false,sortable: false}
        ];

    $scope.rechargeGrid = {
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        enableHorizontalScrollbar: false,        //横向滚动条
        enableVerticalScrollbar : false,  		//纵向滚动条
//		rowHeight:35,
        columnDefs: $scope.columnDefs,
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            $scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.paginationOptions.pageNo = newPage;
                $scope.paginationOptions.pageSize = pageSize;
                $scope.getRechargeList();
            });
        }
    };

    //修改出款预警的基本信息
    $scope.updateWarnInfo = function () {
        $scope.updateWarnInfoStatus = true;
        $http({
            url:'superBank/updateOutWarn',
            method:'POST',
            data: {warnPhone: $scope.baseInfo.warnPhone, warnAmount: $scope.baseInfo.warnAmount}
        }).success(function(result){
            $scope.updateWarnInfoStatus = false;
            $scope.notice(result.msg);
        });
    }
    //账户充值
    $scope.recharge = function(){
        $scope.rechargeStatus = true;
        $http({
            url:'superBank/recharge',
            method:'POST',
            data: {amount: $scope.amount}
        }).success(function(result){
            $scope.rechargeStatus = false;
           $scope.notice(result.msg);
           if(result.status){
               $scope.getWarnAccount();
               $scope.getRechargeList();
               $scope.amount = "";
           }
        });
    }
});

