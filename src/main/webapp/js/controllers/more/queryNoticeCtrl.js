/**
 * 查询公告
 */
angular.module('inspinia').controller("queryNoticeCtrl", function($scope, $http, $state, $stateParams,$filter,i18nService,SweetAlert,$document) {
	//数据源
	$scope.bool=[{text:"是",value:"1"},{text:"否",value:"0"}];
	$scope.targetTypes=[{text:"不限",value:'0'},{text:"商户",value:'1'},{text:"代理商",value:'2'}];
	$scope.statusTypes=[{text:"全部",value:'0'},{text:"待下发",value:'1'},{text:"正常",value:'2'}];
	$scope.showStatuses=[{text:"非弹窗",value:'0'},{text:"弹窗(仅一次)",value:'1'},{text:"弹窗(每日一次)",value:'2'}];
	$scope.msgTypes=[{text:"业务通知",value:'0'},{text:"系统通知",value:'1'}];
	$scope.targetTypesStr = angular.toJson($scope.targetTypes);
	$scope.statusTypesStr = angular.toJson($scope.statusTypes);
	$scope.showStatusesStr = angular.toJson($scope.showStatuses);
	$scope.msgTypesStr = angular.toJson($scope.msgTypes);
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	$scope.baseInfo = {sysType:'0',status:'0',createTimeBegin:null,createTimeEnd:null,issuedTimeBegin:null,issuedTimeEnd:null,msgType:'0'};
	$scope.queryFunc = function(){
		$scope.result=[];
		if($scope.baseInfo.createTimeBegin!=null&& $scope.baseInfo.createTimeEnd != null && 
				$scope.baseInfo.createTimeBegin > $scope.baseInfo.createTimeEnd){
			$scope.notice("创建起始时间不能大于终止时间");
			return ;
		}
		if($scope.baseInfo.issuedTimeBegin!= null && $scope.baseInfo.issuedTimeEnd != null && 
				$scope.baseInfo.issuedTimeBegin > $scope.baseInfo.issuedTimeEnd){
			$scope.notice("下发起始时间不能大于终止时间");
			return ;
		}
		$http.post("notice/selectByParam.do","baseInfo=" + angular.toJson($scope.baseInfo)+"&pageNo="+
				$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
				{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		.success(function(data){
			$scope.result=data.result;
			$scope.noticeListGrid.totalItems = data.totalCount;
		});
	}

    $scope.noticeListGrid = {
	        data: 'result',
	        paginationPageSize:10,                  //分页数量
	        paginationPageSizes: [10,20,50,100],	  //切换每页记录数
	        useExternalPagination: true,		  //开启拓展名
	        enableHorizontalScrollbar: true,        //横向滚动条
            enableVerticalScrollbar : true,  		//纵向滚动条 
	        columnDefs: [
                {field: 'ntId',displayName: '通告ID',width:60,pinnable: false,sortable: false},
                {field: 'strong',displayName: '通告ID',width:60,visible:false},
                {field: 'sysType',displayName: '下发对象',width:60,pinnable: false,sortable: false,cellFilter:"formatDropping:" + $scope.targetTypesStr},
	            {field: 'title',displayName: '标题',width:400, pinnable: false,sortable: false,cellTemplate:"<div style='width:100%; text-align: left'><div ng-show='grid.appScope.isStrong(row.entity.strong,1)' class='lh30' style='color:red;display:inline;'>【置顶】</div><div ng-show='grid.appScope.isStrong(row.entity.strong,2)' class='lh30' style='display:inline'>{{row.entity.title}}</div></div>"},
	            {field: 'status',displayName: '状态',width:60,pinnable: false,sortable: false,cellFilter:"formatDropping:" + $scope.statusTypesStr},
	            {field: 'showStatus',displayName: '提示方式',width:60,pinnable: false,sortable: false,cellFilter:"formatDropping:" + $scope.showStatusesStr},
	            {field: 'validTime',displayName: '弹窗生效时间段',width:380,pinnable: false,sortable: false,cellTemplate:"<div>{{row.entity.validBeginTime| date:'yyyy-MM-dd HH:mm:ss'}}~~{{row.entity.validEndTime| date:'yyyy-MM-dd HH:mm:ss'}}</div>"},
	           // {field: 'msgType',displayName: '类型',pinnable: false,sortable: false,cellFilter:"formatDropping:" + $scope.msgTypesStr},
	            {field: 'createTime',displayName: '创建时间',width:180,pinnable: false,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
	            {field: 'issuedTime',displayName: '下发时间',width: 150,pinnable: false,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
	            {field: 'action',displayName: '操作',width: 200,pinnable: false,sortable: false,editable:true,cellTemplate:
	            	'<div class="lh30"><a ng-show="grid.appScope.hasPermit(\'notice.detail\')"  ui-sref="sys.noticeDetail({id:row.entity.ntId})">详情</a><span ng-switch on="row.entity.status"><span ng-switch-when="1"><a ng-show="grid.appScope.hasPermit(\'notice.update\')" ui-sref="sys.modifyNotice({id:row.entity.ntId})"> | 修改</a>' +
	            	'<a ng-show="grid.appScope.hasPermit(\'notice.deliver\')" ui-sref="sys.deliverNotice({id:row.entity.ntId})"> | 下发</a></span><span ng-switch-when="2"><a ng-show="grid.appScope.hasPermit(\'notice.detail\')" ng-click="grid.appScope.recoverNotice(row.entity.ntId)"> | 收回</a>' +
	            	'</span><a ng-show="grid.appScope.clearStrong(row.entity,1)" ng-click="grid.appScope.optStrong(row.entity.ntId,1)"> | 置顶</a><a ng-show="grid.appScope.clearStrong(row.entity,2)" ng-click="grid.appScope.optStrong(row.entity.ntId,2)"> | 取消置顶</a><a ng-show="grid.appScope.isDelete(row.entity)" ng-click="grid.appScope.deleteNotice(row.entity.ntId)"> | 删除</a></div>'}
	        ],
	        onRegisterApi: function(gridApi) {                
	            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
	            	$scope.paginationOptions.pageNo = newPage;
	            	$scope.paginationOptions.pageSize = pageSize;
	            	$scope.queryFunc();
	            });
	        }
	 };


	$scope.isStrong =function(strong,type){
		if(type==1){
            if(strong === 1){
                return true;
            }
            return false;
		}
        return true;
	}
    $scope.clearStrong =function(obj,type){
        if(obj.status!='2'){
            return false;
        }
        var strong = obj.strong;
        if(type==2){
            if(strong === 1){
                return true;
            }else {
                return false;
            }
		}else{
            if(strong === 1){
                return false;
            }else {
                return true;
            }
		}
    }
    $scope.isDelete =function(obj){
        console.log(obj.title + " " + obj.msgType + " " + obj.status);
		if(obj.msgType==1 || obj.status==1){		//系统消息或待下发的消息可以删除
            console.log(obj.title + " " + obj.msgType + " " + obj.status);
            return true;
		}
		return false;
    }

    $scope.queryFunc();
    $scope.recoverNotice = function(ntId){
    	SweetAlert.swal({
            title: "确认收回？",
//            text: "",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "提交",
            cancelButtonText: "取消",
            closeOnConfirm: true,
            closeOnCancel: true },
	        function (isConfirm) {
	            if (isConfirm) {
	            	$http.post("notice/recoverNotice.do","id="+ntId,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
	        		.success(function(msg){
	        			$scope.notice(msg.msg);
	        			$scope.queryFunc();
	        		}).error(function(){
	        		});
	            }
        });
    }

    $scope.optStrong = function(ntId,type){
		$http.post("notice/optStrong.do","id="+ntId+"&type="+type,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			.success(function(msg){
				$scope.notice(msg.msg);
				$scope.queryFunc();
			}).error(function(){
		});
    }

    $scope.deleteNotice = function(ntId){
        SweetAlert.swal({
                title: "是否删除本公告，删除后不可恢复",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post("notice/deleteNotice.do","id="+ntId,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function(msg){
                            $scope.notice(msg.msg);
                            $scope.queryFunc();
                        }).error(function(){
                    });
                }
            });
    }
  //reset
	$scope.resetForm=function(){
		$scope.baseInfo= {sysType:'0',status:'0',createTimeBegin:null,createTimeEnd:null,issuedTimeBegin:null,issuedTimeEnd:null,msgType:'0'};
	}

	//页面绑定回车事件
	$document.bind("keypress", function(event) {
		$scope.$apply(function (){
			if(event.keyCode == 13){
				$scope.queryFunc();
			}
		})
	});
});
