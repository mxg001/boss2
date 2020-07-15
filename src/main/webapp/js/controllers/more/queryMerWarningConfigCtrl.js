/**
 * 查询商户预警服务
 */
angular.module('inspinia',['angularFileUpload','uiSwitch']).controller("queryMerWarningConfigCtrl", function($scope, $http, $state, $stateParams,$filter,i18nService,FileUploader,SweetAlert,$document) {
	//数据源
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.warningTypes=[{text:"≥X天无交易商户",value:'NO_TRAN'},{text:"交易下滑X%及以上商户",value:'TRAN_SLIDE'},{text:"未认证商户",value:'UNCERTIFIED'}];
    $scope.baseInfo = {warningTypes:'',id:''};
    $scope.info = {};
    $scope.teamIdes=[];
    $scope.submitting = false;
    //reset
    $scope.resetForm=function(){
        $scope.baseInfo = {warningTypes:'',id:''};
    }
    $scope.resetForm();
    $scope.queryFunc = function(){
		$http.post("merWarning/selectMerchantWarningPage","baseInfo=" + angular.toJson($scope.baseInfo)+"&pageNo="+
				$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
				{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		.success(function(data){
			$scope.result=data.result;
			$scope.merWarningGrid.totalItems = data.totalCount;
		});
	}
    $scope.queryFunc();

    $scope.merWarningGrid = {
	        data: 'result',
	        paginationPageSize:10,                  //分页数量
	        paginationPageSizes: [10,20,50,100],	  //切换每页记录数
	        useExternalPagination: true,		  //开启拓展名
	        enableHorizontalScrollbar: true,        //横向滚动条
            enableVerticalScrollbar : true,  		//纵向滚动条 
	        columnDefs: [
                {field: 'id',displayName: '服务ID',width:60,pinnable: false,sortable: false},
                {field: 'warningName',displayName: '服务名称',width:180, pinnable: false,sortable: false},
                {field: 'noTranDay',displayName: '统计天数',width:180, pinnable: false,sortable: false},
                {field: 'tranSlideRate',displayName: '统计百分比',width:180, pinnable: false,sortable: false},
                {field: 'teamName',displayName: '所属组织',width:180,pinnable: false,sortable: false},
	            {field: 'isUsed',displayName: '是否使用',width:180,pinnable: false,sortable: false,cellTemplate:
                    '<span><switch class="switch switch-s" ng-model="row.entity.isUsed" ng-change="grid.appScope.switchIsUsedStatus(row)" /></span>'
                    /*+'<span class="lh30"> <span ng-show="row.entity.isUsed==1">开启</span><span ng-show="row.entity.isUsed==0">关闭</span></span>'*/},
	            {field: 'warningImgUrl',displayName: '展示图标',width:180,pinnable: false,sortable: false,
                    cellTemplate:'<div style="text-align:center;">' +
                    '<div style="overflow:hidden; display:inline-block;" ng-show="row.entity.warningImgUrl!=null&&row.entity.warningImgUrl!=\'\'" >' +
                    '<div style="float: left;">' +
                    '<a href="{{row.entity.warningImgUrl}}" fancybox rel="group">' +
                    '<img style="width:70px;height:35px;" ng-src="{{row.entity.warningImgUrl}}"/>' +
                    '</a>'+
                    '</div></div></div>'},
	            {field: 'warningTitle',displayName: '展示标题',width:180,pinnable: false,sortable: false},
	            {field: 'warningUrl',displayName: '跳转链接',width:180,pinnable: false,sortable: false},
	            {field: 'createTime',displayName: '创建日期',width:180,pinnable: false,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
	            {field: 'action',displayName: '操作',width: 200,pinnable: false,sortable: false,editable:true,cellTemplate:
	            	'<div class="lh30"><a ng-click="grid.appScope.merWarningEdit(row.entity.id)">编辑</a>' +
	            	'<a  ng-click="grid.appScope.deleteMerchantWarning(row.entity.id)"> | 删除</a></div>'}
	        ],
	        onRegisterApi: function(gridApi) {                
	            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
	            	$scope.paginationOptions.pageNo = newPage;
	            	$scope.paginationOptions.pageSize = pageSize;
	            	$scope.queryFunc();
	            });
	        }
	 };

    $scope.switchIsUsedStatus=function(row){
        if(row.entity.isUsed){
            $scope.serviceTitle = "确定开启？";
            $scope.serviceText = "";
        } else {
            $scope.serviceTitle = "确定关闭？";
            $scope.serviceText = "";
        }
        SweetAlert.swal({
                title: $scope.serviceTitle,
                text: $scope.serviceText,
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    if(row.entity.isUsed==true){
                        row.entity.isUsed=1;
                    } else if(row.entity.isUsed==false){
                        row.entity.isUsed=0;
                    }
                    var data={"isUsed":row.entity.isUsed,"id":row.entity.id};
                    $http.post("merWarning/switchIsUsedStatus",angular.toJson(data))
                        .success(function(data){
                            $scope.notice(data.msg);
                            if(data.status){
                                $scope.queryFunc();
                            }else{
                                row.entity.isUsed = !row.entity.isUsed;
                            }
                        })
                        .error(function(data){
                            row.entity.isUsed = !row.entity.isUsed;
                            $scope.notice("服务器异常");
                        });
                } else {
                    row.entity.isUsed = !row.entity.isUsed;
                }
            });
    };

    $scope.deleteMerchantWarning = function(id){
        SweetAlert.swal({
                title: "您正在进行删除操作，删除后将不可恢复，是否继续？",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post("merWarning/deleteMerchantWarning","id="+id,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function(msg){
                            $scope.notice(msg.msg);
                            $scope.queryFunc();
                        }).error(function(){
                    });
                }
            });
    }

    //图1
    //上传图片,定义控制器路径
    var uploaderImg1 = $scope.uploaderImg1 = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数
        removeAfterUpload: false,  //上传后删除文件
        autoUpload:true,     //文件加入队列之后自动上传，默认是false
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    uploaderImg1.filters.push({
        name: 'imageFilter',
        fn: function(item, options) {
            return this.queue.length < 1;
        }
    });
    //过滤格式
    uploaderImg1.filters.push({
        name: 'imageFilter',
        fn: function(item,options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });
    uploaderImg1.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            $scope.info.warningImg= response.url;
        }
    };

    $scope.queryTeamName = function(){
        $http.get('teamInfo/queryTeamName.do').success(function(msg){
            $scope.team=msg.teamInfo;
        });
    }

    $scope.teamGrid = { // 配置表格
        data : 'team',
        enableHorizontalScrollbar : 0, // 去掉滚动条
        enableVerticalScrollbar : 1, // 去掉滚动条
        columnDefs : [ // 表格数据
            {field : 'teamName',displayName : '组织名称'}],
        onRegisterApi : function(gridApi) {
            $scope.gridApiTeam = gridApi;
        },
        isRowSelectable: function(row){ // 选中行
            if($scope.teamIdes != null){
                for(var i=0;i<$scope.teamIdes.length;i++){
                    if(row.entity.teamId==$scope.teamIdes[i]){
                        row.grid.api.selection.selectRow(row.entity);
                    }
                }
            }
        }
    };

    //修改配置
    $scope.merWarningEdit = function(id){
        $scope.title="编辑服务";
        $scope.saveStatus=1;
        uploaderImg1.clearQueue();
        $http.post("merWarning/selectMerchantWarningDetail","id="+id,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function (data) {
            if(data.status){
                $scope.info=data.info;
                $scope.teamIdes=$scope.info.teamId.split(",");
                $scope.queryTeamName();
                $("#merWarningAddModel").modal("show");
            }
        })
    }

    //添加
    $scope.merWarningAddModel = function(){
        $scope.title="新增服务";
        $scope.saveStatus=0;
        $scope.info={};
        uploaderImg1.clearQueue();
        $scope.queryTeamName();
        $("#merWarningAddModel").modal("show");
    }
    //返回
    $scope.cancel=function(){
        $('#merWarningAddModel').modal('hide');
    }

    $scope.saveInfo = function(){
        var isNum=/^(([1-9][0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,2})))$/;
        if($scope.info.warningType == undefined || $scope.info.warningType == null || $scope.info.warningType==""){
            $scope.notice("请选择服务!");
            return;
        }
        angular.forEach($scope.warningTypes,function(item){
            if(item.value==$scope.info.warningType){
                $scope.info.warningName=item.text;
            }
        });
        if($scope.info.warningType=='NO_TRAN'){
            if($scope.info.noTranDay == undefined || $scope.info.noTranDay == null || $scope.info.noTranDay==""){
                $scope.notice("统计天数不能为空!");
                return;
            }else if($scope.info.noTranDay < 1){
                $scope.notice("统计天数格式不正确!");
                return;
            }
            $scope.info.tranSlideRate="";
        }else if($scope.info.warningType=='TRAN_SLIDE'){
            if($scope.info.tranSlideRate == undefined || $scope.info.tranSlideRate == null || $scope.info.tranSlideRate==""){
                $scope.notice("统计百分比不能为空!");
                return;
            }else if(!isNum.test($scope.info.tranSlideRate)){
                $scope.notice("统计百分比格式不正确!");
                return;
            }
            $scope.info.noTranDay="";
        }else{
            $scope.info.noTranDay="";
            $scope.info.tranSlideRate="";
        }
        $scope.info.teamId='';
        $scope.teames = $scope.gridApiTeam.selection.getSelectedRows();
        if($scope.teames!=null && $scope.teames.length>0){
            for (var i = 0; i < $scope.teames.length; i++) {
                $scope.info.teamId = $scope.info.teamId + $scope.teames[i].teamId + ',';
            }
            $scope.info.teamId=$scope.info.teamId.substr(0,$scope.info.teamId.length-1);
        } else {
            $scope.notice('请选择所属组织!');
            return;
        }
        if($scope.info.warningTitle == undefined || $scope.info.warningTitle == null || $scope.info.warningTitle==""){
            $scope.notice("展示标题不能为空!");
            return;
        }
        if($scope.info.warningUrl == undefined || $scope.info.warningUrl == null || $scope.info.warningUrl==""){
            $scope.notice("跳转链接不能为空!");
            return;
        }
        if($scope.info.warningImg == undefined || $scope.info.warningImg == null || $scope.info.warningImg==""){
            $scope.notice("展示图标不能为空!");
            return;
        }
        if ($scope.submitting == true) {
            return;
        }
        $scope.submitting = true;
        var data = {"info" : $scope.info};
        if($scope.saveStatus==0){
            $http.post("merWarning/insertMerWarning",angular.toJson(data))
                .success(function(data){
                    if(data.status){
                        $scope.notice(data.msg);
                        $("#merWarningAddModel").modal("hide");
                        $scope.queryFunc();
                    }else{
                        $scope.notice(data.msg);
                    }
                    $scope.submitting = false;
                })
                .error(function(){
                    $scope.submitting = false;
                });
        }else if($scope.saveStatus==1){
            $http.post("merWarning/updateMerWarning",angular.toJson(data))
                .success(function(data){
                    if(data.status){
                        $scope.notice(data.msg);
                        $("#merWarningAddModel").modal("hide");
                        $scope.queryFunc();
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

	//页面绑定回车事件
	$document.bind("keypress", function(event) {
		$scope.$apply(function (){
			if(event.keyCode == 13){
				$scope.queryFunc();
			}
		})
	});
});
