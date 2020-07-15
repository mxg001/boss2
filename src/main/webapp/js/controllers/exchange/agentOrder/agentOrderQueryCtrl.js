/**
 * 产品列表
 */
angular.module('inspinia').controller('agentOrderQueryCtrl',function($scope,$http,i18nService,SweetAlert,$document){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    $scope.orderStatusSelect =$scope.orderStatusExchange;
    $scope.orderStatusStr=angular.toJson($scope.orderStatusSelect);

    $scope.accStatusSelect = $scope.accStatusExchange;
    $scope.accStatusStr=angular.toJson($scope.accStatusSelect);

    $scope.oemList=[];
    //组织列表
    $http.post("exchangeOem/getOemList",
        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function(data){
            if(data.status){
                $scope.oemList.push({value:"",text:"全部"});
                var list=data.list;
                if(list!=null&&list.length>0){
                    for(var i=0; i<list.length; i++){
                        $scope.oemList.push({value:list[i].oemNo,text:list[i].oemName});
                    }
                }
            }else{
                $scope.notice(data.msg);
            }
        });

    //清空
    $scope.clear=function(){
        $scope.info={orderNo:"",merNo:"",payOrderNo:"",oemNo:"",accStatus:"",payTimeBegin:"",
            payTimeEnd:"",orderStatus:"",accTimeBegin:"",accTimeEnd:"",
            createTimeBegin:moment(new Date().getTime()-6*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
            createTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'
        };
    };
    $scope.clear();

    $scope.clearTotalAmount=function () {
        $scope.totalAmount={plateShareTotal:0,oemShareTotal:0,agentAmoutTotal:0,merAmoutTotal:0};
    };
    $scope.clearTotalAmount();


    $scope.userGrid={                           //配置表格
        data: 'result',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'orderNo',displayName:'订单ID',width:180},
            { field: 'oemNo',displayName:'组织ID',width:180 },
            { field: 'orderStatus',displayName:'订单状态',width:120,cellFilter:"formatDropping:" +  $scope.orderStatusStr },
            { field: 'merNo',displayName:'贡献人ID',width:180 },
            { field: 'userName',displayName:'贡献人名称',width:180 },
            { field: 'amount',displayName:'售价',width:180,cellFilter:"currency:''" },
            { field: 'provideAmout',displayName:'发放奖金',width:180,cellFilter:"currency:''" },
            { field: 'createTime',displayName:'创建时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'payTime',displayName:'支付时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'payOrderNo',displayName:'关联支付订单',width:300 },
            { field: 'oemName',displayName:'组织名称',width:180 },
            { field: 'oemShare',displayName:'品牌商分润',width:180,cellFilter:"currency:''" },
            { field: 'plateShare',displayName:'平台分润',width:180,cellFilter:"currency:''" },
            { field: 'agentNoOne',displayName:'一级分润代理商编号',width:180 },
            { field: 'agentOneAmout',displayName:'一级分润代理商分润',width:180,cellFilter:"currency:''" },
            { field: 'agentNoTwo',displayName:'二级分润代理商编号',width:180 },
            { field: 'agentTwoAmout',displayName:'二级分润代理商分润',width:180,cellFilter:"currency:''" },
            { field: 'agentAmout',displayName:'代理商总分润',width:180,cellFilter:"currency:''" },
            { field: 'merAmout',displayName:'用户总分润',width:180,cellFilter:"currency:''" },
            { field: 'accStatus',displayName:'记账状态',width:120,cellFilter:"formatDropping:" +  $scope.accStatusStr },
            { field: 'accTime',displayName:'入账时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'id',displayName:'操作',width:180,pinnedRight:true, cellTemplate:
            '<div class="lh30">'+
            '<a target="_blank" ui-sref="exchange.agentOrderDetail({id:row.entity.id})">详情</a> ' +
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
        $http.post("agentOrder/selectAll","info="+angular.toJson($scope.info)+"&pageNo="+
            $scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.result=data.page.result;
                    $scope.userGrid.totalItems = data.page.totalCount;
                    if(data.totalAmount!=null){
                        $scope.totalAmount=data.totalAmount;
                    }else{
                        $scope.clearTotalAmount();
                    }
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
                    location.href = "agentOrder/importDetail?info=" + encodeURI(angular.toJson($scope.info));
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