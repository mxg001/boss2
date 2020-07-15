/**
 * 机具查询
 */
angular.module('inspinia',['angularFileUpload','infinity.angular-chosen']).controller('terminalQueryCtrl',function(SweetAlert,$document,$scope,$state,$filter,$log,$http,$stateParams,$compile,FileUploader,$uibModal,i18nService,$q,$timeout){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	//下拉框数据源
	$scope.bools=[{text:"全部",value:-1},{text:"是",value:1},{text:"否",value:0}];
	$scope.terminalStates=[{text:"全部",value:-1},{text:"已入库",value:0},{text:"已分配",value:1},{text:"申请中",value:3},{text:"已使用",value:2}];
	$scope.type="-1";

	$scope.agents=[{value:-1,text:"全部"}];
	//代理商
	$http.post("agentInfo/selectAllInfo.do")
		.success(function(msg){
			//响应成功
			for(var i=0; i<msg.length; i++){
				$scope.agents.push({value:msg[i].agentNo,text:msg[i].agentName});
			}
		});
	
	
	//机具通道
	$scope.channelList=[{value:"",text:"全部"}];
	$http.post("sysDict/getListByKey.do?sysKey=JJTD")
	.success(function(data){
		//响应成功
	    for(var i=0; i<data.length; i++){
	    	 $scope.channelList.push({value:data[i].sysValue,text:data[i].sysName});
		}
	});

	//子类型
	$scope.checkActivityCode = function(){
		$scope.typeNos=[{value:"",text:"全部"}];
		$http.post("activity/getActivityTypeNoList").success(function (data) {
			if(data.status){
				for(var i=0; i<data.info.length; i++){
					$scope.typeNos.push({value:data.info[i].activityTypeNo,text:data.info[i].activityTypeName});
				}
			}
		})
	};
	$scope.checkActivityCode();

	// 准备数据时使用的所有promise
	var promises = [];
	
	// 准备
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

	$scope.agentList = [];
	$http.get('agentInfo/selectByLevelOne.do')
	.success(function(result){
		if(result==null || result.size<0){
			return ;
		}
		$scope.agentList = angular.copy(result);
		$scope.agentList.unshift({agentNo:"",agentName:"全部"})
	});
	$scope.hardTypeList = [];
	var hpPromise=$q.defer();
	promises.push(hpPromise.promise);
	$http.get('hardwareProduct/selectAllInfo.do')
	.success(function(result){
		if(!result)
			return;
		$scope.termianlTypes=result;
		$scope.termianlTypes.splice(0,0,{hpId:"-1",typeName:"全部"});
		angular.forEach(result,function(data){
			$scope.hardTypeList.push({value:data.hpId,text:data.typeName});
		});
		hpPromise.resolve();
		delete hpPromise;
	})
	
	$q.all(promises).then(function(){
		// 获取数据完成了
		promises = [];
		initGrid();
	});

	//清空
	$scope.clear=function(){
		$scope.info={channel:"",snStart:"",snEnd:"",merchantName:"",openStatus:-1,type:"-1",psamNo:"",psamNo1:"",bool:-1,hasKey:-1,bpId:"",agentNo:"",activityType:"",activityTypeNo:""};
	}
	$scope.clear();
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	 $scope.gridOptions={                          //配置表格
	 	  paginationPageSize:10,                  //分页数量
          paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
          useExternalPagination: true,                //分页数量
          useExternalSorting: true,
          enableGridMenu: true,
          exporterPdfDefaultStyle : {fontFamily: 'Arial',fontSize:10},
	      onRegisterApi: function(gridApi) {                //选中行配置
	         $scope.gridApi = gridApi;
	         //全选
	         $scope.gridApi.selection.on.rowSelectionChangedBatch($scope,function (rows) {
	            if(rows[0].isSelected){
	               $scope.testRow = rows[0].entity;
	               for(var i=0;i<rows.length;i++){
	            	   rowList[rows[i].entity.id]=rows[i].entity;
	               }
	            }else{
	            	rowList={};
	            }
	         })
	         //单选
	         $scope.gridApi.selection.on.rowSelectionChanged($scope,function (row) {
	            if(row.isSelected){
	               $scope.testRow = row.entity;
	               rowList[row.entity.id]=row.entity;
	            }else{
	            	delete rowList[row.entity.id];
	            }
	         })
	         $scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
		          	$scope.paginationOptions.pageNo = newPage;
		          	$scope.paginationOptions.pageSize = pageSize;
				    $scope.selectInfo();
				 	
		          	rowList={};
		     });
	      }
	};
	function initGrid(){
		 $scope.gridOptions.columnDefs = [                           //表格数据
	         { field: 'id',displayName:'序号',width:100},
	         { field: 'sn',displayName:'SN号',width:150},
	         { field: 'terNo',displayName:'银联终端号',width:150},
	         { field: 'unionMerNo',displayName:'银联报备商户号',width:150},
	         { field: 'type',displayName:'硬件产品种类',width:150,cellFilter:"formatDropping:"+angular.toJson($scope.hardTypeList)},
			 { field: 'merchantNo',displayName:'商户编号',width:150 },
	         { field: 'merchantName',displayName:'商户简称',width:150 },
	         { field: 'bpName',displayName:'业务产品',width:150 },
             { field: 'userCode',displayName:'所属盟主编号',width:150 },
             { field: 'realName',displayName:'所属盟主姓名',width:150 },
	         { field: 'oneAgentName',displayName:'一级代理商',width:150 },
	         { field: 'agentName',displayName:'所属代理商',width:150 },
	         { field: 'terminalId',displayName:'终端号',width:150 },
	         { field: 'openStatus',displayName:'机具状态',width:150,
//	        	 cellFilter:"formatDropping:[{text:'已入库',value:0},{text:'已分配',value:1},{text:'已使用',value:2}]"
	        	 cellFilter:"formatDropping:"+angular.toJson($scope.terminalStates)
	         },
	         { field: 'hasKey',displayName:'是否有密钥',width:150,
	        	 cellFilter:"formatDropping:[{text:'是',value:1},{text:'否',value:null}]"
	         },
	         { field: 'channel',displayName:'机具通道',width:180
	         },
	         { field: 'startTime',displayName:'启用时间',width:180,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'
	         },
	         { field: 'activityTypeName',displayName:'机具活动类型',width:180
	         },
			 { field: 'activityTypeNoName',displayName:'欢乐返子类型',width:150 },
			 { field: 'id',displayName:'操作',pinnedRight:true,width:230,
	        	 cellTemplate:
	        		   '<div class="" style="float:left; margin:8px 0 0 10px;    color: #337ab7;" ng-switch on="$eval(\'row.entity.openStatus\')">'
					 		+'<div ng-switch-when=1>'
					 		//	+'<div ng-switch on="$eval(\'row.entity.agentLevel\')">'
						 		//	+'<div ng-switch-when=1>'
						 			+'<a target="_black" ng-show="grid.appScope.hasPermit(\'terminal.detail\')" ui-sref="terminalQueryDetail({termId:row.entity.id})">详情</a><a ng-show="grid.appScope.hasPermit(\'terminal.recover\')" ng-click="grid.appScope.solution(row.entity.id)"> | 机具回收</a>'
						 		//	+'</div>'
						 			//+'<div ng-switch-default>'
						 			//	+'<a ng-show="grid.appScope.hasPermit(\'terminal.detail\')" ui-sref="terminalQueryDetail({termId:row.entity.id})"> | 详情</a>'
						 			//+'</div>'
					 			//+'</div>'
					 		+'</div>'
					 		+'<div ng-switch-when=2>'
					 			+'<a target="_black" ng-show="grid.appScope.hasPermit(\'terminal.detail\')" ui-sref="terminalQueryDetail({termId:row.entity.id})">详情</a><a ng-show="grid.appScope.hasPermit(\'terminal.unbundling\')" ng-click="grid.appScope.unbundling(row.entity.id)"> | 解绑</a>'
					 		+'</div>'
					 		+'<div ng-switch-when=0>'
					 			+'<a target="_black" ng-show="grid.appScope.hasPermit(\'terminal.detail\')" ui-sref="terminalQueryDetail({termId:row.entity.id})">详情</a><a ng-show="grid.appScope.hasPermit(\'terminal.update\')" ui-sref="terminalUpdate({termId:row.entity.id})"> | 修改</a>'
					 			+'<a ng-show="grid.appScope.hasPermit(\'terminal.issued\')" ng-click="grid.appScope.distributionInfoTerminal(row.entity)"> | 机具下发</a>'
					 		
					 			//+'<a ng-show="grid.appScope.hasPermit(\'terminal.activity\')" ng-click="grid.appScope.updateActivity()"> | 修改机具活动</a>'
					 			
					   +'</div>'
					   +'</div>'
					   +'<div class="" style="display: inline-block;float:left;  margin: 8px 0 0 5px;" ng-show="row.entity.type==\'13\'|| row.entity.type==\'19\' || row.entity.type==\'123\'">'
					 	+	'<a ng-show="grid.appScope.hasPermit(\'terminal.exportCode\')" ng-click="grid.appScope.getActiveCode(row.entity)">  | 激活码</a>'
					 	+'</div>'
	         }
	      ];
	}
	//查看激活码
	$scope.getActiveCode = function(entity){
		$http.get("terminalInfo/getActiveCode?sn="+entity.sn)
			.success(function(msg){
				if(msg.status){
					$("#activeCodeModal").modal("show");
					$scope.activeCodeStr = msg.activeCode;
				} else{
					$scope.notice(msg.msg);
				}
			}).error(function(){
				$scope.notice("服务异常");
			})
	};
	$scope.cancel = function(){
		$scope.clearItems();
		$("#activeCodeModal").modal("hide");
		$("#exportKeyModal").modal("hide");
		
	}
	$scope.activityModelCancel = function(){
		$("#activityModel").modal("hide");
		$scope.snStart="";
		$scope.snEnd="";
		$scope.type="-1";
//		$scope.activityType="";
	}
	//导入密钥
    $scope.exportKey = function(){
    	$("#exportKeyModal").modal("show");
    }
    var aa = [];
	//上传图片,定义控制器路径
    var uploader = $scope.uploader = new FileUploader({
        url: 'terminalInfo/exportKey',
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

     $scope.uploadKey=function(){
    	 $scope.submitting = true;
    	 uploader.uploadAll();//上传
    	 uploader.onSuccessItem = function(msg) {//上传成功后的回调函数，在里面执行提交
		     if(msg.isSuccess){
		    	 $scope.notice("导入成功");
		     }else{
		    	$scope.notice("导入失败");
		     }
    		 $scope.submitting = false;
		 };	
		 return false;
     }
	
	var rowList={};
	
	 //模糊条件查询
	 $scope.selectInfo=function(){
		 if(($scope.info.merchantName==null||$scope.info.merchantName=="")
			 &&($scope.info.agentNo==null||$scope.info.agentNo==""||$scope.info.agentNo=="-1")
			 &&($scope.info.snStart==null||$scope.info.snStart=="")
			 &&($scope.info.snEnd==null||$scope.info.snEnd=="")
             &&($scope.info.userCode==null||$scope.info.userCode=="")){
			 $scope.notice("请选择代理商名称或输入SN号、商户名称中任意一项!");
			 return;
		 }
		 if($scope.loadImg){
			 return;
		 }
		 $scope.loadImg = true;//ng-show,查询的时候置为true，查询完置为false

		 $http.post("terminalInfo/selectByCondition.do",
				 "info="+angular.toJson($scope.info)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
				 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		 .success(function(result){
			 $scope.loadImg = false;
				//响应成功
			 	if(!result.result)
			 		return;
				$scope.gridOptions.data = result.list.result; 
				$scope.gridOptions.totalItems = result.list.totalCount;
		}).error(function(){
			$scope.loadImg = false;
		});
	 }
//	 $scope.selectInfo();
	 
	 //申请回归
	 $scope.applyReturn = function(){
		 var ids = [];
		 angular.forEach($scope.gridApi.selection.getSelectedRows(),function(data){
			 if(data.openStatus=='1'){
				 ids.push(data.id);
			 }
		 })
		 if(ids.length==0){
			 $scope.notice("没有可回归的机具");
			 return;
		 }
		 $http.post('terminalInfo/applyReturn.do',angular.toJson(ids))
		 .success(function(msg){
			 $scope.notice(msg.msg);
		 }).error(function(){
			 $scope.notice("服务异常")
		 })
	 };
	 
	 //机具回收
	 $scope.solution=function(id){
		 var modalScope = $scope.$new();
		 modalScope.id=id;
		 var modalInstance = $uibModal.open({
             templateUrl : 'views/terminal/solutionModal.html',  //指向上面创建的视图
             controller : 'terminalQuerySolutionModalCtrl',// 初始化模态范围
             scope:modalScope
         })
         modalScope.modalInstance=modalInstance;
         modalInstance.result.then(function(selectedItem){  
             $http.post("terminalInfo/solutionById.do",
             		"id="+angular.toJson(id)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
             		{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
    		 .success(function(datas){
    				//响应成功
    			 if(datas.result){
					 $scope.selectInfo();
     				 $scope.notice("操作成功");
    			 }else{
    				 $scope.notice(datas.message);
    			 }
    			 
    		 });
         },function(){
             $log.info('取消: ' + new Date())
         })
		 
	 };

	//解绑
	$scope.unbundling=function(id){
		var modalScope = $scope.$new();
		modalScope.id=id;
		var modalInstance = $uibModal.open({
			templateUrl : 'views/terminal/solutionModal.html',  //指向上面创建的视图
			controller : 'terminalQueryUnbundlingByIdCtrl',// 初始化模态范围
			scope:modalScope
		})
		modalScope.modalInstance=modalInstance;
		modalInstance.result.then(function(selectedItem){  
			$http.post("terminalInfo/unbundlingById.do", "id="+id, {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			.success(function(msg){
				//响应成功
				if(msg.status){
	 				$scope.notice(msg.msg);
	 				$scope.selectInfo();
				}else{
					$scope.notice(msg.msg);
				}
			});
		},function(){
			$log.info('取消: ' + new Date())
		})
	};

	//数组去重的方法
	Array.prototype.unique2 = function(){
		this.sort(); //先排序
		var res = [this[0]];
		for(var i = 1; i < this.length; i++){
			if(this[i] != res[res.length - 1]){
				res.push(this[i]);
			}
		}
		return res;
	}

	//批量解绑
	$scope.batchUnbundling=function(){
		$scope.list = $scope.gridApi.selection.getSelectedRows();
		if ($scope.list == null || $scope.list.length == 0) {
			$scope.notice("没有选中条目");
			return;
		}
		$scope.settleList = [];
		$scope.settleMerchantNameList = [];
		$scope.settleMerchantName = "";
		for (var i = 0; i < $scope.list.length; i++) {
			// 判断选中条目是否正确
			if ($scope.list[i].openStatus != 2) {
				$scope.notice("存在未绑定的机具，请检查选中条目。");
				return;
			}
			$scope.settleList[$scope.settleList.length] = $scope.list[i].id;
			$scope.settleMerchantNameList[$scope.settleMerchantNameList.length] = $scope.list[i].merchantName;
		}
		$scope.settleMerchantNameList = $scope.settleMerchantNameList.unique2();
		for (var i = 0; i < $scope.settleMerchantNameList.length; i++) {	//商户去重
			$scope.settleMerchantName += $scope.settleMerchantNameList[i] + "商户";
			if (i != $scope.settleMerchantNameList.length - 1) {
				$scope.settleMerchantName += ", ";
			}
		}
		SweetAlert.swal({
		title: "",
		text: "是否与 " + $scope.settleMerchantName + " 解除绑定？解绑后机具可被重新分配。已参加欢乐送、欢乐返活动机具不可解绑！",
		showCancelButton: true,
		confirmButtonColor: "#DD6B55",
		confirmButtonText: "确定",
		cancelButtonText: "取消",
		closeOnConfirm: true,
		closeOnCancel: true },
		function (isConfirm) {
			if (isConfirm) {
				$http.post("terminalInfo/batchUnbundlingById.do", "idList="+$scope.settleList,
						{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
				.success(function(msg){
					if(msg.status){
		 				$scope.notice(msg.msg);
		 				$scope.selectInfo();
					}else{
						$scope.notice(msg.msg);
					}
				});
            }
		});
	};

	 //机具单个下发
	 $scope.distributionInfoTerminal=function(entity){
		 var modalScope = $scope.$new();
		 modalScope.agent = [];
		 $http.post("agentInfo/selectByLevelOne.do")
	   	 .success(function(msg){
	   		//响应成功
	   	   	for(var i=0; i<msg.length; i++){
	   	   		modalScope.agent.push({value:msg[i].agentNo,text:msg[i].agentName});
	   	   	}
	   	});
		 modalScope.info={number:1,agentNo:-1};
		 var modalInstance = $uibModal.open({
             animation: true,
             templateUrl: 'adminModalContent.html',
             controller: 'distributionTerminalCtrl',
             size: "lg",
             scope:modalScope
         });
		 modalScope.modalInstance=modalInstance;
		 modalInstance.result.then(function(selectedItem){  
			 var list=[];
			 list[0]=entity;
			 var data={"agentNo":selectedItem.info.agentNo,"list":list};
			 if(selectedItem.info.agentNo!=-1 && selectedItem.info.agentNo!=null){
				 $http.post("terminalInfo/distributionTerminal.do",
				 	"param="+angular.toJson(data)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
        			 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		   		 .success(function(datas){
		   				//响应成功
		   			 if(datas.result){
						 $scope.selectInfo();
	     				 $scope.notice("操作成功");
	    			 }else{
	    				 $scope.notice(datas.msg);
	    			 }
		   			 rowList={};
		   		 });
			 }else{
				 $scope.notice("请选择代理商");
			 }
	     },function(){
	            $log.info('取消: ' + new Date())
	     })
		 
		 
	 }
	 //修改机具的硬件种类及活动
	 $scope.showActivityModel = function(){
		 $scope.snStart="";
		 $scope.snEnd="";
		 $scope.type="-1";
//		 $scope.activityType="";
		 $("#activityModel").modal("show");
	};
	//显示“修改硬件种类及活动”，如果勾选的机具有已使用的“提示不能修改”
	 $scope.updateAllTerActivity = function(){	
		 if($scope.snStart==undefined ||$scope.snEnd== undefined||$scope.snStart==""||$scope.snEnd==""){  
		     $scope.notice("SN为空请输入");  
		     return;
		}  
		 if($scope.type==-1){
			 $scope.notice("请选择硬件产品种类");  
		     return;
		 }
		var data = {		
					"snStart":$scope.snStart,
					"snEnd":$scope.snEnd,
					"type":$scope.type,
					"activityType":$scope.activityType
				};
			 $http.post("terminalInfo/updateAllTerActivity",angular.toJson(data))
				.success(function(data){
					if(data.status){
						$scope.notice(data.msg);
						$("#activityModel").modal("hide");
						 $scope.selectInfo();
					}else{
						$scope.notice(data.msg);
						$scope.submitting = false;
					}
				});
		}
	 
	
	 //机具批量下发
	 $scope.distributionTerminal=function(){
		 var disList=angular.copy(rowList);
		 var num=0;
		 for(index in disList ){
			 if(disList[index].openStatus!="0"){
				 delete disList[disList[index].id];
				 continue;
			 }
			 num++;
		 }
		 if(num!=0){
			 var modalScope = $scope.$new();
			 modalScope.agent = [];
			 $http.post("agentInfo/selectByLevelOne.do")
		   	 .success(function(msg){
		   		//响应成功
		   	   	for(var i=0; i<msg.length; i++){
		   	   		modalScope.agent.push({value:msg[i].agentNo,text:msg[i].agentName});
		   	   	}
		   	});
			 modalScope.info={number:num,agentNo:-1};
			 var modalInstance = $uibModal.open({
	             animation: true,
	             templateUrl: 'adminModalContent.html',
	             controller: 'distributionTerminalCtrl',
	             size: "lg",
	             scope:modalScope
	         });
			 modalScope.modalInstance=modalInstance;
			 modalInstance.result.then(function(selectedItem){  
				 var list=[];
				 var s=0;
				 for(index in disList){
					 if(s==num){
						 break;
					 }else{
						 list[s]=disList[index];
						 s++;
					 }
				 }
				 var data={"agentNo":selectedItem.info.agentNo,"list":list};
				 if(selectedItem.info.agentNo!=-1 && selectedItem.info.agentNo!=null){
					 $http.post("terminalInfo/distributionTerminal.do",
					 	"param="+angular.toJson(data)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
	        			 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			   		 .success(function(datas){
			   				//响应成功
			   			 if(datas.result){
							 $scope.selectInfo();
		     				 $scope.notice(datas.msg);
		    			 }else{
		    				 $scope.notice(datas.msg);
		    			 }
			   			 rowList={};
			   		 });
				 }else{
					 $scope.notice("请选择代理商");
				 }
		     },function(){
		            $log.info('取消: ' + new Date())
		     })
		 }else{
			 $scope.notice("请选择下发的对象");
		 }
	 }
	 var merList=[];
	 var merListBind=[];
	 //绑定
	 $scope.bindingTerminal=function(){
		 
		 var bisList=angular.copy(rowList);
		 var num=0;
		 for(index in bisList ){
			 if(bisList[index].openStatus!="1"){
				 delete bisList[bisList[index].id];
				 continue;
			 }
			 num++;
		 }
		 if(num==0){
			 $scope.notice("请选择绑定的对象");
			 return false;
		 }
		 var list1=[];
		 var s1=0;
		 for(index in bisList){
			 if(s1==num){
				 break;
			 }else{
				 list1[s1]=bisList[index];
				 s1++;
			 }
		 }
		 var mm=0;
		 for(var i=0;i<list1.length;i++){
			 if(list1[0].agentNo!=list1[i].agentNo){
				 mm=1;
			 }
		 }
		
		 if(mm!=0){
			 $scope.notice("请选择相同代理商的机具绑定");
			 return;
		 }else{
			 $http.post("merchantInfo/selectAllInfo.do","agentNo="+angular.toJson(list1[0].agentNo),{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			 .success(function(msg){
				//响应成功
				if(msg.length==0){
					 $scope.notice("当前代理商下没有商户，请先进行商户进件");
					 return;
				}
		    	for(var i=0; i<msg.length; i++){
		    		merList.push({value:msg[i].merchantNo,text:msg[i].merchantNo});
		    		merListBind.push({value:msg[i].merchantNo,text:msg[i].merchantName});
		    	}
			}); 
		 }
		 var modalScope = $scope.$new();
		 modalScope.merLists=merList;
		 modalScope.info={number:num,merId:-1,merBpId:-1};
		 modalScope.merListBinds=merListBind;			 var modalInstance = $uibModal.open({
             animation: true,
             templateUrl: 'adminModalContent1.html',
             controller: 'bindingTerminalCtrl',
             size: "lg",
             scope:modalScope
         });
		 modalScope.modalInstance=modalInstance;
		 modalInstance.result.then(function(selectedItem){  
			 var list=[];
			 var s=0;
			 for(index in bisList){
				 if(s==num){
					 break;
				 }else{
					 list[s]=bisList[index];
					 s++;
				 }
			 }
			 var data={"merNo":selectedItem.info.merId, "bpId": selectedItem.info.merBpId,"list":list};
			 if(selectedItem.info.merId!=-1){
				 $http.post("terminalInfo/bindingTerminal.do",
						 "param="+angular.toJson(data),
	        			 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		   		 .success(function(result){
		   				//响应成功
                     $scope.notice(result.msg);
		   			 if(result.status){
                         $scope.selectInfo();
	    			 }
		   			 rowList={};
		   			 merList=[];
		   		 });
			 }else{
				 $scope.notice("请选择商户,如果没有商户请先进行商户进件");
//					 rowList={};
//		   			 merList=[];
			 }
			 
	     },function(){
//		    	 rowList={};
//	   			 merList=[];
	            $log.info('取消: ' + new Date())
	     })
			 
	 }
	 rowList=rowList;
	 
	//导出
	 $scope.exportExcel=function(){
		 // if($scope.info.startTimeBegin==null||$scope.info.startTimeBegin==""
			//  ||$scope.info.startTimeEnd==null||$scope.info.startTimeEnd==""){
			//  $scope.notice("申请时间范围不能为空!");
			//  return;
		 // }
	        SweetAlert.swal({
	            title: "确认导出？",
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
		            	if($scope.gridOptions.data==null || $scope.gridOptions.data.length==0){
		            		$scope.notice("没有可导出的数据");
		       			 	return;
		       		 	} else {
			       		 	var ids = "";
				       		 angular.forEach($scope.gridOptions.data,function(data,index){
				       			 ids += data.id+",";
				       		 });
				       		 ids =ids.substring(0,ids.length-1)
				       		 location.href="terminalInfo/exportTerminalInfo.do?ids="+ids;
		       		 	}
		            }
	        });
	    };

	//代理商名称模糊查询
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


	$scope.importDiscountShow = function(){
		$('#importDiscount').modal('show');
	};
	$scope.importDiscountcancel = function(){
		$('#importDiscount').modal('hide');
	};
	//上传图片,定义控制器路径
	var uploaderTypeFile = $scope.uploaderTypeFile = new FileUploader({
		url: 'terminalInfo/importDiscount',
		queueLimit: 1,   //文件个数
		removeAfterUpload: true,  //上传后删除文件
		headers : {'X-CSRF-TOKEN' : $scope.csrfData}
	});
	//过滤长度，只能上传一个
	uploaderTypeFile.filters.push({
		name: 'isFile',
		fn: function(item, options) {
			return this.queue.length < 1;
		}
	});
	$scope.clearItems = function(){  //重新选择文件时，清空队列，达到覆盖文件的效果
		uploaderTypeFile.clearQueue();
	};
	$scope.importcommit=false;
	$scope.importResult={};
	$scope.resultSta=false;
	$scope.loadImgA = false;
	//机具产品类型批量导入
	$scope.importDiscount=function(){
		if(uploaderTypeFile.queue.length<=0){
			$scope.notice("上传文件不能为空!");
			return;
		}
		if($scope.importcommit){
			return;
		}
		$scope.loadImgA = true;
		$scope.importcommit=true;
		$scope.importResult={};
		uploaderTypeFile.uploadAll();//上传
		uploaderTypeFile.onSuccessItem = function(fileItem, response, status, headers) {//上传成功后的回调函数，在里面执行提交
			if(response.status){
				$scope.notice(response.msg);
			}else{
				$scope.notice(response.msg);
			}
			$scope.importcommit=false;
			$scope.loadImgA = false;
		};
	};
	//获取异步导入结果
	$scope.getImportResult = function () {
		$http.post("terminalInfo/getImportResult",{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			.success(function(data){
				if(data.status){
					$scope.notice(data.msg);
					$scope.importResult=data.info;
					$scope.resultSta=true;
				}else{
					$scope.notice(data.msg);
				}
			});
	};

	// 下载导入文件
	$scope.downloadResult = function () {
		if($scope.importResult.batchNo==null||$scope.importResult.batchNo==""){
			$scope.notice("无下载结果文件!");
			return;
		}
		location.href = "terminalInfo/downloadResult?batchNo="+$scope.importResult.batchNo;
	};

	// 查询列表导出
	$scope.exportInfo = function () {
		if(($scope.info.merchantName==null||$scope.info.merchantName=="")
			&&($scope.info.agentNo==null||$scope.info.agentNo==""||$scope.info.agentNo=="-1")
			&&($scope.info.snStart==null||$scope.info.snStart=="")
			&&($scope.info.snEnd==null||$scope.info.snEnd=="")
			&&($scope.info.userCode==null||$scope.info.userCode=="")){
			$scope.notice("请选择代理商名称或输入SN号、商户名称中任意一项!");
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
					//location.href = "terminalInfo/importDetail?info=" + encodeURI(angular.toJson($scope.info));
					$scope.exportInfoClick("terminalInfo/importDetail", {"info": angular.toJson($scope.info)});
				}
			});
	};



	//页面绑定回车事件
	$document.bind("keypress", function(event) {
		$scope.$apply(function (){
			if(event.keyCode == 13){
				$scope.selectInfo();
			}
		})
	});
		 
});


angular.module('inspinia').controller('bindingTerminalCtrl',function($scope,$uibModalInstance,$http){
	 $scope.solutionModalClose=function(){
		 $uibModalInstance.dismiss();
	 }
	 
	 $scope.solutionModalOk=function(){
		 if($scope.info.merId == -1){
			 $scope.notice("请选择商户");
			 return;
		 }
		 if($scope.info.merBpId == -1){
			 $scope.notice("请选择业务产品");
			 return;
		 }
		 $uibModalInstance.close($scope);
	 }
	 
	 $scope.change=function(id){
		 for(var i =0;i<$scope.merListBinds.length;i++){
			 if($scope.merListBinds[i].value==id){
				 $scope.merNames=$scope.merListBinds[i].text;
			 }
		 }
	 }
	 $scope.$watch('info.merId', function(newValue, oldValue){
		 $scope.merBPLists = [];
		 $scope.info.merBpId = -1;
		 if(newValue == -1)
			 return;
		 $http.post('terminalInfo/getMbpByMerId','merId='+newValue,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		 .success(function(datas){
			 if(datas && datas.result){
				 var a = [];
				 if(datas.list && datas.list.length){
					 angular.forEach(datas.list, function(data){
						 a.push({value:data.bpId,text:data.bpName || '业务产品'});
					 });
				 }else{
					 $scope.notice("该商户没有业务产品");
				 }
				 $scope.merBPLists = a;
				 $scope.info.merBpId = -1;
			 }else{
				 $scope.notice(datas && datas.msg ? datas.msg : "获取商户业务产品列表失败");
			 }
		 });
	 });
	 
});

angular.module('inspinia').controller('distributionTerminalCtrl',function($scope,$uibModalInstance){
	 $scope.solutionModalClose=function(){
		 $uibModalInstance.dismiss();
	 }
	 
	 $scope.solutionModalOk=function(){
		 $uibModalInstance.close($scope);
	 }
});

angular.module('inspinia').controller('terminalQuerySolutionModalCtrl',function($scope) {
	$scope.title="机具回收";
	$scope.message="确定机具回收吗？";
	 $scope.solutionModalClose=function(){
		 $scope.modalInstance.dismiss();
	 }
	 
	 $scope.solutionModalOk=function(){
		 $scope.modalInstance.close($scope);
	 }
});
angular.module('inspinia').controller('terminalQueryUnbundlingByIdCtrl',function($scope){
	$scope.title="机具解绑";
	$scope.message="确定解除绑定吗？";
	 $scope.solutionModalClose=function(){
		 $scope.modalInstance.dismiss();
	 }
	 
	 $scope.solutionModalOk=function(){
		 $scope.modalInstance.close($scope);
		 $scope.gridApi.selection.clearSelectedRows();//20170223清空选择
	 }
});
