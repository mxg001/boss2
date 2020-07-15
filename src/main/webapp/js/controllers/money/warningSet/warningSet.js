/**
 * 预警阀值设置
 * 出款服务的
 */
angular.module('inspinia',['uiSwitch', 'infinity.angular-chosen']).controller('moneyWarningSetCtrl',function($scope, $http,$timeout,$document, i18nService,SweetAlert) {
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions = angular.copy($scope.paginationOptions);

    $scope.warnStatusAll = [{text:"全部",value:null},{text:"打开",value:1},{text:"关闭",value:0}];

    //清空
    $scope.resetForm = function() {
        $scope.baseInfo={serviceType:"", warnStatus:null, acqId:null};
    };
    $scope.resetForm();
    $scope.acqOrgs=[{value:null,text:"全部"}];

    //收单机构
    $http.post("acqOrgAction/selectBoxAllInfo")
        .success(function(msg){
            //响应成功
            for(var i=0; i<msg.length; i++){
                $scope.acqOrgs.push({value:msg[i].id,text:msg[i].acqName});
            }
        });

    $scope.columnDefInfo = [
        {field: 'serviceId',displayName: '出款服务ID',width: 120,pinnable: false,sortable: false},
        {field: 'serviceName',displayName: '服务名称',width: 150},
        {field: 'acqEnname',displayName: '收单机构',width: 150},
        {field: 'serviceType',displayName: '服务类型',width: 150, cellFilter:"formatDropping:" + angular.toJson($scope.moneyServiceType)},
        {field: 'warnTimeStr',displayName: '预警时间',width: 150},
        {field: 'warningCycleStr',displayName: '结算中预警周期',width: 180},
        {field: 'exceptionNumber',displayName: '结算中异常笔数',width: 180},
        {field: 'failurWarningCycleStr',displayName: '结算失败预警周期',width: 180},
        {field: 'failurExceptionNumber',displayName: '结算失败异常笔数',width: 180},
        {field: 'warnStatus',displayName: '状态',width: 150,cellTemplate:
            '<span ng-show="grid.appScope.hasPermit(\'warningSetSettle.updateWarnStatus\')"><switch class="switch switch-s" ng-model="row.entity.warnStatus" ng-change="grid.appScope.switchStatus(row)" /></span>'
            +'<span class="lh30" ng-show="!grid.appScope.hasPermit(\'warningSetSettle.updateWarnStatus\')"> <span ng-show="row.entity.warnStatus==1">打开</span><span ng-show="row.entity.warnStatus==0">关闭</span></span>'
        },
        {field: 'action',displayName: '操作',width: 150,pinnedRight:true,sortable: false,editable:true,cellTemplate:
            '<div class="lh30">'
            +'<a ng-show="grid.appScope.hasPermit(\'warningSetSettle.updateWaringInfo\')" '
            + 'ng-click="grid.appScope.updateModal(row.entity)">修改 </a>'
            +'<a ng-show="grid.appScope.hasPermit(\'warningSetSettle.deleteWarning\')'
            + ' && row.entity.warnTimeType!=1" '
            + 'ng-click="grid.appScope.deleteWarning(row)">删除</a>'
            +'</div>'
        }
    ];

    $scope.warnGrid = {
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
//		rowHeight:35,
        columnDefs: $scope.columnDefInfo,
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            $scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.paginationOptions.pageNo = newPage;
                $scope.paginationOptions.pageSize = pageSize;
                $scope.query();
            });
        }
    };
    $scope.warnGrid.data = [];


    //查询
    $scope.query=function(){
        $scope.submitting = true;
        $scope.loadImg = true;
        $http({
            url: 'warningSetSettle/selectPage?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
            data: $scope.baseInfo,
            method:'POST'
        }).success(function (result) {
            $scope.submitting = false;
            $scope.loadImg = false;
            if (!result.status){
                $scope.notice(result.msg);
                return;
            }
            $scope.warnGrid.data = result.data.result;
            $scope.warnGrid.totalItems = result.data.totalCount;
        }).error(function () {
            $scope.submitting = false;
            $scope.loadImg = false;
            $scope.notice('服务器异常,请稍后再试.');
        });
    }

    $scope.updateModal = function(entity){
        $("#updateModal").modal("show");
        $scope.warnInfo = angular.copy(entity);
    }

    $scope.updateWarn = function(){
        $http.post('warningSetSettle/updateWaringInfo', "info=" + angular.toJson($scope.warnInfo),
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function (result) {
                $scope.notice(result.msg);
                if (result.status){
                    $scope.cancel();
                    $scope.query();
                }
            }).error(function () {
                $scope.notice('服务器异常,请稍后再试.');
            });
    }

    $scope.cancel = function(){
        $("#updateModal").modal("hide");
    }

    //修改状态
    $scope.switchStatus=function(row){
        if(row.entity.warnStatus){
            $scope.swalTitle = "确定打开?";
        } else {
            $scope.swalTitle = "确定关闭?";
        }
        SweetAlert.swal({
                title: $scope.swalTitle,
                // text: $scope.serviceText,
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    if(row.entity.warnStatus==true){
                        row.entity.warnStatus=1;
                    } else if(row.entity.warnStatus==false){
                        row.entity.warnStatus=0;
                    }
                    $http({
                        method: 'POST',
                        url: 'warningSetSettle/updateWarnStatus',
                        data: {"warnStatus":row.entity.warnStatus,"id":row.entity.id}
                    }).success(function(data){
                        $scope.notice(data.msg);
                        if(data.status){
                            $scope.query();
                        }else{
                            row.entity.warnStatus = !row.entity.warnStatus;
                        }
                    })
                    .error(function(data){
                        row.entity.warnStatus = !row.entity.warnStatus;
                        $scope.notice("服务器异常");
                    });
                } else {
                    row.entity.warnStatus = !row.entity.warnStatus;
                }
            });
    };

    //删除
    $scope.deleteWarning = function(row){
        SweetAlert.swal({
                title: "确定删除?",
                text: "",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http({
                        method: 'POST',
                        url: 'warningSetSettle/deleteWarning',
                        data: {"id": row.entity.id}
                    }).success(function (data) {
                        $scope.notice(data.msg);
                        if (data.status) {
                            $scope.query();
                        }
                    })
                }
            });
    };

    // 导出
    $scope.exportInfo = function () {
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
                    location.href = "warningSetSettle/exportInfo?baseInfo=" +encodeURI(encodeURI(angular.toJson($scope.baseInfo)));
                }
            });
    };

    //页面绑定回车事件
    $document.bind("keypress", function(event) {
        $scope.$apply(function (){
            if(event.keyCode == 13){
                $scope.query();
            }
        })
    });

})