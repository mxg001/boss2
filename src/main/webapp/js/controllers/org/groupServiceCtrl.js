/**
 * 收单服务
 */
angular.module('inspinia',['uiSwitch']).controller("groupServiceCtrl", function($scope, $http, $state, $stateParams, i18nService,SweetAlert,$document) {
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	i18nService.setCurrentLang('zh-cn');
	//数据库字段决定1:是，2：否，所以没有使用全局
	$scope.boolAll = [{text:'全部',value:""},{text:'是',value:1},{text:'否',value:2}];
	$scope.info = {};
	$scope.timeInfo={};
	$scope.openInfo={};
	$scope.selectOpenInfo={};
	$http.post('groupService/acqOrgSelectBox.do'
	).success(function(data){
		$scope.acqOrgs = data;
		$scope.acqOrgs.splice(0,0,{"acqName":"全部","id":""});
	}).error(function(){
	}); 
	
	$scope.query = function() {
		$http.post('groupService/queryAcqService.do',
       		 "info="+angular.toJson($scope.info)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
       		 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
        ).success(function(data){
            $scope.groupServiceDate = data.result;
			$scope.groupServiceGrid.totalItems = data.totalCount;//总记录数
        }).error(function(){
        }); 
	};
	$scope.query();
	
	$scope.reset = function() {
		$scope.info = {acqId:"",serviceType:"",feeIsCard:"",quotaIsCard:""};
	};
	$scope.reset();
	
	$scope.groupServiceGrid = {
	        data: 'groupServiceDate',
	        paginationPageSize:10,                  //分页数量
	        paginationPageSizes: [10,20,50,100],	//切换每页记录数
	        useExternalPagination: true,		  //开启拓展名
//	        enableHorizontalScrollbar: 0,        //去掉滚动条
//	        enableVerticalScrollbar : 0, 
	        columnDefs: [
                {field: 'id',displayName: '收单服务ID',width: 120,pinnable: false,sortable: false},
                {field: 'serviceName',displayName: '服务名称',width: 150,pinnable: false,sortable: false},
                {field: 'acqName',displayName: '收单机构',width: 150,pinnable: false,sortable: false},
                {field: 'serviceType',displayName: '服务类型',width: 200,pinnable: false,sortable: false,cellFilter:"formatDropping:"+angular.toJson($scope.acqServiceTypes)},
                {field: 'feeIsCard',displayName: '费率区分银行卡种类',width: 180,pinnable: false,sortable: false,cellFilter:"formatDropping:"+angular.toJson($scope.boolAll)},
                {field: 'quotaIsCard',displayName: '限额区分银行卡种类',width: 180,pinnable: false,sortable: false,cellFilter:"formatDropping:"+angular.toJson($scope.boolAll)},
                {field: 'serviceStatus',displayName: '状态',width: 180,pinnable: false,sortable: false,
                	cellTemplate:
                		'<span ng-show="grid.appScope.hasPermit(\'groupService.switch\')"><switch class="switch switch-s" ng-model="row.entity.serviceStatus" ng-change="grid.appScope.openModel(row.entity)" /></span>'
        	            +'<span ng-show="!grid.appScope.hasPermit(\'groupService.switch\')"> <span ng-show="row.entity.serviceStatus==1">开启</span><span ng-show="row.entity.serviceStatus==0">关闭</span></span>'
                
                },
				{field: 'timeStartTime',displayName: '指定日期关闭开始时间',width:180,pinnable: false,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
				{field: 'timeEndTime',displayName: '指定日期关闭结束时间',width:180,pinnable: false,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
				{field: 'periodicityStartTime',displayName: '周期性关闭开始时间',width: 150,pinnable: false,sortable: false},
				{field: 'periodicityEndTime',displayName: '周期性关闭结束时间',width: 150,pinnable: false,sortable: false},
				{field: 'closePrompt',displayName: '关闭提示语',width:200,pinnable: false,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
				{name: 'action',displayName: '操作',width: 280,pinnedRight:true,pinnable: false,sortable: false,editable:true,cellTemplate:
	            	"<a class='lh30' ng-show='grid.appScope.hasPermit(\"groupService.detail\")'  ui-sref='org.groupServiceDetail({id:row.entity.id})' >详情</a>"
	            	+"<a class='lh30' ng-show='grid.appScope.hasPermit(\"groupService.updateRate\")' ui-sref='org.updateServiceRate({id:row.entity.id})' > | 修改费率</a>"
	            	+"<a class='lh30' ng-show='grid.appScope.hasPermit(\"groupService.updateQuota\")' ui-sref='org.updateServiceQuota({id:row.entity.id,quotaIsCard:row.entity.quotaIsCard})' > | 修改限额</a>"
					+"<a class='lh30' ng-show='row.entity.serviceStatus==1&&grid.appScope.hasPermit(\"groupService.updateTimeSwitch\")' ng-click='grid.appScope.hardWardAddModel(row.entity)' > | 定时开关</a>"
				}
	        ],
	        onRegisterApi: function(gridApi) {                
	            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
	            	$scope.paginationOptions.pageNo = newPage;
	            	$scope.paginationOptions.pageSize = pageSize;
	            	$scope.query();
	            });
	        }
	};

	//定时关闭服务
	$scope.hardWardAddModel = function(entity){
		if(entity.timeStartTime==null){
			$scope.timeInfo.timeSwitch=1;
		}else{
			$scope.timeInfo.timeSwitch=entity.timeSwitch;
		}
		$scope.timeInfo.id=entity.id;

		$scope.timeInfo.timeStartTime=entity.timeStartTime==null?null:moment(entity.timeStartTime).format('YYYY-MM-DD HH:mm:ss');
		$scope.timeInfo.timeEndTime=entity.timeEndTime==null?null:moment(entity.timeEndTime).format('YYYY-MM-DD HH:mm:ss');
		$scope.timeInfo.periodicityStartTime=entity.periodicityStartTime;
		$scope.timeInfo.periodicityEndTime=entity.periodicityEndTime;
		$scope.timeInfo.closePrompt=entity.closePrompt;
		$("#hardWardAddModel").modal("show");
	}
	$scope.cancel=function(){
		$('#hardWardAddModel').modal('hide');
	}
	$scope.saveTime=function(){
		if($scope.timeInfo.timeSwitch==1){
			/*
			//开启
			if($scope.timeInfo.timeStartTime==null||$scope.timeInfo.timeStartTime==""
				||$scope.timeInfo.timeEndTime==null||$scope.timeInfo.timeEndTime==""){
				$scope.notice("指定日期起始不能为空!");
				return;
			}
			 */
			var isNotNullTimeStartTime = $scope.timeInfo.timeStartTime!=null && $scope.timeInfo.timeStartTime!="";
			var isNullTimeEndTime = $scope.timeInfo.timeEndTime == null || $scope.timeInfo.timeEndTime=="";
			var isNotNullTimeEndTime = $scope.timeInfo.timeEndTime!=null && $scope.timeInfo.timeEndTime!="";
			var isnullTimeStartTime = $scope.timeInfo.timeStartTime == null || $scope.timeInfo.timeStartTime=="" ;
			if((isNotNullTimeStartTime && isNullTimeEndTime) || (isNotNullTimeEndTime && isnullTimeStartTime)){
				$scope.notice("指定日期关闭要么为空要么都填写!");
				return ;
			}
			// 判断周期性时间要么为空要么都填写
			var isNotNullPeriodicityStartTime = $scope.timeInfo.periodicityStartTime!=null && $scope.timeInfo.periodicityStartTime!="";
			var isNullPeriodicityEndTime = $scope.timeInfo.periodicityEndTime == null || $scope.timeInfo.periodicityEndTime=="";
			var isNotNullPeriodicityEndTime = $scope.timeInfo.periodicityEndTime!=null && $scope.timeInfo.periodicityEndTime!="";
			var isnullPeriodicityStartTime = $scope.timeInfo.periodicityStartTime == null || $scope.timeInfo.periodicityStartTime=="" ;
			if((isNotNullPeriodicityStartTime && isNullPeriodicityEndTime) || (isNotNullPeriodicityEndTime && isnullPeriodicityStartTime)){
				$scope.notice("周期性关闭要么为空要么都填写!");
				return ;
			}
			if($scope.timeInfo.closePrompt==null||$scope.timeInfo.closePrompt==""){
				$scope.notice("服务关闭时,客户端提示语不能为空!");
				return;
			}
		}
		if($scope.submitting){
			return;
		}
		$scope.submitting=true;
		$http.post("groupService/updateTimeSwitch","info="+angular.toJson($scope.timeInfo),
			{headers: {'Content-Type':'application/x-www-form-urlencoded'}})
			.success(function(msg){
				$scope.notice(msg.msg);
				$scope.query();
				$scope.submitting=false;
				$scope.cancel();
			})
			.error(function (msg) {
				$scope.notice(msg.msg);
				$scope.submitting=false;
				$scope.cancel();
			});
	}

	//开关
	$scope.openModel = function(entity){
		$scope.selectOpenInfo=entity;
		$scope.openInfo.serviceStatus=entity.serviceStatus;
		$scope.openInfo.id=entity.id;
		if(entity.serviceStatus==true){
			//关闭-->开启
			$scope.openService(entity);
		}else{
			//开启-->关闭
			$scope.openInfo.closePrompt="";
			$("#openModel").modal("show");
		}

	}
	$scope.openModelcancel=function(istrue){
		if(!istrue){
			if($scope.selectOpenInfo.serviceStatus==true){
				$scope.selectOpenInfo.serviceStatus = false;
			} else {
				$scope.selectOpenInfo.serviceStatus = true;
			}
		}
		$('#openModel').modal('hide');
	}

	$scope.closeService=function(){
		if($scope.selectOpenInfo.serviceStatus==true){
			$scope.openInfo.serviceStatus=1;
		} else if($scope.selectOpenInfo.serviceStatus==false){
			$scope.openInfo.serviceStatus=0;
			if($scope.openInfo.closePrompt==null||$scope.openInfo.closePrompt==""){
				$scope.notice("服务关闭时,客户端提示语不能为空!");
				return;
			}
		}
		$http.post("groupService/updateAcqServiceStatus.do","info="+angular.toJson($scope.openInfo),
			{headers: {'Content-Type':'application/x-www-form-urlencoded'}})
			.success(function(data){
				if(data.status){
					$scope.notice(data.msg);
					$scope.openModelcancel(true);
				}else{
					$scope.notice(data.msg);
					$scope.openModelcancel(false);
				}
				$scope.query();
			})
			.error(function(data){
				$scope.notice("服务异常");
				$scope.openModelcancel(false);
			});
	};

	$scope.openService=function(entity){
		$scope.serviceText = "确定开启？";
		SweetAlert.swal({
				title: $scope.serviceText,
				type: "warning",
				showCancelButton: true,
				confirmButtonColor: "#DD6B55",
				confirmButtonText: "提交",
				cancelButtonText: "取消",
				closeOnConfirm: true,
				closeOnCancel: true },
			function (isConfirm) {
				if (isConfirm) {
					if($scope.openInfo.serviceStatus==true){
						$scope.openInfo.serviceStatus=1;
					} else if($scope.openInfo.serviceStatus==false){
						$scope.openInfo.serviceStatus=0;
					}
					$http.post("groupService/updateAcqServiceStatus.do","info="+angular.toJson($scope.openInfo),
						{headers: {'Content-Type':'application/x-www-form-urlencoded'}})
						.success(function(data){
							if(data.status){
								$scope.notice("操作成功");
							}else{
								if(entity.serviceStatus==true){
									entity.serviceStatus = false;
								} else {
									entity.serviceStatus = true;
								}
								$scope.notice("操作失败");
							}
							$scope.query();
						})
						.error(function(data){
							if(entity.serviceStatus==true){
								entity.serviceStatus = false;
							} else {
								entity.serviceStatus = true;
							}
							$scope.notice("服务异常")
						});
				} else {
					if(entity.serviceStatus==true){
						entity.serviceStatus = false;
					} else {
						entity.serviceStatus = true;
					}
				}
			});

	};
	//页面绑定回车事件
	$document.bind("keypress", function(event) {
		$scope.$apply(function (){
			if(event.keyCode == 13){
				$scope.query();
			}
		})
	});
	
});