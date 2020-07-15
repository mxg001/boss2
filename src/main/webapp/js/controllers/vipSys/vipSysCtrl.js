/**
 * 会员活动详情
 */
angular.module('inspinia',['uiSwitch']).controller('vipSysCtrl',function(i18nService,$scope,$http,$state,$stateParams,$compile,$filter,SweetAlert,$location){
	//会员活动详情
	$scope.query = function(){
		$http.get('couponActivity/vipSysDetail?actId='+$stateParams.actId+"&query=2")
			.success(function(msg){
				if(msg.status){
					$scope.info = msg.info;
					$scope.rechargeEntityGrid.data = msg.entity;
				} else {
					$scope.notice(msg.msg);
				}
			});
	}
	var returnFlag = 0;
	if("edit"==$stateParams.edit ){
		returnFlag = 1;
		$scope.couponEdit=true;
	}else{
		returnFlag = 0;
		$scope.couponEdit=false;
	}
	$scope.add = function () {
		$location.url('/func/vipSys/vipSysEdit/'+$stateParams.actId+'/add'+'/1');
	}

	$scope.query();
	$scope.rechargeEntityGrid = {                  //配置表格
		enableHorizontalScrollbar: 1,       //横向滚动条
		enableVerticalScrollbar : 1,  		//纵向滚动条
		columnDefs:[                        //表格数据
			{ field: 'id',displayName:'id',width:60},
			{ field: 'couponName',displayName:'物品名称',width:120},
			{ field: 'couponAmount',displayName:'价值'},
			{ field: 'effectiveDays',displayName:'有效期',cellTemplate:'<div class="lh30">{{row.entity.effectiveDays}}天<div/>'},
			{ field: 'activityFirst',displayName:'优先级'},
			{ field: 'couponExplain',displayName:'券说明'},
			{ field: 'action',displayName:'操作',width:100,
				cellTemplate:
					'<span><a  class="lh30" ng-show="grid.appScope.hasPermit(\'couponActivity.vipSysEntityDetail\')"  ui-sref="func.vipSysEdit({id:row.entity.id,view:\'view\',returnFlag:\''+returnFlag+'\'})">详情</a><span>'
					+'<span><a  class="lh30" ng-show="grid.appScope.couponEdit && grid.appScope.hasPermit(\'couponActivity.updateVipTicket\')" ui-sref="func.vipSysEdit({id:row.entity.id,view:\'edit\',returnFlag:\''+returnFlag+'\'})"> | 修改</a></span>'
					+'<span><a  class="lh30" ng-show="grid.appScope.couponEdit && grid.appScope.hasPermit(\'couponActivity.delVipEntity\')"  ng-click="grid.appScope.deleteHappyReturnType(row.entity.id)"> | 删除</a></span>'
				,width:130
			}
		]
	};

	//删除
	$scope.deleteHappyReturnType = function(id){
		SweetAlert.swal({
				title: "确认删除?",
				type: "warning",
				showCancelButton: true,
				confirmButtonColor: "#DD6B55",
				confirmButtonText: "提交",
				cancelButtonText: "取消",
				closeOnConfirm: true,
				closeOnCancel: true },
			function (isConfirm) {
				if (isConfirm) {
					$http.get('couponActivity/delVipEntity?id='+id)
						.success(function(msg){
							$scope.notice(msg.msg);
							$scope.query();
						}).error(function(msg){
						$scope.notice(msg.msg);
					});
				}
			});
	};

	$scope.commit = function(){
		$scope.subDisable=true;
		$http.post('couponActivity/vipSysUpdate',
			"info="+encodeURIComponent(angular.toJson($scope.info)),
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



	//奖励有效期天数格式
	$scope.isDays = function(obj,attr){
		var exp = /^[1-9]\d*$/;
		var num = obj[attr];
		if(!exp.test(num)){
			$scope.notice("奖励有效期天数格式不对");
			obj[attr] = null;
		}
	}
	//可获取数量格式
	$scope.isNum = function(obj,attr){
		var exp = /^[1-9]\d*$/;
		var num = obj[attr];
		if(!exp.test(num) && num != -1){

			if(attr==='orderBy'){
				$scope.notice("序列号格式不对");
			}else {
				$scope.notice("数量格式不对");
			}
			obj[attr] = null;
		}
	}
	//赠送金额格式
	$scope.isBigdecimal = function(obj,attr){
		var exp = /^([1-9][\d]{0,7}|0)(\.[\d]{1,2})?$/;
		var num = obj[attr];
		if(!exp.test(num)){
			$scope.notice("金额格式不正确");
			obj[attr] = null;
			$scope.totalAmount(obj);
		}
	}

});

