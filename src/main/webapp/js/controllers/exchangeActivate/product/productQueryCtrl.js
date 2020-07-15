/**
 * 产品列表
 */
angular.module('inspinia').controller('exchangeActivateProductQueryCtrl',function($scope,$http,i18nService,SweetAlert,$document){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    //清空
    $scope.clear=function(){
        $scope.info={productName:""};
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
            { field: 'orgName',displayName:'机构名称',width:180 },
            { field: 'typeName',displayName:'产品类别',width:180 },
            { field: 'productName',displayName:'产品名称',width:180 },
            { field: 'originalPrice',displayName:'券面价格',width:180 },
            // { field: 'productShorthand',displayName:'产品简称',width:180},
            { field: 'excPoint',displayName:'兑换积分',width:180 },
            { field: 'excNum',displayName:'兑换次数',width:180 },
            { field: 'settleDay',displayName:'结算周期(天)',width:180 },
            { field: 'minDay',displayName:'最短有效时间(天)',width:180 },
            { field: 'excPrice',displayName:'兑换价格',width:180,cellFilter:"currency:''" },
            { field: 'id',displayName:'操作',width:180,pinnedRight:true, cellTemplate:
            '<div class="lh30">'+
            '<a target="_blank" ui-sref="exchangeActivate.productDetail({id:row.entity.id})">详情</a> ' +
            '<a ng-show="grid.appScope.hasPermit(\'exchangeActivateProduct.updateExchangeProduct\')" target="_blank" ui-sref="exchangeActivate.productEdit({id:row.entity.id})"> | 编辑</a> ' +
            '<a ng-show="grid.appScope.hasPermit(\'exchangeActivateProduct.deleteExchangeProduct\')" ng-click="grid.appScope.deleteProduct(row.entity.id)""> | 删除</a> ' +
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
        $http.post("exchangeActivateProduct/selectAll","info="+angular.toJson($scope.info)+"&pageNo="+
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
    $scope.deleteProduct=function (id) {
        SweetAlert.swal({
                title:"确认删除?",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post("exchangeActivateProduct/deleteExchangeProduct","id="+id,
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
                            $scope.submitting = false;
                            $scope.notice(data.msg);
                        });
                }
            });
    };

    //获取机构
    $http.post("exchangeActivateOrg/getOrgManagementList")
        .success(function(data){
            if(data.status){
                $scope.orgList=[];
                $scope.orgList.push({value:"",text:"全部"});
                var orgList=data.list;
                if(orgList!=null&&orgList.length>0){
                    for(var i=0; i<orgList.length; i++){
                        $scope.orgList.push({value:orgList[i].orgCode, text:orgList[i].orgName});
                    }
                }
            }
        });
    //获取产品类别
    $scope.getProductTypeList=function (orgCode) {
        $http.post("productTypeActivate/getProductTypeList","orgCode="+orgCode,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.typeList=[];
                    $scope.typeList.push({value:"",text:"全部"});
                    var typeList=data.list;
                    if(typeList!=null&&typeList.length>0){
                        for(var i=0; i<typeList.length; i++){
                            $scope.typeList.push({value:typeList[i].typeCode, text:typeList[i].typeName});
                        }
                    }
                }
            });
    }
    $scope.getProductTypeList("");
    //机构变更事件
    $scope.changeOrgCode=function () {
        if($scope.addinfo.orgCode!=null&&$scope.addinfo.orgCode!=""){
            $scope.getProductTypeList($scope.addinfo.orgCode);
            $scope.addinfo.typeCode="";
        }
    };

    $scope.addBankModal = function(){
        $scope.addinfo={orgCode:"",typeCode:"",excNum:-1,underlineWriteoff:"1"};
        $('#addBankModal').modal('show');
    };
    $scope.cancel = function(){
        $('#addBankModal').modal('hide');
    };

    $scope.submitting = false;
    //开启
    $scope.addBank = function(){
        if($scope.submitting){
            return;
        }
        $scope.submitting = true;
        $http.post("exchangeActivateProduct/addExchangeProduct","info="+angular.toJson($scope.addinfo)+"&list="+angular.toJson($scope.listOrgDefault),
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
                    location.href = "exchangeActivateProduct/importDetail?info=" + encodeURI(angular.toJson($scope.info));
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