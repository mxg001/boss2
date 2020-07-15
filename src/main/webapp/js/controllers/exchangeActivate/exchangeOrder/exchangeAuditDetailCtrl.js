/**
 * 积分兑换核销详情/核销
 */
angular.module('inspinia').controller('exchangeActivateOrderAuditDetailCtrl',function($scope,$http,i18nService,$stateParams,$state,SweetAlert){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文

    $scope.orderStatusSelect = $scope.orderStatusExchange;
    $scope.orderStatusStr=angular.toJson($scope.orderStatusSelect);

    $scope.accStatusSelect = $scope.accStatusExchange;
    $scope.accStatusStr=angular.toJson($scope.accStatusSelect);

    $scope.channelSelect = angular.copy($scope.orderChannelExchange);
    $scope.channelStr=angular.toJson($scope.channelSelect);
    $scope.channelSelect.unshift({text:"全部",value:null});


    $scope.receiveStatusSelect = [{text:"请选择是否收到预付卡",value:null},{text:"未收到",value:"0"},{text:"已收到",value:"1"}];
    $scope.receiveStatusStr=angular.toJson($scope.receiveStatusSelect);

    $scope.checkStatusSelect = [{text:"全部",value:""},{text:"核销中",value:"0"},{text:"核销成功",value:"1"},
        {text:"核销失败",value:"2"}];
    $scope.checkStatusStr=angular.toJson($scope.checkStatusSelect);

    $scope.checkModeSelect = [{text:"全部",value:""},{text:"一次核销",value:"1"},{text:"二次核销",value:"2"},
        {text:"导入核销",value:"3"},{text:"API核销",value:"4"}];
    $scope.checkModeStr=angular.toJson($scope.checkModeSelect);

    $scope.info={};

    $http.post("exchangeActivateOrder/getAuditExchangeOrder","id="+$stateParams.id,
        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function(data){
            if(data.status){
                $scope.info=data.order;
                $scope.receiveState=data.receiveState;
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

    $scope.submitting = false;
    $scope.saveWriteOff=function(sta){
        if ($scope.submitting) {
            return;
        }
        var title="";
        if(sta==1){
            title="确认核销?";
        }else{
            title="确认核销失败?";
        }
        SweetAlert.swal({
                title:title,
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    //封装提交实体
                    $scope.addInfo={};
                    $scope.addInfo.orderNo=$scope.info.orderNo;
                    $scope.addInfo.checkStatus=sta;
                    $scope.addInfo.receiveState=$scope.receiveState;
                    $scope.addInfo.checkMode="1";
                    if($scope.receiveState=="1"){
                        if($scope.info.receiveStatus==null||$scope.info.receiveStatus==""){
                            $scope.notice("预付卡类型的收货状态不能为空!");
                            return;
                        }
                        $scope.addInfo.receiveStatus=$scope.info.receiveStatus;
                    }
                    if(sta==1){
                        //核销成功
                        if($scope.info.channel==null||$scope.info.channel==""){
                            $scope.notice("核销渠道不能为空!");
                            return;
                        }
                        if($scope.info.writeOffPrice==null||$scope.info.writeOffPrice==""){
                            $scope.notice("核销价格不能为空!");
                            return;
                        }
                    }else{
                        //核销失败
                        if($scope.info.checkReason==null||$scope.info.checkReason==""){
                            $scope.notice("核销原因不能为空!");
                            return;
                        }
                    }
                    $scope.addInfo.channel=$scope.info.channel;
                    $scope.addInfo.saleOrderNo=$scope.info.saleOrderNo;
                    $scope.addInfo.writeOffPrice=$scope.info.writeOffPrice;
                    $scope.addInfo.remark=$scope.info.checkReason;

                    $scope.submitting = true;
                    $http.post("exchangeActivateOrder/updateWriteOff","info="+angular.toJson($scope.addInfo),
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function(data){
                            if(data.status){
                                $scope.notice(data.msg);
                                $state.transitionTo('exchangeActivate.exchangeAudit',null,{reload:true});
                            }else{
                                $scope.notice(data.msg);
                            }
                            $scope.submitting = false;
                        })
                        .error(function(data){
                            $scope.notice(data.msg);
                            $scope.submitting = false;
                        });
                }
            });
    };

    $scope.saveWriteOffTwo=function(sta){
        if ($scope.submitting) {
            return;
        }
        var title="";
        if(sta==1){
            title="确认二次核销?";
        }else{
            title="确认核销失败?";
        }
        SweetAlert.swal({
                title:title,
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    //封装提交实体
                    $scope.addInfo={};
                    $scope.addInfo.orderNo=$scope.info.orderNo;
                    $scope.addInfo.checkStatus=sta;
                    $scope.addInfo.receiveState=$scope.receiveState;
                    $scope.addInfo.checkMode="2";
                    if($scope.receiveState=="1"){
                        if($scope.info.receiveStatus==null||$scope.info.receiveStatus==""){
                            $scope.notice("预付卡类型的收货状态不能为空!");
                            return;
                        }
                        $scope.addInfo.receiveStatus=$scope.info.receiveStatus;
                    }
                    if(sta==1){
                        //核销成功
                        if($scope.info.channel==null||$scope.info.channel==""){
                            $scope.notice("核销渠道不能为空!");
                            return;
                        }
                        if($scope.info.writeOffPrice==null||$scope.info.writeOffPrice==""){
                            $scope.notice("核销价格不能为空!");
                            return;
                        }
                    }else{
                        //核销失败
                        if($scope.info.checkReason==null||$scope.info.checkReason==""){
                            $scope.notice("核销原因不能为空!");
                            return;
                        }
                    }
                    $scope.addInfo.channel=$scope.info.channel;
                    $scope.addInfo.saleOrderNo=$scope.info.saleOrderNo;
                    $scope.addInfo.writeOffPrice=$scope.info.writeOffPrice;
                    $scope.addInfo.remark=$scope.info.checkReason;

                    $scope.submitting = true;
                    $http.post("exchangeActivateOrder/updateAgainWriteOff","info="+angular.toJson($scope.addInfo),
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function(data){
                            if(data.status){
                                $scope.notice(data.msg);
                                $state.transitionTo('exchangeActivate.exchangeAudit',null,{reload:true});
                            }else{
                                $scope.notice(data.msg);
                            }
                            $scope.submitting = false;
                        })
                        .error(function(data){
                            $scope.notice(data.msg);
                            $scope.submitting = false;
                        });
                }
            });
    };
});