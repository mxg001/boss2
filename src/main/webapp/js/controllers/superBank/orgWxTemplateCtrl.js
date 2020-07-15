/**
 * 微信模板配置
 */
angular.module('inspinia',['infinity.angular-chosen']).controller('orgWxTemplateCtrl',function($scope,$http,i18nService,$document,SweetAlert){

    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    $scope.typeList = [{text:"信用卡",value:"1"},{text:"贷款",value:"2"}]

    $scope.typeAll = [{text:"全部",value:""},{text:"信用卡",value:"1"},{text:"贷款",value:"2"}]

    $scope.applicationList = [{text:"全部",value:"0"},{text:"公众号",value:"1"},{text:"APP",value:"2"}]

    $scope.resetForm = function () {
        $scope.baseInfo = {orgId:'-1',type:'',application:'0'};//查询条件的form
    };
    $scope.resetForm();

    $scope.columnDefs = [
        {field: 'orgId',displayName: '组织ID',width: 200,pinnable: false,sortable: false},
        {field: 'orgName',displayName: '组织名称',width: 200,pinnable: false,sortable: false},
        {field: 'type',displayName: '场景类型',width: 200,pinnable: false,sortable: false},
        {field: 'templateid',displayName: '模板ID',width: 500,cellTemplate:'' +
            '<div class="lh30" ng-show="row.entity.action!=2">{{row.entity.templateId}}</div>' +
            '<div class="lh30" ng-show="row.entity.action==2">' +
            '<input style="text-align:center;width:400px;height: 30px" ng-model="row.entity.templateId"/></div>'},
        {field: 'action',displayName: '操作',width: 120,pinnedRight:true,
            cellTemplate:
            '<div  class="lh30" ng-show="row.entity.action!=2&&grid.appScope.hasPermit(\'orgWxTemplate.add\')">' +
            '<a ng-click="grid.appScope.orgWxTemplateEdit(row.entity)">编辑</a></div>'
            +
            '<div  class="lh30" ng-show="row.entity.action==2&&grid.appScope.hasPermit(\'orgWxTemplate' +
            '.add\')">' +
            '<a ng-click="grid.appScope.orgWxTemplateUpd(row.entity)">保存</a></div>'
        }
    ];

    $scope.sysOptionColumnDefs = [
        {field: 'optionGroupId',displayName: '数据字典组ID',width: 120,pinnable: false,sortable: false},
        {field: 'subOptionGroupId',displayName: '联动下级字典',width: 120,pinnable: false,sortable: false},
        {field: 'code',displayName: '数据字典项编码',width: 180,pinnable: false,sortable: false},
        {field: 'name',displayName: '数据字典项名称',width: 120,cellTemplate:'' +
            '<div class="lh30" ng-show="row.entity.action!=2">{{row.entity.name}}</div>' +
            '<div class="lh30" ng-show="row.entity.action==2">' +
            '<input style="text-align:center;width:120px;height: 30px" ng-model="row.entity.name"/></div>'},
        {field: 'description',displayName: '数据字典项描述',width: 350,pinnable: false,sortable: false},
        {field: 'rank',displayName: '数据字典项排序',width: 120,pinnable: false,sortable: false},
        {field: 'enabled',displayName: '是否生效',width: 120,pinnable: false,sortable: false},
        {field: 'action',displayName: '操作',width: 120,pinnedRight:true,
            cellTemplate:
            '<div  class="lh30" ng-show="row.entity.action!=2&&grid.appScope.hasPermit(\'sysOption.update\')">' +
            '<a ng-click="grid.appScope.orgWxTemplateEdit(row.entity)">编辑</a></div>'
            +
            '<div  class="lh30" ng-show="row.entity.action==2&&grid.appScope.hasPermit(\'sysOption.update' +
            '\')">' +
            '<a ng-click="grid.appScope.sysOptionUpd(row.entity)">保存</a></div>'
        }
    ];

    $scope.orgSourceColumnDefs = [
        {field: 'orgName',displayName: '组织ID',width: 150,pinnable: false,sortable: false},
        {field: 'sourceId',displayName: '功能ID',width: 150,pinnable: false,sortable: false},
        {field: 'sourceName',displayName: '功能名称',width: 150,pinnable: false,sortable: false},
        {field: 'type',displayName: '类型',width: 150,pinnable: false,sortable: false,cellFilter: "formatDropping:"+angular.toJson($scope.typeList)},
        {field: 'application',displayName: '应用位置',width: 150,pinnable: false,sortable: false ,cellFilter: "formatDropping:"+angular.toJson($scope.applicationList)},
        {field: 'remark',displayName: '备注',width: 150,pinnable: false,sortable: false},
        {field: 'action',displayName: '操作',width: 120,pinnedRight:true,
            cellTemplate:
            '<div  class="lh30" ng-show="row.entity.action!=2&&grid.appScope.hasPermit(\'sysOption.update\')">' +
            '<a ng-click="grid.appScope.deleteOrgSourceConf(row.entity)">删除</a></div>'
        }
    ];

    $scope.orgWxTemplateGrid = {
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		  	//开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs: $scope.columnDefs,
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            $scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.paginationOptions.pageNo = newPage;
                $scope.paginationOptions.pageSize = pageSize;
            });
        }
    };

    $scope.orgSourceGrid = {
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		  	//开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs: $scope.orgSourceColumnDefs,
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            $scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.paginationOptions.pageNo = newPage;
                $scope.paginationOptions.pageSize = pageSize;
                $scope.queryOrgSourcConfList();
            });
        }
    };

    $scope.sysOptionTemplateGrid = {
        useExternalPagination: true,		  	//开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs: $scope.sysOptionColumnDefs,
        // onRegisterApi: function(gridApi) {
        //     $scope.gridApi = gridApi;
        //     $scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
        //         $scope.paginationOptions.pageNo = newPage;
        //         $scope.paginationOptions.pageSize = pageSize;
        //         $scope.query();
        //     });
        // }
    };


    $scope.orgInfoList = [];


    $scope.queryOrgSourcConfList = function(){
        $scope.submitting = true;
        $scope.loadImg = true;
        $http({
            url: 'superBank/queryOrgSourcConfList?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
            data: $scope.baseInfo,
            method:'POST'
        }).success(function (msg) {
            $scope.submitting = false;
            $scope.loadImg = false;
            if (!msg.status){
                $scope.notice(msg.msg);
                return;
            }
            $scope.orgSourceGrid.data = msg.data;
            $scope.orgSourceGrid.totalItems = msg.data.totalCount;
        }).error(function (msg) {
            $scope.submitting = false;
            $scope.loadImg = false;
            $scope.notice('服务器异常,请稍后再试.');
        });
    };
    $scope.queryOrgSourcConfList();


    //获取组织
    $scope.getOrgList = function(){
        $http({
            url:"superBank/getOrgInfoList",
            method:"POST"
        }).success(function(msg){
            if(msg.status){
                $scope.orgInfoListAll = msg.data;
                $scope.orgInfoList = angular.copy($scope.orgInfoListAll);
                $scope.orgInfoListAll.unshift({orgId:'-1',orgName:"全部"});
            }
        }).error(function(){
            $scope.notice("获取组织列表异常");
        });
    };
    $scope.getOrgList();

    $scope.changeSource = function(){
        $http({
            url: 'superBank/changeSource?type='+$scope.baseInfo.type,
            method:'GET',
        }).success(function (msg) {
            if (!msg.status){
                $scope.notice(msg.msg);
                return;
            }else{
                $scope.listAll = msg.data;
            }
        }).error(function (msg) {
            $scope.notice('服务器异常,请稍后再试.');
        });
    };
    $scope.changeSource();
    //获取银行家字典
    $scope.querySysOptionList = function(){
        $scope.submitting = true;
        $scope.loadImg = true;
        $http({
            url: 'superBank/querySysOptionList',
            method:'POST'
        }).success(function (msg) {
            $scope.submitting = false;
            $scope.loadImg = false;
            if (!msg.status){
                $scope.notice(msg.msg);
                return;
            }
            $scope.sysOptionTemplateGrid.data = msg.data;
        }).error(function (msg) {
            $scope.submitting = false;
            $scope.loadImg = false;
            $scope.notice('服务器异常,请稍后再试.');
        });
    };
    $scope.querySysOptionList();

    //编辑配置
    $scope.orgWxTemplateEdit = function(entity){
        entity.action=2;
    };

    //删除
    $scope.deleteOrgSourceConf = function(entity){
        var name=entity.name;
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
                    $http.post("superBank/deleteOrgSourceConf",angular.toJson(entity))
                        .success(function(data){
                            if(data.status){
                                $scope.notice(data.msg);
                                $scope.queryOrgSourcConfList();
                            }else{
                                $scope.notice(data.msg);
                                $scope.submitting = false;
                            }
                        });
                }
            });
    };


    //更新
    $scope.sysOptionUpd = function(entity){
        var name=entity.name;
        if(name==""||null==name){
            $scope.notice("数据字典名称不能为空");
            return
        }
        SweetAlert.swal({
                title: "确定保存吗？",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post("superBank/sysOptionUpd",angular.toJson(entity))
                        .success(function(data){
                            if(data.status){
                                $scope.notice(data.msg);
                                entity.action=1;
                                $scope.querySysOptionList();
                            }else{
                                $scope.notice(data.msg);
                                $scope.submitting = false;
                            }
                        });
                }
            });
    };

    //更新
    $scope.orgWxTemplateUpd = function(entity){
        SweetAlert.swal({
                title: "确定保存吗？",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post("superBank/updateOrgWxTemplate",angular.toJson(entity))
                        .success(function(data){
                            if(data.status){
                                $scope.notice(data.msg);
                                entity.action=1;
                            }else{
                                $scope.notice(data.msg);
                                $scope.submitting = false;
                            }
                        });
                }
            });
    };

    $scope.query = function () {
        $scope.submitting = true;
        $scope.loadImg = true;

        $http({
            url: 'superBank/getOrgWxTemplate?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
            data: $scope.baseInfo,
            method:'POST'
        }).success(function (msg) {
            $scope.submitting = false;
            $scope.loadImg = false;
            if (!msg.status){
                $scope.notice(msg.msg);
                return;
            }
            $scope.orgWxTemplateGrid.data = msg.data.result;
            $scope.orgWxTemplateGrid.totalItems = msg.data.totalCount;
        }).error(function (msg) {
            $scope.submitting = false;
            $scope.loadImg = false;
            $scope.notice('服务器异常,请稍后再试.');
        });
    };

    $scope.addOrgWxTemplate = function(){
        $("#addOrgWxTemplate").modal("show");
    };

    $scope.cancel = function(){
        $("#addOrgWxTemplate").modal("hide");
    };

    $scope.addOrgSourceConf = function(){
        $scope.baseInfo.type=1;
        $("#addOrgSourceConf").modal("show");
    };

    $scope.cancelOrg = function(){
        $("#addOrgSourceConf").modal("hide");
    };

    //新增
    $scope.saveOrgWxTemplate = function(){
        var data = {
            "orgId" : $scope.baseInfo.orgId,
            "type":$scope.baseInfo.type,
            "templateId":$scope.baseInfo.templateId,
        };
        $http.post("superBank/addOrgWxTemplate",angular.toJson(data))
            .success(function(data){
                if(data.status){
                    $scope.notice("新增成功");
                    $scope.resetForm();
                    $("#addOrgWxTemplate").modal("hide");
                    $scope.query();
                }else{
                    $scope.notice(data.msg);
                    $scope.submitting = false;
                }
            });
    };

    //新增
    $scope.saveOrgSourceConf = function(){
        var data = {
            "orgId" : $scope.baseInfo.orgId,
            "sourceId":$scope.baseInfo.sourceId,
            "type":$scope.baseInfo.type,
            "application":$scope.baseInfo.application,
        };
        $http.post("superBank/saveOrgSourceConf",angular.toJson(data))
            .success(function(data){
                if(data.status){
                    $scope.notice("新增成功");
                    $scope.resetForm();
                    $("#addOrgSourceConf").modal("hide");
                    $scope.queryOrgSourcConfList();
                }else{
                    $scope.notice(data.msg);
                    $scope.submitting = false;
                }
            });
    };


    //页面绑定回车事件
    $document.bind("keypress", function(event) {
        $scope.$apply(function (){
            if(event.keyCode == 13){
                $scope.query();
            }
        });
    });
});