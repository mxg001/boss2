/**
 * 超级银行家组织管理
 */
angular.module('inspinia').controller('orgInfoManagerCtrl',function($scope,$http,i18nService,$document,SweetAlert){
    //数据源
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    $scope.accountStatusList = [{text:"全部",value:""},{text:"未开户",value:"0"}
                                    ,{text:"已开户",value:"1"}];
    $scope.resetForm = function () {
        $scope.baseInfo = {orgId:-1,accountStatus:""};
    }
    $scope.isOpenList=[{text:"否",value:null},{text:"否",value:"0"},{text:"是",value:"1"}];
    $scope.resetForm();

    $scope.columnDefs = [
        {field: 'orgId',displayName: '品牌组织ID',width: 120,pinnable: false,sortable: false},
        {field: 'orgName',displayName: '组织名称',width: 120,pinnable: false,sortable: false},
        {field: 'isOpen',displayName: '是否外放',width: 120,pinnable: false,sortable: false,cellFilter:"formatDropping:" + angular.toJson($scope.isOpenList)},
        // {field: 'orgLogo',displayName: 'logo',width: 150,pinnable: false,sortable: false,
        //     cellTemplate:'<img style="width: 140px; height: 36px;" ng-show="row.entity.orgLogoUrl" ng-src="{{row.entity.orgLogoUrl}}" />'},
        // {field: 'profitSwitch',displayName: '红包开关',width:150,pinnable: false,sortable: false,cellTemplate:
        //     '<span ng-show="grid.appScope.hasPermit(\'agent.switchProfitStatus\')"><switch class="switch switch-s" ng-model="row.entity.profitSwitch" ng-change="grid.appScope.switchProfitStatus(row)" /></span>'
        //     +'<span ng-show="!grid.appScope.hasPermit(\'agent.switchProfitStatus\')"> <span ng-show="row.entity.profitSwitch==1">开启</span><span ng-show="row.entity.profitSwitch==0">关闭</span></span>'
        // },
        // {field: 'promotionSwitch',displayName: '单个订单红包超过品牌分润',width:150,pinnable: false,sortable: false,cellTemplate:
        //     '<span ng-show="grid.appScope.hasPermit(\'agent.agentPromotion\')"><switch class="switch switch-s" ng-model="row.entity.promotionSwitch" ng-change="grid.appScope.switchPromotionStatus(row)" /></span>'
        //     +'<span ng-show="!grid.appScope.hasPermit(\'agent.agentPromotion\')"> <span ng-show="row.entity.profitSwitch==1">开启</span><span ng-show="row.entity.profitSwitch==0">关闭</span></span>'
        // },
        {field: 'publicAccount',displayName: '微信公众号',width: 150,pinnable: false,sortable: false},
        {field: 'superOrgcode',displayName: '超级还组织编号',width: 200,pinnable: false,sortable: false},
        {field: 'v2Orgcode',displayName: 'V2组织编号',width: 180,pinnable: false,sortable: false},
        {field: 'v2AgentNumber',displayName: 'V2代理商编号',width: 180,pinnable: false,sortable: false},
        {field: 'agentPrice',displayName: '代理价格',width: 120,pinnable: false,sortable: false},
        {field: 'agentCost',displayName: '品牌代理成本',width: 180,pinnable: false,sortable: false},
        {field: 'agentPushCost',displayName: '品牌发放总奖金',width: 180,pinnable: false,sortable: false},
        {field: 'openRepayPrice',displayName: '超级还激活售价',width: 180,pinnable: false,sortable: false},
        {field: 'openRepayCost',displayName: '品牌超级还激活成本',width: 180,pinnable: false,sortable: false},
        {field: 'openRepayPushCost',displayName: '品牌超级还激活发放总奖金',width: 180,pinnable: false,sortable: false},
        // {field: 'receiveWxCost',displayName: '品牌收款微信成本',width: 180,pinnable: false,sortable: false},
        // {field: 'receiveZfbCost',displayName: '品牌收款支付宝成本',width: 180,pinnable: false,sortable: false},
        {field: 'receiveKjCost',displayName: '品牌收款快捷成本扣率',width: 180,pinnable: false,sortable: false},
        {field: 'receivePushCost',displayName: '品牌快捷收款发放奖金扣率',width: 180,pinnable: false,sortable: false},
        {field: 'repaymentCost',displayName: '品牌超级还成本扣率',width: 180,pinnable: false,sortable: false},
        {field: 'repaymentPushCost',displayName: '品牌超级还发放奖金扣率',width: 180,pinnable: false,sortable: false},
        {field: 'upManagerNum',displayName: '升经理需发展个数',width: 180,pinnable: false,sortable: false},
        {field: 'upManagerCardnum',displayName: '升经理需办卡个数',width: 180,pinnable: false,sortable: false},
        {field: 'upBankerNum',displayName: '升银行家需发展个数',width: 180,pinnable: false,sortable: false},
        {field: 'upBankerCardnum',displayName: '升银行家需办卡个数',width: 180,pinnable: false,sortable: false},
        {field: 'accountStatus',displayName: '开户状态',width: 120,pinnable: false,sortable: false,
            cellFilter:"formatDropping:" + angular.toJson($scope.accountStatusList)},
        {field: 'remark',displayName: '备注',width: 150,pinnable: false,sortable: false},
        {field: 'action',displayName: '操作',width: 300,pinnedRight:true,sortable: false,editable:true,cellTemplate:
    	'<a class="lh30" ng-show="grid.appScope.hasPermit(\'superBank.orgBanksConfig\')" '
        + 'ui-sref="superBank.orgBanksConfig({orgId:row.entity.orgId})">  信用卡银行配置  </a>'
        +'<a class="lh30" ng-show="grid.appScope.hasPermit(\'superBank.orgLoansConfig\')" '
        + 'ui-sref="superBank.orgLoansConfig({orgId:row.entity.orgId})"> 贷款机构配置  </a>'
        +'<a class="lh30" ng-show="grid.appScope.hasPermit(\'superBank.updateOrgInfo\')" '
        + 'ui-sref="superBank.updateOrgInfo({orgId:row.entity.orgId})"> 修改</a>'
        + '<a class="lh30" ng-show="grid.appScope.hasPermit(\'superBank.updateOrgInfo\')' +
        '&&row.entity.accountStatus==0"'
                + 'ng-click="grid.appScope.openAccount(row.entity.v2AgentNumber)"> | 开户</a>'}
    ];

    $scope.orgInfoGrid = {
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
        $http({
            url: 'superBank/orgInfoManager?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
            data: $scope.baseInfo,
            method:'POST'
        }).success(function (msg) {
            $scope.submitting = false;
            $scope.loadImg = false;
            if (!msg.status){
                $scope.notice(msg.msg);
                return;
            }
            $scope.orgInfoGrid.data = msg.data.result;
            $scope.orgInfoGrid.totalItems = msg.data.totalCount;
        }).error(function (msg) {
            $scope.submitting = false;
            $scope.loadImg = false;
            $scope.notice('服务器异常,请稍后再试.');
        });
    };

    //获取所有的银行家组织
    $scope.orgInfoList = [];
    $scope.getOrgInfoList = function () {
        $http({
            url:"superBank/getOrgInfoList",
            method:"POST"
        }).success(function(msg){
            if(msg.status){
                $scope.orgInfoList = msg.data;
                $scope.orgInfoList.unshift({orgId:-1,orgName:"全部"});
            }
        }).error(function(){
            $scope.notice("获取组织信息异常");
        })
    };
    $scope.getOrgInfoList();

    // // 导出
    // $scope.exportInfo = function (id) {
    //     SweetAlert.swal({
    //             title: "确定导出吗？",
    //             type: "warning",
    //             showCancelButton: true,
    //             confirmButtonColor: "#DD6B55",
    //             confirmButtonText: "确定",
    //             cancelButtonText: "取消",
    //             closeOnConfirm: true,
    //             closeOnCancel: true },
    //         function (isConfirm) {
    //             if (isConfirm) {
    //                 location.href="invitePrizes/exportInfo?info="+angular.toJson($scope.baseInfo);
    //             }
    //         });
    // };
    //
    // 开户
    $scope.openAccount = function (agentNo) {
        SweetAlert.swal({
                title: "确定开户吗？",
                type: "info",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http({
                        url: 'superBank/openAccount',
                        data: agentNo,
                        method:'POST'
                    }).success(function (msg) {
                        $scope.notice(msg.msg);
                        if (msg.status){
                            $scope.query();
                        }
                    }).error(function (msg) {
                        $scope.notice('服务器异常,请稍后再试.');
                    });
                }
            });
    };
    //
    // // 批量入账
    // $scope.batchRecorded = function (id) {
    //     selectedRows = $scope.gridApi.selection.getSelectedRows();
    //     if(selectedRows==null || selectedRows.length==0){
    //         $scope.notice('请选择要入账的条目');
    //         return;
    //     }
    //     validIds = [];
    //     angular.forEach(selectedRows,function(data){
    //         if (data.accountStatus != '1') {
    //             validIds[validIds.length] = data.id;
    //         }
    //     });
    //     if (validIds == null || validIds.length == 0) {
    //         $scope.notice('请选择可入账的条目');
    //         return;
    //     }
    //     SweetAlert.swal({
    //             title: "批量入账",
    //             text: "满足入账条件的数据有 " + validIds.length + " 条，是否确定入账？",
    //             type: "warning",
    //             showCancelButton: true,
    //             confirmButtonColor: "#DD6B55",
    //             confirmButtonText: "确定",
    //             cancelButtonText: "取消",
    //             closeOnConfirm: true,
    //             closeOnCancel: true },
    //         function (isConfirm) {
    //             if (isConfirm) {
    //                 $http({
    //                     url: 'invitePrizes/batchRecordAccount',
    //                     data: validIds,
    //                     method:'POST'
    //                 }).success(function (msg) {
    //                     $scope.notice(msg.msg);
    //                     if (msg.status){
    //                         $scope.query();
    //                     }
    //                 }).error(function (msg) {
    //                     $scope.notice('服务器异常,请稍后再试.');
    //                 });
    //             }
    //         });
    // }



    //页面绑定回车事件
    $document.bind("keypress", function(event) {
        $scope.$apply(function (){
            if(event.keyCode == 13){
                $scope.query();
            }
        })
    });
});