/**
3 * 导入机具
 */
angular.module('inspinia',['angularFileUpload','infinity.angular-chosen']).controller('termianlImportCtrl',function($scope,$state,$http,$stateParams,FileUploader,$window,$timeout,SweetAlert){
	
	$scope.agent=[{text:"请选择",value:""}];
	$scope.typeNos=[{text:"请选择",value:""}];
    $scope.relationTypeList = [{text:"互斥",value:"0"},{text:"同组",value:"1"}];
	$scope.info={};
	//业务产品
	$http.get('hardwareProduct/selectAllInfo.do')
	.success(function(result){
		if(!result)
			return;
		$scope.termianlTypes=result;
        $scope.info.termianlType = result[0].hpId;
	})
	
	//代理商查询 20170223tgh 搜索下拉===
	$http.get('agentInfo/selectByLevelOne.do')
	.success(function(largeLoad) {
		if(!largeLoad){
			return;
		}
		for(var i=0; i<largeLoad.length; i++){
	   		$scope.agent.push({value:largeLoad[i].agentNo,text:largeLoad[i].agentName});
	   	}
	});


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
							$scope.agent = $scope.agentt;
							oldValue = value;
						});
				},800);
		}

	}

    $scope.errorCount=0;
    $scope.successCount=0;

    $scope.cancelImportButchModel=function () {
        $("#importResultButchModel").modal("hide");
        $scope.loadImgA = false;
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
	
	//=====

	var aa = [];
	//上传图片,定义控制器路径
	var opts = {termianlType:"",agentNo:"",activityTypeNo:"",activityType:""};
	if(_parameterName)
		opts[_parameterName] = _token;
    var uploader = $scope.uploader = new FileUploader({
        url: 'terminalInfo/importTerminal',
        formData:[opts],
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
    $scope.commit=function(){
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
                    if(!$scope.info.termianlType){
                        $scope.notice("请选择硬件产品！");
                        return false;
                    }
		       		if($scope.info.agentNo=="" || $scope.info.agentNo==null){
                        $scope.notice("请选择代理商！");
                        return;
                    }
                    if ($scope.loadImgA) {
                        return;
                    }
                    $scope.loadImgA = true;
                    console.log($scope.loadImgA);
                    if($scope.info.activityType == undefined){
                        $scope.info.activityType = "";
                    }
                    if($scope.info.activityTypeNo == undefined){
                        $scope.info.activityTypeNo = "";
                    }

                    // 活动类型
                    var activityTypeStr = "";
                    var needAdd = true;
                    angular.forEach($scope.activityTypeGrid.data,function(item){
                        if(item.selectStatus){
                            //去重
                            var strArr = activityTypeStr.split(',');
                            for(var i=0; i<strArr.length; i++){
                                if(strArr[i]==item.sys_value){
                                    needAdd = false;
                                    break;
                                }
                            }
                            if(needAdd){
                                activityTypeStr = activityTypeStr + item.sys_value + ",";
                            }
                        }
                    });
                    //去掉最后一个逗号
                    if(activityTypeStr.length>0){
                        activityTypeStr=activityTypeStr.substring(0,activityTypeStr.length-1);
                        $scope.info.activityType = activityTypeStr;
                    } else {
                        $scope.info.activityType = null;
                    }
                    uploader.queue[0].formData[0].termianlType=$scope.info.termianlType;
                    uploader.queue[0].formData[0].agentNo=$scope.info.agentNo;
                    uploader.queue[0].formData[0].activityType=$scope.info.activityType;
                    uploader.queue[0].formData[0].activityTypeNo=$scope.info.activityTypeNo;
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

    //获取活动类型（包含群组关系）
    $scope.getAllActivityGroup = function () {
        $http.post('activityGroup/getAllActivityGroup')
            .success(function(msg){
                if(msg.status){
                    $scope.activityTypeGrid.data = msg.activityTypeList;
                } else {
                    $scope.notice(msg.msg);
                }
            }).error(function(){
            $scope.notice("服务异常");
        });
    }
    $scope.getAllActivityGroup();


    $scope.activityTypeGrid = {
        columnDefs: [
            {field: 'selectStatus',displayName: '',width: '30',cellTemplate:
                    '<input type="checkbox" ng-disabled="row.entity.disabledStatus" ng-change="grid.appScope.selectedActivityType(row.entity)" ng-model="row.entity.selectStatus" ng-checked="row.entity.selectStatus"/>'},
            {field: 'sys_name',displayName: '活动类型',width: 180},
            {field: 'group_no',displayName: '群组',width: 180},
            {field: 'relation_type',displayName: '关系',width: 180,cellFilter:"formatDropping:"+angular.toJson($scope.relationTypeList)},
        ]
    };

    //勾选活动类型时
    $scope.selectedActivityType = function(entity){
        if((entity.sys_value==7||entity.sys_value==8)&&entity.selectStatus){
            $scope.isActivityHardware=true;
            if(entity.sys_value==7){
                $scope.info.activityTypeNo="";
                $scope.selectHlfActivityHardware($scope.info.termianlType,'009');
            }else{
                $scope.info.activityTypeNo="";
                $scope.selectHlfActivityHardware($scope.info.termianlType,'008')
            }
        }else if((entity.sys_value==7||entity.sys_value==8)&&!entity.selectStatus){
            $scope.isActivityHardware=false;
            $scope.typeNos = [];
        }
        //传入活动类型
        //$scope.info.activityType = entity.sys_value;
        angular.forEach($scope.activityTypeGrid.data,function(item){
            if(entity.group_no!=null && item.group_no!=null
                && entity.relation_type!=null  && item.relation_type!=null
                && item.group_no==entity.group_no
                && item.group_no==entity.group_no
                && entity.relation_type=='0'
                && item.id!= entity.id){
                if(entity.selectStatus){
                    item.disabledStatus = true;
                } else {
                    item.disabledStatus = false;
                }
            }
            if(entity.group_no!=null  && item.group_no!=null
                && entity.relation_type!=null  && item.relation_type!=null
                && item.group_no==entity.group_no
                && item.group_no==entity.group_no
                && entity.relation_type=='1'
                && item.id!= entity.id){
                if(entity.selectStatus){
                    item.selectStatus = true;
                } else {
                    item.selectStatus = false;
                }
            }
        });
    }


    $scope.selectHlfActivityHardware=function(hardId,activityCode){
        $http.post('activity/selectHBActivityHardwareList?hardId='+hardId + "&activityCode="+activityCode)
            .success(function(result){
                if(result.status){
                    $scope.typeNos=[{text:"请选择",value:""}];
                    var list = result.list;
                    for(var i=0; i<list.length; i++){
                        $scope.typeNos.push({text:list[i].activityTypeName,value:list[i].activityTypeNo});
                    }
                }
            })
            .error(function(){
            });
    }



    $scope.changeClear = function(){
        $scope.isActivityHardware=false;
        $scope.typeNos=[];
        $scope.info.activityTypeNo="";
        angular.forEach($scope.activityTypeGrid.data,function(item){
            item.disabledStatus = false;
            item.selectStatus=false;
        })
    }

});