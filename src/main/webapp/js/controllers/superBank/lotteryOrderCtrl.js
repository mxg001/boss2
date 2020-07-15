/**彩票订单查询*/
angular.module('inspinia',['infinity.angular-chosen','angularFileUpload']).controller('lotteryOrderCtrl',function($scope,$http,i18nService,$document,SweetAlert,$timeout,FileUploader){
    
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    
    $scope.accountStatusList = [{text:"全部",value:""},{text:"已记账",value:"1"},{text:"记账失败",value:"2"}];//记账状态

    $scope.roles=[{text:"全部",value:""},{text:"专员",value:"20"},{text:"经理",value:"30"},{text:"银行家",value:"40"}];
    
    $scope.resetForm = function () {
        $scope.baseInfo = {accountStatus:"",orgId:-1,oneCode:"",oneRole:"",
            /*startTime:moment(new Date().getTime()-24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',*/
            endTime:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'
        };
        clearSubCondition();
    };
    
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
    	$scope.baseInfo.twoCode = "";
        $scope.baseInfo.threeCode = "";
        $scope.baseInfo.fourCode = "";
        
        $scope.baseInfo.twoName = "";
        $scope.baseInfo.threeName = "";
        $scope.baseInfo.fourName = "";
        
        $scope.baseInfo.twoRole = "";
        $scope.baseInfo.threeRole = "";
        $scope.baseInfo.fourRole = "";
        
        $scope.baseInfo.phone = "";
        $scope.baseInfo.openProvince = "全部";
        $scope.baseInfo.openCity = "全部";
        $scope.baseInfo.openRegion = "全部";
        $scope.baseInfo.remark = null;
        
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
        {field: 'orderNo',displayName: '订单ID',width: 220,pinnable: false,sortable: false},
        {field: 'deviceJno',displayName: '投注设备流水号',width: 220,pinnable: false,sortable: false},
        {field: 'batchNo',displayName: '导入批次号',width: 150,pinnable: false,sortable: false},
        {field: 'orgId',displayName: '所属组织',width: 150,pinnable: false,sortable: false},
        {field: 'orgName',displayName: '组织名称',width: 150,pinnable: false,sortable: false},
        {field: 'orderStatus',displayName: '订单状态',width: 150,pinnable: false,sortable: false},
        {field: 'userCode',displayName: '贡献人ID',width: 150,pinnable: false,sortable: false},
        {field: 'userName',displayName: '贡献人名称',width: 150,pinnable: false,sortable: false},
        {field: 'phone',displayName: '贡献人手机号',width: 150,pinnable: false,sortable: false},
        {field: 'loanAmount',displayName: '付款金额',width: 150,pinnable: false,sortable: false},
        {field: 'profitType',displayName: '奖金方式',width: 250,pinnable: false,sortable: false},
        {field: 'awardRequire',displayName: '奖励要求',width: 250,pinnable: false,sortable: false},
        {field: 'loanBankRate',displayName: '彩票机构总奖金扣率',width: 250,pinnable: false,sortable: false},
        {field: 'price',displayName: '彩票机构总奖金',width: 250,pinnable: false,sortable: false},
        {field: 'loanOrgRate',displayName: '品牌发放总资金扣率',width: 250,pinnable: false,sortable: false},
        {field: 'totalBonus',displayName: '品牌发放总奖金',width: 250,pinnable: false,sortable: false},
        {field: 'betTime',displayName: '投注时间',width: 250,pinnable: false,sortable: false,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'lotteryType',displayName: '彩种',width: 250,pinnable: false,sortable: false},
        {field: 'issue',displayName: '投注期号',width: 140,pinnable: false,sortable: false},
        {field: 'redeemTime',displayName: '兑奖时间',width: 140,pinnable: false,sortable: false,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'awardAmount',displayName: '中奖总金额',width: 140,pinnable: false,sortable: false},
        {field: 'isBigPrize',displayName: '大奖标志',width: 150,pinnable: false,sortable: false},
        {field: 'oneCode',displayName: '一级编号',width: 120,pinnable: false,sortable: false},
        {field: 'oneName',displayName: '一级名称',width: 150,pinnable: false,sortable: false},
        {field: 'oneRole',displayName: '一级身份',width: 150,pinnable: false,sortable: false},
        {field: 'oneProfit',displayName: '一级分润',width: 150,pinnable: false,sortable: false},
        {field: 'twoCode',displayName: '二级编号',width: 120,pinnable: false,sortable: false},
        {field: 'twoName',displayName: '二级名称',width: 150,pinnable: false,sortable: false},
        {field: 'twoRole',displayName: '二级身份',width: 150,pinnable: false,sortable: false},
        {field: 'twoProfit',displayName: '二级分润',width: 150,pinnable: false,sortable: false},
        {field: 'threeCode',displayName: '三级编号',width: 120,pinnable: false,sortable: false},
        {field: 'threeName',displayName: '三级名称',width: 150,pinnable: false,sortable: false},
        {field: 'threeRole',displayName: '三级身份',width: 150,pinnable: false,sortable: false},
        {field: 'threeProfit',displayName: '三级分润',width: 150,pinnable: false,sortable: false},
        {field: 'fourCode',displayName: '四级编号',width: 120,pinnable: false,sortable: false},
        {field: 'fourName',displayName: '四级名称',width: 150,pinnable: false,sortable: false},
        {field: 'fourRole',displayName: '四级身份',width: 150,pinnable: false,sortable: false},
        {field: 'fourProfit',displayName: '四级分润',width: 150,pinnable: false,sortable: false},
        {field: 'orgProfit',displayName: '品牌商分润',width: 150,pinnable: false,sortable: false},
        {field: 'plateProfit',displayName: '平台分润',width: 150,pinnable: false,sortable: false},
        {field: 'accountStatus',displayName: '记账状态',width: 150,pinnable: false,sortable: false},
        { field: 'openProvince',displayName:'省',width:150 },
        { field: 'openCity',displayName:'市',width:150 },
        { field: 'openRegion',displayName:'区',width:150 },
        { field: 'createDate',displayName:'创建时间',width:150 },
        { field: 'remark',displayName:'备注',width:150 },
        {field: 'action',displayName: '操作',width: 120,pinnedRight:true,sortable: false,editable:true,cellTemplate:
        '<a class="lh30" target="_blank"  ui-sref="superBank.lotteryOrderDetail({orderNo:row.entity.orderNo})">详情</a>'}
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
            url: 'superBank/lotteryOrder?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
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
            $scope.orderMainSum = result.data.sumOrder;
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
                    location.href="superBank/exportLotteryOrder?baseInfo=" +encodeURI(encodeURI(angular.toJson($scope.baseInfo)));
                }
            });
    };

    //导出
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
                    location.href="superBank/exportLotteryOrder?baseInfo=" +encodeURI(encodeURI(angular.toJson($scope.baseInfo)));
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