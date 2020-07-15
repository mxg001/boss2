/**
 * 出款订单详情
 */
angular.module('inspinia').controller('detailCreditRepayOrderCtrl',function($scope,$http,$state,$stateParams,$compile,$log,i18nService){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.statusSelect=[{text:"初始化",value:'0'},{text:"未执行",value:'1'},{text:"还款中",value:'2'},{text:"还款成功",value:'3'},
    	{text:"还款失败",value:'4'},{text:"挂起",value:'5'},{text:"终止",value:'6'},{text:"逾期待还",value:'7'}];
    $scope.billingStatusSelect=[{text:"未记账",value:'0'},{text:"发起记账失败",value:'1'},{text:"记账成功",value:'2'},{text:"记账失败",value:'3'}];

    $http.get('creditRepayOrder/selectById/'+$stateParams.id+'?tallyOrderNo='+$stateParams.tallyOrderNo)
        .success(function(msg) {
            if(msg.status){
                $scope.info=msg.order;
                $scope.settleInfoRecordData=msg.detailList;
            }else{
                $scope.notice(msg.msg);
            }
        });

    //表格
    $scope.settleInfoRecord={
        data:'settleInfoRecordData',
        columnDefs:[
            {field:'planNo',displayName:'任务流水号',width:180,pinnable:false,sortable:false},
            {field:'planAmount',displayName:'金额',width:180,pinnable:false,sortable:false,cellFilter:"currency:''"},
            {field:'accountNo',displayName:'还款卡号',width:180,pinnable:false,sortable:false},
            {field:'planType',displayName:'类型',width:110,pinnable:false,sortable:false,
                cellFilter:"formatDropping:[{text:'给用户还款',value:'IN'},{text:'用户消费',value:'OUT'}]"
            },
            {field:'planStatus',displayName:'结算状态',width:110,pinnable:false,sortable:false,
                cellFilter:"formatDropping:[{text:'未执行',value:'0'},{text:'执行中',value:'1'},{text:'执行成功',value:'2'},{text:'执行失败',value:'3'}]"
            },
            {field:'resMsg',displayName:'错误信息',width:180,pinnable:false,sortable:false},
            {field:'createTime',displayName:'创建时间',width:180,pinnable:false,sortable:false,cellFilter: "date:'yyyy-MM-dd HH:mm:ss'"},
            {field:'planTime',displayName:'计划时间',width:180,pinnable:false,sortable:false,cellFilter: "date:'yyyy-MM-dd HH:mm:ss'"},
            {field:'bak1',displayName:'备注',width:150,pinnable:false,sortable:false}
            //,{field:'bak2',displayName:'备注2',width:150,pinnable:false,sortable:false}
        ]
    };

    /**
     * 获取敏感数据
     */
    $scope.dataSta=true;
    $scope.getDataProcessing = function () {
        if($scope.dataSta){
            $http.get('creditRepayOrder/getDataProcessing/'+$stateParams.id+'?tallyOrderNo='+$stateParams.tallyOrderNo)
                .success(function(data) {
                    if(data.status){
                        $scope.info.mobileNo = data.info.mobileNo;
                        $scope.info.idCardNo = data.info.idCardNo;
                        $scope.dataSta=false;
                    }else{
                        $scope.notice(data.msg);
                    }
                });
        }
    };
})