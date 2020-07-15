angular.module('inspinia').controller('customServiceProblemCtrl', function ($scope, $rootScope, $http, $state, $stateParams, $compile, $filter, i18nService, SweetAlert) {


	i18nService.setCurrentLang('zh-cn'); // 设置语言为中文
	$scope.paginationOptions=angular.copy($scope.paginationOptions);

	$scope.qo = {
		customServiceProblemType: 0,
		appScope: "",
		problemName: ""
	}
	$scope.customServiceProblemTypeList = [];
	$scope.customServiceProblemTypeListShow = [];
	$scope.appScopeList = [];
	$scope.appScopeListCheckbox = [];

	// 问题类型
	$http
		.post(
			"sysDict/getListByKey.do?sysKey=CUSTOM_SERVICE_PROBLEM_TYPE")
		.success(
			function (msg) {
				$scope.customServiceProblemTypeList
					.push({
						sysName: "全部",
						sysValue: 0
					});
				// 响应成功
				for (var i = 0; i < msg.length; i++) {
					$scope.customServiceProblemTypeList
						.push({
							sysValue: msg[i].sysValue,
							sysName: msg[i].sysName
						});
					$scope.customServiceProblemTypeListShow
						.push({
							value: msg[i].sysValue,
							text: msg[i].sysName
						});
				}
			});

	// 应用范围
	$http.post("sysDict/getListByKey.do?sysKey=AGENT_OEM")
		.success(function (msg) {
			$scope.appScopeList.push({
				sysName: "全部",
				sysValue: ""
			});
			// 响应成功
			for (var i = 0; i < msg.length; i++) {
				$scope.appScopeList.push({
					sysValue: msg[i].sysValue,
					sysName: msg[i].sysName
				});
				$scope.appScopeListCheckbox.push({
					sysValue: msg[i].sysValue,
					sysName: msg[i].sysName,
					checkStatus: 0
				});
			}
		});
	$scope.orgAll = 0;
	// 全选组织
	$scope.changeOrgAll = function () {
		angular.forEach($scope.appScopeListCheckbox, function (
			item) {
			item.checkStatus = $scope.orgAll;
		});
	}
	$scope.changeOrg = function (checkStatus) {
		if (checkStatus == 0) {
			$scope.orgAll = checkStatus;
		}
		var count = 0;
		angular.forEach($scope.appScopeListCheckbox, function (
			item) {
			if (item.checkStatus == 1) {
				count++;
			}
		});
		if (count == $scope.appScopeListCheckbox.length) {
			$scope.orgAll = 1;
		}
	}
	// 查询
	$scope.query = function () {
		$http(
			{
				url: 'customService/problemQuery?pageNo='
					+ $scope.paginationOptions.pageNo
					+ "&pageSize="
					+ $scope.paginationOptions.pageSize,
				method: 'POST',
				data: $scope.qo
			})
			.success(
				function (data) {
					$scope.customServiceProblemGrid.data = data.result;
					$scope.customServiceProblemGrid.totalItems = data.totalCount;
				})
	}
	$scope.query();

	// 导出
	$scope.exportInfo = function () {
		SweetAlert
			.swal(
				{
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
						$scope.exportInfoClick("customService/problemExport", {"qoStr": angular.toJson($scope.qo)});
					}
				})
	}

	// 删除
	$scope.problemRemove = function (id) {
		SweetAlert.swal({
			title: "确认删除？",
			showCancelButton: true,
			confirmButtonColor: "#DD6B55",
			confirmButtonText: "确认",
			cancelButtonText: "取消",
			closeOnConfirm: true,
			closeOnCancel: true
		}, function (isConfirm) {
			if (isConfirm) {
				$http.post(
					"customService/problemRemove?id=" + id)
					.success(function (msg) {
						if (msg.status) {
							$scope.query();
						} else {
							$scope.notice(msg.msg);
						}
					});
			}
		})
	}

	$scope.problemSave = function () {
		// 进行参数校验
		var problemName = $scope.problem.problemName;
		if (problemName == null || problemName == "") {
			$scope.notice("问题名称不能为空");
			return;
		}
		var problemContent = $scope.problem.problemContent;
		if (problemContent == null || problemContent == "") {
			$scope.notice("问题内容不能为空");
			return;
		}
		// 判断适用范围是否被有选中
		var count = 0;
		angular.forEach($scope.appScopeListCheckbox, function (
			item) {
			if (item.checkStatus == 1) {
				count++;
			}
		});
		if (count == 0) {
			$scope.notice("请选择适用范围");
			return;
		}
		$scope.problem.appScopeList = $scope.appScopeListCheckbox;

		$http({
			url: 'customService/problemAdd',
			method: 'POST',
			data: $scope.problem
		}).success(function (msg) {
			if (msg.status) {
				$scope.notice("保存成功");
				$('#problemAddModel').modal('hide');
				$scope.query();
			} else {
				$scope.notice(msg.msg);
			}
		});
	}

	// 显示modal
	$scope.add = function () {
		$scope.orgAll = 0;
		angular.forEach($scope.appScopeListCheckbox, function (
			item) {
			item.checkStatus = 0;
		});
		$scope.problem = {
			problemType: "1"
		};
		$('#problemAddModel').modal('show');
	};
	$scope.problemDetail = function (entity) {
		$scope.problem = entity;
		$('#problemDetailModel').modal('show');
	}

	$scope.cancelDetail = function () {
		$('#problemDetailModel').modal('hide');
	}
	$scope.edit = function (entity) {
		$scope.orgAll = 0;
		angular.forEach($scope.appScopeListCheckbox, function (
			item) {
			item.checkStatus = 0;
		});
		$scope.problem = entity;
		var appScope = entity.appScope;
		angular.forEach($scope.appScopeListCheckbox, function (
			item) {
			if (appScope.indexOf(item.sysValue) != -1) {
				item.checkStatus = 1;
			}
		});
		var count = 0;
		angular.forEach($scope.appScopeListCheckbox, function (
			item) {
			if (item.checkStatus == 1) {
				count++;
			}
		});
		if (count == $scope.appScopeListCheckbox.length) {
			$scope.orgAll = 1;
		}
		$('#problemAddModel').modal('show');
	};

	// 隐藏modal
	$scope.cancelAdd = function () {
		$('#problemAddModel').modal('hide');
	};

	// 清空
	$scope.clear = function () {
		$scope.qo = {
			customServiceProblemType: 0,
			appScope: "",
			problemName: ""
		}
	}

	$scope.columnDefs = [
		{
			field: 'problemId',
			displayName: '序列',
			width: 80
		},
		{
			field: 'problemTypeName',
			displayName: '类型',
			width: 120
		},
		{
			field: 'problemName',
			displayName: '问题名称',
			width: 150
		},
		{
			field: 'appScopeName',
			displayName: '适用范围',
			width: 150
		},
		{
			field: 'clicks',
			displayName: '点击率',
			width: 100
		},
		{
			field: 'solveNum',
			displayName: '已解决',
			width: 100
		},
		{
			field: 'noSolveNum',
			displayName: '未解决',
			width: 100
		},
		{
			field: 'id',
			displayName: '操作',
			width: 200,
			pinnedRight: true,
			cellTemplate: '<div class="lh30" >'
				+ '<a  ng-click="grid.appScope.problemDetail(row.entity)">  详情</a> '
				+ '<a ng-show="grid.appScope.hasPermit(\'customServiceProblemEdit\')"  ng-click="grid.appScope.edit(row.entity)"> | 修改</a> '
				+ '<a ng-show="grid.appScope.hasPermit(\'customServiceProblemRemove\')"  ng-click="grid.appScope.problemRemove(row.entity.problemId)"> | 删除</a> '
				+ '</div>'
		}];

	$scope.customServiceProblemGrid = { // 配置表格
		paginationPageSize: 10, // 分页数量
		paginationPageSizes: [10, 20, 50, 100], // 切换每页记录数
		useExternalPagination: true,
		columnDefs: $scope.columnDefs,
		onRegisterApi: function (gridApi) {
			$scope.gridApi = gridApi;
			gridApi.pagination.on
				.paginationChanged(
					$scope,
					function (newPage, pageSize) {
						$scope.paginationOptions.pageNo = newPage;
						$scope.paginationOptions.pageSize = pageSize;
						$scope.query();
					});
		}
	};
	/**
	 *富文本框按钮控制
	 */
	$scope.summeroptions = {
		toolbar: [
			['style', ['bold', 'italic', 'underline', 'clear']],
			['fontface', ['fontname']],
			['textsize', ['fontsize']],
			['fontclr', ['color']],
			['alignment', ['ul', 'ol', 'paragraph', 'lineheight']],
			['height', ['height']],
			['insert', ['hr']],
			// ['insert', ['link','picture','video','hr']],
			['view', ['codeview']]
		]
	};
}).filter('trustHtml', function ($sce) {
		return function (input) {
			return $sce.trustAsHtml(input);
		}
});