/**
 * 超级nfc，激活码管理
 */
angular.module('inspinia',['infinity.angular-chosen','angularFileUpload']).controller('commonCodeCtrl',function($scope,$http,$state,$stateParams,i18nService,$timeout,FileUploader){
	//数据源
	i18nService.setCurrentLang('zh-cn');

    // 查询分页
    $scope.page = {
        pageNo : 1,
        pageSize: 10
    };
    // 查询条件
    $scope.info  = {
        agentNo:""
    };


	$scope.activationCodeGrid = {
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs: [
            {field: 'agentName',displayName: '代理商名称',pinnable: false,width: 280,sortable: false},
            {field: 'agentNo',displayName: '代理商编号',pinnable: false,width: 280,sortable: false},
            { field: 'commonCodeUrl',displayName:'通用码样式',width:380,
                cellTemplate:
                '<div ng-show="row.entity.commonCodeUrl!=null"> ' +
                  '<a href="{{row.entity.commonCodeUrl}}" fancybox rel="group">' +
                    '<img style="width:70px;height:35px;"   ng-src="{{row.entity.commonCodeUrl}}"/>' +
                  '</a>'+
                  '</div>'
            },
            {field: 'action',displayName: '操作',width: 250,pinnedRight:true,sortable: false,editable:true,cellTemplate:
	         	'<div class="lh30">'
	            +'<a class="lh30" ng-show="grid.appScope.hasPermit(\'commonCode.updateCommonCode\')" ui-sref="superNfc.updateCommonCode({id:row.entity.id})" >修改</a>'
	            //+'<a class="lh30" ng-show="row.entity.agentNo!=\'default\' && grid.appScope.hasPermit(\'commonCode.delCommonCode\') && row.entity.commonCodeUrl!=null" ng-click="grid.appScope.delCommonCodeById(row.entity.id)"> | 删除</a>'
            	+'</div>'
            }
        ],
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            $scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
	          	$scope.page.pageNo = newPage;
	          	$scope.page.pageSize = pageSize;
	            $scope.query();
	        });
        }
	 };
    // 查询按钮
    $scope.query = function () {
        if ($scope.loadImg) {
            return;
        }
        $scope.loadImg = true;
        $http({
            url: 'commonCode/queryAll?pageNo='+$scope.page.pageNo +"&pageSize=" + $scope.page.pageSize,
            method: 'POST',
            data:$scope.info
        }).success(function (data) {
            $scope.activationCodeGrid.data =data.data;
            $scope.activationCodeGrid.totalItems = data.count;
            $scope.loadImg = false;
        });
    };

    $scope.query();



	// 重置按钮
	$scope.resetForm = function () {
        $scope.info = {agentNo: null};
    };




    //删除通用码
    $scope.delCommonCodeById = function(id){
        $http({
            url: 'commonCode/delCommonCode?id='+id,
            method: 'POST'
        }).success(function (data) {
            if(data.result){
                $scope.query();
                window.open('welcome.do#/superNfc/commonCode', '_self');
            }
            $scope.notice(data.msg);
        });
    }


});


