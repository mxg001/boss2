/**
 * 超级银行家用户管理
 */
angular.module('inspinia').controller('userManageCtrl',function($scope,$http,i18nService, SweetAlert){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
	// $scope.payBackList = [{text:"全部",value:""},{text:"已退款",value:"1"},{text:"未退款",value:"0"}];
	$scope.payMoneyList = [{text:"全部",value:""},{text:"已缴费",value:"1"},{text:"未缴费",value:"0"}];
	$scope.openAccountList = [{text:"全部",value:""},{text:"已开户",value:"1"},{text:"未开户",value:"0"}];
	$scope.statusList = [{text:"全部",value:""},{text:"正常",value:"1"},{text:"冻结",value:"2"}];
    $scope.statusMentorList = [{text:"全部",value:""},{text:"否",value:"0"},{text:"是",value:"1"}];
	$scope.isOpenList=[{text:"否",value:null},{text:"否",value:"0"},{text:"是",value:"1"}];
    $scope.userTypeList = [{text:"全部",value:""},{text:"用户",value:"10"},
       						 {text:"专员",value:"20"},{text:"经理",value:"30"},
       						 {text:"银行家",value:"40"}];//用户身份
	//清空
	$scope.clear=function(){
		$scope.info={
            userType:"",payBack:"",accountStatus:"",orgId:"",status:"",payMoneyStatus:"",statusMentor:"",
            openProvince:"全部",openCity:"全部",openRegion:"全部",
            createDateStart:moment(new Date().getTime()-24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
            createDateEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'};
	}
    $scope.clear();

    $scope.userGrid={                           //配置表格
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
        useExternalPagination: true,
        columnDefs:[                           //表格数据
            { field: 'userCode',displayName:'用户ID',width:130},
            { field: 'orgName',displayName:'所属组织',width:130 },
            { field: 'isOpen',displayName:'是否外放',width:130 ,cellFilter:"formatDropping:" + angular.toJson($scope.isOpenList)},
            { field: 'userName',displayName:'姓名',width:130},
            { field: 'nickName',displayName:'昵称',width:130},
            { field: 'phone',displayName:'手机号',width:150 },
            { field: 'statusMentor',displayName:'优秀导师',width:150 },
            { field: 'weixinCode',displayName:'微信号',width:150 },
            { field: 'totalProfit',displayName:'总收益',width:150 },
            { field: 'userType',displayName:'代理身份',width:120},
            { field: 'status',displayName:'状态',width:120,cellFilter:"formatDropping:" + angular.toJson($scope.statusList)},
            { field: 'accountStatus',displayName:'是否开户',width:120},
            { field: 'createDateStr',displayName:'入驻时间',width:150},
            { field: 'repaymentUserNo',displayName:'超级还用户编号',width:150 },
            { field: 'receiveUserNo',displayName:'收款商户编号',width:150 },
            { field: 'topOneCode',displayName:'一级代理ID',width:150 },
            { field: 'topTwoCode',displayName:'二级代理ID',width:150 },
            { field: 'topThreeCode',displayName:'三级代理ID',width:150 },
            { field: 'openProvince',displayName:'省',width:150 },
            { field: 'openCity',displayName:'市',width:150 },
            { field: 'openRegion',displayName:'区',width:150 },
            { field: 'remark',displayName:'备注',width:150 },
            { field: 'payMoneyStatus',displayName:'是否缴费',width:150 },
            { field: 'toagentDateStr',displayName:'支付时间',width:150 },
            { field: 'id',displayName:'操作',width:200,pinnedRight:true,
                cellTemplate:
                '<a class="lh30" ng-show="grid.appScope.hasPermit(\'superBank.userInfoDetail\')" target="_blank" ' +
				'ui-sref="superBank.userInfoDetail({userCode:row.entity.userCode})">详情</a>'
                + '<a class="lh30" ng-show="grid.appScope.hasPermit(\'superBank.updateUserInfo\')" ' +
				'ui-sref="superBank.updateUserInfo({userCode:row.entity.userCode})"> | 修改</a>'
                + '<a class="lh30" ng-show="row.entity.status==1&&grid.appScope.hasPermit(\'superBank.updateUserStatus\')" ng-click="grid.appScope.updateUserStatus(row.entity.userCode,2)"> | 冻结</a>'
                + '<a class="lh30" ng-show="row.entity.status==2&&grid.appScope.hasPermit(\'superBank.updateUserStatus\')" ng-click="grid.appScope.updateUserStatus(row.entity.userCode,1)"> | 解冻</a>'
            }
        ],
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.paginationOptions.pageNo = newPage;
                $scope.paginationOptions.pageSize = pageSize;
                $scope.selectInfo();
            });
        }
    };

    //省市区
    $scope.list = LAreaDataBaidu;
    $scope.c = function () {
        $scope.selected2 = "";
        $scope.selected3 = "";
    };

    $scope.c2 = function () {
        $scope.selected3 = "";
    };
	//条件分页查询
	$scope.selectInfo = function(){
        $scope.submitting = true;
        $scope.loadImg = true;
        if ($scope.selected != undefined && $scope.selected != null) {
            $scope.info.openProvince = $scope.selected.name;
            if ($scope.selected2 != undefined && $scope.selected2 != null) {
                $scope.info.openCity = $scope.selected2.name;
                if ($scope.selected3 != undefined && $scope.selected3 != null) {
                    $scope.info.openRegion = $scope.selected3.name;
                } else {
                    $scope.info.openRegion = "";
                }
            } else {
                $scope.info.openCity = "";
                $scope.info.openRegion = "";
            }
        } else {
            $scope.info.openProvince = "";
            $scope.info.openCity = "";
            $scope.info.openRegion = "";
        }
        $http({
            url: "superBank/userInfoManager?pageNo=" + $scope.paginationOptions.pageNo + "&pageSize=" + $scope.paginationOptions.pageSize,
            data: $scope.info,
            method: 'POST'
        }).success(function (result) {
            $scope.submitting = false;
            $scope.loadImg = false;
            if (result.status) {
                $scope.userGrid.data = result.data.result;
                $scope.userGrid.totalItems = result.data.totalCount;
            } else {
                $scope.notice(result.msg);
            }
        }).error(function () {
            $scope.submitting = false;
            $scope.loadImg = false;
            $scope.notice("系统异常，请稍后再试");
        });
        getUserTotal();
	};

	function getUserTotal(){
        $http({
            url: "superBank/getUserTotal?pageNo=" + $scope.paginationOptions.pageNo + "&pageSize=" + $scope.paginationOptions.pageSize,
            data: $scope.info,
            method: 'POST'
        }).success(function (result) {
            $scope.submitting = false;
            $scope.loadImg = false;
            if (result.status) {
                $scope.totalMap = result.data;
            } else {
                $scope.notice(result.msg);
            }
        }).error(function () {
            $scope.submitting = false;
            $scope.loadImg = false;
            $scope.notice("系统异常，请稍后再试");
        });
    }

	// 导出
	$scope.exportInfo = function () {
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
                location.href="superBank/exportUserInfo?info="+encodeURI(encodeURI(angular.toJson($scope.info)));
            }
        });
    };

	 //查询所有银行家组织
	 $scope.orgInfoList = [];
	 $scope.getOrgInfoList = function () {
        $http({
            url:"superBank/getOrgInfoList",
            method:"POST"
        }).success(function(msg){
            if(msg.status){
                $scope.orgInfoList = msg.data;
                $scope.orgInfoList.unshift({orgId:"",orgName:"全部"});
            }
        }).error(function(){
            $scope.notice("获取组织信息异常");
        })
    };
    $scope.getOrgInfoList();

    
    //冻结解冻弹框确定提交操作
    $scope.handleRemark='';
    $scope.handleUserCode='';
    $scope.handleStatus=1;
    $scope.updateHandleStatusCommit=function(){
        $http({
            url:"superBank/updateUserStatus",
            method:"POST",
            data:{userCode: $scope.handleUserCode, status: $scope.handleStatus, operReason: $scope.handleRemark}
        }).success(function(result){
            $scope.notice(result.msg);
            $('#updateUserStatusModal').modal('hide');
           if(result.status){
               $scope.selectInfo();
           }
        });
        
    };

   //冻结解冻弹框关闭取消操作
    $scope.updateHandleStatusColse=function(){
        $('#updateUserStatusModal').modal('hide');
        $scope.selectInfo();
    };
    
   //冻结解冻弹框
    $scope.updateUserStatus = function(userCode, status){
        var title = ""
        if(status == 1){
            title = "确定解冻";
        } else {
            title = "确定冻结";
        }
        $('#myTitle').html(title);
        $scope.handleUserCode = userCode;
        $scope.handleStatus = status;
        $scope.handleRemark='';
        $('#updateUserStatusModal').modal('show');
    }
    
    //post查询
    $scope.getAreaList=function(name,type,callback){
        if(name == null || name=="undefine"){
            return;
        }
        $http.post('areaInfo/getAreaByName.do','name='+name+'&&type='+type,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
        ).success(function(data){
            callback(data);
        }).error(function(){
        });
    }
    $scope.provinceGroup = [{name:"全部"}];
    $scope.cityGroup = [{name:"全部"}];
    $scope.areaGroup = [{name:"全部"}];
    //省
    $scope.getAreaList(0,"p",function(data){
        $scope.provinceGroup = data;
        $scope.provinceGroup.unshift({name:"全部"});
        $scope.info.openCity = "全部";
        $scope.info.openRegion = "全部";
    });
    //市
    $scope.getCities = function() {
        $scope.getAreaList($scope.info.openProvince,"",function(data){
            $scope.cityGroup = data;
            $scope.cityGroup.unshift({name:"全部"});
            $scope.info.openCity = "全部";
            $scope.info.openRegion = "全部";
        });
    }
    //县
    $scope.getAreas = function() {
        $scope.getAreaList($scope.info.openCity,"",function(data){
            $scope.areaGroup = data;
            $scope.areaGroup.unshift({name:"全部"});
            $scope.info.openRegion = "全部";
        });
    }
    $scope.condition = {conditionStatus: false, conditionMsg: '全部条件'};
    $scope.changeCondition = function(){
        $scope.condition.conditionStatus = !$scope.condition.conditionStatus;
        if($scope.condition.conditionStatus){
            //清空子菜单的条件
            clearSubCondition();
            $scope.condition.conditionMsg = '清空收起';
        } else {
            $scope.condition.conditionMsg = '全部条件';
        }
    }
    function clearSubCondition(){
        $scope.info.openProvince = "全部";
        $scope.info.openCity = "全部";
        $scope.info.openRegion = "全部";
        $scope.info.toagentDateStrat = null;
        $scope.info.toagentDateEnd = null;
        $scope.info.remark = null;
    }
});