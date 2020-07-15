/**
 * 黑名单资料管理
 */
angular.module('inspinia',['uiSwitch']).controller('blackDataQueryCtrl',function($scope, $http, $state,$interval, $stateParams, i18nService,$filter,SweetAlert,$document,$location,$window) {
    i18nService.setCurrentLang('zh-cn');

    $scope.paginationOptions = {pageNo : 1,pageSize : 10};
    $scope.info = {};

    //处理状态
    $scope.dealStatuses = [{text:"未处理",value:'0'},{text:"已处理",value:'1'}, {text:"解冻",value:'2'}];
    //推广来源
    $scope.sources = [{text:"正常",value:'0'},{text:"超级推",value:'1'}, {text:"代理商",value:'2'}, {text:"人人代理",value:'3'}];
    //商户回复状态
    $scope.merReplyStatus = [{text:"初始化",value:'0'},{text:"未回复",value:'1'}, {text:"已回复",value:'2'}];
    $scope.haveHis = [{text:"否",value:'0'},{text:"是",value:'1'}];
    $scope.teamList = [];
    $scope.remark={id:'', riskLastRemark:''};

    $scope.selectTeamList = function () {
        $http.get("blackData/selectTeamList").success(
            function (data) {
                $scope.teamList = data.teamList;
            });
    }
    $scope.selectTeamList();


    $scope.rulesList = {};
    $scope.getRulesList = function () {
        $http.get("riskRulesAction/selectAllWithOutStatus").success(
            function (data) {
                $scope.rulesList = data.rulesList;
        });
    }
    $scope.getRulesList();

    $scope.query = function () {
        $http({
            url: 'blackData/selectByParam?pageNo='+$scope.paginationOptions.pageNo+
            '&pageSize='+$scope.paginationOptions.pageSize,
            data: $scope.info,
            method:'POST'
        }).success(function (result) {
            if (!result.status){
                $scope.notice(result.msg);
                return;
            }
            $scope.myGrid.data = result.page.result;
            $scope.myGrid.totalItems = result.page.totalCount;
        }).error(function (result) {
            $scope.notice('服务器异常,请稍后再试.');
        });
    };

    $scope.columnDefs = [
        {field: 'merchantNo',displayName: '商户编号',width: 150,pinnable: false,sortable: false},
        {field: 'merchantName',displayName: '商户名称',width: 220,pinnable: false,sortable: false},
        {field: 'lawyer',displayName: '商户姓名',width: 120,pinnable: false,sortable: false},
        {field: 'merchantPhone',displayName: '商户手机号',width: 120,pinnable: false,sortable: false},
        {field: 'agentNo',displayName: '代理商编号',width: 120,pinnable: false,sortable: false},
        {field: 'agentName',displayName: '代理商名称',width: 120,pinnable: false,sortable: false},
        {field: 'merRiskRulesNo',displayName: '触发规则编号',width: 150,pinnable: false,sortable: false},
        {field: 'blackCreator',displayName: '冻结记录创建人',width: 150,pinnable: false,sortable: false},
        {field: 'teamName',displayName: '组织名称',width: 150,pinnable: false,sortable: false},
        {field: 'recommendedSource',displayName: '推广来源',width: 120,pinnable: false,sortable: false,
            cellFilter:"formatDropping:"+ angular.toJson($scope.sources)},
        {field: 'merLastDealStatus',displayName: '商户回复状态',width: 150,pinnable: false,sortable: false,
            cellFilter:"formatDropping:"+ angular.toJson($scope.merReplyStatus)},
        {field: 'merLastDealTime',displayName: '商户回复时间',width: 150,pinnable: false,sortable: false,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'riskLastDealStatus',displayName: '处理状态',width: 150,pinnable: false,sortable: false,
            cellFilter:"formatDropping:"+ angular.toJson($scope.dealStatuses)},
        {field: 'riskLastDealOperator',displayName: '处理人',width: 120,pinnable: false,sortable: false},
        {field: 'riskLastDealTime',displayName: '处理时间',width: 150,pinnable: false,sortable: false,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'haveTriggerHis',displayName: '历史触发记录',width: 150,pinnable: false,sortable: false,
            cellFilter:"formatDropping:"+ angular.toJson($scope.haveHis)},
        {field: 'blackCreateRemark',displayName: '黑名单备注',width: 120,pinnable: false,sortable: false},
        {field: 'createTime',displayName: '创建时间',width: 150,pinnable: false,sortable: false,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'riskLastRemark',displayName: '备注',width: 120,pinnable: false,sortable: false},
        {field: 'id',displayName: '操作',width: 300,pinnedRight: true,pinnable: false,sortable: false,editable:true,cellTemplate:
            "<div  class='lh30'>" +
            "<a ng-show='grid.appScope.hasPermit(\"blackData.deal\")&&row.entity.riskLastDealStatus!=2'  ui-sref='risk.blackDataInfo({id:row.entity.id,type:2})'> 处理 </a>" +
            "<a ng-show='grid.appScope.hasPermit(\"blackData.info\")'  ui-sref='risk.blackDataInfoTwo({id:row.entity.id,type:1})'> 查看 </a>" +
            "<a ui-sref='merchant.queryMerDetail({mertId:row.entity.merBusinessProId})' target='_blank'> 商户详情 </a>" +
            "<a ng-show='grid.appScope.hasPermit(\"blackData.tradeQuery\")' ui-sref='trade.tradeQuery' target='_blank'> 交易导出 </a>" +
            "<a ng-show='grid.appScope.hasPermit(\"blackData.remark\")'  ng-click='grid.appScope.openRemarkModal(row.entity.id)&&row.entity.riskLastDealStatus!=2'> 备注 </a>" +
            "</div>"
        }
    ];


    $scope.myGrid = {
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
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
        }
    };

    $scope.reset = function () {
        $scope.info={};
    }

    $scope.export = function () {
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
                    if($scope.myGrid.data==null || $scope.myGrid.data.length==0){
                        $scope.notice("没有可导出的数据");
                        return;
                    } else {
                        location.href="blackData/export?info="+encodeURIComponent(angular.toJson($scope.info));
                    }
                }
            });
    }

    $scope.openRemarkModal = function(id){
        $('#remarkModal').modal('show');
        $scope.remark.id = id;
    };

    $scope.merDetail = function (merchantNo) {
        $http.get('blackData/queryMbpId?merchantNo='+merchantNo).success(function (result) {
            var url = "/merchant/queryMerDetail/"+result.mbpId;
            $location.url(url);
        });
    }

    $scope.addRemark = function () {
        if(isBlank($scope.remark.id)){
            return;
        }

        $http({
            url: 'blackData/addRemark',
            data: $scope.remark,
            method:'POST'
        }).success(function (result) {
            $scope.closeRemarkModal();
            $scope.query();
        }).error(function (result) {
            $scope.notice('服务器异常,请稍后再试.');
        });

    }

    $scope.closeRemarkModal = function(){
        $('#remarkModal').modal('hide');
        $scope.remark.riskLastRemark="";
    };


    //参数非空判断
    isBlank = function (param) {
        if(param=="" || param==null ){
            return true;
        }else{
            return false;
        }
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
