/**
 * 贷款奖金配置
 */
angular.module('inspinia',['infinity.angular-chosen']).controller('loanBonusCtrl',function($scope,$http,i18nService,$document,SweetAlert){

    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    $scope.rewardRequirementsList = [{text:"有效注册",value:"1"},{text:"授信成功",value:"3"},{text:"有效借款",value:"2"}];
    $scope.profitTypeList = [{text:"固定金额",value:"1"},{text:"按放贷金额比例",value:"2"}];
    $scope.isOpenList = [{text:"全部",value:""},{text:"是",value:1},{text:"否",value:0}];

    $scope.resetForm = function () {
        $scope.conf = {orgId:'-1',isOpen:''};
    };
    $scope.resetForm();

    $scope.columnDefs = [
        {field: 'id',displayName: 'ID',width: 80,pinnable: false,sortable: false},
        {field: 'orgName',displayName: '组织',width: 150,pinnable: false,sortable: false},
        {field: 'loanAlias',displayName: '贷款机构别称',width: 150,pinnable: false,sortable: false},
        // {field: 'loanBonus',displayName: '放贷奖金',width: 150,cellTemplate:'<div class="lh30" ng-show="row.entity.action!=2">N*{{row.entity.loanBonus}}%</div><div class="lh30" ng-show="row.entity.action==2">N*<input style="text-align:center;width:50px;" ng-model="row.entity.loanBonus"/>%</div>'},
        // {field: 'registerBonus',displayName: '注册奖金',width: 100,cellTemplate:'<div class="lh30" ng-show="row.entity.action!=2">{{row.entity.registerBonus}}</div><div class="lh30" ng-show="row.entity.action==2"><input style="text-align:center;width:50px;" ng-model="row.entity.registerBonus"/></div>'},
        // {field: 'orgCostLoan',displayName: '品牌成本-放贷',width: 160,cellTemplate:'<div class="lh30" ng-show="row.entity.action!=2">N*{{row.entity.orgCostLoan}}%</div><div class="lh30" ng-show="row.entity.action==2">N*<input style="text-align:center;width:50px;" ng-model="row.entity.orgCostLoan"/>%</div>'},
        // {field: 'orgCostReg',displayName: '品牌成本-注册',width: 160,cellTemplate:'<div class="lh30" ng-show="row.entity.action!=2">{{row.entity.orgCostReg}}</div><div class="lh30" ng-show="row.entity.action==2"><input style="text-align:center;width:50px;" ng-model="row.entity.orgCostReg"/></div>'},
        // {field: 'orgPushLoan',displayName: '品牌发放总奖金扣率-放贷',width: 200,cellTemplate:'<div class="lh30" ng-show="row.entity.action!=2">N*{{row.entity.orgPushLoan}}%</div><div class="lh30" ng-show="row.entity.action==2">N*<input style="text-align:center;width:50px;" ng-model="row.entity.orgPushLoan"/>%</div>'},
        // {field: 'orgPushReg',displayName: '品牌发放总奖金-注册',width: 180,cellTemplate:'<div class="lh30" ng-show="row.entity.action!=2">{{row.entity.orgPushReg}}</div><div class="lh30" ng-show="row.entity.action==2"><input style="text-align:center;width:50px;" ng-model="row.entity.orgPushReg"/></div>'},
        {field: 'isOpen',displayName: '是否外放组织',width: 150,pinnable: false,sortable: false,cellFilter:"formatDropping:" + angular.toJson($scope.isOpenList)},
        {field: 'profitType',displayName: '奖金方式',width: 150,cellFilter:"formatDropping:" + angular.toJson($scope.profitTypeList)},
        {field: 'rewardRequirements',displayName: '奖励要求',width: 150,cellFilter:"formatDropping:" + angular.toJson($scope.rewardRequirementsList)},
        {field: 'loanBonus',displayName: '贷款机构总奖金',width: 160,cellTemplate:
                '<div class="lh30" ng-show="row.entity.profitType==1">{{row.entity.loanBonus}}</div>' +
                '<div class="lh30" ng-show="row.entity.profitType==2">N*{{row.entity.loanBonus}}%</div>'
        },
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
        	'<div  class="lh30" ng-show="row.entity.action!=2&&grid.appScope.hasPermit(\'loanBonusConf.add\')">' +
            '<a ng-click="grid.appScope.creditCardConfEdit(row.entity)">编辑</a></div>'
        	+
        	'<div  class="lh30" ng-show="row.entity.action==2&&grid.appScope.hasPermit(\'loanBonusConf.add\')">' +
            '<a ng-click="grid.appScope.creditCardUpd(row.entity)">保存</a></div>'
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

    $scope.loanCompanies = [];
    $scope.companiesInAdd = [];
    $scope.orgInfoList = [];
    
    $scope.changeOps = function (sourceId) {
        var orgId = $scope.loan.orgId;
    	angular.forEach($scope.companiesInAdd, function(item){
			 if(item.id==sourceId){
				 $scope.loan = item;
				 return;
	     	}
	     });
        $scope.loan.orgId = orgId;
        $scope.loan.sourceId = sourceId;//防止页面ngchange反应不过来，这里重新赋值一下
   };
    
    
    //获取机构列表
    $scope.getLoanCompanies = function(){
    	 $http({
             url:"superBank/getLoanList",
             method:"POST"
         }).success(function(msg){
             if(msg.status){
                 $scope.loanCompanies = msg.data;
                 $scope.companiesInAdd = msg.data.concat();
                 $scope.loan = msg.data[0];
                 $scope.loanCompanies.unshift({id:'-1',loanAlias:"全部"});
             }
         }).error(function(){
             $scope.notice("获取机构列表异常");
         });
    };
    $scope.getLoanCompanies();
    
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
    
    //编辑配置
    $scope.creditCardConfEdit = function(entity){
        if(!entity.profitType){
            $scope.notice("奖金方式不能为空");
            return;
        }
    	 entity.action=2;
    };
    
    //更新
    $scope.creditCardUpd = function(entity){
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
                    $http.post("superBank/updateLoanConf",angular.toJson(entity))
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
            url: 'superBank/loanConfList?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
            data: $scope.conf,
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
   
    $scope.addLoan = function(){
        $scope.loan.orgId = "0";
        $scope.loan.sourceId = $scope.companiesInAdd[0].id;
    	$("#addLoan").modal("show");
    };
    
    $scope.cancel = function(){
    	$("#addLoan").modal("hide");
    };
    
    //新增
    $scope.saveLoanCompany = function(){
 	 $http.post("superBank/addLoanCompany",angular.toJson($scope.loan))
 		.success(function(data){
            $scope.notice(data.msg);
 			if(data.status){
 				$scope.cancel();
 				$scope.query();
 			}else{
 				$scope.submitting = false;
 			}
 		});
    };
    
    //页面绑定回车事件
    $document.bind("keypress", function(event) {
        $scope.$apply(function (){
            if(event.keyCode == 13){
                $scope.query();
            }
        });
    });
});