/**
 * 信用卡银行管理
 */
angular.module('inspinia',['uiSwitch']).controller('creditcardSourceCtrl',function($scope,$http,i18nService,$document,SweetAlert){
    //数据源
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    $scope.statusAll = [{text:"全部",value:""},{text:"是",value:"on"}
        ,{text:"否",value:"off"}];
    $scope.batchStatusList = [{text:"是",value:"1"},{text:"否",value:"0"}];
    $scope.accessMethodsList = [{text:"全部",value:""},{text:"H5",value:"H5"},{text:"API",value:"API"}];
    
/*
    $scope.cardTypeList = [{text:"全部",value:""},{text:"普通卡",value:"1"},{text:"校园卡",value:"2"}];
*/
    $scope.resetForm = function () {
        $scope.baseInfo = {status:"",approveStatus:"",autoShareStatus:"",cardType:"",batchStatus:"",cardActiveStatus:"",accessMethods:""};
    }
    $scope.resetForm();

    $scope.columnDefs = [
        {field: 'code',displayName: '银行编码',width: 120,pinnable: false,sortable: false},
        {field: 'bankName',displayName: '银行名称',width: 150,pinnable: false,sortable: false},
        {field: 'bankNickName',displayName: '银行别称',width: 150,pinnable: false,sortable: false},
        {field: 'batchStatus',displayName: '查询是否秒结',width: 120,pinnable: false,sortable: false,cellFilter:"formatDropping:" + angular.toJson($scope.batchStatusList)},
/*
        {field: 'cardType',displayName: '卡类型',width: 120,pinnable: false,sortable: false,cellFilter:"formatDropping:" + angular.toJson($scope.cardTypeList)},
*/
        {field: 'showLogo',displayName: '显示的logo',width: 150,pinnable: false,sortable: false,
            cellTemplate:'<img style="width: 140px; height: 36px;" ng-show="row.entity.showLogoUrl" ng-src="{{row.entity.showLogoUrl}}" />'},
        {field: 'source',displayName: '来源',width: 200,pinnable: false,sortable: false},
        {field: 'statusInt',displayName: '是否上架',width:150,pinnable: false,sortable: false,cellTemplate:
            '<span ng-show="grid.appScope.hasPermit(\'superBank.updateBankStatus\')">' +
             '<switch class="switch switch-s" ng-model="row.entity.statusInt" ng-change="grid.appScope.switchStatus(row)" />' +
            '</span>'
            +'<span ng-show="!grid.appScope.hasPermit(\'superBank.updateBankStatus\')">' +
                ' <span ng-show="row.entity.statusInt==1">是</span>' +
             '<span ng-show="row.entity.statusInt==0">否</span>' +
            '</span>'
        },
        {field: 'showOrder',displayName: '顺序',width: 180,pinnable: false,sortable: false},
        {field: 'cardBonus',displayName: '发卡奖金',width: 120,pinnable: false,sortable: false,cellFilter:'currency:""'},
        {field: 'firstBrushBonus',displayName: '首刷奖金',width: 180,pinnable: false,sortable: false,cellFilter:'currency:""'},
        {field: 'approveStatus',displayName: '是否秒批',width: 180,pinnable: false,sortable: false,cellFilter:"formatDropping:" + angular.toJson($scope.bool)},
        {field: 'cardActiveStatus',displayName: '发卡奖金是否要求卡片激活',width: 210,pinnable: false,sortable: false,cellFilter:"formatDropping:" + angular.toJson($scope.bool)},
        {field: 'quickCardStatus',displayName: '是否展示极速办卡标签',width: 180,pinnable: false,sortable: false,cellFilter:"formatDropping:" + angular.toJson($scope.bool)},
        {field: 'specialLabel',displayName: '为银行增加特别标签',width: 180,pinnable: false,sortable: false},
        {field: 'accessMethods',displayName: '接入方式',width: 180,pinnable: false,sortable: false,cellFilter:"formatDropping:" + angular.toJson($scope.accessMethodsList)},
        {field: 'autoShareStatus',displayName: '是否自动结算分润',width: 180,pinnable: false,sortable: false,cellFilter:"formatDropping:" + angular.toJson($scope.bool)},
        {field: 'specialPosition',displayName: '特别推荐位置',width: 180,pinnable: false,sortable: false},
        /*{field: 'activityImage',displayName: '活动图片',width: 150,pinnable: false,sortable: false,
            cellTemplate:'<img style="width: 140px; height: 36px;" ng-show="row.entity.activityImageUrl" ng-src="{{row.entity.activityImageUrl}}" />'},
        {field: 'activityLink',displayName: '活动图片链接',width: 180,pinnable: false,sortable: false},*/
        {field: 'applyCardGuideImg',displayName: '办卡攻略图片',width: 150,pinnable: false,sortable: false,
            cellTemplate:'<img style="width: 140px; height: 36px;" ng-show="row.entity.applyCardGuideImgUrl" ng-src="{{row.entity.applyCardGuideImgUrl}}" />'},
        {field: 'action',displayName: '操作',width: 150,pinnedRight:true,sortable: false,editable:true,cellTemplate:
            '<a class="lh30" target="_blank" '
            + 'ui-sref="superBank.creditcardSourceDetail({id:row.entity.id})">详情</a>'
            +'<a class="lh30" ng-show="grid.appScope.hasPermit(\'superBank.updateCreditcardSource\')" '
            + 'ui-sref="superBank.updateCreditcardSource({id:row.entity.id})"> | 修改</a>'
        }
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
            url: 'creditcardSource/selectList?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
            data: $scope.baseInfo,
            method:'POST'
        }).success(function (msg) {
            $scope.submitting = false;
            $scope.loadImg = false;
            if (!msg.status){
                $scope.notice(msg.msg);
                return;
            }
            $scope.infoGrid.data = msg.data.result;
            $scope.infoGrid.totalItems = msg.data.totalCount;
        }).error(function (msg) {
            $scope.submitting = false;
            $scope.loadImg = false;
            $scope.notice('服务器异常,请稍后再试.');
        });
    };

    //修改上架开关
    $scope.switchStatus=function(row){
        if(row.entity.status){
            $scope.serviceText = "确定开启";
        } else {
            $scope.serviceText = "确定关闭";
        }
        SweetAlert.swal({
                title: "",
                text: $scope.serviceText,
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    if(row.entity.statusInt==true){
                        row.entity.status='on';
                    } else if(row.entity.statusInt==false){
                        row.entity.status='off';
                    }
                    var data={"status":row.entity.status,"id":row.entity.id};
                    $http.post("creditcardSource/updateBankStatus",angular.toJson(data))
                        .success(function(data){
                            $scope.notice(data.msg);
                            if(data.status){
                                $scope.query();
                            }else{
                                row.entity.status = !row.entity.status;
                            }
                        })
                        .error(function(data){
                            row.entity.status = !row.entity.status;
                            $scope.notice("服务器异常");
                        });
                } else {
                    row.entity.statusInt = !row.entity.status;
                }
            });
    };

    //页面绑定回车事件
    $document.bind("keypress", function(event) {
        $scope.$apply(function (){
            if(event.keyCode == 13){
                $scope.query();
            }
        })
    });
});