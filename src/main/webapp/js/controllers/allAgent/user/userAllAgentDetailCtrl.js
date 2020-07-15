/**
 * 盟主详情
 */
angular.module('inspinia').controller('userAllAgentDetailCtrl',function($scope,$http,i18nService,$state,$stateParams,$q){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions1=angular.copy($scope.paginationOptions);
    $scope.paginationOptions2=angular.copy($scope.paginationOptions);

    $scope.idCardNoStateSelect = [{text:"未完成认证",value:0},{text:"已完成认证",value:1}];
    $scope.idCardNoStateStr=angular.toJson($scope.idCardNoStateSelect);

    $scope.userTypeSelect = [{text:"全部",value:""},{text:"机构",value:1},{text:"大盟主",value:2},{text:"盟主",value:3}];
    $scope.userTypeStr=angular.toJson($scope.userTypeSelect);

    $scope.gradeSelect = [{text:"普通盟主",value:"0"},{text:"黄金盟主",value:"1"},
        {text:"铂金盟主",value:"2"},{text:"黑金盟主",value:"3"},{text:"钻石盟主",value:"4"}];

    $scope.userTypeSelect = [{text:"全部",value:""},{text:"机构",value:1},{text:"大盟主",value:2},{text:"盟主",value:3}];
    $scope.pageDividedCount={totalAmount:0,canDrawAmount:0,remainAmount:0,withdrawAmount:0};
    $scope.pageMachineCount={totalAmount:0,canDrawAmount:0,withdrawAmount:0};
    $scope.info={};

    //清空
    $scope.clearDetail=function(){
        $scope.baseInfo={transType:"",startDate:"",endDate:""};
    };
    $scope.clearDetail();

    $http.post("userAllAgent/getUserAllAgent","id="+$stateParams.id,
        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function(data){
            if(data.status){
                $scope.info=data.user;
            }
        });


    $scope.dividedGrid={                           //配置表格
        data: 'divided',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs : [                           //表格数据
            { field: 'amount',displayName:'金额(元)',width:180},
            { field: 'incomeType',displayName:'收支类别',width:180,cellFilter:"formatDropping:" + angular.toJson($scope.transTypes)},
            { field: 'incomeTime',displayName:'日期',width:180,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'}
        ],
        onRegisterApi: function(gridApi) {
            $scope.gridApi1 = gridApi;
            /*gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.paginationOptions1.pageNo = newPage;
                $scope.paginationOptions1.pageSize = pageSize;
                $scope.queryDivided();
            });*/
        }
    };

    $scope.machineGrid={                           //配置表格
        data: 'machine',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs : [                           //表格数据
            { field: 'amount',displayName:'金额(元)',width:180},
            { field: 'incomeType',displayName:'收支类别',width:180,cellFilter:"formatDropping:" + angular.toJson($scope.transTypes)},
            { field: 'incomeTime',displayName:'日期',width:180,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'}
        ],
        onRegisterApi: function(gridApi) {
            $scope.gridApi2 = gridApi;
            /*gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.paginationOptions2.pageNo = newPage;
                $scope.paginationOptions2.pageSize = pageSize;
                $scope.queryMachine();
            });*/
        }
    };

    $scope.queryDivided=function(){
        if ($scope.loadImg1) {
            return;
        }
        $scope.loadImg1 = true;
        $scope.baseInfo.id=$stateParams.id;
        $http.post("userAllAgent/selectDivided","info="+angular.toJson($scope.baseInfo),
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.pageDividedCount.totalAmount=data.totalAmount;
                    $scope.pageDividedCount.canDrawAmount=data.canDrawAmount;
                    $scope.pageDividedCount.remainAmount=data.remainAmount;
                    $scope.pageDividedCount.withdrawAmount=data.withdrawAmount;
                    $scope.divided=data.incomeDetails;
                    //$scope.dividedGrid.totalItems = data.page.totalCount;
                }
                $scope.loadImg1 = false;
            })
            .error(function(data){
                $scope.notice(data.msg);
                $scope.loadImg1 = false;
            });
    };

    $scope.queryMachine=function(){
        if ($scope.loadImg2) {
            return;
        }
        $scope.loadImg2 = true;
        $scope.baseInfo.id=$stateParams.id;
        $http.post("userAllAgent/selectMachine","info="+angular.toJson($scope.baseInfo),
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.pageMachineCount.totalAmount=data.totalAmount;
                    $scope.pageMachineCount.canDrawAmount=data.canDrawAmount;
                    $scope.pageMachineCount.withdrawAmount=data.withdrawAmount;
                    $scope.machine=data.incomeDetails;
                    //$scope.machineGrid.totalItems = data.page.totalCount;
                }
                $scope.loadImg2 = false;
            })
            .error(function(data){
                $scope.notice(data.msg);
                $scope.loadImg2 = false;
            });
    };

    $scope.queryDetail=function(){
        $scope.queryDivided();
        $scope.queryMachine()
    };
    $scope.queryDetail();

    /**
     * 获取敏感数据
     */
    $scope.dataSta=true;
    $scope.getDataProcessing = function () {
        if($scope.dataSta){
            $http.post("userAllAgent/getDataProcessing","id="+$stateParams.id,
                {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                .success(function(data) {
                    if(data.status){
                        $scope.info.mobile = data.user.mobile;
                        $scope.info.idCardNo = data.user.idCardNo;
                        if(data.user.card!=null){
                            $scope.info.card.mobile = data.user.card.mobile;
                        }
                        $scope.dataSta=false;
                    }else{
                        $scope.notice(data.msg);
                    }
                });
        }
    };
});