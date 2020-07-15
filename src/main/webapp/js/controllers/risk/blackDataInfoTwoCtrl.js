/**
 * 黑名单资料详情
 */
angular.module('inspinia').controller('blackDataInfoTwoCtrl',function($scope,$http,i18nService,$state,$stateParams,$location){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文

    $scope.info = {};
    $scope.deal = {};
    $scope.dealRecord = {};
    $scope.replyRecord = {};
    //处理状态
    $scope.dealStatuses = [{text:"未处理",value:'0'},{text:"已处理",value:'1'}, {text:"解冻",value:'2'}];
    //推广来源
    $scope.sources = [{text:"正常",value:'0'},{text:"超级推",value:'1'}, {text:"代理商",value:'2'}, {text:"人人代理",value:'3'}];
    //商户回复状态
    $scope.merReplyStatus = [{text:"初始化",value:'0'},{text:"未处理",value:'1'}, {text:"已处理",value:'2'}];
    $scope.haveHis = [{text:"否",value:'0'},{text:"是",value:'1'}];
    //日志操作类型
    $scope.operateTypes = [{text:"添加黑名单",value:'0'},{text:"回复",value:'1'}, {text:"添加备注",value:'2'}];
    $scope.type = $stateParams.type;

    //风控处理页面是否显示
    if($stateParams.type == 2){
        $scope.replyHtmlFlag = true;
        $scope.replyButton = false;
    }else {
        $scope.replyHtmlFlag = false;
        $scope.replyButton = true;
    }
    $scope.logFlag = ($stateParams.type==1?true:false);

    $scope.getBlackDataInfo = function () {
        $http({
            url: 'blackData/getDetailTwo?id='+$stateParams.id,
            method:'POST'
        }).success(function (result) {
            $scope.info = result.info;
            $scope.dealRecord = result.dealRecord;
            $scope.replyRecord = result.replyRecord;
            $scope.recordList = result.recordList;
            $scope.deal = result.deal;
            $scope.dealReplyRecords = result.dealReplyRecords;
            $scope.logGrid.data = result.logs;
        }).error(function (result) {
            $scope.notice('服务器异常,请稍后再试.');
        });
    }

    /**
     * 获取敏感数据
     */
    $scope.dataSta=true;
    $scope.getDataProcessing = function () {
        if($scope.dataSta){
            $http.post("blackData/getDataProcessing","id="+$stateParams.id,
                {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                .success(function(data) {
                    $scope.info.merchantPhone = data.info.merchantPhone;
                    $scope.info.merchantIdCard = data.info.merchantIdCard;
                    $scope.dataSta=false;
                });
        }
    };
    $scope.logGrid={                           //配置表格
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[
            //表格数据
            { field: 'index',displayName:'序号',width:80,cellTemplate:"<span>{{rowRenderIndex+1}}</span>"},
            { field: 'operateType',displayName:'操作名称',cellFilter:"formatDropping:"+angular.toJson($scope.operateTypes),width:150},
            { field: 'operateTime',displayName:'操作时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'operator',displayName:'操作人',width:150}
        ],
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
        }
    };

    $scope.templateList = {};
    $scope.getTemplateList = function () {
        $http.get("dealTemplate/selectAll").success(
            function (data) {
                $scope.templateList = data.templateList;
            });
    }

    $scope.showMsg = function () {
        var templates = $scope.templateList;
        for(var i in templates){
            if(templates[i].templateNo == $scope.deal.riskDealTemplateNo){
                $scope.deal.riskDealMsg = templates[i].templateContent;
                if(templates[i].templateNo == 103){
                    var temp = templates[i].templateContent.toString().format($scope.info.bankName,$scope.info.accountNo,$scope.info.accountNo);
                    $scope.deal.riskDealMsg = temp.replace(/null/g, "  ");
                }
                break;
            }
        }

    }

    String.prototype.format=function() {
        if(arguments.length==0) return this;
        for(var s=this, i=0; i<arguments.length; i++)
            s=s.replace(new RegExp("\\{"+i+"\\}","g"), arguments[i]);
        return s;
    };

    $scope.submit = function () {
        $scope.submitting = true;
        if(isBlank($scope.deal.riskDealTemplateNo)){
            $scope.notice("必要参数不能为空");
            $scope.submitting = false;
            return;
        }
        $scope.deal.merchantNo = $scope.info.merchantNo;
        var type = 1;
        //提交
        if(isBlank($scope.deal.origOrderNo)){
            $scope.deal.origOrderNo = $scope.info.orderNo;
        }else{//修改
            type = 2;
        }
        $http({
            url: 'blackData/deal?type='+type,
            data: $scope.deal,
            method:'POST'
        }).success(function (result) {
            if (result.status){
                $scope.notice("提交成功");
                //$scope.getBlackDataInfo();
                $location.url("/risk/blackDataQuery");

            }else {
                $scope.notice(result.msg);
            }
            $scope.submitting = false;
        }).error(function (result) {
            $scope.notice('服务器异常,请稍后再试.');
            $scope.submitting = false;
        });

    }


    $scope.showReplyHtml = function () {
        $scope.replyHtmlFlag = true;
        $scope.replyButton = false;
        $scope.logFlag = false;
        //$scope.getTemplateList("");
    }

    //参数非空判断
    isBlank = function (param) {
        if(param=="" || param==null ){
            return true;
        }else{
            return false;
        }
    }

    $scope.openReplyModal = function(){
        $('#replyModal').modal('show');
    };
    $scope.closeReplyModal = function(){
        $('#replyModal').modal('hide');
    };



});