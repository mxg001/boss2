/**
 * 保留昵称列表
 */
angular.module('inspinia',['angularFileUpload']).controller('persistNickNameCtrl', function ($scope, $http, i18nService, SweetAlert, $document,$q,$filter,FileUploader) {
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions = angular.copy($scope.paginationOptions);

    $scope.addInfo={};
    $scope.isEdit = true;
    $scope.subDisable = false;

    //清空
    $scope.clear = function () {
        $scope.info = {keyWord: "",createTimeBegin:"",createTimeEnd:""};
    };
    $scope.clear();

    $scope.keyGrid = {                           //配置表格
        data: 'result',
        paginationPageSize: 10,                  //分页数量
        paginationPageSizes: [10, 20, 50, 100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar: true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            {field: 'keyWord', displayName: '保留昵称', width: 200},
            {field: 'createTime', displayName: '添加日期', width: 200, cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
            {field: 'modifyTime', displayName: '编辑日期', width: 200, cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
            {
                field: 'id', displayName: '操作', width: 200, pinnedRight: true, cellTemplate:
                '<div class="lh30">' +
                '<a ng-click="grid.appScope.persistNickNameDetail(row.entity.keyWord)">详情</a> ' +
                '<a ng-click="grid.appScope.updatePersistNickName(row.entity.id,row.entity.keyWord)"> | 编辑</a> ' +
                '<a ng-click="grid.appScope.deletePersistNickName(row.entity.id)"> | 删除</a> ' +
                '</div>'
            }
        ],
        onRegisterApi: function (gridApi) {
            $scope.gridApi = gridApi;
            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.paginationOptions.pageNo = newPage;
                $scope.paginationOptions.pageSize = pageSize;
                $scope.queryFunc();
            });
        }
    };

    $scope.queryFunc = function () {
        if ($scope.loadImg) {
            return;
        }
        $scope.loadImg = true;
        $http.post("persistNickName/selectPersistNickNameList", "info=" + angular.toJson($scope.info) + "&pageNo=" +
            $scope.paginationOptions.pageNo + "&pageSize=" + $scope.paginationOptions.pageSize,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function (data) {
                if (data.status) {
                    $scope.result = data.page.result;
                    $scope.keyGrid.totalItems = data.page.totalCount;
                } else {
                    $scope.notice(data.msg);
                }
                $scope.loadImg = false;
            })
            .error(function (data) {
                $scope.notice(data.msg);
                $scope.loadImg = false;
            });
    };
    $scope.queryFunc();

    $scope.showModel = function (op) {
        $("#showModel").modal("show");
        $scope.isEdit = true;
        if("up"===op){
            $scope.subDisable = true;
        }else{
            $scope.subDisable = false;
            $scope.addInfo={};
        }
    };

    $scope.persistNickNameDetail = function(keyWord){
        $scope.addInfo.keyWord=keyWord;
        $scope.isEdit = false;
        $("#showModel").modal("show");
    }

    $scope.updatePersistNickName = function (id,keyWord) {
        $scope.addInfo.id = id;
        $scope.addInfo.keyWord = keyWord;
        $scope.showModel("up");
    };

    $scope.deletePersistNickName = function (id) {
        SweetAlert.swal({
                title: "确定删除吗？",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post("persistNickName/deletePersistNickName", "id=" + id,
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function (data) {
                            $scope.notice(data.msg);
                            $scope.loadImg = false;
                            $("#showModel").modal("hide");
                            $scope.queryFunc();
                        })
                        .error(function (data) {
                            $scope.notice(data.msg);
                            $scope.loadImg = false;
                            $("#showModel").modal("hide");
                        });
                }
            });
    }

    $scope.recordOp = function () {
        if ($scope.loadImg) {
            return;
        }
        if (typeof($scope.addInfo.keyWord) === "undefined" || $scope.addInfo.keyWord === "") {
            $scope.notice("请填写保留昵称");
            return;
        }
        if($scope.subDisable){
            $scope.addInfo.keyWord=$scope.addInfo.keyWord.replace(/，/g,",");
            if($scope.addInfo.keyWord.indexOf(",")!=-1){
                $scope.notice("修改昵称只允许单个添加");
                return;
            }
        }
        $scope.loadImg = true;
        var url = $scope.subDisable ?  "persistNickName/updatePersistNickName" : "persistNickName/addPersistNickName";
        $http.post(url, "info=" + angular.toJson($scope.addInfo),
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function (data) {
                $scope.notice(data.msg);
                $scope.loadImg = false;
                $("#showModel").modal("hide");
                $scope.queryFunc();
                $scope.clear();
            })
            .error(function (data) {
                $scope.notice(data.msg);
                $scope.loadImg = false;
                $("#showModel").modal("hide");
                $scope.clear();
            });
    };

    $scope.errorCount=0;
    $scope.successCount=0;

    $scope.importButchModel=function () {
        $("#importButchModel").modal("show");
    }

    $scope.cancelImportButchModel=function () {
        $('#importButchModel').modal('hide');
        $("#importResultButchModel").modal("hide");
        $scope.loadImg = false;
    }

    $scope.serviceGrid = {
        data: 'errorlist',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs: [
            {field: 'keyWord',displayName: '导入保留昵称',width:200},
            {field: 'errorResult',displayName: '失败信息',width:200}
        ]
    };

    var uploader = $scope.uploader = new FileUploader({
        url: 'persistNickName/importButchUpload',
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
    $scope.submit=function(){
        if ($scope.loadImg) {
            return;
        }
        if ($scope.loadImgA) {
            return;
        }
        $scope.loadImg = true;
        $scope.loadImgA = true;
        console.log($scope.loadImgA)
        uploader.uploadAll();//上传
        uploader.onSuccessItem = function(fileItem, response, status, headers) {//上传成功后的回调函数，在里面执行提交
            if(response.status){
                $scope.loadImg = false;
                $scope.loadImgA = false;
                $("#importResultButchModel").modal("show");
                $scope.errorCount=response.errorCount;
                $scope.successCount=response.successCount;
                $scope.errorlist=response.errorlist;
                $scope.queryFunc();
            }else{
                $scope.notice(response.msg);
                $scope.loadImg = false;
                $scope.loadImgA = false;
            }
        };
        return false;
    }

    $scope.export = function () {
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
                    location.href="persistNickName/exportPersistNickName?param="+angular.toJson($scope.info);
                }
            });

    }

    //页面绑定回车事件
    $document.bind("keypress", function (event) {
        $scope.$apply(function () {
            if (event.keyCode == 13) {
                $scope.queryFunc();
            }
        })
    });
});