/**
 * 代理商账户控制
 */
angular.module('inspinia',['uiSwitch']).controller('agentAccountControlCtrl',function($scope,$http,$state,$stateParams,$compile,$filter,$log,$uibModal,$timeout,SweetAlert,i18nService){
    i18nService.setCurrentLang('zh-cn');
    $scope.baseInfo={"status":0};
    $scope.submitting = false;
    $scope.info={};
    $scope.statusType=0;
    $scope.agentAccountControl={};
    $scope.paginationOptions = {
        pageNo: 1,
        pageSize: 10
    };

    // 代理商账户控制的表格
    $scope.gridOptions={                           //配置表格
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
        useExternalPagination: true,                //分页数量
        columnDefs:[                           //表格数据
            { field: 'agentName',displayName:'代理商名称',width:200},
            { field: 'agentNo',displayName:'代理商编号',width:200},
            { field: 'retainAmount',displayName:'账户留存金额',width:200},
            { field: 'status',displayName:'控制开关',width:200,cellTemplate:
                    '<span ><switch class="switch switch-s" ng-true-value="1" ng-false-value="0" ng-model="row.entity.status" ng-change="grid.appScope.open1(row)" /></span>'
            },
            { field: 'action',displayName:'操作',width:200,
                cellTemplate:
                '<a class="lh30" ng-show="grid.appScope.hasPermit(\'agentAccountControl.editAgentAccountControl\')" ng-click="grid.appScope.editAgentAccountControl(row.entity)">修改</a>'+
                '<a class="lh30" ng-show="grid.appScope.hasPermit(\'agentAccountControl.deleteAgentAccountControl\')" ng-click="grid.appScope.deleteAgentAccountControl(row.entity)"> | 删除</a>'
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

    $scope.clear = function () {
        $scope.info = {};
    };

    //代理商账户控制-列表
    $scope.query = function(){
        $http({
            url: 'agentAccountControl/queryAgentAccountControl?pageNo=' + $scope.paginationOptions.pageNo + "&pageSize=" + $scope.paginationOptions.pageSize,
            data: $scope.info,
            method: 'POST'
        }).success(function (data) {
            if(data.status){
                $scope.gridOptions.data = data.page.result;
                $scope.gridOptions.totalItems = data.page.totalCount;
                console.log(data.page.result);
                $scope.gridOptions.totalItems = data.page.totalCount;
            }else{
                $scope.notice(data.msg);
            }
        }).error(function(){
        });
    }
    $scope.query();

    //代理商账户控制-默认
    $scope.queryDefault = function(){
        $http.get('agentAccountControl/queryAgentAccountControlByDefault')
            .success(function(msg){
                if(msg.status){
                    if(msg.agentAccountControlDefault){
                        $scope.baseInfo = msg.agentAccountControlDefault;
                    }
                } else {
                    $scope.notice(msg.msg);
                }
            });
    }
    $scope.queryDefault();

    //添加
    $scope.hardWardAddModel = function(){
        $scope.statusType=0;
        $("#hardWardAddModel").modal("show");
        $("#agentNo").removeAttr("disabled");
        $scope.agentAccountControl={};
    };

    //返回
    $scope.cancel=function(){
        $scope.agentAccountControl={};
        $('#hardWardAddModel').modal('hide');
    };

    //修改
    $scope.editAgentAccountControl = function(entity){
        $scope.statusType=1;
        $http.post("agentAccountControl/queryAgentAccountControlByAgentNo",entity.agentNo).success(function (data) {
            if(data.status){
                $scope.agentAccountControl = data.param;
                $("#agentNo").attr("disabled","disabled");
                $("#hardWardAddModel").modal("show");
            }
        })
    };

    //提交
    $scope.commit = function(){
        $scope.date=$scope.baseInfo;
        if($scope.baseInfo.status){
            $scope.date.status=1;
        }else{
            $scope.date.status=0;
        }
        var isNum=/^(([1-9][0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,2})))$/;
        if($scope.baseInfo.status){
            if($scope.baseInfo.retainAmount==undefined||$scope.baseInfo.retainAmount==null){
                $scope.notice("账户留存金额不能为空!");
                return;
            }else{
                if($scope.baseInfo.retainAmount!=0&&!isNum.test($scope.baseInfo.retainAmount)){
                    $scope.notice("账户留存金额格式不正确!");
                    return;
                }
            }
        }
        if ($scope.submitting == true) {
            return;
        }
        $scope.submitting = true;

        $http.post("agentAccountControl/saveAgentAccountControl",$scope.date)
            .success(function(data){
                if(data.status){
                    $scope.notice(data.msg);
                    $scope.query();
                }else{
                    $scope.notice(data.msg);
                }
                $scope.submitting = false;
            })
            .error(function(){
                $scope.submitting = false;
            });
    }

    //查找代理商id
    $scope.queryAgentNoById = function(){
        if($scope.agentAccountControl.agentNo){
            $http.post("agentAccountControl/queryAgentByID",$scope.agentAccountControl.agentNo).success(function (data) {
                if(data.status){
                    $scope.agentAccountControl.agentNo=data.agent.agentNo;
                    $scope.agentAccountControl.agentName=data.agent.agentName;
                }else{
                    $scope.notice("代理商不存在！");
                    $scope.agentAccountControl.agentNo="";
                }
            })
        }
    };

    //新增代理商账户控制
    $scope.addAgentAccountControl = function(){
        var isNum=/^(([1-9][0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,2})))$/;
        if($scope.agentAccountControl.agentNo == null || $scope.agentAccountControl.agentNo==""){
            $scope.notice("代理商编号不能为空!");
            return;
        }
        if($scope.agentAccountControl.agentName == null || $scope.agentAccountControl.agentName==""){
            $scope.notice("没有查找到对应的代理商!");
            return;
        }
        if($scope.agentAccountControl.retainAmount==undefined || $scope.agentAccountControl.retainAmount==null ){
            $scope.notice("留存金额不能为空!");
            return;
        }else{
            if($scope.agentAccountControl.retainAmount!=0&&!isNum.test($scope.agentAccountControl.retainAmount)){
                $scope.notice("留存金额格式不正确!");
                return;
            }
        }
        if ($scope.submitting == true) {
            return;
        }
        $scope.submitting = true;
        if($scope.statusType==0) {
            $http.post("agentAccountControl/addAgentAccountControl", $scope.agentAccountControl)
                .success(function (data) {
                    if (data.status) {
                        $scope.notice(data.msg);
                        $scope.cancel();
                        $scope.query();
                    } else {
                        $scope.notice(data.msg);
                    }
                    $scope.submitting = false;
                })
                .error(function () {
                    $scope.submitting = false;
                });
            $scope.clear();
        }else if($scope.statusType==1){
            $http.post("agentAccountControl/editAgentAccountControl", $scope.agentAccountControl)
                .success(function (data) {
                    if (data.status) {
                        $scope.notice(data.msg);
                        $scope.cancel();
                        $scope.query();
                    } else {
                        $scope.notice(data.msg);
                    }
                    $scope.submitting = false;
                })
                .error(function () {
                    $scope.submitting = false;
                });
            $scope.clear();
        }
    };

    $scope.open1=function(row){
        if(row.entity.status){
            $scope.serviceText = "确定开启？";
        } else {
            $scope.serviceText = "确定关闭？";
        }
        SweetAlert.swal({
                title: $scope.serviceText,
//            text: "服务状态为关闭后，不能正常交易!",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    if(row.entity.status==true){
                        row.entity.status=1;
                    } else if(row.entity.status==false){
                        row.entity.status=0;
                    }
                    $http.post("agentAccountControl/updateAgentAccountControlSwitch.do",row.entity)
                        .success(function(data){
                            if(data.status){
                                $scope.notice("操作成功！");
                                $scope.query();
                            }else{
                                if(row.entity.status==true){
                                    row.entity.status = false;
                                } else {
                                    row.entity.status = true;
                                }
                                $scope.notice("操作失败！");
                            }
                        })
                        .error(function(data){
                            if(row.entity.status==true){
                                row.entity.status = false;
                            } else {
                                row.entity.status = true;
                            }
                            $scope.notice("服务器异常")
                        });
                } else {
                    if(row.entity.status==true){
                        row.entity.status = false;
                    } else {
                        row.entity.status = true;
                    }
                }
            });

    };

    //删除代理商账户控制
    $scope.deleteAgentAccountControl = function(entity){
        if(entity.status){
            $scope.notice("状态为开启中，不可以删除");
            return;
        }
        SweetAlert.swal({
                title: "确认删除代理商账户控制?",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post('agentAccountControl/deleteAgentAccountControl',entity.agentNo)
                        .success(function(msg){
                            $scope.notice(msg.msg);
                            $scope.query();
                        }).error(function(msg){
                        $scope.notice(msg.msg);
                    });
                }
            });
    };

    //取消提交，并还原正在编辑的配置信息
    $scope.submitCancel=function(){
        $scope.query();
    }
});



