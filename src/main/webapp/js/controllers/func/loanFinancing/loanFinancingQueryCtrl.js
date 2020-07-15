/**
 * 贷款理财配置
 */
angular.module('inspinia').controller('loanFinancingQueryCtrl',function($scope,$http,i18nService,SweetAlert,$document){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    //上下架状态
    $scope.statusSelect = [{text:"全部",value:null},{text:"下架",value:0},{text:"上架",value:1}];
    $scope.statusStr=angular.toJson($scope.statusSelect);

    //清空
    $scope.clear=function(){
        $scope.info={status:null};
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
            { field: 'id',displayName:'产品编号',width:100},
            { field: 'logImg',displayName:'产品logo',width:180,cellTemplate:'' +
                    '<div ng-show="row.entity.logImg!=null"> ' +
                        '<a href="{{row.entity.logImg}}" fancybox rel="group">' +
                            '<img style="width:70px;height:35px;" ng-src="{{row.entity.logImg}}"/>' +
                        '</a>' +
                    '</div>'
            },
            { field: 'productName',displayName:'产品名称',width:180 },
            { field: 'sortNo',displayName:'排序',width:180 },
            { field: 'status',displayName:'上下架状态',width:120,cellFilter:"formatDropping:" +  $scope.statusStr },
            { field: 'clickTimes',displayName:'点击次数',width:180 },
            { field: 'createTime',displayName:'创建时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'lastUpdateTime',displayName:'最后操作时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'operator',displayName:'操作人',width:180 },
            { field: 'id',displayName:'操作',width:180,pinnedRight:true, cellTemplate:
            '<div class="lh30">'+
                '<a target="_blank" ui-sref="func.loanFinancingDetail({id:row.entity.id})">详情</a> ' +
                '<a ng-show="row.entity.status==0&&grid.appScope.hasPermit(\'loanFinancing.updateLoanFinancingStatus\')" ng-click="grid.appScope.updateStatus(row.entity.id,row.entity.status)""> | 上架</a> ' +
                '<a ng-show="row.entity.status==1&&grid.appScope.hasPermit(\'loanFinancing.updateLoanFinancingStatus\')" ng-click="grid.appScope.updateStatus(row.entity.id,row.entity.status)"> | 下架</a> ' +
                '<a ng-show="row.entity.status==0&&grid.appScope.hasPermit(\'loanFinancing.updateLoanFinancing\')" target="_blank" ui-sref="func.loanFinancingEdit({id:row.entity.id})"> | 修改</a> ' +
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
    $scope.loadImg = false;
    $scope.query=function(){
        if ($scope.loadImg) {
            return;
        }
        $scope.loadImg = true;
        $http.post("loanFinancing/selectAll","info="+angular.toJson($scope.info)+"&pageNo="+
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

    //开启
    $scope.updateStatus = function(id,orgStatus){
        var str="";
        var status=0;
        if(orgStatus==0){
            str="确认上架?";
            status=1;
        }else{
            str="确认下架?";
        }
        SweetAlert.swal({
                title: str,
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post("loanFinancing/updateLoanFinancingStatus","id="+id+"&status="+status,
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
                    $scope.exportInfoClick("loanFinancing/importDetail",{"info":angular.toJson($scope.info)});
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