/**
 * 日历管理
*/
angular.module('inspinia').controller('calendarCtrl',function($scope,$http,$state,$stateParams,i18nService,SweetAlert,$document){
	i18nService.setCurrentLang('zh-cn');
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	$scope.baseInfo = {type:0};
	$scope.newInfo = {type:1,startDate:moment(new Date().getTime()).format('YYYY-MM-DD'),status:1};
	$scope.statusType = [{text:"正常",value:1},{text:"失效",value:0}];
	$scope.allType = [{text:"全部",value:0},{text:"法定节假日",value:1},{text:"周末",value:2}];
	$scope.types = [{text:"法定节假日",value:1},{text:"周末",value:2}];
	$scope.dayOfWeek='[{text:"星期日",value:1},{text:"星期一",value:2},{text:"星期二",value:3},{text:"星期三",value:4},{text:"星期四",value:5},'
		+'{text:"星期五",value:6},{text:"星期六",value:7}]';
	$scope.calendarData=[];
	$scope.type=1;
	$scope.calendar = {
		data: 'calendarData',
		paginationPageSize: 10,
		paginationPageSizes: [10, 20, 50, 100],
		useExternalPagination: true,		  	//开启拓展名
		columnDefs: [
            {field: 'eventName',width: 200, displayName: '事件名称'},
            {field: 'sysDate',width: 200, displayName: '系统时间',cellFilter:'date:"yyyy-MM-dd"'},
            {field: 'year',width: 100, displayName: '年'},
            {field: 'month',width: 100, displayName: '月'},
            {field: 'day',width: 100, displayName: '日'},
            {field: 'week',width: 100, displayName: '星期', cellFilter:"formatDropping:"+$scope.dayOfWeek},
            {field: 'type',width: 100, displayName: '类型', cellFilter:"formatDropping:"+ angular.toJson($scope.allType)},
            {field: 'status',width: 100, displayName: '状态', cellFilter:"formatDropping:"+ angular.toJson($scope.statusType)},
            {field: 'options',width: 200, displayName: '操作',pinnedRight:true, cellTemplate:
            	'<div class="lh30"><a ng-show="grid.appScope.hasPermit(\'calendar.update\')" ng-click="grid.appScope.editUserModal(row.entity)">修改</a>'
               +'<a ng-show="grid.appScope.hasPermit(\'calendar.delete\')" ng-click="grid.appScope.deleteMenu(row.entity.id)"> | 删除</a></div>'
            }
        ],
        onRegisterApi: function(gridApi){
        	$scope.gridApi = gridApi;
        	$scope.gridApi.pagination.on.paginationChanged($scope, function(newPage, pageSize){
        		$scope.paginationOptions.pageNo = newPage;
        		$scope.paginationOptions.pageSize = pageSize;
        		$scope.query();
        	});
        }
	};
	
	//查询
	$scope.query = function(){
//		if($scope.baseInfo.startDate>$scope.baseInfo.endDate){
//			$scope.notice("起始时间不能早于终止时间");
//			return;
//		}
		$http.post('sysCalendar/selectCalendarByCondition.do',"baseInfo="+angular.toJson($scope.baseInfo)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+
			$scope.paginationOptions.pageSize,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
				.success(function(page){
					if(!page){
						return;
					}
					$scope.calendarData = page.result;
					$scope.calendar.totalItems = page.totalCount;
				}).error(function(){
				});
	}
	$scope.query();
	
	//增加
	$scope.calendarModal = function(){
		$scope.newInfo = {type:1,sysDate:moment(new Date().getTime()).format('YYYY-MM-DD'),status:1};
		$scope.type=1;
		$("#calendarModal").modal("show");
	}
	//修改
	$scope.editUserModal = function(entity){
		$scope.newInfo = angular.copy(entity);
		$scope.type=2;
		$("#calendarModal").modal("show");
	}
	$scope.deleteMenu=function(id){
        SweetAlert.swal({
            title: "确认删除？",
//            text: "",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            closeOnConfirm: true,
            closeOnCancel: true },
	        function (isConfirm) {
	            if (isConfirm) {
	            	$http.post("sysCalendar/deleteCalendar.do","id="+id,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
	        		.success(function(msg){
	        			$scope.notice(msg.msg);
	        			$scope.query();
	        		}).error(function(){
	        		});
	            }
        });
    };
	$scope.submitNewInfo = function(){
		$scope.submitting = true;
		var url="";
		if($scope.type==1){
			url="sysCalendar/saveCalendar.do";
		}else{
			url="sysCalendar/updateCalendar.do";
		}
		$http.post(url,$scope.newInfo)
		.success(function(msg){
			if(msg.status){
				$("#calendarModal").modal("hide");
				$scope.query();
			}
			$scope.notice(msg.msg);
			$scope.submitting = false;
		}).error(function(){
			$scope.submitting = false;
		});
	}
	$scope.cancel=function(){
		$("#calendarModal").modal("hide");
	}
	
	//清空查询条件
	$scope.resetForm = function(){
		$scope.baseInfo = {};
	}

	//页面绑定回车事件
	$document.bind("keypress", function(event) {
		$scope.$apply(function (){
			if(event.keyCode == 13){
				$scope.query();
			}
		})
	});
	
});