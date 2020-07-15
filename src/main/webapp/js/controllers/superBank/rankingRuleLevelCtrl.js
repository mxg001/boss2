/**
 * 超级银行家贷款订单详情
 */
angular.module('inspinia', ['infinity.angular-chosen', 'angularFileUpload']).controller('rankingRuleLevelCtrl',function($scope,$http,$state,$stateParams,i18nService,$document,SweetAlert,$timeout,FileUploader){
	$scope.dataTypeList=[{text:"请选择",value:""},{text:"收益金额",value:"0"},{text:"会员数(仅指已缴费的人)",value:"1"},{text:"用户数(包括未缴费的人)",value:"2"},{text:"已完成订单数",value:"3"}];//订单状态
	$scope.numList = ["一","二","三","四","五","六","七","八","九","十","十一","十二","十三","十四","十五","十六","十七","十八","十九","二十"];
	$scope.orgNum = 0;
	$scope.selectOrgList =[];
	$scope.currencyRegex = /^(?!(0[0-9]{0,}$))[0-9]{1,}[.]{0,}[0-9]{0,}$/;
	$scope.pointRegex = /^(\d+\s*,\s*)*\d+$/;
	$scope.numberRegex = /^\+?[1-9]\d*$/;
	$scope.imgIsEdit = false;
	//$scope.totalMoney = 0;
	
	$http({
        url:"superBank/rankingRuleLevel?id=" + $stateParams.id +"&operate="+$stateParams.operate,
        method:"GET"
    }).success(function(result){
        if (result.status){
            $scope.baseInfo = result.data;
            $scope.operate = $stateParams.operate;
            if($stateParams.operate == '0'){
            	$scope.rulePageName = '新增排行榜规则';
            	$scope.isEdit = true;
            	$scope.baseInfo.ruleType=2;
            	$scope.baseInfo.orgType=0;
            	$scope.baseInfo.status= 1;
            	$scope.baseInfo.openOrg = -1;
            	$scope.baseInfo.openOrgInfo = '-1-全部组织';
            	$scope.baseInfo.levelList=[{levelNum:1,level:'一',prizePeopleCount:1,singlePrize:''},{levelNum:2,level:'二',prizePeopleCount:1,singlePrize:''},{levelNum:3,level:'三',prizePeopleCount:1,singlePrize:''}];
            	$scope.orgInfoList = $scope.baseInfo.orgList;
            	$scope.baseInfo.dataType='';
            }else if($stateParams.operate == '1'){
            	$scope.isEdit = true;
            	$scope.rulePageName = '修改排行榜规则';
            	$scope.orgInfoList = $scope.baseInfo.orgList;
            	$scope.selectOrgList = $scope.baseInfo.selectOrgs;
            	$scope.checkBusiesType($scope.baseInfo.busiType);
            }else if($stateParams.operate == '2'){
            	$scope.isEdit = false;
            	$scope.rulePageName = '排行榜规则详情';
            	$scope.checkBusiesType($scope.baseInfo.busiType);
            }
        } else {
           $scope.notice(result.msg);
        }
        /*  var temStartTime = $scope.baseInfo.startTime;
        var temEndTime = $scope.baseInfo.endTime;
        if(temStartTime != null){
        	$scope.baseInfo.startTime = moment(new Date(temStartTime).getTime()).format('YYYY-MM-DD hh:mm:ss');
        }
        if(temEndTime != null){
        	$scope.baseInfo.endTime = moment(new Date(temEndTime).getTime()).format('YYYY-MM-DD hh:mm:ss');
        }*/
    }).error(function () {
        $scope.notice("服务器异常，请稍后再试");
    });
	
	//获取组织
    /*$scope.getOrgList = function(){
    	
   	 $http({
            url:"superBank/getOrgInfoList",
            method:"POST"
        }).success(function(msg){
            if(msg.status){
                //$scope.orgInfoList = msg.data;
            }
        }).error(function(){
            $scope.notice("获取组织列表异常");
        });
   };
   $scope.getOrgList();*/
   
   //处理默认选中业务类型
	$scope.checkBusiesType = function(busiTypeStr){
		if(busiTypeStr != null && busiTypeStr != ""){
			$scope.busies = busiTypeStr.split("-");
			
			angular.forEach($scope.busies, function (busi) {
    			if(busi==1){
    				$scope.creditcardCheck=1;
    			}else if(busi==2){
    				$scope.loanCheck=1;
    			}else if(busi==3){
    				$scope.merchCheck=1;
    			}else if(busi==4){
    				$scope.inteCheck=1;
    			}else if(busi==5){
    				$scope.insCheck=1;
    			}else if(busi==6){
    				$scope.creditSearchCheck=1;
    			}else if(busi==7){
    				$scope.wzCheck=1;
    			}else if(busi==8){
    				$scope.paymentCheck=1;
    			}else if(busi==9){
    				$scope.rankingCheck=1;
    			}
    		});
		}
	}
	
   $scope.selectOrg = function (index) {
	   if($scope.selectOrgList == null){
		   $scope.selectOrgList = [];
	   }
	   $scope.selectOrgList.push($scope.orgInfoList[index]);
	   $scope.orgInfoList.splice(index, 1);
   };
   
   $scope.removeOrg = function (index) {
	   if($scope.orgInfoList == null){
		   $scope.orgInfoList = [];
	   }
	   $scope.orgInfoList.push($scope.selectOrgList[index]);
	   $scope.selectOrgList.splice(index, 1);
   };
   
   $scope.confirmSelectOrg = function(){
	   if($scope.selectOrgList.length == 0){
		   $scope.notice("请选择组织");
		   return;
	   }
	   var selectOrgs = '';
	   var selectOrgInfos = '';
	   for(var i = 0;i < $scope.selectOrgList.length;i++){
		   if(i == $scope.selectOrgList.length-1){
			   selectOrgs = selectOrgs+$scope.selectOrgList[i].orgId;
			   selectOrgInfos = selectOrgInfos + ($scope.selectOrgList[i].orgId+'-'+$scope.selectOrgList[i].orgName);
		   }else{
			   selectOrgs = selectOrgs+$scope.selectOrgList[i].orgId+',';
			   selectOrgInfos = selectOrgInfos + ($scope.selectOrgList[i].orgId+'-'+$scope.selectOrgList[i].orgName)+'; ';
		   }
	   }
	   $scope.baseInfo.openOrg = selectOrgs;
	   $scope.baseInfo.openOrgInfo = selectOrgInfos;
	   $scope.baseInfo.orgType=1;
	   $scope.cancel();
   };
   
   $scope.selectOrgRad = function(){
	   if($scope.baseInfo.orgType == 0){
		   $scope.openOrgHid =  $scope.baseInfo.openOrg;
		   $scope.openOrgInfoHid =  $scope.baseInfo.openOrgInfo;
		   $scope.baseInfo.openOrg = -1;
		   $scope.baseInfo.openOrgInfo = '-1 全部组织';
	   }else{
		   $scope.selectOrgBox();
		   $scope.baseInfo.openOrg = $scope.openOrgHid;
		   $scope.baseInfo.openOrgInfo = $scope.openOrgInfoHid;
	   }
	   
   };
   
   $scope.removeLevel = function (index) {
	   if($scope.baseInfo.levelList.length == 1){
  		 $scope.notice("至少要添加一级");
  		 return;
	   }
	   $scope.baseInfo.levelList.splice($scope.baseInfo.levelList.length-1, 1);
	   $scope.countTotalAmount();
   };
   $scope.addLevel = function () {
    	if($scope.baseInfo.levelList.length >= 20){
    		 $scope.notice("最多可以生成二十级");
    		 return;
    	}
		 $scope.baseInfo.levelList.push({levelNum:$scope.baseInfo.levelList.length+1,level:$scope.numList[$scope.baseInfo.levelList.length],prizePeopleCount:1,singlePrize:''});
		 $scope.countTotalAmount();
		
	};
	
	$scope.countTotalAmount = function(){
		 var temTotalMoney = 0;
		 for(var i = 0;i<$scope.baseInfo.levelList.length;i++){
			 var prizePeople = $scope.baseInfo.levelList[i].prizePeopleCount;
			 var singlePrize = $scope.baseInfo.levelList[i].singlePrize;
			 if(prizePeople == '' || prizePeople == undefined){
				 prizePeople = 0;
			 }
			 if(singlePrize == '' || singlePrize == undefined){
				 singlePrize = 0;
			 }
			  temTotalMoney = (temTotalMoney + prizePeople*singlePrize);
		 }
		 $scope.baseInfo.levelTotalMoney = temTotalMoney.toFixed(2);
	};
	
	$scope.selectOrgBox = function(){
		if($stateParams.operate == '2'){
			return;
		}
        $('#selectOrgBox').modal('show');
    };
    
	$scope.cancel = function(){
        $('#selectOrgBox').modal('hide');
    };
  //上传图片,定义控制器路径
    $scope.uploaderLogo = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数
        removeAfterUpload: false,  //上传后删除文件
        autoUpload:true,     //文件加入队列之后自动上传，默认是false
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    $scope.uploaderLogo.filters.push({
        name: 'imageFilter',
        fn: function(item, options) {
            return this.queue.length < 1;
        }
    });
    //过滤格式
    $scope.uploaderLogo.filters.push({
        name: 'imageFilter',
        fn: function(item /*{File|FileLikeObject}*/, options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });
    $scope.uploaderLogo.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            $scope.baseInfo.advertUrl = response.url;
            $scope.imgIsEdit = true;
        }
    };
    
    //提交
    $scope.submit = function(){
    	if($scope.baseInfo.levelList.length == 0){
    		$scope.notice("每个组织中各级奖金设置必须要有一级");
    		return;
    	}
    	 var peopleCount = 0;
    	 for(var i = 0;i<$scope.baseInfo.levelList.length;i++){
			 var prizePeople = $scope.baseInfo.levelList[i].prizePeopleCount;
			 if(prizePeople == '' || prizePeople == undefined){
				 prizePeople = 0;
			 }
			  peopleCount = peopleCount + Number(prizePeople);
		 }
    	 
    	 if($scope.baseInfo.dataType=='3'){
    		 var busies = $(".busiType");//业务类型
    		 var indexStr = "";
    		 for(var i=0;i<busies.length;i++){
    			 if(busies[i].checked){
    				 if(indexStr!=""){
    					 indexStr = indexStr + "-" + busies[i].value;
    				 }else{
    					 indexStr = busies[i].value;
    				 }
    				 
    			 } 
    		 }
    		 if(indexStr==""){
    			 $scope.notice("至少选择一种业务类型");
    			 return ;
    		 }else{
    			 $scope.baseInfo.busiType = indexStr;
    		 }
    	 }
    	 
		 if(peopleCount > 200){
			 $scope.notice("各级奖金设置获奖总人数不能超过200人");
			 return;
		 }
    	  
    	 /*if($scope.baseInfo.totalAmount < $scope.totalMoney){
    		 $scope.notice("活动总奖金>=合计各级奖金*组织个数    排行榜才会生成");
    		 return;
    	 }*/
    	 /*if($scope.baseInfo.startTime == '' || $scope.baseInfo.endTime == ''){
    		 $scope.notice("活动开始时间和结束时间不能为空");
    		 return;
    	 }*/
    	 if( $scope.baseInfo.advertUrl == null || $scope.baseInfo.advertUrl == ''){
    		 $scope.notice("广告位图片为空或未同步，请稍等片刻提交！");
    		 return;
    	 }
    	
    	
        $scope.submitting = true;
        $http({
            url: "superBank/saveRankingRuleLevel",
            method: "POST",
            data: $scope.baseInfo
        }).success(function(result){
            $scope.submitting = false;
            $scope.notice(result.msg);
            if(result.status){
                $state.transitionTo('superBank.rankingRule',null,{reload:true});
               
            }
        });
    };

    
    $scope.isOrder = false;
    $scope.changeDataType = function(){
    	if($scope.baseInfo.dataType=="3"){
    		//显示业务
    		 $scope.isOrder = true;
    	}else{
    		//隐藏业务
    		$scope.isOrder = false;
    	}
    }
    
});