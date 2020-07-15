/**
 * 商户查询
 */

angular.module('inspinia', ['infinity.angular-chosen']).controller('saleMerchantQueryCtrl',function($scope,$http,$state,$stateParams,$compile,$timeout,$filter,i18nService,$document){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	//数据源
	$scope.IsYN=[{text:"全部",value:-1},{text:"是",value:1},{text:"否",value:0}];
	$scope.merchantStates=[{text:"全部",value:-1},{text:"待一审",value:1},{text:"待平台审核",value:2},{text:"审核失败",value:3},{text:"正常",value:4},{text:"关闭",value:0}];
	$scope.termianlTypes=[{text:"全部",value:0}];
	$scope.agent = [{text:"全部",value:""}];
	$scope.merAccounts=[{text:"全部",value:""},{text:"是",value:1},{text:"否",value:0}]
	$scope.riskStatusaa=[{text:"全部",value:""},{text:"正常",value:1},{text:"不进不出",value:3},{text:"只进不出",value:2}]
	$scope.openStatuss=[{text:"全部",value:""},{text:"通过",value:1},{text:"不通过",value:0}]

	//清空
	$scope.clear=function(){
		isVerifyTime = 1;
		$scope.info={openStatus:"",autoCheckTimes:"",riskStatus:"",merAccount:"",acqOrgMerNo:"",cardId:"",mbpId:"",
				merchantNo:"",merchantExamineState:-1,agentName:"",agentNode:-1,productType:"-1",termianlType:"-1",mobilephone:"",
				sTime:moment(new Date().getTime()-6*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',eTime:moment(new Date().getTime()).format('YYYY-MM-DD'+' 23:59:59')};
	}
	$scope.clear();
	
	//业务产品
	$http.get('businessProductDefine/selectAllInfo.do')
	.success(function(largeLoad) {
		if(!largeLoad)
			return
		$scope.productTypes=largeLoad;
		$scope.productTypes.splice(0,0,{bpId:"-1",bpName:"全部"});
	});
	
	//机具类型
	$http.get('hardwareProduct/selectAllInfo.do')
	.success(function(result){
		if(!result)
			return;
		$scope.termianlTypes=result;
		$scope.termianlTypes.splice(0,0,{hpId:"-1",typeName:"全部"});
	})
	//代理商
	 $http.post("agentInfo/selectAllInfoSale")
   	 .success(function(msg){
   			//响应成功
   	   	for(var i=0; i<msg.length; i++){
   	   		$scope.agent.push({value:msg[i].agentNo,text:msg[i].agentName});
   	   	}
   	});
	$scope.paginationOptions=angular.copy($scope.paginationOptions);

	//是否校验时间
	isVerifyTime = 1;//校验：1，不校验：0

	keyChange=function(){
		if ($scope.info.mobilephone || $scope.info.cardId || $scope.info.merchantNo) {
			isVerifyTime = 0;
		} else {
			isVerifyTime = 1;
		}
	}

	setBeginTime=function(setTime){
		$scope.info.sTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
	}
	setEndTime=function(setTime){
		$scope.info.eTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
	}

	//查询
	$scope.selectInfo=function(){
		if ($scope.loadImg) {
			return;
		}
		if (!($scope.info.mobilephone || $scope.info.cardId || $scope.info.merchantNo)) {
			if(!($scope.info.sTime && $scope.info.eTime)){
				$scope.notice("创建时间不能为空");
				return;
			}
			var stime = new Date($scope.info.sTime).getTime();
			var etime = new Date($scope.info.eTime).getTime();
			if ((etime - stime) > (31 * 24 * 60 * 60 * 1000)) {
				$scope.notice("创建时间范围不能超过31天");
				return;
			}
		}
		$scope.loadImg = true;
		$http.post(
			'merchantBusinessProduct/selectByParamSale.do',
			 "info="+angular.toJson($scope.info)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
			 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
		).success(function(result){
			//响应成功]
			$scope.gridOptions.data = result.result; 
			$scope.gridOptions.totalItems = result.totalCount;
			$scope.loadImg = false;
		});
	}

	 $scope.gridOptions={                           //配置表格
		      paginationPageSize:10,                  //分页数量
		      paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
		      useExternalPagination: true,
			  enableHorizontalScrollbar: 1,        //横向滚动条
			  enableVerticalScrollbar : 1,  		//纵向滚动条
		      columnDefs:[                           //表格数据
		         { field: 'id',displayName:'商户进件编号',width:150},
		         { field: 'teamName',displayName:'所属组织',width:150
		         },
		         { field: 'merchantNo',displayName:'商户编号',width:150},
		         { field: 'merchantName',displayName:'商户简称' ,width:150},
		         { field: 'mobilePhone',displayName:'商户手机号',width:150 },
		         { field: 'bpName',displayName:'业务产品',width:150 },
		         { field: 'agentName',displayName:'代理商名称',width:150 },
		         { field: 'status',displayName:'状态',width:150,
		        	 cellFilter:"formatDropping:[{text:'待一审',value:1},{text:'待平台审核',value:2},{text:'审核失败',value:3},{text:'正常',value:4},{text:'关闭',value:0}]"
		         },
		         { field: 'riskStatus',displayName:'商户冻结状态',width:150,
		        	 cellFilter:"formatDropping:[{text:'正常',value:1},{text:'只进不出',value:2},{text:'不进不出',value:3}]"
		         },
		         { field: 'merAccount',displayName:'已开户',width:150,
		        	 cellFilter:"formatDropping:[{text:'否',value:0},{text:'是',value:1}]"
		         },
		         { field: 'merCreateTime',displayName:'创建时间',width:150,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'
		         },
		         { field: 'id',displayName:'操作',width:230,pinnedRight:true,
		        	 cellTemplate:'<a class="lh30"  ng-show="grid.appScope.hasPermit(\'merchantSelect.detail\')" ui-sref="sale.queryMerDetail({mertId:row.entity.id})">详情</a> '
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
	$scope.getStates =getStates;
	var oldValue="";
	var timeout="";
	function getStates(value) {
		$scope.agentt = [];
		var newValue=value;
		if(newValue != oldValue){
			if (timeout) $timeout.cancel(timeout);
			timeout = $timeout(
				function(){
					$http.post('agentInfo/selectAllInfo','item=' + value,
						{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
						.then(function (response) {
							if(response.data.length==0) {
								$scope.agentt.push({value: "", text: ""});
							}else{
								for(var i=0; i<response.data.length; i++){
									$scope.agentt.push({value:response.data[i].agentNo,text:response.data[i].agentName});
								}
							}
							$scope.agent = $scope.agentt;
							oldValue = value;
						});
				},800);
		}

	}

	//页面绑定回车事件
	$document.bind("keypress", function(event) {
		$scope.$apply(function (){
			if(event.keyCode == 13){
				$scope.selectInfo();
			}
		})
	});

    $scope.riskStatusa1=[{text:"正常",value:"1"},{text:"只进不出",value:"2"},{text:"不进不出",value:"3"}]



    
})