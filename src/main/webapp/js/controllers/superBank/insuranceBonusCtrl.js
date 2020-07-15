/**
 * 信用卡配置
 */
angular. module('inspinia',['infinity.angular-chosen']).controller('insuranceBonusCtrl',function($scope,$http,i18nService,$document,$window){

    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    $scope.resetForm = function () {
        $scope.baseInfo = {companyNo:'-1',orgId:'-1',productId:'-1',profitType:"1",status:"0"};//查询条件的form
    };
    $scope.profitTypeList = [{text:"固定金额",value:"1"},{text:"按保险金额比例",value:"2"}];
    $scope.statusList= [{text:"购买成功",value:"0"} ];
    $scope.resetForm();

    $scope.columnDefs = [
        {field: 'id',displayName: 'ID',width: 100,pinnable: false,sortable: false},
        {field: 'orgName',displayName: '组织',pinnable: false,sortable: false},
        {field: 'companyNickName',displayName: '保险公司别称',width: 130,pinnable: false,sortable: false},
        {field: 'productName',displayName: '产品名称',width: 130,pinnable: false,sortable: false},
        {field: 'profitType',displayName: '奖金方式',width: 100,pinnable: false,sortable: false,cellFilter:"formatDropping:"+ angular.toJson($scope.profitTypeList)},
        {field: 'statusStr',displayName: '奖励要求',width: 100,pinnable: false,sortable: false},
        {field: 'totalBonus',displayName: '保单总奖金',cellTemplate:'' +
            '<div class="lh30" ng-show="row.entity.action!=2&&row.entity.profitType==1">{{row.entity.totalBonus}}</div>' +
            '<div class="lh30" ng-show="row.entity.action!=2&&row.entity.profitType==2">N*{{row.entity.totalBonus}}%</div>' +
            '<div class="lh30" ng-show="row.entity.action==2&&row.entity.profitType==1"><input style="text-align:center;width:50px;height: 30px" ng-model="row.entity.totalBonus"/></div>'+
            '<div class="lh30" ng-show="row.entity.action==2&&row.entity.profitType==2">N*<input style="text-align:center;width:50px;height: 30px" ng-model="row.entity.totalBonus"/>%</div>'},
        {field: 'companyBonus',displayName: '公司截留',cellTemplate:'' +
            '<div class="lh30" ng-show="row.entity.action!=2&&row.entity.profitType==1">{{row.entity.companyBonus}}</div>' +
            '<div class="lh30" ng-show="row.entity.action!=2&&row.entity.profitType==2">N*{{row.entity.companyBonus}}%</div>' +
            '<div class="lh30" ng-show="row.entity.action==2&&row.entity.profitType==1"><input style="text-align:center;width:50px;height: 30px" ng-model="row.entity.companyBonus"/></div>'+
            '<div class="lh30" ng-show="row.entity.action==2&&row.entity.profitType==2">N*<input style="text-align:center;width:50px;height: 30px" ng-model="row.entity.companyBonus"/>%</div>'},
        {field: 'orgBonus',displayName: '品牌截留',cellTemplate:'' +
            '<div class="lh30" ng-show="row.entity.action!=2&&row.entity.profitType==1">{{row.entity.orgBonus}}</div>' +
            '<div class="lh30" ng-show="row.entity.action!=2&&row.entity.profitType==2">N*{{row.entity.orgBonus}}%</div>' +
            '<div class="lh30" ng-show="row.entity.action==2&&row.entity.profitType==1"><input style="text-align:center;width:50px;height: 30px" ng-model="row.entity.orgBonus"/></div>'+
            '<div class="lh30" ng-show="row.entity.action==2&&row.entity.profitType==2">N*<input style="text-align:center;width:50px;height: 30px" ng-model="row.entity.orgBonus"/>%</div>'},
        {field: 'action',displayName: '操作',pinnedRight:true,
        	cellTemplate:
        	'<div  class="lh30" ng-show="row.entity.action!=2"><input type="hidden" ng-model="row.entity.sourceId"/><input type="hidden" ng-model="row.entity.id"/><input type="hidden" ng-model="row.entity.action" /><a ng-show="grid.appScope.hasPermit(\'insuranceBonus.edit\')"  ng-click="grid.appScope.insuranceBonusEdit(row.entity)">编辑</a></div>'
        	+
        	'<div  class="lh30" ng-show="row.entity.action==2"><a ng-show="grid.appScope.hasPermit(\'insuranceBonus.edit\')" ng-click="grid.appScope.insuranceBonusUpd(row.entity)">保存</a></div>'
        }
    ];
    
    $scope.orgInfoGrid = {
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


    //获取保险公司
    $scope.getInsuranceCompany = function(){
        $http({
            url:"insuranceCompany/getCompanyList",
            method:"POST"
        }).success(function(msg){
            if(msg.status){
                $scope.companyListAll = msg.data;
                $scope.companyList=angular.copy($scope.companyListAll)
                $scope.companyListAll.unshift({companyNo:'-1',companyNickName:"全部"});
            }
        }).error(function(){
            $scope.notice("获取组织列表异常");
        });
    };
    $scope.getInsuranceCompany();

    //获取产品
    $scope.getInsuranceProduct = function(){
        $http({
            url:"insuranceProduct/getList",
            method:"POST"
        }).success(function(msg){
            if(msg.status){
                $scope.productListAll = msg.data;
                $scope.productListAll.unshift({productId:'-1',productName:"全部"});
            }
        }).error(function(){
            $scope.notice("获取组织列表异常");
        });
    };
    $scope.getInsuranceProduct();

    //获取组织
    $scope.getOrgList = function(){
   	 $http({
            url:"superBank/getOrgInfoList",
            method:"POST"
        }).success(function(msg){
            if(msg.status){
                $scope.orgInfoList = msg.data;
                $scope.orgInfoList.unshift({orgId:'0',orgName:"默认"});
                $scope.orgInfoListAll = angular.copy($scope.orgInfoList);
                $scope.orgInfoList.unshift({orgId:'-1',orgName:"全部"});
            }
        }).error(function(){
            $scope.notice("获取组织列表异常");
        });
   };
   $scope.getOrgList();

    $scope.productChange= function(productId){
        var productListAll= $scope.productList;
        var productIdStr= productId;
        for(var productId in productListAll){
            var data = productListAll[productId];
            if(data.productId==productIdStr){
                $scope.baseInfo.profitType=data.bonusType;
            }
        }
    }

    $scope.companyChange= function(companyNo){
        $http({
            url:"insuranceBonus/getByCompanyNo",
            method:"POST",
            data: companyNo,
        }).success(function(msg){
            if(msg.status){
                $scope.productList = msg.data;
            }
        }).error(function(){
            $scope.notice("获取公司信息异常");
        });
    };

    //编辑配置
    $scope.insuranceBonusEdit = function(entity){
    	 entity.action=2;
    };


    //更新
    $scope.insuranceBonusUpd = function(entity){
        var totalBonus=entity.totalBonus;
        var companyBonus=entity.companyBonus;
        var orgBonus=entity.orgBonus;
        if(!validate(totalBonus)){
            $scope.notice("保单总奖金输入不合法")
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
            $scope.notice("保单总奖金应该大于等于公司截留和品牌截留的和")
            return
        }
    	 $http.post("insuranceBonus/updateInsuranceBonus",angular.toJson(entity))
    		.success(function(data){
    			if(data.status){
    				$scope.notice(data.msg);
    				entity.action=1;
    			}else{
    				$scope.notice(data.msg);
    				$scope.submitting = false;
    			}
    		});
    };
    
    $scope.query = function () {
        $scope.submitting = true;
        $scope.loadImg = true;
        
        $http({
            url: 'insuranceBonus/getInsuranceBonus?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
            data: $scope.baseInfo,
            method:'POST'
        }).success(function (msg) {
            $scope.submitting = false;
            $scope.loadImg = false;
            if (!msg.status){
                $scope.notice(msg.msg);
                return;
            }
            $scope.orgInfoGrid.data = msg.data.result;
            $scope.orgInfoGrid.totalItems = msg.data.totalCount;
        }).error(function (msg) {
            $scope.submitting = false;
            $scope.loadImg = false;
            $scope.notice('服务器异常,请稍后再试.');
        });
    };
   
    $scope.addInsurance = function(){
        $scope.baseInfo = {companyNo:'-1',orgId:'0',productId:'-1',profitType:"1",status:"0"};
    	$("#addInsurance").modal("show");
    };
    
    $scope.cancel = function(){
    	$("#addInsurance").modal("hide");
        $scope.resetForm();
    };
    
    //新增
    $scope.saveInsurance = function(){
        var productId=$scope.baseInfo.productId;
        var totalBonus=$scope.baseInfo.totalBonus;
        var companyBonus=$scope.baseInfo.companyBonus;
        var orgBonus=$scope.baseInfo.orgBonus;
        if(productId==null||productId==""){
            $scope.notice("请选择产品名称")
            return
        }
        if(!validate(totalBonus)){
            $scope.notice("保单总奖金输入不合法")
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
            $scope.notice("保单总奖金应该大于等于公司截留和品牌截留的和")
            return
        }
 	 $http.post("insuranceBonus/addInsuranceConf",angular.toJson($scope.baseInfo))
 		.success(function(data){
            $scope.notice(data.msg);
 			if(data.status){
 				$("#addInsurance").modal("hide");
                $scope.resetForm();
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