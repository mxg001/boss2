angular.module('inspinia').controller('activityOrderInfoQueryCtrl',function($scope,$http,$state,$filter,$stateParams,$compile,$uibModal,$timeout,$log,i18nService,SweetAlert,$document){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.loadImg = false;
    $scope.myDate  = new Date();
    var date = $filter('date')($scope.myDate,'yyyy-MM-dd');
    $scope.info={startTime:date+" 00:00:00",endTime:date + " 23:59:59"};
    $scope.payMethodList = [{text:"POS",value:"1"},{text:"支付宝",value:"2"},{text:"微信",value:"3"},{text:"快捷",value:"4"}];
  /*  $http.post("activityOrder/actOrderInfoQuery","info="+angular.toJson($scope.info),{headers: {'Content-Type': 'application/x-www-form-urlencoded'}}).success(function (data) {

        $scope.gridOptions.data = data.page.result;
        $scope.gridOptions.totalItems = data.page.totalCount;
    })*/
    $scope.total = {};
  /*  $http.post("activityOrder/actOrderInfoCount","info="+angular.toJson($scope.info),{headers: {'Content-Type': 'application/x-www-form-urlencoded'}}).success(function (data) {
        $scope.total.transAmount = data.total.transAmount;
    })*/

    $scope.gridOptions = {
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
        useExternalPagination: true,                //分页数量//配置表格
        columnDefs:[                        //表格数据
            {field: 'id', displayName: '序号',width:80 },
            { field: 'orderNo',displayName:'业务订单编号',width:160 },
            { field: 'merchantNo',displayName:'商户编号',width:130 },
            { field: 'merchantName',displayName:'商户名称',width:130 },
            { field: 'agentName',displayName:'所属代理商',width:130 },
            { field: 'oneAgentName',displayName:'一级代理商',width:130 },
            { field: 'mobileNo',displayName:'商户手机号',width:120 },
            { field: 'transAmount',displayName:'交易金额',width:90 },
            { field: 'couponCode',displayName:'订单类型',width:90,
                cellFilter:"formatDropping:"+angular.toJson($scope.couponCodes)},
            { field: 'transStatus',displayName:'订单状态',width:100 ,
                cellFilter:"formatDropping:"+angular.toJson($scope.transStatusAll)},
            { field: 'merAccNo',displayName:'收款商户',width:130 },
            /*{ field: 'transTime',displayName:'交易时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:100},*/
            { field: 'createTime',displayName:'交易时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:100},
            { field: 'payMethod',displayName:'支付方式',width:130,cellFilter:"formatDropping:"+angular.toJson($scope.payMethodList) },
            { field: 'payOrderNo',displayName:'支付订单号',width:100},
            { field: 'remark',displayName:'备注',width:100},
            { field: 'id',displayName:'操作',pinnedRight:true,width:130,cellTemplate:'<a ui-sref="func.activityOrderInfo({id:row.entity.id})">详情</a>' }
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
    $scope.query = function () {
        $scope.loadImg = true;
        $http.post("activityOrder/actOrderInfoQuery","info="+angular.toJson($scope.info)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}}).success(function (data) {
            $scope.loadImg = false;
            $scope.gridOptions.data = data.page.result;
            $scope.gridOptions.totalItems = data.page.totalCount;
        })

        $http.post("activityOrder/actOrderInfoCount","info="+angular.toJson($scope.info),{headers: {'Content-Type': 'application/x-www-form-urlencoded'}}).success(function (data) {
            $scope.loadImg = false;
            if(data.total!=null){
                $scope.total.transAmount = data.total.transAmount;
            }else{
                $scope.total.transAmount='';
            }
        })

    }
    $scope.agentList= [];
    //获取代理商
    $http.post("agentInfo/selectAllInfo")
        .success(function(msg){
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
                            for(var i=0; i<response.data.length; i++){
                                $scope.agentt.push({value:response.data[i].agentNo,text:response.data[i].agentNo + " " + response.data[i].agentName});
                            }
                            $scope.agentList = $scope.agentt;
                            oldValue = value;
                        });
                },800);
        }
    };

    $scope.oneAgentList=[];
    //一级代理商
    $http.post("agentInfo/selectAllOneInfo")
        .success(function(msg){
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
                            for(var i=0; i<response.data.length; i++){
                                $scope.agenttOne.push({value:response.data[i].agentNo,text:response.data[i].agentNo + " " + response.data[i].agentName});
                            }
                            $scope.oneAgentList = $scope.agenttOne;
                            oldValueOne = value;
                        });
                },800);
        }
    };


    $scope.clear = function () {
        $scope.info = {};
    }
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
                    location.href="activityOrder/actOrderInfoExport?info="+angular.toJson($scope.info);
                }
            });

    }

})








