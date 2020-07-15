/**
 * 平台红包账户查询
 */
angular.module('inspinia').controller('plateAccountCtrl', function($scope, $http,$stateParams,i18nService, SweetAlert){
    //数据源
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    //(0平台、1品牌商、2个人)
    $scope.userType= [{text:"平台",value:"0"},{text:"品牌商",value:"1"},{text:"个人",value:"2"}];
    $scope.userTypeStr=angular.toJson($scope.userType);
    
    $scope.resetForm = function () {
        $scope.baseInfo = {createDateStart:moment(new Date().getTime()-7*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
            createDateEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
            type:"",busType:"",id:$stateParams.id};
    }
    $scope.resetForm();
    
    $scope.columnDefs = [
         {field: 'typeInter',displayName:'',pinnable: false,sortable: false,width:0},
         {field: 'orderNo',displayName: '订单编号',pinnable: false,sortable: false},
        {field: 'accountName',displayName: '账户名称',pinnable: false,sortable: false},
        {field: 'userType',displayName: '用户类型',pinnable: false,sortable: false,cellFilter:"formatDropping:"+$scope.userTypeStr},
        {field: 'type',displayName: '变动类型',pinnable: false,sortable: false},
        {field: 'redOrderId',displayName: '对应红包ID',pinnable: false,sortable: false,cellTemplate:
            /*'<a class="lh30" target="_blank"  '
            + 'ui-sref="red.redEnvelopesGrantDetail({id:row.entity.redOrderId})">{{row.entity.redOrderId}}</a>'*/
            '<span ng-show="row.entity.typeInter>=8">  <a class="lh30" target="_blank" ui-sref="red.manorTransactionRecoreDetail({orderId:row.entity.redOrderId})">{{row.entity.redOrderId}}</a></span>'
            +'<span ng-show="row.entity.typeInter<8">  <a class="lh30" target="_blank" ui-sref="red.redEnvelopesGrantDetail({id:row.entity.redOrderId})">{{row.entity.redOrderId}}</a></span>'
            },
        {field: 'busType',displayName: '红包业务类型',pinnable: false,sortable: false,cellTemplate:
        	'<span ng-show="row.entity.typeInter>=8">领地开关</span>'
            +'<span ng-show="row.entity.typeInter<8">{{row.entity.busType}}</span>'
        },
        {field: 'createDateStr',displayName: '记账时间',pinnable: false,sortable: false},
        {field: 'transAmountStr',displayName: '金额',pinnable: false,sortable: false},
        {field: 'redmoney',displayName: '红包账户金额',pinnable: false,sortable: false},
        {field: 'remark',displayName: '备注',pinnable: false,sortable: false}
    ];

    $scope.grid = {
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
//		rowHeight:35,
        columnDefs: $scope.columnDefs,
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            $scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.paginationOptions.pageNo = newPage;
                $scope.paginationOptions.pageSize = pageSize;
                $scope.query();
            });
        }
    };

    $scope.queryAccount = function () {
        $http({
           // url: 'red/plateAccountInfo?id='+$stateParams.id,
            url: 'manor/plateAccountInfo?id='+$stateParams.id,
            method:'GET'
        }).success(function (result) {
            console.log(result)
            if (result.status){
                $scope.accountInfo = result.data;
            }
        });
    }
    $scope.queryAccount();
    
    $scope.query = function () {
        $scope.queryAccount()
        $scope.submitting = true;
        $scope.loadImg = true;
        $http({
        //    url: 'red/plateAccountDetail?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
            url: 'manor/plateAccountDetail?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
            data: $scope.baseInfo,
            method:'POST'
        }).success(function (result) {
            $scope.submitting = false;
            $scope.loadImg = false;
            if (!result.status){
                $scope.notice(result.msg);
                return;
            }
            $scope.grid.data = result.data.page.result;
            $scope.grid.totalItems = result.data.page.totalCount;
            $scope.totalInfo = result.data.totalInfo;
        });
    };
    // $scope.query();

    $scope.exportInfo=function(){
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
                    location.href="manor/exportPlateAccountDetail?baseInfo="+encodeURI(angular.toJson($scope.baseInfo));
                }
            });
    };
    
});
