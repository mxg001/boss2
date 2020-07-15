/**
 * 直清商户批量报备查询
 */
angular.module('inspinia',['angularFileUpload']).controller("queryZqFileSync", function($scope, $http, i18nService,$document,SweetAlert,FileUploader) {
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    $scope.statusList = [{text: "全部", value: ""}, {text: "报备完成", value: "2"}, {text: "报备中", value: "1"}];

    //清空
    $scope.resetForm = function () {
        $scope.baseInfo = {status:"",channelCode:"",batchNo:"",
            createTimeStart:moment(new Date().getTime()-7*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
            createTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'};
    }
    $scope.resetForm();

    //获取所有直清收单机构
    $scope.acqList = [{ acqEnname: "",acqName: "全部"}];
    $http.post("acqOrgAction/selectAllZqOrg")
        .success(function (result) {
            for (var i = 0; i < result.length; i++) {
                $scope.acqList.push({acqEnname: result[i].acqEnname, acqName: result[i].acqName});
            }
        });

    //查询
    $scope.query = function(){
        $scope.submitting = true;
        $scope.loadImg = true;
        $http({
            url:"zqFileSync/selectPage?pageNo=" + $scope.paginationOptions.pageNo +  "&pageSize=" + $scope.paginationOptions.pageSize,
            method:"post",
            data:$scope.baseInfo
        }).success(function(result){
            $scope.submitting = false;
            $scope.loadImg = false;
            if (!result || !result.status){
                $scope.notice (result.msg);
                return;
            }
            $scope.zqFileSyncGrid.data = result.data.result;
            $scope.zqFileSyncGrid.totalItems = result.data.totalCount;
        }).error(function(){
            $scope.submitting = false;
            $scope.loadImg = false;
            $scope.notice("服务器异常");
        });
    };
    $scope.query();

    $scope.columnDefs = [
                    {field: 'batchNo',displayName: '批次号',width:120},
                    {field: 'createTimeStr',displayName: '创建时间',width:150},
                    {field: 'channelCode',displayName: '收单机构',width:150},
                    {field: 'statusName',displayName: '状态',width:150},
                    {field: 'innerNum',displayName: '有效数据总条数',width:150},
                    {field: 'fileName',displayName: '文件名',width:150,cellTemplate:
                            '<a href="{{row.entity.fileUrl}}" target="_blank" class="lh30">{{row.entity.fileName}}</a>'},
                    {field: 'operator',displayName: '操作人',width:150},
                {field: 'action',displayName: '操作',width: 150,pinnedRight:true,sortable: false,editable:true,cellTemplate:
            '<a class="lh30"  ng-show="grid.appScope.hasPermit(\'zqFileSync.updateStatus\')&&row.entity.status!=2" '
            + ' ng-click="grid.appScope.updateStatus(row.entity)">强制正常</a>'
            }
    ];

    $scope.zqFileSyncGrid = {
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
                    location.href = "zqFileSync/export?baseInfo=" +encodeURI(encodeURI(angular.toJson($scope.baseInfo)));
                }
            });
    };

    //修改上架开关
    $scope.updateStatus = function(entity){
        SweetAlert.swal({
                title: "",
                text: "确定要强制正常?",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http({
                        url:"zqFileSync/updateStatus",
                        method:"post",
                        data:{batchNo:entity.batchNo, status:"2"}
                    }).success(function(result){
                            $scope.notice(result.msg);
                            if(result.status){
                                $scope.query();
                            }
                        });
                }
            });
    };

    //上传文件,定义控制器路径
    $scope.uploader = new FileUploader({
        url: 'zqFileSync/importData',
        queueLimit: 1,   //文件个数
        removeAfterUpload: true,  //上传后删除文件
        headers : {'X-CSRF-TOKEN' : $scope.csrfData},
        formData:[{channelCode:""}]
    });
    //过滤格式
    $scope.uploader.filters.push({
        name: 'fileFilter',
        fn: function(item /*{File|FileLikeObject}*/, options) {
            var type = '|' + item.name.slice(item.name.lastIndexOf('.') + 1) + '|';
            return '|xlsx|xls|'.indexOf(type) !== -1;
        }
    });
    // 批量导入modal
    $scope.importModal = function(){
        $scope.importInfo.channelCode = "";
        $('#importModal').modal('show');
    }

    $scope.cancel = function(){
        $('#importModal').modal('hide');
    }

    //批量导入提交数据
    $scope.importStatus = false;
    $scope.importInfo = function(){
        $scope.importStatus = true;
        $scope.uploader.queue[0].formData[0].channelCode = $scope.importInfo.channelCode;
        if($scope.uploader.queue.length > 0){
            $scope.uploader.uploadAll();
            $scope.uploader.onSuccessItem = function(fileItem, response, status, headers) {//上传成功后的回调函数，在里面执行提交
                $scope.importStatus = false;
                $scope.notice(response.msg);
                $scope.cancel();
                $scope.query();
            };
        }
    }

    //页面绑定回车事件
    $document.bind ("keypress", function(event) {
        $scope.$apply(function (){
            if(event.keyCode == 13){
                $scope.query();
            }
        })
    });

});