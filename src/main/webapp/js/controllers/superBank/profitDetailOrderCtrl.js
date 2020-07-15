/**
 * 分润明细订单查询
 */
angular.module('inspinia').controller('profitDetailOrderCtrl',function($scope,$http,i18nService,$document,SweetAlert,$timeout){
    //数据源
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    //订单类型:1:代理授权；2:信用卡申请 3:收款 4:信用卡还款 5:贷款
    //订单状态:1:已创建；2:待支付；3:待审核 4:已授权 5:订单成功 6:订单失败  9:回收站
    $scope.statusList = [{text:"全部",value:""},{text:"已创建",value:"1"},{text:"待支付",value:"2"},
                          {text:"待审核",value:"3"},{text:"已授权",value:"4"},{text:"已完成",value:"5"},
                          {text:"订单失败",value:"6"},{text:"回收站",value:"9"}];//订单状态
    $scope.orderTypeList = [{text:"全部",value:""},{text:"代理授权",value:"1"},{text:"信用卡申请",value:"2"}
                             ,{text:"收款",value:"3"},{text:"开通信用卡还款",value:"4"},{text:"贷款注册",value:"5"}
                            ,{text:"贷款批贷",value:"6"},{text:"还款",value:"7"},{text:"彩票代购",value:"8"},{text:"征信",value:"10"}
                            ,{text:"红包领地",value:"11"},{text:"违章代缴",value:"14"},{text:"保险",value:"15"},{text:"积分商城",value:"16"}];//订单类型
    $scope.userTypeList = [{text:"全部",value:""},{text:"专员",value:"20"},{text:"经理",value:"30"}
                             ,{text:"银行家",value:"40"},{text:"平台",value:"60"},{text:"OEM",value:"50"}
                            ];//用户身份
    $scope.accountStatusList = [{text:"全部",value:""},{text:"未记账",value:"0"},{text:"已记账",value:"1"},{text:"记账失败",value:"2"}];//记账状态

    $scope.resetForm = function () {
        $scope.baseInfo = {status:"",orderType:"",
            createDateStart:moment(new Date().getTime()-24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
            createDateEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'
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
        $scope.baseInfo.userType = "";
        $scope.baseInfo.accountStatus = "";
        $scope.baseInfo.orgId = -1;
        $scope.baseInfo.openProvince = "全部";
        $scope.baseInfo.openCity = "全部";
        $scope.baseInfo.openRegion = "全部";
        $scope.baseInfo.remark = null;
        $scope.baseInfo.shareUserRemark = null;
    }
    //post查询
    $scope.getAreaList=function(name,type,callback){
        if(name == null || name=="undefine"){
            return;
        }
        $http.post('areaInfo/getAreaByName.do','name='+name+'&&type='+type,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
        ).success(function(data){
            callback(data);
        }).error(function(){
        });
    }
    $scope.provinceGroup = [{name:"全部"}];
    $scope.cityGroup = [{name:"全部"}];
    $scope.areaGroup = [{name:"全部"}];
    //省
    $scope.getAreaList(0,"p",function(data){
        $scope.provinceGroup = data;
        $scope.provinceGroup.unshift({name:"全部"});
        $scope.baseInfo.openCity = "全部";
        $scope.baseInfo.openRegion = "全部";
    });
    //市
    $scope.getCities = function() {
        $scope.getAreaList($scope.baseInfo.openProvince,"",function(data){
            $scope.cityGroup = data;
            $scope.cityGroup.unshift({name:"全部"});
            $scope.baseInfo.openCity = "全部";
            $scope.baseInfo.openRegion = "全部";
        });
    }
    //县
    $scope.getAreas = function() {
        $scope.getAreaList($scope.baseInfo.openCity,"",function(data){
            $scope.areaGroup = data;
            $scope.areaGroup.unshift({name:"全部"});
            $scope.baseInfo.openRegion = "全部";
        });
    }
    $scope.columnDefs = [
        {field: 'id',displayName: '分润明细ID',width: 200,pinnable: false,sortable: false},
        {field: 'orgId',displayName: '所属组织',width: 150,pinnable: false,sortable: false},
        {field: 'orgName',displayName: '品牌商名称',width: 150,pinnable: false,sortable: false},
        {field: 'orderType',displayName: '订单类型',width: 150,pinnable: false,sortable: false},
        {field: 'status',displayName: '订单状态',width: 150,pinnable: false,sortable: false},
        {field: 'orderNo',displayName: '订单编号',width: 150,pinnable: false,sortable: false},
        {field: 'shareUserCode',displayName: '贡献人ID',width: 150,pinnable: false,sortable: false},
        {field: 'shareNickName',displayName: '贡献人昵称',width: 150,pinnable: false,sortable: false},
        {field: 'shareUserName',displayName: '贡献人名称',width: 150,pinnable: false,sortable: false},
        {field: 'shareUserPhone',displayName: '贡献人手机号',width: 150,pinnable: false,sortable: false},
        {field: 'totalProfit',displayName: '总奖金',width: 150,pinnable: false,sortable: false},
        {field: 'userName',displayName: '收益人姓名',width: 140,pinnable: false,sortable: false},
        {field: 'userCode',displayName: '收益人ID',width: 140,pinnable: false,sortable: false},
        {field: 'userType',displayName: '收益人身份',width: 140,pinnable: false,sortable: false},
        {field: 'userProfit',displayName: '收益人分润',width: 140,pinnable: false,sortable: false},
        {field: 'profitLevel',displayName: '当前分润层级',width: 140,pinnable: false,sortable: false},
        {field: 'createDateStr',displayName: '创建时间',width: 150,pinnable: false,sortable: false},
        {field: 'accountStatus',displayName: '记账状态',width: 120,pinnable: false,sortable: false},
        { field: 'openProvince',displayName:'省',width:150 },
        { field: 'openCity',displayName:'市',width:150 },
        { field: 'openRegion',displayName:'区',width:150 },
        { field: 'remark',displayName:'备注',width:150 },
        { field: 'shareUserRemark',displayName:'贡献人备注',width:150 },
        // {field: 'action',displayName: '操作',width: 120,pinnedRight:true,sortable: false,editable:true,cellTemplate:
        // '<a class="lh30" target="_blank" ng-show="grid.appScope.hasPermit(\'superBank.repayOrderDetail\')" '
        // + 'ui-sref="superBank.repayOrderDetail({orderNo:row.entity.orderNo})">详情</a>'}
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
            url: 'superBank/profitDetailOrder?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
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
    // //异步获取用户
    // $scope.userInfoList = [{userCode: "", userName: "全部"}];
    // var oldValue="-1";
    // var timeout="";
    // function getUserInfoList(value){
    //     var newValue=value;
    //     if(newValue != oldValue) {
    //         if (timeout)
    //             $timeout.cancel(timeout);
    //         timeout = $timeout(function () {
    //             $http({
    //                 url: "superBank/selectUserInfoList",
    //                 data: "userCode=" + value,
    //                 headers: {'Content-Type': 'application/x-www-form-urlencoded'},
    //                 method: "POST"
    //             }).success(function (result) {
    //                 if (result.status) {
    //                     if (result.data.length == 0) {
    //                         $scope.userInfoList = [{userCode: "", userName: "全部"}];
    //                     } else {
    //                         $scope.userInfoList = result.data;
    //                         $scope.userInfoList.unshift({userCode: "", userName: "全部"});
    //                     }
    //                     oldValue = value;
    //                 } else {
    //                     $scope.notice(result.msg);
    //                 }
    //             }).error(function () {
    //                 $scope.notice("系统异常，请稍候再试");
    //             })
    //         }
    //         , 800);
    //     }
    // }
    // $scope.getUserInfoList = getUserInfoList;
    // $scope.getUserInfoList("");
    //
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
                    location.href="superBank/exportProfitDetail?baseInfo=" +encodeURI(encodeURI(angular.toJson($scope.baseInfo)));
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