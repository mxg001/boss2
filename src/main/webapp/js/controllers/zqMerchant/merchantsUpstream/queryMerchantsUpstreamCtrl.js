/**
 * 直清商户查询
 */

angular.module('inspinia').controller('queryMerchantsUpstreamCtrl', function ($scope, $http, $state, $stateParams,$document,SweetAlert,i18nService) {
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    //数据源
    $scope.info = {unionpayMerNo:null};
    $scope.unlist=null;
    //清空
    $scope.clear = function () {
        $scope.info = {unionpayMerNo:null};
    };

    $scope.query=function(){
        if($scope.info.unionpayMerNo==null||$scope.info.unionpayMerNo==""){
            $scope.notice("银联报备商户编号不能为空!");
            return;
        }
        $http.post("merchantsUpstream/selectByParam.do","baseInfo=" + angular.toJson($scope.info)+"&pageNo="+
            $scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
            {headers: {'Content-Type':'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.unlist=data.unlist;
                    $scope.result=data.page.result;
                    $scope.gridOptions.totalItems = data.page.totalCount;
                }else{
                    $scope.info.unlist=null;
                    $scope.notice(data.msg);
                }

            });
    }
    //$scope.query();手动查询

    $scope.gridOptions={                           //配置表格
        data: 'result',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'rowNo',displayName:'序号',width:100 },
            { field: 'channelCode',displayName:'通道名称',width:180 },
            { field: 'unionpayMerNo',displayName:'银联报备商户编号',width:180 },
            { field: 'mbpId',displayName:'商户进件编号',width:180 },
            { field: 'merchantNo',displayName:'商户编号',width:180 },
            { field: 'merchantName',displayName:'商户名称',width:180 },
            { field: 'mobilephone',displayName:'手机号',width:180 },
            { field: 'balance',displayName:'账户余额',width:180 },
            { field: 'bankAccount',displayName:'银行账号',width:180 },
            { field: 'bankCode',displayName:'银行编码',width:180 },
            { field: 'bankName',displayName:'银行名称',width:180 },
            { field: 'wxRates',displayName:'微信费率档',width:180 },
            { field: 'zfbRates',displayName:'支付宝费率档',width:180 },
            { field: 'result',displayName:'查询结果',width:180 },
            { field: 'errorMsg',displayName:'错误描述',width:180 }
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

    $scope.import=function(){
        if($scope.info.unionpayMerNo==null||$scope.info.unionpayMerNo==""){
            $scope.notice("银联报备商户编号不能为空!");
            return;
        }
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
                    location.href="merchantsUpstream/exportDetail?baseInfo="+encodeURI(angular.toJson($scope.info));
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