/**
 * 信用卡配置
 */
angular.module('inspinia',['infinity.angular-chosen']).controller('creditcardBonusCtrl',function($scope,$http,i18nService,$document,$window){

    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    $scope.isOpenList = [{text:"全部",value:""},{text:"是",value:1},{text:"否",value:0}];
    
    $scope.resetForm = function () {
        $scope.creditCardConf = {sourceId:-1,orgId:'-1',isOpen:''};//查询条件的form
        $scope.creditCard = {sourceId:-1,orgId:'0'};//新增数据的form
    };
    $scope.resetForm();

    $scope.columnDefs = [
        {field: 'id',displayName: 'ID',width: 100,pinnable: false,sortable: false},
        {field: 'orgName',displayName: '组织',pinnable: false,sortable: false},
        {field: 'bankNickName',displayName: '银行别称',pinnable: false,sortable: false},
       /* {field: 'bankBonus',displayName: '银行发放总奖金',cellFilter:"currency:''",cellTemplate:'<div class="lh30" ng-show="row.entity.action!=2">{{row.entity.bankBonus}}</div><div class="lh30" ng-show="row.entity.action==2"><input style="height:28px;width:100px;text-align:center;" ng-model="row.entity.bankBonus"/></div>'},
        {field: 'orgCost',displayName: '公司收入',cellFilter:"currency:''",cellTemplate:'<div class="lh30" ng-show="row.entity.action!=2">{{row.entity.orgCost}}</div><div class="lh30" ng-show="row.entity.action==2"><input style="height:28px;width:100px;text-align:center;" ng-model="row.entity.orgCost"/></div>'},
        {field: 'orgPushCost',displayName: '品牌发放总奖金',cellFilter:"currency:''",cellTemplate:'<div class="lh30" ng-show="row.entity.action!=2">{{row.entity.orgPushCost}}</div><div class="lh30" ng-show="row.entity.action==2"><input style="height:28px;width:100px;text-align:center;" ng-model="row.entity.orgPushCost"/></div>'},
        {field: 'isOnlyone',displayName: '首次办卡才有奖励',cellTemplate:'<div class="lh30" ng-show="row.entity.action!=2"><label ng-show="row.entity.isOnlyone==0">否</label><label ng-show="row.entity.isOnlyone==1">是</label></div><div class="lh30" ng-show="row.entity.action==2"><select ng-model="row.entity.isOnlyone"><option selected="selected" value="0">否</option><option value="1">是</option></select></div>'},
        */
        {field: 'isOpen',displayName: '是否外放组织',width: 150,pinnable: false,sortable: false,cellFilter:"formatDropping:" + angular.toJson($scope.isOpenList)},
        {field: 'cardBonus',displayName: '发卡奖金',pinnable: false,sortable: false,cellTemplate:
            '<div class="lh30" >{{row.entity.cardBonus==null?0.00:row.entity.cardBonus|currency:\'\'}}</div>'},
        {field: 'firstBrushBonus',displayName: '首刷奖金',pinnable: false,sortable: false,cellTemplate:
            '<div class="lh30" >{{row.entity.firstBrushBonus==null?0.00:row.entity.firstBrushBonus|currency:\'\'}}</div>'},
       {field: 'cardCompanyBonus',displayName: '发卡公司截留',cellFilter:"currency:''",cellTemplate:'<div class="lh30" ng-show="row.entity.action!=2">{{row.entity.cardCompanyBonus}}</div><div class="lh30" ng-show="row.entity.action==2"><input style="height:28px;width:100px;text-align:center;" ng-model="row.entity.cardCompanyBonus"/></div>'},
       {field: 'cardOemBonus',displayName: '发卡品牌截留',cellFilter:"currency:''",cellTemplate:'<div class="lh30" ng-show="row.entity.action!=2">{{row.entity.cardOemBonus}}</div><div class="lh30" ng-show="row.entity.action==2"><input style="height:28px;width:100px;text-align:center;" ng-model="row.entity.cardOemBonus"/></div>'},
        {field: 'firstCompanyBonus',displayName: '首刷公司截留',cellFilter:"currency:''",cellTemplate:'<div class="lh30" ng-show="row.entity.action!=2">{{row.entity.firstCompanyBonus}}</div><div class="lh30" ng-show="row.entity.action==2"><input style="height:28px;width:100px;text-align:center;" ng-model="row.entity.firstCompanyBonus"/></div>'},
        {field: 'firstOemBonus',displayName: '首刷品牌截留',cellFilter:"currency:''",cellTemplate:'<div class="lh30" ng-show="row.entity.action!=2">{{row.entity.firstOemBonus}}</div><div class="lh30" ng-show="row.entity.action==2"><input style="height:28px;width:100px;text-align:center;" ng-model="row.entity.firstOemBonus"/></div>'},
        {field: 'action',displayName: '操作',pinnedRight:true,
        	cellTemplate:
        	'<div  class="lh30" ng-show="row.entity.action!=2"><input type="hidden" ng-model="row.entity.sourceId"/><input type="hidden" ng-model="row.entity.id"/><input type="hidden" ng-model="row.entity.action" /><a ng-show="grid.appScope.hasPermit(\'creditcardBonus.edit\')"  ng-click="grid.appScope.creditCardConfEdit(row.entity)">编辑</a></div>'
        	+
        	'<div  class="lh30" ng-show="row.entity.action==2"><a ng-show="grid.appScope.hasPermit(\'creditcardBonus.edit\')" ng-click="grid.appScope.creditCardUpd(row.entity)">保存</a></div>'
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

    
    $scope.changeOps = function (op) {
		 angular.forEach($scope.banksListInAdd, function(item){
			 if(item.id==op){
				 $scope.creditCard.bankBonus=item.bankBonus;
				 $scope.creditCard.cardBonus = (item.cardBonus != undefined && item.cardBonus != null)?item.cardBonus : 0.00 ;
				 $scope.creditCard.firstBrushBonus = (item.firstBrushBonus != undefined && item.firstBrushBonus != null)?item.firstBrushBonus :0.00 ;
	     	}
	     });
    };
    
    $scope.banksListInAdd = [];
    $scope.banksList = [];
    $scope.orgInfoList = [];
    $scope.orgInfoListInAdd = [];
    //获取银行列表
    $scope.getBanks = function(){
    	 $http({
             url:"superBank/banksList",
             method:"POST"
         }).success(function(msg){
             if(msg.status){
                 $scope.banksList = msg.data;
                 $scope.banksListInAdd = angular.copy(msg.data);
                 // $scope.creditCard={sourceId:msg.data[0].id,bankBonus:msg.data[0].bankBonus};
                 $scope.banksList.unshift({id:-1,bankNickName:'全部'});
             }
         }).error(function(){
             $scope.notice("获取银行列表异常");
         });
    };
    $scope.getBanks();
    
    //获取组织
    $scope.getOrgList = function(){
   	 $http({
            url:"superBank/getOrgInfoList",
            method:"POST"
        }).success(function(msg){
            if(msg.status){
                $scope.orgInfoList = msg.data;
                $scope.orgInfoList.unshift({orgId:'0',orgName:"默认"});
                $scope.orgInfoListInAdd = angular.copy($scope.orgInfoList);//新增页面orgInfoListInAdd只需要默认和所有组织，不需要全部，'默认'就是全部
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
    
    function isNumber(value){
    	var reg= /^([1-9]\d{0,9}|0)(\.\d{1,2})?$/g;  
    	return reg.test(value);
    }
    
    function checkData(entity){
    	var cardCompanyBonus = entity.cardCompanyBonus;
    	var cardOemBonus = entity.cardOemBonus;
    	var firstCompanyBonus = entity.firstCompanyBonus;
    	var firstOemBonus = entity.firstOemBonus;
    	var cardBonus = entity.cardBonus;
    	var firstBrushBonus = entity.firstBrushBonus;
    	if(!isNumber(cardCompanyBonus)){  
	  		 $scope.notice("发卡公司截留金额格式不正确：只能为正数且最多2位小数！");
			return false;
	  	}else if(!isNumber(cardOemBonus)){
	  		 $scope.notice("发卡OEM截留金额格式不正确：只能为正数且最多2位小数！");
			return false;
		}else if(!isNumber(firstCompanyBonus)){
	  		 $scope.notice("首刷公司截留金额格式不正确：只能为正数且最多2位小数！");
			return false;
		}else if(!isNumber(firstOemBonus)){
	  		 $scope.notice("首刷OEM截留金额格式不正确：只能为正数且最多2位小数！");
			return false;
		}else if(Number(cardBonus)<Number(cardCompanyBonus)+Number(cardOemBonus)){ 
            $scope.notice("发卡公司截留奖金+发卡品牌截留奖金必须<=发卡奖金")
            return false;
        }else if(Number(firstBrushBonus)<Number(firstCompanyBonus)+Number(firstOemBonus)){ 
            $scope.notice("首刷公司截留奖金+首刷品牌截留奖金必须<=首刷奖金")
            return false;
        }
    	return true;
    }
    
    //更新
    $scope.creditCardUpd = function(entity){
    	
    	if(!checkData(entity)){
    		return;
    	}
    	
    	 $http.post("superBank/updateCreditCardConf",angular.toJson(entity))
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
            url: 'superBank/getCreditCardConf?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
            data: $scope.creditCardConf,
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
   
    $scope.addBank = function(){
    	$scope.creditCard = {};
    	$scope.creditCard.orgId = "0";
    	$("#addBank").modal("show");
    };
    
    $scope.cancel = function(){
    	$("#addBank").modal("hide");
    };
    
    //新增
    $scope.saveBank = function(){
    	// var data = {
 		// 	 	"sourceId" : $scope.creditCard.sourceId,
 		// 		"orgCost":$scope.creditCard.orgCost,
 		// 		"orgPushCost":$scope.creditCard.orgPushCost,
 		// 		"bankBonus":$scope.creditCard.bankBonus,
 		// 		"isOnlyone":$scope.creditCard.isOnlyone
 		// };
    	if($scope.creditCard.sourceId==undefined ||$scope.creditCard.sourceId==''||$scope.creditCard.sourceId=='-1'){
	 		$scope.notice("请选择银行");
			return false;
	  	}
    	if(!checkData($scope.creditCard)){
    		return;
    	}
    	
 	 $http.post("superBank/addCredtiCardConf",angular.toJson($scope.creditCard))
 		.success(function(data){
            $scope.notice(data.msg);
 			if(data.status){
 				$("#addBank").modal("hide");
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