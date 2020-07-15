/**
 * 红包审核
 */
angular.module('inspinia').controller('redEnvelopesGrantExamineCtrl',function($scope,$http,$stateParams,i18nService,SweetAlert){
    //数据源
    i18nService.setCurrentLang('zh-cn');
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

    $scope.payTypeSelect=[{text:"全部",value:''},{text:"分润账户余额",value:'0'},{text:"微信支付",value:'1'},
        {text:"红包账户余额",value:'2'},{text:"内部账户",value:'3'}];
    $scope.payTypeStr=angular.toJson($scope.payTypeSelect);

    // 红包评论明细状态
    $scope.statusDisSelect=[{text:"全部",value:''},{text:"正常",value:'0'},{text:"已删除",value:'1'}];
    $scope.statusDisStr=angular.toJson($scope.statusDisSelect);

    $scope.info={};
    $scope.imageAllState=0;
    //获取用户详情
    $scope.getRedEnvelopesDetail = function(){
        $http({
            url:'redEnvelopesGrant/selectById/'+$stateParams.id,
            method:'GET'
        }).success(function(data) {
            if (data.status) {
                $scope.info=data.regDetail;
                $scope.imageList = data.imageList;
                if(data.imageList!=null&&data.imageList.length>0){
                    $scope.imageAllState=1;
                }
            } else {
                $scope.notice(result.msg);
            }
        }).error(function(){
            $scope.notice("系统异常，请稍后再试");
        });
    };
    $scope.getRedEnvelopesDetail();

    $scope.gridDisPaginationOptions={pageNo:1,pageSize:10};
    //红包评论明细查询
    $scope.disQuery=function(){
        $http.post("redEnvelopesGrant/selectRedEnvelopesGrantDiscuss","id="+$stateParams.id+"&pageNo="+
            $scope.gridDisPaginationOptions.pageNo+"&pageSize="+$scope.gridDisPaginationOptions.pageSize,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.gridDis.data=data.page.result;
                    $scope.gridDis.totalItems=data.page.totalCount;
                }else{
                    $scope.notice(data.msg);
                }
            });
    }
    //红包评论明细
    $scope.gridDis={                           //配置表格
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'id',displayName:'评论ID' },
            { field: 'userCode',displayName:'用户iD'},
            { field: 'userNickName',displayName:'微信呢称'},
            { field: 'createDate',displayName:'评论时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
            { field: 'content',displayName:'评论内容',width:200 },
            { field: 'status',displayName:'状态',cellFilter:"formatDropping:"+$scope.statusDisStr},
            { field: 'id',displayName:'操作',pinnedRight:true,
                cellTemplate:'<div class="lh30"> ' +
                '<a ng-show="row.entity.status==0" ng-click="grid.appScope.deleteDis(row.entity)"  target="_black" >删除</a>' +
                '</div>'
            }
        ],
        onRegisterApi: function(gridApi) {
            $scope.gridDisApi = gridApi;
            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.gridDisPaginationOptions.pageNo = newPage;
                $scope.gridDisPaginationOptions.pageSize = pageSize;
                $scope.disQuery();
            });
        }
    };
    $scope.disQuery();

    //评论删除
    $scope.deleteDiscuss = function(baseInfo){
        $http({
            url:'redEnvelopesGrant/deleteRedEnvelopesGrantDiscuss',
            method:'POST',
            data:baseInfo
        }).success(function(msg){
            $scope.notice(msg.msg);
            if(msg.status){
                $scope.disQuery();
                $scope.cancel();
            }
        });
    };

    $scope.deleteDis = function (discussInfo) {
        $scope.discussInfo = discussInfo;
        $scope.discussInfo.reason = '1';
        $('#deleteModal').modal('show');
    }

    //风控关闭红包modal
    $scope.modifyStatusRisk = function(){
        $scope.redOrdersOption = {reason:'1', redOrderId:$stateParams.id,status:'1'};
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
                $scope.getRedEnvelopesDetail();
                $scope.cancel();
            }
        });
    }
    //风控开启红包
    $scope.openStatusRisk = function(){
        SweetAlert.swal({
                title: "确认开启？",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $scope.redOrdersOption = {redOrderId:$stateParams.id,status:'0'};
                    $http({
                        url:'redEnvelopesGrant/updateStatusRisk',
                        method:'POST',
                        data:$scope.redOrdersOption
                    }).success(function(msg){
                        $scope.notice(msg.msg);
                        if(msg.status){
                            $scope.getRedEnvelopesDetail();
                        }
                    });
                }
            });
    }

    $scope.cancel = function(){
        $('#deleteModal').modal('hide');
        $('#riskModal').modal('hide');
    }

    //修改图片状态
    $scope.modifyImage = function(id,state){
        if(state==1){
            SweetAlert.swal({
                    title: "确认屏蔽？",
                    type: "warning",
                    showCancelButton: true,
                    confirmButtonColor: "#DD6B55",
                    confirmButtonText: "提交",
                    cancelButtonText: "取消",
                    closeOnConfirm: true,
                    closeOnCancel: true },
                function (isConfirm) {
                    if (isConfirm) {
                        $http.get('redEnvelopesGrant/updateRedEnvelopesGrantImage/'+id+'/'+state)
                            .success(function(msg){
                                $scope.notice(msg.msg);
                                $scope.getRedEnvelopesDetail();
                            }).error(function(msg){
                            $scope.notice(msg.msg);
                        });
                    }
                });
        }else if(state==0){
            SweetAlert.swal({
                    title: "确认开启？",
                    type: "warning",
                    showCancelButton: true,
                    confirmButtonColor: "#DD6B55",
                    confirmButtonText: "提交",
                    cancelButtonText: "取消",
                    closeOnConfirm: true,
                    closeOnCancel: true },
                function (isConfirm) {
                    if (isConfirm) {
                        $http.get('redEnvelopesGrant/updateRedEnvelopesGrantImage/'+id+'/'+state)
                            .success(function(msg){
                                $scope.notice(msg.msg);
                                $scope.getRedEnvelopesDetail();
                            }).error(function(msg){
                                $scope.notice(msg.msg);
                        });
                    }
                });
        }

    }
    //修改图片状态
    $scope.modifyImageAll = function(){
        SweetAlert.swal({
                title: "确认屏蔽所有图片？",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.get('redEnvelopesGrant/updateRedEnvelopesGrantImageAll/'+$stateParams.id)
                        .success(function(msg){
                            $scope.notice(msg.msg);
                            $scope.getRedEnvelopesDetail();
                        }).error(function(msg){
                        $scope.notice(msg.msg);
                    });
                }
            });
    }
    //修改图片状态
    $scope.modifyRemark = function(){
        SweetAlert.swal({
                title: "确认屏蔽说明？",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.get('redEnvelopesGrant/updateRemark/'+$stateParams.id)
                        .success(function(msg){
                            $scope.notice(msg.msg);
                            $scope.getRedEnvelopesDetail();
                        }).error(function(msg){
                        $scope.notice(msg.msg);
                    });
                }
            });
    }

});
