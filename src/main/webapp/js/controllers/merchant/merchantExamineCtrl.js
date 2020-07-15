/**
 * 审核查询
 */

angular.module('inspinia', ['infinity.angular-chosen']).controller('merchantExamineCtrl',function(i18nService,$scope,$http,$timeout,$document,SweetAlert,$state,$stateParams,$compile,$filter){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	$scope.IsYN=[{text:"全部",value:-1},{text:"是",value:0},{text:"否",value:1}];
	$scope.termianlTypes=[{text:"全部",value:0}];
	$scope.agent = [{text:"全部",value:""}];
	$scope.userNames=[]
	$scope.activityTypes=[{text:"全部",value:""}];
	$scope.statusTypes=[{text:"待平台审核",value:'2'},{text:"已转自动审件",value:'5'},{text:"自动审核失败",value:'3'},{text:"复审退件",value:'999'}];
	// $scope.recommendedSources=[{text:"全部",value:""},{text:"正常注册",value:"0"},{text:"微创业",value:"1"},{text:"代理商分享",value:"2"},{text:"超级盟主",value:"3"}];

	$scope.itemSourceSelect= angular.copy($scope.merItemSourceList);
	$scope.itemSourceStr=angular.toJson($scope.itemSourceSelect);
	$scope.info={sTime:moment(new Date().getTime()-6*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
            eTime:moment(new Date().getTime()).format('YYYY-MM-DD'+' 23:59:59'),
            auditorId:"",cardId:"",mbpId:"",merchantNo:"",agentName:"",
			agentNode:-1,productType:"-1",termianlType:"-1",mobilephone:"",activityType:"",
			recommendedSource:"",status:"2",itemSource:"",merchantType:"",authChannel:"",autoMbpChannel:""};

	$scope.authChannels = [{text:"全部",value:""}];
	$scope.huoTiChannels = [{text:"全部",value:""}];
	$http.get('merchantBusinessProduct/queryHuoTiChannels').success(function(data){
		for(var i=0;i<data.length;i++){
			$scope.huoTiChannels.push({text:data[i].channel_name,value:data[i].channel_code});
		}
	});
	$http.get('merchantBusinessProduct/queryAuthChannels').success(function(res){
		for(var i=0;i<res.data.length;i++){
			$scope.authChannels.push({text:res.data[i].sys_name,value:res.data[i].sys_value});
		}
	});

	$http.get('terminalInfo/selectAllActivityType.do')
	.success(function(result){
		if(!result){
			return;
		}
		for(var i=0; i<result.length; i++){
 	   		$scope.activityTypes.push({value:result[i].sys_value,text:result[i].sys_name});
 	   	}

	})
	//业务产品
	$http.get('businessProductDefine/selectAllInfo.do')
	.success(function(largeLoad) {
		if(!largeLoad)
			return
		$scope.productTypes=largeLoad;
		$scope.productTypes.splice(0,0,{bpId:"-1",bpName:"全部"});
	});
	
	//平台审核人
	$http.get('merchantInfo/selectAllUserBox')
	.success(function(largeLoad) {
		$scope.userNames=largeLoad;
		$scope.userNames.splice(0,0,{id:"",userName:"全部"});
	});
	
	//机具类型
	$http.get('hardwareProduct/selectAllInfo.do')
	.success(function(result){
		if(!result)
			return;
		$scope.termianlTypes=result;
		$scope.termianlTypes.splice(0,0,{hpId:"-1",typeName:"全部"});
	})
	
	//模糊查询
	$scope.selectInfos=function(){
		if ($scope.loadImg) {
			return;
		}
		if(!($scope.info.sTime && $scope.info.eTime)){
			$scope.notice("创建时间不能为空");
			return;
		}
		var stime = new Date($scope.info.sTime).getTime();
		var etime = new Date($scope.info.eTime).getTime();
		if ((etime - stime) > (365 * 24 * 60 * 60 * 1000)) {
			$scope.notice("创建时间范围不能超过365天");
			return;
		}
		$scope.loadImg = true;
		$http.post(
			'merchantBusinessProduct/selectByStatusParam',
			 "info="+angular.toJson($scope.info)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
			 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
		).success(function(data){
			//响应成功
			if(data.status){
				$scope.gridOptions.data = data.page.result; 
				$scope.gridOptions.totalItems = data.page.totalCount;
			}else{
				$scope.notice(data.msg);
			}
			$scope.loadImg = false;
		}).error(function(data){
			$scope.notice("查询失败");
			$scope.loadImg = false;
		});
	}
	
	$scope.selectInfos();

	//清空
	$scope.clear=function(){
		$scope.info={sTime:moment(new Date().getTime()-6*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
	            eTime:moment(new Date().getTime()).format('YYYY-MM-DD'+' 23:59:59'),
	            auditorId:"",cardId:"",mbpId:"",merchantNo:"",agentName:"",
				agentNode:-1,productType:"-1",termianlType:"-1",mobilephone:"",activityType:"",
				recommendedSource:"",status:"2"};
	}

    //批量转自动审件
    $scope.toAutoCheckBatch=function(){
        var merchantSelectedList = $scope.gridApi.selection.getSelectedRows();
        var idList = [];
        angular.forEach(merchantSelectedList, function(item){
            idList[idList.length] = item.id;
        });
        $http.post('merchantBusinessProduct/autoCheckBatch.do',angular.toJson(idList))
            .success(function(data){
                if(data.bols){
                    $scope.getTextes("批量转自动审件",data.msg)
                    $scope.selectInfos();
                }else{
                    $scope.notice(data.msg);
                }
            })
    }

    $scope.getTextes = function (title,text) {
        SweetAlert.swal({
            title: title,
            text: text,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "确认",
        });

    }

	//代理商
	 $http.post("agentInfo/selectAllInfo")
  	 .success(function(msg){
  			//响应成功
  			//$scope.agent.push({value:null,text:'全部'});
  		 	for(var i=0; i<msg.length; i++){
	    		$scope.agent.push({value:msg[i].agentNo,text:msg[i].agentName});
	    	}
  	});
	$scope.paginationOptions=angular.copy($scope.paginationOptions);	


	$scope.merSelect=function(row){
		$scope.gridApi.selection.clearSelectedRows();
		$scope.gridApi.selection.selectRow(row.entity);
		$http.get('merchantBusinessProduct/merchantExamineDetailBefor?mbpId='+row.entity.id)
			.success(function(data) {
				if(data.bols){
					//页面跳转实现打开新窗口
					window.open('welcome.do#/merchant/MerExamineDetail/'+row.entity.id, 'view_window');
					//$state.transitionTo('merchant.MerExamineDetail',{mertId:row.entity.id},{reload:true});
				}else {
					$scope.notice(data.msg);
				}
			});

	};
	 $scope.gridOptions={                           //配置表格
			  paginationPageSize:10,                  //分页数量
			  paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
			  useExternalPagination: true,  
		      columnDefs:[                           //表格数据
				  { field: 'id',displayName:'商户进件编号',width:150},
				  { field: 'teamName',displayName:'所属组织',width:150},
				  { field: 'merchantNo',displayName:'商户编号',width:150},
		         { field: 'merchantName',displayName:'商户名称',width:150 },
                  { field: 'merchantType',displayName:'商户类型',width:150,
                      cellFilter:"formatDropping:"+ angular.toJson($scope.merchantTypeLists)
                  },
		         { field: 'mobilePhone',displayName:'商户手机号',width:150 },
		         { field: 'bpName',displayName:'业务产品',width:150 },
		         { field: 'agentName',displayName:'代理商名称',width:150 },
		         { field: 'auditNum',displayName:'初审次数',width:150 },
		         { field: 'userName',displayName:'平台审核人员',width:150 },
		         { field: 'realAuthChannelName',displayName:'鉴权通道',width:150 },
		         { field: 'huoTiChannel',displayName:'自动审核活体通道',width:150 },
		         { field: 'recommendedSource',displayName:'推广来源',width:150,
		        	 cellFilter:"formatDropping:" + angular.toJson($scope.recommendedSources)
		         },
				  { field: 'itemSource',displayName:'进件来源',width:120,cellFilter:"formatDropping:" + $scope.itemSourceStr},

				  { field: 'status',displayName:'状态',width:150,
		              cellFilter:"formatDropping:[{text:'待一审',value:1},{text:'待平台审核',value:2},{text:'已转自动审件',value:5},{text:'自动审核失败',value:3},{text:'正常',value:4},{text:'关闭',value:0}]"
		         },
		         { field: 'createTime',displayName:'创建时间',width:150,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'
		         },
		         { field: 'id',displayName:'操作',width:150,pinnedRight:true,
		               cellTemplate:'<div  class="lh30"><a ng-show="grid.appScope.hasPermit(\'merchant.examineRcored\')" ng-click="grid.appScope.merSelect(row)" target="_black" >审核</a></div>'
		         }
		      ],
		      onRegisterApi: function(gridApi) {                
		          $scope.gridApi = gridApi;
		          gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
		          	$scope.paginationOptions.pageNo = newPage;
		          	$scope.paginationOptions.pageSize = pageSize;
		             $scope.selectInfos();
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

	setBeginTime=function(setTime){
		$scope.info.sTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
	}

	setEndTime=function(setTime){
		$scope.info.eTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
	}

	//页面绑定回车事件
	$document.bind("keypress", function(event) {
		$scope.$apply(function (){
			if(event.keyCode == 13){
				$scope.selectInfos();
			}
		})
	});

});