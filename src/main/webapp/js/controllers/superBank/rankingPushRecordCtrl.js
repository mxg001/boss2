/**
 * 用户奖金发放记录
 */
angular.module('inspinia').controller('rankingPushRecordCtrl',function($scope,$http,i18nService,$document,SweetAlert){
    //数据源
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    $scope.rankingTypes = [{text:"全部",value:""},{text:"周榜",value:"0"},{text:"月榜",value:"1"},{text:"年榜",value:"2"}];
    $scope.pushStatusList = [{text:"全部",value:""},{text:"已发放",value:"1"},{text:"已移除",value:"2"}];
    $scope.accountStatusList = [{text:"全部",value:""},{text:"未记账",value:"0"},{text:"已记账",value:"1"},{text:"记账失败",value:"2"}];
    $scope.rankingTypeStr ='[{text:"周榜",value:"0"},{text:"月榜",value:"1"},{text:"年榜",value:"2"}]';
    $scope.pushStatusStr ='[{text:"未发放",value:"0"},{text:"已发放",value:"1"},{text:"已移除",value:"2"}]';
    $scope.accountStatusStr ='[{text:"未记账",value:"0"},{text:"已记账",value:"1"},{text:"记账失败",value:"2"}]';
    $scope.dataTypes = [{text:"全部",value:""},{text:"收益金额",value:"0"},{text:"会员数",value:"1"},{text:"用户数",value:"2"},{text:"订单数",value:"3"}];
    $scope.dataTypesStr = '[{text:"全部",value:""},{text:"收益金额",value:"0"},{text:"会员数",value:"1"},{text:"用户数",value:"2"},{text:"订单数",value:"3"}]';
    
    $scope.resetForm = function () {
        $scope.baseInfo = {rankingNo:"",rankingType:"",userCode:"",
            phone:"",pushStatus:"",orgId:"",accountStatus:"",dataType:""
        };
    }
    $scope.resetForm();

    //查询所有银行家组织
    $scope.orgInfoList = [];
    $scope.getOrgInfoList = function () {
        $http({
            url:"superBank/getOrgInfoList",
            method:"POST"
        }).success(function(msg){
            if(msg.status){
                $scope.orgInfoList = msg.data;
                $scope.orgInfoList.unshift({orgId:"",orgName:"全部"});
            }
        }).error(function(){
            $scope.notice("获取组织信息异常");
        })
    };
    $scope.getOrgInfoList();

    $scope.columnDefs = [
        {field: 'orderNo',displayName: '获奖订单号',width: 200,pinnable: false,sortable: false},
        {field: 'rankingNo',displayName: '排行榜编号',width: 150,pinnable: false,sortable: false},
        {field: 'batchNo',displayName: '期号',width: 150,pinnable: false,sortable: false},
        {field: 'ruleNo',displayName: '规则编号',width: 150,pinnable: false,sortable: false},
        {field: 'rankingName',displayName: '排行榜名称',width: 150,pinnable: false,sortable: false},
        {field: 'rankingType',displayName: '统计周期',width: 150,pinnable: false,sortable: false,cellFilter:"formatDropping:" + $scope.rankingTypeStr},
        {field: 'dataType',displayName: '统计数据',width: 150,pinnable: false,sortable: false,cellFilter:"formatDropping:" + $scope.dataTypesStr},
        {field: 'orgName',displayName: '所属组织',width: 150,pinnable: false,sortable: false},
        {field: 'showNo',displayName: '排名',width: 150,pinnable: false,sortable: false},
        {field: 'userName',displayName: '用户姓名',width: 150,pinnable: false,sortable: false},
        {field: 'deNickName',displayName: '微信昵称',width: 150,pinnable: false,sortable: false},
        {field: 'userCode',displayName: '用户ID',width: 150,pinnable: false,sortable: false},
        {field: 'phone',displayName: '手机号',width: 150,pinnable: false,sortable: false},
        {field: 'rankingData',displayName: '统计总额',width: 150,pinnable: false,sortable: false},
        {field: 'rankingLevel',displayName: '获奖等级',width: 150,pinnable: false,sortable: false},
        {field: 'rankingMoney',displayName: '获奖金额',width: 150,cellFilter:"currency:''",pinnable: false,sortable: false},
        {field: 'pushStatus',displayName: '用户发放状态',width: 150,pinnable: false,sortable: false,cellFilter:"formatDropping:" + $scope.pushStatusStr},
        {field: 'accountStatus',displayName: '记账状态',width: 150,pinnable: false,sortable: false,cellFilter:"formatDropping:" + $scope.accountStatusStr},
        {field: 'pushTime',displayName: '发放时间',width: 150,pinnable: false,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'startTime',displayName: '统计开始时间',width: 150,pinnable: false,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'endTime',displayName: '统计结束时间',width: 150,pinnable: false,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'}
    ];

    $scope.orderGrid = {
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
        $scope.submitting = true;
        $scope.loadImg = true;
        $http.post('superBank/getRankingPushRecord',"baseInfo=" + angular.toJson($scope.baseInfo) + "&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+
            $scope.paginationOptions.pageSize, {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
        ).success(function (result) {
            $scope.submitting = false;
            $scope.loadImg = false;
            if (!result.status){
                $scope.notice(result.msg);
                return;
            }
            $scope.peopleCount =result.data.page.totalCount;
            $scope.totalMoneyCount =result.data.totalMoneyCount;
            $scope.pushTotalMoneyCount =result.data.pushTotalMoneyCount;

            $scope.orderGrid.data = result.data.rankingPushRecordInfoList;
            $scope.orderGrid.totalItems = result.data.page.totalCount;
            $scope.orderMainSum = result.data.orderMainSum;
        }).error(function () {
            $scope.submitting = false;
            $scope.loadImg = false;
            $scope.notice('服务器异常,请稍后再试.');
        });
    };

    // 导出
    $scope.exportInfo = function () {
        SweetAlert.swal({
                title: "确定导出吗？",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    location.href="superBank/exportRankingPushRecord?baseInfo=" +encodeURI(encodeURI(angular.toJson($scope.baseInfo)));
                }
            });
    };

    //页面绑定回车事件
    $document.bind("keypress", function(event) {
        $scope.$apply(function (){
            if(event.keyCode == 13){
                $scope.query();
            }
        })
    });
});