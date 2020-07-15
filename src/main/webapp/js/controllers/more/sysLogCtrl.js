/**
 * 日志管理
 */
angular.module('inspinia').controller('sysLogCtrl',function($scope,$http,$state,$stateParams,i18nService,$document){
	i18nService.setCurrentLang('zh-cn');
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
//	$scope.baseInfo = {};
	$scope.resetForm = function(){
		$scope.baseInfo = {parent_menu_id:0,menu_id:0,oper_id:0
				,oper_start_time:moment(new Date().getTime()-6*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00'
				,oper_end_time:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'};
	}
	$scope.resetForm();
	$scope.logData=[];
	$scope.operStatus = [{text:'成功', value:1},{text:'失败', value:0}];
	$scope.parentMenuList = [];
	$scope.menuList = [];
	$scope.operList = [];
	//获取parentMenuList
	$http.get("menu/getMenuByParent?parentId=" + 0).success(function(msg){
		$scope.baseInfo.parent_menu_id = 0;
		$scope.parentMenuList = msg;
		$scope.parentMenuList.unshift({'id':0,"menuName":'全部'});
		$scope.getMenuList();
	})
	$scope.getMenuList = function(){
		$scope.baseInfo.menu_id = 0;
		if($scope.baseInfo.parent_menu_id != 0){
			$http.get("menu/getMenuByParent?parentId=" + $scope.baseInfo.parent_menu_id).success(function(msg){
				$scope.menuList = msg;
				$scope.menuList.unshift({'id':0,"menuName":'全部'});
			});
		} else {
			$scope.menuList = [{'id':0,"menuName":'全部'}];
		}
		$scope.getOperList();
	}
	$scope.getOperList = function(){
		$scope.baseInfo.oper_id = 0;
		if($scope.baseInfo.menu_id != 0){
			$http.get("menu/getMenuByParent?parentId=" + $scope.baseInfo.menu_id).success(function(msg){
				$scope.operList = msg;
				$scope.operList.unshift({'id':0,"menuName":'全部'});
			});
		} else {
			$scope.operList = [{'id':0,"menuName":'全部'}];
		}
	}
	
	$scope.logGrid = {
		data: 'logData',
		paginationPageSize: 10,
		paginationPageSizes: [10, 20, 50, 100],
		useExternalPagination: true,		  	//开启拓展名
		columnDefs: [
		    {field: 'id',displayName:'日志编号'},
            {field: 'user_name',  displayName: '操作人'},
            {field: 'oper_code',  displayName: '菜单编码'},
            {field: 'menu_name', width:300, displayName: '菜单名称'},
            {field: 'oper_ip', displayName: 'IP'},
            {field: 'oper_time', displayName: '操作时间', cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
            {field: 'oper_status', displayName: '操作状态',cellFilter:"formatDropping:" + angular.toJson($scope.operStatus)},
            {field: 'options',  displayName: '操作',pinnedRight:true, cellTemplate:
            	'<div class="lh30"><a ng-click="grid.appScope.logDetail(row.entity.id)">详情</a></div>'
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
		$scope.loadImg = true;
		$scope.submitting = true;
//		if($scope.baseInfo.startDate>$scope.baseInfo.endDate){
//			$scope.notice("起始时间不能早于终止时间");
//			return;
//		}
		if(!$scope.baseInfo.oper_start_time){
			$scope.notice("起始时间不能为空");
			$scope.loadImg = false;
			$scope.submitting = false;
			return;
		}
		$http.post('sysLog/queryByCondition',"baseInfo="+angular.toJson($scope.baseInfo)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+
			$scope.paginationOptions.pageSize,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
				.success(function(msg){
					$scope.loadImg = false;
					$scope.submitting = false;
					if(msg.status){
						$scope.logData = msg.page.result;
						$scope.logGrid.totalItems = msg.page.totalCount;
					} else {
						$scope.notice(msg.msg);
					}
				}).error(function(){
					$scope.loadImg = false;
					$scope.submitting = false;
				});
	}
//	$scope.query();
	
	//详情
	$scope.logInfo = {};
	$scope.logDetail = function(id){
		$scope.logInfo = {};
		$('#logDetailModal').modal('show');
		$http.get('sysLog/queryDetail/' + id).success(function(msg){
			$scope.logInfo = msg;
		});
	}
	
	$scope.cancel = function(){
		$('#logDetailModal').modal('hide');
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