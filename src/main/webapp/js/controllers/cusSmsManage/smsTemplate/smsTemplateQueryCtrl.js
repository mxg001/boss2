/**
 * 短信模板列表
 */
angular.module('inspinia').controller('smsTemplateQueryCtrl',function($scope,$http,i18nService,SweetAlert,$document){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    $scope.statusSelect = [{text:"全部",value:"-1"},{text:"正常",value:"1"},
        {text:"待下发",value:"2"},{text:"置顶",value:"3"}];
    $scope.statusStr=angular.toJson($scope.statusSelect);

    $scope.departmentSelect=angular.copy($scope.departmentTypeList);
    $scope.departmentSelect.unshift({text:"全部",value:""});
    $scope.departmentStr=angular.toJson($scope.departmentSelect);

    $scope.typeSelect =angular.copy($scope.smsBusinessTypeList);
    $scope.typeSelect.unshift({text:"全部",value:""});
    $scope.typeStr=angular.toJson($scope.typeSelect);

    //清空
    $scope.clear=function(){
        $scope.info={department:"",type:"",
            createTimeBegin:moment(new Date().getTime()-6*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
            createTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'
        };
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
            { field: 'id',displayName:'模板编号',width:180},
            { field: 'department',displayName:'所属部门',width:120,cellFilter:"formatDropping:" +  $scope.departmentStr },
            { field: 'type',displayName:'业务类型',width:120,cellFilter:"formatDropping:" +  $scope.typeStr },
            { field: 'template',displayName:'模板内容',width:600 },
            { field: 'createOperator',displayName:'创建人',width:180 },
            { field: 'createTime',displayName:'创建时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'lastUpdateOperator',displayName:'操作人',width:180 },
            { field: 'lastUpdateTime',displayName:'操作时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'id',displayName:'操作',width:220,pinnedRight:true, cellTemplate:
                '<div class="lh30">'+
                    '<a target="_blank" ng-click="grid.appScope.addInfoModalDetailShow(row.entity)">详情</a> ' +
                    '<a ng-show="grid.appScope.hasPermit(\'cusSmsTemplate.editSmsTemplate\')" ng-click="grid.appScope.addInfoModalShow(2,row.entity)"> | 修改</a> ' +
                    '<a ng-show="grid.appScope.hasPermit(\'cusSmsTemplate.deleteSmsTemplate\')" ng-click="grid.appScope.deleteInfo(row.entity.id)"> | 删除</a> ' +
                    '<a ng-show="grid.appScope.hasPermit(\'cusSmsTemplate.sendTemplate\')" target="_blank" ui-sref="cusSms.smsTemplateSend({id:row.entity.id})"> | 发送</a> ' +
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
        $http.post("cusSmsTemplate/selectAll","info="+angular.toJson($scope.info)+"&pageNo="+
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

    $scope.editSta=false;
    $scope.addInfoModalShow=function(sta,entity){
        if(sta==1){//新增
            $scope.addInfo={department:"",type:""};
            $scope.title="新增短信模板";
            $scope.editSta=false;
        }else{//修改
            $scope.addInfo=angular.copy(entity);
            $scope.title="修改短信模板";
            $scope.editSta=true;
        }
        $('#addInfoModal').modal('show');
    };
    $scope.addInfoModalHide = function(){
        $('#addInfoModal').modal('hide');
    };
    $scope.addInfoModalDetailShow=function(entity){
        $scope.addInfo=angular.copy(entity);
        $('#addInfoModalDetail').modal('show');
    };
    $scope.addInfoModalDetailHide = function(){
        $('#addInfoModalDetail').modal('hide');
    };
    //提交数据
    $scope.submitInfo=function(){
        var titleStr="";
        var url="";
        if($scope.editSta){
            titleStr="确认修改短信模板?";
            url="cusSmsTemplate/editSmsTemplate";
        }else{
            titleStr="确认新增短信模板?";
            url="cusSmsTemplate/addSmsTemplate";
        }
        SweetAlert.swal({
                title: titleStr,
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    var data={
                        info:angular.toJson($scope.addInfo),
                    };
                    var postCfg = {
                        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                        transformRequest: function (data) {
                            return $.param(data);
                        }
                    };
                    $http.post(url,data,postCfg)
                        .success(function(data){
                            if(data.status){
                                $scope.notice(data.msg);
                                $scope.query();
                                $scope.addInfoModalHide();
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

    //删除
    $scope.deleteInfo=function(id){
        SweetAlert.swal({
                title: "确认删除该短信模板?",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post("cusSmsTemplate/deleteSmsTemplate","id="+id,
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

    //页面绑定回车事件
    $document.bind("keypress", function(event) {
        $scope.$apply(function (){
            if(event.keyCode == 13){
                $scope.query();
            }
        })
    });
});