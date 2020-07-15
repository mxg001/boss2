/**
 * 超级返活动查询
 */
angular.module('inspinia',['angularFileUpload','infinity.angular-chosen']).controller('superHappyBackQueryCtrl',function($scope,$http,$state,$stateParams,i18nService,$document,SweetAlert,FileUploader,$timeout){
    //数据源
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    $scope.baseInfo = {activeOrder:"",currentCycle:"",currentTargetStatus:"",activityTargetStatus:"",containsLower:"0",merchantNo:"",agentNo:"",subType:"3"};
    $scope.agentGrades = [{text:'包含所有',value:''},{text:'不包含',value:'1'},{text:'仅包含直属',value:'2'}];
    $scope.currentCycleList = [{text:'全部',value:''},{text:'第一次考核奖励',value:'1'},{text:'第一次奖励考核子考核',value:'1.1'},{text:'第二次考核奖励',value:'2'},{text:'第三次考核奖励',value:'3'},{text:'第四次考核奖励',value:'4'}];
    $scope.currentTargetStatusList = [{text:'全部',value:''},{text:'未开始',value:'0'},{text:'考核中',value:'1'},{text:'已达标',value:'2'},{text:'未达标',value:'3'}];
    $scope.activityTargetStatusList = [{text:'全部',value:''},{text:'考核中',value:'0'},{text:'已达标',value:'1'},{text:'未达标',value:'2'}];
    $scope.agentNoList = [];

    $scope.totalData = {
        totalReward: '0.00',
        totalNoReward: '0.00'
    };
    //代理商
    $http.post("agentInfo/selectAllInfo")
        .success(function(msg){
            //响应成功
            for(var i=0; i<msg.length; i++){
                $scope.agent.push({value:msg[i].agentNode,text:msg[i].agentNo + " " + msg[i].agentName});
                $scope.oneAgent.push({value:msg[i].agentNo,text:msg[i].agentNo + " " + msg[i].agentName});
            }
        });
    //动态代理商
    $scope.agent=[{value:"",text:"全部"}];
    $scope.getStates =getStates;
    var oldValue="";
    var timeout="";
    function getStates(value) {
        $scope.agentt = [];
        var newValue=value;
        if(newValue != oldValue){
            if (timeout) $timeout.cancel(timeout);
            timeout = $timeout(
                function(){
                    $http.post('agentInfo/selectAllInfo','item=' + value,
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .then(function (response) {
                            if(response.data.length==0) {
                                $scope.agentt.push({value: "", text: "全部"});
                            }else{
                                $scope.agentt.push({value: "", text: "全部"});
                                for(var i=0; i<response.data.length; i++){
                                    $scope.agentt.push({value:response.data[i].agentNode,text:response.data[i].agentNo + " " + response.data[i].agentName});
                                }
                            }
                            $scope.agent = $scope.agentt;
                            oldValue = value;
                        });
                },800);
        }
    };

    //条件查询一级代理商
    $scope.oneAgent=[{value:"",text:"全部"}];
    $scope.getStatesOne =getStatesOne;
    var oldValueOne="";
    var timeoutOne="";
    function getStatesOne(value) {
        $scope.agenttOne = [];
        var newValueOne=value;
        if(newValueOne != oldValueOne){
            if (timeoutOne) $timeout.cancel(timeoutOne);
            timeoutOne = $timeout(
                function(){
                    $http.post('agentInfo/selectAllInfo','item=' + value,
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .then(function (response) {
                            if(response.data.length==0) {
                                $scope.agenttOne.push({value: "", text: "全部"});
                            }else{
                                $scope.agenttOne.push({value: "", text: "全部"});
                                for(var i=0; i<response.data.length; i++){
                                    $scope.agenttOne.push({value:response.data[i].agentNo,text:response.data[i].agentNo + " " + response.data[i].agentName});
                                }
                            }
                            $scope.oneAgent = $scope.agenttOne;
                            oldValueOne = value;
                        });
                },800);
        }
    };

    //代理商名称/编号  改变时调用
    $scope.changeAgentNode = function () {
        $scope.disabledMerchantType = !$scope.baseInfo.agentNo;
    };


    //查询
    $scope.query=function(){
        $http({
            url: 'activityDetail/newHappyBackCount',
            method: 'POST',
            data: $scope.baseInfo
        }).success(function(data){
            if(!data.status){
                $scope.notice("没有查询到数据");
                return;
            }
            $scope.totalData = data.data;
            $scope.baseInfo.pageNo=$scope.paginationOptions.pageNo;
            $scope.baseInfo.pageSize=$scope.paginationOptions.pageSize;

            $http({
                url: 'activityDetail/newHappyBackQuery',
                method: 'POST',
                data: $scope.baseInfo
            }).success(function(data){
                if(!data.status){
                    $scope.notice("没有查询到数据");
                    return;
                }
                $scope.activityGrid.data =data.data.list;
                $scope.activityGrid.totalItems = data.data.totalCount;
            })
        });
    };
    $scope.columnDefs = [
        {field: 'activeOrder',displayName: '激活流水号',pinnable: false,width: 180,sortable: false},
        {field: 'activeTime',displayName: '激活时间',pinnable: false,width: 180,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'targetAmount',displayName: '达标条件(元)',pinnable: false,width: 150,sortable: false,headerTooltip: '达标条件指商户绑定信用卡后，该卡累计mpos交易量需达到的金额',cellTemplate:
            '<div class="lh30">'
            +'累计交易≥<span ng-bind="row.entity.targetAmount"/>'
            +'</div>'},
        {field: 'agentTransTotalType',displayName: '累计交易量统计方式',pinnable: false,width: 180,sortable: false,cellFilter:"formatDropping:" +angular.toJson($scope.transTotalTypes)},
        {field: 'rewardAmount',displayName: '达标奖励金额(元)',pinnable: false,width: 150,sortable: false},
        {field: 'currentCycle',displayName: '考核周期',pinnable: false,width: 150,sortable: false,cellFilter:"formatDropping:" +angular.toJson($scope.currentCycleList)},
        {field: 'rewardStartTime',displayName: '考核开始日期',pinnable: false,width: 180,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'rewardEndTime',displayName: '考核结束日期',pinnable: false,width: 180,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'currentTargetStatus',displayName: '当前考核达标状态',pinnable: false,width: 180,sortable: false,cellFilter:"formatDropping:" +angular.toJson($scope.currentTargetStatusList)},
        {field: 'currentTargetTime',displayName: '当前考核达标日期',pinnable: false,width: 180,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'rewardAccountStatus',displayName: '奖励入账状态',pinnable: false,width: 180,sortable: false,cellFilter:"formatDropping:[{text:'未入账',value:'0'},{text:'已入账',value:'1'}]"},
        {field: 'rewardAccountTime',displayName: '奖励入账日期',pinnable: false,width: 180,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'activityTargetStatus',displayName: '活动达标状态',pinnable: false,width: 180,sortable: false,cellFilter:"formatDropping:" +angular.toJson($scope.activityTargetStatusList)},
        {field: 'activityTargetTime',displayName: '活动达标日期',pinnable: false,width: 180,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'activityTypeNo',displayName: '欢乐返子类型编号',pinnable: false,width: 150,sortable: false},
        {field: 'merchantNo',displayName: '所属商户编号',pinnable: false,width: 180,sortable: false},
        {field: 'teamName',displayName: '所属组织',pinnable: false,width: 150,sortable: false},
        {field: 'teamEntryName',displayName: '所属子组织',pinnable: false,width: 150,sortable: false},
        {field: 'hardId',displayName: '硬件产品ID',pinnable: false,width: 150,sortable: false},
        {field: 'agentName',displayName: '所属代理商名称',pinnable: false,width: 180,sortable: false},
        {field: 'agentNo',displayName: '所属代理商编号',pinnable: false,width: 180,sortable: false},
        {field: 'oneAgentName',displayName: '一级代理商名称',pinnable: false,width: 180,sortable: false},
        {field: 'oneAgentNo',displayName: '一级代理商编号',pinnable: false,width: 180,sortable: false},
        {field: 'action',displayName: '操作',width: 150,pinnable:false,sortable: false,pinnedRight:true,cellTemplate:
            '<div class="lh30">'
            +'<a class="lh30" '
            +'ng-show="grid.appScope.hasPermit(\'activity.agentAwardDetail\')"'
            +'ng-click="grid.appScope.agentAwardDetail(row.entity.id)" target="_black">代理商明细</a>'
            +'</div>'
        }
    ];


    $scope.agentAwardDetail=function(id){
        $http.post("activityDetail/agentAwardDetail",
            "id="+id,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(result){
                if (result.status) {
                    // 批量更改信息展示
                    $("#agentAwardDetailModel").modal("show");
                    $scope.agentAwardDetailList=result.data;
                }else {
                    $scope.notice(result.msg);
                }
            })

    }

    $scope.closeAgentAwardDetail=function(){
        $("#agentAwardDetailModel").modal("hide");
    }

    $scope.agentAwardDetailGrid = {
        data: 'agentAwardDetailList',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],      //切换每页记录数
        useExternalPagination: true,            //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,          //纵向滚动条
        columnDefs: [
            {field: 'agentName',displayName: '代理商名称',pinnable: false,width: 180,sortable: false},
            {field: 'agentNo',displayName: '代理商编号',pinnable: false,width: 180,sortable: false},
            {field: 'agentLevel',displayName: '代理商级别',pinnable: false,width: 180,sortable: false},
            {field: 'amount',displayName: '奖励金额',pinnable: false,width: 180,sortable: false},
            {field: 'accountStatus',displayName: '入账状态',pinnable: false,width: 180,sortable: false,width:80,cellFilter:"formatDropping:[{text:'未入账',value:'0'},{text:'已入账',value:'1'},{text:'',value:'-1'}]"},
            {field: 'accountTime',displayName: '入账日期',pinnable: false,width: 180,sortable: false,width:150,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
            {field: 'remark',displayName: '备注',pinnable: false,width: 180,sortable: false,width:300}
        ]

    };

    $scope.activityGrid = {
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs: $scope.columnDefs,
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            $scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.paginationOptions.pageNo = newPage;
                $scope.paginationOptions.pageSize = pageSize;
                $scope.query();
            });
            //行选中事件
            $scope.gridApi.selection.on.rowSelectionChanged($scope,function(row,event){
                if(row.isSelected){
                    $scope.baseInfo.pageAll=false;
                    $scope.baseInfo.countAll=false;
                }
            });
            //全选事件
            $scope.gridApi.selection.on.rowSelectionChangedBatch($scope,function(row,event){
                if(row){
                    if(row[0].isSelected){
                        $scope.baseInfo.pageAll=false;
                        $scope.baseInfo.countAll=false;
                    }
                }
            });

        }
    };
    $scope.query();
    //reset
    $scope.resetForm=function(){
        $scope.baseInfo = {
            activeOrder:"",
            currentCycle:"",
            currentTargetStatus:"",
            activityTargetStatus:"",
            containsLower:"",
            merchantNo:"",
            agentNo:"",
            subType:"3"
        };
    };
    $scope.resetForm();
    //导出
    $scope.exportExcel=function(){
        SweetAlert.swal({
                title: "确认导出？",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    if($scope.activityGrid.data==null || $scope.activityGrid.data.length==0){
                        $scope.notice("没有可导出的数据");
                        return;
                    } else {
                        $scope.baseInfo.pageNo=1;
                        $scope.baseInfo.pageSize=$scope.activityGrid.totalItems;
                        $scope.exportInfoClick("activityDetail/exportHappySendNew",{"baseInfo":angular.toJson($scope.baseInfo)});
                    }
                }
            });
    };

    var result=false;
    //批量奖励入账
    $scope.rewardIsBooked=function () {
        var ids = "";
        var selectList=$scope.gridApi.selection.getSelectedRows();
        if(selectList==null||selectList.length==0){
            $scope.notice("请选择需要奖励入账的记录");
            return false;
        }
        if(selectList!=null&&selectList.length>0){
            for(var i=0;i<selectList.length;i++){
                var item=selectList[i];
                if(item.rewardAccountStatus==0 && item.currentTargetStatus==2){
                    ids = ids + item.id + ",";
                }
            }
        }
        if(ids==""){
            $scope.notice("没有符合入账的记录");
            return false;
        }
        $scope.baseInfo.checkIds=ids.substring(0,ids.length-1);

        if(result){
            return;
        }
        result=true;
        SweetAlert.swal({
                title: "确认批量奖励入账？",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post('activityDetail/happySendNewAgentRewardIsBooked',"baseInfo="+angular.toJson($scope.baseInfo) + "&pageNo=" + $scope.paginationOptions.pageNo + "&pageSize="+$scope.paginationOptions.pageSize,
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function(msg){
                            $scope.notice(msg.msg);
                            if(msg.status){
                                $scope.cancel();
                                $scope.query();
                            }
                            result=false;
                        }).error(function(){
                        $scope.notice('系统异常');
                        result=false;
                    });
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