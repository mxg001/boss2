/**
 * 贷款订单查询
 */
angular.module('inspinia', ['infinity.angular-chosen']).controller('creditOrderInquiryCtrl',function($scope,$http,i18nService,$document,SweetAlert,$timeout){
    //数据源
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    //订单类型:1代理授权；2信用卡申请 3收款 4信用卡还款 5贷款注册 6贷款批贷
    // $scope.statusList = [{text:"全部",value:""}];
    $scope.productIdList = [{text: "全部", value: ""},{text:"已创建",value:"1"},{text:"待审核",value:"2"},{text:"已完成",value:"3"},{text:"审核不通过",value:"4"},{text:"回收站",value:"5"},{text:"已办理过",value:"6"}];//订单状态
    $scope.statusList = [{text:"全部",value:""},{text:"待支付",value:"1"},{text:"已支付",value:"2"},{text:"已完成",value:"3"},{text:"生成失败",value:"4"},{text:"退款成功",value:"5"},{text:"退款失败",value:"6"},{text:"已失效",value:"7"}];//记账状态
    $scope.payMethodList = [{text:"全部",value:""},{text:"微信支付",value:"1"},{text:"支付宝支付",value:"2"},{text:"快捷支付",value:"3"}];//奖金类型
    $scope.productNameList = [{text:"全部",value:""},{text:"深度报告类型",value:"1"},{text:"黑名单多头报告类型",value:"2"},{text:"信用评测报告类型",value:"3"}];
    $scope.resetForm = function () {
        $scope.baseInfo = {productId:"",status:"",orgId:-1,payMethod:"",reportType:"",
            generationTimeBegin:moment(new Date().getTime()-24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
            generationTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
        };
        clearSubCondition();
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
        // $scope.baseInfo.userCode = "";
        $scope.baseInfo.oneUserCode = "";
        $scope.baseInfo.twoUserCode = "";
        $scope.baseInfo.thrUserCode = "";
        $scope.baseInfo.fouUserCode = "";
        // $scope.baseInfo.userName = "";
        $scope.baseInfo.oneUserName = "";
        $scope.baseInfo.twoUserName = "";
        $scope.baseInfo.thrUserName = "";
        $scope.baseInfo.fouUserName = "";
        $scope.baseInfo.oneUserType = "";
        $scope.baseInfo.twoUserType = "";
        $scope.baseInfo.thrUserType = "";
        $scope.baseInfo.fouUserType = "";
        $scope.baseInfo.shareUserPhone = "";
        $scope.baseInfo.openProvince = "全部";
        $scope.baseInfo.openCity = "全部";
        $scope.baseInfo.openRegion = "全部";
        $scope.baseInfo.remark = null;
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
    $scope.columnDefs = [
        {field: 'yhjOrderNo',displayName: '订单ID',width: 200,pinnable: false,sortable: false},
        {field: 'orderNo',displayName: '大数据订单ID',width: 150,pinnable: false,sortable: false},
        {field: 'status',displayName: '订单状态',width: 150,pinnable: false,sortable: false},
        {field: 'payMethod',displayName: '支付方式',width: 150,pinnable: false,sortable: false},
        {field: 'payNo',displayName: '收款流水号',width: 200,pinnable: false,sortable: false},
        {field: 'orgName',displayName: '所属组织',width: 150,pinnable: false,sortable: false},
        {field: 'generationTimeStr',displayName: '报告生成时间',width: 150,pinnable: false,sortable: false},
        {field: 'reportNo',displayName: '报告编号',width: 150,pinnable: false,sortable: false},
        {field: 'reportTypeName',displayName: '报告类型名称',width: 150,pinnable: false,sortable: false},
        {field: 'productName',displayName: '报告名称',width: 150,pinnable: false,sortable: false},
        {field: 'price',displayName: '报告价格',width: 150,pinnable: false,sortable: false},
        {field: 'recordName',displayName: '订单姓名',width: 150,pinnable: false,sortable: false},
        {field: 'recordPhone',displayName: '订单手机号',width: 150,pinnable: false,sortable: false},
        {field: 'recordIdNo',displayName: '订单身份证号',width: 150,pinnable: false,sortable: false},
        {field: 'contactPhone',displayName: '联系人手机号',width: 150,pinnable: false,sortable: false},
        {field: 'userCode',displayName: '贡献人ID',width: 150,pinnable: false,sortable: false},
        {field: 'userName',displayName: '贡献人名称',width: 150,pinnable: false,sortable: false},
        {field: 'shareUserPhone',displayName: '贡献人手机号',width: 150,pinnable: false,sortable: false},
        {field: 'totalBonus',displayName: '发放总奖金额',width: 180,pinnable: false,sortable: false},
        {field: 'accountStatus',displayName: '记账状态',width: 150,pinnable: false,sortable: false},
        {field: 'createTimeStr',displayName: '订单生成时间',width: 180,pinnable: false,sortable: false},
        {field: 'refundTimeStr',displayName: '退款时间',width: 180,pinnable: false,sortable: false},
        {field: 'accountStatus',displayName: '记账状态',width: 150,pinnable: false,sortable: false},
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
        {field: 'zxCostPrice',displayName: '大数据系统给银行家成本',width: 180,pinnable: false,sortable: false},
        {field: 'plateProfit',displayName: '平台分润',width: 150,pinnable: false,sortable: false},
        {field: 'realPlatProfit',displayName: '平台实际分润',width: 180,pinnable: false,sortable: false},
        {field: 'rate',displayName: '领地业务基准分红配置',width: 150,pinnable: false,sortable: false},
        {field: 'basicBonusAmount',displayName: '领地业务基准分红',width: 150,pinnable: false,sortable: false},
        {field: 'adjustRatio',displayName: '调节系数',width: 180,pinnable: false,sortable: false},
        {field: 'bonusAmount',displayName: '领地分红',width: 150,pinnable: false,sortable: false},
        {field: 'redUserCode',displayName: '领地分红领取用户编号',width: 180,pinnable: false,sortable: false},
        {field: 'redUserName',displayName: '领地分红领取用户姓名',width: 180,pinnable: false,sortable: false},
        { field: 'openProvince',displayName:'省',width:150 },
        { field: 'openCity',displayName:'市',width:150 },
        { field: 'openRegion',displayName:'区',width:150 },
        { field: 'remark',displayName:'备注',width:150 },
        {field: 'action',displayName: '操作',width: 120,pinnedRight:true,sortable: false,editable:true,cellTemplate:
        '<a class="lh30" target="_blank" ng-show="grid.appScope.hasPermit(\'superBank.inquiryOrderDetail\')" '
        + 'ui-sref="superBank.inquiryOrderDetail({orderNo:row.entity.orderNo})">详情</a>'}
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
        if ($scope.selected != undefined && $scope.selected != null) {
            $scope.baseInfo.openProvince = $scope.selected.name;
            if ($scope.selected2 != undefined && $scope.selected2 != null) {
                $scope.baseInfo.openCity = $scope.selected2.name;
                if ($scope.selected3 != undefined && $scope.selected3 != null) {
                    $scope.baseInfo.openRegion = $scope.selected3.name;
                } else {
                    $scope.baseInfo.openRegion = "";
                }
            } else {
                $scope.baseInfo.openCity = "";
                $scope.baseInfo.openRegion = "";
            }
        } else {
            $scope.baseInfo.openProvince = "";
            $scope.baseInfo.openCity = "";
            $scope.baseInfo.openRegion = "";
        }
      $scope.submitting = true;
        $scope.loadImg = true;
        $http({
            url: 'superBank/selectOrderInquiryPage?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
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

    //贷款订单批量导入modal
    $scope.importModal = function(){
        $scope.importInfo.loanSourceId = "";
        $('#importModal').modal('show');

    }

    $scope.cancel = function(){
        $('#importModal').modal('hide');
    }

 

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
            } else {
                $scope.notice("获取组织信息异常");
            }
        }).error(function(){
            $scope.notice("获取组织信息异常");
        })
    };
    $scope.getOrgInfoList();

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
                    location.href="superBank/exportInquiryOrder?baseInfo=" +encodeURI(encodeURI(angular.toJson($scope.baseInfo)));
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