/**
 * 调单管理查询
 */
angular.module('inspinia',['infinity.angular-chosen']).controller('surveyOrderQueryCtrl',function($scope,$http,i18nService,$document,SweetAlert,$timeout){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    //调单类型
    $scope.orderTypeCodeStr=angular.toJson($scope.orderTypeCodeList);
    //业务类型
    $scope.orderServiceCodeStr=angular.toJson($scope.orderServiceCodeList);

    //交易方式
    $scope.payMethodTypeSelect=angular.copy($scope.payMethodType);
    $scope.payMethodTypeSelect.unshift({text:"全部",value:""});
    $scope.payMethodTypeStr = angular.toJson($scope.payMethodType);

    //回复状态
    $scope.replyStatusSelect1=[{text:"未回复",value:"0"},{text:"未回复(下级已提交)",value:"1"},{text:"已回复",value:"2"},
        {text:"逾期未回复",value:"3"},{text:"逾期未回复(下级已提交)",value:"4"},{text:"逾期已回复",value:"5"},{text:"无需处理",value:"6"}];
    $scope.replyStatusStr=angular.toJson($scope.replyStatusSelect1);
    $scope.replyStatusSelect=angular.copy($scope.replyStatusSelect1);
    $scope.replyStatusSelect.unshift({text:"全部",value:""});

    //处理状态
    $scope.dealStatusSelect1=[{text:"未处理",value:"0"},{text:"部分提供",value:"1"},{text:"持卡人承认交易",value:"2"},
        {text:"全部提供",value:"3"},{text:"无法提供",value:"4"},{text:"逾期部分提供",value:"5"},
        {text:"逾期全部提供",value:"6"},{text:"逾期未回",value:"7"},{text:"已回退",value:"8"},{text:"无需提交资料",value:"9"}];
    $scope.dealStatusStr=angular.toJson($scope.dealStatusSelect1);
    $scope.dealStatusSelect=angular.copy($scope.dealStatusSelect1);
    $scope.dealStatusSelect.unshift({text:"全部",value:""});

    //是否添加扣款
    $scope.haveAddDeductSelect1=[{text:"未添加",value:"0"},{text:"已添加",value:"1"}];
    $scope.haveAddDeductStr=angular.toJson($scope.haveAddDeductSelect1);
    $scope.haveAddDeductSelect=angular.copy($scope.haveAddDeductSelect1);
    $scope.haveAddDeductSelect.unshift({text:"全部",value:""});

    //是否包含下级
    $scope.boolSelect=[{text:"否",value:"2"},{text:"是",value:"1"}];

    //上游回复状态
    $scope.acqReplyStatusSelect=[{text:"全部",value:""},{text:"未回复",value:"0"},{text:"已回复",value:"1"}];
    $scope.acqReplyStatusStr = angular.toJson($scope.acqReplyStatusSelect);

    //清空
    $scope.clear=function(){
        $scope.info={acqCode:"",orderTypeCode:"",replyStatus:"",haveAddDeduct:"",dealStatus:"",
            agentNo:"",bool:"",oneAgentNo:"",bool:"2",payMethod:"",orderServiceCode:"",
            acqReplyStatus:"",transStatus:"",
            createTimeBegin:moment(new Date().getTime()-6*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
            createTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'
        };
    };
    $scope.clear();

    //收单机构
    $http.post("acqOrgAction/selectBoxAllInfo")
        .success(function(msg){
            //响应成功
            if(msg==null){
                return;
            }
            $scope.acqOrgs=[{text:"全部",value:""}];
            for(var i=0; i<msg.length; i++){
                $scope.acqOrgs.push({value:msg[i].acqEnname,text:msg[i].acqEnname});
            }
        });

    //获取代理商
    $http.post("agentInfo/selectAllInfo")
        .success(function(msg){
            $scope.agentList=[{value:"",text:"全部"}];
            for(var i=0; i<msg.length; i++){
                $scope.agentList.push({value:msg[i].agentNo,text:msg[i].agentNo + " " + msg[i].agentName});
            }

        });
    //条件查询代理商
    $scope.getStates =getStates;
    var oldValue="";
    var timeout="";
    function getStates(value) {
        $scope.agentt = [];
        var newValue=value;
        if(newValue != oldValue){
            if (timeout) $timeout.cancel(timeout);
            timeout = $timeout(
                function(){
                    $http.post('agentInfo/selectAllInfo','item=' + value,
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .then(function (response) {
                            if(response.data.length==0) {
                                $scope.agentt =[{value: "", text: "全部"}];
                            }else{
                                $scope.agentt =[{value: "", text: "全部"}];
                                for(var i=0; i<response.data.length; i++){
                                    $scope.agentt.push({value:response.data[i].agentNo,text:response.data[i].agentNo + " " + response.data[i].agentName});
                                }
                            }
                            $scope.agentList = $scope.agentt;
                            oldValue = value;
                        });
                },800);
        }
    };

    //一级代理商
    $http.post("agentInfo/selectAllOneInfo")
        .success(function(msg){
            $scope.oneAgentList=[{value:"",text:"全部"}];
            for(var i=0; i<msg.length; i++){
                $scope.oneAgentList.push({value:msg[i].agentNo,text:msg[i].agentNo + " " + msg[i].agentName});
            }

        });
    //一级代理商
    $scope.getOneStates =getOneStates;
    var oldValueOne="";
    var timeoutOne="";
    function getOneStates(value) {
        var newValueOne=value;
        if(newValueOne != oldValueOne){
            if (timeoutOne) $timeout.cancel(timeout);
            timeoutOne = $timeout(
                function(){
                    $http.post('agentInfo/selectAllOneInfo','item=' + value,
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .then(function (response) {
                            if(response.data.length==0) {
                                $scope.agenttOne =[{value: "", text: "全部"}];
                            }else{
                                $scope.agenttOne =[{value: "", text: "全部"}];
                                for(var i=0; i<response.data.length; i++){
                                    $scope.agenttOne.push({value:response.data[i].agentNo,text:response.data[i].agentNo + " " + response.data[i].agentName});
                                }
                            }
                            $scope.oneAgentList = $scope.agenttOne;
                            oldValueOne = value;
                        });
                },800);
        }
    };

    $scope.userGrid={                           //配置表格
        data: 'result',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'orderNo',displayName:'调单号',width:180 },
            { field: 'orderTypeCode',displayName:'调单类型',cellFilter:"formatDropping:"+$scope.orderTypeCodeStr,width:150},
            { field: 'transOrderNo',displayName:'订单编号',width:180,
                cellTemplate:'' +
                '<div ng-show="row.entity.transOrderNo!=null" style="margin-top:7px;"> ' +
                    '<div ng-if="row.entity.orderServiceCode==\'3\'">'+
                        '<a target="_blank" ui-sref="creditRepay.transactionDetail({orderNo:row.entity.transOrderNo})">{{row.entity.transOrderNo}}</a> ' +
                    '</div>'+
                    '<div ng-if="row.entity.orderServiceCode!=\'3\'&& row.entity.transOrderDatabase==\'now\'">'+
                        '<a target="_blank" ui-sref="trade.tradeQueryDetail({id:row.entity.transOrderNo,val:0})">{{row.entity.transOrderNo}}</a> ' +
                    '</div>'+
                    '<div ng-if="row.entity.orderServiceCode!=\'3\'&& row.entity.transOrderDatabase==\'old\'">'+
                        '<a target="_blank" ui-sref="histrade.histradeQueryDetail({id:row.entity.transOrderNo,val:0})">{{row.entity.transOrderNo}}</a> ' +
                    '</div>'+
                '</div>'
            },
            { field: 'orderServiceCode',displayName:'业务类型',cellFilter:"formatDropping:"+$scope.orderServiceCodeStr,width:150},
            { field: 'acqReferenceNo',displayName:'系统参考号',width:180 },
            { field: 'createTime',displayName:'发起时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'replyEndTime',displayName:'截止回复时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'replyStatus',displayName:'回复状态',cellFilter:"formatDropping:"+$scope.replyStatusStr,width:150},
            { field: 'dealStatus',displayName:'处理状态',cellFilter:"formatDropping:"+$scope.dealStatusStr,width:150},
            { field: 'replyTypeName',displayName:'提交回复角色',width:180 },
            { field: 'haveAddDeduct',displayName:'是否添加扣款',cellFilter:"formatDropping:"+$scope.haveAddDeductStr,width:150},
            { field: 'urgeNum',displayName:'催单次数',width:180,
                cellTemplate:'' +
                '<div ng-show="row.entity.urgeNum!=null" style="margin-top:7px;"> ' +
                '<div>'+
                '<a target="_blank" ng-click="grid.appScope.openRecordModal(row.entity.orderNo)">{{row.entity.urgeNum}}</a> ' +
                '</div>'+
                '</div>'
            },
            { field: 'merchantNo',displayName:'商户编号',width:180 },
            { field: 'merchantName',displayName:'商户名称',width:180 },
            { field: 'acqMerchantNo',displayName:'收单商户编号',width:180 },
            { field: 'paUserNo',displayName:'所属盟主编号',width:180 },
            { field: 'transStatus',displayName:'交易状态',cellFilter:"formatDropping:"+$scope.transStatusStr,width:150 },
            { field: 'transAccountNo',displayName:'交易卡号',width:180 },
            { field: 'transAmount',displayName:'交易金额',width:150,pinnable:false,sortable:false,cellFilter:"currency:''"},
            { field: 'transTime',displayName:'交易时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'agentName',displayName:'所属代理商',width:180 },
            { field: 'oneAgentName',displayName:'一级代理商',width:180 },
            { field: 'saleName',displayName:'一级代理商所属销售',width:200},
            { field: 'acqReplyStatus',displayName:'上游回复状态',cellFilter:"formatDropping:"+$scope.acqReplyStatusStr,width:150},
            { field: 'lastReplyTime',displayName:'代理商回复时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'acqCode',displayName:'收单机构',width:180 },
            { field: 'payMethod',displayName:'交易方式',cellFilter:"formatDropping:"+$scope.payMethodTypeStr,width:150},
            { field: 'creator',displayName:'创建人',width:180 },
            { field: 'id',displayName:'操作',width:400,pinnedRight:true, cellTemplate:
                '<div class="lh30">'+
                '<a target="_blank" ui-sref="risk.surveyOrderDetail({id:row.entity.id})">查看</a> ' +
                '<a ng-show="(row.entity.replyStatus==2||row.entity.replyStatus==5)&&row.entity.dealStatus!=9&&grid.appScope.hasPermit(\'surveyOrder.regresses\')" ng-click="grid.appScope.openHandleModal(row.entity,1)"> | 回退</a> ' +
                '<a ng-show="(row.entity.replyStatus==2||row.entity.replyStatus==5)&&row.entity.dealStatus!=9&&grid.appScope.hasPermit(\'surveyOrder.handle\')" ng-click="grid.appScope.openHandleModal(row.entity,2)"> | 处理</a> ' +
                '<a ng-show="row.entity.dealStatus==9&&grid.appScope.hasPermit(\'surveyOrder.handle\')" ng-click="grid.appScope.openHandleModal(row.entity,2)"> | 处理备注</a> ' +
                '<a ng-show="row.entity.haveAddDeduct==0&&grid.appScope.hasPermit(\'surveyOrder.deduct\')" ng-click="grid.appScope.openDeductModal(row.entity.id)"> | 添加扣款</a> ' +
                '<a ng-show="grid.appScope.hasPermit(\'surveyOrder.upstream\')" ng-click="grid.appScope.openUpstreamModal(row.entity.id)"> | 上游备注</a> ' +
                '<a ng-show="row.entity.dealStatus!=9&&grid.appScope.hasPermit(\'surveyOrder.reminder\')" ng-click="grid.appScope.reminder(row.entity.id)"> | 催单</a> ' +
                '<a ng-show="((row.entity.replyStatus==0&&row.entity.dealStatus==0)||row.entity.dealStatus==9)&&grid.appScope.hasPermit(\'surveyOrder.deleteSurveyOrder\')" ng-click="grid.appScope.deleteOrder(row.entity.id)"> | 删除</a> ' +
                '</div>'
            }
        ],
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.paginationOptions.pageNo = newPage;
                $scope.paginationOptions.pageSize = pageSize;
                $scope.query();
            });
        }
    };
    $scope.query=function(){
        if ($scope.loadImg) {
            return;
        }
        $scope.loadImg = true;
        $http.post("surveyOrder/selectAll","info="+angular.toJson($scope.info)+"&pageNo="+
            $scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.result=data.page.result;
                    $scope.userGrid.totalItems = data.page.totalCount;
                }else{
                    $scope.notice(data.msg);
                }
                $scope.loadImg = false;
            })
            .error(function(data){
                $scope.notice(data.msg);
                $scope.loadImg = false;
            });
    };

    //删除调单
    $scope.deleteOrder=function (id) {
        SweetAlert.swal({
                title:"确定要删除调单吗?",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post("surveyOrder/deleteSurveyOrder","id="+id,
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function(data){
                            if(data.status){
                                $scope.notice(data.msg);
                                $scope.query();
                            }else{
                                $scope.notice(data.msg);
                            }
                        })
                        .error(function(data){
                            $scope.notice(data.msg);
                        });
                }
            });
    };

    //批量删除调单
    $scope.deleteOrderBatch=function () {
        var selectList = $scope.gridApi.selection.getSelectedRows();
        if(selectList==null||selectList.length==0){
            $scope.notice("请选择数据!");
            return;
        }
        var ids="";
        var num=0;
        if(selectList!=null&&selectList.length>0){
            for(var i=0;i<selectList.length;i++){
                if((selectList[i].replyStatus=="0"&&selectList[i].dealStatus=="0")||selectList[i].dealStatus=="9"){
                    ids = ids + selectList[i].id +",";
                    num++;
                }
            }
        }
        if(ids==""){
            $scope.notice("请选择符合删除的数据!");
            return;
        }
        ids=ids.substring(0,ids.length-1);
        var title="选中的数据,符合的有"+num+"条,确认批量删除?";
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
                    $http.post("surveyOrder/deleteOrderBatch","ids="+ids,
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function(data){
                            if(data.status){
                                $scope.notice(data.msg);
                                $scope.query();
                            }else{
                                $scope.notice(data.msg);
                            }
                        })
                        .error(function(data){
                            $scope.notice(data.msg);
                        });
                }
            });
    };

    //催单
    $scope.reminder=function (id) {
        SweetAlert.swal({
                title:"确定催单?",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post("surveyOrder/reminder","id="+id,
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function(data){
                            if(data.status){
                                $scope.notice(data.msg);
                                $scope.query();
                            }else{
                                $scope.notice(data.msg);
                            }
                        })
                        .error(function(data){
                            $scope.notice(data.msg);
                        });
                }
            });
    };

    //批量催单
    $scope.reminderBatch=function () {
        var selectList = $scope.gridApi.selection.getSelectedRows();
        if(selectList==null||selectList.length==0){
            $scope.notice("请选择数据!");
            return;
        }
        var ids="";
        var num=0;
        if(selectList!=null&&selectList.length>0){
            for(var i=0;i<selectList.length;i++){
                if(selectList[i].dealStatus!="9"){
                    ids = ids + selectList[i].id +",";
                    num++;
                }
            }
        }
        if(ids==""){
            $scope.notice("请选择符合催单的数据!");
            return;
        }
        ids=ids.substring(0,ids.length-1);
        var title="选中的数据,符合的有"+num+"条,确认批量催单?";
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
                    $http.post("surveyOrder/reminderBatch","ids="+ids,
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function(data){
                            if(data.status){
                                $scope.notice(data.msg);
                                $scope.query();
                            }else{
                                $scope.notice(data.msg);
                            }
                        })
                        .error(function(data){
                            $scope.notice(data.msg);
                        });
                }
            });
    };

    $scope.openHandleModal = function(entity,sta){
        $scope.addState=sta;
        $scope.dealStatusSelectState=true;
        if(sta==1){//回退
            $scope.dealStatusSelectAdd=[{text:"已回退",value:"8"}];
            $scope.addInfo={dealStatus:"8",dealRemark:"",id:entity.id};
            $scope.addTitle="回退的订单号:"+entity.orderNo+",确认回退?";
        }else if(sta==2){//单个处理
            if(entity.dealStatus=="9"){
                $scope.dealStatusSelectAdd=[{text:"无需提交资料",value:"9"}];
                $scope.addInfo={dealStatus:"9",dealRemark:"",id:entity.id};
                $scope.dealStatusSelectState=false;
            }else{
                $scope.dealStatusSelectAdd=[{text:"全部",value:""},{text:"部分提供",value:"1"},{text:"持卡人承认交易",value:"2"},
                    {text:"全部提供",value:"3"},{text:"无法提供",value:"4"},{text:"逾期部分提供",value:"5"},
                    {text:"逾期全部提供",value:"6"},{text:"逾期未回",value:"7"}];
                $scope.addInfo={dealStatus:"",dealRemark:"",id:entity.id};
            }
            $scope.addTitle="处理的订单号:"+entity.orderNo+",确认处理?";
        }else if(sta==3){//批量处理
            $scope.dealStatusSelectAdd=[{text:"全部",value:""},{text:"部分提供",value:"1"},{text:"持卡人承认交易",value:"2"},
                {text:"全部提供",value:"3"},{text:"无法提供",value:"4"},{text:"逾期部分提供",value:"5"},
                {text:"逾期全部提供",value:"6"},{text:"逾期未回",value:"7"}];

            var selectList = $scope.gridApi.selection.getSelectedRows();
            if(selectList==null||selectList.length==0){
                $scope.notice("请选择数据!");
                return;
            }
            var ids="";
            var num=0;
            if(selectList!=null&&selectList.length>0){
                for(var i=0;i<selectList.length;i++){
                    if((selectList[i].replyStatus=="2"||selectList[i].replyStatus=="5")&&selectList[i].dealStatus!="9"){
                        ids = ids + selectList[i].id +",";
                        num++;
                    }
                }
            }
            if(ids==""){
                $scope.notice("请选择符合处理的数据!");
                return;
            }
            $scope.addTitle="选中的数据,符合处理的有"+num+"条,确认批量处理?";
            $scope.addInfo={dealStatus:"",dealRemark:"",ids:ids.substring(0,ids.length-1)};
        }
        $('#handleModal').modal('show');
    };
    $scope.closeHandleModal = function(){
        $('#handleModal').modal('hide');
    };

    //回退与处理
    $scope.hasSubmit = false;
    $scope.regresses=function () {
        if($scope.hasSubmit){
            return;
        }
        if($scope.addState==1){
            $scope.hasSubmit = true;
            $http.post("surveyOrder/regresses","info="+angular.toJson($scope.addInfo),
                {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                .success(function(data){
                    if(data.status){
                        $scope.notice(data.msg);
                        $scope.query();
                        $scope.closeHandleModal();
                        $scope.hasSubmit = false;
                    }else{
                        $scope.notice(data.msg);
                    }
                })
                .error(function(data){
                    $scope.notice(data.msg);
                });
        }else if($scope.addState==2){
            $scope.hasSubmit = true;
            $http.post("surveyOrder/handle","info="+angular.toJson($scope.addInfo),
                {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                .success(function(data){
                    if(data.status){
                        $scope.notice(data.msg);
                        $scope.query();
                        $scope.closeHandleModal();
                        $scope.hasSubmit = false;
                    }else{
                        $scope.notice(data.msg);
                    }
                })
                .error(function(data){
                    $scope.notice(data.msg);
                });
        }else if($scope.addState==3){
            $http.post("surveyOrder/handleBatch","info="+angular.toJson($scope.addInfo),
                {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                .success(function(data){
                    if(data.status){
                        $scope.notice(data.msg);
                        $scope.query();
                        $scope.closeHandleModal();
                    }else{
                        $scope.notice(data.msg);
                    }
                })
                .error(function(data){
                    $scope.notice(data.msg);
                });
        }

    };

    $scope.openDeductModal = function(id){
        $scope.addDeductInfo={acqDeductAmount:"",acqDeductTime:"",acqDeductRemark:"",id:id};
        $('#deductModal').modal('show');
    };
    $scope.closeDeductModal = function(){
        $('#deductModal').modal('hide');
    };

    //添加扣款记录
    $scope.deduct=function () {
        if($scope.addDeductInfo.acqDeductTime==null||$scope.addDeductInfo.acqDeductTime==""){
            $scope.notice("上游扣款时间不能为空");
            return;
        }
        $http.post("surveyOrder/deduct","info="+angular.toJson($scope.addDeductInfo),
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.notice(data.msg);
                    $scope.query();
                    $scope.closeDeductModal();
                }else{
                    $scope.notice(data.msg);
                }
            })
            .error(function(data){
                $scope.notice(data.msg);
            });
    };

    $scope.openUpstreamModal = function(id){
        $scope.upstreamInfo={acqReplyStatus:"",acqReplyRemark:"",id:id};
        $('#upstreamModal').modal('show');
    };
    $scope.closeUpstreamModal = function(){
        $('#upstreamModal').modal('hide');
    };

    //添加上游备注
    $scope.upstream=function () {
        $http.post("surveyOrder/upstream","info="+angular.toJson($scope.upstreamInfo),
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.notice(data.msg);
                    $scope.query();
                    $scope.closeUpstreamModal();
                }else{
                    $scope.notice(data.msg);
                }
            })
            .error(function(data){
                $scope.notice(data.msg);
            });
    };

    // 导出
    $scope.exportInfo = function () {
        SweetAlert.swal({
                title: "确认导出？",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true
            },
            function (isConfirm) {
                if (isConfirm) {
                    location.href="surveyOrder/importDetail?info="+encodeURI(angular.toJson($scope.info));
                }
            });
    };


    $scope.recordGrid={                           //配置表格
        data: 'recordData',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'rowNo',displayName:'序号',width:180 },
            { field: 'createTime',displayName:'催单时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:200}
        ]
    };
    $scope.openRecordModal = function(orderNo){
        $http.post("surveyOrder/getRecordList","orderNo="+orderNo,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.recordData=data.list;
                    $('#recordModal').modal('show');
                }
            });
    };

    $scope.closeRecordModal = function(){
        $('#recordModal').modal('hide');
    };

    //条件显示问题
    $scope.mtxt="全部条件";
    $scope.visible= false;
    $scope.toggle = function(){
        if($scope.visible == false){
            $scope.mtxt="收起";
            $scope.visible=true;
        }else{
            $scope.mtxt="全部条件";
            $scope.visible=false;
        }
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