/**
 * oem列表
 */
angular.module('inspinia').controller('exchangeActivateOemQueryCtrl',function($scope,$http,i18nService,SweetAlert,$document){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    $scope.openOemStateSelect = [{text:"全部",value:""},{text:"开通",value:"1"},{text:"未开通",value:"2"}];


    $scope.agentAccountSelect = [{text:"否",value:"0"},{text:"是",value:"1"}];
    $scope.agentAccountStr=angular.toJson($scope.agentAccountSelect);

    //清空
    $scope.clear=function(){
        $scope.info={oemName:"",openOemState:""};
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
            { field: 'id',displayName:'序号',width:80},
            { field: 'oemNo',displayName:'oem编号',width:180 },
            { field: 'oemName',displayName:'组织名称',width:180 },
            { field: 'publicAccountName',displayName:'微信公众号',width:180 },
            { field: 'teamId',displayName:'V2组织编号',width:180 },
            { field: 'agentNo',displayName:'V2代理商编号',width:180 },
            { field: 'repaymentOemNo',displayName:'对应超级还组织编号',width:180 },
            { field: 'receiveOemNo',displayName:'对应V2商户收款组织编号',width:180 },
            { field: 'agentAccount',displayName:'开户状态',width:120,cellFilter:"formatDropping:" +  $scope.agentAccountStr},
            { field: 'companyNo',displayName:'公司编号',width:180 },
            { field: 'companyName',displayName:'公司名称',width:180 },
            { field: 'remark',displayName:'备注',width:180 },
            { field: 'id',displayName:'操作',width:250,pinnedRight:true, cellTemplate:
            '<div class="lh30">'+
            '<a target="_blank" ui-sref="exchangeActivate.exchangeOemDetail({id:row.entity.id})">详情</a> ' +
            '<a ng-show="grid.appScope.hasPermit(\'exchangeActivateOem.updateExchangeOem\')" target="_blank" ui-sref="exchangeActivate.exchangeOemEdit({id:row.entity.id})"> | 编辑</a> ' +
            '<a ng-show="grid.appScope.hasPermit(\'exchangeActivateOem.selectProductOemList\')" target="_blank" ui-sref="exchangeActivate.productOem({oemNo:row.entity.oemNo})"> | 产品配置</a> ' +
            '<a ng-show="grid.appScope.hasPermit(\'exchangeActivateOem.synchronizationProductOem\')" ng-click="grid.appScope.synchronizationProductOem(row.entity.id)""> | 同步</a> ' +
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
        $http.post("exchangeActivateOem/selectAll","info="+angular.toJson($scope.info)+"&pageNo="+
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

    //同步
    $scope.synchronizationProductOem=function (id) {
        SweetAlert.swal({
                title:"确认同步?",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post("exchangeActivateOem/synchronizationProductOem","id="+id,
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function(data){
                            if(data.status){
                                $scope.notice(data.msg);
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

    //开通超级还/商户收款对应组织
    $scope.openUpOem=function () {
        var selectList = $scope.gridApi.selection.getSelectedRows();
        if(selectList==null||selectList.length==0){
            $scope.notice("请选中要删除的数据!");
            return false;
        }
        if(selectList.length>1){
            $scope.notice("只能选中一条数据!");
            return false;
        }
        var id=selectList[0].id;
        SweetAlert.swal({
                title:"确认开通超级还/商户收款对应组织?",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post("exchangeActivateOem/openUpOem","id="+id,
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function(data){
                            if(data.status){
                                $scope.notice(data.msg);
                            }else{
                                $scope.notice(data.msg);
                            }
                            $scope.query();
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
                    location.href = "exchangeActivateOem/importDetail?info=" + encodeURI(angular.toJson($scope.info));
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