/**
 * 积分兑换订单详情
 */
angular.module('inspinia').controller('exchangeActivateOrderDetailCtrl',function($scope,$http,i18nService,$stateParams){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文

    $scope.orderStatusSelect = $scope.orderStatusExchange;
    $scope.orderStatusStr=angular.toJson($scope.orderStatusSelect);

    $scope.accStatusSelect = $scope.accStatusExchange;
    $scope.accStatusStr=angular.toJson($scope.accStatusSelect);

    $scope.channelSelect = angular.copy($scope.orderChannelExchange);
    $scope.channelStr=angular.toJson($scope.channelSelect);
    $scope.channelSelect.unshift({text:"全部",value:null});


    $scope.receiveStatusSelect = [{text:"未收到",value:"0"},{text:"已收到",value:"1"}];
    $scope.receiveStatusStr=angular.toJson($scope.receiveStatusSelect);

    $scope.checkStatusSelect = [{text:"核销中",value:"0"},{text:"核销成功",value:"1"},
        {text:"核销失败",value:"2"}];
    $scope.checkStatusStr=angular.toJson($scope.checkStatusSelect);

    $scope.checkModeSelect = [{text:"全部",value:""},{text:"一次核销",value:"1"},{text:"二次核销",value:"2"},
        {text:"导入核销",value:"3"},{text:"API核销",value:"4"}];
    $scope.checkModeStr=angular.toJson($scope.checkModeSelect);

    $scope.info={};

    $http.post("exchangeActivateOrder/getExchangeOrder","id="+$stateParams.id,
        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function(data){
            if(data.status){
                $scope.info=data.order;
                $scope.result=data.order.writeOffHisList;
            }
        });

    $scope.userGrid={                           //配置表格
        data: 'result',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'checkOper',displayName:'核销人',width:150},
            { field: 'createTime',displayName:'核销时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'checkMode',displayName:'核销方式',width:120,cellFilter:"formatDropping:" +  $scope.checkModeStr},
            { field: 'checkStatus',displayName:'核销状态',width:120,cellFilter:"formatDropping:" +  $scope.checkStatusStr },
            { field: 'channel',displayName:'核销渠道',width:180,cellFilter:"formatDropping:" +  $scope.channelStr },
            { field: 'saleOrderNo',displayName:'核销渠道订单号',width:180},
            { field: 'remark',displayName:'核销原因',width:600}
        ],
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
        }
    };

    /**
     * 获取敏感数据
     */
    $scope.dataSta=true;
    $scope.getDataProcessing = function () {
        if($scope.dataSta){
            $http.get('exchangeActivateOrder/getDataProcessing?id='+$stateParams.id)
                .success(function(data) {
                    if(data.status){
                        $scope.info.mobileUsername = data.order.mobileUsername;
                        $scope.info.idCardNo = data.order.idCardNo;
                        $scope.dataSta=false;
                    }else{
                        $scope.notice(data.msg);
                    }
                });
        }
    };
});