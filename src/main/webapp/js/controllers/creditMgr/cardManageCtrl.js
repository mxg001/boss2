/**
 * 卡片管理
 */
angular.module('inspinia',['infinity.angular-chosen']).controller('cardManageCtrl',function($scope,$http,$state,$stateParams,i18nService,$document,SweetAlert,$timeout){
	//数据源
	i18nService.setCurrentLang('zh-cn');

	$scope.paginationOptions = {pageNo : 1,pageSize : 10};
	$scope.cardStatusSelect = [{text:"已删除",value:'0'},{text:"正常",value:'1'}];

	//获取所有组织
	$scope.org=[{value:"", text:"全部"}];
	$http.post("cmUserManage/selectOrgAllInfo")
		.success(function(msg){
			//响应成功
			for(var i=0; i<msg.length; i++){
				$scope.org.push({value:msg[i].orgId, text:msg[i].orgId + " " + msg[i].orgName});
			}
		});

	$scope.resetForm = function () {
		$scope.info = {sCreateTime:moment(new Date().getTime()-6*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
	            eCreateTime:moment(new Date().getTime()).format('YYYY-MM-DD'+' 23:59:59'), srcOrgId:""};
	}
	$scope.resetForm();

	$scope.columnDefs = [
		{field: 'id',displayName: '卡片ID',width: 120},
		{field: 'userNo',displayName: '用户ID',width: 150},
		{field: 'userMobile',displayName: '登录手机号码',width: 150},
		{field: 'orgName',displayName: '所属组织',width: 150},
		{field: 'cardNo',displayName: '卡号',width: 150},
		{field: 'bankName',displayName: '银行名称',width: 150},
		{field: 'userName',displayName: '姓名',width: 120},
		{field: 'mobileNo',displayName: '预留手机号码',width: 150,cellTemplate:
			'<div class="col-sm-12 checkbox" ng-show="row.entity.action==1">{{row.entity.mobileNo}}</div>'
			+'<div class="col-sm-12 checkbox" ng-show="row.entity.action==2"><input ng-model="row.entity.mobileNo"/></div>'},
		{field: 'mail',displayName: '邮箱',width: 150,cellTemplate:
			'<div class="col-sm-12 checkbox" ng-show="row.entity.action==1">{{row.entity.mail}}</div>'
			+'<div class="col-sm-12 checkbox" ng-show="row.entity.action==2"><input ng-model="row.entity.mail"/></div>'},
		{field: 'totalAmount',displayName: '总额度',width: 150,cellTemplate:
			'<div class="col-sm-12 checkbox" ng-show="row.entity.action==1">{{row.entity.totalAmount}}</div>'
			+'<div class="col-sm-12 checkbox" ng-show="row.entity.action==2"><input ng-model="row.entity.totalAmount"/></div>'},
		{field: 'creditScore',displayName: '积分',width: 150},
		{field: 'statementDate',displayName: '账单日',width: 150,cellTemplate:
			'<div class="col-sm-12 checkbox">每月{{row.entity.statementDate}}日</div>'},
		{field: 'repaymentDate',displayName: '还款日',width: 150,cellTemplate:
			'<div class="col-sm-12 checkbox">每月{{row.entity.repaymentDate}}日</div>'},
		{field: 'remindTime',displayName: '还款提醒（提前N天）',width: 150,cellTemplate:
			'<div class="col-sm-12 checkbox">{{row.entity.remindTime}}天</div>'},
		{field: 'cardStatus',displayName: '状态',width: 120,cellFilter:"formatDropping:"+angular.toJson($scope.cardStatusSelect)},
		{field: 'createTime',displayName: '创建时间',width: 150,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
		{field: 'action',displayName: '操作',width: 140,pinnedRight:true,editable:true,cellTemplate:
			'<a class="lh30" ui-sref="creditMgr.cardDetail({id:row.entity.id})" target="_black">详情</a>'
			+'<a class="lh30" ng-show="grid.appScope.hasPermit(\'creditMgr.updateCard\')&&row.entity.action==1" ng-click="grid.appScope.edit(row.entity)"> | 修改</a>'
			+'<a class="lh30" ng-show="grid.appScope.hasPermit(\'creditMgr.updateCard\')&&row.entity.action==2" ng-click="grid.appScope.save(row.entity)"> | 保存</a>'
		}
	];

	$scope.myGrid = {
		paginationPageSize:10,                  //分页数量
		paginationPageSizes: [10,20,50,100],	//切换每页记录数
		useExternalPagination: true,		  //开启拓展名
		enableHorizontalScrollbar: true,        //横向滚动条
		enableVerticalScrollbar : true,  		//纵向滚动条
		columnDefs: $scope.columnDefs,
		onRegisterApi: function(gridApi) {
			$scope.gridApi = gridApi;
			$scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
				$scope.paginationOptions.pageNo = newPage;
				$scope.paginationOptions.pageSize = pageSize;
				$scope.query();
			});
		}
	};

	$scope.query = function () {
		if ($scope.loadImg) {
			return;
		}
		$scope.loadImg = true;
		$http({
			url: 'cmCard/selectCardInfo?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
			data: $scope.info,
			method:'POST'
		}).success(function (data) {
			$scope.loadImg = false;
			if (!data.status){
				$scope.notice(data.msg);
				return;
			}
			$scope.myGrid.data = data.page.result;
			$scope.myGrid.totalItems = data.page.totalCount;
			angular.forEach($scope.myGrid.data,function(item){
				item.action = 1;
				if (!item.creditScore) {
					item.creditScore = '---';
				}
			})
		}).error(function (msg) {
			$scope.notice('服务器异常,请稍后再试.');
			$scope.loadImg = false;
		});
	};

	//编辑配置
	$scope.edit = function(entity){
		 entity.action=2;
	}

	//保存配置
	$scope.save = function(entity){
		if (!(entity.mobileNo && entity.totalAmount)) {
			$scope.notice("数据不能为空");
			return;
		}
		if(!entity.mobileNo.match(/^1[3-9]\d{9}$/)){
			$scope.notice("手机号码格式不正确");
            return;
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
					$http.post("cmCard/updateCardInfo",angular.toJson(entity))
						.success(function(data){
							$scope.notice(data.msg);
							if(data.status){
								$scope.query();
							}
						});
	            }
	    });
	}

	//导出
	$scope.exportInfo=function(){
		if($scope.info.sCreateTime == "" || $scope.info.eCreateTime == ""){
			$scope.notice("创建时间不能为空");
			return;
		}
		var stime = new Date($scope.info.sCreateTime).getTime();
		var etime = new Date($scope.info.eCreateTime).getTime();
		var sum = (etime - stime) / (24 * 60 * 60 * 1000);
		if(sum > 31){
			$scope.notice("创建时间的跨度不能超过一个月");
			return;
		}
		SweetAlert.swal({
			title: "确认导出？",
			showCancelButton: true,
			confirmButtonColor: "#DD6B55",
			confirmButtonText: "确定",
			cancelButtonText: "取消",
			closeOnConfirm: true,
			closeOnCancel: true 
		},
		function (isConfirm) {
			if (isConfirm) {
				location.href="cmCard/exportCmCard?info="+encodeURI(angular.toJson($scope.info));
			}
		});
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