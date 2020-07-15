/**
 * 违章代缴订单查询
 */
angular.module('inspinia', ['infinity.angular-chosen', 'angularFileUpload']).controller('carOrderCtrl',function($scope,$http,i18nService,$document,SweetAlert,$timeout,FileUploader){
    //数据源
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    
    $scope.statusList = [{text:"全部",value:""},{text:"已创建",value:"1"},{text:"待审核",value:"3"},{text:"已完成",value:"5"},{text:"回收站",value:"9"}];//订单状态
    $scope.accountStatusList = [{text:"全部",value:""},{text:"未记账",value:"0"},{text:"已记账",value:"1"},{text:"记账失败",value:"2"}];//记账状态
    $scope.loanTypeList = [{text:"全部",value:""},{text:"有效注册",value:"1"},{text:"授信成功",value:"3"},{text:"有效借款",value:"2"}];//订单类型
    $scope.profitTypeList = [{text:"全部",value:""},{text:"固定金额",value:"1"},{text:"按借款金额比例",value:"2"}];//奖金类型
    $scope.violationTypes = [{text:"全部",value:""},{text:"扣分单",value:"1"},{text:"非扣分单",value:"0"}];
    $scope.statuses = [{text:"全部",value:""},{text:"订单成功",value:"5"},{text:"订单失败",value:"6"}];
    $scope.roles = [{text:"全部",value:""},{text:"专员",value:"20"},{text:"经理",value:"30"},{text:"银行家",value:"40"}];
    
    $scope.statusStr = '[{text:"订单成功",value:"5"},{text:"订单失败",value:"6"}]';
    $scope.orderTypeStr = '[{text:"扣分单",value:"1"},{text:"非扣分单",value:"0"}]';
    $scope.userTypeStr = '[{text:"专员",value:"20"},{text:"经理",value:"30"},{text:"银行家",value:"40"}]';
    $scope.accountStatusStr = '[{text:"已记账",value:"1"},{text:"未记账",value:"0"},{text:"记账失败",value:"2"}]';
    
    $scope.resetForm = function () {
        $scope.baseInfo = {status:"",orgId:-1,oneUserCode:"",accountStatus:"",violationType:"",oneUserCode:"",oneUserName:"",oneUserType:""};
        clearSubCondition();
    }
    $scope.resetForm();

    $scope.condition = {conditionStatus: false, conditionMsg: '全部条件'};
    $scope.changeCondition = function(){
        $scope.condition.conditionStatus = !$scope.condition.conditionStatus;
        if($scope.condition.conditionStatus){
            clearSubCondition();
            $scope.condition.conditionMsg = '清空收起';
        } else {
            $scope.condition.conditionMsg = '全部条件';
        }
    }
    function clearSubCondition(){

        $scope.baseInfo.twoUserCode = "";
        $scope.baseInfo.thrUserCode = "";
        $scope.baseInfo.fouUserCode = "";

        $scope.baseInfo.twoUserName = "";
        $scope.baseInfo.thrUserName = "";
        $scope.baseInfo.fouUserName = "";

        $scope.baseInfo.twoUserType = "";
        $scope.baseInfo.thrUserType = "";
        $scope.baseInfo.fouUserType = "";
        $scope.baseInfo.shareUserPhone = "";
        $scope.baseInfo.openProvince = "全部";
        $scope.baseInfo.openCity = "全部";
        $scope.baseInfo.openRegion = "全部";
        $scope.baseInfo.remark = null;
    }

    $scope.columnDefs = [
        {field: 'orderNo',displayName: '订单ID',width: 200,pinnable: false,sortable: false},
        {field: 'outOrderNo',displayName: '外部订单号',width: 200,pinnable: false,sortable: false},
        {field: 'violationType',displayName: '违章类型',width: 150,pinnable: false,sortable: false,cellFilter:"formatDropping:" + $scope.orderTypeStr},
        {field: 'score',displayName: '扣分',width: 150,pinnable: false,sortable: false},
        {field: 'violationTime',displayName: '违章时间',width: 150,pinnable: false,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'violationCity',displayName: '违章城市',width: 150,pinnable: false,sortable: false},
        {field: 'carNum',displayName: '车牌号',width: 150,pinnable: false,sortable: false},
        {field: 'receiveAmount',displayName: '订单总额',width: 150,pinnable: false,sortable: false},
        {field: 'price',displayName: '银行家项目总分润',width: 150,pinnable: false,sortable: false},
        {field: 'totalBonus',displayName: '发放总奖金',width: 150,pinnable: false,sortable: false},
        {field: 'orgName',displayName: '所属组织',width: 150,pinnable: false,sortable: false},
        {field: 'status',displayName: '订单状态',width: 150,pinnable: false,sortable: false,cellFilter:"formatDropping:" + $scope.statusStr},
        {field: 'userCode',displayName: '贡献人ID',width: 150,pinnable: false,sortable: false},
        {field: 'userName',displayName: '贡献人名称',width: 150,pinnable: false,sortable: false},
        {field: 'phone',displayName: '贡献人手机号',width: 150,pinnable: false,sortable: false},
        {field: 'createDate',displayName: '创建时间',width: 150,pinnable: false,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'completeDate',displayName: '订单完成时间',width: 150,pinnable: false,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'oneUserCode',displayName: '一级编号',width: 120,pinnable: false,sortable: false},
        {field: 'oneUserName',displayName: '一级名称',width: 150,pinnable: false,sortable: false},
        {field: 'oneUserType',displayName: '一级身份',width: 150,pinnable: false,sortable: false,cellFilter:"formatDropping:" + $scope.userTypeStr},
        {field: 'oneUserProfit',displayName: '一级分润',width: 150,pinnable: false,sortable: false},
        {field: 'twoUserCode',displayName: '二级编号',width: 120,pinnable: false,sortable: false},
        {field: 'twoUserName',displayName: '二级名称',width: 150,pinnable: false,sortable: false},
        {field: 'twoUserType',displayName: '二级身份',width: 150,pinnable: false,sortable: false,cellFilter:"formatDropping:" + $scope.userTypeStr},
        {field: 'twoUserProfit',displayName: '二级分润',width: 150,pinnable: false,sortable: false},
        {field: 'thrUserCode',displayName: '三级编号',width: 120,pinnable: false,sortable: false},
        {field: 'thrUserName',displayName: '三级名称',width: 150,pinnable: false,sortable: false},
        {field: 'thrUserType',displayName: '三级身份',width: 150,pinnable: false,sortable: false,cellFilter:"formatDropping:" + $scope.userTypeStr},
        {field: 'thrUserProfit',displayName: '三级分润',width: 150,pinnable: false,sortable: false},
        {field: 'fouUserCode',displayName: '四级编号',width: 120,pinnable: false,sortable: false},
        {field: 'fouUserName',displayName: '四级名称',width: 150,pinnable: false,sortable: false},
        {field: 'fouUserType',displayName: '四级身份',width: 150,pinnable: false,sortable: false,cellFilter:"formatDropping:" + $scope.userTypeStr},
        {field: 'fouUserProfit',displayName: '四级分润',width: 150,pinnable: false,sortable: false},
        {field: 'orgProfit',displayName: '品牌商分润',width: 150,pinnable: false,sortable: false},
        {field: 'plateProfit',displayName: '平台分润',width: 150,pinnable: false,sortable: false},
        {field: 'realPlatProfit',displayName: '平台实际分润',width: 180,pinnable: false,sortable: false},
        {field: 'rate',displayName: '领地业务基准分红配置',width: 150,pinnable: false,sortable: false},
        {field: 'basicBonusAmount',displayName: '领地业务基准分红',width: 150,pinnable: false,sortable: false},
        {field: 'adjustRatio',displayName: '调节系数',width: 180,pinnable: false,sortable: false},
        {field: 'bonusAmount',displayName: '领地分红',width: 150,pinnable: false,sortable: false},
        {field: 'redUserCode',displayName: '领地分红领取用户编号',width: 180,pinnable: false,sortable: false},
        {field: 'redUserName',displayName: '领地分红领取用户姓名',width: 180,pinnable: false,sortable: false},
        {field: 'accountStatus',displayName: '记账状态',width: 150,pinnable: false,sortable: false,cellFilter:"formatDropping:" + $scope.accountStatusStr},
        { field: 'openProvince',displayName:'省',width:150 },
        { field: 'openCity',displayName:'市',width:150 },
        { field: 'openRegion',displayName:'区',width:150 },
        { field: 'remark',displayName:'备注',width:150 },
        {field: 'action',displayName: '操作',width: 120,pinnedRight:true,sortable: false,editable:true,cellTemplate:
        '<a class="lh30" target="_blank" ng-show="grid.appScope.hasPermit(\'superBank.carOrderDetail\')" '
        + 'ui-sref="superBank.carOrderDetail({orderNo:row.entity.orderNo})">详情</a>'}
    ];

    $scope.orderGrid = {
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
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

    //省市区
    $scope.list = LAreaDataBaidu;
    $scope.c = function () {
        $scope.selected2 = "";
        $scope.selected3 = "";
    };

    $scope.c2 = function () {
        $scope.selected3 = "";
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
            url: 'superBank/findCarOrder?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
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
            $scope.orderMainSum = result.data.orderSum;
        }).error(function () {
            $scope.submitting = false;
            $scope.loadImg = false;
            $scope.notice('服务器异常,请稍后再试.');
        });
    };
   
    $scope.cancel = function(){
        $('#importModal').modal('hide');
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
                    location.href="superBank/exportCarOrder?baseInfo=" +encodeURI(encodeURI(angular.toJson($scope.baseInfo)));
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