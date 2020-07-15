function config($stateProvider, $urlRouterProvider, $ocLazyLoadProvider, IdleProvider, KeepaliveProvider, $httpProvider) {
    IdleProvider.idle(5); // in seconds
    IdleProvider.timeout(120); // in seconds

    $urlRouterProvider.otherwise("/welcome/index");
    var csrfHeadName = $("meta[name=_csrf_header]").attr('content');
    var csrfData = $("meta[name=_csrf]").attr('content');
    $httpProvider.defaults.headers.common[csrfHeadName]=csrfData;
    $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';

    $httpProvider.interceptors.push(function($q,$rootScope){
        return {
            responseError : function(rejection) {
                if (rejection.status === 401) {
                    location.reload();
                }  else if(rejection.status === 403){
                    alert('没有足够的权限');
                    history.go(-1)
                } else if(rejection.status === 449){
                    location.reload();
                }
                return $q.reject(rejection);
            }
        };
    });

    $ocLazyLoadProvider.config({
        debug: false,
        modules: [
            {
                name: 'ngGrid',
                files: ['js/plugins/nggrid/ng-grid.css', 'js/plugins/nggrid/ng-grid-2.0.3.min.js'],
                cache: true
            }, {
                name: 'datePicker',
                files: ['css/plugins/datapicker/angular-datapicker.css',
                    'js/plugins/datapicker/angular-datepicker.js'],
                cache: true
            }, {
                name: 'ui.select',
                files: ['js/plugins/ui-select/select.min.js', 'css/plugins/ui-select/select.min.css'],
                cache: true
            }, {
                name: 'colorpicker.module',
                files: ['css/plugins/clockpicker/clockpicker.css', 'js/plugins/clockpicker/clockpicker.js',
                    'css/plugins/colorpicker/colorpicker.css', 'js/plugins/colorpicker/bootstrap-colorpicker-module.js'],
                cache: true
            }, {
                name: 'infinity-chosen',
                files: ['css/plugins/chosen/chosen.css', 'js/plugins/chosen/chosen.jquery.js', 'js/plugins/chosen/infinity-angular-chosen.js'],
                cache: true
            }, {
                name: 'localytics.directives',
                files: ['css/plugins/chosen/chosen.css', 'js/plugins/chosen/chosen.jquery.js', 'js/plugins/chosen/chosen.js'],
                cache: true
            }, {
                name: 'oitozero.ngSweetAlert',
                files: ['js/plugins/sweetalert/sweetalert.min.js', 'css/plugins/sweetalert/sweetalert.css'
                    , 'js/plugins/sweetalert/angular-sweetalert.min.js']
            },
            {
                name: 'fileUpload',
                files: ['js/angular/angular-file-upload.min.js']
            },{
                name: 'angular-summernote',
                files: ['js/plugins/summernote/angular-summernote.min.js', 'css/plugins/summernote/summernote.css']
            },{
                name: 'summernote',
                files: ['js/plugins/summernote/summernote.min.js']
            },{
                name: 'ui-switch',
                files: ['css/plugins/angular-ui-switch/angular-ui-switch.css', 'js/plugins/angular-ui-switch/angular-ui-switch.js']
            },{
                name: 'ngJsTree',
                files: ['js/plugins/jsTree/jstree.min.js', 'js/plugins/jsTree/ngJsTree.min.js', 'css/plugins/jsTree/style.min.css'],
                cache: true
            },{
                name: 'fancybox',
                files: ['css/plugins/fancybox/jquery.fancybox.css', 'js/plugins/fancybox/jquery.mousewheel.pack.js','js/plugins/fancybox/jquery.fancybox.pack.js']
            }, {//新的时间控件
                name: 'My97DatePicker',
                files: ['js/plugins/My97DatePicker/WdatePicker.js']
            }
        ]
    });
    /*** 路由初始化 ***/
    initStates($stateProvider);
}


angular.module('inspinia')
    .config(config)
    .run(function ($rootScope, $state, $stateParams) {
        $rootScope.$state = $state;
        $rootScope.$stateParams = $stateParams;
        $rootScope.$on("$stateChangeSuccess",  function(event, toState, toParams, fromState, fromParams) {
            // to be used for back button //won't work when page is reloaded.
            $rootScope.previousState_name = fromState.name;
            $rootScope.previousState_params = fromParams;
        });
        //back button function called from back button's ng-click="back()"
        $rootScope.back = function() {//实现返回的函数
            $state.go($rootScope.previousState_name,$rootScope.previousState_params);
        };
    });
