/**
 * 产品列表
 */
angular.module('inspinia').controller('exchangeActivateHelpCenterQueryCtrl',function($scope,$http,i18nService,SweetAlert,$document){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    $scope.statusSelect = [{text:"全部",value:""},{text:"关闭",value:"1"},{text:"开启",value:"2"}];
    $scope.statusStr=angular.toJson($scope.statusSelect);

    $scope.categorySelect = [{text:"全部",value:""},{text:"使用教程",value:"1"},{text:"常见问题",value:"2"}];
    $scope.categoryStr=angular.toJson($scope.categorySelect);

    $scope.modelSelect = [{text:"全部",value:""},{text:"兑换视频教程",value:"1"},{text:"图文教程",value:"2"},
        {text:"常见问题",value:"3"}];
    $scope.modelStr=angular.toJson($scope.modelSelect);

    //清空
    $scope.clear=function(){
        $scope.info={title:"",category:"",model:""};
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
            { field: 'id',displayName:'ID',width:100},
            { field: 'title',displayName:'标题',width:180 },
            { field: 'category',displayName:'类别',width:120,cellFilter:"formatDropping:" +  $scope.categoryStr},
            { field: 'model',displayName:'模块',width:120,cellFilter:"formatDropping:" +  $scope.modelStr},
            { field: 'status',displayName:'状态',width:120,cellFilter:"formatDropping:" +  $scope.statusStr},
            { field: 'sort',displayName:'排序',width:180 },
            { field: 'createTime',displayName:'创建时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'link',displayName:'H5链接',width:400},
            { field: 'id',displayName:'操作',width:180,pinnedRight:true, cellTemplate:
                '<div class="lh30">'+
                '<a target="_blank" ui-sref="exchangeActivate.helpCenterDetail({id:row.entity.id})">详情</a> ' +
                '<a ng-show="grid.appScope.hasPermit(\'exchangeActivateHelpCenter.updateHelpCenterState\')&&row.entity.status==1" ng-click="grid.appScope.updateHelpCenterState(row.entity)""> | 启用</a> ' +
                '<a ng-show="grid.appScope.hasPermit(\'exchangeActivateHelpCenter.updateHelpCenterState\')&&row.entity.status==2" ng-click="grid.appScope.updateHelpCenterState(row.entity)""> | 关闭</a> ' +
                '<a ng-show="grid.appScope.hasPermit(\'exchangeActivateHelpCenter.updateHelpCenter\')&&row.entity.status==1" target="_blank" ui-sref="exchangeActivate.helpCenterEdit({id:row.entity.id})"> | 修改</a> ' +
                '<a ng-show="grid.appScope.hasPermit(\'exchangeActivateHelpCenter.deleteHelpCenter\')&&row.entity.status==1" ng-click="grid.appScope.deleteHelpCenter(row.entity.id)""> | 删除</a> ' +
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
        $http.post("exchangeActivateHelpCenter/selectAll","info="+angular.toJson($scope.info)+"&pageNo="+
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

    $scope.deleteHelpCenter=function(id){
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
                    $http.post("exchangeActivateHelpCenter/deleteHelpCenter","id="+id,
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
    $scope.updateHelpCenterState=function(entry){
        var title="";
        var sta="1";
        if(entry.status=="1"){
            title="确认启用?";
            sta="2";
        }else if(entry.status=="2"){
            title="确认关闭?";
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
                    $http.post("exchangeActivateHelpCenter/updateHelpCenterState","id="+entry.id+"&state="+sta,
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