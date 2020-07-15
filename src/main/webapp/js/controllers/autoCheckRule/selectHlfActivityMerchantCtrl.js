/**
 * 活跃商户活动配置
 */
angular.module('inspinia').controller('selectHlfActivityMerchantCtrl',function(SweetAlert,i18nService,$scope,$http,$state,$stateParams,$compile,$filter,$log,$uibModal){
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    $scope.info={};
    $scope.firstRepeatStatuses = [{text:'是',value:'1'},{text:'否',value:'0'}];
    $scope.submitting = false;
    $scope.saveStatus=0;//0保存1修改

    //清空
    $scope.clear=function(){
        $scope.activityMerchant={ruleId:"",ruleName:""};
    };
    $scope.clear();

    // 活跃商户活动配置表格
    $scope.gridOptions={						//配置表格
        paginationPageSize:10,					//分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        enableHorizontalScrollbar: 1,			//横向滚动条
        enableVerticalScrollbar : 1,			//纵向滚动条
        columnDefs:[							//表格数据
            { field: 'ruleId',displayName:'活动ID',width:100},
            { field: 'ruleName',displayName:'活动名称',width:150},
            { field: 'startTime',displayName:'开始日期',width:150,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
            { field: 'endTime',displayName:'结束日期',width:150,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
            { field: 'firstRewardAmount',displayName:'首次注册奖励金额(元)',width:240,cellFilter:"currency:''"},
            { field: 'firstDeductAmount',displayName:'首次注册扣款金额(元)',width:240,cellFilter:"currency:''"},
            { field: 'repeatRewardAmount',displayName:'重复注册奖励金额(元)',width:240,cellFilter:"currency:''"},
            { field: 'repeatDeductAmount',displayName:'重复注册扣款金额(元)',width:240,cellFilter:"currency:''"},
            { field: 'createTime',displayName:'创建日期',width:240,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
            { field: 'action',displayName:'操作',width:200,pinnedRight:true,editable:true,
                cellTemplate:
                '<a class="lh30" ng-click="grid.appScope.detail(row.entity.ruleId)">详情</a> ' +
                '<a class="lh30" ng-show="grid.appScope.hasPermit(\'activity.editHlfActivityMerchantRule\')" ng-click="grid.appScope.actHardwareEdit(row.entity.ruleId)">| 修改</a> ' +
                '<a class="lh30" ng-show="grid.appScope.hasPermit(\'activity.deleteHlfActivityMerchantRule\')" ng-click="grid.appScope.delete(row.entity.ruleId)">| 删除</a> '
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
            url: 'activity/selectHlfActivityMerchantRule?pageNo=' + $scope.paginationOptions.pageNo + "&pageSize=" + $scope.paginationOptions.pageSize,
            data: $scope.activityMerchant,
            method: 'POST'
        }).success(function (data) {
            if(data.status){
                $scope.gridOptions.data = data.page.result;
                $scope.gridOptions.totalItems = data.page.totalCount;
            }else{
                $scope.notice(data.msg);
            }
        }).error(function(){
        });
    };
    $scope.query();

	//提交
    $scope.commit = function(){
        if($scope.info.startTime==null||$scope.info.startTime==""
            ||$scope.info.endTime==null||$scope.info.endTime==""){
            $scope.notice("活动时间不能为空!");
            return;
        }
        if($scope.info.ruleName==null||$scope.info.ruleName==""){
            $scope.notice("活动名称不能为空!");
            return;
        }
        var isNum=/^(([1-9][0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,2})))$/;
        var isInt = /^[1-9]\d*$/;//正整数
        if($scope.info.firstRewardMonth==null||$scope.info.firstRewardMonth===""){
            $scope.notice("首次注册奖励考核月数不能为空!");
            return;
        }else{
            if($scope.info.firstRewardMonth!=0 && !isInt.test($scope.info.firstRewardMonth)){
                $scope.notice("首次注册奖励考核月数为正整数");
                return;
            }
        }
        if($scope.info.firstRewardTotalAmount==null||$scope.info.firstRewardTotalAmount===""){
            $scope.notice("首次注册奖励累计交易金额不能为空!");
            return;
        }else{
            if($scope.info.firstRewardTotalAmount!=0 && !isNum.test($scope.info.firstRewardTotalAmount)){
                $scope.notice("首次注册奖励累计交易金额格式不正确!");
                return;
            }
        }
        if($scope.info.firstRewardAmount==null||$scope.info.firstRewardAmount===""){
            $scope.notice("首次注册奖励金额不能为空!");
            return;
        }else{
            if($scope.info.firstRewardAmount!=0 && !isNum.test($scope.info.firstRewardAmount)){
                $scope.notice("首次注册奖励金额格式不正确!");
                return;
            }
        }
        if($scope.info.firstRewardAmount!=0&&!isInt.test($scope.info.firstRewardMonth)){
            $scope.notice("首次注册奖励金额不为0时,首次注册奖励考核月数为正整数!");
            return;
        }

        if($scope.info.firstDeductMonth==null||$scope.info.firstDeductMonth===""){
            $scope.notice("首次注册扣款考核月数不能为空!");
            return;
        }else{
            if($scope.info.firstDeductMonth!=0 && !isInt.test($scope.info.firstDeductMonth)){
                $scope.notice("首次注册扣款考核月数为正整数");
                return;
            }
        }
        if($scope.info.firstDeductTotalAmount==null||$scope.info.firstDeductTotalAmount===""){
            $scope.notice("首次注册扣款累计交易金额不能为空!");
            return;
        }else{
            if($scope.info.firstDeductTotalAmount!=0 && !isNum.test($scope.info.firstDeductTotalAmount)){
                $scope.notice("首次注册扣款累计交易金额格式不正确!");
                return;
            }
        }
        if($scope.info.firstDeductAmount==null||$scope.info.firstDeductAmount===""){
            $scope.notice("首次注册扣款金额不能为空!");
            return;
        }else{
            if($scope.info.firstDeductAmount!=0 && !isNum.test($scope.info.firstDeductAmount)){
                $scope.notice("首次注册扣款金额格式不正确!");
                return;
            }
        }
        if($scope.info.firstDeductAmount!=0&&!isInt.test($scope.info.firstDeductMonth)){
            $scope.notice("首次注册扣款金额不为0时,首次注册扣款考核月数为正整数!");
            return;
        }

        if($scope.info.firstRepeatStatus==0){
            if($scope.info.repeatRewardMonth==null||$scope.info.repeatRewardMonth===""){
                $scope.notice("重复注册奖励考核月数不能为空!");
                return;
            }else{
                if($scope.info.repeatRewardMonth!=0 && !isInt.test($scope.info.repeatRewardMonth)){
                    $scope.notice("重复注册奖励考核月数为正整数");
                    return;
                }
            }
            if($scope.info.repeatRewardTotalAmount==null||$scope.info.repeatRewardTotalAmount===""){
                $scope.notice("重复注册奖励累计交易金额不能为空!");
                return;
            }else{
                if($scope.info.repeatRewardTotalAmount!=0 && !isNum.test($scope.info.repeatRewardTotalAmount)){
                    $scope.notice("重复注册奖励累计交易金额格式不正确!");
                    return;
                }
            }
            if($scope.info.repeatRewardAmount==null||$scope.info.repeatRewardAmount===""){
                $scope.notice("重复注册奖励金额不能为空!");
                return;
            }else{
                if($scope.info.repeatRewardAmount!=0 && !isNum.test($scope.info.repeatRewardAmount)){
                    $scope.notice("重复注册奖励金额格式不正确!");
                    return;
                }
            }
            if($scope.info.repeatRewardAmount!=0&&!isInt.test($scope.info.repeatRewardMonth)){
                $scope.notice("重复注册奖励金额不为0时,重复注册奖励考核月数为正整数!");
                return;
            }

            if($scope.info.repeatDeductMonth==null||$scope.info.repeatDeductMonth===""){
                $scope.notice("重复注册扣款考核月数不能为空!");
                return;
            }else{
                if($scope.info.repeatDeductMonth!=0 && !isInt.test($scope.info.repeatDeductMonth)){
                    $scope.notice("重复注册扣款考核月数为正整数");
                    return;
                }
            }
            if($scope.info.repeatDeductTotalAmount==null||$scope.info.repeatDeductTotalAmount===""){
                $scope.notice("重复注册扣款累计交易金额不能为空!");
                return;
            }else{
                if($scope.info.repeatDeductTotalAmount!=0 && !isNum.test($scope.info.repeatDeductTotalAmount)){
                    $scope.notice("重复注册扣款累计交易金额格式不正确!");
                    return;
                }
            }
            if($scope.info.repeatDeductAmount==null||$scope.info.repeatDeductAmount===""){
                $scope.notice("重复注册扣款金额不能为空!");
                return;
            }else{
                if($scope.info.repeatDeductAmount!=0 && !isNum.test($scope.info.repeatDeductAmount)){
                    $scope.notice("重复注册扣款金额格式不正确!");
                    return;
                }
            }
            if($scope.info.repeatDeductAmount!=0&&!isInt.test($scope.info.repeatDeductMonth)){
                $scope.notice("重复注册扣款金额不为0时,重复注册扣款考核月数为正整数!");
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
            $http.post("activity/addHlfActivityMerchantRule",angular.toJson(data))
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
            $http.post("activity/editHlfActivityMerchantRule",angular.toJson(data))
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

    $scope.isDetail=false;
    $scope.detail= function(id){
        $scope.isDetail=true;
        $scope.saveStatus=1;
        $http.post("activity/selectHlfActivityMerchantRuleById",angular.toJson(id)).success(function (data) {
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

	//修改配置
    $scope.actHardwareEdit = function(id){
        $scope.isDetail=false;
        $scope.saveStatus=1;
        $http.post("activity/selectHlfActivityMerchantRuleById",angular.toJson(id)).success(function (data) {
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
        $scope.isDetail=false;
        $scope.saveStatus=0
        $("#hardWardAddModel").modal("show");
        $scope.info={startTime:'',endTime:'',ruleName:'',firstRewardMonth:'',firstRewardType:0,firstRewardTotalAmount:'',
            firstRewardAmount:'',firstDeductMonth:'', firstDeductType:0, firstDeductTotalAmount:'', firstDeductAmount:'',
            firstRepeatStatus:1,repeatRewardMonth:'',repeatRewardType:0,repeatRewardTotalAmount:'',
            repeatRewardAmount:'',repeatDeductMonth:'', repeatDeductType:0, repeatDeductTotalAmount:'', repeatDeductAmount:''};
    }
    //返回
    $scope.cancel=function(){
        $scope.info={};
        $('#hardWardAddModel').modal('hide');
    }

    $scope.delete=function(id){
        SweetAlert.swal({
                title: "如活动删除后，已关联该活动的机具均不会再继续参与活跃商户活动，是否继续操作？",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post("activity/deleteHlfActivityMerchantRule",id)
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
