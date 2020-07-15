angular.module('inspinia', ['uiSwitch','infinity.angular-chosen','angularFileUpload']).controller("agentAuthCtrl",function ($scope, $http, i18nService, SweetAlert, $document,$timeout,FileUploader) {

    $scope.addRecordData={};
    $scope.apiUrl="agentAuth/addRecord";
    $scope.baseInfo = {};
    $scope.paginationOptions = angular.copy($scope.paginationOptions);
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.recordCheckSelect=[{value:"",text:"全部"},{value:"0",text:"未通过"},{value:"1",text:"通过"}];
    $scope.isTopes=[{value:"",text:"全部"},{value:"1",text:"是"},{value:"2",text:"否"}];

    $scope.resetForm = function () {
        $scope.baseInfo={record_check:"",lowerLevelStatus:"0",is_top:""};
    };
    $scope.resetForm();

    //一级代理商
    $http.post("agentInfo/selectAllOneInfo")
        .success(function(msg){
            $scope.oneAgentListA=[{value:"",text:"全部"}];
            $scope.oneAgentListB=[{value:"",text:"全部"}];
            for(var i=0; i<msg.length; i++){
                $scope.oneAgentListA.push({value:msg[i].agentNo,text:msg[i].agentNo + " " + msg[i].agentName});
                $scope.oneAgentListB.push({value:msg[i].agentNo,text:msg[i].agentNo + " " + msg[i].agentName});
            }
        });
    //一级代理商A
    $scope.getOneStatesA =getOneStatesA;
    var oldValueOneA="";
    var timeoutOneA="";
    function getOneStatesA(value) {
        var newValueOneA=value;
        if(newValueOneA != oldValueOneA){
            if (timeoutOneA) $timeout.cancel(timeoutOneA);
            timeoutOneA = $timeout(
                function(){
                    $http.post('agentInfo/selectAllOneInfo','item=' + value,
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .then(function (response) {
                            if(response.data.length==0) {
                                $scope.oneAgentListA =[{value: "", text: "全部"}];
                            }else{
                                $scope.oneAgentListA =[{value: "", text: "全部"}];
                                for(var i=0; i<response.data.length; i++){
                                    $scope.oneAgentListA.push({value:response.data[i].agentNo,text:response.data[i].agentNo + " " + response.data[i].agentName});
                                }
                            }
                            oldValueOneA = value;
                        });
                },800);
        }
    };


    //一级代理商B
    $scope.getOneStatesB =getOneStatesB;
    var oldValueOneB="";
    var timeoutOneB="";
    function getOneStatesB(value) {
        var newValueOneB=value;
        if(newValueOneB != oldValueOneB){
            if (timeoutOneB) $timeout.cancel(timeoutOneB);
            timeoutOneB = $timeout(
                function(){
                    $http.post('agentInfo/selectAllOneInfo','item=' + value,
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .then(function (response) {
                            if(response.data.length==0) {
                                $scope.oneAgentListB =[{value: "", text: "全部"}];
                            }else{
                                $scope.oneAgentListB =[{value: "", text: "全部"}];
                                for(var i=0; i<response.data.length; i++){
                                    $scope.oneAgentListB.push({value:response.data[i].agentNo,text:response.data[i].agentNo + " " + response.data[i].agentName});
                                }
                            }
                            oldValueOneB = value;
                        });
                },800);
        }
    };

    $scope.getAgent = function(){
        $http.get("agentFunctionManager/findAgentInfoByAgentNo.do?agentNo="+$scope.addRecordData.agent_link)
            .success(function(data){
                $scope.addRecordData.createDate="";
                if(!data){
                    $scope.notice('代理商不存在');
                }else{
                    //设置回显
                    $scope.addRecordData.createDate = data.createDate;
                }

            });
    }

    $scope.agentGrid = {
        data: "agentData",
        paginationPageSize: 10,                  //分页数量
        paginationPageSizes: [10, 20, 50, 100],	//切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar: true,  		//纵向滚动条
        columnDefs: [
            {field: 'agent_authorized', displayName: '代理商编号', width: 120},
            {field: 'agent_authorized_name', displayName: '代理商名称', width: 230},
            {field: 'agent_link', displayName: '授权查询代理商编号', width: 170},
            {field: 'agent_link_name', displayName: '下级代理商名称', width: 230},
            {field: 'record_status',displayName: '关联关系开关',width:120,cellTemplate:
                '<span ><switch class="switch switch-s" ng-model="row.entity.record_status" ng-change="grid.appScope.upStatus(row.entity)" /></span>'
            },
            {field: 'is_look',displayName: '数据查询开关',width:120,cellTemplate:
                    '<span ><switch class="switch switch-s" ng-model="row.entity.is_look" ng-change="grid.appScope.upLook(row.entity)" /></span>'
            },
            {field:'record_check',displayName: '审核状态',width: 180,
                cellTemplate:
                '<span ng-show="grid.appScope.hasPermit(\'agentAuth.routing.checkRecord\')">' +
                '   <div ng-show="row.entity.record_check==0">' +
                '       <switch class="switch switch-s" ng-model="row.entity.record_check" ng-change="grid.appScope.upCheckStatus(row.entity)" />' +
                '   </div>' +
                '   <div ng-show="row.entity.record_check==1" style="margin-top:7px;" >' +
                '       <span>开启</span>'+
                '   </div>' +
                '</span>'
                +'<span ng-show="!grid.appScope.hasPermit(\'agentAuth.routing.checkRecord\')" > ' +
                '   <div style="margin-top:7px;">' +
                '      <span ng-show="row.entity.record_check==1">开启</span>' +
                '      <span ng-show="row.entity.record_check==0">关闭</span>' +
                '   </div>' +
                '</span>'
            },
            {field: 'is_top', displayName: '是否为顶层代理商', width: 170,cellFilter:"formatDropping:" +  angular.toJson($scope.isTopes)},
            {field: 'record_creator', displayName: '创建人员', width: 150},
            {field: 'check_user', displayName: '审核人员', width: 150},
            {field: 'create_time',displayName: '创建时间',width:150,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
            {
                field: 'action', displayName: '操作', width: 160, pinnedRight: true, editable: true, cellTemplate:
            '<div class="lh30">' +
            '<a ng-show="grid.appScope.hasPermit(\'agentAuth.routing.upRecord\')&&row.entity.record_check==0" ng-click="grid.appScope.upRecord(row.entity)">修改|  </a>' +
            '<a ng-show="grid.appScope.hasPermit(\'agentAuth.routing.delRecord\')"  ng-click="grid.appScope.delRecord(row.entity)">删除</a>' +
            + '</div>'
            }
        ],
        onRegisterApi: function (gridApi) {
            $scope.gridApi = gridApi;
            $scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.paginationOptions.pageNo = newPage;
                $scope.paginationOptions.pageSize = pageSize;
                $scope.query();
            });
        }
    };
    //审核
    $scope.upCheckStatus = function (data) {
        console.log(data.record_code);
        SweetAlert.swal({
                title: data.record_check ? "确定审核通过？" : "确定审核不通过？",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true
            },
            function (isConfirm) {
                if (isConfirm) {
                    if (data.record_check == true) {
                        data.record_check = 1;
                    } else if (data.record_check == false) {
                        data.record_check = 0;
                    }
                    var js = {record_check: data.record_check, record_code: data.record_code, op_type: "check"};
                    $http.post("agentAuth/routing/checkRecord", "data=" + angular.toJson(js), {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function (res) {
                            $scope.query();
                            $scope.notice(res.msg);
                        })
                        .error(function () {
                            data.record_check = !data.record_check;
                            $scope.notice("服务器异常");
                        });
                } else {
                    data.record_check = !data.record_check;
                }
            });
    };

    //修改状态
    $scope.upStatus = function (sysDict) {
        console.log(sysDict.record_code);
        SweetAlert.swal({
                title: sysDict.record_status ? "确定开启？" : "关闭关联关系后下方的所有三方关系都会自动关闭，是否继续？",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true
            },
            function (isConfirm) {
                if (isConfirm) {
                    if (sysDict.record_status == true) {
                        sysDict.record_status = 1;
                    } else if (sysDict.record_status == false) {
                        sysDict.record_status = 0;
                        sysDict.is_look = 0;
                    }
                    $http.post("agentAuth/upStatus", "info="+angular.toJson(sysDict), {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function (res) {
                            $scope.query();
                            $scope.notice(res.msg);
                        })
                        .error(function () {
                            sysDict.record_status = !sysDict.record_status;
                            $scope.notice("服务器异常");
                        });
                } else {
                    sysDict.record_status = !sysDict.record_status;
                }
            });
    };

    //修改状态
    $scope. upLook = function (sysDict) {
        console.log(sysDict.record_code);
        SweetAlert.swal({
                title: sysDict.is_look ? "确定开启？" : "确定关闭？",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true
            },
            function (isConfirm) {
                if (isConfirm) {
                    if (sysDict.is_look == true) {
                        sysDict.is_look = 1;
                    } else if (sysDict.is_look == false) {
                        sysDict.is_look = 0;
                    }
                    $http.post("agentAuth/upLook", "info="+angular.toJson(sysDict), {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function (res) {
                            $scope.query();
                            $scope.notice(res.msg);
                        })
                        .error(function () {
                            sysDict.is_look = !sysDict.is_look;
                            $scope.notice("服务器异常");
                        });
                } else {
                    sysDict.is_look = !sysDict.is_look;
                }
            });
    };

    //修改
    $scope.upRecord = function (data) {
        $scope.addRecordModal();
        $("#opTypeTitle").html("修改代理商授权查询");
        $scope.apiUrl = "agentAuth/routing/upRecord";
        $scope.addRecordData.op_type = "modify";
        $scope.addRecordData.agent_link = data.agent_link;
        $scope.addRecordData.record_code = data.record_code;
        $scope.addRecordData.record_status = data.record_status;
        $scope.addRecordData.agent_authorized = data.agent_authorized;
    };

    //删除
    $scope.delRecord = function (data) {
        SweetAlert.swal({
                title: "确认删除？",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true
            },
            function (isConfirm) {
                if (isConfirm) {
                    var js = {record_status:data.record_status,record_code:data.record_code};
                    $http.post("agentAuth/delRecord", "data=" + angular.toJson(js), {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function (res) {
                            $scope.query();
                            $scope.notice(res.msg);
                        })
                        .error(function () {
                            $scope.notice("服务器异常");
                        });
                }
            });
    };

    //显示modal
    $scope.addRecordModal = function () {
        $("#opTypeTitle").html("新增代理商授权查询");
        $scope.addRecordData={record_status:1};
        $scope.apiUrl = "agentAuth/addRecord";
        $('#addRecordModal').modal('show');
    };

    //新增记录
    $scope.addRecord = function () {
        var data = $scope.addRecordData;
        data = angular.toJson(data);
        $http.post($scope.apiUrl, "data=" + data
            , {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
        )
            .success(function (msg) {
                $scope.cancel();
                $scope.query();
                $scope.notice(msg.msg);
            });
    };

    //隐藏modal
    $scope.cancel = function () {
        $('#addRecordModal').modal('hide');
    };

    $scope.query = function () {
        $scope.loadImg = true;
        var data = {"baseInfo": $scope.baseInfo, "page": $scope.paginationOptions};
        data = angular.toJson(data);
        $http.post('agentAuth/getDatas', "data="+data
            , {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
        ).success(function (msg) {
            $scope.loadImg = false;
            $scope.agentData = msg.result;
            $scope.agentGrid.totalItems = msg.totalCount;
        }).error(function () {
            $scope.loadImg = false;
        });
    };


    // 导出
    $scope.exportInfo = function () {
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
                    location.href="agentAuth/importDetail?info="+encodeURI(angular.toJson($scope.baseInfo));
                }
            });
    };


    $scope.importDiscountShow = function(){
        $('#importDiscount').modal('show');
    };
    $scope.importDiscountHide = function(){
        $('#importDiscount').modal('hide');
    };
    //上传图片,定义控制器路径
    var uploader = $scope.uploader = new FileUploader({
        url: 'agentAuth/importDiscount',
        queueLimit: 1,   //文件个数
        removeAfterUpload: true,  //上传后删除文件
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    uploader.filters.push({
        name: 'isFile',
        fn: function(item, options) {
            return this.queue.length < 1;
        }
    });
    $scope.clearItems = function(){  //重新选择文件时，清空队列，达到覆盖文件的效果
        uploader.clearQueue();
    };
    $scope.submitting = false;
    //导入
    $scope.importDiscount=function(){
        $scope.submitting = true;
        uploader.uploadAll();//上传
        uploader.onSuccessItem = function(fileItem, response, status, headers) {//上传成功后的回调函数，在里面执行提交
            if(response.status){
                $scope.notice(response.msg);
                $scope.importDiscountHide();
            }else{
                $scope.notice(response.msg);
            }
            $scope.submitting = false;
        };
    };
    //页面绑定回车事件
    $document.bind("keypress", function (event) {
        $scope.$apply(function () {
            if (event.keyCode == 13) {
                $scope.query();
            }
        })
    });

});