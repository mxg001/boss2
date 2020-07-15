/**
 * 下载管理
 */
angular.module('inspinia').controller('exportManageQueryCtrl',function($scope,$http,i18nService,SweetAlert,$document,$stateParams){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    $scope.statusSelect = [{text:"全部",value:-1},{text:"初始化",value:0},{text:"成功",value:1},{text:"失败",value:2}];
    $scope.statusStr=angular.toJson($scope.statusSelect);
    //清空
    $scope.clear=function(){
        $scope.info={id:"",status:-1};
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
            { field: 'id',displayName:'ID',width:70},
            { field: 'status',displayName:'状态',width:100,cellFilter:"formatDropping:" +  $scope.statusStr },
            { field: 'fileName',displayName:'下载文件名',width:300 },
            { field: 'createTime',displayName:'创建时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'filterRemark',displayName:'查询条件',width:400 },
            { field: 'msg',displayName:'反馈结果',width:300 },
            { field: 'id',displayName:'操作',width:180,pinnedRight:true, cellTemplate:
                '<div class="lh30">'+
                    '<a ng-show="row.entity.status==1" ng-click="grid.appScope.downloadExport(row.entity.id)"">下载|</a> ' +
                    '<a ng-click="grid.appScope.deleteExport(row.entity.id)"">  删除 </a> ' +
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
        $http.post("exportManage/selectAll","info="+angular.toJson($scope.info)+"&pageNo="+
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
    $scope.queryStart=function(){
        if($scope.exportManageID>-1){
            $scope.info.id=angular.copy($scope.exportManageID)
            $scope.query();
            $scope.info.id="";
            $scope.exportManageID=-1;
        }
    };
    $scope.queryStart();
    //下载
    $scope.downloadExport=function(id){
        SweetAlert.swal({
                title: "确认下载?",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $scope.exportInfoClick("exportManage/downloadExcel",{"id":id});
                }
            });
    };
    $scope.deleteExport=function(id){
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
                    $http.post("exportManage/deleteExportManage","id="+id,
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