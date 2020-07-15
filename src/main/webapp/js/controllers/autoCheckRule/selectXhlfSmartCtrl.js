/**
 * 新欢乐送智能版活动
 */
angular.module('inspinia').controller('selectXhlfSmartCtrl',function(SweetAlert,i18nService,$scope,$http,$state,$stateParams,$compile,$filter,$log,$uibModal){
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    $scope.info={};
    $scope.firstRepeatStatuses = [{text:'是',value:'1'},{text:'否',value:'0'}];
    $scope.submitting = false;
    $scope.saveStatus=0;//0保存1修改

    //清空
    $scope.clear=function(){
        $scope.xhlfSmart={id:"",activityName:""};
    };
    $scope.clear();

    // 活动配置表格
    $scope.gridOptions={						//配置表格
        paginationPageSize:10,					//分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        enableHorizontalScrollbar: 1,			//横向滚动条
        enableVerticalScrollbar : 1,			//纵向滚动条
        columnDefs:[							//表格数据
            { field: 'id',displayName:'活动ID',width:100},
            { field: 'activityName',displayName:'活动名称',width:150},
            { field: 'oneTransAmount',displayName:'第1次考核条件',width:240,cellTemplate:
                    '<div ng-show="row.entity.oneLimitDays!=null&&row.entity.oneLimitDays!=0" style="padding: 8px 5px;">激活后{{row.entity.oneLimitDays}}天内，累计交易≥{{row.entity.oneTransAmount}}元</div>'
            },
            { field: 'twoTransAmount',displayName:'第2次考核条件',width:240,cellTemplate:
                    '<div ng-show="row.entity.twoLimitDays!=null&&row.entity.twoLimitDays!=0" style="padding: 8px 5px;">激活后{{row.entity.twoLimitDays}}天内，累计交易≥{{row.entity.twoTransAmount}}元</div>'
            },
            { field: 'threeTransAmount',displayName:'第3次考核条件',width:240,cellTemplate:
                    '<div ng-show="row.entity.threeLimitDays!=null&&row.entity.threeLimitDays!=0" style="padding: 8px 5px;">激活后{{row.entity.threeLimitDays}}天内，累计交易≥{{row.entity.threeTransAmount}}元</div>'
            },
            { field: 'createTime',displayName:'创建日期',width:240,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
            { field: 'action',displayName:'操作',width:200,pinnedRight:true,editable:true,
                cellTemplate:
                '<a class="lh30" ng-click="grid.appScope.detail(row.entity.id)">详情</a> ' +
                '<a class="lh30" ng-show="grid.appScope.hasPermit(\'activity.editXhlfSmart\')" ng-click="grid.appScope.actHardwareEdit(row.entity.id)">| 修改</a> ' +
                '<a class="lh30" ng-show="grid.appScope.hasPermit(\'activity.deleteXhlfSmart\')" ng-click="grid.appScope.delete(row.entity.id)">| 删除</a> '
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
            url: 'activity/selectXhlfSmart?pageNo=' + $scope.paginationOptions.pageNo + "&pageSize=" + $scope.paginationOptions.pageSize,
            data: $scope.xhlfSmart,
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
       if($scope.info.oneLimitDays==null||$scope.info.oneLimitDays==""){
            $scope.info.oneLimitDays=0;
        }
        if($scope.info.oneTransAmount==null||$scope.info.oneTransAmount==""){
            $scope.info.oneTransAmount="0";
        }
        if($scope.info.oneRewardMerAmount==null||$scope.info.oneRewardMerAmount==""){
            $scope.info.oneRewardMerAmount="0";
        }
        if($scope.info.oneRepeatRewardMerAmount==null||$scope.info.oneRepeatRewardMerAmount==""){
            $scope.info.oneRepeatRewardMerAmount="0";
        }
        if($scope.info.oneRewardAgentAmount==null||$scope.info.oneRewardAgentAmount==""){
            $scope.info.oneRewardAgentAmount="0";
        }
        if($scope.info.oneRepeatRewardAgentAmount==null||$scope.info.oneRepeatRewardAgentAmount==""){
            $scope.info.oneRepeatRewardAgentAmount="0";
        }

        if($scope.info.twoLimitDays==null||$scope.info.twoLimitDays==""){
            $scope.info.twoLimitDays=0;
        }
        if($scope.info.twoTransAmount==null||$scope.info.twoTransAmount==""){
            $scope.info.twoTransAmount="0";
        }
        if($scope.info.twoRewardAgentAmount==null||$scope.info.twoRewardAgentAmount==""){
            $scope.info.twoRewardAgentAmount="0";
        }
        if($scope.info.twoRepeatRewardAgentAmount==null||$scope.info.twoRepeatRewardAgentAmount==""){
            $scope.info.twoRepeatRewardAgentAmount="0";
        }

        if(!$scope.checkInputDay($scope.info.oneLimitDays)
        ||!$scope.checkInputDay($scope.info.twoLimitDays)
        ||!$scope.checkInputDay($scope.info.threeLimitDays)
            ||!$scope.checkInputAmount($scope.info.oneTransAmount)
            ||!$scope.checkInputAmount($scope.info.oneRewardMerAmount)
            ||!$scope.checkInputAmount($scope.info.oneRepeatRewardMerAmount)
            ||!$scope.checkInputAmount($scope.info.oneRewardAgentAmount)
            ||!$scope.checkInputAmount($scope.info.oneRepeatRewardAgentAmount)
            ||!$scope.checkInputAmount($scope.info.twoTransAmount)
            ||!$scope.checkInputAmount($scope.info.twoRewardAgentAmount)
            ||!$scope.checkInputAmount($scope.info.twoRepeatRewardAgentAmount)
            ||!$scope.checkInputAmount($scope.info.threeTransAmount)
            ||!$scope.checkInputAmount($scope.info.threeRewardAgentAmount)
            ||!$scope.checkInputAmount($scope.info.threeRepeatRewardAgentAmount)

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
            $http.post("activity/addXhlfSmart",angular.toJson(data))
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
            $http.post("activity/editXhlfSmart",angular.toJson(data))
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
        $http.post("activity/selectXhlfSmartById",angular.toJson(id)).success(function (data) {
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
        $http.post("activity/selectXhlfSmartById",angular.toJson(id)).success(function (data) {
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
                title: "您正在进行删除操作，删除后将不可恢复，是否继续操作？",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post("activity/deleteXhlfSmart",id)
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
