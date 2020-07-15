/**
 * 产品类别列表
 */
angular.module('inspinia').controller('productTypeQueryCtrl',function($scope,$http,i18nService,SweetAlert,$document){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    $scope.declaraTypeSelect = [{text:"全部",value:""},{text:"微信报单",value:"1"},{text:"自助报单",value:"2"}];
    $scope.declaraTypeStr=angular.toJson($scope.declaraTypeSelect);

    $scope.orgList=[];
    //获取机构
    $http.post("orgManagement/getOrgManagementList")
        .success(function(data){
            if(data.status){
                $scope.orgList.push({value:"",text:"全部"});
                var orgList=data.list;
                if(orgList!=null&&orgList.length>0){
                    for(var i=0; i<orgList.length; i++){
                        $scope.orgList.push({value:orgList[i].orgCode, text:orgList[i].orgName});
                    }
                }
            }
        });
    //清空
    $scope.clear=function(){
        $scope.info={orgCode:"",typeName:""};
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
            { field: 'id',displayName:'序号',width:150},
            { field: 'typeCode',displayName:'编码',width:150},
            { field: 'orgName',displayName:'机构名称',width:180 },
            { field: 'typeName',displayName:'产品类别名称',width:180 },
            { field: 'videoUrl',displayName:'视频地址',width:300 },
            { field: 'courseUrl',displayName:'兑换教程',width:300 },
            { field: 'bankUrl',displayName:'兑换入口',width:300 },
            { field: 'hint',displayName:'兑换入口跳转提示语',width:300 },
            { field: 'declaraType',displayName:'报单方式',width:120,cellFilter:"formatDropping:" +  $scope.declaraTypeStr},
            { field: 'id',displayName:'操作',width:180,pinnedRight:true, cellTemplate:
            '<div class="lh30">'+
            '<a target="_blank" ui-sref="exchange.productTypeDetail({id:row.entity.id})">详情</a> ' +
            '<a ng-show="grid.appScope.hasPermit(\'productType.editProductType\')" target="_blank" ui-sref="exchange.productTypeEdit({id:row.entity.id})" > | 修改</a> ' +
            '<a ng-show="grid.appScope.hasPermit(\'productType.deleteProductType\')" ng-click="grid.appScope.deleteType(row.entity.id)""> | 删除</a> ' +
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
        $http.post("productType/selectAll","info="+angular.toJson($scope.info)+"&pageNo="+
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

    //删除
    $scope.deleteType = function(id){
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
                    $http.post("productType/deleteProductType","id="+id,
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

    //批量删除
    $scope.deleteTypeBatch = function(){
        var selectList = $scope.gridApi.selection.getSelectedRows();
        if(selectList==null||selectList.length==0){
            $scope.notice("请选中要删除的数据!");
            return false;
        }
        var ids="";
        if(selectList!=null&&selectList.length>0){
            for(var i=0;i<selectList.length;i++){
                ids = ids + selectList[i].id + ",";
            }
        }
        ids=ids.substring(0,ids.length-1);
        SweetAlert.swal({
                title: "确认批量删除?",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post("productType/deleteProductTypeBatch","ids="+ids,
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
                    location.href = "productType/importDetail?info=" + encodeURI(angular.toJson($scope.info));
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