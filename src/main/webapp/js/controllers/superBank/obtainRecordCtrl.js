/**
 * 用户提现记录查询
 */
angular.module('inspinia').controller('obtainRecordCtrl',function($scope,$http,i18nService,$document,SweetAlert){
    //数据源
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    //订单类型:提现订单类型;1 用户分润提现
    //提现状态:1未提交；2结算中；3已结算 4结算失败
    $scope.statusList = [{text:"全部",value:""},{text:"未提交",value:"1"},{text:"结算中",value:"2"},
                          {text:"已结算",value:"3"},{text:"结算失败",value:"4"}];
    $scope.orderTypeList = [{text:"全部",value:""},{text:"用户分润提现",value:"1"}];
    /*$scope.userTypeList = [{text:"全部",value:""},{text:"用户",value:"10"},{text:"专员",value:"20"},{text:"经理",value:"30"}
        ,{text:"银行家",value:"40"},{text:"平台",value:"60"},{text:"OEM",value:"50"}
    ];//用户身份*/
    $scope.resetForm = function () {
        $scope.baseInfo = {status:"",orderType:"",
            createDateStart:moment(new Date().getTime()-24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
            createDateEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'
        };
    }
    $scope.resetForm();

    /*//获取所有的银行家组织
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
    $scope.getOrgInfoList();*/

    $scope.columnDefs = [
        {field: 'obtainNo',displayName: '订单ID',width: 200,pinnable: false,sortable: false},
        {field: 'userCode',displayName: '用户ID',width: 150,pinnable: false,sortable: false},
        /*{field: 'userCode',displayName: '出款通道',width: 150,pinnable: false,sortable: false},
        {field: 'userCode',displayName: '出款通道的流水号',width: 150,pinnable: false,sortable: false},*/
        {field: 'nickName',displayName: '昵称',width: 150,pinnable: false,sortable: false},
        {field: 'userName',displayName: '姓名',width: 150,pinnable: false,sortable: false},
        {field: 'phone',displayName: '手机号',width: 150,pinnable: false,sortable: false},
        {field: 'status',displayName: '结算状态',width: 150,pinnable: false,sortable: false},
        {field: 'orderType',displayName: '订单类型',width: 150,pinnable: false,sortable: false},
        {field: 'orderType',displayName: '提现金额',width: 150,pinnable: false,sortable: false},
        {field: 'obtainAmount',displayName: '出款金额',width: 150,pinnable: false,sortable: false},
        {field: 'obtainFee',displayName: '出款手续费',width: 150,pinnable: false,sortable: false},
        {field: 'createDateStr',displayName: '创建时间',width: 150,pinnable: false,sortable: false},
        {field: 'userType',displayName: '用户身份',width: 150,pinnable: false,sortable: false},
        {field: 'orgName',displayName: '所属组织',width: 150,pinnable: false,sortable: false}
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
            url: 'superBank/obtainRecord?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
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
                    location.href="superBank/exportObtainRecord?baseInfo=" +angular.toJson($scope.baseInfo);
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