/**
 * 直清商户服务报件查询
 */
angular.module('inspinia').controller("queryZqServiceInfo", function($scope, $http, i18nService,$document,SweetAlert,$timeout) {
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    $scope.statusList = [{text: "全部", value: ""}, {text: "成功", value: "2"}, {text: "失败", value: "1"}];
    $scope.dealStatusList = [{text: "全部", value: ""}, {text: "初始化", value: "0"}, {text: "未处理", value: "1"}
                            , {text: "处理成功", value: "2"}, {text: "处理失败", value: "3"}];

    //清空
    $scope.resetForm = function () {
        $scope.baseInfo = {status:"",bpId:null,serviceId:null,channelCode:"",dealStatus:"",
            createTimeStart:moment(new Date().getTime()-7*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
            createTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'};
    }
    $scope.resetForm();

    //业务产品
    $http.post('businessProductDefine/selectAllInfo.do')
        .success(function (result) {
            if (result) {
                $scope.bpList = result;
                $scope.bpList.splice(0, 0, {bpId: null, bpName: "全部"});
            }
        });

    //服务
    $http.post('service/selectList')
        .success(function (result) {
            if (result.status) {
                $scope.serviceList = result.data;
                $scope.serviceList.splice(0, 0, {serviceId: null, serviceName: "全部"});
            }
        });

    //获取所有直清收单机构
    $scope.acqList = [{ acqEnname: "",acqName: "全部"}];
    $http.post("acqOrgAction/selectAllZqOrg")
        .success(function (result) {
            for (var i = 0; i < result.length; i++) {
                $scope.acqList.push({acqEnname: result[i].acqEnname, acqName: result[i].acqName});
            }
        });

    //异步获取商户
    // $scope.merchantList = [{merchantNo: "", merchantName: "全部"}];
    // var oldValue="-1";
    // var timeout="";
    // function getMerchantList(value){
    //     var newValue=value;
    //     if(newValue != oldValue) {
    //         if (timeout)
    //             $timeout.cancel(timeout);
    //         timeout = $timeout(function () {
    //                 $http({
    //                     url: "merchantBusinessProduct/getMerchantFew",
    //                     data: "item=" + value,
    //                     headers: {'Content-Type': 'application/x-www-form-urlencoded'},
    //                     method: "POST"
    //                 }).success(function (result) {
    //                     if (result.status) {
    //                         if (result.data.length == 0) {
    //                             $scope.merchantList = [{merchantNo: "", merchantName: "全部"}];
    //                         } else {
    //                             $scope.merchantList = result.data;
    //                             $scope.merchantList.unshift({merchantNo: "", merchantName: "全部"});
    //                         }
    //                         oldValue = value;
    //                     } else {
    //                         $scope.notice(result.msg);
    //                     }
    //                 }).error(function () {
    //                     $scope.notice("系统异常，请稍候再试");
    //                 })
    //             }
    //             , 800);
    //     }
    // }
    // $scope.getMerchantList = getMerchantList;
    // $scope.getMerchantList("");

    //查询
    $scope.query = function(){
        $scope.submitting = true;
        $scope.loadImg = true;
        $http({
            url:"zqServiceInfo/selectZqServiceInfoPage?pageNo=" + $scope.paginationOptions.pageNo +  "&pageSize=" + $scope.paginationOptions.pageSize,
            method:"post",
            data:$scope.baseInfo
        }).success(function(result){
            $scope.submitting = false;
            $scope.loadImg = false;
            if (!result || !result.status){
                $scope.notice (result.msg);
                return;
            }
            $scope.zqServiceInfoGrid.data = result.data.result;
            $scope.zqServiceInfoGrid.totalItems = result.data.totalCount;
        }).error(function(){
            $scope.submitting = false;
            $scope.loadImg = false;
            $scope.notice("服务器异常");
        });
    };
    // $scope.query();

    $scope.columnDefs = [
                    {field: 'createTimeStr',displayName: '创建时间',width:150},
                    {field: 'mbpId',displayName: '商户进件编号',width:150},
                    {field: 'merchantNo',displayName: '商户编号',width:150},
                    {field: 'merchantName',displayName: '商户名称',width:150},
                    {field: 'mobilephone',displayName: '手机号',width:150},
                    {field: 'bpName',displayName: '业务产品',width:150},
                    {field: 'serviceName',displayName: '服务名称',width:150},
                    {field: 'channelCode',displayName: '通道名称',width:150},
                    {field: 'statusName',displayName: '服务报备状态',width:150},
                    {field: 'msg',displayName: '备注',width:150},
                    {field: 'unionpayMerNo',displayName: '直清商户号',width:150},
                    {field: 'operator',displayName: '操作人',width:150},
                    {field: 'lastUpdateTimeStr',displayName: '最后报件时间',width:150},
                    {field: 'acqServiceMerNo',displayName: '上游商户号',width:150},
                    {field: 'merWxNo',displayName: '微信号',width:150},
                    {field: 'merRealName',displayName: '真实姓名',width:150},
                    {field: 'dealStatusName',displayName: '业务处理状态',width:150},
                    {field: 'dealOperator',displayName: '业务处理操作人',width:150},
                    {field: 'action',displayName: '操作',width: 150,pinnedRight:true,sortable: false,editable:true,cellTemplate:
                        '<a class="lh30"  ng-show="grid.appScope.hasPermit(\'zqServiceInfo.updateDealStatus\')&&row.entity.dealStatus!=2&&row.entity.dealStatus!=0" '
                        + ' ng-click="grid.appScope.updateDealStatus(row.entity,2)">处理成功</a>'
                        +'<a class="lh30"  ng-show="grid.appScope.hasPermit(\'zqServiceInfo.updateDealStatus\')&&row.entity.dealStatus!=3&&row.entity.dealStatus!=0" '
                        + ' ng-click="grid.appScope.updateDealStatus(row.entity,3)"> 处理失败</a>'
                    }
    ];

    $scope.zqServiceInfoGrid = {
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs: $scope.columnDefs,
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            $scope.gridApi.pagination.on.paginationChanged ($scope, function(newPage, pageSize) {
                $scope.paginationOptions.pageNo = newPage;
                $scope.paginationOptions.pageSize = pageSize;
                $scope.query();
            });
        }
    };

    // 导出
    $scope.export = function () {
        SweetAlert.swal({
                title: "确定导出吗？",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    location.href = "zqServiceInfo/export?baseInfo=" +encodeURI(encodeURI(angular.toJson($scope.baseInfo)));
                }
            });
    };

    $scope.zqSyncSerBatch = function(){
        $scope.selectedInfo = $scope.gridApi.selection.getSelectedRows();
        if($scope.selectedInfo == null || $scope.selectedInfo.length == 0){
            $scope.notice('请选择需要报件的商户');
            return;
        }
        var infoList = [];
        angular.forEach($scope.selectedInfo, function(item){
            if(item.status!=null&&item.status!="2"){
                infoList.push(item);
            }
        });
        if(infoList==null || infoList.length == 0){
            $scope.notice('请选择需要报件的商户');
            return;
        }
        SweetAlert.swal({
                title: "确定要对这些商户进行报件吗？",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http({
                        url:"zqServiceInfo/zqSyncSerBatch",
                        method:"post",
                        data:infoList
                    }).success(function(result){
                        $scope.notice (result.msg);
                        if (result.status){
                            $scope.query();
                        }
                    }).error(function(){
                        $scope.submitting = false;
                        $scope.loadImg = false;
                        $scope.notice("服务器异常");
                    });
                }
            });
    }

    //修改处理状态
    $scope.updateDealStatus = function(entity, dealStatus){
        var dealStatusText = dealStatus == "2"?"确定要处理成功?":"确定要处理失败?";
        SweetAlert.swal({
                title: "",
                text: dealStatusText,
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http({
                        url:"zqServiceInfo/updateDealStatus",
                        method:"post",
                        data:{id:entity.id, dealStatus:dealStatus}
                    }).success(function(result){
                        $scope.notice(result.msg);
                        if(result.status){
                            $scope.query();
                        }
                    });
                }
            });
    };

    //页面绑定回车事件
    $document.bind ("keypress", function(event) {
        $scope.$apply(function (){
            if(event.keyCode == 13){
                $scope.query();
            }
        })
    });

});