/**
 * 资金险设置查询
 */
angular.module('inspinia').controller('safeConfigQueryCtrl',function($scope,$http,i18nService,$document){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    //清空
    $scope.clear=function(){
        $scope.info={proCode:"",proName:""};
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
            { field: 'proCode',displayName:'产品编码',width:180 },
            { field: 'bxUnitName',displayName:'承保单位',width:180},
            { field: 'routeScale',displayName:'路由比例(%)',width:180},
            { field: 'proName',displayName:'产品名称',width:180},
            { field: 'createTime',displayName:'创建时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'lastUpdateTime',displayName:'最后修改时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'id',displayName:'操作',width:180,pinnedRight:true, cellTemplate:
                '<div class="lh30">'+
                '<a target="_blank" ui-sref="safe.safeConfigDetail({id:row.entity.id})">详情</a> ' +
                '<a ng-show="grid.appScope.hasPermit(\'safeConfig.saveSafeConfig\')" target="_blank" ui-sref="safe.safeConfigEdit({id:row.entity.id})"> | 修改</a> ' +
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
        $http.post("safeConfig/selectAll","info="+angular.toJson($scope.info)+"&pageNo="+
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

    $scope.submitting = false;
    $scope.saveRouteScale = function(){
        if($scope.submitting){
            return;
        }
        $scope.submitting = true;
        $http.post("safeConfig/saveRouteScale",
            "info="+angular.toJson($scope.result),
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                $scope.submitting = false;
                if(data.status){
                    $scope.notice(data.msg);
                    $scope.cancel();
                    $scope.query();
                }else{
                    $scope.notice(data.msg);
                }
            })
            .error(function(data){
                $scope.submitting = false;
                $scope.notice(data.msg);
            });
    };

    $scope.query();

    $scope.channelEditModel = function(){
        $("#channelEditModel").modal("show");
    }
    //返回
    $scope.cancel=function(){
        $('#channelEditModel').modal('hide');
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