/**
 * 鼓励金详情
 */
angular.module('inspinia').controller('loanAndRewardCtrl',function(SweetAlert,$scope,$http,$stateParams,$state){
	
	if("edit"==$stateParams.edit){
		$scope.couponEdit=true;
	}else{
		$scope.couponEdit=false;
	}
	
	
	
	$scope.myGrid = {                  //配置表格
		    enableHorizontalScrollbar: 1,       //横向滚动条
		    enableVerticalScrollbar : 1,  		//纵向滚动条
		    columnDefs:[                        //表格数据
		    	{ field: 'couponName',displayName:'券名称',width:200},
				{ field: 'couponAmount',displayName:'券面值',width:200,	//cellFilter:"currency:''",
					cellTemplate:'<div class="col-sm-12 checkbox" ng-show="row.entity.action==1">{{row.entity.couponAmount}}</div><div class="col-sm-12 checkbox" ng-show="row.entity.action==2"><input ng-model="row.entity.couponAmount"/></div>'},
				{ field: 'effectiveDays',displayName:'奖励有效期',width:300,
						cellTemplate: '<div class="col-sm-12 checkbox" ng-show="row.entity.action==1">从获得奖励当天算起  {{row.entity.effectiveDays}} 天内使用有效</div><div class="col-sm-12 checkbox" ng-show="row.entity.action==2"><input ng-model="row.entity.effectiveDays"/></div>'},
				{ field: 'action',displayName:'操作',width:200,
					cellTemplate:
		      		'<a class="lh30" ng-show="row.entity.action==1&& '+$scope.couponEdit+' && grid.appScope.hasPermit(\'func.editcouponActivity\')" ng-click="grid.appScope.actEdit(row.entity)"><input type="hidden" ng-model="row.entity.action" />编辑</a>'+
					'<a class="lh30" ng-show="row.entity.action==2 && '+$scope.couponEdit+' && grid.appScope.hasPermit(\'func.editcouponActivity\')" ng-click="grid.appScope.actSave(row.entity)">保存</a>'
				}
		    ]
	};
	
	
	
	//鼓励金详情
	$scope.query = function(){
		$http.get('couponActivity/rewardDetail?actId='+$stateParams.actId)
		.success(function(msg){
			if(msg.status){
				$scope.info = msg.info;
			} else {
				$scope.notice(msg.msg);
			}
		});
		
		$http.get('couponActivity/selectCardAndReward?activetiyCode=13')
		.success(function(msg){
			if(msg.status){
				$scope.myGrid.data = msg.couponList;
				angular.forEach($scope.myGrid.data,function(item){
					item.action=1;
				})
			} else {
				$scope.notice("查询券信息失败");
			}
		});
	}
	$scope.query();
	
	$scope.commit = function(){
		$scope.subDisable = true;
		$http.post('couponActivity/loanAndRewardSave',
				"info="+angular.toJson($scope.info),
				 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		.success(function(data){
			if(data.status){
				$scope.notice(data.msg);
				$state.transitionTo('func.couponActivity',null,{reload:true});
				$scope.subDisable = false;
			}else{
				$scope.notice(data.msg);
				$scope.subDisable = false;
			}
		});
	}
	
	
	//编辑配置
	$scope.actEdit = function(entity){
		 entity.action=2;
	}

	//保存配置
	$scope.actSave = function(entity){
		var isNum=/^(([1-9][0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,2})))$/;
		if(entity.couponAmount == undefined || entity.couponAmount==null || entity.couponAmount==""){
			$scope.notice("券面值不能为空!");
			return;
		}else{
			if(!isNum.test(entity.couponAmount)){
				$scope.notice("券面值格式不正确!");
				return;
			}
		}
		if(entity.effectiveDays == undefined || entity.effectiveDays==null || entity.effectiveDays==""){
			$scope.notice("奖励有效期不能为空!");
			return;
		}else{
			if(!isNum.test(entity.effectiveDays)){
				$scope.notice("奖励有效期格式不正确!");
				return;
			}
		}
		SweetAlert.swal({
			title: "确认保存？",
			showCancelButton: true,
			confirmButtonColor: "#DD6B55",
			confirmButtonText: "保存",
			cancelButtonText: "取消",
			closeOnConfirm: true,
			closeOnCancel: true },
			function (isConfirm) {
				if (isConfirm) {
//					var data = { "info" : entity };
					$http.post("couponActivity/updateRegisteredCoupon",angular.toJson(entity))
						.success(function(data){
							if(data.status){
								entity.action = 1;
							}
							$scope.notice(data.msg);
						});
	            }
	    });
	}
	
	
	//赠送金额格式
	$scope.isBigdecimal = function(obj,attr){
		var exp = /^([1-9][\d]{0,7}|0)(\.[\d]{1,2})?$/;
		var num = obj[attr];
		if(!exp.test(num)){
			$scope.notice("赠送金额格式不正确");
			obj[attr] = null;
		   }
		}
	//可获取数量格式
	$scope.isNum = function(obj,attr){
		var exp = /^[1-9]\d*$/;
		var num = obj[attr];
		if(!exp.test(num) && num != -1){
			$scope.notice("可获取数量格式不对");
			obj[attr] = null;
		   }
		}
	//奖励有效期天数格式
	$scope.isDays = function(obj,attr){
		var exp = /^[1-9]\d*$/;
		var num = obj[attr];
		if(!exp.test(num)){
			$scope.notice("奖励有效期天数格式不对");
			obj[attr] = null;
		   }
		}
});

