/**
 * 商户详情查看
 */

angular.module('inspinia').controller('zqMerInfoDetailCtrl', function ($scope, $http, $state, $stateParams, $compile, $filter, $log, $uibModal) {

    //数据源
    $scope.itemStatus=[{value:"1",text:"待一审"},{value:"2",text:"待平台审核"},{text:"审核失败",value:"3"},{text:"正常",value:"4"},{text:"关闭",value:"0"}];
    $scope.merStatusList=[{text:"正常",value:"1"},{text:"关闭",value:"0"}];
    $scope.businessTypes=[];
    $scope.rates=[];
    $scope.quotas=[];
    $scope.info={};
    $scope.mbp=[];
    $scope.merExa=[];
    $scope.preFreezeList=[];
    $scope.listMri=[];
    $scope.listMris=[];
    $scope.serviceStatus=[];
    $scope.terminal=[];
    $scope.listacr=[];
    $scope.checkStatus="";
    $scope.isCheck="";
    $scope.agent={};
    $scope.merAgent={};
    //账户信息
    $scope.accountAllInfo=[];
    $scope.accountBalances=[];

    $http.get('zqMerInfoMgr/showZqMerInfoDetail.do?merServiceId=' + $stateParams.merServiceId)
        .success(function (largeLoad) {
            if (largeLoad.bols) {
                if (largeLoad.listmr == null || largeLoad.listmsq == null || largeLoad.serviceMgr == null || largeLoad.mi == null || largeLoad.mbp == null || largeLoad.listel == null || largeLoad.listmri == null) {
                    $scope.notice("数据为空");
                    return;
                } else {
                    $scope.rates = largeLoad.listmr;
                    $scope.quotas = largeLoad.listmsq;
                    $scope.info = largeLoad.mi;
                    $scope.agent = largeLoad.agent;
                    $scope.merAgent = largeLoad.merAgent;
                    if ($scope.info.address == "") {
                        $scope.info.address = null;
                    }
                    $scope.terminal = largeLoad.tiPage.result;
                    $scope.terminalTotal = largeLoad.tiPage.totalCount;
                    $scope.mbp = largeLoad.mbp;
                    $scope.merExa = largeLoad.listel;
                    $scope.preFreezeList = largeLoad.preFreezeList;
                    $scope.listMri = largeLoad.listmri;
                    $scope.listacr = largeLoad.listacr;  //isCheck  checkStatus
                    $scope.checkStatus = largeLoad.checkStatus;
                    $scope.isCheck = largeLoad.isCheck;
                    $scope.listMris = largeLoad.listmris;
                    for (var i = 0; i < $scope.listMri.length; i++) {
                        if ($scope.listMri[i].mriId == 6) {
                            $scope.info.idCardNo = $scope.listMri[i].content;
                        }
                    }
                    $scope.serviceStatus = largeLoad.serviceMgr;
                    $scope.zqMerInfoLogData = largeLoad.zqMerLogs;
                    //账户查询
                    var merDate = {"merNo": largeLoad.mbp.merchantNo};
                    $http.post('merchantInfo/getAccountInfo', angular.toJson(merDate))
                        .success(function (data) {
                            if (data.bols) {
                                $scope.accountBalances = data.alist;
                                $scope.accountDetailMgr.data = data.list;
                                $scope.accountDetailMgr.totalItems = parseInt(data.total);
                            } else {
                                $scope.notice(data.msg);
                            }
                        })
                }
            } else {
                $scope.notice(largeLoad.msg);
            }
        });
    /**
     * 获取敏感数据
     */
    $scope.dataSta=true;
    $scope.getDataProcessing = function () {
        if($scope.dataSta){
            $http.post("zqMerInfoMgr/getDataProcessing",'merServiceId='+$stateParams.merServiceId,
                {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                .success(function(largeLoad) {
                    if(largeLoad.bols){
                        $scope.info.mobilephone = largeLoad.mi.mobilephone;
                        $scope.info.idCardNo = largeLoad.mi.idCardNo;
                        $scope.listMri=largeLoad.listmri;
                        for(var i =0;i<$scope.listMri.length;i++){
                            if($scope.listMri[i].mriId==6){
                                $scope.info.idCardNo=$scope.listMri[i].content;
                            }
                        }
                        $scope.dataSta=false;
                    }else{
                        $scope.notice(data.msg);
                    }
                });
        }
    };

    $scope.merchantTypes = function (data) {
        if (data == 1) {
            return '个人';
        }
        if (data == 2) {
            return '个体商户';
        }
        if (data == 3) {
            return '企业商户';
        }
    };

    $scope.accountTypes = function (data) {
        if (data == 1) {
            return '对公';
        }
        if (data == 2) {
            return '对私';
        }
    }

    $scope.rateTypes = [{text: "每笔固定金额", value: "1"}, {text: "每笔扣率", value: "2"}, {
        text: "每笔扣率带保底封顶",
        value: "3"
    }, {text: "每笔扣率+固定金额", value: "4"}
        , {text: "单笔阶梯扣率", value: "5"},{text:"扣率+封顶",value:6}]
    $scope.merchantRateList = {
        data: 'rates',
        columnDefs: [
            {field: 'serviceName', displayName: '服务名称', width: 120, pinnable: false, sortable: false},
            {
                field: 'cardType', displayName: '银行卡种类', width: 120, pinnable: false, sortable: false,
                cellFilter: "formatDropping:" + angular.toJson($scope.cardType)
            },
            {
                field: 'holidaysMark', displayName: '节假日标志', width: 80, pinnable: false, sortable: false,
                cellFilter: "formatDropping:" + $scope.holidaysStr
            },
            {
                field: 'rateType', displayName: '费率方式', width: 220, pinnable: false, sortable: false,
                cellFilter: "formatDropping:" + angular.toJson($scope.rateTypes)
            },
            {field: 'oneRate', displayName: '一级代理商管控费率', width: 260, pinnable: false, sortable: false},
            {field: 'merRate', displayName: '商户费率', width: 260, pinnable: false, sortable: false},
            {
                field: 'fixedMark', displayName: '固定标志', width: 120, pinnable: false, sortable: false,
                cellFilter: "formatDropping:" + angular.toJson($scope.fixedMark)
            }
        ]
    };

    $scope.merchantQuotaList = {
        data: 'quotas',
        columnDefs: [
            {field: 'serviceName', displayName: '服务名称', width: 120, pinnable: false, sortable: false},
            {
                field: 'cardType', displayName: '银行卡种类', width: 120, pinnable: false, sortable: false,
                cellFilter: "formatDropping:" + angular.toJson($scope.cardType)
            },
            {
                field: 'holidaysMark', displayName: '节假日标志', width: 80, pinnable: false, sortable: false,
                cellFilter: "formatDropping:" + $scope.holidaysStr
            },
            {
                field: 'singleDayAmount',
                displayName: '单日最大交易金额(元）',
                width: 200,
                pinnable: false,
                sortable: false,
                cellFilter: "currency:''"
            },
            {
                field: 'singleMinAmount',
                displayName: '单笔最小交易额(元）',
                width: 200,
                pinnable: false,
                sortable: false,
                cellFilter: "currency:''"
            },
            {
                field: 'singleCountAmount',
                displayName: '单笔最大交易额（元）',
                width: 200,
                pinnable: false,
                sortable: false,
                cellFilter: "currency:''"
            },
            {
                field: 'singleDaycardAmount',
                displayName: '单日单卡最大交易额（元）',
                width: 200,
                pinnable: false,
                sortable: false,
                cellFilter: "currency:''"
            },
            {field: 'singleDaycardCount', displayName: '单日单卡最大交易笔数（笔）', width: 200, pinnable: false, sortable: false},
            {
                field: 'fixedMark', displayName: '固定标志', width: 120, pinnable: false, sortable: false,
                cellFilter: "formatDropping:" + angular.toJson($scope.fixedMark)
            }
        ]
    };

    $scope.servicesStatusLie = [{text: "关闭", value: 0}, {text: "开通", value: 1}];
    $scope.serviceTradeType = [{text: "集群模式", value: "0"}, {text: "直清模式", value: "1"}];
    $scope.serviceSyncStatus = [{text: "初始化", value: "0"}, {text: "同步成功", value: "1"}, {text: "同步失败", value: "2"}, {text: "审核中", value: "3"}];

    $scope.serviceStatusMgr = {
        data: 'serviceStatus',
        columnDefs: [
            {field: 'serviceName', displayName: '服务名称', width: 200, pinnable: false, sortable: false},
            {
                field: 'status', displayName: '服务状态', width: 100, pinnable: false, sortable: false,
                cellFilter: "formatDropping:" + angular.toJson($scope.servicesStatusLie)
            },
            {
                field: 'tradeType', displayName: '交易模式', width: 150, pinnable: false, sortable: false,
                cellFilter: "formatDropping:" + angular.toJson($scope.serviceTradeType)
            },
            {
                field: 'channelCode', displayName: '直清通道', width: 150, pinnable: false, sortable: false
            },
            {
                field: 'syncStatus', displayName: '同步状态', width: 100, pinnable: false, sortable: false,
                cellFilter: "formatDropping:" + angular.toJson($scope.serviceSyncStatus)
            },
            {field: 'unionpayMerNo', displayName: '银联报备商户号', width: 150, pinnable: false, sortable: false},
            {field: 'terminalNo', displayName: '终端号', width: 150, pinnable: false, sortable: false},
            {field: 'syncRemark', displayName: '同步备注', width: 250, pinnable: false, sortable: false}
        ]
    };

    $scope.zqMerInfoLogMgr = {
        data: 'zqMerInfoLogData',
        columnDefs: [
            {field: 'channelCode', displayName: '同步通道', width: 150, pinnable: false, sortable: false},
            {field: 'serviceName', displayName: '服务名称', width: 200, pinnable: false, sortable: false},
            {field: 'syncRequires', displayName: '同步要素', width: 200, pinnable: false, sortable: false},
            {field: 'syncStatus', displayName: '同步状态', width: 100, pinnable: false, sortable: false,
                cellFilter: "formatDropping:" + angular.toJson($scope.serviceSyncStatus)
            },
            {field: 'syncRemark', displayName: '原因', width: 250, pinnable: false, sortable: false},
            {field: 'createTime', displayName: '同步时间', width: 200, pinnable: false, sortable: false,
                cellFilter: "date:'yyyy-MM-dd HH:mm:ss'"
            }
        ]
    };

    $scope.preFrozenRecords = {
        data: 'preFreezeList',
        columnDefs: [
            {
                field: 'operTime', displayName: '时间', pinnable: false, sortable: false,
                cellFilter: "date:'yyyy-MM-dd HH:mm:ss'"
            },
            {field: 'operName', displayName: '操作人', pinnable: false, sortable: false},
            {field: 'operLog', displayName: '操作日志', pinnable: false, sortable: false},
            {field: 'preFreezeNote', displayName: '备注', pinnable: false, sortable: false}
        ]
    };

    $scope.merchantRecords = {
        data: 'merExa',
        columnDefs: [
            {
                field: 'openStatus', displayName: '状态', pinnable: false, sortable: false,
                cellFilter: "formatDropping:[{text:'审核失败',value:'2'},{text:'审核成功',value:'1'}]"
            },
            {field: 'examinationOpinions', displayName: '内容', pinnable: false, sortable: false},
            {
                field: 'createTime', displayName: '时间', pinnable: false, sortable: false,
                cellFilter: "date:'yyyy-MM-dd HH:mm:ss'"
            },
            {field: 'realName', displayName: '操作员', pinnable: false, sortable: false}
        ]
    };

    $scope.merchantCheckRecords = {
        data: 'listacr',
        columnDefs: [
            {field: 'ruleCode', displayName: '效验项', pinnable: false, sortable: false},
            {field: 'checkInfo', displayName: '效验信息', pinnable: false, sortable: false},
            {
                field: 'checkResult', displayName: '效验结果', pinnable: false, sortable: false,
                cellFilter: "formatDropping:[{text:'通过',value:'1'},{text:'未通过',value:'0'}]"
            },
            {
                field: 'createTime',
                displayName: '时间',
                pinnable: false,
                sortable: false,
                cellFilter: "date:'yyyy-MM-dd HH:mm:ss'"
            }
        ]
    };

    $scope.merchantDetailed = {
        data: 'listMri',
        columnDefs: [
            {field: 'itemName', displayName: '进件要求项名称', pinnable: false, sortable: false},
            {
                field: 'content', displayName: '资料', pinnable: false, sortable: false,
                cellTemplate: '<div  class="lh30" ng-switch on="$eval(\'row.entity.exampleType\')">'
                + '<div ng-switch-when="2">'
                + '<div>'
                + '<a ui-sref="{{COL_FIELD}}">{{$eval(\'row.entity.itemName\')}} 附件下载</a>'
                + '</div>'
                + '</div>'
                + '<div ng-switch-when="3">'
                + '<div>{{COL_FIELD}}</div>'
                + '</div>'
                + '</div>'
            },
            {
                field: 'status', displayName: '状态', pinnable: false, sortable: false,
                cellFilter: "formatDropping:[{text:'待审核',value:0},{text:'审核通过',value:1},{text:'审核失败',value:2}]"
            },
            {
                field: 'auditTime', displayName: '审核通过时间', pinnable: false, sortable: false,
                cellFilter: "date:'yyyy-MM-dd'"
            }
        ]
    };


    //查看进件资料
    $scope.updateMriInfo = function (pp, id, status, content, name) {
        var modalScope = $scope.$new();
        modalScope.id = id;
        modalScope.pp = 1;
        if (pp == 1) {
            modalScope.dd = 1;
        } else {
            modalScope.dd = 0;
        }
        modalScope.type = 1;
        modalScope.name = name;
        modalScope.img = 2;
        modalScope.content = content;
        var modalInstance = $uibModal.open({
            templateUrl: 'views/merchant/merchantUpdateModal.html',  //指向上面创建的视图
            controller: 'merchantModalCtrl222',// 初始化模态范围
            scope: modalScope,
            size: 'lg'
        })
        modalScope.modalInstance = modalInstance;
        modalInstance.result.then(function (selectedItem) {
        }, function () {
            $log.info('取消: ' + new Date())
        })

    };
    $scope.operations = [{text: "减少", value: "debit"}, {text: "增加", value: "credit"}, {
        text: "冻结",
        value: "freeze"
    }, {text: "解冻", value: "unfreeze"}, {text: "全部", value: ""}]
    //账户信息模块
    $scope.accountInfo = {operation: ""};
    //获取账户余额信息和初始化账号交易记录

    $scope.accountBalance = {                           //配置表格
        data: 'accountBalances',
        columnDefs: [                           //表格数据
            {field: 'accountNo', displayName: '账号', width: 230},
            {field: 'balance', displayName: '余额', width: 150},
            {field: 'avaliBalance', displayName: '可用余额', width: 150},
            //{ field: 'controlAmount',displayName:'控制余额（已冻结余额）',width:200 },
            {field: 'settlingAmount', displayName: '结算中余额', width: 150},
            //{ field: 'preFreezeAmount',displayName:'预冻结余额',width:150 },
        ]
    };
    $scope.paginationOptions = angular.copy($scope.paginationOptions);
    $scope.accountDetailMgr = {                           //配置表格
        paginationPageSize: 10,                  //分页数量
        paginationPageSizes: [10, 20, 50, 100],	  //切换每页记录数
        useExternalPagination: true,
        columnDefs: [                           //表格数据
            {
                field: 'recordDate', displayName: '记账时间', width: 150,
                cellTemplate: "<div class='lh30'>{{row.entity.recordDate | date:'yyyy-MM-dd'}} {{row.entity.recordTime | date:'HH:mm:ss'}}</div>"
            },
            {
                field: 'debitCreditSide', displayName: '操作', width: 150,
                cellFilter: "formatDropping:[{text:'减少',value:'debit'},{text:'增加',value:'credit'},{text:'冻结',value:'freeze'},{text:'解冻',value:'unFreeze'}]"
            },
            {field: 'recordAmount', displayName: '金额', width: 150},
            {field: 'balance', displayName: '余额', width: 150},
            {field: 'avaliBalance', displayName: '可用余额', width: 150},
        ],
        onRegisterApi: function (gridApi) {
            $scope.gridApi = gridApi;
            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.paginationOptions.pageNo = newPage;
                $scope.paginationOptions.pageSize = pageSize;
                $scope.selectAccountInfo();
            });
        }
    };

    $scope.selectAccountInfo = function () {
        if ($scope.accountInfo.sdate > $scope.accountInfo.edate) {
            $scope.notice("起始时间不能大于结束时间");
            return;
        }
        var data = {
            "merNo": $scope.mbp.merchantNo,
            "sdate": $scope.accountInfo.sdate,
            "edata": $scope.accountInfo.edate,
            "operation": $scope.accountInfo.operation,
            "pageNo": $scope.paginationOptions.pageNo,
            "pageSize": $scope.paginationOptions.pageSize
        };
        $http.post(
            'merchantInfo/getAccountTranInfo',
            "info=" + angular.toJson(data), {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
        ).success(function (result) {
            if (result.bols) {
                $scope.accountDetailMgr.data = result.list;
                $scope.accountDetailMgr.totalItems = result.total;
            } else {
                $scope.notice(data.msg);
            }
        });

    };

    $scope.clearAccountInfo = function () {
        $scope.accountInfo = {operation: ""};
    };
});

angular.module('inspinia').controller('merchantModalCtrl222', function ($scope, $stateParams, $http) {
    $scope.solutionModalClose = function () {
        $scope.modalInstance.dismiss();
    };

    $scope.solutionModalOk = function () {
        $scope.modalInstance.close($scope);
    };
});