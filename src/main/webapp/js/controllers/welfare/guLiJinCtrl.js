/**
 * 购买鼓励金活动详情
 */
angular.module('inspinia',['uiSwitch']).controller('guLiJinCtrl',function(i18nService,$scope,$http,$state,$stateParams,$compile,$filter,SweetAlert,$location){
	//购买鼓励金详情
	$scope.query = function(){
		$http.get('couponActivity/couponRecharge?actId='+$stateParams.actId+"&query=2")
			.success(function(msg){
				if(msg.status){
					$scope.info = msg.info;
					$scope.rechargeEntityGrid.data = msg.entity;
				} else {
					$scope.notice(msg.msg);
				}
			});
	}
	if("edit"==$stateParams.edit){
		$scope.couponEdit=true;
	}else{
		$scope.couponEdit=false;
	}

	$scope.add = function () {
		$location.url('/func/welfare/guLiJinEdit/'+$stateParams.actId+'/add');
	}

	$scope.query();
	$scope.rechargeEntityGrid = {                  //配置表格
		enableHorizontalScrollbar: 1,       //横向滚动条
		enableVerticalScrollbar : 1,  		//纵向滚动条
		columnDefs:[                        //表格数据
			{ field: 'id',displayName:'id',width:60},
			{ field: 'couponName',displayName:'券名称',width:120},
			// { field: 'orderBy',displayName:'序列号',width:120},
			{ field: 'couponExplain',displayName:'券说明'},
			{ field: 'surplusCount',displayName:'今日剩余',width:100},
			{ field: 'action',displayName:'操作',width:100,
				cellTemplate:
					'<span><a  class="lh30" ui-sref="func.guLiJinEdit({id:row.entity.id,view:\'view\'})">详情</a><span>'
					+'<span><a  class="lh30" ui-sref="func.guLiJinEdit({id:row.entity.id,view:\'edit\'})"> | 修改</a></span>'
				,width:130
			}
		]
	};

	$scope.commit = function(){
		$scope.subDisable=true;
		$http.post('couponActivity/saveCouponActivity',
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
	/**
	 * 计算总金额
	 */
	$scope.totalAmount = function(obj){
		var couponAmount = obj['couponAmount'];
		var giftAmount = obj['giftAmount'];
		if( undefined==couponAmount || couponAmount==null){
			couponAmount =0;
		}
		if(undefined==giftAmount || giftAmount==null){
			giftAmount =0;
		}
		$("#totalAmount").val(giftAmount*1+couponAmount*1);
	}
});

