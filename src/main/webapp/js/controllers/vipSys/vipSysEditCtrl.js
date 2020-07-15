/**
 * 会员系统活动详情
 */
angular.module('inspinia',['uiSwitch']).controller('vipSysEditCtrl',function(i18nService,$scope,$http,$state,$stateParams,
                                                                                $compile,$filter,SweetAlert,$location){
    $scope.goodsType = [{text:"请选择",value:null}];//物品类型
    $scope.priorities = [{text:"请选择",value:null}];//优先级
    $scope.purposes = [{text:"请选择",value:null}];//用途
    $scope.submitAction = null;


    //编辑券配置
    var url = "";
    if("view" == $stateParams.view){
        $("#coupon_title").text("物品详情");
        $scope.isView = true;
    }else{
        $scope.isView = false;
        $("#coupon_title").text("修改物品");
    }
    if("add" != $stateParams.view){
        $http.get("couponActivity/vipSysEntityDetail?entityId="+$stateParams.id)
            .success(function(msg){
                if(msg.status){
                    $scope.couponEntity = msg.entity;
                    $scope.changeGoodsType();
                  $scope.submitAction = "couponActivity/updateVipTicket";
                }
            }).error(function(){
            $scope.submitting = false;
        })
    }else{
        $scope.submitAction = "couponActivity/vipSysEntityAdd";
        var actId = $stateParams.id;
        $scope.info={};
        $scope.couponEntity = {couponType:null,activityFirst:null,manYiJinShow:false,purpose:null,effectiveDays:null,backRate:null,couponCode:actId,activetiyCode:actId};
        $("#totalAmount").val(null);
        $scope.isView = false;
        $("#coupon_title").text("新增物品");
    }

    $scope.changeGoodsType = function(){
      if($scope.couponEntity.couponType == "3"){//抵用金
        $scope.couponEntity.manYiJinShow = false;
        $scope.couponEntity.cancelVerificationCode = "2";
      }else if($scope.couponEntity.couponType == "7"){//满溢金
        $scope.couponEntity.manYiJinShow = true;
        $scope.couponEntity.cancelVerificationCode = "5";
      }else if($scope.couponEntity.couponType == "6"){//鼓励金
        $scope.couponEntity.manYiJinShow = false;
        $scope.couponEntity.cancelVerificationCode = "1";
      }
      if($scope.isView && $scope.couponEntity.couponType != "7"){
        $scope.couponEntity.couponStandard = null;
        $scope.couponEntity.backRate = null;
      }

      if($scope.couponEntity.couponType==null || $scope.couponEntity.couponType == ""){
        $scope.couponEntity.purpose = null;
      }else{
        for(var i=0; i<$scope.purposes.length; i++){
          if($scope.purposes[i].value == $scope.couponEntity.cancelVerificationCode){
            $scope.couponEntity.purpose = $scope.purposes[i].text;
          }
        }
      }




    }

    //获取物品类型
    $http.get("sysDict/getListByKey.do?sysKey=vipSys_goodsType")
      .success(function(res){
        for(var i=0; i<res.length; i++){
          $scope.goodsType.push({value:res[i].sysValue,text:res[i].sysName});
        }
      });
    //获取优先级
    $http.get("sysDict/getListByKey.do?sysKey=vipSys_priorities")
      .success(function(res){
        for(var i=0; i<res.length; i++){
          $scope.priorities.push({value:res[i].sysValue,text:res[i].sysName});
        }
      });
  //获取用途
  $http.get("sysDict/getListByKey.do?sysKey=CANCEL_VERIFICATION_CODE")
    .success(function(res){
      for(var i=0; i<res.length; i++){
        $scope.purposes.push({value:res[i].sysValue,text:res[i].sysName});
      }
    });

    $scope.submitCouponRecharge = function(){

    	$scope.submitting = true;
        var data={
            info:angular.toJson($scope.couponEntity)
        };
        var postCfg = {
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            transformRequest: function (data) {
                return $.param(data);
            }
        };
		$http.post($scope.submitAction,data,postCfg)
			.success(function(msg){
				$scope.notice(msg.msg);
				$scope.submitting = false;
        $scope.back();
			}).error(function(){
				$scope.notice("提交失败");
				$scope.submitting = false;
			})
    }

    $scope.cancel = function(){
    	$('#newCouponRecharge').modal('hide');
    }
  //奖励有效期天数格式
	$scope.isDays = function(obj,attr){
		var exp = /^[1-9]\d*$/;
		var num = obj[attr];
		if(!exp.test(num)){
			$scope.notice("有效期天数格式不对");
			obj[attr] = null;
		   }
		}

	//金额格式(?!0\d)
	$scope.isBigdecimal = function(obj,attr){
		var exp = /^([1-9][?!0\d]{0,7}|0)(\.[\d]{1,2})?$/;
		var num = obj[attr];
		var msg = "";
		if(attr=="couponAmount"){
      msg = "价值";
    }else if(attr=="couponStandard"){
      msg = "达标金额";
      if(Number(num)<=0){
        $scope.notice(msg+"格式不正确");
        obj[attr] = null;
        return;
      }
    }else if(attr=="backRate"){
      msg = "返现比例";
    }
		if(!exp.test(num)){
			$scope.notice(msg+"格式不正确");
			obj[attr] = null;
			return;
		}
		if(attr=="backRate" && (num<=0 || num >100)){
      $scope.notice(msg+"格式不正确");
      obj[attr] = null;
    }
  }

  $scope.back = function () {
      $location.url(('/func/vipSys/vipSys/'+$scope.couponEntity.activetiyCode+'/')+($stateParams.returnFlag==0?'view':'edit'));
  }

});

