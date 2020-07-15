angular.module('inspinia').controller('happySendActivitySetUpCtrl',function($scope,$http,$state,$stateParams,$compile,$uibModal,$timeout,$log,i18nService,SweetAlert,$document){
$scope.info={waitDay:"",cashServiceId:"",startTime:"",endTime:"",activityCode:"002",activiyName:"欢乐送"};
$scope.hardWaredata=[];
$scope.activityCode="002";
$scope.activiyName="欢乐送";

//机具类型
$http.get('hardwareProduct/selectAllInfo.do')
.success(function(result){
	if(!result)
		return;
	$scope.termianlTypes=result;
});


$scope.query = function(){
	$http.post('activity/happySendActivity',
			"info="+angular.toJson({"activityCode":$scope.activityCode})+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
				{headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
			).success(function(data){
		if(data.status){
			$scope.gridOptions.data = data.page.result; 
  			$scope.gridOptions.totalItems = data.page.totalCount;
			console.log(data.page.result);
			$scope.gridOptions.totalItems = data.page.totalCount;
			$scope.info.waitDay = data.info.waitDay;
			$scope.info.cashServiceId = data.info.cashServiceId;
			$scope.info.startTime = data.info.startTime;
			$scope.info.endTime = data.info.endTime;
			$scope.info.agentServiceId = data.info.agentServiceId;
			angular.forEach($scope.gridOptions.data,function(item){
				item.action=1;
			})
		}else{
			$scope.notice(data.msg);
		}
		}).error(function(){
	 });
 }
$scope.query();
$scope.paginationOptions=angular.copy($scope.paginationOptions);
$scope.gridOptions={                           //配置表格
    paginationPageSize:10,                  //分页数量
    paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
    useExternalPagination: true,
	  enableHorizontalScrollbar: 1,        //横向滚动条
	  enableVerticalScrollbar : 1,  		//纵向滚动条
    columnDefs:[                           //表格数据
       { field: 'typeName',displayName:'设备类型',width:150},
       { field: 'price',displayName:'冻结金额',width:150,cellFilter:"currency:''",cellTemplate:'<div class="col-sm-8" ng-show="row.entity.action==1">{{row.entity.price}}</div><div class="col-sm-8" ng-show="row.entity.action==2"><input ng-model="row.entity.price"/></div>'},
       { field: 'targetAmout',displayName:'活动目标金额',width:150,cellFilter:"currency:''",cellTemplate:'<div class="col-sm-8" ng-show="row.entity.action==1">{{row.entity.targetAmout}}</div><div class="col-sm-8" ng-show="row.entity.action==2"><input ng-model="row.entity.targetAmout"/></div>'},
       { field: 'action',displayName:'操作',width:270,pinnedRight:true,
      	 cellTemplate:
      		 '<div  class="lh30" ng-show="row.entity.action==1"><input type="hidden" ng-model="row.entity.action" /><a ng-show="grid.appScope.hasPermit(\'activity.editActivityHardWare\')"  ng-click="grid.appScope.actHardwareEdit(row.entity)">编辑</a></div>'+
      		 '<div  class="lh30" ng-show="row.entity.action==2"><a ng-show="grid.appScope.hasPermit(\'activity.editActivityHardWare\')" ng-click="grid.appScope.actHardwareSave(row.entity)">保存</a></div>'
       }
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
//编辑配置
$scope.actHardwareEdit = function(entity){
	 entity.action=2;
}
//保存配置
$scope.actHardwareSave = function(entity){
	 var data = {
			 	"price" : entity.price,
				"targetAmout":entity.targetAmout,
				"hardId":entity.hardId
		};
	 $http.post("activity/editActivityHardWare",angular.toJson(data))
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

//添加
$scope.hardWardAddModel = function(){
	$("#hardWardAddModel").modal("show");
}
/*$scope.chName = function(){
	$scope.typeName = $scope.hdIp.typeName;
}
*/
$scope.submitCancel=function(){
	$scope.query();
}
$scope.cancel=function(){
	$scope.hardWare={hardId:"",price:"",targetAmout:""};
	$('#hardWardAddModel').modal('hide');
}
$scope.saveHardWare = function(){
	var isNum=/^(([1-9][0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,2})))$/;
	if($scope.hardWare.hardId == undefined){
		alert("请选择硬件设备");
		return;
	}
	if($scope.hardWare.price=="" ||$scope.hardWare.targetAmout== ""){  
	     alert("所传参数为空请输入");  
	     return;
	}  
	if(!isNum.test($scope.hardWare.price)){
		alert("冻结金额格式不正确");
		return;
	 }
	if(!isNum.test($scope.hardWare.targetAmout)){
		alert("活动阙值格式不正确");
		return;
	 }
	/* console.log("大小"+$scope.gridOptions.data.length);
	for(var i=0;i<$scope.gridOptions.data.length;i++){
		 console.log($scope.gridOptions.data[i].hardId);
		 console.log($scope.hdIp.hpId);
		if($scope.gridOptions.data[i].hardId==$scope.hdIp.hpId){
			alert("有重复不能重新添加");
			return;
		}
	}*/var data = {
			"hardWare" : $scope.hardWare,
			"activityCode":$scope.activityCode,
			"activiyName":$scope.activiyName
			
	};
	 $http.post("activity/addActivityHardWare",angular.toJson(data))
		.success(function(data){
			if(data.status){
				$scope.notice(data.msg);
				$("#hardWardAddModel").modal("hide");
				$scope.hardWare={hardId:"",price:"",targetAmout:""};
				$scope.query();
			}else{
				$scope.notice(data.msg);
				$scope.submitting = false;
			}
		});
	//$scope.gridOptions.data.push({"typeName":$scope.typeName,"hardId":$scope.hdIp.hpId,"price":$scope.price,"targetAmout":$scope.targetAmout});
}
function isPositiveNum(s){//是否为正整数  
    var re = /^[0-9]*[1-9][0-9]*$/ ;  
    return re.test(s)  
} 
$scope.commit = function(){	
	if($scope.info.waitDay=="" ||$scope.info.cashServiceId== ""){  
	     alert("所传参数为空请输入");  
	     return;
	}  
	if(!isPositiveNum($scope.info.waitDay)){
		$scope.notice("等待期必须为正整数");
		return;
	}
	if(!isPositiveNum($scope.info.cashServiceId)){
		$scope.notice("提现服务ID必须为正整数");
		return;
	}
	if(!isPositiveNum($scope.info.agentServiceId)){
		$scope.notice("代理商提现服务ID必须为正整数");
		return;
	}
	if($scope.info.startTime>$scope.info.endTime){
		$scope.notice("起始时间不能大于结束时间");
		return;
	}
	var data = {
			"info" : $scope.info
		};
	$http.post("activity/addHappySendActivity",angular.toJson(data))
	.success(function(data){
		if(data.status){
			$scope.notice(data.msg);
		}else{
			$scope.notice(data.msg);
			$scope.submitting = false;
		}
	});
}


});

