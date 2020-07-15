/**
 * 路由列表
 */
angular.module('inspinia',['infinity.angular-chosen','uiSwitch','angularFileUpload']).controller('excRouteQueryCtrl',function($scope,$http,i18nService,SweetAlert,$document,$timeout,FileUploader){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    $scope.statusSelect = [{text:"全部",value:""},{text:"开启",value:"1"},{text:"关闭",value:"0"}];
    $scope.statusStr=angular.toJson($scope.statusSelect);

    //获取产品
    $scope.getProductList=function (val) {
        $http.post("exchangeProduct/productListSelect","val="+val,
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
            { field: 'priority',displayName:'优先级',width:180 },
            {field: 'status',displayName: '状态',width: 180,
                cellTemplate:
                '<span ng-show="grid.appScope.hasPermit(\'excRoute.closeRoute\')"><switch class="switch switch-s" ng-model="row.entity.status" ng-true-value="1" ng-false-value="0" ng-change="grid.appScope.closeRoute(row.entity)" /></span>'
                +'<span ng-show="!grid.appScope.hasPermit(\'excRoute.closeRoute\')"> <span ng-show="row.entity.status==1">开启</span><span ng-show="row.entity.status==0">关闭</span></span>'

            },
            { field: 'remark',displayName:'核销渠道描述',width:180 },
            { field: 'createTime',displayName:'创建时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'id',displayName:'操作',width:200,pinnedRight:true, cellTemplate:
                '<div class="lh30">'+
                '<a target="_blank" ui-sref="exchange.routeDetail({id:row.entity.id})">详情</a> ' +
                '<a ng-show="grid.appScope.hasPermit(\'excRoute.updateRoute\')&&row.entity.status==0" target="_blank" ui-sref="exchange.routeEdit({id:row.entity.id})"> | 修改</a> ' +
                '<a ng-show="grid.appScope.hasPermit(\'excRoute.addGood\')" target="_blank" ui-sref="exchange.routeGoodAdd({channelNo:row.entity.channelNo})"> | 增加商品</a> ' +
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
        $http.post("excRoute/selectAll","info="+angular.toJson($scope.info)+"&pageNo="+
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

    $scope.closeRoute=function(entity){
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
                    $http.post("excRoute/closeRoute","id="+entity.id+"&state="+sta,
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


    $scope.importDiscountShow = function(){
        $('#importDiscount').modal('show');
    };
    $scope.importDiscountHide = function(){
        $('#importDiscount').modal('hide');
    };
    //上传图片,定义控制器路径
    var uploader = $scope.uploader = new FileUploader({
        url: 'excRoute/importDiscount',
        queueLimit: 1,   //文件个数
        removeAfterUpload: true,  //上传后删除文件
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    uploader.filters.push({
        name: 'isFile',
        fn: function(item, options) {
            return this.queue.length < 1;
        }
    });
    $scope.clearItems = function(){  //重新选择文件时，清空队列，达到覆盖文件的效果
        uploader.clearQueue();
    };
    $scope.submitting = false;
    //导入
    $scope.importDiscount=function(){
        $scope.submitting = true;
        uploader.uploadAll();//上传
        uploader.onSuccessItem = function(fileItem, response, status, headers) {//上传成功后的回调函数，在里面执行提交
            if(response.status){
                $scope.notice(response.msg);
                $scope.importDiscountHide();
            }else{
                $scope.notice(response.msg);
            }
            $scope.submitting = false;
        };
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