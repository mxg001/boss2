/**
 * 领地买卖记录查询
 */
angular.module('inspinia').controller('manorTransactionRecoreCtrl', function($scope, $http,$stateParams,i18nService, SweetAlert){
    //数据源
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
   
    //'订单状态:1已创建；2待支付；3:待审核 4已授权 5订单成功 6订单失败 7已办理过  9已关闭'
    $scope.statusList = [{text:"全部",value:""},{text:"待支付",value:"2"},
                         {text:"成功",value:"5"},{text:"已支付",value:"18"},{text:"已退款",value:"19"}
                        ];//订单状态
    $scope.statusListStr=angular.toJson($scope.statusList);
    //'记账状态;0待入账；1已记账；2记账失败',
    $scope.accountStatusList = [{text:"全部",value:""},{text:"待入账",value:"0"},{text:"已记账",value:"1"},{text:"记账失败",value:"2"}];
    $scope.accStatusList = [{text:"待入账",value:"0"},{text:"已记账",value:"1"},{text:"记账失败",value:"2"}];
    $scope.accountStatusListStr=angular.toJson($scope.accStatusList);
    //1 微信，2 支付宝，3 快捷，4红包账户，5分润账户',
    $scope.payMethodList = [{text:"全部",value:""},{text:"微信",value:"1"},{text:"支付宝",value:"2"},{text:"快捷支付",value:"3"},{text:"红包账户",value:"4"},{text:"分润账户",value:"5"}];
    $scope.methodList = [{text:"微信",value:"1"},{text:"支付宝",value:"2"},{text:"快捷支付",value:"3"},{text:"红包账户",value:"4"},{text:"分润账户",value:"5"}];
    $scope.payMethodListStr=angular.toJson($scope.methodList);
    //收款通道
    $scope.payChannelStatusList = [{text:"全部",value:""},{text:"微信官方",value:"wx"},{text:"支付宝官方",value:"ali"},{text:"中钢银通",value:"kj"}];//通道名称
    $scope.payChannelList = [{text:"微信官方",value:"wx"},{text:"支付宝官方",value:"ali"},{text:"中钢银通",value:"kj"}];//通道名称
    $scope.payChannelStatusListStr=angular.toJson($scope.payChannelList);
   
    $scope.userTypeListStr=angular.toJson($scope.userTypeList);
    
    console.debug($scope.userTypeListStr);
    
    $scope.sumMoney = "";
    $scope.sumPlatMoney = "";
    
    $scope.resetForm = function () {
        $scope.baseInfo = {payDateStart:moment(new Date().getTime()-7*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
            payDateEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
            cDateStart:moment(new Date().getTime()-7*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
            cDateEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
            payOrderNo:"",payChannelNo:"",newPhone:"",newUserName:"",nid:"",phone:"",userName:"",oid:"",orderNo:"",oldLordsOrgId:"",newLordsOrgId:"",status:"",accountStatus:"",payMethod:"",payChannel:"",oneUserType:"",
            oneUserCode:"",oneUserName:"",twoUserType:"",twoUserCode:"",twoUserName:"",thrUserType:"",thrUserCode:"",thrUserName:"",fouUserType:"",fouUserCode:"",fouUserName:""};
        $scope.selected = "";
        $scope.selected2 = "";
        $scope.selected3 = "";
    }
    $scope.resetForm();
    
    $scope.columnDefs = [
        {field: 'orderId',displayName: 'id',pinnable: false,sortable: false,width:150},
        {field: 'orderNo',displayName: '交易订单编号',pinnable: false,sortable: false,width:150},
        {field: 'territoryId',displayName: '领地ID',pinnable: false,sortable: false,width:150},
        {field: 'payDate',displayName: '交易时间',pinnable: false,sortable: false,width:150,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'cDate',displayName: '创建时间',pinnable: false,sortable: false,width:150,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'provinceName',displayName: '省',pinnable: false,sortable: false,width:150},
        {field: 'cityName',displayName: '市',pinnable: false,sortable: false,width:150},
        {field: 'regionName',displayName: '区',pinnable: false,sortable: false,width:150},
        {field: 'status',displayName: '订单状态',pinnable: false,sortable: false,width:150,cellFilter:"formatDropping:"+$scope.statusListStr},
        {field: 'accountStatus',displayName: '记账状态',pinnable: false,sortable: false,width:150,cellFilter:"formatDropping:"+$scope.accountStatusListStr},
        {field: 'oldLordsOrgName',displayName: '原领主所属组织',pinnable: false,sortable: false,width:150},
        {field: 'oldLordsUserCode',displayName: '原领主ID',pinnable: false,sortable: false,width:150},
        {field: 'userName',displayName: '原领主姓名',pinnable: false,sortable: false,width:150},
        {field: 'nickName',displayName: '原领主昵称',pinnable: false,sortable: false,width:150},
        {field: 'phone',displayName: '原领主手机号',pinnable: false,sortable: false,width:150},
        {field: 'oldPayDate',displayName: '原领主买入时间',pinnable: false,sortable: false,width:150,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'oldPayPrice',displayName: '原领主买入价格',pinnable: false,sortable: false,width:150},
        {field: 'price',displayName: '转让价格',pinnable: false,sortable: false,width:150},
        {field: 'tradeFeeConfStr',displayName: '领地交易手续费率',pinnable: false,sortable: false,width:150},
        {field: 'tradeFee',displayName: '领地交易手续费',pinnable: false,sortable: false,width:150},
        {field: 'premiumPrice',displayName: '溢价',pinnable: false,sortable: false,width:150},
        {field: 'oldTotalProfit',displayName: '原领主累计收益',pinnable: false,sortable: false,width:150},
        {field: 'tradeProfit',displayName: '原领主转让收益',pinnable: false,sortable: false,width:150},
        {field: 'newLordsOrgName',displayName: '新领主所属组织',pinnable: false,sortable: false,width:150},
        {field: 'newLordsUserCode',displayName: '新领主ID',pinnable: false,sortable: false,width:150},
        {field: 'newUserName',displayName: '新领主姓名',pinnable: false,sortable: false,width:150},
        {field: 'newNickName',displayName: '新领主昵称',pinnable: false,sortable: false,width:150},
        {field: 'newPhone',displayName: '新领主手机号',pinnable: false,sortable: false,width:150},
        {field: 'payMethod',displayName: '支付方式',pinnable: false,sortable: false,width:150,cellFilter:"formatDropping:"+$scope.payMethodListStr},
        {field: 'payChannel',displayName: '收款通道',width: 150,pinnable: false,sortable: false,cellFilter:"formatDropping:"+$scope.payChannelStatusListStr},
        {field: 'payChannelNo',displayName: '收款通道商户号',width: 150,pinnable: false,sortable: false},
        {field: 'payOrderNo',displayName: '收款通道流水号',width: 150,pinnable: false,sortable: false},
        {field: 'totalBonus',displayName: '发放总奖金',pinnable: false,sortable: false,width:150},
        {field: 'oneUserCode',displayName: '一级编号',pinnable: false,sortable: false,width:150},
        {field: 'oneUserName',displayName: '一级姓名',pinnable: false,sortable: false,width:150},
        {field: 'oneUserType',displayName: '一级身份',pinnable: false,sortable: false,width:150,cellFilter:"formatDropping:"+$scope.userTypeListStr},
        {field: 'oneUserProfit',displayName: '一级分润',pinnable: false,sortable: false,width:150},
       
        {field: 'twoUserCode',displayName: '二级编号',pinnable: false,sortable: false,width:150},
        {field: 'twoUserName',displayName: '二级姓名',pinnable: false,sortable: false,width:150},
        {field: 'twoUserType',displayName: '二级身份',pinnable: false,sortable: false,width:150,cellFilter:"formatDropping:"+$scope.userTypeListStr},
        {field: 'twoUserProfit',displayName: '二级分润',pinnable: false,sortable: false,width:150},
       
        {field: 'thrUserCode',displayName: '三级编号',pinnable: false,sortable: false,width:150},
        {field: 'thrUserName',displayName: '三级姓名',pinnable: false,sortable: false,width:150},
        {field: 'thrUserType',displayName: '三级身份',pinnable: false,sortable: false,width:150,cellFilter:"formatDropping:"+$scope.userTypeListStr},
        {field: 'thrUserProfit',displayName: '三级分润',pinnable: false,sortable: false,width:150},
        
        {field: 'fouUserCode',displayName: '四级编号',pinnable: false,sortable: false,width:150},
        {field: 'fouUserName',displayName: '四级姓名',pinnable: false,sortable: false,width:150},
        {field: 'fouUserType',displayName: '四级身份',pinnable: false,sortable: false,width:150,cellFilter:"formatDropping:"+$scope.userTypeListStr},
        {field: 'fouUserProfit',displayName: '四级分润',pinnable: false,sortable: false,width:150},
       
        {field: 'orgProfit',displayName: '当前领主所属组织分润',pinnable: false,sortable: false,width:150},
        {field: 'plateProfit',displayName: '平台分润',pinnable: false,sortable: false,width:150},
        {field: 'transAmountStr',displayName: '操作',pinnedRight:true,width:150,cellTemplate:
            '<a class="lh30" ui-sref="red.manorTransactionRecoreDetail({orderId:row.entity.orderId})">详情</a>'}
        
    ];

    //red.manorTransactionRecoreDetail
    $scope.grid = {
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
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
    	if ($scope.selected!= undefined && $scope.selected!=null) {
    		$scope.baseInfo.provinceName = $scope.selected.name;
    		if ($scope.selected2!= undefined && $scope.selected2!=null) {
        		$scope.baseInfo.cityName = $scope.selected2.name;
        		if ($scope.selected3!= undefined && $scope.selected3!=null) {
            		$scope.baseInfo.regionName = $scope.selected3.name;
        		}else{
        			$scope.baseInfo.regionName = "";
        		}
    		}else{
    			$scope.baseInfo.cityName = "";
    			$scope.baseInfo.regionName = "";
    		}
		}else{
			$scope.baseInfo.provinceName = "";
			$scope.baseInfo.cityName = "";
			$scope.baseInfo.regionName = "";
		}
        $scope.submitting = true;
        $scope.loadImg = true;
        $http.post("manor/transactionRecore","baseInfo=" + angular.toJson($scope.baseInfo)+"&pageNo="+
            $scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function (result) {
            $scope.submitting = false;
            $scope.loadImg = false;
            if (!result.status){
                $scope.notice(result.msg);
                return;
            }
            $scope.sumMoney = result.data.recoresum.sumMoney;
            $scope.sumPlatMoney = result.data.recoresum.sumPlatMoney;
            $scope.sumPremiumMoney = result.data.recoresum.sumPremiumMoney;
            $scope.sumDividedMoney = result.data.recoresum.sumDividedMoney;
            $scope.sumTradeFee = result.data.recoresum.sumTradeFee;
            $scope.grid.data = result.data.page.result;
            $scope.grid.totalItems = result.data.page.totalCount;
            $scope.totalInfo = result.data.totalInfo;
        }).error(function () {
            $scope.submitting = false;
            $scope.loadImg = false;
            $scope.notice('服务器异常,请稍后再试.');
        });
    };
    
    //查询所有银行家组织
    $scope.orgInfoList = [];
    $scope.getOrgInfoList = function () {
        $http({
            url:"superBank/getOrgInfoList",
            method:"POST"
        }).success(function(msg){
            if(msg.status){
                $scope.orgInfoList = msg.data;
                $scope.orgInfoList.unshift({orgId:"",orgName:"全部"});
            }
        }).error(function(){
            $scope.notice("获取组织信息异常");
        })
    };
    $scope.getOrgInfoList();
    
    //省市区
    $scope.list = LAreaDataBaidu;
      $scope.c = function () {
    	  console.debug($scope.selected);
          $scope.selected2 = "";
          $scope.selected3 = "";
      };
      
      $scope.c2 = function () { 
          $scope.selected3 = "";
    };
    
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
                    location.href="manor/exportManorTransactionRecore?baseInfo="+encodeURI(encodeURI(angular.toJson($scope.baseInfo)));
                }
            });
    };
    
});
