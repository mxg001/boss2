/**
 * APP二维码
 */
angular.module('inspinia').controller('managerSmsCtrl',function($scope,$http,$state,$stateParams,$compile,$filter,i18nService,SweetAlert,$document){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文

    $scope.statusType = [{text:"开启",value:1},{text:"关闭",value:0}];
	$scope.info={serviceName:"",channelCode:""};
	$scope.info2={serviceName:"",channelCode:""};
    $scope.serviceTypes=[{text:"全部",value:""}];
    $scope.smsChannels=[{text:"全部",value:""}];
    $scope.smsChannels2=[{text:"请选择",value:""}];
    var isFirstSelect=false;
	//查询
	$scope.selectServiceInfo=function(){
		$http.post(
				'managerSms/getServiceList',
				 "info="+angular.toJson($scope.info)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
				 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
			).success(function(result){
				//响应成功
				$scope.serviceInfoOptions.data = result.page.result;
				$scope.serviceInfoOptions.totalItems = result.page.totalCount;


            if(!isFirstSelect){
                isFirstSelect=true;
                var serviceData=$scope.serviceInfoOptions.data;
                for(var i=0; i<serviceData.length; i++){
                    $scope.serviceTypes.push({value:serviceData[i].service_name,text:serviceData[i].service_zh_name});
                }
            }



			});
	}


    //查询
    $scope.selectChannelInfo=function(){
        $http.post(
            'managerSms/getSmsChannelList',
            "info=&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
        ).success(function(result){
            //响应成功
            $scope.smsChannels=[{text:"全部",value:""}];
            $scope.smsChannels2=[{text:"请选择",value:""}];
            $scope.channelInfoOptions.data = result.page.result;
            $scope.channelInfoOptions.totalItems = result.page.totalCount;
            var channelData=$scope.channelInfoOptions.data;

            for(var i=0; i<channelData.length; i++){
                $scope.smsChannels.push({value:channelData[i].channel_code,text:channelData[i].channel_name});
                if(channelData[i].channel_status==1){
                    $scope.smsChannels2.push({value:channelData[i].channel_code,text:channelData[i].channel_name});
                }

            }
        });
    }

	$scope.selectServiceInfo();
	$scope.selectChannelInfo();
	//清空
	$scope.clear=function(){
		$scope.info={serviceName:"",channelCode:""};
	}
	
	$scope.paginationOptions=angular.copy($scope.paginationOptions);

    var rowList=[];
    var num=0;
	$scope.serviceInfoOptions={                           //配置表格
		      paginationPageSize:100,                  //分页数量
		      paginationPageSizes: [100],	  //切换每页记录数
		      useExternalPagination: true,
		      columnDefs:[                           //表格数据
		         { field: 'service_zh_name',displayName:'服务类型'},
		         { field: 'channel_name',displayName:'短信通道'},
		         { field: 'id',displayName:'操作',
		        	 cellTemplate:'<a class="lh30" ng-click="grid.appScope.oneChangeChannelModal(row.entity)">切换通道</a>'
		         }
		      ],
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            //全选
            $scope.gridApi.selection.on.rowSelectionChangedBatch($scope,function (rows) {
                if(rows[0].isSelected){
                    $scope.testRow = rows[0].entity;
                    for(var i=0;i<rows.length;i++){
                        rowList.push(rows[i].entity);
                        num++;
                    }
                }else{
                    rowList=[];
                    num=0;
                }
            })
            //单选
            $scope.gridApi.selection.on.rowSelectionChanged($scope,function (row) {
                if(row.isSelected){
                    $scope.testRow = row.entity;
                    rowList.push(row.entity);
                    num++;
                }else{
                    var index = rowList.indexOf(row.entity);
                    if (index > -1) {
                        rowList.splice(index, 1);
                    }
                    num--;
                    if(num<0){
                        num=0;
                    }
                }
            })
        }

    };


    $scope.channelInfoOptions={                           //配置表格
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
        useExternalPagination: true,
        columnDefs:[                           //表格数据
            { field: 'channel_name',displayName:'通道名称'},
            { field: 'channel_status',displayName:'通道状态', cellFilter:"formatDropping:"+ angular.toJson($scope.statusType)},
            { field: 'id',displayName:'操作',
                cellTemplate:'<a class="lh30" ng-show="row.entity.channel_status==0" ng-click="grid.appScope.openChannel(row.entity.channel_code)">开启通道</a>'+'<a class="lh30" ng-show="row.entity.channel_status==1" ng-click="grid.appScope.closeChannel(row.entity.channel_code)">关闭通道</a>'
            }
        ],
    };

    $scope.openChannel=function(channelCode){
        SweetAlert.swal({
                title: "确认开启通道？",
//            text: "",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post("managerSms/changeChannelStatus","channelCode="+channelCode+"&opCode=able",{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function(msg){
                            $scope.notice(msg.msg);
                            $scope.selectChannelInfo();
                        }).error(function(){
                        $scope.notice("操作失败");
                    });
                }
            });
    };
    $scope.closeChannel=function(channelCode){
        SweetAlert.swal({
                title: "确认关闭通道？",
//            text: "",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post("managerSms/changeChannelStatus","channelCode="+channelCode+"&opCode=disable",{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function(msg){
                            $scope.notice(msg.msg);
                            $scope.selectChannelInfo();
                        }).error(function(){
                        $scope.notice("操作失败");
                    });
                }
            });
    };

    $scope.oneChangeChannelModal = function (entity) {
        rowList=[];
        num=1;
        rowList.push(entity);
        $scope.openChangeChannelModal();
    }

    //切换通道模态框
    $scope.openChangeChannelModal = function () {
        if(num<=0){
            $scope.notice("请勾选要切换通道的服务类型");
            return;
        }

        $scope.info2.serviceName = "";
        $scope.info2.channelCode =$scope.smsChannels2[0].value;
        $scope.submitting = false;
        $("#changeChannelModal").modal("show");
    };
    //关闭切换通道模态框
    $scope.cancel = function () {

        $scope.info2.serviceName = "";
        $scope.info2.channelCode = "";
        $scope.submitting = false;
        $("#changeChannelModal").modal("hide");

    };
    $scope.toChangeChannel = function () {
        if($scope.info2.channelCode==""){
            $scope.notice("请选择要切换的通道");
            return;
        }
        var serviceNames="";
        for(var i in rowList){
            serviceNames=serviceNames+rowList[i].service_name+",";
        }
        serviceNames=serviceNames.substring(0,serviceNames.length-1);
        $http.post("managerSms/changeServiceChannel","serviceName="+serviceNames+"&channelCode="+$scope.info2.channelCode,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(msg){
                $scope.notice(msg.msg);
                $scope.selectServiceInfo();
                num=0;
                rowList=[];
                $("#changeChannelModal").modal("hide");
            }).error(function(){
            $scope.notice("操作失败");
        });
    };


// 导出
    $scope.exportInfo = function () {
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
                    location.href = "managerSms/importDetail?info=" + encodeURI(angular.toJson($scope.info));
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
})