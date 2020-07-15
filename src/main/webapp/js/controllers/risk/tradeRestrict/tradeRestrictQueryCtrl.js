/**
 * 交易限制记录
 */
angular.module('inspinia',['infinity.angular-chosen','uiSwitch']).controller('tradeRestrictQueryCtrl',function($scope,$http,i18nService,SweetAlert,$document,$timeout){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    $scope.statusSelect = [{text:"全部",value:"-1"},{text:"开启",value:"1"},{text:"关闭",value:"2"},{text:"失效",value:"3"}];

    //清空
    $scope.clear=function(){
        $scope.info={status:"-1"};
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
            { field: 'limitNumber',displayName:'受限卡号/商户号',width:180 },
            {field: 'status',displayName: '限制状态',width: 180,
                cellTemplate:
                    '<div ng-show="row.entity.status==3">'+
                        '<span class="lh30" >失效</span>'+
                    '</div>'+
                    '<div ng-show="row.entity.status!=3">'+
                        '<span  ng-show="grid.appScope.hasPermit(\'tradeRestrict.changeStatus\')"><switch class="switch switch-s" ng-model="row.entity.buttonStatus" ng-true-value="1" ng-false-value="0" ng-change="grid.appScope.changeStatus(row.entity)" /></span>'+
                        '<span ng-show="!grid.appScope.hasPermit(\'tradeRestrict.changeStatus\')"> <span ng-show="row.entity.status==1" class="lh30" >开启</span><span ng-show="row.entity.status==2" class="lh30" >关闭</span></span>'+
                    '</div>'
            },
            { field: 'merchantName',displayName:'商户名称',width:180 },
            { field: 'orderNo',displayName:'交易订单号',width:180,
                cellTemplate:''+
                '<div ng-show="row.entity.orderNo!=null" style="margin-top:7px;"> ' +
                '<a target="_blank" ui-sref="trade.tradeQueryDetail({id:row.entity.orderNo,val:0})">{{row.entity.orderNo}}</a> ' +
                '</div>'
            },
            { field: 'merchantPhone',displayName:'商户手机号',width:180 },
            { field: 'rollNo',displayName:'规则编号',width:180 },
            { field: 'triggerNumber',displayName:'限制次数',width:180 },
            { field: 'agentNo',displayName:'代理商编号',width:180 },
            { field: 'resultNo',displayName:'失败返回码',width:180 },
            { field: 'createPerson',displayName:'操作人',width:180 },
            { field: 'operationTime',displayName:'操作时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'merchantTime',displayName:'商户注册时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'createTime',displayName:'创建时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'invalidTime',displayName:'失效时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180}
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
        $http.post("tradeRestrict/selectAll","info="+angular.toJson($scope.info)+"&pageNo="+
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
    //导出信息
    $scope.exportInfo=function(){
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
                    $scope.exportInfoClick("tradeRestrict/importDetail",{"info":angular.toJson($scope.info)});
                }
            });
    };
    //状态当个改变
    $scope.changeStatus=function(entry){
        var title="";
        var sta=0;
        if(entry.buttonStatus==1||entry.buttonStatus==true){
            title="确认开启限制?";
            sta=1;
        }else{
            title="确认解除限制?";
            sta=2;
        }
        SweetAlert.swal({
                title: title,
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post("tradeRestrict/changeStatus","id="+entry.id+"&status="+sta,
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function(data){
                            if(data.status){
                                $scope.notice(data.msg);
                                $scope.query();
                            }else{
                                $scope.notice(data.msg);
                                $scope.rollBackEntry(entry);
                            }
                        })
                        .error(function(data){
                            $scope.notice(data.msg);
                            $scope.rollBackEntry(entry);
                        });
                }else{
                    $scope.rollBackEntry(entry);
                }
            });
    };
    $scope.rollBackEntry=function (entry) {
         if(entry.status==1){
             entry.buttonStatus=1;
        }else{
             entry.buttonStatus=0;
        }
    };

    //状态批量改变
    $scope.riskStatusChangeBatch=function(status){
        var selectList = $scope.gridApi.selection.getSelectedRows();
        var title1="";
        var title2="";
        if(status==1){
            title1="请选中要批量开启限制的数据!";
            title2="确认批量开启限制?";
        }else{
            title1="请选中要批量解除限制的数据!";
            title2="确认批量解除限制?";
        }
        if(selectList==null||selectList.length==0){
            $scope.notice(title1);
            return false;
        }
        var ids="";
        var count=0;
        if(selectList!=null&&selectList.length>0){
            for(var i=0;i<selectList.length;i++){
                if(status==1){
                    if(selectList[i].status==2){
                        ids = ids + selectList[i].id + ",";
                        count++;
                    }
                }else{
                    if(selectList[i].status==1){
                        ids = ids + selectList[i].id + ",";
                        count++;
                    }
                }
            }
        }
        if(ids==""){
            $scope.notice("选中的数据,没有符合条件的数据!");
            return false;
        }
        ids=ids.substring(0,ids.length-1);
        title2="符合的数据有"+count+"条,"+title2;
        SweetAlert.swal({
                title: title2,
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post("tradeRestrict/riskStatusChangeBatch","ids="+ids+"&status="+status,
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