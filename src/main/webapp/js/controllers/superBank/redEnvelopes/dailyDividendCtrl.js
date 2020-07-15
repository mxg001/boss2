/**
 * 领主领地收益表
 */
angular.module('inspinia').controller('dailyDividendCtrl', function($scope, $http,$stateParams,i18nService){
    //数据源
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    $scope.statusList=[{text:"全部",value:""},{text:"未领取",value:"0"},{text:"已领取",value:"1"}];
    $scope.statusStr=[{text:"未领取",value:"0"},{text:"已领取",value:"1"}];
    $scope.resetForm = function () {
        $scope.baseInfo = {
            status:"",openProvince:"全部",orgId:-1,phone:"",
            profitDateStart:moment(new Date().getTime()-24*2*60*60*1000).format('YYYY-MM-DD'),
            profitDateEnd:moment(new Date().getTime()).format('YYYY-MM-DD')};
    }
    $scope.resetForm();
    //查询所有银行家组织
    $scope.orgs=[];
    $scope.getAllOrg = function(){
        $http({
            url:"superBank/getOrgInfoList",
            method:"POST"
        }).success(function(msg){
            if(msg.status){
                $scope.orgs = msg.data;
                $scope.orgs.unshift({orgId:-1,orgName:"全部"});
            }
        }).error(function(){
            $scope.notice("获取组织列表异常");
        });
    };
    $scope.getAllOrg();
    
    $scope.columnDefs = [
        {field: 'profitDate',displayName: '收益日期',pinnable: false,sortable: false,width:150},
        {field: 'territoryId',displayName: '领地ID',pinnable: false,sortable: false,width:150},
        {field: 'provinceName',displayName: '省',pinnable: false,sortable: false,width:150},
        {field: 'cityName',displayName: '市',pinnable: false,sortable: false,width:150},
        {field: 'regionName',displayName: '区',pinnable: false,sortable: false,width:150},
        {field: 'territoryAvgPrice',displayName: '领地均价',pinnable: false,sortable: false,width:150},
        {field: 'territoryPrice',displayName: '领地价格',pinnable: false,sortable: false,width:150},
        {field: 'adjustRatio',displayName: '调节系数',pinnable: false,sortable: false,width:150},
        {field: 'profitAmount',displayName: '收益总金额',pinnable: false,sortable: false,width:150},
        {field: 'businessBasicBonusAmount',displayName: '业务基准分红',pinnable: false,sortable: false,width:150},
        {field: 'businessBonusAmount',displayName: '业务分红',pinnable: false,sortable: false,width:150},
        {field: 'premiumTotalAmount',displayName: '领地总溢价',pinnable: false,sortable: false,width:150},
        {field: 'premiumTotalAmountConfStr',displayName: '领地总溢价分红比例',pinnable: false,sortable: false,width:150},
        {field: 'premiumBonusAmount',displayName: '领地总溢价分红总奖金',pinnable: false,sortable: false,width:150},
        {field: 'premiumTradeBasicAmount',displayName: '领地溢价交易基准分红',pinnable: false,sortable: false,width:150},
        {field: 'premiumTradeAmount',displayName: '领地溢价交易分红',pinnable: false,sortable: false,width:150},
        {field: 'randomBonusTotalAmount',displayName: '每日随机分红总金额',pinnable: false,sortable: false,width:150},
        {field: 'randomBonusBasicAmount',displayName: '每日随机基准分红',pinnable: false,sortable: false,width:150},
        {field: 'randomBonusAmount',displayName: '每日随机实际分红',pinnable: false,sortable: false,width:150},
        {field: 'userCode',displayName: '领取用户编号',pinnable: false,sortable: false,width:150},
        {field: 'userName',displayName: '领取用户姓名',pinnable: false,sortable: false,width:150},
        {field: 'phone',displayName: '领取用户手机号',pinnable: false,sortable: false,width:150},
        {field: 'orgName',displayName: '领取用户所属组织',pinnable: false,sortable: false,width:150},
        {field: 'status',displayName: '领取状态',pinnable: false,sortable: false,width:150,cellFilter:"formatDropping:" + angular.toJson($scope.statusStr)},
        {field: 'receiveTime',displayName: '领取时间',pinnable: false,sortable: false,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:150},
    ];

    $scope.grid = {
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
//		rowHeight:35,
        columnDefs: $scope.columnDefs,
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            $scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.paginationOptions.pageNo = newPage;
                $scope.paginationOptions.pageSize = pageSize;
                $scope.query();
            });
        }
    };


    $scope.query = function () {
        if ($scope.selected != undefined && $scope.selected != null) {
            $scope.baseInfo.provinceName = $scope.selected.name;
            if ($scope.selected2 != undefined && $scope.selected2 != null) {
                $scope.baseInfo.cityName = $scope.selected2.name;
                if ($scope.selected3 != undefined && $scope.selected3 != null) {
                    $scope.baseInfo.regionName = $scope.selected3.name;
                } else {
                    $scope.baseInfo.regionName = "";
                }
            } else {
                $scope.baseInfo.cityName = "";
                $scope.baseInfo.regionName = "";
            }
        } else {
            $scope.baseInfo.provinceName = "";
            $scope.baseInfo.cityName = "";
            $scope.baseInfo.regionName = "";
        }

        $scope.submitting = true;
        $scope.loadImg = true;
        $http({
            url: 'manor/dailyDividend?pageNo=' + $scope.paginationOptions.pageNo + '&pageSize=' + $scope.paginationOptions.pageSize,
            data: $scope.baseInfo,
            method: 'POST'
        }) .success(function (result) {
                    $scope.submitting = false;
                    $scope.loadImg = false;
                    if (!result.status) {
                        $scope.notice(result.msg);
                        return;
                    }
                    $scope.grid.data = result.data.page.result;
                    $scope.grid.totalItems = result.data.page.totalCount;
                    $scope.totalInfo = result.data.totalInfo;
                    $scope.manorMainSum = result.data.manorMainSum;
                }).error(function () {
                    $scope.submitting = false;
                    $scope.loadImg = false;
                    $scope.notice('服务器异常,请稍后再试.');
                })
    }
    //省市区
    $scope.list = LAreaDataBaidu;
    $scope.c = function () {
        $scope.selected2 = "";
        $scope.selected3 = "";
    };

    $scope.c2 = function () {
        $scope.selected3 = "";
    };
});
