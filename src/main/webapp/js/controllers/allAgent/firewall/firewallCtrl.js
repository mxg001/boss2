/**
 * 防火墙
 */
angular.module('inspinia',['uiSwitch']).controller('firewallCtrl', function ($scope, $http, i18nService, SweetAlert, $document,$q) {
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions = angular.copy($scope.paginationOptions);

    $scope.query = {ctrl_biz_type: "",able_status:"", list_type: "white"};
    $scope.btnText = "保存";
    $scope.subDisable = false;

    //清空
    $scope.clear = function () {
        $scope.query = {
            user_code: "", real_name: "",
            ctrl_biz_type: "", able_status: "",
            ctrl_target_type: "user", list_type: "white",
            createTimeBegin: moment(new Date().getTime() - 6 * 24 * 60 * 60 * 1000).format('YYYY-MM-DD') + ' 00:00:00',
            createTimeEnd: moment(new Date().getTime()).format('YYYY-MM-DD') + ' 23:59:59'
        };
        $scope.addInfo = {ctrl_biz_type: "", able_status: "enable", ctrl_target_type: "user", list_type: "white"};
    };
    $scope.clear();

    $scope.firewallStatusList = [{value:"",text:"全部"},{value:"enable",text:"启用"},{value:"disable",text:"未启用"}];

    $scope.ctrlBizType ={sys_key:"ctrl_biz_type"};

    var promises = [];
    $scope.ctrlBizTypeList = [];
    var hpPromise=$q.defer();
    promises.push(hpPromise.promise);
    $http.post("firewall/selectSysConfig","info="+ angular.toJson($scope.ctrlBizType),
        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function (data) {
            if (data.status) {
                $scope.ctrlBizTypeList.push({value: "", text: "全部"});
                var list = data.list;
                if (list != null && list.length > 0) {
                    for (var i = 0; i < list.length; i++) {
                        $scope.ctrlBizTypeList.push({value: list[i].sys_name, text: list[i].sys_value});
                    }
                }
                hpPromise.resolve();
                delete hpPromise;
            }
        });
    $q.all(promises).then(function(){
        // 获取数据完成了
        promises = [];
        initGrid();
    });

    $scope.userGrid = {                           //配置表格
        data: 'result',
        paginationPageSize: 10,                  //分页数量
        paginationPageSizes: [10, 20, 50, 100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar: true,  		//纵向滚动条
        onRegisterApi: function (gridApi) {
            $scope.gridApi = gridApi;
            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.paginationOptions.pageNo = newPage;
                $scope.paginationOptions.pageSize = pageSize;
                $scope.queryFunc();
            });
        }
    };

    function initGrid(){
        $scope.userGrid.columnDefs=[                           //表格数据
            {field: 'ctrl_target_code', displayName: '盟主编号', width: 180},
            {field: 'real_name', displayName: '盟主姓名', width: 180},
            {field: 'ctrl_biz_type', displayName: '名单类型', width: 180,cellFilter:"formatDropping:" +  angular.toJson($scope.ctrlBizTypeList)},
            {field: 'remark', displayName: '备注', width: 180},
            {field: 'able_status', displayName: '状态', width: 180//,cellFilter:"formatDropping:" +  angular.toJson($scope.firewallStatusList)
                ,cellTemplate:
                    '<span><switch class="switch switch-s" ng-true-value="enable" ng-false-value="disable"' +
                    ' ng-model="row.entity.able_status_switch" ng-change="grid.appScope.recordStatusOp(row.entity)" /></span>'
            },
            {field: 'create_time', displayName: '创建时间', cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"', width: 180},
            {
                field: 'id', displayName: '操作', width: 220, pinnedRight: true, cellTemplate:
                    '<div class="lh30">' +
                    '<a ng-click="grid.appScope.deleteRecord(row.entity.firewall_code)">删除</a> ' +
                    '<a ng-click="grid.appScope.updateRecord(row.entity.firewall_code,row.entity.ctrl_target_code,row.entity.ctrl_biz_type,row.entity.remark)"> | 修改</a> ' +
                    '</div>'
            }
        ];
    }

    $scope.queryFunc = function () {
        if ($scope.loadImg) {
            return;
        }
        // console.log(angular.toJson($scope.query));
        $scope.loadImg = true;
        $http.post("firewall/selectRecordList", "info=" + angular.toJson($scope.query) + "&pageNo=" +
            $scope.paginationOptions.pageNo + "&pageSize=" + $scope.paginationOptions.pageSize,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function (data) {
                if (data.status) {
                    $scope.result = data.page.result;
                    $scope.userGrid.totalItems = data.page.totalCount;
                } else {
                    $scope.notice(data.msg);
                }
                $scope.loadImg = false;
            })
            .error(function (data) {
                $scope.notice(data.msg);
                $scope.loadImg = false;
            });
    };
    //页面绑定回车事件
    $document.bind("keypress", function (event) {
        $scope.$apply(function () {
            if (event.keyCode == 13) {
                $scope.queryFunc();
            }
        })
    });

    $scope.showModel = function (op) {
        $("#showModel").modal("show");
        if("up"===op){
            $scope.btnText = "修改";
            $scope.subDisable = true;
        }else{
            $scope.btnText = "保存";
            $scope.subDisable = false;
            $scope.addInfo.brandCode = "";
            $scope.addInfo.groupName = "";
        }
    };

    $scope.updateRecord = function (firewall_code,ctrl_target_code,ctrl_biz_type,remark) {
        $scope.addInfo.firewall_code = firewall_code;
        $scope.addInfo.ctrl_target_code = ctrl_target_code;
        $scope.addInfo.ctrl_biz_type = ctrl_biz_type;
        $scope.addInfo.remark = remark;
        $scope.showModel("up");
    };

    $scope.deleteRecord = function (firewall_code) {
        $scope.addInfo.firewall_code = firewall_code;
        SweetAlert.swal({
                title: "确定删除吗？",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post("firewall/deleteRecord", "info=" + angular.toJson($scope.addInfo),
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function (data) {
                            $scope.notice(data.msg);
                            $scope.loadImg = false;
                            $("#showModel").modal("hide");
                            $scope.queryFunc();
                        })
                        .error(function (data) {
                            $scope.notice(data.msg);
                            $scope.loadImg = false;
                            $("#showModel").modal("hide");
                        });
                }
            });
    }

    $scope.recordOp = function () {
        if ($scope.loadImg) {
            return;
        }
        if($scope.addInfo.ctrl_biz_type===""){
            $scope.notice("请选择名单类型");
            return;
        }
        console.log($scope.addInfo.ctrl_target_code);
        if (typeof($scope.addInfo.ctrl_target_code) === "undefined" || $scope.addInfo.ctrl_target_code === "") {
            $scope.notice("请填写盟主编号");
            return;
        }
        $scope.loadImg = true;
        var url = $scope.btnText==="保存" ?  "firewall/addRecord" : "firewall/updateRecord";
        $http.post(url, "info=" + angular.toJson($scope.addInfo),
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function (data) {
                $scope.notice(data.msg);
                $scope.loadImg = false;
                $("#showModel").modal("hide");
                $scope.queryFunc();
                $scope.clear();
            })
            .error(function (data) {
                $scope.notice(data.msg);
                $scope.loadImg = false;
                $("#showModel").modal("hide");
                $scope.clear();
            });
    };

    $scope.recordStatusOp = function (row) {
        if (row.able_status_switch) {
            $scope.serviceText = "确定开启？";
        } else {
            $scope.serviceText = "确定关闭？";
        }
        SweetAlert.swal({
                title: $scope.serviceText,
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true
            },
            function (isConfirm) {
                if(isConfirm){
                    // console.log("修改前" + row.able_status);
                    row.able_status = (row.able_status === "disable") ? "enable" : "disable";
                    // console.log("修改后" + row.able_status);
                    $http.post("firewall/updateRecord", "info=" + angular.toJson(row),
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function (data) {
                            $scope.notice(data.msg);
                            $("#showModel").modal("hide");
                            $scope.queryFunc();
                        })
                        .error(function (data) {
                            $scope.notice(data.msg);
                            $("#showModel").modal("hide");
                            $scope.queryFunc();
                        });
                }
            });
    };
    $scope.queryFunc();
});