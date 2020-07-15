/**
 * 首页详情
 */
angular.module('inspinia',[]).controller("indexCtrl", function($scope, $http, $location, $stateParams,$window) {



}).filter('trustHtml', function ($sce) {
  return function (input) {
    return $sce.trustAsHtml(input);
  }
});