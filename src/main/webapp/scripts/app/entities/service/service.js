'use strict';

angular.module('dutyappApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('service', {
                parent: 'entity',
                url: '/service',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'dutyappApp.service.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/service/services.html',
                        controller: 'ServiceController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('service');
                        return $translate.refresh();
                    }]
                }
            })
            .state('serviceDetail', {
                parent: 'entity',
                url: '/service/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'dutyappApp.service.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/service/service-detail.html',
                        controller: 'ServiceDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('service');
                        return $translate.refresh();
                    }]
                }
            });
    });
