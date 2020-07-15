/**
 * 保险订单详情
 */
angular.module('inspinia').controller('safeOrderDetailCtrl',function($scope,$http,i18nService,$stateParams){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文

    //承保单位
    $scope.bxUnitSelect =angular.copy($scope.insurerList);
    $scope.bxUnitStr=angular.toJson($scope.bxUnitSelect);

    //结算方式
    $scope.settlementMethodSelect=[{text:'全部',value:""},{text:'T1',value:'1'},{text:'T0',value:'0'}];
    $scope.settlementMethodStr=angular.toJson($scope.settlementMethodSelect);

    //投保状态
    $scope.bxTypeSelect=[{text:'全部',value:""},
        {text:'初始化',value:'INIT'},{text:'投保成功',value:'SUCCESS'},{text:'投保失败',value:'FAILED'},
        {text:'已退保',value:'OVERLIMIT'},{text:'退保失败',value:'RECEDEFAILED'}
    ];
    $scope.bxTypeStr=angular.toJson($scope.bxTypeSelect);

    //性质
    $scope.cClntMrkSelect=[{text:'自然人',value:'1'},{text:'非自然人',value:'0'}];
    //证件类型
    $scope.cCertfClsSelect=[{text:'居民身份证',value:'120001'},{text:'护照',value:'120002'},
        {text:'军人证',value:'120003'},{text:'其他',value:'120009'}];
    //与被保人关系
    $scope.cRelCodeSelect=[{text:'雇佣',value:'601001'},{text:'子女',value:'601002'},
        {text:'父母',value:'601003'},{text:'配偶',value:'601004'},{text:'本人',value:'601005'},
        {text:'其它',value:'601006'}];
    //性别
    $scope.cSexSelect=[{text:'男',value:'1'},{text:'女',value:'2'}];

    $scope.cArrivalTimeSelect=[{text:'24小时',value:'1503006'},{text:'48小时',value:'1503007'}];

    $scope.info={};

    $http.post("safeOrder/getSafeOrderDetail","id="+$stateParams.id,
        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function(data){
            if(data.status){
                $scope.info=data.order;
            }
        });

    /**
     * 获取敏感数据
     */
    $scope.dataSta=true;
    $scope.getDataProcessing = function () {
        if($scope.dataSta){
            $http.post("safeOrder/getDataProcessing","id="+$stateParams.id,
                {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                .success(function(data) {
                    if(data.status){
                        $scope.info.cMobile = data.order.cMobile;
                        $scope.info.cCertfCde = data.order.cCertfCde;
                        $scope.info.cMobile1 = data.order.cMobile1;
                        $scope.info.cCertNo = data.order.cCertNo;
                        $scope.dataSta=false;
                    }else{
                        $scope.notice(data.msg);
                    }
                });
        }
    };
});