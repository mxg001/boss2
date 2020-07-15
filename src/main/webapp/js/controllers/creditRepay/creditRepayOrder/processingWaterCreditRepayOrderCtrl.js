/**
 * 还款订单处理流水
 */
angular.module('inspinia').controller('processingWaterCreditRepayOrderCtrl',function($scope,$http,$state,$stateParams,$compile,$uibModal,SweetAlert,$log,i18nService,$document){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.planStatusSelect=[{text:"全部",value:''},{text:'未执行',value:'0'},{text:'执行中',value:'1'},{text:'执行成功',value:'2'},{text:'执行失败',value:'3'}];
    $scope.planTypeSelect=[{text:"给用户还款",value:'IN'},{text:"用户消费",value:'OUT'}];
    $scope.planStatusStr=angular.toJson($scope.planStatusSelect);
    $scope.planTypeStr=angular.toJson($scope.planTypeSelect);

    $scope.planAmountTotal=0;

//    $scope.info={planNo:"",planStatus:"",batchNo:"",merchantNo:"",minPlanAmount:"",maxPlanAmount:"",accountNo:"",
//    		planTimeBegin:moment(new Date().getTime()-6*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
//    		planTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'};
    //清空
    $scope.clear=function(){
    	isVerifyTime = 1;
        $scope.info={planNo:"",planStatus:"",batchNo:"",merchantNo:"",minPlanAmount:"",maxPlanAmount:"",accountNo:"",
        		planTimeBegin:moment(new Date().getTime()-6*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
        		planTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD'+' 23:59:59')};
    }
    $scope.clear();

    $scope.listAcqCode = function () {
        $http.post('repaySettleOrder/listAcqCode')
            .success(function(data){
                if(data.status){
                    $scope.acqCodeSelect = data.acqCodes;
                }else{
                    $scope.acqCodeSelect = [{text:"中茂",value:'ZM'},{text:"合利宝",value:'HLB'},{text:"开放平台",value:'openPay'}];
                }
            });
    }
    $scope.listAcqCode();

    //是否校验时间
    isVerifyTime = 1;//校验：1，不校验：0

    keyChange=function(){
    	if ($scope.info.planNo || $scope.info.batchNo || $scope.info.merchantNo) {
    		isVerifyTime = 0;
    	} else {
    		isVerifyTime = 1;
    	}
    }


    $scope.query=function(){
    	if ($scope.loadImg) {
			return;
		}
        if($scope.info.minPlanAmount!=""
            && $scope.info.maxPlanAmount!=""
            && parseFloat($scope.info.minPlanAmount) > parseFloat($scope.info.maxPlanAmount)){
            $scope.notice("任务金额最小值不能大于最大值");
            return;
        }
        if (!($scope.info.planNo || $scope.info.batchNo || $scope.info.merchantNo)) {
        	if(!($scope.info.planTimeBegin && $scope.info.planTimeEnd)){
    			$scope.notice("计划时间不能为空");
    			return;
    		}
        	var stime = new Date($scope.info.planTimeBegin).getTime();
        	var etime = new Date($scope.info.planTimeEnd).getTime();
        	if ((etime - stime) > (31 * 24 * 60 * 60 * 1000)) {
        		$scope.notice("计划时间范围不能超过31天");
                return;
        	}
    	}
        $scope.loadImg = true;
        $http.post("creditRepayOrder/selectDetail.do","baseInfo="+angular.toJson($scope.info)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(msg){
                if(msg.status){
                    $scope.result = msg.page.result;
                    $scope.gridOptions.totalItems = msg.page.totalCount;
                    if(msg.planAmountTotal != null){
                        $scope.planAmountTotal = msg.planAmountTotal;
                    }
                }else{
                    $scope.notice(msg.msg);
                }
                $scope.loadImg = false;
            });
    }
    //$scope.query();手动查询

    $scope.gridOptions={                           //配置表格
        data: 'result',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'planNo',displayName:'任务流水号',width:180 },
            { field: 'planAmount',displayName:'金额',width:180,cellFilter:"currency:''" },
            { field: 'accountNo',displayName:'还款卡号',width:180},
            { field: 'batchNo',displayName:'来源订单号',width:180 },
            { field: 'merchantNo',displayName:'用户编号',width:180 },
            { field: 'planType',displayName:'类型',cellFilter:"formatDropping:"+$scope.planTypeStr,width:150},
            { field: 'acqCode',displayName:'交易通道',width:180 },
            { field: 'resMsg',displayName:'错误信息',width:180 },
            { field: 'createTime',displayName:'创建时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'planTime',displayName:'计划时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'bak1',displayName:'备注',width:180 }
            //,{ field: 'bak2',displayName:'备注2',width:180 }
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

    $scope.import=function(){
    	if($scope.info.minPlanAmount!=""
            && $scope.info.maxPlanAmount!=""
            && parseFloat($scope.info.minPlanAmount) > parseFloat($scope.info.maxPlanAmount)){
            $scope.notice("任务金额最小值不能大于最大值");
            return;
        }
        if (!($scope.info.planNo || $scope.info.batchNo || $scope.info.merchantNo)) {
        	if(!($scope.info.planTimeBegin && $scope.info.planTimeEnd)){
    			$scope.notice("计划时间不能为空");
    			return;
    		}
        	var stime = new Date($scope.info.planTimeBegin).getTime();
        	var etime = new Date($scope.info.planTimeEnd).getTime();
        	if ((etime - stime) > (31 * 24 * 60 * 60 * 1000)) {
        		$scope.notice("计划时间范围不能超过31天");
                return;
        	}
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
                    location.href="creditRepayOrder/exportDetailAllInfo?baseInfo="+encodeURI(angular.toJson($scope.info));
                }
            });
    }

//	verifyTime=function(type,st,et,day){
////		debugger;
////    	var stime = new Date($scope.info.planTimeBegin).getTime();
////    	var etime = new Date($scope.info.planTimeEnd).getTime();
////    	$(et).val(moment(etime).format("YYYY-MM-DD HH:mm:ss"));
//		var stime = new Date($(st).val()).getTime();
//		var etime = new Date($(et).val()).getTime();
//		if (!stime && !etime) {
//			return;
//		}
//		if (type == 1) { // 填完起始时间
//			if (!etime || (etime - stime) > (day * 24 * 60 * 60 * 1000)) {
//				//结束时间为空，或时间差超过day天，自动填充为day天后，且在当前时间前
//				var setTime = stime + day * 24 * 60 * 60 * 1000;
//				var ntime = new Date().getTime();
//				setTime = setTime > ntime ? ntime : setTime;
//				$scope.info.planTimeEnd = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
//			}
//		} else { // 填完结束时间
//			if (!stime || (etime - stime) > (day * 24 * 60 * 60 * 1000)) {
//				//起始时间为空，或时间差超过day天，自动填充为day天前
//				var setTime = etime - day * 24 * 60 * 60 * 1000;
//				$scope.info.planTimeBegin = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
//				$(st).val(moment(setTime).format("YYYY-MM-DD HH:mm:ss"));
//			}
//		}
//    }

    setBeginTime=function(setTime){
    	$scope.info.planTimeBegin = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
    }

    setEndTime=function(setTime){
    	$scope.info.planTimeEnd = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
    }

    //页面绑定回车事件
    $document.bind("keypress", function(event) {
        $scope.$apply(function (){
            if(event.keyCode == 13){
                $scope.query();
            }
        })
    });
})