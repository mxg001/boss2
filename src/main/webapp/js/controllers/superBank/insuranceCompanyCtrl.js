/**
 * 保险公司管理
 */
angular.module('inspinia',['uiSwitch']).controller('insuranceCompanyCtrl',function($scope,$http,i18nService,$document,SweetAlert){
    //数据源
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    $scope.statusAll = [{text:"全部",value:""},{text:"是",value:1}
        ,{text:"否",value:0}];

    $scope.resetForm = function () {
        $scope.baseInfo = {status:"",createOrderType:"1"};
    }
    $scope.resetForm();

    $scope.columnDefs = [
        {field: 'companyNo',displayName: '保险公司编号',width: 100,pinnable: false,sortable: false},
        {field: 'companyNickName',displayName: '保险公司别称',width: 150,pinnable: false,sortable: false},
        {field: 'companyName',displayName: '保险公司名称',width: 150,pinnable: false,sortable: false},
        {field: 'showLogo',displayName: '显示的logo',width: 150,pinnable: false,sortable: false,
            cellTemplate:'<img style="width: 140px; height: 36px;" ng-show="row.entity.showLogoUrl" ng-src="{{row.entity.showLogoUrl}}" />'},
        {field: 'source',displayName: '来源',width: 120,pinnable: false,sortable: false},
        {field: 'createOrderTypeStr',displayName: '创建订单方式',width: 150,pinnable: false,sortable: false},
        {field: 'status',displayName: '当前状态',width:150,pinnable: false,sortable: false,cellTemplate:
            '<span ng-show="grid.appScope.hasPermit(\'superBank.updateInsuranceCompany\')">' +
             '<switch class="switch switch-s" ng-model="row.entity.status" ng-change="grid.appScope.switchStatus(row)" />' +
            '</span>'
            +'<span ng-show="!grid.appScope.hasPermit(\'superBank.updateInsuranceCompany\')">' +
                ' <span ng-show="row.entity.status==1">是</span>' +
             '<span ng-show="row.entity.status==0">否</span>' +
            '</span>'
        },
        {field: 'action',displayName: '操作',width: 150,pinnedRight:true,sortable: false,editable:true,cellTemplate:
            '<a class="lh30" target="_blank" '
            + 'ui-sref="superBank.insuranceCompanyDetail({id:row.entity.companyNo})">详情</a>'
            +'<a class="lh30" ng-show="grid.appScope.hasPermit(\'superBank.updateInsuranceCompany\')" '
            + 'ui-sref="superBank.updateInsuranceCompany({id:row.entity.companyNo})"> | 修改</a>'
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

    $scope.query = function () {
        $scope.submitting = true;
        $scope.loadImg = true;
        $http({
            url: 'insuranceCompany/selectList?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
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
                    var data={"status":row.entity.status,"companyNo":row.entity.companyNo};
                    $http.post("insuranceCompany/updateCompanyStatus",angular.toJson(data))
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
                    row.entity.status = !row.entity.status;
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