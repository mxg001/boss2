/**
 * 顶层代理商管理
 */

angular.module('inspinia', ['uiSwitch','infinity.angular-chosen','angularFileUpload']).controller('topAgentManagementCtrl',function($scope, $http, i18nService, SweetAlert, $document,$timeout,FileUploader){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions = angular.copy($scope.paginationOptions);
    $scope.baseInfo = {};
    $scope.addRecordData = {};
    $scope.resetForm = function () {
        $scope.baseInfo={agent_authorized:""};
    };
    $scope.resetForm();

    //一级代理商
    $http.post("agentInfo/selectAllOneInfo")
        .success(function(msg){
            $scope.oneAgentListA=[{value:"",text:"全部"}];
            for(var i=0; i<msg.length; i++){
                $scope.oneAgentListA.push({value:msg[i].agentNo,text:msg[i].agentNo + " " + msg[i].agentName});
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

    $scope.getAgent = function(){
        $http.get("agentFunctionManager/findAgentInfoByAgentNo.do?agentNo="+$scope.addRecordData.agent_authorized)
            .success(function(data){
                $scope.addRecordData.agent_authorized_name="";
                if(!data){
                    $scope.notice('代理商不存在');
                }else if(data.agentLevel!=1){
                    $scope.notice('代理商不是一级代理商');
                }else{
                    //设置回显
                    $scope.addRecordData.agent_authorized_name = data.agentName;
                }

            });
    }

    $scope.query = function () {
        $scope.loadImg = true;
        $http.post("agentAuth/selectTopAgentManagement","info="+angular.toJson($scope.baseInfo)+"&pageNo="+
            $scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                $scope.agentTopGrid.data=data.page.result;
                $scope.agentTopGrid.totalItems = data.page.totalCount;
                $scope.loadImg = false;
            }).error(function () {
            $scope.loadImg = false;
        });
    };
    $scope.query();
    $scope.agentTopGrid = {
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs: [
            {field: 'agent_authorized', displayName: '代理商编号', width: 200},
            {field: 'agent_authorized_name', displayName: '代理商名称', width: 200},
            {field: 'create_time',displayName: '添加日期',width:200,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
            {field: 'action', displayName: '操作', width: 160, pinnedRight: true, editable: true, cellTemplate:
                '<div class="lh30">' +
                '<a ng-click="grid.appScope.delRecord(row.entity.agent_authorized)">删除</a>' +
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


    //显示modal
    $scope.addRecordModal = function () {
        $scope.addRecordData = {};
        $('#addRecordModal').modal('show');
    };

    //隐藏modal
    $scope.cancel = function () {
        $('#addRecordModal').modal('hide');
    };

    //新增记录
    $scope.addRecord = function () {
        if($scope.addRecordData.agent_authorized==undefined || $scope.addRecordData.agent_authorized.trim().length==0){
            $scope.notice("代理商编号不能为空");
            return ;
        }
        if($scope.addRecordData.agent_authorized_name==undefined || $scope.addRecordData.agent_authorized_name.trim().length==0){
            $scope.notice("代理商名称不能为空");
            return ;
        }
        $http.post("agentAuth/addTopAgentManagement","agent_authorized="+$scope.addRecordData.agent_authorized,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(msg){
                if(msg.status){
                    $scope.cancel();
                    $scope.resetForm();
                    $scope.query();
                    $scope.notice(msg.msg);
                }else{
                    $scope.notice(msg.msg);
                }
            })
    };

    //删除
    $scope.delRecord = function (agent_authorized) {
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
                    $http.post("agentAuth/deleteTopAgentManagement", "agent_authorized=" + agent_authorized, {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function (res) {
                            if(res.status){
                                $scope.resetForm();
                                $scope.query();
                                $scope.notice(res.msg);
                            }else{
                                $scope.notice(res.msg);
                            }
                        })
                        .error(function (res) {
                            $scope.notice(res.msg);
                        });
                }
            });
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