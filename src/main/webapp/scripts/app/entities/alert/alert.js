'use strict';

angular.module('dutyappApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('alert', {
                parent: 'entity',
                url: '/alert',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'dutyappApp.alert.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/alert/alerts.html',
                        controller: 'AlertController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('alert');
                        return $translate.refresh();
                    }]
                }
            })
            .state('alertDetail', {
                parent: 'entity',
                url: '/alert/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'dutyappApp.alert.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/alert/alert-detail.html',
                        controller: 'AlertDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('alert');
                        return $translate.refresh();
                    }]
                }
            });
    });
