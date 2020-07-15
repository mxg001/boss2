/**
 * VIP优享活动详情
 */
angular.module('inspinia',['uiSwitch']).controller('activityVipCtrl',function(i18nService,$scope,$http,$state,$stateParams,$compile,$filter,SweetAlert){

    $scope.addInfo={};
    
    if($stateParams.actId=='10'){
    	$scope.addInfo.team_id='200010';
    }else{
    	$scope.addInfo.team_id='300020';
    }
    
    $scope.submitting = false;

	//VIP优享详情
	$scope.query = function(){
		$http.get('couponActivity/couponActivityVip?actId='+$stateParams.actId)
		.success(function(msg){
			if(msg.status){
				$scope.info = msg.info;
				$scope.rechargeEntityGrid.data = msg.entity;
			} else {
				$scope.notice(msg.msg);
			}
		});
	}
	if("edit"==$stateParams.edit){
		$scope.couponEdit=true;
	}else{
		$scope.couponEdit=false;
	}

	$scope.query();
	$scope.rechargeEntityGrid = {                  //配置表格
		    enableHorizontalScrollbar: 1,       //横向滚动条
		    enableVerticalScrollbar : 1,  		//纵向滚动条
		    columnDefs:[                        //表格数据
                { field: 'index',displayName: '序号',width: 100,cellTemplate: "<span class='checkbox'>{{rowRenderIndex + 1}}</span>"},
                { field: 'name',displayName:'名称',width:120},
                { field: 'time',displayName:'时间',width:100,cellTemplate:'<div class="lh30">{{row.entity.time}}天<div/>'},
                { field: 'original_price',displayName:'原价'},
                { field: 'discount_price',displayName:'折扣价',width:100},
                { field: 'sort_num',displayName:'排序',width:100},
                { field: 'team_id',displayName:'类别',width:100,cellFilter:"formatDropping:"+angular.toJson($scope.allAgentOemText)},
                { field: 'is_recommend', displayName: '推荐',width:100,cellTemplate:
                        '<span ng-show="!{{grid.appScope.couponEdit}}"> <span ng-show="row.entity.is_recommend==1">开启</span><span ng-show="row.entity.is_recommend==0">关闭</span></span>'+
                    '<span ng-show="{{grid.appScope.couponEdit}}"><switch class="switch switch-s" ng-true-value="1" ng-false-value="0" ng-model="row.entity.is_recommend" ng-change="grid.appScope.openStatus_Shelves(row,\'is_recommend\')" /></span>'
                },
                { field: 'is_switch', displayName: '开关',width:100,cellTemplate:
                    '<span ng-show="!{{grid.appScope.couponEdit}}"> <span ng-show="row.entity.is_switch==1">开启</span><span ng-show="row.entity.is_switch==0">关闭</span></span>'+
                    '<span ng-show="{{grid.appScope.couponEdit}}"> <switch class="switch switch-s" ng-true-value="1" ng-false-value="0" ng-model="row.entity.is_switch" ng-change="grid.appScope.openStatus_Shelves(row,\'is_switch\')" /></span>'
                },
                { field: 'action',displayName:'操作',width:100,
                    cellTemplate:'<span ng-show="{{grid.appScope.couponEdit}}"><a  class="lh30" ng-click="grid.appScope.activityVipEditModel(row.entity)">修改</a></span>'
                }
            ]
		};

        //添加
        $scope.activityVipAddModel = function(){
            $scope.saveStatus=0;
            $scope.modelText="新增";
            $("#activityVipAddModel").modal("show");
            
            var tempTeamId = '';
            if($stateParams.actId=='10'){
            	tempTeamId='200010';
            }else{
            	tempTeamId='300020';
            }
            
            
            $scope.addInfo={name:"",time:"",original_price:"",discount_price:"",sort_num:"",team_id:tempTeamId};
        }
        //编辑
        $scope.activityVipEditModel = function(entity){
            $scope.saveStatus=1;
            $scope.modelText="编辑";
            $("#activityVipAddModel").modal("show");
            $scope.addInfo=entity;
        }
        //返回
        $scope.cancel=function(){
            $scope.addInfo={};
            $('#activityVipAddModel').modal('hide');
        }


    //状态修改
    $scope.openStatus_Shelves=function(row,col){
    	console.debug(row);
        if("is_recommend"==col){
            if(row.entity.is_recommend){
                $scope.serviceText = "确定打开？";
            } else {
                $scope.serviceText = "确定关闭？";
            }
        }else{
            if(row.entity.is_switch){
                $scope.serviceText = "确定打开？";
            } else {
                $scope.serviceText = "确定关闭？";
            }
        }


        SweetAlert.swal({
            title: $scope.serviceText,
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "提交",
            cancelButtonText: "取消",
            closeOnConfirm: true,
            closeOnCancel: true },
	        function (isConfirm) {
	            if (isConfirm) {
	            	//debugger;
	             	var val;
	            	if("is_recommend"==col){
	            		if(row.entity.is_recommend==true){
	            			val = 1;
		            	} else if(row.entity.is_recommend==false){
		            		val = 0;
		            	}
	            	}else{
	            		if(row.entity.is_switch==true){
	            			val = 1;
		            	} else if(row.entity.is_switch==false){
		            		val = 0;
		            	}
	            	}
	                $http.get("couponActivity/updateVipStatus?id="+row.entity.id+"&col="+col+"&val="+val+"&teamId="+row.entity.team_id)
	            	.success(function(data){
	            		if(data.status){
                            $scope.query();
	            			$scope.notice("操作成功！");
	            		}else{
	            			if("is_recommend"==col){
			            		if(row.entity.is_recommend==true){
				            		row.entity.is_recommend=false;
				            	}else {
				            		row.entity.is_recommend=true;
				            	}
			            	}else{
			            		if(row.entity.is_switch==true){
				            		row.entity.is_switch=false;
				            	} else {
				            		row.entity.is_switch=true;
				            	}
			            	}
	            			$scope.notice("操作失败！");
	            		}
	            	})
	            	.error(function(data){
	            		if("is_recommend"==col){
		            		if(row.entity.is_recommend==true){
			            		row.entity.is_recommend=false;
			            	}else {
			            		row.entity.is_recommend=true;
			            	}
		            	}else{
		            		if(row.entity.is_switch==true){
			            		row.entity.is_switch=false;
			            	} else {
			            		row.entity.is_switch=true;
			            	}
		            	}
	            		$scope.notice("服务器异常")
	            	});
	            } else {
	            	if("is_recommend"==col){
	            		if(row.entity.is_recommend==true){
		            		row.entity.is_recommend=false;
		            	}else {
		            		row.entity.is_recommend=true;
		            	}
	            	}else{
	            		if(row.entity.is_switch==true){
		            		row.entity.is_switch=false;
		            	} else {
		            		row.entity.is_switch=true;
		            	}
	            	}
	            }
        });
    	
    };
    
    $scope.commit = function(){
    	$scope.subDisable=true;
        $http.post('couponActivity/saveCouponActivity',
				"info="+encodeURIComponent(angular.toJson($scope.info)),
				 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		.success(function(data){
			if(data.status){
				$scope.notice(data.msg);
				$state.transitionTo('func.couponActivity',null,{reload:true});
				$scope.subDisable = false;
			}else{
				$scope.notice(data.msg);
				$scope.subDisable = false;
			}
		});
    }

    //提交
    $scope.submit = function(){
        if($scope.addInfo.name==null||$scope.addInfo.name===""){
            $scope.notice("名称不能为空!");
            return;
        }
        if($scope.addInfo.time==null||$scope.addInfo.time===""){
            $scope.notice("时间不能为空!");
            return;
        }
        var exp = /^[1-9]\d*$/;
        if($scope.addInfo.time==0||!exp.test($scope.addInfo.time)){
            $scope.notice("时间格式不对");
        }
        if($scope.addInfo.original_price==null||$scope.addInfo.original_price===""){
            $scope.notice("原价不能为空!");
            return;
        }
        var isNum=/^(([1-9][0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,2})))$/;
        if($scope.addInfo.original_price==0 || !isNum.test($scope.addInfo.original_price)){
            $scope.notice("原价格式不对");
            return;
        }
        if($scope.addInfo.discount_price!=null&&$scope.addInfo.discount_price!=""){
            if($scope.addInfo.discount_price==0 || !isNum.test($scope.addInfo.discount_price)){
                $scope.notice("折扣价格式不对");
                return;
            }
        }
        if($scope.addInfo.sort_num==null||$scope.addInfo.sort_num===""){
            $scope.notice("排序不能为空!");
            return;
        }
        if(!exp.test($scope.addInfo.sort_num)){
            $scope.notice("排序格式不对");
        }

        if($scope.addInfo.team_id==null||$scope.addInfo.team_id===""){
            $scope.notice("请选择类别!");
            return;
        }
        
        
        if ($scope.submitting == true) {
            return;
        }
        $scope.addInfo.activity_code=$stateParams.actId;
        //$scope.submitting = true;
        var data = {
            "info" : $scope.addInfo
        };
        if($scope.saveStatus==0){
            $http.post("couponActivity/addActivityVip",angular.toJson(data))
                .success(function(data){
                    if(data.status){
                        $scope.notice(data.msg);
                        $scope.query();
                        $scope.cancel();
                    }else{
                        $scope.notice(data.msg);
                    }
                    $scope.submitting = false;
                })
                .error(function(){
                    $scope.submitting = false;
                });
        }else if($scope.saveStatus==1){
            $http.post("couponActivity/updateActivityVip",angular.toJson(data))
                .success(function(data){
                    if(data.status){
                        $scope.notice(data.msg);
                        $scope.query();
                        $scope.cancel();
                    }else{
                        $scope.notice(data.msg);
                    }
                    $scope.submitting = false;
                })
                .error(function(){
                    $scope.submitting = false;
                });
        }
    }
});

