/**
 * 盟主商户查询
 */
angular.module('inspinia').controller('merchantAllAgentQueryCtrl',function($scope,$http,i18nService,SweetAlert,$document){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    //清空
    $scope.clear=function(){
        $scope.info={userCode:"",realName:"",mobile:"",merchantCode:"",};
    };
    $scope.clear();

    $scope.clearCount=function () {
        $scope.countSet={merchantCount:0};
    };
    $scope.clearCount();

    $scope.merchantGrid={                           //配置表格
        data: 'result',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	  //切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'merchantNo',displayName:'商户编号',width:180},
            { field: 'merchantName',displayName:'商户名称',width:180},
            { field: 'mobilePhone',displayName:'商户手机号码',width:180},
            { field: 'userCode',displayName:'所属盟主编号',width:180},
            { field: 'realName',displayName:'所属盟主姓名',width:180},
            { field: 'nickName',displayName:'所属盟主昵称',width:180},
            { field: 'mobile',displayName:'所属盟主手机',width:180},
            { field: 'oneUserCode',displayName:'所属机构编号',width:180},
            { field: 'oneAgentNo',displayName:'所属机构代理商编号',width:180},
            { field: 'oneAgentName',displayName:'所属机构代理商名称',width:180 },
            { field: 'brandName',displayName:'所属品牌',width:180},
            { field: 'createTime',displayName:'注册日期',width:180,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"' }
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
        $http.post("merchantAllAgent/selectMerchantAllAgent","info="+angular.toJson($scope.info)+"&pageNo="+
            $scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.result=data.page.result;
                    $scope.merchantGrid.totalItems = data.page.totalCount;
                    $scope.countSet=data.countSet;
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

    //页面绑定回车事件
    $document.bind("keypress", function(event) {
        $scope.$apply(function (){
            if(event.keyCode == 13){
                $scope.query();
            }
        })
    });
});