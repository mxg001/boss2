angular.module('inspinia',['uiSwitch']).controller('switchSpecialSet072Ctrl',function(i18nService,$scope,$http,$state,$stateParams,$compile,$filter,SweetAlert){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    //查询开关设置相关信息
    $scope.getFunInfo = function(){
        $http.get('functionManager/getFunctionManagerInfo?functionNumber=' + $stateParams.functionNumber)
            .success(function(data){
                if(data.status){
                    $scope.baseInfo = data.info;
                    $scope.titleName = $scope.baseInfo.functionName;
                } else {
                    $scope.notice(data.msg);
                }
            });
    };
    $scope.getFunInfo();

    //查询开关设置相关信息
    $scope.query = function(){
        $http.get('functionManager/getFunctionManagerTeamList?functionNumber=' + $stateParams.functionNumber)
            .success(function(data){
                if(data.status){
                    $scope.result = data.list;
                } else {
                    $scope.notice(data.msg);
                }
            });
    };
    $scope.query();

    $scope.teamGrid={                           //配置表格
        data: 'result',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            {field: 'teamName', displayName: '组织'},
            {field: 'teamId', displayName: '组织ID'},
            {field: 'teamEntryName', displayName: '子组织'},
            {field: 'teamEntryId', displayName: '子组织ID'},
            {field: 'txFee', displayName: '提现手续费（元）'},
            {field: 'status', displayName: '开关状态',width:90,cellTemplate:
                '<div class="lh30">'+
                '<span ><switch class="switch switch-s" ng-true-value="1" ng-false-value="0" ng-model="row.entity.status" ng-change="grid.appScope.openSwitch(row)" /></span>' +
                '</div>'
            },
            { field: 'id',displayName:'操作',width:150,pinnedRight:true, cellTemplate:
                '<div class="lh30">'+
                '<a ng-click="grid.appScope.teamEditModalShow(row.entity.id)">修改</a> ' +
                '<a ng-show="false" ng-click="grid.appScope.deleteTeamInfo(row.entity.id)">删除</a> ' +
                '</div>'
            }
        ],
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
        }
    };

    $scope.submitting = false;
    //保存开关基本信息
    $scope.submit = function(){
        if($scope.submitting){
            return;
        }
        $scope.submitting = true;
        $http({
            method: "post",
            url: "functionManager/updateBaseInfo",
            data: $scope.baseInfo
        }).success(function(result){
            $scope.notice(result.msg);
            $scope.submitting = false;
        }).error(function(){
            $scope.notice('服务异常');
            $scope.submitting = false;
        });
    };

    $scope.teamType=[];
    //组织
    $http.get('teamInfo/queryTeamName.do').success(function(msg){
        $scope.teamType.push({text:"请选择",value:null});
        for(var i=0;i<msg.teamInfo.length;i++){
            $scope.teamType.push({text:msg.teamInfo[i].teamName,value:msg.teamInfo[i].teamId+","+msg.teamInfo[i].teamName});
        }
    });
    //获取所有的子组织数据
    $scope.allSubTeams = [];
    $http.get('teamInfo/querySubTeams').success(function(result){
        $scope.subTeamMap = result.subTeamMap;
    });

    //组织数据变动时间
    $scope.hasSubTeam = function(teamId){
        if(teamId == "" || teamId == null){
            $scope.subTeams = [];
        }else {
            var key=teamId.split(",")[0];
            $scope.subTeams = [];
            var temp = $scope.subTeamMap[key];
            if(null != temp && temp != undefined){
                $scope.subTeams.push({text:"请选择",value:null});
                angular.forEach(temp, function (e) {
                    $scope.subTeams.push({text:e.teamEntryName,value:e.teamEntryId+","+e.teamEntryName});
                });
            }
        }
        if($scope.commitType==1){
            $scope.addInfo.teamEntryId = null;
        }
    };

    $scope.commitType=1;//1-新增，2-修改

    $scope.teamAddModalShow = function(){
        $scope.commitType=1;
        $scope.addInfo = {teamId:null,teamEntryId:null};
        $scope.subTeams = [];
        $('#teamAddModal').modal('show');
    };

    $scope.teamAddModalHide = function(){
        $('#teamAddModal').modal('hide');
    };

    //修改
    $scope.teamEditModalShow = function(id){
        $http.post("functionManager/getTeamModelInfo","functionNumber=072&id="+id,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.addInfo = data.info;
                    $scope.commitType=2;
                    $("#teamAddModal").modal("show");
                    $scope.addInfo.teamId=$scope.addInfo.teamId+","+$scope.addInfo.teamName;
                    if($scope.addInfo.teamEntryId!=null){
                        $scope.addInfo.teamEntryId=$scope.addInfo.teamEntryId+","+$scope.addInfo.teamEntryName;
                    }
                    $scope.hasSubTeam($scope.addInfo.teamId);
                }else{
                    $scope.notice(data.msg);
                }
            })
            .error(function(data){
                $scope.notice(data.msg);
            });
    };

    //新增
    $scope.submittingMode=false;
    $scope.teamAdd=function () {
        if($scope.submittingMode){
            return;
        }
        //校验
        $scope.submittingMode=true;
        $scope.subInfo=angular.copy($scope.addInfo);
        $scope.subInfo.functionNumber=$stateParams.functionNumber;
        //数据转换
        if($scope.subInfo.teamId!=null&&$scope.subInfo.teamId!=""){
            var strs=$scope.subInfo.teamId.split(",");
            $scope.subInfo.teamId=strs[0];
            $scope.subInfo.teamName=strs[1];
        }
        if($scope.subInfo.teamEntryId!=null&&$scope.subInfo.teamEntryId!=""){
            var strs=$scope.subInfo.teamEntryId.split(",");
            $scope.subInfo.teamEntryId=strs[0];
            $scope.subInfo.teamEntryName=strs[1];
        }

        var data={
            info:angular.toJson($scope.subInfo),
        };
        var postCfg = {
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            transformRequest: function (data) {
                return $.param(data);
            }
        };


        var commitUrl=null;
        if($scope.commitType==1){
            commitUrl="functionManager/saveFunctionTeam";
        }else{
            commitUrl="functionManager/updateFunctionTeam";
        }

        $http.post(commitUrl,data,postCfg)
            .success(function(data){
                if(data.status){
                    $scope.query();
                    $scope.notice(data.msg);
                    $scope.teamAddModalHide();
                }else{
                    $scope.notice(data.msg);
                }
                $scope.submittingMode=false;
            })
            .error(function(data){
                $scope.notice(data.msg);
                $scope.submittingMode=false;
            });
    };
    //删除
    $scope.deleteTeamInfo=function(id){
        SweetAlert.swal({
                title: "确认删除?",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post("functionManager/deleteFunctionTeam","id="+id,
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function(data){
                            if(data.status){
                                $scope.query();
                                $scope.notice(data.msg);
                            }else{
                                $scope.notice(data.msg);
                            }
                        })
                        .error(function(data){
                            $scope.notice(data.msg);
                        });

                }
            });
    };

    $scope.openSwitch=function(row){
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
                    var data={"status":row.entity.status,"id":row.entity.id};
                    $http.post("functionManager/updateFunctionManageConfigStatus.do",angular.toJson(data))
                        .success(function(data){
                            if(data.status){
                                $scope.notice("操作成功！");
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



});