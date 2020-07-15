
angular.module('inspinia',['infinity.angular-chosen','angularFileUpload']).controller('userManagerEditCtrl',function($scope,$http,$state,$stateParams,i18nService,$timeout,FileUploader){
	  //数据源
	  i18nService.setCurrentLang('zh-cn');
    $scope.dutyTypeSelect = [{text:"请选择",value:""},{text:"按照销售人员分类",value:1},{text:"按工单类型分类",value:2}];
    $scope.bossUserNameSelect = [];



    $scope.save = function(){
        if($scope.info.bossUserName==null || $scope.info.bossUserName==""){
            $scope.notice("boss账号不能为空!");
            return;
        }
        //如没有勾选了部门工单管理员 负责类型不能为空
        if($scope.info.roleType=="1"){
            if($scope.info.dutyType==null || $scope.info.dutyType==""){
                $scope.notice("负责类型不能为空!");
                return;
            }
            if($scope.info.dutyType==1){
                $scope.preUpdateDutyData = angular.copy($scope.selectedDutyData.saleData);
            }else if($scope.info.dutyType==2){
                $scope.preUpdateDutyData = angular.copy($scope.selectedDutyData.workTypeData);
            }

            console.log("$scope.preUpdateDutyData : "+$scope.preUpdateDutyData);
            
            $scope.info.dutyData = "";
            if($scope.preUpdateDutyData.length >0 ){
                for(var i=0; i<$scope.preUpdateDutyData.length; i++){
                    $scope.info.dutyData += $scope.preUpdateDutyData[i].k +",";
                }
                $scope.info.dutyData = $scope.info.dutyData.substr(0,$scope.info.dutyData.length-1);
            }else{
                var msg = "";
                if($scope.info.dutyType==1){
                    msg = "请选择销售人员!";
                }else{
                    msg = "请选择工单类型!";
                }
                $scope.notice(msg);
                return;
            }
        }
        $scope.submitting = true;
        $http({
            url: 'workOrderUser/edit',
            method: 'POST',
            data: $scope.info
        }).success(function (result) {
            $scope.submitting = false;
            if(result.status){
                $state.transitionTo('workOrder.userManager',null,{reload:false});
            }
            $scope.notice(result.msg);
        }).error(function (result){
            $scope.submitting = false;
        });
    }


    //获取负责类型数据
    $scope.queryDutyTypeData = function(){
        if($scope.info.dutyType==1){
            if($scope.selectedDutyData.saleDataList!=null && $scope.selectedDutyData.saleDataList.length>0){
                $scope.toSelectDutyData = $scope.selectedDutyData.saleDataList;
                $scope.autoColumnsChange();
                return;
            }
        }else if($scope.info.dutyType==2){
            if($scope.selectedDutyData.workTypeDataList!=null && $scope.selectedDutyData.workTypeDataList.length>0){
                $scope.toSelectDutyData = $scope.selectedDutyData.workTypeDataList;
                $scope.autoColumnsChange();
                return;
            }
        }

        $http.post('workOrderUser/getDutyTypeDate',"dutyType="+$scope.info.dutyType,
          {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
        ).success(function(result) {
            if(result.status){
                $scope.toSelectDutyData = result.data;
                if($scope.info.dutyType==1){
                    $scope.selectedDutyData.saleDataList = result.data;
                }else if($scope.info.dutyType==2){
                    $scope.selectedDutyData.workTypeDataList = result.data;
                }
                $scope.autoColumnsChange();
            }else{
                $scope.notice(result.msg);
            }
        }).error(function() {
            $scope.notice("获取负责类型数据失败");
        });
    }

    $scope.autoColumnsChange = function(){
        if($scope.info.dutyType==1){
            $scope.toSelectDutyDataList.columnDefs = [
                {field: 'v',displayName: '销售人员姓名',width: 300}
            ];
        }else if($scope.info.dutyType==2){
            $scope.toSelectDutyDataList.columnDefs = [
                {field: 'v',displayName: '工单类型',width: 300}
            ];
        }
    }



    $scope.selectedDutyData={workTypeDataList:[],saleDataList:[],workTypeData:[],saleData:[]};
    var rowList = [];
    $scope.toSelectDutyDataList = {
        data: 'toSelectDutyData',
        columnDefs: [],
        onRegisterApi : function(gridApi) {
            $scope.gridApiProduct = gridApi;
            //全选
            $scope.gridApiProduct.selection.on.rowSelectionChangedBatch($scope,function (rows) {
                console.log("select all begin .........");
                if(rows[0].isSelected){
                    for(var i=0;i<rows.length;i++){
                        rowList[rows[i].entity.k]=rows[i].entity;
                    }
                    $scope.addList(rowList);
                }else{
                    rowList={};
                    for(var i=0;i<rows.length;i++){
                        $scope.delData(rows[i].entity);
                    }
                }
                console.log("select all end .........");
            })
            $scope.gridApiProduct.selection.on.rowSelectionChanged($scope,function (row) {
                console.log("select single begin .........");
                if(row.isSelected){
                    rowList[row.entity.bpId]=row.entity;
                    $scope.addData(row.entity);
                }
                else{
                    delete rowList[row.entity.k];
                    $scope.delData(row.entity);
                }
                console.log("select single end .........");
            })
        },
        isRowSelectable: function(row){
            //回显选中行
            if($scope.info.dutyData){
                var arr = $scope.info.dutyData.split(",");
                for(var j in arr){
                    if(row.entity.k==arr[j]){
                        row.grid.api.selection.selectRow(row.entity);
                    }
                }
            }
        }
    };



    $scope.getAllUsers = function(){
        $http.get('workOrderUser/getAllUsers')
          .success(function(result) {
              if(!result.status){
                  return;
              }
              if(result.data.length==0) {
                  $scope.bossUserNameSelect.push({value: "", text: "全部"});
              }else{
                  $scope.bossUserNameSelect.push({value: "", text: "全部"});
                  for(var i=0; i<result.data.length; i++){
                      $scope.bossUserNameSelect.push({value:result.data[i].userName,text:result.data[i].userName + " " + result.data[i].realName});
                  }
              }
          });
    }

    $http({
        url: 'workOrderUser/getWorkUserById?id='+$stateParams.id,
        method: 'get',
    }).success(function (result) {
        if(result.status){
            $scope.info = result.data;
            $scope.getAllUsers();
            if($scope.info.roleType=="1"){
                $scope.queryDutyTypeData();
            }else{
                $scope.info.dutyType = "";
            }
        }else{
            $scope.notice(result.msg);
        }

    });

    $scope.addList = function(rowList){
        if(rowList!=null){
            for(var i in rowList){
                if(rowList[i]!=null&&rowList[i]!=""){
                    if($scope.info.dutyType==1){
                        $scope.selectedDutyData.saleData.push({k:rowList[i].k,v:rowList[i].v});
                    }else if($scope.info.dutyType==2){
                        $scope.selectedDutyData.workTypeData.push({k:rowList[i].k,v:rowList[i].v});
                    }

                }
            }
        }
    };

    $scope.addData = function(row){
        if($scope.info.dutyType==1){
            $scope.selectedDutyData.saleData.push({k:row.k,v:row.v});
        }else if($scope.info.dutyType==2){
            $scope.selectedDutyData.workTypeData.push({k:row.k,v:row.v});
        }
    };

    $scope.delData = function(row){
        if(row!=null&&row!=""){
            if($scope.info.dutyType==1){
                for(var j=0;j<$scope.selectedDutyData.saleData.length;j++){
                    var dateItem=$scope.selectedDutyData.saleData[j];
                    if(row.k==dateItem.k){
                        $scope.selectedDutyData.saleData.splice(j, 1);
                    }
                }
            }else if($scope.info.dutyType==2){
                for(var j=0;j<$scope.selectedDutyData.workTypeData.length;j++){
                    var dateItem=$scope.selectedDutyData.workTypeData[j];
                    if(row.k==dateItem.k){
                        $scope.selectedDutyData.workTypeData.splice(j, 1);
                    }
                }
            }

            for(var j=0;j<$scope.toSelectDutyData.length;j++){
                var dateItem=$scope.toSelectDutyData[j];
                if(row.k==dateItem.k){
                    $scope.gridApiProduct.selection.unSelectRow($scope.toSelectDutyData[j]);
                }
            }
        }
    };
});


