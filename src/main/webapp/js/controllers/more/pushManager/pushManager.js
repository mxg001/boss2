/**
 */
angular.module('inspinia',['angularFileUpload','infinity.angular-chosen']).controller('pushManagerCtrl',function($scope,$http,$state,$stateParams,i18nService,SweetAlert,$document,FileUploader){
  i18nService.setCurrentLang('zh-cn');
  $scope.paginationOptions=angular.copy($scope.paginationOptions);


  $scope.mobileTypeArr = [{text:"全部",value:null},{text:"ios",value:2},{text:"android",value:1}];//移动端类型 默认选中全部
  $scope.pushObjArr = [{text:"全部",value:""}];//推送对象 默认选中全部
  $scope.pushStatusArr = [{text:"全部",value:null},{text:"未推送",value:"0"},{text:"已推送",value:"1"},{text:"推送失败",value:"2"}];//推送对象 默认选中全部
  $scope.calendarData = [];
  $scope.previewBaseInfo = {};
  var opts = {pushId: null};
  $scope.importPushId = null;
  $scope.jumpUrlEncode = "";

  //清空查询条件
  $scope.resetForm = function(){
    $scope.jumpUrlEncode = "";
    $scope.baseInfo = {mobileTerminalType:null,pushObj:"",pushStatus:null,
      createTimeBegin:moment(new Date().getTime()-30*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',createTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD'+' 23:59:59')};
  }
  $scope.resetForm();

  $scope.exportInfo=function(){
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
          $scope.exportInfoClick("pushManager/exportPushManager",{"baseInfo":angular.toJson($scope.baseInfo)});
        }
      });
  }

  $scope.imPortPushManagerFromExcel = function(pushId){
    $('#imPortPushManagerFromExcel').modal('show');
    opts.pushId= pushId;
    $scope.importPushId = pushId;
  }
  $scope.cancel = function(){
    $('#imPortPushManagerFromExcel').modal('hide');
    $('#checkModal').modal('hide');
  }
  //上传图片,定义控制器路径
  var uploader = $scope.uploader = new FileUploader({
    url: 'pushManager/imPortPushManagerFromExcel',
    queueLimit: 1,   //文件个数
    removeAfterUpload: true,  //上传后删除文件
    headers : {'X-CSRF-TOKEN' : $scope.csrfData},
    formData: [opts]
  });
  //过滤长度，只能上传一个
  uploader.filters.push({
    name: 'isFile',
    fn: function(item, options) {
      return this.queue.length < 1;
    }
  });
  //过滤格式
  $scope.uploader.filters.push({
    name: 'fileFilter',
    fn: function(item /*{File|FileLikeObject}*/, options) {
      var type = '|' + item.name.slice(item.name.lastIndexOf('.') + 1) + '|';
      return '|xlsx|xls|'.indexOf(type) !== -1;
    }
  });
  $scope.clearItems = function(){  //重新选择文件时，清空队列，达到覆盖文件的效果
    uploader.clearQueue();
  }
  //商户导入
  $scope.importCommit=function(){
    $scope.submitting = true;
    uploader.uploadAll();//上传
    uploader.onSuccessItem = function(fileItem, response, status, headers) {//上传成功后的回调函数，在里面执行提交
      if(response.status){
        $scope.notice(response.msg);
        $('#imPortPushManagerFromExcel').modal('hide');
      }else{
        $scope.notice(response.msg);
      }
      $scope.submitting = false;
    };
  }

  $http.post("pushManager/getAppInfo")
    .success(function(data){
      if(data.status){
        var appInfos = data.appInfos;
        for(var i=0; i<appInfos.length; i++){
          $scope.pushObjArr.push({value:appInfos[i].sys_value,text:appInfos[i].sys_name});
        }
      }else{
        $scope.notice(data.msg);
      }

    });




  $scope.calendar = {
    data: 'calendarData',
    paginationPageSize: 10,
    paginationPageSizes: [10, 20, 50, 100],
    useExternalPagination: true,		  	//开启拓展名
    columnDefs: [
      {field: 'id',width: 80, displayName: '推送id'},
      {field: 'pushContent',width: 200, displayName: '推送内容'},
      {field: 'jumpUrl',width: 200, displayName: '跳转链接'},
      {field: 'mobileTerminalType',width: 80, displayName: '移动端类型',cellFilter:"formatDropping:"+angular.toJson($scope.mobileTypeArr)},
      {field: 'pushObjName',width: 200, displayName: '推送对象' },
      {field: 'pushTime',width: 200, displayName: '推送时间',cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
      {field: 'createTime',width: 200, displayName: '创建时间',cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
      {field: 'createPerson',width: 100, displayName: '创建人'},
      {field: 'pushStatus',width: 100, displayName: '推送状态',cellFilter:"formatDropping:"+angular.toJson($scope.pushStatusArr)},
      {field: 'options',width: 260, displayName: '操作',pinnedRight:true, cellTemplate:
          '<div class="lh30"><a  ui-sref="sys.pushManagerDetail({id:row.entity.id})" target="_black">详情' +
          '</a><a ng-show="grid.appScope.hasPermit(\'pushManager.tuPush\') && row.entity.pushStatus==0 && row.entity.dingshiOrNow==0" ui-sref="sys.pushManagerDetail({id:row.entity.id,isPush:1})" target="_black"> | 推送</a>'+
          '</a><a ng-show="grid.appScope.hasPermit(\'pushManager.saveOrUpdatePushManager\') && row.entity.pushStatus==0 " ui-sref="sys.updatePushManager({id:row.entity.id})" target="_black">  | 修改</a>'
          +'<a ng-show="grid.appScope.hasPermit(\'pushManager.imPortPushManagerFromExcel\') && row.entity.targetUser==1 &&row.entity.pushStatus==0 " ng-click="grid.appScope.imPortPushManagerFromExcel(row.entity.id)"> | 导入</a>'
          +'<a ng-show="grid.appScope.hasPermit(\'pushManager.previewPushManager\') && row.entity.pushStatus!=1 && row.entity.pushStatus!=2" ng-click="grid.appScope.previewPushManager(row.entity.id)"> | 预览</a>'
          +'<a ng-show="grid.appScope.hasPermit(\'pushManager.delPushManagerById\') && row.entity.pushStatus==0" ng-click="grid.appScope.delPushManager(row.entity.id)"> | 删除</a>'
      }
    ],
    onRegisterApi: function(gridApi){
      $scope.gridApi = gridApi;
      $scope.gridApi.pagination.on.paginationChanged($scope, function(newPage, pageSize){
        $scope.paginationOptions.pageNo = newPage;
        $scope.paginationOptions.pageSize = pageSize;
        $scope.query();
      });
    }
  };

  //关闭预览
  $scope.cancelPreview = function(){
    $('#previewPushManager').modal('hide');
  }
  //提交预览
  $scope.commitPreview = function(){
    if($scope.previewBaseInfo.previewMerchantNo==null || $scope.previewBaseInfo.previewMerchantNo==""){
      $scope.notice("请输入商户号!");
      return;
    }
    $http.post("pushManager/previewPushManager","merchantNo="+$scope.previewBaseInfo.previewMerchantNo+"&pushId="+$scope.previewBaseInfo.previewPushId,
      {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
      .success(function(data){
        console.log("status ："+data.status);
        if(data.status){
          $scope.notice(data.msg);
          $('#previewPushManager').modal('hide');
        }else{
          $scope.notice(data.msg);
        }
      });
  }

//预览
  $scope.previewPushManager = function(pushId){
    $('#previewPushManager').modal('show');

    $http.post("pushManager/getPushManagerById","id="+pushId,
      {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
      .success(function(data){
        if(data.status){
          $scope.previewBaseInfo=data.baseInfo;
          $scope.previewBaseInfo.previewPushId = pushId;
          $scope.pushObjArrSelected = data.baseInfo.pushObj.split(",");
          if(data.baseInfo.dingshiOrNow==1){
            $scope.showDateSelectedFlag = true;
          }
        }else{
          $scope.notice(data.msg);
        }
      });
  }



  //查询
  $scope.query = function(){

    $http.post('pushManager/selectPushManagerByCondition',"baseInfo="+angular.toJson($scope.baseInfo)+"&jumpUrl="+encodeURIComponent($scope.jumpUrlEncode)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+
      $scope.paginationOptions.pageSize,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
      .success(function(page){
        if(!page){
          return;
        }

        $scope.calendarData = page.result;
        $scope.calendar.totalItems = page.totalCount;
      }).error(function(){
        $scope.notice("系统异常！");
    });
  }
  $scope.query();
  //页面绑定回车事件
  $document.bind("keypress", function(event) {
    $scope.$apply(function (){
      if(event.keyCode == 13){
        $scope.query();
      }
    })
  });

  $scope.delPushManager = function (id) {
    var text = "你确定删除该条推送信息吗?";
    SweetAlert.swal({
        title: text,
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        closeOnConfirm: true,
        closeOnCancel: true
      },
      function (isConfirm) {
        if (isConfirm) {
          var data={"id":id};
          $http.post('pushManager/delPushManagerById',"id="+id,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
              if(data.status){
                $scope.notice(data.msg);
              }else{
                $scope.notice(data.msg);
              }
              $scope.query();
            }).error(function(){
          });
        }
      });
  }
});