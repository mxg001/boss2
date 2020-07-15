function initSystem(stateProvider){
	    /*欢迎页面*/
		stateProvider.state('welcome', {
	        abstract: true,
	        url: "/welcome",
	        templateUrl: "views/common/content.html",
	    })
	    .state('welcome.index', {
	        url: "/index",
	        data: {pageTitle: '欢迎登陆'},
	        templateUrl: "views/welcome.html",
          resolve: {
              deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                  return $ocLazyLoad.load({
                      name: 'inspinia',
                      files: ['js/controllers/workOrder/welcomeCtrl.js?ver=' + verNo]
                  });
              }]
          }
	    })


	    //-------------功能管理-------start--------------------------------------------
        /*功能管理*/
        .state('func', {
            abstract: true,
            url: "/func",
            templateUrl: "views/common/content.html",
        })
        .state('func.autoCheckRule', {
            url: "/autoCheckRule",
            templateUrl: "views/func/autoCheckRule.html",
            data: {pageTitle: '自动审件控制'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('ngGrid');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/autoCheckRule/autoCheckRuleCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('func.switch', {
            url: "/switch",
            templateUrl: "views/func/switch.html",
            data: {pageTitle: '功能管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives')
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/autoCheckRule/switchCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('func.happySendActivity', {
            url: "/happySendActivity",
            templateUrl: "views/func/happySendActivitySetUp.html",
            data: {pageTitle: '欢乐送活动'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives')
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/autoCheckRule/happySendActivitySetUpCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('func.happyReturn', {
        	url: "/happyReturn",
        	templateUrl: "views/func/happyReturnSetUp.html",
        	data: {pageTitle: '欢乐返活动'},
        	resolve: {
        		loadPlugin: function ($ocLazyLoad) {
        			$ocLazyLoad.load('localytics.directives');
        			$ocLazyLoad.load('ui.select');
        			$ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('infinity-chosen');
        		},
        		deps: ['$ocLazyLoad', function ($ocLazyLoad) {
        			return $ocLazyLoad.load({
        				name: 'inspinia',
        				files: ['js/controllers/autoCheckRule/happyReturnSetUpCtrl.js?ver='+verNo]
        			});
        		}]
        	}
        })

            .state('func.loanFinancing', {
                url: "/loanFinancing",
                templateUrl: "views/func/loanFinancing/loanFinancingQuery.html",
                data: {pageTitle: '贷款理财配置'},
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('localytics.directives');
                        $ocLazyLoad.load('oitozero.ngSweetAlert');
                        $ocLazyLoad.load('ui.select');
                        $ocLazyLoad.load('My97DatePicker');
                        $ocLazyLoad.load('fancybox');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/controllers/func/loanFinancing/loanFinancingQueryCtrl.js?ver='+verNo]
                        });
                    }]
                }
            })
            .state('func.loanFinancingAdd', {
                url: "/loanFinancingAdd",
                templateUrl: "views/func/loanFinancing/loanFinancingAdd.html",
                data: {pageTitle: '贷款理财配置新增'},
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
                            files: ['js/controllers/func/loanFinancing/loanFinancingAddCtrl.js?ver='+verNo]
                        });
                    }]
                }
            })
            .state('func.loanFinancingEdit', {
                url: "/loanFinancingEdit/:id",
                templateUrl: "views/func/loanFinancing/loanFinancingEdit.html",
                data: {pageTitle: '贷款理财配置修改'},
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
                            files: ['js/controllers/func/loanFinancing/loanFinancingEditCtrl.js?ver='+verNo]
                        });
                    }]
                }
            })
            .state('func.loanFinancingDetail', {
                url: "/loanFinancingDetail/:id",
                templateUrl: "views/func/loanFinancing/loanFinancingDetail.html",
                data: {pageTitle: '贷款理财配置详情'},
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
                            files: ['js/controllers/func/loanFinancing/loanFinancingEditCtrl.js?ver='+verNo]
                        });
                    }]
                }
            })
        //-------------功能管理-------end--------------------------------------------

        .state('activity.selectHappyReturnRewardActivity', {
            url: "/selectHappyReturnRewardActivity",
            templateUrl: "views/func/selectHappyReturnRewardActivity.html",
            data: {pageTitle: '欢乐返活动个性化配置'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives')
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/autoCheckRule/selectHappyReturnRewardActivityCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('activity.selectHlfActivityMerchant', {
            url: "/selectHlfActivityMerchant",
            templateUrl: "views/func/selectHlfActivityMerchant.html",
            data: {pageTitle: '活跃商户活动配置'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives')
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/autoCheckRule/selectHlfActivityMerchantCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
            .state('activity.selectHlfActivityAgent', {
                url: "/selectHlfActivityAgent",
                templateUrl: "views/func/selectHlfActivityAgent.html",
                data: {pageTitle: '代理商奖励活动'},
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('localytics.directives')
                        $ocLazyLoad.load('ui.select');
                        $ocLazyLoad.load('oitozero.ngSweetAlert');
                        $ocLazyLoad.load('My97DatePicker');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/controllers/autoCheckRule/selectHlfActivityAgentCtrl.js?ver='+verNo]
                        });
                    }]
                }
            })
            .state('activity.selectXhlfSmart', {
                url: "/selectXhlfSmart",
                templateUrl: "views/func/selectXhlfSmart.html",
                data: {pageTitle: '新欢乐送智能版活动'},
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('localytics.directives')
                        $ocLazyLoad.load('ui.select');
                        $ocLazyLoad.load('oitozero.ngSweetAlert');
                        $ocLazyLoad.load('My97DatePicker');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/controllers/autoCheckRule/selectXhlfSmartCtrl.js?ver='+verNo]
                        });
                    }]
                }
            })
            .state('activity.selectHlfGroup', {
                url: "/selectHlfGroup",
                templateUrl: "views/func/selectHlfGroup.html",
                data: {pageTitle: '分组管理'},
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('localytics.directives')
                        $ocLazyLoad.load('ui.select');
                        $ocLazyLoad.load('oitozero.ngSweetAlert');
                        $ocLazyLoad.load('My97DatePicker');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/controllers/autoCheckRule/selectHlfGroupCtrl.js?ver='+verNo]
                        });
                    }]
                }
            })
            .state('activity.selectHappyReturnAgentActivity', {
            url: "/selectHappyReturnAgentActivity/:activityId",
            templateUrl: "views/func/selectHappyReturnAgentRewardActivity.html",
            data: {pageTitle: '代理商管理'},
            //controller: "selectHappyReturnAgentRewardActivityCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives')
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/autoCheckRule/selectHappyReturnAgentRewardActivityCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('activity.addHappyReturnAgentActivity', {
            url: "/addHappyReturnAgentActivity/:activityId",
            templateUrl: "views/func/addHappyReturnAgentActivity.html",
            data: {pageTitle: '代理商管理'},
            //controller: "addHappyReturnAgentActivityCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives')
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/autoCheckRule/addHappyReturnAgentActivityCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        //========================================================================
        .state('func.happyReturnType', {
            url: "/happyReturnType",
            templateUrl: "views/func/happyReturnType.html",
            data: {pageTitle: '欢乐返子类型'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives')
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('infinity-chosen');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/autoCheckRule/happyReturnTypeCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        //欢乐返子类型详情
        .state('func.happyReturnTypeDetail',{
            url: '/happyReturnTypeDetail/:id',
            views: {
                '':{templateUrl:'views/func/happyReturnDetail.html'}
            },
            data: {pageTitle: '欢乐返子类型详情'},
            resolve: {
                deps: ['$ocLazyLoad',function($ocLazyLoad){
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/autoCheckRule/happyReturnDetailCtrl.js?ver='+verNo]
                    })
                }]
            }
        })
         //========================================================================
        .state('func.switchMerSet', {
            url: "/switchMerSet/:functionNumber,:merList",
            templateUrl: "views/func/switchMerSet.html",
            data: {pageTitle: '商户黑名单'},
            //controller: "switchSetCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives')
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/autoCheckRule/switchMerSetCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
            .state('func.switchSet', {
                url: "/switchSet/:functionNumber,:blacklist",
                templateUrl: "views/func/switchSet.html",
                data: {pageTitle: '代理商管理'},
                //controller: "switchSetCtrl",
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('localytics.directives')
                        $ocLazyLoad.load('ngGrid');
                        $ocLazyLoad.load('datePicker');
                        $ocLazyLoad.load('ui.select');
                        $ocLazyLoad.load('ui-switch');
                        $ocLazyLoad.load('oitozero.ngSweetAlert');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/controllers/autoCheckRule/switchSetCtrl.js?ver='+verNo]
                        });
                    }]
                }
            })
         //====新增代理商====================================================================
         .state('func.addAgentFunctionManager', {
            url: "/addAgentFunctionManager/:functionNumber,:blacklist",
            templateUrl: "views/func/addAgentFunctionManager.html",
            data: {pageTitle: '代理商管理'},
            //controller: "addAgentFunctionManagerCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives')
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/autoCheckRule/addAgentFunctionManagerCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
         //====新增商户黑名单====================================================================
            .state('func.addMerFunctionManager', {
                url: "/addMerFunctionManager/:functionNumber,:merBlackList",
                templateUrl: "views/func/addMerFunctionManager.html",
                data: {pageTitle: '商户黑名单'},
                //controller: "addAgentFunctionManagerCtrl",
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('localytics.directives')
                        $ocLazyLoad.load('ngGrid');
                        $ocLazyLoad.load('datePicker');
                        $ocLazyLoad.load('ui.select');
                        $ocLazyLoad.load('ui-switch');
                        $ocLazyLoad.load('oitozero.ngSweetAlert');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/controllers/autoCheckRule/addMerFunctionManagerCtrl.js?ver='+verNo]
                        });
                    }]
                }
            })
         //====删除代理商====================================================================
         .state('func.delAgentFunctionManager', {
            url: "/delAgentFunctionManager",
            templateUrl: "views/func/delAgentFunctionManager.html",
            data: {pageTitle: '代理商管理'},
            //controller: "delAgentFunctionManagerCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives')
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/autoCheckRule/delAgentFunctionManagerCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        //====批量新增代理商====================================================================
        .state('func.addButchAgentFunctionManager', {
            url: "/addButchAgentFunctionManager/:functionNumber,:blacklist",
            //controller: 'addButchAgentFunctionManagerCtrl',
            views: {
                "": {templateUrl: "views/func/addButchAgentFunctionManager.html"},
                "addButchAgentFunctionManagerCommon@func.addButchAgentFunctionManager": {templateUrl: "views/func/addButchAgentFunctionManagerCommon.html"}
            },
            data: {pageTitle: '代理商批量新增'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                	$ocLazyLoad.load('fileUpload');
                	$ocLazyLoad.load('localytics.directives');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/autoCheckRule/addButchAgentFunctionManagerCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        //====批量新增商户黑名单====================================================================
            .state('func.addButchMerFunctionManager', {
                url: "/addButchMerFunctionManager/:functionNumber,:blacklist",
                //controller: 'addButchAgentFunctionManagerCtrl',
                views: {
                    "": {templateUrl: "views/func/addButchMerFunctionManager.html"}
                },
                data: {pageTitle: '代理商批量新增'},
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('fileUpload');
                        $ocLazyLoad.load('localytics.directives');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/controllers/autoCheckRule/addButchMerFunctionManagerCtrl.js?ver='+verNo]
                        });
                    }]
                }
            })
        //**************代理商账户控制***********
        .state('func.agentAccountControl', {
            url: "/agentAccountControl",
            templateUrl: "views/func/agentAccountControl.html",
            data: {pageTitle: '代理商账户控制'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives')
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ui-switch');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/autoCheckRule/agentAccountControlCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        //**************行业切换活动***********
        .state('func.industrySwitchActivity', {
            url: "/industrySwitchActivity",
            templateUrl: "views/func/industrySwitchActivity.html",
            data: {pageTitle: '代理商账户控制'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives')
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/func/industrySwitchActivityCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        //**************微创业活动设置***********
        .state('func.superPushConfig', {
            url: "/func/superPushConfig",
            templateUrl: "views/func/superPushConfig.html",
            data: {pageTitle: '微创业活动设置'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives')
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('ui-switch');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/autoCheckRule/superPushConfigCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('func.couponActivityDetail', {
            url: "/couponActivity/rewardDetail/:actId",
            templateUrl: "views/func/couponActivityDetail.html",
            data: {pageTitle: '鼓励金活动详情'},
            //controller: "detailcouponActivityCtrl",
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/autoCheckRule/detailcouponActivityCtrl.js?ver='+verNo]
                    });
                }],
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('ngGrid');
                }
            }
        })
        .state('func.editcouponActivity', {
            url: "/couponActivity/editreward/:actId",
            templateUrl: "views/func/editrouponActivity.html",
            data: {pageTitle: '鼓励金活动编辑'},
            //controller: "editcouponActivityCtrl",
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/autoCheckRule/editcouponActivityCtrl.js?ver='+verNo]
                    });
                }],
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('ngGrid');
                },
            }
        })
        
         .state('func.cardAndReward', {
        	url: "/couponActivity/cardAndReward/:actId/:edit",
        	templateUrl: "views/func/cardAndReward.html",
        	data: {pageTitle: '办卡送奖励'},
        	resolve: {
        		deps: ['$ocLazyLoad', function ($ocLazyLoad) {
        			return $ocLazyLoad.load({
        				name: 'inspinia',
                        files: ['js/controllers/autoCheckRule/cardAndRewardCtrl.js?ver='+verNo]
        			});
        		}],
        		loadPlugin: function ($ocLazyLoad) {
        			$ocLazyLoad.load('ngGrid');
        			$ocLazyLoad.load('oitozero.ngSweetAlert');
        		}
        	}
        })
         .state('func.loanAndReward', {
        	url: "/couponActivity/loanAndReward/:actId/:edit",
        	templateUrl: "views/func/loanAndReward.html",
        	data: {pageTitle: '贷款送奖励'},
        	resolve: {
        		deps: ['$ocLazyLoad', function ($ocLazyLoad) {
        			return $ocLazyLoad.load({
        				name: 'inspinia',
                        files: ['js/controllers/autoCheckRule/loanAndRewardCtrl.js?ver='+verNo]
        			});
        		}],
        		loadPlugin: function ($ocLazyLoad) {
        			$ocLazyLoad.load('ngGrid');
        			$ocLazyLoad.load('oitozero.ngSweetAlert');
        		}
        	}
        })
        .state('func.registeredActivityDetail', {
        	url: "/couponActivity/registeredDetail/:actId",
        	templateUrl: "views/func/registeredDetail.html",
        	data: {pageTitle: '注册返活动详情'},
        	resolve: {
        		deps: ['$ocLazyLoad', function ($ocLazyLoad) {
        			return $ocLazyLoad.load({
        				name: 'inspinia',
        				files: ['js/controllers/func/registeredDetailCtrl.js?ver='+verNo]
        			});
        		}],
        		loadPlugin: function ($ocLazyLoad) {
        			$ocLazyLoad.load('ngGrid');
        		}
        	}
        })
        .state('func.editRegisteredActivity', {
        	url: "/couponActivity/editRegistered/:actId",
        	templateUrl: "views/func/editRegistered.html",
        	data: {pageTitle: '注册返活动编辑'},
        	resolve: {
        		deps: ['$ocLazyLoad', function ($ocLazyLoad) {
        			return $ocLazyLoad.load({
        				name: 'inspinia',
        				files: ['js/controllers/func/editRegisteredCtrl.js?ver='+verNo]
        			});
        		}],
        		loadPlugin: function ($ocLazyLoad) {
        			$ocLazyLoad.load('oitozero.ngSweetAlert');
        			$ocLazyLoad.load('ngGrid');
        		}
        	}
        })
        .state('func.couponRecharge', {
            url: "/couponActivity/couponRecharge/:actId/:edit",
            templateUrl: "views/func/couponRecharge.html",
            data: {pageTitle: '充值返活动详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('ngGrid');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/autoCheckRule/couponRechargeCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('func.couponRechargeDetailEdit', {
            url: "/couponActivity/couponRechargeDetailEdit/:id/:view",
            templateUrl: "views/func/couponRechargeEdit.html",
            data: {pageTitle: '充值返活动详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/autoCheckRule/couponRechargeEditCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('func.activityVip', {
            url: "/couponActivity/activityVip/:actId/:edit",
            templateUrl: "views/func/activityVip.html",
            data: {pageTitle: 'VIP优享活动详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('ngGrid');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/autoCheckRule/activityVipCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        //抵用金活动福利
      .state('func.diYongJin', {
          url: "/welfare/diYongJin/:actId/:edit",
          templateUrl: "views/func/welfare/diYongJin.html",
          data: {pageTitle: '抵用金活动福利详情'},
          resolve: {
              loadPlugin: function ($ocLazyLoad) {
                  $ocLazyLoad.load('oitozero.ngSweetAlert');
                  $ocLazyLoad.load('ui-switch');
                  $ocLazyLoad.load('ngGrid');
              },
              deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                  return $ocLazyLoad.load({
                      name: 'inspinia',
                      files: ['js/controllers/welfare/diYongJinCtrl.js?ver='+verNo]
                  });
              }]
          }
      })
      //抵用金活动福利 新增券
      .state('func.diYongJinEdit', {
          url: "/welfare/diYongJinEdit/:id/:view",
          templateUrl: "views/func/welfare/diYongJinEdit.html",
          data: {pageTitle: '抵用金活动福利详情'},
          resolve: {
              loadPlugin: function ($ocLazyLoad) {
                  $ocLazyLoad.load('oitozero.ngSweetAlert');
                  $ocLazyLoad.load('ui-switch');
                  $ocLazyLoad.load('ngGrid');
                  $ocLazyLoad.load('My97DatePicker');
              },
              deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                  return $ocLazyLoad.load({
                      name: 'inspinia',
                      files: ['js/controllers/welfare/diYongJinEditCtrl.js?ver='+verNo]
                  });
              }]
          }
      })
      //会员系统
      .state('func.vipSys', {
          url: "/vipSys/vipSys/:actId/:edit",
          templateUrl: "views/func/vipSys/vipSys.html",
          data: {pageTitle: '物品详情'},
          resolve: {
              loadPlugin: function ($ocLazyLoad) {
                  $ocLazyLoad.load('oitozero.ngSweetAlert');
                  $ocLazyLoad.load('ui-switch');
                  $ocLazyLoad.load('ngGrid');
              },
              deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                  return $ocLazyLoad.load({
                      name: 'inspinia',
                      files: ['js/controllers/vipSys/vipSysCtrl.js?ver='+verNo]
                  });
              }]
          }
      })
      //会员系统 新增券
      .state('func.vipSysEdit', {
          url: "/vipSys/vipSysEdit/:id/:view/:returnFlag",
          templateUrl: "views/func/vipSys/vipSysEdit.html",
          data: {pageTitle: '物品详情'},
          resolve: {
              loadPlugin: function ($ocLazyLoad) {
                  $ocLazyLoad.load('oitozero.ngSweetAlert');
                  $ocLazyLoad.load('ui-switch');
                  $ocLazyLoad.load('ngGrid');
                  $ocLazyLoad.load('My97DatePicker');
              },
              deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                  return $ocLazyLoad.load({
                      name: 'inspinia',
                      files: ['js/controllers/vipSys/vipSysEditCtrl.js?ver='+verNo]
                  });
              }]
          }
      })
        //鼓励金活动福利
      .state('func.guLiJin', {
          url: "/welfare/guLiJin/:actId/:edit",
          templateUrl: "views/func/welfare/guLiJin.html",
          data: {pageTitle: '鼓励金活动福利详情'},
          resolve: {
              loadPlugin: function ($ocLazyLoad) {
                  $ocLazyLoad.load('oitozero.ngSweetAlert');
                  $ocLazyLoad.load('ui-switch');
                  $ocLazyLoad.load('ngGrid');
              },
              deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                  return $ocLazyLoad.load({
                      name: 'inspinia',
                      files: ['js/controllers/welfare/guLiJinCtrl.js?ver='+verNo]
                  });
              }]
          }
      })
      //鼓励金活动福利 新增券
      .state('func.guLiJinEdit', {
          url: "/welfare/guLiJinEdit/:id/:view",
          templateUrl: "views/func/welfare/guLiJinEdit.html",
          data: {pageTitle: '鼓励金活动福利详情'},
          resolve: {
              loadPlugin: function ($ocLazyLoad) {
                  $ocLazyLoad.load('oitozero.ngSweetAlert');
                  $ocLazyLoad.load('ui-switch');
                  $ocLazyLoad.load('ngGrid');
                  $ocLazyLoad.load('My97DatePicker');
              },
              deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                  return $ocLazyLoad.load({
                      name: 'inspinia',
                      files: ['js/controllers/welfare/guLiJinEditCtrl.js?ver='+verNo]
                  });
              }]
          }
      })
        //购买鼓励金
        .state('func.buyReward', {
            url: "/couponActivity/buyReward/:actId/:edit",
            templateUrl: "views/func/buyReward.html",
            data: {pageTitle: '购买鼓励金活动详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('ngGrid');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/autoCheckRule/buyRewardCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('func.buyRewardDetailEdit', {
            url: "/couponActivity/buyRewardDetailEdit/:id/:view",
            templateUrl: "views/func/buyRewardEdit.html",
            data: {pageTitle: '购买鼓励金活动详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/autoCheckRule/buyRewardEditCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        //云闪付
         .state('func.cloudPay', {
            url: "/couponActivity/cloudPay/:actId/:edit",
            templateUrl: "views/func/cloudPayActivity.html",
            data: {pageTitle: '充值返活动详情'},
            //controller: "cloudPayCtrl",
            resolve: {
            	loadPlugin: function ($ocLazyLoad) {
            		$ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('ngGrid');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/autoCheckRule/cloudPayCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        //邀请有奖
        .state('func.invitePrizes', {
            url: "/couponActivity/invitePrizes/:actId/:edit",
            templateUrl: "views/func/invitePrizes.html",
            data: {pageTitle: '邀请有奖活动详情'},
            resolve: {
            	loadPlugin: function ($ocLazyLoad) {
            		$ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('fileUpload');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/autoCheckRule/invitePrizesCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
         //====邀请有奖，新增代理商====================================================================
         .state('func.addAgentInvitePrizes', {
            url: "/addAgentInvitePrizes",
            templateUrl: "views/func/addAgentInvitePrizes.html",
            data: {pageTitle: '邀请有奖新增代理商'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/autoCheckRule/addAgentInvitePrizesCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        //====邀请有奖，批量新增代理商====================================================================
        .state('func.addButchAgentInvitePrizes', {
            url: "/addButchAgentInvitePrizes",
            templateUrl: "views/func/addButchAgentInvitePrizes.html",
            data: {pageTitle: '邀请有奖批量新增代理商'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                	$ocLazyLoad.load('fileUpload');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/autoCheckRule/addButchAgentInvitePrizesCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('func.couponActivity', {
            url: "/couponActivity",
            templateUrl: "views/func/couponActivity.html",
            data: {pageTitle: '活动管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/autoCheckRule/couponActivityCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
            .state('func.redemptionActivity', {
                url: "/couponActivity/redemptionActivity",
                templateUrl: "views/func/redemptionActivity.html",
                data: {pageTitle: '兑奖活动设置'},
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('localytics.directives');
                        $ocLazyLoad.load('oitozero.ngSweetAlert');
                        $ocLazyLoad.load('summernote');
                        $ocLazyLoad.load('angular-summernote');
                        $ocLazyLoad.load('My97DatePicker');
                        $ocLazyLoad.load('ui-switch');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/controllers/func/redemptionActivityCtrl.js?ver='+verNo]
                        });
                    }]
                }
            })
            .state('func.redemptionActivityManage', {
                url: "/couponActivity/redemptionActivityManage",
                templateUrl: "views/func/redemptionActivityManage.html",
                data: {pageTitle: '奖券类型管理'},
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('localytics.directives');
                        $ocLazyLoad.load('oitozero.ngSweetAlert');
                        $ocLazyLoad.load('summernote');
                        $ocLazyLoad.load('angular-summernote');
                        $ocLazyLoad.load('My97DatePicker');
                        $ocLazyLoad.load('ui-switch');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/controllers/func/redemptionActivityManageCtrl.js?ver='+verNo]
                        });
                    }]
                }
            })
        .state('func.prizeConfigure', {
            url: "/prizeConfigure/:funcCode",
            templateUrl: "views/luckDraw/prizeConfigure/prizeConfigure.html",
            data: {pageTitle: '抽奖活动设置'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('summernote');
                    $ocLazyLoad.load('angular-summernote');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('ui-switch');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/plugins/summernote/summernote.min.js','js/plugins/summernote/angular-summernote.min.js', 'css/plugins/summernote/summernote.css',
                            'js/controllers/luckDraw/prizeConfigure/prizeConfigureCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('func.prizeConfigureDetail', {
            url: "/prizeConfigureDetail/:funcCode",
            templateUrl: "views/luckDraw/prizeConfigure/prizeConfigureDetail.html",
            data: {pageTitle: '抽奖活动设置详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('summernote');
                    $ocLazyLoad.load('angular-summernote');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('ui-switch');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/plugins/summernote/summernote.min.js','js/plugins/summernote/angular-summernote.min.js', 'css/plugins/summernote/summernote.css',
                            'js/controllers/luckDraw/prizeConfigure/prizeConfigureDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('func.prizeAdd', {
            url: "/prizeAdd/:funcCode",
            templateUrl: "views/luckDraw/prizeConfigure/prizeAdd.html",
            data: {pageTitle: '奖项新增'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('fileUpload');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/luckDraw/prizeConfigure/prizeAddCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('func.prizeEdit', {
            url: "/prizeEdit/:funcCode/:id",
            templateUrl: "views/luckDraw/prizeConfigure/prizeEdit.html",
            data: {pageTitle: '奖项修改'},
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
                        files: ['js/controllers/luckDraw/prizeConfigure/prizeEditCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('func.prizeDetail', {
            url: "/prizeDetail/:funcCode/:id",
            templateUrl: "views/luckDraw/prizeConfigure/prizeDetail.html",
            data: {pageTitle: '奖项详情'},
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
                        files: ['js/controllers/luckDraw/prizeConfigure/prizeDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('func.prizeDetailBack', {
            url: "/prizeDetailBack/:funcCode/:id",
            templateUrl: "views/luckDraw/prizeConfigure/prizeDetailBack.html",
            data: {pageTitle: '奖项详情'},
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
                        files: ['js/controllers/luckDraw/prizeConfigure/prizeDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('func.blacklist', {
            url: "/blacklist/:funcCode/:id",
            templateUrl: "views/luckDraw/prizeConfigure/prizeBlacklistQuery.html",
            data: {pageTitle: '奖项黑名单管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/luckDraw/prizeConfigure/prizeBlacklistQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('func.blacklistBack', {
            url: "/blacklistBack/:funcCode/:id",
            templateUrl: "views/luckDraw/prizeConfigure/prizeBlacklistBackQuery.html",
            data: {pageTitle: '奖项黑名单管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/luckDraw/prizeConfigure/prizeBlacklistBackQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('func.activityOrderInfoQuery', {
            url: "/activityOrderInfoQuery",
            templateUrl: "views/func/activityOrderInfoQuery.html",
            data: {pageTitle: '购买记录查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/func/activityOrderInfoQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('func.activityOrderInfo', {
            url: "/activityOrderInfo/:id",
            templateUrl: "views/func/activityOrderInfo.html",
            data: {pageTitle: '充值返详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('infinity-chosen');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/func/activityOrderInfoCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('func.zxIndustryDetail', {
            url: "/couponActivity/zxIndustryDetail/:activetiyCode",
            templateUrl: "views/func/zxIndustryDetail.html",
            data: {pageTitle: '自选行业收费详情'},
            //controller: "zxIndustryDetailCtrl",
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/autoCheckRule/zxIndustryDetailCtrl.js?ver='+verNo]
                    });
                }],
                loadPlugin: function ($ocLazyLoad) {
                }
            }
        })
        .state('func.zxIndustryUpdate', {
            url: "/couponActivity/zxIndustryUpdate/:activetiyCode",
            templateUrl: "views/func/zxIndustryUpdate.html",
            data: {pageTitle: '自选行业收费修改'},
            //controller: "zxIndustryUpdateCtrl",
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/autoCheckRule/zxIndustryUpdateCtrl.js?ver='+verNo]
                    });
                }],
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('ui-switch');
                },
            }
        })
        .state('func.superCollection', {
            url: "/superCollection",
            templateUrl: "views/func/superCollection/superCollectionQuery.html",
            data: {pageTitle:'超级收款'},
            //controller: "superCollectionQueryCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('colorpicker.module');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/func/superCollection/superCollectionQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
            .state('func.vasShareRuleManager', {
                url: "/vasShareRuleManager",
                templateUrl: "views/func/vasShareRuleManager.html",
                data: {pageTitle: '增值业务分润管理'},
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('localytics.directives')
                        $ocLazyLoad.load('oitozero.ngSweetAlert');
                        $ocLazyLoad.load('ui-switch');
                        $ocLazyLoad.load('My97DatePicker');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/controllers/func/vasShareRuleQueryCtrl.js?ver='+verNo]
                        });
                    }]
                }
            })
            .state('func.agentVasShareRule',{
                url: '/vasShareRuleManager/:id/:vasServiceNo/:teamId/:teamEntryId',
                templateUrl:'views/func/agentVasShareRuleManager.html',
                data: {pageTitle: '代理商分润'},
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('localytics.directives');
                        $ocLazyLoad.load('oitozero.ngSweetAlert');
                        $ocLazyLoad.load('ui-switch');
                        $ocLazyLoad.load('infinity-chosen');
                        $ocLazyLoad.load('datePicker');
                        $ocLazyLoad.load('ngGrid');
                        $ocLazyLoad.load('ui.select');
                        $ocLazyLoad.load('My97DatePicker');
                    },
                    deps: ['$ocLazyLoad',function($ocLazyLoad){
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/controllers/func/agentVasShareRuleQueryCtrl.js?ver='+verNo]
                        })
                    }]
                }
            })



        .state('func.queryAuthList', {
            url: "/queryAuthList",
            templateUrl: "views/agentAuth/queryAuthList.html",
            data: {pageTitle:'三方授权查询'},
            //controller: "agentAuthCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives')
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/agentAuth/agentAuthCtrl.js?ver='+verNo]
                    });
                }]
            }
        })

        .state('func.topAgentManagement', {
            url: "/topAgentManagement",
            templateUrl: "views/agentAuth/topAgentManagement.html",
            data: {pageTitle: '顶层代理商管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {;
                    $ocLazyLoad.load('localytics.directives')
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('fileUpload');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/agentAuth/topAgentManagementCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('func.setting', {
            url: "/switchSet/:functionNumber/:functionName",
            templateUrl: "views/func/setting.html",
            data: {pageTitle: '业务设置'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/autoCheckRule/settingCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('func.setting062', {
            url: "/switchSet062/:functionNumber/:functionName",
            templateUrl: "views/func/setting062.html",
            data: {pageTitle: '业务设置'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/autoCheckRule/settingCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
            .state('func.switchSpecialSet066', {
                url: "/switchSpecialSet066/:functionNumber",
                templateUrl: "views/func/switchSpecial/switchSpecialSet066.html",
                data: {pageTitle: '业务设置'},
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('oitozero.ngSweetAlert');
                        $ocLazyLoad.load('ui.select');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/controllers/func/switchSpecial/switchSpecialSet066Ctrl.js?ver='+verNo]
                        });
                    }]
                }
            })
            .state('func.switchSpecialSet070', {
                url: "/switchSet070/:functionNumber/:functionName",
                templateUrl: "views/func/switchSpecial/switchSpecialSet070.html",
                data: {pageTitle: '业务设置'},
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('localytics.directives');
                        $ocLazyLoad.load('infinity-chosen');
                        $ocLazyLoad.load('ngGrid');
                        $ocLazyLoad.load('oitozero.ngSweetAlert');
                        $ocLazyLoad.load('ui.select');
                        $ocLazyLoad.load('My97DatePicker');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/controllers/func/switchSpecial/switchSpecialSet070Ctrl.js?ver='+verNo]
                        });
                    }]
                }
            })

            .state('func.switchSpecialSet065', {
                url: "/switchSpecialSet065/:functionNumber",
                templateUrl: "views/func/switchSpecial/switchSpecialSet065.html",
                data: {pageTitle: '业务设置'},
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('oitozero.ngSweetAlert');
                        $ocLazyLoad.load('ui.select');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/controllers/func/switchSpecial/switchSpecialSet065Ctrl.js?ver='+verNo]
                        });
                    }]
                }
            })
            .state('func.switchSpecialSet069', {
                url: "/switchSpecialSet069/:functionNumber",
                templateUrl: "views/func/switchSpecial/switchSpecialSet069.html",
                data: {pageTitle: '业务设置'},
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('oitozero.ngSweetAlert');
                        $ocLazyLoad.load('ui.select');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/controllers/func/switchSpecial/switchSpecialSet069Ctrl.js?ver='+verNo]
                        });
                    }]
                }
            })
            .state('func.switchSpecialSet072', {
                url: "/switchSpecialSet072/:functionNumber",
                templateUrl: "views/func/switchSpecial/switchSpecialSet072.html",
                data: {pageTitle: '业务设置'},
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('oitozero.ngSweetAlert');
                        $ocLazyLoad.load('ui.select');
                        $ocLazyLoad.load('ui-switch');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/controllers/func/switchSpecial/switchSpecialSet072Ctrl.js?ver='+verNo]
                        });
                    }]
                }
            })
            .state('func.switchSpecialSet073', {
                url: "/switchSpecialSet073/:functionNumber",
                templateUrl: "views/func/switchSpecial/switchSpecialSet073.html",
                data: {pageTitle: '业务设置'},
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('oitozero.ngSweetAlert');
                        $ocLazyLoad.load('ui.select');
                        $ocLazyLoad.load('ui-switch');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/controllers/func/switchSpecial/switchSpecialSet073Ctrl.js?ver='+verNo]
                        });
                    }]
                }
            })
            .state('func.switchSpecialSet074', {
                url: "/switchSpecialSet074/:functionNumber",
                templateUrl: "views/func/switchSpecial/switchSpecialSet074.html",
                data: {pageTitle: '业务设置'},
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('oitozero.ngSweetAlert');
                        $ocLazyLoad.load('ui.select');
                        $ocLazyLoad.load('ui-switch');
                        $ocLazyLoad.load('My97DatePicker');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/controllers/func/switchSpecial/switchSpecialSet074Ctrl.js?ver='+verNo]
                        });
                    }]
                }
            })
            .state('func.appShowConfig', {
                url: "/appShowConfig/:fmcId",
                templateUrl: "views/func/switchSpecial/appShowConfig.html",
                data: {pageTitle: 'APP下发控制'},
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('oitozero.ngSweetAlert');
                        $ocLazyLoad.load('ui.select');
                        $ocLazyLoad.load('ui-switch');
                        $ocLazyLoad.load('fileUpload');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/controllers/func/switchSpecial/appShowConfigCtrl.js?ver='+verNo]
                        });
                    }]
                }
            })
            .state('func.setting064', {
                url: "/switchSet064/:functionNumber/:functionName",
                templateUrl: "views/func/setting064.html",
                data: {pageTitle: '业务设置'},
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('oitozero.ngSweetAlert');
                        $ocLazyLoad.load('My97DatePicker');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/controllers/autoCheckRule/settingCtrl.js?ver='+verNo]
                        });
                    }]
                }
            })
        //******************活动查询******************
        .state('activity', {
            abstract: true,
            url: "/activity",
            templateUrl: "views/common/content.html",
        })
        .state('activity.merchantProfitQuery', {
        	url: "/activity/merchantProfitQuery",
        	templateUrl: "views/activity/merchantProfitQuery.html",
        	data: {pageTitle: '商户收益查询'},
        	resolve: {
        		loadPlugin: function ($ocLazyLoad) {
        			$ocLazyLoad.load('oitozero.ngSweetAlert');
        			$ocLazyLoad.load('infinity-chosen');
        			$ocLazyLoad.load('ngGrid');
        			$ocLazyLoad.load('ui.select');
        			$ocLazyLoad.load('My97DatePicker');
        		},
        		deps: ['$ocLazyLoad', function ($ocLazyLoad) {
        			return $ocLazyLoad.load({
        				name: 'inspinia',
        				files: ['js/controllers/activity/merchantProfitQueryCtrl.js?ver='+verNo]
        			});
        		}]
        	}
        })
        .state('activity.invitePrizesQuery', {
            url: "/activity/invitePrizesQuery",
            templateUrl: "views/activity/invitePrizesQuery.html",
            data: {pageTitle: '邀请有奖查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/activity/invitePrizesQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('activity.couponQuery', {
        	url: "/couponQuery",
        	templateUrl: "views/func/couponQuery.html",
        	data: {pageTitle: '发行券查询'},
        	resolve: {
        		loadPlugin: function ($ocLazyLoad) {
        			$ocLazyLoad.load('localytics.directives');
        			$ocLazyLoad.load('oitozero.ngSweetAlert');
        			$ocLazyLoad.load('infinity-chosen');
        			$ocLazyLoad.load('ngGrid');
        			$ocLazyLoad.load('ui.select');
        			$ocLazyLoad.load('My97DatePicker');
        		},
        		deps: ['$ocLazyLoad', function ($ocLazyLoad) {
        			return $ocLazyLoad.load({
        				name: 'inspinia',
        				files: ['js/controllers/autoCheckRule/queryCouponCtrl.js?ver='+verNo]
        			});
        		}]
        	}
        })
            .state('activity.couponImport', {
                url: "/couponImport",
                templateUrl: "views/func/couponImport/couponImport.html",
                data: {pageTitle: '批量新增券'},
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('fileUpload');
                        $ocLazyLoad.load('oitozero.ngSweetAlert');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/controllers/func/couponImport/couponImportCtrl.js?ver='+verNo]
                        });
                    }]
                }
            })
        .state('activity.activityVerificationInfoQueryCtrl', {
            url: "/activityVerificationInfoQuery",
            templateUrl: "views/func/activityVerIficationInfoQuery.html",
            data: {pageTitle: '使用券查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/func/activityVerificationInfoQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        //******************欢乐送商户查询*************
        .state('activity.activity', {
            url: "/activity/activity",
            templateUrl: 'views/activity/activity.html',
            data: {pageTitle: '欢乐送业务查询'},
            resolve: {
            	loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/activity/activityCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        //******************微创业商户查询*************
        .state('activity.superPush', {
            url: "/activity/superPush",
            templateUrl: 'views/activity/superPush.html',
            data: {pageTitle: '微创业商户查询'},
            resolve: {
            	loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/activity/superPushCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        //******************微创业商户详情*************
        .state('activity.superPushMerchantDetail', {
            url: "/activity/superPushMerchantDetail/:merchantNo",
            templateUrl: 'views/activity/superPushMerchantDetail.html',
            data: {pageTitle: '微创业商户详情'},
            resolve: {
            	loadPlugin: function ($ocLazyLoad) {
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/activity/superPushMerchantDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        //******************微创业提现详情*************
        .state('activity.superPushCashDetail', {
            url: "/activity/superPushCashDetail/:merchantNo",
            templateUrl: 'views/activity/superPushCashDetail.html',
            data: {pageTitle: '微创业提现详情'},
            resolve: {
            	loadPlugin: function ($ocLazyLoad) {
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/activity/superPushCashDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        //******************微创业分润详情*************
        .state('activity.superPushShareDetail', {
            url: "/activity/superPushShareDetail/:merchantNo",
            templateUrl: 'views/activity/superPushShareDetail.html',
            data: {pageTitle: '微创业分润详情'},
            resolve: {
            	loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('datePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/activity/superPushShareDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
         //******************微创业收益明细*************
        .state('activity.superPushShare', {
            url: "/activity/superPushShare",
            templateUrl: 'views/activity/superPushShare.html',
            data: {pageTitle: '微创业收益明细'},
            resolve: {
            	loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/activity/superPushShareCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        //******************欢乐返商户查询*************
	   .state('activity.happyBack', {
	       url: "/happyBack",
	       templateUrl: 'views/activity/happyBack.html',
	       data: {pageTitle: '欢乐返查询'},
	       //controller: "happyBackCtrl",
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
	                   files: ['js/controllers/activity/happyBackCtrl.js?ver='+verNo]
	               });
	           }]
	       }
	   })
        .state('activity.happyBackDetail', {
            url: "/happyBackDetail/:hId",
            templateUrl: "views/activity/happyBackDetail.html",
            data: {pageTitle: '欢乐返商户详情'},
            //controller: "happyBackDetailCtrl",
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
                        files: ['js/controllers/activity/happyBackDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })

        //新欢乐送活动查询
        .state('activity.happySendNew', {
            url: "/happySendNew",
            templateUrl: 'views/activity/happySendNew.html',
            data: {pageTitle: '新欢乐送活动查询'},
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
                        files: ['js/controllers/activity/happySendNewCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
            .state('activity.superHappyBack', {
                url: "/superHappyBack",
                templateUrl: 'views/activity/superHappyBack/superHappyBackQuery.html',
                data: {pageTitle: '超级返活动查询'},
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
                            files: ['js/controllers/activity/superHappyBack/superHappyBackQueryCtrl.js?ver='+verNo]
                        });
                    }]
                }
            })
        // 新欢乐送商户奖励
        .state('activity.happySendMer', {
            url: "/happySendMer",
            templateUrl: 'views/activity/happySendMer.html',
            data: {pageTitle: '新欢乐送商户奖励'},
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
                        files: ['js/controllers/activity/happySendMerCtrl.js?ver='+verNo]
                    });
                }]
            }
        })

        //欢乐返活跃商户活动查询
        .state('activity.happyBackActivityMerchant', {
            url: "/happyBackActivityMerchant",
            templateUrl: "views/activity/happyBackActivityMerchant.html",
            data: {pageTitle: '欢乐返活跃商户活动查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/activity/happyBackActivityMerchantCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        //欢乐返代理商奖励查询
            .state('activity.happyBackActivityAgent', {
                url: "/happyBackActivityAgent",
                templateUrl: "views/activity/happyBackActivityAgent.html",
                data: {pageTitle: '欢乐返代理商奖励查询'},
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('localytics.directives');
                        $ocLazyLoad.load('oitozero.ngSweetAlert');
                        $ocLazyLoad.load('infinity-chosen');
                        $ocLazyLoad.load('datePicker');
                        $ocLazyLoad.load('ngGrid');
                        $ocLazyLoad.load('ui.select');
                        $ocLazyLoad.load('My97DatePicker');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/controllers/activity/happyBackActivityAgentCtrl.js?ver='+verNo]
                        });
                    }]
                }
            })
        .state('activity.activityVipQuery', {
            url: "/activityVipQuery",
            templateUrl: 'views/activity/activityVipQuery.html',
            data: {pageTitle: 'VIP优享订单查询'},
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
                        files: ['js/controllers/activity/activityVipQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })

        .state('activity.luckDrawOrder', {
            url: "/luckDrawOrder",
            templateUrl: 'views/luckDraw/luckDrawOrder/luckDrawOrderQuery.html',
            data: {pageTitle: '抽奖信息查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/luckDraw/luckDrawOrder/luckDrawOrderQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('activity.luckDrawOrderDetail', {
            url: "/luckDrawOrderDetail/:id",
            templateUrl: 'views/luckDraw/luckDrawOrder/luckDrawOrderDetail.html',
            data: {pageTitle: '抽奖信息详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/luckDraw/luckDrawOrder/luckDrawOrderDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('activity.accessCount', {
            url: "/accessCount",
            templateUrl: 'views/luckDraw/accessCount/accessCountQuery.html',
            data: {pageTitle: '抽奖数据统计'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/luckDraw/accessCount/accessCountQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        /*服务管理*/
        .state('service', {
            abstract: true,
            url: "/service",
            templateUrl: "views/common/content.html",
        })
        .state('service.addService', {
            url: "/addService",
            data: {pageTitle: '服务种类增加'},
            views: {
                '': {
                    templateUrl: 'views/service/addService.html'
                },
                'serviceBase@service.addService': {
                    templateUrl: 'views/service/serviceBaseInfo.html'
                }
            },
            //controller: "addServiceCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('colorpicker.module');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/services/addServiceCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('service.queryService', {
            url: "/queryService",
            data: {pageTitle: '服务种类查询'},
            views: {
                '': {
                    templateUrl: 'views/service/queryService.html'
                },
                'serviceBase@service.queryService': {
                    templateUrl: 'views/service/serviceBaseInfo.html'
                }
            },
            //controller: "queryServiceCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/services/queryServiceCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('service.serviceDetail', {
            url: "/serviceDetail/:serviceId",
            templateUrl: "views/service/serviceDetail.html",
            data: {pageTitle: '服务详情'},
            //controller: "detailServiceCtrl",
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/services/detailServiceCtrl.js?ver='+verNo]
                    });
                }],
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('ngGrid');
                },
            }
        })
        .state('service.editService', {
            url: "/editService/:serviceId",
            data: {pageTitle: '服务修改'},
            views: {
                '': {
                    templateUrl: 'views/service/editService.html'
                },
                'serviceBase@service.editService': {
                    templateUrl: 'views/service/serviceBaseInfo.html'
                }
            },
            //controller: "editServiceCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('ngGrid')
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('colorpicker.module');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/services/editServiceCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        //==========代理商分润设置 ==================================================
        .state('service.agentProfit', {
            url: "/agentProfit/:serviceId",
            data: {pageTitle: '代理商分润'},
            views: {
                '': {
                    templateUrl: 'views/service/agentProfit.html'
                },
//                'serviceBase@service.editService': {
//                    templateUrl: 'views/service/serviceBaseInfo.html'
//                }
            },
            //controller: "agentProfitCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('ngGrid')
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('colorpicker.module');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/services/agentProfitCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        //==========================================================================
        .state('service.addRequireItem', {
            url: "/require/addRequireItem",
            data: {pageTitle: '进件要求项添加'},
            //controller:"addRequireCtrl",
            views: {
                '': {templateUrl: "views/service/addRequireItem.html"},
                'requireBase@service.addRequireItem': {templateUrl: "views/service/requireBaseInfo.html"}
            },
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                	$ocLazyLoad.load('fileUpload');
                	$ocLazyLoad.load('localytics.directives');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/services/addRequireCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('service.queryRequireItem', {
            url: "/require/queryRequireItem",
            templateUrl: "views/service/queryRequireItem.html",
            data: {pageTitle: '进件要求项查询'},
            //controller: 'queryRequireCtrl',
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/services/queryRequireCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('service.requireDetail', {
            url: "/require/requireDetail/:id",
            templateUrl: "views/service/requireDetail.html",
            //controller: 'requireDetailCtrl',
            data: {pageTitle: '进件要求项详情'},
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/services/requireDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('service.editRequireItem', {
            url: "/require/editRequireItem/:id",
            //controller:"editRequireCtrl",
            data: {pageTitle: '进件要求项修改'},
            views: {
                '': {templateUrl: "views/service/editRequireItem.html"},
                'requireBase@service.editRequireItem': {templateUrl: "views/service/requireBaseInfo.html"}
            },
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
        			$ocLazyLoad.load('fileUpload');
                	$ocLazyLoad.load('localytics.directives');
                	$ocLazyLoad.load('datePicker')
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/services/editRequireCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('service.addProduct', {
        	url: "/addProduct",
        	//controller: "addProductCtrl",
        	data: { pageTitle: '业务产品增加' },
        	views:{'':{templateUrl: "views/service/addProduct.html"},
        		'productBase@service.addProduct':{templateUrl: "views/service/productBase.html"}
        	},
        	 resolve:{
            	 loadPlugin: function ($ocLazyLoad) {
             		 $ocLazyLoad.load('datePicker');
            		 $ocLazyLoad.load('localytics.directives');
            		 $ocLazyLoad.load('oitozero.ngSweetAlert');
            		 $ocLazyLoad.load('fileUpload')
	             },
	             deps:['$ocLazyLoad', function($ocLazyLoad) {
	                 return $ocLazyLoad.load({
	                     name: 'inspinia',
	                     files: ['js/controllers/services/addProductCtrl.js?ver='+verNo]
	                 });
	             }]
	        },
         })
        .state('service.queryProduct', {
            url: "/queryProduct",
            templateUrl: "views/service/queryProduct.html",
            data: {pageTitle: '业务产品查询'},
            //controller: "queryProductCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/services/queryProductCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('service.productDetail', {
            url: "/productDetail/:id",
            templateUrl: "views/service/productDetail.html",
            data: {pageTitle: '业务产品详情'},
            //controller: "productDetailCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                	$ocLazyLoad.load('localytics.directives');
                	$ocLazyLoad.load('datePicker')
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/services/productDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('service.editProduct', {
        	url: "/editProduct/:id",
        	data: {pageTitle: '业务产品修改'},
        	views:{'':{templateUrl: "views/service/editProduct.html"},
        		'productBase@service.editProduct':{templateUrl: "views/service/productBase.html"}
        	},
        	resolve:{
            	 loadPlugin: function ($ocLazyLoad) {
            		 $ocLazyLoad.load('datePicker');
            		 $ocLazyLoad.load('localytics.directives');
            		 $ocLazyLoad.load('oitozero.ngSweetAlert');
            		 $ocLazyLoad.load('fileUpload')
	             },
	             deps:['$ocLazyLoad', function($ocLazyLoad) {
	                 return $ocLazyLoad.load({
	                     name: 'inspinia',
	                     files: ['js/controllers/services/editProductCtrl.js?ver='+verNo]
	                 });
	             }]
        	 },
        })
        .state('service.editProductBase', {
        	url: "/editProductBase/:id",
        	data: {pageTitle: '业务产品基础数据修改'},
        	templateUrl: "views/service/editProductBase.html",
        	resolve:{
            	 loadPlugin: function ($ocLazyLoad) {
            		 $ocLazyLoad.load('datePicker');
            		 $ocLazyLoad.load('localytics.directives');
            		 $ocLazyLoad.load('oitozero.ngSweetAlert');
            		 $ocLazyLoad.load('fileUpload');
	             },
	             deps:['$ocLazyLoad', function($ocLazyLoad) {
	                 return $ocLazyLoad.load({
	                     name: 'inspinia',
	                     files: ['js/controllers/services/editProductBaseCtrl.js?ver='+verNo]
	                 });
	             }]
        	 },
        })
        //====硬件产品新增====================================================================
        .state('service.insertHard', {
            url: "/service/insertHard",
            data: {pageTitle: '硬件产品新增'},
            //controller:"addHardCtrl",
            views: {
                '': {templateUrl: "views/service/addHard.html"},
//                'requireBase@service.addRequireItem': {templateUrl: "views/service/requireBaseInfo.html"}
            },
            resolve: {
            	deps: ['$ocLazyLoad', function ($ocLazyLoad) {
	                return $ocLazyLoad.load({
	                    name: 'inspinia',
	                    files: ['js/controllers/services/addHardCtrl.js?ver='+verNo]
	                });
            	}]
            }
        })
        .state('service.updateHard', {
            url: "/service/updateHard/:id",
            data: {pageTitle: '硬件产品修改'},
            //controller:"updateHardCtrl",
            views: {
                '': {templateUrl: "views/service/updateHard.html"},
//                'requireBase@service.addRequireItem': {templateUrl: "views/service/requireBaseInfo.html"}
            },
            resolve: {
            	loadPlugin: function ($ocLazyLoad) {
            		$ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
				},
            	deps: ['$ocLazyLoad', function ($ocLazyLoad) {
	                return $ocLazyLoad.load({
	                    name: 'inspinia',
	                    files: ['js/controllers/services/updateHardCtrl.js?ver='+verNo]
	                });
            	}]
            }
        })
        .state('service.queryHard', {
            url: "/queryHard",
            templateUrl: "views/service/queryHard.html",
            data: {pageTitle: '硬件产品查询'},
            //controller: "queryHardCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/services/queryHardCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        /*代理商*/
        .state('agent', {
            abstract: true,
            url: "/agent",
            templateUrl: "views/common/content.html"
        })
        .state('agent.addAgent', {
            url: "/addAgent",
            data: {pageTitle: '新增代理商'},
            views: {
                '': {templateUrl: "views/agent/addAgent.html"},
                'agentBase@agent.addAgent': {templateUrl: "views/agent/agentBase.html"}
            },
            resolve: {
				loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('infinity-chosen');
					$ocLazyLoad.load('oitozero.ngSweetAlert');
					$ocLazyLoad.load('fileUpload')
				},
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/agent/addAgentCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('agent.queryAgent', {
            url: "/queryAgent",
            templateUrl: "views/agent/queryAgent.html",
            data: {pageTitle: '查询代理商'},
            resolve: {
				loadPlugin: function ($ocLazyLoad) {
					$ocLazyLoad.load('ui-switch');
					$ocLazyLoad.load('oitozero.ngSweetAlert');
				},
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/agent/queryAgentCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('agent.agentDetail',{
        	url: '/agentDetail/:id/:teamId',
        	views: {
        		'':{templateUrl:'views/agent/agentDetail.html'},
        		'agentDetailBaseTwo@agent.agentDetail': {templateUrl: "views/agent/agentDetailBaseTwo.html"}
        	},
        	//controller: 'agentDetailCtrl',
        	data: {pageTitle: '代理商详情'},
        	resolve: {
        		deps: ['$ocLazyLoad',function($ocLazyLoad){
        			return $ocLazyLoad.load({
        				name: 'inspinia',
        				files: ['js/controllers/agent/agentDetailCtrl.js?ver='+verNo]
        			})
        		}]
        	}
        })
        .state('agent.editAgent', {
            url: "/editAgent/:agentNo/:teamId",
            views: {
                '': {templateUrl: "views/agent/editAgent.html"},
                'agentBase@agent.editAgent': {templateUrl: "views/agent/agentBase.html"}
            },
            //controller: "editAgentCtrl",
            data: {pageTitle: '修改代理商'},
            resolve: {
				loadPlugin: function ($ocLazyLoad) {
					$ocLazyLoad.load('oitozero.ngSweetAlert');
					$ocLazyLoad.load('fileUpload');
					$ocLazyLoad.load('datePicker');
					$ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('infinity-chosen');
				},
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/agent/editAgentCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        //代理商审核列表
        .state('agent.auditQuery', {
            url: "/agentAuditQuery",
            templateUrl: "views/agent/agentAuditQuery.html",
            //controller: "agentAuditQueryCtrl",
            data: {pageTitle: '代理商审核列表'},
            resolve: {
				loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
				},
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/agent/agentAuditQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        //代理商审核
        .state('agent.audit', {
            url: "/agentAudit/:id/:teamId",
            views: {
        		'':{templateUrl:'views/agent/agentAudit.html'},
        		'agentDetailBase@agent.audit': {templateUrl: "views/agent/agentDetailBase.html"}
        	},
            //controller: "agentAuditCtrl",
            data: {pageTitle: '代理商审核'},
            resolve: {
				loadPlugin: function ($ocLazyLoad){
					$ocLazyLoad.load('ui-switch');
					$ocLazyLoad.load('oitozero.ngSweetAlert');
				},
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/agent/agentAuditCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        //代理商角色
        .state('agent.queryAgentRoleOem', {
            url: "/queryAgentRoleOem",
            templateUrl: "views/agent/queryAgentRoleOem.html",
            controller: "queryAgentRoleOemCtrl",
            data: {pageTitle: '代理商角色列表'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/agent/queryAgentRoleOemCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        /*商户管理*/
        .state('merchant', {
            abstract: true,
            url: "/merchant",
            templateUrl: "views/common/content.html",
        })
        .state('merchant.queryMer', {
            url: "/queryMer",
            templateUrl: "views/merchant/merchantQuery.html",
            data: {pageTitle: '查询商户'},
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
                        files: ['js/controllers/merchant/merchantQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('merchant.queryMerDetail', {
            url: "/queryMerDetail/:mertId",
            templateUrl: "views/merchant/merchantQueryDetail.html",
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
                        files: ['js/controllers/merchant/merchantDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('merchant.MerUpdate', {
            url: "/MerUpdate/:mertId",
            templateUrl: "views/merchant/merchantUpdate.html",
            data: {pageTitle: '修改商户'},
            //controller: "merchantUpdateCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/merchant/merchantUpdateCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('merchant.auditMer', {
            url: "/auditMer",
            templateUrl: "views/merchant/merchantExamine.html",
            data: {pageTitle: '商户审核'},
            //controller: "merchantExamineCtrl",
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
                        files: ['js/controllers/merchant/merchantExamineCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('merchant.migrate', {
            url: "/migrate",
            templateUrl: "views/merchant/merchantMigrate.html",
            data: {pageTitle: '商户迁移'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/merchant/merchantMigrateCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('merchant.addMigrate', {
            url: "/addMigrate",
            templateUrl: "views/merchant/addMerchantMigrate.html",
            data: {pageTitle: '新增商户迁移'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('fileUpload');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/merchant/addMerchantMigrateCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('merchant.migrateCheck', {
            url: "/migrateCheck/:id",
            templateUrl: "views/merchant/migrateCheck.html",
            data: {pageTitle: '商户迁移审核'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ui.select');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/merchant/migrateCheckCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('merchant.migrateDetail', {
            url: "/migrateDetail/:id",
            templateUrl: "views/merchant/migrateDetail.html",
            data: {pageTitle: '商户迁移详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ui.select');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/merchant/migrateDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('merchant.MerExamineDetail', {
            url: "/MerExamineDetail/:mertId",
            templateUrl: "views/merchant/merchantExamineDetail.html",
            data: {pageTitle: '商户审核详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/merchant/merchantExamineDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('merchant.auditorManager', {
            url: "/auditorManager",
            templateUrl: "views/merchant/auditorManager.html",
            data: {pageTitle: '审核人管理'},
            //controller: "auditorManagerCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ui-switch');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/merchant/auditorManagerCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('merchant.auditorManagerSet', {
            url: "/auditorManagerSet",
            templateUrl: "views/merchant/auditorManagerSet.html",
            data: {pageTitle: '设置规则'},
            //controller: "auditorManagerSetCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives')
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ui-switch');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/merchant/auditorManagerSetCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('merchant.auditorRecord', {
            url: "/auditorRecord",
            templateUrl: "views/merchant/auditorRecord.html",
            data: {pageTitle: '审核统计'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/merchant/auditorRecordCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('merchant.perfectMerchantQuery', {
            url: "/perfectMerchantQuery",
            templateUrl: "views/merchant/perfectMerchantQuery.html",
            data: {pageTitle: '完善商户查询'},
            //controller: "perfectMerchantQueryCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/merchant/perfectMerchantQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('merchant.termianlApplyQuery', {
            url: "/termianlApplyQuery",
            templateUrl: 'views/merchant/termianlApplyQuery.html',
            data: {pageTitle: '机具申请查询'},
            //controller: "termianlApplyQueryCtrl",
            resolve: {
	            loadPlugin: function ($ocLazyLoad) {
	            	$ocLazyLoad.load('localytics.directives')
	                $ocLazyLoad.load('ngGrid');
		            $ocLazyLoad.load('datePicker');
	            },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/merchant/termianlApplyQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
         .state('merchant.termianlApplyDetail', {
            url: "/termianlApplyDetail/:id",
            templateUrl: 'views/merchant/termianlApplyDetail.html',
            data: {pageTitle: '机具申请详情'},
            //controller: "termianlApplyDetailCtrl",
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/merchant/termianlApplyDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('merchant.zqMerchantQuery', {
            url: "/zqMerInfoMgr",
            templateUrl: 'views/zqMerchant/zqMerchantQuery.html',
            data: {pageTitle: '直清商户管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
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
                        files: ['js/controllers/zqMerchant/zqMerchantQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('merchant.zqMerchantDetail', {
            url: "/showZqMerInfoDetail/:merServiceId",
            templateUrl: "views/zqMerchant/zqMerInfoDetail.html",
            data: {pageTitle: '直清商户详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/zqMerchant/zqMerInfoDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('merchant.queryMerchantsUpstream', {
            url: "/queryMerchantsUpstream",
            templateUrl: 'views/zqMerchant/merchantsUpstream/queryMerchantsUpstream.html',
            data: {pageTitle: '商户上游账户查询'},
            //controller: "queryMerchantsUpstreamCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/zqMerchant/merchantsUpstream/queryMerchantsUpstreamCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        /*商户冻结查询*/
        .state('merchant.merprefrozen', {
            url: "/merprefrozen",
            templateUrl: "views/merchant/merPreFrozen.html",
            data: {pageTitle: '商户冻结查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/merchant/merPreFrozenCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('merchant.verifiedWarning', {
            url: "/verifiedWarning",
            templateUrl: "views/merchant/verifiedWarning.html",
            data: {pageTitle: '实名认证预警'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {;
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/merchant/verifiedWarningCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('merchant.queryZqServiceInfo', {
            url: "/queryZqServiceInfo",
            data: {pageTitle: '直清商户服务报件查询'},
            templateUrl: 'views/merchant/zqServiceInfo/queryZqServiceInfo.html',
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load ({
                        name: 'inspinia',
                        files: ['js/controllers/merchant/zqServiceInfo/queryZqServiceInfo.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('merchant.queryZqFileSync', {
            url: "/queryZqFileSync",
            data: {pageTitle: '直清商户批量报备查询'},
            templateUrl: 'views/merchant/zqServiceInfo/queryZqFileSync.html',
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('fileUpload');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load ({
                        name: 'inspinia',
                        files: ['js/controllers/merchant/zqServiceInfo/queryZqFileSync.js?ver='+verNo]
                    });
                }]
            }
        })
        /*历史订单查询*/
         .state('histrade', {
            abstract: true,
            url: "/histrade",
            templateUrl: "views/common/content.html",
        })
         .state('histrade.tradeQuery', {
            url: "/histradeQuery",
            data: {pageTitle: '交易查询'},
            templateUrl: 'views/trade/histradeQuery.html',
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
                        files: ['js/controllers/trade/hisqueryTradeCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('histrade.histradeQueryDetail', {
            url: "/histradeQueryDetail/:id/:val",
            templateUrl: 'views/trade/histradeQueryDetail.html',
            data: {pageTitle: '交易详细信息'},
            //controller: "hisqueryTradeDetailCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ngGrid');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/trade/hisqueryTradeDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
         /*出款订单查询*/
        .state('histrade.outOrderQuery', {
            url: "/hisoutOrderQuery",
            data: {pageTitle: '交易查询'},
            templateUrl: 'views/trade/hisoutOrderQuery.html',
            //controller: "hisoutOrderQueryCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ngGrid');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/trade/hisoutOrderQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
         .state('histrade.hisoutOrderDetail', {
            url: "/hisoutOrderDetail/:id",
            templateUrl: 'views/trade/hisoutOrderDetail.html',
            data: {pageTitle: '出款订单详细信息'},
            //controller: "hisoutOrderDetailCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ngGrid');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/trade/hisoutOrderDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
           .state('histrade.outDetailQuery', {
            url: "/hisoutDetailQuery",
            data: {pageTitle: '出款明细查询'},
            templateUrl: 'views/trade/hisoutDetailQuery.html',
            //controller: "hisoutDetailQueryCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ngGrid');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/trade/hisoutDetailQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('histrade.outDetailQueryDetail', {
            url: "/outDetailQueryDetail/:id",
            data: {pageTitle: '出款明细详情'},
            templateUrl: 'views/trade/hisoutDetailQueryDetail.html',
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('ngGrid');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/trade/hisoutDetailQueryDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        /*订单查询*/
        .state('trade', {
            abstract: true,
            url: "/trade",
            templateUrl: "views/common/content.html",
        })
        /*交易查询*/
        .state('trade.tradeQuery', {
            url: "/tradeQuery",
            data: {pageTitle: '交易查询'},
            templateUrl: 'views/trade/tradeQuery.html',
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/trade/queryTradeCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        /*交易预警*/
            .state('trade.tradeWarning', {
                url: "/tradeWarning",
                templateUrl: "views/trade/tradeWarning.html",
                data: {pageTitle: '交易预警'},
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {;
                        $ocLazyLoad.load('infinity-chosen');
                        $ocLazyLoad.load('ngGrid');
                        $ocLazyLoad.load('My97DatePicker');
                        $ocLazyLoad.load('ui.select');
                        $ocLazyLoad.load('oitozero.ngSweetAlert');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/controllers/trade/tradeWarningCtrl.js?ver='+verNo]
                        });
                    }]
                }
            })
        .state('trade.tradeSettleQuery', {
            url: "/tradeSettleQuery",
            data: {pageTitle: '代付订单查询'},
            templateUrl: 'views/trade/tradeSettleQuery.html',
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
                        files: ['js/controllers/trade/queryTradeSettleCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('trade.acqSingleImport', {
            url: "/acqSingleImport",
            templateUrl: "views/trade/acqSingleImport.html",
            data: {pageTitle: '长款导入'},
            //controller: "acqSingleImportCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('fileUpload');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/trade/acqSingleImportCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('trade.tradeQueryDetail', {
            url: "/tradeQueryDetail/:id/:val",
            templateUrl: 'views/trade/tradeQueryDetail.html',
            data: {pageTitle: '交易详细信息'},
            //controller: "queryTradeDetailCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ngGrid');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/trade/queryTradeDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
         .state('trade.tradeFrozen', {
            url: "/tradeFrozen/:id",
            templateUrl: 'views/trade/tradeFrozenModalCtr.html',
            data: {pageTitle: '交易详细信息'},
            //controller: "tradeFrozenCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ngGrid');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/trade/tradeFrozenCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        /*出款订单查询*/
        .state('trade.outOrderQuery', {
            url: "/outOrderQuery",
            data: {pageTitle: '交易查询'},
            templateUrl: 'views/trade/outOrderQuery.html',
            //controller: "outOrderQueryCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ngGrid');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/trade/outOrderQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('trade.outOrderDetail', {
            url: "/outOrderDetail/:id",
            templateUrl: 'views/trade/outOrderDetail.html',
            data: {pageTitle: '出款订单详细信息'},
            //controller: "outOrderDetailCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ngGrid');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/trade/outOrderDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
         .state('trade.outDetailQuery', {
            url: "/outDetailQuery",
            data: {pageTitle: '出款明细查询'},
            templateUrl: 'views/trade/outDetailQuery.html',
            //controller: "outDetailQueryCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ngGrid');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/trade/outDetailQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
         .state('trade.outDetailQueryDetail', {
            url: "/outDetailQueryDetail/:id",
            data: {pageTitle: '出款明细详情'},
            templateUrl: 'views/trade/outDetailQueryDetail.html',
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('ngGrid');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/trade/outDetailQueryDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })

        /*/!*机具管理*!/
        .state('terminal', {
            url: "/terminal",
            views: {
                '': {
                    templateUrl: 'views/common/singleContent.html'
                },
                'mainView@terminal': {
                    templateUrl: 'views/terminal/terminalQuery.html'
                }
            },
            data: {pageTitle: '机具管理'},
            //controller: "terminalQueryCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/terminal/terminalQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('terminalQueryDetail', {
            url: "/terminalQueryDetail/:termId",
            views: {
                '': {
                    templateUrl: 'views/common/singleContent.html'
                },
                'mainView@terminalQueryDetail': {
                    templateUrl: 'views/terminal/terminalQueryDetail.html'
                }
            },
            data: {pageTitle: '机具详细信息'},
            //controller: "terminalDetailCtrl",
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/terminal/terminalDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('terminalUpdate', {
            url: "/terminalUpdate/:termId",
            views: {
                '': {
                    templateUrl: 'views/common/singleContent.html'
                },
                'mainView@terminalUpdate': {
                    templateUrl: 'views/terminal/terminalUpdate.html'
                }
            },
            data: {pageTitle: '机具修改'},
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                	$ocLazyLoad.load('localytics.directives');
                	$ocLazyLoad.load('infinity-chosen');
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/terminal/terminalUpdateOrAddCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('updateAllTerActivity', {
            url: "/updateAllTerActivity/:specialStatus",
            views: {
                '': {
                    templateUrl: 'views/common/singleContent.html'
                },
                'mainView@updateAllTerActivity': {
                    templateUrl: 'views/terminal/updateAllTerActivity.html'
                }
            },
            data: {pageTitle: '修改硬件种类及活动'},
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                	$ocLazyLoad.load('localytics.directives');
                	$ocLazyLoad.load('infinity-chosen');
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/terminal/updateAllTerActivityCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('termianlImport', {
            url: "/termianlImport",
            views: {
                '': {
                    templateUrl: 'views/common/singleContent.html'
                },
                'mainView@termianlImport': {
                    templateUrl: 'views/terminal/termianlImport.html'
                }
            },
            data: {pageTitle: '机具导入'},
            //controller: "termianlImportCtrl",
            resolve: {
            	  loadPlugin: function ($ocLazyLoad) {
            		  $ocLazyLoad.load('infinity-chosen');
                      $ocLazyLoad.load('fileUpload');
                      $ocLazyLoad.load('oitozero.ngSweetAlert');
                  },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/terminal/termianlImportCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('termianlBindBatch', {
            url: "/termianlBindBatch",
            views: {
                '': {
                    templateUrl: 'views/common/singleContent.html'
                },
                'mainView@termianlBindBatch': {
                    templateUrl: 'views/terminal/termianlBindBatch.html'
                }
            },
            data: {pageTitle: '机具批量绑定'},
//            //controller: "termianlBindBatchCtrl",
            resolve: {
            	  loadPlugin: function ($ocLazyLoad) {
            		  $ocLazyLoad.load('infinity-chosen');
                      $ocLazyLoad.load('fileUpload');
                      $ocLazyLoad.load('oitozero.ngSweetAlert');
                  },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/terminal/termianlBindBatchCtrl.js?ver='+verNo]
                    });
                }]
            }
        })*/

        /*机具管理*/
        .state('terminal', {
            url: "/terminal",
            templateUrl: "views/common/content.html"
        })
        .state('terminal.terminalQuery', {
            url: "/terminalQuery",
            data: {pageTitle: '机具管理'},
            templateUrl: 'views/terminal/terminalQuery.html',
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/terminal/terminalQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('terminal.terActivityCheck', {
            url: "/terActivityCheck",
            data: {pageTitle: '活动考核机具'},
            templateUrl: 'views/terminal/terActivityCheck.html',
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/terminal/terActivityCheckCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('terminalQueryDetail', {
            url: "/terminalQueryDetail/:termId",
            views: {
                '': {
                    templateUrl: 'views/common/singleContent.html'
                },
                'mainView@terminalQueryDetail': {
                    templateUrl: 'views/terminal/terminalQueryDetail.html'
                }
            },
            data: {pageTitle: '机具详细信息'},
            //controller: "terminalDetailCtrl",
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/terminal/terminalDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('terminalUpdate', {
            url: "/terminalUpdate/:termId",
            views: {
                '': {
                    templateUrl: 'views/common/singleContent.html'
                },
                'mainView@terminalUpdate': {
                    templateUrl: 'views/terminal/terminalUpdate.html'
                }
            },
            data: {pageTitle: '机具修改'},
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                	$ocLazyLoad.load('localytics.directives');
                	$ocLazyLoad.load('infinity-chosen');
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/terminal/terminalUpdateOrAddCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('updateAllTerActivity', {
            url: "/updateAllTerActivity/:specialStatus",
            views: {
                '': {
                    templateUrl: 'views/common/singleContent.html'
                },
                'mainView@updateAllTerActivity': {
                    templateUrl: 'views/terminal/updateAllTerActivity.html'
                }
            },
            data: {pageTitle: '修改硬件种类及活动'},
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                	$ocLazyLoad.load('localytics.directives');
                	$ocLazyLoad.load('infinity-chosen');
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/terminal/updateAllTerActivityCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('termianlImport', {
            url: "/termianlImport",
            views: {
                '': {
                    templateUrl: 'views/common/singleContent.html'
                },
                'mainView@termianlImport': {
                    templateUrl: 'views/terminal/termianlImport.html'
                }
            },
            data: {pageTitle: '机具导入'},
            //controller: "termianlImportCtrl",
            resolve: {
            	  loadPlugin: function ($ocLazyLoad) {
            		  $ocLazyLoad.load('infinity-chosen');
                      $ocLazyLoad.load('fileUpload');
                      $ocLazyLoad.load('oitozero.ngSweetAlert');
                  },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/terminal/termianlImportCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('termianlBindBatch', {
            url: "/termianlBindBatch",
            views: {
                '': {
                    templateUrl: 'views/common/singleContent.html'
                },
                'mainView@termianlBindBatch': {
                    templateUrl: 'views/terminal/termianlBindBatch.html'
                }
            },
            data: {pageTitle: '机具批量绑定'},
//            //controller: "termianlBindBatchCtrl",
            resolve: {
            	  loadPlugin: function ($ocLazyLoad) {
            		  $ocLazyLoad.load('infinity-chosen');
                      $ocLazyLoad.load('fileUpload');
                      $ocLazyLoad.load('oitozero.ngSweetAlert');
                  },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/terminal/termianlBindBatchCtrl.js?ver='+verNo]
                    });
                }]
            }
        })


        .state('addSecret', {
            url: "/addSecret",
            views: {
                '': {
                    templateUrl: 'views/common/singleContent.html'
                },
                'mainView@addSecret': {
                    templateUrl: 'views/terminal/addSecret.html'
                }
            },
            data: {pageTitle: '生成密钥'},
            //controller: "termianlSecretCtrl",
            resolve: {
            	 loadPlugin: function ($ocLazyLoad) {
                     $ocLazyLoad.load('localytics.directives');
                     $ocLazyLoad.load('oitozero.ngSweetAlert');
                 },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/terminal/terminalSecretCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        /*收款吗管理*/
        .state('gatherCodeManager', {
            url: "/gatherCodeManager/id",
            views: {
                '': {
                    templateUrl: 'views/common/singleContent.html'
                },
                'mainView@gatherCodeManager': {
                    templateUrl: 'views/terminal/gatherCodeManager.html'
                }
            },
            data: {pageTitle: '收款码管理'},
            //controller: "gatherCodeManagerCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/terminal/gatherCodeManagerCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        /*收款码详情*/
        .state('gatherCodeDetail', {
            url: "/gatherCodeDetail/:id",
            views: {
                '': {
                    templateUrl: 'views/common/singleContent.html'
                },
                'mainView@gatherCodeDetail': {
                    templateUrl: 'views/terminal/gatherCodeDetail.html'
                }
            },
            data: {pageTitle: '收款码详情'},
            //controller: "gatherCodeDetailCtrl",
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/terminal/gatherCodeDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        /*我的信息*/
        .state('myInfo', {
            abstract: true,
            url: "/myInfo",
            templateUrl: "views/common/content.html",
        })
        .state('myInfo.info', {
            url: "/info",
            templateUrl: "views/icons.html",
            data: {pageTitle: '我的信息'}
        })
        .state('myInfo.account', {
            url: "/account",
            templateUrl: "views/icons.html",
            data: {pageTitle: '我的账户'}
        })
        /*收单机构*/
        .state('org', {
            abstract: true,
            url: "/org",
            templateUrl: "views/common/content.html",
        })
        .state('org.groupService', {
            url: "/groupService",
            templateUrl: "views/org/groupService.html",
            data: {pageTitle: '收单服务管理'},
            //controller: "groupServiceCtrl",
            resolve: {
            	loadPlugin: function ($ocLazyLoad) {
            		$ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('ui-switch');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/org/groupServiceCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('org.addGroupService', {
            url: "/addGroupService",
            views: {
                '': {
                    templateUrl: 'views/org/addGroupService.html'
                },
                'serviceBaseInfo@org.addGroupService': {
                    templateUrl: 'views/org/serviceBaseInfo.html'
                },
                'serviceQuotaInfo@org.addGroupService': {
                    templateUrl: 'views/org/serviceQuotaInfo.html'
                },
                'serviceCommonQuotaInfo@org.addGroupService': {
                    templateUrl: 'views/org/serviceCommonQuotaInfo.html'
                }
            },
            data: {pageTitle: '增加收单服务'},
            //controller: "addGroupServiceCtrl",
            resolve: {
            	loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('colorpicker.module');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/org/addGroupServiceCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('org.groupServiceDetail', {
            url: "/serviceDetail/:id",
            templateUrl: "views/org/groupServiceDetail.html",
            data: {pageTitle: '收单服务详情'},
            //controller: "groupServiceDetailCtrl",
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/org/groupServiceDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('org.updateServiceRate', {
            url: "/updateServiceRate/:id",
            templateUrl: "views/org/updateServiceRate.html",
            data: {pageTitle: '修改收单服务费率'},
            //controller: "updateServiceRateCtrl",
            resolve: {
            	loadPlugin: function ($ocLazyLoad) {
            		$ocLazyLoad.load('datePicker');
            		$ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/org/updateServiceRateCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('org.updateServiceQuota', {
            url: "/updateServiceQuota/:id/:quotaIsCard",
            views: {
                '': {
                    templateUrl: 'views/org/updateServiceQuota.html'
                },
                'serviceQuotaInfo@org.updateServiceQuota': {
                    templateUrl: 'views/org/serviceQuotaInfo.html'
                },
                'serviceCommonQuotaInfo@org.updateServiceQuota': {
                    templateUrl: 'views/org/serviceCommonQuotaInfo.html'
                }
            },
            data: {pageTitle: '修改收单服务限额'},
            //controller: "updateServiceQuotaCtrl",
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/org/updateServiceQuotaCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        /*收单预警人员管理*/
        .state('org.csWarningPeople', {
            url: "/csWarningPeople",
            data: {pageTitle: '收单预警人员管理'},
            views: {
                '': {
                    templateUrl: 'views/org/warningPeople/collectingWarningPeople.html'
                },
                'warningPeopleCenter@org.csWarningPeople':{
                    templateUrl: 'views/org/warningPeople/warningPeopleCenter.html'
                }
            },
            //controller: "collectingWarningPeopleCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('infinity-chosen')
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad){
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/org/warningPeople/collectingWarningPeopleCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        /* 收单服务任务设置*/
        .state('org.setAcqServiceWarningPeople', {
            url : "/setAcqServiceWarningPeople/:id",
            templateUrl : "views/org/warningPeople/setAcqServiceWarningPeople.html",
            data : {
                pageTitle : '设置收单服务任务'
            },
            // controller : "setAcqServiceWarningPeopleCtrl",
            resolve : {
                deps : ['$ocLazyLoad', function($ocLazyLoad) {
                    return $ocLazyLoad.load({name : 'inspinia',
                        files : [ 'js/controllers/org/warningPeople/setAcqServiceWarningPeopleCtrl.js?ver='+verNo ]
                    });
                }]
            }
        })
        .state('org.warningSet', {
            url: "/warningSet",
            data: {pageTitle: '预警阀值设置'},
            templateUrl: 'views/org/warningSet/warningSet.html',
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('colorpicker.module');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load ({
                        name: 'inspinia',
                        files: ['js/controllers/org/warningSet/warningSet.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('org.addWarningSet', {
            url: "/addWarningSet",
            data: {pageTitle: '新增预警阀值设置'},
            templateUrl: 'views/org/warningSet/addWarningSet.html',
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('colorpicker.module');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load ({
                        name: 'inspinia',
                        files: ['js/controllers/org/warningSet/addWarningSet.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('org.managerRoute', {
            url: "/managerRoute",
            templateUrl: "views/org/managerRoute.html",
            data: {pageTitle: '路由集群管理'},
            //controller: "managerRouteCtrl",
            resolve: {
            	loadPlugin: function ($ocLazyLoad) {
            		$ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('fileUpload');
            		    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/org/managerRouteCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('org.addRouteGroup', {
            url: "/addRouteGroup",
            views: {
                '': {
                    templateUrl: 'views/org/addRouteGroup.html'
                },
                'routeGroupBaseInfo@org.addRouteGroup': {
                    templateUrl: 'views/org/routeGroupBaseInfo.html'
                }
            },
            data: {pageTitle: '增加路由集群'},
            //controller: "addRouteGroupCtrl",
            resolve: {
            	loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('colorpicker.module');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/org/addRouteGroupCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('org.updateRouteGroup', {
            url: "/updateRouteGroup/:id",
            views: {
                '': {
                    templateUrl: 'views/org/updateRouteGroup.html'
                },
                'routeGroupBaseInfo@org.updateRouteGroup': {
                    templateUrl: 'views/org/routeGroupBaseInfo.html'
                }
            },
            data: {pageTitle: '修改路由集群'},
            //controller: "updateRouteGroupCtrl",
            resolve: {
            	loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('colorpicker.module')
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/org/updateRouteGroupCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('org.routeGroupDetail', {
            url: "/routeGroupDetail/:id",
            templateUrl: 'views/org/routeGroupDetail.html',
            data: {pageTitle: '路由集群详情'},
            //controller: "routeGroupDetailCtrl",
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/org/routeGroupDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('org.routeMer', {
            url: "/routeMer",
            templateUrl: "views/org/routeMer.html",
            data: {pageTitle: '集群中普通商户'},
            //controller: "routeMerCtrl",
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/org/routeMerCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('org.groupMerTransfer', {
            url: "/groupMerTransfer",
            templateUrl: "views/org/groupMerTransfer.html",
            data: {pageTitle: '集群中普通商户转移'},
            //controller: "groupMerTransfer",
            resolve: {
            	loadPlugin: function ($ocLazyLoad) {
            		$ocLazyLoad.load('infinity-chosen')
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/org/groupMerTransferCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('org.routerOrg', {
            url: "/routerOrg",
            templateUrl: "views/org/routerOrg.html",
            data: {pageTitle: '集群中收单商户'},
            //controller: "routerOrgCtrl",
            resolve: {
            	loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/org/routerOrgCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
            .state('org.routerOrgBatchDelete', {
                url: "/routerOrgBatchDelete",
                templateUrl: "views/org/routerOrg/routerOrgBatchDelete.html",
                data: {pageTitle: '导入删除'},
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('fileUpload');
                        $ocLazyLoad.load('oitozero.ngSweetAlert');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/controllers/org/routerOrg/routerOrgBatchDeleteCtrl.js?ver='+verNo]
                        });
                    }]
                }
            })
        .state('org.acqOrg', {
            url: "/acqOrg",
            templateUrl: "views/org/acqOrg.html",
            data: {pageTitle: '收单机构管理'},
            //controller: "acqOrgCtrl",
            resolve: {
            	loadPlugin: function ($ocLazyLoad) {
            		$ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ui-switch');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/org/acqOrgCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        //====收单机构管理新增====================================================================
         .state('org.addAcqOrg', {
            url: "/addAcqOrg",
            templateUrl: "views/org/addAcqOrg.html",
            data: {pageTitle: '收单机构新增'},
            //controller: "addAcqOrgCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives')
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('colorpicker.module');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/org/addAcqOrgCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('org.configuraDetail', {
            url: "/configuraDetail/:id",
            templateUrl: "views/org/acqOrgDetail.html",
            data: {pageTitle: '收单机构配置详情'},
            //controller: "acqOrgDetailCtrl",
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/org/acqOrgDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('org.updateConfigura', {
            url: "/updateConfigura/:id",
            templateUrl: "views/org/acqOrgUpdate.html",
            data: {pageTitle: '收单机构修改配置'},
            //controller: "acqOrgUpdateCtrl",
            resolve: {
            	 loadPlugin: function ($ocLazyLoad) {
                     $ocLazyLoad.load('datePicker');
                     $ocLazyLoad.load('colorpicker.module');
                 },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/org/acqOrgUpdateCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('org.orgMer', {
            url: "/orgMer",
            templateUrl: "views/org/acqMerchant.html",
            data: {pageTitle: '收单机构商户'},
            //controller: "acqMerchantCtrl",
            resolve: {
            	loadPlugin: function ($ocLazyLoad) {
            		  $ocLazyLoad.load('infinity-chosen')
                      $ocLazyLoad.load('ui.select');
                      $ocLazyLoad.load('fileUpload');
                      $ocLazyLoad.load('ui-switch');
                      $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/org/acqMerchantCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
            .state('org.acqMerBatchColse', {
                url: "/acqMerBatchColse",
                templateUrl: "views/org/acqMerchant/acqMerBatchColse.html",
                data: {pageTitle: '批量关闭'},
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('fileUpload');
                        $ocLazyLoad.load('oitozero.ngSweetAlert');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/controllers/org/acqMerchant/acqMerBatchColseCtrl.js?ver='+verNo]
                        });
                    }]
                }
            })
          .state('org.orgIn', {
            url: "/orgIn",
            templateUrl: "views/org/acqIn.html",
            data: {pageTitle: '收单商户进件管理'},
            resolve: {
            	loadPlugin: function ($ocLazyLoad) {
            		  $ocLazyLoad.load('infinity-chosen')
                      $ocLazyLoad.load('ui.select');
                      $ocLazyLoad.load('fileUpload');
                      $ocLazyLoad.load('ui-switch');
                      $ocLazyLoad.load('oitozero.ngSweetAlert');
                      $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/org/acqInCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('org.acqMerchantDetail', {
           url: "/acqMerchantDetail/:id",
           templateUrl: "views/org/acqMerchantDetail.html",
           data: {pageTitle: '收单机构商户详情'},
           //controller: "acqMerchantDetailCtrl",
           resolve: {
               deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                   return $ocLazyLoad.load({
                       name: 'inspinia',
                       files: ['js/controllers/org/acqMerchantDetailCtrl.js?ver='+verNo]
                   });
               }]
           }
       })
       .state('org.acqInDetail', {
           url: "/acqInDetail/:id/:flag",
           templateUrl: "views/org/acqInDetail.html",
           data: {pageTitle: '收单机构商户详情'},
           //controller: "acqMerchantDetailCtrl",
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
                       files: ['js/controllers/org/acqInDetailCtrl.js?ver='+verNo]
                   });
               }]
           }
       })
        .state('org.acqMerchantUp', {
           url: "/acqMerchantUp/:id",
           templateUrl: "views/org/acqMerchantUp.html",
           data: {pageTitle: '收单机构商户修改'},
           //controller: "acqMerchantUpCtrl",
           resolve: {
        	   loadPlugin: function ($ocLazyLoad) {
         		  $ocLazyLoad.load('localytics.directives')
                   $ocLazyLoad.load('datePicker');
                   $ocLazyLoad.load('ui.select');
        	   },
               deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                   return $ocLazyLoad.load({
                       name: 'inspinia',
                       files: ['js/controllers/org/acqMerchantUpCtrl.js?ver='+verNo]
                   });
               }]
           }
       })
        .state('org.acqMerchantAdd', {
           url: "/acqMerchantAdd",
           templateUrl: "views/org/acqMerchantAdd.html",
           data: {pageTitle: '收单机构商户新增'},
           //controller: "acqMerchantAddCtrl",
           resolve: {
        	   loadPlugin: function ($ocLazyLoad) {
         		  $ocLazyLoad.load('localytics.directives')
                   $ocLazyLoad.load('datePicker');
                   $ocLazyLoad.load('ui.select');
        	   },
               deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                   return $ocLazyLoad.load({
                       name: 'inspinia',
                       files: ['js/controllers/org/acqMerchantAddCtrl.js?ver='+verNo]
                   });
               }]
           }
       })
        .state('org.acqMerchantimportCtrl', {
            url: "/acqMerchantimportCtrl",
            templateUrl: "views/org/acqMerchantImprot.html",
            data: {pageTitle: '机具导入'},
            //controller: "acqMerchantimportCtrl",
            resolve: {
            	  loadPlugin: function ($ocLazyLoad) {
                      $ocLazyLoad.load('fileUpload');
                  },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/org/acqMerchantimportCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('org.orgTerminal', {
            url: "/orgTerminal",
            templateUrl: "views/org/acqTerminal.html",
            data: {pageTitle: '收单机构终端'},
            //controller: "acqTerminalCtrl",
            resolve: {
            	loadPlugin: function ($ocLazyLoad) {
            		  $ocLazyLoad.load('infinity-chosen')
                      $ocLazyLoad.load('ui.select');
                      $ocLazyLoad.load('ui-switch');
                      $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/org/acqTerminalCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('org.orgTerminalDetail', {
            url: "/orgTerminalDetail/:id",
            templateUrl: "views/org/acqTerminalDetail.html",
            data: {pageTitle: '收单机构终端详情'},
            //controller: "acqTerminalDetailCtrl",
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/org/acqTerminalDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('org.acqTerminalUp', {
            url: "/acqTerminalUp/:id",
            templateUrl: "views/org/acqTerminalUp.html",
            data: {pageTitle: '收单机构终端修改'},
            //controller: "acqTerminalUpCtrl",
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/org/acqTerminalUpCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('org.bpClusterQuery', {
            url: "/bpClusterQuery",
            templateUrl: "views/org/businessProductsClusterQuery.html",
            data: {pageTitle: '业务产品默认集群查询'},
	        //controller: "bpClusterCtrl",
	        resolve: {
	            deps: ['$ocLazyLoad', function ($ocLazyLoad) {
	                return $ocLazyLoad.load({
	                    name: 'inspinia',
	                    files: ['js/controllers/org/bpClusterCtrl.js?ver='+verNo]
	                });
	            }]
	        }
        })
        .state('org.bpClusterUpOrAdd', {
            url: "/bpClusterUpOrAdd/:id",
            templateUrl: "views/org/businessProductsClusterUpOrAdd.html",
            data: {pageTitle: '业务产品默认集群修改和新增'},
	        //controller: "bpClusterUpOrAddCtrl",
	        resolve: {
	            deps: ['$ocLazyLoad', function ($ocLazyLoad) {
	                return $ocLazyLoad.load({
	                    name: 'inspinia',
	                    files: ['js/controllers/org/bpClusterUpOrAddCtrl.js?ver='+verNo]
	                });
	            }]
	        }
        })
        .state('org.bpClusterDetail', {
            url: "/bpClusterDetail/:id",
            templateUrl: "views/org/businessProductsClusterDetail.html",
            data: {pageTitle: '业务产品默认集群详情'},
	        //controller: "bpClusterDetailCtrl",
	        resolve: {
	            deps: ['$ocLazyLoad', function ($ocLazyLoad) {
	                return $ocLazyLoad.load({
	                    name: 'inspinia',
	                    files: ['js/controllers/org/bpClusterDetailCtrl.js?ver='+verNo]
	                });
	            }]
	        }
        })
        .state('org.jumpRoute', {
            url: "/jumpRoute/page.do",
            templateUrl: "views/org/jumpRoute.html",
            data: {pageTitle: '按交易跳转集群设置'},
            resolve: {
            	 loadPlugin: function ($ocLazyLoad) {
                     $ocLazyLoad.load('ui-switch');
                     $ocLazyLoad.load('oitozero.ngSweetAlert');
                     $ocLazyLoad.load('localytics.directives');
                     $ocLazyLoad.load('datePicker');
                     $ocLazyLoad.load('infinity-chosen');
                 },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/org/jumpRouteCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('org.jumpRouteAdd', {
            url: "/jumpRoute/jumpRouteAdd",
            views: {
                '': {
                    templateUrl: 'views/org/jumpRouteAdd.html'
                },
                'jumpRouteBase@org.jumpRouteAdd': {
                    templateUrl: 'views/org/jumpRouteBase.html'
                }
            },
            data: {pageTitle: '新增交易跳转集群设置'},
            ////controller: "jumpRouteAddCtrl",
            resolve: {
            	 loadPlugin: function ($ocLazyLoad) {
                     $ocLazyLoad.load('datePicker');
                     $ocLazyLoad.load('ui-switch');
                     $ocLazyLoad.load('oitozero.ngSweetAlert');
                     $ocLazyLoad.load('localytics.directives');
                     $ocLazyLoad.load('colorpicker.module');
                 },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/org/jumpRouteAddCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('org.jumpRouteUpdate', {
            url: "/jumpRoute/jumpRouteUpdate/:id/:type",
            views: {
                '': {
                    templateUrl: 'views/org/jumpRouteUpdate.html'
                },
                'jumpRouteBase@org.jumpRouteUpdate': {
                    templateUrl: 'views/org/jumpRouteBase.html'
                }
            },
            data: {pageTitle: '修改交易跳转集群设置'},
            //controller: "jumpRouteUpdateCtrl",
            resolve: {
            	 loadPlugin: function ($ocLazyLoad) {
                     $ocLazyLoad.load('datePicker');
                     $ocLazyLoad.load('ui-switch');
                     $ocLazyLoad.load('oitozero.ngSweetAlert');
                     $ocLazyLoad.load('localytics.directives');
                     $ocLazyLoad.load('colorpicker.module');
                 },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/org/jumpRouteUpdateCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('org.jumpRouteDetail', {
            url: "/jumpRoute/jumpRouteDetail/:id/:type",
            templateUrl: "views/org/jumpRouteDetail.html",
            data: {pageTitle: '按交易跳转集群设置详情'},
            //controller: "jumpRouteUpdateCtrl",
            resolve: {
            	 loadPlugin: function ($ocLazyLoad) {
                     $ocLazyLoad.load('datePicker');
                     $ocLazyLoad.load('ui-switch');
                     $ocLazyLoad.load('oitozero.ngSweetAlert');
                     $ocLazyLoad.load('localytics.directives');
                     $ocLazyLoad.load('colorpicker.module');
                 },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/org/jumpRouteUpdateCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('org.targetAmountWarning', {
            url: "/jumpRoute/targetAmountWarning",
            templateUrl: "views/org/targetAmountWarning.html",
            data: {pageTitle: '目标金额预警'},
            //controller: "jumpRouteUpdateCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/org/targetAmountWarningCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('org.queryBlacklistAmount', {
            url: "/queryBlacklistAmount",
            data: {pageTitle: '金额黑名单查询'},
            templateUrl: 'views/org/queryBlacklistAmount.html',
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load ({
                        name: 'inspinia',
                        files: ['js/controllers/org/queryBlacklistAmount.js?ver='+verNo]
                    });
                }]
            }
        })


        // 三方数据查询=============================================================================
        .state('threeData', {
            abstract: true,
            url: "/threeData",
            templateUrl: "views/common/content.html"
        })
        .state('threeData.TradeSumInfo', {
            url: "/TradeSumInfo",
            templateUrl: "views/threeData/TradeSumInfoQuery.html",
            data: {pageTitle: '交易汇总报表'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/threeData/TradeSumInfoQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
}