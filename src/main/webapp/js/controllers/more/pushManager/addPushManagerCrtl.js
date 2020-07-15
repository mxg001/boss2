/**
 */
angular.module('inspinia').controller('saveOrUpdatePushManagerCrtl',function($scope, $http, $state, $stateParams, i18nService,$filter) {

	i18nService.setCurrentLang('zh-cn');
	$scope.pushObjArr = [];//推送对象
	$scope.pushObjArrSelected = [];//修改的时候回显以选择的推送对象
	$scope.mobileTypeArr = [{text:"全部",value:null},{text:"ios",value:2},{text:"android",value:1}];//移动端类型 默认选中全部
	$scope.pushObjCheckAllFlag = false;
	$scope.showDateSelectedFlag = false;
	$scope.baseInfo = {"targetUser":"0","mobileTerminalType":null,"dingshiOrNow":"0"};



		$scope.pushObjCheckAll = function(pushObjCheckAllFlag){
			if(pushObjCheckAllFlag){
				angular.forEach($scope.pushObjArr,function(data){
					data.isChecked = 1;
				})
			} else {
				angular.forEach($scope.pushObjArr,function(data){
					data.isChecked = 0;
				})
			}
		}

		$scope.showDateSelected = function(showDateSelectedFlag){
			$scope.showDateSelectedFlag = !showDateSelectedFlag;
			$scope.baseInfo.pushTime = "";
		}

	$scope.getAppInfo = function(){
		$http.post("pushManager/getAppInfo")
			.success(function(data){
				if(data.status){
					var appInfos = data.appInfos;
					for(var i=0; i<appInfos.length; i++){
						$scope.pushObjArr.push({value:appInfos[i].sys_value,text:appInfos[i].sys_name,isChecked:$.inArray(appInfos[i].sys_value, $scope.pushObjArrSelected)!=-1});
					}
				}else{
					$scope.notice(data.msg);
				}

			});
	}

		$scope.getPushManagerById = function () {
			if($stateParams.id!=null && $stateParams.id!=""){
				$http.post("pushManager/getPushManagerById","id="+$stateParams.id,
					{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
					.success(function(data){
						if(data.status){
							$scope.baseInfo=data.baseInfo;
							$scope.pushObjArrSelected = data.baseInfo.pushObj.split(",");
							$scope.baseInfo.pushTime = moment(new Date(data.baseInfo.pushTime).getTime()).format('YYYY-MM-DD HH:mm:ss');
							if(data.baseInfo.dingshiOrNow==1){
								$scope.showDateSelectedFlag = true;
							}
							$scope.getAppInfo();
						}else{
							$scope.notice(data.msg);
						}
					});
			}else{
				$scope.getAppInfo();
			}
		}

	$scope.getPushManagerById();






	$scope.commitPushManager=function(){

		if($scope.baseInfo.dingshiOrNow==1){
			if($scope.baseInfo.pushTime==null || $scope.baseInfo.pushTime==""){
				$scope.notice("定时时间不能为空！");
				return;
			}
		}
		var str="";
		if($("input:checkbox[name='pushObjCheckBox']:checked").length<=0){
			$scope.notice("推送对象不能为空！");
			return;
		}
		$("input:checkbox[name='pushObjCheckBox']:checked").each(function() { // 遍历多选框
			if(""==str){
				str=str+$(this).val();
			}else{
				str=str+","+$(this).val();
			}
		});

		$scope.baseInfo.pushObj = str;
		$scope.submitting = true;
		var jumpUrl = $scope.baseInfo.jumpUrl;
		$scope.baseInfo.jumpUrl = null;
		if(jumpUrl == null){
			jumpUrl ="";
		}
		$http.post('pushManager/saveOrUpdatePushManager',
				"baseInfo="+angular.toJson($scope.baseInfo)+"&jumpUrl="+encodeURIComponent(jumpUrl),
				 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		.success(function(data){
			if(!data.status){
				$scope.notice(data.msg);
				$scope.baseInfo.jumpUrl = jumpUrl;
				$scope.submitting = false;
			}else{
				$scope.notice(data.msg);
				$scope.submitting = false;
				$state.transitionTo('sys.pushManager',null,{reload:true});
				
			}
		})
	}
	
})