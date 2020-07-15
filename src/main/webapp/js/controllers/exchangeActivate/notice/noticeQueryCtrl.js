/**
 * 产品列表
 */
angular.module('inspinia').controller('exchangeActivateNoticeQueryCtrl',function($scope,$http,i18nService,SweetAlert,$document){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    $scope.statusSelect = [{text:"全部",value:"-1"},{text:"正常",value:"1"},
        {text:"待下发",value:"2"},{text:"置顶",value:"3"}];
    $scope.statusStr=angular.toJson($scope.statusSelect);

    $scope.oemList=[];
    //组织列表
    $http.post("exchangeActivateOem/getOemList",
        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function(data){
            if(data.status){
                $scope.oemList.push({value:"",text:"全部"});
                var list=data.list;
                if(list!=null&&list.length>0){
                    for(var i=0; i<list.length; i++){
                        $scope.oemList.push({value:list[i].oemNo,text:list[i].oemName});
                    }
                }
            }else{
                $scope.notice(data.msg);
            }
        });

    //清空
    $scope.clear=function(){
        $scope.info={oemNo:"",title:"",status:"-1",
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
            { field: 'status',displayName:'状态',width:120,cellFilter:"formatDropping:" +  $scope.statusStr },
            { field: 'createTime',displayName:'创建时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'sendTime',displayName:'下发时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'sendUser',displayName:'下发人',width:180 },
            { field: 'lastUpdateTime',displayName:'最后修改时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'id',displayName:'操作',width:180,pinnedRight:true, cellTemplate:
                '<div class="lh30">'+
                '<a target="_blank" ui-sref="exchangeActivate.noticeDetail({id:row.entity.id})">详情</a> ' +
                '<a ng-show="grid.appScope.hasPermit(\'exchangeActivateNotice.updateNotice\')&&row.entity.status==2" target="_blank" ui-sref="exchangeActivate.noticeEdit({id:row.entity.id})"> | 修改</a> ' +
                '<a ng-show="grid.appScope.hasPermit(\'exchangeActivateNotice.updateNoticeState\')&&row.entity.status==2" ng-click="grid.appScope.updateNoticeState(row.entity.id,1)""> | 下发</a> ' +
                '<a ng-show="grid.appScope.hasPermit(\'exchangeActivateNotice.updateNoticeState\')&&row.entity.status!=2" ng-click="grid.appScope.updateNoticeState(row.entity.id,2)""> | 回收</a> ' +
                '<a ng-show="grid.appScope.hasPermit(\'exchangeActivateNotice.updateNoticeState\')&&row.entity.status==1" ng-click="grid.appScope.updateNoticeState(row.entity.id,3)""> | 置顶</a> ' +
                '<a ng-show="grid.appScope.hasPermit(\'exchangeActivateNotice.updateNoticeState\')&&row.entity.status==3" ng-click="grid.appScope.updateNoticeState(row.entity.id,4)""> | 取消置顶</a> ' +
                '<a ng-show="grid.appScope.hasPermit(\'exchangeActivateNotice.deleteNotice\')&&row.entity.status==2" ng-click="grid.appScope.deleteNotice(row.entity.id)""> | 删除</a> ' +
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
        $http.post("exchangeActivateNotice/selectAll","info="+angular.toJson($scope.info)+"&pageNo="+
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
                    $http.post("exchangeActivateNotice/deleteNotice","id="+id,
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
                    $http.post("exchangeActivateNotice/updateNoticeState","id="+id+"&state="+sta,
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

    //页面绑定回车事件
    $document.bind("keypress", function(event) {
        $scope.$apply(function (){
            if(event.keyCode == 13){
                $scope.query();
            }
        })
    });
});