/**
 * 优惠券活动设置
 */
angular.module('inspinia').controller('couponActivityCtrl',function($scope,$http,$state,$stateParams,$compile,$filter,$log,$uibModal,$timeout){

//查询优惠券活动设置信息
	$scope.query = function(){
		$http.get('couponActivity/getInfo')
		.success(function(msg){
			if(msg.status){
				$scope.rewardActivityGrid.data = msg.raList;
				console.debug($scope.rewardActivityGrid.data);
			} else {
				$scope.notice(msg.msg);
			}
		});
	}
	$scope.query();
	$scope.rewardActivityGrid = {                  //配置表格
	    enableHorizontalScrollbar: 1,       //横向滚动条
	    enableVerticalScrollbar : 1,  		//纵向滚动条
	    columnDefs:[                        //表格数据
	       { field: 'activetiyCode',displayName:'活动编号',width:110},
	       { field: 'activetiyType',displayName:'活动行为',width:120},
	       { field: 'activityExplain',displayName:'活动说明'},
	       { field: 'action',displayName:'操作',
	      	 cellTemplate:
             '<div ng-switch="row.entity.activetiyCode">'
						 +'<div ng-switch-when="17">'
						 +'<span><a  class="lh30" ng-show="grid.appScope.hasPermit(\'couponActivity.vipSysDetail\')"  ui-sref="func.vipSys({actId:row.entity.id,edit:\'view\'})">详情</a><span>'
						 +'<span><a  class="lh30" ng-show="grid.appScope.hasPermit(\'couponActivity.vipSysUpdate\')" ui-sref="func.vipSys({actId:row.entity.id,edit:\'edit\'})"> | 修改</a></span>'
						 +'</div>'
						 +'<div ng-switch-when="15">'
						 +'<span><a  class="lh30" ng-show="grid.appScope.hasPermit(\'couponActivity.guLiJinDetail\')"  ui-sref="func.guLiJin({actId:row.entity.id,edit:\'view\'})">详情</a><span>'
						 +'<span><a  class="lh30" ng-show="grid.appScope.hasPermit(\'couponActivity.guLiJinSave\')" ui-sref="func.guLiJin({actId:row.entity.id,edit:\'edit\'})"> | 修改</a></span>'
						 +'</div>'
						 +'<div ng-switch-when="16">'
						 +'<span><a  class="lh30" ng-show="grid.appScope.hasPermit(\'couponActivity.diYongJinDetail\')"  ui-sref="func.diYongJin({actId:row.entity.id,edit:\'view\'})">详情</a><span>'
						 +'<span><a  class="lh30" ng-show="grid.appScope.hasPermit(\'couponActivity.diYongJinSave\')" ui-sref="func.diYongJin({actId:row.entity.id,edit:\'edit\'})"> | 修改</a></span>'
						 +'</div>'
						 +'<div ng-switch-when="12">'
						 +'<span><a  class="lh30" ng-show="grid.appScope.hasPermit(\'couponActivity.cardAndRewardDetail\')"  ui-sref="func.cardAndReward({actId:row.entity.id,edit:\'view\'})">详情</a><span>'
						 +'<span><a  class="lh30" ng-show="grid.appScope.hasPermit(\'couponActivity.cardAndRewardSave\')" ui-sref="func.cardAndReward({actId:row.entity.id,edit:\'edit\'})"> | 修改</a></span>'
						 +'</div>'
						 +'<div ng-switch-when="13">'
						 +'<span><a  class="lh30" ng-show="grid.appScope.hasPermit(\'couponActivity.loanAndRewardDetail\')"  ui-sref="func.loanAndReward({actId:row.entity.id,edit:\'view\'})">详情</a><span>'
						 +'<span><a  class="lh30" ng-show="grid.appScope.hasPermit(\'couponActivity.loanAndRewardSave\')" ui-sref="func.loanAndReward({actId:row.entity.id,edit:\'edit\'})"> | 修改</a></span>'
						 +'</div>'
             +'<div ng-switch-when="3">'
             +'<span><a  class="lh30" ng-show="grid.appScope.hasPermit(\'func.couponActivityDetail\')"  ui-sref="func.couponRecharge({actId:row.entity.id,edit:\'view\'})">详情</a><span>'
             +'<span><a  class="lh30" ng-show="grid.appScope.hasPermit(\'func.editcouponActivity\')" ui-sref="func.couponRecharge({actId:row.entity.id,edit:\'edit\'})"> | 修改</a></span>'
             +'</div>'
             +'<div ng-switch-when="6">'
             +'<span><a  class="lh30" ng-show="grid.appScope.hasPermit(\'func.couponActivityDetail\')"  ui-sref="func.buyReward({actId:row.entity.id,edit:\'view\'})">详情</a><span>'
             +'<span><a  class="lh30" ng-show="grid.appScope.hasPermit(\'func.editcouponActivity\')" ui-sref="func.buyReward({actId:row.entity.id,edit:\'edit\'})"> | 修改</a></span>'
             +'</div>'
             +'<div ng-switch-when="11">'
             +'<span><a  class="lh30" ng-show="grid.appScope.hasPermit(\'func.couponActivityDetail\')"  ui-sref="func.buyReward({actId:row.entity.id,edit:\'view\'})">详情</a><span>'
             +'<span><a  class="lh30" ng-show="grid.appScope.hasPermit(\'func.editcouponActivity\')" ui-sref="func.buyReward({actId:row.entity.id,edit:\'edit\'})"> | 修改</a></span>'
             +'</div>'
             +'<div ng-switch-when="4">'
             +'<span><a  class="lh30" ng-show="grid.appScope.hasPermit(\'invitePrizes.saveInfo\')"  ui-sref="func.invitePrizes({actId:row.entity.id,edit:\'view\'})">详情</a><span>'
             +'<span><a  class="lh30" ng-show="grid.appScope.hasPermit(\'invitePrizes.saveInfo\')" ui-sref="func.invitePrizes({actId:row.entity.id,edit:\'edit\'})"> | 修改</a></span>'
             +'</div>'
             +'<div ng-switch-when="5">'
             +'<span><a  class="lh30" ng-show="grid.appScope.hasPermit(\'invitePrizes.saveInfo\')"  ui-sref="func.cloudPay({actId:row.entity.id,edit:\'view\'})">详情</a><span>'
             +'<span><a  class="lh30" ng-show="grid.appScope.hasPermit(\'invitePrizes.saveInfo\')" ui-sref="func.cloudPay({actId:row.entity.id,edit:\'edit\'})"> | 修改</a></span>'
             +'</div>'
             +'<div ng-switch-when="1">'
             +'<span><a  class="lh30" ng-show="grid.appScope.hasPermit(\'func.couponActivityDetail\')"  ui-sref="func.registeredActivityDetail({actId:row.entity.id})">详情</a><span>'
             +'<span><a  class="lh30" ng-show="grid.appScope.hasPermit(\'func.editcouponActivity\')" ui-sref="func.editRegisteredActivity({actId:row.entity.id})"> | 修改</a></span>'
             +'</div>'
             +'<div ng-switch-when="7">'
             +'<span><a  class="lh30"  ui-sref="func.zxIndustryDetail({activetiyCode:row.entity.activetiyCode})">详情</a><span>'
             +'<span><a  class="lh30" ng-show="grid.appScope.hasPermit(\'func.zxIndustryUpdate\')" ui-sref="func.zxIndustryUpdate({activetiyCode:row.entity.activetiyCode})"> | 修改</a></span>'
             +'</div>'
             +'<div ng-switch-when="8">'
             +'<span><a  class="lh30"  target="_blank" ui-sref="func.prizeConfigureDetail({funcCode:row.entity.activetiyCode})">详情</a><span>'
             +'<span><a  class="lh30" ng-show="grid.appScope.hasPermit(\'prizeConfigure.updatePrizeConfigure\')" target="_blank" ui-sref="func.prizeConfigure({funcCode:row.entity.activetiyCode})"> | 修改</a></span>'
             +'</div>'
             +'<div ng-switch-when="9">'
             +'<span><a  class="lh30" ng-show="grid.appScope.hasPermit(\'func.redemptionActivityDetail\')"  ui-sref="func.redemptionActivity">详情</a><span>'
             +'</div>'
             +'<div ng-switch-when="10">'
             +'<span><a  class="lh30" ng-show="grid.appScope.hasPermit(\'couponActivity.couponActivityVip\')"  ui-sref="func.activityVip({actId:row.entity.id,edit:\'view\'})">详情</a><span>'
             +'<span><a  class="lh30" ng-show="grid.appScope.hasPermit(\'couponActivity.editCouponActivityVip\')" ui-sref="func.activityVip({actId:row.entity.id,edit:\'edit\'})"> | 修改</a></span>'
             +'</div>'
             +'<div ng-switch-when="14">'
             +'<span><a  class="lh30" ng-show="grid.appScope.hasPermit(\'couponActivity.couponActivityVip\')"  ui-sref="func.activityVip({actId:row.entity.id,edit:\'view\'})">详情</a><span>'
             +'<span><a  class="lh30" ng-show="grid.appScope.hasPermit(\'couponActivity.editCouponActivityVip\')" ui-sref="func.activityVip({actId:row.entity.id,edit:\'edit\'})"> | 修改</a></span>'
             +'</div>'
             +'<div ng-switch-default>'
             +'<span><a  class="lh30" ng-show="grid.appScope.hasPermit(\'func.couponActivityDetail\')"  ui-sref="func.couponActivityDetail({actId:row.entity.id})">详情</a><span>'
             +'<span><a  class="lh30" ng-show="grid.appScope.hasPermit(\'func.editcouponActivity\')" ui-sref="func.editcouponActivity({actId:row.entity.id})"> | 修改</a></span>'
             +'</div>'
             +'</div>'
               ,width:130
	       }
	    ]
	};
});

