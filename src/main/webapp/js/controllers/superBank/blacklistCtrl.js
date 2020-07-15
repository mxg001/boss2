/**yxs
 * 黑名单管理
 */
angular.module('inspinia',['uiSwitch']).controller('blacklistCtrl',function($scope,$http,i18nService,$document,SweetAlert){
    //数据源
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    $scope.typeList = [{text:"全部",value:""},{text:"抢红包黑名单",value:"0"},{text:"购买领地黑名单",value:"1"},{text:"办理信用卡黑名单",value:"2"},{text:"提现黑名单",value:"3"}];
    $scope.statusAll = [{text:"全部",value:""},{text:"打开",value:"1"},{text:"关闭",value:"0"}];
    $scope.baseInfo = {};
    $scope.blackLoglistDate=[];
    $scope.blackLogRollNo='0';
    $scope.query = function () {
        $scope.submitting = true;
        $scope.loadImg = true;
        $http({
            url: 'superBank/selectBlackList?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
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
    $scope.query();
    
    $scope.resetForm = function () {
        $scope.baseInfo = {type:"",status:"",userId:"",userPhone:"",userIdCard:"",createBy:"",
        		createDateStart:moment(new Date().getTime()-6*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
        		createDateEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'};
        clearSubCondition();
    }
    $scope.resetForm();
    
    
    $scope.columnDefs = [
        {field: 'id',displayName: '序号',width: 120,pinnable: false,sortable: false},
        {field: 'type',displayName: '黑名单类型',width: 150,pinnable: false,sortable: false,
        	cellFilter:'formatDropping:[{text:"抢红包黑名单",value:"0"},{text:"购买领地黑名单",value:"1"},{text:"办理信用卡黑名单",value:"2"},{text:"提现黑名单",value:"3"}]'},
        {field: 'userCode',displayName: '用户ID',width: 150,pinnable: false,sortable: false},
        {field: 'userPhone',displayName: '手机号',width: 120,pinnable: false,sortable: false},
        {field: 'userIdCard',displayName: '身份证',width: 150,pinnable: false,sortable: false},
        {field: 'status',displayName: '状态',width:150,pinnable: false,sortable: false,cellTemplate:
            '<span ng-show="grid.appScope.hasPermit(\'superBank.updateBlackStatus\')">' +
             '<switch class="switch switch-s" ng-model="row.entity.status" ng-change="grid.appScope.switchStatus(row)" />' +
            '</span>'
            +'<span ng-show="!grid.appScope.hasPermit(\'superBank.updateBlackStatus\')">' +
                ' <span ng-show="row.entity.status==1">是</span>' +
             '<span ng-show="row.entity.status==0">否</span>' +
            '</span>'
        },
        {field: 'createDate',displayName: '创建时间',width: 180,pinnable: false,sortable: false,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'createBy',displayName: '创建人',width: 120,pinnable: false,sortable: false},
        {field: 'remark',displayName: '备注',width: 180,pinnable: false,sortable: false},
        {field: 'action',displayName: '操作',width: 150,pinnedRight:true,sortable: false,editable:true,cellTemplate:
              "<a ng-show='grid.appScope.hasPermit(\"superBank.deleteBlack\")'  ng-click='grid.appScope.deleteBlack(row.entity.id)'>  删除 </a>" +
              "<a ng-show='grid.appScope.hasPermit(\"superBank.viewDoLog\")'  ng-click='grid.appScope.showLog(row.entity.id)'> | 日志 </a>" 
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



    
    
    <!--黑名单日志查看弹出框,并查询请求-->
    $scope.showLog = function(blackId){
        $scope.blackLogRollNo=blackId;
        $http.post('superBank/selectBlackLogList',
            "blackId="+blackId+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize+'&temp='+new Date().getTime(),
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(!data.status){
                    $scope.notice("查询失败");
                    return;
                }

                $scope.blackLoglistDate =data.data.result;
                $scope.showLogGrid.totalItems = data.data.totalCount;
                $("#showLogDiv").modal({height:400,width:860});
            });
    };

    <!--黑名单日志查询展示结果-->
    $scope.showLogGrid = {
        data:"blackLoglistDate",
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50],	  //切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        columnDefs: [
            {field: 'createDate',displayName: '操作时间',width: 150,pinnable: false,sortable: false,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',enableColumnMenu: false,enableSorting: false},
            {field: 'type',displayName: '操作功能',width: 150,pinnable: false,sortable: false,enableColumnMenu: false,enableSorting: false,
            	cellFilter:'formatDropping:[{text:"添加黑名单",value:"0"},{text:"关闭黑名单",value:"1"},{text:"打开黑名单",value:"2"}]'},
            {field: 'createBy',displayName: '操作人',width: 150,pinnable: false,sortable: false,enableColumnMenu: false,enableSorting: false},
            {field: 'remark',displayName: '备注',width: 380,pinnable: false,sortable: false,enableColumnMenu: false,enableSorting: false}
        ],
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.paginationOptions.pageNo = newPage;
                $scope.paginationOptions.pageSize = pageSize;
                $scope.showLog($scope.blackLogRollNo);
            });
        }
    };
    
    
    
    $scope.handleResults=1;
    $scope.handleRemark='';
    $scope.updateHandleStatusCommit=function(){
        var data={"id":$scope.id,"status":$scope.handleResults,"remark":$scope.handleRemark};
        $http.post("superBank/updateBlackStatus",angular.toJson(data))
            .success(function(msg){
                if(msg.status){
                    $scope.notice(msg.msg);
                    $('#updateStatusModal').modal('hide');
                    $scope.query();
                }else{
                    $scope.notice(msg.msg);
                    $('#updateStatusModal').modal('hide');
                    $scope.query();
                }
            }).error(function(){
        });
    };
    
    $scope.updateHandleStatusColse=function(){
        $('#updateStatusModal').modal('hide');
        $scope.query();
    };
    
    //修改上架开关
    $scope.switchStatus=function(row){
        if(row.entity.status){
            $scope.serviceText = "确定开启";
        } else {
            $scope.serviceText = "确定关闭";
        }
        $('#myTitle').html($scope.serviceText);
        $scope.id=row.entity.id;
        if(row.entity.status==true){
            $scope.handleResults=1;
        } else if(row.entity.status==false){
            $scope.handleResults=0;
        }
        $scope.handleRemark = row.entity.remark;
        $('#updateStatusModal').modal('show');
        
//        SweetAlert.swal({
//                title: "",
//                text: $scope.serviceText,
//                type: "warning",
//                showCancelButton: true,
//                confirmButtonColor: "#DD6B55",
//                confirmButtonText: "确定",
//                cancelButtonText: "取消",
//                closeOnConfirm: true,
//                closeOnCancel: true },
//            function (isConfirm) {
//                if (isConfirm) {
//                    if(row.entity.statusInt==true){
//                        row.entity.status='on';
//                    } else if(row.entity.statusInt==false){
//                        row.entity.status='off';
//                    }
//                    var data={"status":row.entity.status,"id":row.entity.id};
//                    $http.post("superBank/updateBlackStatus",angular.toJson(data))
//                        .success(function(data){
//                            $scope.notice(data.msg);
//                            if(data.status){
//                                $scope.query();
//                            }else{
//                                row.entity.status = !row.entity.status;
//                            }
//                        })
//                        .error(function(data){
//                            row.entity.status = !row.entity.status;
//                            $scope.notice("服务器异常");
//                        });
//                } else {
//                    row.entity.statusInt = !row.entity.status;
//                }
//            });
    };

    //删除黑名单
    $scope.deleteBlack=function(id){
        SweetAlert.swal({
                title: "确认删除该条黑名单吗？",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确认",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post('superBank/deleteBlack?id='+id).success(function(msg){
                        if(msg.status){
                            $scope.notice(msg.msg);
                            $scope.query();
                        } else {
                            $scope.notice(msg.msg);
                        }
                    }).error(function(){
                    });
                }
            });
    };
    
  
    function  clearSubCondition(){
    	$scope.baseInfo.typeList="";
    	$scope.baseInfo.statusAll="";
    	$scope.baseInfo.userId="";
    	$scope.baseInfo.userPhone="";
    	$scope.baseInfo.userIdCard="";
    	$scope.baseInfo.createBy="";
//    	$scope.baseInfo.createDateStart="";
//    	$scope.baseInfo.createDateEnd="";
    }
    
    
    //页面绑定回车事件
    $document.bind("keypress", function(event) {
        $scope.$apply(function (){
            if(event.keyCode == 13){
                $scope.query();
            }
        })
    });
});