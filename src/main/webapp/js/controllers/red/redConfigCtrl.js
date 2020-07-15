/**
 * 红包配置管理
 */
angular.module('inspinia').controller('redConfigCtrl',function($scope, i18nService,SweetAlert,$http, $state){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文

    $scope.updateRedInfo={};
    $scope.clear = function () {
        $scope.baseInfo={"bus_type":"","push_type":"",
            "receive_type":"","push_area":"","status":"",
            "push_org_id":-1};
    }
    $scope.clear();
    var busType = [];
    angular.forEach($scope.redBusTypes, function(item){
        if(item.value!="0"){
            busType[busType.length] = item;
        }
    });
    $scope.redBusTypes = busType;
    $scope.arrayList={};
    $scope.orgs=[];
    $scope.getAllOrg = function(){
        $http({
            url:"superBank/getOrgInfoList",
            method:"POST"
        }).success(function(msg){
            if(msg.status){
                $scope.orgs = msg.data;
                $scope.orgs.unshift({orgId:0,orgName:"平台"});
                $scope.orgs.unshift({orgId:-1,orgName:"全部"});
            }
        }).error(function(){
            $scope.notice("获取组织列表异常");
        });
    };
    $scope.getAllOrg();

    $scope.redConfigGridDefs = [
        {field: 'id',displayName: '红包配置id',width: 150,pinnable: false,sortable: false },
        {field: 'status',displayName: '红包配置状态',width: 150,pinnable: false,sortable: false,cellFilter:"formatDropping:"+angular.toJson($scope.redStatus)},
        {field: 'bus_type',displayName: '业务类型',width: 150,pinnable: false,sortable: false,cellFilter:"formatDropping:"+angular.toJson($scope.redBusType)},
        {field: 'push_type',displayName: '发放人类型',width: 150,pinnable: false,sortable: false,cellFilter:"formatDropping:"+angular.toJson($scope.redPushType)},
        {field: 'push_org_name',displayName: '发放人组织',width: 140,pinnable: false,sortable: false},
        {field: 'receive_type',displayName: '接收类型',width: 150,pinnable: false,sortable: false,cellFilter:"formatDropping:"+angular.toJson($scope.redReceiveType)},
        {field: 'push_area',displayName: '发放范围',width: 140,pinnable: false,sortable: false,cellFilter:"formatDropping:"+angular.toJson($scope.redPushArea)},
        {field: 'red_amount',displayName: '红包金额',width: 140,pinnable: false,sortable: false},
        {field: 'red_number',displayName: '领取人数',width: 140,pinnable: false,sortable: false},
        {field: 'total_amount',displayName: '活动总金额',width: 140,pinnable: false,sortable: false},
        // {field: 'allow_org_profit',displayName: '是否超过品牌分润',width: 160,pinnable: false,sortable: false,cellFilter:"formatDropping:[{value:0,text:'不允许'},{value:1,text:'允许'}]"},
        {field: 'create_time',displayName: '创建时间',width: 140,pinnable: false,sortable: false ,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'update_time',displayName: '修改时间',width: 150,pinnable: false,sortable: false,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'action',displayName: '操作',width: 200,pinnedRight:true,
            cellTemplate:
            '<div  class="lh30" ><a ui-sref="red.redConfigDetail({id:row.entity.id})">详情</a>' +
            ' <a ng-show="grid.appScope.hasPermit(\'red.editRedConfig\')"  ui-sref="red.editRedConfig({id:row.entity.id})">修改</a>' +
            ' <a ng-show="grid.appScope.hasPermit(\'red.redConfigUpdStatus\')"  ng-click="grid.appScope.updateResStatusOpen(row.entity)">修改状态</a></div>'
        }    ];


    $scope.updateResStatusOpen = function (entity) {
        $scope.updateRedInfo = angular.copy(entity);
        $("#updateResStatusModal").modal("show");
    }
    $scope.cancel = function(){
        $("#updateResStatusModal").modal("hide");
    }
    $scope.updateStatus = function () {
        $http({
            url: "red/redConfigUpdStatus",
            data: $scope.updateRedInfo,
            method: "post"
        }).success(function (result) {
            $scope.notice(result.msg);
            if (result.status) {
                $("#updateResStatusModal").modal("hide");
                $scope.query();
            }
        })
    };

    

    $scope.redConfigGrid = {
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		  	//开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs: $scope.redConfigGridDefs ,
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            $scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.paginationOptions.pageNo = newPage;
                $scope.paginationOptions.pageSize = pageSize;
                $scope.query();
            });
        }
    };

    $scope.query = function(){
        $scope.submitting = true;
        $scope.loadImg = true;

        $http({
            url: 'red/redConfig?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
            data:$scope.baseInfo,
            method:'post'
        }).success(function (msg) {
            $scope.submitting = false;
            $scope.loadImg = false;
            if (!msg.status){
                $scope.notice(msg.msg);
                return;
            }
            $scope.redConfigGrid.data = msg.data.result;
            $scope.redConfigGrid.totalItems = msg.data.totalCount;
        }).error(function (msg) {
            $scope.submitting = false;
            $scope.loadImg = false;
            $scope.notice('服务器异常,请稍后再试.');
        });
    }
    $scope.query();

});