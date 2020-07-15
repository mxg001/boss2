/**
 * 核销渠道商品明细查询
 */
angular.module('inspinia',['infinity.angular-chosen','uiSwitch']).controller('routeGoodQueryCtrl',function($scope,$http,i18nService,SweetAlert,$document,$timeout){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    $scope.statusSelect = [{text:"全部",value:""},{text:"开启",value:"1"},{text:"关闭",value:"0"}];
    $scope.statusStr=angular.toJson($scope.statusSelect);

    $scope.goodModeSelect = [{text:"全部",value:""},{text:"文字报单",value:"1"},{text:"图片报单",value:"2"}];
    $scope.goodModeStr=angular.toJson($scope.goodModeSelect);

    //获取产品
    $scope.getProductList=function (val) {
        $http.post("exchangeActivateProduct/productListSelect","val="+val,
            {headers: {'Content-Type':'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.productList=[];
                    $scope.productList.push({value:"",text:"全部"});
                    var list=data.list;
                    if(list!=null&&list.length>0){
                        for(var i=0; i<list.length; i++){
                            $scope.productList.push({value:list[i].id,text:"["+list[i].id+"]"+list[i].productName});
                        }
                    }
                }
            });
    }
    $scope.getProductList("");

    //动态筛选产品
    $scope.getStates =getStates;
    var oldValue="";
    var timeout="";
    function getStates(value) {
        var newValue=value;
        if(newValue != oldValue){
            if (timeout) $timeout.cancel(timeout);
            timeout = $timeout(
                function(){
                    $scope.getProductList(value);
                },800);
        }
    };

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
            { field: 'id',displayName:'ID',width:120},
            { field: 'channelNo',displayName:'核销渠道编号',width:180 },
            { field: 'channelName',displayName:'核销渠道名称',width:180 },
            { field: 'goodTypeNo',displayName:'上游渠道ID',width:180 },
            { field: 'channelGoodNo',displayName:'渠道商品编号',width:180 },
            { field: 'channelGoodName',displayName:'渠道商品名称',width:180 },
            { field: 'goodContentId',displayName:'商品内容ID',width:180 },
            { field: 'pId',displayName:'产品ID',width:180 },
            { field: 'pName',displayName:'产品名称',width:180 },
            {field: 'status',displayName: '状态',width: 180,cellTemplate:
                '<span ng-show="grid.appScope.hasPermit(\'excActRouteGood.closeGood\')"><switch class="switch switch-s" ng-model="row.entity.status" ng-true-value="1" ng-false-value="0" ng-change="grid.appScope.closeGood(row.entity)" /></span>'
                +'<span ng-show="!grid.appScope.hasPermit(\'excActRouteGood.closeGood\')"> <span ng-show="row.entity.status==1">开启</span><span ng-show="row.entity.status==0">关闭</span></span>'

            },
            { field: 'goodMode',displayName:'核销方式',width:120,cellFilter:"formatDropping:" +  $scope.goodModeStr},
            { field: 'channelPrice',displayName:'核销价格',width:180,cellFilter:"currency:''" },
            { field: 'createTime',displayName:'创建时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'id',displayName:'操作',width:180,pinnedRight:true, cellTemplate:
                '<div class="lh30">'+
                '<a target="_blank" ui-sref="exchangeActivate.routeGoodDetail({id:row.entity.id})">详情</a> ' +
                '<a target="_blank" ng-show="grid.appScope.hasPermit(\'excActRouteGood.updateGood\')&&row.entity.status==0" ui-sref="exchangeActivate.routeGoodEdit({id:row.entity.id})"> | 修改</a> ' +
                '<a ng-show="grid.appScope.hasPermit(\'excActRouteGood.deleteGood\')&&row.entity.status==0" ng-click="grid.appScope.deleteGood(row.entity.id)"> | 删除</a> ' +
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
        $http.post("excActRouteGood/selectAll","info="+angular.toJson($scope.info)+"&pageNo="+
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

    $scope.deleteGood=function(id){
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
                    $http.post("excActRouteGood/deleteGood","id="+id,
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
    $scope.closeGood=function(entity){
        var title="";
        var sta=0;
        if(entity.status==false){
            title="确认关闭?";
        }else{
            title="确认开启?";
            sta=1;
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
                    $http.post("excActRouteGood/closeGood","id="+entity.id+"&state="+sta,
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function(data){
                            if(data.status){
                                $scope.notice(data.msg);
                                $scope.query();
                            }else{
                                $scope.notice(data.msg);
                                $scope.reduction(entity);
                            }
                        })
                        .error(function(data){
                            $scope.notice(data.msg);
                            $scope.reduction(entity);
                        });
                }else{
                    $scope.reduction(entity);
                }
            });
    };

    /**
     *还原控件
     */
    $scope.reduction=function (entity) {
        if(entity.status==false){
            entity.status=true;
        }else{
            entity.status=false;
        }
    };

    /**
     *批量开启、关闭
     */
    $scope.closeGoodBatch=function(sta){
        var selectList = $scope.gridApi.selection.getSelectedRows();
        if(selectList==null||selectList.length==0){
            $scope.notice("请选中要操作的数据!");
            return false;
        }
        var ids="";
        var num=0;
        if(selectList!=null&&selectList.length>0){
            for(var i=0;i<selectList.length;i++){
                if(sta==0){
                    if(selectList[i].status==1||selectList[i].status==true){
                        ids = ids + selectList[i].id + ",";
                        num++;
                    }
                }else if(sta==1){
                    if(selectList[i].status==0||selectList[i].status==false){
                        ids = ids + selectList[i].id + ",";
                        num++;
                    }
                }
            }
        }
        if(num==0){
            $scope.notice("选中数据没有符合的数据!");
            return false;
        }
        var title="";
        if(sta==0){
            title="选中符合的数据有"+num+"条,确认批量关闭?";
        }else if(sta==1){
            title="选中符合的数据有"+num+"条,确认批量开启?";
        }
        ids=ids.substring(0,ids.length-1);
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
                    $http.post("excActRouteGood/closeGoodBatch","ids="+ids+"&state="+sta,
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
                    location.href = "excActRouteGood/importDetail?info=" + encodeURI(angular.toJson($scope.info));
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