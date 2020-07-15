/**
 * 商户冻结查询
 */

angular.module('inspinia', ['infinity.angular-chosen']).controller('merPreFrozenCtrl',function($scope,$http,$state,$timeout,$stateParams,$compile,$filter,i18nService,SweetAlert,$document){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	//数据源
    $scope.merStatusList=[{text:"全部",value:""},{text:"正常",value:"1"},{text:"关闭",value:"0"}];
    $scope.queryModeList=[{text:"预冻结",value:"0"},{text:"冻结/解冻",value:"1"}];
    $scope.riskStatusaa=[{text:"全部",value:""},{text:"正常",value:1},{text:"不进不出",value:3},{text:"只进不出",value:2}]
    $scope.riskStatusList=[{text:"全部",value:""},{text:"正常",value:"1"},{text:"只进不出",value:"2"},{text:"不进不出",value:"3"}];
	$scope.info={sTime:moment(new Date().getTime()-6*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',eTime:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',riskStatus:"",queryMode:"0",mobilephone:"",merchantNo:"",merStatus:""};

	//查询
	$scope.selectInfo=function(){
		if($scope.loadImg){
			return;
		}
        if($scope.info.mobilephone=="" && ($scope.info.eTime=="" || $scope.info.sTime=="") && $scope.info.merchantNo=="" ){
            $scope.notice("查询条件中商户编号、手机号、操作时间必填一项");
            return;
        }
        if($scope.info.eTime!="" && $scope.info.eTime!="") {
            if ($scope.info.sTime > $scope.info.eTime) {
                $scope.notice("起始时间不能大于结束时间");
                return;
            }
        }
		$scope.loadImg = true;
		$http.post(
				'merchantPreFrozenAction/selectAllInfo.do',
				"info="+angular.toJson($scope.info)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
				{headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
		).success(function(data){
                if(data.bols){
                	$scope.gridOptions.totalItems = data.page.totalCount;
				    $scope.gridOptions.data = data.page.result;
                }else{
				$scope.notice(data.msg);
			}
			$scope.loadImg = false;
		}).error(function(result){
			$scope.loadImg = false;
			$scope.notice("系统异常!");
		});
	}
	//清空
	$scope.clear=function(){
		$scope.info={sTime:moment(new Date().getTime()-6*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',eTime:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',riskStatus:"",queryMode:"0",mobilephone:"",merchantNo:"",merStatus:""};
	}
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	$scope.gridOptions={                           //配置表格
		paginationPageSize:10,                  //分页数量
		paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
		useExternalPagination: true,
		enableHorizontalScrollbar: 1,        //横向滚动条
		enableVerticalScrollbar : 1,  		//纵向滚动条
		columnDefs:[                           //表格数据
			{ field: 'merchantNo',displayName:'商户编号',width:150},
            { field: 'merchantName',displayName:'商户名称' ,width:150},
            { field: 'mobilephone',displayName:'手机号',width:150},
            { field: 'operMoney',displayName:'操作金额',cellFilter:"currency:''",width:150 },
            { field: 'operTime',displayName:'操作时间',width:150,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'
            },
            { field: 'operName',displayName:'操作人',width:150},
            { field: 'operType',displayName:'操作类型',width:150,
                cellFilter:"formatDropping:[{text:'冻结',value:0},{text:'解冻',value:1},{text:'预冻结',value:2}]"
                //cellFilter:"formatDropping:[{text:'冻结',value:0},{text:'解冻',value:1},{text:'预冻结',value:2},{text:'冻结/解冻',value:3}]"
            },
            { field: 'operLog',displayName:'操作日志',width:150},
			{ field: 'orderNo',displayName:'商户订单号',width:150 },
			{ field: 'operReason',displayName:'备注/原因',width:150 },
            { field: 'merStatus',displayName:'商户状态',width:150,
                cellFilter:"formatDropping:[{text:'关闭',value:0},{text:'正常',value:1}]"
            },
			{ field: 'riskStatus',displayName:'商户冻结状态',width:150,
				cellFilter:"formatDropping:[{text:'正常',value:1},{text:'只进不出',value:2},{text:'不进不出',value:3}]"
			},
		],
		onRegisterApi: function(gridApi) {
			$scope.gridApi = gridApi;
			gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
				$scope.paginationOptions.pageNo = newPage;
				$scope.paginationOptions.pageSize = pageSize;
				$scope.selectInfo();
			});
		}
	};

    //导出信息//打开导出终端模板
    $scope.exportExcel=function(){
        if($scope.loadImg){
            return;
        }
        SweetAlert.swal({
                title: "确认导出？",
//		            text: "",
//		            type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true
            },
            function (isConfirm) {
                if (isConfirm) {
                    location.href="merchantPreFrozenAction/exportPreFrozenInfo?info="+encodeURI(angular.toJson($scope.info));
                }
            });
    }


	//页面绑定回车事件
	$document.bind("keypress", function(event) {
		$scope.$apply(function (){
			if(event.keyCode == 13){
				$scope.selectInfo();
			}
		})
	});

})