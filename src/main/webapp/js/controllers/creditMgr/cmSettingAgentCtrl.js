angular.module('inspinia',['angularFileUpload']).controller('cmSettingAgentCtrl',function(i18nService,$scope,$http,$state,$stateParams,$compile,$filter,SweetAlert,FileUploader){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文

    $scope.myDate  = new Date();
    var date = $filter('date')($scope.myDate,'yyyy-MM-dd');
    $scope.info={startTime:date+" 00:00:00",endTime:date + " 23:59:59",agentNo:"",agentName:"",srcOrgId:"",srcOrgPrduct:""};
    $scope.numCount='0';
    $scope.errorCount='0';

    $scope.clear=function(){
		$scope.info={startTime:date+" 00:00:00",endTime:date + " 23:59:59",agentNo:"",agentName:"",srcOrgId:"",srcOrgPrduct:""};
	}
    $scope.clear();
	
	$scope.paginationOptions=angular.copy($scope.paginationOptions);

    $scope.query = function () {
        if ($scope.loadImg) {
            return;
        }
        $scope.loadImg = true;
        $http.post("cmSetting/selectVipConfigAgent","info="+angular.toJson($scope.info)+"&pageNo="+
            $scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
            $scope.loadImg = false;
            if (!data.status){
                $scope.notice(data.msg);
                $scope.numCount='0';
                return;
            }
            $scope.switchSetTable.data = data.page.result;
            $scope.switchSetTable.totalItems = data.page.totalCount;
            $scope.numCount = data.numCount;
        }).error(function (msg) {
            $scope.notice('服务器异常,请稍后再试.');
            $scope.loadImg = false;
        });
    };
	
	var rowList={};
	var num=0;
	$scope.query();
	$scope.switchSetTable = {
			paginationPageSize: 10,
			paginationPageSizes: [10, 20, 50, 100],
			useExternalPagination: true,		  	//开启拓展名
			columnDefs: [
                {field: 'index',displayName: '序号',pinnable: false,width: 100,sortable: false,cellTemplate: "<span class='checkbox'>{{rowRenderIndex + 1}}</span>"},
                {field: 'agentNo', displayName: '代理商编号',pinnable: false,width: 180,sortable: false},
	            {field: 'agentName', displayName: '代理商名称',pinnable: false,width: 180,sortable: false},
                {field: 'agentLevel', displayName: '代理商级别',pinnable: false,width: 180,sortable: false},
                {field: 'vipFee', displayName: '会员服务费',pinnable: false,width: 180,sortable: false,cellTemplate: "<span>{{row.entity.vipFee}}元</span>"},
                {field: 'validPeriod', displayName: '会员有效期',pinnable: false,width: 180,sortable: false,cellTemplate: "<span>{{row.entity.validPeriod}}天</span>"},
                {field: 'agentShare', displayName: '代理商分润比',pinnable: false,width: 180,sortable: false,cellTemplate: "<span>{{row.entity.agentShare}}%</span>"},
                {field: 'srcOrgPrduct', displayName: '所属组织',pinnable: false,width: 180,sortable: false},
                {field: 'srcOrgId', displayName: '组织ID',pinnable: false,width: 180,sortable: false},
                {field: 'createTime', displayName: '创建时间',pinnable: false,width: 180,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
                {field: 'remark', displayName: '备注',pinnable: false,width: 180,sortable: false},
	            {field: 'id', displayName: '操作',width: 130,pinnedRight:true,sortable: false,cellTemplate:
	            	'<a ng-click="grid.appScope.cmSettingAgentEditModel(row.entity)">修改</a></div>' +
					'<a ng-click="grid.appScope.deleteById(row.entity.agentNo)"> | 删除</a></div>'
		        }
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
		             $scope.selectByParam();
		          });
		      }
	
		};
	 $scope.del=function(){
		 if(num<1){
			 $scope.notice("删除最少选中一条");
			 return;
		 }
		 var list=[];
		 var mm=0;
		 for(index in rowList){
			 list[mm]=rowList[index];
			 mm++;
		 }
		 SweetAlert.swal({
	            title: "确认删除？",
	            showCancelButton: true,
	            confirmButtonColor: "#DD6B55",
	            confirmButtonText: "提交",
	            cancelButtonText: "取消",
	            closeOnConfirm: true,
	            closeOnCancel: true 
	            },
		        function (isConfirm) {
		            if (isConfirm) {
		            	$http.post("cmSetting/deleteCmSettingInfo",angular.toJson(list))
		   	   		 	.success(function(data){
			   	   			 if(data.status){
                                 $scope.notice(data.msg);
                                 $scope.query();
			   	   			 }else{
			   	   				 $scope.notice(data.msg);
			   	   			 }
			   	   			 rowList={};
			   	   			 num=0;
		   	   		 	});
		            }else{
		            	 rowList={};
		   	   			 num=0;
		            }
	        });
	 }
	 //删除单个
	 $scope.deleteById=function(agentNo){
	        SweetAlert.swal({
	            title: "确认删除？",
//	            text: "",
	            type: "warning",
	            showCancelButton: true,
	            confirmButtonColor: "#DD6B55",
	            confirmButtonText: "提交",
	            cancelButtonText: "取消",
	            closeOnConfirm: true,
	            closeOnCancel: true },
		        function (isConfirm) {
		            if (isConfirm) {
		            	$http.post('cmSetting/deleteCmSettingByAgentNo?agentNo='+agentNo)
		   	   		 	.success(function(data){
		            		$scope.notice(data.msg);
		    				$scope.query();
		    			}).error(function(){
		    				$socpe.notice("删除失败");
		    			})
		            }
	        });
	    };



    //新增
    $scope.cmSettingAgentAddModel = function(){
        $("#cmSettingAgentAddModel").modal("show");
        $("#saveButton").show();
        $("#editButton").hide();
        $("#agentNo").removeAttr("disabled");
        $scope.cmVipConfigAgent={};
    }
	//返回
    $scope.cancelCmSettingAgentAddModel=function(){
        $scope.cmVipConfigAgent={};
        $('#cmSettingAgentAddModel').modal('hide');
    }
    //修改
    $scope.cmSettingAgentEditModel = function(entity){
        $http.post("cmSetting/selectVipConfigAgentByAgentNo",entity.agentNo).success(function (data) {
            if(data.status){
                $scope.cmVipConfigAgent = data.param;
                $("#cmSettingAgentAddModel").modal("show");
                $("#saveButton").hide();
                $("#editButton").show();
                $("#agentNo").attr("disabled","disabled");
            }
        })
    }
    //批量导入
    $scope.cmSettingAgentAddButchModel = function(){
        $('#errorDate').hide();
        $("#cmSettingAgentAddButchModel").modal("show");
    }
    //返回
    $scope.cancelCmSettingAgentAddButch=function(){
        $('#cmSettingAgentAddButchModel').modal('hide');
    }

    $scope.selectAgentTeamByAgentNo=function(){
    	if($scope.cmVipConfigAgent.agentNo==undefined|| $scope.cmVipConfigAgent.agentNo.trim().length==0){
            $scope.notice("请输入代理商编号");
            return ;
		}
        $http({
            url: 'agentInfo/selectAgentTeamByAgentNo',
            data: $scope.cmVipConfigAgent.agentNo,
            method:'POST'
        }).success(function (msg) {
            	if(msg.status){
            	    if(msg.result.agent_level!=1){
                        $scope.cmVipConfigAgent.agentNo='';
                        $scope.cmVipConfigAgent.agentName='';
                        $scope.cmVipConfigAgent.agentLevel='';
                        $scope.cmVipConfigAgent.srcOrgPrduct='';
                        $scope.cmVipConfigAgent.srcOrgId='';
                        $scope.notice("该代理商不是一级代理商");
                    }
                    $scope.cmVipConfigAgent.agentNo=msg.result.agent_no;
                    $scope.cmVipConfigAgent.agentName=msg.result.agent_name;
                    $scope.cmVipConfigAgent.agentLevel=msg.result.agent_level;
                    $scope.cmVipConfigAgent.srcOrgPrduct=msg.result.team_name;
                    $scope.cmVipConfigAgent.srcOrgId=msg.result.team_id;
				}else{
                    $scope.cmVipConfigAgent.agentNo='';
                    $scope.cmVipConfigAgent.agentName='';
                    $scope.cmVipConfigAgent.agentLevel='';
                    $scope.cmVipConfigAgent.srcOrgPrduct='';
                    $scope.cmVipConfigAgent.srcOrgId='';
                    $scope.notice(msg.msg);
                    return;
				}
            });
    }


    $scope.saveCmSettingAgent=function(){
        var zzs = /^[0-9]*[1-9][0-9]*$/;//判断正整数
        var zs = /^\d+(?=\.{0,1}\d+$|$)/;//判断正数
        if($scope.cmVipConfigAgent.agentNo==null|| $scope.cmVipConfigAgent.agentNo==''
            ||$scope.cmVipConfigAgent.agentName==null|| $scope.cmVipConfigAgent.agentName==''
            ||$scope.cmVipConfigAgent.agentLevel==null|| $scope.cmVipConfigAgent.agentLevel==''
            ||$scope.cmVipConfigAgent.srcOrgPrduct==null|| $scope.cmVipConfigAgent.srcOrgPrduct==''
            ||$scope.cmVipConfigAgent.srcOrgId==null|| $scope.cmVipConfigAgent.srcOrgId==''){
            $scope.notice("请输入代理商编号");
            return ;
        }
        if (!zs.test($scope.cmVipConfigAgent.vipFee) || $scope.cmVipConfigAgent.vipFee=="0") {
            $scope.notice("会员服务费必须为正数");
            return;
        }
        if (!zzs.test($scope.cmVipConfigAgent.validPeriod)) {
            $scope.notice("有效期必须为正整数");
            return;
        }
        if (!zs.test($scope.cmVipConfigAgent.agentShare)) {
            $scope.notice("代理商分润必须为正数");
            return;
        }
        $http({
            url: 'cmSetting/saveCmSettingAgent',
            data: $scope.cmVipConfigAgent,
            method:'POST'
        }).success(function (msg) {
            if(msg.status){
                $scope.cancelCmSettingAgentAddModel();
                $scope.query();
            }else{
                $scope.notice(msg.msg);
                return;
            }
        });

    }

    $scope.editCmSettingAgent=function(){
        var zzs = /^[0-9]*[1-9][0-9]*$/;//判断正整数
        var zs = /^\d+(?=\.{0,1}\d+$|$)/;//判断正数
        if($scope.cmVipConfigAgent.agentNo==null|| $scope.cmVipConfigAgent.agentNo==''
            ||$scope.cmVipConfigAgent.agentName==null|| $scope.cmVipConfigAgent.agentName==''
            ||$scope.cmVipConfigAgent.agentLevel==null|| $scope.cmVipConfigAgent.agentLevel==''
            ||$scope.cmVipConfigAgent.srcOrgPrduct==null|| $scope.cmVipConfigAgent.srcOrgPrduct==''
            ||$scope.cmVipConfigAgent.srcOrgId==null|| $scope.cmVipConfigAgent.srcOrgId==''){
            $scope.notice("请输入代理商编号");
            return ;
        }
        if (!zs.test($scope.cmVipConfigAgent.vipFee) || $scope.cmVipConfigAgent.vipFee=="0") {
            $scope.notice("会员服务费必须为正数");
            return;
        }
        if (!zzs.test($scope.cmVipConfigAgent.validPeriod)) {
            $scope.notice("有效期必须为正整数");
            return;
        }
        if (!zs.test($scope.cmVipConfigAgent.agentShare)) {
            $scope.notice("代理商分润必须为正数");
            return;
        }
        $http({
            url: 'cmSetting/editCmSettingAgent',
            data: $scope.cmVipConfigAgent,
            method:'POST'
        }).success(function (msg) {
            if(msg.status){
                $scope.cancelCmSettingAgentAddModel();
                $scope.query();
            }else{
                $scope.notice(msg.msg);
                return;
            }
        });

    }


    var uploader = $scope.uploader = new FileUploader({
        url: 'cmSetting/addButchCmSettingAgent',
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

    $scope.submit=function(){
        uploader.uploadAll();//上传
        uploader.onSuccessItem = function(fileItem, response, status, headers) {//上传成功后的回调函数，在里面执行提交
            if(!response.result){
                if(response.errorCount>0){
                    $('#errorDate').show();
                    $scope.errorCount=response.errorCount;
                    $scope.notice(response.msg);
                    $scope.query();
                }else{
                    $scope.notice(response.msg);
                    $('#cmSettingAgentAddButchModel').modal('hide');
                    $scope.query();
                }
            }else{
                $scope.notice(response.msg);
            }
        };
        return false;
    }

});