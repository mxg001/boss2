/**
 * 邀请有奖活动设置
 */
angular.module('inspinia',['uiSwitch', 'infinity.angular-chosen', 'angularFileUpload']).controller('invitePrizesCtrl',function($scope,$http,$state,$stateParams,i18nService,FileUploader,$timeout,SweetAlert){
	i18nService.setCurrentLang('zh-cn');
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	$scope.activityStatusList = [{text:"全部",value:""},{text:"未开始",value:0},{text:"进行中",value:1},
								{text:"已结束",value:2}];
	//页面动作
	$scope.edit = $stateParams.edit;
	
	//清空
	$scope.resetForm = function(){
		$scope.info = {activityStatus:"",agentNo:""};
	}
	$scope.resetForm();
	
	//取消,也就是查询配置数据
	$scope.submitCancel = function(){
		//查询邀请有奖的基本配置
		$http.post('invitePrizes/getInfo')
		.success(function(msg){
			if(msg.status){
				$scope.baseInfo = msg.baseInfo;
				if($scope.baseInfo && $scope.baseInfo.invitePrizesSwitch=="1"){
					$scope.baseInfo.invitePrizesSwitch = 1;
				} else {
					$scope.baseInfo.invitePrizesSwitch = 0;
				}
			} else {
				$scope.notice(msg.msg);
			}
		});
	}
	$scope.submitCancel();
	
//	$http.get('invitePrizes/getAgentListByParam',
//			"baseInfo="+angular.toJson($scope.baseInfo)
//			+"&pageNo="+$scope.paginationOptions.pageNo
//			+"&pageSize="+$scope.paginationOptions.pageSize
//			,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
	
	//查询邀请有奖的代理商配置信息
	$scope.query = function(){
		$scope.loadImg = true;
		$http({
			url: 'invitePrizes/getAgentListByParam?pageNo='+$scope.paginationOptions.pageNo + "&pageSize="+$scope.paginationOptions.pageSize,
			data:$scope.info,
			method:'POST'
		}).success(function(msg){
			$scope.loadImg = false;
			if(msg.status){
				$scope.agentGrid.data = msg.page.result;
				$scope.agentGrid.totalItems = msg.page.totalCount;
			} else {
				$scope.notice(msg.msg);
			}
		}).error(function(){
			$scope.loadImg = false;
			$scope.notice("系统异常");
		});
	}
	$scope.query();

	$scope.agentGrid = {                  //配置表格
		paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
	    columnDefs:[                        //表格数据
	       { field: 'agentNo',width:100,displayName:'代理商编号'},
	       { field: 'agentName',displayName:'代理商名称'},
	       { field: 'startDate',displayName:'活动状态',cellFilter:"formatActivity:row.entity.endDate" },
	       
//	       { field: 'activityStatus',displayName:'活动状态',cellTemplate:
//	    	   '<div class="col-sm-12 checkbox" ng-show="row.entity.action==1">'
//	     	  +' <div ng-show="row.entity.profitType3==1">{{row.entity.perFixIncome3}}'
//	     	  +' </div>'
//	     	  +'</div>'},
	     	 { field: 'activityTime',displayName:'活动时间',cellTemplate:
		    	   '<div class="col-sm-12 checkbox" >'
		     	  +' {{row.entity.startDate | date:"yyyy-MM-dd"}}至{{row.entity.endDate | date:"yyyy-MM-dd"}}'
		     	  +'</div>'},
	       { field: 'action',displayName:'操作',
	      	 cellTemplate:
	      		 '<a  class="lh30" ng-show="grid.appScope.hasPermit(\'invitePrizes.updateAgentActivityDate\') " ng-click="grid.appScope.updateAgentActivityDate(row.entity)">设置时间</a>'
	       }
	    ],
        onRegisterApi: function(gridApi){
        	$scope.gridApi = gridApi;
        	$scope.gridApi.pagination.on.paginationChanged($scope, function(newPage, pageSize){
        		$scope.paginationOptions.pageNo = newPage;
        		$scope.paginationOptions.pageSize = pageSize;
        		$scope.query();
        	});
        }
	};
	//修改代理商的活动日期modal
	$scope.updateAgentActivityDate = function(entity){
		$scope.agentInfo = angular.copy(entity);
		$scope.agentInfo.startDate = formatTimesToDate($scope.agentInfo.startDate);
		$scope.agentInfo.endDate = formatTimesToDate($scope.agentInfo.endDate);
		$('#agentModal').modal('show');
	}
	//提交修改代理商的活动日期
	$scope.submitAgentInfo = function(url, data){
		$http({
			url:url,
			data:data,
			method:"POST"
		}).success(function(msg){
			$scope.notice(msg.msg);
			if(msg.status){
				$scope.cancel();
				$scope.query();
			}
		})
	}
	//新增代理商
	$scope.insertAgent = function(){
		$scope.insertInfo = {};
		$('#insertModal').modal('show');
	}
	//查找代理商
	$scope.getAgent = function(){
		if(!$scope.insertInfo.agentNo){
			return;
		}
		$http({
			url:"invitePrizes/getAgent?agentNo=" + $scope.insertInfo.agentNo + "&agentLevel=1",
			method:"POST"
		}).success(function(data){
			if(data){
				$scope.insertInfo.agentName = data.agentName;
			} else {
				$scope.insertInfo.agentName = "";
				$scope.notice('请输入正确的一级代理商编号');
			}
		});
	}
	
	//批量新增代理商modal
	$scope.insertAgentBatch = function(){
		$('#insertBatchModal').modal('show');
	}
	
	var uploader = $scope.uploader = new FileUploader({
		url: 'invitePrizes/insertAgentBatch',
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
     
	 $scope.submitBatchAgent=function(){
		 $scope.submitting = true;
    	 uploader.uploadAll();//上传
    	 uploader.onSuccessItem = function(fileItem, response, status, headers) {//上传成功后的回调函数，在里面执行提交
    		 $scope.submitting = false;
    		 $scope.notice(response.msg);
		     if(!response.result){
		    	 //上传完成后跳转到哪个页面
		    	 $scope.cancel();
		    	 $scope.query();
		     }
		 };
     }
	
	$scope.cancel = function(){
		$('#agentModal').modal('hide');
		$('#insertModal').modal('hide');
		$('#insertBatchModal').modal('hide');
	}
	//将毫秒数转换为日期
	function formatTimesToDate(times){
		return new moment(times).format('YYYY-MM-DD');
	}
	
	//批量删除
	$scope.deleteBatch = function(){
		var agentNoList = [];
		$scope.selectedAgent = $scope.gridApi.selection.getSelectedRows();
        if(!$scope.selectedAgent || $scope.selectedAgent.length<1){
            $scope.notice('请选择需要删除的数据');
            return;
        }
		var today = new Date();
		var todayDate = + new Date(today.getYear() + 1900, today.getMonth(), today.getDate());
		angular.forEach($scope.selectedAgent,function( data){
			//进行中的不可以删除
	    	if(!(todayDate >= data.startDate && todayDate<= data.endDate)){
                agentNoList[agentNoList.length] = data.agentNo;
	    	}
		});
		if(!agentNoList || agentNoList.length<1){
			$scope.notice('进行中的不能删除');
			return;
		}
		SweetAlert.swal({
	        title: "确认删除",
	        text: "满足删除条件的有" + agentNoList.length + "条",
	        type: "warning",
	        showCancelButton: true,
	        confirmButtonColor: "#DD6B55",
	        confirmButtonText: "提交",
	        cancelButtonText: "取消",
	        closeOnConfirm: true,
	        closeOnCancel: true },
	        function (isConfirm) {
	            if (isConfirm) {
	            	$http({
	            		url:"invitePrizes/deleteBatch",
	            		data:agentNoList,
	            		method:'POST'
	            	})
	            	.success(function(msg){
	            		$scope.notice(msg.msg);
	            		if(msg.status){
	            			$scope.query();
	            		}
	            	}).error(function(){
	            		$scope.notice('服务异常');
	            	})
	            }
	        }
		);
	}
	
	//提交微创业配置信息
	$scope.submit = function(){
		$scope.submitting = true;
		$http({
			url:"invitePrizes/saveInfo",
			data:$scope.baseInfo,
			method:'POST'
		}).success(function(msg){
			$scope.notice(msg.msg);
				if(msg.status){
					$scope.submitCancel();
				}
				$scope.submitting = false;
			}).error(function(){
				$scope.notice('服务异常');
				$scope.submitting = false;
			});
	}
	
	//获取所有的一级代理商
	$scope.oneAgentList=[{value:"",text:"全部"}];
	//代理商
	$scope.agentList=[{value:"",text:"全部"}];
	 $http.post("agentInfo/selectAllOneInfo")
  	 .success(function(msg){
  			//响应成功
  	   	for(var i=0; i<msg.length; i++){
  	   		$scope.oneAgentList.push({value:msg[i].agentNo,text:msg[i].agentNo + " " + msg[i].agentName});
  	   	}
  	});
	//异步获取一级代理商
		var oneOldValue="";
		var oneTimeout="";
		$scope.getOneAgentList = function(value) {
			$scope.agentt = [];
				var newValue=value;
				if(newValue != oneOldValue){
					if (oneTimeout) $timeout.cancel(oneTimeout);
					oneTimeout = $timeout(
						function(){
							$http.post('agentInfo/selectAllOneInfo','item=' + value,
									{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
								.then(function (response) {
									if(response.data.length==0) {
										$scope.agentt.push({value: "", text: "全部"});
									}else{
										$scope.agentt.push({value: "", text: "全部"});
										for(var i=0; i<response.data.length; i++){
											$scope.agentt.push({value:response.data[i].agentNo,text:response.data[i].agentNo + " " + response.data[i].agentName});
										}
									}
									$scope.oneAgentList = $scope.agentt;
									oneOldValue = value;
								});
						},800);
				}
		};
	
	
}).filter("formatActivity",function(){
    return function(startDate, endDate){
    	var today = new Date();
    	var todayDate = + new Date(today.getYear() + 1900, today.getMonth(), today.getDate());
    	if(todayDate < startDate){
    		return "未开始";
    	}
    	if(todayDate >= startDate && todayDate<= endDate){
    		return "进行中";
    	}
    	if(todayDate > endDate){
    		return "已结束";
    	}
    }});