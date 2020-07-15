/**
 * 公告详情
 */
angular.module('inspinia').controller('noticeDetailCtrl',function($scope,$http,$state,$stateParams){

    //超级盟主品牌列表
    $scope.oemListes=[];
    $http.post("awardParam/getOemList",
        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function(data){
            if(data.status){
                $scope.oemListes.push({value:"",text:"全部"});
                var list=data.list;
                if(list!=null&&list.length>0){
                    for(var i=0; i<list.length; i++){
                        $scope.oemListes.push({value:list[i].brandCode,text:list[i].brandName});
                    }
                }
            }
        });

    //获取所有的银行家组织
    $scope.orgInfoList = [{orgId:-1,orgName:"全部"}];
    $scope.getOrgInfoList = function () {
        $http({
            url:"superBank/getOrgInfoList",
            method:"POST"
        }).success(function(msg){
            if(msg.status){
                $scope.orgInfoList = msg.data;
                angular.forEach($scope.orgInfoList, function(item){
                    item.checkStatus = 0;
                });
            }
            $scope.getBaseInfo();
        }).error(function(){
            $scope.notice("获取组织信息异常");
        })
    };
    $scope.getOrgInfoList();

    $scope.getBaseInfo = function(){
        $http({
            url:"superBank/noticeDetail/" + $stateParams.id,
            method:"GET"
        }).success(function(result){
            $scope.baseInfo = result.data;
            if($scope.baseInfo.orgId != null){
                if($scope.baseInfo.orgId == -1){
                    $scope.orgAll = 1;
                } else {
                    angular.forEach($scope.orgInfoList, function(item){
                        if($scope.baseInfo.orgId.indexOf(item.orgId)>-1){
                            item.checkStatus = 1;
                        }
                    });
                }
            }
        });
    }

    $scope.oemListResult=false;
    $scope.oemTypeCheck= function(){
        $scope.oemListResult=false;
        $("input:checkbox[name='oemtypecheck']:checked").each(function() { // 遍历多选框
            if($(this).val()==11){
                $scope.oemListResult=true;
            }
        });
    }
    $scope.oemTypeCheck();
});