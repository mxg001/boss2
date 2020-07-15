/**
 * 领主领地收益表
 */
angular.module('inspinia').controller('manorMoneyDetlCtrl', function($scope, $http,$stateParams,i18nService, SweetAlert){
    //数据源
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    $scope.resetForm = function () {
        $scope.baseInfo = {
        		createDateStart:moment(new Date().getTime()-7*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
        		createDateEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
        		userId:"",userName:"",lordsOrgId:-1,phone:""};
        $scope.selected = "";
        $scope.selected2 = "";
        $scope.selected3 = "";
    }
    $scope.resetForm();

    //查询所有银行家组织
    $scope.orgs=[];
    $scope.getAllOrg = function(){
        $http({
            url:"superBank/getOrgInfoList",
            method:"POST"
        }).success(function(msg){
            if(msg.status){
                $scope.orgs = msg.data;
                $scope.orgs.unshift({orgId:-1,orgName:"全部"});
            }
        }).error(function(){
            $scope.notice("获取组织列表异常");
        });
    };
    $scope.getAllOrg();
    
    $scope.columnDefs = [
        {field: 'orgName',displayName: '领主所属组织',pinnable: false,sortable: false,width:150},
        {field: 'lordsUserCode',displayName: '领主ID',pinnable: false,sortable: false,width:150},
        {field: 'userName',displayName: '领主姓名',pinnable: false,sortable: false,width:150},
        {field: 'nickName',displayName: '领主昵称',pinnable: false,sortable: false,width:150},
        {field: 'phone',displayName: '领主手机号',pinnable: false,sortable: false,width:150},
        {field: 'transAmount',displayName: '转入金额',pinnable: false,sortable: false,width:150},
        {field: 'territoryId',displayName: '领地ID',pinnable: false,sortable: false,width:150},
        {field: 'provinceName',displayName: '省',pinnable: false,sortable: false,width:150},
        {field: 'cityName',displayName: '市',pinnable: false,sortable: false,width:150},
        {field: 'regionName',displayName: '区',pinnable: false,sortable: false,width:150},
        {field: 'createDate',displayName: '转入时间',pinnable: false,sortable: false,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:150}
    ];

    $scope.grid = {
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
//		rowHeight:35,
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
    	if ($scope.selected!= undefined && $scope.selected!=null) {
    		$scope.baseInfo.provinceName = $scope.selected.name;
    		if ($scope.selected2!= undefined && $scope.selected2!=null) {
        		$scope.baseInfo.cityName = $scope.selected2.name;
        		if ($scope.selected3!= undefined && $scope.selected3!=null) {
            		$scope.baseInfo.regionName = $scope.selected3.name;
        		}else{
        			$scope.baseInfo.regionName = "";
        		}
    		}else{
    			$scope.baseInfo.cityName = "";
    			$scope.baseInfo.regionName = "";
    		}
		}else{
			$scope.baseInfo.provinceName = "";
			$scope.baseInfo.cityName = "";
			$scope.baseInfo.regionName = "";
		}
    	
    	
        $scope.submitting = true;
        $scope.loadImg = true;
        $http.post("manor/manorMoneyDetl","baseInfo=" + angular.toJson($scope.baseInfo)+"&pageNo="+
            $scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function (result) {
            $scope.submitting = false;
            $scope.loadImg = false;
            if (!result.status){
                $scope.notice(result.msg);
                return;
            }
            $scope.grid.data = result.data.page.result;
            $scope.grid.totalItems = result.data.page.totalCount;
            $scope.totalInfo = result.data.totalInfo;
        }).error(function () {
            $scope.submitting = false;
            $scope.loadImg = false;
            $scope.notice('服务器异常,请稍后再试.');
        });
    };
    
    $scope.baseInfo.lordsUserCode = $stateParams.id;
    $scope.query();
    
    //省市区
    $scope.list = LAreaDataBaidu;
      $scope.c = function () {
          $scope.selected2 = "";
          $scope.selected3 = "";
      };
      
      $scope.c2 = function () { 
          $scope.selected3 = "";
    };
});
