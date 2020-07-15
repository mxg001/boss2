/**
 * 调单详情
 */
angular.module('inspinia').controller('surveyOrderDetailCtrl',function($scope,$http,i18nService,$state,$stateParams){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文

    $scope.getOrderDetail=function () {
        $http.post("surveyOrder/getSurveyOrderDetail","id="+$stateParams.id,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.info=data.order;
                    $scope.result=$scope.info.logList;
                    if($scope.info.replyList.length>0){
                        $scope.replyTitle="回复记录,"+$scope.info.replyList.length+"条";
                    }
                }
            });
    };
    $scope.getOrderDetail();

    $scope.titleModal = function(errormsg){
        $scope.error=errormsg;
        $('#addBankModal').modal('show');
    };
    $scope.cancel = function(){
        $('#addBankModal').modal('hide');
    };

    //日志操作
    $scope.orderReplyResultSelect=[{text:"添加调单",value:"0"},{text:"回退",value:"1"},{text:"处理",value:"2"},
        {text:"催单",value:"3"},{text:"删除",value:"4"},{text:"添加扣款",value:"5"},
        {text:"标注上游扣款信息",value:"6"},{text:"标注上游下发信息",value:"7"},
        {text:"标注商户扣款信息",value:"8"},{text:"标注商户下发信息",value:"9"},
        {text:"标注代理商扣款信息",value:"10"},{text:"标注代理商下发信息",value:"11"},
        {text:"标注扣款处理状态",value:"12"},{text:"标注下发处理状态",value:"13"},
        {text:"上游备注",value:"14"}];
    $scope.orderReplyResultStr=angular.toJson($scope.orderReplyResultSelect);

    //回复状态
    $scope.replyStatusSelect=[{text:"未回复",value:"0"},{text:"未回复(下级已提交)",value:"1"},{text:"已回复",value:"2"},
        {text:"逾期未回复",value:"3"},{text:"逾期未回复(下级已提交)",value:"4"},{text:"逾期已回复",value:"5"},{text:"无需处理",value:"6"}];
    $scope.replyStatusStr=angular.toJson($scope.replyStatusSelect);

    //处理状态
    $scope.dealStatusSelect=[{text:"未处理",value:"0"},{text:"部分提供",value:"1"},{text:"持卡人承认交易",value:"2"},
        {text:"全部提供",value:"3"},{text:"无法提供",value:"4"},{text:"逾期部分提供",value:"5"},
        {text:"逾期全部提供",value:"6"},{text:"逾期未回",value:"7"},{text:"已回退",value:"8"},{text:"无需提交资料",value:"9"}];
    $scope.dealStatusStr=angular.toJson($scope.dealStatusSelect);

    //上游回复状态
    $scope.acqReplyStatusSelect=[{text:"全部",value:""},{text:"未回复",value:"0"},{text:"已回复",value:"1"}];
    $scope.acqReplyStatusStr = angular.toJson($scope.acqReplyStatusSelect);


    $scope.userGrid={                           //配置表格
        data: 'result',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'operateType',displayName:'操作名称',cellFilter:"formatDropping:"+$scope.orderReplyResultStr,width:150},
            { field: 'operateTime',displayName:'操作时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'operator',displayName:'操作人',width:150},
            { field: 'operateDetail',displayName:'备注',width:600}
        ],
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
        }
    };

    $scope.openReplyModal = function(){
        $('#replyModal').modal('show');
    };
    $scope.closeDeductModal = function(){
        $('#replyModal').modal('hide');
    };

    /**
     *下载文件
     */
    $scope.updateFile=function (name) {
        location.href="upload/updateFile?name="+name;
    };
});