/**
 * 积分兑换核销管理
 */
angular.module('inspinia',['angularFileUpload']).controller('exchangeActivateOrderAuditQueryCtrl',function($scope,$http,i18nService,SweetAlert,$document,FileUploader){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    $scope.orderStatusSelect =$scope.orderStatusExchange;
    $scope.orderStatusStr=angular.toJson($scope.orderStatusSelect);

    $scope.accStatusSelect =$scope.accStatusExchange;
    $scope.accStatusStr=angular.toJson($scope.accStatusSelect);

    $scope.channelSelect = angular.copy($scope.orderChannelExchange);
    $scope.channelStr=angular.toJson($scope.channelSelect);
    $scope.channelSelect.unshift({text:"全部",value:null});

    $scope.checkStatusSelect = [{text:"全部",value:""},{text:"核销中",value:"0"},{text:"核销成功",value:"1"},
        {text:"核销失败",value:"2"}];
    $scope.checkStatusStr=angular.toJson($scope.checkStatusSelect);
    //组织列表
    $scope.oemList=[];
    $http.post("exchangeActivateOem/getOemList",
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
    //获取机构
    $scope.orgList=[];
    $http.post("exchangeActivateOrg/getOrgManagementList")
        .success(function(data){
            if(data.status){
                $scope.orgList.push({value:"",text:"全部"});
                var orgList=data.list;
                if(orgList!=null&&orgList.length>0){
                    for(var i=0; i<orgList.length; i++){
                        $scope.orgList.push({value:orgList[i].orgCode, text:orgList[i].orgName});
                    }
                }
            }
        });
    //清空
    $scope.clear=function(){
        $scope.info={orderNo:"",orderStatus:"",oemNo:"",merNo:"",userName:"",mobileUsername:"", orgCode:"",
            checkStatus:"",checkStatusOne:"",accStatus:"",checkOper:"",accTimeBegin:"",accTimeEnd:"",
            createTimeBegin:moment(new Date().getTime()-6*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
            createTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
            checkTimeBegin:"",checkTimeEnd:"",channelCheckStatus:"",channel:null
        };
    };
    $scope.clear();

    $scope.clearTotalAmount=function () {
        $scope.totalAmount={plateShareTotal:0,writeOffPriceTotal:0,oemShareTotal:0,priceTotal:0};
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
            { field: 'orgName',displayName:'兑换机构',width:180 },
            { field: 'typeName',displayName:'产品类别',width:180 },
            { field: 'productName',displayName:'产品名称',width:180 },
            { field: 'uploadImage',displayName:'上传截图',width:180,cellTemplate:'' +
                '<div ng-show="row.entity.uploadImage!=null"> ' +
                    '<a href="{{row.entity.uploadImage}}" fancybox rel="group">' +
                        '<img style="width:70px;height:35px;" ng-src="{{row.entity.uploadImage}}"/>' +
                    '</a>' +
                '</div>'
            },
            { field: 'redeemCode',displayName:'兑换码',width:180 },
            { field: 'validityDateStart',displayName:'有效期开始时间',cellFilter: 'date:"yyyy-MM-dd"',width:180},
            { field: 'validityDateEnd',displayName:'有效期截止时间',cellFilter: 'date:"yyyy-MM-dd"',width:180},
            { field: 'productRemark',displayName:'产品备注',width:180 },
            { field: 'merNo',displayName:'贡献人ID',width:180 },
            { field: 'userName',displayName:'贡献人名称',width:180 },
            { field: 'mobileUsername',displayName:'贡献人手机号',width:180 },
            { field: 'idCardNo',displayName:'贡献人证件号',width:180 },
            { field: 'price',displayName:'兑换价格',width:180,cellFilter:"currency:''" },
            { field: 'createTime',displayName:'创建时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'oemName',displayName:'组织名称',width:180 },
            { field: 'agentNo',displayName:'直属代理商ID',width:180 },
            { field: 'agentName',displayName:'直属代理商名称',width:180 },
            { field: 'oneAgentNo',displayName:'一级代理商ID',width:180 },
            { field: 'oneAgentName',displayName:'一级代理商名称',width:180},
            { field: 'oemShare',displayName:'品牌商分润',width:180,cellFilter:"currency:''" },
            { field: 'plateShare',displayName:'平台分润',width:180,cellFilter:"currency:''" },
            { field: 'accStatus',displayName:'记账状态',width:180,cellFilter:"formatDropping:" +  $scope.accStatusStr },
            { field: 'accTime',displayName:'入账时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'channel',displayName:'核销渠道',width:180,cellFilter:"formatDropping:" +  $scope.channelStr },
            { field: 'channelCheckStatus',displayName:'上游渠道核销状态',width:180,cellFilter:"formatDropping:" +  $scope.checkStatusStr},
            { field: 'channelCheckTime',displayName:'上游渠道核销时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'saleOrderNo',displayName:'核销渠道订单ID',width:180},
            { field: 'writeOffPrice',displayName:'核销价格',width:180,cellFilter:"currency:''" },
            { field: 'checkStatusOne',displayName:'一次核销状态',width:180,cellFilter:"formatDropping:" +  $scope.checkStatusStr},
            { field: 'checkStatus',displayName:'二次核销状态',width:180,cellFilter:"formatDropping:" +  $scope.checkStatusStr},
            { field: 'checkOper',displayName:'核销人',width:180 },
            { field: 'checkTime',displayName:'核销时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'id',displayName:'操作',width:300,pinnedRight:true, cellTemplate:
            '<div class="lh30">'+
            '<a target="_blank" ui-sref="exchangeActivate.exchangeAuditDetail({id:row.entity.id})">详情</a> ' +
            '<a ng-show="row.entity.channelCheckStatus==0&&row.entity.checkStatusOne!=1&&row.entity.checkStatus==0&&row.entity.orderStatus==\'SUCCESS\'&&grid.appScope.hasPermit(\'exchangeActivateOrder.orderApiSelect\')"  ng-click="grid.appScope.orderApiSelect(row.entity.id)"> | 核销查询</a> ' +
            '<a ng-show="(row.entity.channelCheckStatus==null||row.entity.channelCheckStatus==2)&&row.entity.checkStatusOne!=1&&row.entity.checkStatus==0&&row.entity.orderStatus==\'SUCCESS\'&&grid.appScope.hasPermit(\'exchangeActivateOrder.orderApi\')"  ng-click="grid.appScope.orderApi(row.entity.id)"> | 通道核销</a> ' +
            '<a ng-show="(row.entity.channelCheckStatus==null||row.entity.channelCheckStatus==2)&&row.entity.checkStatusOne!=1&&row.entity.checkStatus==0&&row.entity.orderStatus==\'SUCCESS\'&&grid.appScope.hasPermit(\'exchangeActivateOrder.updateWriteOff\')" target="_blank" ui-sref="exchangeActivate.exchangeAuditEdit({id:row.entity.id})"> | 核销</a> ' +
            '<a ng-show="(row.entity.channelCheckStatus==null||row.entity.channelCheckStatus==2)&&row.entity.checkStatusOne==1&&row.entity.checkStatus==0&&row.entity.orderStatus==\'SUCCESS\'&&grid.appScope.hasPermit(\'exchangeActivateOrder.updateAgainWriteOff\')" target="_blank" ui-sref="exchangeActivate.exchangeAuditTwoEdit({id:row.entity.id})"> | 二次核销</a> ' +
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
        $http.post("exchangeActivateOrder/selectAuditAll","info="+angular.toJson($scope.info)+"&pageNo="+
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
    $scope.exportInfo = function (sta) {
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
                    location.href="exchangeActivateOrder/importAuditDetail?info="+encodeURI(angular.toJson($scope.info))+"&state="+sta;
                }
            });
    };
    /*
     * 上游查询订单
     */
    $scope.orderApiSelect = function (id) {
        SweetAlert.swal({
                title: "确认上游核销查询？",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true
            },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post("exchangeActivateOrder/orderApiSelect","id="+id,
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

    /*
     * 通道核销
     */
    $scope.orderApi = function (id) {
        SweetAlert.swal({
                title: "确认上游通道核销？",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true
            },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post("exchangeActivateOrder/orderApi","id="+id,
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

    $scope.importDiscountShow = function(){
        $('#importDiscount').modal('show');
    }
    $scope.cancel = function(){
        $('#importDiscount').modal('hide');
    }
    //上传图片,定义控制器路径
    var uploader = $scope.uploader = new FileUploader({
        url: 'exchangeActivateOrder/importDiscount',
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
    };
    //导入
    $scope.importDiscount=function(){
        uploader.uploadAll();//上传
        uploader.onSuccessItem = function(fileItem, response, status, headers) {//上传成功后的回调函数，在里面执行提交
            if(response.status){
                $scope.notice(response.msg);
                $scope.cancel();
            }else{
                $scope.notice(response.msg);
            }
        };
    };

    //条件显示问题
    $scope.mtxt="全部条件";
    $scope.visible= false;
    $scope.toggle = function(){
        if($scope.visible == false){
            $scope.mtxt="收起";
            $scope.visible=true;
        }else{
            $scope.mtxt="全部条件";
            $scope.visible=false;
        }
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