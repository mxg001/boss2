/**
 * oem产品列表
 */
angular.module('inspinia',['infinity.angular-chosen']).controller('exchangeActivateOemProductQueryCtrl',function($scope,$http,i18nService,$stateParams,SweetAlert,$document,$timeout){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    $scope.oemNoOne=$stateParams.oemNo;
    $scope.shelveSelect = [{text:"全部",value:""},{text:"上架",value:"1"},{text:"下架",value:"2"}];
    $scope.shelveStr=angular.toJson($scope.shelveSelect);
    //清空
    $scope.clear=function(){
        $scope.info={oemNo:$stateParams.oemNo,orgCode:"",typeCode:"",pId:"",shelve:""};
    };
    $scope.clear();

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
    $scope.orgList=[];
    //获取机构
    $http.post("exchangeActivateOrg/getOrgManagementList")
        .success(function(data){
            if(data.status){
                $scope.orgList.push({value:"",text:"全部"});
                var list=data.list;
                if(list!=null&&list.length>0){
                    for(var i=0; i<list.length; i++){
                        $scope.orgList.push({value:list[i].orgCode,text:list[i].orgName});
                    }
                }
            }
        });
    $scope.typeList=[];
    //获取产品类别
    $scope.getProductTypeList=function (orgCode) {
        $http.post("productTypeActivate/getProductTypeList","orgCode="+orgCode,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.typeList=[];
                    $scope.typeList.push({value:"",text:"全部"});
                    var list=data.list;
                    if(list!=null&&list.length>0){
                        for(var i=0; i<list.length; i++){
                            $scope.typeList.push({value:list[i].typeCode, text:list[i].typeName});
                        }
                    }
                }
            });
    }
    $scope.getProductTypeList("");

    //获取产品
    $scope.productList=[];
    $scope.getProductList=function (name) {
        $http.post("exchangeActivateProduct/getProductList","name="+name+"&typeCode=-1",
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.productList=[];
                    $scope.productList.push({value:"",text:"全部"});
                    var list=data.list;
                    if(list!=null&&list.length>0){
                        for(var i=0; i<list.length; i++){
                            $scope.productList.push({value:list[i].id,text:list[i].productName});
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

    $scope.userGrid={                           //配置表格
        data: 'result',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'id',displayName:'序号',width:150},
            { field: 'oemName',displayName:'组织名称',width:180 },
            { field: 'orgName',displayName:'机构名称',width:180 },
            { field: 'typeName',displayName:'产品类别',width:180 },
            { field: 'productName',displayName:'产品名称',width:180 },
            { field: 'shelve',displayName:'状态',width:120,cellFilter:"formatDropping:" +  $scope.shelveStr },
            { field: 'brandPrice',displayName:'品牌价格',width:180,cellFilter:"currency:''" },
            { field: 'id',displayName:'操作',width:180,pinnedRight:true, cellTemplate:
            '<div class="lh30">'+
            '<a target="_blank" ui-sref="exchangeActivate.productOemDetail({id:row.entity.id,oemNo:row.entity.oemNo})">详情</a> ' +
            '<a ng-show="row.entity.shelve==2&&grid.appScope.hasPermit(\'exchangeActivateOem.updateProductOem\')" target="_blank" ui-sref="exchangeActivate.productOemEdit({id:row.entity.id,oemNo:row.entity.oemNo})"> | 编辑</a> ' +
            '<a ng-show="row.entity.shelve==2&&grid.appScope.hasPermit(\'exchangeActivateOem.updateProductOemShelve\')" ng-click="grid.appScope.productShelves(row.entity.id,row.entity.shelve)"> | 上架</a> ' +
            '<a ng-show="row.entity.shelve==1&&grid.appScope.hasPermit(\'exchangeActivateOem.updateProductOemShelve\')" ng-click="grid.appScope.productShelves(row.entity.id,row.entity.shelve)"> | 下架</a> ' +
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
        $http.post("exchangeActivateOem/selectProductOemList","info="+angular.toJson($scope.info)+"&pageNo="+
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
    //上下架
    $scope.productShelves=function (id,state) {
        var str="";
        var shelves="2";
        if(state=="1"){
            str="确认下架?";
        }else{
            str="确认上架?";
            shelves="1";
        }
        SweetAlert.swal({
                title:str,
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post("exchangeActivateOem/updateProductOemShelve","id="+id+"&state="+shelves,
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
    //批量上/下架
    $scope.productShelvesBatch=function (state) {
        var str="";
        var shelves="2";
        var selectList = $scope.gridApi.selection.getSelectedRows();
        if(selectList==null||selectList.length==0){
            $scope.notice("请选择数据!");
            return;
        }
        var ids="";
        if(state==1){
            str="确认批量下架?";
            if(selectList!=null&&selectList.length>0){
                for(var i=0;i<selectList.length;i++){
                    if(selectList[i].shelve=="1"){
                        ids = ids + selectList[i].id +",";
                    }
                }
            }
            if(ids==""){
                $scope.notice("请选择下架的数据!");
                return;
            }
        }else{
            str="确认批量上架?";
            shelves="1";
            if(selectList!=null&&selectList.length>0){
                for(var i=0;i<selectList.length;i++){
                    if(selectList[i].shelve=="2"){
                        ids = ids + selectList[i].id +",";
                    }
                }
            }
            if(ids==""){
                $scope.notice("请选择上架的数据!");
                return;
            }
        }

        ids=ids.substring(0,ids.length-1);
        SweetAlert.swal({
                title:str,
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post("exchangeActivateOem/updateProductOemShelveBatch","ids="+ids+"&state="+shelves,
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