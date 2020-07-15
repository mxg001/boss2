/**
 * 查询卡bin列表
 */
angular.module('inspinia',['uiSwitch']).controller('cardBinsQueryCtrl',function($scope,$http,$state,$stateParams,$compile,$uibModal,SweetAlert,$log,i18nService,$document,$timeout){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.stateSelect=[{text:"打开",value:1},{text:"关闭",value:0}];
    $scope.stateStr=angular.toJson($scope.stateSelect);

    $scope.cardTypeSelect=[{text:"贷记卡",value:1},{text:"借记卡",value:2},{text:"借贷合一卡",value:3},{text:"其他",value:0}];
    $scope.cardTypeStr=angular.toJson($scope.cardTypeSelect);
    $scope.businessTypes=[{text:"借贷合一卡bin白名单",value:2},{text:"境外卡",value:1}];
    $scope.businessTypeStr=angular.toJson($scope.businessTypes);

    $scope.currencySelect=$scope.cardBinsCurrencys;
    $scope.currencyStr=angular.toJson($scope.currencySelect);

    //清空
    $scope.clear=function(){
        $scope.info={cardNo:"",state:"",cardType:"",currency:"",cardBank:"",businessType:"",
            createTimeBegin:moment(new Date().getTime()-6*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
            createTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'};
    }
    $scope.clear();

    $scope.gridOptions={                           //配置表格
        data: 'result',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'id',displayName:'编号',width:180 },
            { field: 'businessType',displayName:'业务类型',cellFilter:"formatDropping:"+$scope.businessTypeStr,width:150},
            { field: 'cardNo',displayName:'卡bin/全卡号',width:180 },
            { field: 'cardDigit',displayName:'全卡号位数',width:180 },
            { field: 'state',displayName:'状态',width:180,cellTemplate:'' +
            '<div>' +
            '  <div ng-show="grid.appScope.hasPermit(\'cardBins.openStateCardBins\')" >'+
            '    <span>' +
            '      <switch class="switch switch-s" ng-true-value="1" ng-false-value="0" ng-model="row.entity.state" ng-change="grid.appScope.openState(row.entity)" />' +
            '    </span>' +
            '  </div> ' +
            '  <div ng-show="!grid.appScope.hasPermit(\'cardBins.openStateCardBins\')" >' +
            '     <span ng-show="row.entity.state==1">打开</span> ' +
            '     <span ng-show="row.entity.state==0">关闭</span>  ' +
            '  </div> ' +
            '</div>'
            },
            { field: 'cardType',displayName:'卡种',cellFilter:"formatDropping:"+$scope.cardTypeStr,width:150},
            { field: 'cardBank',displayName:'发卡行',width:180},
            { field: 'cardStyle',displayName:'卡类型',width:180},
            { field: 'currency',displayName:'交易币种',cellFilter:"formatDropping:"+$scope.currencyStr,width:150},
            { field: 'remarks',displayName:'备注',width:180},
            { field: 'createTime',displayName:'创建时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'createName',displayName:'创建人',width:180},
            { field: 'id',displayName:'操作',pinnedRight:true,width:120,
                cellTemplate:'<div class="lh30">' +
                '<a ng-show="grid.appScope.hasPermit(\'cardBins.updateCardBins\')" ui-sref="risk.editCardBins({id:row.entity.id})" target="_black" >修改</a>' +
                '<a ng-show="grid.appScope.hasPermit(\'cardBins.deleteCardBins\')&&row.entity.state==0" ng-click="grid.appScope.deleteCardBins(row.entity.id)" target="_black" > | 删除</a>' +
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
        $http.post("cardBins/selectByParam","info=" + angular.toJson($scope.info)+"&pageNo="+
            $scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.result=data.page.result;
                    $scope.gridOptions.totalItems = data.page.totalCount;
                }else{
                    $scope.notice(data.msg);
                }
                $scope.loadImg = false;
            });
    }
    //手动查询
    $scope.query();



    $scope.import=function(){
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
                    $scope.exportInfoClick("cardBins/exportInfo",{"info":angular.toJson($scope.info)});
                }
            });
    };

    //删除公告
    $scope.deleteCardBins = function(id){
        SweetAlert.swal({
                title: "确认删除卡bin?",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post("cardBins/deleteCardBins","id=" + id,
                    {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function(data){
                            if(data.status){
                                $scope.notice(data.msg);
                                $scope.query();
                            }else{
                                $scope.notice(data.msg);
                                $scope.query();
                            }
                        });
                }
            });
    }

    //批量删除
    $scope.deleteBatchCardBins = function(){
        var selectList = $scope.gridApi.selection.getSelectedRows();
        if(selectList==null||selectList.length<1){
            $scope.notice("请选中需要删除的数据!");
            return ;
        }
        var ids = "";
        for(var i=0;i<selectList.length;i++){
            ids = ids + selectList[i].id + ",";
            if(selectList[i].state==1){
                $scope.notice("打开状态不能删除!");
                return ;
            }
        }
        ids=ids.substring(0,ids.length-1);

        SweetAlert.swal({
                title: "确认批量删除卡bin?",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post("cardBins/deleteBatchCardBins","ids=" + ids,
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function(data){
                            if(data.status){
                                $scope.notice(data.msg);
                                $scope.query();
                            }else{
                                $scope.notice(data.msg);
                                $scope.query();
                            }
                        });
                }
            });
    }

    $scope.openState=function (entity) {
        var str="";
        var sta=-1;
        if(entity.state){
            str="确认打开？";
            sta=1;
        }else{
            str="确认关闭？";
            sta=0;
        }
        SweetAlert.swal({
                title: str,
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post("cardBins/openStateCardBins","id=" +entity.id+"&state="+sta,
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function(data){
                            if(data.status){
                                $scope.notice(data.msg);
                                $scope.query();
                            }else{
                                $scope.notice(data.msg);
                                $scope.query();
                            }
                        });
                }else{
                    if(entity.state){
                        entity.state=false;
                    }else{
                        entity.state=true;
                    }
                }
            });
    }
    //页面绑定回车事件
    $document.bind("keypress", function(event) {
        $scope.$apply(function (){
            if(event.keyCode == 13){
                $scope.query();
            }
        })
    });
})