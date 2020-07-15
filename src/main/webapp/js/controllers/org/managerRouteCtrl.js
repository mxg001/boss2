/**
 * 路由集群管理
 */
angular.module('inspinia',['uiSwitch','angularFileUpload']).controller("managerRouteCtrl", function($scope, $http, $state, $stateParams, i18nService,SweetAlert,$document,FileUploader){
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	i18nService.setCurrentLang('zh-cn');
	
	$scope.status = [{text:'正常',value:0},{text:'停用',value:1}];
/*	$scope.statusAll = [{text:'全部',value:''},{text:'正常',value:0},{text:'停用',value:1}];
*/	$scope.routeTypes = [{text:'全部',value:''},{text:'销售专用',value:1},{text:'公司自建',value:2},{text:'技术测试',value:3}];
	$scope.accountsPeriods = [{text:'全部',value:''},{text:'T+1',value:1},{text:'T+0',value:2}];
	$scope.acqServices = [{"id":"","serviceName":"全部"}];
	$scope.reset = function() {
		$scope.info = {routeType:"",serviceType:"",accountsPeriod:"",mySettle:"",agentNo:"",groupProvince:"全部",acqId:"-1",acqServiceId:"",
			createTimeBegin:moment(new Date().getTime()-7*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',createTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD'+' 23:59:59')};
	};
	$scope.reset();
	$scope.queryByService = function() {
        $scope.info.acqServiceId="";
        $scope.acqServices = [{"id":"","serviceName":"全部"}];
		$http.post('routeGroup/acqServiceSelectBox.do',
				angular.toJson({"acqId":$scope.info.acqId})
		).success(function(msg){
			if(msg){
				angular.forEach(msg,function(data,index){
					$scope.acqServices.push({"id":data.id,"serviceName":data.serviceName});
				});
			}
			
		}).error(function(){
		}); 
	}
    $scope.queryByService();
	$scope.query = function() {
		var info = angular.copy($scope.info);
		if(info.groupProvince=='全部'){
			info.groupProvince='';
		}
		$http.post('routeGroup/queryRouteGroupList.do',
       		 "info="+angular.toJson(info)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
       		 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
        ).success(function(data){
            $scope.routeGroupDate = data.result;
			$scope.routeGroupGrid.totalItems = data.totalCount;//总记录数
        }).error(function(){
        }); 
	};
	$scope.query();


	//是否校验时间
	isVerifyTime = 0;//校验：1，不校验：0
	setBeginTime=function(setTime){
		$scope.info.createTimeBegin = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
	}
	setEndTime=function(setTime){
		$scope.info.createTimeEnd = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
	}

	
	$http.post('routeGroup/acqOrgSelectBox.do'
	).success(function(data){
		$scope.acqOrgs = data;
		$scope.acqOrgs.splice(0,0,{"acqName":"全部","id":"-1"});
	}).error(function(){
	}); 
	
	$http.post('routeGroup/oneLevelAgentSelectBox.do'
	).success(function(data){
		$scope.agentInfos = data;
		$scope.agentInfos.splice(0,0,{"agentName":"全部","agentNo":""});
	}).error(function(){
	}); 
	
	$http.post('areaInfo/getAreaByName.do',"type=p&name=''",{headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
	).success(function(data){
		$scope.provinceInfos = data;
		$scope.provinceInfos.splice(0,0,{"name":"全部"});
	}).error(function(){
	}); 
	
	$scope.routeGroupGrid = {
	        data: 'routeGroupDate',
	        paginationPageSize:10,                  //分页数量
	        paginationPageSizes: [10,20,50,100],	//切换每页记录数
	        useExternalPagination: true,		  //开启拓展名
//	        enableHorizontalScrollbar: 0,        //去掉滚动条
//	        enableVerticalScrollbar : 0, 
	        columnDefs: [
                {field: 'id',displayName: '序号',pinnable: false,sortable: false},
                {field: 'groupCode',displayName: '集群编号',pinnable: false,sortable: false},
                {field: 'groupName',displayName: '集群名称',pinnable: false,sortable: false},
                {field: 'createTime',displayName: '创建时间',pinnable: false,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
                {field: 'userName',displayName: '创建人',pinnable: false,sortable: false,width:100},
                {field: 'mySettle',displayName: '是否优质',pinnable: false,sortable: false,cellFilter:"formatDropping:"+angular.toJson($scope.bool),width:100},
               /* {field: 'status',displayName: '状态',width: 180,pinnable: false,sortable: false,
                	cellTemplate:
                		'<span ng-show="grid.appScope.hasPermit(\'managerRoute.switch\')"><switch class="switch switch-s" ng-model="row.entity.status" ng-change="grid.appScope.open(row)" /></span>'
        	            +'<span ng-show="!grid.appScope.hasPermit(\'managerRoute.switch\')"> <span ng-show="row.entity.status==1">开启</span><span ng-show="row.entity.status==0">关闭</span></span>'
                },*/
	            {name: 'action',displayName: '操作',pinnable: true,sortable: false,editable:true,cellTemplate:
	            	"<div class='lh30'><a ng-show='grid.appScope.hasPermit(\"managerRoute.update\")' ui-sref='org.updateRouteGroup({id:row.entity.id})'>修改</a><a ng-show='grid.appScope.hasPermit(\"managerRoute.detail\")' ui-sref='org.routeGroupDetail({id:row.entity.id})'> | 详情</a>"
	            	+"<a ng-show='grid.appScope.hasPermit(\"managerRoute.addMerchant\")' ng-click='grid.appScope.openAddMerchant(row.entity)'> | 增加普通商户</a><a ng-show='grid.appScope.hasPermit(\"managerRoute.addAcqMerchant\")' ng-click='grid.appScope.openAddAcqMerchant(row.entity)'> | 增加收单商户</a>"
	            	+ "<a ng-show='grid.appScope.hasPermit(\"managerRoute.delete\")' ng-click='grid.appScope.openDelModel(row.entity.id,row.entity.groupCode)'> | 删除 </a>" 
	            	+"</div>"
	            			
	            	}
	        ],
	        onRegisterApi: function(gridApi) {   
	        	 $scope.gridApi = gridApi;
	            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
	            	$scope.paginationOptions.pageNo = newPage;
	            	$scope.paginationOptions.pageSize = pageSize;
	            	$scope.query();
	            });
	        }
	};
	
	
	
	
	
	
	//打开删除终端模板
	$scope.openDelModel=function(id,no){
		$scope.no=no;
		$scope.id=id;
		$('#myModal').modal('show');
	}
	
	//提交删除终端模板
	$scope.commitDelModel=function(){
		$http.post('routeGroup/deleteByid',
				"ids="+angular.toJson($scope.id)+"&groupCode="+angular.toJson($scope.no),{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		.success(function(data){
			if(!data.bols){
				$scope.notice(data.msg);

			}else{
				$scope.notice("删除成功");
				for(var i=0;i<$scope.routeGroupDate.length;i++){
					if($scope.routeGroupDate[i].id==$scope.id){
						$scope.routeGroupDate.splice(i,1);
						break;
					}
				}
				$scope.query();
				console.info(data);
				
			}
		})
		$('#myModal').modal('hide');
	}
	
	$scope.open=function(row){
		if(row.entity.status){
			$scope.serviceText = "确定开启？";
		} else {
			$scope.serviceText = "确定关闭？";
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
	            	if(row.entity.status==true){
	            		row.entity.status=1;
	            	} else if(row.entity.status==false){
	            		row.entity.status=0;
	            	}
	            	var data={"status":row.entity.status,"id":row.entity.id};
	                $http.post("routeGroup/updateGroupStatus.do",angular.toJson(data))
	            	.success(function(data){
	            		if(data.status){
	            			$scope.notice("操作成功");
	            		}else{
	            			if(row.entity.status==true){
	    	            		row.entity.status = false;
	    	            	} else {
	    	            		row.entity.status = true;
	    	            	}
	            			$scope.notice("操作失败");
	            		}
	            	})
	            	.error(function(data){
	            		if(row.entity.status==true){
    	            		row.entity.status = false;
    	            	} else {
    	            		row.entity.status = true;
    	            	}
	            		$scope.notice("服务异常")
	            	});
	            } else {
	            	if(row.entity.status==true){
	            		row.entity.status = false;
	            	} else {
	            		row.entity.status = true;
	            	}
	            }
        });
    	
    };
	$scope.serviceTypeAll1=[];
	$scope.openAddMerchant = function(record) {
		$('#addMerchantModal').modal('show');
		$scope.merchantInfo = {groupCode:record.groupCode};
	}
	
	$scope.checkServiceType=function(merId){
		$http.post('merchantInfo/checkServiceType?merId='+merId)
			.success(function(msg){
	            if(msg.status){
	            	$scope.serviceTypeAll1=msg.list;
					$scope.submitting = false;
	            }else{
	            	$scope.notice(msg.msg);
	            }
	        }).error(function(){
				$scope.submitting = false;
	        }); 
		
//		
		
	}
	
	$scope.saveMerchant = function() {
		$scope.submitting = true;
		if(isNaN($scope.merchantInfo.serviceType)){
			$scope.notice("请选择服务");
			$scope.submitting = false;
			return;
		}
		$http.post('routeGroup/addRouteGroupMerchant.do',
       		 angular.toJson($scope.merchantInfo)
        ).success(function(msg){
            if(msg.status){
            	$('#addMerchantModal').modal('hide');
            	$scope.resetModal();
				$scope.submitting = false;
            }
            $scope.notice(msg.msg);
            $scope.submitting = false;
        }).error(function(){
			$scope.submitting = false;
        }); 
	}
	
	$scope.openAddAcqMerchant = function(record) {
		$('#addAcqMerchantModal').modal('show');
		$scope.acqMerchantInfo = {groupCode:record.groupCode};
	}
	$scope.saveAcqMerchant = function() {
		$scope.submitting = true;
		$http.post('routeGroup/addRouteGroupAcqMerchant.do',
       		 angular.toJson($scope.acqMerchantInfo)
        ).success(function(msg){
            if(msg.status){
            	$('#addAcqMerchantModal').modal('show');
            	//$scope.resetModal();
				$scope.submitting =false;
            }
            $scope.notice(msg.msg);
            $scope.submitting =false;
        }).error(function(){
			$scope.submitting =false;
        }); 
	}
	
	//当模态框隐藏时触发
	$('#addMerchantModal').on('hide.bs.modal', function () {
		$scope.resetModal();
	})
	$scope.resetModal = function() {
		$scope.merchantInfo = {};
		$scope.acqMerchantInfo = {};
	}

	$scope.importDiscountShow = function(){
		$('#importDiscount').modal('show');
	}
	$scope.cancel = function(){
		$('#importDiscount').modal('hide');
	}
	//上传图片,定义控制器路径
	var uploader = $scope.uploader = new FileUploader({
		url: 'routeGroup/importDiscount',
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
	//导入
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