/**
 * 保险订单查询
 */
angular.module('inspinia',['infinity.angular-chosen']).controller('safeOrderQueryCtrl',function($scope,$http,i18nService,SweetAlert,$timeout,$document){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    $scope.bxUnitSelect =angular.copy($scope.insurerList);
    $scope.bxUnitStr=angular.toJson($scope.bxUnitSelect);

    $scope.settlementMethodSelect=[{text:'全部',value:""},{text:'T1',value:'1'},{text:'T0',value:'0'}];
    $scope.settlementMethodStr=angular.toJson($scope.settlementMethodSelect);

    $scope.bxTypeSelect=[{text:'全部',value:""},
        {text:'初始化',value:'INIT'},{text:'投保成功',value:'SUCCESS'},{text:'投保失败',value:'FAILED'},
        {text:'已退保',value:'OVERLIMIT'},{text:'退保失败',value:'RECEDEFAILED'}
    ];
    $scope.bxTypeStr=angular.toJson($scope.bxTypeSelect);

    $scope.lowerAgentSelect=[{text:'否',value:0},{text:'是',value:1}];

    //清空
    $scope.clear=function(){
        $scope.info={bxUnit:"0",bxType:"",transStatus:"",settlementMethod:"",oneAgentNo:"",prodNo:"",
            lowerAgent:0,sysSource:"",
            tTimeBegin:moment(new Date().getTime()-6*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
            tTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'
        };
    };
    $scope.clear();

    $scope.clearOrderTotal=function(){
        $scope.total={countTotal:0,nPrmTotal:0,nFeeTotal:0};
    };
    $scope.clearOrderTotal();

    //产品列表
    $scope.prodNoList=[{value:"",text:"全部"}];
    $http.post("safeConfig/getSafeConfigList",
        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function(data){
            if(data.status){
                var list=data.list;
                if(list!=null&&list.length>0){
                    for(var i=0; i<list.length; i++){
                        $scope.prodNoList.push({value:list[i].proCode,text:list[i].proCode});
                    }
                }
            }else{
                $scope.notice(data.msg);
            }
        });

    //获取代理商
    $http.post("agentInfo/selectAllInfo")
        .success(function(msg){
            $scope.agentList=[{value:"",text:"全部"}];
            for(var i=0; i<msg.length; i++){
                $scope.agentList.push({value:msg[i].agentNo,text:msg[i].agentNo + " " + msg[i].agentName});
            }

        });
    //条件查询代理商
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
                                $scope.agentt =[{value: "", text: "全部"}];
                            }else{
                                $scope.agentt =[{value: "", text: "全部"}];
                                for(var i=0; i<response.data.length; i++){
                                    $scope.agentt.push({value:response.data[i].agentNo,text:response.data[i].agentNo + " " + response.data[i].agentName});
                                }
                            }
                            $scope.agentList = $scope.agentt;
                            oldValue = value;
                        });
                },800);
        }
    };

    $scope.userGrid={                           //配置表格
        data: 'result',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'bxOrderNo',displayName:'保险订单号',width:180 },
            { field: 'tTime',displayName:'投保时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'orderNo',displayName:'交易订单号',width:180 },
            { field: 'merchantNo',displayName:'商户编号',width:180 },
            { field: 'oneAgentNo',displayName:'一级代理商编号',width:180 },
            { field: 'settlementMethod',displayName:'结算周期',width:120,cellFilter:"formatDropping:" +  $scope.settlementMethodStr },
            { field: 'transAmount',displayName:'交易金额',width:180,cellFilter:"currency:''" },
            { field: 'nFee',displayName:'保费-成本价',width:180,cellFilter:"currency:''" },
            { field: 'nPrm',displayName:'保费-售价',width:180,cellFilter:"currency:''" },
            { field: 'nAmt',displayName:'保额',width:180,cellFilter:"currency:''" },
            { field: 'transStatus',displayName:'支付状态',width:120,cellFilter:"formatDropping:" +  $scope.transStatusStr},
            { field: 'bxType',displayName:'投保状态',width:120,cellFilter:"formatDropping:" +  $scope.bxTypeStr },
            { field: 'thirdOrderNo',displayName:'保单号',width:180 },
            { field: 'tBeginTime',displayName:'保险起期',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'tEndTime',displayName:'保险止期',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'id',displayName:'操作',width:180,pinnedRight:true, cellTemplate:
                '<div class="lh30">'+
                '<a target="_blank" ui-sref="safe.safeOrderDetail({id:row.entity.id})">详情</a> ' +
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
        $http.post("safeOrder/selectAll","info="+angular.toJson($scope.info)+"&pageNo="+
            $scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.result=data.page.result;
                    $scope.userGrid.totalItems = data.page.totalCount;
                    $scope.total=data.total;
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
                    location.href = "safeOrder/importDetail?info=" + encodeURI(angular.toJson($scope.info));
                }
            });
    };

    //手工退保
    $scope.retreatsSafeBatch = function(){
        var selectList = $scope.gridApi.selection.getSelectedRows();
        if(selectList==null||selectList.length==0){
            $scope.notice("请选中要退保的数据!");
            return false;
        }
        var ids="";
        if(selectList!=null&&selectList.length>0){
            var nowDate=new Date();
            for(var i=0;i<selectList.length;i++){
                if(selectList[i].transStatus=="FAILED"
                    &&selectList[i].bxType=="SUCCESS"
                    &&selectList[i].tEndTime>nowDate.getTime()){
                    ids = ids + selectList[i].id + ",";
                }
            }
        }
        if(ids==""){
            $scope.notice("选中的数据不含符合退保的数据!");
            return false;
        }
        ids=ids.substring(0,ids.length-1);
        SweetAlert.swal({
                title: "确认批量退保?",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post("safeOrder/retreatsSafe","ids="+ids,
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
    //页面绑定回车事件
    $document.bind("keypress", function(event) {
        $scope.$apply(function (){
            if(event.keyCode == 13){
                $scope.query();
            }
        })
    });
});