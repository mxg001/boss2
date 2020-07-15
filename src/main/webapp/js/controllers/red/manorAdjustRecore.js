/**
 * 平台红包账户查询
 */
angular.module('inspinia').controller('manorAdjustRecoreCtrl', function($scope, $http,$stateParams,i18nService, SweetAlert){
    //数据源
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    $scope.resetForm = function () {
        $scope.baseInfo = {createTimeStart:moment(new Date().getTime()-7*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
        		createTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
            isTrade:"",lordsOrgId:"",userId:"",userName:"",phone:"",type:"asc",oderByCol:"territory_id"};
        $scope.selected = "";
        $scope.selected2 = "";
        $scope.selected3 = "";
    }
    $scope.resetForm();
    //
    $scope.isTradeList= [{text:"全部",value:""},{text:"是",value:"1"},{text:"否",value:"0"}];
    $scope.isTradeListStr=angular.toJson($scope.isTradeList);
    
    
    $scope.descList= [{text:"升序",value:"asc"},{text:"降序",value:"desc"}];
    $scope.orderByList= [{text:"领地id",value:"territory_id"},{text:"当前领主买入时间",value:"pay_date"},
                         {text:"当前领主买入价格",value:"pay_price"}];
    
    $scope.columnDefs = [
        {field: 'id',displayName: '调价记录ID',pinnable: false,sortable: false,width:150},
        {field: 'createTime',displayName: '调价时间',pinnable: false,sortable: false,width:150,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'territoryId',displayName: '领地ID',pinnable: false,sortable: false,width:150},
        {field: 'provinceName',displayName: '省',pinnable: false,sortable: false,width:150},
        {field: 'cityName',displayName: '市',pinnable: false,sortable: false,width:150},
        {field: 'regionName',displayName: '区',pinnable: false,sortable: false,width:150},
        {field: 'orgName',displayName: '当时领主所属组织',pinnable: false,sortable: false,width:150},
        {field: 'lordsUserCode',displayName: '当时领主ID',pinnable: false,sortable: false,width:150},
        {field: 'userName',displayName: '当时领主姓名',pinnable: false,sortable: false,width:150},
        {field: 'nickName',displayName: '当时领主昵称',pinnable: false,sortable: false,width:150},
        {field: 'phone',displayName: '当时领主手机号',pinnable: false,sortable: false,width:150},
        {field: 'payDate',displayName: '当时领主买入时间',pinnable: false,sortable: false,width:150,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'payPrice',displayName: '当时领主买入价格',pinnable: false,sortable: false,width:150},
        {field: 'isTrade',displayName: '暂不转让',pinnable: false,sortable: false,width:150,cellFilter:"formatDropping:"+$scope.isTradeListStr},
        {field: 'oldTradePrice',displayName: '原转让价格',pinnable: false,sortable: false,width:150},
        {field: 'newTradePrice',displayName: '新转让价格',pinnable: false,sortable: false,width:150}
    ];

    $scope.grid = {
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
        $http.post("manor/manorAdjustRecore","baseInfo=" + angular.toJson($scope.baseInfo)+"&pageNo="+
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
    // $scope.query();
    
    //查询所有银行家组织
    $scope.orgInfoList = [];
    $scope.getOrgInfoList = function () {
        $http({
            url:"superBank/getOrgInfoList",
            method:"POST"
        }).success(function(msg){
            if(msg.status){
                $scope.orgInfoList = msg.data;
                $scope.orgInfoList.unshift({orgId:"",orgName:"全部"});
            }
        }).error(function(){
            $scope.notice("获取组织信息异常");
        })
    };
    $scope.getOrgInfoList();
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
