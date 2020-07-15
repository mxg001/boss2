/**
 * 超级兑用户详情/编辑
 */
angular.module('inspinia').controller('userManagementDetailCtrl',function($scope,$state,$http,$stateParams,i18nService){
    //数据源
    i18nService.setCurrentLang('zh-cn');
    $scope.merAccountSelect = [{text:"是",value:1},{text:"否",value:0}];
    $scope.merAccountStr=angular.toJson($scope.merAccountSelect);
    $scope.statusSelect = [{text:"全部",value:""},{text:"正常",value:"1"},{text:"不进不出",value:"2"},{text:"只进不出",value:"3"}];
    $scope.statusStr=angular.toJson($scope.statusSelect);
    $scope.merActStatusSelect = [{text:"全部",value:""},{text:"未激活",value:"1"},{text:"已激活",value:"2"}];
    $scope.merActStatusStr=angular.toJson($scope.merActStatusSelect);

    $scope.merCapaSelect=$scope.merCapaExchange;
    $scope.merCapaStr=angular.toJson($scope.merCapaSelect);

    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    $scope.debitCreditSideList=[{text:"全部",value:""},{text:"减少",value:"debit"},
        {text:"增加",value:"credit"},{text:"冻结",value:"freeze"},
        {text:"解冻",value:"unfreeze"}];

    $scope.freezeStatusSelect = [{text:"解冻",value:"0"},{text:"预冻结",value:"1"}];
    $scope.freezeStatusStr=angular.toJson($scope.freezeStatusSelect);


    //清空
    $scope.clear=function(){
        $scope.accountInfoRecord = {
            debitCreditSide :"",
            userId:$stateParams.merchantNo,
            recordTimeStart:moment(new Date().getTime()-24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
            recordTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'
        };
    };
    $scope.clear();

    //获取用户详情
    $scope.getUserDetail=function(){
        $http.post("userManagement/getUserManagement","merchantNo="+$stateParams.merchantNo,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.info=data.user;
                    $scope.dateMap=data.dateMap;
                    $scope.accountBalances.data=data.balance;
                    $scope.userFreezeResult=data.freezeList;
                }else{
                    $scope.notice(data.msg);
                }
            }).error(function(){
                 $scope.notice("系统异常，请稍后再试");
            });
    };
    $scope.getUserDetail();

    $scope.userFreezeGrid={                           //配置表格
        data: 'userFreezeResult',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'freezeOper',displayName:'操作人',width:150},
            { field: 'createTime',displayName:'操作时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'freezeStatus',displayName:'操作状态',width:120,cellFilter:"formatDropping:" +  $scope.freezeStatusStr},
            { field: 'freezeAmount',displayName:'预冻结金额',width:180,cellFilter:"currency:''" },
            { field: 'remark',displayName:'核销原因',width:600}
        ],
        onRegisterApi: function(gridApi) {
            $scope.userFreezeGridApi = gridApi;
        }
    };

    //账户余额
    $scope.accountBalances = {                 //配置表格
        columnDefs:[                           //表格数据
            { field: 'accountNo',displayName:'账号'},
            { field: 'balance',displayName:'余额'},
            { field: 'controlAmount',displayName:'冻结金额' }
        ]
    };

    //账户明细
    $scope.accountDetail={                           //配置表格
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
        useExternalPagination: true,
        enableHorizontalScrollbar: 1,        //横向滚动条
        enableVerticalScrollbar : 1,
        columnDefs:[                           //表格数据
            { field: 'recordDateTime',displayName:'记账时间',cellTemplate:"<div class='lh30'>{{row.entity.recordDate | date:'yyyy-MM-dd'}} {{row.entity.recordTime | date:'HH:mm:ss'}}</div>"
            },
            { field: 'debitCreditSide',displayName:'操作',cellFilter:"formatDropping:" + angular.toJson($scope.debitCreditSideList)},
            { field: 'recordAmount',displayName:'金额' },
            { field: 'avaliBalance',displayName:'可用余额' }
        ],
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            $scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.paginationOptions.pageNo = newPage;
                $scope.paginationOptions.pageSize = pageSize;
                $scope.getUserAccountDetail();
            });
        }
    };
    //获取用户账户明细
    $scope.getUserAccountDetail = function(){
        if($scope.loadImg){
            return;
        }
        $scope.loadImg = true;
        $http({
            url:"userManagement/getUserAccountDetail?pageNo=" + $scope.paginationOptions.pageNo + "&pageSize=" + $scope.paginationOptions.pageSize,
            data:$scope.accountInfoRecord,
            method:"POST"
        }).success(function(result){
            $scope.loadImg = false;
            if(result.status){
                $scope.accountDetail.data = result.data.list;
                $scope.accountDetail.totalItems = result.data.total;
            }else{
                $scope.notice(result.msg);
            }
        }).error(function(){
            $scope.notice("系统异常，请稍后再试");
            $scope.loadImg = false;
        });
    };

    $scope.updateCardModal = function(){
        $scope.newCard = {cardNo:"",bankName:"",accountPhone:"",bankProvince:"",bankCity:"",cnapsNo:"",
            merchantNo:$stateParams.merchantNo};
        $scope.bankList = [{cnapsNo:"",bankName:"请选择"}];
        $('#updateCardModal').modal('show');
    }
    $scope.cancel = function(){
        $('#updateCardModal').modal('hide');
    }
    $scope.getAreaList = function (name, type, callback) {
        if (name == null || name == "undefine") {
            return;
        }
        $http.post('areaInfo/getAreaByName', 'name=' + name + '&&type=' + type,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
        ).success(function (data) {
            callback(data);
        }).error(function () {
        });
    }

    //省，加载页面时自动触发
    $scope.getAreaList(0, "p", function (data) {
        $scope.provinceList = data;
    });
    //修改省时，获取市的信息
    $scope.getCities = function () {
        $scope.areaList = [];
        $scope.getAreaList($scope.newCard.bankProvince, "", function (data) {
            $scope.cityList = data;
        });
    }
    //修改市时，获取区的信息
    $scope.getAreas = function () {
        $scope.getAreaList($scope.newCard.bankCity, "", function (data) {
            $scope.areaList = data;
        });
    };
    $scope.getCardInfo = function(cardNo){
        if(!cardNo){
            return;
        }
        $http({
            url:"superBank/getCardInfo?cardNo=" + cardNo,
            method:"GET"
        }).success(function(result){
            if(result.status){
                $scope.cardMsg = "";
                $scope.newCard.bankName = result.data.bankName;
                $scope.getBankList($scope.newCard.bankName, $scope.newCard.bankCity);
            } else {
                $scope.cardMsg = result.msg;
                $scope.notice(result.msg);
            }
        })
    };

    //获取支行集合
    $scope.getBankList = function(bankName, cityName){
        if(!bankName || !cityName){
            return;
        }
        $http({
            method:"POST",
            url:"superBank/getPosCnaps",
            data:{"bankName":bankName, "address":cityName}
        }).success(function(result){
            if(result.status){
                if(result.data && result.data.length > 0){
                    $scope.bankList = result.data;
                    $scope.bankList.unshift({cnapsNo:"",bankName:"请选择"});
                }
            } else {
                $scope.notice(result.msg);
            }
        })
    };

    //修改结算卡
    $scope.updateUserCard = function () {
        if($scope.submitting){
            return;
        }
        if($scope.newCard.cardNo==null||$scope.newCard.cardNo==""){
            $scope.notice("银行卡号不能为空!");
            return;
        }
        if($scope.newCard.bankName==null||$scope.newCard.bankName==""){
            $scope.notice("银行名称不能为空!");
            return;
        }
        if($scope.newCard.accountPhone==null||$scope.newCard.accountPhone==""){
            $scope.notice("手机号不能为空!");
            return;
        }
        if($scope.newCard.bankProvince==null||$scope.newCard.bankProvince==""
            ||$scope.newCard.bankCity==null||$scope.newCard.bankCity==""
            ){
            $scope.notice("开户地址不能为空!");
            return;
        }
        if($scope.newCard.cnapsNo==null||$scope.newCard.cnapsNo==""){
            $scope.notice("分行不能为空!");
            return;
        }
        $scope.submitting=true;
        $http.post("userManagement/updateSettlementCard","info="+angular.toJson($scope.newCard),
            {headers: {'Content-Type':'application/x-www-form-urlencoded'}})
            .success(function(data){
                $scope.submitting=false;
                if(data.status){
                    $scope.notice(data.msg);
                    $scope.cancel();
                    $scope.getUserDetail();
                }else{
                    $scope.notice(data.msg);
                }
            }).error(function(data){
                $scope.submitting=false;
                $scope.notice("系统异常，请稍后再试");
            });
    };

    /**
     * 获取敏感数据
     */
    $scope.dataSta=true;
    $scope.getDataProcessing = function () {
        if($scope.dataSta){
            $http.get('userManagement/getDataProcessing?merchantNo='+$stateParams.merchantNo)
                .success(function(data) {
                    if(data.status){
                        $scope.info.mobileUsername = data.user.mobileUsername;
                        $scope.info.businessCode = data.user.businessCode;
                        $scope.info.mobileNo = data.user.mobileNo;
                        $scope.dataSta=false;
                    }else{
                        $scope.notice(data.msg);
                    }
                });
        }
    };
});
