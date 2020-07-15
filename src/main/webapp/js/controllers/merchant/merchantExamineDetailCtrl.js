/**
 * 商户审核
 */

angular.module('inspinia').controller('merchantExamineDetailCtrl',function(i18nService,$scope,$http,$state,$stateParams,$compile,$filter,$log,$uibModal){


	//涮新页面跳转最顶端
	window.scrollTo(0, 0);

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
	$scope.remerExa=[];
	$scope.listMri=[];
	$scope.listMris=[];
	$scope.terminal=[];
	$scope.agent={};
	$scope.merAgent={};
	$scope.loadEditMccInput=false;
	$scope.loadShowMccText=true;
    $scope.editBtn=true;
    $scope.saveBtn=false;
	$http.get('merchantBusinessProduct/merchantExamineDetail?ids='+$stateParams.mertId)
	.success(function(largeLoad) {
		if(largeLoad.bols){
			if(largeLoad.mbp==null){
				$scope.notice("数据为空");
				return;
			}else{
				$scope.merAgent = largeLoad.merAgent;
				$scope.agent=largeLoad.agent;
				$scope.info=largeLoad.mi;
				$scope.mbp=largeLoad.mbp;
				$scope.terminal=largeLoad.tiPage.result;
				$scope.merExa=largeLoad.listel;
				$scope.remerExa=largeLoad.exlistel;
				$scope.listMri=largeLoad.listmri;
				$scope.listMris=largeLoad.listmris;
				$scope.comparList=largeLoad.comparList;
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

	$scope.regressionOld=function(data){
		$http.get('merchantBusinessProduct/deleteUserAuditKeyDetail?mbpId='+$stateParams.mertId);
		$state.transitionTo('merchant.auditMer',null,{reload:true});
	};


	$scope.merchantTypes=function(data){
        for(var i =0;i<$scope.merchantTypeLists.length;i++){
            if($scope.merchantTypeLists[i].value==data){
                return $scope.merchantTypeLists[i].text;
            }
        }
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
			]
		};

	//企业对比信息
	$scope.comparListGrid={
		data:'comparList',
		columnDefs:[
			{field:'returnState',width:100,displayName:'状态',pinnable:false,sortable:false,
				cellFilter: "formatDropping:[{text:'比对失败',value:'2'},{text:'比对通过',value:'1'}]"
			},
			{field:'msg',width:700,displayName:'对比详情',pinnable:false,sortable:false},
			{field:'createTime',width:150,displayName:'时间',pinnable:false,sortable:false,
				cellFilter: "date:'yyyy-MM-dd HH:mm:ss'"
			},
			{field:'channelCode',width:150,displayName:'数据源',pinnable:false,sortable:false}
		]
	};

		$scope.merchantRecords={
			data:'merExa',
			columnDefs:[
		           {field:'openStatus',width:100,displayName:'状态',pinnable:false,sortable:false,
		        	   cellFilter: "formatDropping:[{text:'审核失败',value:'2'},{text:'审核成功',value:'1'}]"
		           },
		           {field:'examinationOpinions',width:700,displayName:'内容',pinnable:false,sortable:false},
		           {field:'createTime',width:150,displayName:'时间',pinnable:false,sortable:false,
		        	   cellFilter: "date:'yyyy-MM-dd HH:mm:ss'"
		           }, 
		           {field:'realName',width:150,displayName:'操作员',pinnable:false,sortable:false}
				]
		};
		
		$scope.remerchantRecords={
				data:'remerExa',
				columnDefs:[
			           {field:'openStatus',width:100,displayName:'状态',pinnable:false,sortable:false,
							cellFilter: "formatDropping:[{text:'复审不通过',value:'2'},{text:'复审通过',value:'1'},{text:'复审退件',value:'3'}]"
			           },
			           {field:'examinationOpinions',width:700,displayName:'内容',pinnable:false,sortable:false},
			           {field:'createTime',width:150,displayName:'时间',pinnable:false,sortable:false,
			        	   cellFilter: "date:'yyyy-MM-dd HH:mm:ss'"
			           }, 
			           {field:'realName',width:150,displayName:'操作员',pinnable:false,sortable:false}
					]
		};
		$scope.merchantDetailed={
			data:'listMri',
			columnDefs:[
		       {field:'itemName',displayName:'进件要求项名称',width:200,pinnable:false,sortable:false},
		       {field:'content',displayName:'资料',pinnable:false,sortable:false,
		    	   cellTemplate:
		    		'<div class="lh30" ng-switch on="$eval(\'row.entity.exampleType\')">'
						+'<div ng-switch-when="2">'
							+'<div>'
								+'<a ui-sref="{{COL_FIELD}}">{{$eval(\'row.entity.itemName\')}} 附件下载</a>'
							+'</div>'
						+'</div>'
						+'<div ng-switch-when="3">'
						+'<div class="lh30" ng-switch on="$eval(\'row.entity.mriId\')">'
                    +'<div ng-switch-default>'
                    +'<div>{{COL_FIELD}}</div>'
                    +'</div>'
                    +'<div ng-switch-when="37">'
                    +'<div id="showMcc" ng-show="grid.appScope.isShowMcc()" style="overflow: hidden;white-space: nowrap;text-overflow:ellipsis;">{{$eval(\'row.entity.industryMcc.industryName1\')}}-{{$eval(\'row.entity.industryMcc.industryName\')}}</div>'
                    +'<div ng-show="grid.appScope.isEditMcc()">'
                    + '<select style="float:left;width: 35%" id="mcc1Sel" class="form-control" ng-model="row.entity.industryMcc.parentId" ng-change="grid.appScope.mcc1Change(row.entity)" ng-options="x.id as x.industryName for x in grid.appScope.getMccList1()"></select>'
                    +'<select style="float:left;width: 65%" class="form-control" ng-model="row.entity.industryMcc.mcc" ng-options="x.mcc as x.industryName for x in grid.appScope.getMccList2()"></select></div>'

                    +'</div>'
					+'</div>'

						+'</div>'
					+'</div>'
				   },
	           {field:'status',displayName:'状态', width:80,pinnable:false,sortable:false,
		    	   cellFilter: "formatDropping:[{text:'待审核',value:0},{text:'审核通过',value:1},{text:'审核失败',value:2}]"
	           },
	           {field:'auditTime',displayName:'审核通过时间',width:130,pinnable:false,sortable:false,
		        	   cellFilter: "date:'yyyy-MM-dd'"
	           },
	           {field:'id',displayName:'操作',pinnable:false,sortable:false,
	        	   cellTemplate:
					   '<span><label ng-show="row.entity.mriId!=5" class="lh30"><input type="checkbox" ng-click="grid.appScope.rdt(1,COL_FIELD,$eval(\'row.entity.mriId\'))" name="a{{COL_FIELD}}" value="2"/>不通过</label>'
					   +'<span ng-show="row.entity.mriId==37&&grid.appScope.editBtn" class="lh50" style="display: inline-block;padding:3px;"><input class="btn" type="button" ng-click="grid.appScope.editMcc($eval(\'row.entity\'))" value="修改"  /></span>'
					   +'<span ng-show="row.entity.mriId==37&&grid.appScope.saveBtn" class="lh50" style="display: inline-block;padding:3px;"><input class="btn" type="button" ng-click="grid.appScope.saveMcc($eval(\'row.entity\'))" value="保存"  /></span></span>'
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


    	$scope.editMcc=function(entity){
            $scope.editBtn=false;
            $scope.saveBtn=true;
           $scope.loadEditMccInput=true;
          $scope.loadShowMccText=false;
            $scope.getIndustryMcc1(entity);
    	}
    $scope.saveMcc=function(entity){
        //$scope.notice("保存所属行业mcc:"+entity.industryMcc.mcc);
        $http.post('merchantBusinessProduct/saveIndustryMcc', 'id='+entity.id+'&mcc=' + entity.industryMcc.mcc+'&merNo=' + entity.merchantNo,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
        ).success(function (data) {
        	if(data.bols){
                $('#showMcc').html(data.industryMcc.industryName1+"-"+data.industryMcc.industryName);
                $scope.notice(data.msg);
                $scope.editBtn=true;
                $scope.saveBtn=false;
                $scope.loadEditMccInput=false;
                $scope.loadShowMccText=true;
			}else{
                $scope.notice(data.msg);
			}

        }).error(function () {
        });
    }
    $scope.mcc1Change=function(entity){
        $scope.getIndustryMcc(entity.industryMcc.parentId, function (data) {
            entity.industryMcc.mcc=data[0].mcc;
            $scope.mcc2List = data;
        });
    }
    	$scope.isEditMcc=function(){
          return $scope.loadEditMccInput;
    	}

   	$scope.getMccList1=function(){
       		 return $scope.mcc1List;
	}
    $scope.getMccList2=function(){
        return $scope.mcc2List;
    }
    $scope.isShowMcc=function(){
          return $scope.loadShowMccText;
    }

    $scope.getIndustryMcc = function (parentId, callback) {
        $http.post('merchantBusinessProduct/getIndustryMcc', 'parentId=' + parentId,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
        ).success(function (data) {
            callback(data);
        }).error(function () {
        });
    }
    $scope.mcc1List=[];
    $scope.mcc2List=[];
    //1级MCC，加载页面时自动触发
    $scope.getIndustryMcc1=function(entity){
        $scope.getIndustryMcc("0", function (data) {
            $scope.mcc1List = data;
        });
        $scope.getIndustryMcc(entity.industryMcc.parentId, function (data) {
            $scope.mcc2List = data;
        });
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
			if(val==2||val==4){
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
				data={"bpId":$scope.mbp.bpId,"auditorId": $scope.mbp.auditorId,"info":$scope.info,"listMri":$scope.listMri,"listMris":$scope.listMris,"opinion":$scope.asd.oppend,"merbpId":$scope.mbp.id,"val":val};
			}
			$http.post("merchantBusinessProduct/examineRcored.do",angular.toJson(data))
	    	.success(function(data){
	    		if(data.result){
					$scope.notice(data.msg);
					if(val==1||val==2||data.infoState){
						$state.go('merchant.auditMer');
						$scope.submitting = false;
					}else{
						$scope.asd.oppend="";
						$state.transitionTo('merchant.MerExamineDetail',{mertId:data.infos},{reload:true});
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

		$scope.errMsg="";
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
					$scope.errMsg=data.errMsg;
	    			$scope.apps=2;
	    		}
	    	})
	    	.error(function(data){
	    		$scope.notice("操作失败");
	    	});
		}
	/**/
	$(".txtprevent").focus(function (e) {
		e.preventDefault();
	})
	/**/
		
});

angular.module('inspinia').controller('merchantModalCtrl123',function($scope,$stateParams,$http){
	 $scope.solutionModalClose=function(){
		 $scope.modalInstance.dismiss();
	 }
	 
	 $scope.solutionModalOk=function(){
		 $scope.modalInstance.close($scope);
	 }
});