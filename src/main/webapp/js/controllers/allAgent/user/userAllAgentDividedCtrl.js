/**
 * 盟主列表
 */
angular.module('inspinia').controller('userAllAgentDividedCtrl',function($scope,$http,i18nService,SweetAlert,$document,$stateParams){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    $scope.userCode=$stateParams.userCode;

    $scope.dividedGrid={                           //配置表格
        data: 'result',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'gradeRate',displayName:'当前分润等级',width:180,
                cellTemplate:'<div ng-show="row.entity.gradeRate!=null">' +
                'Lv.{{row.entity.gradeRate}}</div>'},
            { field: 'shareRatio',displayName:'当前分润比例',width:180,
                cellTemplate:'<div ng-show="row.entity.shareRatio!=null">' +
                '万{{row.entity.shareRatio}}</div>'},
            { field: 'preRate',displayName:'调整前分润比例',width:180,
                cellTemplate:'<div ng-show="row.entity.preRate!=null">' +
                '万{{row.entity.preRate}}</div>'},
            { field: 'afterRate',displayName:'调整后分润比例',width:180,
                cellTemplate:'<div ng-show="row.entity.afterRate!=null">' +
                '万{{row.entity.afterRate}}</div>'},
            { field: 'createTime',displayName:'调整日期',width:180,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'}
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
        $http.post("userAllAgent/selectDividedAdjustDetail","userCode="+$stateParams.userCode+"&pageNo="+
            $scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.result=data.page.result;
                    $scope.dividedGrid.totalItems = data.page.totalCount;
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
    //页面绑定回车事件
    $document.bind("keypress", function(event) {
        $scope.$apply(function (){
            if(event.keyCode == 13){
                $scope.query();
            }
        })
    });
});