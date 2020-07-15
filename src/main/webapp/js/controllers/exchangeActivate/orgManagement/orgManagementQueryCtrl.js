/**
 * 机构列表
 */
angular.module('inspinia',['angularFileUpload']).controller('exchangeActivateOrgQueryCtrl',function($scope,$http,i18nService, FileUploader,SweetAlert,$document){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    $scope.orgStatusSelect = [{text:"全部",value:""},{text:"关闭",value:"0"},{text:"开启",value:"1"}];
    $scope.orgStatusStr=angular.toJson($scope.orgStatusSelect);

    $scope.financeSelect = [{text:"全部",value:""},{text:"否",value:"0"},{text:"是",value:"1"}];
    $scope.financeStr=angular.toJson($scope.financeSelect);

    //清空
    $scope.clear=function(){
        $scope.info={orgName:"",finance:""};
    };
    $scope.clear();

    $scope.userGrid={                           //配置表格
        data: 'result',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'id',displayName:'序号',width:100},
            { field: 'orgCode',displayName:'编码',width:180 },
            { field: 'orgName',displayName:'机构名称',width:180 },
            { field: 'orgStatus',displayName:'状态',width:120,cellFilter:"formatDropping:" +  $scope.orgStatusStr },
            { field: 'orgLogo',displayName:'logo',width:180,cellTemplate:'' +
            '<div ng-show="row.entity.orgLogo!=null"> ' +
                '<a href="{{row.entity.orgLogo}}" fancybox rel="group">' +
                    '<img style="width:70px;height:35px;" ng-src="{{row.entity.orgLogo}}"/>' +
                '</a>' +
            '</div>'
            },
            { field: 'sort',displayName:'顺序',width:180},
            { field: 'finance',displayName:'是否具备金融属性',width:120,cellFilter:"formatDropping:" +  $scope.financeStr },
            { field: 'remark',displayName:'积分查询方式',width:180 },
            { field: 'id',displayName:'操作',width:180,pinnedRight:true, cellTemplate:
            '<div class="lh30">'+
            '<a target="_blank" ui-sref="exchangeActivate.orgManagementDetail({id:row.entity.id})">详情</a> ' +
            '<a ng-show="grid.appScope.hasPermit(\'exchangeActivateOrg.updateOrgManagement\')" target="_blank" ui-sref="exchangeActivate.orgManagementEdit({id:row.entity.id})"> | 编辑</a> ' +
            '<a ng-show="row.entity.orgStatus==0&&grid.appScope.hasPermit(\'exchangeActivateOrg.updateOrgStatus\')" ng-click="grid.appScope.updateOrgStatus(row.entity.id,row.entity.orgStatus)""> | 开启</a> ' +
            '<a ng-show="row.entity.orgStatus==1&&grid.appScope.hasPermit(\'exchangeActivateOrg.updateOrgStatus\')" ng-click="grid.appScope.updateOrgStatus(row.entity.id,row.entity.orgStatus)"> | 关闭</a> ' +
            '</div>'
            }
        ],
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.paginationOptions.pageNo = newPage;
                $scope.paginationOptions.pageSize = pageSize;
                $scope.query();
            });
        }
    };
    $scope.query=function(){
        if ($scope.loadImg) {
            return;
        }
        $scope.loadImg = true;
        $http.post("exchangeActivateOrg/selectAll","info="+angular.toJson($scope.info)+"&pageNo="+
            $scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.result=data.page.result;
                    $scope.userGrid.totalItems = data.page.totalCount;
                }else{
                    $scope.notice(data.msg);
                }
                $scope.loadImg = false;
            })
            .error(function(data){
                $scope.notice(data.msg);
                $scope.loadImg = false;
            });
    };

    $http.post("exchangeActivateOrg/getaddOrgDefault",
        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function(data){
            if(data.status){
                $scope.orgDefault=data.listOrgDefault;
                $scope.listOrgDefault = angular.copy($scope.orgDefault);
            }else{
                $scope.notice(data.msg);
            }
        })
        .error(function(data){
            $scope.notice(data.msg);
        });


    //开启
    $scope.updateOrgStatus = function(id,orgStatus){
        var str="";
        var state=0;
        if(orgStatus==0){
            str="确认开启?";
            state=1;
        }else{
            str="确认关闭该机构,关闭后前端用户将无法申请该机构产品?";
        }
        SweetAlert.swal({
                title: str,
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post("exchangeActivateOrg/updateOrgStatus","id="+id+"&state="+state,
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function(data){
                            if(data.status){
                                $scope.notice(data.msg);
                                $scope.query();
                            }else{
                                $scope.notice(data.msg);
                            }
                        })
                        .error(function(data){
                            $scope.notice(data.msg);
                        });

                }
            });
    };

    $scope.addBankModal = function(){
        $scope.addinfo={};
        $scope.addinfo.finance="1";
        $scope.listOrgDefault = angular.copy($scope.orgDefault);
        uploaderImg.cancelAll();
        $('#addBankModal').modal('show');
    };
    $scope.cancel = function(){
        $('#addBankModal').modal('hide');
    };

    $scope.submitting = false;
    //开启
    $scope.addBank = function(){
        if($scope.submitting){
            return;
        }
        if(uploaderImg.queue.length<1){
            $scope.notice("logo不能为空");
            return;
        }
        if($scope.addinfo.finance==null||$scope.addinfo.finance==""){
            $scope.notice("是否具备金融属性不能为空");
            return;
        }
        $scope.submitting = true;
        uploaderImg.uploadAll();// 上传消息图片
        uploaderImg.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
            if (response.url != null) { // 回调参数url
                $scope.addinfo.orgLogo= response.url;

                $http.post("exchangeActivateOrg/addOrgManagement","info="+angular.toJson($scope.addinfo)+"&list="+angular.toJson($scope.listOrgDefault),
                    {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                    .success(function(data){
                        $scope.submitting = false;
                        if(data.status){
                            $scope.notice(data.msg);
                            $scope.cancel();
                            $scope.query();
                        }else{
                            $scope.notice(data.msg);
                        }
                    })
                    .error(function(data){
                        $scope.submitting = false;
                        $scope.notice(data.msg);
                    });
            }
        };
    };

    //上传图片,定义控制器路径
    var uploaderImg = $scope.uploaderImg = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数
        removeAfterUpload: true,  //上传后删除文件
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    uploaderImg.filters.push({
        name: 'imageFilter',
        fn: function(item, options) {
            return this.queue.length < 1;
        }
    });
    //过滤格式
    uploaderImg.filters.push({
        name: 'imageFilter',
        fn: function(item,options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });

    // 导出
    $scope.exportInfo = function () {
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
                    location.href = "exchangeActivateOrg/importDetail?info=" + encodeURI(angular.toJson($scope.info));
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