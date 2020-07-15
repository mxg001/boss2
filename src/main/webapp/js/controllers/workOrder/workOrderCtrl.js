
angular.module('inspinia',['infinity.angular-chosen','angularFileUpload']).controller('workOrderCtrl',function($scope,$http,SweetAlert,$state,$stateParams,i18nService,$timeout,FileUploader){
	  //数据源
	  i18nService.setCurrentLang('zh-cn');

    $scope.nameSelect = [];
    $scope.replyTypeSelect = [{text:"全部",value:""},{text:"一级和所属代理商均可回复",value:1},{text:"仅有一级代理商回复",value:2},{text:"仅有所属代理商回复",value:3}];
    $scope.deptSelect = angular.copy(initData.DEPT_LIST);
    $scope.deptSelect.unshift({text:"全部",value:""});
    $scope.currentStatusSelectStr = [{text:"待处理",value:"0"},{text:"已处理",value:"2"},{text:"已驳回",value:"3"}];//'部门处理状态：0:待处理,2:已处理,3:已驳回,4:无需回复,5:已关闭'
    $scope.statusSelect = [{text:"全部",value:""},{text:"处理中",value:"1"},{text:"已处理",value:"2"},{text:"已关闭",value:"4"}];
    $scope.currentDeptUserSelect = [{text:"全部",value:""}];
    $scope.createType = [{text:"全部",value:""},{text:"平台",value:"P"},{text:"代理商",value:"A"}];
    $scope.overDueStatusSelect = [{text:"全部",value:""},{text:"代理商逾期回复",value:"1"},{text:"未逾期",value:"0"}];
    $scope.overDueStatusStr = [{text:"代理商逾期回复",value:"1"},{text:"未逾期",value:"0"}];
    $scope.transferUserSelect = [];
    $scope.transferInfo = {orderNoArr:[],receiverId:""};

    // 重置按钮
    $scope.resetForm = function () {
        $scope.info = {orderNo:"",currentDeptNo:"",workTypeId:"",
          status:"",currentUserId:"",createType:"",createUserId:"",overDueReply:"",queryType:"0",currentStatus:"0",oneAgentNode:""
          /*,
          createTimeBegin:moment(new Date().getTime()-30*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
          createTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
          lastUpdateTimeBegin:moment(new Date().getTime()-30*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
          lastUpdateTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
          endReplyTimeBegin:moment(new Date().getTime()-30*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
          endReplyTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'*/};

    };

    $scope.resetForm();

    if($stateParams.type == 1){
      $scope.info.queryType = "0";
    }else{
      $scope.info.queryType = "0";
    }

    // 查询分页
  $scope.paginationOptions=angular.copy($scope.paginationOptions);


  //获取当前部门用户
  $http.get('workOrderUser/getCurrentDeptUser')
    .success(function(result) {
      if(!result.status){
        return;
      }
      if(result.data==null || result.data.length==0) {
        $scope.transferUserSelect.push({value: "", text: "全部"});
      }else{
        $scope.transferUserSelect.push({value: "", text: "全部"});
        for(var i=0; i<result.data.length; i++){
          $scope.transferUserSelect.push({value:result.data[i].userId,text: result.data[i].bossRealName});
        }
      }
    });


  //获取当前用户信息
  $http.get('workOrder/getCurrentWorkUser')
    .success(function(result) {
      if(!result.status){
        return;
      }
      $scope.managerFlag = result.data;
    });

    //获取  工单类型
    $http.get('workType/getAll')
      .success(function(result) {
        if(!result.status){
          return;
        }
        if(result.data==null || result.data.length==0) {
          $scope.nameSelect.push({value: "", text: "全部"});
        }else{
          $scope.nameSelect.push({value: "", text: "全部"});
          for(var i=0; i<result.data.length; i++){
            $scope.nameSelect.push({value:result.data[i].id,text: result.data[i].name});
          }
        }
      });

    //处理人筛选项值显示登录账户所属部门所有账号的真实姓名
    $http.get('workOrder/getCurrentDeptUser')
      .success(function(result) {
          if(!result.status){
              return;
          }
          for(var i=0; i<result.data.length; i++){
              $scope.currentDeptUserSelect.push({value:result.data[i].id,text: result.data[i].realName});
          }
      });

	  $scope.workOrderGrid = {
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs: [
            {field: 'orderNo',displayName: '工单编号',pinnable: false,width: 200,sortable: false},
            {field: 'workTypeName',displayName: '工单类型',pinnable: false,width: 150,sortable: false},
          {field: 'createUserName',displayName: '发起人',pinnable: false,width: 150,sortable: false},
          {field: 'dealProcessName',displayName: '处理流程',pinnable: false,width: 280,sortable: false},
            {field: 'currentDeptName',displayName: '当前处理部门',pinnable: false,width: 150,sortable: false},
          {field: 'currentStatus',displayName: '部门处理状态',pinnable: false,width: 150,sortable: false,cellFilter:"formatDropping:"+angular.toJson($scope.currentStatusSelectStr)},
          {field: 'status',displayName: '工单状态',pinnable: false,width: 150,sortable: false,cellFilter:"formatDropping:"+angular.toJson($scope.statusSelect)},
          {field: 'currentUserName',displayName: '所属部门处理人',pinnable: false,width: 150,sortable: false},
          {field: 'oneAgentNo',displayName: '一级代理商编号',pinnable: false,width: 150,sortable: false},
          {field: 'oneAgentName',displayName: '一级代理商名称',pinnable: false,width: 150,sortable: false},
          {field: 'agentNo',displayName: '所属代理商编号',pinnable: false,width: 150,sortable: false},
          {field: 'agentName',displayName: '所属代理商名称',pinnable: false,width: 150,sortable: false},
          {field: 'overDueReply',displayName: '逾期回复状态',pinnable: false,width: 150,sortable: false,cellFilter:"formatDropping:"+angular.toJson($scope.overDueStatusStr)},
          {field: 'endReplyTime',displayName: '代理截止回复日期',pinnable: false,width: 150,sortable: false,cellFilter : 'date:"yyyy-MM-dd HH:mm:ss"'},
          {field: 'lastUpdateTime',displayName: '最后更新日期',pinnable: false,width: 150,sortable: false,cellFilter : 'date:"yyyy-MM-dd HH:mm:ss"'},
          {field: 'createTime',displayName: '创建日期',pinnable: false,width: 150,sortable: false,cellFilter : 'date:"yyyy-MM-dd HH:mm:ss"'},
            {field: 'action',displayName: '操作',width: 250,pinnedRight:true,sortable: false,editable:true,cellTemplate:
	         	'<div class="lh30">'
                  +'<a class="lh30" ng-show="grid.appScope.hasPermit(\'workOrder.detail\')" ng-click="grid.appScope.orderDetail(row)" " >详情</a>'
                  +'<a class="lh30" ng-show="row.entity.remarkStatus && grid.appScope.hasPermit(\'workOrder.remark\')" ui-sref="workOrder.workOrderRemark({id:row.entity.id})" > | 备注</a>'
                  +'<a class="lh30" ng-show="row.entity.replyStatus && grid.appScope.hasPermit(\'workOrder.reply\')" ui-sref="workOrder.workOrderReply({type:0,id:row.entity.id})" > | 处理</a>'
                  +'<a class="lh30" ng-show="row.entity.rejectStatus && grid.appScope.hasPermit(\'workOrder.reject\')" ui-sref="workOrder.workOrderReject({type:0,id:row.entity.id})" > | 驳回</a>'
                  +'<a class="lh30" ng-show="row.entity.closeStatus && grid.appScope.hasPermit(\'workOrder.close\')" ng-click="grid.appScope.closeOrder(row)" > | 关闭</a>'
            	+'</div>'
            }
        ],
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            $scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
	          	$scope.paginationOptions.pageNo = newPage;
	          	$scope.paginationOptions.pageSize = pageSize;
	            $scope.query();
	        });
        }
	 };
    // 查询按钮
    $scope.query = function () {
        if ($scope.loadImg) {
            return;
        }
        $scope.loadImg = true;

        $http.post('workOrder/query',
          "info="+angular.toJson($scope.info)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+
          $scope.paginationOptions.pageSize,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
          .success(function(result){
              if(result.status){
                $scope.workOrderGrid.data =result.data.result;
                $scope.workOrderGrid.totalItems = result.data.totalCount;
              }else{
                $scope.notice(result.msg);
              }
              $scope.loadImg = false;
          }).error(function(){
          $scope.notice("系统异常！");
        });
    };

    $scope.query();

    $scope.orderDetail = function (row){
      window.open('welcome.do#/workOrder/workOrder/workOrderDetail/'+row.entity.id, 'blank');
    }


    $scope.export=function(){
      if($scope.loadImg){
        return;
      }

      SweetAlert.swal({
          title: "确认导出？",
          showCancelButton: true,
          confirmButtonColor: "#DD6B55",
          confirmButtonText: "提交",
          cancelButtonText: "取消",
          closeOnConfirm: true,
          closeOnCancel: true
        },
        function (isConfirm) {
          if (isConfirm) {
            $scope.exportInfoClick("workOrder/export",{"info":angular.toJson($scope.info)});
          }
        });
    }


    //删除工单类型
    $scope.del = function(row){

      SweetAlert.swal({
          title: "确认删除？",
          showCancelButton: true,
          confirmButtonColor: "#DD6B55",
          confirmButtonText: "提交",
          cancelButtonText: "取消",
          closeOnConfirm: true,
          closeOnCancel: true
        },
        function (isConfirm) {
          if (isConfirm) {
            $http({
              url: 'workType/del?id='+row.entity.id,
              method: 'get'
            }).success(function (result) {
              if(result.status){
                $scope.query();
              }
              $scope.notice(result.msg);
            });
          }
        });

    }

  //关闭工单
  $scope.closeOrder = function(row){

    SweetAlert.swal({
        title: "确认关闭？",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "提交",
        cancelButtonText: "取消",
        closeOnConfirm: true,
        closeOnCancel: true
      },
      function (isConfirm) {
        if (isConfirm) {
          $http({
            url: 'workOrder/close?id='+row.entity.id,
            method: 'get'
          }).success(function (result) {
            if(result.status){
              $scope.query();
            }
            $scope.notice(result.msg);
          });
        }
      });


  }


  $scope.transferOrder= function(){
    $scope.submitting = true;
    var selectedRecordRows = $scope.gridApi.selection.getSelectedRows();
    for(var i in selectedRecordRows){
      $scope.transferInfo.orderNoArr.push(selectedRecordRows[i].orderNo);
    }
    $http({
      url: 'workOrderItem/transfer',
      method: 'POST',
      data: $scope.transferInfo
    }).success(function (result) {
      if(result.status){
        $scope.transferResult = result.data.orders;
        $scope.transferResultInfo = result.data;
        $scope.submitting = false;
        //$scope.hideTransferModal();
        $scope.showTransferResultModal();
      }else{
        $scope.submitting = false;
        $scope.notice(result.msg);
      }
    });
  }

  $scope.transferResultGrid = {
    data: 'transferResult',
    paginationPageSize:10,                  //分页数量
    paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
    useExternalPagination: true,		  //开启拓展名
    enableHorizontalScrollbar: true,        //横向滚动条
    enableVerticalScrollbar : true,  		//纵向滚动条
    columnDefs: [
      {field: 'id',displayName: 'ID',pinnable: false,width: 100,sortable: false},
      {field: 'workTypeName',displayName: '工单类型',pinnable: false,width: 150,sortable: false},
      {field: 'reason',displayName: '失败原因',pinnable: false,width: 250,sortable: false}
    ],
    onRegisterApi: function(gridApi) {
      $scope.transferResultGridApi = gridApi;
    }
  };

    $scope.showTransferModal = function(){
      var selectedRecordRows = $scope.gridApi.selection.getSelectedRows();
      if(selectedRecordRows.length<=0){
        $scope.notice("请选择工单!");
        return;
      }
      $scope.transferInfo = {orderNoArr:[],receiverId:""};
      $('#transferModal').modal('show');
    }

    $scope.hideTransferModal = function(){
      $('#transferModal').modal('hide');
    }




  $scope.showTransferResultModal = function(){
    $('#transferResultModal').modal('show');
  }

  $scope.hideTransferResultModal = function(){
    $('#transferResultModal').modal('hide');
    $scope.hideTransferModal();
    $scope.query();
  }


  //代理商
  $scope.agentList=[{value:"",text:"全部"}];

  $http.post("agentInfo/selectAllInfo")
    .success(function(msg){
      //响应成功
      for(var i=0; i<msg.length; i++){
        $scope.agentList.push({value:msg[i].agentNode,text:msg[i].agentNo + " " + msg[i].agentName});
      }
    });

  //异步获取直属代理商
  var oldValue="";
  var timeout="";
  $scope.getAgentList = function(value) {
    $scope.agentt = [];
    var newValue=value;
    if(newValue != oldValue){
      if (timeout) $timeout.cancel(timeout);
      timeout = $timeout(
        function(){
          $http.post('agentInfo/selectAllInfo','item=' + value,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .then(function (response) {
              if(response.data.length==0) {
                $scope.agentt.push({value: "", text: "全部"});
              }else{
                $scope.agentt.push({value: "", text: "全部"});
                for(var i=0; i<response.data.length; i++){
                  $scope.agentt.push({value:response.data[i].agentNode,text:response.data[i].agentNo + " " + response.data[i].agentName});
                }
              }
              $scope.agentList = $scope.agentt;
              oldValue = value;
            });
        },800);
    }
  };

});


