/**
 * 按交易跳转集群设置
 */
angular.module('inspinia',['uiSwitch','infinity.angular-chosen']).controller("jumpRouteCtrl", function($scope, $http, $state, $stateParams, i18nService,SweetAlert,$document) {
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	i18nService.setCurrentLang('zh-cn');
	//数据库字段决定1:是，2：否，所以没有使用全局
	$scope.reset = function() {
		$scope.baseInfo = {status:null,acqId:null,teamId:null,relationActivity:null};
	};
    $scope.reset();

    $scope.statusList = [{text:"全部",value:null},{text:"生效",value:1},{text:"失效",value:0}];
    $scope.acqOrgs=[{value:null,text:"全部"}];
    $scope.effectiveDateTypeList = [{text:'每天',value:1},{text:'周一至周五',value:2},{text:'法定工作日',value:3},{text:'自定义',value:4}];

	//是否关联活动
	$scope.relationActivitySelect = [{value:null,text:"全部"},{text:"否",value:0},{text:"是",value:1}];
	$scope.relationActivityStr=angular.toJson($scope.relationActivitySelect);

    $http.get('teamInfo/queryTeamName.do').success(function(msg){
        $scope.teamType=[];
        $scope.teamType.push({text:'全部',value:null});
        for(var i=0;i<msg.teamInfo.length;i++){
            $scope.teamType.push({text:msg.teamInfo[i].teamName,value:msg.teamInfo[i].teamId});
        }
    });

    //收单机构
    $http.post("acqOrgAction/selectBoxAllInfo")
        .success(function(msg){
            //响应成功
            for(var i=0; i<msg.length; i++){
                $scope.acqOrgs.push({value:msg[i].id,text:msg[i].acqName});
            }
        });

	$scope.query = function() {
		$http.post('jumpRoute/page.do',
       		 "baseInfo="+angular.toJson($scope.baseInfo)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
       		 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
        ).success(function(data){
            $scope.jumpRouteGrid.data = data.result;
			$scope.jumpRouteGrid.totalItems = data.totalCount;//总记录数
        }).error(function(){
        }); 
	};
	$scope.query();
	
	$scope.jumpRouteGrid = {
	        paginationPageSize:10,                  //分页数量
	        paginationPageSizes: [10,20,50,100],	//切换每页记录数
	        useExternalPagination: true,		  //开启拓展名
	        columnDefs: [
                {field: 'id',displayName: 'id',width: 120,pinnable: false,sortable: false},
                {field: 'status',displayName: '状态',width: 120,pinnable: false,sortable: false, cellFilter:'formatDropping:' + angular.toJson($scope.statusList)},
                {field: 'effectiveDateType',displayName: '生效日期',width: 150,pinnable: false,sortable: false, cellFilter:"formatDropping:"+angular.toJson($scope.effectiveDateTypeList)},
                {field: 'startDate',displayName: '开始生效日期',width: 150,pinnable: false,sortable: false, cellFilter:'date:"yyyy-MM-dd"'},
                {field: 'endDate',displayName: '截止生效日期',width: 150,pinnable: false,sortable: false,cellFilter:'date:"yyyy-MM-dd"'},
                {field: 'cardType',displayName: '卡类型',width: 150,pinnable: false,sortable: false, cellFilter:'formatDropping:'+angular.toJson($scope.cardType)},
                {field: 'startTime',displayName: '每天生效时间',width: 150,pinnable: false,sortable: false,cellFilter:'date:"HH:mm:ss"'},
                {field: 'endTime',displayName: '每天截止时间',width: 150,pinnable: false,sortable: false,cellFilter:'date:"HH:mm:ss"'},
                // {field: 'jumpTimes',displayName: '跳转次数',width: 150,pinnable: false,sortable: false},
                {field: 'minTransAmount',displayName: '最小交易金额',width: 150,pinnable: false,sortable: false},
                {field: 'maxTransAmount',displayName: '最大交易金额',width: 150,pinnable: false,sortable: false},
                // {field: 'apartDays',displayName: '相隔天数',width: 150,pinnable: false,sortable: false},
                {field: 'groupCode',displayName: '可用集群',width: 150,pinnable: false,sortable: false},
                {field: 'groupName',displayName: '跳转目标路由集群',width: 250,pinnable: false,sortable: false},
                {field: 'acqEnname',displayName: '收单机构',width: 150,pinnable: false,sortable: false},
                {field: 'remark',displayName: '备注',width: 200,pinnable: false,sortable: false},
	            {name: 'action',displayName: '操作',width: 200,pinnedRight:true,pinnable: false,sortable: false,editable:true,cellTemplate:
	            	"<a class='lh30' ui-sref='org.jumpRouteDetail({id:row.entity.id,type:\"detail\"})' >详情</a>"
	            	+"<a class='lh30' ng-show='grid.appScope.hasPermit(\"jumpRoute.save\")' ui-sref='org.jumpRouteUpdate({id:row.entity.id,type:\"update\"})' > | 修改</a>"
	            	+"<a class='lh30' ng-show='grid.appScope.hasPermit(\"jumpRoute.delete\")' ng-click='grid.appScope.deleteInfo(row.entity.id)'> | 删除</a>"}
	        ],
	        onRegisterApi: function(gridApi) {                
	            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
	            	$scope.paginationOptions.pageNo = newPage;
	            	$scope.paginationOptions.pageSize = pageSize;
	            	$scope.query();
	            });
	        }
	};
	$scope.whiteListDate=[];
	$scope.whiteListMgrGrid={
			data:'whiteListDate',
			columnDefs:[
		           {field:'merchantNo',displayName:'商户编号',width:200,pinnable:false,sortable:false},
		           {field:'createPerson',displayName:'添加人',width:150,pinnable:false,sortable:false},
		           {field:'id',displayName:'操作',width:150,pinnable:false,sortable:false,
		        	   cellTemplate:'<a class="lh30" ng-show="grid.appScope.hasPermit(\'acqOrg.deleteWhite\')" ng-click="grid.appScope.delWhiteList(row.entity.id)">删除</a>'
		           }
				]
	};
	//打开白名单管理模板
	$scope.openWhiteList=function(){
		$http.get('jumpRoute/selectAllWlInfo')
		.success(function(data){
			if(!data.status){
				$scope.notice(data.msg);
			}else{
				$scope.whiteListDate =data.result; 
				$('#whitelistList').modal('show');
			}
		})
	}
	//删除白名单
	$scope.delWhiteList=function(id){
		SweetAlert.swal({
            title: "确认删除？",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "提交",
            cancelButtonText: "取消",
            closeOnConfirm: true,
            closeOnCancel: true },
	        function (isConfirm) {
	            if (isConfirm) {
	            	$http.post('jumpRoute/deleteWlInfo',
	        				"ids="+angular.toJson(id),{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
	        		.success(function(data){
	        			if(!data.status){
	        				$scope.notice(data.msg);
	        			}else{
	        				$scope.notice(data.msg);
	        				for(var i=0;i<$scope.whiteListDate.length;i++){
	        					if($scope.whiteListDate[i].id==id){
	        						$scope.whiteListDate.splice(i,1);
	        						break;
	        					}
	        				}
	        			}
	        		});
	            }
        });
		
	}
	
	
	//打开白名单模板
	$scope.openAddWhiteList=function(){
		$('#whitelistAdd').modal('show');
		$scope.roll.merchantNo = null;
	}
	//添加白名单
	$scope.addWhiteList=function(){
		$scope.submitting = true;
		$http.post('jumpRoute/addWlInfo',
				"info="+$scope.roll.merchantNo,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		.success(function(data){
			if(!data.status){
				$scope.notice(data.msg);
				$scope.submitting = false;
			}else{
				$scope.notice(data.msg);
				$('#whitelistAdd').modal('hide');
				$scope.submitting = false;
				$scope.roll.merchantNo = null;
			}
		})
	}
	
    
    $scope.deleteInfo=function(id){
        SweetAlert.swal({
            title: "确认删除？",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            closeOnConfirm: true,
            closeOnCancel: true },
	        function (isConfirm) {
	            if (isConfirm) {
	            	$http.post("jumpRoute/delete.do","id="+id,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
	        		.success(function(msg){
	        			if(msg.status){
	        				$scope.notice(msg.msg);
		        			$scope.query();
	        			} else {
	        				$scope.notice(msg.msg);
	        			}
	        		}).error(function(){
	        		});
	            }
        });
    };

    // 导出
    $scope.export = function () {
        SweetAlert.swal({
                title: "确定导出吗？",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    // location.href = "cjtOrder/export?baseInfo=" +encodeURI(encodeURI(angular.toJson($scope.baseInfo)));
                    $scope.exportInfoClick("jumpRoute/export",{"baseInfo":angular.toJson($scope.baseInfo)});
                }
            });
    };


	//页面绑定回车事件
	$document.bind("keypress", function(event) {
		$scope.$apply(function (){
			if(event.keyCode == 13){
				$scope.query();
			}
		})
	});
	
});