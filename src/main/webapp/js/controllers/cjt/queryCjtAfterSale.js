/**
 * 售后订单查询
 */
angular.module('inspinia',['angularFileUpload']).controller("queryCjtAfterSale", function($scope, $http, i18nService,$document,FileUploader,SweetAlert) {
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    // 售后申请订单状态 0-待平台处理1-售后中 2-已处理 3-已取消
    $scope.statusList = [{text:"待平台处理",value:"0"},{text:"售后中",value:"1"},
        {text:"已处理",value:"2"},{text:"已取消",value:"3"}];
    $scope.statusListAll = angular.copy($scope.statusList);
    $scope.statusListAll.unshift({text:"全部",value:null});

    //清空
    $scope.resetForm = function () {
        $scope.baseInfo = {
            status:null,afterSaleType:null,
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
            url:"cjtAfterSale/selectPage?pageNo=" + $scope.paginationOptions.pageNo +  "&pageSize=" + $scope.paginationOptions.pageSize,
            method:"post",
            data:$scope.baseInfo
        }).success(function(result){
            $scope.submitting = false;
            $scope.loadImg = false;
            if (!result || !result.status){
                $scope.notice (result.msg);
                return;
            }
            $scope.cjtAfterSaleGrid.data = result.data.page.result;
            $scope.cjtAfterSaleGrid.totalItems = result.data.page.totalCount;
            $scope.totalMap = result.data.totalMap;
        }).error(function(){
            $scope.submitting = false;
            $scope.loadImg = false;
            $scope.notice("服务器异常");
        });
    };
    // $scope.query();

    $scope.columnDefs = [
                    {field: 'orderNo',width:180,displayName: '售后编号'},
                    {field: 'serviceOrderNo',width:180,displayName: '关联订单编号',cellTemplate:
                        '<a class="lh30" target="_blank" ui-sref="cjt.detailCjtOrder({orderNo:row.entity.serviceOrderNo})">' +
                        '{{row.entity.serviceOrderNo}}</a>'
                    },
                    {field: 'afterSaleTypeStr',width:150,displayName: '售后类型'},
                    {field: 'applyRemark',width:150,displayName: '售后说明'},
                    {field: 'applyImg',width:150,displayName: '图片凭证1',
                        cellTemplate:
                        '<a href="{{row.entity.applyImgUrl1}}" fancybox rel="group" ng-show="{{row.entity.applyImgUrl1}}">'
                        +'<img width="140px" height="36px" ng-src="{{row.entity.applyImgUrl1}}"/>'
                        +'</a>'},
                    {field: 'applyImg',width:150,displayName: '图片凭证2',
                        cellTemplate:
                        '<a href="{{row.entity.applyImgUrl2}}" fancybox rel="group" ng-show="{{row.entity.applyImgUrl2}}">'
                        +'<img width="140px" height="36px" ng-src="{{row.entity.applyImgUrl2}}"/>'
                        +'</a>'},
                    {field: 'applyImg',width:150,displayName: '图片凭证3',
                        cellTemplate:
                        '<a href="{{row.entity.applyImgUrl3}}" fancybox rel="group" ng-show="{{row.entity.applyImgUrl3}}">'
                        +'<img width="140px" height="36px" ng-src="{{row.entity.applyImgUrl3}}"/>'
                        +'</a>'},
                    {field: 'statusStr',width:150,displayName: '售后状态'},
                    {field: 'dealRemark',width:150,displayName: '平台处理结果'},
                    {field: 'dealImg',width:180,displayName: '平台处理图片凭证1',
                        cellTemplate:
                        '<a href="{{row.entity.dealImgUrl1}}" fancybox rel="group" ng-show="{{row.entity.dealImgUrl1}}">'
                        +'<img width="140px" height="36px" ng-src="{{row.entity.dealImgUrl1}}"/>'
                        +'</a>'},
                    {field: 'dealImg',width:180,displayName: '平台处理图片凭证2',
                        cellTemplate:
                        '<a href="{{row.entity.dealImgUrl2}}" fancybox rel="group" ng-show="{{row.entity.dealImgUrl2}}">'
                        +'<img width="140px" height="36px" ng-src="{{row.entity.dealImgUrl2}}"/>'
                        +'</a>'},
                    {field: 'dealImg',width:180,displayName: '平台处理图片凭证3',
                        cellTemplate:
                        '<a href="{{row.entity.dealImgUrl3}}" fancybox rel="group" ng-show="{{row.entity.dealImgUrl3}}">'
                        +'<img width="140px" height="36px" ng-src="{{row.entity.dealImgUrl3}}"/>'
                        +'</a>'},
                    {field: 'createTimeStr',width:150,displayName: '提交日期'},
                    {field: 'dealPerson',width:150,displayName: '处理人'},
                    {field: 'dealTimeStr',width:150,displayName: '处理日期'},
                    {field: 'action',width:150,displayName: '操作',pinnedRight:true,sortable: false,editable:true,cellTemplate:
                        '<a class="lh30" ng-show="row.entity.status==0&&grid.appScope.hasPermit(\'cjtAfterSale.updateStatus\')" '
                        + ' ng-click="grid.appScope.updateStatus(row.entity.orderNo)">立即处理</a>'
                        + '<a class="lh30" ng-show="row.entity.status==1&&grid.appScope.hasPermit(\'cjtAfterSale.deal\')" '
                        + ' ng-click="grid.appScope.dealModal(row.entity.orderNo)">继续处理</a>'
                        }
    ];

    $scope.cjtAfterSaleGrid = {
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
                    // location.href = "cjtAfterSale/export?baseInfo=" +encodeURI(encodeURI(angular.toJson($scope.baseInfo)));
                    $scope.exportInfoClick("cjtAfterSale/export",{"baseInfo":angular.toJson($scope.baseInfo)});
                }
            });
    };

    $scope.updateStatus = function(orderNo){
        $http({
            method: "post",
            url: "cjtAfterSale/updateStatus?orderNo=" + orderNo
        }).success(function(result){
           if(result.status) {
               $scope.dealModal(orderNo);
           } else {
               $scope.notice(result.msg);
           }
        });
    }

    $scope.dealModal = function(orderNo) {
        $scope.dealInfo = {orderNo: orderNo};
        $scope.uploaderImgList.queue = [];
        $('#dealModal').modal('show');
    }

    $scope.cancel = function(){
        $('#dealModal').modal('hide');
    }

    $scope.deal = function(){
        if("uploading"===$scope.completeAllImgs){
            $scope.notice("还有图片未上传完成,请稍候再试!");
            return;
        }
        $scope.dealInfo.dealImg = "";
        $("#imageUL li").each(function () {
            var yunName = $(this).attr("yun_name");
            if(yunName==="undefined" || typeof (yunName) ==="undefined"){
            }else{
                $scope.dealInfo.dealImg += yunName+",";
            }
        });
        $http({
            method: "post",
            url: "cjtAfterSale/deal",
            data: $scope.dealInfo
        }).success(function(result) {
            $scope.notice(result.msg);
            if(result.status) {
                $scope.cancel();
                $scope.query();
            }
        })
    }

    $scope.uploaderImgListMax = 3;
    //批量上传
    $scope.uploaderImgList = new FileUploader({
        url: 'upload/fileUpload.do',
        // queueLimit: $scope.uploaderImgListMax,   //文件个数
        removeAfterUpload: false,  //上传后删除文件
        autoUpload:true,     //文件加入队列之后自动上传，默认是false
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，限制最多上传个数
    $scope.uploaderImgList.filters.push({
        name: 'imageLengthFilter',
        fn: function(item, options) {
            if(this.queue.length < $scope.uploaderImgListMax) {
                return true;
            } else {
                $scope.notice("最多仅能上传3张图片");
                return false;
            }
        }
    });
    //过滤格式
    $scope.uploaderImgList.filters.push({
        name: 'imageFilter',
        fn: function(item,options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|'.indexOf(type) !== -1;
        }
    });

    $scope.uploaderImgList.onBeforeUploadItem = function(fileItem) {// 上传前的回调函数，在里面执行提交
        $scope.completeAllImgs = "uploading";
        console.log("图片开始上传[onBeforeUploadItem]");
        console.log("queue.length["+this.queue.length+"];uploaderImgListMax["+$scope.uploaderImgListMax+"]");
    };

    $scope.uploaderImgList.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            var itemIndex = this.getIndexOfItem(fileItem);
            $("#imageUL li").eq(itemIndex).attr("yun_name",response.url);
            console.log("图片上传完成一项[onSuccessItem]" + response.url);
        }
    };

    $scope.completeAllImgs = "start";
    $scope.uploaderImgList.onBeforeUpload = function(){
        console.log("图片开始上传[onBeforeUpload]");
        $scope.completeAllImgs = "uploading";
    };
    $scope.uploaderImgList.onCompleteAll = function(){
        console.log("所有图片上传完成[onCompleteAll]");
        $scope.completeAllImgs = "end";
    };

    //页面绑定回车事件
    $document.bind ("keypress", function(event) {
        $scope.$apply(function (){
            if(event.keyCode == 13){
                $scope.query();
            }
        })
    });

});