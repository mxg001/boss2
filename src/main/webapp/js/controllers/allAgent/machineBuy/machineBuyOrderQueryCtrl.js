/**
 * 机具申购订单
 */
angular.module('inspinia',['angularFileUpload']).controller("machineBuyOrderQueryCtrl", function($scope, $http, $state, $stateParams,$filter,i18nService,SweetAlert,$document,FileUploader) {
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    //数据源
    $scope.orderStatuses=[{text:"全部",value:null},{text:"待发货",value:0},{text:"待付款",value:1},{text:"已发货",value:2},{text:"已关闭",value:4}];
    $scope.transChanneles=[{text:"全部",value:""},{text:"微信支付",value:"wx"},{text:"支付宝支付",value:"zfb"},{text:"快捷支付",value:"kj"}];
    $scope.pageCount={receivedCount:0,receivedAmountCount:0,waitSendCount:0,waitSendAmountCount:0,turnPlatformCount:0,turnPlatformAmountCount:0,
        entryAmountCount:0,goodsTotalCount:0,shareAmountCount:0,shareNoAmountCount:0,snCount:0};
    $scope.errorCount=0;
    $scope.successCount=0;
    $scope.time='';
    $scope.isPlatformes=[{text:"全部",value:null},{text:"机构发货",value:0},{text:"平台发货",value:1},{text:"大盟主发货",value:2},{text:"盟主发货",value:3}]
    $scope.entryStatuses=[{text:"全部",value:null},{text:"未入账",value:0},{text:"已入账",value:1}]
    $scope.accStatuses=[{text:"全部",value:""},{text:"未入账",value:"NOENTERACCOUNT"},{text:"已入账",value:"ENTERACCOUNTED"}]
    $scope.sendTypes=[{text:"全部",value:""},{text:"快递配送",value:"1"},{text:"线下自提",value:"2"}]
    $scope.afterSaleStatuses=[{text:"全部",value:null},{text:"待机构处理",value:0},{text:"待平台处理",value:1},{text:"已处理",value:2},{text:"已取消",value:3}]
    $scope.afterSaleStatusetest=[{text:"待机构处理",value:0},{text:"待平台处理",value:1},{text:"已处理",value:2},{text:"已取消",value:3}]
    $scope.transStatuses=[{text:"全部",value:null},{text:"未支付",value:0},{text:"交易成功",value:1},{text:"交易失败",value:2}]
    $scope.shipWayes = [{text:"全部",value:""},{text:"机具类",value:1},{text:"物料类",value:2}];

    //clear
    $scope.clear=function(){
        $scope.info={orderNo:"",userName:"",userCode:"",oneUserCode:"",oneRealName:"",orderStatus:null,transChannel:"",brandCode:"",
            transNo:"",entryStatus:null,accStatus:"",isPlatform:null,agentNo:"",sendType:"",afterSaleStatus:null,transStatus:null,shipWay:"",
            startTime:moment(new Date().getTime()-6*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
            endTime:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
            transStartTime:"",transEndTime:"",sendStartTime:"",sendEndTime:"",entryStartTime:"",entryEndTime:"",shareStartTime:"",shareEndTime:""};
    }
    $scope.clear();
    
    $scope.servicesGrid = {
        data: 'result',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs: [
            {field: 'orderNo',displayName: '订单编号',width:200},
            {field: 'img',displayName: '商品图片',width:180,
                cellTemplate:'' +
                '<div ng-show="row.entity.img!=null"' +
                '<a href="{{row.entity.img}}" fancybox rel="group">' +
                '<img style="width:70px;height:35px;" ng-src="{{row.entity.img}}"/>' +
                '</a>' +
                '</div>'},
            {field: 'gName',displayName: '商品标题',width:180},
            {field: 'color',displayName: '颜色',width:180},
            {field: 'size',displayName: '尺码',width:180},
            {field: 'price',displayName: '商品销售价(元)',width:180},
            {field: 'goodsCost',displayName: '机构成本价(元)',width:180},
            {field: 'num',displayName: '购买数量',width:100},
            {field: 'orderNo',displayName: '运费',width:100,cellTemplate:'<div>包邮</div>'},
            {field: 'isPlatform',displayName: '发货方',width:100,cellFilter:"formatDropping:" +  angular.toJson($scope.isPlatformes)},
            {field: 'shipWay',displayName: '商品类型',width:100,cellFilter:"formatDropping:" +  angular.toJson($scope.shipWayes)},
            {field: 'totalAmount',displayName: '订单金额(元)',width:180},
            {field: 'entryStatus',displayName: '机具款项入账状态',width:180,cellFilter:"formatDropping:" +  angular.toJson($scope.entryStatuses)},
            {field: 'entryAmount',displayName: '机具款项入账金额(元)',width:180},
            {field: 'entryTime',displayName: '机具款项入账时间',width:180,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
            {field: 'accStatus',displayName: '机具分润入账状态',width:180,cellFilter:"formatDropping:" +  angular.toJson($scope.accStatuses)},
            {field: 'shareAmount',displayName: '机具分润入账金额(元)',width:180},
            {field: 'accTime',displayName: '机具分润入账时间',width:180,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
            {field: 'realName',displayName: '申购盟主姓名',width:180},
            {field: 'userCode',displayName: '申购盟主编号',width:180},
            {field: 'parentId',displayName: '申购盟主推荐人编号',width:180},
            {field: 'orderStatus',displayName: '订单状态',width:180,cellFilter:"formatDropping:" +  angular.toJson($scope.orderStatuses)},
            {field: 'afterSaleStatus',displayName: '售后状态',width:180,cellFilter:"formatDropping:" +  angular.toJson($scope.afterSaleStatusetest)},
            {field: 'sendType',displayName: '订单类型',width:180,cellFilter:"formatDropping:" +  angular.toJson($scope.sendTypes)},
            {field: 'receiver',displayName: '收件人',width:180},
            {field: 'receiverMobile',displayName: '联系方式',width:150},
            {field: 'receiverAddress',displayName: '收货地址',width:180},
            {field: 'remark',displayName: '备注',width:180},
            {field: 'brandName',displayName: '所属品牌',width:180},
            {field: 'agentNo',displayName: '代理商编号',width:180},
            {field: 'oneUserCode',displayName: '机构编号',width:180},
            {field: 'oneUserName',displayName: '机构名称',width:180},
            {field: 'transChannel',displayName: '支付方式',width:180,cellFilter:"formatDropping:" +  angular.toJson($scope.transChanneles)},
            {field: 'transStatus',displayName: '支付状态',width:180,cellFilter:"formatDropping:" +  angular.toJson($scope.transStatuses)},
            {field: 'transNo',displayName: '支付订单号',width:100},
            {field: 'transTime',displayName: '支付日期',width:180,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
            {field: 'createTime',displayName: '下单日期',width:180,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
            {field: 'sendTime',displayName: '发货日期',width:180,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
            {field: 'action',displayName: '操作',width:150,pinnedRight:true,editable:true,cellTemplate:
                '<a class="lh30" ng-show="grid.appScope.hasPermit(\'machineBuyOrder.sendMachineBuyOrderSN\')&&row.entity.orderStatus==\'0\'&&row.entity.isPlatform==\'1\'" ui-sref="allAgent.machineBuyOrderShipMachine({orderNo:row.entity.orderNo,num:row.entity.num,goodCode:row.entity.goodCode})">发货</a>' +
                '<a class="lh30" ng-show="row.entity.orderStatus==\'2\'" ui-sref="allAgent.machineBuyOrderShipMachineDetail({orderNo:row.entity.orderNo,goodCode:row.entity.goodCode})">发货信息</a> '}
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
        $http.post("machineBuyOrder/queryMachineBuyOrderList","info="+angular.toJson($scope.info)+"&pageNo="+
            $scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.pageCount=data.pageCount;
                    $scope.result=data.page.result;
                    $scope.servicesGrid.totalItems = data.page.totalCount;
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

    $scope.cancelOrder=function (orderNo) {
        SweetAlert.swal({
                title: "您正在进行取消订单操作，如是已付款的订单取消金额将原路返回给买家，是否继续操作？",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post('machineBuyOrder/updateMachineBuyOrderByCancelOrder',"orderNo="+orderNo,
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function(msg){
                            $scope.notice(msg.msg);
                            if(msg.status){
                                $scope.cancelSendSNButchModel();
                                $scope.query();
                            }
                        }).error(function(){
                        $scope.notice('系统异常');
                    });
                }
            });
    }

    $scope.sendSNButchModel=function () {
        $("#sendSNButchModel").modal("show");
    }

    $scope.cancelSendSNButchModel=function () {
        $('#sendSNButchModel').modal('hide');
        $("#sendSNResultButchModel").modal("hide");
        $("#accountEntryButchModel").modal("hide");
        $scope.loadImg = false;
    }

    $scope.accountEntryButchModel=function () {
        $("#accountEntryButchModel").modal("show");
    }


    $scope.sendSNGrid = {
        data: 'errorlist',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs: [
            {field: 'orderNo',displayName: '订单编号',width:100},
            {field: 'transportCompany',displayName: '快递公司',width:200},
            {field: 'postNo',displayName: '快递单号',width:200},
            {field: 'errorResult',displayName: '原因',width:200}
        ]
    };

    var uploader = $scope.uploader = new FileUploader({
        url: 'machineBuyOrder/sendSNButchUpload',
        queueLimit: 1,   //文件个数
        removeAfterUpload: true,  //上传后删除文件
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    uploader.filters.push({
        name: 'isFile',
        fn: function(item, options) {
            return this.queue.length < 1;
        }
    });


    $scope.clearItems = function(){  //重新选择文件时，清空队列，达到覆盖文件的效果
        uploader.clearQueue();
    }

    $scope.submit=function(){
        if ($scope.loadImg) {
            return;
        }
        $scope.loadImg = true;
        uploader.uploadAll();//上传
        uploader.onSuccessItem = function(fileItem, response, status, headers) {//上传成功后的回调函数，在里面执行提交
            if(response.status){
                $("#sendSNResultButchModel").modal("show");
                $scope.errorCount=response.errorCount;
                $scope.successCount=response.successCount;
                $scope.errorlist=response.errorlist;
                $scope.query();
                $scope.loadImg = false;
            }else{
                $scope.notice(response.msg);
                $scope.loadImg = false;
            }
        };
        $scope.loadImg = false;
        return false;
    }

    $scope.submitAccountEntry=function(){
        $http.post('machineBuyOrder/accountEntryMachineBuyOrder',"time="+$scope.time,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(msg){
                if(msg.status){
                    $scope.notice(msg.msg+"成功"+msg.entrySuccessCount+"条，失败"+msg.entryErrorCount+"条。");
                    $scope.cancelSendSNButchModel();
                    $scope.query();
                }else{
                    $scope.notice(msg.msg);
                }
            }).error(function(){
            $scope.notice('系统异常');
        });
    }

    $scope.export = function () {
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
                    location.href="machineBuyOrder/exportMachineBuyOrder?param="+angular.toJson($scope.info);
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

