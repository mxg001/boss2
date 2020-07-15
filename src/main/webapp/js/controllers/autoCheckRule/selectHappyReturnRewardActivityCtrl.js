/**
 * 欢乐返活动设置
 */
angular.module('inspinia').controller('selectHappyReturnRewardActivityCtrl',function(SweetAlert,i18nService,$scope,$http,$state,$stateParams,$compile,$filter,$log,$uibModal){
i18nService.setCurrentLang('zh-cn');

    $scope.info={activityName:"",startTime:"",endTime:"",cumulateTransMinusDay:"",cumulateTransDay:"",cumulateAmountMinus:"",
        cumulateAmountAdd:"",repeatCumulateAmountMinus:"", repeatCumulateAmountAdd:"", repeatCumulateTransDay:"", repeatCumulateTransMinusDay:""};
    $scope.submitting = false;
    $scope.saveStatus=0;//0保存1修改

    //清空
    $scope.clear=function(){
        $scope.activityRewardConfig={activityName:""};
    };
    $scope.clear();

    // 欢乐返-循环送的表格
    $scope.gridOptions={						//配置表格
        paginationPageSize:10,					//分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        enableHorizontalScrollbar: 1,			//横向滚动条
        enableVerticalScrollbar : 1,			//纵向滚动条
        columnDefs:[							//表格数据
            { field: 'id',displayName:'序号',width:100},
            { field: 'activityName',displayName:'活动名称',width:150},
            { field: 'startTime',displayName:'活动开始时间',width:150,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
            { field: 'endTime',displayName:'活动结束时间',width:150,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
            { field: 'cumulateTransDay',displayName:'首次注册累计交易奖励时间',width:240},
            { field: 'cumulateTransMinusDay',displayName:'首次注册累计交易扣费时间',width:240},
            { field: 'cumulateAmountAdd',displayName:'首次注册累计交易金额(奖励)',width:240,cellFilter:"currency:''"},
            { field: 'cumulateAmountMinus',displayName:'首次注册累计交易金额(扣费)',width:240,cellFilter:"currency:''"},
            { field: 'repeatCumulateTransDay',displayName:'重复注册累计交易奖励时间',width:240},
            { field: 'repeatCumulateTransMinusDay',displayName:'重复注册累计交易扣费时间',width:240},
            { field: 'repeatCumulateAmountAdd',displayName:'重复注册累计交易金额(奖励)',width:240,cellFilter:"currency:''"},
            { field: 'repeatCumulateAmountMinus',displayName:'重复注册累计交易金额(扣费)',width:240,cellFilter:"currency:''"},
            { field: 'action',displayName:'操作',width:200,pinnedRight:true,editable:true,
                cellTemplate:
                /*'<a class="lh30" ui-sref="activity.selectHappyReturnAgentActivity({activityId:row.entity.id})">代理商设置</a> ' +*/
                '<a class="lh30" ng-show="grid.appScope.hasPermit(\'activity.addHappyReturnRewardActivity\')" ng-click="grid.appScope.actHardwareEdit(row.entity.id)">修改</a> ' +
                '<a class="lh30" ng-show="grid.appScope.hasPermit(\'activity.deleteHappyReturnRewardActivity\')" ng-click="grid.appScope.delete(row.entity.id)">| 删除</a> '
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

    $scope.query = function () {
        $http({
            url: 'activity/selectHappyReturnRewardActivity?pageNo=' + $scope.paginationOptions.pageNo + "&pageSize=" + $scope.paginationOptions.pageSize,
            data: $scope.activityRewardConfig,
            method: 'POST'
        }).success(function (data) {
            if(data.status){
                $scope.gridOptions.data = data.page.result;
                $scope.gridOptions.totalItems = data.page.totalCount;
                console.log(data.page.result);
            }else{
                $scope.notice(data.msg);
            }
        }).error(function(){
        });
    };
    $scope.query();

	//提交
    $scope.commit = function(){
        if($scope.info.activityName==null||$scope.info.activityName==""){
            $scope.notice("活动名称不能为空!");
            return;
        }
        if($scope.info.startTime==null||$scope.info.startTime==""
            ||$scope.info.endTime==null||$scope.info.endTime==""){
            $scope.notice("活动时间不能为空!");
            return;
        }
        var isNum=/^(([1-9][0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,2})))$/;
        if($scope.info.cumulateTransMinusDay==null||$scope.info.cumulateTransMinusDay==""){
            $scope.notice("首次注册累计交易扣费时间不能为空!");
            return;
        }
        if($scope.info.cumulateTransDay==null||$scope.info.cumulateTransDay==""){
            $scope.notice("首次注册累计交易奖励时间不能为空!");
            return;
        }
        if($scope.info.cumulateTransDay>$scope.info.cumulateTransMinusDay){
            $scope.notice("首次注册累计交易奖励时间不能大于累计交易扣费时间!");
            return;
        }
        if($scope.info.cumulateAmountMinus==null||$scope.info.cumulateAmountMinus==""){
            $scope.notice("首次注册累计交易金额<不能为空");
            return;
        }else{
            if($scope.info.cumulateAmountMinus!=0 && !isNum.test($scope.info.cumulateAmountMinus)){
                $scope.notice("首次注册累计交易金额<格式不正确!");
                return;
            }
        }
        if($scope.info.cumulateAmountAdd==null||$scope.info.cumulateAmountAdd==""){
            $scope.notice("首次注册累计交易金额≥不能为空!");
            return;
        }else{
            if($scope.info.cumulateAmountAdd!=0 && !isNum.test($scope.info.cumulateAmountAdd)){
                $scope.notice("首次注册累计交易金额≥格式不正确");
                return;
            }
        }

        if($scope.info.repeatCumulateTransMinusDay==null||$scope.info.repeatCumulateTransMinusDay==""){
            $scope.notice("重复注册累计交易扣费时间不能为空!");
            return;
        }
        if($scope.info.repeatCumulateTransDay==null||$scope.info.repeatCumulateTransDay==""){
            $scope.notice("重复注册累计交易奖励时间不能为空!");
            return;
        }
        if($scope.info.repeatCumulateTransDay>$scope.info.repeatCumulateTransMinusDay){
            $scope.notice("重复注册累计交易奖励时间不能大于累计交易扣费时间!");
            return;
        }
        if($scope.info.repeatCumulateAmountMinus==null||$scope.info.repeatCumulateAmountMinus==""){
            $scope.notice("重复注册累计交易金额<不能为空");
            return;
        }else{
            if($scope.info.repeatCumulateAmountMinus!=0 && !isNum.test($scope.info.repeatCumulateAmountMinus)){
                $scope.notice("重复注册累计交易金额<格式不正确!");
                return;
            }
        }
        if($scope.info.repeatCumulateAmountAdd==null||$scope.info.repeatCumulateAmountAdd==""){
            $scope.notice("重复注册累计交易金额≥不能为空!");
            return;
        }else{
            if($scope.info.repeatCumulateAmountAdd!=0 && !isNum.test($scope.info.repeatCumulateAmountAdd)){
                $scope.notice("重复注册累计交易金额≥格式不正确");
                return;
            }
        }
        if ($scope.submitting == true) {
            return;
        }
        $scope.submitting = true;
        var data = {
            "info" : $scope.info
        };
        if($scope.saveStatus==0){
            $http.post("activity/addHappyReturnRewardActivity",angular.toJson(data))
                .success(function(data){
                    if(data.status){
                        $scope.notice(data.msg);
                        $scope.query();
                        $scope.cancel();
                    }else{
                        $scope.notice(data.msg);
                    }
                    $scope.submitting = false;
                })
                .error(function(){
                    $scope.submitting = false;
                });
        }else if($scope.saveStatus==1){
            $http.post("activity/editHappyReturnRewardActivity",angular.toJson(data))
                .success(function(data){
                    if(data.status){
                        $scope.notice(data.msg);
                        $scope.query();
                        $scope.cancel();
                    }else{
                        $scope.notice(data.msg);
                    }
                    $scope.submitting = false;
                })
                .error(function(){
                    $scope.submitting = false;
                });
        }

    }

	//取消
    $scope.submitCancel=function(){
        $scope.query();
    }
    $scope.submitCancel();

	//修改配置
    $scope.actHardwareEdit = function(id){
        $scope.saveStatus=1;
        $http.post("activity/selectHappyReturnRewardActivityById",angular.toJson(id)).success(function (data) {
            if(data.status){
                $scope.info = data.param;
                if(data.param != null) {
                    var stime = new Date(data.param.startTime).format("yyyy-MM-dd hh:mm:ss");
                    var etime = new Date(data.param.endTime).format("yyyy-MM-dd hh:mm:ss");
                    $scope.info.startTime = stime;
                    $scope.info.endTime = etime;
                }
                $("#hardWardAddModel").modal("show");
            }
        })
    }

	//添加
    $scope.hardWardAddModel = function(){
        $scope.saveStatus=0
        $("#hardWardAddModel").modal("show");
        $scope.info={activityName:"",startTime:"",endTime:"",cumulateTransMinusDay:"",
            cumulateTransDay:"",cumulateAmountMinus:"",cumulateAmountAdd:"",repeatCumulateAmountMinus:"",
            repeatCumulateAmountAdd:"", repeatCumulateTransDay:"", repeatCumulateTransMinusDay:""};
    }
    //返回
    $scope.cancel=function(){
        $scope.info={};
        $('#hardWardAddModel').modal('hide');
    }

    $scope.delete=function(id){
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
                    $http.post("activity/deleteHappyReturnRewardActivity",id)
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
                    $scope.exportInfoClick("activity/importHappyReturnRewardActivity",{"info":angular.toJson($scope.activityRewardConfig)});
                }
            });
    };
//console.log();
//转换日期格式
Date.prototype.format = function(fmt) { 
    var o = { 
       "M+" : this.getMonth()+1,			//月份 
       "d+" : this.getDate(),				//日 
       "h+" : this.getHours(),				//小时 
       "m+" : this.getMinutes(),			//分 
       "s+" : this.getSeconds(),			//秒 
       "q+" : Math.floor((this.getMonth()+3)/3),	//季度 
       "S"  : this.getMilliseconds()		//毫秒 
   };
   if(/(y+)/.test(fmt)) {
           fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
   }
    for(var k in o) {
       if(new RegExp("("+ k +")").test(fmt)){
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
        }
    }
   return fmt;
}

});
