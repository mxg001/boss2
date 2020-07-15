/**
 * 优惠券
 */
angular.module('inspinia').controller('queryCuponCtrl',function($scope,$http,$state,$stateParams,$compile,$uibModal,$timeout,$log,i18nService,SweetAlert,$document){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	$scope.paginationOptions=angular.copy($scope.paginationOptions);

	$scope.loadImg = false;
	$scope.agent = [{text:"全部",value:""}];
	$scope.oneAgent = [{text:"全部",value:""}];
    $scope.payMethodList = [{text:"POS",value:"1"},{text:"支付宝",value:"2"},{text:"微信",value:"3"},{text:"快捷",value:"4"}];


	$scope.couponTypeSelect = angular.copy($scope.couponTypeList);
	$scope.couponTypeStr=angular.toJson($scope.couponTypeSelect);
	$scope.couponTypeSelect.unshift({text:"全部",value:""});

    //清空
	$scope.clear = function(){
		isVerifyTime = 1;
		$scope.info = {merchantName:"",couponCode:"",mobilephone:"",couponNo:"",agentName:"",oneAgentName:"",couponType:"",
				startTime:moment(new Date().getTime()-6*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
				endTime:moment(new Date().getTime()).format('YYYY-MM-DD'+' 23:59:59')};
	}
	$scope.clear();
	//代理商
	 $http.post("agentInfo/selectAllInfo")
 	 .success(function(msg){
 			//响应成功"
 	   	for(var i = 0; i < msg.length; i++){
 	   		$scope.agent.push({value:msg[i].agentNo,text:msg[i].agentName});
 	   	}
 	});
	//一级代理商
	$http.post("agentInfo/selectByLevelOne.do")
		.success(function(msg){
			//响应成功"
			for(var i = 0; i < msg.length; i++){
				$scope.oneAgent.push({value:msg[i].agentNo,text:msg[i].agentName});
			}
	});

	//是否校验时间
	isVerifyTime = 1;//校验：1，不校验：0

	keyChange=function(){
		if ($scope.info.merchantName || $scope.info.mobilephone || $scope.info.couponNo
				|| ($scope.info.couponNoBeg && $scope.info.couponNoEnd)
				|| ($(d5223).val() && $(d5224).val())) {
			isVerifyTime = 0;
		} else {
			isVerifyTime = 1;
		}
	}

	setBeginTime=function(setTime){
		$scope.info.startTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
	}
	setEndTime=function(setTime){
		$scope.info.endTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
	}

	//活动优惠券领用列表
	$scope.query = function(sta){
		if ($scope.loadImg) {
			return;
		}
		if (!($scope.info.merchantName || $scope.info.mobilephone || $scope.info.couponNo
				|| ($scope.info.couponNoBeg && $scope.info.couponNoEnd)
				|| ($(d5223).val() && $(d5224).val()))) {
			if(!($scope.info.startTime && $scope.info.endTime)){
				$scope.notice("获得时间不能为空");
				return;
			}
			var stime = new Date($scope.info.startTime).getTime();
			var etime = new Date($scope.info.endTime).getTime();
			if ((etime - stime) > (31 * 24 * 60 * 60 * 1000)) {
				$scope.notice("获得时间范围不能超过31天");
				return;
			}
		}
		$scope.loadImg = true;
	    $http.post("couponActivity/getCouponAll","info="+angular.toJson($scope.info)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
				{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			.success(function(msg){
				if(msg.status){
					$scope.gridOptions.data = msg.page.result;
				} else {
					$scope.notice(msg.msg);
				}
				$scope.loadImg = false;
			});
		if(sta==2){
			$http.post("couponActivity/getCouponAllSum","info="+angular.toJson($scope.info),
				{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
				.success(function(msg){
					if(msg.status){
						$scope.gridOptions.totalItems = msg.total.totalNum;
						$scope.total = msg.total;//统计信息
					} else {
						$scope.notice(msg.msg);
					}
				});
		}
	}

	$scope.export= function () {
		if (!($scope.info.merchantName || $scope.info.mobilephone || $scope.info.couponNo
				|| ($scope.info.couponNoBeg && $scope.info.couponNoEnd)
				|| ($(d5223).val() && $(d5224).val()))) {
			if(!($scope.info.startTime && $scope.info.endTime)){
				$scope.notice("获得时间不能为空");
				return;
			}
			var stime = new Date($scope.info.startTime).getTime();
			var etime = new Date($scope.info.endTime).getTime();
			if ((etime - stime) > (31 * 24 * 60 * 60 * 1000)) {
				$scope.notice("获得时间范围不能超过31天");
				return;
			}
		}
		SweetAlert.swal({
				title: "确认导出？",
				showCancelButton: true,
				confirmButtonColor: "#DD6B55",
				confirmButtonText: "提交",
				cancelButtonText: "取消",
				closeOnConfirm: true,
				closeOnCancel: true
			},
			function (isConfirm) {
				if (isConfirm) {
					$scope.exportInfoClick("couponActivity/exportCoupon",{"info":angular.toJson($scope.info)});
				}
			});
	}
	$scope.addCoupon={couponCode:""};
	$scope.addNewCoupon = function () {
		$("#addCouponModal").modal("show");
	}
	$scope.addCouponModalHide = function () {
		$("#addCouponModal").modal("hide");
		$scope.addCoupon={};
	}

	$scope.addCouponSave = function () {
		if(!$scope.addCoupon.couponCode){
			$scope.notice("请选择参与活动！");
			return;
		}
		if(!$scope.addCoupon.activityEntityId){
			$scope.notice("请选择赠送券类型！");
			return;
		}
		if(!$scope.addCoupon.addNum){
			$scope.notice("请填写赠送数量！");
			return;
		}else{
			if(isNaN($scope.addCoupon.addNum)){
				$scope.notice("赠送数量只能是数字！");
				return;
			}
		}
		if(!$scope.addCoupon.merchantNo){
			$scope.notice("请填写商户编号！");
			return;
		}

		SweetAlert.swal({
				title: "确定新增？",
				showCancelButton: true,
				confirmButtonColor: "#DD6B55",
				confirmButtonText: "提交",
				cancelButtonText: "取消",
				closeOnConfirm: true,
				closeOnCancel: true
			},
			function (isConfirm) {
				if (isConfirm) {
					$http.post("couponActivity/addNewCoupon","info="+angular.toJson($scope.addCoupon),{headers: {'Content-Type': 'application/x-www-form-urlencoded'}}).success(function (data) {
						$scope.notice(data.msg);
						$("#addCouponModal").modal("hide");
						$scope.addCoupon={couponCode:""};
					})
				}
			});
	}

	$scope.merInfoQuery=function () {
		if($scope.addCoupon.merchantNo){
			$http.post("couponActivity/merInfo","merchantNo="+$scope.addCoupon.merchantNo,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}}).success(function (data) {
				if(!data){
					$scope.notice("当前商户不存在！");
					$scope.addCoupon.merchantNo="";
				}else{
					$scope.addCoupon.merchantName=data.merchantName;
				}
			})
		}
	}

	$scope.actEntitys=[];
	$scope.actEntityList=function () {
		console.log($scope.addCoupon)
		$http.post("couponActivity/actEntityList","couponCode="+$scope.addCoupon.couponCode,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}}).success(function (data) {
			$scope.actEntitys=data;
			if(data.length > 0)
				$scope.addCoupon.activityEntityId=data[0].id;
		})
	}

	$scope.abandonedCoupon = function (id) {
		SweetAlert.swal({
				title: "确定废弃？",
				showCancelButton: true,
				confirmButtonColor: "#DD6B55",
				confirmButtonText: "提交",
				cancelButtonText: "取消",
				closeOnConfirm: true,
				closeOnCancel: true
			},
			function (isConfirm) {
				if (isConfirm) {
					$http.post("couponActivity/abandonedCoupon","id="+id,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}}).success(function (data) {
						$scope.notice(data.msg)
					})
				}
			});
	}
	$scope.gridOptions = {       
		paginationPageSize:10,                  //分页数量
	    paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
	    useExternalPagination: true,                //分页数量//配置表格
	    columnDefs:[                        //表格数据
	       {field: 'id', displayName: '序号',width:80 },
	       { field: 'merchantName',displayName:'商户名称',width:160 },
	       { field: 'merchantNo',displayName:'商户编号',width:130 },
	       { field: 'mobilephone',displayName:'商户手机号',width:120 },
	       { field: 'couponCode',displayName:'参与活动',width:90 },
	       { field: 'agentName',displayName:'直属代理商',width:160 },
	       { field: 'oneAgentName',displayName:'一级代理商',width:160 },
	       { field: 'transAmount',displayName:'购买金额',width:90 },
	       { field: 'giftAmount',displayName:'赠送金额',width:90 },
	       { field: 'faceValue',displayName:'总价值金额',width:90 },
	       { field: 'balance',displayName:'可用金额',width:90 },
	       { field: 'useValue',displayName:'已使用金额',width:90 },
			{ field: 'couponType',displayName:'券类型',width:130,cellFilter:"formatDropping:"+$scope.couponTypeStr},
			{ field: 'couponStatus',displayName:'使用状态',width:100 },
	       { field: 'startTime',displayName:'获得日期',cellFilter: 'date:"yyyy-MM-dd"',width:100},
	       { field: 'endTime',displayName:'失效日期',cellFilter: 'date:"yyyy-MM-dd"',width:100},
	       { field: 'couponNo',displayName:'券编号',width:130 },
	       { field: 'payMethod',displayName:'支付方式',width:130,cellFilter:"formatDropping:"+angular.toJson($scope.payMethodList) },
			{ field: 'payOrderNo',displayName:'支付订单号',width:130 },
	       { field: 'id',displayName:'操作',pinnedRight:true,width:130 ,cellTemplate:'<div ng-show="grid.appScope.hasPermit(\'couponActivity.abandonedActivityCoupon\')"><a ng-show="row.entity.couponStatus != \'已作废\'" ng-click="grid.appScope.abandonedCoupon(row.entity.id)">作废</a><span ng-show="row.entity.couponStatus == \'已作废\'">已作废</span></div>'},
	    ],
		onRegisterApi: function(gridApi) {                
			$scope.gridApi = gridApi;
	        gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
	        	$scope.paginationOptions.pageNo = newPage;
	          	$scope.paginationOptions.pageSize = pageSize;
	            $scope.query(1);
	        });
		}
	};
});

