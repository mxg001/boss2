/**
 * 贷款订单查询
 */
angular.module('inspinia', ['infinity.angular-chosen', 'angularFileUpload']).controller('loanOrderCtrl',function($scope,$http,i18nService,$document,SweetAlert,$timeout,FileUploader){
    //数据源
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    //订单类型:1代理授权；2信用卡申请 3收款 4信用卡还款 5贷款注册 6贷款批贷
    // $scope.statusList = [{text:"全部",value:""}];
    $scope.statusList = [{text:"全部",value:""},{text:"已创建",value:"1"},{text:"待审核",value:"3"},{text:"已完成",value:"5"},{text:"回收站",value:"9"}];//订单状态
    $scope.accountStatusList = [{text:"全部",value:""},{text:"未记账",value:"0"},{text:"已记账",value:"1"},{text:"记账失败",value:"2"}];//记账状态
    $scope.loanTypeList = [{text:"全部",value:""},{text:"有效注册",value:"1"},{text:"授信成功",value:"3"},{text:"有效借款",value:"2"}];//订单类型
    $scope.profitTypeList = [{text:"全部",value:""},{text:"固定金额",value:"1"},{text:"按借款金额比例",value:"2"}];//奖金类型
    $scope.accessWayList = [{text:"H5",value:1},{text:"API",value:2}];//接入方式

    $scope.resetForm = function () {
        $scope.baseInfo = {orderType:"5,6",status:"",orgId:-1,oneUserCode:"",loanSourceId:-1,accountStatus:"",
            loanType:"",profitType:"",
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
        {field: 'orderNo',displayName: '订单ID',width: 200,pinnable: false,sortable: false},
        {field: 'orgId',displayName: '所属组织',width: 150,pinnable: false,sortable: false},
        {field: 'orgName',displayName: '组织名称',width: 150,pinnable: false,sortable: false},
        {field: 'orderType',displayName: '订单类型',width: 150,pinnable: false,sortable: false},
        {field: 'ppStatus',displayName: '授信状态',width: 150,pinnable: false,sortable: false,
            cellTemplate: '<div style="margin-top:8px;"><span>{{row.entity.ppStatus === 1 ? "已授信": row.entity.ppStatus === 2 ? "已授信":"未授信"}}</span></div>'},
        {field: 'status',displayName: '订单状态',width: 150,pinnable: false,sortable: false},
        {field: 'accessWay',displayName: '接入方式',width: 150,pinnable: false,sortable: false,
            cellTemplate: '<div style="margin-top:8px;"><span>{{row.entity.accessWay === 1 ? "H5":"API"}}</span></div>'},

        {field: 'loanAlias',displayName: '放款机构别称',width: 150,pinnable: false,sortable: false},
        {field: 'userCode',displayName: '贡献人ID',width: 150,pinnable: false,sortable: false},
        {field: 'userName',displayName: '贡献人名称',width: 150,pinnable: false,sortable: false},
        {field: 'shareUserPhone',displayName: '贡献人手机号',width: 150,pinnable: false,sortable: false},
        {field: 'orderName',displayName: '订单姓名',width: 150,pinnable: false,sortable: false},
        {field: 'orderPhone',displayName: '订单手机号',width: 150,pinnable: false,sortable: false},
        {field: 'orderIdNo',displayName: '订单证件号',width: 150,pinnable: false,sortable: false},
        {field: 'loanAmount',displayName: '贷款金额',width: 150,pinnable: false,sortable: false},
        {field: 'profitTypeStr',displayName: '奖金方式',width: 150,pinnable: false,sortable: false},
        {field: 'loanTypeStr',displayName: '奖励要求',width: 150,pinnable: false,sortable: false},
        {field: 'loanBankRate',displayName: '贷款机构总奖金扣率',width: 180,pinnable: false,sortable: false},
        {field: 'price',displayName: '贷款机构总奖金',width: 180,pinnable: false,sortable: false},
        {field: 'loanOrgBonus',displayName: '品牌发放总资金扣率',width: 180,pinnable: false,sortable: false},
        {field: 'totalBonus',displayName: '品牌发放总奖金',width: 180,pinnable: false,sortable: false},
        {field: 'createDateStr',displayName: '创建时间',width: 150,pinnable: false,sortable: false},
        {field: 'completeDateStr',displayName: '订单完成时间',width: 150,pinnable: false,sortable: false},
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
        {field: 'realPlatProfit',displayName: '平台实际分润',width: 180,pinnable: false,sortable: false},
        {field: 'rate',displayName: '领地业务基准分红配置',width: 150,pinnable: false,sortable: false},
        {field: 'basicBonusAmount',displayName: '领地业务基准分红',width: 150,pinnable: false,sortable: false},
        {field: 'adjustRatio',displayName: '调节系数',width: 180,pinnable: false,sortable: false},
        {field: 'bonusAmount',displayName: '领地分红',width: 150,pinnable: false,sortable: false},
        {field: 'redUserCode',displayName: '领地分红领取用户编号',width: 180,pinnable: false,sortable: false},
        {field: 'redUserName',displayName: '领地分红领取用户姓名',width: 180,pinnable: false,sortable: false},
        {field: 'accountStatus',displayName: '记账状态',width: 150,pinnable: false,sortable: false},
        {field: 'loanOrderNo',displayName: '关联订单号',width: 150,pinnable: false,sortable: false},
        { field: 'openProvince',displayName:'省',width:150 },
        { field: 'openCity',displayName:'市',width:150 },
        { field: 'openRegion',displayName:'区',width:150 },
        { field: 'remark',displayName:'备注',width:150 },
        {field: 'action',displayName: '操作',width: 120,pinnedRight:true,sortable: false,editable:true,cellTemplate:
        '<a class="lh30" target="_blank" ng-show="grid.appScope.hasPermit(\'superBank.loanOrderDetail\')" '
        + 'ui-sref="superBank.loanOrderDetail({orderNo:row.entity.orderNo})">详情</a>'}
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

    //上传文件,定义控制器路径
    $scope.uploader = new FileUploader({
        url: 'superBank/importLoanOrder',
        queueLimit: 1,   //文件个数
        removeAfterUpload: true,  //上传后删除文件
        headers : {'X-CSRF-TOKEN' : $scope.csrfData},
        formData:[{loanSourceId:""}]
    });
    //过滤格式
    $scope.uploader.filters.push({
        name: 'fileFilter',
        fn: function(item /*{File|FileLikeObject}*/, options) {
            var type = '|' + item.name.slice(item.name.lastIndexOf('.') + 1) + '|';
            return '|xlsx|xls|'.indexOf(type) !== -1;
        }
    });
    //贷款订单批量导入modal
    $scope.importModal = function(){
        $scope.importInfo.loanSourceId = "";
        $('#importModal').modal('show');

    }

    $scope.cancel = function(){
        $('#importModal').modal('hide');
    }

    //贷款订单批量导入提交数据
    $scope.importStatus = false;
    $scope.importInfo = function(){
        $scope.importStatus = true;
        $scope.uploader.queue[0].formData[0].loanSourceId = $scope.importInfo.loanSourceId;
        if($scope.uploader.queue.length > 0){
            $scope.uploader.uploadAll();
            $scope.uploader.onSuccessItem = function(fileItem, response, status, headers) {//上传成功后的回调函数，在里面执行提交
                $scope.importStatus = false;
                var option={
                    message: '测试提示！',
                    classes: 'alert-info',
                    templateUrl: "views/common/notify.html",
                    position : "center",
                    duration:20000
                };
                $scope.notice(response.msg, option);
            };
        }
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
    //获取所有的贷款机构
    $scope.loanListAll = [{id:-1,loanAlias:"全部"}];
    $scope.getLoanList = function () {
        $http({
            url:"superBank/getLoanList",
            method:"POST"
        }).success(function(msg){
            if(msg.status){
                $scope.loanList = msg.data;
                $scope.loanListAll = angular.copy($scope.loanList);
                $scope.loanListAll.unshift({id:-1,loanAlias:"全部"});
            } else {
                $scope.notice("获取贷款机构信息异常");
            }
        }).error(function(){
            $scope.notice("获取贷款机构信息异常");
        })
    };
    $scope.getLoanList();

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
                    location.href="superBank/exportLoanOrder?baseInfo=" +encodeURI(encodeURI(angular.toJson($scope.baseInfo)));
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