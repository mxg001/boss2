/**
 * 信用卡银行管理
 */
angular.module('inspinia',['uiSwitch']).controller('insuranceProductCtrl',function($scope,$http,i18nService,$document,SweetAlert){
    //数据源
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    $scope.productTypeList = [{text:"全部",value:""},{text:"意外险",value:"1"},{text:"健康医疗险",value:"2"} ,{text:"家财险",value:"3"} ,{text:"医疗意外险",value:"4"}];

    $scope.resetForm = function () {
        $scope.baseInfo = {productType:"",companyNo:-1};
    }
    $scope.resetForm();

    $scope.columnDefs = [
        {field: 'productId',displayName: '序号',width: 120,pinnable: false,sortable: false},
        {field: 'recommendStatusStr',displayName: '是否推荐',width: 150,pinnable: false,sortable: false},
        {field: 'companyNickName',displayName: '保险公司别称',width: 150,pinnable: false,sortable: false},
        {field: 'companyName',displayName: '保险公司名称',width: 150,pinnable: false,sortable: false},
        {field: 'productTypeStr',displayName: '保险种类',width: 200,pinnable: false,sortable: false},
        {field: 'status',displayName: '上下架',width:150,pinnable: false,sortable: false,cellTemplate:
            '<span ng-show="grid.appScope.hasPermit(\'superBank.updateInsuranceProduct\')">' +
            '<switch class="switch switch-s" ng-model="row.entity.status" ng-change="grid.appScope.switchStatus(row)" />' +
            '</span>'
            +'<span ng-show="!grid.appScope.hasPermit(\'superBank.updateInsuranceProduct\')">' +
            ' <span ng-show="row.entity.status==1">是</span>' +
            '<span ng-show="row.entity.status==0">否</span>' +
            '</span>'
        },
        {field: 'productName',displayName: '产品名称',width: 180,pinnable: false,sortable: false},
        {field: 'upperProductId',displayName: '上游产品id',width: 180,pinnable: false,sortable: false},
        {field: 'productPrice',displayName: '产品价格',width: 180,pinnable: false,sortable: false,cellFilter:'currency:""'},
        {field: 'productImageUrl',displayName: '产品图片',width: 120,pinnable: false,sortable: false,
            cellTemplate:'<img style="width: 140px; height: 36px;" ng-show="row.entity.productImageUrl" ng-src="{{row.entity.productImageUrl}}" />'},
        {field: 'showOrder',displayName: '显示顺序',width: 180,pinnable: false,sortable: false},
        {field: 'bonusTypeStr',displayName: '奖金方式',width: 180,pinnable: false,sortable: false},
        {field: 'bonusSettleTimeStr',displayName: '奖金结算时间',width: 180,pinnable: false,sortable: false},
        {field: 'action',displayName: '操作',width: 150,pinnedRight:true,sortable: false,editable:true,cellTemplate:
            '<a class="lh30" target="_blank" '
            + 'ui-sref="superBank.insuranceProductDetail({id:row.entity.productId})">详情</a>'
            +'<a class="lh30" ng-show="grid.appScope.hasPermit(\'superBank.updateInsuranceProduct\')" '
            + 'ui-sref="superBank.updateInsuranceProduct({id:row.entity.productId})"> | 修改</a>'
            +'<a class="lh30" ng-show="grid.appScope.hasPermit(\'superBank.updateInsuranceProduct\')" '
            + 'ng-click="grid.appScope.deleteProduct(row.entity.productId,row.entity.status)"> | 删除</a>'
        }
    ];

    $scope.infoGrid = {
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
//		rowHeight:35,
        columnDefs: $scope.columnDefs,
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            $scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.paginationOptions.pageNo = newPage;
                $scope.paginationOptions.pageSize = pageSize;
                $scope.query();
            });
        }
    };

    //获取保险公司
    $scope.getInsuranceCompany = function(){
        $http({
            url:"insuranceCompany/getCompanyList",
            method:"POST"
        }).success(function(msg){
            if(msg.status){
                $scope.companyListAll = msg.data;
                $scope.companyListAll.unshift({companyNo:-1,companyNickName:"全部"});
            }
        }).error(function(){
            $scope.notice("获取组织列表异常");
        });
    };
    $scope.getInsuranceCompany();

    $scope.query = function () {
        $scope.submitting = true;
        $scope.loadImg = true;
        $http({
            url: 'insuranceProduct/selectList?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
            data: $scope.baseInfo,
            method:'POST'
        }).success(function (msg) {
            $scope.submitting = false;
            $scope.loadImg = false;
            if (!msg.status){
                $scope.notice(msg.msg);
                return;
            }
            $scope.infoGrid.data = msg.data.result;
            $scope.infoGrid.totalItems = msg.data.totalCount;
        }).error(function (msg) {
            $scope.submitting = false;
            $scope.loadImg = false;
            $scope.notice('服务器异常,请稍后再试.');
        });
    };

    //修改上架开关
    $scope.switchStatus=function(row){
        if(row.entity.status){
            $scope.serviceText = "确定开启";
        } else {
            $scope.serviceText = "确定关闭";
        }
        SweetAlert.swal({
                title: "",
                text: $scope.serviceText,
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    if(row.entity.status==true){
                        row.entity.status=1;
                    } else if(row.entity.status==false){
                        row.entity.status=0;
                    }
                    var data={"status":row.entity.status,"productId":row.entity.productId};
                    $http.post("insuranceProduct/updateProductStatus",angular.toJson(data))
                        .success(function(data){
                            $scope.notice(data.msg);
                            if(data.status){
                                $scope.query();
                            }else{
                                row.entity.status = !row.entity.status;
                            }
                        })
                        .error(function(data){
                            row.entity.status = !row.entity.status;
                            $scope.notice("服务器异常");
                        });
                } else {
                    row.entity.statusInt = !row.entity.status;
                }
            });
    };

    //修改上架开关
    $scope.deleteProduct=function(productId,status){
        SweetAlert.swal({
                title: "",
                text: "确定删除吗",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                var data={"status":status,"productId":productId};
                $http({
                    url:"insuranceProduct/deleteProduct",
                    method:"POST",
                    data: angular.toJson(data)
                }).success(function(msg){
                    if(msg.status){
                        $scope.notice("删除成功");
                        $scope.query();
                    }
                }).error(function(){
                    $scope.notice("服务器异常");
                });
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