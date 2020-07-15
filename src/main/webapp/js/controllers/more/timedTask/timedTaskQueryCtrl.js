/**
 * 定时任务监控
 */
angular.module('inspinia').controller('timedTaskQueryCtrl',function($scope,$http,$stateParams,SweetAlert,i18nService,$document){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.taskStatusSelect=[{text:"全部",value:''},{text:"不存在",value:'NONE'},{text:"正常",value:'NORMAL'},
        {text:"暂停",value:'PAUSED'},{text:"完成",value:'COMPLETE'},{text:"错误",value:'ERROR'},{text:"阻塞",value:'BLOCKED'}];
    $scope.enabledStateSelect=[{text:"全部",value:-1},{text:"未启动",value:0},{text:"启动",value:1}];
    $scope.warningStateSelect=[{text:"全部",value:-1},{text:"否",value:0},{text:"是",value:1}];
    $scope.taskStatusStr=angular.toJson($scope.taskStatusSelect);
    $scope.enabledStateStr=angular.toJson($scope.enabledStateSelect);
    $scope.warningStateStr=angular.toJson($scope.warningStateSelect);

    $scope.taskInfo={};
    //清空
    $scope.clear=function(){
        $scope.info={taskName:"",taskGroup:"",enabledState:-1,taskStatus:""};
    }
    $scope.clear();

    $scope.query=function(){
        if ($scope.loadImg) {
            return;
        }
        $scope.loadImg = true;
        $http.post("timedTask/selectByParam","info=" + angular.toJson($scope.info)+"&pageNo="+
            $scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.result=data.page.result;
                    $scope.gridOptions.totalItems = data.page.totalCount;
                }else{
                    $scope.notice(data.msg);
                }
                $scope.loadImg = false;
            });
    }
    $scope.query();

    $scope.gridOptions={                           //配置表格
        data: 'result',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'id',displayName:'应用编号',width:150 },
            { field: 'taskName',displayName:'定时任务名称',width:150 },
            { field: 'taskGroup',displayName:'定时任务组',width:150 },
            { field: 'remarks',displayName:'运营备注',width:180 },
            { field: 'programName',displayName:'程序名称',width:150 },
            { field: 'expression',displayName:'表达式',width:150 },
            { field: 'enabledState',displayName:'任务状态',cellFilter:"formatDropping:"+$scope.enabledStateStr,width:150},
            { field: 'retrievalTime',displayName:'最新检索时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'taskStatus',displayName:'当时定时状态',cellFilter:"formatDropping:"+$scope.taskStatusStr,width:150},
            { field: 'lastTime',displayName:'上次执行时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'nextTime',displayName:'预计下次执行时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'warningState',displayName:'是否预警',cellFilter:"formatDropping:"+$scope.warningStateStr,width:150},
            { field: 'earlyWarningThreshold',displayName:'超时阀值(分钟)',width:180 },
            { field: 'abnormalTime',displayName:'异常开始时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'id',displayName:'操作',pinnedRight:true,width:250,
                cellTemplate:'<div class="lh30">' +
                '<a ui-sref="sys.detailTimedTask({id:row.entity.id})" target="_black" >详情</a>'+
                '<a ng-show="grid.appScope.hasPermit(\'timedTask.editSubmit\')" ui-sref="sys.editTimedTask({id:row.entity.id})" target="_black" > | 修改</a>'+
                '<a ng-show="grid.appScope.hasPermit(\'timedTask.resetTimedTask\')" ng-click="grid.appScope.resetTimedTask(row.entity)"> | 重启</a>' +
                '<a ng-show="row.entity.enabledState==1&&grid.appScope.hasPermit(\'timedTask.closedTimedTask\')" ng-click="grid.appScope.closedTimedTask(row.entity)"> | 停止</a>' +
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
    //关闭
    $scope.closedTimedTask=function (entity) {
        SweetAlert.swal({
                title: "确认关闭定时任务？",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true
            },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post("timedTask/closedTimedTask","jobName="+entity.taskName+"&jobGroup="+entity.taskGroup,
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function(data){
                            if(data.status){
                                $scope.notice(data.msg);
                            }else{
                                $scope.notice(data.msg);
                            }
                            $scope.query();
                        })
                        .error(function(){
                            $scope.notice('系统异常');
                        });
                }
            });
    }
    //重置
    $scope.resetTimedTask=function (entity) {
        SweetAlert.swal({
                title: "确认重启定时任务？",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true
            },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post("timedTask/resetTimedTask","jobName="+entity.taskName+"&jobGroup="+entity.taskGroup,
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function(data){
                            if(data.status){
                                $scope.notice(data.msg);
                            }else{
                                $scope.notice(data.msg);
                            }
                            $scope.query();
                        })
                        .error(function(){
                            $scope.notice('系统异常');
                        });
                }
            });
    }

    //页面绑定回车事件
    $document.bind("keypress", function(event) {
        $scope.$apply(function (){
            if(event.keyCode == 13){
                $scope.query();
            }
        })
    });
})