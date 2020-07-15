/**
 * banner广告管理
 */
angular.module('inspinia',['uiSwitch']).controller('adCtrl',function($scope,$http,i18nService,$document,$window,SweetAlert){

    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    //状态
    $scope.adStatus = [{value:"-1",text:"全部"},{value:"on",text:"打开"},{value:"off",text:"关闭"}];
    //位置
    $scope.applyTypeList = [{value:"",text:"全部"},{value:"1",text:"App+公众号"},{value:"2",text:"App"},{value:"3",text:"公众号"}];
    $scope.postionTypeList = [{value:"1",text:"首页"},{value:"2",text:"办卡查询"},{value:"3",text:"贷款申请"},{value:"4",text:"信用卡列表位置"}];
    $scope.postionTypeListAll = [{value:"",text:"全部"},{value:"1",text:"首页"},{value:"2",text:"办卡查询"},{value:"3",text:"贷款申请"},{value:"4",text:"信用卡列表位置"}];

    $scope.resetForm = function () {
    	$scope.ad = {orgId:"-1",postionType:"",status:"-1",applyType:""};
    };
    $scope.resetForm();

    $scope.columnDefs = [
        {field: 'id',displayName: 'ID',width: 60,pinnable: false,sortable: false},
        {field: 'orgName',displayName: '组织',width: 150,pinnable: false,sortable: false},
        {field: 'applyType',displayName: '应用类型',width: 200,pinnable: false,sortable: false,cellFilter:"formatDropping:" + angular.toJson($scope.applyTypeList)},
        {field: 'title',displayName: 'banner标题',width: 200},
        {field: 'postionType',displayName: 'banner位置',width: 200,pinnable: false,sortable: false,cellFilter:"formatDropping:" + angular.toJson($scope.postionTypeList)},
        {field: 'upDate',displayName: '上线时间',width: 200,cellFilter:"date:'yyyy-MM-dd HH:mm:ss'"},
        {field: 'downDate',displayName: '下线时间',width: 200,cellFilter:"date:'yyyy-MM-dd HH:mm:ss'"},
        {field: 'showNo',displayName: '权重',width: 100,cellFilter:"number"},
        {field: 'status',displayName: '状态',width: 130,cellTemplate:'<span ><switch class="switch switch-s" ng-model="row.entity.status"  ng-true-value="off" ng-true-value="on" ng-change="grid.appScope.onOff(row.entity)" /></span>'},
        {field: 'action',displayName: '操作',width: 150,pinnedRight:true,
        	cellTemplate:
        	'<div  class="lh30"><a ui-sref="superBank.adDetail({id:row.entity.id})">详情</a>&nbsp;'
        		+'<a  ui-sref="superBank.updAd({id:row.entity.id})">修改</a>'
        		+'&nbsp;<a ng-show="row.entity.status!=\'on\'" ng-click="grid.appScope.delAd(row.entity.id)">删除</a>'
        		+'</div>'
        }
    ];
    
    $scope.adGrid = {
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		  	//开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs: $scope.columnDefs,
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            $scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.paginationOptions.pageNo = newPage;
                $scope.paginationOptions.pageSize = pageSize;
                $scope.query();
            });
        }
    };

    $scope.query = function () {
        $scope.submitting = true;
        $scope.loadImg = true;
        
        $http({
            url: 'superBank/adManage?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
            data: $scope.ad,
            method:'POST'
        }).success(function (msg) {
            $scope.submitting = false;
            $scope.loadImg = false;
            
            if (!msg.status){
            	$scope.notice(msg.msg);
                return;
            }
            $scope.adGrid.data = msg.data.result;
            $scope.adGrid.totalItems = msg.data.totalCount;
            angular.forEach($scope.adGrid.data, function(item){
            	if("on" == item.status){
            		item.status = true;
            	}else{
            		item.status = false;
            	}
            });
        }).error(function (msg) {
            $scope.submitting = false;
            $scope.loadImg = false;
            $scope.notice('服务器异常,请稍后再试.');
        });
    };
   
    $scope.addnotice = function(){
    	$("#addnotice").modal("show");
    };
    
    $scope.cancel = function(){
    	$("#addnotice").modal("hide");
    };
    //删除广告
    $scope.delAd = function (id) {
    	SweetAlert.swal({
			title: "确定删除这条数据吗？",
			type: "warning",
			showCancelButton: true,
			confirmButtonColor: "#DD6B55",
			confirmButtonText: "确定",
			cancelButtonText: "取消",
			closeOnConfirm: true,
			closeOnCancel: true },
			function (isConfirm) {
				if (isConfirm) {
					$http({
						url: "superBank/delAd?id="+id,
						method:'GET'
					}).success(function (msg) {
						$scope.notice(msg.msg);
						if (msg.status){
							$scope.query();
						}
					}).error(function (msg) {
						$scope.notice('服务器异常,请稍后再试.');
					});
				}
			});
    };
    
    //状态开关
    $scope.onOff = function(entity){
		if(entity.status){
			$scope.serviceText = "确定打开？";
		} else {
			$scope.serviceText = "确定关闭？";
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
		        	var statusReal = "";
		        	if(entity.status==true){
		        		statusReal = "on";
		        	} else if(entity.status==false){
		        		statusReal = "off";
		        	}
		        	var data={"status":statusReal,"id":entity.id};
		            $http.post("superBank/adOnOff",angular.toJson(data))
		        	.success(function(data){
		        		if(data.status){
		        			$scope.notice("操作成功！");
		        			
		        		}else{
		        			if(entity.status==true){
			            		entity.status = false;
			            	} else {
			            		entity.status = true;
			            	}
		        			$scope.notice(data.msg);
		        		}
		        	})
		        	.error(function(data){
		        		if(entity.status==true){
		            		entity.status = false;
		            	} else {
		            		entity.status = true;
		            	}
		        		$scope.notice("服务器异常");
		        	});
		        } else {
		        	if(entity.status==true){
		        		entity.status = false;
		        	} else {
		        		entity.status = true;
		        	}
		        }
		});
			
	};
    
      //组织列表
    $scope.orgs=[];
    $scope.getAllOrg = function(){
   	 $http({
            url:"superBank/getOrgInfoList",
            method:"POST"
        }).success(function(msg){
            if(msg.status){
                $scope.orgs = msg.data;
                $scope.orgs.unshift({orgId:'-1',orgName:"全部"});
            }
        }).error(function(){
            $scope.notice("获取组织列表异常");
        });
   };
   $scope.getAllOrg();
   
   
    //页面绑定回车事件
    $document.bind("keypress", function(event) {
        $scope.$apply(function (){
            if(event.keyCode == 13){
                $scope.query();
            }
        });
    });
});