/**
 * 红包发放查询
 */
angular.module('inspinia').controller('queryRedEnvelopesGrantCtrl',function($scope,$http,$state,$stateParams,$compile,$uibModal,SweetAlert,$log,i18nService,$document,$timeout){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.statusSelect=[{text:"全部",value:''},{text:"初始化",value:'-1'},{text:"发放中",value:'0'},{text:"已领完",value:'1'},
        {text:"已到期",value:'2'}];
    $scope.statusStr=angular.toJson($scope.statusSelect);

    $scope.pushTypeSelect=$scope.redPushTypes;
    $scope.pushTypeStr=angular.toJson($scope.pushTypeSelect);

    $scope.receiveTypeSelect=$scope.redReceiveTypes;
    $scope.receiveTypeStr=angular.toJson($scope.receiveTypeSelect);

    $scope.busTypeSelect= $scope.redBusTypes;
    $scope.busTypeStr=angular.toJson($scope.busTypeSelect);

    $scope.pushAreaSelect=$scope.redPushAreas;
    $scope.pushAreaStr=angular.toJson($scope.pushAreaSelect);

    $scope.hasProfitSelect=[{text:"全部",value:''},{text:"是",value:'0'},{text:"否",value:'1'}];
    $scope.hasProfitStr=angular.toJson($scope.hasProfitSelect);

    $scope.statusRiskSelect=[{text:"全部",value:''},{text:"正常",value:'0'},{text:"已屏蔽",value:'1'}];
    $scope.statusRiskStr=angular.toJson($scope.statusRiskSelect);

    $scope.statusRecoverySelect=[{text:"全部",value:''},{text:"待处理",value:'0'},{text:"处理成功",value:'1'},
        {text:"处理失败",value:'2'},{text:"处理中",value:'3'}];
    $scope.statusRecoveryStr=angular.toJson($scope.statusRecoverySelect);

    $scope.statusAccountSelect=[{text:"全部",value:''},{text:"待入账",value:'0'},{text:"已记账",value:'1'},
        {text:"记账失败",value:'2'}];
    $scope.statusAccountStr=angular.toJson($scope.statusAccountSelect);

    $scope.recoveryTypeSelect=[{text:"全部",value:''},{text:"原路退回",value:'0'},{text:"归平台所有",value:'1'},
        {text:"无需处理",value:'2'}];
    $scope.recoveryTypeStr=angular.toJson($scope.recoveryTypeSelect);
    //支付方式(0分润账户余额1微信支付2红包账户余额3内部账户4支付宝支付
    $scope.payTypeSelect=[{text:"全部",value:''},{text:"分润账户余额",value:'0'},{text:"微信支付",value:'1'},
        {text:"红包账户余额",value:'2'},{text:"内部账户",value:'3'},{text:"支付宝支付",value:'4'}];
    $scope.payTypeStr=angular.toJson($scope.payTypeSelect);

    //清空
    $scope.clear=function() {
        $scope.info = {
            status: "", pushType: "", receiveType: "", busType: "", hasProfit: "", statusRisk: "",
            recoveryType: "", statusRecovery: "", pushArea: "", payType: "", orgId: "",
            createDateMin: moment(new Date().getTime() - 6 * 24 * 60 * 60 * 1000).format('YYYY-MM-DD') + ' 00:00:00',
            createDateMax: moment(new Date().getTime()).format('YYYY-MM-DD') + ' 23:59:59'
        };
    }
    $scope.clear();

    $scope.allCount=0;
    $scope.amountCount=0;

    //查询所有银行家组织
    $scope.orgInfoList = [];
    $scope.getOrgInfoList = function () {
        $http({
            url:"superBank/getOrgInfoList",
            method:"POST"
        }).success(function(msg){
            if(msg.status){
                $scope.orgInfoList = msg.data;
                $scope.orgInfoList.unshift({orgId:"",orgName:"全部"});
            }
        }).error(function(){
            $scope.notice("获取组织信息异常");
        })
    };
    $scope.getOrgInfoList();


    $scope.query=function(){
        if ($scope.loadImg) {
            return;
        }
        $scope.loadImg = true;
        $http.post("redEnvelopesGrant/selectByParam","info=" + angular.toJson($scope.info)+"&pageNo="+
            $scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.result=data.page.result;
                    $scope.allCount=data.page.totalCount;
                    $scope.gridOptions.totalItems = data.page.totalCount;
                    if(data.sunOrder!=null){
                        $scope.sunOrder=data.sunOrder;
                    }
                }else{
                    $scope.notice(data.msg);
                }
                $scope.loadImg = false;
            }).error(function () {
                $scope.submitting = false;
                $scope.loadImg = false;
                $scope.notice('服务器异常,请稍后再试.');
            });
    }
    //$scope.query();手动查询

    $scope.gridOptions={                           //配置表格
        data: 'result',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'id',displayName:'红包ID',width:180 },
            { field: 'confId',displayName:'红包配置ID',width:180 },
            { field: 'busType',displayName:'业务类型',cellFilter:"formatDropping:"+$scope.busTypeStr,width:150},
            { field: 'pushType',displayName:'发放人类型',cellFilter:"formatDropping:"+$scope.pushTypeStr,width:150},
            { field: 'receiveType',displayName:'接收人类型',cellFilter:"formatDropping:"+$scope.receiveTypeStr,width:150},
            { field: 'orgName',displayName:'发放人组织名称',width:180 },
            { field: 'hasProfit',displayName:'是否收取佣金',cellFilter:"formatDropping:"+$scope.hasProfitStr,width:150},
            { field: 'status',displayName:'红包状态',cellFilter:"formatDropping:"+$scope.statusStr,width:150},
            { field: 'statusRisk',displayName:'风控状态',cellFilter:"formatDropping:"+$scope.statusRiskStr,width:150},
            { field: 'recoveryType',displayName:'剩余金额处理方式',cellFilter:"formatDropping:"+$scope.recoveryTypeStr,width:150},
            { field: 'statusRecovery',displayName:'剩余金额处理状态',cellFilter:"formatDropping:"+$scope.statusRecoveryStr,width:150},
            { field: 'pushArea',displayName:'发放范围',cellFilter:"formatDropping:"+$scope.pushAreaStr,width:150},
            { field: 'pushAmount',displayName:'红包金额',width:180,cellFilter:"currency:''" },
            { field: 'pushNum',displayName:'个数',width:180 },
            // { field: 'pushEachAmount',displayName:'单个领取金额',width:180 },
            { field: 'pushUserCode',displayName:'发红包用户ID',width:180 },
            { field: 'pushRealName',displayName:'发红包用户姓名',width:180 },
            { field: 'pushUserName',displayName:'发红包用户昵称',width:180 },
            { field: 'pushUserPhone',displayName:'发红包手机号',width:180 },
            { field: 'dxUserCode',displayName:'单个定向接收用户ID',width:190 },
            { field: 'dxUserName',displayName:'单个定向接收用户昵称',width:190 },
            { field: 'dxUserPhone',displayName:'单个定向接收用户手机号',width:200 },
            { field: 'payType',displayName:'支付方式',cellFilter:"formatDropping:"+$scope.payTypeStr,width:150},
            { field: 'orderNo',displayName:'关联业务订单ID',width:180 },
            { field: 'payOrderNo',displayName:'关联支付订单ID',width:180 },
            { field: 'pushFee',displayName:'服务费',width:180,cellFilter:"currency:''" },
            { field: 'oneUserProfit',displayName:'一级分润',width:180,cellFilter:"currency:''" },
            { field: 'oneUserCode',displayName:'一级编号',width:180 },
            { field: 'twoUserProfit',displayName:'二级分润',width:180,cellFilter:"currency:''" },
            { field: 'twoUserCode',displayName:'二级编号',width:180 },
            { field: 'thrUserProfit',displayName:'三级分润',width:180,cellFilter:"currency:''" },
            { field: 'thrUserCode',displayName:'三级编号',width:180 },
            { field: 'fouUserProfit',displayName:'四级分润',width:180,cellFilter:"currency:''" },
            { field: 'fouUserCode',displayName:'四级编号',width:180 },
            { field: 'plateProfit',displayName:'平台分润',width:180,cellFilter:"currency:''" },
            { field: 'orgProfit',displayName:'OEM品牌分润',width:180,cellFilter:"currency:''" },
            { field: 'createDate',displayName:'红包创建时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'expDate',displayName:'红包失效时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'id',displayName:'操作',pinnedRight:true,width:180,
                cellTemplate:'<div class="lh30"> ' +
                '<a ui-sref="red.redEnvelopesGrantDetail({id:row.entity.id})"  target="_blank" >详情</a>' +
                '<a ng-show="row.entity.busType==0&&grid.appScope.hasPermit(\'redEnvelopesGrant.update\')" ui-sref="red.redEnvelopesGrantExamine({id:row.entity.id})" target="_blank" > | 审核</a>' +
                '<a ng-show="row.entity.busType==0&&row.entity.statusRisk==0&&grid.appScope.hasPermit(\'redEnvelopesGrant.updateStatusRisk\')" ng-click="grid.appScope.modifyStatusRisk(row.entity)"  > | 风控关闭</a>' +
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

    //<a ng-show="row.entity.busType==0&&row.entity.statusRisk==1&&grid.appScope.hasPermit(\'redEnvelopesGrant.updateStatusRisk\')" ng-click="grid.appScope.openStatusRisk(row.entity)"  > | 取消关闭</a>
    
    $scope.import=function(){
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
                    location.href="redEnvelopesGrant/exportInfo?info="+encodeURI(angular.toJson($scope.info));
                }
            });
    };
    $scope.changeRecoveryType=function(){
        if($scope.info.recoveryType=='2'){
            $scope.info.statusRecovery='0';
        }else{
            $scope.info.statusRecovery='';
        }
    }

    //风控关闭红包modal
    $scope.modifyStatusRisk = function(entity){
        $scope.redOrdersOption = {reason:'1', redOrderId:entity.id,status:'1'};
        $('#riskModal').modal('show');
    }
    //风控关闭红包提交数据
    $scope.riskClose = function(redOrdersOption){
        $http({
            url:'redEnvelopesGrant/updateStatusRisk',
            method:'POST',
            data:redOrdersOption
        }).success(function(msg){
            $scope.notice(msg.msg);
            if(msg.status){
                $scope.cancel();
                $scope.query();
            }
        });
    }
    //风控开启红包
    $scope.openStatusRisk = function(entity){
        SweetAlert.swal({
                title: "取消关闭？",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $scope.redOrdersOption = {redOrderId:entity.id,status:'0'};
                    $http({
                        url:'redEnvelopesGrant/updateStatusRisk',
                        method:'POST',
                        data:$scope.redOrdersOption
                    }).success(function(msg){
                        $scope.notice(msg.msg);
                        if(msg.status){
                            $scope.query();
                        }
                    });
                }
            });
    }

    $scope.cancel = function(){
        $('#riskModal').modal('hide');
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