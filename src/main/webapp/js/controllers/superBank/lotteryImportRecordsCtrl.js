/**
 * 彩票记录导入管理
 */
angular.module('inspinia', ['infinity.angular-chosen', 'angularFileUpload']).controller('lotteryImportRecordsCtrl',function($scope,$http,i18nService,$document,SweetAlert,$timeout,FileUploader){
    //数据源
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    $scope.statusList = [{text:"全部",value:""},{text:"待匹配",value:"1"},{text:"已匹配",value:"2"}];//订单状态
    $scope.lotteryTypes = [{text:"全部",value:""},{text:"福彩",value:"1"},{text:"体彩",value:"2"}];//彩票类型
    $scope.lotteryTypesStr = '[{text:"福彩",value:"1"},{text:"体彩",value:"2"}]';
    
    $scope.resetForm = function () {
        $scope.record = {status:"",
        	importDateStart:moment(new Date().getTime()-24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
        	importDateEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
        	lotteryType:""
        };
    }
    $scope.resetForm();

    $scope.columnDefs = [
	     {field: 'batchNo',displayName: '批次号',width: 180,pinnable: false,sortable: false},
	     {field: 'lotteryType',displayName: '彩票类型',width: 180,pinnable: false,sortable: false,cellFilter:"formatDropping:" + $scope.lotteryTypesStr},
	     {field: 'importDate',displayName: '导入时间',width: 180,pinnable: false,sortable: false},
	     {field: 'fileName',displayName: '文件名',width: 200,pinnable: false,sortable: false,cellTemplate:'<div  class="lh30"><a ng-href="{{row.entity.fileUrl}}" target="_self">{{row.entity.fileName}}</a></div>'},
	     {field: 'status',displayName: '进度状态',width: 100,pinnable: false,sortable: false,cellTemplate:'<div class="lh30" ng-show="row.entity.status==1">待匹配</div><div class="lh30" ng-show="row.entity.status==2">已匹配</div><div class="lh30" ng-show="row.entity.status==3">匹配中</div>'},
	     {field: 'remark',displayName: '匹配结果',width: 200,pinnable: false,sortable: false},
	     {field: 'updateDate',displayName: '系统处理时间',width: 180,pinnable: false,sortable: false},
	     {field: 'updateBy',displayName: '导入操作人',width: 160,pinnable: false,sortable: false},
	     {field: 'action',displayName: '操作',width: 120,pinnedRight:true,
	        	cellTemplate:
	        	'<div  class="lh30" ng-show="row.entity.status==1"><a ng-click="grid.appScope.deleteImportRecord(row.entity)">删除</a></div>'
	        	+
	        	'<div  class="lh30" ng-show="row.entity.action==0 || row.entity.action==2">删除</div>'
	        }
     
     ];

    $scope.lotteryImportRecordsGrid = {
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
//		rowHeight:35,
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
            url: 'superBank/lotteryImportList?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
            data: $scope.record,
            method:'POST'
        }).success(function (result) {
            $scope.submitting = false;
            $scope.loadImg = false;
            if (!result.status){
                $scope.notice(result.msg);
                return;
            }
            $scope.lotteryImportRecordsGrid.data = result.data.result;
            $scope.lotteryImportRecordsGrid.totalItems = result.data.totalCount;
        }).error(function () {
            $scope.submitting = false;
            $scope.loadImg = false;
            $scope.notice('服务器异常,请稍后再试.');
        });
    };

    $scope.deleteImportRecord = function(entity){
   	 var data = {
   			 	"id" : entity.id
   		};
   	 	$http.post("superBank/deleteLotteryImport",angular.toJson(data))
   		.success(function(data){
   			if(data.status){
   				$scope.notice(data.msg);
   				$scope.query();
   			}else{
   				$scope.notice(data.msg);
   			}
   		});
   };

   $scope.modulTypes = [{text:"福彩",value:"1"},{text:"体彩",value:"2"}];
   
    //上传文件,定义控制器路径
    $scope.uploader = new FileUploader({
        url: 'superBank/importLottery',
        queueLimit: 1,   //文件个数
        removeAfterUpload: true,  //上传后删除文件
        headers : {'X-CSRF-TOKEN' : $scope.csrfData},
        formData:[{id:"",bonusType:"",lotteryType:"1"}]
    });
    //过滤格式
    $scope.uploader.filters.push({
        name: 'fileFilter',
        fn: function(item /*{File|FileLikeObject}*/, options) {
            var type = '|' + item.name.slice(item.name.lastIndexOf('.') + 1) + '|';
            if('|xlsx|xls|'.indexOf(type) == -1){
            	$scope.notice("文件有误，请重新提交!");
            }
            return '|xlsx|xls|'.indexOf(type) !== -1;
        }
    });
    //批量导入modal
    $scope.importModal = function(){
        $scope.importInfo.id = "";
        $scope.importInfo.bonusType = "1";

        $scope.importInfo.lotteryType = "1";
        
        $('#importModal').modal('show');
    }

    $scope.cancel = function(){
        $('#importModal').modal('hide');
    }

   
    
    //批量导入提交数据
    $scope.importStatus = false;
    
    $scope.importInfo = function(){
    	  SweetAlert.swal({
              title: "确认彩票类型与表格数据一致？",
              type: "warning",
              showCancelButton: true,
              confirmButtonColor: "#DD6B55",
              confirmButtonText: "确定",
              cancelButtonText: "取消",
              closeOnConfirm: true,
              closeOnCancel: true },
          function (isConfirm) {
              if (isConfirm) {
            	  
            	  $scope.importStatus = true;
                  $scope.uploader.queue[0].formData[0].id = $scope.importInfo.id;
                  $scope.uploader.queue[0].formData[0].bonusType = $scope.importInfo.bonusType;
                  $scope.uploader.queue[0].formData[0].lotteryType = $scope.importInfo.lotteryType;
                  if($scope.uploader.queue.length > 0){
                      $scope.uploader.uploadAll();
                      $scope.uploader.onSuccessItem = function(fileItem, response, status, headers) {//上传成功后的回调函数，在里面执行提交
                          $scope.importStatus = false;
                          $scope.notice(response.msg);
                          $scope.cancel();
                          $scope.query();
                      };
                  }
                  
              }
         });
    };

    $scope.downTemplate = function (id,bonusType) {
        if(!id){
            $scope.notice("银行不能为空");
            return;
        }
        location.href = "bankImport/importTemplate?id=" + encodeURI(id) + "&bonusType=" + bonusType;
    }

    //页面绑定回车事件
    $document.bind("keypress", function(event) {
        $scope.$apply(function (){
            if(event.keyCode == 13){
                $scope.query();
            }
        });
    });
});