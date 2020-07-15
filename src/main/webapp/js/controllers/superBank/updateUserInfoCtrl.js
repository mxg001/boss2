/**
 * 超级银行家用户修改
 */
angular.module('inspinia').controller('updateUserInfoCtrl',function($scope,$http,$state,$stateParams,i18nService){
    //数据源
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
	$scope.debitCreditSideList=[{text:"全部",value:""},{text:"减少",value:"debit"},
								{text:"增加",value:"credit"},{text:"冻结",value:"freeze"},
								{text:"解冻",value:"unfreeze"}];
    $scope.payBackList=[{text:"未退款",value:"0"},{text:"已退款",value:"1"}];
    $scope.statusMentorList = [{text:"否",value:"0"},{text:"是",value:"1"}];
    $scope.submitBaseInfoStatus = false;//提交修改资料
    //清空
    $scope.clear=function(){
        $scope.accountInfoRecord = {
        	debitCreditSide :"",
			userId:$stateParams.userCode,
            recordTimeStart:moment(new Date().getTime()-24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
            recordTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'
        };
    }
    $scope.clear();

	//获取用户详情
	$scope.getUserDetail = function(){
        $http({
			url:'superBank/selectUserDetail?userCode='+$stateParams.userCode,
			method:'GET'
		}).success(function(result) {
			if (result.status) {
                $scope.baseInfo = result.data.userInfo;
                $scope.userCard = result.data.userCard;
                if($scope.baseInfo.accountStatus == '1'){
                    $scope.getUserAccountInfo();
                    $scope.getUserAccountDetail();
                }
			} else {
                $scope.notice(result.msg);
			}
		}).error(function(){
			$scope.notice("系统异常，请稍后再试");
		});
	};

	//获取用户账户数据
	$scope.getUserAccountInfo = function(){
        $http({
            url:'superBank/getUserAccountInfo?userCode='+$stateParams.userCode,
            method:'GET'
        }).success(function(result) {
            if (result.status) {
                $scope.accountBalances.data = result.data;
            } else {
                $scope.notice(result.msg);
			}
        }).error(function(){
            $scope.notice("系统异常，请稍后再试");
        });
	};

	//获取用户账户明细
    $scope.getUserAccountDetail = function(){
        $scope.submitting = true;
        $scope.loadImg = true;
        $http({
			url:"superBank/getUserAccountDetail?pageNo=" + $scope.paginationOptions.pageNo + "&pageSize=" + $scope.paginationOptions.pageSize,
			data:$scope.accountInfoRecord,
			method:"POST"
		}).success(function(result){
            $scope.submitting = false;
            $scope.loadImg = false;
            if(result.status){
                $scope.accountDetail.data = result.data.list;
                $scope.accountDetail.totalItems = result.data.total;
            }else{
                $scope.notice(result.msg);
            }
        }).error(function(){
            $scope.notice("系统异常，请稍后再试");
        });
    };

    $scope.getUserDetail();
    // $scope.getUserAccountInfo();
    // $scope.getUserAccountDetail();
    

    $scope.submit = function () {
    	var name = $scope.baseInfo.nickName;
    	var webchatid = $scope.baseInfo.weixinCode;
    	var phone = $scope.baseInfo.phone;
    	var realname = $scope.baseInfo.userName;
    	if(name==undefined || name.length==0){
    		$scope.notice("请填写昵称");
    		return ;
    	}
    	if(webchatid==undefined || webchatid.length==0){
    		$scope.notice("请填写微信号");
    		return ;
    	}
    	if(phone==undefined || phone.length==0){
    		$scope.notice("请填写手机号");
    		return ;
    	}
    	if(realname==undefined || realname.length==0){
    		$scope.notice("请填写姓名");
    		return ;
    	}
        $scope.baseInfo.nickName=encodeURI(name);
        $scope.submitBaseInfoStatus = true;
        $http({
            url:"superBank/updateUserInfo",
            data:$scope.baseInfo,
            method:"POST"
        }).success(function(result){
            $scope.submitBaseInfoStatus = false;
            $scope.notice(result.msg);
            if(result.status){
                $state.transitionTo('superBank.userManage',null,{reload:true});
            }
        }).error(function () {
            $scope.submitBaseInfoStatus = false;
            $scope.notice("系统异常，请稍后再试");
        })
    };

    $scope.updateCardModal = function(){
        if(!$scope.userCard){
            $scope.notice("结算卡不能为空");
            return ;
        }
        $scope.newCard = {cnapsNo:"",cardNo:"",bankProvince:"",bankCity:"",
                            id:$scope.userCard.id,
                            userCode:$scope.userCard.userCode,
                            accountPhone:$scope.userCard.accountPhone};
        $('#updateCardModal').modal('show');
    }
    $scope.cancel = function(){
        $('#updateCardModal').modal('hide');
    }
    $scope.getAreaList = function (name, type, callback) {
        if (name == null || name == "undefine") {
            return;
        }
        $http.post('areaInfo/getAreaByName', 'name=' + name + '&&type=' + type,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
        ).success(function (data) {
            callback(data);
        }).error(function () {
        });
    }

    //省，加载页面时自动触发
    $scope.getAreaList(0, "p", function (data) {
        $scope.provinceList = data;
    });
    //修改省时，获取市的信息
    $scope.getCities = function () {
        $scope.areaList = [];
        $scope.getAreaList($scope.newCard.bankProvince, "", function (data) {
            $scope.cityList = data;
        });
    }
    //修改市时，获取区的信息
    $scope.getAreas = function () {
        $scope.getAreaList($scope.newCard.bankCity, "", function (data) {
            $scope.areaList = data;
        });
    };
    $scope.getCardInfo = function(cardNo){
        if(!cardNo){
            return;
        }
        $http({
            url:"superBank/getCardInfo?cardNo=" + cardNo,
            method:"GET"
        }).success(function(result){
            if(result.status){
                $scope.cardMsg = "";
                $scope.newCard.bankName = result.data.bankName;
                $scope.getBankList($scope.newCard.bankName, $scope.newCard.bankCity);
            } else {
                $scope.cardMsg = result.msg;
                $scope.notice(result.msg);
            }
        })
    };
    $scope.bankList = [{cnapsNo:"",bankName:"请选择"}];
    //获取支行集合
    $scope.getBankList = function(bankName, cityName){
        if(!bankName || !cityName){
            return;
        }
        $http({
            method:"POST",
            url:"superBank/getPosCnaps",
            data:{"bankName":bankName, "address":cityName}
        }).success(function(result){
            if(result.status){
                if(result.data && result.data.length > 0){
                    $scope.bankList = result.data;
                    $scope.bankList.unshift({cnapsNo:"",bankName:"请选择"});
                }
            } else {
                $scope.notice(result.msg);
            }
        })
    }
    //修改结算卡
    $scope.updateUserCard = function(){
        $scope.newCard.cnapsNo = $scope.newCard.cnaps.cnapsNo;
        $scope.newCard.bankBranchName = $scope.newCard.cnaps.bankName;
        $http({
            url:"superBank/updateUserCard",
            data: $scope.newCard,
            method:"POST"
        }).success(function(result){
            $scope.notice(result.msg);
            if(result.status){
                $scope.userCard.cardNo = $scope.newCard.cardNo;
                $scope.userCard.accountPhone = $scope.newCard.accountPhone;
                $scope.cancel();
            }
        })
    };

    //账户余额
	$scope.accountBalances = {                 //配置表格
        columnDefs:[                           //表格数据
            { field: 'accountNo',displayName:'账号'},
            { field: 'balance',displayName:'余额'},
            { field: 'controlAmount',displayName:'冻结金额' }
        ]
    };

	//账户明细
	$scope.accountDetail={                           //配置表格
			paginationPageSize:10,                  //分页数量
			paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
			useExternalPagination: true,
			enableHorizontalScrollbar: 1,        //横向滚动条
			enableVerticalScrollbar : 1,
			columnDefs:[                           //表格数据
				{ field: 'recordDate',displayName:'记账时间',cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
				{ field: 'debitCreditSide',displayName:'操作',cellFilter:"formatDropping:" + angular.toJson($scope.debitCreditSideList)},
				{ field: 'recordAmount',displayName:'金额' },
				{ field: 'avaliBalance',displayName:'可用余额' }
			],
			onRegisterApi: function(gridApi) {
				$scope.gridApi = gridApi;
				$scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
					$scope.paginationOptions.pageNo = newPage;
					$scope.paginationOptions.pageSize = pageSize;
					$scope.getUserAccountDetail();
				});
			}
		};

});
