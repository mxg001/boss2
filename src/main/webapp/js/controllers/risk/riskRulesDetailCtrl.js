/**
 * 风控规则详情
 */
angular.module('inspinia').controller('riskRulesDetailCtrl',function($scope, $http, $state, $stateParams, i18nService,$filter) {
	i18nService.setCurrentLang('zh-cn');
	
	$scope.info={};
	$scope.allScope = {};
    $scope.merTypeList=[];
//	$scope.warns=[{text:"手机",value:1},{text:"短信",value:2},{text:"邮件",value:3}];
//	$scope.nodes=[{text:"交易",value:1},{text:"审件",value:2},{text:"提现",value:3},{text:"实名认证",value:4}];
    $scope.pcType=0;
	
	//查询
	$http.post('riskRulesAction/selectRiskRulesDetailById',
			"ids="+angular.toJson($stateParams.id),
			 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
	.success(function(data){
		if(!data.bols){
			$scope.notice("查询出错");
			return;
		}
		
		$scope.allScope = data.teaminfos;
		
		$scope.info=data.result;
		
		if($scope.info.rulesTeamIds!=null && $scope.info.rulesTeamIds!=""){
			if("0"==$scope.info.rulesTeamIds){
				$scope.info.rulesTeamIds="全部";
			}else{
				
				 var text = "";
				 for(var i=0;i<$scope.allScope.length;i++){
					  if($scope.info.rulesTeamIds.indexOf($scope.allScope[i].teamId)!=-1){
						  text+= ","+$scope.allScope[i].teamName;
					  };
			     }
				 if(text != ""){
					 text = text.substring(1);
	                 $scope.info.rulesTeamIds = text;
	             }
			}
		}


        if($scope.info.rulesMerchantType!=null && $scope.info.rulesMerchantType!=""){

            for(var i=0; i<data.merTypeInfos.length; i++){
                $scope.merTypeList.push({value:data.merTypeInfos[i].sysValue,text:data.merTypeInfos[i].sysName,state:false});
            }

            if("0"==$scope.info.rulesMerchantType){
                $scope.info.rulesMerchantType="全部";
            }else{

                var mertext = "";
                for(var i=0;i<$scope.merTypeList.length;i++){
                    if($scope.info.rulesMerchantType.indexOf($scope.merTypeList[i].value)!=-1){
                        mertext+= ","+$scope.merTypeList[i].text;
                    };
                }
                if(mertext != ""){
                    mertext = mertext.substring(1);
                    $scope.info.rulesMerchantType = mertext;
                }
            }
        }
		
	})

    if($stateParams.id==114){
        $scope.pcType=1;
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
    }else{
        $scope.pcType=0;
	}
})