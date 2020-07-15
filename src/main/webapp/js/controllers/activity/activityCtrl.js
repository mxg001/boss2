/**
 * 欢乐送业务查询
 */
angular.module('inspinia',['angularFileUpload','infinity.angular-chosen']).controller('activityCtrl',function($scope,$http,$state,$stateParams,i18nService,$document,SweetAlert,FileUploader,$timeout){
	//数据源
	i18nService.setCurrentLang('zh-cn');
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	$scope.discountStatus = [{text:'全部',value:-1},{text:'未扣回',value:0},{text:'已扣回',value:1}];
	$scope.disabledMerchantType=true;
	$scope.acqOrgs=[{text:"全部",value:""}];
	$scope.accountCheckTotal=0;
	//reset
	$scope.resetForm=function(){
		$scope.baseInfo = {merchantN:"",agentN:"",countAll:true,pageAll:false,acqOrgId:"",status:"",
				discountStatus:-1,checkStatus:"",merchantType:"1",checkIds:null,
				activeTimeStart:moment(new Date().getTime() - 24 * 3600 * 1000).format('YYYY-MM-DD'+' 00:00:00'),
	            activeTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD'+' 23:59:59'),
            	enterTimeStart:"",
            	enterTimeEnd:""};
		$scope.disabledMerchantType=true;
		isVerifyTime = 1;
	}
	$scope.resetForm();
	//当前页
	$scope.pageAllClick=function () {
		if($scope.baseInfo.pageAll){
			$scope.baseInfo.countAll=false;
		}else{
			$scope.baseInfo.countAll=true;
		}
		$scope.gridApi.selection.clearSelectedRows();
	}
	//所有页
	$scope.countAllClick=function(){
		if($scope.baseInfo.countAll){
			$scope.baseInfo.pageAll=false;
		}else{
			$scope.baseInfo.pageAll=true;
		}
		$scope.gridApi.selection.clearSelectedRows();

	}

	//是否校验时间
	isVerifyTime = 1;//校验：1，不校验：0

	keyChange=function(){
		if ($scope.baseInfo.activeOrder || $scope.baseInfo.merchantN) {
			isVerifyTime = 0;
		} else {
			isVerifyTime = 1;
		}
	}

	setBeginTime=function(setTime){
		$scope.baseInfo.activeTimeStart = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
	}
	setEndTime=function(setTime){
		$scope.baseInfo.activeTimeEnd = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
	}

	//查询
	$scope.query=function(){
		if (!($scope.baseInfo.activeOrder || $scope.baseInfo.merchantN)) {
            if(!($scope.baseInfo.enterTimeStart && $scope.baseInfo.enterTimeEnd)){
                if(!($scope.baseInfo.activeTimeStart && $scope.baseInfo.activeTimeEnd)){
                    $scope.notice("激活日期不能为空");
                    return;
                }

            }

            if(($scope.baseInfo.activeTimeStart && $scope.baseInfo.activeTimeEnd)){
                var stime = new Date($scope.baseInfo.activeTimeStart).getTime();
                var etime = new Date($scope.baseInfo.activeTimeEnd).getTime();
                if ((etime - stime) > (31 * 24 * 60 * 60 * 1000)) {
                    $scope.notice("激活日期范围不能超过31天");
                    return;
                }
            }

            if(($scope.baseInfo.enterTimeStart && $scope.baseInfo.enterTimeEnd)){
                var stime = new Date($scope.baseInfo.enterTimeStart).getTime();
                var etime = new Date($scope.baseInfo.enterTimeEnd).getTime();
                if ((etime - stime) > (31 * 24 * 60 * 60 * 1000)) {
                    $scope.notice("进件日期范围不能超过31天");
                    return;
                }
            }



		}
		$scope.baseInfo.checkIds=null;
		$scope.loadImg = true;
		$http.post('activityDetail/selectActivityDetail.do',"baseInfo="+angular.toJson($scope.baseInfo)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+
				$scope.paginationOptions.pageSize,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
				.success(function(msg){
					$scope.loadImg = false;
					if(!msg)
						return;
					if(msg.status){
						$scope.activityGrid.data =msg.page.result;
						$scope.activityGrid.totalItems = msg.page.totalCount;
						$scope.accountCheckTotal = msg.accountCheckTotal;
					}
				}).error(function(){
			$scope.loadImg = false;
		});
//		
	}
//	$scope.query();
	$scope.activityGrid = {
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs: [
            {field: 'id',displayName: '序号',pinnable: false,width: 180,sortable: false},
            {field: 'activeOrder',displayName: '激活订单号',pinnable: false,width: 180,sortable: false},
            {field: 'cashOrder',displayName: '提现流水号',pinnable: false,width: 180,sortable: false},
            {field: 'activeTime',displayName: '激活时间',pinnable: false,width: 180,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
            {field: 'merchantNo',displayName: '商户编号',pinnable: false,width: 180,sortable: false},
            {field: 'merchantName',displayName: '商户名称',pinnable: false,width: 180,sortable: false},
            {field: 'enterTime',displayName: '进件时间',pinnable: false,width: 180,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
            {field: 'acqEnname',displayName: '收单机构',pinnable: false,width: 180,sortable: false},
            {field: 'transTotal',displayName: '交易累计金额',pinnable: false,width: 180,sortable: false},
            {field: 'merchantFee',displayName: '交易手续费',pinnable: false,width: 180,sortable: false},
            {field: 'frozenAmout',displayName: '冻结金额',pinnable: false,width: 180,sortable: false},
            {field: 'merchantFeeAmount',displayName: '商户提现费',pinnable: false,width: 180,sortable: false},
            {field: 'merchantOutAmount',displayName: '商户到账金额',pinnable: false,width: 180,sortable: false},
            {field: 'settleTransferId',displayName:'出款明细ID',pinnable: false, width:180,sortable: false},
            {field: 'cashTime',displayName: '提现时间',pinnable: false,width: 180,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
            {field: 'status',displayName: '活动状态',pinnable: false,width: 180,sortable: false,cellFilter:"formatDropping:" + angular.toJson($scope.activityStatus)},
            {field: 'targetAmout',displayName: '活动任务金额',pinnable: false,width: 180,sortable: false},
            {field: 'agentName',displayName: '所属代理商名称',pinnable: false,width: 180,sortable: false},
            {field: 'agentNo',displayName: '所属代理商编号',pinnable: false,width: 180,sortable: false},
            {field: 'oneAgentName',displayName: '一级代理商名称',pinnable: false,width: 180,sortable: false},
            {field: 'oneAgentNo',displayName: '一级代理商编号',pinnable: false,width: 180,sortable: false},
            {field: 'checkStatus',displayName: '核算状态',pinnable: false,width: 180,sortable: false,cellFilter:"formatDropping:" + angular.toJson($scope.checkStatus)},
            {field: 'checkOperatorName',displayName: '核算操作人',pinnable: false,width: 180,sortable: false},
            {field: 'checkTime',displayName: '核算操作时间',pinnable: false,width: 180,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
            {field: 'discountStatus',displayName: '是否扣回',pinnable: false,width: 180,sortable: false,cellFilter:"formatDropping:" + angular.toJson($scope.discountStatus)},
            {field: 'discountOperatorName',displayName: '扣回操作人',pinnable: false,width: 180,sortable: false},
            {field: 'discountTime',displayName: '扣回操作时间',pinnable: false,width: 180,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
            {field: 'action',displayName: '操作',pinnedRight:true,width: 180,sortable: false,editable:true,cellTemplate:
	         	'<div class="lh30" ng-show="grid.appScope.hasPermit(\'activity.happySendActivityAdjust\')">' +
				'<span ng-show="row.entity.checkStatus!=1' +
				'&&row.entity.status!=1">' +
				' <a ng-click="grid.appScope.checkModal(row.entity.id)" >核算</a>' +
				'</span></div>'}
	     
        ],
        onRegisterApi: function(gridApi) {                
            $scope.gridApi = gridApi;
            $scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
	          	$scope.paginationOptions.pageNo = newPage;
	          	$scope.paginationOptions.pageSize = pageSize;
	            $scope.query();
			});
			//行选中事件
			$scope.gridApi.selection.on.rowSelectionChanged($scope,function(row,event){
				if(row.isSelected){
					$scope.baseInfo.pageAll=false;
					$scope.baseInfo.countAll=false;
				}
			});
			//全选事件
			$scope.gridApi.selection.on.rowSelectionChangedBatch($scope,function(row,event){
				if(row){
					if(row[0].isSelected){
						$scope.baseInfo.pageAll=false;
						$scope.baseInfo.countAll=false;
					}
				}
			});
        }
	 };
	$scope.showT = false;
	$scope.showTrue = function(){
		$scope.showT = true;
	}

	//收单机构
	$http.post("acqOrgAction/selectBoxAllInfo")
		.success(function(msg){
			//响应成功
			if(msg==null){
				return;
			}
			for(var i=0; i<msg.length; i++){
				$scope.acqOrgs.push({value:msg[i].id,text:msg[i].acqName});
			}
		});

	//代理商名称/编号  改变时调用
	$scope.changeAgentNode = function () {
        $scope.disabledMerchantType = !$scope.baseInfo.agentN;
    };
	//活动状态为未激活时，隐藏激活时间
	$scope.changeActivityStatus = function(){
		if($scope.baseInfo.status==1){
			$scope.activityTimeHide = true;
		} else {
			$scope.activityTimeHide = false;
		}
	}
	//获取代理商
	$http.post("agentInfo/selectAllInfo")
		.success(function(msg){
			//响应成功
			for(var i=0; i<msg.length; i++){
				$scope.agent.push({value:msg[i].agentNode,text:msg[i].agentNo + " " + msg[i].agentName});
				$scope.oneAgent.push({value:msg[i].agentNo,text:msg[i].agentNo + " " + msg[i].agentName});
			}
		});
	//条件查询代理商
	$scope.agent=[{value:"",text:"全部"}];
	$scope.getStates =getStates;
	var oldValue="";
	var timeout="";
	function getStates(value) {
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
									$scope.agentt.push({value: "", text: "全部"});
								}else{
									$scope.agentt.push({value: "", text: "全部"});
									for(var i=0; i<response.data.length; i++){
										$scope.agentt.push({value:response.data[i].agentNode,text:response.data[i].agentNo + " " + response.data[i].agentName});
									}
								}
								$scope.agent = $scope.agentt;
								oldValue = value;
							});
					},800);
			}
	};


	//条件查询一级代理商
	$scope.oneAgent=[{value:"",text:"全部"}];
	$scope.getStatesOne =getStatesOne;
	var oldValueOne="";
	var timeoutOne="";
	function getStatesOne(value) {
		$scope.agenttOne = [];
		var newValueOne=value;
		if(newValueOne != oldValueOne){
			if (timeoutOne) $timeout.cancel(timeoutOne);
			timeoutOne = $timeout(
				function(){
					$http.post('agentInfo/selectAllInfo','item=' + value,
						{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
						.then(function (response) {
							if(response.data.length==0) {
								$scope.agenttOne.push({value: "", text: "全部"});
							}else{
								$scope.agenttOne.push({value: "", text: "全部"});
								for(var i=0; i<response.data.length; i++){
									$scope.agenttOne.push({value:response.data[i].agentNo,text:response.data[i].agentNo + " " + response.data[i].agentName});
								}
							}
							$scope.oneAgent = $scope.agenttOne;
							oldValueOne = value;
						});
				},800);
		}
	};
	//核算
	$scope.checkModal = function(id){
		$scope.checkStatus = 1;
		$scope.baseInfo.checkIds=id;
		$scope.baseInfo.batchOrOne="1";
		$('#checkModal').modal('show');
	}
	//批量核算
	$scope.checkBatch = function(){
		if (!($scope.baseInfo.activeOrder || $scope.baseInfo.merchantN)) {
			if(!($scope.baseInfo.activeTimeStart && $scope.baseInfo.activeTimeEnd)){
				$scope.notice("激活日期不能为空");
				return;
			}
			var stime = new Date($scope.baseInfo.activeTimeStart).getTime();
			var etime = new Date($scope.baseInfo.activeTimeEnd).getTime();
			if ((etime - stime) > (31 * 24 * 60 * 60 * 1000)) {
				$scope.notice("激活日期范围不能超过31天");
				return;
			}
		}
		var ids = "";
		$scope.baseInfo.batchOrOne="2";
		if($scope.baseInfo.countAll||$scope.baseInfo.pageAll){
			$scope.checkStatus = 1;
			$scope.baseInfo.checkIds=null;
			$('#checkModal').modal('show');
		}else{
			var selectList = $scope.gridApi.selection.getSelectedRows();
			if(selectList==null||selectList.length==0){
				$scope.notice("请选择需要核算的数据");
				return false;
			}
			if(selectList!=null&&selectList.length>0){
				for(var i=0;i<selectList.length;i++){
					var item=selectList[i];
					if(item.checkStatus!=1&&item.status!=1) {
						ids = ids + item.id + ",";
					}
				}
			}
			if(ids==""){
				$scope.notice("请选择需要核算的数据");
				return false;
			}
			$scope.checkStatus = 1;
			$scope.baseInfo.checkIds=ids.substring(0,ids.length-1);
			$('#checkModal').modal('show');
		}
	}
	//提交核算
	$scope.submitCheck = function(){
		$scope.submitting = true;
		$http.post('activityDetail/happySendActivityAdjust',"baseInfo="+angular.toJson($scope.baseInfo) + "&pageNo=" + $scope.paginationOptions.pageNo + "&pageSize="+$scope.paginationOptions.pageSize+"&status="+$scope.checkStatus,
				{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		.success(function(msg){
			if(msg.status){
				$scope.notice(msg.msg);
				$scope.cancel();
				$scope.query();
			} else {
				$scope.notice(msg.msg);
			}
			$scope.submitting = false;
		});
	}
	
	//导出
	 $scope.exportExcel=function(){
		if (!($scope.baseInfo.activeOrder || $scope.baseInfo.merchantN)) {
				if(!($scope.baseInfo.activeTimeStart && $scope.baseInfo.activeTimeEnd)){
					$scope.notice("激活日期不能为空");
					return;
				}
				var stime = new Date($scope.baseInfo.activeTimeStart).getTime();
				var etime = new Date($scope.baseInfo.activeTimeEnd).getTime();
				if ((etime - stime) > (31 * 24 * 60 * 60 * 1000)) {
					$scope.notice("激活日期范围不能超过31天");
					return;
				}
			}
	 	if($scope.baseInfo.countAll||$scope.baseInfo.pageAll){
	 		//当前页
	 		if($scope.baseInfo.pageAll){
				$scope.baseInfo.checkIds=null;
				SweetAlert.swal({
						title: "确认导出当前页所有的数据？",
						showCancelButton: true,
						confirmButtonColor: "#DD6B55",
						confirmButtonText: "提交",
						cancelButtonText: "取消",
						closeOnConfirm: true,
						closeOnCancel: true
					},
					function (isConfirm) {
						if (isConfirm) {
							location.href = "activityDetail/exportExcel.do?baseInfo=" + encodeURI(angular.toJson($scope.baseInfo)) + "&pageNo=" + $scope.paginationOptions.pageNo + "&pageSize="  +$scope.paginationOptions.pageSize;
						}
					});
			}
			//全部数据
			if($scope.baseInfo.countAll){
				$scope.baseInfo.checkIds=null;
				SweetAlert.swal({
						title: "确认导出所有的数据？",
						showCancelButton: true,
						confirmButtonColor: "#DD6B55",
						confirmButtonText: "提交",
						cancelButtonText: "取消",
						closeOnConfirm: true,
						closeOnCancel: true
					},
					function (isConfirm) {
						if (isConfirm) {
							location.href = "activityDetail/exportExcel.do?baseInfo=" + encodeURI(angular.toJson($scope.baseInfo)) + "&pageNo=" + $scope.paginationOptions.pageNo + "&pageSize="  +$scope.paginationOptions.pageSize;
						}
					});
			}
		}else {
			var selectList = $scope.gridApi.selection.getSelectedRows();
			if(selectList!=null&&selectList.length>0){
				var ids = "";
				for(var i=0;i<selectList.length;i++){
					if(i<selectList.length-1){
						ids = ids + selectList[i].id + ",";
					}else{
						ids = ids + selectList[i].id;
					}
				}
				$scope.baseInfo.checkIds=ids;
			}
			SweetAlert.swal({
					title: "确认导出选中的数据？",
					showCancelButton: true,
					confirmButtonColor: "#DD6B55",
					confirmButtonText: "提交",
					cancelButtonText: "取消",
					closeOnConfirm: true,
					closeOnCancel: true
				},
				function (isConfirm) {
					if (isConfirm) {
						location.href = "activityDetail/exportExcel.do?baseInfo=" + encodeURI(angular.toJson($scope.baseInfo)) + "&pageNo=" + $scope.paginationOptions.pageNo + "&pageSize=" +$scope.paginationOptions.pageSize;
					}
				});
		}
	 };
	  
	  $scope.importDiscountShow = function(){
		  $('#importDiscount').modal('show');
	  }
	  $scope.cancel = function(){
		  $('#importDiscount').modal('hide');
		  $('#checkModal').modal('hide');
	  }
	  //上传图片,定义控制器路径
	    var uploader = $scope.uploader = new FileUploader({
	        url: 'activityDetail/importDiscount',
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
	    //过滤格式
//	    uploader.filters.push({name : 'fileFilter',fn : function(
//			item /* {File|FileLikeObject} */,options) {
//				var type = '|'+ item.type.slice(item.type.lastIndexOf('/') + 1)+ '|';
//				return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
//			}
//		});
	     $scope.clearItems = function(){  //重新选择文件时，清空队列，达到覆盖文件的效果
	    	 uploader.clearQueue();
	     }
	     //回盘导入
	     $scope.importDiscount=function(){
	    	 $scope.submitting = true;
	    	 uploader.uploadAll();//上传
	    	 uploader.onSuccessItem = function(fileItem, response, status, headers) {//上传成功后的回调函数，在里面执行提交
			     if(response.status){
			    	 $scope.notice(response.msg);
			     }else{
			    	$scope.notice(response.msg);
			     }
			     $scope.submitting = false;
			 };
	     }

	//页面绑定回车事件
	$document.bind("keypress", function(event) {
		$scope.$apply(function (){
			if(event.keyCode == 13){
				$scope.query();
			}
		})
	});
	
});