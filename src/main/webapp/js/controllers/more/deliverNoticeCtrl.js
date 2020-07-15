/**
 * 公告详情
 */
angular.module('inspinia').controller("deliverNoticeCtrl", function($scope, $http, $state, $stateParams) {
	//数据源
	$scope.sysType=[{text:"商户",value:'1'},{text:"代理商",value:'2'}];
//	$scope.receiveType=[{text:"直营所有级别代理商",value:'1'},{text:"所有一级代理商",value:'2'},{text:"直营一级代理商",value:'3'},{text:"非直营一级代理商",value:'4'}];
	$scope.agentBusiness = [{text:"全部",value:'0'},{text:"指定一级代理商下（包括下级发展）的商户",value:'1'}];
	$scope.baseInfo = {agentBusiness:'0'};
	$scope.isAll = [{text:'所有代理商',value:'1'},{text:'所有一级代理商',value:'2'}];
    $scope.oemTypes=[];//缓存所有指定组织
    $scope.oemListResult=false;
    $scope.oemListes=[];
	$scope.$state = $state;
	//业务产品表格
	$scope.productsGrid = { // 配置表格
		data : 'customerData',
		enableHorizontalScrollbar : 0, // 去掉滚动条
		enableVerticalScrollbar : 1, // 去掉滚动条
		columnDefs : [ // 表格数据
		               {field : 'teamName',displayName : '所属组织'},
		               {field : 'bpName',displayName : '业务产品名称'},
		               {field : 'remark',displayName : '业务产品说明'}],
		onRegisterApi : function(gridApi) {
			$scope.gridApiProduct = gridApi;
		}
	};
	
	var id = $stateParams.id;
	
	$http.get('notice/selectById/'+id).success(function(msg){
		$scope.noticeInfo = msg.notice;
		/*$scope.customerData = msg.allProduct;*/
		$scope.customerData = msg.products;
        $scope.oemTypes=msg.oemTypes;
        $scope.oemListes=msg.oemListes;
		if(msg.notice.receiveType == '1'){
			$scope.baseInfo.team = '0';
			$scope.baseInfo.isAll = '1';
		}
		if(msg.notice.receiveType == '2'){
			$scope.baseInfo.team = '0';
			$scope.baseInfo.isAll = '2';
		}
		if(msg.notice.agentNo != '0'){
			$scope.baseInfo.agentBusiness = '1';
		}
        if(msg.notice.oemType.indexOf("11")!=-1){
            $scope.oemListResult=true;
        }
	}).error(function(){
	});
	
    $scope.deliverNotice = function(){
    	$scope.submitting = true;
    	$http.get('notice/deliverNotice/' + id).success(function(msg){
    		$scope.submitting = false;
    		if(msg.status){
    			$scope.notice(msg.msg);
    			$state.transitionTo('sys.queryNotice',null,{reload:true});
    		}else{
    			$scope.notice(msg.msg);
    		}
    		
    	}).error(function(){
    		$scope.notice("下发失败！");
    		$scope.submitting = false;
    	});
    }
}).filter('trustHtml', function ($sce) {
    return function (input) {
        return $sce.trustAsHtml(input);
    }
});
