/**
 * 贷款订单查询
 */
angular.module('inspinia', ['infinity.angular-chosen']).controller('superExchangeOrderCtrl',function($scope,$http,i18nService,$document,SweetAlert){
    //数据源
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    $scope.statusList = [{text:"全部",value:""},{text:"报单成功",value:"5"}];//订单状态
    $scope.resetForm = function () {
        $scope.baseInfo = {status:"",orgId:-1,orderPhone:"",orderName:"",
            orderIdNo:"",userCode:"",userName:"", sharePhone:"",agencyAlias:"全部",
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
        {field: 'orderNo',displayName: '订单ID',width: 200,pinnable: false,sortable: false},
        {field: 'payOrderNo',displayName: '关联订单ID',width: 200,pinnable: false,sortable: false},
        {field: 'orgId',displayName: '所属组织',width: 150,pinnable: false,sortable: false},
        {field: 'orgName',displayName: '组织名称',width: 150,pinnable: false,sortable: false},
        {field: 'status',displayName: '订单状态',width: 150,pinnable: false,sortable: false},
        {field: 'loanType',displayName: '兑换产品类别',width: 150,pinnable: false,sortable: false},
        {field: 'orderName',displayName: '兑换产品名称',width: 150,pinnable: false,sortable: false},
        {field: 'loanAmount',displayName: '兑换积分数',width: 150,pinnable: false,sortable: false},
        {field: 'price',displayName: '兑换价格',width: 140,pinnable: false,sortable: false,cellFilter:'currency:""'},
        {field: 'receiveAmount',displayName: '核销价格',width: 140,pinnable: false,sortable: false,cellFilter:'currency:""'},
        {field: 'createDateStr',displayName: '订单创建时间',width: 150,pinnable: false,sortable: false},
        {field: 'userCode',displayName: '贡献人ID',width: 150,pinnable: false,sortable: false},
        {field: 'userName',displayName: '贡献人名称',width: 140,pinnable: false,sortable: false},
        {field: 'shareUserPhone',displayName: '贡献人手机号',width: 140,pinnable: false,sortable: false},
        {field: 'totalBonus',displayName: '品牌发放总奖金',width: 140,pinnable: false,sortable: false,cellFilter:'currency:""'},
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
        {field: 'orgName',displayName: '品牌商名称',width: 150,pinnable: false,sortable: false},
        {field: 'orgProfit',displayName: '品牌商分润',width: 150,pinnable: false,sortable: false,cellFilter:'currency:""'},
        {field: 'plateProfit',displayName: '平台分润',width: 150,pinnable: false,sortable: false,cellFilter:'currency:""'},
        {field: 'accountStatus',displayName: '记账状态',width: 150,pinnable: false,sortable: false},
        {field: 'openProvince',displayName:'省',width:150 },
        {field: 'openCity',displayName:'市',width:150 },
        {field: 'openRegion',displayName:'区',width:150 },
        {field: 'remark',displayName:'备注',width:150 },
        {field: 'action',displayName: '操作',width: 120,pinnedRight:true,sortable: false,editable:true,cellTemplate:
            '<a class="lh30" ng-show="grid.appScope.hasPermit(\'superBank.insuranceOrderDetail\')" '
            + 'ui-sref="superBank.superExchangeOrderDetail({orderNo:row.entity.orderNo})">详情</a>'}
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
            url: 'superExcOrder/orderManager?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
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
                    location.href="superExcOrder/exportSuperExcOrder?baseInfo=" +encodeURI(encodeURI(angular.toJson($scope.baseInfo)));
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