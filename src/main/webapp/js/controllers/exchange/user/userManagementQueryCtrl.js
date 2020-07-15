/**
 * 超级兑用户列表
 */
angular.module('inspinia').controller('userManagementQueryCtrl',function($scope,$http,i18nService,$document,SweetAlert){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    $scope.merAccountSelect = [{text:"否",value:0},{text:"是",value:1}];
    $scope.merAccountStr=angular.toJson($scope.merAccountSelect);

    $scope.merCapaSelect=$scope.merCapaExchange;
    $scope.merCapaStr=angular.toJson($scope.merCapaSelect);

    $scope.freezeAmountStateSelect = [{text:"全部",value:""},{text:"冻结",value:"1"}];

    //组织列表
    $scope.oemList=[{value:"",text:"全部"}];
    $http.post("exchangeOem/getOemList",
        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function(data){
            if(data.status){
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

    //清空
    $scope.clear=function(){
        $scope.info={
            merchantNo:"",oemNo:"",status:"",mobileUsername:"",userName:"",merCapa:"",parMerNo:"",
            freezeAmountState:"",
            createTimeBegin:moment(new Date().getTime()-24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
            createTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
            paymentTimeBegin:"",paymentTimeEnd:""
        };
    }
    $scope.clear();

    $scope.clearMerInfoTotal=function () {
        $scope.merInfoTotal={merTotal:0,ordmemTotal:0,supermemTotal:0,merActTotal:0,
            ordparTotal:0,goldparTotal:0,diamparTotal:0};
    };
    $scope.clearMerInfoTotal();

    $scope.userGrid={                           //配置表格
        data: 'result',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'merchantNo',displayName:'用户ID',width:130},
            { field: 'oemName',displayName:'组织名称',width:130 },
            { field: 'accountName',displayName:'姓名',width:130},
            { field: 'userName',displayName:'昵称',width:130},
            { field: 'mobileUsername',displayName:'手机号',width:150 },
            { field: 'totalBalance',displayName:'总收益',width:180,cellFilter:"currency:''" },
            { field: 'merCapa',displayName:'代理身份',width:120,cellFilter:"formatDropping:" + $scope.merCapaStr },
            { field: 'merAccount',displayName:'是否开户',width:120,cellFilter:"formatDropping:" + $scope.merAccountStr},
            { field: 'createTime',displayName:'入驻时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'paymentTime',displayName:'支付时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'parMerNo',displayName:'上级代理ID',width:150 },
            { field: 'freezeAmountBalance',displayName:'冻结金额',width:180,cellFilter:"currency:''" },
            { field: 'freezeAmount',displayName:'预冻结金额',width:180,cellFilter:"currency:''" },
            { field: 'id',displayName:'操作',width:200,pinnedRight:true, cellTemplate:
                '<div class="lh30">'+
                    '<a target="_blank" ui-sref="exchange.userManagementDetail({merchantNo:row.entity.merchantNo})">详情</a> ' +
                    '<a ng-show="grid.appScope.hasPermit(\'userManagement.updateUserManagement\')" target="_blank" ui-sref="exchange.userManagementEdit({merchantNo:row.entity.merchantNo})"> | 修改</a> ' +
                    '<a ng-show="grid.appScope.hasPermit(\'userManagement.userFreeze\')" target="_blank" ng-click="grid.appScope.freezeModal(row.entity)"> | 预冻结</a> ' +
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
        $http.post("userManagement/selectAll","info="+angular.toJson($scope.info)+"&pageNo="+
            $scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.result=data.page.result;
                    $scope.userGrid.totalItems = data.page.totalCount;
                    $scope.merInfoTotal=data.merInfoTotal;
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
                    location.href="userManagement/importDetail?info="+encodeURI(angular.toJson($scope.info));
                }
            });
    };

    $scope.freezeModal = function(entity){
        $scope.addinfo={};
        $scope.addinfo.merchantNo=entity.merchantNo;
        $scope.addinfo.remark=entity.freezeRemark;
        $scope.addinfo.freezeAmount=entity.freezeAmount;
        $('#freezeModal').modal('show');
    };
    $scope.cancel = function(){
        $('#freezeModal').modal('hide');
    };

    $scope.addUserFreeze=function (sta) {
        $scope.subAddinfo=angular.copy($scope.addinfo);
        var title="";
        if(sta==1){
            if($scope.subAddinfo.freezeAmount==null||$scope.subAddinfo.freezeAmount===""){
                $scope.notice("预冻结金额不能为空!");
                return;
            }
            if(Number($scope.subAddinfo.freezeAmount)<=0){
                $scope.notice("预冻结金额不能小于等于0!");
                return;
            }
            if($scope.subAddinfo.remark==null||$scope.subAddinfo.remark==""){
                $scope.notice("备注不能为空!");
                return;
            }
            title="确认预冻结?";
            $scope.subAddinfo.freezeStatus="1";
        }else if(sta==0){
            $scope.subAddinfo.freezeAmount=0;
            $scope.subAddinfo.remark=null;
            title="确认解结?";
            $scope.subAddinfo.freezeStatus="0";
        }
        SweetAlert.swal({
                title:title,
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true
            },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post("userManagement/userFreeze","info="+angular.toJson($scope.subAddinfo),
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function(data){
                            if(data.status){
                                $scope.notice(data.msg);
                                $scope.cancel();
                                $scope.query();
                            }else{
                                $scope.notice(data.msg);
                                $scope.cancel();
                            }
                        })
                        .error(function(data){
                            $scope.notice(data.msg);
                            $scope.cancel();
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