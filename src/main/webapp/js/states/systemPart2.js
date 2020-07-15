function initSystemPart2(stateProvider) {
    /*欢迎页面*/
    stateProvider
    /* 出款服务管理   */
        .state('money', {
            abstract: true,
            url: "/money",
            templateUrl: "views/common/content.html",
        })
        .state('money.managerService', {
            url: "/managerService",
            templateUrl: "views/money/managerService.html",
            data: {pageTitle: '出款服务管理'},
            //controller: "managerServiceCtrl",//
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/money/managerServiceCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('money.setjob', {
            url: "/setjob/:id",
            templateUrl: "views/money/setjob.html",
            data: {pageTitle: '预警人员分配任务'},
            //controller: "setjobCtrl",//
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/money/setjobCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        /*出款预警人员管理*/
        .state('money.kingWarningPeople', {
            url: "/kingWarningPeople",
            data: {pageTitle: '出款预警人员管理'},
            views: {
                '': {
                    templateUrl: 'views/org/warningPeople/kingWarningPeople.html'
                },
                'warningPeopleCenter@money.kingWarningPeople': {
                    templateUrl: 'views/org/warningPeople/warningPeopleCenter.html'
                }
            },
            //controller: "kingWarningPeopleCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('infinity-chosen')
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/org/warningPeople/kingWarningPeopleCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('money.addService', {
            url: "/addService",
            templateUrl: "views/money/addService.html",
            data: {pageTitle: '增加出款服务'},
            //controller: "addServiceCtrl",
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/money/addServiceCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('money.serviceDetail', {
            url: "/serviceDetail/:id/:serviceType",
            templateUrl: "views/money/serviceDetail.html",
            data: {pageTitle: '出款服务详情'},
            //controller: "serviceDetailCtrl",
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/money/serviceDetailCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('money.updateService', {
            url: "/updateService/:id",
            templateUrl: "views/money/updateService.html",
            data: {pageTitle: '修改出款服务'},
            //controller: "updateServiceCtrl",
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/money/updateServiceCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('money.updateRate', {
            url: "/updateRate/:id/:serviceType",
            templateUrl: "views/money/updateRate.html",
            data: {pageTitle: '修改出款服务费率'},
            //controller: "updateRateCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/money/updateRateCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('money.warningSet', {
            url: "/warningSet",
            data: {pageTitle: '预警阀值设置'},
            templateUrl: 'views/money/warningSet/warningSet.html',
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('colorpicker.module');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/money/warningSet/warningSet.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('money.addWarningSet', {
            url: "/addWarningSet",
            data: {pageTitle: '新增预警阀值设置'},
            templateUrl: 'views/money/warningSet/addWarningSet.html',
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('colorpicker.module');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/money/warningSet/addWarningSet.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('money.agentWithdraw', {
            url: "/agentWithdraw",
            templateUrl: "views/money/agentWithdraw.html",
            data: {pageTitle: '代理商提现控制'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('ui-switch');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/money/agentWithdrawCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        /*风控管理*/
        .state('risk', {
            abstract: true,
            url: "/risk",
            templateUrl: "views/common/content.html",
        })
            .state('risk.recheckQuery', {
                url: "/recheckQuery",
                templateUrl: "views/risk/recheckQuery.html",
                data: {pageTitle: '复审查询'},
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('infinity-chosen')
                        $ocLazyLoad.load('ngGrid');
                        $ocLazyLoad.load('My97DatePicker');
                        $ocLazyLoad.load('ui.select');
                        $ocLazyLoad.load('oitozero.ngSweetAlert');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/controllers/risk/recheckQueryCtrl.js?ver='+verNo]
                        });
                    }]
                }
            })

            .state('risk.MerExamineDetail', {
                url: "/risk/:mertId",
                templateUrl: "views/risk/remerchantExamineDetail.html",
                data: {pageTitle: '商户审核详情'},
                //controller: "remerchantExamineDetailCtrl",
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('ngGrid');
                        $ocLazyLoad.load('datePicker');
                        $ocLazyLoad.load('ui.select');
                        $ocLazyLoad.load('fancybox');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/controllers/risk/remerchantExamineDetailCtrl.js?ver='+verNo]
                        });
                    }]
                }
            })
        .state('risk.blacklistQuery', {
            url: "/blacklistQuery",
            templateUrl: "views/risk/blacklistQuery.html",
            data: {pageTitle: '黑名单管理'},
            //controller: "blacklistQueryCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/blacklistQueryCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('risk.blacklistimport', {
            url: "/blacklistimport",
            templateUrl: "views/risk/blacklistImport.html",
            data: {pageTitle: '黑名单批量导入'},
            resolve: {
            	  loadPlugin: function ($ocLazyLoad) {
                      $ocLazyLoad.load('fileUpload');
                  },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/blacklistImportCtrl.js?ver='+verNo]
                    
                    });
                }]
            }
        })
        
        
        .state('risk.blackDataQuery', {
            url: "/blackDataQuery",
            templateUrl: "views/risk/blackDataQuery.html",
            data: {pageTitle: '黑名单资料处理管理'},
	        //controller: "blackDataQueryCtrl",
	        resolve: {
	        	loadPlugin: function ($ocLazyLoad) {
	        		$ocLazyLoad.load('datePicker');
	                $ocLazyLoad.load('ui-switch');
	                $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
		        },
	            deps: ['$ocLazyLoad', function ($ocLazyLoad) {
	                return $ocLazyLoad.load({
	                    name: 'inspinia',
	                    files: ['js/controllers/risk/blackDataQueryCtrl.js?ver='+verNo]
	                });
	            }]
	        }
        })
            .state('risk.blackDataInfo', {
                url: "/blackData/:id/:type",
                templateUrl: 'views/risk/blackDataInfo.html',
                data: {pageTitle: '黑名单资料处理'},
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('localytics.directives');
                        $ocLazyLoad.load('oitozero.ngSweetAlert');
                        $ocLazyLoad.load('fancybox');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/controllers/risk/blackDataInfoCtrl.js?ver='+verNo]
                        });
                    }]
                }
            })
        .state('risk.blackDataInfoTwo', {
            url: "/blackDataTwo/:id/:type",
            templateUrl: 'views/risk/blackDataInfoTwo.html',
            data: {pageTitle: '黑名单资料详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/blackDataInfoTwoCtrl.js']
                    });
                }]
            }
        })
        .state('risk.blacklistDetail', {
            url: "/blacklistDetail/:id",
            templateUrl: "views/risk/blacklistDetail.html",
            data: {pageTitle: '黑名单详情'},
            //controller: "blacklistDetailCtrl",
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/blacklistDetailCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('risk.blacklistUp', {
            url: "/blacklistUp/:id",
            templateUrl: "views/risk/blacklistUp.html",
            data: {pageTitle: '黑名单修改'},
            //controller: "blacklistUpCtrl",
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/blacklistUpCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('risk.blacklistAdd', {
            url: "/blacklistAdd",
            templateUrl: "views/risk/blacklistAdd.html",
            data: {pageTitle: '黑名单新增'},
            //controller: "blacklistAddCtrl",
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/blacklistAddCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('risk.blacklistDetailMag', {
            url: "/blacklistDetailMag/:id/:no",
            templateUrl: "views/risk/blacklistDetailMag.html",
            data: {pageTitle: '黑名单明细管理'},
            //controller: "blacklistDetailMagCtrl",
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/blacklistDetailMagCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('risk.whitelistMag', {
            url: "/whitelistMag",
            templateUrl: "views/risk/whitelistMag.html",
            data: {pageTitle: '白名单管理'},
            //controller: "whitelistMagCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/whitelistMagCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('risk.whitelistDetail', {
            url: "/whitelistDetail/:id",
            templateUrl: "views/risk/whitelistDetail.html",
            data: {pageTitle: '白名单详情'},
            //controller: "whitelistDetailCtrl",
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/whitelistDetailCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('risk.whitelistUp', {
            url: "/whitelistUp/:id",
            templateUrl: "views/risk/whitelistUp.html",
            data: {pageTitle: '白名单修改'},
            //controller: "whitelistUpCtrl",
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/whitelistUpCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('risk.whitelistAdd', {
            url: "/whitelistAdd",
            templateUrl: "views/risk/whitelistAdd.html",
            data: {pageTitle: '白名单新增'},
            //controller: "whitelistAddCtrl",
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/whitelistAddCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('risk.whitelistDetailMag', {
            url: "/whitelistDetailMag/:id/:no",
            templateUrl: "views/risk/whitelistDetailMag.html",
            data: {pageTitle: '白名单明细管理'},
            //controller: "whitelistDetailMagCtrl",
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/whitelistDetailMagCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('risk.riskProblemMag', {
            url: "/riskProblemMag",
            templateUrl: "views/risk/riskProblemMag.html",
            data: {pageTitle: '风控问题管理'},
            //controller: "riskProblemMagCtrl",
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/riskProblemMagCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('risk.riskProblemDetail', {
            url: "/riskProblemDetail/:id",
            templateUrl: "views/risk/riskProblemDetail.html",
            data: {pageTitle: '风控问题详情'},
            //controller: "riskProblemDetailCtrl",
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/riskProblemDetailCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('risk.riskProblemAdd', {
            url: "/riskProblemAdd",
            templateUrl: "views/risk/riskProblemAdd.html",
            data: {pageTitle: '风控问题新增'},
            //controller: "riskProblemAddCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('ui.select');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/riskProblemAddCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('risk.riskProblemUp', {
            url: "/riskProblemUp/:id",
            templateUrl: "views/risk/riskProblemUp.html",
            data: {pageTitle: '风控问题修改'},
            //controller: "riskProblemUpCtrl",
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/riskProblemUpCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('risk.riskFeedback', {
            url: "/riskFeedback/:id",
            templateUrl: "views/risk/riskFeedback.html",
            data: {pageTitle: '风控问题处理结果反馈'},
            //controller: "riskFeedbackCtrl",
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/riskFeedbackCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('risk.riskProblemAudit', {
            url: "/riskProblemAudit",
            templateUrl: "views/risk/riskProblemAudit.html",
            data: {pageTitle: '风控问题审核'},
            //controller: "riskProblemAuditCtrl",
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/riskProblemAuditCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('risk.riskProblemAuditResult', {
            url: "/riskProblemAuditResult/:id",
            templateUrl: "views/risk/riskProblemAuditResult.html",
            data: {pageTitle: '风控问题审核结果'},
            //controller: "riskProblemAuditResultCtrl",
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/riskProblemAuditResultCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('risk.faceRecognition', {
            url: "/faceRecognition",
            templateUrl: "views/risk/faceRecognition.html",
            data: {pageTitle: '人脸认证通道配置'},
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/faceRecognitionCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('risk.riskRulesMag', {
            url: "/riskRulesMag",
            templateUrl: "views/risk/riskRulesMgr.html",
            data: {pageTitle: '风控规则管理'},
            //controller: "riskRulesMgrCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/riskRulesMgrCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('risk.riskRulesDetail', {
            url: "/riskRulesDetail/:id",
            templateUrl: "views/risk/riskRulesDetail.html",
            data: {pageTitle: '风控规则详情'},
            //controller: "riskRulesDetailCtrl",
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/riskRulesDetailCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('risk.riskRulesSet', {
            url: "/riskRulesSet/:id",
            templateUrl: "views/risk/riskRulesSet.html",
            data: {pageTitle: '风控规则设置'},
            //controller: "riskRulesSetCtrl",
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/riskRulesSetCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('risk.riskRulesSet102', {
            url: "/riskRulesSet102/:id",
            
            views: {
                '': {
                	templateUrl: "views/risk/riskRulesSet102.html",
                },
                'riskRulesSetCommon@risk.riskRulesSet102': {
                    templateUrl: 'views/risk/riskRulesSetCommon.html'
                }
            },
            
            data: {pageTitle: '风控规则设置'},
            //controller: "riskRulesSetCtrl",
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/riskRulesSetCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('risk.riskRulesSet103', {
            url: "/riskRulesSet103/:id",
            views: {
                '': {
                	templateUrl: "views/risk/riskRulesSet103.html",
                },
                'riskRulesSetCommon@risk.riskRulesSet103': {
                    templateUrl: 'views/risk/riskRulesSetCommon.html'
                }
            },
            data: {pageTitle: '风控规则设置'},
            //controller: "riskRulesSetCtrl",
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/riskRulesSetCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('risk.riskRulesSet104', {
            url: "/riskRulesSet104/:id",
            views: {
                '': {
                	templateUrl: "views/risk/riskRulesSet104.html",
                },
                'riskRulesSetCommon@risk.riskRulesSet104': {
                    templateUrl: 'views/risk/riskRulesSetCommon.html'
                }
            },
            data: {pageTitle: '风控规则设置'},
            //controller: "riskRulesSetCtrl",
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/riskRulesSetCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('risk.riskRulesSet105', {
            url: "/riskRulesSet105/:id",
            views: {
                '': {
                	templateUrl: "views/risk/riskRulesSet105.html",
                },
                'riskRulesSetCommon@risk.riskRulesSet105': {
                    templateUrl: 'views/risk/riskRulesSetCommon.html'
                }
            },
            data: {pageTitle: '风控规则设置'},
            //controller: "riskRulesSetCtrl",
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/riskRulesSetCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('risk.riskRulesSet106', {
            url: "/riskRulesSet106/:id",
            views: {
                '': {
                	templateUrl: "views/risk/riskRulesSet106.html",
                },
                'riskRulesSetCommon@risk.riskRulesSet106': {
                    templateUrl: 'views/risk/riskRulesSetCommon.html'
                }
            },
            data: {pageTitle: '风控规则设置'},
            //controller: "riskRulesSetCtrl",
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/riskRulesSetCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('risk.riskRulesSet107', {
            url: "/riskRulesSet107/:id",
            views: {
                '': {
                	templateUrl: "views/risk/riskRulesSet107.html",
                },
                'riskRulesSetCommon@risk.riskRulesSet107': {
                    templateUrl: 'views/risk/riskRulesSetCommon.html'
                }
            },
            data: {pageTitle: '风控规则设置'},
            //controller: "riskRulesSetCtrl",
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/riskRulesSetCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('risk.riskRulesSet108', {
            url: "/riskRulesSet108/:id",
            views: {
                '': {
                	templateUrl: "views/risk/riskRulesSet108.html",
                },
                'riskRulesSetCommon@risk.riskRulesSet108': {
                    templateUrl: 'views/risk/riskRulesSetCommon.html'
                }
            },
            data: {pageTitle: '风控规则设置'},
            //controller: "riskRulesSetCtrl",
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/riskRulesSetCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('risk.riskRulesSet109', {
            url: "/riskRulesSet109/:id",
            views: {
                '': {
                	templateUrl: "views/risk/riskRulesSet109.html",
                },
                'riskRulesSetCommon@risk.riskRulesSet109': {
                    templateUrl: 'views/risk/riskRulesSetCommon.html'
                }
            },
            data: {pageTitle: '风控规则设置'},
            //controller: "riskRulesSetCtrl",
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/riskRulesSetCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('risk.riskRulesSet110', {
            url: "/riskRulesSet110/:id",
            views: {
                '': {
                	templateUrl: "views/risk/riskRulesSet110.html",
                },
                'riskRulesSetCommon@risk.riskRulesSet110': {
                    templateUrl: 'views/risk/riskRulesSetCommon.html'
                }
            },
            data: {pageTitle: '风控规则设置'},
            //controller: "riskRulesSetCtrl",
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/riskRulesSetCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('risk.riskRulesSet111', {
            url: "/riskRulesSet111/:id",
            views: {
                '': {
                	templateUrl: "views/risk/riskRulesSet111.html",
                },
                'riskRulesSetCommon@risk.riskRulesSet111': {
                    templateUrl: 'views/risk/riskRulesSetCommon.html'
                }
            },
            data: {pageTitle: '风控规则设置'},
            //controller: "riskRulesSetCtrl",
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/riskRulesSetCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('risk.riskRulesSet112', {
            url: "/riskRulesSet112/:id",
            views: {
                '': {
                	templateUrl: "views/risk/riskRulesSet112.html",
                },
                'riskRulesSetCommon@risk.riskRulesSet112': {
                    templateUrl: 'views/risk/riskRulesSetCommon.html'
                }
            },
            data: {pageTitle: '风控规则设置'},
            //controller: "riskRulesSetCtrl",
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/riskRulesSetCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('risk.riskRulesSet113', {
            url: "/riskRulesSet113/:id",
            views: {
                '': {
                	templateUrl: "views/risk/riskRulesSet113.html",
                },
                'riskRulesSetCommon@risk.riskRulesSet113': {
                    templateUrl: 'views/risk/riskRulesSetCommon.html'
                }
            },
            data: {pageTitle: '风控规则设置'},
            //controller: "riskRulesSetCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/riskRulesSetCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('risk.riskRulesSet114', {
            url: "/riskRulesSet114/:id",
            views: {
                '': {
                	templateUrl: "views/risk/riskRulesSet114.html",
                },
                'riskRulesSetCommon@risk.riskRulesSet114': {
                    templateUrl: 'views/risk/riskRulesSetCommon.html'
                }
            },
            data: {pageTitle: '风控规则设置'},
            //controller: "riskRulesSetCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/riskRulesSetCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('risk.riskRulesSet115', {
            url: "/riskRulesSet115/:id",
            views: {
                '': {
                	templateUrl: "views/risk/riskRulesSet115.html",
                },
                'riskRulesSetCommon@risk.riskRulesSet115': {
                    templateUrl: 'views/risk/riskRulesSetCommon.html'
                }
            },
            data: {pageTitle: '风控规则设置'},
            ///controller: "riskRulesSetCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/riskRulesSetCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('risk.riskRulesSet116', {
            url: "/riskRulesSet116/:id",
            views: {
                '': {
                    templateUrl: "views/risk/riskRulesSet116.html",
                },
                'riskRulesSetCommon@risk.riskRulesSet116': {
                    templateUrl: 'views/risk/riskRulesSetCommon.html'
                }
            },
            data: {pageTitle: '风控规则设置'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/riskRulesSetCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('risk.riskRulesSet117', {
            url: "/riskRulesSet117/:id",
            views: {
                '': {
                    templateUrl: "views/risk/riskRulesSet117.html",
                },
                'riskRulesSetCommon@risk.riskRulesSet117': {
                    templateUrl: 'views/risk/riskRulesSetCommon.html'
                }
            },
            data: {pageTitle: '风控规则设置'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/riskRulesSetCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('risk.riskRulesSet118', {
            url: "/riskRulesSet118/:id",
            views: {
                '': {
                    templateUrl: "views/risk/riskRulesSet118.html",
                },
                'riskRulesSetCommon@risk.riskRulesSet118': {
                    templateUrl: 'views/risk/riskRulesSetCommon.html'
                }
            },
            data: {pageTitle: '风控规则设置'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/riskRulesSetCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('risk.riskRulesSet119', {
            url: "/riskRulesSet119/:id",
            views: {
                '': {
                    templateUrl: "views/risk/riskRulesSet119.html",
                },
                'riskRulesSetCommon@risk.riskRulesSet119': {
                    templateUrl: 'views/risk/riskRulesSetCommon.html'
                }
            },
            data: {pageTitle: '风控规则设置'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/riskRulesSetCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('risk.riskRulesSet120', {
            url: "/riskRulesSet120/:id",
            views: {
                '': {
                    templateUrl: "views/risk/riskRulesSet120.html",
                },
                'riskRulesSetCommon@risk.riskRulesSet120': {
                    templateUrl: 'views/risk/riskRulesSetCommon.html'
                }
            },
            data: {pageTitle: '风控规则设置'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/riskRulesSetCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('risk.riskRulesSet121', {
            url: "/riskRulesSet121/:id",
            views: {
                '': {
                    templateUrl: "views/risk/riskRulesSet121.html",
                },
                'riskRulesSetCommon@risk.riskRulesSet121': {
                    templateUrl: 'views/risk/riskRulesSetCommon.html'
                }
            },
            data: {pageTitle: '风控规则设置'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/riskRulesSetCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
         .state('risk.riskRulesSet122', {
            url: "/riskRulesSet122/:id",
            views: {
                '': {
                    templateUrl: "views/risk/riskRulesSet122.html",
                },
                'riskRulesSetCommon@risk.riskRulesSet122': {
                    templateUrl: 'views/risk/riskRulesSetCommon.html'
                }
            },
            data: {pageTitle: '风控规则设置'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/riskRulesSetCtrl.js?ver='+verNo]
                    });
                }]
            }
        })

         .state('risk.riskRulesSet123', {
            url: "/riskRulesSet123/:id",
            views: {
                '': {
                    templateUrl: "views/risk/riskRulesSet123.html",
                },
                'riskRulesSetCommon@risk.riskRulesSet123': {
                    templateUrl: 'views/risk/riskRulesSetCommon.html'
                }
            },
            data: {pageTitle: '风控规则设置'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/riskRulesSetCtrl.js?ver='+verNo]
                    });
                }]
            }
        })

         .state('risk.riskRulesSet124', {
            url: "/riskRulesSet124/:id",
            views: {
                '': {
                    templateUrl: "views/risk/riskRulesSet124.html",
                },
                'riskRulesSetCommon@risk.riskRulesSet124': {
                    templateUrl: 'views/risk/riskRulesSetCommon.html'
                }
            },
            data: {pageTitle: '风控规则设置'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/riskRulesSetCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
         .state('risk.riskRulesSet125', {
            url: "/riskRulesSet125/:id",
            views: {
                '': {
                    templateUrl: "views/risk/riskRulesSet125.html",
                },
                'riskRulesSetCommon@risk.riskRulesSet125': {
                    templateUrl: 'views/risk/riskRulesSetCommon.html'
                }
            },
            data: {pageTitle: '风控规则设置'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/riskRulesSetCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('risk.riskRulesSet127', {
            url: "/riskRulesSet127/:id",
            views: {
                '': {
                    templateUrl: "views/risk/riskRulesSet127.html",
                },
                'riskRulesSetCommon@risk.riskRulesSet127': {
                    templateUrl: 'views/risk/riskRulesSetCommon.html'
                }
            },
            data: {pageTitle: '风控规则设置'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/riskRulesSetCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('risk.riskRulesSet128', {
            url: "/riskRulesSet128/:id",
            views: {
                '': {
                    templateUrl: "views/risk/riskRulesSet128.html",
                },
                'riskRulesSetCommon@risk.riskRulesSet128': {
                    templateUrl: 'views/risk/riskRulesSetCommon.html'
                }
            },
            data: {pageTitle: '风控规则设置'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/riskRulesSetCtrl.js?ver='+verNo]
                    });
                }]
            }
        })

        .state('risk.riskRulesSet129', {
            url: "/riskRulesSet129/:id",
            views: {
                '': {
                    templateUrl: "views/risk/riskRulesSet129.html",
                },
                'riskRulesSetCommon@risk.riskRulesSet129': {
                    templateUrl: 'views/risk/riskRulesSetCommon.html'
                }
            },
            data: {pageTitle: '风控规则设置'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/riskRulesSetCtrl.js?ver='+verNo]
                    });
                }]
            }
        })

        .state('risk.riskRulesSet130', {
            url: "/riskRulesSet130/:id",
            views: {
                '': {
                    templateUrl: "views/risk/riskRulesSet130.html",
                },
                'riskRulesSetCommon@risk.riskRulesSet130': {
                    templateUrl: 'views/risk/riskRulesSetCommon.html'
                }
            },
            data: {pageTitle: '风控规则设置'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/riskRulesSetCtrl.js?ver='+verNo]
                    });
                }]
            }
        })

        .state('risk.tradeRestrict', {
            url: "/tradeRestrict",
            templateUrl: 'views/risk/tradeRestrict/tradeRestrictQuery.html',
            data: {pageTitle: '交易限制记录'},
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
                        files: ['js/controllers/risk/tradeRestrict/tradeRestrictQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        //回复模板管理
        .state('risk.dealTemplate', {
            url: "/dealTemplate",
            templateUrl: "views/risk/dealTemplate.html",
            data: {pageTitle: '回复模板管理'},
            //controller: "dealTemplateCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/dealTemplateCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('risk.queryAuthCardList', {
            url: "/queryAuthCardList",
            templateUrl: "views/risk/authCardQuery.html",
            data: {pageTitle: '实名认证记录'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/authCardCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        /*风控事件记录*/
        .state('risk.queryEventRecordList', {
            url: "/queryEventRecordList",
            templateUrl: "views/risk/riskEventRecordQuery.html",
            data: {pageTitle: '风控事件记录'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/riskEventRecord.js?ver=' + verNo]
                    });
                }]
            }
        })
        /*风控事件记录详情*/
        .state('risk.riskEventRecordDetail', {
            url: "/riskEventRecordDetail/:id",
            templateUrl: "views/risk/riskEventRecordDetail.html",
            data: {pageTitle: '风控问题详情'},
            //controller: "riskEventRecordDetailCtrl",
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/riskEventRecordDetailCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('risk.cardBins', {
            url: "/cardBins",
            templateUrl: "views/risk/cardBins/cardBinsQuery.html",
            data: {pageTitle: '卡bin管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ngGrid');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/cardBins/cardBinsQueryCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('risk.addCardBins', {
            url: "/addCardBins",
            data: {pageTitle: '卡bin新增'},
            views: {
                '': {
                    templateUrl: 'views/risk/cardBins/addCardBins.html'
                },
                'cardBinsCommon@risk.addCardBins': {
                    templateUrl: 'views/risk/cardBins/cardBinsCommon.html'
                }
            },
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/cardBins/addCardBinsCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('risk.editCardBins', {
            url: "/editCardBins/:id",
            data: {pageTitle: '卡bin修改'},
            views: {
                '': {
                    templateUrl: 'views/risk/cardBins/editCardBins.html'
                },
                'cardBinsCommon@risk.editCardBins': {
                    templateUrl: 'views/risk/cardBins/cardBinsCommon.html'
                }
            },
            //controller: "modifyCreditRepayNoticeCtrl",
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/cardBins/editCardBinsCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('risk.importCardBins', {
            url: "/importCardBins",
            templateUrl: "views/risk/cardBins/importCardBins.html",
            data: {pageTitle: '卡bin导入'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('localytics.directives');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/cardBins/importCardBinsCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('risk.surveyOrder', {
            url: "/surveyOrder",
            templateUrl: 'views/risk/surveyOrder/surveyOrderQuery.html',
            data: {pageTitle: '调单管理'},
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
                        files: ['js/controllers/risk/surveyOrder/surveyOrderQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('risk.surveyOrderAdd', {
            url: "/surveyOrderAdd",
            templateUrl: 'views/risk/surveyOrder/surveyOrderAdd.html',
            data: {pageTitle: '调单新增'},
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
                        files: ['js/controllers/risk/surveyOrder/surveyOrderAddCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('risk.surveyOrderDetail', {
            url: "/surveyOrderDetail/:id",
            templateUrl: 'views/risk/surveyOrder/surveyOrderDetail.html',
            data: {pageTitle: '调单详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/surveyOrder/surveyOrderDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('risk.deduct', {
            url: "/deduct",
            templateUrl: 'views/risk/deduct/deductQuery.html',
            data: {pageTitle: '调单扣款查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/deduct/deductQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('risk.deductDetail', {
            url: "/deductDetail",
            templateUrl: 'views/risk/deductDetail/deductDetailQuery.html',
            data: {pageTitle: '调单扣款明细查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');

                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/risk/deductDetail/deductDetailQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        /********风控管理 end*************/

        /********代理商工单 begin*************/
      .state('workOrder', {
          abstract: true,
          url: "/workOrder",
          templateUrl: "views/common/content.html",
      })
      .state('workOrder.userManager', {
          url: "/workOrder/userManager",
          templateUrl: 'views/workOrder/userManager.html',
          data: {pageTitle: '人员管理'},
          resolve: {
              loadPlugin: function ($ocLazyLoad) {
                  $ocLazyLoad.load('oitozero.ngSweetAlert');
                  $ocLazyLoad.load('ui-switch');
                  $ocLazyLoad.load('localytics.directives');
                  $ocLazyLoad.load('infinity-chosen');
                  $ocLazyLoad.load('My97DatePicker');
                  $ocLazyLoad.load('fileUpload');
              },
              deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                  return $ocLazyLoad.load({
                      name: 'inspinia',
                      files: ['js/controllers/workOrder/userManagerCtrl.js?ver='+verNo]
                  });
              }]
          }
      })
      .state('workOrder.userManagerEdit', {
          url: "/workOrder/userManagerEdit/:id",
          templateUrl: 'views/workOrder/userManagerEdit.html',
          data: {pageTitle: '修改工单处理人员'},
          resolve: {
              loadPlugin: function ($ocLazyLoad) {
                  $ocLazyLoad.load('localytics.directives');
                  $ocLazyLoad.load('oitozero.ngSweetAlert');
                  $ocLazyLoad.load('infinity-chosen');
                  $ocLazyLoad.load('My97DatePicker');
                  $ocLazyLoad.load('fileUpload');
              },
              deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                  return $ocLazyLoad.load({
                      name: 'inspinia',
                      files: ['js/controllers/workOrder/userManagerEditCtrl.js?ver='+verNo]
                  });
              }]
          }
      })
      .state('workOrder.welcome', {
          url: "/welcome/index",
          data: {pageTitle: '待处理工单'},
          templateUrl: "views/welcome.html",
          controller: "welcomeCtrl",
          resolve: {
              deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                  return $ocLazyLoad.load({
                      name: 'inspinia',
                      files: ['js/controllers/workOrder/welcomeCtrl.js?ver=' + verNo]
                  });
              }]
          }
      })
      .state('workOrder.userManagerAdd', {
          url: "/workOrder/userManagerAdd",
          templateUrl: 'views/workOrder/userManagerAdd.html',
          data: {pageTitle: '新增工单处理人员'},
          resolve: {
              loadPlugin: function ($ocLazyLoad) {
                  $ocLazyLoad.load('localytics.directives');
                  $ocLazyLoad.load('oitozero.ngSweetAlert');
                  $ocLazyLoad.load('infinity-chosen');
                  $ocLazyLoad.load('My97DatePicker');
                  $ocLazyLoad.load('fileUpload');
              },
              deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                  return $ocLazyLoad.load({
                      name: 'inspinia',
                      files: ['js/controllers/workOrder/userManagerAddCtrl.js?ver='+verNo]
                  });
              }]
          }
      })


      .state('workOrder.workOrderType', {
          url: "/workOrder/workOrderType",
          templateUrl: 'views/workOrder/workType.html',
          data: {pageTitle: '工单类型'},
          resolve: {
              loadPlugin: function ($ocLazyLoad) {
                  $ocLazyLoad.load('oitozero.ngSweetAlert');
                  $ocLazyLoad.load('ui-switch');
                  $ocLazyLoad.load('localytics.directives');
                  $ocLazyLoad.load('infinity-chosen');
                  $ocLazyLoad.load('My97DatePicker');
                  $ocLazyLoad.load('fileUpload');
              },
              deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                  return $ocLazyLoad.load({
                      name: 'inspinia',
                      files: ['js/controllers/workOrder/workTypeCtrl.js?ver='+verNo]
                  });
              }]
          }
      })

      .state('workOrder.workTypeAdd', {
          url: "/workOrder/workTypeAdd",
          templateUrl: 'views/workOrder/workTypeAdd.html',
          data: {pageTitle: '新增工单类型'},
          resolve: {
              loadPlugin: function ($ocLazyLoad) {
                  $ocLazyLoad.load('oitozero.ngSweetAlert');
                  $ocLazyLoad.load('ui-switch');
                  $ocLazyLoad.load('localytics.directives');
                  $ocLazyLoad.load('infinity-chosen');
                  $ocLazyLoad.load('My97DatePicker');
                  $ocLazyLoad.load('fileUpload');
                  $ocLazyLoad.load('fancybox');

                  $ocLazyLoad.load('summernote');
                  $ocLazyLoad.load('angular-summernote');
              },
              deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                  return $ocLazyLoad.load({
                      name: 'inspinia',
                      files: ['js/controllers/workOrder/workTypeAddCtrl.js?ver='+verNo]
                  });
              }]
          }
      })

      .state('workOrder.workTypeEdit', {
          url: "/workOrder/workTypeEdit/:id",
          templateUrl: 'views/workOrder/workTypeEdit.html',
          data: {pageTitle: '修改工单类型'},
          resolve: {
              loadPlugin: function ($ocLazyLoad) {
                  $ocLazyLoad.load('oitozero.ngSweetAlert');
                  $ocLazyLoad.load('ui-switch');
                  $ocLazyLoad.load('localytics.directives');
                  $ocLazyLoad.load('infinity-chosen');
                  $ocLazyLoad.load('My97DatePicker');
                  $ocLazyLoad.load('fileUpload');
                  $ocLazyLoad.load('fancybox');

                  $ocLazyLoad.load('summernote');
                  $ocLazyLoad.load('angular-summernote');
              },
              deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                  return $ocLazyLoad.load({
                      name: 'inspinia',
                      files: ['js/controllers/workOrder/workTypeEditCtrl.js?ver='+verNo]
                  });
              }]
          }
      })

      .state('workOrder.workTypeDetail', {
          url: "/workOrder/workTypeDetail/:id",
          templateUrl: 'views/workOrder/workTypeDetail.html',
          data: {pageTitle: '工单类型详情'},
          resolve: {
              loadPlugin: function ($ocLazyLoad) {
                  $ocLazyLoad.load('oitozero.ngSweetAlert');
                  $ocLazyLoad.load('ui-switch');
                  $ocLazyLoad.load('localytics.directives');
                  $ocLazyLoad.load('infinity-chosen');
                  $ocLazyLoad.load('My97DatePicker');
                  $ocLazyLoad.load('fileUpload');
                  $ocLazyLoad.load('fancybox');

                  $ocLazyLoad.load('summernote');
                  $ocLazyLoad.load('angular-summernote');
              },
              deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                  return $ocLazyLoad.load({
                      name: 'inspinia',
                      files: ['js/controllers/workOrder/workTypeDetailCtrl.js?ver='+verNo]
                  });
              }]
          }
      })

      .state('workOrder.workOrderManager', {
          url: "/workOrder/workOrder/:type",
          templateUrl: 'views/workOrder/workOrder.html',
          data: {pageTitle: '工单记录'},
          resolve: {
              loadPlugin: function ($ocLazyLoad) {
                  $ocLazyLoad.load('oitozero.ngSweetAlert');
                  $ocLazyLoad.load('ui-switch');
                  $ocLazyLoad.load('localytics.directives');
                  $ocLazyLoad.load('infinity-chosen');
                  $ocLazyLoad.load('My97DatePicker');
                  $ocLazyLoad.load('fileUpload');
                  $ocLazyLoad.load('fancybox');

                  $ocLazyLoad.load('summernote');
                  $ocLazyLoad.load('angular-summernote');
              },
              deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                  return $ocLazyLoad.load({
                      name: 'inspinia',
                      files: ['js/controllers/workOrder/workOrderCtrl.js?ver='+verNo]
                  });
              }]
          }
      })
      .state('workOrder.workOrderAdd', {
          url: "/workOrder/workOrderAdd",
          templateUrl: 'views/workOrder/workOrderAdd.html',
          data: {pageTitle: '新增工单'},
          resolve: {
              loadPlugin: function ($ocLazyLoad) {
                  $ocLazyLoad.load('oitozero.ngSweetAlert');
                  $ocLazyLoad.load('ui-switch');
                  $ocLazyLoad.load('localytics.directives');
                  $ocLazyLoad.load('infinity-chosen');
                  $ocLazyLoad.load('My97DatePicker');
                  $ocLazyLoad.load('fileUpload');
                  $ocLazyLoad.load('fancybox');

                  $ocLazyLoad.load('summernote');
                  $ocLazyLoad.load('angular-summernote');
              },
              deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                  return $ocLazyLoad.load({
                      name: 'inspinia',
                      files: ['js/controllers/workOrder/workOrderAddCtrl.js?ver='+verNo]
                  });
              }]
          }
      })

      .state('workOrder.workOrderReply', {
          url: "/workOrder/workOrderReply/:type/:id",
          templateUrl: 'views/workOrder/workOrderReply.html',
          data: {pageTitle: '工单处理'},
          resolve: {
              loadPlugin: function ($ocLazyLoad) {
                  $ocLazyLoad.load('oitozero.ngSweetAlert');
                  $ocLazyLoad.load('ui-switch');
                  $ocLazyLoad.load('localytics.directives');
                  $ocLazyLoad.load('infinity-chosen');
                  $ocLazyLoad.load('My97DatePicker');
                  $ocLazyLoad.load('fileUpload');
                  $ocLazyLoad.load('fancybox');

                  $ocLazyLoad.load('summernote');
                  $ocLazyLoad.load('angular-summernote');
              },
              deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                  return $ocLazyLoad.load({
                      name: 'inspinia',
                      files: ['js/controllers/workOrder/workOrderReplyCtrl.js?ver='+verNo]
                  });
              }]
          }
      })
      .state('workOrder.workOrderRemark', {
          url: "/workOrder/workOrderRemark/:id",
          templateUrl: 'views/workOrder/workOrderRemark.html',
          data: {pageTitle: '工单备注'},
          resolve: {
              loadPlugin: function ($ocLazyLoad) {
                  $ocLazyLoad.load('oitozero.ngSweetAlert');
                  $ocLazyLoad.load('ui-switch');
                  $ocLazyLoad.load('localytics.directives');
                  $ocLazyLoad.load('infinity-chosen');
                  $ocLazyLoad.load('My97DatePicker');
                  $ocLazyLoad.load('fileUpload');
                  $ocLazyLoad.load('fancybox');

                  $ocLazyLoad.load('summernote');
                  $ocLazyLoad.load('angular-summernote');
              },
              deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                  return $ocLazyLoad.load({
                      name: 'inspinia',
                      files: ['js/controllers/workOrder/workOrderRemarkCtrl.js?ver='+verNo]
                  });
              }]
          }
      })
      .state('workOrder.workOrderDetail', {
          url: "/workOrder/workOrderDetail/:id",
          templateUrl: 'views/workOrder/workOrderDetail.html',
          data: {pageTitle: '工单详情'},
          resolve: {
              loadPlugin: function ($ocLazyLoad) {
                  $ocLazyLoad.load('oitozero.ngSweetAlert');
                  $ocLazyLoad.load('ui-switch');
                  $ocLazyLoad.load('localytics.directives');
                  $ocLazyLoad.load('infinity-chosen');
                  $ocLazyLoad.load('My97DatePicker');
                  $ocLazyLoad.load('fileUpload');
                  $ocLazyLoad.load('fancybox');

                  $ocLazyLoad.load('summernote');
                  $ocLazyLoad.load('angular-summernote');
              },
              deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                  return $ocLazyLoad.load({
                      name: 'inspinia',
                      files: ['js/controllers/workOrder/workOrderDetailCtrl.js?ver='+verNo]
                  });
              }]
          }
      })
      .state('workOrder.workOrderReject', {
          url: "/workOrder/workOrderReject/:type/:id",
          templateUrl: 'views/workOrder/workOrderReject.html',
          data: {pageTitle: '工单驳回'},
          resolve: {
              loadPlugin: function ($ocLazyLoad) {
                  $ocLazyLoad.load('oitozero.ngSweetAlert');
                  $ocLazyLoad.load('ui-switch');
                  $ocLazyLoad.load('localytics.directives');
                  $ocLazyLoad.load('infinity-chosen');
                  $ocLazyLoad.load('My97DatePicker');
                  $ocLazyLoad.load('fileUpload');
                  $ocLazyLoad.load('fancybox');

                  $ocLazyLoad.load('summernote');
                  $ocLazyLoad.load('angular-summernote');
              },
              deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                  return $ocLazyLoad.load({
                      name: 'inspinia',
                      files: ['js/controllers/workOrder/workOrderRejectCtrl.js?ver='+verNo]
                  });
              }]
          }
      })
      /********代理商工单 end*************/


        /*管理中心*/
        .state('sys', {
            abstract: true,
            url: "/sys",
            templateUrl: "views/common/content.html",
        })
        /************** 推送管理 start ***************/
      .state('sys.pushManager', {
          url: "/sys/pushManager",
          templateUrl: 'views/more/pushManager/pushManager.html',
          data: {pageTitle: '推送管理'},
          resolve: {
              loadPlugin: function ($ocLazyLoad) {
                  $ocLazyLoad.load('localytics.directives');
                  $ocLazyLoad.load('oitozero.ngSweetAlert');
                  $ocLazyLoad.load('infinity-chosen');
                  $ocLazyLoad.load('My97DatePicker');
                  $ocLazyLoad.load('fileUpload');
              },
              deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                  return $ocLazyLoad.load({
                      name: 'inspinia',
                      files: ['js/controllers/more/pushManager/pushManager.js?ver='+verNo]
                  });
              }]
          }
      })
      .state('sys.addPushManager', {
          url: "/addPushManager",

          views: {
              "": {templateUrl: "views/more/pushManager/addPushManager.html"},
              "pushManagercommon@sys.addPushManager": {templateUrl: "views/more/pushManager/addPushManagerCommon.html"}
          },

          //controller: 'saveOrUpdatePushManagerCrtl',
          data: {pageTitle: '推送内容管理新增新增'},
          resolve: {
              loadPlugin: function ($ocLazyLoad) {
                  $ocLazyLoad.load('datePicker');
                  $ocLazyLoad.load('oitozero.ngSweetAlert');
                  $ocLazyLoad.load('My97DatePicker');
              },
              deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                  return $ocLazyLoad.load({
                      name: 'inspinia',
                      files: ['js/controllers/more/pushManager/addPushManagerCrtl.js?ver=' + verNo]
                  });
              }]
          }
      })


      .state('sys.pushManagerDetail', {
          url: "/sys/pushManagerDetail/:id/:isPush",
          templateUrl: 'views/more/pushManager/pushManagerDetail.html',
          data: {pageTitle: '推送管理'},
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
                      files: ['js/controllers/more/pushManager/pushManagerDetailCtrl.js?ver='+verNo]
                  });
              }]
          }
      })
      .state('sys.updatePushManager', {
          url: "/updatePushManager/:id",
          views: {
              "": {templateUrl: "views/more/pushManager/updatePushManager.html"},
              "pushManagercommon@sys.updatePushManager": {templateUrl: "views/more/pushManager/addPushManagerCommon.html"}
          },

          //controller: 'saveOrUpdatePushManagerCrtl',
          data: {pageTitle: '推送内容修改'},
          resolve: {
              loadPlugin: function ($ocLazyLoad) {
                  $ocLazyLoad.load('datePicker');
                  $ocLazyLoad.load('oitozero.ngSweetAlert');
                  $ocLazyLoad.load('My97DatePicker');
              },
              deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                  return $ocLazyLoad.load({
                      name: 'inspinia',
                      files:  ['js/controllers/more/pushManager/addPushManagerCrtl.js?ver=' + verNo]
                  });
              }]
          }
      })
      /************** 推送管理 end ***************/
        .state('sys.noticeDetail', {
            url: "/noticeDetail/:id",
            //controller: "noticeDetailCtrl",
            data: {pageTitle: '公告详情'},
            views: {
                '': {
                    templateUrl: 'views/more/noticeDetail.html'
                },
                'noticeCommon@sys.noticeDetail': {
                    templateUrl: 'views/more/noticeCommon.html'
                }
            },
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('datePicker')
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/more/noticeDetailCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('sys.deliverNotice', {
            url: "/deliverNotice/:id",
            //controller: "deliverNoticeCtrl",
            data: {pageTitle: '下发公告'},
            views: {
                '': {
                    templateUrl: 'views/more/deliverNotice.html'
                },
                'noticeCommon@sys.deliverNotice': {
                    templateUrl: 'views/more/noticeCommon.html'
                }
            },
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('datePicker')
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/more/deliverNoticeCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('sys.modifyNotice', {
            url: "/modifyNotice/:id",
            data: {pageTitle: '修改公告'},
            views: {
                '': {
                    templateUrl: 'views/more/modifyNotice.html'
                },
                'noticeEditCommon@sys.modifyNotice': {
                    templateUrl: 'views/more/noticeEditCommon.html'
                }
            },
            //controller: "modifyNoticeCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('summernote');
                    $ocLazyLoad.load('angular-summernote');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/plugins/summernote/summernote.min.js','js/plugins/summernote/angular-summernote.min.js', 'css/plugins/summernote/summernote.css',
                            'js/controllers/more/modifyNoticeCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('sys.addNotice', {
                url: "/addNotice",
                templateUrl: "views/icons.html",
                data: {pageTitle: '新增公告'},
                views: {
                    '': {
                        templateUrl: 'views/more/addNotice.html'
                    },
                    'noticeEditCommon@sys.addNotice': {
                        templateUrl: 'views/more/noticeEditCommon.html'
                    }
                },
                //controller: "addNoticeCtrl",
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('fileUpload');
                        $ocLazyLoad.load('localytics.directives');
                        $ocLazyLoad.load('summernote');
                        $ocLazyLoad.load('angular-summernote');
                        $ocLazyLoad.load('My97DatePicker');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/plugins/summernote/summernote.min.js','js/plugins/summernote/angular-summernote.min.js', 'css/plugins/summernote/summernote.css',
                                'js/controllers/more/addNoticeCtrl.js?ver='+verNo]
                        });
                    }]
                }
            }
        )
        .state('sys.queryNotice', {
            url: "/queryNotice",
            templateUrl: "views/more/queryNotice.html",
            data: {pageTitle: '查询公告'},
            //controller: "queryNoticeCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/more/queryNoticeCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('sys.addBanner', {
            url: "/addBanner",
            views: {
                "": {templateUrl: "views/more/addBanner.html"},
                "bannerCommon@sys.addBanner": {templateUrl: "views/more/bannerCommon.html"}
            },
            data: {pageTitle: '新增App banner'},
            //controller: "addBannerCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('localytics.directives');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/more/addBannerCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('sys.queryBanner', {
            url: "/queryBanner",
            templateUrl: "views/more/queryBanner.html",
            data: {pageTitle: 'App banner查询'},
            //controller: "queryBannerCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/more/queryBannerCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('sys.bannerDetail', {
            url: "/bannerDetail/:id",
            templateUrl: "views/more/bannerDetail.html",
            data: {pageTitle: 'App banner详情'},
            //controller: "bannerDetailCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('localytics.directives');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/more/bannerDetailCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('sys.modifyBanner', {
            url: "/modifyBanner/:id",
            views: {
                "": {templateUrl: "views/more/modifyBanner.html"},
                "bannerCommon@sys.modifyBanner": {templateUrl: "views/more/bannerCommon.html"}
            },
            data: {pageTitle: 'App 修改banner'},
            //controller: "modifyBannerCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('localytics.directives');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/more/modifyBannerCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('sys.queryAppInfo', {
            url: "/queryAppInfo",
            //controller: 'queryAppInfoCtrl',
            templateUrl: "views/more/queryAppInfo.html",
            data: {pageTitle: '查询代理商App关于软件'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/more/queryAppInfoCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('sys.appInfoDetail', {
            url: '/appInfo/selectDetail/:id',
            //controller: 'appInfoDetailCtrl',
            templateUrl: 'views/more/appInfoDetail.html',
            data: {pageTitle: '代理商App关于软件详情'},
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/more/appInfoDetailCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('sys.addAppInfo', {
            url: "/addAppInfo",
            //controller: 'addAppInfoCtrl',
            views: {
                "": {templateUrl: "views/more/addAppInfo.html"},
                "appInfoCommon@sys.addAppInfo": {templateUrl: "views/more/appInfoCommon.html"}
            },
            data: {pageTitle: '新增代理商App关于软件设置'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('localytics.directives');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/more/addAppInfoCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('sys.modifyAppInfo', {
            url: "/modifyAppInfo/:id",
            //controller: 'modifyAppInfoCtrl',
            views: {
                "": {templateUrl: "views/more/modifyAppInfo.html"},
                "appInfoCommon@sys.modifyAppInfo": {templateUrl: "views/more/appInfoCommon.html"}
            },
            data: {pageTitle: '修改代理商App关于软件设置'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('localytics.directives');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/more/modifyAppInfoCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('sys.userFeedback', {
            url: "/userFeedback",
            templateUrl: "views/more/userFeedbackQuery.html",
            data: {pageTitle: '用户反馈问题查询'},
            resolve: {
                loadPlugin: function($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/more/userFeedbackQueryCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('sys.userFeedbackDetail', {
            url: "/userFeedbackDetail/:id",
            templateUrl: "views/more/userFeedbackDetail.html",
            data: {pageTitle: '用户反馈问题详情'},
            resolve: {
                loadPlugin: function($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('fancybox');

                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/more/userFeedbackDetailCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('sys.logs', {
            url: "/managerLog",
            templateUrl: "views/more/sysLog.html",
            data: {pageTitle: '日志管理'},
            //controller: 'sysLogCtrl',
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('datePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/more/sysLogCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('sys.agentLog', {
            url: "/agentLog",
            templateUrl: "views/more/agentLog.html",
            data: {pageTitle: '日志管理'},
            controller:'agentLogCtrl',
            resolve:{
                loadPlugin: function($ocLazyLoad) {
                    $ocLazyLoad.load('datePicker');
                },
                deps: ['$ocLazyLoad', function($ocLazyLoad){
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/more/agentLogCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('sys.calendar', {
            url: "/calendar",
            templateUrl: "views/more/calendar.html",
            //controller: 'calendarCtrl',
            data: {pageTitle: '日历'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/more/calendarCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('sys.msg', {
            url: "/msg",
            templateUrl: "views/msg/msg.html",
            //controller: 'msgCtrl',
            data: {pageTitle: '提示语'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/msg/msg.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('sys.msgAdd', {
            url: "/msgAdd",

            views: {
                "": {templateUrl: "views/msg/msgAdd.html"},
                "msgcommon@sys.msgAdd": {templateUrl: "views/msg/msgcommon.html"}
                },

            //controller: 'msgAddCtrl',
            data: {pageTitle: '提示语新增'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: [' js/controllers/msg/msgAdd.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('sys.msgDetail', {
            url: '/sys/msgDetail/:id',
            templateUrl: "views/msg/msgDetail.html",
            data: {pageTitle: '提示语详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/msg/msgDetailCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        
         .state('sys.msgUpdate', {
            url: "/msgUpdate/:id",

            views: {
                "": {templateUrl: "views/msg/msgUpdate.html"},
                "msgcommon@sys.msgUpdate": {templateUrl: "views/msg/msgcommon.html"}
                },

            //controller: 'msgUpdateCtrl',
            data: {pageTitle: '提示语新增'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: [' js/controllers/msg/msgUpdate.js?ver=' + verNo]
                    });
                }]
            }
        })
        
        .state('sys.agentShareTask', {
            url: "/agentShareTask",
            templateUrl: "views/more/agentShareTask.html",
            data: {pageTitle: '代理商分润任务'},
            //controller: "agentShareTaskCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/more/agentShareTaskCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        /*定时任务监控*/
        .state('sys.timedTask', {
            url: "/timedTask",
            templateUrl: "views/more/timedTask/timedTaskQuery.html",
            data: {pageTitle: '定时任务监控'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ngGrid');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/more/timedTask/timedTaskQueryCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('sys.detailTimedTask', {
            url: "/detailTimedTask/:id",
            templateUrl: "views/more/timedTask/detailTimedTask.html",
            data: {pageTitle: '定时任务监控详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/more/timedTask/detailTimedTaskCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('sys.editTimedTask', {
            url: "/editTimedTask/:id",
            templateUrl: "views/more/timedTask/editTimedTask.html",
            data: {pageTitle: '定时任务监控编辑'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/more/timedTask/detailTimedTaskCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        /*定时任务监控预警人员管理*/
        .state('sys.taskWarningPeople', {
            url: "/taskWarningPeople",
            data: {pageTitle: '定时任务监控预警人员管理'},
            views: {
                '': {
                    templateUrl: 'views/org/warningPeople/taskWarningPeople.html'
                },
                'warningPeopleCenter@sys.taskWarningPeople': {
                    templateUrl: 'views/org/warningPeople/warningPeopleCenter.html'
                }
            },
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('infinity-chosen')
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/org/warningPeople/taskWarningPeopleCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('sys.taskEditWarningPeople', {
            url: "/taskEditWarningPeople/:id",
            data: {pageTitle: '设置任务'},
            templateUrl: "views/org/warningPeople/taskEditWarningPeople.html",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('infinity-chosen')
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/org/warningPeople/taskEditWarningPeopleCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('sys.appInfo', {
            url: "/appInfo",
            templateUrl: "views/more/appInfo.html",
            data: {pageTitle: 'APP二维码查询'},
            //controller: 'appInfoCtrl',
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('ngGrid')
                    $ocLazyLoad.load('datePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/more/appInfoCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('sys.appInfoQueryDetail', {
            url: "/appInfoQueryDetail/:id",
            templateUrl: "views/more/appInfoQueryDetail.html",
            data: {pageTitle: 'APP二维详情'},
            //controller: 'appInfoQueryDetailCtrl',
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('ngGrid')
                    $ocLazyLoad.load('datePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/more/appInfoQueryDetailCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('sys.appInfoQueryAdd', {
            url: "/appInfoQueryAdd",
            templateUrl: "views/more/appInfoQueryAdd.html",
            data: {pageTitle: 'APP二维新增'},
            //controller: 'appInfoQueryAddCtrl',
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('localytics.directives');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/more/appInfoQueryAddCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('sys.appInfoQueryUp', {
            url: "/appInfoQueryUp/:id",
            templateUrl: "views/more/appInfoQueryUp.html",
            data: {pageTitle: 'APP二维修改'},
            //controller: 'appInfoQueryUpCtrl',
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('localytics.directives');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/more/appInfoQueryUpCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('sys.appMobileVerInfo', {
            url: "/appMobileVerInfo/:id",
            templateUrl: "views/more/appMobileVerInfo.html",
            data: {pageTitle: 'APP二维历史记录查询'},
            //controller: 'appMobileVerInfoCtrl',
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('ngGrid')
                    $ocLazyLoad.load('datePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/more/appMobileVerInfoCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('sys.appMobileVerInfoDetail', {
            url: "/appMobileVerInfoDetail/:id",
            templateUrl: "views/more/appMobileVerInfoDetail.html",
            data: {pageTitle: 'APP二维历史记录查询详情'},
            //controller: 'appMobileVerInfoDetailCtrl',
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/more/appMobileVerInfoDetailCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('sys.appMobileVerInfoAddOrUp', {
            url: "/appMobileVerInfoAddOrUp/:id/:appNo/:appName",
            templateUrl: "views/more/appMobileVerInfoAddOrUp.html",
            data: {pageTitle: 'APP二维历史记录新增和修改'},
            //controller: 'appMobileVerInfoAddOrUpCtrl',
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('fileUpload');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/more/appMobileVerInfoAddOrUpCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        /*预警事件查询*/
        .state('sys.allWarningEvents', {
            url: "/allWarningEvents",
            templateUrl: "views/org/warningPeople/queryWarningEvents.html",
            data: {pageTitle: '预警事件查询'},
            //controller: 'warningEventsCtrl',
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('infinity-chosen')
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/org/warningPeople/warningEventsCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('sys.managerSms', {
            url: '/managerSms',
            templateUrl: "views/more/managerSms.html",
            data: {pageTitle: '短信通道管理'},
            //controller: 'appInfoCtrl',
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('ngGrid')
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/more/managerSmsCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('sys.sdbConfig', {
            url: "/addSdbConfig",
            templateUrl: 'views/more/addSdbConfig.html',
            data: {pageTitle: '盛代宝配置'},
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
                        files: ['js/controllers/more/addSdbConfigCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('sys.merWarningConfig', {
            url: "/queryMerWarningConfig",
            templateUrl: "views/more/queryMerWarningConfig.html",
            data: {pageTitle: '商户预警配置'},
            //controller: "queryNoticeCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('fancybox');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('fileUpload');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/more/queryMerWarningConfigCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('sys.exportManage', {
            url: "/exportManage",
            templateUrl: "views/more/exportManage/exportManageQuery.html",
            data: {pageTitle: '导出下载管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/more/exportManage/exportManageQueryCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })


        /*用户中心*/
        .state('user', {
            abstract: true,
            url: "/user",
            templateUrl: "views/common/content.html",
        })
        .state('user.customServiceProblemManage', {
            url: "/user/customService",
            templateUrl: "views/user/customServiceProblem.html",
            data: {pageTitle: '客服问题管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('summernote');
                    $ocLazyLoad.load('angular-summernote');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/plugins/summernote/summernote.min.js','js/plugins/summernote/angular-summernote.min.js', 'css/plugins/summernote/summernote.css',
                            'js/controllers/user/customServiceProblemCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('user.user', {
            url: "/user/user",
            views: {
                "": {templateUrl: "views/user/user.html"},
                "modalRole@user.user": {templateUrl: "views/user/modalRole.html"},
                "modalRight@user.user": {templateUrl: "views/user/modalRight.html"}
            },
            data: {pageTitle: '用户管理'},
            //controller: 'userCtrl',
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/user/userCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('user.userAddMenu', {
            url: "/user/userAddMenu:id/:userName",
            templateUrl: "views/user/userAddMenu.html",
            data: {pageTitle: '权限添加菜单'},
            //controller: 'userAddMenuCtrl',
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/user/userAddMenuCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('user.role', {
            url: "/user/role",
            views: {
                "": {templateUrl: "views/user/role.html"},
                "modalUser@user.role": {templateUrl: "views/user/modalUser.html"},
                "modalRight@user.role": {templateUrl: "views/user/modalRight.html"}
            },
            data: {pageTitle: "角色管理"},
            //controller: "roleCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ngJsTree');
                },
                deps: ["$ocLazyLoad", function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: "inspinia",
                        files: ["js/controllers/user/roleCtrl2.js"]
                    });
                }]
            }
        })
        .state('user.right', {
            url: "/user/right",
            views: {
                "": {templateUrl: "views/user/right.html"},
                "modalUser@user.right": {templateUrl: "views/user/modalUser.html"},
                "modalRole@user.right": {templateUrl: "views/user/modalRole.html"},
                "modalMenu@user.right": {templateUrl: "views/user/modalMenu.html"}
            },
            data: {pageTitle: "权限管理"},
            //controller: "rightCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ["$ocLazyLoad", function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: "inspinia",
                        files: ["js/controllers/user/rightCtrl.js?ver=" + verNo]
                    });
                }]
            }
        })
        .state('user.rightAddMenu', {
            url: "/user/rightAddMenu:id/:rightName",
            templateUrl: "views/user/role2.html",
            data: {pageTitle: '权限添加菜单'},
            //controller: 'roleCtrl2',
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ngJsTree');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/user/roleCtrl2.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('user.menu', {
            url: "/user/menu",
            views: {
                "": {templateUrl: "views/user/menu2.html"},
                "modalRight@user.menu": {templateUrl: "views/user/modalRight.html"}
            },
            data: {pageTitle: '菜单管理'},
            //controller: 'menuCtrl',
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ngJsTree');
                    $ocLazyLoad.load('ui-switch');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/user/menuCtrl2.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('user.menuAddRight', {
            url: "/user/menuAddRight:id",
            templateUrl: "views/user/menuAddRight.html",
            data: {pageTitle: '菜单添加权限'},
            //controller: 'menuAddRightCtrl',
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/user/menuAddRightCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('user.dictionary', {
            url: "/user/dictionary",
            templateUrl: "views/user/dictionary.html",
            data: {pageTitle: "数据字典"},
            //controller: "dictionaryCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ["$ocLazyLoad", function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: "inspinia",
                        files: ["js/controllers/user/dictionaryCtrl.js?ver=" + verNo]
                    });
                }]
            }
        })
        .state('user.userTotal', {
            url: "/userTotal",
            templateUrl: "views/user/userTotal.html",
            data: {pageTitle: '用户统计'},
            //controller: "userTotalCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives')
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/user/userTotalCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('user.addUser', {
            url: "/addUser",
            templateUrl: "views/user/addUser.html",
            data: {pageTitle: '新增用户'},
            //controller: "addUserCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives')
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ui.select');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/user/addUserCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('user.sensitive', {
            url: "/sensitive",
            templateUrl: "views/user/sensitiveWords/querySensitiveWords.html",
            data: {pageTitle: '敏感词查询'},
            //controller: "querySensitiveWordsCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/user/sensitiveWords/querySensitiveWordsCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('user.batchSensitive', {
            url: "/batchSensitive",
            templateUrl: "views/user/sensitiveWords/leadInSensitiveWords.html",
            data: {pageTitle: '敏感词导入'},
            //controller: "leadInSensitiveWordsCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/user/sensitiveWords/leadInSensitiveWordsCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        /*销售管理*/
        .state('sale', {
            abstract: true,
            url: "/sale",
            templateUrl: "views/common/content.html",
        })
        .state('sale.merchantSelect', {
            url: "/merchantSelect",
            templateUrl: "views/sale/merchantQuery.html",
            data: {pageTitle: '查询商户'},
            //controller: "saleMerchantQueryCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('ui.select');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/sale/merchantQueryCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('sale.queryMerDetail', {
            url: "/queryMerDetail/:mertId",
            templateUrl: "views/sale/merchantQueryDetail.html",
            data: {pageTitle: '商户详情'},
            //controller: "merchantDetailCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives')
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/sale/merchantDetailCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        /*销售交易查询*/
        .state('sale.transSelect', {
            url: "/transSelect",
            templateUrl: "views/sale/tradeQuery.html",
            data: {pageTitle: '交易查询'},
            //controller: "saleQueryTradeCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/sale/queryTradeCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('sale.tradeQueryDetail', {
            url: "/tradeQueryDetail/:id/:val",
            templateUrl: "views/sale/tradeQueryDetail.html",
            data: {pageTitle: '交易详细信息'},
            //controller: "queryTradeDetailCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('ui.select');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/sale/queryTradeDetailCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        /*销售代理商查询*/
        .state('sale.agentSelect', {
            url: "/agentSelect",
            templateUrl: "views/sale/queryAgent.html",
            //controller: "saleQueryAgentCtrl",
            data: {pageTitle: '查询代理商'},
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/sale/queryAgentCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('sale.agentDetail', {
            url: '/agentDetail/:id/:teamId',
            views: {
                '': {templateUrl: 'views/sale/agentDetail.html'},
                'agentDetailBaseTwo@sale.agentDetail': {templateUrl: "views/sale/agentDetailBaseTwo.html"}
            },
            //controller: 'agentDetailCtrl',
            data: {pageTitle: '代理商详情'},
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/sale/agentDetailCtrl.js?ver=' + verNo]
                    })
                }]
            }
        })
        //信用卡奖金配置
        .state('superBank.creditcardBonus', {
            url: '/superBank/getCreditCardConf',
            templateUrl: "views/superBank/creditcardBonus.html",
            //controller: 'creditcardBonusCtrl',
            data: {pageTitle: '信用卡奖励表'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    //$ocLazyLoad.load('datePicker');
                    //$ocLazyLoad.load('localytics.directives');
                    //$ocLazyLoad.load('ngGrid');
                    //$ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('infinity-chosen');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/creditcardBonusCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        //分润比例配置
        .state('superBank.orgProfitConf', {
            url: '/superBank/getOrgProfitConf',
            templateUrl: "views/superBank/orgProfitConf.html",
            //controller: 'orgProfitConfCtrl',
            data: {pageTitle: '代理分润配置表'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('infinity-chosen');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/orgProfitConfCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        //贷款奖金管理
        .state('superBank.loanBonus', {
            url: '/superBank/loanConfList',
            templateUrl: "views/superBank/loanBonus.html",
            //controller: 'loanBonusCtrl',
            data: {pageTitle: '贷款总奖金计算表'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/loanBonusCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        //保单总奖金管理
        .state('superBank.insuranceBonus', {
            url: '/superBank/insuranceBonus',
            templateUrl: "views/superBank/insuranceBonus.html",
            //controller: 'insuranceBonusCtrl',
            data: {pageTitle: '保单总奖金管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/insuranceBonusCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        //积分兑换总奖金管理
        .state('superBank.superExchangeBonus', {
            url: '/superBank/superExchangeBonus',
            templateUrl: "views/superBank/superExchange.html",
            //controller: 'superExchangeCtrl',
            data: {pageTitle: '积分兑换总奖金管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/superExchangeCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        //彩票组织奖金管理
        .state('superBank.lotteryBonus', {
            url: '/superBank/lotteryConfList',
            templateUrl: "views/superBank/lotteryBonus.html",
            //controller: 'lotteryBonusCtrl',
            data: {pageTitle: '彩票总奖金管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('infinity-chosen');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/lotteryBonusCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        //彩票订单导入
        .state('superBank.lotteryImportRecords', {
            url: "/superBank/lotteryImportList",
            templateUrl: "views/superBank/lotteryImportRecords.html",
            data: {pageTitle: '彩票代购订单导入管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('fileUpload');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/lotteryImportRecordsCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        //公告管理
        .state('superBank.noticeList', {
            url: '/superBank/noticeList',
            templateUrl: "views/superBank/noticeList.html",
            //controller: 'noticeCtrl',
            data: {pageTitle: '公告管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('ui-switch');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/noticeCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        //公告详情
        .state('superBank.noticeDetail', {
            url: '/superBank/noticeDetail/:id',
            templateUrl: "views/superBank/noticeDetail.html",
            data: {pageTitle: '公告详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('summernote');
                    $ocLazyLoad.load('angular-summernote');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/noticeDetailCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        //新增公告
        .state('superBank.addNotice', {
            url: '/superBank/addNotice',
            templateUrl: "views/superBank/addNotice.html",
            data: {pageTitle: '新增公告'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('summernote');
                    $ocLazyLoad.load('angular-summernote');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/addNoticeCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        //修改公告
        .state('superBank.updateNotice', {
            url: '/superBank/updateNotice/:id',
            templateUrl: "views/superBank/updateNotice.html",
            data: {pageTitle: '修改公告'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('summernote');
                    $ocLazyLoad.load('angular-summernote');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/updateNoticeCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        //广告管理
        .state('superBank.adManage', {
            url: '/superBank/adManage',
            templateUrl: "views/superBank/adManage.html",
            //controller: 'adCtrl',
            data: {pageTitle: '广告管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('ngGrid');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/adCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        //新增广告
        .state('superBank.addAd', {
            url: '/superBank/addAd',
            templateUrl: "views/superBank/addAd.html",
            data: {pageTitle: '广告新增'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('infinity-chosen');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/addAdCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        //广告详情
        .state('superBank.adDetail', {
            url: '/superBank/adDetail/:id',
            templateUrl: "views/superBank/adDetail.html",
            //controller: 'adDetailCtrl',
            data: {pageTitle: '广告详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/adDetailCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        //广告修改
        .state('superBank.updAd', {
            url: '/superBank/updAd/:id',
            templateUrl: "views/superBank/updAd.html",
            //controller: 'updAdCtrl',
            data: {pageTitle: '广告修改'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/updAdCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        //彩票查询
        .state('superBank.lotteryOrder', {
            url: '/superBank/lotteryOrder',
            templateUrl: "views/superBank/lotteryOrder.html",
            //controller: 'lotteryOrderCtrl',
            data: {pageTitle: '彩票订单管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/lotteryOrderCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('superBank.lotteryOrderDetail', {
            url: '/superBank/lotteryOrderDetail/:orderNo',
            templateUrl: "views/superBank/lotteryOrderDetail.html",
            //controller: 'lotteryOrderDetailCtrl',
            data: {pageTitle: '彩票订单详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/lotteryOrderDetailCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        //排行榜管理
        .state('superBank.rankingRecord', {
            url: '/superBank/rankingRecord',
            templateUrl: "views/superBank/rankingRecord.html",
            //controller: 'rankingRecordCtrl',
            data: {pageTitle: '排行榜管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/rankingRecordCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        //排行榜规则管理
        .state('superBank.rankingRule', {
            url: "/superBank/rankingRuleList",
            templateUrl: "views/superBank/rankingRule.html",
            data: {pageTitle: '规则管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('fileUpload');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/rankingRuleCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        //排行榜明细
        .state('superBank.rankingRecordDetail', {
            url: "/superBank/rankingRecordDetail/:recordId",
            templateUrl: "views/superBank/rankingRecordDetail.html",
            data: {pageTitle: '排行榜明细'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/rankingRecordDetailCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        //查看排行榜明细
        .state('superBank.findRankingRecordDetail', {
            url: "/superBank/findRankingRecordDetail/:recordId",
            templateUrl: "views/superBank/findRankingRecordDetail.html",
            data: {pageTitle: '排行榜详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/findRankingRecordDetailCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('superBank.rankingRuleLevel', {
            url: '/superBank/rankingRuleLevel/:id/:operate',
            templateUrl: "views/superBank/rankingRuleLevel.html",
            //controller: 'rankingRuleLevelCtrl',
            data: {pageTitle: '排行榜规则详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/rankingRuleLevelCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        //奖金发放记录
        .state('superBank.rankingPushRecord', {
            url: "/superBank/rankingPushRecord",
            templateUrl: "views/superBank/rankingPushRecord.html",
            data: {
                pageTitle: '用户奖金发放记录'
            },
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: [
                    '$ocLazyLoad',
                    function ($ocLazyLoad) {
                        return $ocLazyLoad
                            .load({
                                name: 'inspinia',
                                files: ['js/controllers/superBank/rankingPushRecordCtrl.js?ver='
                                + verNo]
                            });
                    }]
            }
        })
        .state('superBank.sensitive', {
            url: "superBank/sensitive",
            templateUrl: "views/superBank/querySensitiveWords.html",
            data: {pageTitle: '敏感词查询'},
            //controller: "querySensitiveWordsCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/querySensitiveWordsCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('superBank.batchSensitive', {
            url: "superBank/batchSensitive",
            templateUrl: "views/superBank/leadInSensitiveWords.html",
            data: {pageTitle: '敏感词导入'},
            //controller: "leadInSensitiveWordsCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/leadInSensitiveWordsCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        //征信总奖金管理
        .state('superBank.creditTotalBonus', {
            url: '/superBank/creditTotalBonus',
            templateUrl: "views/superBank/creditTotalBonus.html",
            //controller: 'creditTotalBonusCtrl',
            data: {pageTitle: '征信总奖金管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/creditTotalBonusCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        /*征信订单查询*/
        .state('superBank.inquiryOrder', {
            url: "/superBank/inquiryOrder",
            templateUrl: "views/superBank/creditOrderInquiry.html",
            data: {pageTitle: '征信订单查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/red/map.js', 'js/controllers/superBank/creditOrderInquiryCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        /*征信订单详情界面*/
        .state('superBank.inquiryOrderDetail', {
            url: "/superBank/inquiryOrderDetail/:orderNo",
            templateUrl: 'views/superBank/inquiryOrderDetail.html',
            data: {pageTitle: '征信订单详情'},
            resolve: {
                // loadPlugin: function ($ocLazyLoad) {
                // },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/inquiryOrderDetailCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        //违章代缴订单查询
        .state('superBank.carOrder', {
            url: '/superBank/findCarOrder',
            templateUrl: "views/superBank/carOrder.html",
            //controller: 'carOrderCtrl',
            data: {pageTitle: '违章订单查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/red/map.js', 'js/controllers/superBank/carOrderCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        //违章代缴订单详情
        .state('superBank.carOrderDetail', {
            url: "/superBank/carOrderDetail/:orderNo",
            templateUrl: "views/superBank/carOrderDetail.html",
            data: {pageTitle: '违章代缴订单详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/carOrderDetailCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        //违章代缴订单奖金配置
        .state('superBank.carOrderProfitConf', {
            url: '/superBank/carOrderProfitConf',
            templateUrl: "views/superBank/carOrderProfitConf.html",
            //controller: 'carOrderProfitConfCtrl',
            data: {pageTitle: '违章订单奖金配置'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/carOrderProfitConfCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        /************** 保单对账管理 start ***************/
        .state('policy', {
            abstract: true,
            url: "/policy",
            templateUrl: "views/common/content.html",
        })
        /*对账文件上传*/
        .state('policy.billAdd', {
            url: "/billAdd",
            templateUrl: "views/capitalInsurance/policy/billAdd.html",
            data: {pageTitle: '对账文件上传'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('fileUpload');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/capitalInsurance/policy/billAddCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        /*对账信息查询*/
        .state('policy.billQuery', {
            url: "/billQuery",
            templateUrl: "views/capitalInsurance/policy/billQuery.html",
            data: {pageTitle: '对账信息查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/capitalInsurance/policy/billQueryCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        /*对账详细信息查询*/
        .state('policy.billEneryQuery', {
            url: "/billEneryQuery/:batchNo",
            templateUrl: "views/capitalInsurance/policy/billEneryQuery.html",
            data: {pageTitle: '对账详细信息查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/capitalInsurance/policy/billEneryQueryCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        /*对账详情*/
        .state('policy.billEneryDetail', {
            url: "/billEneryDetail/:id",
            templateUrl: 'views/capitalInsurance/policy/billEneryDetail.html',
            data: {pageTitle: '对账详情'},
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/capitalInsurance/policy/billEneryDetailCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        /*代理分润月度报表*/
        .state('policy.billShareReport', {
            url: "/billShareReport",
            templateUrl: "views/capitalInsurance/policy/billShareReport.html",
            data: {pageTitle: '代理分润月度报表'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/capitalInsurance/policy/billShareReportCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        /************** 保单对账管理 end******************/
        /************** 账户资金损失险 start ***************/
        .state('safe', {
            abstract: true,
            url: "/safe",
            templateUrl: "views/common/content.html",
        })
        .state('safe.safeConfig', {
            url: "/safeConfig",
            templateUrl: 'views/capitalInsurance/config/safeConfigQuery.html',
            data: {pageTitle: '资金险设置'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/capitalInsurance/config/safeConfigQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('safe.safeConfigEdit', {
            url: "/safeConfigEdit/:id",
            templateUrl: 'views/capitalInsurance/config/safeConfigEdit.html',
            data: {pageTitle: '资金险设置修改'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/capitalInsurance/config/safeConfigEditCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('safe.safeConfigDetail', {
            url: "/safeConfigDetail/:id",
            templateUrl: 'views/capitalInsurance/config/safeConfigDetail.html',
            data: {pageTitle: '资金险设置详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/capitalInsurance/config/safeConfigDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('safe.safeOrder', {
            url: "/safeOrder",
            templateUrl: 'views/capitalInsurance/order/safeOrderQuery.html',
            data: {pageTitle: '保险订单查询'},
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
                        files: ['js/controllers/capitalInsurance/order/safeOrderQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('safe.safeOrderDetail', {
            url: "/safeOrderDetail/:id",
            templateUrl: 'views/capitalInsurance/order/safeOrderDetail.html',
            data: {pageTitle: '保险订单详情'},
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
                        files: ['js/controllers/capitalInsurance/order/safeOrderDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
    /************** 账户资金损失险 end ***************/

     /********短信发送管理 start *************/
        /*短信发送管理*/
        .state('cusSms', {
            abstract: true,
            url: "/cusSms",
            templateUrl: "views/common/content.html",
        })
        .state('cusSms.smsTemplate', {
            url: "/smsTemplate",
            templateUrl: 'views/cusSmsManage/smsTemplate/smsTemplateQuery.html',
            data: {pageTitle: '短信模板'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/cusSmsManage/smsTemplate/smsTemplateQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('cusSms.smsTemplateSend', {
            url: "/smsTemplateSend/:id",
            templateUrl: 'views/cusSmsManage/smsTemplate/smsTemplateSend.html',
            data: {pageTitle: '短信模板发送'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/cusSmsManage/smsTemplate/smsTemplateSendCtrl.js?ver='+verNo]
                    });
                }]
            }
        })

        .state('cusSms.smsRecord', {
            url: "/smsRecord",
            templateUrl: 'views/cusSmsManage/smsRecord/smsRecordQuery.html',
            data: {pageTitle: '发送记录'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/cusSmsManage/smsRecord/smsRecordQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('cusSms.smsRecordDetail', {
            url: "/smsRecordDetail/:id",
            templateUrl: 'views/cusSmsManage/smsRecord/smsRecordDetail.html',
            data: {pageTitle: '发送记录详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/cusSmsManage/smsRecord/smsRecordDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
    /************** 短信发送管理 end ***************/
}