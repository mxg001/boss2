/**
 */

angular.module('inspinia').controller('acqInDetailCtrl',function(i18nService,$scope,$http,$state,$stateParams,$compile,$filter,$log,$uibModal){
	/**
	 */
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	$scope.useables=[{text:"失效",value:0},{text:"生效",value:1}];
	
	$scope.auditStatuss=[{text:"全部",value:-1},{text:"待审核",value:1},{text:"审核通过",value:2},{text:"审核不通过",value:3}];

	
	$scope.info={};
	$scope.listMris=[];

	$scope.flag = $stateParams.flag;

	$scope.merchantTypes=function(data){
		if(data==1){
			return '个体收单商户';
		}
		if(data==2){
			return '企业收单商户';
		}
	};
	$scope.accountTypes=function(data){
		if(data==1){
			return '对私';
		}
		if(data==2){
			return '对公';
		}
	};
	
	
	$scope.merExa=[];
	
	$http.get('acqInMerchantAction/selectDetailByParam?ids='+$stateParams.id)
	.success(function(largeLoad) {
		
		if(largeLoad.bols){
			$scope.info=largeLoad.result;
			$scope.merExa=largeLoad.record;
			$scope.listMris=largeLoad.listStrs;
		}else{
			$scope.notice(largeLoad.msg);
		}
	});
	
		
		$scope.merchantRecords={
			data:'merExa',
			columnDefs:[
		           {field:'auditStatus',displayName:'状态',pinnable:false,sortable:false,
		        	   cellFilter: "formatDropping:[{text:'审核不通过',value:3},{text:'审核通过',value:2},{text:'正常',value:1}]"
		           },
		           {field:'examinationOpinions',displayName:'审核意见',pinnable:false,sortable:false},
		           {field:'createTime',displayName:'时间',pinnable:false,sortable:false,
		        	   cellFilter: "date:'yyyy-MM-dd HH:mm:ss'"
		           }, 
		           {field:'realName',displayName:'操作员',pinnable:false,sortable:false}
				]
		};
		
		var strs=[];
		//var strs2=[];
		var opiniontext = "";
        var notAllowIds="";
		
		function asdf(){
			$scope.info.examinationOpinions=opiniontext;
			for(var is in strs){
				if(!strs.hasOwnProperty(is))
					continue;
				$scope.info.examinationOpinions += strs[is];
				//if(!$scope.asd.oppend)
				$scope.info.examinationOpinions += "\n";
			}

			/*for(var is in strs2){
				if(!strs2.hasOwnProperty(is))
					continue;
				$scope.info.examinationOpinions += strs2[is];
				$scope.info.examinationOpinions += "\n";
			}*/

		}

		//人工修改的意见
		$scope.upds=function(){
			opiniontext=$scope.info.examinationOpinions;
		}

		/*$scope.baseData = [{id:42, status:1}];
		$scope.rdts2 = function(id){
			for (var i = 0; i < $scope.baseData.length; i++) {
				$scope.baseData[i].id = id;
				$scope.baseData[i].status = $scope.baseData[i].status == 1 ? 0 : 1;
				if($scope.baseData[i].status == 0){
					$http.get('merchantBusinessProduct/selectMriremark.do?ids='+ id)
						.success(function(largeLoad) {
							strs2[id]=largeLoad.checkMsg;
							asdf();
						})
				}else {
					delete strs2[id];
					asdf();
				}
			}
		}*/
		
		$scope.rdts=function(status,id,mriId){
			for(var i=0;i<$scope.listMris.length;i++){
				if($scope.listMris[i].id==id){
					if(!$scope.listMris[i].aStatus){
						$scope.listMris[i].aStatus="不通过";
                        notAllowIds=notAllowIds+mriId+","
						$http.get('merchantBusinessProduct/selectMriremark.do?ids='+mriId)
						.success(function(largeLoad) {
							strs[id]=largeLoad.checkMsg;
							asdf();
						})
					}	
					else if($scope.listMris[i].aStatus=="不通过"){
						$scope.listMris[i].aStatus="通过";
						delete strs[id];
						if(notAllowIds.indexOf(mriId+",")!=-1){
                            notAllowIds=notAllowIds.replace(mriId+",","");
						}
						asdf();
					}	
					else if($scope.listMris[i].aStatus=="通过"){
						$scope.listMris[i].aStatus="不通过";
                        notAllowIds=notAllowIds+mriId+","
						$http.get('merchantBusinessProduct/selectMriremark.do?ids='+mriId)
						.success(function(largeLoad) {
							strs[id]=largeLoad.checkMsg;
							asdf();
						})
					}
				}
			}
        }
		
		$scope.submitting = false;
		$scope.commitInfo=function(val){
			$scope.submitting = true;
			if(val==2||val==4){
				if("undefined"==$scope.info.examinationOpinions || null == $scope.info.examinationOpinions || ""== $scope.info.examinationOpinions.trim())
				{
					$scope.submitting = false;
					$scope.notice("审核不通过必须填写审核意见"); 
					return ;
				}	
			}
			if(notAllowIds.endsWith(",")){
                notAllowIds=notAllowIds.substring(0,notAllowIds.length-1);
			}
			data = {"id":$stateParams.id,"notAllowIds":notAllowIds,"val":val,"examinationOpinions":$scope.info.examinationOpinions};
			
			
	//		$http.post("merchantBusinessProduct/examineRcored.do",angular.toJson(data))
			$http.post("acqInMerchantAction/audit.do",angular.toJson(data))
	    	.success(function(data){
	    		if(data.result){
	    			$scope.notice(data.msg);
	    			if(val==1||val==2||!data.result){
						$state.go('org.orgIn');
						$scope.submitting = false;
					}else{
						if("undefined"==$scope.info.examinationOpinions || null == $scope.info.examinationOpinions || ""== $scope.info.examinationOpinions.trim())
						{$scope.info.examinationOpinions="";};
						
						if(data.nextId==-1){
							$state.go('org.orgIn');
							$scope.submitting = false;
							return ;
						}
						
						$state.transitionTo('org.acqInDetail',{id:data.nextId,flag:2},{reload:true});
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
		
});

angular.module('inspinia').controller('merchantModalCtrl123',function($scope,$stateParams,$http){
	 $scope.solutionModalClose=function(){
		 $scope.modalInstance.dismiss();
	 }
	 
	 $scope.solutionModalOk=function(){
		 $scope.modalInstance.close($scope);
	 }
});