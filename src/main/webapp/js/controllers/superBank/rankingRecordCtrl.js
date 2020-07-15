/**
 * 排行榜管理
 */
angular.module('inspinia',['angularFileUpload']).controller('rankingRecordCtrl',function($scope, $http, $state,FileUploader,SweetAlert){

	//返回上页
    $scope.goback = function () {
    	history.go(-1);
    };

    $scope.isDisabled = false;
    
    
    $scope.columnDefs = [
         {field: 'rankingNo',displayName: '排行榜编号',width: 200,pinnable: false,sortable: false},
         {field: 'batchNo',displayName: '期号',width: 150,pinnable: false,sortable: false},
         {field: 'ruleNo',displayName: '排行榜规则编号',width: 150,pinnable: false,sortable: false},
         {field: 'rankingName',displayName: '排行榜名称',width: 150,pinnable: false,sortable: false},
         {field: 'rankingType',displayName: '统计周期',width: 150,pinnable: false,sortable: false},
         {field: 'dataType',displayName: '统计数据',width: 150,pinnable: false,sortable: false},
         {field: 'orgName',displayName: '所属组织',width: 150,pinnable: false,sortable: false},
         {field: 'pushNum',displayName: '本期获奖人数',width: 150,pinnable: false,sortable: false},
         {field: 'pushTotalAmount',displayName: '本期奖金',width: 150,pinnable: false,sortable: false},
         {field: 'pushRealNum',displayName: '实发人数',width: 150,pinnable: false,sortable: false},
         {field: 'pushRealAmount',displayName: '实发奖金',width: 150,pinnable: false,sortable: false},
         {field: 'status',displayName: '榜单状态',width: 150,pinnable: false,sortable: false},
         {field: 'createDate',displayName: '榜单生成时间',width: 200,pinnable: false,sortable: false,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
         {field: 'startDate',displayName: '统计开始时间',width: 200,pinnable: false,sortable: false,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
         {field: 'endDate',displayName: '统计截止时间',width: 200,pinnable: false,sortable: false,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
         {field: 'action',displayName: '操作',width: 180,pinnedRight:true,sortable: false,editable:true,cellTemplate:
         '<a class="lh30" target="_blank" ng-show="grid.appScope.hasPermit(\'superBank.rankingRecordDetail\')" '
         + 'ui-sref="superBank.findRankingRecordDetail({recordId:row.entity.id})">详情</a> <input ng-show="false" ng-model="row.entity.id"/>' + 
         ' <a class="lh30" target="_blank" ng-show="row.entity.status == \'未发放\'" '
         + 'ui-sref="superBank.rankingRecordDetail({recordId:row.entity.id})">审核</a>' +
         ' <a class="lh30" target="_blank" ng-show="row.entity.status == \'未发放\'" '
         + 'ng-click="grid.appScope.pushBonus({recordId:row.entity.id,orgName:row.entity.orgName,rankingNo:row.entity.rankingNo,rankingName:row.entity.rankingName})">发放奖金</a>'}
     ];

     $scope.orderGrid = {
         paginationPageSize:10,                  //分页数量
         paginationPageSizes: [10,20,50,100],	//切换每页记录数
         useExternalPagination: true,		  //开启拓展名
         enableHorizontalScrollbar: true,        //横向滚动条
         enableVerticalScrollbar : false,  		//纵向滚动条
//                 		rowHeight:35,
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

     $scope.query = function () {
         $scope.submitting = true;
         $scope.loadImg = true;
         $http({
             url: 'superBank/rankingRecord?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
             data: $scope.baseInfo,
             method:'POST'
         }).success(function (result) {
             $scope.submitting = false;
             $scope.loadImg = false;
             if (!result.status){
                 $scope.notice(result.msg);
                 return;
             }
             $scope.orderGrid.data = result.data.page.result;
             $scope.orderGrid.totalItems = result.data.page.totalCount;
             $scope.orderMainSum = result.data.orderMainSum;
         }).error(function () {
             $scope.submitting = false;
             $scope.loadImg = false;
             $scope.notice('服务器异常,请稍后再试.');
         });
     };

     /**发放奖金*/
     $scope.pushBonus = function(data){
    	 $scope.loadImg = true;
    	 SweetAlert.swal({
             title: "确定发放组织："+data.orgName+"    排行榜名称："+data.rankingName+"    排行榜编号："+data.rankingNo+"   吗？ \n\n  奖金发放后将不可收回，请核对无误再确认！",
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
                     url: 'superBank/pushRankingBonus?recordId='+data.recordId,
                     method:"GET"
                 }).success(function (result) {
                     $scope.pushSubmitting = true;
                     $scope.loadImg = false;
                     if (!result.status){
                         $scope.notice(result.msg);
                         return;
                     }
                     $scope.notice(result.msg);
                     $scope.query();
                     
                 }).error(function () {
                     $scope.loadImg = false;
                     $scope.notice('服务器异常,请稍后再试.');
                 });
             }else{
            	 $scope.loadImg = false;
             }
         });
     }
  
     //0周榜 1月榜 2年榜
    $scope.types = [{value:"",text:"全部"},{value:"0",text:"周榜 "},{value:"1",text:"月榜"},{value:"2",text:"年榜"}];
    //0未生成 1未发放  2已经发放  3发放完成

    $scope.statuses = [{value:"",text:"全部"},{value:"1",text:"未发放"},{value:"2",text:"已发放"}];
    //0收益金额 1会员数 2用户数
    $scope.dataTypes = [{value:"",text:"全部"},{value:"0",text:"收益金额"},{value:"1",text:"会员数"},{value:"2",text:"用户数"},{value:"3",text:"订单数"}];
    
    $scope.baseInfo = {"status":"","rankingType":"","dataType":"","orgId":"-1"};

    
    //组织列表
    $scope.orgs=[];
    $scope.getAllOrg = function(){
   	 $http({
            url:"superBank/getOrgInfoList",
            method:"POST"
        }).success(function(msg){
            if(msg.status){
                $scope.orgs = msg.data;
                $scope.orgs.unshift({orgId:'-1',orgName:"全部"});
            }
        }).error(function(){
            $scope.notice("获取组织列表异常");
        });
   };
   $scope.getAllOrg();
  
   $scope.resetForm = function (){
	   $scope.baseInfo = {"rankingType":"","status":"","rankingName":"","ruleNo":"","orgId":"-1","dataType":""};
   };
    $scope.resetForm();
   
});