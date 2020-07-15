/**
 * 代理商奖励活动
 */
angular.module('inspinia').controller('selectHlfActivityAgentCtrl',function(SweetAlert,i18nService,$scope,$http,$state,$stateParams,$compile,$filter,$log,$uibModal){
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    $scope.info={};
    $scope.firstRepeatStatuses = [{text:'是',value:'1'},{text:'否',value:'0'}];
    $scope.submitting = false;
    $scope.saveStatus=0;//0保存1修改

    //清空
    $scope.clear=function(){
        $scope.activityAgent={id:"",activityName:""};
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
            { field: 'id',displayName:'活动ID',width:100},
            { field: 'activityName',displayName:'活动名称',width:150},
            { field: 'scanTargetAmount',displayName:'扫码交易满奖达标条件',width:240,cellTemplate:
                    '<div ng-show="row.entity.scanActivityDays!=null&&row.entity.scanActivityDays!=0" style="padding: 8px 5px;">激活后{{row.entity.scanActivityDays}}天内，累计交易≥{{row.entity.scanTargetAmount}}元</div>'
            },
            { field: 'scanRewardAmount',displayName:'首次注册奖励金额(元)',width:240,cellFilter:"currency:''"},
            { field: 'scanRepeatRewardAmount',displayName:'重复注册奖励金额(元)',width:240,cellFilter:"currency:''"},
            { field: 'allTargetAmount',displayName:'全部交易满奖达标条件',width:240,cellTemplate:
                    '<div ng-show="row.entity.allActivityDays!=null&&row.entity.allActivityDays!=0" style="padding: 8px 5px;">激活后{{row.entity.allActivityDays}}天内，累计交易≥{{row.entity.allTargetAmount}}元</div>'
            },
            { field: 'allRewardAmount',displayName:'首次注册奖励金额(元)',width:240,cellFilter:"currency:''"},
            { field: 'allRepeatRewardAmount',displayName:'重复注册奖励金额(元)',width:240,cellFilter:"currency:''"},
            { field: 'createTime',displayName:'创建日期',width:240,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
            { field: 'action',displayName:'操作',width:200,pinnedRight:true,editable:true,
                cellTemplate:
                '<a class="lh30" ng-click="grid.appScope.detail(row.entity.id)">详情</a> ' +
                '<a class="lh30" ng-show="grid.appScope.hasPermit(\'activity.editHlfActivityAgentRule\')" ng-click="grid.appScope.actHardwareEdit(row.entity.id)">| 修改</a> ' +
                '<a class="lh30" ng-show="grid.appScope.hasPermit(\'activity.deleteHlfActivityAgentRule\')" ng-click="grid.appScope.delete(row.entity.id)">| 删除</a> '
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
            url: 'activity/selectHlfActivityAgentRule?pageNo=' + $scope.paginationOptions.pageNo + "&pageSize=" + $scope.paginationOptions.pageSize,
            data: $scope.activityAgent,
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

    $scope.checkInputDay= function(inputDay){
        var isInt = /^[1-9]\d*$/;//正整数
        if(inputDay==0||isInt.test(inputDay)){
            return true;
        }
        return false;
    }
    $scope.checkInputAmount= function(inputAmount){
        var isNum=/^(([1-9][0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,2})))$/;
        if(inputAmount==0||isNum.test(inputAmount)){
            return true;
        }
        return false;
    }

	//提交
    $scope.commit = function(){

        if($scope.info.activityName==null||$scope.info.activityName==""){
            $scope.notice("活动名称不能为空!");
            return;
        }
        if($scope.info.activityName.length>30){
            $scope.notice("活动名称不能超过30字!");
            return;
        }
       if($scope.info.scanActivityDays==null||$scope.info.scanActivityDays==""){
            $scope.info.scanActivityDays=0;
        }
        if($scope.info.scanTargetAmount==null||$scope.info.scanTargetAmount==""){
            $scope.info.scanTargetAmount="0";
        }
        if($scope.info.scanRewardAmount==null||$scope.info.scanRewardAmount==""){
            $scope.info.scanRewardAmount="0";
        }
        if($scope.info.scanRepeatRewardAmount==null||$scope.info.scanRepeatRewardAmount==""){
            $scope.info.scanRepeatRewardAmount="0";
        }

        if($scope.info.allActivityDays==null||$scope.info.allActivityDays==""){
            $scope.info.allActivityDays=0;
        }
        if($scope.info.allTargetAmount==null||$scope.info.allTargetAmount==""){
            $scope.info.allTargetAmount="0";
        }
        if($scope.info.allRewardAmount==null||$scope.info.allRewardAmount==""){
            $scope.info.allRewardAmount="0";
        }
        if($scope.info.allRepeatRewardAmount==null||$scope.info.allRepeatRewardAmount==""){
            $scope.info.allRepeatRewardAmount="0";
        }

        if(!$scope.checkInputDay($scope.info.scanActivityDays)
        ||!$scope.checkInputDay($scope.info.allActivityDays)
            ||!$scope.checkInputAmount($scope.info.scanTargetAmount)
            ||!$scope.checkInputAmount($scope.info.scanRewardAmount)
            ||!$scope.checkInputAmount($scope.info.scanRepeatRewardAmount)
            ||!$scope.checkInputAmount($scope.info.allTargetAmount)
            ||!$scope.checkInputAmount($scope.info.allRewardAmount)
            ||!$scope.checkInputAmount($scope.info.allRepeatRewardAmount)

        ){
            $scope.notice("请核对您的输入是否都为正整数");
            return;
        }

        if ($scope.submitting == true) {
            return;
        }
        $scope.submitting = true;
        var data = {
            "info" : $scope.info
        };
        if($scope.saveStatus==0){
            $http.post("activity/addHlfActivityAgentRule",angular.toJson(data))
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
            $http.post("activity/editHlfActivityAgentRule",angular.toJson(data))
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
        $http.post("activity/selectHlfActivityAgentRuleById",angular.toJson(id)).success(function (data) {
            if(data.status){
                $scope.info = data.param;
                if(data.param != null) {
                   // var stime = new Date(data.param.startTime).format("yyyy-MM-dd hh:mm:ss");
                    //var etime = new Date(data.param.endTime).format("yyyy-MM-dd hh:mm:ss");
                   // $scope.info.startTime = stime;
                   // $scope.info.endTime = etime;
                }
                $("#hardWardAddModel").modal("show");
            }
        })
    }

	//修改配置
    $scope.actHardwareEdit = function(id){
        $scope.isDetail=false;
        $scope.saveStatus=1;
        $http.post("activity/selectHlfActivityAgentRuleById",angular.toJson(id)).success(function (data) {
            if(data.status){
                $scope.info = data.param;
                if(data.param != null) {
                    //var stime = new Date(data.param.startTime).format("yyyy-MM-dd hh:mm:ss");
                   // var etime = new Date(data.param.endTime).format("yyyy-MM-dd hh:mm:ss");
                   // $scope.info.startTime = stime;
                   // $scope.info.endTime = etime;
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
                title: "如活动删除后，已关联该活动的机具均不会再继续参与代理商活动奖励，是否继续操作？",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post("activity/deleteHlfActivityAgentRule",id)
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
