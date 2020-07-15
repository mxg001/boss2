/**
 * 贷款机构管理
 */
angular.module('inspinia',['uiSwitch']).controller('loanInstitutionManagementCtrl',function($scope,$http,i18nService,$document,SweetAlert){
    //数据源
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    $scope.statusAll = [{text:"全部",value:""},{text:"是",value:"on"},{text:"否",value:"off"}];
    $scope.rewardRequirementsList = [{text:"全部",value:""},{text:"有效注册",value:"1"},{text:"授信成功",value:"3"},{text:"有效借款",value:"2"}];//奖励要求
    $scope.profitTypeList = [{text:"全部",value:""},{text:"固定金额",value:"1"},{text:"按放贷比例",value:"2"}];//奖金模式
    
    $scope.resetForm = function () {
        $scope.baseInfo = {status:"",rewardRequirements:"",profitType:""};
    }
    $scope.resetForm();

    $scope.columnDefs = [
        {field: 'id',displayName: '贷款机构编码',width: 120,pinnable: false,sortable: false},
        {field: 'loanProduct',displayName: '贷款机构名称',width: 120,pinnable: false,sortable: false},
        {field: 'loanAlias',displayName: '贷款机构别称',width: 150,pinnable: false,sortable: false},
        {field: 'showLogoUrl',displayName: '显示的logo',width: 150,pinnable: false,sortable: false,
            cellTemplate:'<img style="width: 140px; height: 36px;" ng-show="row.entity.showLogoUrl" ng-src="{{row.entity.showLogoUrl}}" />'},
        {field: 'source',displayName: '来源',width: 200,pinnable: false,sortable: false},
        {field: 'accessWay',displayName: '接入方式',width: 150,pinnable: false,sortable: false,
            cellTemplate: '<div style="margin-top:8px;"><span>{{row.entity.accessWay === 1 ? "H5":"API"}}</span></div>'},
        {field: 'statusInt',displayName: '是否上架',width:150,pinnable: false,sortable: false,cellTemplate:
            '<span ng-show="grid.appScope.hasPermit(\'superBank.updateBankStatus\')"><switch class="switch switch-s" ng-model="row.entity.statusInt" ng-change="grid.appScope.switchStatus(row)" /></span>'
            +'<span ng-show="!grid.appScope.hasPermit(\'superBank.updateBankStatus\')"> <span ng-show="row.entity.statusInt==1">是</span><span ng-show="row.entity.statusInt==0">否</span></span>'
        },
        {field: 'showOrder',displayName: '顺序',width: 180,pinnable: false,sortable: false},
        {field: 'profitType',displayName: '奖金方式',width: 120,pinnable: false,sortable: false,cellFilter:"formatDropping:" + angular.toJson($scope.profitTypeList)},
        {field: 'rewardRequirements',displayName: '奖励要求',width: 180,pinnable: false,sortable: false,cellFilter:"formatDropping:" + angular.toJson($scope.rewardRequirementsList)},
        {field: 'showLoanBonus',displayName: '贷款机构总奖金',width: 180,pinnable: false,sortable: false},
        {field: 'action',displayName: '操作',width: 150,pinnedRight:true,sortable: false,editable:true,cellTemplate:
            '<a class="lh30" target="_blank" '
            + 'ui-sref="superBank.loanInstitutionDetail({id:row.entity.id})">详情</a>'
            +'<a class="lh30" ng-show="grid.appScope.hasPermit(\'superBank.loanInstitutionDetail\')" '
            + 'ui-sref="superBank.updateLoanInstitution({id:row.entity.id})"> | 修改</a>'
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
            url: 'loanInstitutionManagement/selectList?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
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
                    $http.post("loanInstitutionManagement/updateLoanStatus",angular.toJson(data))
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
                    row.entity.statusInt = !row.entity.statusInt;
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