/**
 * 交易查询
 */
angular.module('inspinia', ['angularFileUpload','infinity.angular-chosen']).controller('terActivityCheckCtrl',
	function($scope,$http,$state,$stateParams,$compile,$uibModal,$timeout,$log,i18nService,SweetAlert,$document,$q,FileUploader,$location){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	//数据源
	$scope.oneAgents=[{value:"",text:"全部"}];
	$scope.agents=[{value:"",text:"全部"}];
	$scope.termianlTypes=[];
	$scope.openStatus=[{text:"已入库",value:0},{text:"已分配",value:1},{text:"申请中",value:3},{text:"已使用",value:2}];
	$scope.status=[{value:0,text:"未激活"},{value:1,text:"已激活"}];
	$scope.checkStatus=[{value:1,text:"未达标"},{value:2,text:"考核中"},{value:3,text:"已达标"}];
	$scope.orders=[{value:0,text:"考核剩余天数从高到低"},{value:1,text:"考核剩余天数从低到高"}];
	$scope.typeNos=[];
	$scope.info={};
	$scope.bools=[{text:"是",value:1},{text:"否",value:0}];



	//代理商
	$http.post("agentInfo/selectAllOneInfo.do")
		.success(function(msg){
			//响应成功
			for(var i=0; i<msg.length; i++){
				$scope.oneAgents.push({value:msg[i].agentNo,text:msg[i].agentName});
				$scope.agents.push({value:msg[i].agentNo,text:msg[i].agentName});
			}
		});

	$scope.oneAgentStates =oneAgentStates;
	var oldValue="";
	var timeout="";
	function oneAgentStates(value) {
		$scope.agentt = [];
		var newValue=value;
		if(newValue != oldValue){
			if (timeout) $timeout.cancel(timeout);
			timeout = $timeout(
				function(){
					$http.post('agentInfo/selectAllInfo','item=' + value,
						{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
						.then(function (response) {
							if(response.data.length==0) {
								$scope.agentt.push({value: "", text: ""});
							}else{
								for(var i=0; i<response.data.length; i++){
									$scope.agentt.push({value:response.data[i].agentNo,text:response.data[i].agentName});
								}
							}
							$scope.oneAgents = $scope.agentt;
							oldValue = value;
						});
				},800);
		}
	}


	$scope.agentsStates =agentsStates;
	var oldValue="";
	var timeout="";
	function agentsStates(value) {
		$scope.agentt = [];
		var newValue=value;
		if(newValue != oldValue){
			if (timeout) $timeout.cancel(timeout);
			timeout = $timeout(
				function(){
					$http.post('agentInfo/selectAllInfo','item=' + value,
						{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
						.then(function (response) {
							if(response.data.length==0) {
								$scope.agentt.push({value: "", text: ""});
							}else{
								for(var i=0; i<response.data.length; i++){
									$scope.agentt.push({value:response.data[i].agentNo,text:response.data[i].agentName});
								}
							}
							$scope.agents = $scope.agentt;
							oldValue = value;
						});
				},800);
		}
	}


	//硬件产品
	$scope.pns = [];
	$http.post("hardwareProduct/selectAllHardwareProduct")
		.success(function(result){
			if(result.status){
				var list = result.list;
				//响应成功
				for(var i=0; i<list.length; i++){
					$scope.pns.push({value:list[i].hpId,text:list[i].typeName});
				}
			}
		});


	// 准备数据时使用的所有promise
	var promises = [];

	// 业务产品下拉列表
	var bpPromise=$q.defer();
	promises.push(bpPromise.promise);
	$http.get('businessProductDefine/selectAllInfo.do')
		.success(function(result){
			if(!result)
				return;
			$scope.bpList = [];
			angular.forEach(result, function(data){
				$scope.bpList.push({text:data.bpName,value:data.bpId});
			});
			$scope.BpIdsStr = angular.toJson($scope.bpList);
			$scope.bpListAll = angular.copy($scope.bpList);
			$scope.bpListAll.unshift({text:"全部",value:""});
			if($scope.gridOptions){
				angular.forEach($scope.gridOptions.columnDefs, function(data){
					if(data.field == 'bpId'){
						data.cellFilter = "formatDropping:"+$scope.BpIdsStr;
						return false;
					}
				});
			}
			bpPromise.resolve();
			delete bpPromise;
		});



	//子类型
	$scope.checkActivityCode = function(){
		$http.post("activity/getActivityTypeNoList").success(function (data) {
			if(data.status){
				for(var i=0; i<data.info.length; i++){
					$scope.typeNos.push({value:data.info[i].activityTypeNo,text:data.info[i].activityTypeName});
				}
			}
		})
	};
	$scope.checkActivityCode();


	//查询
	$scope.query=function(){
		/*if(($scope.info.oneAgentNo==null||$scope.info.oneAgentNo==""||$scope.info.oneAgentNo=="-1")
			&&($scope.info.agentNo==null||$scope.info.agentNo==""||$scope.info.agentNo=="-1")
			&&($scope.info.snStart==null||$scope.info.snStart=="")
			&&($scope.info.snEnd==null||$scope.info.snEnd=="")
			&&($scope.info.userCode==null||$scope.info.userCode=="")){
			$scope.notice("请输入SN号、一级代理商、所属代理商中任意一项!");
			return;
		}*/
		if($scope.loadImg){
			return;
		}
		$scope.loadImg = true;//ng-show,查询的时候置为true，查询完置为false

		$http.post("terminalInfo/selectWithTACByCondition",
			"info="+angular.toJson($scope.info)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
			{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			.success(function(data){
				$scope.loadImg = false;
				//响应成功
				if(!data.status)
					return;
				$scope.gridOptions.data = data.page.result;
				$scope.gridOptions.totalItems = data.page.totalCount;
			}).error(function(){
			$scope.loadImg = false;
		});
	}

	$scope.clear = function(){
		$scope.info={oneAgentNo:"",agentNo:"",type:"",bpId:"",activityTypeNo:"",openStatus:"",status:"",checkStatus:"",order:""};
	}



	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	var rowList={};
	var num=0;
	$scope.gridOptions={                           //配置表格
		 	  paginationPageSize:10,                  //分页数量
	          paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
	          useExternalPagination: true,                //分页数量
		      columnDefs:[                           //表格数据
				  {field: 'sn',displayName: 'SN号',width: 180,pinnable: false,sortable: false,
					  cellTemplate:'<a  target="_blank" class="lh30" ui-sref="terminalQueryDetail({termId:row.entity.id})">{{row.entity.sn}}</a>'},
				  { field: 'typeName',displayName:'硬件产品种类',width:150},
				  { field: 'bpName',displayName:'业务产品',width:150 },
				  { field: 'oneAgentNo',displayName:'一级代理商编号',width:150 },
				  { field: 'oneAgentName',displayName:'一级代理商名称',width:150 },
				  { field: 'agentNo',displayName:'所属代理商编号',width:150 },
				  { field: 'agentName',displayName:'所属代理商名称',width:150 },
				  { field: 'checkTime',displayName:'考核日期',width:180,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
				  { field: 'dueDaysValue',displayName:'考核剩余天数',width:150 },
				  { field: 'status',displayName:'激活状态',width:150,cellFilter:"formatDropping:"+angular.toJson($scope.status)},
				  { field: 'openStatus',displayName:'机具状态',width:150,cellFilter:"formatDropping:"+angular.toJson($scope.openStatus)},
				  { field: 'activityTypeNoName',displayName:'欢乐返子类型',width:150 }
		      ],
			  onRegisterApi: function(gridApi) {                
		          $scope.gridApi = gridApi;
		          //全选
			      $scope.gridApi.selection.on.rowSelectionChangedBatch($scope,function (rows) {
			            if(rows[0].isSelected){
			               $scope.testRow = rows[0].entity;
			               for(var i=0;i<rows.length;i++){
			            	   rowList[rows[i].entity.agentNo]=rows[i].entity;
			            	   num++;
			               }
			            }else{
			            	rowList={};
			            	num=0;
			            }
			         })
			         //单选
			         $scope.gridApi.selection.on.rowSelectionChanged($scope,function (row) {
			            if(row.isSelected){
			               $scope.testRow = row.entity;
			               rowList[row.entity.agentNo]=row.entity;
			               num++;
			            }else{
			            	delete rowList[row.entity.agentNo];
			            	num--;
			            	if(num<0){
			            		num=0;
			            	}
			            }
			         })
		          gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
		          	$scope.paginationOptions.pageNo = newPage;
		          	$scope.paginationOptions.pageSize = pageSize;
		             $scope.query();
		          });
		      }
		};



	// 查询列表导出
	$scope.export = function () {
		/*if(($scope.info.oneAgentNo==null||$scope.info.oneAgentNo==""||$scope.info.oneAgentNo=="-1")
			&&($scope.info.agentNo==null||$scope.info.agentNo==""||$scope.info.agentNo=="-1")
			&&($scope.info.snStart==null||$scope.info.snStart=="")
			&&($scope.info.snEnd==null||$scope.info.snEnd=="")
			&&($scope.info.userCode==null||$scope.info.userCode=="")){
			$scope.notice("请输入SN号、一级代理商、所属代理商中任意一项!");
			return;
		}*/
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
					//location.href = "terminalInfo/importDetail?info=" + encodeURI(angular.toJson($scope.info));
					$scope.exportInfoClick("terminalInfo/exportTerminalWithTAC", {"info": angular.toJson($scope.info)});
				}
			});
	};

	/**
	 * 更改考核期限弹窗
	 */
	$scope.showModal = function(){
		$('#updateDueDays').modal('show');
	}

	$scope.closeModal = function(){
		$('#updateDueDays').modal('hide');
	}


	$scope.errorCount=0;
	$scope.successCount=0;

	$scope.cancelImportButchModel=function () {
		$("#importResultButchModel").modal("hide");
		$scope.loadImgA = false;
		$("#updateDueDays").modal("hide");


	}

	$scope.serviceGrid = {
		data: 'errorlist',
		paginationPageSize:10,                  //分页数量
		paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
		useExternalPagination: true,		    //开启拓展名
		enableHorizontalScrollbar: true,        //横向滚动条
		enableVerticalScrollbar : true,  		//纵向滚动条
		columnDefs: [
			{field: 'sn',displayName: 'SN号',width:200},
			{field: 'errorResult',displayName: '失败原因',width:300}
		]
	};


	var aa = [];
	//上传图片,定义控制器路径
	var uploader = $scope.uploader = new FileUploader({
		url: 'terminalInfo/changeDueDays',
		formData:"",
		queueLimit: 1,   //文件个数
		removeAfterUpload: true,  //上传后删除文件
		headers : {'X-CSRF-TOKEN' : $scope.csrfData}
	});
	//过滤长度，只能上传一个
	uploader.filters.push({
		name: 'isFile',
		fn: function(item, options) {
			return this.queue.length < 1;
		}
	});

	$scope.clearItems = function(){  //重新选择文件时，清空队列，达到覆盖文件的效果
		uploader.clearQueue();
	}

	$scope.loadImgA = false;
	$scope.import=function(){
		SweetAlert.swal({
				title: "确认导入？",
//	            text: "",
//	            type: "warning",
				showCancelButton: true,
				confirmButtonColor: "#DD6B55",
				confirmButtonText: "提交",
				cancelButtonText: "取消",
				closeOnConfirm: true,
				closeOnCancel: true },
			function (isConfirm) {
				if (isConfirm) {
					if ($scope.loadImgA) {
						return;
					}
					$scope.loadImgA = true;
					console.log($scope.loadImgA);
					uploader.uploadAll();//上传
					uploader.onSuccessItem = function(fileItem, response, status, headers) {//上传成功后的回调函数，在里面执行提交
						if(response.result){
							$("#importResultButchModel").modal("show");
							$scope.errorCount=response.errorCount;
							$scope.successCount=response.successCount;
							$scope.errorlist=response.errorlist;
						}else{
							$scope.notice(response.msg);
						}
						$scope.loadImgA = false;
					};
				}
			});
	}


	//页面绑定回车事件
	$document.bind("keypress", function(event) {
		$scope.$apply(function (){
			if(event.keyCode == 13){
				$scope.query(1);
			}
		})
	});

})

