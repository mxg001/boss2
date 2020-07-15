/**
 * 调单扣款明细查询
 */
angular.module('inspinia').controller('deductQueryCtrl',function($scope,$http,i18nService,$document,SweetAlert){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    //调单类型
    $scope.orderTypeCodeStr=angular.toJson($scope.orderTypeCodeList);

    //扣款处理状态
    $scope.agentDeductDealStatusSelect=[{text:"全部",value:""},{text:"未处理",value:"0"},{text:"已处理",value:"1"}];
    $scope.agentDeductDealStatusStr=angular.toJson($scope.agentDeductDealStatusSelect);

    //下发处理状态
    $scope.agentIssueDealStatusSelect=[{text:"全部",value:""},{text:"未处理",value:"0"},{text:"已处理",value:"1"}];
    $scope.agentIssueDealStatusStr=angular.toJson($scope.agentIssueDealStatusSelect);

    //清空
    $scope.clear=function(){
        $scope.info={agentDeductDealStatus:"",agentIssueDealStatus:"",orderTypeCode:""};
    };
    $scope.clear();


    $scope.userGrid={                           //配置表格
        data: 'result',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'orderNo',displayName:'调单号',width:180 },
            { field: 'transOrderNo',displayName:'订单编号',width:180,
                cellTemplate:'' +
                '<div ng-show="row.entity.transOrderNo!=null" style="margin-top:7px;"> ' +
                '<div ng-if="row.entity.orderServiceCode==\'3\'">'+
                '<a target="_blank" ui-sref="creditRepay.transactionDetail({orderNo:row.entity.transOrderNo})">{{row.entity.transOrderNo}}</a> ' +
                '</div>'+
                '<div ng-if="row.entity.orderServiceCode!=\'3\'&& row.entity.transOrderDatabase==\'now\'">'+
                '<a target="_blank" ui-sref="trade.tradeQueryDetail({id:row.entity.transOrderNo,val:0})">{{row.entity.transOrderNo}}</a> ' +
                '</div>'+
                '<div ng-if="row.entity.orderServiceCode!=\'3\'&& row.entity.transOrderDatabase==\'old\'">'+
                '<a target="_blank" ui-sref="histrade.histradeQueryDetail({id:row.entity.transOrderNo,val:0})">{{row.entity.transOrderNo}}</a> ' +
                '</div>'+
                '</div>'
            },
            { field: 'acqReferenceNo',displayName:'系统参考号',width:180 },
            { field: 'orderTypeCode',displayName:'调单类型',cellFilter:"formatDropping:"+$scope.orderTypeCodeStr,width:180},
            { field: 'merchantNo',displayName:'商户编号',width:180 },
            { field: 'transAmount',displayName:'交易金额',width:180,pinnable:false,sortable:false,cellFilter:"currency:''"},

            { field: 'acqDeductAmount',displayName:'上游扣款金额',width:180,pinnable:false,sortable:false,cellFilter:"currency:''"},
            { field: 'acqDeductTime',displayName:'上游扣款时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'acqDeductRemark',displayName:'上游扣款备注',width:300 },

            { field: 'merDeductAmount',displayName:'商户已扣款金额',width:180,pinnable:false,sortable:false,cellFilter:"currency:''"},
            { field: 'merDeductTime',displayName:'商户已扣款时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'agentRemainDeductAmount',displayName:'代理商待扣款金额',width:180,pinnable:false,sortable:false,cellFilter:"currency:''"},
            { field: 'merDeductRemark',displayName:'商户已扣款备注',width:300 },

            { field: 'agentHaveDeductAmount',displayName:'代理商已扣款金额',width:180,pinnable:false,sortable:false,cellFilter:"currency:''"},
            { field: 'agentDeductTime',displayName:'代理商已扣款时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'agentNeedDeductAmount',displayName:'代理商需扣款金额',width:180,pinnable:false,sortable:false,cellFilter:"currency:''"},
            { field: 'agentDeductRemark',displayName:'代理商已扣款备注',width:300 },

            { field: 'agentDeductDealStatus',displayName:'扣款处理状态',cellFilter:"formatDropping:"+$scope.agentDeductDealStatusStr,width:150},
            { field: 'agentDeductDealRemark',displayName:'财务扣款处理备注',width:300 },

            { field: 'acqIssueAmount',displayName:'上游下发金额',width:180,pinnable:false,sortable:false,cellFilter:"currency:''"},
            { field: 'acqIssueTime',displayName:'上游下发时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'acqIssueRemark',displayName:'上游下发备注',width:180 },

            { field: 'merIssueAmount',displayName:'商户已下发金额',width:180,pinnable:false,sortable:false,cellFilter:"currency:''"},
            { field: 'merIssueTime',displayName:'商户已下发时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'agentRemainIssueAmount',displayName:'代理商待下发金额',width:180,pinnable:false,sortable:false,cellFilter:"currency:''"},
            { field: 'merIssueRemark',displayName:'商户已下发备注',width:300 },

            { field: 'agentHaveIssueAmount',displayName:'代理商已下发金额',width:180,pinnable:false,sortable:false,cellFilter:"currency:''"},
            { field: 'agentIssueTime',displayName:'代理商已下发时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'agentNeedIssueAmount',displayName:'代理商需下发金额',width:180,pinnable:false,sortable:false,cellFilter:"currency:''"},
            { field: 'agentIssueRemark',displayName:'代理商已下发备注',width:300 },

            { field: 'agentIssueDealStatus',displayName:'下发处理状态',cellFilter:"formatDropping:"+$scope.agentIssueDealStatusStr,width:150},
            { field: 'agentIssueDealRemark',displayName:'财务处理下发备注',width:300 },
            { field: 'id',displayName:'操作',width:300,pinnedRight:true, cellTemplate:
            '<div class="lh30">'+
            '<a ng-show="grid.appScope.hasPermit(\'surveyDeduct.upperReaches\')" ng-click="grid.appScope.openAcqModal(row.entity.orderNo,1)">标注上游</a> ' +
            '<a ng-show="grid.appScope.hasPermit(\'surveyDeduct.merchant\')" ng-click="grid.appScope.openAcqModal(row.entity.orderNo,2)"> | 标注商户</a> ' +
            '<a ng-show="grid.appScope.hasPermit(\'surveyDeduct.agent\')" ng-click="grid.appScope.openAcqModal(row.entity.orderNo,3)"> | 标注代理商</a> ' +
            '<a ng-show="grid.appScope.hasPermit(\'surveyDeduct.withdrawing\')" ng-click="grid.appScope.openAcqModal(row.entity.orderNo,4)"> | 处理</a> ' +
            '</div>'
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
        $http.post("surveyDeduct/selectGroup","info="+angular.toJson($scope.info)+"&pageNo="+
            $scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.result=data.page.result;
                    $scope.userGrid.totalItems = data.page.totalCount;
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

    // 导出
    $scope.exportInfo = function () {
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
                    location.href="surveyDeduct/importDetail?info="+encodeURI(angular.toJson($scope.info));
                }
            });
    };
    $scope.openAcqModal = function(orderNo,staAll){
        $scope.addInfo={orderNo:orderNo,sta:0,staAll:staAll,selectSta:"",amoutOne:"",amoutTwo:"",time:"",remark:""};
        $scope.acqModalChange();
        $('#acqModal').modal('show');
    };
    $scope.closeAcqModal = function(){
        $('#acqModal').modal('hide');
    };
    $scope.acqModalChange=function () {
        $scope.selectSta=false;
        $scope.amountSta2=false;
        if($scope.addInfo.staAll==1){
            $scope.title="标注上游";
            $scope.title1="标注上游扣款信息";
            $scope.title2="标注上游下发信息";
            if($scope.addInfo.sta==0){
                $scope.amountTitle="上游扣款金额";
                $scope.timeTitle="上游扣款时间";
            }else{
                $scope.amountTitle="上游下发金额";
                $scope.timeTitle="上游下发时间";
            }
        }else if($scope.addInfo.staAll==2){
            $scope.title="标注商户";
            $scope.title1="标注商户扣款信息";
            $scope.title2="标注商户下发信息";
            $scope.amountSta2=true;
            if($scope.addInfo.sta==0){
                $scope.amountTitle="商户已扣款金额";
                $scope.timeTitle="商户已扣款时间";
                $scope.amountTitle2="代理商待扣款金额";
            }else{
                $scope.amountTitle="商户已下发金额";
                $scope.timeTitle="商户已下发时间";
                $scope.amountTitle2="代理商待下发金额";
            }
        }else if($scope.addInfo.staAll==3){
            $scope.title="标注代理商";
            $scope.title1="标注代理商扣款信息";
            $scope.title2="标注代理商下发信息";
            $scope.amountSta2=true;
            if($scope.addInfo.sta==0){
                $scope.amountTitle="代理商已扣款金额";
                $scope.timeTitle="代理商已扣款时间";
                $scope.amountTitle2="代理商需扣款金额";
            }else{
                $scope.amountTitle="代理商已下发金额";
                $scope.timeTitle="代理商已下发时间";
                $scope.amountTitle2="代理商需下发金额";
            }
        }else if($scope.addInfo.staAll==4){
            $scope.selectSta=true;
            $scope.title="扣款处理";
            $scope.title1="标注扣款处理状态";
            $scope.title2="标注下发处理状态";
            if($scope.addInfo.sta==0){
                $scope.selectStaList=$scope.agentDeductDealStatusSelect;
            }else{
                $scope.selectStaList=$scope.agentIssueDealStatusSelect;
            }
        }

    };
    //提交
    $scope.commitInfo=function () {
        if($scope.addInfo.staAll!=4){
            if($scope.addInfo.time==null||$scope.addInfo.time==""){
                $scope.notice($scope.timeTitle+"不能为空!");
                return;
            }
        }
        //实现权限分离
        var url="";
        if($scope.addInfo.staAll==1){
            url="surveyDeduct/upperReaches";
        }else if($scope.addInfo.staAll==2){
            url="surveyDeduct/merchant";
        }else if($scope.addInfo.staAll==3){
            url="surveyDeduct/agent";
        }else if($scope.addInfo.staAll==4){
            url="surveyDeduct/withdrawing";
        }
        $http.post(url,"info="+angular.toJson($scope.addInfo),
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.notice(data.msg);
                    $scope.query();
                    $scope.closeAcqModal();
                }else{
                    $scope.notice(data.msg);
                }
            })
            .error(function(data){
                $scope.notice(data.msg);
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