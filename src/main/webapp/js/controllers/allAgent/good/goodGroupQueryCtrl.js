/**
 * 商品分类列表
 */
angular.module('inspinia').controller('goodGroupQueryCtrl', function ($scope, $http, i18nService, SweetAlert, $document) {
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions = angular.copy($scope.paginationOptions);

    $scope.addInfo = {brandCode: ""};
    $scope.btnText = "保存";
    $scope.subDisable = false;


    //清空
    $scope.clear = function () {
        $scope.info = {
            status: "", brandCode: "",
            createTimeBegin: moment(new Date().getTime() - 6 * 24 * 60 * 60 * 1000).format('YYYY-MM-DD') + ' 00:00:00',
            createTimeEnd: moment(new Date().getTime()).format('YYYY-MM-DD') + ' 23:59:59'
        };
    };
    $scope.clear();

    //组织列表
    $scope.oemList = [];
    $http.post("awardParam/getOemList",
        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function (data) {
            if (data.status) {
                $scope.oemList.push({value: "", text: "全部"});
                var list = data.list;
                if (list != null && list.length > 0) {
                    for (var i = 0; i < list.length; i++) {
                        $scope.oemList.push({value: list[i].brandCode, text: list[i].brandName});
                    }
                }
            }
        });

    $scope.userGrid = {                           //配置表格
        data: 'result',
        paginationPageSize: 10,                  //分页数量
        paginationPageSizes: [10, 20, 50, 100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar: true,  		//纵向滚动条
        columnDefs: [                           //表格数据
            {field: 'group_name', displayName: '分类名称', width: 180},
            {field: 'brand_name', displayName: '所属品牌', width: 180},
            {field: 'num', displayName: '排序', width: 180},
            {field: 'create_time', displayName: '创建时间', cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"', width: 180},
            {
                field: 'id', displayName: '操作', width: 220, pinnedRight: true, cellTemplate:
                    '<div class="lh30">' +
                    '<a ng-click="grid.appScope.deleteGoodsGroup(row.entity.group_code)">删除</a> ' +
                    '<a ng-click="grid.appScope.updateGoodsGroup(row.entity.group_code,row.entity.brand_code,row.entity.group_name,row.entity.num)"> | 修改</a> ' +
                    '</div>'
            }
        ],
        onRegisterApi: function (gridApi) {
            $scope.gridApi = gridApi;
            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.paginationOptions.pageNo = newPage;
                $scope.paginationOptions.pageSize = pageSize;
                $scope.query();
            });
        }
    };
    $scope.query = function () {
        if ($scope.loadImg) {
            return;
        }
        $scope.loadImg = true;
        $http.post("goodAllAgent/goodGroupQuery", "info=" + angular.toJson($scope.info) + "&pageNo=" +
            $scope.paginationOptions.pageNo + "&pageSize=" + $scope.paginationOptions.pageSize,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function (data) {
                if (data.status) {
                    $scope.result = data.page.result;
                    $scope.userGrid.totalItems = data.page.totalCount;
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
    //页面绑定回车事件
    $document.bind("keypress", function (event) {
        $scope.$apply(function () {
            if (event.keyCode == 13) {
                $scope.query();
            }
        })
    });

    $scope.showGoodsGroupModel = function (op) {
        $("#showGoodsGroupModel").modal("show");
        if("up"===op){
            $scope.btnText = "修改";
            $scope.subDisable = true;
        }else{
            $scope.btnText = "保存";
            $scope.subDisable = false;
            $scope.addInfo.brandCode = "";
            $scope.addInfo.groupName = "";
            $scope.addInfo.num = "";
        }
    };

    $scope.updateGoodsGroup = function (groupCode,brandCode,groupName,num) {
        $scope.addInfo.brandCode = brandCode;
        $scope.addInfo.groupName = groupName;
        $scope.addInfo.groupCode = groupCode;
        $scope.addInfo.num=num;
        $scope.showGoodsGroupModel("up");
    };

    $scope.deleteGoodsGroup = function (groupCode) {
        $scope.addInfo.groupCode = groupCode;
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
                    $http.post("goodAllAgent/deleteGoodsGroup", "info=" + angular.toJson($scope.addInfo),
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function (data) {
                            $scope.query();
                            $scope.notice(data.msg);
                            $scope.loadImg = false;
                            $("#showGoodsGroupModel").modal("hide");
                        })
                        .error(function (data) {
                            $scope.notice(data.msg);
                            $scope.loadImg = false;
                            $("#showGoodsGroupModel").modal("hide");
                        });
                }
            });
    }

    $scope.goodsGroupOp = function () {
        if ($scope.loadImg) {
            return;
        }
        if($scope.addInfo.brandCode===""){
            $scope.notice("请选择所属品牌");
            return;
        }
        if($scope.addInfo.groupName===""){
            $scope.notice("请填写分类名称");
            return;
        }
        if($scope.addInfo.num===""){
            $scope.notice("排序不能为空");
            return;
        }
        var exp = /^[0-9]*$/;
        if(!exp.test($scope.addInfo.num)){
            $scope.notice("排序格式不对");
            return;
        }
        $scope.loadImg = true;
        var url = $scope.btnText==="保存" ?  "goodAllAgent/addGoodsGroup" : "goodAllAgent/updateGoodsGroup";
        $http.post(url, "info=" + angular.toJson($scope.addInfo),
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function (data) {
                $scope.notice(data.msg);
                $scope.loadImg = false;
                $("#showGoodsGroupModel").modal("hide");
                $scope.query();
            })
            .error(function (data) {
                $scope.notice(data.msg);
                $scope.loadImg = false;
                $("#showGoodsGroupModel").modal("hide");
            });
    };
    $scope.query();
});