/**
 * 黑名单管理
 */
angular.module('inspinia',['uiSwitch']).controller('blacklistQueryCtrl',function($scope, $http, $state,$interval, $stateParams, i18nService,$filter,SweetAlert,$document) {
    i18nService.setCurrentLang('zh-cn');
    $scope.info={rollType:-1,rollBelong:2,rollStatus:-1};
    $scope._rollStatus=[{text:"全部",value:-1},{text:"开启",value:1},{text:"关闭",value:0}];
    $scope.blacklistDate=[];
    $scope.blackLoglistDate=[];
    $scope.blackLogRollNo='0';
    //查询
    $scope.query=function(){
        if($scope.info.sdate>$scope.info.edate){
            $scope.notice("起始时间不能大于结束时间");
            return;
        }
        $http.post('riskRollAction/selectRollAllInfo',
            "info="+angular.toJson($scope.info)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(!data.bols){
                    $scope.notice("查询失败");
                    return;
                }
                $scope.blacklistDate =data.page.result;
                $scope.blacklistGrid.totalItems = data.page.totalCount;
            })
    };
    $scope.query();
    //清空
    $scope.reset = function() {
        $scope.info={rollType:-1,rollBelong:2,rollNo:"",rollName:"",rollStatus:-1,sdate:"",edate:""}
    };

    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    $scope.blacklistGrid = {
        data:"blacklistDate",
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	  //切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        columnDefs: [
            {field: 'id',displayName: 'ID',width: 100,pinnable: false,sortable: false},
            {field: 'rollNo',displayName: '商户编号/身份证号/银行卡号/手机号',width: 270,pinnable: false,sortable: false},
            {field: 'rollTypeName',displayName: '黑名单类型',width: 120,pinnable: false,sortable: false},
            {field: 'rollStatus',displayName: '状态',width: 130,pinnable: false,sortable: false,
                cellTemplate:
                '<span ng-show="grid.appScope.hasPermit(\'blackList.switch\')"><switch class="switch switch-s" ng-model="row.entity.rollStatus" ng-change="grid.appScope.open(row)" /></span>'
                +'<span ng-show="!grid.appScope.hasPermit(\'blackList.switch\')"> <span ng-show="row.entity.rollStatus==1">开启</span><span ng-show="row.entity.rollStatus==0">关闭</span></span>'
            },

            {field: 'createTime',displayName: '创建时间',width: 150,pinnable: false,sortable: false,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
            {field: 'userName',displayName: '创建人',width: 150,pinnable: false,sortable: false},
            {field: 'remark',displayName: '备注',width: 250,pinnable: false,sortable: false},
            {field: 'userMsg',displayName: '代理商提示语',width: 250,pinnable: false,sortable: false},
            {field: 'id',displayName: '操作',width: 150,pinnedRight: true,pinnable: false,sortable: false,editable:true,cellTemplate:
                "<div  class='lh30'>" +
                " <a ng-show='grid.appScope.hasPermit(\"blackList.update\")'  ui-sref='risk.blacklistUp({id:row.entity.id})'>修改 </a>" +
                "<a ng-show='grid.appScope.hasPermit(\"blackList.delete\")'  ng-click='grid.appScope.deleteBlacklist(row.entity.id)'> | 删除 </a>" +
                "<a ng-show='grid.appScope.hasPermit(\"blackList.update\")'  ng-click='grid.appScope.showLog(row.entity.rollNo)'> | 日志 </a>" +
                "</div>"
            }
        ],
        onRegisterApi: function(gridApi) {
            $scope.blacklistGridApi = gridApi;
            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.paginationOptions.pageNo = newPage;
                $scope.paginationOptions.pageSize = pageSize;
                $scope.query();
            });
        }
    };

    //<!--黑名单日志查看弹出框,并查询请求-->
    $scope.showLog = function(rollNo){
        $scope.blackLogRollNo=rollNo;
        $http.post('riskRollAction/selectLogsByRollNo',
            "rollNo="+rollNo+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize+'&temp='+new Date().getTime(),
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(!data.bols){
                    $scope.notice("查询失败");
                    return;
                }

                $scope.blackLoglistDate =data.page.result;
                $scope.showLogGrid.totalItems = data.page.totalCount;
                $("#showLogDiv").modal({height:400,width:860});
            });
    };

   // <!--黑名单日志查询展示结果-->
    $scope.showLogGrid = {
        data:"blackLoglistDate",
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50],	  //切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        columnDefs: [
            {field: 'createTime',displayName: '操作时间',width: 150,pinnable: false,sortable: false,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',enableColumnMenu: false,enableSorting: false},
            {field: 'operationType',displayName: '操作功能',width: 150,pinnable: false,sortable: false,enableColumnMenu: false,enableSorting: false},
            {field: 'createBy',displayName: '操作人',width: 150,pinnable: false,sortable: false,enableColumnMenu: false,enableSorting: false},
            {field: 'remark',displayName: '备注',width: 380,pinnable: false,sortable: false,enableColumnMenu: false,enableSorting: false}
        ],
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.paginationOptions.pageNo = newPage;
                $scope.paginationOptions.pageSize = pageSize;
                $scope.showLog($scope.blackLogRollNo);
            });
        }
    };

    //批量删除
    $scope.delBatch = function() {
        var ids = [];
        var rows = $scope.blacklistGridApi.selection.getSelectedRows();
        for (var i=0; i<rows.length; i++) {
            ids.push(rows[i].id);
        }
        ids = ids.join(",");
        if (ids == "") {
            $scope.notice("请在表格中选择要删除的黑名单");
        } else {
            SweetAlert.swal({
                    title: "确认删除选中的黑名单吗？",
                    type: "warning",
                    showCancelButton: true,
                    confirmButtonColor: "#DD6B55",
                    confirmButtonText: "确认",
                    cancelButtonText: "取消",
                    closeOnConfirm: true,
                    closeOnCancel: true },
                function (isConfirm) {
                    if (isConfirm) {
                        $http.post('riskRollAction/delBatch',
                            "ids="+angular.toJson(ids),
                            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                            .success(function(data){
                                if(!data.bols){
                                    $scope.notice(data.msg);
                                }else{
                                    $scope.notice(data.msg);
                                    $scope.query();
                                }
                            })
                    }
                });
        }
    };

    //批量打开
    $scope.openBatch = function() {
        var ids = [];
        var rows = $scope.blacklistGridApi.selection.getSelectedRows();
        for (var i=0; i<rows.length; i++) {
            ids.push(rows[i].id);
        }
        ids = ids.join(",");
        if (ids == "") {
            $scope.notice("请在表格中选择要打开的黑名单");
        } else {
            SweetAlert.swal({
                    title: "确认打开选中的黑名单吗？",
                    type: "warning",
                    showCancelButton: true,
                    confirmButtonColor: "#DD6B55",
                    confirmButtonText: "确认",
                    cancelButtonText: "取消",
                    closeOnConfirm: true,
                    closeOnCancel: true },
                function (isConfirm) {
                    if (isConfirm) {
                        $http.post('riskRollAction/openBatch',
                            "ids="+angular.toJson(ids),
                            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                            .success(function(data){
                                if(!data.bols){
                                    $scope.notice(data.msg);
                                }else{
                                    $scope.notice(data.msg);
                                    $scope.query();
                                }
                            })
                    }
                });
        }
    };

    $scope.handleResults=1;
    $scope.handleRemark='';
    $scope.rollNo='';
    $scope.rollType='';
    $scope.updateHandleStatusCommit=function(){
        var data={"id":$scope.id,"rollStatus":$scope.handleResults,"remark":$scope.handleRemark,"rollNo":$scope.rollNo,"rollType":$scope.rollType};
        $http.post("riskRollAction/updateRollStatus",angular.toJson(data))
            .success(function(msg){
                if(msg.bols){
                    $scope.notice(msg.msg);
                    $('#updateStatusModal').modal('hide');
                    $scope.query();
                }else{
                    $scope.notice(msg.msg);
                    $('#updateStatusModal').modal('hide');
                    $scope.query();
                }
            }).error(function(){
        });
    };

    $scope.updateHandleStatusColse=function(){
        $('#updateStatusModal').modal('hide');
        $scope.query();
    };

    //开通 || 关闭
    $scope.open=function(row){
        if(row.entity.rollStatus){
            $scope.serviceText = "确定开启？";
        } else {
            $scope.serviceText = "确定关闭？";
        }
       // <!---->
        $('#myTitle').html($scope.serviceText);
        $scope.id=row.entity.id;
        if(row.entity.rollStatus==true){
            $scope.handleResults=1;
        } else if(row.entity.rollStatus==false){
            $scope.handleResults=0;
        }
        $scope.handleRemark = row.entity.remark;
        $scope.rollNo=row.entity.rollNo;
        $scope.rollType=row.entity.rollType;
        $('#updateStatusModal').modal('show');
     //   <!---->
        /*
        SweetAlert.swal({
            title: $scope.serviceText,
            html:true,
            text: '备注<textarea rows="3" cols="20" id="myRemark">'+row.entity.remark+'</textarea>',
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "提交",
            cancelButtonText: "取消",
            closeOnConfirm: true,
            closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    if(row.entity.rollStatus==true){
                        row.entity.rollStatus=1;
                        row.entity.remark = $('#myRemark').val();
                    } else if(row.entity.rollStatus==false){
                        row.entity.rollStatus=0;
                        row.entity.remark = $('#myRemark').val();
                    }
                    $http.post("riskRollAction/updateRollStatus",angular.toJson(row.entity))
                    .success(function(data){
                        if(data.bols){
                            SweetAlert.swal("提示", "操作成功！", "success");
                        }else{
                            if(row.entity.rollStatus==true){
                                row.entity.rollStatus = false;
                            } else {
                                row.entity.rollStatus = true;
                            }
                            SweetAlert.swal("提示", "操作失败！", "error");
                        }
                    })
                    .error(function(data){
                        if(row.entity.rollStatus==true){
                            row.entity.rollStatus = false;
                        } else {
                            row.entity.rollStatus = true;
                        }
                        $scope.notice("服务器异常")
                    });
                } else {
                    if(row.entity.rollStatus==true){
                        row.entity.rollStatus = false;
                    } else {
                        row.entity.rollStatus = true;
                    }
                }
        });
*/
    };

    //删除黑名单
    $scope.deleteBlacklist=function(id){
        SweetAlert.swal({
                title: "确认删除该条黑名单吗？",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确认",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post('riskRollAction/deleteByid?ids='+id).success(function(msg){
                        if(msg.bols){
                            $scope.notice(msg.msg);
                            $scope.query();
                        } else {
                            $scope.notice(msg.msg);
                        }
                    }).error(function(){
                    });
                }
            });
    };

    $scope.openAddblacklist=function(entity){
        $scope.roll={rollNo:entity.rollNo,rollType:entity.rollType,rollNumber:""};
        $('#addblacklist').modal('show');
    };

    $scope.addblacklistNo=function(){
        $scope.submitting =true;
        if($scope.roll.rollNumber==""||$scope.roll.rollNumber==null){
            $scope.notice("请填写完整的信息");
            $scope.submitting = false;
            return;
        }
        $http.post('riskRollAction/addRollListInfo',
            "info="+angular.toJson($scope.roll),
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(!data.bols){
                    $scope.notice(data.msg);
                    $scope.submitting = false;
                }else{
                    $scope.notice(data.msg);
                    $('#addblacklist').modal('hide');
                    $scope.submitting = false;
                }
            })
    };

    //黑名单导出
    //by zouruijin
    //zrj@eeepay.cn rjzou@qq.com
    $scope.exportInfo=function(){
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
                  $scope.exportInfoClick("riskRollAction/exportInfo.do",{"info":angular.toJson($scope.info)});
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

});
