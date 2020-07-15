/**
 * 新版超级推商户查询
 */
angular.module('inspinia',['infinity.angular-chosen']).controller("queryCjtMerchantInfo", function($scope, $http, i18nService,$document,$timeout) {
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    //清空
    $scope.terBindStatusList = [{text:"未绑定",value:"0"},{text:"已绑定",value:"1"}];
    $scope.terApplyStatusList = [{text:"未申领",value:"0"},{text:"已申领",value:"1"}];
    $scope.activityStatusList = [{text:"未达标",value:"0"},{text:"已达标",value:"1"}];

    $scope.terBindStatusListAll = angular.copy($scope.terBindStatusList);
    $scope.terBindStatusListAll.unshift({text:"全部",value:null});
    $scope.terApplyStatusListAll = angular.copy($scope.terApplyStatusList);
    $scope.terApplyStatusListAll.unshift({text:"全部",value:null});
    $scope.activityStatusListAll = angular.copy($scope.activityStatusList);
    $scope.activityStatusListAll.unshift({text:"全部",value:null});

    $scope.resetForm=function(){
        $scope.baseInfo = {
            terBindStatus:null,terApplyStatus:null,activityStatus:null,agentNode:"",oneAgentNo:"",
            createTimeStart:moment(new Date().getTime() - 24 * 3600 * 1000).format('YYYY-MM-DD'+' 00:00:00'),
            createTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD'+' 23:59:59')};
    }
    $scope.resetForm();

    //查询
    $scope.query = function(){
        $scope.submitting = true;
        $scope.loadImg = true;
        $http({
            url:"cjtMerchantInfo/selectPage?pageNo=" + $scope.paginationOptions.pageNo +  "&pageSize=" + $scope.paginationOptions.pageSize,
            method:"post",
            data:$scope.baseInfo
        }).success(function(result){
            $scope.submitting = false;
            $scope.loadImg = false;
            if (!result || !result.status){
                $scope.notice (result.msg);
                return;
            }
            $scope.cjtMerchantInfoGrid.data = result.data.page.result;
            $scope.cjtMerchantInfoGrid.totalItems = result.data.page.totalCount;
            $scope.totalMap = result.data.totalMap;
        }).error(function(){
            $scope.submitting = false;
            $scope.loadImg = false;
            $scope.notice("服务器异常");
        });
    };
    // $scope.query();

    $scope.columnDefs = [
                    {field: 'merchantName',width:150,displayName: '商户名称'},
                    {field: 'merchantNo',width:150,displayName: '商户编号'},
                    {field: 'mobilephone',width:150,displayName: '手机号'},
                    {field: 'createTime',width:150,displayName: '注册时间',cellFilter:"date:'yyyy-MM-dd HH:mm:ss'"},
                    {field: 'terApplyStatus',width:150,displayName: '机具申领状态',cellFilter:"formatDropping:" + angular.toJson($scope.terApplyStatusList)},
                    {field: 'terBindStatus',width:150,displayName: '机具绑定状态',cellFilter:"formatDropping:" + angular.toJson($scope.terBindStatusList)},
                    {field: 'activityStatus',width:150,displayName: '活动补贴达标状态',cellFilter:"formatDropping:" + angular.toJson($scope.activityStatusList)},
                    {field: 'agentName',width:150,displayName: '直属代理商名称'},
                    {field: 'agentNo',width:150,displayName: '直属代理商编号'},
                    {field: 'oneAgentNo',width:150,displayName: '一级代理商编号'},
                    {field: 'oneMerchantNo',width:150,displayName: '上一级商户编号'},
                    {field: 'twoMerchantNo',width:150,displayName: '上二级商户编号'},
                    {field: 'profitAmount',width:150,displayName: '累计收益'},
                    {field: 'haveAmount',width:200,displayName: '起止日期内刷卡交易总金额（元）'},
                    {field: 'targetAmount',width:180,displayName: '活动补贴达标标准（元）'},
                    {field: 'startTime',width:180,displayName: '活动补贴达标起始日期',cellFilter:"date:'yyyy-MM-dd HH:mm:ss'"},
                    {field: 'endTime',width:180,displayName: '活动补贴达标截止日期',cellFilter:"date:'yyyy-MM-dd HH:mm:ss'"},
                    {field: 'targetTime',width:180,displayName: '活动补贴达标时间',cellFilter:"date:'yyyy-MM-dd HH:mm:ss'"},
                {field: 'action',width:150,displayName: '操作',pinnedRight:true,sortable: false,editable:true,cellTemplate:
            '<a class="lh30" target="_blank" ng-show="grid.appScope.hasPermit(\'cjtMerchantInfo.detail\')" '
            + 'ui-sref="cjt.detailCjtMerchantInfo({merchantNo:row.entity.merchantNo})">商户详情</a>'
            +'<a class="lh30" target="_blank"  ng-show="grid.appScope.hasPermit(\'cjtMerchantInfo.accountDetail\')" '
            + 'ui-sref="cjt.accountDetail({merchantNo:row.entity.merchantNo})"> 提现详情</a>'}
    ];

    $scope.cjtMerchantInfoGrid = {
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs: $scope.columnDefs,
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            $scope.gridApi.pagination.on.paginationChanged ($scope, function(newPage, pageSize) {
                $scope.paginationOptions.pageNo = newPage;
                $scope.paginationOptions.pageSize = pageSize;
                $scope.query();
            });
        }
    };

    //获取所有的代理商
    $scope.oneAgentList=[{value:"",text:"全部"}];
    //代理商
    $scope.agentList=[{value:"",text:"全部"}];
    $http.post("agentInfo/selectAllOneInfo")
        .success(function(msg){
            //响应成功
            for(var i=0; i<msg.length; i++){
                $scope.oneAgentList.push({value:msg[i].agentNo,text:msg[i].agentNo + " " + msg[i].agentName});
            }
        });
    $http.post("agentInfo/selectAllInfo")
        .success(function(msg){
            //响应成功
            for(var i=0; i<msg.length; i++){
                $scope.agentList.push({value:msg[i].agentNode,text:msg[i].agentNo + " " + msg[i].agentName});
            }
        });

    //异步获取直属代理商
    var oldValue="";
    var timeout="";
    $scope.getAgentList = function(value) {
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
                            $scope.agentList = $scope.agentt;
                            oldValue = value;
                        });
                },800);
        }
    };
    //异步获取一级代理商
    var oneOldValue="";
    var oneTimeout="";
    $scope.getOneAgentList = function(value) {
        $scope.agentt = [];
        var newValue=value;
        if(newValue != oneOldValue){
            if (oneTimeout) $timeout.cancel(oneTimeout);
            oneTimeout = $timeout(
                function(){
                    $http.post('agentInfo/selectAllOneInfo','item=' + value,
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .then(function (response) {
                            if(response.data.length==0) {
                                $scope.agentt.push({value: "", text: "全部"});
                            }else{
                                $scope.agentt.push({value: "", text: "全部"});
                                for(var i=0; i<response.data.length; i++){
                                    $scope.agentt.push({value:response.data[i].agentNo,text:response.data[i].agentNo + " " + response.data[i].agentName});
                                }
                            }
                            $scope.oneAgentList = $scope.agentt;
                            oneOldValue = value;
                        });
                },800);
        }
    };



    //页面绑定回车事件
    $document.bind ("keypress", function(event) {
        $scope.$apply(function (){
            if(event.keyCode == 13){
                $scope.query();
            }
        })
    });

});