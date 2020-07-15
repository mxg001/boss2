/**
 * 组织分润配置
 */
angular.module('inspinia',['infinity.angular-chosen']).controller('orgProfitConfCtrl',function($scope,$http,i18nService,$document,$window){

    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
//1代理授权 2信用卡申请 3收款(快捷) 4还款申请(激活还款) 5贷款注册  6贷款批贷   7还款(订单)  8 收款(支付宝) 9 收款(微信)
    $scope.productTypeList = [{"text":"代理授权","value":1},{"text":"信用卡申请","value":2},{"text":"收款(快捷)","value":3},
                        {"text":"还款申请(激活还款)","value":4},{"text":"贷款注册","value":5},{"text":"贷款批贷","value":6},
                        {"text":"还款(订单)","value":7},{"text":"收款(支付宝)","value":8},{"text":"收款(微信)","value":9},
                        {"text":"彩票代购","value":10},{"text":"征信","value":11},{"text":"保险","value":12},{"text":"违章代缴","value":14},
                        {"text":"积分商城","value":16},{"text":"完美还款","value":17}];

    $scope.resetForm = function () {
        $scope.baseInfo = {orgId:-1};
    };
    

    $scope.columnDefs = [
        {field: 'orgId',displayName: '品牌组织ID',width:120,pinnable: false,sortable: false},
        {field: 'orgName',displayName: '所属组织',cellTemplate:'<div class="lh30" ng-show="row.entity.orgId==-1">默认</div><div class="lh30" ng-show="row.entity.orgId!=-1">{{row.entity.orgName}}</div>',pinnable: false,sortable: false},
        {field: 'seltMember',displayName: '直推-专员',cellFilter:"currency:''",
            cellTemplate:
            '<div class="lh30" ng-show="row.entity.action!=2">' +
                '<span ng-show="row.entity.profitType==1">{{row.entity.seltMember}}</span>' +
                '<span ng-show="row.entity.profitType!=1">J*{{row.entity.seltMember}}%</span></div>' +
            '<div class="lh30" ng-show="row.entity.action==2">' +
                '<span ng-show="row.entity.profitType==1"><input style="text-align:center;width:80px;" ng-model="row.entity.seltMember"/>' +
                '</span><span ng-show="row.entity.profitType!=1">J*<input style="text-align:center;width:80px;" ng-model="row.entity.seltMember"/>%</span></div>'},
        {field: 'seltManager',displayName: '直推-经理',cellFilter:"currency:''",
            cellTemplate:
                '<div class="lh30" ng-show="row.entity.action!=2">' +
                    '<span ng-show="row.entity.profitType==1">{{row.entity.seltManager}}</span>' +
                    '<span ng-show="row.entity.profitType!=1">J*{{row.entity.seltManager}}%</span></div>' +
                '<div class="lh30" ng-show="row.entity.action==2">' +
                    '<span ng-show="row.entity.profitType==1"><input style="text-align:center;width:80px;" ng-model="row.entity.seltManager"/></span>' +
                     '<span ng-show="row.entity.profitType!=1">J*<input style="text-align:center;width:80px;" ng-model="row.entity.seltManager"/>%</span></div>'},
        {field: 'seltBanker',displayName: '直推-银行家',cellFilter:"currency:''",
            cellTemplate:
                '<div class="lh30" ng-show="row.entity.action!=2">' +
                    '<span ng-show="row.entity.profitType==1">{{row.entity.seltBanker}}</span>' +
                    '<span ng-show="row.entity.profitType!=1">J*{{row.entity.seltBanker}}%</span></div>' +
                '<div class="lh30" ng-show="row.entity.action==2">' +
                     '<span ng-show="row.entity.profitType==1"><input style="text-align:center;width:80px;" ng-model="row.entity.seltBanker"/></span>' +
                    '<span ng-show="row.entity.profitType!=1">J*<input style="text-align:center;width:80px;" ng-model="row.entity.seltBanker"/>%</span></div>'},
        {field: 'action',displayName: '操作',width: 120,pinnedRight:true,
        	cellTemplate:
        	'<div  class="lh30" ng-show="row.entity.action!=2"><input type="hidden" ng-model="row.entity.id"/><input type="hidden" ng-model="row.entity.action" /><a ng-show="grid.appScope.hasPermit(\'activity.editActivityHardWare\')"  ng-click="grid.appScope.creditCardConfEdit(row.entity)">编辑</a></div>'
        	+
        	'<div  class="lh30" ng-show="row.entity.action==2"><a ng-show="grid.appScope.hasPermit(\'activity.editActivityHardWare\')" ng-click="grid.appScope.updOrgProfitConf(row.entity)">保存</a></div>'
        }
    ];
    
    $scope.orgProfitGrid = {
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

    $scope.conf = {orgId:"-1"};
    
    $scope.orgInfoList = [];
    $scope.orgsInAddPage = [{orgId:'-1',orgName:"默认"}];
    
    $scope.getOrgList = function(){
    	 $http({
             url:"superBank/getOrgInfoList",
             method:"POST"
         }).success(function(msg){
             if(msg.status){

                 $scope.orgInfoList = msg.data;
                 var _msg = msg.data.concat();
                 // $scope.orgsInAddPage = msg.data.concat();//新增页的下拉框
                 // $scope.orgsInAddPage.push(msg.data.concat());//新增页的下拉框
                 for(var i=0; i<_msg.length; i++){
                     $scope.orgsInAddPage.push({orgId:_msg[i].orgId,orgName:_msg[i].orgName})
                 }
                 $scope.orgConf={orgId:$scope.orgsInAddPage[0].orgId};
                 $scope.orgInfoList.unshift({orgId:'-1',orgName:"全部"});
             }
         }).error(function(){
             $scope.notice("获取组织列表异常");
         });
    };
    
    $scope.getOrgList();
    
    
    //编辑配置
    $scope.creditCardConfEdit = function(entity){
    	 entity.action=2;
    };
    
    //保存修改
    $scope.updOrgProfitConf = function(entity){
    	 var data = {
    			 	"id" : entity.id,
    				"seltMember":entity.seltMember,
    				"seltManager":entity.seltManager,
    				"seltBanker":entity.seltBanker
    		};
    	 $http.post("superBank/updOrgProfit",angular.toJson(data))
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
            url: 'superBank/getOrgProfitConf?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
            data: $scope.conf,
            method:'POST'
        }).success(function (msg) {
            $scope.submitting = false;
            $scope.loadImg = false;
            if (!msg.status){
                $scope.notice(msg.msg);
                return;
            }
            $scope.orgProfitGrid.data = msg.data.result;
            $scope.orgProfitGrid.totalItems = msg.data.totalCount;
        }).error(function (msg) {
            $scope.submitting = false;
            $scope.loadImg = false;
            $scope.notice('服务器异常,请稍后再试.');
        });
    };
   
    $scope.addLoan = function(){
    	$("#addOrgProfitConf").modal("show");
    };
    
    $scope.cancel = function(){
    	$("#addOrgProfitConf").modal("hide");
    };
    
    //新增数据
    $scope.saveOrgProfitConf = function(){
 	 $http.post("superBank/addOrgPofitConf",angular.toJson($scope.orgConf))
 		.success(function(data){
            $scope.notice(data.msg);
 			if(data.status){
 				$scope.query();
 				$scope.cancel();
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