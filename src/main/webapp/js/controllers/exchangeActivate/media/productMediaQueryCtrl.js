/**
 * 商品媒体资源
 */
angular.module('inspinia',['infinity.angular-chosen']).controller('productMediaQueryCtrl',function($scope,$http,i18nService,SweetAlert,$document,$timeout){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);


    //获取路由列表
    $scope.routeList=[];
    $http.post("excActRoute/getRouteALLList")
        .success(function(data){
            if(data.status){
                $scope.routeList=[];
                $scope.routeList.push({value:"",text:"全部"});
                var list=data.list;
                if(list!=null&&list.length>0){
                    for(var i=0; i<list.length; i++){
                        $scope.routeList.push({value:list[i].channelNo,text:"["+list[i].channelNo+"]"+list[i].channelName});
                    }
                }
            }
        });

    //清空
    $scope.clear=function(){
        $scope.info={pId:""};
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
            { field: 'id',displayName:'ID',width:180},
            { field: 'channelNo',displayName:'核销渠道编号',width:180 },
            { field: 'channelName',displayName:'核销渠道名称',width:180 },
            { field: 'goodTypeNo',displayName:'上游渠道id',width:180 },
            { field: 'channelGoodNo',displayName:'渠道商品编号',width:180 },
            { field: 'channelGoodName',displayName:'渠道商品名称',width:180 },
            { field: 'createTime',displayName:'查询时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'id',displayName:'操作',width:180,pinnedRight:true, cellTemplate:
                '<div class="lh30">'+
                '<a ng-show="grid.appScope.hasPermit(\'excActProductMedia.downloadMedia\')" ng-click="grid.appScope.downloadMedia(row.entity.id)"">商品信息下载</a> ' +
                '<a ng-show="grid.appScope.hasPermit(\'excActProductMedia.deleteMedia\')" ng-click="grid.appScope.deleteMedia(row.entity.id)""> | 删除</a> ' +
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
        $http.post("excActProductMedia/selectAll","info="+angular.toJson($scope.info)+"&pageNo="+
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

    $scope.deleteMedia=function(id){
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
                    $http.post("excActProductMedia/deleteMedia","id="+id,
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
     *下载文件
     */
    $scope.downloadMedia=function (id) {
        location.href="excActProductMedia/downloadMedia?id="+id;
    };

    $scope.addMediaShow = function(){
        $scope.addInfo={channelNo:""};
        $('#addMedia').modal('show');
    };
    $scope.addMediaHide = function(){
        $('#addMedia').modal('hide');
    };

    $scope.submitting = false;
    /**
     * 上游查询文件
     */
    $scope.addProductMedia=function(){
        if ($scope.submitting) {
            return;
        }
        $scope.submitting = true;
        $http.post("excActProductMedia/addProductMedia","info="+angular.toJson($scope.addInfo),
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.notice(data.msg);
                    $scope.query();
                    $scope.addMediaHide();
                }else{
                    $scope.notice(data.msg);
                }
                $scope.submitting = false;
            })
            .error(function(data){
                $scope.notice(data.msg);
                $scope.submitting = false;
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