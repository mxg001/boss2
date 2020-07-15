/**
 * 欢乐返商户详情
 */
angular.module('inspinia').controller('happyBackDetailCtrl',function($scope,$http,$stateParams,i18nService){
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    $scope.baseInfo = {merchantNo:$stateParams.merchantNo,transTimeStart:"",transTimeEnd:""};
    $scope.mbp=[];
    $scope.entryStatuses = [{text:'未入账',value:'0'},{text:'已入账',value:'1'}];
    $scope.entryBuckleStatuses = [{text:'未扣款',value:'0'},{text:'已扣款',value:'1'}];
    $scope.transferStatuses = [{text:'未扣款',value:'0'},{text:'已发起预调账',value:'1'}];
    $scope.cashBackSwitches = [{text:'关闭',value:'0'},{text:'打开',value:'1'}];

    //查询欢乐返商户分润详情
    $scope.query = function(){
        $http.get('activityDetail/selectHappyBackDetailInfo.do?ids='+$stateParams.hId)
            .success(function(msg){
                if(msg.status){
                    $scope.agentCashBackDetailList=msg.acbList;
                    $scope.agentFullPrizeDetailList=msg.acbfpList;
                    $scope.agentNoFullBuckleDetailList=msg.acbnfbList;
                    $scope.mbp=msg.mbp;
                    angular.forEach( $scope.agentFullPrizeDetailList,function(item){
                        if(item.entryStatus==0){
                            item.entryTime="";
                        }
                    })
                    angular.forEach( $scope.agentNoFullBuckleDetailList,function(item){
                        if(item.preTransferStatus==1){
                            item.entryStatus="";
                            item.entryTime="";
                        }
                        if(item.entryStatus==0){
                            item.entryTime="";
                        }
                    })
                }
            });
    }
    $scope.query();

    //代理商返现明细
    $scope.agentCashBackDetail={
        data:'agentCashBackDetailList',
        columnDefs:[
            {field:'agentName',displayName:'代理商名称',pinnable:false,sortable:false},
            {field:'agentNo',displayName:'代理商编号',pinnable:false,sortable:false},
            {field:'agentLevel',displayName:'代理商级别',pinnable:false,sortable:false},
            {field:'cashBackAmount',displayName:'返现金额',pinnable:false,sortable:false},
            {field:'cashBackSwitch',displayName:'返现开关状态',pinnable:false,sortable:false,cellFilter:"formatDropping:" + angular.toJson($scope.cashBackSwitches)},
            {field:'entryStatus',displayName:'返现入账状态',pinnable:false,sortable:false,cellFilter:"formatDropping:" + angular.toJson($scope.entryStatuses)},
            {field:'entryTime',displayName:'入账时间',pinnable:false,sortable:false,cellFilter: "date:'yyyy-MM-dd HH:mm:ss'"},
            {field:'remark',displayName:'备注',pinnable:false,sortable:false}]
    }

    //代理商满奖明细
    $scope.agentFullPrizeDetail={
        data:'agentFullPrizeDetailList',
        columnDefs:[
            {field:'agentName',displayName:'代理商名称',pinnable:false,sortable:false},
            {field:'agentNo',displayName:'代理商编号',pinnable:false,sortable:false},
            {field:'agentLevel',displayName:'代理商级别',pinnable:false,sortable:false},
            {field:'cashBackAmount',displayName:'满奖金额',pinnable:false,sortable:false},
            {field:'cashBackSwitch',displayName:'满奖开关状态',pinnable:false,sortable:false,cellFilter:"formatDropping:" + angular.toJson($scope.cashBackSwitches)},
            {field:'entryStatus',displayName:'入账状态',pinnable:false,sortable:false,cellFilter:"formatDropping:" + angular.toJson($scope.entryStatuses)},
            {field:'entryTime',displayName:'入账时间',pinnable:false,sortable:false,cellFilter: "date:'yyyy-MM-dd HH:mm:ss'"},
            {field:'remark',displayName:'备注',pinnable:false,sortable:false}]
    }

    //代理商不满扣明细
    $scope.agentNoFullBuckleDetail={
        data:'agentNoFullBuckleDetailList',
        columnDefs:[
            {field:'agentName',displayName:'代理商名称',pinnable:false,sortable:false},
            {field:'agentNo',displayName:'代理商编号',pinnable:false,sortable:false},
            {field:'agentLevel',displayName:'代理商级别',pinnable:false,sortable:false},
            {field:'cashBackAmount',displayName:'不满扣金额',pinnable:false,sortable:false},
            {field:'cashBackSwitch',displayName:'不满扣开关状态',pinnable:false,sortable:false,cellFilter:"formatDropping:" + angular.toJson($scope.cashBackSwitches)},
            {field:'entryStatus',displayName:'扣款状态',pinnable:false,sortable:false,cellFilter:"formatDropping:" + angular.toJson($scope.entryBuckleStatuses)},
            {field:'entryTime',displayName:'扣款时间',pinnable:false,sortable:false,cellFilter: "date:'yyyy-MM-dd HH:mm:ss'"},
            {field:'preTransferStatus',displayName:'预调账状态',pinnable:false,sortable:false,cellFilter:"formatDropping:" + angular.toJson($scope.transferStatuses)},
            {field:'preTransferTime',displayName:'预调账时间',pinnable:false,sortable:false,cellFilter: "date:'yyyy-MM-dd HH:mm:ss'"},
            {field:'remark',displayName:'备注',pinnable:false,sortable:false}]
    }


    $scope.exclusionStatuss=function(data){
        if(data==-1){
            return '全部';
        }
        if(data==1){
            return '不互斥';
        }
        if(data==0){
            return '互斥';
        }
    };




    $scope.statusTypes=function(data){
        if(data==""){
            return '全部';
        }
        if(data==1){
            return '未激活';
        }
        if(data==2){
            return '已激活';
        }
        if(data==6){
            return '已返鼓励金';
        }
        if(data==7){
            return '已扣款';
        }
        if(data==8){
            return '预调账已发起';
        }
        if(data==9){
            return '已奖励';
        }
    };




    $scope.subjectTypes=function(data){
        if(data==""){
            return '全部';
        }
        if(data=="008"){
            return '欢乐返-循环送';
        }
        if(data=="009"){
            return '欢乐返';
        }

    };


});

