/**
 * 充值返活动详情
 */
angular.module('inspinia',['uiSwitch']).controller('diYongJinCtrl',function(i18nService,$scope,$http,$state,$stateParams,$compile,$filter,SweetAlert){

	//鼓励金详情
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
					'<span><a  class="lh30" ui-sref="func.diYongJinEdit({id:row.entity.id,view:\'view\'})" >详情</a><span>'
					+'<span><a  class="lh30" ui-sref="func.diYongJinEdit({id:row.entity.id,view:\'edit\'})"> | 修改</a></span>'
				,width:130
			}
		]
	};
	//状态修改：购买状态、下架状态通用
	$scope.openStatus_Shelves=function(row,col){
		if("status"==col){
			if(row.entity.isstatus){
				$scope.serviceText = "确定开启？";
			} else {
				$scope.serviceText = "确定关闭？";
			}
		}else{
			if(row.entity.isshelves){
				$scope.serviceText = "确定开启？";
			} else {
				$scope.serviceText = "确定关闭？";
			}
		}

		SweetAlert.swal({
				title: $scope.serviceText,
				type: "warning",
				showCancelButton: true,
				confirmButtonColor: "#DD6B55",
				confirmButtonText: "提交",
				cancelButtonText: "取消",
				closeOnConfirm: true,
				closeOnCancel: true },
			function (isConfirm) {
				if (isConfirm) {
				debugger;
					var val;
					if("status"==col){
						if(row.entity.isstatus==true){
							val = 0;
						} else if(row.entity.isstatus==false){
							val = 1;
						}
					}else{
						if(row.entity.isshelves==true){
							val = 1;
						} else if(row.entity.isshelves==false){
							val = 0;
						}
					}
					$http.get("couponActivity/updateStatus?id="+row.entity.id+"&col="+col+"&val="+val)
						.success(function(data){
							if(data.status){
								$scope.notice("操作成功！");
							}else{
								if("status"==col){
									if(row.entity.isstatus==true){
										row.entity.isstatus=false;
									}else {
										row.entity.isstatus=true;
									}
								}else{
									if(row.entity.isshelves==true){
										row.entity.isshelves=false;
									} else {
										row.entity.isshelves=true;
									}
								}
								$scope.notice("操作失败！");
							}
						})
						.error(function(data){
							if("status"==col){
								if(row.entity.isstatus==true){
									row.entity.isstatus=false;
								}else {
									row.entity.isstatus=true;
								}
							}else{
								if(row.entity.isshelves==true){
									row.entity.isshelves=false;
								} else {
									row.entity.isshelves=true;
								}
							}
							$scope.notice("服务器异常")
						});
				} else {
					if("status"==col){
						if(row.entity.isstatus==true){
							row.entity.isstatus=false;
						}else {
							row.entity.isstatus=true;
						}
					}else{
						if(row.entity.isshelves==true){
							row.entity.isshelves=false;
						} else {
							row.entity.isshelves=true;
						}
					}
				}
			});

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
	$scope.submitCouponRecharge = function(){
		$scope.submitting = true;
		$http.post("couponActivity/saveCouponEntity",angular.toJson({"info":$scope.couponEntity}))
			.success(function(msg){
				$scope.notice(msg.msg);
				$("#newCouponRecharge").modal("hide");
				$scope.submitting = false;
				$scope.query();
			}).error(function(){
			$scope.notice("提交失败");
			$scope.submitting = false;
		})
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

