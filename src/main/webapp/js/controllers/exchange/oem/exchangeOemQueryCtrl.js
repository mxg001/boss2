/**
 * oem列表
 */
angular.module('inspinia').controller('exchangeOemQueryCtrl',function($scope,$http,i18nService,SweetAlert,$document){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    //清空
    $scope.clear=function(){
        $scope.info={oemName:""};
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
            { field: 'id',displayName:'序号',width:80},
            { field: 'oemNo',displayName:'oem编号',width:180 },
            { field: 'oemName',displayName:'组织名称',width:180 },
            { field: 'publicAccountName',displayName:'微信公众号',width:180 },
            { field: 'teamId',displayName:'V2组织编号',width:180 },
            { field: 'agentNo',displayName:'V2代理商编号',width:180 },
            { field: 'companyNo',displayName:'公司编号',width:180 },
            { field: 'companyName',displayName:'公司名称',width:180 },
            { field: 'remark',displayName:'备注',width:180 },
            { field: 'id',displayName:'操作',width:250,pinnedRight:true, cellTemplate:
            '<div class="lh30">'+
            '<a target="_blank" ui-sref="exchange.exchangeOemDetail({id:row.entity.id})">详情</a> ' +
            '<a ng-show="grid.appScope.hasPermit(\'exchangeOem.updateExchangeOem\')" target="_blank" ui-sref="exchange.exchangeOemEdit({id:row.entity.id})"> | 编辑</a> ' +
            '<a ng-show="grid.appScope.hasPermit(\'exchangeOem.selectProductOemList\')" target="_blank" ui-sref="exchange.productOem({oemNo:row.entity.oemNo})"> | 产品配置</a> ' +
            '<a ng-show="grid.appScope.hasPermit(\'exchangeOem.synchronizationProductOem\')" ng-click="grid.appScope.synchronizationProductOem(row.entity.id)""> | 同步</a> ' +

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
        $http.post("exchangeOem/selectAll","info="+angular.toJson($scope.info)+"&pageNo="+
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

    //同步
    $scope.synchronizationProductOem=function (id) {
        SweetAlert.swal({
                title:"确认同步?",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post("exchangeOem/synchronizationProductOem","id="+id,
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function(data){
                            if(data.status){
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
                    location.href = "exchangeOem/importDetail?info=" + encodeURI(angular.toJson($scope.info));
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