
angular.module('inspinia',['infinity.angular-chosen','angularFileUpload']).controller('userManagerAddCtrl',function($scope,$http,$state,$stateParams,i18nService,$timeout,FileUploader){
	//数据源
	  i18nService.setCurrentLang('zh-cn');
	  $scope.dutyTypeSelect = [{text:"请选择",value:""},{text:"按照销售人员分类",value:1},{text:"按工单类型分类",value:2}];
    $scope.info = {dutyType:"",roleType:"1"};



    $scope.bossUserNameSelect = [];
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


    $scope.add = function(){
        if($scope.info.bossUserName==null || $scope.info.bossUserName==""){
            $scope.notice("boss账号不能为空!");
            return;
        }
        //如勾选了部门工单管理员选项则忽略负责类型
        if($scope.info.roleType=="1"){
           if($scope.info.dutyType==null || $scope.info.dutyType==""){
               $scope.notice("负责类型不能为空!");
               return;
           }
            $scope.info.dutyData = "";
            if($scope.selectedDutyData.length >0 ){
                for(var i=0; i<$scope.selectedDutyData.length; i++){
                    $scope.info.dutyData += $scope.selectedDutyData[i].k +",";
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
            url: 'workOrderUser/add',
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

    $scope.selectedDutyData=[];
    var rowList = [];
    $scope.toSelectDutyDataList = {
        data: 'toSelectDutyData',
        columnDefs: [],
        onRegisterApi : function(gridApi) {
            $scope.gridApiProduct = gridApi;
            //全选
            $scope.gridApiProduct.selection.on.rowSelectionChangedBatch($scope,function (rows) {
                if(rows[0].isSelected){
                    for(var i=0;i<rows.length;i++){
                        rowList[rows[i].entity.k]=rows[i].entity;
                    }
                    $scope.addList(rowList);
                }else{
                    rowList={};
                    for(var i=0;i<rows.length;i++){
                        $scope.delteData(rows[i].entity);
                    }
                }
            })
            $scope.gridApiProduct.selection.on.rowSelectionChanged($scope,function (row) {
                if(row.isSelected){
                    rowList[row.entity.bpId]=row.entity;
                    $scope.addData(row.entity);
                }
                else{
                    delete rowList[row.entity.k];
                    $scope.delteData(row.entity);
                }
            })
        },
        isRowSelectable: function(row){ // 选中行

        }
    };
    $scope.delteData = function(row){
        if(row!=null&&row!=""){
            for(var j=0;j<$scope.selectedDutyData.length;j++){
                var dateItem=$scope.selectedDutyData[j];
                if(row.k==dateItem.k){
                    $scope.selectedDutyData.splice(j, 1);
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

    //获取负责类型数据
    $scope.queryDutyTypeData = function(){
        $http.post('workOrderUser/getDutyTypeDate',"dutyType="+$scope.info.dutyType,
          {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
        ).success(function(result) {
            if(result.status){
                $scope.toSelectDutyData = result.data;
                $scope.selectedDutyData=[];
                if($scope.info.dutyType==1){
                    $scope.toSelectDutyDataList.columnDefs = [
                        {field: 'v',displayName: '销售人员姓名',width: 300}
                    ];
                }else if($scope.info.dutyType==2){
                    $scope.toSelectDutyDataList.columnDefs = [
                        {field: 'v',displayName: '工单类型',width: 300}
                    ];
                }
            }else{
                $scope.notice(result.msg);
            }

        }).error(function() {
            $scope.notice("获取负责类型数据失败");
        });
    }

    $scope.addList = function(rowList){
        if(rowList!=null){
            for(var i in rowList){
                if(rowList[i]!=null&&rowList[i]!=""){
                    $scope.selectedDutyData.push({k:rowList[i].k,v:rowList[i].v});
                }
            }
        }
    };

    $scope.addData = function(row){
        $scope.selectedDutyData.push({k:row.k,v:row.v});
    };

});


