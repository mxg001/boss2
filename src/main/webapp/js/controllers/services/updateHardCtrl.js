/**
/**
 * 硬件产品新增
 */
angular.module('inspinia').controller('updateHardCtrl',function($scope,$http,$state,$stateParams,$compile,$filter,$stateParams){
	
	$scope.info = {orgId:null};
	$scope.team = [{text:'全部',value:null}];
    $scope.secretTypeList = [{text:'无',value:0},{text:'双密钥',value:1}];
	$scope.subTeamMap = [];
	$scope.subTeams = [];
	$scope.showSubTeams = false;

	$scope.commit=function(){
		// 判断所属组织
		if(null == $scope.info.orgId){
			$scope.notice("请选择所属组织！");
			return;
		}
		// 所属子组织必填
		if(null != $scope.subTeamMap[$scope.info.orgId]){
			if($scope.info.teamEntryId == "" || $scope.info.teamEntryId == null){
				$scope.notice("请选择所属子组织！");
				return;
			}
		}else{
			$scope.info.teamEntryId ="";
		}
		$scope.submitting = true;
		$http.post("hardwareProduct/updateHard",angular.toJson($scope.info))
		.success(function(data){
			if(data.bols){
				$scope.notice(data.msg);
				$state.transitionTo('service.queryHard',null,{reload:true});
			}else{
				$scope.notice(data.msg);
				$scope.submitting = false;
			}
		});
	}
	
	$http.get('hardwareProduct/queryHardById/'+$stateParams.id).success(function(msg){
		if(msg.status){
			for(var i=0;i<msg.teamInfo.length;i++){
				$scope.team.push({text:msg.teamInfo[i].teamName,value:msg.teamInfo[i].teamId});
			}
			$scope.info = msg.hp;
			$scope.subTeamMap = msg.subTeamMap;
			//判断是否展示子组织下拉框
			var temp = $scope.subTeamMap[$scope.info.orgId];
			if(null != temp && temp != undefined){
				$scope.subTeams = [];
				angular.forEach(temp, function (e) {
					$scope.subTeams.push({text:e.teamEntryName,value:e.teamEntryId});
				});
				$scope.showSubTeams = true;
			}else {
				$scope.showSubTeams = false;
			}
		} else {
			$scope.notice('查询失败');
		}
	});

	$scope.hasSubTeam = function(teamId){
		var temp = $scope.subTeamMap[teamId];
		if(null != temp && temp != undefined){
			$scope.subTeams = [];
			angular.forEach(temp, function (e) {
				$scope.subTeams.push({text:e.teamEntryName,value:e.teamEntryId});
			});
			$scope.showSubTeams = true;
		}else {
			$scope.showSubTeams = false;
		}
		$scope.info.teamEntryId ="";
	}


})