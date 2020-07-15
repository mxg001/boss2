/**
 * 信用卡银行数据导入管理
 */
angular.module('inspinia', ['infinity.angular-chosen', 'angularFileUpload']).controller('bankImportCtrl',function($scope,$http,i18nService,$document,SweetAlert,$timeout,FileUploader){
    //数据源
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    $scope.statusList = [{text:"全部",value:""},{text:"无需操作",value:"0"},{text:"已匹配",value:"1"},{text:"待匹配",value:"2"}];//订单状态
    $scope.bonusTypeList = [{text:"全部", value:""},{text:"办卡", value:"1"},{text:"首刷", value:"2"}];
    $scope.resetForm = function () {
        $scope.baseInfo = {status:"",bonusType:"",
            importTimeStart:moment(new Date().getTime()-24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
            importTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'
        };
    }
    $scope.resetForm();

    $scope.columnDefs = [
        {field: 'id',displayName: 'ID',width: 120,pinnable: false,sortable: false},
        {field: 'batchNo',displayName: '导入批次号',width: 120,pinnable: false,sortable: false},
        {field: 'bankCode',displayName: '银行编码',width: 150,pinnable: false,sortable: false},
        {field: 'bankName',displayName: '银行名称',width: 150,pinnable: false,sortable: false},
        {field: 'bankNickName',displayName: '银行别称',width: 150,pinnable: false,sortable: false},
        {field: 'bonusType',displayName: '类型',width: 150,pinnable: false,sortable: false,cellFilter:"formatDropping:" + angular.toJson($scope.bonusTypeList)},
        {field: 'fileUrl',displayName: '导入文件',width: 250,pinnable: false,sortable: false,cellTemplate:
            '<a href="{{row.entity.fileUrlStr}}" class="lh30">{{row.entity.fileUrl}}</a>'
        },
        {field: 'importTime',displayName: '导入时间',width: 150,pinnable: false,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'importBy',displayName: '导入操作人',width: 150,pinnable: false,sortable: false},
        {field: 'message',displayName: '导入结果',width: 150,pinnable: false,sortable: false},
        {field: 'status',displayName: '状态',width: 150,pinnable: false,sortable: false,cellFilter:"formatDropping:" + angular.toJson($scope.statusList)},
        {field: 'action',displayName: '操作',width: 120,pinnedRight:true,sortable: false,editable:true,cellTemplate:
        '<a class="lh30" ng-show="grid.appScope.hasPermit(\'bankImport.matchData\')&&row.entity.status==2" '
        + 'ng-click="grid.appScope.matchData(row.entity)">匹配</a>'}
    ];

    $scope.infoGrid = {
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
//		rowHeight:35,
        columnDefs: $scope.columnDefs,
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            $scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.paginationOptions.pageNo = newPage;
                $scope.paginationOptions.pageSize = pageSize;
                $scope.query();
            });
        }
    };

    $scope.query = function () {
        $scope.submitting = true;
        $scope.loadImg = true;
        $http({
            url: 'bankImport/selectList?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
            data: $scope.baseInfo,
            method:'POST'
        }).success(function (result) {
            $scope.submitting = false;
            $scope.loadImg = false;
            if (!result.status){
                $scope.notice(result.msg);
                return;
            }
            $scope.infoGrid.data = result.data.result;
            $scope.infoGrid.totalItems = result.data.totalCount;
        }).error(function () {
            $scope.submitting = false;
            $scope.loadImg = false;
            $scope.notice('服务器异常,请稍后再试.');
        });
    };

    //修改上架开关
    $scope.matchData=function(entity){
        if(!(entity && entity.status == 2)){
            $scope.notice("只有待匹配的才能匹配");
            return;
        }
        SweetAlert.swal({
                title: "",
                text: "确定匹配",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $scope.loadImg = true;
                    $http.post("bankImport/matchData",angular.toJson({"id":entity.id}))
                        .success(function(data){
                            $scope.loadImg = false;
                            $scope.notice(data.msg);
                            if(data.status){
                                $scope.query();
                            }
                        });
                }
            });
    };

    //上传文件,定义控制器路径
    $scope.uploader = new FileUploader({
        url: 'bankImport/importData',
        queueLimit: 1,   //文件个数
        removeAfterUpload: true,  //上传后删除文件
        headers : {'X-CSRF-TOKEN' : $scope.csrfData},
        formData:[{id:"",bonusType:""}]
    });
    //过滤格式
    $scope.uploader.filters.push({
        name: 'fileFilter',
        fn: function(item /*{File|FileLikeObject}*/, options) {
            var type = '|' + item.name.slice(item.name.lastIndexOf('.') + 1) + '|';
            return '|xlsx|xls|'.indexOf(type) !== -1;
        }
    });
    //批量导入modal
    $scope.importModal = function(){
        $scope.importInfo.id = "";
        $scope.importInfo.bonusType = "1";

        $('#importModal').modal('show');
    }

    $scope.cancel = function(){
        $('#importModal').modal('hide');
    }

    //批量导入提交数据
    $scope.importStatus = false;
    $scope.importInfo = function(){
        $scope.importStatus = true;
        $scope.uploader.queue[0].formData[0].id = $scope.importInfo.id;
        $scope.uploader.queue[0].formData[0].bonusType = $scope.importInfo.bonusType;
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

    //获取所有的银行
    $scope.getBanks = function(){
        $http({
            url:"superBank/banksList",
            method:"POST"
        }).success(function(msg){
            if(msg.status){
                $scope.banksList = msg.data;
            }
        }).error(function(){
            $scope.notice("获取银行列表异常");
        });
    };
    $scope.getBanks();

    $scope.downTemplate = function (id,bonusType) {
        if(!id){
            $scope.notice("银行不能为空");
            return;
        }
        location.href = "bankImport/importTemplate?id=" + encodeURI(id) + "&bonusType=" + bonusType;
    }

    //页面绑定回车事件
    $document.bind("keypress", function(event) {
        $scope.$apply(function (){
            if(event.keyCode == 13){
                $scope.query();
            }
        })
    });
});