/**
 * 商户修改
 */
angular.module('inspinia', ['angularFileUpload', 'uiSwitch']).controller('merchantUpdateCtrl', ['$scope', '$http', '$filter', '$log', '$state', '$stateParams', '$compile', '$uibModal', 'FileUploader', function ($scope, $http, $filter, $log, $state, $stateParams, $compile, $uibModal, FileUploader) {

	$scope.mTypes = [{text: "个人", value: "1"}, {text: "个体商户", value: "2"}, {text: "企业商户", value: "3"}];
	$scope.itemStatus = [{text: "审核失败", value: "3"}, {text: "正常", value: "4"}]
//	$scope.itemStatus=[{text:"审核失败",value:"3"},{text:"正常",value:"4"},{text:"关闭",value:"0"}]
	$scope.merStatusList = [{text: "正常", value: "1"}, {text: "关闭", value: "0"}]
	$scope.tradeType = [{text: "集群模式", value: "0"}, {text: "直清模式", value: "1"}];
	$scope.businessTypes = [];
	$scope.productTypes = [];
	$scope.twoInfo = [];
	$scope.zqAcqOrgs = [{text: "请选择", value: ""}];
	//业务产品
	$http.get('businessProductDefine/selectAllInfo.do')
			.success(function (largeLoad) {
				if (!largeLoad)
					return
				for (var i = 0; i < largeLoad.length; i++) {
					$scope.productTypes.push({value: largeLoad[i].bpId, text: largeLoad[i].bpName});
				}
			});

	//经营范围下拉框
	$http.post("merchantInfo/selectSysName")
			.success(function (msg) {
				//响应成功
				for (var i = 0; i < msg.length; i++) {
					$scope.businessTypes.push({value: msg[i].sysValue, text: msg[i].sysName});
				}
			});

	//获取所有直清收单机构
	$http.post("acqOrgAction/selectAllZqOrg")
			.success(function (msg) {
				//响应成功
				for (var i = 0; i < msg.length; i++) {
					$scope.zqAcqOrgs.push({value: msg[i].acqEnname, text: msg[i].acqName});
				}
			});

	//数据源
	$scope.rates = [];
	$scope.quotas = [];
	$scope.info = {};
	$scope.mbp = [];
	$scope.merExa = [];
	$scope.listMri = [];
	$scope.listMris = [];
	$scope.serviceStatus = [];
	$scope.terminal = [];
	$scope.listacr = [];
	$scope.checkStatus = "";
	$scope.isCheck = "";
	$scope.agent = {};
	$scope.merAgent = {};
	var quotasbz = [];
	$http.get('merchantBusinessProduct/selectDetailInfo.do?ids=' + $stateParams.mertId)
			.success(function (largeLoad) {
				if (largeLoad.listmr == null || largeLoad.listmsq == null || largeLoad.mi == null || largeLoad.mbp == null || largeLoad.listel == null || largeLoad.serviceMgr == null) {
					return;
				} else {
					$scope.merAgent = largeLoad.merAgent;
					$scope.agent = largeLoad.agent;
					$scope.rates = largeLoad.listmr;
					$scope.quotas = largeLoad.listmsq;
					quotasbz = largeLoad.sqlist;
					$scope.info = largeLoad.mi;
					$scope.mbp = largeLoad.mbp;
					$scope.terminal = largeLoad.tiPage.result;
					$scope.terminalTotal = largeLoad.tiPage.totalCount;
					$scope.listacr = largeLoad.listacr;  //isCheck  checkStatus
					$scope.checkStatus = largeLoad.checkStatus;
					$scope.isCheck = largeLoad.isCheck;
					$scope.mbp.bpId = parseInt($scope.mbp.bpId);
					if (largeLoad.agent.isApprove == 1) {
						$scope.itemStatus.push({value: 1, text: "待一审"});
					} else {
						$scope.itemStatus.push({value: 2, text: "待平台审核"});
					}
					if (isNaN($scope.mbp.bpId)) {
						$scope.mbp.bpId = $scope.productTypes[0];
					}
					$scope.merExa = largeLoad.listel;
					$scope.listMri = largeLoad.listmri;
					$scope.listMris = largeLoad.listmris;
					for (var i = 0; i < $scope.listMri.length; i++) {
						if ($scope.listMri[i].mriId == 6) {
							$scope.info.idCardNo = $scope.listMri[i].content;
						}
					}
					$scope.serviceStatus = largeLoad.serviceMgr;
					for (var i = 0; i < $scope.serviceStatus.length; i++) {
						$scope.serviceStatus[i].status = parseInt($scope.serviceStatus[i].status);
						if (isNaN($scope.serviceStatus[i].status)) {
							$scope.notice("转换失败或者值为NULL");
						}
					}
					//经营范围二级下拉框
					$http.post("merchantInfo/selectTwoSysName", "key=" + angular.toJson(largeLoad.mi.businessType), {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
							.success(function (msg) {
								$scope.twoInfo = [];
								//响应成功
								for (var i = 0; i < msg.length; i++) {
									$scope.twoInfo.push({value: msg[i].sysValue, text: msg[i].sysName});
								}
							});
				}
			});

	//经营范围的二级下拉框
	$scope.choseTwo = function (key) {
		$http.post("merchantInfo/selectTwoSysName", "key=" + angular.toJson(key), {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
				.success(function (msg) {
					$scope.twoInfo = [];
					//响应成功
					for (var i = 0; i < msg.length; i++) {
						$scope.twoInfo.push({value: msg[i].sysValue, text: msg[i].sysName});
					}
					$scope.info.industryType = msg[0].sysValue;
				});
	}

	$scope.accountTypes = function (data) {
		if (data == 1) {
			return '对公';
		}
		if (data == 2) {
			return '对私';
		}
	}
	$scope.rateTypes = [{text: "每笔固定金额", value: "1"}, {text: "每笔扣率", value: "2"}, {
		text: "每笔扣率带保底封顶",
		value: "3"
	}, {text: "每笔扣率+固定金额", value: "4"}
		, {text: "单笔阶梯扣率", value: "5"},{text:"扣率+封顶",value:6}]
	var ddser = '<div ng-switch on="$eval(\'row.entity.fixedMark\')">'
			+ '<div ng-switch-when=0>'
			+ '<input ng-blur="grid.appScope.checkQuota(row.entity.singleDayAmount,row.entity.singleMinAmount,row.entity.singleCountAmount,row.entity.singleDaycardAmount,row.entity.singleDaycardCount,row.entity.serviceId,row.entity.cardType,row.entity.holidaysMark)" style="width:98%;height:98%;" class="ui-widget input" ng-readonly="false" ng-model="row.entity[col.field]"/>'
			+ '</div>'
			+ '<div ng-switch-when=1>'
			+ '<input disabled="disabled" style="width:98%;height:98%;" class="ui-widget input" ng-readonly="false" ng-model="row.entity[col.field]"/>'
			+ '</div>'
			+ '</div>';
	$scope.merchantRateList = {
		data: 'rates',
		columnDefs: [
			{field: 'serviceName', displayName: '服务名称', width: 120, pinnable: false, sortable: false},
			{
				field: 'cardType', displayName: '银行卡种类', width: 150, pinnable: false, sortable: false,
				cellFilter: "formatDropping:" + angular.toJson($scope.cardType)
			},
			{
				field: 'holidaysMark', displayName: '节假日标志', width: 150, pinnable: false, sortable: false,
				cellFilter: "formatDropping:" + $scope.holidaysStr
			},
			{
				field: 'rateType', displayName: '费率方式', width: 220, pinnable: false, sortable: false,
				cellFilter: "formatDropping:" + angular.toJson($scope.rateTypes)
			},
			{
				field: 'oneRate', displayName: '一级代理商管控费率', width: 220, pinnable: false, sortable: false
			},
			{
				field: 'merRate', displayName: '商户费率', width: 220, pinnable: false, sortable: false,
				cellTemplate: '<div ng-switch on="$eval(\'row.entity.fixedMark\')">'
				+ '<div ng-switch-when=0>'
				+ '<input style="width:98%;height:98%;" ng-blur="grid.appScope.judgeRate(row.entity.serviceId,row.entity.cardType,row.entity.holidaysMark)" class="ui-widget input" ng-readonly="false" ng-model="row.entity[col.field]"/>'
				+ '</div>'
				+ '<div ng-switch-when=1>'
				+ '<p ng-bind="row.entity[col.field]"/>'
				+ '</div>'
				+ '</div>'
			},
			{
				field: 'fixedMark', displayName: '固定标志', width: 160, pinnable: false, sortable: false,
				cellFilter: "formatDropping:" + angular.toJson($scope.fixedMark)
			}
		]
	};

	$scope.merchantQuotaList = {
		data: 'quotas',
		columnDefs: [
			{field: 'serviceName', displayName: '服务名称', width: 100, pinnable: false, sortable: false},
			{
				field: 'cardType', displayName: '银行卡种类', width: 150, pinnable: false, sortable: false,
				cellFilter: "formatDropping:" + angular.toJson($scope.cardType)
			},
			{
				field: 'holidaysMark', displayName: '节假日标志', width: 150, pinnable: false, sortable: false,
				cellFilter: "formatDropping:" + $scope.holidaysStr
			},
			{
				field: 'singleDayAmount', displayName: '单日最大交易金额(元）', width: 200, pinnable: false, sortable: false,
				cellTemplate: ddser
			},
			{
				field: 'singleMinAmount', displayName: '单笔最小交易额（元）', width: 200, pinnable: false, sortable: false,
				cellTemplate: ddser
			},
			{
				field: 'singleCountAmount', displayName: '单笔最大交易额（元）', width: 200, pinnable: false, sortable: false,
				cellTemplate: ddser
			},
			{
				field: 'singleDaycardAmount', displayName: '单日单卡最大交易额（元）', width: 200, pinnable: false, sortable: false,
				cellTemplate: ddser
			},
			{
				field: 'singleDaycardCount', displayName: '单日单卡最大交易笔数（笔）', width: 250, pinnable: false, sortable: false,
				cellTemplate: ddser
			},
			{
				field: 'fixedMark', displayName: '固定标志', width: 160, pinnable: false, sortable: false,
				cellFilter: "formatDropping:" + angular.toJson($scope.fixedMark)
			}
		]
	};

	$scope.serviceStatusMgr = {
		data: 'serviceStatus',
		columnDefs: [
			{field: 'serviceName', displayName: '服务名称', width: 220, pinnable: false, sortable: false},
			{
				field: 'status', displayName: '服务状态', width: 300, pinnable: false, sortable: false,
				cellTemplate: '<switch class="switch" ng-model="row.entity.status"  ng-change="grid.appScope.open(row.entity.id,row.entity.status)" />'
			},
			{
				field: 'tradeType', displayName: '交易模式', width: 220, pinnable: false, sortable: false,
				cellTemplate: '<select class="form-control" ng-change="grid.appScope.cleanChannelCode(row.entity.id,row.entity.tradeType)" style="height: 90%;width:90%;margin-top: 5px;" ng-model="row.entity.tradeType" ng-options="x.value as x.text for x in grid.appScope.tradeType" />'

			},
			{
				field: 'channelCode', displayName: '直清通道', width: 220, pinnable: false, sortable: false,
				cellTemplate: '<select ng-show="row.entity.tradeType==1" class="form-control" style="height: 90%;width:90%;margin-top: 5px;" ng-model="row.entity.channelCode" ' +
				'ng-options="x.value as x.text for x in grid.appScope.zqAcqOrgs"/>'
			}/*,
			 {
			 field: 'options',
			 width: 220,
			 displayName: '操作',
			 pinnedRight: true,
			 cellTemplate: '<a ng-click="grid.appScope.saveZqMerService(row.entity)">保存</a>'
			 }*/
		]
	};
	//保存商户交易模式和通道
	/*$scope.saveZqMerService = function(entity){
	 var id = entity.id;
	 var tradeType = entity.tradeType;
	 var channelCode = entity.channelCode;
	 if(tradeType == '1' && (channelCode == null || channelCode == '')){
	 $scope.notice("请选择直清通道");
	 return;
	 }
	 var data = {"id": id, "tradeType": tradeType, "channelCode": channelCode};
	 $http.post("merchantBusinessProduct/saveZqMerService.do", angular.toJson(data))
	 .success(function (result) {
	 if (result.result) {
	 $scope.notice("保存成功");
	 } else {
	 $scope.notice(result.msg);
	 }

	 });
	 };*/
	
	$scope.cleanChannelCode = function (id,val) {
		if (val == 0) {
			for (var i = 0; i < $scope.serviceStatus.length; i++) {
				if ($scope.serviceStatus[i].id == id) {
					$scope.serviceStatus[i].channelCode = null;
				}
			}
		}
	}
	
	//开通服务
	$scope.open = function (id, val) {
		if (val == true) {
			val = 0;
		} else {
			val = 1;
		}
		if (val == 1) {
			for (var i = 0; i < $scope.serviceStatus.length; i++) {
				if ($scope.serviceStatus[i].id == id) {
					$scope.serviceStatus[i].status = false;
					var data = {"mbp": $scope.mbp, "id": id, "status": 0};
					$http.post("merchantBusinessProduct/updateMerchantService.do", angular.toJson(data))
							.success(function (result) {
								if (result.result) {
									$scope.notice("关闭成功");
								} else {
									$scope.notice("关闭失败");
								}

							});
				}
			}
		} else {
			for (var i = 0; i < $scope.serviceStatus.length; i++) {
				if ($scope.serviceStatus[i].id == id) {
					$scope.serviceStatus[i].status = true;
					var data = {"mbp": $scope.mbp, "id": id, "status": 1};
					$http.post("merchantBusinessProduct/updateMerchantService.do", angular.toJson(data))
							.success(function (result) {
								if (result.result) {
									$scope.notice("开启成功");
								} else {
									$scope.notice("开启失败");
								}
							});
				}
			}

		}
	}

	//审核记录
	$scope.merchantRecords = {
		data: 'merExa',
		columnDefs: [
			{
				field: 'openStatus', displayName: '状态', width: 120, pinnable: false, sortable: false,
				cellFilter: "formatDropping:[{text:'审核失败',value:'2'},{text:'审核成功',value:'1'}]"
			},
			{field: 'examinationOpinions', displayName: '内容', width: 360, pinnable: false, sortable: false},
			{
				field: 'createTime', displayName: '时间', width: 200, pinnable: false, sortable: false,
				cellFilter: "date:'yyyy-MM-dd HH:mm:ss'"
			},
			{field: 'realName', displayName: '操作员', width: 200, pinnable: false, sortable: false}
		]
	};

	
	$scope.merchantCheckRecords = {
		data: 'listacr',
		columnDefs: [
			{field: 'ruleCode', displayName: '效验项', width: 120, pinnable: false, sortable: false},
			{field: 'checkInfo', displayName: '效验信息', width: 200, pinnable: false, sortable: false},
			{
				field: 'checkResult', displayName: '效验结果', width: 200, pinnable: false, sortable: false,
				cellFilter: "formatDropping:[{text:'通过',value:'1'},{text:'未通过',value:'0'}]"
			},
			{
				field: 'createTime',
				displayName: '时间',
				width: 200,
				pinnable: false,
				sortable: false,
				cellFilter: "date:'yyyy-MM-dd HH:mm:ss'"
			}
		]
	};

	//审核审批
	$scope.merchantDetailed = {
		data: 'listMri',
		columnDefs: [
			{field: 'itemName', displayName: '进件要求项名称'},
			{
				field: 'content', displayName: '资料',
				cellTemplate: '<div ng-switch on="$eval(\'row.entity.exampleType\')">'
				+ '<div ng-switch-when="2">'
				+ '<div>'
				+ '<a ui-sref="{{COL_FIELD}}">{{$eval(\'row.entity.itemName\')}} 附件下载</a>'
				+ '</div>'
				+ '</div>'
				+ '<div ng-switch-when="3">'
				+ '<div ng-switch on="$eval(\'row.entity.mriId\')">'
				+ '<div ng-switch-when="7">'
				+ '<input style="width:220px; height: 30px; float: left; margin-left: 10px;" disabled="disabled" class="ui-widget input" ng-readonly="false" ng-model="row.entity[col.field]"/> <a class="lh30" ng-click="grid.appScope.updateAddress(row.entity[col.field],$eval(\'row.entity.mriId\'))">修改</a>'
				+ '</div>'
				+ '<div ng-switch-when="15">'
				+ '<input style="width:220px; height: 30px; float: left; margin-left: 10px;" disabled="disabled" class="ui-widget input" ng-readonly="false" ng-model="row.entity[col.field]"/> <a class="lh30" ng-click="grid.appScope.updateAddress(row.entity[col.field],$eval(\'row.entity.mriId\'))">修改</a>'
				+ '</div>'
				+ '<div ng-switch-when="37">'
				+ '<input style="width:220px; height: 30px; float: left; margin-left: 10px;" disabled="disabled" class="ui-widget input" ng-readonly="false" ng-model="row.entity[col.field]"/> '
				+ '</div>'
				+ '<div ng-switch-default>'
				+ '<input style="width:220px; height: 30px; float: left; margin-left: 10px;" class="ui-widget input" ng-readonly="false" ng-model="row.entity[col.field]"/>'
				+ '</div>'
				+ '</div>'
				+ '</div>'
				+ '</div>'
			},
			{
				field: 'status', displayName: '状态',
				cellFilter: "formatDropping:[{text:'待审核',value:0},{text:'审核通过',value:1},{text:'审核失败',value:2}]"
			},
			{
				field: 'auditTime', displayName: '审核通过时间',
				cellFilter: 'date:"yyyy-MM-dd"'
			}
		]
	};

	var fls = 0;

	//提交所有信息
	$scope.commitInfo = function () {
		$scope.submitting = true;
		if (fls == 0) {
			if ($scope.mbp.status == "4") {
				for (var i = 0; i < $scope.listMri.length; i++) {
					if ($scope.listMri[i].status == "2" || $scope.listMri[i].status == "0") {
						$scope.notice("进件项有不通过的，不能选择审核成功");
						$scope.submitting = false;
						return;
					}
				}
				for (var i = 0; i < $scope.listMris.length; i++) {
					if ($scope.listMris[i].status == "2" || $scope.listMris[i].status == "0") {
						$scope.notice("进件项有不通过的，不能选择审核成功");
						$scope.submitting = false;
						return;
					}
				}
			}
			var regName =/^[\u4e00-\u9fa5·•]{1,30}$/;
			if(!regName.test($scope.info.lawyer)){
				$scope.notice('法人姓名填写有误');
				$scope.submitting = false;
				return false;
			}
			for(var index=0; index<$scope.serviceStatus.length; index++){
				if($scope.serviceStatus[index].tradeType == '1' && ($scope.serviceStatus[index].channelCode == null || $scope.serviceStatus[index].channelCode == '')){
					$scope.notice($scope.serviceStatus[index].serviceName + " 服务的直清通道不能为空");
					$scope.submitting = false;
					return;
				}
			}
			var data = {
				"mbp": $scope.mbp,
				"info": $scope.info,
				"listMri": $scope.listMri,
				"listMsr": $scope.rates,
				"listMsq": $scope.quotas,
				"serviceData": $scope.serviceStatus
			};
			
			$http.post("merchantBusinessProduct/updateMerchantInfo.do", angular.toJson(data))
					.success(function (result) {
						if (result.result) {
							$scope.notice(result.msg);
							$state.go('merchant.queryMer');
						} else {
							$scope.notice(result.msg);
							$scope.submitting = false;
						}
					});
		} else {
			fls = 0;
			$scope.submitting = false;
		}
	}




	//服务限额判断
	$scope.checkQuota = function (singleDayAmount, singleMinAmount, singleCountAmount, singleDaycardAmount, singleDaycardCount, serviceId, cardId, did) {
		for (var i = 0; i < quotasbz.length; i++) {
			if (quotasbz[i].serviceId == serviceId && quotasbz[i].cardType == cardId && quotasbz[i].holidaysMark == did) {
				if (singleDayAmount == "" || singleCountAmount == "" || singleDaycardAmount == "" || singleDaycardAmount == "" || singleMinAmount == "") {
					$scope.notice("请输入正确的金额或数字");
					fls = 1;
					return;
				}
				if (isNaN(singleDayAmount) || isNaN(singleCountAmount) || isNaN(singleDaycardAmount) || isNaN(singleDaycardCount) || isNaN(singleMinAmount)) {
					$scope.notice("请输入正确的金额或数字");
					fls = 1;
					return;
				}
				fls = 0;
				var str = "";
				if (parseFloat(quotasbz[i].singleDayAmount) < parseFloat(singleDayAmount)) {
					str = "单日最大交易金额必须小于等于当前金额=" + quotasbz[i].singleDayAmount;
					fls = 1;
				}
				if (parseFloat(quotasbz[i].singleCountAmount) < parseFloat(singleCountAmount)) {
					str = "单笔最大交易额必须小于等于当前金额=" + quotasbz[i].singleCountAmount;
					fls = 1;
				}
				if (parseFloat(quotasbz[i].singleMinAmount) < parseFloat(singleMinAmount)) {
					str = "单笔最小交易额必须小于等于当前金额=" + quotasbz[i].singleMinAmount;
					fls = 1;
				}
				if (parseFloat(quotasbz[i].singleDaycardAmount) < parseFloat(singleDaycardAmount)) {
					str = "单日单卡最大交易额必须小于等于当前金额=" + quotasbz[i].singleDaycardAmount;
					fls = 1;
				}
				if (parseFloat(quotasbz[i].singleDaycardCount) < parseFloat(singleDaycardCount)) {
					str = "单日单卡最大交易笔数必须小于等于当前笔数=" + quotasbz[i].singleDaycardCount;
					fls = 1;
				}
				if (str != "") {
					$scope.notice(str);
				}
			}
		}

	}

	//判断费率
	$scope.judgeRate = function (id, cc, hh) {
		for (var i = 0; i < $scope.rates.length; i++) {
			if ($scope.rates[i].serviceId == id && $scope.rates[i].cardType == cc && $scope.rates[i].holidaysMark == hh) {
				if ($scope.rates[i].oneRate == $scope.rates[i].merRate) {
					continue;
				} else {
					if ($scope.rates[i].rateType == 1) {
						if (!judgeOne($scope.rates[i].merRate, $scope.rates[i].oneRate))
							$scope.notice("费率格式有误或费率小于一级费率=", $scope.rates[i].merRate);
						else
							fls = 0;
					}
					if ($scope.rates[i].rateType == 2) {
						if (!judgeTwo($scope.rates[i].merRate, $scope.rates[i].oneRate))
							$scope.notice("费率格式有误或费率小于一级费率=", $scope.rates[i].merRate);
						else
							fls = 0;
					}
					if ($scope.rates[i].rateType == 3) {
						var str1 = new Array();
						str1 = $scope.rates[i].merRate.split("~");

						var str2 = new Array();
						str2 = $scope.rates[i].oneRate.split("~");
						if (str1.length != 3) {
							$scope.notice("费率格式有误");
						} else {
							if (judgeOne(str1[0], str2[0])) {
								if (judgeTwo(str1[1], str2[1])) {
									if (judgeOne(str1[2], str2[2])) {
										fls = 0;
									} else {
										$scope.notice("费率格式有误或费率小于一级费率=", $scope.rates[i].merRate);
									}
								} else {
									$scope.notice("费率格式有误或费率小于一级费率=", $scope.rates[i].merRate);
								}
							} else {
								$scope.notice("费率格式有误或费率小于一级费率=", $scope.rates[i].merRate);
							}
						}

					}
					if ($scope.rates[i].rateType == 4) {

						var str1 = new Array();
						str1 = $scope.rates[i].merRate.split("+");

						var str2 = new Array();
						str2 = $scope.rates[i].oneRate.split("+");

						if (str1.length != 2) {
							$scope.notice("费率格式有误");
						} else {
							if (judgeTwo(str1[0], str2[0])) {
								if (judgeOne(str1[1], str2[1])) {
									fls = 0;
								} else {
									$scope.notice("费率格式有误或费率小于一级费率=", $scope.rates[i].merRate);
								}
							} else {
								$scope.notice("费率格式有误或费率小于一级费率=", $scope.rates[i].merRate);
							}
						}

					}
					if ($scope.rates[i].rateType == 5) {
						var str1 = new Array();
						str1 = $scope.rates[i].merRate.split("<");

						var str2 = new Array();
						str2 = $scope.rates[i].oneRate.split("<");

						if (str1.length % 2 == 0) {
							$scope.notice("费率格式有误");
						} else {

							if (str1.length == 3) {
								if (judgeTwo(str1[0], str2[0])) {
									if (judgeOne(str1[1], str2[1])) {
										if (judgeTwo(str1[2], str2[2])) {
											fls = 0;
										} else {
											$scope.notice("费率格式有误或费率小于一级费率=", $scope.rates[i].merRate);
										}
									} else {
										$scope.notice("费率格式有误或费率小于一级费率=", $scope.rates[i].merRate);
									}
								} else {
									$scope.notice("费率格式有误或费率小于一级费率=", $scope.rates[i].merRate);
								}
							}

							if (str1.length == 5) {
								if (judgeTwo(str1[0], str2[0])) {
									if (judgeOne(str1[1], str2[1])) {
										if (judgeTwo(str1[2], str2[2])) {
											if (judgeOne(str1[3], str2[3])) {
												if (judgeTwo(str1[4], str2[4])) {
													fls = 0;
												} else {
													$scope.notice("费率格式有误或费率小于一级费率=", $scope.rates[i].merRate);
												}
											} else {
												$scope.notice("费率格式有误或费率小于一级费率=", $scope.rates[i].merRate);
											}
										} else {
											$scope.notice("费率格式有误或费率小于一级费率=", $scope.rates[i].merRate);
										}
									} else {
										$scope.notice("费率格式有误或费率小于一级费率=", $scope.rates[i].merRate);
									}
								} else {
									$scope.notice("费率格式有误或费率小于一级费率=", $scope.rates[i].merRate);
								}
							}

						}

					}
					if ($scope.rates[i].rateType == 6) {
						var str1 = new Array();
						str1 = $scope.rates[i].merRate.split("~");

						var str2 = new Array();
						str2 = $scope.rates[i].oneRate.split("~");
						if (str1.length != 2) {
							$scope.notice("费率格式有误");
						} else {
							if (judgeTwo(str1[0], str2[0])) {
								if (judgeOne(str1[1], str2[1])) {
									fls = 0;
								} else {
									$scope.notice("费率格式有误或费率小于一级费率=", $scope.rates[i].merRate);
								}
							} else {
								$scope.notice("费率格式有误或费率小于一级费率=", $scope.rates[i].merRate);
							}
						}
					}

				}
			}

		}
	}

	//针对每笔固定金额
	function judgeOne(merRate, oneRate) {
		var mer = new Array();
		mer = merRate.split(".");
		if (isNaN(merRate)) {
			fls = 1;
			return false;
		}
		if (mer.length != 2) {
			fls += 1;
			return false;
		} else if (mer[1].length > 2) {
			fls += 1;
			return false;
		} else if (parseFloat(merRate) > parseFloat(oneRate)) {
			fls += 1;
			return false;
		}
		fls += 0;
		return true;
	}

	//针对每笔扣率
	function judgeTwo(merRate, oneRate) {
		var mer = new Array();
		mer = merRate.split(".");
		if (isNaN(merRate.substring(0, merRate.lastIndexOf("%")))) {
			fls = 1;
			return false;
		}
		if (merRate.lastIndexOf("%") == -1) {
			fls += 1;
			return false;
		} else if (mer[1].length > 3) {
			fls += 1;
			return false;
		} else if (mer.length != 2) {
			fls += 1;
			return false;
		} else if (parseFloat(merRate.substring("%")) > parseFloat(oneRate.substring("%"))) {
			fls += 1;
			return false;
		}
		fls += 0;
		return true;
	}

	//上传图片,定义控制器路径
	var uploader = $scope.uploader = new FileUploader({
		url: 'upload/fileUpload.do',
		queueLimit: 1,   //文件个数
		removeAfterUpload: true  //上传后删除文件
	});
	//过滤长度，只能上传一个
	uploader.filters.push({
		name: 'imageFilter',
		fn: function (item, options) {
			return this.queue.length < 1;
		}
	});
	//过滤格式
	uploader.filters.push({
		name: 'imageFilter',
		fn: function (item /*{File|FileLikeObject}*/, options) {
			var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
			return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
		}
	});
	$scope.clearItems = function () {  //重新选择文件时，清空队列，达到覆盖文件的效果
		uploader.clearQueue();
	}
	//修改进件资料
	$scope.updateMriInfo = function (mriId, pp, id, merBpId, status, content, name) {
		var modalScope = $scope.$new();
		modalScope.id = id;
		modalScope.pp = pp;
		modalScope.type = status;
		modalScope.mriId = mriId;
		modalScope.name = name;
		modalScope.num = 0;
		modalScope.content = content;
		modalScope.fileSelect = null;
		modalScope.uploader = uploader;
		var modalInstance = $uibModal.open({
			templateUrl: 'views/merchant/merchantUpdateModal.html',  //指向上面创建的视图
			controller: 'merchantModalCtr',// 初始化模态范围
			scope: modalScope,
			size: 'lg'
		})
		modalScope.modalInstance = modalInstance;
		modalInstance.result.then(function (selectedItem) {
			if (selectedItem.uploader.queue.length > 0) {
				var name = mriId + "_" + angular.copy(selectedItem.uploader.queue[0].file.name);
				selectedItem.uploader.queue[0].file.name = name;
			} else {
				$scope.submitting = false;
				return;
			}
			uploader.uploadAll();//上传
			uploader.onSuccessItem = function (fileItem, response, status, headers) {//上传成功后的回调函数，在里面执行提交
				if (response.url != null) {
					var data = {"img": response.url, "id": id};
					$http.post('merchantBusinessProduct/updateItemImg.do', angular.toJson(data))
							.success(function (msg) {
								if (msg.result) {
									for (var i = 0; i < $scope.listMris.length; i++) {
										if ($scope.listMris[i].mriId == mriId) {
											$scope.listMris[i].content = msg.datas;
											$scope.listMris[i].status = 0;
										}
									}
									$scope.notice("修改成功");
								} else {
									$scope.notice("修改失败");
								}
							}).error(function () {
					});
				}
			};
		}, function () {
			$log.info('取消: ' + new Date())
		})

	}

	//修改经营地址
	$scope.updateAddress = function (address, mriId) {
		var ars = address.split("-");
		var modalScope = $scope.$new();
		modalScope.address = address;
		modalScope.mriId = mriId;
		modalScope.info.province=ars[0];
		$scope.refeshCities();
		modalScope.info.city=ars[1];
		$scope.refeshAreas();
		modalScope.info.district=ars[2];
		modalScope.info.addressa=ars[3];
		var modalInstance = $uibModal.open({
			templateUrl: 'views/merchant/merchantUpdateAddress.html',  //指向上面创建的视图
			controller: 'merchantAddressCtr',// 初始化模态范围
			scope: modalScope,
			size: 'lg'
		})
		modalScope.modalInstance = modalInstance;
		modalInstance.result.then(function (selectedItem) {

			if (mriId != 15) {
				if (selectedItem.info.province == null || selectedItem.info.city == null || selectedItem.info.district == null || selectedItem.info.addressa == "") {
					$scope.notice("开户行地址为空");
				} else {
					var str = selectedItem.info.province + "-" + selectedItem.info.city + "-" + selectedItem.info.district + "-" + selectedItem.info.addressa;
					for (var i = 0; i < $scope.listMri.length; i++) {
						if ($scope.listMri[i].mriId == selectedItem.mriId) {
							$scope.listMri[i].content = str;
							selectedItem.info.province = "";
							selectedItem.info.city = "";
							selectedItem.info.district = "";
							selectedItem.info.addressa = "";
						}
					}
				}
			} else {
				if (selectedItem.info.province == null || selectedItem.info.city == null || selectedItem.info.district == null) {
					$scope.notice("开户行地址为空");
				} else {
					var str = selectedItem.info.province + "-" + selectedItem.info.city + "-" + selectedItem.info.district;
					for (var i = 0; i < $scope.listMri.length; i++) {
						if ($scope.listMri[i].mriId == selectedItem.mriId) {
							$scope.listMri[i].content = str;
							selectedItem.info.province = "";
							selectedItem.info.city = "";
							selectedItem.info.district = "";
						}
					}
				}
			}
		}, function () {
			$log.info('取消: ' + new Date())
		})
	}
	
	$scope.refeshAreaList = function (name, type, callback) {
		if (name == null || name == "undefine") {
			return;
		}
		$http.post('areaInfo/getAreaByName', 'name=' + name + '&&type=' + type,
				{headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
		).success(function (data) {
			callback(data);
		}).error(function () {
		});
	}

	//市，页面上ng-change生时触发
	$scope.refeshCities = function () {
		$scope.areaGroup = [];
		$scope.refeshAreaList($scope.info.province, "", function (data) {
			$scope.cityGroup = data;
		});
	}
	//县，页面上ng-change生时触发
	$scope.refeshAreas = function () {
		$scope.refeshAreaList($scope.info.city, "", function (data) {
			$scope.areaGroup = data;
		});
	}
}]);

angular.module('inspinia').controller('merchantModalCtr', function ($scope, $stateParams, $http, FileUploader) {

	$scope.solutionModalClose = function () {
		$scope.modalInstance.dismiss();
	}

	$scope.solutionModalOk = function () {
		$scope.modalInstance.close($scope);
	}
});

angular.module('inspinia').controller('merchantAddressCtr', function ($scope, $stateParams, $http) {

	$scope.solutionModalClose = function () {
		$scope.modalInstance.dismiss();
	}

	$scope.solutionModalOk = function () {
		$scope.modalInstance.close($scope);
	}

	$scope.getAreaList = function (name, type, callback) {
		if (name == null || name == "undefine") {
			return;
		}
		$http.post('areaInfo/getAreaByName', 'name=' + name + '&&type=' + type,
				{headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
		).success(function (data) {
			callback(data);
		}).error(function () {
		});
	}

	//省，加载页面时自动触发
	$scope.getAreaList(0, "p", function (data) {
		$scope.provinceGroup = data;
	});
	//市，页面上ng-change生时触发
	$scope.getCities = function () {
		$scope.areaGroup = [];
		$scope.getAreaList($scope.info.province, "", function (data) {
			$scope.cityGroup = data;
		});
	}
	//县，页面上ng-change生时触发
	$scope.getAreas = function () {
		$scope.getAreaList($scope.info.city, "", function (data) {
			$scope.areaGroup = data;
		});
	}

});
