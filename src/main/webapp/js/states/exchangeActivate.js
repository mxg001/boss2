function initExchangeActivate(stateProvider){
    	/************** 超级兑(激活码版) ***************/
        stateProvider.state('exchangeActivate', {
            abstract: true,
            url: "/exchangeActivate",
            templateUrl: "views/common/content.html",
        })
        .state('exchangeActivate.activationCode', {
            url: "/activationCode",
            templateUrl: "views/exchangeActivate/activationCode/activationCode.html",
            data: {pageTitle: '激活码管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('infinity-chosen');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchangeActivate/activationCode/activationCodeCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchangeActivate.userManagement', {
            url: "/userManagement",
            templateUrl: 'views/exchangeActivate/user/userManagementQuery.html',
            data: {pageTitle: '超级兑用户管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('infinity-chosen');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchangeActivate/user/userManagementQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchangeActivate.userManagementDetail', {
            url: "/userManagementDetail/:merchantNo",
            templateUrl: "views/exchangeActivate/user/userManagementDetail.html",
            data: {pageTitle: '超级兑用户详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('fancybox');
                    $ocLazyLoad.load('localytics.directives');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchangeActivate/user/exchangeActivateUserDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchangeActivate.userManagementEdit', {
            url: "/userManagementEdit/:merchantNo",
            templateUrl: "views/exchangeActivate/user/userManagementEdit.html",
            data: {pageTitle: '超级兑用户修改'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('fancybox');
                    $ocLazyLoad.load('localytics.directives');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchangeActivate/user/userManagementEditCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchangeActivate.exchangeOem', {
            url: "/exchangeOem",
            templateUrl: 'views/exchangeActivate/oem/exchangeOemQuery.html',
            data: {pageTitle: '积分兑现组织管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchangeActivate/oem/exchangeOemQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchangeActivate.exchangeOemEdit', {
            url: "/exchangeOemEdit/:id",
            templateUrl: 'views/exchangeActivate/oem/exchangeOemEdit.html',
            data: {pageTitle: '积分兑现组织修改'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchangeActivate/oem/exchangeOemEditCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchangeActivate.exchangeOemDetail', {
            url: "/exchangeOemDetail/:id",
            templateUrl: 'views/exchangeActivate/oem/exchangeOemDetail.html',
            data: {pageTitle: '积分兑现组织详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchangeActivate/oem/exchangeOemEditCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchangeActivate.exchangeOemAdd', {
            url: "/exchangeOemAdd",
            templateUrl: 'views/exchangeActivate/oem/exchangeOemAdd.html',
            data: {pageTitle: '积分兑现组织新增'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('fileUpload');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchangeActivate/oem/exchangeOemAddCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchangeActivate.productOem', {
            url: "/productOem/:oemNo",
            templateUrl: 'views/exchangeActivate/oem/productOemQuery.html',
            data: {pageTitle:'OEM产品管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('infinity-chosen');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchangeActivate/oem/productOemQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchangeActivate.productOemEdit', {
            url: "/productOemEdit/:id/:oemNo",
            templateUrl: 'views/exchangeActivate/oem/productOemEdit.html',
            data: {pageTitle: 'OEM产品修改'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('infinity-chosen');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchangeActivate/oem/productOemEditCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchangeActivate.productOemDetail', {
            url: "/productOemDetail/:id/:oemNo",
            templateUrl: 'views/exchangeActivate/oem/productOemDetail.html',
            data: {pageTitle: 'OEM产品详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('infinity-chosen');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchangeActivate/oem/productOemEditCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchangeActivate.productOemAdd', {
            url: "/productOemAdd/:oemNo",
            templateUrl: 'views/exchangeActivate/oem/productOemAdd.html',
            data: {pageTitle:'OEM产品新增'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('infinity-chosen');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchangeActivate/oem/productOemAddCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchangeActivate.orgManagement', {
            url: "/orgManagement",
            templateUrl: 'views/exchangeActivate/orgManagement/orgManagementQuery.html',
            data: {pageTitle: '积分机构管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchangeActivate/orgManagement/orgManagementQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchangeActivate.orgManagementEdit', {
            url: "/orgManagementEdit/:id",
            templateUrl: 'views/exchangeActivate/orgManagement/orgManagementEdit.html',
            data: {pageTitle: '积分机构修改'},
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
                        files: ['js/controllers/exchangeActivate/orgManagement/orgManagementEditCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchangeActivate.orgManagementDetail', {
            url: "/orgManagementDetail/:id",
            templateUrl: 'views/exchangeActivate/orgManagement/orgManagementDetail.html',
            data: {pageTitle: '积分机构详情'},
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
                        files: ['js/controllers/exchangeActivate/orgManagement/orgManagementEditCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchangeActivate.productType', {
            url: "/productType",
            templateUrl: 'views/exchangeActivate/productType/productTypeQuery.html',
            data: {pageTitle: '积分产品类别管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchangeActivate/productType/productTypeQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchangeActivate.productTypeAdd', {
            url: "/productTypeAdd",
            templateUrl: 'views/exchangeActivate/productType/productTypeAdd.html',
            data: {pageTitle: '积分产品类别新增'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('summernote');
                    $ocLazyLoad.load('angular-summernote');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('fileUpload');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchangeActivate/productType/productTypeAddCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchangeActivate.productTypeEdit', {
            url: "/productTypeEdit/:id",
            templateUrl: 'views/exchangeActivate/productType/productTypeEdit.html',
            data: {pageTitle: '积分产品类别编辑'},
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
                        files: ['js/controllers/exchangeActivate/productType/productTypeEditCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchangeActivate.productTypeDetail', {
            url: "/productTypeDetail/:id",
            templateUrl: 'views/exchangeActivate/productType/productTypeDetail.html',
            data: {pageTitle: '积分产品类别详情'},
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
                        files: ['js/controllers/exchangeActivate/productType/productTypeEditCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchangeActivate.product', {
            url: "/product",
            templateUrl: 'views/exchangeActivate/product/productQuery.html',
            data: {pageTitle: '积分产品管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchangeActivate/product/productQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchangeActivate.productEdit', {
            url: "/productEdit/:id",
            templateUrl: 'views/exchangeActivate/product/productEdit.html',
            data: {pageTitle: '积分产品修改'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchangeActivate/product/productEditCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchangeActivate.productDetail', {
            url: "/productDetail/:id",
            templateUrl: 'views/exchangeActivate/product/productDetail.html',
            data: {pageTitle: '积分产品详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchangeActivate/product/productEditCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchangeActivate.exchangeOrder', {
            url: "/exchangeOrder",
            templateUrl: 'views/exchangeActivate/exchangeOrder/exchangeOrderQuery.html',
            data: {pageTitle: '积分兑换订单查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchangeActivate/exchangeOrder/exchangeOrderQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchangeActivate.exchangeOrderDetail', {
            url: "/exchangeOrderDetail/:id",
            templateUrl: 'views/exchangeActivate/exchangeOrder/exchangeOrderDetail.html',
            data: {pageTitle: '积分兑换订单详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchangeActivate/exchangeOrder/exchangeOrderDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchangeActivate.exchangeAudit', {
            url: "/exchangeAudit",
            templateUrl: 'views/exchangeActivate/exchangeOrder/exchangeAuditQuery.html',
            data: {pageTitle: '积分兑换核销管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchangeActivate/exchangeOrder/exchangeAuditQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchangeActivate.exchangeAuditDetail', {
            url: "/exchangeAuditDetail/:id",
            templateUrl: 'views/exchangeActivate/exchangeOrder/exchangeAuditDetail.html',
            data: {pageTitle: '积分兑换核销详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchangeActivate/exchangeOrder/exchangeAuditDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchangeActivate.exchangeAuditEdit', {
            url: "/exchangeAuditEdit/:id",
            templateUrl: 'views/exchangeActivate/exchangeOrder/exchangeAuditEdit.html',
            data: {pageTitle: '积分兑换核销'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchangeActivate/exchangeOrder/exchangeAuditDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchangeActivate.exchangeAuditTwoEdit', {
            url: "/exchangeAuditTwoEdit/:id",
            templateUrl: 'views/exchangeActivate/exchangeOrder/exchangeAuditTwoEdit.html',
            data: {pageTitle: '积分兑换二次核销'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchangeActivate/exchangeOrder/exchangeAuditDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchangeActivate.shareOrder', {
            url: "/shareOrder",
            templateUrl: 'views/exchangeActivate/shareOrder/shareOrderQuery.html',
            data: {pageTitle: '订单分润明细查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchangeActivate/shareOrder/shareOrderQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchangeActivate.withdrawals', {
            url: "/withdrawals",
            templateUrl: 'views/exchangeActivate/withdrawals/withdrawalsQuery.html',
            data: {pageTitle: '用户提现记录查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchangeActivate/withdrawals/withdrawalsQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchangeActivate.banner', {
            url: "/banner",
            templateUrl: 'views/exchangeActivate/banner/bannerQuery.html',
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
                        files: ['js/controllers/exchangeActivate/banner/bannerQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchangeActivate.bannerAdd', {
            url: "/bannerAdd",
            templateUrl: 'views/exchangeActivate/banner/bannerAdd.html',
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
                        files: ['js/controllers/exchangeActivate/banner/bannerAddCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchangeActivate.bannerEdit', {
            url: "/bannerEdit/:id",
            templateUrl: 'views/exchangeActivate/banner/bannerEdit.html',
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
                        files: ['js/controllers/exchangeActivate/banner/bannerEditCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchangeActivate.bannerDetail', {
            url: "/bannerDetail/:id",
            templateUrl: 'views/exchangeActivate/banner/bannerDetail.html',
            data: {pageTitle: 'Banner详情'},
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
                        files: ['js/controllers/exchangeActivate/banner/bannerEditCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchangeActivate.notice', {
            url: "/notice",
            templateUrl: 'views/exchangeActivate/notice/noticeQuery.html',
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
                        files: ['js/controllers/exchangeActivate/notice/noticeQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchangeActivate.noticeAdd', {
            url: "/noticeAdd",
            templateUrl: 'views/exchangeActivate/notice/noticeAdd.html',
            data: {pageTitle: '公告新增'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('summernote');
                    $ocLazyLoad.load('angular-summernote');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchangeActivate/notice/noticeAddCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchangeActivate.noticeEdit', {
            url: "/noticeEdit/:id",
            templateUrl: 'views/exchangeActivate/notice/noticeEdit.html',
            data: {pageTitle: '公告编辑'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('summernote');
                    $ocLazyLoad.load('angular-summernote');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchangeActivate/notice/noticeEditCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchangeActivate.noticeDetail', {
            url: "/noticeDetail/:id",
            templateUrl: 'views/exchangeActivate/notice/noticeDetail.html',
            data: {pageTitle: '公告详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('summernote');
                    $ocLazyLoad.load('angular-summernote');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchangeActivate/notice/noticeEditCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchangeActivate.helpCenter', {
            url: "/helpCenter",
            templateUrl: 'views/exchangeActivate/helpCenter/helpCenterQuery.html',
            data: {pageTitle: '帮助中心管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchangeActivate/helpCenter/helpCenterQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchangeActivate.helpCenterAdd', {
            url: "/helpCenterAdd",
            templateUrl: 'views/exchangeActivate/helpCenter/helpCenterAdd.html',
            data: {pageTitle: '帮助中心新增'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchangeActivate/helpCenter/helpCenterAddCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchangeActivate.helpCenterEdit', {
            url: "/helpCenterEdit/:id",
            templateUrl: 'views/exchangeActivate/helpCenter/helpCenterEdit.html',
            data: {pageTitle: '帮助中心编辑'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchangeActivate/helpCenter/helpCenterEditCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchangeActivate.helpCenterDetail', {
            url: "/helpCenterDetail/:id",
            templateUrl: 'views/exchangeActivate/helpCenter/helpCenterDetail.html',
            data: {pageTitle: '帮助中心编辑'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchangeActivate/helpCenter/helpCenterEditCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchangeActivate.receive', {
            url: "/receive",
            templateUrl: 'views/exchangeActivate/receive/receiveOrderQuery.html',
            data: {pageTitle: '收款订单查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchangeActivate/receive/receiveOrderQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchangeActivate.receiveOrderDetail', {
            url: "/receiveOrderDetail/:id",
            templateUrl: 'views/exchangeActivate/receive/receiveOrderDetail.html',
            data: {pageTitle: '收款订单查询详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchangeActivate/receive/receiveOrderDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchangeActivate.repayment', {
            url: "/repayment",
            templateUrl: 'views/exchangeActivate/repayment/repaymentOrderQuery.html',
            data: {pageTitle: '信用卡还款订单查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchangeActivate/repayment/repaymentOrderQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchangeActivate.repaymentOrderDetail', {
            url: "/repaymentOrderDetail/:id",
            templateUrl: 'views/exchangeActivate/repayment/repaymentOrderDetail.html',
            data: {pageTitle: '信用卡还款订单详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchangeActivate/repayment/repaymentOrderDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        /*路由管理*/
        .state('exchangeActivate.route', {
            url: "/route",
            templateUrl: 'views/exchangeActivate/route/routeQuery.html',
            data: {pageTitle: '路由管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('fileUpload');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchangeActivate/route/routeQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchangeActivate.routeAdd', {
            url: "/routeAdd",
            templateUrl: 'views/exchangeActivate/route/routeAdd.html',
            data: {pageTitle: '新增核销渠道'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchangeActivate/route/routeAddCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchangeActivate.routeEdit', {
            url: "/routeEdit/:id",
            templateUrl: 'views/exchangeActivate/route/routeEdit.html',
            data: {pageTitle: '修改核销渠道'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchangeActivate/route/routeEditCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchangeActivate.routeDetail', {
            url: "/routeDetail/:id",
            templateUrl: 'views/exchangeActivate/route/routeDetail.html',
            data: {pageTitle: '核销渠道详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchangeActivate/route/routeEditCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchangeActivate.routeGoodAdd', {
            url: "/routeGoodAdd/:channelNo",
            templateUrl: 'views/exchangeActivate/route/routeGoodAdd.html',
            data: {pageTitle: '新增渠道商品'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('infinity-chosen');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchangeActivate/route/routeGoodAddCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchangeActivate.routeGood', {
            url: "/routeGood",
            templateUrl: 'views/exchangeActivate/routeGood/routeGoodQuery.html',
            data: {pageTitle: '核销渠道商品明细查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('infinity-chosen');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchangeActivate/routeGood/routeGoodQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchangeActivate.routeGoodDetail', {
            url: "/routeGoodDetail/:id",
            templateUrl: 'views/exchangeActivate/routeGood/routeGoodDetail.html',
            data: {pageTitle: '渠道商品详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('infinity-chosen');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchangeActivate/routeGood/routeGoodEditCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchangeActivate.routeGoodEdit', {
            url: "/routeGoodEdit/:id",
            templateUrl: 'views/exchangeActivate/routeGood/routeGoodEdit.html',
            data: {pageTitle: '修改渠道商品'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('infinity-chosen');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchangeActivate/routeGood/routeGoodEditCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchangeActivate.productMedia', {
            url: "/productMedia",
            templateUrl: 'views/exchangeActivate/media/productMediaQuery.html',
            data: {pageTitle: '上游商品信息查询'},
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
                        files: ['js/controllers/exchangeActivate/media/productMediaQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        
}