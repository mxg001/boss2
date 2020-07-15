/**
 * 代理商奖励活动
 */
angular.module('inspinia').controller('selectHlfGroupCtrl',function(SweetAlert,i18nService,$scope,$http,$state,$stateParams,$compile,$filter,$log,$uibModal){
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    //活动类型
    $scope.subjectTypes = [{text:"欢乐返",value:"009"},{text:"新欢乐送",value:"010"},{text:"超级返活动",value:"011"}/*,{text:"欢乐返-循环送",value:"008"}*/];

    $scope.info={groupName:"",activityCode:"009",activityTypeNo:""};
    $scope.firstRepeatStatuses = [{text:'是',value:'1'},{text:'否',value:'0'}];
    $scope.submitting = false;
    $scope.saveStatus=0;//0保存1修改2详情

    //清空
    $scope.clear=function(){
        $scope.hlfGroup={id:"",groupName:""};
    };
    $scope.clear();

    $scope.checkActivityTypeNo = function(activityTypeNo){

        if(activityTypeNo==null||activityTypeNo==""){
            $scope.checkActivityCode($scope.info.activityCode);
            return;
        }
        $scope.hlfDataX=[];
        if($scope.info.activityCode=="008"){
            $scope.hlfDataX=$scope.hlfData1;
        }else if($scope.info.activityCode=="009"){
            $scope.hlfDataX=$scope.hlfData2;
        }else if($scope.info.activityCode=="010"){
            $scope.hlfDataX=$scope.hlfData3;
        }else if($scope.info.activityCode=="011"){
            $scope.hlfDataX=$scope.hlfData4;
        }
        for(var index=0;index<$scope.hlfDataX.length;index++){
            var x=$scope.hlfDataX[index];
            if(x.activityTypeNo==activityTypeNo){
                $scope.hlfData=[];
                $scope.hlfData.push(x);
                break;
            }
        }
    }

    //欢乐返活动子类型
    $scope.checkActivityCode = function(activityCode){
        $scope.typeNos=[];
        $scope.hlfData=[];
        if(activityCode=="008"){
            $scope.typeNos=$scope.typeNos1;
            $scope.hlfData=$scope.hlfData1;
        }else if(activityCode=="009"){
            $scope.typeNos=$scope.typeNos2;
            $scope.hlfData=$scope.hlfData2;
        }else if(activityCode=="010"){
            $scope.typeNos=$scope.typeNos3;
            $scope.hlfData=$scope.hlfData3;
        }else if(activityCode=="011"){
            $scope.typeNos=$scope.typeNos4;
            $scope.hlfData=$scope.hlfData4;
        }

    };
    $scope.checkActivityCode1 = function(){
        $scope.typeNos1=[];
        $scope.hlfData1=[];
        $http.post("activity/queryByactivityTypeNoList","008").success(function (data) {
            if(data.status){
                $scope.hlfData1 = data.info;
                for(var i=0; i<data.info.length; i++){
                    $scope.typeNos1.push({value:data.info[i].activityTypeNo,text:data.info[i].activityTypeNo+" "+data.info[i].activityTypeName});
                }
            }
        })
    };
    $scope.checkActivityCode1();

    $scope.checkActivityCode2 = function(){
        $scope.typeNos2=[];
        $scope.hlfData2=[];
        $http.post("activity/queryByactivityTypeNoList","009").success(function (data) {
            if(data.status){
                $scope.hlfData2 = data.info;
                for(var i=0; i<data.info.length; i++){
                    $scope.typeNos2.push({value:data.info[i].activityTypeNo,text:data.info[i].activityTypeNo+" "+data.info[i].activityTypeName});
                }
            }
        })
    };
    $scope.checkActivityCode2();


    $scope.checkActivityCode3 = function(){
        $scope.typeNos3=[];
        $scope.hlfData3=[];
        $http.post("activity/queryByactivityTypeNoList","010").success(function (data) {
            if(data.status){
                $scope.hlfData3 = data.info;
                for(var i=0; i<data.info.length; i++){
                    $scope.typeNos3.push({value:data.info[i].activityTypeNo,text:data.info[i].activityTypeNo+" "+data.info[i].activityTypeName});
                }
            }
        })
    };
    $scope.checkActivityCode3();


    $scope.checkActivityCode4 = function(){
        $scope.typeNos4=[];
        $scope.hlfData4=[];
        $http.post("activity/queryByactivityTypeNoList","011").success(function (data) {
            if(data.status){
                $scope.hlfData4 = data.info;
                for(var i=0; i<data.info.length; i++){
                    $scope.typeNos4.push({value:data.info[i].activityTypeNo,text:data.info[i].activityTypeNo+" "+data.info[i].activityTypeName});
                }
            }
        })
    };
    $scope.checkActivityCode4();

    $scope.hlfList = {
        data: 'hlfData',
        // enableHorizontalScrollbar: true,        //横向滚动条
        // enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs: [
            {field: 'activityTypeNo',displayName: '子类型编号',width: 150},
            {field: 'activityTypeName',displayName: '子类型名称',width:150},
            {field: 'orgName',displayName: '所属组织',width: 150},
            {field: 'teamEntryName',displayName: '所属子组织',width: 150},
        ],
        onRegisterApi : function(gridApi) {
            $scope.gridApiGroup = gridApi;
            //全选
            $scope.gridApiGroup.selection.on.rowSelectionChangedBatch($scope,function (rows) {
                if(rows[0].isSelected){
                    $scope.testRow = rows[0].entity;
                    for(var i=0;i<rows.length;i++){
                        rowList[rows[i].entity.activityTypeNo]=rows[i].entity;
                    }
                    $scope.addList(rowList);
                }else{
                    rowList={};
                    for(var i=0;i<rows.length;i++){
                        $scope.delteData(rows[i].entity);
                    }
                }
            })
            //单选
            $scope.gridApiGroup.selection.on.rowSelectionChanged($scope,function (row) {
                if(row.isSelected){
                    rowList[row.entity.activityTypeNo]=row.entity;
                    $scope.addData(row.entity);
                }
                else{
                    delete rowList[row.entity.activityTypeNo];
                    $scope.delteData(row.entity);
                }
            })
        },
       isRowSelectable: function(row){ // 选中行
            if($scope.addhlfData != null && $scope.addhlfData.length>0){
                for(var i=0;i<$scope.addhlfData.length;i++){
                    if(row.entity.activityTypeNo==$scope.addhlfData[i].activityTypeNo){
                        row.grid.api.selection.selectRow(row.entity);
                    }
                }
            }
        }
    };


    $scope.addhlfData=[];
    var rowList = [];
    $scope.addhlfList = {
        data: 'addhlfData',
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs : [                           //表格数据
            {field: 'activityTypeNo',displayName: '子类型编号',width: 150},
            {field: 'activityTypeName',displayName: '子类型名称',width:150},
            {field: 'orgName',displayName: '所属组织',width: 150},
            {field: 'teamEntryName',displayName: '所属子组织',width: 150},
            {field: 'action',displayName: '操作',width:80,pinnedRight:true,cellTemplate:
                    '<a ng-show="grid.appScope.saveStatus!=2" class="lh30" ng-click="grid.appScope.delteData(row.entity)">移除</a> '}],
        onRegisterApi: function(gridApi) {
            $scope.addhlfListGridApi = gridApi;
        },
    };

    $scope.addhlfList2 = {
        data: 'addhlfData',
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs : [                           //表格数据
            {field: 'activityTypeNo',displayName: '子类型编号',width: 150},
            {field: 'activityTypeName',displayName: '子类型名称',width:150},
            {field: 'orgName',displayName: '所属组织',width: 150},
            {field: 'teamEntryName',displayName: '所属子组织',width: 150}],
        onRegisterApi: function(gridApi) {
            $scope.addhlfListGridApi2 = gridApi;
        },
    };

    $scope.addList = function(rowList){
        if(rowList!=null){
            for(var i in rowList){
                if(rowList[i]!=null&&rowList[i]!=""){
                    if($scope.checkData($scope.addhlfData,rowList[i],null)){
                        $scope.addhlfData.push({
                            activityTypeNo:rowList[i].activityTypeNo,
                            activityTypeName:rowList[i].activityTypeName,
                            orgId:rowList[i].orgId,
                            orgName:rowList[i].orgName,
                            teamEntryId:rowList[i].teamEntryId,
                            teamEntryName:rowList[i].teamEntryName,
                        });
                        //$scope.cancel();
                    }
                }
            }
        }
    };

    $scope.addData = function(row){
        if(row!=null&&row!=""){
            if($scope.checkData($scope.addhlfData,row,null)){
                $scope.addhlfData.push({
                    activityTypeNo:row.activityTypeNo,
                    activityTypeName:row.activityTypeName,
                    orgId:row.orgId,
                    orgName:row.orgName,
                    teamEntryId:row.teamEntryId,
                    teamEntryName:row.teamEntryName
                });
               // $scope.cancel();
            }
        }
    };

    $scope.checkData = function(dataList,info,oldInfo){
        if(dataList!=null&&dataList.length>0){
            for(var i=0;i<dataList.length;i++){
                var item=dataList[i];
                if(oldInfo!=null){
                    if(item.activityTypeNo==oldInfo.activityTypeNo){
                        continue;
                    }
                }
                if(item.activityTypeNo==info.activityTypeNo){
                    return false;
                }
            }
        }
        return true;
    };

    $scope.delteData = function(row){
        if(row!=null&&row!=""){
            for(var j=0;j<$scope.addhlfData.length;j++){
                var dateItem=$scope.addhlfData[j];
                if(row.activityTypeNo==dateItem.activityTypeNo){
                    $scope.addhlfData.splice(j, 1);
                }
            }
            for(var j=0;j<$scope.hlfData.length;j++){
                var dateItem=$scope.hlfData[j];
                if(row.activityTypeNo==dateItem.activityTypeNo){
                    $scope.gridApiGroup.selection.unSelectRow($scope.hlfData[j]);
                }
            }
        }
    };





    // 配置表格
    $scope.gridOptions={						//配置表格
        paginationPageSize:10,					//分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        enableHorizontalScrollbar: 1,			//横向滚动条
        enableVerticalScrollbar : 1,			//纵向滚动条
        columnDefs:[							//表格数据
            { field: 'id',displayName:'ID',width:100},
            { field: 'groupName',displayName:'分组名称',width:150},
            { field: 'createTime',displayName:'创建日期',width:240,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
            { field: 'action',displayName:'操作',width:200,pinnedRight:true,editable:true,
                cellTemplate:
                '<a class="lh30" ng-show="grid.appScope.hasPermit(\'activity.editHlfGroup\')" ng-click="grid.appScope.hlfGroupEdit(row.entity.id)">编辑</a> ' +
                '<a class="lh30" ng-click="grid.appScope.detail(row.entity.id)">| 详情</a> ' +
                '<a class="lh30" ng-show="grid.appScope.hasPermit(\'activity.deleteHlfGroup\')" ng-click="grid.appScope.delete(row.entity.id)">| 删除</a> '
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

    $scope.query = function () {
        $http({
            url: 'activity/selectHlfGroup?pageNo=' + $scope.paginationOptions.pageNo + "&pageSize=" + $scope.paginationOptions.pageSize,
            data: $scope.hlfGroup,
            method: 'POST'
        }).success(function (data) {
            if(data.status){
                $scope.gridOptions.data = data.page.result;
                $scope.gridOptions.totalItems = data.page.totalCount;
                console.log(data.page.result);
            }else{
                $scope.notice(data.msg);
            }
        }).error(function(){
        });
    };
    $scope.query();

    $scope.checkInputDay= function(inputDay){
        var isInt = /^[1-9]\d*$/;//正整数
        if(inputDay==0||isInt.test(inputDay)){
            return true;
        }
        return false;
    }
    $scope.checkInputAmount= function(inputAmount){
        var isNum=/^(([1-9][0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,2})))$/;
        if(inputAmount==0||isNum.test(inputAmount)){
            return true;
        }
        return false;
    }

	//提交
    $scope.commit = function(){

        if($scope.info.groupName==null||$scope.info.groupName==""){
            $scope.notice("分组名称不能为空!");
            return;
        }
        if($scope.info.groupName.length>20){
            $scope.notice("分组名称不能超过20字!");
            return;
        }
        var firstOrgId;
        var firstTeamEntryId;
        var nextOrgId;
        var nextTeamEntryId;
        for(var i=0;i<$scope.addhlfData.length;i++){
            var groupItem=$scope.addhlfData[i];
            if(i==0){
                firstOrgId=groupItem.orgId;
                firstTeamEntryId=groupItem.teamEntryId;
            }else{
                nextOrgId=groupItem.orgId;
                nextTeamEntryId=groupItem.teamEntryId;
                if(firstOrgId!=nextOrgId){
                    $scope.notice("所属组织不一致不可添加在同一分组");
                    return;
                }else{
                    if(firstTeamEntryId!=nextTeamEntryId){
                        $scope.notice("所属组织不一致不可添加在同一分组");
                        return;
                    }
                }
            }


        }




        if ($scope.submitting == true) {
            return;
        }
        $scope.submitting = true;
        var data = {
            "info" : $scope.info,
            "addhlfData" :$scope.addhlfData
        };
        if($scope.saveStatus==0){
            $http.post("activity/addHlfGroup",angular.toJson(data))
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
            $http.post("activity/editHlfGroup",angular.toJson(data))
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

	//取消
    $scope.submitCancel=function(){
        $scope.query();
    }
    $scope.submitCancel();

    $scope.isDetail=false;
    $scope.detail= function(id){
        $scope.isDetail=true;
        $scope.saveStatus=2;
        $http.post("activity/selectHlfGroupById",angular.toJson(id)).success(function (data) {
            if(data.status){
                $scope.info = data.param;
                $scope.addhlfData=data.list;
                $scope.info.activityCode="009";
                $scope.checkActivityCode("009");
                $("#groupDetailModel").modal("show");
            }
        })
    }



	//修改配置
    $scope.hlfGroupEdit = function(id){
        $scope.isDetail=false;
        $scope.saveStatus=1;
        $http.post("activity/selectHlfGroupById",angular.toJson(id)).success(function (data) {
            if(data.status){
                $scope.info = data.param;
                //$scope.addhlfData=data.list;
                $scope.hlfData=[];
                $scope.addhlfData=[];
                $scope.info.activityCode="009";
                $scope.checkActivityCode("009");
                $("#groupAddModel").modal("show");
                $scope.checkShow(data.list);
            }
        })
    }

    $scope.checkShow = function(list){
        $scope.gridApiGroup.selection.clearSelectedRows();
        var rows1=[];

        rows1=rows1.concat($scope.hlfData2);
        rows1=rows1.concat($scope.hlfData3);
        rows1=rows1.concat($scope.hlfData4);
        var rows2=list;
        var isCheck=false;
        if(rows1 != null && rows1.length>0){
            if(rows2 != null && rows2.length>0){

                for(var i=0;i<rows1.length;i++){
                    isCheck=false;
                    for(var s=0;s<rows2.length;s++){
                        if(rows1[i].activityTypeNo==rows2[s].activityTypeNo){
                            isCheck=true;
                            break;
                        }
                    }
                    if(isCheck){
                        $scope.addData(rows1[i]);
                        $scope.gridApiGroup.selection.selectRow(rows1[i]);
                    }else{
                        $scope.gridApiGroup.selection.unSelectRow(rows1[i]);
                    }

                }

            }
        }
    }



	//添加
    $scope.groupAddModel = function(){
        $scope.checkActivityCode("009");
        $scope.addhlfData=[];
        $scope.isDetail=false;
        $scope.saveStatus=0;
        $("#groupAddModel").modal("show");
        $scope.gridApiGroup.selection.clearSelectedRows();
        $scope.info={groupName:"",activityCode:"009",activityTypeNo:""};
    }
    //返回
    $scope.cancel=function(){
        $scope.info={groupName:"",activityCode:"009",activityTypeNo:""};
        $('#groupAddModel').modal('hide');
        $("#groupDetailModel").modal("hide");
    }

    $scope.delete=function(id){
        SweetAlert.swal({
                title: "如分组删除后，将清空此分组下的所有子类型，是否继续操作？",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确认",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post("activity/deleteHlfGroupById",id)
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



});
