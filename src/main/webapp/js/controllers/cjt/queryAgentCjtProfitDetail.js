/**
 * 超级推收益明细查询
 */
angular.module('inspinia',['infinity.angular-chosen','uiSwitch']).controller("queryAgentCjtProfitDetail", function($scope, $http, i18nService,$document,SweetAlert,$timeout) {
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    // 入账状态 0-未入账 1-已入账 2-入账失败
    $scope.rechargeStatusList = [{text:"全部", value:null},{text:"未入账", value:"0"},
        {text:"已入账", value:"1"},{text:"入账失败", value:"2"}];

    //奖励类型 recommend推荐奖励 activity活动补贴 posTrade刷卡交易 noCardTrade无卡交易'
    $scope.profitTypeList = [{text:"全部", value:null},{text:"推荐奖励", value:"recommend"},{text:"活动补贴", value:"activity"}];

    //清空
    $scope.resetForm = function () {
        $scope.baseInfo = {profitType:null,rechargeStatus:null,merchantNo:"",
            createTimeStart:moment(new Date().getTime()-7*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
            createTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'
        };
    }
    $scope.resetForm();

    $http({
        url:"cjtProfitDetail/selectAgentProfitSwitch",
        method:"get",
    }).success(function(result) {
        if(result && result.status) {
            $scope.profitAutoSwitch = result.data;
        }
    });

    //查询
    $scope.query = function(){
        $scope.submitting = true;
        $scope.loadImg = true;
        $http({
            url:"cjtProfitDetail/selectAgentPage?pageNo=" + $scope.paginationOptions.pageNo +  "&pageSize=" + $scope.paginationOptions.pageSize,
            method:"post",
            data:$scope.baseInfo
        }).success(function(result){
            $scope.submitting = false;
            $scope.loadImg = false;
            if (!result || !result.status){
                $scope.notice (result.msg);
                return;
            }
            $scope.cjtProfitDetailGrid.data = result.data.page.result;
            $scope.cjtProfitDetailGrid.totalItems = result.data.page.totalCount;
            $scope.totalMap = result.data.totalMap;
            $scope.profitAutoSwitch = result.data.profitAutoSwitch;
        }).error(function(){
            $scope.submitting = false;
            $scope.loadImg = false;
            $scope.notice("服务器异常");
        });
    };
    // $scope.query();

    $scope.columnDefs = [
                    {field: 'orderNo',width:150,displayName: '序号'},
                    {field: 'merchantName',width:150,displayName: '收益代理商名称'},
                    {field: 'merchantNo',width:150,displayName: '收益代理编号'},
                    {field: 'profitAmount',width:150,displayName: '收益奖金'},
                    {field: 'profitTypeStr',width:150,displayName: '收益类型'},
                    {field: 'profitFromAmount',width:150,displayName: '交易金额'},
                    {field: 'profitFromOrderNo',width:150,displayName: '交易订单号'},
                    {field: 'fromMerchantNo',width:150,displayName: '交易商户编号'},
                    {field: 'createTimeStr',width:150,displayName: '收益创建时间'},
                    {field: 'rechargeStatusStr',width:150,displayName: '收益入账状态'},
                    {field: 'rechargeTimeStr',width:150,displayName: '收益入账时间'}
    ];

    $scope.cjtProfitDetailGrid = {
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs: $scope.columnDefs,
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            $scope.gridApi.pagination.on.paginationChanged ($scope, function(newPage, pageSize) {
                $scope.paginationOptions.pageNo = newPage;
                $scope.paginationOptions.pageSize = pageSize;
                $scope.query();
            });
        }
    };

    // 导出
    $scope.export = function () {

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
                    $scope.exportInfoClick("cjtProfitDetail/agentExport",{"baseInfo":angular.toJson($scope.baseInfo)});
                }
            });
    };

    //批量入账
    $scope.rechargeBatch = function(){
        var rowList = $scope.gridApi.selection.getSelectedRows();
        if(rowList == null || rowList.length < 1) {
            $scope.notice("请选择需要批量入账的记录");
            return;
        }
        var orderNoList = [];
        angular.forEach(rowList, function(item) {
           if(item.rechargeStatus != 1 && item.orderNo) {
               orderNoList[orderNoList.length] = item.orderNo;
           }
        });
        if(orderNoList.length < 1 ) {
            $scope.notice("请选择需要批量入账的记录");
            return;
        }
        var title = "已选择" + orderNoList.length + "条记录";
        var text = "是否确定批量入账？";
        SweetAlert.swal({
                title: title,
                text: text,
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http({
                        method: "post",
                        url: "cjtProfitDetail/rechargeAgentBatch",
                        data: orderNoList
                    }).success(function(msg){
                        $scope.notice(msg.msg);
                        if(msg.status){
                            $scope.query();
                        }
                    }).error(function(){
                        $scope.notice('服务异常');
                    });
                }
            }
        );
    }

    $scope.updateProfitAutoSwitch = function(){
        SweetAlert.swal({
                title: $scope.profitAutoSwitch? "确定开启？" : "确定关闭？",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    if($scope.profitAutoSwitch == true){
                        $scope.profitAutoSwitch = 1;
                    } else if($scope.profitAutoSwitch == false){
                        $scope.profitAutoSwitch = 0;
                    }
                    $http({
                        method:"post",
                        url:"cjtProfitDetail/updateAgentProfitAutoSwitch",
                        data: $scope.profitAutoSwitch
                    }).success(function(result){
                        $scope.notice(result.msg);
                        if(!result.status){
                            $scope.profitAutoSwitch = !$scope.profitAutoSwitch;
                        }
                    });
                }
                else {
                    $scope.profitAutoSwitch = !$scope.profitAutoSwitch;
                }
            });
    }

    // -----商户名称/编号 下拉框查询 start -----//
    $scope.merchantList = [{text:"全部",value:""}];
    //获取少量的商户
    $http.post("agentInfo/selectAllInfo",
        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function(msg){
            //响应成功
            for(var i=0; i<msg.length; i++){
                $scope.merchantList.push({value:msg[i].agentNo,text:msg[i].agentNo + " " + msg[i].agentName});
            }
        });

    $scope.getMerchantSelect =getMerchantSelect;
    var oldValueMer="";
    var timeoutMer="";
    function getMerchantSelect(value) {
        $scope.itemList = [];
        var newValueMer=value;
        if(newValueMer != oldValueMer){
            if (timeoutMer) $timeout.cancel(timeoutMer);
            timeoutMer = $timeout(
                function(){
                    $http.post('agentInfo/selectAllInfo','item='+value,
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function (msg) {
                            if(msg.length==0) {
                                $scope.itemList.push({value: "", text:"全部"});
                            }else{
                                for(var i=0; i<msg.length; i++){
                                    $scope.itemList.push({value:msg[i].agentNo,text:msg[i].agentNo + " " + msg[i].agentName});
                                }
                            }
                            $scope.merchantList = $scope.itemList;
                            oldValueMer = value;
                        });
                },800);
        }
    }
    // -----商户名称/编号 下拉框查询 end -----//


    //页面绑定回车事件
    $document.bind ("keypress", function(event) {
        $scope.$apply(function (){
            if(event.keyCode == 13){
                $scope.query();
            }
        })
    });

});