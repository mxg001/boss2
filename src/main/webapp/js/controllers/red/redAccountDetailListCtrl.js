/**
 * 红包账户明细查询
 */
angular.module('inspinia').controller('redAccountDetailListCtrl', function($scope, $http,$stateParams,i18nService, SweetAlert){
    //数据源
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    //(0平台、1品牌商、2个人)
    $scope.userType= [{text:"平台",value:"0"},{text:"品牌商",value:"1"},{text:"个人",value:"2"}];
    $scope.userTypeStr=angular.toJson($scope.userType);
    $scope.resetForm = function () {
       /* $scope.baseInfo = {createDateStart:moment(new Date().getTime()-7*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
            createDateEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
            type:"",busType:"",id:$stateParams.id};*/

        $scope.baseInfo = {createDateStart:moment(new Date().getTime()-30*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
            createDateEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',userType:"1",type:"",busType:""};
    }
    $scope.resetForm();
    
    $scope.columnDefs = [
         {field: 'typeInter',displayName:'',pinnable: false,sortable: false,width:0},
         {field: 'id',displayName: '记账流水号',pinnable: false,sortable: false,width:150},
        {field: 'createDateStr',displayName: '记账时间',pinnable: false,sortable: false,width:150},
        {field: 'transAmountStr',displayName: '记账金额',pinnable: false,sortable: false,width:150},
        {field: 'beforetransredmoneytStr',displayName: '记账前账户余额',pinnable: false,sortable: false,width:150},
        {field: 'redmoney',displayName: '记账后账户余额',pinnable: false,sortable: false,width:150},
        {field: 'accountName',displayName: '账户名称',pinnable: false,sortable: false,width:150},
        {field: 'userType',displayName: '用户类型',pinnable: false,sortable: false,width:150,cellFilter:"formatDropping:"+$scope.userTypeStr},
        {field: 'relationId',displayName: '用户ID/组织编号',pinnable: false,sortable: false,width:150},
   /*     {field: 'userOrgName',displayName: '用户或者组织名称',pinnable: false,sortable: false,width:150},*/
        {field: 'orderNo',displayName: '关联订单号',pinnable: false,sortable: false,width:150},
        {field: 'type',displayName: '变动类型',pinnable: false,sortable: false,width:150},
        {field: 'redOrderId',displayName: '对应红包ID',pinnable: false,sortable: false,width:150,cellTemplate:
            /*'<a class="lh30" target="_blank"  '
            + 'ui-sref="red.redEnvelopesGrantDetail({id:row.entity.redOrderId})">{{row.entity.redOrderId}}</a>'*/
                '<span ng-show="row.entity.typeInter>=8">  <a class="lh30" target="_blank" ui-sref="red.manorTransactionRecoreDetail({orderId:row.entity.redOrderId})">{{row.entity.redOrderId}}</a></span>'
                +'<span ng-show="row.entity.typeInter<8">  <a class="lh30" target="_blank" ui-sref="red.redEnvelopesGrantDetail({id:row.entity.redOrderId})">{{row.entity.redOrderId}}</a></span>'
        },
        {field: 'busType',displayName: '红包业务类型',pinnable: false,sortable: false,width:150,cellTemplate:
                '<span ng-show="row.entity.typeInter>=8">领地开关</span>'
                +'<span ng-show="row.entity.typeInter<8">{{row.entity.busType}}</span>'
        }
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
    $scope.diffDate = function () {
        var createDateStart=new Date($scope.baseInfo.createDateStart);
        var createDateEnd=new Date($scope.baseInfo.createDateEnd);
        var  diffDate=createDateEnd.getTime()-createDateStart.getTime();
        var days=Math.floor(diffDate/(24*3600*1000))
        if(days>90){
            return false;
        }
        return true;
    }
    $scope.query = function () {
        if (!$scope.diffDate()) {
            $scope.notice("查询范围不能超过三个月");
            return;
        }
        if (!$scope.baseInfo.createDateStart) {
            $scope.notice("记账初始时间不能为空");
            return;
        }
        if (!$scope.baseInfo.createDateEnd) {
            $scope.notice("记账结束时间不能为空");
            return;
        }
        $scope.submitting = true;
        $scope.loadImg = true;
        $http({
            url: 'manor/redAccountDetailList?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
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
    $scope.exportInfo=function(){
        if (!$scope.baseInfo.createDateStart) {
            $scope.notice("记账初始时间不能为空");
            return;
        }
        if (!$scope.baseInfo.createDateEnd) {
            $scope.notice("记账结束时间不能为空");
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
                    location.href="manor/exportRedAccountDetail?baseInfo="+encodeURI(angular.toJson($scope.baseInfo));
                }
            });
    };
    
});
