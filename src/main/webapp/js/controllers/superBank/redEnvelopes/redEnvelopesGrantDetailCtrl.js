/**
 * 超级银行家用户详情
 */
angular.module('inspinia').controller('redEnvelopesGrantDetailCtrl',function($scope,$http,$stateParams,i18nService){
    //数据源
    i18nService.setCurrentLang('zh-cn');
    $scope.statusSelect=[{text:"全部",value:''},{text:"初始化",value:'-1'},{text:"发放中",value:'0'},{text:"已领完",value:'1'},
        {text:"已到期",value:'2'}];
    $scope.statusStr=angular.toJson($scope.statusSelect);

    $scope.pushTypeSelect=$scope.redPushTypes;
    $scope.pushTypeStr=angular.toJson($scope.pushTypeSelect);

    $scope.receiveTypeSelect=$scope.redReceiveTypes;
    $scope.receiveTypeStr=angular.toJson($scope.receiveTypeSelect);

    $scope.busTypeSelect= $scope.redBusTypes;
    $scope.busTypeStr=angular.toJson($scope.busTypeSelect);

    $scope.pushAreaSelect=$scope.redPushAreas;
    $scope.pushAreaStr=angular.toJson($scope.pushAreaSelect);

    $scope.hasProfitSelect=[{text:"全部",value:''},{text:"是",value:'0'},{text:"否",value:'1'}];
    $scope.hasProfitStr=angular.toJson($scope.hasProfitSelect);

    $scope.statusRiskSelect=[{text:"全部",value:''},{text:"正常",value:'0'},{text:"已屏蔽",value:'1'}];
    $scope.statusRiskStr=angular.toJson($scope.statusRiskSelect);

    $scope.statusRecoverySelect=[{text:"全部",value:''},{text:"待处理",value:'0'},{text:"处理成功",value:'1'},
        {text:"处理失败",value:'2'},{text:"处理中",value:'3'}];
    $scope.statusRecoveryStr=angular.toJson($scope.statusRecoverySelect);

    $scope.statusAccountSelect=[{text:"全部",value:''},{text:"待入账",value:'0'},{text:"已记账",value:'1'},
        {text:"记账失败",value:'2'}];
    $scope.statusAccountStr=angular.toJson($scope.statusAccountSelect);

    $scope.recoveryTypeSelect=[{text:"全部",value:''},{text:"原路退回",value:'0'},{text:"归平台所有",value:'1'},
        {text:"无需处理",value:'2'}];
    $scope.recoveryTypeStr=angular.toJson($scope.recoveryTypeSelect);

    $scope.payTypeSelect=[{text:"全部",value:''},{text:"分润账户余额",value:'0'},{text:"微信支付",value:'1'},
                          {text:"红包账户余额",value:'2'},{text:"内部账户",value:'3'},{text:"支付宝支付",value:'4'}];
    $scope.payTypeStr=angular.toJson($scope.payTypeSelect);

   // 红包评论明细状态
    $scope.statusDisSelect=[{text:"全部",value:''},{text:"正常",value:'0'},{text:"删除",value:'1'}];
    $scope.statusDisStr=angular.toJson($scope.statusDisSelect);

    $scope.info={};
    $scope.imageList={};
    //获取用户详情
    $scope.getRedEnvelopesDetail = function(){
        $http({
            url:'redEnvelopesGrant/selectById/'+$stateParams.id,
            method:'GET'
        }).success(function(data) {
            if (data.status) {
                $scope.info=data.regDetail;
                $scope.imageList = data.imageList;
            } else {
                $scope.notice(result.msg);
            }
        }).error(function(){
            $scope.notice("系统异常，请稍后再试");
        });
    };

    $scope.getRedEnvelopesDetail();

    $scope.gridRerPaginationOptions={pageNo:1,pageSize:10};
    //红包领取查询
    $scope.rerQuery=function(){
        $http.post("redEnvelopesReceive/selectRedEnvelopesReceive","id="+$stateParams.id+"&pageNo="+
            $scope.gridRerPaginationOptions.pageNo+"&pageSize="+$scope.gridRerPaginationOptions.pageSize,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.gridRer.data=data.page.result;
                    $scope.gridRer.totalItems=data.page.totalCount;
                }else{
                    $scope.notice(data.msg);
                }
            });
    }
    //红包领取
    $scope.gridRer={                           //配置表格
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'getUserName',displayName:'领取人姓名'},
            { field: 'nickName',displayName:'微信呢称'},
            { field: 'getUserCode',displayName:'领取用户ID'},
            { field: 'amount',displayName:'金额',width:180,cellFilter:"currency:''" },
            { field: 'getDate',displayName:'领取时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'}
        ],
        onRegisterApi: function(gridApi) {
            $scope.gridRerApi = gridApi;
            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.gridRerPaginationOptions.pageNo = newPage;
                $scope.gridRerPaginationOptions.pageSize = pageSize;
                $scope.rerQuery();
            });
        }
    };
    $scope.rerQuery();

    $scope.gridDisPaginationOptions={pageNo:1,pageSize:10};
    //红包评论明细查询
    $scope.disQuery=function(){
        $http.post("redEnvelopesGrant/selectRedEnvelopesGrantDiscuss","id="+$stateParams.id+"&pageNo="+
            $scope.gridDisPaginationOptions.pageNo+"&pageSize="+$scope.gridDisPaginationOptions.pageSize,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.gridDis.data=data.page.result;
                    $scope.gridDis.totalItems=data.page.totalCount;
                }else{
                    $scope.notice(data.msg);
                }
            });
    }
    //红包评论明细
    $scope.gridDis={                           //配置表格
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'id',displayName:'评论ID' },
            { field: 'userCode',displayName:'用户iD' },
            { field: 'userNickName',displayName:'微信呢称' },
            { field: 'createDate',displayName:'评论时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
            { field: 'content',displayName:'评论内容',width:200 },
            { field: 'status',displayName:'状态',cellFilter:"formatDropping:"+$scope.statusDisStr}
        ],
        onRegisterApi: function(gridApi) {
            $scope.gridDisApi = gridApi;
            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.gridDisPaginationOptions.pageNo = newPage;
                $scope.gridDisPaginationOptions.pageSize = pageSize;
                $scope.disQuery();
            });
        }
    };
    $scope.disQuery();

    $scope.gridOptPaginationOptions={pageNo:1,pageSize:10};
    //红包最近操作查询
    $scope.optQuery=function(){
        $http.post("redEnvelopesGrant/selectRedEnvelopesGrantOption","id="+$stateParams.id+"&pageNo="+
            $scope.gridOptPaginationOptions.pageNo+"&pageSize="+$scope.gridOptPaginationOptions.pageSize,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.gridOpt.data=data.page.result;
                    $scope.gridOpt.totalItems=data.page.totalCount;
                }else{
                    $scope.notice(data.msg);
                }
            });
    }
    //红包最近操作
    $scope.gridOpt={                           //配置表格
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'createDate',displayName:'时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
            { field: 'optUserName',displayName:'操作人' },
            { field: 'optContent',displayName:'操作内容'},
            { field: 'reason',displayName:'原因',},
            { field: 'remark',displayName:'备注',}
        ],
        onRegisterApi: function(gridApi) {
            $scope.gridOptApi = gridApi;
            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.gridOptPaginationOptions.pageNo = newPage;
                $scope.gridOptPaginationOptions.pageSize = pageSize;
                $scope.optQuery();
            });
        }
    };
    $scope.optQuery();
});
