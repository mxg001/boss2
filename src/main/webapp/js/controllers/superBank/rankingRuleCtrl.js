/**
 * 排行榜
 */
angular.module('inspinia',['uiSwitch']).controller('rankingRuleCtrl',function($scope,$http,i18nService,$document,SweetAlert){
    //数据源
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    $scope.statusList = [{text:"全部",value:""},{text:"关闭",value:"0"},{text:"打开",value:"1"}];

    $scope.ruleTypeList = [{text:"全部",value:""},{text:"周榜",value:"0"},{text:"月榜",value:"1"},{text:"年榜",value:"2"}];
    
    $scope.rankingBy = '[{text:"收益金额",value:"0"},{text:"会员数",value:"1"},{text:"用户数",value:"2"},{text:"订单数",value:"3"}]';
    
    $scope.resetForm = function () {
        $scope.baseInfo = {ruleCode:"",ruleType:"",status:"",openOrg:""};
    };
    $scope.resetForm();

    $scope.columnDefs = [
        {field: 'ruleCode',displayName: '排行榜规则编号',width: 150,pinnable: false,sortable: false},
        {field: 'ruleName',displayName: '排行榜名称',width: 150,pinnable: false,sortable: false},
        {field: 'ruleType',displayName: '统计周期',width: 150,pinnable: false,sortable: false,cellTemplate:'<div class="lh30" ng-show="row.entity.ruleType==0">周榜</div><div class="lh30" ng-show="row.entity.ruleType==1">月榜</div><div class="lh30" ng-show="row.entity.ruleType==2">年榜</div>'},
        {field: 'dataType',displayName: '统计数据',width: 150,pinnable: false,sortable: false,cellFilter:"formatDropping:" + $scope.rankingBy},
        {field: 'status',displayName: '活动开关',width:150,pinnable: false,sortable: false,cellTemplate:
            '<switch class="switch switch-s" ng-model="row.entity.statusInt" ng-change="grid.appScope.switchStatus(row)" />'//</span>'
            //+'<span ng-show="row.entity.statusInt==1">打开</span><span ng-show="row.entity.statusInt==0">关闭</span></span>'
        },
        {field: 'showOrderNo',displayName: '显示顺序',width: 150,pinnable: false,sortable: false},
       /* {field: 'advertOrderNo',displayName: '广告位置',width: 150,pinnable: false,sortable: false},*/
        {field: 'advertUrl',displayName: '广告图片',width: 150,pinnable: false,sortable: false,
            cellTemplate:'<img style="width: 140px; height: 36px;" ng-show="row.entity.advertUrl" ng-src="{{row.entity.advertUrl}}" />'},
        {field: 'openOrg',displayName: '开通组织',width: 180,pinnable: false,sortable: false},
       /* {field: 'totalAmount',displayName: '奖金总额',width: 120,pinnable: false,sortable: false,cellFilter:'currency:""'},*/
        {field: 'startTime',displayName: '活动开始时间',width: 180,pinnable: false,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'endTime',displayName: '活动截止时间',width: 180,pinnable: false,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'updateTime',displayName: '修改时间',width: 180,pinnable: false,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'action',displayName: '操作',width: 150,pinnedRight:true,sortable: false,editable:true,cellTemplate:
            '<a class="lh30" '
            + 'ui-sref="superBank.rankingRuleLevel({id:row.entity.id,operate:2})">详情</a>'
            +'<a class="lh30" ui-sref="superBank.rankingRuleLevel({id:row.entity.id,operate:1})"> | 修改</a>'
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

    $scope.query = function () {
        $scope.submitting = true;
        $scope.loadImg = true;
        $http({
            url: 'superBank/rankingRuleList?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
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

    //修改上架开关
    $scope.switchStatus=function(row){
        if(row.entity.status){
            $scope.serviceText = "确定打开";
        } else {
            $scope.serviceText = "确定关闭";
        }
        SweetAlert.swal({
                title: "",
                text: $scope.serviceText,
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    if(row.entity.statusInt==true){
                        row.entity.status='1';
                    } else if(row.entity.statusInt==false){
                        row.entity.status='0';
                    }
                    var data={"status":row.entity.status,"id":row.entity.id,"openTime":row.entity.openTime};
                    $http.post("superBank/updateRankingRuleStatus",angular.toJson(data))
                        .success(function(data){
                            $scope.notice(data.msg);
                            if(data.status){
                                $scope.query();
                            }else{
                                row.entity.status = !row.entity.status;
                            }
                        })
                        .error(function(data){
                            row.entity.status = !row.entity.status;
                            $scope.notice("服务器异常");
                        });
                } else {
                    row.entity.statusInt = !row.entity.status;
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