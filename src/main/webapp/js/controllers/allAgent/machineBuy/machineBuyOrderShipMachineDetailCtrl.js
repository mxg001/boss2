/**
 * 发货信息
 */
angular.module('inspinia').controller('machineBuyOrderShipMachineDetailCtrl',function($scope,$http,i18nService,$state,$stateParams,$q){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    $scope.info={};
    $scope.good={};
    var promises = [];
    $scope.hardTypeList = [];
    var hpPromise=$q.defer();
    promises.push(hpPromise.promise);
    $http.get('hardwareProduct/selectAllInfo.do')
        .success(function(result){
            if(!result)
                return;
            $scope.termianlTypes=result;
            $scope.termianlTypes.splice(0,0,{hpId:"-1",typeName:"全部"});
            angular.forEach(result,function(data){
                $scope.hardTypeList.push({text:data.typeName,value:""+data.hpId});
            });
            hpPromise.resolve();
            delete hpPromise;
        })
    $q.all(promises).then(function(){
        // 获取数据完成了
        promises = [];
        initGrid();
    });

    $http.post("goodAllAgent/getGoodsCode","goodsCode="+$stateParams.goodCode,
        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function(data){
            if(data.status){
                $scope.good=data.good;
            }
        });

    $scope.SNGrid = {
        data: 'result',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100,500],	  //切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.paginationOptions.pageNo = newPage;
                $scope.paginationOptions.pageSize = pageSize;
                $scope.query();
            });
        }
    };
    function initGrid(){
        $scope.SNGrid.columnDefs = [                           //表格数据
            {field: 'sn', displayName: 'SN号',width: 300},
            { field: 'type',displayName:'硬件产品种类',width:300,cellFilter:"formatDropping:"+angular.toJson($scope.hardTypeList)}
        ];
    }

    $scope.query=function(){
        if ($scope.loadImg) {
            return;
        }
        $scope.loadImg = true;
        $http.post("machineBuyOrder/queryShipMachineDetail","orderNo="+$stateParams.orderNo+"&pageNo="+
            $scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.info=data.info;
                    $scope.result=data.page.result;
                    $scope.SNGrid.totalItems = data.page.totalCount;
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
    $scope.query();
});