/**
 * 贷款奖金配置
 */
angular.module('inspinia',['infinity.angular-chosen']).controller(  'superExchangeCtrl',function($scope,$http,i18nService,$document,SweetAlert){

    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    $scope.rewardClaimList = [{text:"购买成功",value:"0"}];
    $scope.resetForm = function () {
        $scope.baseInfo = {orgId:'-1',agencyAlias:"全部",shareRate:$scope.defaultPointExchangeRatio};
    };
    $scope.resetForm();

    $scope.columnDefs = [
        {field: 'id',displayName: 'ID',width: 80,pinnable: false,sortable: false},
        {field: 'orgName',displayName: '组织',width: 150,pinnable: false,sortable: false},
        {field: 'rewardClaim',displayName: '奖励要求',width: 150,cellFilter:"formatDropping:" + angular.toJson($scope.rewardClaimList)},
        {field: 'totalBonus',displayName: '积分兑换总奖金',width: 160,cellTemplate:
            '<div class="lh30" ng-show="row.entity.action!=2&&row.entity.profitType==1">{{row.entity.totalBonus}}</div>' +
            '<div class="lh30" ng-show="row.entity.action!=2&&row.entity.profitType==2">N*{{row.entity.totalBonus}}%</div>' +
            '<div class="lh30" ng-show="row.entity.action==2&&row.entity.profitType==1"><input style="text-align:center;width:50px;height: 30px" ng-model="row.entity.totalBonus"/></div>'+
            '<div class="lh30" ng-show="row.entity.action==2&&row.entity.profitType==2">N*<input style="text-align:center;width:50px;height: 30px" ng-model="row.entity.totalBonus"/>%</div>'},
        {field: 'companyBonus',displayName: '公司截留',width: 160,cellTemplate:
                '<div class="lh30" ng-show="row.entity.action!=2&&row.entity.profitType==1">{{row.entity.companyBonus}}</div>' +
                '<div class="lh30" ng-show="row.entity.action!=2&&row.entity.profitType==2">N*{{row.entity.companyBonus}}%</div>' +
                '<div class="lh30" ng-show="row.entity.action==2&&row.entity.profitType==1"><input style="text-align:center;width:50px;height: 30px" ng-model="row.entity.companyBonus"/></div>'+
                '<div class="lh30" ng-show="row.entity.action==2&&row.entity.profitType==2">N*<input style="text-align:center;width:50px;height: 30px" ng-model="row.entity.companyBonus"/>%</div>'},
        {field: 'orgBonus',displayName: '品牌截留',width: 200,cellTemplate:
            '<div class="lh30" ng-show="row.entity.action!=2&&row.entity.profitType==1">{{row.entity.orgBonus}}</div>' +
            '<div class="lh30" ng-show="row.entity.action!=2&&row.entity.profitType==2">N*{{row.entity.orgBonus}}%</div>' +
            '<div class="lh30" ng-show="row.entity.action==2&&row.entity.profitType==1"><input style="text-align:center;width:50px;height: 30px" ng-model="row.entity.orgBonus"/></div>'+
            '<div class="lh30" ng-show="row.entity.action==2&&row.entity.profitType==2">N*<input style="text-align:center;width:50px;height: 30px" ng-model="row.entity.orgBonus"/>%</div>'},
        {field: 'action',displayName: '操作',width: 120,pinnedRight:true,
        	cellTemplate:
        	'<div  class="lh30" ng-show="row.entity.action!=2&&grid.appScope.hasPermit(\'superExchange.add\')">' +
            '<a ng-click="grid.appScope.superExchangeEdit(row.entity)">编辑</a></div>'
        	+
        	'<div  class="lh30" ng-show="row.entity.action==2&&grid.appScope.hasPermit(\'superExchange' +
            '.add\')">' +
            '<a ng-click="grid.appScope.superExchangeUpd(row.entity)">保存</a></div>'
        }
    ];
    
    $scope.loanBonusGrid = {
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		  	//开启拓展名
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

    $scope.orgInfoList = [];
    
    //获取组织
    $scope.getOrgList = function(){
   	 $http({
            url:"superBank/getOrgInfoList",
            method:"POST"
        }).success(function(msg){
            if(msg.status){
                $scope.orgInfoListAll = msg.data;
                $scope.orgInfoListAll.unshift({orgId:'0',orgName:"默认"});
                $scope.orgInfoList = angular.copy($scope.orgInfoListAll);
                $scope.orgInfoListAll.unshift({orgId:'-1',orgName:"全部"});
            }
        }).error(function(){
            $scope.notice("获取组织列表异常");
        });
   };
   $scope.getOrgList();
   
   //查询默认组织的用户兑换积分比率：新增按钮点击，呈现：默认组织和默认组织的用户兑换积分比率
	$http.post('sysDict/getByKey.do',"sysKey=DEFAULT_POINT_EXCHANGE_RATIO", {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
	.success(function(msg){
		if(msg.status && msg.sysDict){
			$scope.defaultPointExchangeRatio = msg.sysDict.sysValue;
			$scope.baseInfo.shareRate= $scope.defaultPointExchangeRatio;
		} else {
			$scope.notice('默认组织用户兑换积分比率的数据字典未配置');
		}
	});

    $scope.changeOrg = function(){
     if($scope.baseInfo.orgId==0){
    		$scope.baseInfo.shareRate=$scope.defaultPointExchangeRatio;
     }else{
        $http({
            url:"superExcOrder/changeOrg?orgId="+$scope.baseInfo.orgId,
            method:"GET",
        }).success(function(msg){
            if(msg.status){
                $scope.baseInfo.shareRate=msg.data;
            }
        }).error(function(){
            $scope.notice("获取组织异常");
        });
     }
    };
    
    //编辑配置
    $scope.superExchangeEdit = function(entity){
    	 entity.action=2;
    };
    
    function isNumber(value){
    	var reg= /^([1-9]\d{0,9}|0)(\.\d{1,2})?$/g;  
    	return reg.test(value);
    }
    
    //更新
    $scope.superExchangeUpd = function(entity){
        var totalBonus=entity.totalBonus;
        var companyBonus=entity.companyBonus;
        var orgBonus=entity.orgBonus;
        if(!isNumber(totalBonus)){
            $scope.notice("积分兑换总奖金输入不合法")
            return
        }
        if(!isNumber(companyBonus)){
            $scope.notice("公司截留输入不合法")
            return
        }
        if(!isNumber(orgBonus)){
            $scope.notice("品牌截留输入不合法")
            return
        }
        if(Number(totalBonus)<Number(companyBonus)+Number(orgBonus)){
        	 $scope.notice("积分兑换总奖金应该大于等于公司截留和品牌截留的和")
             return
        }
        /* if(!validate(totalBonus)){
            $scope.notice("积分兑换总奖金输入不合法")
            return
        }
        if(!validate(companyBonus)){
            $scope.notice("公司截留输入不合法")
            return
        }
        if(!validate(orgBonus)){
            $scope.notice("品牌截留输入不合法")
            return
        }
        if(parseInt(totalBonus)<=parseInt(companyBonus)+parseInt(orgBonus)){
            $scope.notice("积分兑换总奖金应该大于等于公司截留和品牌截留的和")
            return
        }*/
        SweetAlert.swal({
                title: "确定保存吗？",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post("superExchangeBonus/updateSuperExchange",angular.toJson(entity))
                        .success(function(data){
                            if(data.status){
                                $scope.notice(data.msg);
                                entity.action=1;
                            }else{
                                $scope.notice(data.msg);
                                $scope.submitting = false;
                            }
                        });
                }
            });
    };
    
    $scope.query = function () {
        $scope.submitting = true;
        $scope.loadImg = true;
        $http({
            url: 'superExchangeBonus/getSuperExchanges?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
            data: $scope.baseInfo,
            method:'POST'
        }).success(function (msg) {
            $scope.submitting = false;
            $scope.loadImg = false;
            if (!msg.status){
                $scope.notice(msg.msg);
                return;
            }
            $scope.loanBonusGrid.data = msg.data.result;
            $scope.loanBonusGrid.totalItems = msg.data.totalCount;
        }).error(function (msg) {
            $scope.submitting = false;
            $scope.loadImg = false;
            $scope.notice('服务器异常,请稍后再试.');
        });
    };
   
    $scope.addSuperExchange = function(){
        $scope.baseInfo.orgId = "0";
        $scope.baseInfo.agencyAlias = "";
    	$("#addSuperExchange").modal("show");
    };
    
    $scope.cancel = function(){
        $scope.resetForm();
    	$("#addSuperExchange").modal("hide");
    };
    
    //新增
    $scope.saveSuperExchange = function(){
        $scope.baseInfo.profitType='2';
        $scope.baseInfo.rewardClaim='0';
        $scope.baseInfo.type='1';
        var totalBonus=$scope.baseInfo.totalBonus;
        var companyBonus=$scope.baseInfo.companyBonus;
        var orgBonus=$scope.baseInfo.orgBonus;
        if(!isNumber(totalBonus)){
            $scope.notice("积分兑换总奖金输入不合法")
            return
        }
        if(!isNumber(companyBonus)){
            $scope.notice("公司截留输入不合法")
            return
        }
        if(!isNumber(orgBonus)){
            $scope.notice("品牌截留输入不合法")
            return
        }
        if(Number(totalBonus)<Number(companyBonus)+Number(orgBonus)){
       	 $scope.notice("积分兑换总奖金应该大于等于公司截留和品牌截留的和")
            return
       }
        
        /*if(!validate(totalBonus)){
            $scope.notice("积分兑换总奖金输入不合法")
            return
        }
        if(!validate(companyBonus)){
            $scope.notice("公司截留输入不合法")
            return
        }
        if(!validate(orgBonus)){
            $scope.notice("品牌截留输入不合法")
            return
        }
        if(parseInt(totalBonus)<=parseInt(companyBonus)+parseInt(orgBonus)){
            $scope.notice("兑换总奖金应该大于等于公司截留和品牌截留的和")
            return
        }*/
 	 $http.post("superExchangeBonus/addSuperExchange",angular.toJson($scope.baseInfo))
 		.success(function(data){
            $scope.notice(data.msg);
 			if(data.status){
 				$scope.cancel();
                $scope.getAllBonus();
 				$scope.query();
 			}else{
 				$scope.submitting = false;
 			}
 		});
    };

    function validate(num)
    {
        var reg = /^\d+(?=\.{0,1}\d+$|$)/
        if(reg.test(num)) return true;
        return false ;
    }
    //页面绑定回车事件
    $document.bind("keypress", function(event) {
        $scope.$apply(function (){
            if(event.keyCode == 13){
                $scope.query();
            }
        });
    });
});