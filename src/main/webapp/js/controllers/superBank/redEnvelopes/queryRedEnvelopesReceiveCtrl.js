/**
 * 红包领取查询
 */
angular.module('inspinia').controller('queryRedEnvelopesReceiveCtrl',function($scope,$http,$state,$stateParams,$compile,$uibModal,SweetAlert,$log,i18nService,$document,$timeout){
    //i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.statusSelect=[{text:"全部",value:''},{text:"待领取",value:'0'},{text:"已领取",value:'1'},
        {text:"平台回收",value:'2'},{text:"原路退回",value:'3'}];
    $scope.dividendStatusList=[{text:"全部",value:''},{text:"未领取",value:'0'},{text:"已领取",value:'1'}];
    $scope.dividendStatusStr=[{text:"未领取",value:'0'},{text:"已领取",value:'1'}];
    $scope.statusStr=angular.toJson($scope.statusSelect);

    $scope.pushTypeSelect=$scope.redPushTypes;
    $scope.pushTypeStr=angular.toJson($scope.pushTypeSelect);

    $scope.receiveTypeSelect=$scope.redReceiveTypes;
    $scope.receiveTypeStr=angular.toJson($scope.receiveTypeSelect);

    $scope.busTypeSelect= $scope.redBusTypes;
    $scope.busTypeStr=angular.toJson($scope.busTypeSelect);

    $scope.allCount=0;
    $scope.amountCount=0;
    $scope.totalLordsProfit=0;

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

    $scope.query=function(){
        if ($scope.loadImg) {
            return;
        }
        $scope.loadImg = true;
        
        if ($scope.selected!= undefined && $scope.selected!=null) {
    		$scope.info.territoryProvinceName = $scope.selected.name;
    		if ($scope.selected2!= undefined && $scope.selected2!=null) {
        		$scope.info.territoryCityName = $scope.selected2.name;
        		if ($scope.selected3!= undefined && $scope.selected3!=null) {
            		$scope.info.territoryRegionName = $scope.selected3.name;
        		}else{
        			$scope.info.territoryRegionName = "";
        		}
    		}else{
    			$scope.info.territoryCityName = "";
    			$scope.info.territoryRegionName = "";
    		}
		}else{
			$scope.info.territoryProvinceName = "";
			$scope.info.territoryCityName = "";
			$scope.info.territoryRegionName = "";
		}
        
        
        $http.post("redEnvelopesReceive/selectByParam","info=" + angular.toJson($scope.info)+"&pageNo="+
            $scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.result=data.page.result;
                    $scope.allCount=data.page.totalCount;
                    $scope.gridOptions.totalItems = data.page.totalCount;
                    if(data.sunOrder!=null){
                        $scope.amountCount=data.sunOrder.amountCount;
                        $scope.totalLordsProfit=data.sunOrder.totalLordsProfit;
                        $scope.totalBasicBonus=data.sunOrder.totalBasicBonus;
                        $scope.totalBonusAmount=data.sunOrder.totalBonusAmount;
                    }
                }else{
                    $scope.notice(data.msg);
                }
                $scope.loadImg = false;
            }).error(function () {
                $scope.submitting = false;
                $scope.loadImg = false;
                $scope.notice('服务器异常,请稍后再试.');
            });
    }
    //$scope.query();手动查询

    $scope.gridOptions={                           //配置表格
        data: 'result',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'id',displayName:'红包领取ID',width:180 },
            { field: 'redOrderId',displayName:'红包ID',width:180 },
            { field: 'status',displayName:'领取状态',cellFilter:"formatDropping:"+$scope.statusStr,width:150},
            { field: 'pushType',displayName:'发放人类型',cellFilter:"formatDropping:"+$scope.pushTypeStr,width:150},
            { field: 'receiveType',displayName:'接收人数类型',cellFilter:"formatDropping:"+$scope.receiveTypeStr,width:150},
            { field: 'busType',displayName:'业务类型',cellFilter:"formatDropping:"+$scope.busTypeStr,width:150},
            { field: 'confId',displayName:'红包配置ID',width:180 },
            { field: 'orderNo',displayName:'关联业务订单ID',width:180 },
            { field: 'orgName',displayName:'发放人组织名称',width:180 },
            { field: 'pushUserCode',displayName:'发红包用户ID',width:180 },
            { field: 'pushUserName',displayName:'发红包用户姓名',width:180 },
            { field: 'pushUserPhone',displayName:'发红包手机号',width:180 },
            { field: 'getUserCode',displayName:'领取用户ID',width:180 },
            { field: 'getUserName',displayName:'领取用户姓名',width:180 },
            { field: 'getUserPhone',displayName:'领取用户手机号',width:180 },
            { field: 'amount',displayName:'领取金额',width:180,cellFilter:"currency:''" },
            { field: 'getDate',displayName:'领取时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'territoryProvinceName',displayName:'领取用户省',width:180 },
            { field: 'territoryCityName',displayName:'领取用户市',width:180 },
            { field: 'territoryRegionName',displayName:'领取用户区',width:180 },
            // { field: 'rate',displayName:'领地业务基准分红配置',width:180 },
            // { field: 'basicBonusAmount',displayName:'领地业务基准分红',width:180 },
            // { field: 'territoryAvgPrice',displayName:'领地均价',width:180 },
            // { field: 'territoryPrice',displayName:'领地价格',width:180 },
            // { field: 'adjustRatio',displayName:'调节系数',width:180 },
            // { field: 'bonusAmount',displayName:'领地分红',width:180 },
            // { field: 'dividendStatus',displayName:'领地红包领取状态',width:180,cellFilter:"formatDropping:"+angular.toJson($scope.dividendStatusStr)},
            // { field: 'dividendUserCode',displayName:'领地分红领取领主编号',width:180 },
            { field: 'territoryOrgName',displayName:'领主所属组织',width:180 },
            { field: 'territoryUserCode',displayName:'领主用户编号',width:180 },
            { field: 'territoryUserName',displayName:'领主姓名',width:180 },
            { field: 'territoryPhone',displayName:'领主手机号',width:180 },
            // { field: 'receiveTime',displayName:'领地分红领取时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180,width:180 },
            { field: 'lordsProfit',displayName:'领主收益',width:180 }
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


    //清空
    $scope.clear=function(){
        $scope.info={status:"",pushType:"",receiveType:"",busType:"",orgId:"",dividendStatus:"",
            getDateMin:moment(new Date().getTime()-6*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
            getDateMax:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'};
        $scope.selected = "";
        $scope.selected2 = "";
        $scope.selected3 = "";
    }
    $scope.clear();

    $scope.import=function(){
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
                    location.href="redEnvelopesReceive/exportInfo?info="+encodeURI(encodeURI(angular.toJson($scope.info)));
                }
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
    
    //省市区
    $scope.list = LAreaDataBaidu;
      $scope.c = function () {
          $scope.selected2 = "";
          $scope.selected3 = "";
      };
      
      $scope.c2 = function () { 
          $scope.selected3 = "";
    };
})