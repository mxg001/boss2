/**
 * 超级银行家用户详情
 */
angular.module('inspinia').controller('userInfoDetailCtrl',function($scope,$http,$stateParams,i18nService){
    //数据源
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
	$scope.debitCreditSideList=[{text:"全部",value:""},{text:"减少",value:"debit"},
								{text:"增加",value:"credit"},{text:"冻结",value:"freeze"},
								{text:"解冻",value:"unfreeze"}]
    $scope.statusMentorList = [{text:"否",value:"0"},{text:"是",value:"1"}];
    //清空
    $scope.clear=function(){
        $scope.accountInfoRecord = {
        	debitCreditSide :"",
			userId:$stateParams.userCode,
            recordTimeStart:moment(new Date().getTime()-24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
            recordTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'
        };
    }
    $scope.clear();

	//获取用户详情
	$scope.getUserDetail = function(){
        $http({
			url:'superBank/selectUserDetail?userCode='+$stateParams.userCode,
			method:'GET'
		}).success(function(result) {
			if (result.status) {
                $scope.baseInfo = result.data.userInfo;
                $scope.userCard = result.data.userCard;
                if($scope.baseInfo.accountStatus == '1'){
                   $scope.getUserAccountInfo();
                    $scope.getUserAccountDetail();
                }
			} else {
                $scope.notice(result.msg);
			}
		}).error(function(){
			$scope.notice("系统异常，请稍后再试");
		});
	};

	//获取用户账户数据
	$scope.getUserAccountInfo = function(){
        $http({
            url:'superBank/getUserAccountInfo?userCode='+$stateParams.userCode,
            method:'GET'
        }).success(function(result) {
            if (result.status) {
                $scope.accountBalances.data = result.data;
            } else {
                $scope.notice(result.msg);
			}
        }).error(function(){
            $scope.notice("系统异常，请稍后再试");
        });
	};

	//获取用户账户明细
    $scope.getUserAccountDetail = function(){
        $scope.submitting = true;
        $scope.loadImg = true;
        $http({
			url:"superBank/getUserAccountDetail?pageNo=" + $scope.paginationOptions.pageNo + "&pageSize=" + $scope.paginationOptions.pageSize,
			data:$scope.accountInfoRecord,
			method:"POST"
		}).success(function(result){
            $scope.submitting = false;
            $scope.loadImg = false;
            if(result.status){
                $scope.accountDetail.data = result.data.list;
                $scope.accountDetail.totalItems = result.data.total;
            }else{
                $scope.notice(result.msg);
            }
        }).error(function(){
            $scope.notice("系统异常，请稍后再试");
        });
    };
    
  //获取用户冻结解冻日志记录
    $scope.getUserFreezeOperLog = function(){
        $scope.submitting = true;
        $scope.loadImg = true;
        $http({
			url:"superBank/getUserFreezeOperLog?pageNo=" + $scope.paginationOptions.pageNo + "&pageSize=" + $scope.paginationOptions.pageSize,
			data:"userCode=" + $stateParams.userCode,
			headers: {'Content-Type': 'application/x-www-form-urlencoded'},
			method:"POST"
		}).success(function(result){
            $scope.submitting = false;
            $scope.loadImg = false;
            if(result.status){
                $scope.userFreezeOperLog.data = result.data.result;
                $scope.userFreezeOperLog.totalItems = result.data.totalCount;
            }else{
                $scope.notice(result.msg);
            }
        }).error(function(){
            $scope.notice("系统异常，请稍后再试");
        });
    };

    $scope.getUserDetail();


    //账户余额
	$scope.accountBalances = {                 //配置表格
        columnDefs:[                           //表格数据
            { field: 'accountNo',displayName:'账号'},
            { field: 'balance',displayName:'余额'},
            { field: 'controlAmount',displayName:'冻结金额' }
        ]
    };

	//账户明细
	$scope.accountDetail={                           //配置表格
			paginationPageSize:10,                  //分页数量
			paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
			useExternalPagination: true,
			enableHorizontalScrollbar: 1,        //横向滚动条
			enableVerticalScrollbar : 1,
			columnDefs:[                           //表格数据
				{ field: 'recordDateTime',displayName:'记账时间',cellTemplate:"<div class='lh30'>{{row.entity.recordDate | date:'yyyy-MM-dd'}} {{row.entity.recordTime | date:'HH:mm:ss'}}</div>"
                },
				{ field: 'debitCreditSide',displayName:'操作',cellFilter:"formatDropping:" + angular.toJson($scope.debitCreditSideList)},
				{ field: 'recordAmount',displayName:'金额' },
				{ field: 'avaliBalance',displayName:'可用余额' }
			],
			onRegisterApi: function(gridApi) {
				$scope.gridApi = gridApi;
				$scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
					$scope.paginationOptions.pageNo = newPage;
					$scope.paginationOptions.pageSize = pageSize;
					$scope.getUserAccountDetail();
				});
			}
		};
	
	
	//冻结解冻日志记录
	$scope.userFreezeOperLog={                           //配置表格
			paginationPageSize:10,                  //分页数量
			paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
			useExternalPagination: true,
			enableHorizontalScrollbar: 1,        //横向滚动条
			enableVerticalScrollbar : 1,
			columnDefs:[                           //表格数据
				{ field: 'operType',displayName:'操作类型',width:220,
	                cellFilter:"formatDropping:[{text:'解冻',value:1},{text:'冻结',value:2}]"
	            },
	            { field: 'operName',displayName:'操作人',width:240},
	            { field: 'operTime',displayName:'操作时间',width:240,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},	            
	            { field: 'operReason',displayName: '备注',
	            	cellTemplate: '<div class="ui-grid-cell-contents ng-binding ng-scope" title="{{row.entity.operReason}}">{{row.entity.operReason}}</div>'
	         	}
			],
			onRegisterApi: function(gridApi) {
				$scope.gridApi = gridApi;
				$scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
					$scope.paginationOptions.pageNo = newPage;
					$scope.paginationOptions.pageSize = pageSize;
					$scope.getUserFreezeOperLog();
				});
			}
		};

});
