/**
 * 鼓励金详情
 */
angular.module('inspinia').controller('registeredDetailCtrl',function($scope,$http,$stateParams){

	$scope.myGrid = {                  //配置表格
		    enableHorizontalScrollbar: 1,       //横向滚动条
		    enableVerticalScrollbar : 1,  		//纵向滚动条
		    columnDefs:[                        //表格数据
		    	{ field: 'couponName',displayName:'券名称',width:200},
				{ field: 'couponAmount',displayName:'券面值',width:200},
				{ field: 'effectiveDays',displayName:'奖励有效期',width:300,cellTemplate:'<span>从获得奖励当天算起  {{row.entity.effectiveDays}} 天内使用有效</span>'}
		    ]
		};

	//鼓励金详情
	$scope.query = function(){
		$http.get('couponActivity/registeredDetail')
			.success(function(msg){
				if(msg.status){
					$scope.info = msg.info;
				} else {
					$scope.notice("查询活动配置失败");
				}
			});

		$http.get('couponActivity/selectRegisteredCoupon')
			.success(function(msg){
				if(msg.status){
					$scope.myGrid.data = msg.couponList;
				} else {
					$scope.notice("查询券信息失败");
				}
			});
	}
	$scope.query();
});

