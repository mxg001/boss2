/**
 * 贷款奖金配置
 */
angular.module('inspinia',['infinity.angular-chosen']).controller('creditTotalBonusCtrl',function($scope,$http,i18nService,$document,SweetAlert){

    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    $scope.resetForm = function () {
        $scope.conf = {orgId:'-1'};
    };
    $scope.resetForm();

    $scope.columnDefs = [
        {field: 'id',displayName: 'ID',width: 120,pinnable: false,sortable: false},
        {field: 'orgName',displayName: '组织',width: 120,pinnable: false,sortable: false},
        {field: 'productName',displayName: '报告名称',width: 130,pinnable: false,sortable: false},
        {field: 'productPrice',displayName: '报告售价',cellFilter:"currency:''",cellTemplate:'' +
            '<div class="lh30" ng-show="row.entity.action!=2">{{row.entity.productPrice}}</div>' +
            '<div class="lh30" ng-show="row.entity.action==2">' +
            '<input style="height:28px;width:100px;text-align:center;" ng-model="row.entity.productPrice"/>' +
            '</div>'},
        {field: 'price',displayName: '大数据系统给银行家成本',width: 180,pinnable: false,sortable: false},
        {field: 'oemTotalBonus',displayName: '公司截留',cellFilter:"currency:''",cellTemplate:'' +
        '<div class="lh30" ng-show="row.entity.action!=2">{{row.entity.oemTotalBonus}}</div>' +
        '<div class="lh30" ng-show="row.entity.action==2">' +
        '<input style="height:28px;width:100px;text-align:center;" ng-model="row.entity.oemTotalBonus"/>' +
        '</div>'},
        {field: 'oemBonus',displayName: '品牌截留',cellFilter:"currency:''",cellTemplate:'' +
            '<div class="lh30" ng-show="row.entity.action!=2">{{row.entity.oemBonus}}</div>' +
            '<div class="lh30" ng-show="row.entity.action==2">' +
            '<input style="height:28px;width:100px;text-align:center;" ng-model="row.entity.oemBonus"/>' +
            '</div>'},
        {field: 'action',displayName: '操作',width: 120,pinnedRight:true,
        	cellTemplate:
        	'<div  class="lh30" ng-show="row.entity.action!=2&&grid.appScope.hasPermit(\'loanBonusConf.add\')">' +
            '<a ng-click="grid.appScope.creditCardConfEdit(row.entity)">编辑</a></div>'
        	+
        	'<div  class="lh30" ng-show="row.entity.action==2&&grid.appScope.hasPermit(\'loanBonusConf.add\')">' +
            '<a ng-click="grid.appScope.creditCardUpd(row.entity)">保存</a></div>'
        }
    ];
    
    $scope.loanBonusGrid = {
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		  	//开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
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

    $scope.orgInfoListAll = [];
    
    
    //获取组织
    $scope.getOrgList = function(){
   	 $http({
            url:"superBank/getOrgInfoList",
            method:"POST"
        }).success(function(msg){
            if(msg.status){
                $scope.orgInfoListAll = msg.data;
                $scope.orgInfoListAll.unshift({orgId:'0',orgName:"默认"});
                $scope.orgInfoList = angular.copy($scope.orgInfoListAll);
                $scope.orgInfoListAll.unshift({orgId:'-1',orgName:"全部"});
            }
        }).error(function(){
            $scope.notice("获取组织列表异常");
        });
   };
   $scope.getOrgList();
    //获取所有的产品名称
    $scope.getProductAll = function () {
        $http({
            url:"superBank/selectProductAll",
            method:"POST"
        }).success(function(msg){
            if(msg.status){
                $scope.productList = msg.data;
                $scope.productListAll = angular.copy($scope.productList);
                $scope.productListAll.unshift({"productName":"全部"});
            } else {
                $scope.notice("获取产品名称异常");
            }
        }).error(function(){
            $scope.notice("获取产品名称异常");
        })
    };
    $scope.getProductAll();

    
    //编辑配置
    $scope.creditCardConfEdit = function(entity){
    	 entity.action=2;
    };
    
    //更新
    $scope.creditCardUpd = function(entity){
        SweetAlert.swal({
                title: "确定保存吗？",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    if(entity.productPrice<=0){
                        $scope.notice("报告售价不能小于零");
                        return;
                    }
                    if(entity.oemBonus<=0){
                        $scope.notice("品牌截留不能小于零");
                        return;
                    }
                    $http.post("superBank/updateCreditTotal",angular.toJson(entity))
                        .success(function(data){
                            if(data.status){
                                $scope.notice(data.msg);
                                entity.action=1;
                            }else{
                                $scope.notice(data.msg);
                                $scope.submitting = false;
                            }
                        });
                }
            });
    };

    $scope.productChange=function (){
       var productListAll=  $scope.productListAll;
       var productIdStr= $scope.conf.productId;
        for(var productId in productListAll){
                var data = productListAll[productId];
                if(data.productId==productIdStr){
                    var price=data.price;
                    if(price==undefined) {
                        price="";
                    }
                    $scope.conf.price=price;
                }
        }
    }

    $scope.query = function () {
        $scope.submitting = true;
        $scope.loadImg = true;
        
        $http({
            url: 'superBank/getCreditTotalList?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
            data: $scope.conf,
            method:'POST'
        }).success(function (msg) {
            $scope.submitting = false;
            $scope.loadImg = false;
            if (!msg.status){
                $scope.notice(msg.msg);
                return;
            }
            $scope.loanBonusGrid.data = msg.data.result;
            $scope.loanBonusGrid.totalItems = msg.data.totalCount;
        }).error(function (msg) {
            $scope.submitting = false;
            $scope.loadImg = false;
            $scope.notice('服务器异常,请稍后再试.');
        });
    };
   
    $scope.addcreditTotal = function(){
        $scope.conf.orgId = "0";
    	$("#addCreditTotalBonus").modal("show");
    };
    
    $scope.cancel = function(){
    	$("#addCreditTotalBonus").modal("hide");
    };
    
    //新增
    $scope.saveCreditTotal = function(){
        $http.post("superBank/addCreditTotalBonus",angular.toJson($scope.conf))
            .success(function(data){
                $scope.notice(data.msg);
                if(data.status){
                    $scope.cancel();
                    $scope.resetForm();
                    $scope.query();
                }else{
                    $scope.submitting = false;
                }
            });
    };
    
    //页面绑定回车事件
    $document.bind("keypress", function(event) {
        $scope.$apply(function (){
            if(event.keyCode == 13){
                $scope.query();
            }
        });
    });
});