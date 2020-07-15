/**
 * 机具申领订单
 */
angular.module('inspinia',['infinity.angular-chosen','angularFileUpload']).controller("queryCjtOrder", function($scope, $http, i18nService,$document,FileUploader,SweetAlert,$timeout) {
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    //订单状态 0-待付款  1-待发货  2-已发货  4-已关闭
    //售后申请订单状态 0-待平台处理 1-已处理 2-已取消
    //支付订单状态 0-未支付，1-已提交，2-支付成功，3-支付失败
    //支付方式 支付方式 WX-微信，ZFB-支付宝，KJ-快捷
    $scope.orderStatusList = [{text:"全部", value:""},{text:"待付款", value:"0"},
        {text:"待发货", value:"1"},{text:"已发货", value:"2"},{text:"已关闭", value:"4"}];
    $scope.statusList = [{text:"全部", value:""},{text:"待平台处理", value:"0"},{text:"处理中", value:"1"},
        {text:"已处理", value:"2"},{text:"已取消", value:"3"}];
    $scope.transStatusList = [{text:"全部", value:""},{text:"未支付", value:"0"},
        {text:"已提交", value:"1"},{text:"支付成功", value:"2"},{text:"支付失败", value:"3"}];
    $scope.transTypeList = [{text:"全部", value:""},{text:"微信支付", value:"WX"},
        {text:"支付宝支付", value:"ZFB"},{text:"快捷支付", value:"KJ"}];

    $scope.goodOrderTypeSelect = [{text:"全部",value:null},{text:"付费购买",value:1},{text:"免费申领",value:2}];

    $scope.goodOrderTypeStr = [{text:"无",value:null},{text:"付费购买",value:1},{text:"免费申领",value:2}];

    //清空
    $scope.resetForm = function () {
        $scope.baseInfo = {
            merchantNo:"",orderStatus:"",transType:"",transStatus:"",status:"",acqCode:"",goodOrderType:null,
            createTimeStart:moment(new Date().getTime()-7*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
            createTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'
        };
    }
    $scope.resetForm();
    //查询
    $scope.query = function(){
        $scope.submitting = true;
        $scope.loadImg = true;
        $http({
            url:"cjtOrder/selectPage?pageNo=" + $scope.paginationOptions.pageNo +  "&pageSize=" + $scope.paginationOptions.pageSize,
            method:"post",
            data:$scope.baseInfo
        }).success(function(result){
            $scope.submitting = false;
            $scope.loadImg = false;
            if (!result || !result.status){
                $scope.notice (result.msg);
                return;
            }
            $scope.cjtOrderGrid.data = result.data.page.result;
            $scope.cjtOrderGrid.totalItems = result.data.page.totalCount;
            if($scope.paginationOptions.pageNo == 1) {
                $scope.totalMap = result.data.totalMap;
            }
        }).error(function(){
            $scope.submitting = false;
            $scope.loadImg = false;
            $scope.notice("服务器异常");
        });
    };
    // $scope.query();

    $scope.columnDefs = [
        {field: 'orderNo',width:200,displayName: '订单编号'},
        {field: 'merchantNo',width:200,displayName: '商户编号'},
        {field: 'payOrderNo',width:200,displayName: '支付订单号'},
        {field: 'acqOrderNo',width:200,displayName: '交易订单号'},
        {field: 'mainImgUrl1',width:150,displayName: '商品图片',
            cellTemplate:
            '<a href="{{row.entity.mainImgUrl1}}" fancybox rel="group">' +
            '<img width="140px" height="36px" ng-src="{{row.entity.mainImgUrl1}}"/>' +
            '</a>'},
        {field: 'goodsName',width:150,displayName: '商品标题'},
        {field: 'price',width:150,displayName: '商品销售价(元)'},
        {field: 'num',width:150,displayName: '购买数量'},
        {field: 'totalPrice',width:150,displayName: '订单金额(元)'},
        {field: 'orderStatusStr',width:150,displayName: '订单状态'},
        {field: 'statusStr',width:150,displayName: '售后状态'},
        {field: 'goodOrderType',width:150,displayName: '申购类型',cellFilter:"formatDropping:" + angular.toJson($scope.goodOrderTypeStr)},
        {field: 'receiver',width:150,displayName: '收件人'},
        {field: 'receiverPhone',width:150,displayName: '联系方式'},
        {field: 'receiveAddress',width:150,displayName: '收货地址'},
        {field: 'remark',width:150,displayName: '备注'},
        {field: 'transStatusStr',width:150,displayName: '支付状态'},
        {field: 'transTimeStr',width:150,displayName: '支付日期'},
        {field: 'createTimeStr',width:150,displayName: '下单日期'},
        {field: 'logisticsTimeStr',width:150,displayName: '发货日期'},
        {field: 'action',width:200,displayName: '操作',pinnedRight:true,sortable: false,editable:true,cellTemplate:
            '<a class="lh30" target="_blank" ng-show="row.entity.orderStatus==2&&grid.appScope.hasPermit(\'cjtOrder.shipDetail\')"' +
            ' ui-sref="cjt.shipDetail({orderNo:row.entity.orderNo})"> 发货信息</a>'+
            '<a class="lh30" target="_blank" ng-show="row.entity.orderStatus==1&&grid.appScope.hasPermit(\'cjtOrder.ship\')"' +
            ' ui-sref="cjt.ship({orderNo:row.entity.orderNo})"> 发货</a>'+
            '<a class="lh30" target="_blank" ng-show="grid.appScope.hasPermit(\'cjtOrder.detailCjtOrder\')"' +
            ' ui-sref="cjt.detailCjtOrder({orderNo:row.entity.orderNo})"> 详情</a>'
        }
    ];

    $scope.cjtOrderGrid = {
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs: $scope.columnDefs,
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            $scope.gridApi.pagination.on.paginationChanged ($scope, function(newPage, pageSize) {
                $scope.paginationOptions.pageNo = newPage;
                $scope.paginationOptions.pageSize = pageSize;
                $scope.query();
            });
        }
    };

    $scope.importResultGrid = {
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: false,		  //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs: [
            {field: 'orderNo',width:150,displayName: '订单编号'},
            {field: 'logisticsCompany',width:150,displayName: '快递公司'},
            {field: 'logisticsOrderNo',width:150,displayName: '快递单号'},
            {field: 'remark',displayName: '原因'}
        ]
    };

    // 导出
    $scope.export = function () {
        SweetAlert.swal({
                title: "确定导出吗？",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    // location.href = "cjtOrder/export?baseInfo=" +encodeURI(encodeURI(angular.toJson($scope.baseInfo)));
                    $scope.exportInfoClick("cjtOrder/export",{"baseInfo":angular.toJson($scope.baseInfo)});
                }
            });
    };

    //上传文件,定义控制器路径
    $scope.uploader = new FileUploader({
        url: 'cjtOrder/importShip',
        queueLimit: 1,   //文件个数
        removeAfterUpload: true,  //上传后删除文件
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤格式
    $scope.uploader.filters.push({
        name: 'fileFilter',
        fn: function(item /*{File|FileLikeObject}*/, options) {
            var type = '|' + item.name.slice(item.name.lastIndexOf('.') + 1) + '|';
            return '|xlsx|xls|'.indexOf(type) !== -1;
        }
    });
    $scope.uploader.onAfterAddingFile = function(fileItem) {
        $scope.importResultShow = false;
    }
    $scope.importResultShow = false;
    // 批量导入modal
    $scope.importModal = function(){
        $('#importModal').modal('show');
        $scope.importResultShow = false;
    }

    $scope.cancel = function(){
        $('#importModal').modal('hide');
    }

    //批量导入提交数据
    $scope.importInfo = function(){
        if($scope.uploader.queue.length > 0){
            $scope.uploader.uploadAll();
            $scope.uploader.onSuccessItem = function(fileItem, result, status, headers) {//上传成功后的回调函数，在里面执行提交
                if(result.status) {
                    $scope.importResult = {};
                    $scope.importResultGrid.data = result.data.errorList;
                    $scope.importResult.successNum = result.data.successNum;
                    $scope.importResult.failNum = result.data.failNum;
                    $scope.importResultGrid.data = result.data.errorList;
                    $scope.importResultShow = true;
                } else {
                    $scope.notice(result.msg);
                }
            };
        }
    }

// -----商户名称/编号 下拉框查询 start -----//
	$scope.merchantList = [{text:"全部",value:""}];
	//获取少量的商户
	$http.post("merchantBusinessProduct/getMerchantFew",
		{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		.success(function(msg){
			//响应成功
			for(var i=0; i<msg.length; i++){
				$scope.merchantList.push({value:msg[i].merchantNo,text:msg[i].merchantNo+"("+msg[i].merchantName+")"});
			}
		});

	$scope.getMerchantSelect =getMerchantSelect;
	var oldValueMer="";
	var timeoutMer="";
	function getMerchantSelect(value) {
		$scope.itemList = [];
		var newValueMer=value;
		if(newValueMer != oldValueMer){
			if (timeoutMer) $timeout.cancel(timeoutMer);
			timeoutMer = $timeout(
				function(){
					$http.post('merchantBusinessProduct/getMerchantFew','item='+value,
						{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
						.success(function (msg) {
							if(msg.length==0) {
								$scope.itemList.push({value: "", text:"全部"});
							}else{
								for(var i=0; i<msg.length; i++){
									$scope.itemList.push({value:msg[i].merchantNo,text:msg[i].merchantNo+"("+msg[i].merchantName+")"});
								}
							}
							$scope.merchantList = $scope.itemList;
							oldValueMer = value;
						});
				},800);
		}
	}
    // -----商户名称/编号 下拉框查询 end -----//

    //页面绑定回车事件
    $document.bind ("keypress", function(event) {
        $scope.$apply(function (){
            if(event.keyCode == 13){
                $scope.query();
            }
        })
    });

});