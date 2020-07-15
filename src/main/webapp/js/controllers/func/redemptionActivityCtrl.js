/**
 * 兑奖活动详情
 */
angular.module('inspinia',['uiSwitch']).controller('redemptionActivityCtrl',function(i18nService,$scope,$http,$state,$stateParams,$compile,$filter,SweetAlert,$document){
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    //数据源
    $scope.statuses=[{text:"全部",value:null},{text:"未兑奖",value:0},{text:"已兑奖",value:1},{text:"已过期",value:2},{text:"已作废",value:3}];
    $scope.awardTypes=[{text:"全部",value:null},{text:"鼓励金",value:1},{text:"超级积分",value:2},{text:"现金券",value:3}];
    $scope.redemptionList=[];
    $scope.infoAdd={};
    $scope.submitting = false;
    //clear
    $scope.clear=function(){
        $scope.info={excCode:"",status:null,awardType:null,
            startTime:"",
            endTime:"",
            awardStartTime:"",awardEndTime:"",createStartTime:"",createEndTime:""};
    }
    $scope.clear();

    $http.post("redemption/queryRedemptionManageList","info="+angular.toJson($scope.info),
        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function(result){
            if(!result)
                return;
            angular.forEach(result.list,function(data){
                $scope.redemptionList.push({text:data.awardName,value:""+data.id});
            });
        })

    $scope.servicesGrid = {                  //配置表格
        data: 'result',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                        //表格数据
            { field: 'id',displayName:'序列',width:60},
            { field: 'excCode',displayName:'编号',width:180},
            { field: 'status',displayName:'状态',width:180,cellFilter:"formatDropping:" +  angular.toJson($scope.statuses)},
            { field: 'awardName',displayName:'奖券名称',width:180},
            { field: 'awardType',displayName:'类型',width:180,cellFilter:"formatDropping:" +  angular.toJson($scope.awardTypes)},
            { field: 'offBeginTime',displayName:'有效期开始时间',width:180,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
            { field: 'offEndTime',displayName:'有效期结束时间',width:180,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
            { field: 'awardTime',displayName:'使用时间',width:180,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
            { field: 'createTime',displayName:'生成时间',width:180,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
            { field: 'action',displayName:'操作',width:150,pinnedRight:true,editable:true,cellTemplate:
                '<span><a  class="lh30" ng-show="row.entity.status==0 && grid.appScope.hasPermit(\'redemption.invalidRedemptionActivity\')" ng-click="grid.appScope.invalidRedemption(row.entity.id)">作废</a><span>'
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
        $http.post("redemption/queryRedemptionList","info="+angular.toJson($scope.info)+"&pageNo="+
            $scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.result=data.page.result;
                    $scope.servicesGrid.totalItems = data.page.totalCount;
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
    $scope.query();

    $scope.addButchModel=function () {
        $scope.infoAdd.awardsConfigId=null;
        $("#addButchModel").modal("show");
    }

    $scope.cancelAddButchModel=function () {
        $('#addButchModel').modal('hide');
    }

    $scope.addRedemption = function(){
        var isNum=/^[1-9]\d*$/;

        if($scope.infoAdd.num==null || $scope.infoAdd.num===""){
            $scope.notice("生成兑奖券数量不能为空!");
            return;
        }
        if(!isNum.test($scope.infoAdd.num)){
            $scope.notice("生成兑奖券数量格式不正确!");
            return;
        }
        if($scope.infoAdd.awardsConfigId==null || $scope.infoAdd.awardsConfigId===""){
            $scope.notice("奖券名称不能为空!");
            return;
        }
        if($scope.infoAdd.day==null || $scope.infoAdd.day===""){
            $scope.notice("有效期不能为空!");
            return;
        }
        if(!isNum.test($scope.infoAdd.day)){
            $scope.notice("有效期格式不正确!");
            return;
        }

        if ($scope.submitting == true) {
            return;
        }
        $scope.submitting = true;
        var data = {"info" : $scope.infoAdd};
        $http.post("redemption/addRedemption",angular.toJson(data))
            .success(function(data){
                if(data.status){
                    $scope.notice(data.msg);
                    $scope.cancelAddButchModel();
                    $scope.query();
                }else{
                    $scope.notice(data.msg);
                }
                $scope.submitting = false;
            })
            .error(function(){
                $scope.submitting = false;
            });
    }

    $scope.invalidRedemption=function(id){
        SweetAlert.swal({
                title: "确认作废?",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post("redemption/invalidRedemption","id="+id,
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

    $scope.export = function () {
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
                    location.href="redemption/exportRedemption?param="+angular.toJson($scope.info);
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
});

