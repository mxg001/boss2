/**
 * 抽奖信息查询
 */
angular.module('inspinia').controller('luckDrawOrderDetailCtrl',function($scope,$http,i18nService,SweetAlert,$document,$stateParams){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    $scope.statusSelect = [{text:"全部",value:-1},{text:"未中奖",value:1},{text:"已中奖",value:2},
        {text:"已发奖",value:3}];
    $scope.statusStr=angular.toJson($scope.statusSelect);

    $scope.status1Select = [{text:"未发放",value:0},{text:"已发放",value:1}];
    $scope.status1Str=angular.toJson($scope.status1Select);

    $scope.userGrid={                           //配置表格
        data: 'result',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'id',displayName:'ID',width:80},
            { field: 'couponId',displayName:'券ID',width:100 },
            { field: 'couponName',displayName:'券名称',width:180 },
            { field: 'money',displayName:'金额',width:120 },
            { field: 'status',displayName:'状态',width:100,cellFilter:"formatDropping:" +  $scope.status1Str },
            { field: 'createTime',displayName:'创建时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180}
        ],
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
        }
    };
    //获取抽奖详情
    $scope.getAddInfo=function () {
        $http.post("luckDrawOrder/getLuckDrawOrder","id="+$stateParams.id,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.info=data.info;
                    $scope.result=$scope.info.entryList;
                }
            });
    };
    $scope.getAddInfo();

    //页面绑定回车事件
    $document.bind("keypress", function(event) {
        $scope.$apply(function (){
            if(event.keyCode == 13){
                $scope.query();
            }
        })
    });
});