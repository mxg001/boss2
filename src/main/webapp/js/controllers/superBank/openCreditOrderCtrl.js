/**
 * 开通办理信用卡订单查询
 */
angular.module('inspinia', ['infinity.angular-chosen']).controller('openCreditOrderCtrl',function($scope,$http,i18nService,$document,SweetAlert,$timeout){
    //数据源
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    //订单类型:1代理授权 2信用卡申请 3收款 4还款申请 5贷款注册 6贷款批贷 7还款 8开通办理信用卡
    // $scope.statusList = [{text:"全部",value:""}];
    $scope.statusList = [{text:"全部",value:""},{text:"待支付",value:"2"},{text:"已完成",value:"5"},{text:"回收站",value:"9"}];//订单状态
    $scope.resetForm = function () {
        $scope.baseInfo = {orderType:"1",status:"",orgId:-1,oneUserCode:"",refundStatus:"",
            createDateStart:moment(new Date().getTime()-24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
            createDateEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
            accountStatus:"",oneUserType:"",twoUserType:"",thrUserType:"",fouUserType:"",
            userCode:""
        };
    }
    $scope.resetForm();
    $scope.condition = {conditionStatus: false, conditionMsg: '全部条件'};
    $scope.changeCondition = function(){
        $scope.condition.conditionStatus = !$scope.condition.conditionStatus;
        if($scope.condition.conditionStatus){
            //清空子菜单的条件
            clearSubCondition();
            $scope.condition.conditionMsg = '清空收起';
        } else {
            $scope.condition.conditionMsg = '全部条件';
        }
    }
    function clearSubCondition(){
        $scope.baseInfo.userCode = "";
        $scope.baseInfo.oneUserCode = "";
        $scope.baseInfo.twoUserCode = "";
        $scope.baseInfo.thrUserCode = "";
        $scope.baseInfo.fouUserCode = "";
        $scope.baseInfo.userName = "";
        $scope.baseInfo.oneUserName = "";
        $scope.baseInfo.twoUserName = "";
        $scope.baseInfo.thrUserName = "";
        $scope.baseInfo.fouUserName = "";
        $scope.baseInfo.oneUserType = "";
        $scope.baseInfo.twoUserType = "";
        $scope.baseInfo.thrUserType = "";
        $scope.baseInfo.fouUserType = "";
        $scope.baseInfo.shareUserPhone = "";
        $scope.baseInfo.payChannel = "";
        $scope.baseInfo.payChannelNo = "";
        $scope.baseInfo.payOrderNo = "";
        $scope.baseInfo.payDateStart = "";
        $scope.baseInfo.payDateEnd = "";
    }

    $scope.columnDefs = [
        {field: 'orderNo',displayName: '订单ID',width: 200,pinnable: false,sortable: false},
        {field: 'orgId',displayName: '所属组织',width: 150,pinnable: false,sortable: false},
        {field: 'orgName',displayName: '组织名称',width: 150,pinnable: false,sortable: false},
        {field: 'status',displayName: '订单状态',width: 150,pinnable: false,sortable: false},
        {field: 'payChannel',displayName: '收款通道',width: 150,pinnable: false,sortable: false},
        {field: 'payChannelNo',displayName: '收款通道商户号',width: 150,pinnable: false,sortable: false},
        {field: 'payOrderNo',displayName: '收款通道流水号',width: 150,pinnable: false,sortable: false},
        {field: 'userCode',displayName: '贡献人ID',width: 150,pinnable: false,sortable: false},
        {field: 'userName',displayName: '贡献人名称',width: 150,pinnable: false,sortable: false},
        {field: 'shareUserPhone',displayName: '贡献人手机号',width: 150,pinnable: false,sortable: false},
        {field: 'price',displayName: '售价',width: 150,pinnable: false,sortable: false},
        {field: 'totalBonus',displayName: '品牌发放总奖金',width: 150,pinnable: false,sortable: false},
        {field: 'createDate',displayName: '创建时间',width: 150,pinnable: false,sortable: false,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'payDate',displayName: '支付时间',width: 150,pinnable: false,sortable: false,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'payOrderNo',displayName: '关联支付订单',width: 120,pinnable: false,sortable: false},
        {field: 'oneUserCode',displayName: '一级编号',width: 120,pinnable: false,sortable: false},
        {field: 'oneUserName',displayName: '一级名称',width: 150,pinnable: false,sortable: false},
        {field: 'oneUserType',displayName: '一级身份',width: 150,pinnable: false,sortable: false},
        {field: 'oneUserProfit',displayName: '一级分润',width: 150,pinnable: false,sortable: false},
        {field: 'twoUserCode',displayName: '二级编号',width: 120,pinnable: false,sortable: false},
        {field: 'twoUserName',displayName: '二级名称',width: 150,pinnable: false,sortable: false},
        {field: 'twoUserType',displayName: '二级身份',width: 150,pinnable: false,sortable: false},
        {field: 'twoUserProfit',displayName: '二级分润',width: 150,pinnable: false,sortable: false},
        {field: 'thrUserCode',displayName: '三级编号',width: 120,pinnable: false,sortable: false},
        {field: 'thrUserName',displayName: '三级名称',width: 150,pinnable: false,sortable: false},
        {field: 'thrUserType',displayName: '三级身份',width: 150,pinnable: false,sortable: false},
        {field: 'thrUserProfit',displayName: '三级分润',width: 150,pinnable: false,sortable: false},
        {field: 'fouUserCode',displayName: '四级编号',width: 120,pinnable: false,sortable: false},
        {field: 'fouUserName',displayName: '四级名称',width: 150,pinnable: false,sortable: false},
        {field: 'fouUserType',displayName: '四级身份',width: 150,pinnable: false,sortable: false},
        {field: 'fouUserProfit',displayName: '四级分润',width: 150,pinnable: false,sortable: false},
        {field: 'orgProfit',displayName: '品牌商分润',width: 150,pinnable: false,sortable: false},
        {field: 'plateProfit',displayName: '平台分润',width: 150,pinnable: false,sortable: false},
        {field: 'accountStatus',displayName: '记账状态',width: 150,pinnable: false,sortable: false},
        {field: 'action',displayName: '操作',width: 120,pinnedRight:true,sortable: false,editable:true,cellTemplate:
        '<a class="lh30" target="_blank" ng-show="grid.appScope.hasPermit(\'superBank.openCreditDetail\')" '
        + 'ui-sref="superBank.openCreditDetail({orderNo:row.entity.orderNo})">详情</a>'}
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
        $http({
            url: 'superBank/orderManager?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
            data: $scope.baseInfo,
            method:'POST'
        }).success(function (result) {
            $scope.submitting = false;
            $scope.loadImg = false;
            if (!result.status){
                $scope.notice(result.msg);
                return;
            }
            $scope.orderGrid.data = result.data.page.result;
            $scope.orderGrid.totalItems = result.data.page.totalCount;
            $scope.orderMainSum = result.data.orderMainSum;
        }).error(function () {
            $scope.submitting = false;
            $scope.loadImg = false;
            $scope.notice('服务器异常,请稍后再试.');
        });
    };

    //获取所有的银行家组织
    $scope.orgInfoList = [{orgId:-1,orgName:"全部"}];
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
    //获取所有的银行
    $scope.bankList = [{id:-1,bankName:"全部"}];
    $scope.getBankList = function () {
        $http({
            url:"superBank/banksList",
            method:"POST"
        }).success(function(msg){
            if(msg.status){
                $scope.bankList = msg.data;
                $scope.bankList.unshift({id:-1,bankName:"全部"});
            } else {
                $scope.notice("获取银行信息异常");
            }
        }).error(function(){
            $scope.notice("获取银行信息异常");
        })
    };
    $scope.getBankList();

    //异步获取用户
    $scope.userInfoList = [{userCode: "", userName: "全部"}];
    var oldValue="-1";
    var timeout="";
    function getUserInfoList(value){
        var newValue=value;
        if(newValue != oldValue) {
            if (timeout)
                $timeout.cancel(timeout);
            timeout = $timeout(function () {
                    $http({
                        url: "superBank/selectUserInfoList",
                        data: "userCode=" + value,
                        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                        method: "POST"
                    }).success(function (result) {
                        if (result.status) {
                            if (result.data.length == 0) {
                                $scope.userInfoList = [{userCode: "", userName: "全部"}];
                            } else {
                                $scope.userInfoList = result.data;
                                $scope.userInfoList.unshift({userCode: "", userName: "全部"});
                            }
                            oldValue = value;
                        } else {
                            $scope.notice(result.msg);
                        }
                    }).error(function () {
                        $scope.notice("系统异常，请稍候再试");
                    })
                }
                , 800);
        }
    }
    $scope.getUserInfoList = getUserInfoList;
    $scope.getUserInfoList("");

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
                    // $http({
                    //    url:"superBank/exportOrder?baseInfo=" +angular.toJson($scope.baseInfo),
                    //    method:"GET"
                    // });
                    location.href="superBank/exportOpenCredit?baseInfo=" +angular.toJson($scope.baseInfo);
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