/**
 * 查询业务产品
 */
angular.module('inspinia',['uiSwitch']).controller('queryProductCtrl',function($scope,$http,$state,$stateParams,i18nService,$document,SweetAlert){
	//数据源
	i18nService.setCurrentLang('zh-cn');
	$scope.allowIndividualApplySelect = [{text:"全部",value:null},{text:"是",value:"1"},{text:"否",value:"0"}]
	$scope.allowIndividualApplySelectStr = [{text:"是",value:"1"},{text:"否",value:"0"}]
	$scope.isOemSelect = [{text:"全部",value:null},{text:"是",value:"1"},{text:"否",value:"0"}]
  $scope.allowIndividualApplyStr = angular.toJson($scope.allowIndividualApplySelectStr );
	$scope.bool = [{text:'全部',value:null},{text:'否',value:'0'},{text:'是',value:'1'}];
	$scope.type = [{text:'全部',value:null},{text:'个人',value:'1'},{text:'个体商户',value:'2'},{text:'企业商户',value:'3'}];
	$scope.team = [{text:'全部',value:null}];
	$scope.hardwareTypes = [{text:'全部',value:null}];	
	$scope.baseInfo = {};
	$scope.typeStr = angular.toJson($scope.type);
	$scope.boolStr = angular.toJson($scope.bool);
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
    $scope.effectiveStatusList = [{text:'全部',value:null},{text:'失效',value:0},{text:'生效',value:1}];

    //reset
    $scope.resetForm=function(){
        $scope.baseInfo = {isOem:null,allowIndividualApply:null,bpType:null,bpName:null,teamId:null,hpId:null,effectiveStatus:null,
          createTimeBegin:moment(new Date().getTime()-7*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',createTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD'+' 23:59:59')};
    }
    $scope.resetForm();
  //是否校验时间
  isVerifyTime = 0;//校验：1，不校验：0
  setBeginTime=function(setTime){
    $scope.baseInfo.createTimeBegin = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
  }
  setEndTime=function(setTime){
    $scope.baseInfo.createTimeEnd = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
  }
	$http.get('teamInfo/queryTeamName.do').success(function(msg){
		for(var i=0;i<msg.teamInfo.length;i++){
			$scope.team.push({text:msg.teamInfo[i].teamName,value:msg.teamInfo[i].teamId});
		}
	});
	
	$http.get('hardwareProduct/selectAllInfo.do').success(function(msg){
		for(var i=0;i<msg.length;i++){
			$scope.hardwareTypes.push({text:msg[i].typeName,value:msg[i].hpId});
		}
	});
	
	//查询
	$scope.query=function(){
		$http.post('businessProductDefine/selectProduct.do',"baseInfo="+angular.toJson($scope.baseInfo)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+
				$scope.paginationOptions.pageSize,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
				.success(function(data){
					if(!data)
						return;
					$scope.productGrid.data =data.result; 
					$scope.productGrid.totalItems = data.totalCount;
		})
	}
	$scope.query();
	$scope.productGrid = {
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs: [
            {field: 'bpName',displayName: '业务产品名称',pinnable: false,sortable: false},
            {field: 'agentShowName',displayName: '代理商展示名称',pinnable: false,sortable: false},
            {field: 'bpType',displayName: '类型',pinnable: false,sortable: false,cellFilter:"formatDropping:" + $scope.typeStr},
            {field: 'isOem',displayName: '是否OEM',pinnable: false,sortable: false,cellFilter:"formatDropping:" + $scope.boolStr},
            {field: 'teamName',displayName: '所属组织',pinnable: false,sortable: false},
            {field: 'groupNo',displayName: '群组号',pinnable: false,sortable: false},
            {field: 'allowIndividualApply',displayName: '是否允许单独申请',pinnable: false,sortable: false,cellFilter:"formatDropping:" + $scope.allowIndividualApplyStr},
            {field: 'effectiveStatus',displayName: '状态',width:150,pinnable: false,sortable: false,editable:true,cellTemplate:
                '<span ng-show="grid.appScope.hasPermit(\'businessProductDefine.updateEffectiveStatus\')&&row.entity.effectiveStatus==1"><switch class="switch2 switch-s" ng-model="row.entity.effectiveStatus" ng-change="grid.appScope.updateEffectiveStatus(row.entity)" /></span>'
                +'<span ng-show="!grid.appScope.hasPermit(\'businessProductDefine.updateEffectiveStatus\')||row.entity.effectiveStatus==0"> <span class="lh30" ng-show="row.entity.effectiveStatus==1">生效</span><span class="lh30" ng-show="row.entity.effectiveStatus==0">失效</span></span>'
            },
            {field: 'createTime',displayName: '创建时间',pinnable: false,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
            {field: 'createPerson',displayName: '创建人',pinnable: false,sortable: false,width:"100"},
            {field: 'action',displayName: '操作',pinnable: false,sortable: false,editable:true,cellTemplate:
            	'<div class="lh30"><a ng-show="grid.appScope.hasPermit(\'businessProductDefine.detail\')"  ui-sref="service.productDetail({id:row.entity.bpId})">详情</a><a ng-show="grid.appScope.hasPermit(\'businessProductDefine.edit\')" ng-click="grid.appScope.editProduct(row.entity.bpId)"> | 修改</a></div>'}
        ],
        onRegisterApi: function(gridApi) {                
            $scope.gridApi = gridApi;
            $scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
	          	$scope.paginationOptions.pageNo = newPage;
	          	$scope.paginationOptions.pageSize = pageSize;
	            $scope.query();
	     });
        }
	 };
	$scope.query();

    $scope.updateEffectiveStatus=function(entity){
        if(entity.effectiveStatus){
        	$scope.notice("失效的业务产品不能再生效");
            entity.effectiveStatus = false;
        	return;
        } else {
            $scope.serviceText = "业务产品失效后，将不能恢复正常状态，请确认是否继续！";
        }
        SweetAlert.swal({
                title: $scope.serviceText,
//            text: "服务状态为关闭后，不能正常交易!",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    if(entity.effectiveStatus==true){
                        entity.effectiveStatus=1;
                    } else if(entity.effectiveStatus==false){
                        entity.effectiveStatus=0;
                    }
                    $http.post("businessProductDefine/updateEffectiveStatus",{bpId:entity.bpId,effectiveStatus:entity.effectiveStatus})
                        .success(function(data){
                            if(!data.status){
                                if(entity.effectiveStatus==true){
                                    entity.effectiveStatus = false;
                                } else {
                                    entity.effectiveStatus = true;
                                }
                            }
							$scope.notice(data.msg);
                        })
                } else {
                    if(entity.effectiveStatus==true){
                        entity.effectiveStatus = false;
                    } else {
                        entity.effectiveStatus = true;
                    }
                }
            });

    };
	
	//判断能否修改业务产品
	$scope.editProduct = function(id){
		$http.get('businessProductDefine/isUsed/'+id).success(function(msg){
			if(msg.flag){
				$scope.notice('此业务产品已被使用,只能修改部分数据');
				$state.transitionTo('service.editProductBase',{"id":id},{reload:true});
				return false;
			}
			$state.transitionTo('service.editProduct',{"id":id},{reload:true});
		}).error(function(){
		});
	}
	
	//页面绑定回车事件
	$document.bind("keypress", function(event) {
		$scope.$apply(function (){
			if(event.keyCode == 13){
				$scope.query();
			}
		})
	});

  //导出列表
	$scope.exportQueryProduct = function(){

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
          $scope.exportInfoClick("businessProductDefine/exportQueryProduct",{"baseInfo":angular.toJson($scope.baseInfo)});
        }
      });
  }

});