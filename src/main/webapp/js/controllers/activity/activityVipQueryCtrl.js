/**
 * VIP优享订单查询
 */
angular.module('inspinia',['angularFileUpload','infinity.angular-chosen']).controller("activityVipQueryCtrl", function($scope, $http, $state, $stateParams,$filter,i18nService,SweetAlert,$document,FileUploader,$timeout) {
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    //数据源
    $scope.subscribeStatuses=[{text:"全部",value:""},{text:"成功",value:"SUCCESS"},{text:"失败",value:"FAILED"},{text:"未付款",value:"INIT"}];
    $scope.paymentTypes = [{text:"全部",value:""},{text:"支付宝",value:"alipay"},{text:"微信",value:"wechat"},{text:"刷卡",value:"byCard"}];

    //clear
    $scope.clear=function(){
        $scope.info={order_no:"",merchantN:"",mobilephone:"",agentN:"",oneAgentNo:"",team_id:"",
            subscribe_status:"",payment_type:"",payment_order_no:"",
            startTime:moment(new Date().getTime()-6*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
            endTime:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'};
    }
    $scope.clear();

    
    
    
    $scope.agentOemList=[{text:"全部",value:""}];
    $http.post("sysDict/getListByKey.do?sysKey=VIPAGENTOEM")
        .success(function(data){
            //响应成功
            for(var i=0; i<data.length; i++){
                $scope.agentOemList.push({value:data[i].sysValue,text:data[i].sysName});
            }
        //   console.debug(angular.toJson($scope.modelTypeList));
     });
    
    
    
    
    
    //代理商
    $http.post("agentInfo/selectAllInfo")
        .success(function(msg){
            //响应成功
            for(var i=0; i<msg.length; i++){
                $scope.agent.push({value:msg[i].agentNo,text:msg[i].agentNo + " " + msg[i].agentName});
            }
        });
    //动态代理商
    $scope.agent=[{value:"",text:"全部"}];
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
                                $scope.agentt.push({value: "", text: "全部"});
                            }else{
                                $scope.agentt.push({value: "", text: "全部"});
                                for(var i=0; i<response.data.length; i++){
                                    $scope.agentt.push({value:response.data[i].agentNo,text:response.data[i].agentNo + " " + response.data[i].agentName});
                                }
                            }
                            $scope.agent = $scope.agentt;
                            oldValue = value;
                        });
                },800);
        }
    };

    //一级代理商
    $http.post("agentInfo/selectAllOneInfo")
        .success(function(msg){
            //响应成功
            for(var i=0; i<msg.length; i++){
                $scope.oneAgent.push({value:msg[i].agentNo,text:msg[i].agentNo + " " + msg[i].agentName});
            }
        });
    //条件查询一级代理商
    $scope.oneAgent=[{value:"",text:"全部"}];
    $scope.getStatesOne =getStatesOne;
    var oldValueOne="";
    var timeoutOne="";
    function getStatesOne(value) {
        $scope.agenttOne = [];
        var newValueOne=value;
        if(newValueOne != oldValueOne){
            if (timeoutOne) $timeout.cancel(timeoutOne);
            timeoutOne = $timeout(
                function(){
                    $http.post('agentInfo/selectAllOneInfo','item=' + value,
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .then(function (response) {
                            if(response.data.length==0) {
                                $scope.agenttOne.push({value: "", text: "全部"});
                            }else{
                                $scope.agenttOne.push({value: "", text: "全部"});
                                for(var i=0; i<response.data.length; i++){
                                    $scope.agenttOne.push({value:response.data[i].agentNo,text:response.data[i].agentNo + " " + response.data[i].agentName});
                                }
                            }
                            $scope.oneAgent = $scope.agenttOne;
                            oldValueOne = value;
                        });
                },800);
        }
    };

    $scope.servicesGrid = {
        data: 'result',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs: [
            {field: 'id',displayName: '序号',width:180},
            {field: 'order_no',displayName: '业务订单编号',width:180},
            {field: 'merchant_name',displayName: '商户名称',width:180},
            {field: 'merchant_no',displayName: '商户编号',width:180},
            {field: 'mobilephone',displayName: '商户手机号',width:180},
            {field: 'agent_name',displayName: '所属代理商',width:180},
            {field: 'one_agent_name',displayName: '一级代理商',width:180},
            {field: 'name',displayName: '服务名称',width:180},
            {field: 'amount',displayName: '交易金额',width:180},
            {field: 'validity_days',displayName: '有效期（天）',width:180},
            {field: 'subscribe_status',displayName: '订单状态',width:180,cellFilter:"formatDropping:" +  angular.toJson($scope.subscribeStatuses)},
            {field: 'payment_type',displayName: '支付方式',width:180,cellFilter:"formatDropping:" +  angular.toJson($scope.paymentTypes)},
            {field: 'validity_end',displayName: '到期时间',width:180,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
            {field: 'payment_order_no',displayName: '支付订单号',width:180},
            {field: 'create_time',displayName: '创建时间',width:180,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
            { field: 'team_id',displayName:'类别',width:100,cellFilter:"formatDropping:"+angular.toJson($scope.allAgentOemText)},
            {field: 'trans_time',displayName: '支付时间',width:180,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'}

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
        if (!($scope.info.order_no || $scope.info.merchantN || $scope.info.mobilephone)) {
            if(!($scope.info.startTime && $scope.info.endTime)){
                $scope.notice("创建时间不能为空");
                return;
            }
            var startTime = new Date($scope.info.startTime).getTime();
            var endTime = new Date($scope.info.endTime).getTime();
            if ((startTime - endTime) > (31 * 24 * 60 * 60 * 1000)) {
                $scope.notice("创建时间范围不能超过31天");
                return;
            }
        }
        if ($scope.loadImg) {
            return;
        }
        $scope.loadImg = true;
        $http.post("activity/queryActivityVipList","info="+angular.toJson($scope.info)+"&pageNo="+
            $scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.result=data.page.result;
                    $scope.servicesGrid.totalItems = data.page.totalCount;
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

    $scope.export = function () {
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
                    location.href="activity/exportActivityVip?param="+encodeURI(angular.toJson($scope.info));
                }
            });

    }

    //页面绑定回车事件
    $document.bind("keypress", function(event) {
        $scope.$apply(function (){
            if(event.keyCode == 13){
                $scope.query();
            }
        })
    });
});

