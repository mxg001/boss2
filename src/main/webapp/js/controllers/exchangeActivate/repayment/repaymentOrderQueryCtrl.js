/**
 * 还款订单
 */
angular.module('inspinia',['infinity.angular-chosen']).controller('repaymentOrderQueryCtrl',function($scope,$http,i18nService,SweetAlert,$document,$timeout){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    $scope.orderStatusSelect = $scope.orderStatusExchange;
    $scope.orderStatusStr=angular.toJson($scope.orderStatusSelect);

    $scope.accStatusSelect = $scope.accStatusExchange;
    $scope.accStatusStr=angular.toJson($scope.accStatusSelect);

    $scope.repayStatusSelect=[{text:"全部",value:''},{text:"还款成功",value:'3'},
        {text:"还款失败",value:'4'},{text:"终止",value:'6'}];
    $scope.repayStatusStr=angular.toJson($scope.repayStatusSelect);

    $scope.payTypeSelect=[{text:"全部",value:''},{text:"分期还款",value:'1'},
        {text:"全额还款",value:'2'},{text:"完美还款",value:'3'}];
    $scope.payTypeStr=angular.toJson($scope.payTypeSelect);

    //组织列表
    $scope.oemList=[];
    $http.post("exchangeActivateOem/getOemList",
        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function(data){
            if(data.status){
                $scope.oemList.push({value:"",text:"全部"});
                var list=data.list;
                if(list!=null&&list.length>0){
                    for(var i=0; i<list.length; i++){
                        $scope.oemList.push({value:list[i].oemNo,text:list[i].oemName});
                    }
                }
            }else{
                $scope.notice(data.msg);
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

    //清空
    $scope.clear=function(){
        $scope.info={orderNo:"",orderStatus:"",oemNo:"",merNo:"", userName:"",mobileUsername:"", repayMerchantNo:"",
            agentNo:"",oneAgentNo:"",sourceOrderNo:"",accStatus:"",accTimeBegin:"",accTimeEnd:"",
            repayStatus:"",reimbursementChann:"",completionTimeBegin:"",completionTimeEnd:"",payType:"",
            createTimeBegin:moment(new Date().getTime()-6*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
            createTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'
        };
    };
    $scope.clear();

    $scope.clearTotalAmount=function () {
        $scope.totalAmount={planAmountTotal:0,actualAmountTotal:0,plateShareTotal:0,
            oemShareTotal:0,shareAmountTotal:0};
    };
    $scope.clearTotalAmount();

    $scope.userGrid={                           //配置表格
        data: 'result',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'orderNo',displayName:'订单ID',width:180},
            { field: 'oemNo',displayName:'组织ID',width:180 },
            { field: 'oemName',displayName:'组织名称',width:180 },
            { field: 'orderStatus',displayName:'订单状态',width:120,cellFilter:"formatDropping:" +  $scope.orderStatusStr },
            { field: 'repayStatus',displayName:'还款状态',width:120,cellFilter:"formatDropping:" +  $scope.repayStatusStr },
            { field: 'payType',displayName:'还款订单类型',width:120,cellFilter:"formatDropping:" +  $scope.payTypeStr },
            { field: 'reimbursementChann',displayName:'还款通道',width:180 },
            { field: 'merNo',displayName:'贡献人ID',width:180 },
            { field: 'userName',displayName:'贡献人名称',width:180 },
            { field: 'mobileUsername',displayName:'贡献人手机号',width:180 },
            { field: 'repayMerchantNo',displayName:'还款商户ID',width:180 },
            { field: 'sourceOrderNo',displayName:'关联订单号',width:180 },
            { field: 'planAmount',displayName:'目标还款金额',width:180,cellFilter:"currency:''" },
            { field: 'actualAmount',displayName:'实际消费金额',width:180,cellFilter:"currency:''" },
            { field: 'createTime',displayName:'创建时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'completionTime',displayName:'订单完成时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'rate',displayName:'品牌发放总奖金扣率(%)',width:180 },
            { field: 'provideAmout',displayName:'品牌发放总奖金',width:180,cellFilter:"currency:''" },
            { field: 'agentNo',displayName:'直属代理商ID',width:180 },
            { field: 'agentName',displayName:'直属代理商名称',width:180 },
            { field: 'oneAgentNo',displayName:'一级代理商ID',width:180 },
            { field: 'oneAgentName',displayName:'一级代理商名称',width:180},
            { field: 'oemShare',displayName:'品牌商分润',width:180,cellFilter:"currency:''" },
            { field: 'plateShare',displayName:'平台分润',width:180,cellFilter:"currency:''" },
            { field: 'accStatus',displayName:'记账状态',width:120,cellFilter:"formatDropping:" +  $scope.accStatusStr },
            { field: 'accTime',displayName:'入账时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'id',displayName:'操作',width:180,pinnedRight:true, cellTemplate:
            '<div class="lh30">'+
            '<a target="_blank" ui-sref="exchangeActivate.repaymentOrderDetail({id:row.entity.id})">详情</a> ' +
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
        $http.post("exchangeActivateRepaymentOrder/selectAll","info="+angular.toJson($scope.info)+"&pageNo="+
            $scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.result=data.page.result;
                    $scope.userGrid.totalItems = data.page.totalCount;
                    if(data.totalAmount!=null){
                        $scope.totalAmount=data.totalAmount;
                    }else{
                        $scope.clearTotalAmount();
                    }
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
                    location.href = "exchangeActivateRepaymentOrder/importDetail?info=" + encodeURI(angular.toJson($scope.info));
                }
            });
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