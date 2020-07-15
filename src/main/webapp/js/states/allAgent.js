function initAllAgent(stateProvider){
        /************** 超级盟主 ***************/
        stateProvider
            .state('allAgent', {
                abstract: true,
                url: "/allAgent",
                templateUrl: "views/common/content.html",
            })
            .state('allAgent.awardParam', {
                url: "/awardParam",
                templateUrl: 'views/allAgent/oem/awardParamQuery.html',
                data: {pageTitle: '奖项参数设置'},
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('localytics.directives');
                        $ocLazyLoad.load('oitozero.ngSweetAlert');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/controllers/allAgent/oem/awardParamQueryCtrl.js?ver='+verNo]
                        });
                    }]
                }
            })
            .state('allAgent.awardParamAdd', {
                url: "/awardParamAdd",
                templateUrl: 'views/allAgent/oem/awardParamAdd.html',
                data: {pageTitle: '奖项参数设置新增'},
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('localytics.directives');
                        $ocLazyLoad.load('oitozero.ngSweetAlert');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/controllers/allAgent/oem/awardParamAddCtrl.js?ver='+verNo]
                        });
                    }]
                }
            })
            .state('allAgent.awardParamEdit', {
                url: "/awardParamEdit/:id",
                templateUrl: 'views/allAgent/oem/awardParamEdit.html',
                data: {pageTitle: '奖项参数设置修改'},
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('localytics.directives');
                        $ocLazyLoad.load('oitozero.ngSweetAlert');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/controllers/allAgent/oem/awardParamEditCtrl.js?ver='+verNo]
                        });
                    }]
                }
            })
            .state('allAgent.awardParamDetail', {
                url: "/awardParamDetail/:id",
                templateUrl: 'views/allAgent/oem/awardParamDetail.html',
                data: {pageTitle: '奖项参数设置详情'},
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('localytics.directives');
                        $ocLazyLoad.load('oitozero.ngSweetAlert');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/controllers/allAgent/oem/awardParamDetailCtrl.js?ver='+verNo]
                        });
                    }]
                }
            })
            .state('allAgent.awardParamOemEdit', {
                url: "/awardParamOemEdit/:id",
                templateUrl: 'views/allAgent/oem/awardParamOemEdit.html',
                data: {pageTitle: 'oem配置'},
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('fileUpload');
                        $ocLazyLoad.load('localytics.directives');
                        $ocLazyLoad.load('summernote');
                        $ocLazyLoad.load('angular-summernote');
                        $ocLazyLoad.load('My97DatePicker');
                        $ocLazyLoad.load('fancybox');
                        $ocLazyLoad.load('oitozero.ngSweetAlert');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/plugins/summernote/summernote.min.js','js/plugins/summernote/angular-summernote.min.js', 'css/plugins/summernote/summernote.css',
                                'js/controllers/allAgent/oem/awardParamOemEditCtrl.js?ver='+verNo]
                        });
                    }]
                }
            })
            .state('allAgent.allyManage', {
                url: "/allyManage",
                templateUrl: 'views/allAgent/user/userAllAgentQuery.html',
                data: {pageTitle: '盟主管理'},
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('localytics.directives');
                        $ocLazyLoad.load('oitozero.ngSweetAlert');
                        $ocLazyLoad.load('My97DatePicker');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/controllers/allAgent/user/userAllAgentQueryCtrl.js?ver='+verNo]
                        });
                    }]
                }
            })
            .state('allAgent.allyManageDetail', {
                url: "/allyManageDetail/:id",
                templateUrl: 'views/allAgent/user/userAllAgentDetail.html',
                data: {pageTitle: '交易分润比例调整记录'},
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('localytics.directives');
                        $ocLazyLoad.load('oitozero.ngSweetAlert');
                        $ocLazyLoad.load('My97DatePicker');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/controllers/allAgent/user/userAllAgentDetailCtrl.js?ver='+verNo]
                        });
                    }]
                }
            })
            .state('allAgent.userAllAgentDivided', {
                url: "/userAllAgentDivided/:userCode",
                templateUrl: 'views/allAgent/user/userAllAgentDivided.html',
                data: {pageTitle: '盟主详情'},
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('localytics.directives');
                        $ocLazyLoad.load('oitozero.ngSweetAlert');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/controllers/allAgent/user/userAllAgentDividedCtrl.js?ver='+verNo]
                        });
                    }]
                }
            })
            .state('allAgent.userAllAgentEdit', {
                url: "/userAllAgentEdit/:id",
                templateUrl: 'views/allAgent/user/userAllAgentEdit.html',
                data: {pageTitle: '盟主编辑'},
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('localytics.directives');
                        $ocLazyLoad.load('oitozero.ngSweetAlert');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/controllers/allAgent/user/userAllAgentEditCtrl.js?ver='+verNo]
                        });
                    }]
                }
            })
            .state('allAgent.merchantAllAgent', {
                url: "/merchantAllAgent",
                templateUrl: 'views/allAgent/merchant/merchantAllAgentQuery.html',
                data: {pageTitle: '盟主管理'},
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('localytics.directives');
                        $ocLazyLoad.load('oitozero.ngSweetAlert');
                        $ocLazyLoad.load('My97DatePicker');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/controllers/allAgent/merchant/merchantAllAgentQueryCtrl.js?ver='+verNo]
                        });
                    }]
                }
            })
            .state('allAgent.goodManage', {
                url: "/goodManage",
                templateUrl: 'views/allAgent/good/goodAllAgentQuery.html',
                data: {pageTitle: '商品管理'},
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('localytics.directives');
                        $ocLazyLoad.load('oitozero.ngSweetAlert');
                        $ocLazyLoad.load('My97DatePicker');
                        $ocLazyLoad.load('fancybox');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/controllers/allAgent/good/goodAllAgentQueryCtrl.js?ver='+verNo]
                        });
                    }]
                }
            })
            .state('allAgent.goodGroupQuery', {
                url: "/goodGroupQuery",
                templateUrl: 'views/allAgent/good/goodGroupQuery.html',
                data: {pageTitle: '商品分类管理'},
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('localytics.directives');
                        $ocLazyLoad.load('oitozero.ngSweetAlert');
                        $ocLazyLoad.load('My97DatePicker');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/controllers/allAgent/good/goodGroupQueryCtrl.js?ver='+verNo]
                        });
                    }]
                }
            })
            .state('allAgent.firewall', {
                url: "/firewall",
                templateUrl: 'views/allAgent/firewall/firewallQuery.html',
                data: {pageTitle: '白名单管理'},
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('localytics.directives');
                        $ocLazyLoad.load('oitozero.ngSweetAlert');
                        $ocLazyLoad.load('My97DatePicker');
                        $ocLazyLoad.load('ui-switch');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/controllers/allAgent/firewall/firewallCtrl.js?ver='+verNo]
                        });
                    }]
                }
            })
            .state('allAgent.persistNickName', {
                url: "/persistNickName",
                templateUrl: 'views/allAgent/persistNickName/persistNickNameQuery.html',
                data: {pageTitle: '保留昵称列表'},
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('localytics.directives');
                        $ocLazyLoad.load('oitozero.ngSweetAlert');
                        $ocLazyLoad.load('My97DatePicker');
                        $ocLazyLoad.load('fileUpload');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files:['js/controllers/allAgent/persistNickName/persistNickNameCtrl.js?ver='+verNo]
                        });
                    }]
                }
            })
            .state('allAgent.goodAdd', {
                url: "/goodAdd",
                templateUrl: 'views/allAgent/good/goodAllAgentAdd.html',
                data: {pageTitle: '商品新增'},
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('summernote');
                        $ocLazyLoad.load('angular-summernote');
                        $ocLazyLoad.load('localytics.directives');
                        $ocLazyLoad.load('oitozero.ngSweetAlert');
                        $ocLazyLoad.load('fileUpload');
                        $ocLazyLoad.load('fancybox');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/plugins/summernote/summernote.min.js','js/plugins/summernote/angular-summernote.min.js', 'css/plugins/summernote/summernote.css',
                                'js/controllers/allAgent/good/goodAllAgentAddCtrl.js?ver='+verNo]
                        });
                    }]
                }
            })
            .state('allAgent.goodEdit', {
                url: "/goodEdit/:id",
                templateUrl: 'views/allAgent/good/goodAllAgentEdit.html',
                data: {pageTitle: '商品修改'},
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('summernote');
                        $ocLazyLoad.load('angular-summernote');
                        $ocLazyLoad.load('localytics.directives');
                        $ocLazyLoad.load('oitozero.ngSweetAlert');
                        $ocLazyLoad.load('fileUpload');
                        $ocLazyLoad.load('fancybox');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/plugins/summernote/summernote.min.js','js/plugins/summernote/angular-summernote.min.js', 'css/plugins/summernote/summernote.css',
                                'js/controllers/allAgent/good/goodAllAgentEditCtrl.js?ver='+verNo]
                        });
                    }]
                }
            })
            .state('allAgent.goodDetail', {
                url: "/goodAdd/:id",
                templateUrl: 'views/allAgent/good/goodAllAgentDetail.html',
                data: {pageTitle: '商品详情'},
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('summernote');
                        $ocLazyLoad.load('angular-summernote');
                        $ocLazyLoad.load('localytics.directives');
                        $ocLazyLoad.load('oitozero.ngSweetAlert');
                        $ocLazyLoad.load('fileUpload');
                        $ocLazyLoad.load('fancybox');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/plugins/summernote/summernote.min.js','js/plugins/summernote/angular-summernote.min.js', 'css/plugins/summernote/summernote.css',
                                'js/controllers/allAgent/good/goodAllAgentDetailCtrl.js?ver='+verNo]
                        });
                    }]
                }
            })
            .state('allAgent.banner', {
                url: "/banner",
                templateUrl: 'views/allAgent/banner/bannerQuery.html',
                data: {pageTitle: 'Banner管理'},
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('localytics.directives');
                        $ocLazyLoad.load('oitozero.ngSweetAlert');
                        $ocLazyLoad.load('My97DatePicker');
                        $ocLazyLoad.load('ui-switch');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/controllers/allAgent/banner/bannerQueryCtrl.js?ver='+verNo]
                        });
                    }]
                }
            })
            .state('allAgent.bannerAdd', {
                url: "/bannerAdd",
                templateUrl: 'views/allAgent/banner/bannerAdd.html',
                data: {pageTitle: 'Banner新增'},
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('localytics.directives');
                        $ocLazyLoad.load('oitozero.ngSweetAlert');
                        $ocLazyLoad.load('fileUpload');
                        $ocLazyLoad.load('My97DatePicker');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/controllers/allAgent/banner/bannerAddCtrl.js?ver='+verNo]
                        });
                    }]
                }
            })
            .state('allAgent.bannerEdit', {
                url: "/bannerEdit/:id",
                templateUrl: 'views/allAgent/banner/bannerEdit.html',
                data: {pageTitle: 'Banner编辑'},
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('localytics.directives');
                        $ocLazyLoad.load('oitozero.ngSweetAlert');
                        $ocLazyLoad.load('fileUpload');
                        $ocLazyLoad.load('My97DatePicker');
                        $ocLazyLoad.load('fancybox');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/controllers/allAgent/banner/bannerEditCtrl.js?ver='+verNo]
                        });
                    }]
                }
            })
            .state('allAgent.bannerDetail', {
                url: "/bannerDetail/:id",
                templateUrl: 'views/allAgent/banner/bannerDetail.html',
                data: {pageTitle: 'Banner详情'},
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('localytics.directives');
                        $ocLazyLoad.load('oitozero.ngSweetAlert');
                        $ocLazyLoad.load('fileUpload');
                        $ocLazyLoad.load('fancybox');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/controllers/allAgent/banner/bannerEditCtrl.js?ver='+verNo]
                        });
                    }]
                }
            })
            .state('allAgent.notice', {
                url: "/notice",
                templateUrl: 'views/allAgent/notice/noticeQuery.html',
                data: {pageTitle: '公告管理'},
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('localytics.directives');
                        $ocLazyLoad.load('oitozero.ngSweetAlert');
                        $ocLazyLoad.load('My97DatePicker');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/controllers/allAgent/notice/noticeQueryCtrl.js?ver='+verNo]
                        });
                    }]
                }
            })
            .state('allAgent.noticeAdd', {
                url: "/noticeAdd",
                templateUrl: 'views/allAgent/notice/noticeAdd.html',
                data: {pageTitle: '公告新增'},
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('summernote');
                        $ocLazyLoad.load('angular-summernote');
                        $ocLazyLoad.load('localytics.directives');
                        $ocLazyLoad.load('oitozero.ngSweetAlert');
                        $ocLazyLoad.load('My97DatePicker');
                        $ocLazyLoad.load('fileUpload');
                        $ocLazyLoad.load('fancybox');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/plugins/summernote/summernote.min.js','js/plugins/summernote/angular-summernote.min.js', 'css/plugins/summernote/summernote.css',
                                'js/controllers/allAgent/notice/noticeAddCtrl.js?ver='+verNo]
                        });
                    }]
                }
            })
            .state('allAgent.noticeEdit', {
                url: "/noticeEdit/:id",
                templateUrl: 'views/allAgent/notice/noticeEdit.html',
                data: {pageTitle: '公告编辑'},
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('summernote');
                        $ocLazyLoad.load('angular-summernote');
                        $ocLazyLoad.load('localytics.directives');
                        $ocLazyLoad.load('oitozero.ngSweetAlert');
                        $ocLazyLoad.load('My97DatePicker');
                        $ocLazyLoad.load('fileUpload');
                        $ocLazyLoad.load('fancybox');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/plugins/summernote/summernote.min.js','js/plugins/summernote/angular-summernote.min.js', 'css/plugins/summernote/summernote.css',
                                'js/controllers/allAgent/notice/noticeEditCtrl.js?ver='+verNo]
                        });
                    }]
                }
            })
            .state('allAgent.noticeDetail', {
                url: "/noticeDetail/:id",
                templateUrl: 'views/allAgent/notice/noticeDetail.html',
                data: {pageTitle: '公告详情'},
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('summernote');
                        $ocLazyLoad.load('angular-summernote');
                        $ocLazyLoad.load('localytics.directives');
                        $ocLazyLoad.load('oitozero.ngSweetAlert');
                        $ocLazyLoad.load('My97DatePicker');
                        $ocLazyLoad.load('fileUpload');
                        $ocLazyLoad.load('fancybox');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/plugins/summernote/summernote.min.js','js/plugins/summernote/angular-summernote.min.js', 'css/plugins/summernote/summernote.css',
                                'js/controllers/allAgent/notice/noticeEditCtrl.js?ver='+verNo]
                        });
                    }]
                }
            })
            .state('allAgent.machineBuyOrder', {
                url: "/machineBuyOrder",
                templateUrl: 'views/allAgent/machineBuy/machineBuyOrderQuery.html',
                data: {pageTitle: '机具物料申购订单'},
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('localytics.directives');
                        $ocLazyLoad.load('oitozero.ngSweetAlert');
                        $ocLazyLoad.load('infinity-chosen');
                        $ocLazyLoad.load('My97DatePicker');
                        $ocLazyLoad.load('fileUpload');
                        $ocLazyLoad.load('fancybox');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/controllers/allAgent/machineBuy/machineBuyOrderQueryCtrl.js?ver='+verNo]
                        });
                    }]
                }
            })
            .state('allAgent.machineBuyOrderShipMachine', {
                url: "/machineBuyOrderShipMachine/:orderNo/:num/:goodCode",
                templateUrl: 'views/allAgent/machineBuy/machineBuyOrderShipMachine.html',
                data: {pageTitle: '发货机具'},
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('localytics.directives');
                        $ocLazyLoad.load('oitozero.ngSweetAlert');
                        $ocLazyLoad.load('infinity-chosen');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/controllers/allAgent/machineBuy/machineBuyOrderShipMachineCtrl.js?ver='+verNo]
                        });
                    }]
                }
            })
            .state('allAgent.machineBuyOrderShipMachineDetail', {
                url: "/machineBuyOrderShipMachineDetail/:orderNo/:goodCode",
                templateUrl: 'views/allAgent/machineBuy/machineBuyOrderShipMachineDetail.html',
                data: {pageTitle: '发货信息'},
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('localytics.directives');
                        $ocLazyLoad.load('oitozero.ngSweetAlert');
                        $ocLazyLoad.load('infinity-chosen');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/controllers/allAgent/machineBuy/machineBuyOrderShipMachineDetailCtrl.js?ver='+verNo]
                        });
                    }]
                }
            })
            .state('allAgent.afterSaleOrder', {
                url: "/afterSaleOrder",
                templateUrl: 'views/allAgent/afterSale/afterSaleOrderQuery.html',
                data: {pageTitle: '申购售后订单'},
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('localytics.directives');
                        $ocLazyLoad.load('oitozero.ngSweetAlert');
                        $ocLazyLoad.load('infinity-chosen');
                        $ocLazyLoad.load('My97DatePicker');
                        $ocLazyLoad.load('fileUpload');
                        $ocLazyLoad.load('fancybox');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/controllers/allAgent/afterSale/afterSaleOrderQueryCtrl.js?ver='+verNo]
                        });
                    }]
                }
            })
            .state('allAgent.terminalBack', {
                url: "/terminalBack",
                templateUrl: 'views/allAgent/terminalBack/terminalBackQuery.html',
                data: {pageTitle: 'SN号回拨记录'},
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('localytics.directives');
                        $ocLazyLoad.load('oitozero.ngSweetAlert');
                        $ocLazyLoad.load('infinity-chosen');
                        $ocLazyLoad.load('My97DatePicker');
                        $ocLazyLoad.load('fileUpload');
                        $ocLazyLoad.load('fancybox');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/controllers/allAgent/terminalBack/terminalBackQueryCtrl.js?ver='+verNo]
                        });
                    }]
                }
            })
            .state('allAgent.shareDetail', {
                url: "/shareDetail",
                templateUrl: 'views/allAgent/share/shareDetailQuery.html',
                data: {pageTitle: '盟主分润明细'},
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('localytics.directives');
                        $ocLazyLoad.load('oitozero.ngSweetAlert');
                        $ocLazyLoad.load('infinity-chosen');
                        $ocLazyLoad.load('My97DatePicker');
                        $ocLazyLoad.load('fileUpload');
                        $ocLazyLoad.load('fancybox');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/controllers/allAgent/share/shareDetailQueryCtrl.js?ver='+verNo]
                        });
                    }]
                }
            })
            .state('activity.allAgentCashBackDetail', {
                url: "/allAgentCashBackDetail",
                templateUrl: 'views/allAgent/cash/cashBackDetail.html',
                data: {pageTitle: '盟主活动返现明细'},
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('infinity-chosen');
                        $ocLazyLoad.load('oitozero.ngSweetAlert');
                        $ocLazyLoad.load('fileUpload');
                        $ocLazyLoad.load('My97DatePicker');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/controllers/allAgent/cash/cashBackDetailCtrl.js?ver='+verNo]
                        });
                    }]
                }
            })
            .state('allAgent.rankConfigAdd', {
                url: "/rankConfigAdd",
                templateUrl: 'views/allAgent/rankConfig/rankConfigAdd.html',
                data: {pageTitle: '排行榜配置'},
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('summernote');
                        $ocLazyLoad.load('angular-summernote');
                        $ocLazyLoad.load('localytics.directives');
                        $ocLazyLoad.load('oitozero.ngSweetAlert');
                        $ocLazyLoad.load('My97DatePicker');
                        $ocLazyLoad.load('fileUpload');
                        $ocLazyLoad.load('fancybox');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/plugins/summernote/summernote.min.js','js/plugins/summernote/angular-summernote.min.js', 'css/plugins/summernote/summernote.css',
                                'js/controllers/allAgent/rankConfig/rankConfigAddCtrl.js?ver='+verNo]
                        });
                    }]
                }
            })
            .state('allAgent.rankList', {
                url: "/rankList",
                templateUrl: 'views/allAgent/rankList/rankListQuery.html',
                data: {pageTitle: '排行榜奖金发放明细'},
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('localytics.directives');
                        $ocLazyLoad.load('oitozero.ngSweetAlert');
                        $ocLazyLoad.load('My97DatePicker');
                        $ocLazyLoad.load('infinity-chosen');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/controllers/allAgent/rankList/rankListQueryCtrl.js?ver='+verNo]
                        });
                    }]
                }
            })
        
}