angular
		.module('inspinia')
		.controller(
				'TradeSumInfoQueryCtrl',
				function($scope, $rootScope, $http, $state, $stateParams,
						$compile, $filter, i18nService, SweetAlert) {
					i18nService.setCurrentLang('zh-cn'); // 设置语言为中文
					// 数据源
					$scope.info = {teamEntryId:'',
						startTime : moment(
								new Date().getTime() - 24 * 60 * 60 * 1000)
								.format('YYYY-MM-DD'),
						endTime : moment(new Date().getTime()).format(
								'YYYY-MM-DD'),
						agentNo : '',
						showLower : 1,
						agentOem : "",
						incomeStatus : -1
					}
					$scope.agentList = [];
					$scope.agentOemList = [ {value : "", text : "全部"}, {value : "100070", text : "盛POS"}, {value : "200010", text : "盛钱包"} ];
					$scope.incomeStatusList = [ {
						value : -1,
						text : "全部"
					}, {
						value : 0,
						text : "未入账"
					}, {
						value : 1,
						text : "已入账"
					} ];
					$scope.showLowerSelect = [ {
						value : 1,
						text : "是"
					}, {
						value : 2,
						text : "否"
					} ];

					$scope.recordedTime = moment(new Date().getTime()).format(
							'YYYY-MM');

					
					$scope.teamEntryIdList = [{text:'全部',value:''}];
					 $scope.getTeamEntryIdList= function(){
					    	var merTeamId = $scope.info.agentOem;
					    	$http.get('TradeSumInfo/getTeamEntryIdList?merTeamId='+merTeamId).success(function (result) {
								if(result.status && result.data.length>0){
									var d = result.data;
									for (var i = 0; i < d.length; i++) {
										var team = d[i];
										$scope.teamEntryIdList.push({text:team.teamEntryName,value:team.teamEntryId});
									}
								}else {
									$scope.teamEntryIdList = [{text:'全部',value:''}];
								}
								$scope.info.teamEntryId='';
							});
					    }
					
					// 代理商
					$http.post("TradeSumInfo/selectConfigInfo").success(
							function(msg) {
								$scope.agentList.push({
									value : "",
									text : "全部"
								});
								// 响应成功
								for (var i = 0; i < msg.length; i++) {
									$scope.agentList.push({
										value : msg[i].agent_no,
										text : msg[i].agent_name + '('
												+ msg[i].agent_no + ')'
									});
								}
							});
					$scope.isdisabled = false;
					$scope.recordedOk = function() {
						$scope.isdisabled = true;
						// 发送ajax请求
						$http.post(
								"TradeSumInfo/recorded?recordedTime="
										+ $scope.recordedTime).success(
								function(result) {
									if (result.status) {
										$('#recordedModal').modal('hide');
										$scope.notice("入账成功");
										$scope.selectInfo();
									} else {
										$scope.notice("入账失败");
									}
									$scope.isdisabled = false;
								});
					}

					// 显示modal
					$scope.recordedModal = function() {
						$('#recordedModal').modal('show');
					};

					// 隐藏modal
					$scope.cancel = function() {
						$('#recordedModal').modal('hide');
					};

					// 查询
					$scope.selectInfo = function() {
						$http(
								{
									url : 'TradeSumInfo/query?pageNo='
											+ $scope.paginationOptions.pageNo
											+ "&pageSize="
											+ $scope.paginationOptions.pageSize,
									method : 'POST',
									data : $scope.info
								}).success(function(data) {
							$scope.gridOptions.data = data.result;
							$scope.gridOptions.totalItems = data.totalCount;
							$scope.tradeSum();

						})
					}
					$scope.selectInfo();

					// 导出
					$scope.exportInfo = function() {
						SweetAlert.swal({
							title : "确认导出？",
							showCancelButton : true,
							confirmButtonColor : "#DD6B55",
							confirmButtonText : "提交",
							cancelButtonText : "取消",
							closeOnConfirm : true,
							closeOnCancel : true
						}, function(isConfirm) {
							if (isConfirm) {
								//location.href = "TradeSumInfo/export?info="+ angular.toJson($scope.info)
								$scope.exportInfoClick("TradeSumInfo/export",{"info":angular.toJson($scope.info)});
							}
						})
					}

					$scope.tradeSum = function() {

						$http({
							url : 'TradeSumInfo/tradeSum',
							method : 'POST',
							data : $scope.info
						})
								.success(
										function(data) {

											$scope.gridOptions.tradeSum = data.tradeSum;
											$scope.gridOptions.merSum = data.merSum;
											$scope.gridOptions.activeSum = data.activeSum;
											$scope.gridOptions.threeIncomeSum = data.threeIncomeSum;
											$scope.gridOptions.recordedSum = data.recordedSum;

										})
					}
					// 清空
					$scope.clear = function() {
						$scope.teamEntryIdList = [{text:'全部',value:''}];
						$scope.info = {teamEntryId:'',
							startTime : moment(
									new Date().getTime() - 24 * 60 * 60 * 1000)
									.format('YYYY-MM-DD'),
							endTime : moment(new Date().getTime()).format(
									'YYYY-MM-DD'),
							agentNo : '',
							showLower : 1,
							agentOem : "",
							incomeStatus : -1
						}
					}


					$scope.hasSubTeam = function(teamId){
						if(teamId == '100070' || teamId == ""){
							$scope.subTeams = [{value : "100070-001", text : "盛POS21"}, {value : "100070-002", text : "超级盛POS"}];
						}else {
							$scope.subTeams = [];
						}
					}


					$scope.columnDefs = [
							{
								field : 'createTime',
								displayName : '统计日期',
								width : 100,
								cellFilter : 'date:"yyyy-MM-dd"'
							},
							{
								field : 'branch',
								displayName : '机构',
								width : 150
							},
							{
								field : 'oneLevel',
								displayName : '三方1级',
								width : 150
							},
							{
								field : 'twoLevel',
								displayName : '三方2级',
								width : 150
							},
							{
								field : 'threeLevel',
								displayName : '三方3级',
								width : 150
							},
							{
								field : 'fourLevel',
								displayName : '三方4级',
								width : 150
							},
							{
								field : 'fiveLevel',
								displayName : '三方5级',
								width : 150
							},
							{
								field : 'tradeSum',
								displayName : '交易量（元）',
								width : 120
							},
							{
								field : 'merSum',
								displayName : '商户总数',
								width : 120
							},
							{
								field : 'activateSum',
								displayName : '激活总数',
								width : 120
							},
							{
								field : 'machinesStock',
								displayName : '机具库存',
								width : 120
							},
							{
								field : 'unusedMachines',
								displayName : '未使用机具',
								width : 130
							},
							{
								field : 'expiredNotActivated',
								displayName : '到期未激活机具',
								width : 120
							},
							{
								field : 'threeIncome',
								displayName : '三方收益',
								width : 120
							},
							{
								field : 'recordedStatus',
								displayName : '入账状态',
								width : 120,
								cellFilter : "formatDropping:"
										+ angular
												.toJson($scope.incomeStatusList)
							}, {
								field : 'recordedDate',
								displayName : '入账日期',
								width : 180,
								cellFilter : 'date:"yyyy-MM-dd HH:mm:ss"'
							} ];

					$scope.gridOptions = { // 配置表格
						paginationPageSize : 10, // 分页数量
						paginationPageSizes : [ 10, 20, 50, 100 ], // 切换每页记录数
						useExternalPagination : true,
						columnDefs : $scope.columnDefs,
						onRegisterApi : function(gridApi) {
							$scope.gridApi = gridApi;
							gridApi.pagination.on
									.paginationChanged(
											$scope,
											function(newPage, pageSize) {
												$scope.paginationOptions.pageNo = newPage;
												$scope.paginationOptions.pageSize = pageSize;
												$scope.selectInfo();
											});
						}
					};
				}).filter('setxingxing', function() {
			return function(value) {
				if (value) {
					var v = value.substring(7, 11);
					return "*********" + v;
				}
			}
		});