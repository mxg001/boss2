/**
 * 定时任务预警人
 */
angular.module('inspinia',['infinity.angular-chosen']).controller("taskWarningPeopleCtrl", function($scope, $http,$state,i18nService,SweetAlert,$timeout) {
    //数据源
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.info={};
    $scope.userList={};
    $scope.addInfo={};
    $scope.submitting=false;
    $scope.total=0;
    $scope.remaining=0;
    $scope.query=function(){
        if($scope.submitting){
            return;
        }
        $scope.submitting = true;
        $http.post("warningPeople/taskWarningPeople","info="+ angular.toJson($scope.info)+"&pageNo="+
            $scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.total=data.total;
                    $scope.remaining=data.remaining;
                    $scope.result=data.page.result;
                    $scope.warningPeopleGrid.totalItems = data.page.totalCount;
                }else{
                    $scope.notice(data.msg);
                }
                $scope.submitting = false;
            });
    }
    $scope.synchronous=function () {
        $http.get('warningPeople/synchronous/3')
            .success(function(data){
                if(data.status){
                    var str1=$scope.ListToStr(data.updateList);
                    var str2=$scope.ListToStr(data.removeList);
                    retStr="同步用户数据成功,";
                    var i=0;
                    if(str1!=null&&str1!=""){
                        retStr=retStr+"更新了"+str1+"预警人信息!";
                        i++;
                    }
                    if(str2!=null&&str2!=""){
                        retStr=retStr+"删除了"+str2+"预警人!";
                        i++;
                    }
                    if(i>0){
                        $scope.notice(retStr);
                    }
                }
                $scope.query();
            }).error(function(data){
                $scope.notice(data.msg);
            }
        );
    }
    $scope.ListToStr=function (listStr) {
        var str="";
        if(listStr!=null&&listStr.length>0){
            for(var i=0;i<listStr.length;i++){
                if(str==""){
                    str=str+listStr[i];
                }else{
                    str=str+","+listStr[i];
                }
            }
        }
        return str;
    };
    $scope.synchronous();

    $scope.remove=function (id) {
        SweetAlert.swal({
                title: "确认删除？",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.get('warningPeople/remove/'+id)
                        .success(function(msg){
                            $scope.notice(msg.msg);
                            $scope.query();
                        }).error(function(msg){
                            $scope.notice(msg.msg);
                        }
                    );
                }
            });
    }

    $scope.warningPeopleGrid = {
        data: 'result',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs: [
            {field: 'id',displayName: '预警人ID',pinnable: false,sortable: false,width:140},
            {field: 'name',displayName: '姓名',pinnable: false,sortable: false,width:140},
            {field: 'phone',displayName: '手机号',pinnable: false,sortable: false,width:140},
            {field: 'userName',displayName: '用户名',pinnable: false,sortable: false,width:140},
            {field: 'assignmentTask',displayName: '已配任务',pinnable: false,sortable: false,width:500},
            {field: 'id',displayName: '操作',pinnedRight:true,width:180,cellTemplate:
            '<div class="lh30">' +
            '<a ng-click="grid.appScope.remove(row.entity.id)">删除</a>'+
            '<a ng-show="grid.appScope.hasPermit(\'warningPeople.updateWarningPeopleByAssignmentTask\')" ui-sref="sys.taskEditWarningPeople({id:row.entity.id})" target="_black"> | 设置任务</a>'+
            '</div>'}
        ],
        onRegisterApi: function(gridApi) {
            $scope.warningPeopleGridApi = gridApi;
            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.paginationOptions.pageNo = newPage;
                $scope.paginationOptions.pageSize = pageSize;
                $scope.query();
            });
        }
    };


    $scope.addWarningPeople=function(){
        if($scope.addInfo.id==undefined||$scope.addInfo.id==null||$scope.addInfo.id==""){
            $scope.notice("用户不能为空!");
            return ;
        }
        if($scope.submitting){
            return;
        }
        $scope.submitting = true;
        $http.get('warningPeople/add/3/'+$scope.addInfo.id)
            .success(function(msg){
                $scope.submitting = false;
                $scope.notice(msg.msg);
                $scope.query();
            }).error(function(msg){
                $scope.submitting = false;
                $scope.notice(msg.msg);
            }
        );
    };

    //查询user列表
    $scope.selectUser=function () {
        $http.post("user/getUserlimit","info="+ angular.toJson($scope.info),
            {headers: {'Content-Type':'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    if(data.userList.length<=0){
                        $scope.userList={id:0,telNo:'全部'};
                    }else{
                        $scope.userList=data.userList;
                    }
                }else{
                    $scope.notice(data.msg);
                }
            });
    }
    $scope.selectUser();

    $scope.getselectUser =getselectUserFun;
    var oldValue="";
    var timeNo="";
    function getselectUserFun(value){
        var newValue=value;
        if(newValue != oldValue){
            if (timeNo){
                $timeout.cancel(timeNo);
            }
            timeNo = $timeout(
                function(){
                    $scope.info.telNo=value;
                    $http.post('user/getUserlimit',"info="+ angular.toJson($scope.info),
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function(data){
                            if(data.status){
                                if(data.userList.length<=0){
                                    $scope.userList={id:0,telNo:'全部'};
                                }else{
                                    $scope.userList=data.userList;
                                }
                            }else{
                                $scope.userList={id:0,telNo:'全部'};
                            }
                            oldValue = value;
                        });
                },800);
        }
    }

});
