/**
 * 查询服务
 */
angular.module('inspinia',['uiSwitch']).controller("queryServiceCtrl", function($scope, $http, $state, $stateParams,uiGridConstants,i18nService,SweetAlert,$document) {
	var self = this;
	//数据源
  $scope.tFlags=[{text:"全部",value:""},{text:"不涉及",value:0},{text:"只允许T0",value:1},{text:"只允许T1",value:2},{text:"允许T0和T1",value:3}];
  $scope.tFlagsFilter = [{text:"不涉及",value:0},{text:"只允许T0",value:1},{text:"只允许T1",value:2},{text:"允许T0和T1",value:3}];
  $scope.tFlagsStr = angular.toJson($scope.tFlagsFilter);
	$scope.audits=[{text:"全部",value:""},{text:"已审核",value:1},{text:"未审核",value:0}];
	$scope.lock=[{text:"全部",value:""},{text:"已锁定",value:1},{text:"未锁定",value:0}];
	$scope.fixedMark=[{text:"全部",value:""},{text:"固定",value:1},{text:"不固定",value:0}];
    $scope.effectiveStatusList = [{text:'全部',value:null},{text:'失效',value:0},{text:'生效',value:1}];
	$scope.baseInfo={};
	$scope.boolStr=angular.toJson($scope.bool);
	$scope.baseInfoDefault=angular.copy($scope.baseInfo);
	$scope.servicesData=[];
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
    i18nService.setCurrentLang('zh-cn');
    //reset
    $scope.resetForm=function(){
        $scope.baseInfo={tFlag:"",serviceName:"",serviceType:"",rateCard:"",rateHolidays:"",quotaHolidays:"",quotaCard:"",fixedRate:"",fixedQuota:"",
            rateCheckStatus:"",rateLockStatus:"",quotaCheckStatus:"",quotaLockStatus:"",effectiveStatus:null,
          createTimeBegin:moment(new Date().getTime()-7*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',createTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD'+' 23:59:59')};
    }
    $scope.resetForm();

  //是否校验时间
  isVerifyTime = 0;//校验：1，不校验：0
  setBeginTime=function(setTime){
    $scope.baseInfo.createTimeBegin = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
  }

  setEndTime=function(setTime){
    $scope.baseInfo.createTimeEnd = moment(setTime).format("YYYY-MM-DD HH:mm:ss");

  }
	$scope.queryServiceList=function(){
		$http.post('service/queryServiceList',
			  "baseInfo="+angular.toJson($scope.baseInfo)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
			  {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
		).success(function(data, status, headers, config) {  
				$scope.servicesData=data.result;
				$scope.servicesGrid.totalItems = data.totalCount;
		}).error(function(data, status, headers, config) {  
		});
	};
	$scope.queryServiceList();

	//导出列表
  $scope.exportServiceList = function(){
    if($scope.loadImg){
      return;
    }

    SweetAlert.swal({
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
          $scope.exportInfoClick("service/exportSelectList",{"baseInfo":angular.toJson($scope.baseInfo)});
        }
      });
  }
    $scope.serviceTypeStr=angular.toJson($scope.serviceTypes);
	$scope.servicesGrid = {
	        data: 'servicesData',
	        paginationPageSize:10,                  //分页数量
	        paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
	        useExternalPagination: true,		  //开启拓展名
//	        enableHorizontalScrollbar: 0,        //去掉滚动条
	        enableVerticalScrollbar : 0, 
	        columnDefs: [
				{field:'serviceId',displayName: '服务ID',width:100},
                {field: 'serviceName',displayName: '服务名称',width:220,enableSorting: false},
                {field: 'agentShowName',displayName: '代理商展示名称',width:220,enableSorting: false},
                {field: 'serviceType',displayName: '服务类型',width:180,pinnable: false,sortable: false,cellFilter: "formatDropping:"+$scope.serviceTypeStr},
                {field: 'dRate',displayName: '贷记卡商户费率',width:180,pinnable: false,sortable: false},
                {field: 'jRate',displayName: '借记卡商户费率',width:180,pinnable: false,sortable: false},
                {field: 'tFlag',displayName: 'T0T1标志',width:180,pinnable: false,sortable: false,cellFilter: "formatDropping:"+$scope.tFlagsStr},
	            {field: 'rateCard',displayName: '费率区分银行卡种类',width:180,pinnable: false,sortable: false,cellFilter:"formatDropping:"+$scope.boolStr},
	            {field: 'rateHolidays',displayName: '费率区分节假日',width:180,pinnable: false,sortable: false,cellFilter:"formatDropping:"+$scope.boolStr},
	            {field: 'quotaCard',displayName: '限额区分银行卡种类',width:180,pinnable: false,sortable: false,cellFilter:"formatDropping:"+$scope.boolStr},
	            {field: 'quotaHolidays',displayName: '限额区分银行节假日',width:180,pinnable: false,sortable: false,cellFilter:"formatDropping:"+$scope.boolStr},
	            {field: 'serviceStatus',displayName: '开关',width:150,pinnable: false,sortable: false,editable:true,cellTemplate:
	            	'<span ng-show="grid.appScope.hasPermit(\'service.switch\')"><switch class="switch switch-s" ng-model="row.entity.serviceStatus" ng-change="grid.appScope.updateServiceStatus(row)" /></span>'
	            	+'<span ng-show="!grid.appScope.hasPermit(\'service.switch\')"> <span class="lh30" ng-show="row.entity.serviceStatus==1">开启</span><span class="lh30" ng-show="row.entity.serviceStatus==0">关闭</span></span>'
	            },
                {field: 'effectiveStatus',displayName: '状态',width:150,pinnable: false,sortable: false,editable:true,cellTemplate:
                    '<span ng-show="grid.appScope.hasPermit(\'service.updateEffectiveStatus\')&&row.entity.effectiveStatus==1"><switch class="switch2 switch-s" ng-model="row.entity.effectiveStatus" ng-change="grid.appScope.updateEffectiveStatus(row.entity)" /></span>'
                    +'<span ng-show="!grid.appScope.hasPermit(\'service.updateEffectiveStatus\')||row.entity.effectiveStatus==0"> <span class="lh30" ng-show="row.entity.effectiveStatus==1">生效</span><span class="lh30" ng-show="row.entity.effectiveStatus==0">失效</span></span>'
                },
            {field: 'createTime',displayName: '创建时间',width:180,pinnable: false,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
            {field: 'createPerson',displayName: '创建人',width:180,pinnable: false,sortable: false},
	            {field: 'action',displayName: '操作',width:150,pinnedRight:true,pinnable: false,sortable: false,editable:true,cellTemplate:
	            	"<a class='lh30' ng-show='grid.appScope.hasPermit(\"service.detail\")' ui-sref='service.serviceDetail({serviceId:row.entity.serviceId})'>详情</a>" +
	            	"<a class='lh30' ng-show='grid.appScope.hasPermit(\"service.edit\")' ui-sref='service.editService({serviceId:row.entity.serviceId})'> | 修改</a>" +
	            	"<a class='lh30' ui-sref='service.agentProfit({serviceId:row.entity.serviceId})'> | 代理商分润</a>"}
	        ],
	        onRegisterApi: function(gridApi) {                
	            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
	            	$scope.paginationOptions.pageNo = newPage;
	            	$scope.paginationOptions.pageSize = pageSize;
	                $scope.queryServiceList();
					var newHeight = pageSize*30;
					angular.element(document.getElementsByClassName('grid')[0]).css('height', newHeight + 'px');
	            });
	        }
	};

    $scope.updateServiceStatus=function(row){
        if(row.entity.serviceStatus){
            $scope.serviceText = "开启服务？";
        } else {
            $scope.serviceText = "关闭服务？";
        }
        SweetAlert.swal({
                title: $scope.serviceText,
//            text: "服务状态为关闭后，不能正常交易!",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    if(row.entity.serviceStatus==true){
                        row.entity.serviceStatus=1;
                    } else if(row.entity.serviceStatus==false){
                        row.entity.serviceStatus=0;
                    }
                    $http.post("service/updateServiceStatus",angular.toJson({serviceId:row.entity.serviceId,status:row.entity.serviceStatus}))
                        .success(function(data){
                            if(data.status){
                                $scope.notice("操作成功");
                            }else{
                                if(row.entity.serviceStatus==true){
                                    row.entity.serviceStatus = false;
                                } else {
                                    row.entity.serviceStatus = true;
                                }
                                $scope.notice("操作失败");
                            }
                        })
                        .error(function(data){
                            if(row.entity.serviceStatus==true){
                                row.entity.serviceStatus = false;
                            } else {
                                row.entity.serviceStatus = true;
                            }
                            $scope.notice("操作失败");
                        });
                } else {
                    if(row.entity.serviceStatus==true){
                        row.entity.serviceStatus = false;
                    } else {
                        row.entity.serviceStatus = true;
                    }
                }
            });

    };

    $scope.updateEffectiveStatus=function(entity){
        if(entity.effectiveStatus){
            $scope.notice("失效的服务不能再生效");
            entity.effectiveStatus = false;
            return;
        } else {
            $scope.serviceText = "服务种类失效后，将不能恢复正常状态，请确认是否继续！";
        }
        SweetAlert.swal({
                title: $scope.serviceText,
//            text: "服务状态为关闭后，不能正常交易!",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    if(entity.effectiveStatus==true){
                        entity.effectiveStatus=1;
                    } else if(entity.effectiveStatus==false){
                        entity.effectiveStatus=0;
                    }
                    $http.post("service/updateEffectiveStatus",{serviceId:entity.serviceId,effectiveStatus:entity.effectiveStatus})
                        .success(function(data){
                            if(!data.status){
                                if(entity.effectiveStatus==true){
                                    entity.effectiveStatus = false;
                                } else {
                                    entity.effectiveStatus = true;
                                }
                            }
                            $scope.notice(data.msg);
                        })
                } else {
                    if(entity.effectiveStatus==true){
                        entity.effectiveStatus = false;
                    } else {
                        entity.effectiveStatus = true;
                    }
                }
            });

    };

	//页面绑定回车事件
	$document.bind("keypress", function(event) {
		$scope.$apply(function (){
			if(event.keyCode == 13){
				$scope.queryServiceList();
			}
		})
	});
});

