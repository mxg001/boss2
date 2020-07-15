/**
 * 组织彩票总奖金配置
 */
angular.module('inspinia',['infinity.angular-chosen']).controller('lotteryBonusCtrl',function($scope,$http,i18nService,$document,$window){

    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    $scope.resetForm = function () {
        $scope.conf = {orgId:'-1'};
    };
    $scope.resetForm();

    $scope.columnDefs = [
        {field: 'id',displayName: 'ID',width: 80,pinnable: false,sortable: false},
        {field: 'orgName',displayName: '组织',width: 150,cellTemplate:'<div class="lh30" ng-show="row.entity.orgId==0">*</div><div class="lh30" ng-show="row.entity.orgId!=0">{{row.entity.orgName}}</div>'},
        {field: 'bonusType',displayName: '奖金方式',width: 150,cellTemplate:'<div class="lh30" ng-show="row.entity.bonusType==1">按购彩金额比例</div>'},
        {field: 'bonusRequest',displayName: '奖励要求',width: 100,cellTemplate:'<div class="lh30" ng-show="row.entity.bonusRequest==1">成功出票购彩</div>'},
        {field: 'lotteryOrgTotalBonus',displayName: '彩票机构总奖金',width: 160,cellTemplate:'<div class="lh30" ng-show="row.entity.action!=2">N*{{row.entity.lotteryOrgTotalBonus}}%</div><div class="lh30" ng-show="row.entity.action==2">N*<input style="text-align:center;width:50px;" ng-model="row.entity.lotteryOrgTotalBonus"/>%</div>'},
        {field: 'companyBonus',displayName: '公司截留',width: 160,cellTemplate:'<div class="lh30" ng-show="row.entity.action!=2">N*{{row.entity.companyBonus}}%</div><div class="lh30" ng-show="row.entity.action==2">N*<input style="text-align:center;width:50px;" ng-model="row.entity.companyBonus"/>%</div>'},
        {field: 'orgBonus',displayName: '品牌截留',width: 200,cellTemplate:'<div class="lh30" ng-show="row.entity.action!=2">N*{{row.entity.orgBonus}}%</div><div class="lh30" ng-show="row.entity.action==2">N*<input style="text-align:center;width:50px;" ng-model="row.entity.orgBonus"/>%</div>'},
        
        {field: 'action',displayName: '操作',width: 120,pinnedRight:true,
        	cellTemplate:
        	'<div  class="lh30" ng-show="row.entity.action!=2"><input type="hidden" ng-model="row.entity.sourceId"/><input type="hidden" ng-model="row.entity.id"/><input type="hidden" ng-model="row.entity.action" /><a ng-show="grid.appScope.hasPermit(\'activity.editActivityHardWare\')"  ng-click="grid.appScope.lotteryConfEdit(row.entity)">编辑</a></div>'
        	+
        	'<div  class="lh30" ng-show="row.entity.action==2"><a ng-show="grid.appScope.hasPermit(\'activity.editActivityHardWare\')" ng-click="grid.appScope.lotteryUpd(row.entity)">保存</a></div>'
        }
    ];
    
    $scope.lotteryBonusGrid = {
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

    
    //获取组织
    $scope.getOrgList = function(){
   	 $http({
            url:"superBank/getOrgInfoList",
            method:"POST"
        }).success(function(msg){
            if(msg.status){
            	$scope.orgInfoList = msg.data;
            	$scope.orgInfoAllList= angular.copy(msg.data);
            	$scope.orgInfoList.unshift({orgId:'0',orgName:"*"});
                $scope.orgInfoAllList.unshift({orgId:'-1',orgName:"全部"},{orgId:'0',orgName:"*"});
            }
        }).error(function(){
            $scope.notice("获取组织列表异常");
        });
   };
   $scope.getOrgList();
    
    //编辑配置
    $scope.lotteryConfEdit = function(entity){
    	 entity.action=2;
    };
    
    //更新
    $scope.lotteryUpd = function(entity){
    	 var data = {
    			 	"id" : entity.id,
    				"lotteryOrgTotalBonus":entity.lotteryOrgTotalBonus,
    				"companyBonus":entity.companyBonus,
    				"orgBonus":entity.orgBonus
    		};
    	 $http.post("superBank/updateLotteryConf",angular.toJson(data))
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
            url: 'superBank/lotteryConfList?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
            data: $scope.conf,
            method:'POST'
        }).success(function (msg) {
            $scope.submitting = false;
            $scope.loadImg = false;
            if (!msg.status){
                $scope.notice(msg.msg);
                return;
            }
            $scope.lotteryBonusGrid.data = msg.data.result;
            $scope.lotteryBonusGrid.totalItems = msg.data.totalCount;
        }).error(function (msg) {
            $scope.submitting = false;
            $scope.loadImg = false;
            $scope.notice('服务器异常,请稍后再试.');
        });
    };
   
    $scope.addLottery = function(){
    	$("#addLottery").modal("show");
    };
    
    $scope.cancel = function(){
    	$("#addLottery").modal("hide");
    };
    
   
    
    //新增
    $scope.savelottery = function(){
    	
    	 var data = {
 				"orgId":$scope.lottery.orgId,
 				"bonusType":$scope.lottery.bonusType,
 				"bonusRequest":$scope.lottery.bonusRequest,
 				"lotteryOrgTotalBonus":$scope.lottery.lotteryOrgTotalBonus,
 				"companyBonus":$scope.lottery.companyBonus,
 				"orgBonus":$scope.lottery.orgBonus
 		};
 	 $http.post("superBank/addOrgLottery",angular.toJson(data))
 		.success(function(data){
 			if(data.status){
 				//$scope.notice(data.msg);
 				alert(data.msg);
 				$scope.cancel();
 				$scope.query();
 			}else{
 				$scope.notice(data.msg);
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