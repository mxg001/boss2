/**
 * 产品列表
 */
angular.module('inspinia').controller('noticeAllAgentQueryCtrl',function($scope,$http,i18nService,SweetAlert,$document){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    $scope.statusSelect = [{text:"全部",value:"-1"},{text:"正常",value:"1"},
        {text:"待下发",value:"2"},{text:"置顶",value:"3"}];
    $scope.statusStr=angular.toJson($scope.statusSelect);
    $scope.types= [{text:"全部",value:""},{text:"普通公告",value:1},{text:"首页活动弹层",value:2}];
    $scope.oneUserCodes=[];
    //获取机构
    $http.post("userAllAgent/selectOneUserCodeList")
        .success(function(msg){
            //响应成功
            for(var i=0; i<msg.length; i++){
                $scope.oneUserCodes.push({value:msg[i].userCode,text:msg[i].realName});
            }
        });

    $scope.oemList=[];
    $http.post("awardParam/getOemList",
        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function(data){
            if(data.status){
                $scope.oemList.push({value:"",text:"全部"});
                var list=data.list;
                if(list!=null&&list.length>0){
                    for(var i=0; i<list.length; i++){
                        $scope.oemList.push({value:list[i].brandCode,text:list[i].brandName});
                    }
                }
            }
        });

    //清空
    $scope.clear=function(){
        $scope.info={oemNo:"",title:"",status:"-1",orgSet:"",type:"",
            createTimeBegin:moment(new Date().getTime()-6*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
            createTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
            sendTimeBegin:"",sendTimeEnd:""};
    };
    $scope.clear();

    $scope.userGrid={                           //配置表格
        data: 'result',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'id',displayName:'通告ID',width:180},
            { field: 'title',displayName:'标题',width:180 },
            { field: 'type',displayName:'公告类型',width:120,cellFilter:"formatDropping:" +  angular.toJson($scope.types) },
            { field: 'status',displayName:'状态',width:120,cellFilter:"formatDropping:" +  $scope.statusStr },
            { field: 'createTime',displayName:'创建时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'sendTime',displayName:'下发时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'sendUser',displayName:'下发人',width:180 },
            { field: 'lastUpdateTime',displayName:'最后修改时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'id',displayName:'操作',width:330,pinnedRight:true, cellTemplate:
                '<div class="lh30">'+
                '<a target="_blank" ui-sref="allAgent.noticeDetail({id:row.entity.id})">详情</a> ' +
                '<a ng-show="grid.appScope.hasPermit(\'noticeAllAgent.updateNotice\')&&row.entity.status==2" target="_blank" ui-sref="allAgent.noticeEdit({id:row.entity.id})"> | 修改</a> ' +
                '<a ng-show="grid.appScope.hasPermit(\'noticeAllAgent.updateNoticeState\')&&row.entity.status==2" ng-click="grid.appScope.updateNoticeState(row.entity.id,1)""> | 下发</a> ' +
                '<a ng-show="grid.appScope.hasPermit(\'noticeAllAgent.updateNoticeState\')&&row.entity.status!=2" ng-click="grid.appScope.updateNoticeState(row.entity.id,2)""> | 回收</a> ' +
                '<a ng-show="grid.appScope.hasPermit(\'noticeAllAgent.updateNoticeState\')&&row.entity.status==1" ng-click="grid.appScope.updateNoticeState(row.entity.id,3)""> | 置顶</a> ' +
                '<a ng-show="grid.appScope.hasPermit(\'noticeAllAgent.updateNoticeState\')&&row.entity.status==3" ng-click="grid.appScope.updateNoticeState(row.entity.id,4)""> | 取消置顶</a> ' +
                '<a ng-show="grid.appScope.hasPermit(\'noticeAllAgent.deleteNotice\')&&row.entity.status==2" ng-click="grid.appScope.deleteNotice(row.entity.id)""> | 删除</a> ' +
                '<a ng-show="grid.appScope.hasPermit(\'noticeAllAgent.updateNoticeHome\')&&row.entity.type==2&&row.entity.homeStatus==1" ng-click="grid.appScope.updateNoticeHome(row.entity.id,0)""> | 取消弹层</a> ' +
                '<a ng-show="grid.appScope.hasPermit(\'noticeAllAgent.updateNoticeHome\')&&row.entity.type==2&&row.entity.homeStatus==0" ng-click="grid.appScope.updateNoticeHome(row.entity.id,1)""> | 显示弹层</a> ' +
                '<a ng-show="grid.appScope.hasPermit(\'noticeAllAgent.updateUserCodeSet\')&&row.entity.status==2" ng-click="grid.appScope.showUserCodeSetModal(row.entity)""> | 预览公告</a> ' +
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

    $scope.query=function(){
        if ($scope.loadImg) {
            return;
        }
        $scope.loadImg = true;
        $http.post("noticeAllAgent/selectAll","info="+angular.toJson($scope.info)+"&pageNo="+
            $scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.result=data.page.result;
                    $scope.userGrid.totalItems = data.page.totalCount;
                }else{
                    $scope.notice(data.msg);
                }
                $scope.loadImg = false;
            })
            .error(function(data){
                $scope.notice(data.msg);
                $scope.loadImg = false;
            });
    };

    $scope.deleteNotice=function(id){
        SweetAlert.swal({
                title: "是否删除本公告,删除后不可恢复?",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post("noticeAllAgent/deleteNotice","id="+id,
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function(data){
                            if(data.status){
                                $scope.notice(data.msg);
                                $scope.query();
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
    // 0待下发1正常2置顶
    $scope.updateNoticeState=function(id,sta){
        var title="";
        if(sta==1){
            title="确认下发?";
        }else if(sta==2){
            title="确认回收?";
        }else if(sta==3){
            title="确认置顶?";
        }else if(sta==4){
            title="确认取消置顶?";
        }
        SweetAlert.swal({
                title:title,
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post("noticeAllAgent/updateNoticeState","id="+id+"&state="+sta,
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function(data){
                            if(data.status){
                                $scope.notice(data.msg);
                                $scope.query();
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
    $scope.updateNoticeHome=function(id,homeStatus){
        var title="";
        if(homeStatus==0){
            title="确认取消弹层?";
        }else if(homeStatus==1){
            title="确认显示弹层?";
        }
        SweetAlert.swal({
                title:title,
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post("noticeAllAgent/updateNoticeHome","id="+id+"&homeStatus="+homeStatus,
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function(data){
                            if(data.status){
                                $scope.notice(data.msg);
                                $scope.query();
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

    /**
     *更新预览公告用户
     */
    $scope.showUserCodeSetModal = function(entity){
        $scope.addInfo={id:entity.id,userCodeSet:entity.userCodeSet};
        $('#userCodeSetModal').modal('show');
    };

    $scope.closeUserCodeSetModal = function(){
        $('#userCodeSetModal').modal('hide');
    };

    $scope.commitUserCodeSet=function () {
        if($scope.addInfo.userCodeSet!=null){
            if($scope.addInfo.userCodeSet.indexOf("，")>0){
                $scope.notice("接收预览公告的用户只支持英文逗号!");
                return;
            }
        }
        $http.post("noticeAllAgent/updateUserCodeSet","id="+$scope.addInfo.id+"&userCodeSet="+$scope.addInfo.userCodeSet,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.notice(data.msg);
                    $scope.query();
                    $scope.closeUserCodeSetModal();
                }else{
                    $scope.notice(data.msg);
                }
            })
            .error(function(data){
                $scope.notice(data.msg);
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