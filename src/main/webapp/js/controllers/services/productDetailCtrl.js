/**
 * 业务产品详情
 */
angular.module('inspinia',[]).controller('productDetailCtrl',function($scope,$http,$state,$stateParams){
	//数据源
	$scope.bool = [{text:'全部',value:null},{text:'否',value:'0'},{text:'是',value:'1'}];
	$scope.type = [{text:'个人',value:'1'},{text:'个体商户',value:'2'},{text:'企业商户',value:'3'}];
	$scope.isLimitHard = [ {text : '不限',value : '0'}, {text : '指定硬件产品',value : '1'} ];
	$scope.tFlags=[{text:"不涉及",value:0},{text:"只允许T0",value:1},{text:"只允许T1",value:2},{text:"允许T0和T1",value:3}];
	$scope.team = [{text:'全部',value:null}];
	$http.get('businessProductDefine/productDetailCtrl/'+$stateParams.id).success(function(msg){
		$scope.baseInfo = msg.product;
		$scope.services = msg.services;
		$scope.items = msg.items;
		$scope.hards = msg.hards;
		if(msg.product.twoCode == null){
			$scope.twoCodeHide = true;
		}
		if(msg.product.bpImg == null){
			$scope.bpImgHide = true;
		}
	});
	$scope.servicesGrid = {
			data : 'services',
			useExternalPagination : true, // 开启拓展名
			enableHorizontalScrollbar : 1, // 去掉水平滚动条
			enableVerticalScrollbar : 1, // grid垂直滚动条是否显示, 0-不显示
			columnDefs : [ {
				field : 'serviceName',
				displayName : '服务名称',
				width:260,
			},
			/*{
				field : 'serviceType',
				displayName : '服务类型',
				width:260,
				cellFilter: "formatDropping:"+angular.toJson($scope.serviceTypes)
			},*/
			{
				field : 'tFlag',
				displayName : 'T0T1标志',
				width:260,
				cellFilter: "formatDropping:"+angular.toJson($scope.tFlags)
			},
			{
				field : 'jRate',
				displayName : '借记卡商户费率',
				width:260,
				pinnable: false,
			},
			{
				field : 'dRate',
				displayName : '贷记卡商户费率',
				width:260,
				pinnable: false
			}],
			onRegisterApi : function(gridApi) {
				$scope.gridApiService = gridApi;
			}
		};
	
	$scope.itemsGrid = { // 配置表格
			data : 'items',
			enableHorizontalScrollbar : 0, // 去掉滚动条
			enableVerticalScrollbar : 1, // 去掉滚动条
			columnDefs : [ // 表格数据
			{
				field : 'itemName',
				width:470,
				displayName : '进件要求项名称'
			}],
			onRegisterApi : function(gridApi) {
				$scope.gridApiItem = gridApi;
			}	
		};
		
		//硬件产品表格
		$scope.hardsGrid = { // 配置表格
			data : 'hards',
			enableHorizontalScrollbar : 0, // 去掉滚动条
			enableVerticalScrollbar : 1, // 去掉滚动条
			columnDefs : [ // 表格数据
			{
				field : 'typeName',
				width:260,
				displayName : '种类名称'
			},{
				field : 'versionNu',
				width:260,
				displayName : '版本号'
			}],
			onRegisterApi : function(gridApi) {
				$scope.gridApiHard = gridApi;
			}
		};
});