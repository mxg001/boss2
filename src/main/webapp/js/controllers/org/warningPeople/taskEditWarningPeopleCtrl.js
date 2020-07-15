/**
 * 定时任务预警人--设置任务
 */
angular.module('inspinia').controller("taskEditWarningPeopleCtrl", function($scope, $http,$state,i18nService,$stateParams) {
    //数据源
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.wp={};
    $http.post("warningPeople/taskWarningPeopleEdit","id="+$stateParams.id,
        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function(data){
            if(data.status){
                $scope.wp=data.wp;
                $scope.assignmentTaskList=data.list;

                $http.post("timedTask/getTimedTaskByWarningState","warningState=1",
                    {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                    .success(function(msg){
                        if(msg.status){
                            $scope.result=msg.list;
                        }else{
                            $scope.notice(msg.msg);
                        }
                    });
            }else{
                $scope.notice(data.msg);
            }
        });

    $scope.grid = {
        data: 'result',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs: [
            {field: 'id',displayName: '应用编号',pinnable: false,sortable: false,width:140},
            {field: 'taskName',displayName: '定时任务名称',pinnable: false,sortable: false,width:140},
            {field: 'taskGroup',displayName: '定时任务组',pinnable: false,sortable: false,width:140},
            {field: 'programName',displayName: '程序名称',pinnable: false,sortable: false,width:200},
            {field: 'remarks',displayName: '运营备注',pinnable: false,sortable: false,width:400}
        ],
        onRegisterApi : function(gridApi) {
            $scope.gridApi = gridApi;
        },
        isRowSelectable: function(row){ // 选中行
            if($scope.assignmentTaskList != null && $scope.assignmentTaskList.length>0){
                for(var i=0;i<$scope.assignmentTaskList.length;i++){
                    if(row.entity.id==$scope.assignmentTaskList[i]){
                        row.grid.api.selection.selectRow(row.entity);
                        break;
                    }
                }
            }
        }
    };
    $scope.submitGrid=function () {
        var selectList=$scope.gridApi.selection.getSelectedRows();
        var ids = "";
        if(selectList.length>0){
            for(var i=0;i<selectList.length;i++) {
                var item = selectList[i];
                ids = ids + item.id + ",";
            }
            if(ids!=""){
                ids=ids.substring(0,ids.length-1);
            }
        }
        $http.post("warningPeople/updateWarningPeopleByAssignmentTask","assignmentTask="+ids+"&id="+$stateParams.id,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(msg){
                if(msg.status){
                    $scope.notice(msg.msg);
                    $state.transitionTo('sys.taskWarningPeople',null,{reload:true});
                }else{
                    $scope.notice(msg.msg);
                }
            });
    }
});
