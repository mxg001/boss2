/**
 * 直清商户查询
 */

angular.module('inspinia', ['angularFileUpload', 'infinity.angular-chosen']).controller('zqMerchantQueryCtrl', function ($scope, $http, $state, $timeout, $stateParams, i18nService, SweetAlert, FileUploader) {


    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    var rowList = {};
    //数据源
    $scope.agent = [{text: "全部", value: ""}];
    $scope.tradeType = [{text: "全部", value: ""}, {text: "集群模式", value: "0"}, {text: "直清模式", value: "1"}];
    $scope.syncStatus = [{text: "全部", value: ""}, {text: "初始化", value: "0"}, {text: "同步成功", value: "1"}, {text: "同步失败",value: "2"},{text:"审核中",value:"3"}];
    $scope.zqAcqOrgs = [{text: "全部", value: ""}];
    //清空
    $scope.clear = function () {
    	isVerifyTime = 1;
    	$scope.info = {
			sTime: moment(new Date().getTime() - 6 * 24 * 60 * 60 * 1000).format('YYYY-MM-DD') + ' 00:00:00',
			eTime: moment(new Date().getTime()).format('YYYY-MM-DD'+' 23:59:59'),
			acqOrgMerNo: "",
			cardId: "",
			mbpId: "",
			merchantNo: "",
			agentNo: "",
			productType: "",
			mobilephone: "",
			accountName: "",
			tradeType: "",
			syncStatus: "",
			terminalNo: "",
			unionpayMerNo: "",
			impChannelCode: "",
			channelCode: ""
    	};
    }
    $scope.clear();
    //业务产品
    $http.post('businessProductDefine/selectAllInfo.do')
        .success(function (largeLoad) {
            if (largeLoad) {
                $scope.productTypes = largeLoad;
                $scope.productTypes.splice(0, 0, {bpId: "", bpName: "全部"});
            }
        });
    //代理商
    $http.post("agentInfo/selectAllInfo")
        .success(function (msg) {
            //响应成功
            for (var i = 0; i < msg.length; i++) {
                $scope.agent.push({value: msg[i].agentNo, text: msg[i].agentName});
            }
        });

    $scope.getStates = getStates;
    var oldValue = "";
    var timeout = "";
    function getStates(value) {
        $scope.agentt = [];
        var newValue = value;
        if (newValue != oldValue) {
            if (timeout) $timeout.cancel(timeout);
            timeout = $timeout(
                function () {
                    $http.post('agentInfo/selectAllInfo', 'item=' + value,
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .then(function (response) {
                            if (response.data.length == 0) {
                                $scope.agentt.push({value: "", text: ""});
                            } else {
                                for (var i = 0; i < response.data.length; i++) {
                                    $scope.agentt.push({
                                        value: response.data[i].agentNo,
                                        text: response.data[i].agentName
                                    });
                                }
                            }
                            $scope.agent = $scope.agentt;
                            console.log($scope.agent);
                            oldValue = value;
                        });
                }, 800);
        }
    }

    //获取所有直清收单机构
    $http.post("acqOrgAction/selectAllZqOrg")
        .success(function (msg) {
            //响应成功
            for (var i = 0; i < msg.length; i++) {
                $scope.zqAcqOrgs.push({value: msg[i].acqEnname, text: msg[i].acqName});
            }
        });
    //是否显示同步状态字段
    $scope.changeTradeType = function () {
        var tradeType = $scope.info.tradeType;
        $scope.info.syncStatus = "";
        if ("1" == tradeType) {
            $("#syncLabel").css("display", "block");
            $("#syncDiv").css("display", "block");
        } else {
            $("#syncLabel").css("display", "none");
            $("#syncDiv").css("display", "none");
        }
    };
    //是否校验时间
    isVerifyTime = 1;//校验：1，不校验：0

    keyChange=function(){
    	if ($scope.info.mobilephone || $scope.info.cardId || $scope.info.unionpayMerNo || $scope.info.merchantNo) {
    		isVerifyTime = 0;
    	} else {
    		isVerifyTime = 1;
    	}
    }
    //查询
    $scope.selectInfo = function () {
    	if ($scope.loadImg) {
			return;
		}
        if (!($scope.info.mobilephone || $scope.info.cardId || $scope.info.unionpayMerNo || $scope.info.merchantNo)) {
        	if(!($scope.info.sTime && $scope.info.eTime)){
    			$scope.notice("创建时间不能为空");
    			return;
    		}
        	var stime = new Date($scope.info.sTime).getTime();
        	var etime = new Date($scope.info.eTime).getTime();
        	if ((etime - stime) > (93 * 24 * 60 * 60 * 1000)) {
        		$scope.notice("创建时间范围不能超过三个月");
        		return;
        	}
        }
        $scope.loadImg = true;
        $http.post(
            'zqMerInfoMgr/selectAllZqMerInfo.do',
            "info=" + angular.toJson($scope.info) + "&pageNo=" + $scope.paginationOptions.pageNo + "&pageSize=" + $scope.paginationOptions.pageSize,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
        ).success(function (result) {
            if (result) {
                //响应成功]
                $scope.gridOptions.data = result.result;
                $scope.gridOptions.totalItems = result.totalCount;
            }
            $scope.loadImg = false;
        });
    };

    setBeginTime=function(setTime){
    	$scope.info.sTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
    }

    setEndTime=function(setTime){
    	$scope.info.eTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
    }

    $scope.paginationOptions = angular.copy($scope.paginationOptions);

    $scope.gridOptions = {
        paginationPageSize: 10,                  //分页数量
        paginationPageSizes: [10, 20, 50, 100],	  //切换每页记录数
        useExternalPagination: true,                //分页数量
        useExternalSorting: true,
        enableGridMenu: true,
        exporterPdfDefaultStyle: {fontFamily: 'Arial', fontSize: 10},
        onRegisterApi: function (gridApi) {                //选中行配置
            $scope.gridApi = gridApi;
            //全选
            $scope.gridApi.selection.on.rowSelectionChangedBatch($scope, function (rows) {
                if (rows[0].isSelected) {
                    $scope.testRow = rows[0].entity;
                    for (var i = 0; i < rows.length; i++) {
                        rowList[rows[i].entity.id] = rows[i].entity;
                    }
                } else {
                    rowList = {};
                }
            });
            //单选
            $scope.gridApi.selection.on.rowSelectionChanged($scope, function (row) {
                if (row.isSelected) {
                    $scope.testRow = row.entity;
                    rowList[row.entity.id] = row.entity;
                } else {
                    delete rowList[row.entity.id];
                }
            });
            $scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.paginationOptions.pageNo = newPage;
                $scope.paginationOptions.pageSize = pageSize;
                $scope.selectInfo();

                rowList = {};
            });
        },

        columnDefs: [
            {field: 'unionpayMerNo', displayName: '银联报备商户编号', width: 150},
            {field: 'mbpId', displayName: '商户进件编号', width: 150},
            {field: 'merchantNo', displayName: '商户编号', width: 150},
            {
                field: 'tradeType', displayName: '交易模式', width: 150,
                cellFilter: "formatDropping:[{text:'集群模式',value:0},{text:'直清模式',value:1}]"
            },
            {
                field: 'syncStatus', displayName: '直清同步状态', width: 150,
                cellFilter: "formatDropping:[{text:'初始化',value:0},{text:'同步成功',value:1},{text:'同步失败',value:2},{text:'审核中',value:3},{text:'-',value:'-'}]"
            },
            {field: 'miMobilephone', displayName: '商户手机号', width: 150},
            {field: 'merchantName', displayName: '商户名称', width: 150},
            {field: 'agentName', displayName: '代理商名称', width: 150},
            {field: 'bpName', displayName: '业务产品', width: 150},
            {field: 'channelCode', displayName: '通道名称', width: 150},
            {field: 'syncRemark', displayName: '同步备注', width: 220},
            {field: 'createTime', displayName: '创建时间', width: 150, cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
            {field: 'operatorName', displayName: '创建人', width: 150},
            {
                field: 'id', displayName: '操作', width: 270, pinnedRight: true,
                cellTemplate: '<a ui-sref="merchant.zqMerchantDetail({merServiceId:row.entity.mbpId})" target="_black">详情</a>'
            }
        ]

    };
    //显示直清商户详情
    $scope.showZqMerInfoDetail = function (entity) {
        var id = entity.id;

    };
    //下载导入模板
    $scope.downloadZqMerTemplate = function () {
    	 var impChannelCode = $scope.info.impChannelCode;
         if (impChannelCode == null || impChannelCode == "") {
             $scope.submitting = false;
             $scope.notice("请先选择直清通道");
             return;
         }
         location.href = "zqMerInfoMgr/downloadZqMerTemplate.do?channelCode="+impChannelCode;
    };
    
    //导出信息//打开导出终端模板
    $scope.exportInfo = function () {
    	if (!($scope.info.mobilephone || $scope.info.cardId || $scope.info.unionpayMerNo || $scope.info.merchantNo)) {
        	if(!($scope.info.sTime && $scope.info.eTime)){
    			$scope.notice("创建时间不能为空");
    			return;
    		}
        	var stime = new Date($scope.info.sTime).getTime();
        	var etime = new Date($scope.info.eTime).getTime();
        	if ((etime - stime) > (93 * 24 * 60 * 60 * 1000)) {
        		$scope.notice("创建时间范围不能超过三个月");
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
                    location.href = "zqMerInfoMgr/exportZqMerInfo.do?info=" + angular.toJson($scope.info);
                }
            });
    };
    //上传文件,定义参数
    var opts = {impChannelCode: ""};
    if (_parameterName)
        opts[_parameterName] = _token;
    var uploader = $scope.uploader = new FileUploader({
        url: 'zqMerInfoMgr/impZqMerInfo.do',
        headers: {'X-CSRF-TOKEN': $scope.csrfData},
        formData: [opts],
        queueLimit: 1,   //文件个数
        removeAfterUpload: true  //上传后删除文件
    });
    //过滤长度，只能上传一个
    uploader.filters.push({
        name: 'isFile',
        fn: function (item, options) {
            return this.queue.length < 1;
        }
    });
    uploader.removeFromQueue = function (value) {
        var index = this.getIndexOfItem(value);
        if(index >= 0){
            var item = this.queue[index];
            if (item.isUploading) item.cancel();
            this.queue.splice(index, 1);
            item._destroy();
            this.progress = this._getTotalProgress();
        }
    };

    $scope.clearItems = function () {  //重新选择文件时，清空队列，达到覆盖文件的效果
        uploader.clearQueue();
    };
    //直清商户批量导入模态框
    $scope.openImpZqMerModal = function () {
        $scope.clearItems();
        $scope.info.impChannelCode = "";
        $scope.submitting = false;
        $("#exportZqMerModal").modal("show");
    };
    //关闭直清商户批量导入模态框
    $scope.cancel = function () {
        $scope.clearItems();
        $scope.info.impChannelCode = "";
        $scope.submitting = false;
        $("#exportZqMerModal").modal("hide");

    };
    //提交导入的文件
    $scope.uploadZqMerInfo = function () {
        var impChannelCode = $scope.info.impChannelCode;
        if (impChannelCode == null || impChannelCode == "") {
            $scope.submitting = false;
            $scope.notice("直清通道不能为空");
            return;
        }
        var fileNum = uploader.queue.length;
        if (fileNum < 1) {
            $scope.submitting = false;
            $scope.notice("上传的文件不能为空");
            return;
        }
        $scope.submitting = true;
        uploader.queue[0].formData[0].impChannelCode = $scope.info.impChannelCode;
        uploader.uploadAll();//上传
        uploader.onSuccessItem = function (fileItem, response, status, headers) {//上传成功后的回调函数，在里面执行提交
            if (response.result) {
                var hasWarnData = response.hasWarnData;
                var title, confirmButtonText;
                if ('hasWarnData' == hasWarnData) {
                    title = "导入完成，点击”确认“按钮下载异常记录返回信息";
                    confirmButtonText = "确认";
                } else {
                    title = "";
                    confirmButtonText = "导入完成";
                }
                swal({
                    title: title,
                    text: "",
                    type: "success",
                    showCancelButton: false,
                    closeOnConfirm: true,
                    confirmButtonText: confirmButtonText,
                    confirmButtonColor: "#5CB85C"
                }, function () {
                    if ('hasWarnData' == hasWarnData) {
                        location.href = "zqMerInfoMgr/downHandleRes.do";
                    } else {
                        return;
                    }
                });
            } else {
                swal("", response.msg, "error");
            }
            $scope.cancel();
        };
        return false;
    };

    /**
     * 批量同步
     * @returns {boolean}
     */
    $scope.batchSyncZqMer = function () {
        var disList = angular.copy(rowList);
        var num = 0;
        for (index in disList) {
            if (disList[index].syncStatus == "1") {
                delete disList[disList[index].id];
                continue;
            }
            num++;
        }
        if (num < 1) {
            $scope.notice("请选择同步失败的记录");
            return false;
        }
        var list = [];
        var s = 0;
        for (index in disList) {
            if (s == num) {
                break;
            } else {
            	var obj = disList[index];
            	obj.bpName = null;
                list[s] = obj;
                s++;
            }
        }
       
        var data = {"list": list};
        $http.post("zqMerInfoMgr/batchSyncZqMer.do",
                "param=" + angular.toJson(data), {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function (datas) {
                rowList = {};
                //响应成功
                if (datas.result) {
                    swal("操作完成", datas.resMsg, "success");
                } else {
                    swal("", datas.msg, "error");
                }
            });
    }

});