/**
 * 风控规则设置
 */
angular.module('inspinia').controller('riskRulesSetCtrl',function($scope, $http, $state, $stateParams, i18nService,$filter) {
	i18nService.setCurrentLang('zh-cn');
	$scope.info={};
	$scope.allState = false;
	$scope.allMerType= false;
	$scope.allScope = {};
    $scope.merTypeList=[];
    //$scope.checkStatus1 = false;
    //$scope.checkStatus2 = false;
	$scope.isCiTiaoCard=[{text:"是",value:"是"},{text:"否",value:"否"}];
	$scope.isXinPianCard=[{text:"是",value:"是"},{text:"否",value:"否"}];
//	$scope.nodes=[{text:"交易",value:1},{text:"审件",value:2},{text:"提现",value:3},{text:"实名认证",value:4}];
	
	
	 $scope.selectAll = function(m) {
		 $scope.allState = m.target.checked;
		 for(var i=0;i<$scope.allScope.length;i++){
	          $scope.allScope[i].state=false;
	     }
     };
        $scope.updateSelection = function ($event, id) {
            if ($event.target.checked) {
            	$scope.allState = false;
            }
        };
    $scope.selectMerTypeAll = function(m) {
        $scope.allMerType = m.target.checked;
        for(var i=0;i<$scope.merTypeList.length;i++){
            $scope.merTypeList[i].state=false;
        }
    };
    $scope.updateMerTypeSelection = function ($event, id) {
        if ($event.target.checked) {
            $scope.allMerType = false;
        }
    };

    $scope.getDatailInfo=function(){
        $http.post('riskRulesAction/selectByParam',
            "ids="+angular.toJson($stateParams.id),
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(!data.bols){
                    $scope.notice(data.msg);
                    return;
                }

                $scope.allScope = data.teaminfos;

                //响应成功
                $scope.merTypeList = [];
                for(var i=0; i<data.merTypeInfos.length; i++){
                    $scope.merTypeList.push({value:data.merTypeInfos[i].sysValue,text:data.merTypeInfos[i].sysName,state:false});
                }

                $scope.info=data.result;
                $scope.values=data.values;
                if($scope.info.rulesNo == 115){
                    var riskStatus = ($scope.values.riskStatus).split(",");
                    $scope.riskStatus1 = riskStatus[0];
                    $scope.riskStatus2 = riskStatus[1];
                    if($scope.riskStatus1==2){
                        $scope.checkStatus1 = true;
                    }
                    if($scope.riskStatus2==3){
                        $scope.checkStatus2 = true;
                    }
                }

                //回显范围

                if("0"==$scope.info.rulesTeamIds){
                    $scope.allState = true;
                }else{
                    for(var i=0;i<$scope.allScope.length;i++){
                        if($scope.info.rulesTeamIds!=null && $scope.info.rulesTeamIds!=""){
                            if($scope.info.rulesTeamIds.indexOf($scope.allScope[i].teamId)!=-1){
                                $scope.allScope[i].state=true;
                            }else{
                                $scope.allScope[i].state=false;
                            }
                        }
                    }}

                //回显商户类型

                if("0"==$scope.info.rulesMerchantType){
                    $scope.allMerType = true;
                }else{
                    for(var i=0;i<$scope.merTypeList.length;i++){
                        if($scope.info.rulesMerchantType!=null && $scope.info.rulesMerchantType!=""){
                            if($scope.info.rulesMerchantType.indexOf($scope.merTypeList[i].value)!=-1){
                                $scope.merTypeList[i].state=true;
                            }else{
                                $scope.merTypeList[i].state=false;
                            }
                        }
                    }}
            })
    };
    $scope.getDatailInfo();

    //屏蔽规则指令2、3、5
    $scope.rulesInstructionModals=angular.copy($scope.rulesInstructions);
    $scope.rulesInstructionModals.splice(5, 1);//屏蔽规则指令{text:"实名认证",value:5}
    $scope.rulesInstructionModals.splice(2, 2);//屏蔽规则指令{text:"身份证黑名单",value:2}、{text:"银行卡黑名单",value:3}
    $scope.rulesInstructionModals.splice(0, 1);//屏蔽规则指{text:"全部",value:-1}
	
	$scope.rulesInstructionSelcet=1;
    $scope.modifyRulesInstruction=function(rulesNo,rulesInstruction){
        $scope.rulesInstructionSelcet=rulesInstruction;
    	$scope.rulesInstruction=rulesInstruction;
    	$scope.rulesNo=rulesNo;
    	$('#modifyRulesInstructionModal').modal('show');
    }
    
    $scope.modifyRulesInstructionColse=function(){
    	$('#modifyRulesInstructionModal').modal('hide');
    }
    
    $scope.modifyRulesInstructionCommit=function(){
    	var data={"rulesNo":$scope.rulesNo,"rulesInstruction":$scope.rulesInstructionSelcet}
    	$http.post("riskRulesAction/modifyRulesInstruction",angular.toJson(data))
		.success(function(msg){
			if(msg.bols){
				$scope.notice(msg.msg);
                $scope.getDatailInfo();
				$('#modifyRulesInstructionModal').modal('hide');
			}else{
				$scope.notice(msg.msg);
				$('#modifyRulesInstructionModal').modal('hide');
			}
		}).error(function(){
		});
    }
	$scope.commit=function(){
		
		//$scope.submitting = true;
        if($stateParams.id==114) {
            $scope.info.rulesProvincesList = angular.toJson($scope.provinceGridApi.selection.getSelectedRows());
            $scope.info.rulesCityList = angular.toJson($scope.cityGridApi.selection.getSelectedRows());
        }
        if($stateParams.id==115){
            if($scope.riskStatus1 == 0 && $scope.riskStatus2 == 0){
                $scope.notice("商户状态至少选择一个");
                $scope.submitting = false;
                return;
            }
            if($scope.values.accBalanceLimit == "" || $scope.values.accBalanceLimit == null){
                $scope.notice("账户余额不能为空");
                $scope.submitting = false;
                return;
            }
            $scope.values.riskStatus = $scope.riskStatus1+","+$scope.riskStatus2;
        }
        
        if($scope.allState){
        	$scope.info.rulesTeamIds = "0";
        }else{
            var str = "";
        	for(var i=0;i<$scope.allScope.length;i++){
        		 if($scope.allScope[i].state){
        			 str += "," + $scope.allScope[i].teamId;
        		 }
        	 }
        	 if(str != ""){
                 str = str.substring(1);
                 $scope.info.rulesTeamIds = str;
             }else{
            	 $scope.info.rulesTeamIds = "";
             }
        }
        if($scope.allMerType){
            $scope.info.rulesMerchantType = "0";
        }else{
            var merstr = "";
            for(var i=0;i<$scope.merTypeList.length;i++){
                if($scope.merTypeList[i].state){
                    merstr += "," + $scope.merTypeList[i].value;
                }
            }
            if(merstr != ""){
                merstr = merstr.substring(1);
                $scope.info.rulesMerchantType = merstr;
            }else{
                $scope.info.rulesMerchantType = "";
            }
        }



        $scope.summitInfo=angular.copy($scope.info);
        $scope.summitInfo.rulesEngine=null;
		$http.post('riskRulesAction/updateInfo',
			"info="+angular.toJson($scope.summitInfo)+"&values="+angular.toJson($scope.values),
			 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
	    .success(function(data){
			if(!data.bols){
				$scope.notice(data.msg);
				$scope.submitting = false;
			}else{
				$scope.notice(data.msg);
				$state.transitionTo('risk.riskRulesMag',null,{reload:true});
				$scope.submitting = false;
			}
			
	    })
	    
	}

	if($stateParams.id==114){
        //获取所有省份
        $scope.getAreaList=function(name,type,callback){
            if(name == null || name=="undefine"){
                return;
            }
            $http.post('areaInfo/getAreaByName.do','name='+name+'&&type='+type,
                {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
            ).success(function(data){
                callback(data);
            }).error(function(){
            });
        }
        $scope.getAreaList(0, "p", function (data) {
            $scope.provinceGrid.data = data;
        });

        //商户省份表格
        $scope.provinceGrid = {
            columnDefs: [
                {field: 'name',displayName: '省份',width: 200},
            ],
            onRegisterApi : function(gridApi) {
                $scope.provinceGridApi = gridApi;
                $scope.provinceGridApi.selection.on.rowSelectionChanged($scope,function(row,event){
                    //行选中事件
                    $scope.findCity();
                });
                $scope.provinceGridApi.selection.on.rowSelectionChangedBatch($scope,function(row,event){
                    //全选事件
                    $scope.findCity();
                });
            },
            isRowSelectable: function(row){ // 选中行
                if($scope.info.provincesList){
                    for(var i=0;i<$scope.info.provincesList.length;i++){
                        if(row.entity.name==$scope.info.provincesList[i]){
                            row.grid.api.selection.selectRow(row.entity);
                            break;
                        }
                    }
                }
            }
        };

        $scope.findCity=function(){
            $http.post("areaInfo/getAreaCityByParentId",angular.toJson({provinceList:$scope.provinceGridApi.selection.getSelectedRows()})).success(function(data){
                $scope.cityGrid.data = data;
            });
        }

        //商户市表格
        $scope.cityGrid = {
            columnDefs: [
                {field: 'name',displayName:'市',width: 200},
            ],
            onRegisterApi : function(gridApi) {
                $scope.cityGridApi = gridApi;
            },
            isRowSelectable: function(row){ // 选中行
                if($scope.info.cityList){
                    for(var i=0;i<$scope.info.cityList.length;i++){
                        if(row.entity.name==$scope.info.cityList[i]){
                            row.grid.api.selection.selectRow(row.entity);
                            break;
                        }
                    }
                }
            }
        };
	}

	//改变复选框状态
    $scope.changeStatus = function (i) {
        if(i == 1){
            $scope.riskStatus1 = ($scope.riskStatus1 == 0 ? 2 : 0);
            $scope.checkStatus1 = ($scope.riskStatus1 == 2 ? true : false);
        }
        if(i == 2){
            $scope.riskStatus2 = ($scope.riskStatus2 == 0 ? 3 : 0);
            $scope.checkStatus2 = ($scope.riskStatus2 == 3 ? true : false);
        }
    }


})