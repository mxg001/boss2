/**
 * 商户审核
 */

angular.module('inspinia').controller('remerchantExamineDetailCtrl',function(i18nService,$scope,$http,$state,$stateParams,$compile,$filter,$log,$uibModal){
	/**
	 * 商户详情查看
	 */
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	$scope.useables=[{text:"失效",value:0},{text:"生效",value:1}];
	$scope.rates=[];
	$scope.quotas=[];
	$scope.info={};
	$scope.asd={oppend:""};
	$scope.mbp=[];
	$scope.merExa=[];
	$scope.listMri=[];
	$scope.listMris=[];
	$scope.terminal=[];
	$scope.agent={};
	$scope.merAgent={};
	$http.get('merchantBusinessProduct/selectDetailInfo.do?ids='+$stateParams.mertId)
	.success(function(largeLoad) {
		if(largeLoad.bols){
			if(largeLoad.listmr==null||largeLoad.listmsq==null||largeLoad.mi==null||largeLoad.mbp==null||largeLoad.listel==null||largeLoad.listmri==null){
				$scope.notice("数据为空");
				return;
			}else{
				$scope.merAgent = largeLoad.merAgent;
				$scope.rates=largeLoad.listmr;
				$scope.quotas=largeLoad.listmsq;
				$scope.info=largeLoad.mi;
				$scope.agent=largeLoad.agent;
				$scope.mbp=largeLoad.mbp;
                $scope.terminal=largeLoad.tiPage.result;
                $scope.terminalTotal = largeLoad.tiPage.totalCount;
				$scope.merExa=largeLoad.listel;
				$scope.listMri=largeLoad.listmri;
				$scope.listMris=largeLoad.listmris;
				for(var i =0;i<$scope.listMri.length;i++){
					if($scope.listMri[i].mriId==6){
						$scope.info.idCardNo=$scope.listMri[i].content;
					}
				}
			}
		}else{
			$scope.notice(largeLoad.msg);
		}
	});
	
	$scope.merchantTypes=function(data){
        for(var i =0;i<$scope.merchantTypeLists.length;i++){
            if($scope.merchantTypeLists[i].value==data){
                return $scope.merchantTypeLists[i].text;
            }

        }
		// if(data==1){
		// 	return '个人';
		// }
		// if(data==2){
		// 	return '个体商户';
		// }
		// if(data==3){
		// 	return '企业商户';
		// }
	};
	
	$scope.rateTypes=[{text:"每笔固定金额",value:"1"},{text:"每笔扣率",value:"2"},{text:"每笔扣率带保底封顶",value:"3"},{text:"每笔扣率+固定金额",value:"4"}
	,{text:"单笔阶梯扣率",value:"5"}];
	
		$scope.merchantRateList={
			data:'rates',
			columnDefs:[
			    {field:'serviceName',displayName:'服务名称',width:120,pinnable:false,sortable:false}, 
			    {field:'cardType',displayName:'银行卡种类',width:120,pinnable:false,sortable:false,
			    	cellFilter: "formatDropping:"+angular.toJson($scope.cardType)
			    },
				{field:'holidaysMark',displayName:'节假日标志',width:80,pinnable:false,sortable:false,
			    	cellFilter: "formatDropping:" + $scope.holidaysStr
				},
				{field:'rateType',displayName:'费率方式',width:220,pinnable:false,sortable:false,
					cellFilter: "formatDropping:"+angular.toJson($scope.rateTypes)
				},
				{field:'oneRate',displayName:'一级代理商管控费率',width:250,pinnable:false,sortable:false
			    },
				{field:'merRate',displayName:'商户费率',width:320,pinnable:false,sortable:false},
//				{field:'efficientDate',displayName:'生效日期',width:200,pinnable:false,sortable:false,
//					cellFilter: "date:'yyyy-MM-dd'"
//				}
			]
		};
		
		$scope.merchantQuotaList={
			data:'quotas',
			columnDefs:[
	            {field:'serviceName',displayName:'服务名称',width:120,pinnable:false,sortable:false}, 
	            {field:'cardType',displayName:'银行卡种类',width:120,pinnable:false,sortable:false,
	            	cellFilter: "formatDropping:"+angular.toJson($scope.cardType)
	            }, 
	            {field:'holidaysMark',displayName:'节假日标志',width:80,pinnable:false,sortable:false,
	            	cellFilter: "formatDropping:" + $scope.holidaysStr
	            }, 
	            {field:'singleDayAmount',displayName:'单日最大交易金额(元）',width:200,pinnable:false,sortable:false,cellFilter:"currency:''"}, 
	            {field:'singleMinAmount',displayName:'单笔最小交易额(元）',width:200,pinnable:false,sortable:false,cellFilter:"currency:''"}, 
	            {field:'singleCountAmount',displayName:'单笔最大交易额（元）',width:200,pinnable:false,sortable:false,cellFilter:"currency:''"}, 
	            {field:'singleDaycardAmount',displayName:'单日单卡最大交易额（元）',width:200,pinnable:false,sortable:false,cellFilter:"currency:''"}, 
	            {field:'singleDaycardCount',displayName:'单日单卡最大交易笔数（笔）',width:200,pinnable:false,sortable:false}, 
//	            {field:'efficientDate',displayName:'生效日期',width:200,pinnable:false,sortable:false,
//	            	cellFilter: "date:'yyyy-MM-dd'"
//	            } 
			]
		};
		
		
		$scope.merchantRecords={
			data:'merExa',
			columnDefs:[
		           {field:'openStatus',displayName:'状态',pinnable:false,sortable:false,
		        	   cellFilter: "formatDropping:[{text:'审核失败',value:'2'},{text:'审核成功',value:'1'}]"
		           },
		           {field:'examinationOpinions',displayName:'内容',pinnable:false,sortable:false},
		           {field:'createTime',displayName:'时间',pinnable:false,sortable:false,
		        	   cellFilter: "date:'yyyy-MM-dd HH:mm:ss'"
		           }, 
		           {field:'realName',displayName:'操作员',pinnable:false,sortable:false}
				]
		};
		$scope.merchantDetailed={
			data:'listMri',
			columnDefs:[
		       {field:'itemName',displayName:'进件要求项名称',pinnable:false,sortable:false},
		       {field:'content',displayName:'资料',pinnable:false,sortable:false,
		    	   cellTemplate:
		    		'<div class="lh30" ng-switch on="$eval(\'row.entity.exampleType\')">'
						+'<div ng-switch-when="2">'
							+'<div>'
								+'<a ui-sref="{{COL_FIELD}}">{{$eval(\'row.entity.itemName\')}} 附件下载</a>'
							+'</div>'
						+'</div>'
						+'<div ng-switch-when="3">'
							+'<div>{{COL_FIELD}}</div>'
						+'</div>'
					+'</div>'
		       },     
	           {field:'status',displayName:'状态',pinnable:false,sortable:false,
		    	   cellFilter: "formatDropping:[{text:'待审核',value:0},{text:'审核通过',value:1},{text:'审核失败',value:2}]"
	           },
	           {field:'auditTime',displayName:'审核通过时间',width:200,pinnable:false,sortable:false,
		        	   cellFilter: "date:'yyyy-MM-dd'"
	           },
	           {field:'id',displayName:'操作',pinnable:false,sortable:false,
	        	   cellTemplate:'<label ng-show="row.entity.mriId!=5" class="lh30"><input type="checkbox" ng-click="grid.appScope.rdt(1,COL_FIELD,$eval(\'row.entity.mriId\'))" name="a{{COL_FIELD}}" value="2"/>不通过</label>'
	           }
			]
		};
		
		var strs=[];
		var opiniontext = "";
		
		function asdf(){
			$scope.asd.oppend=opiniontext;
			for(var is in strs){
				if(!strs.hasOwnProperty(is))
					continue;
				$scope.asd.oppend += strs[is];
				//if(!$scope.asd.oppend)
				$scope.asd.oppend += "\n";
			}
		}
		
		//人工修改的意见
		$scope.upds=function(){
			opiniontext=$scope.asd.oppend;
		}
		
		$scope.rdt=function(status,id,mriId){
			for(var i=0;i<$scope.listMri.length;i++){
				if($scope.listMri[i].id==id){
					if(!$scope.listMri[i].aStatus){
						$scope.listMri[i].aStatus="不通过";
						$http.get('merchantBusinessProduct/selectMriremark.do?ids='+mriId)
						.success(function(largeLoad) {
							strs[id]=largeLoad.checkMsg;
							asdf();
						})
					}	
					else if($scope.listMri[i].aStatus=="不通过"){
						$scope.listMri[i].aStatus="通过";
						delete strs[id];
						asdf();
					}	
					else if($scope.listMri[i].aStatus=="通过"){
						$scope.listMri[i].aStatus="不通过";
						$http.get('merchantBusinessProduct/selectMriremark.do?ids='+mriId)
						.success(function(largeLoad) {
							strs[id]=largeLoad.checkMsg;
							asdf();
						})
					}
				}
			}
		}
		
		$scope.rdts=function(status,id,mriId){
			for(var i=0;i<$scope.listMris.length;i++){
				if($scope.listMris[i].id==id){
					if(!$scope.listMris[i].aStatus){
						$scope.listMris[i].aStatus="不通过";
						$http.get('merchantBusinessProduct/selectMriremark.do?ids='+mriId)
						.success(function(largeLoad) {
							strs[id]=largeLoad.checkMsg;
							asdf();
						})
					}	
					else if($scope.listMris[i].aStatus=="不通过"){
						$scope.listMris[i].aStatus="通过";
						delete strs[id];
						asdf();
					}	
					else if($scope.listMris[i].aStatus=="通过"){
						$scope.listMris[i].aStatus="不通过";
						$http.get('merchantBusinessProduct/selectMriremark.do?ids='+mriId)
						.success(function(largeLoad) {
							strs[id]=largeLoad.checkMsg;
							asdf();
						})
					}
				}
			}
		}
		
		//查看进件资料
		$scope.updateMriInfo=function(pp,id,status,content,name){
				var modalScope = $scope.$new();
				 modalScope.id=id;
				 modalScope.pp=1;
				 if(pp==1){
					 modalScope.dd=1; 
				 }else{
					 modalScope.dd=0;
				 }
				 modalScope.type=1;
				 modalScope.name=name;
				 modalScope.img=2;
				 modalScope.content=content;
				 var modalInstance = $uibModal.open({
		            templateUrl : 'views/merchant/merchantUpdateModal.html',  //指向上面创建的视图
		            controller : 'merchantModalCtrl123',// 初始化模态范围
		            scope:modalScope,
		            size:'lg'
		        })
		        modalScope.modalInstance=modalInstance;
		        modalInstance.result.then(function(selectedItem){  
		        },function(){
		            $log.info('取消: ' + new Date())
		        })
			
		}
		
		$scope.submitting = false;
		//传商户进件要求项资料id,审核意见,通过还是不通过（0/1）标识,商户业务ID
		$scope.commitInfo=function(val){
			$scope.submitting = true;
			if($scope.listMri.length==0 && $scope.listMris.length==0){
				$scope.notice("没有需要审核的进件项");
				$scope.submitting = false;
				return;
			}
            if(val==2){
                for(var i=0;i<$scope.listMri.length;i++){
                    if($scope.listMri[i].mriId==3
						||$scope.listMri[i].mriId==4
						||$scope.listMri[i].mriId==5
						||$scope.listMri[i].mriId==15
					){
                        if($scope.listMri[i].aStatus=="不通过"){
                            $scope.notice("复审不通过不可修改银行卡信息！");
                            $scope.submitting = false;
                            return;
                        }

                    }
                }

            }

			var data;
			var mm=0;
			for(var i=0;i<$scope.listMri.length;i++){
				if($scope.listMri[i].aStatus=="不通过"){
					mm+=1;
					break;
				}
			}
			for(var i=0;i<$scope.listMris.length;i++){
				if($scope.listMris[i].aStatus=="不通过"){
					mm+=1;
					break;
				}
			}
			if(val==2||val==5 || val==3 || val==6 ){
				if(mm==0){
					$scope.notice("请勾选失败的进件项");
					$scope.submitting = false;
					return;
				}
				data = {
					"info": $scope.info,
					"listMri": $scope.listMri,
					"listMris": $scope.listMris,
					"opinion": $scope.asd.oppend,
					"merbpId": $scope.mbp.id,
					"teamId": $scope.mbp.teamId,
					"merchantName": $scope.mbp.merchantName,
					"mobilePhone": $scope.mbp.mobilePhone,
					"auditorId": $scope.mbp.auditorId,
					"val": val,
					"bpId": $scope.mbp.bpId
				};
			}else{
				if(mm>=1){
					$scope.notice("已经勾选了不通过的进件项"); 
					$scope.submitting = false;
					return;
				}
				data={
					"bpId":$scope.mbp.bpId,
					"auditorId": $scope.mbp.auditorId,
					"info":$scope.info,
					"listMri":$scope.listMri,
					"listMris":$scope.listMris,
					"opinion":$scope.asd.oppend,
					"merbpId":$scope.mbp.id,
					"val":val
				};
			}
			$http.post("merchantBusinessProduct/reexamineRcored.do",angular.toJson(data))
	    	.success(function(data){
	    		if(data.result){
	    			$scope.notice(data.msg);
	    			if(val==1||val==2|| val==3 || !data.result){
						$state.go('risk.recheckQuery');
						$scope.submitting = false;
					}else{
						$scope.asd.oppend="";
						$state.transitionTo('risk.MerExamineDetail',{mertId:data.infos},{reload:true});
					}
	    		}else{
	    			$scope.notice(data.msg);
					$scope.submitting = false;
	    		}
				
	    	})
	    	.error(function(data){
	    		$scope.notice("操作失败");
				$scope.submitting = false;
	    	});
		}
		
		$scope.apps="1";
		//实名认证
		$scope.approve=function(){
			var card=$scope.info.idCardNo;
			var name="";
			var accountNo="";
			var cnapsNo="";
			for(var i =0;i<$scope.listMri.length;i++){
				if($scope.listMri[i].mriId=="3"){
					accountNo=$scope.listMri[i].content;
				}
				if($scope.listMri[i].mriId=="2"){
					name=$scope.listMri[i].content;
				}
				if($scope.listMri[i].mriId=="5"){
					cnapsNo=$scope.listMri[i].content;
				}
			}
			var data={"card":card,"name":name,"accountNo":accountNo,"cnapsNo":cnapsNo};
			$http.post("merchantInfo/checkBankNameIDCard",angular.toJson(data))
	    	.success(function(data){
	    		if(data.bols=="t"){
//	    			$scope.notice(data.msg);
	    			$scope.apps=0;
	    		}else{
//	    			$scope.notice(data.msg);
	    			$scope.apps=2;
	    		}
	    	})
	    	.error(function(data){
	    		$scope.notice("操作失败");
	    	});
		}
		
		
});

angular.module('inspinia').controller('merchantModalCtrl123',function($scope,$stateParams,$http){
	 $scope.solutionModalClose=function(){
		 $scope.modalInstance.dismiss();
	 }
	 
	 $scope.solutionModalOk=function(){
		 $scope.modalInstance.close($scope);
	 }
});