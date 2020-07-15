/**
 * 查询业务产品
 */
angular.module('inspinia',['uiSwitch']).controller('queryHardCtrl',function($scope,$http,$state,$stateParams,i18nService,$document,SweetAlert){
	//数据源
	i18nService.setCurrentLang('zh-cn');
	$scope.team = [{text:'全部',value:null}];
    $scope.devicePns = [{text:'全部',value:""}];
    Array.prototype.push.apply($scope.devicePns,$scope.devicePNTypeLists);
	$scope.baseInfo = {};
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
    $scope.secretTypeList = [{text:'全部',value:null},{text:'无',value:0},{text:'双密钥',value:1}];
    //reset
    $scope.resetForm=function(){
        $scope.baseInfo = {typeName:null,orgId:null,secretType:null,devicePn:""};
    }
    $scope.resetForm();

	$http.get('teamInfo/queryTeamName.do').success(function(msg){
		for(var i=0;i<msg.teamInfo.length;i++){
			$scope.team.push({text:msg.teamInfo[i].teamName,value:msg.teamInfo[i].teamId});
		}
	});
	//是否校验时间
	isVerifyTime = 0;//校验：1，不校验：0
	setBeginTime=function(setTime){
		$scope.baseInfo.createTimeBegin = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
	}

	setEndTime=function(setTime){
		$scope.baseInfo.createTimeEnd = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
	}
	//查询
	$scope.query=function(){
		$http.post('hardwareProduct/selectHard.do',"baseInfo="+angular.toJson($scope.baseInfo)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+
				$scope.paginationOptions.pageSize,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
				.success(function(data){
					if(!data)
						return;
					$scope.productGrid.data =data.result; 
					$scope.productGrid.totalItems = data.totalCount;
		})
	}
	$scope.query();
	$scope.productGrid = {
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs: [
            {field: 'hpId',displayName: '硬件产品ID',pinnable: false,sortable: false},
            {field: 'typeName',displayName: '硬件产品名称',pinnable: false,sortable: false},
            {field: 'model',displayName: '机具型号',pinnable: false,sortable: false},
            {field: 'devicePn',displayName: '机具PN型号',pinnable: false,sortable: false,cellFilter:"formatDropping:" + angular.toJson($scope.devicePNTypeLists)},
            {field: 'facturerCode',displayName: '生产厂商英文标志',pinnable: false,sortable: false},
            {field: 'manufacturer',displayName: '生产厂商',pinnable: false,sortable: false},
            {field: 'versionNu',displayName: '版本号',pinnable: false,sortable: false},
            {field: 'orgName',displayName: '所属组织',pinnable: false,sortable: false},
            {field: 'secretType',displayName: '密钥类型',pinnable: false,sortable: false,cellFilter:"formatDropping:" + angular.toJson($scope.secretTypeList)},
						{field: 'createTime',displayName: '创建时间',pinnable: false,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
						{field: 'createPerson',displayName: '创建人',pinnable: false,sortable: false},
            // {field: 'secretType',displayName: '密钥类型',pinnable: false,sortable: false,cellFilter:"formatDropping:"+$scope.boolStr},
            {field: 'action',displayName: '操作',pinnable: false,sortable: false,editable:true,cellTemplate:
            	'<div class="lh30"><a ng-show="grid.appScope.hasPermit(\'service.updateHard\') "target="_blank" ui-sref="service.updateHard({id:row.entity.hpId})">修改</a></div>'}
        ],
        onRegisterApi: function(gridApi) {                
            $scope.gridApi = gridApi;
            $scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
	          	$scope.paginationOptions.pageNo = newPage;
	          	$scope.paginationOptions.pageSize = pageSize;
	            $scope.query();
	     });
        }
	 };
	$scope.query();
	
	
	//页面绑定回车事件
	$document.bind("keypress", function(event) {
		$scope.$apply(function (){
			if(event.keyCode == 13){
				$scope.query();
			}
		})
	});


});