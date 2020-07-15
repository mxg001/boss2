/**
 * 充值返活动详情
 */
angular.module('inspinia',['uiSwitch']).controller('couponRechargeEditCtrl',function(i18nService,$scope,$http,$state,$stateParams,$compile,$filter,SweetAlert){

    //编辑券配置
    if("view" == $stateParams.view){
        $("#coupon_title").text("充值券配置详情");
        $scope.isView = true;
    }else{
        $scope.isView = false;
        $("#coupon_title").text("修改充值券配置");
    }
    if("add" != $stateParams.view){
        $http.get("couponActivity/couponEntityView?entityId="+$stateParams.id)
            .success(function(msg){
                if(msg.status){
                    $scope.couponEntity = msg.entity;
                    $scope.formatResult = msg.couponTime;
                    $("#totalAmount").val($scope.couponEntity.giftAmount*1+$scope.couponEntity.couponAmount*1);
                }
            }).error(function(){
            $scope.submitting = false;
        })
	}else{
        $scope.info={};
        $scope.couponEntity = {integralScale:100,cancelVerificationCode:2,couponCode:3,activetiyCode:3,isshelves:0,isstatus:0};
        $("#totalAmount").val(null);
        $scope.isView = false;
        $("#coupon_title").text("新增充值券配置");
	}

    //编辑券配置

    $scope.submitCouponRecharge = function(){
    	$scope.submitting = true;
        var data={
            info:angular.toJson($scope.couponEntity),
            formatList:angular.toJson($scope.formatResult),
            deleteinfo:angular.toJson($scope.deleteResult)
        };
        var postCfg = {
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            transformRequest: function (data) {
                return $.param(data);
            }
        };
		$http.post("couponActivity/saveCouponEntity",data,postCfg)
			.success(function(msg){
				$scope.notice(msg.msg);
				$scope.submitting = false;
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

    $scope.modeState=true;
    $scope.info={};
    $scope.formatResult=[];
    $scope.deleteResult=[];

    $scope.formatGrid={                           //配置表格
        data: 'formatResult',
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'startTime',displayName:'开始时间',width:200},
            { field: 'endTime',displayName:'结束时间',width:200},
            { field: 'id',displayName:'操作',width:150, cellTemplate:
                '<div class="lh30" ng-show="'+!$scope.isView+'">'+
                '<a ng-click="grid.appScope.editData(1,row.entity)"">编辑 </a> ' +
                '</div>'
            }
        ],
        onRegisterApi: function(gridApi) {
            $scope.formatGridApi = gridApi;
        }
    };

    $scope.addModal = function(){
        $scope.modeState=true;
        $scope.info={};
        $('#addModal').modal('show');
    };
    $scope.addcancel = function(){
        $scope.info={};
        $('#addModal').modal('hide');
    };
    $scope.saveModal = function(mode){
        if($scope.info!=null){
            dateList=$scope.formatResult;
            if(mode=="add"){
                if($scope.checkDate(dateList,$scope.info,null)){
                    dateList.push({
                        startTime:$scope.info.startTime,
                        endTime:$scope.info.endTime
                    });
                    $scope.addcancel();
                }
            }else if(mode=="edit"){
                var dateList;
                var item=$scope.oldBaseInfo;
                if($scope.checkDate(dateList,$scope.info,item)) {
                    for (var j = 0; j < dateList.length; j++) {
                        var dateItem = dateList[j];
                        if (item.startTime == dateItem.startTime
                            && item.endTime == dateItem.endTime) {
                            dateItem.startTime = $scope.info.startTime;
                            dateItem.endTime = $scope.info.endTime;
                        }
                    }
                    $scope.addcancel();
                }
            }

        }
    };
    $scope.checkDate = function(dateList,info,oldInfo){
        if(info.startTime==null||info.startTime==""
            ||info.endTime==null||info.endTime==""){
            $scope.notice("上下线时间不能为空!");
            return false;
        }
        if(dateList!=null&&dateList.length>0){
            var num=0;
            for(var i=0;i<dateList.length;i++){
                var item=dateList[i];
                if(oldInfo!=null){
                    if(item.startTime==oldInfo.startTime
                        &&item.endTime==oldInfo.endTime){
                        num=i;
                        if(num<dateList.length-1&&num>0){
                            if(dateList[num-1].endTime>=info.startTime){
                                $scope.notice("上架起止时间不符合规则，请重新编辑");
                                return false;
                            }
                            if(dateList[num+1].startTime<=info.endTime){
                                $scope.notice("上架起止时间不符合规则，请重新编辑");
                                return false;
                            }
                        }else if(num<dateList.length-1&&num==0){
                            if(dateList[num+1].startTime<=info.endTime){
                                $scope.notice("上架起止时间不符合规则，请重新编辑");
                                return false;
                            }
                        }else if(num==dateList.length-1&&num>0){
                            if(dateList[num-1].endTime>=info.startTime){
                                $scope.notice("上架起止时间不符合规则，请重新编辑");
                                return false;
                            }
                        }
                        continue;
                    }
                }else{
                    if(item.endTime>=info.startTime){
                        $scope.notice("上架起止时间不符合规则，请重新编辑");
                        return false;
                    }
                }
                if(item.startTime==info.startTime
                    &&item.endTime==info.endTime){
                    $scope.notice("上下线时间已存在,添加失败");
                    return false;
                }
            }
        }
        return true;
    };

    $scope.editData = function(sta,entry){
        $scope.info={};
        $scope.oldBaseInfo={};
        $scope.oldBaseInfo = angular.copy(entry);
        $scope.info=angular.copy(entry);
        if(sta==0){
            $scope.modeState=true;
        }else{
            $scope.modeState=false;
        }
        $('#addModal').modal('show');
    };

    $scope.delteData = function(){
        var selectList;
        var dateList;
        selectList = $scope.formatGridApi.selection.getSelectedRows();
        dateList=$scope.formatResult;

        if(selectList==null||selectList.length==0){
            $scope.notice("请选中要删除的上下线时间数据!");
            return false;
        }
        if(selectList!=null&&selectList.length>0){
            for(var i=0;i<selectList.length;i++){
                var item=selectList[i];
                for(var j=0;j<dateList.length;j++){
                    var dateItem=dateList[j];
                    if(item.endTime==dateItem.endTime
                        &&item.endTime==dateItem.endTime){
                        dateList.splice(j, 1);
                        $scope.deleteResult.push(dateItem);
                    }
                }
            }
        }
    };
});

