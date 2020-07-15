/**
/**
 * 硬件产品新增
 */
angular.module('inspinia').controller('addHardCtrl',function($scope,$http,$state,$stateParams,$compile,$filter){
	$scope.info = {orgId:null,secretType:0,devicePn:$scope.devicePNTypeLists[0].value,teamEntryId:""};
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
		if(null != $scope.subTeamMap[$scope.info.orgId] &&  $scope.info.teamEntryId == "" ){
			$scope.notice("请选择所属子组织！");
			return;
		}else{
			$scope.info.teamEntryId ="";
		}
		$scope.submitting = true;
		$http.post("hardwareProduct/addHard",angular.toJson($scope.info))
		.success(function(data){
			if(data.bols){
				$scope.notice(data.msg);
				$state.transitionTo('service.insertHard',null,{reload:true});
			}else{
				$scope.notice(data.msg);
				$scope.submitting = false;
			}
		});
	}

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
	};

	$http.get('teamInfo/querySubTeams').success(function(result){
		$scope.subTeamMap = result.subTeamMap;
	});
	
	$http.get('teamInfo/queryTeamName.do').success(function(msg){
			for(var i=0;i<msg.teamInfo.length;i++){
				$scope.team.push({text:msg.teamInfo[i].teamName,value:msg.teamInfo[i].teamId});
			}
	});


})