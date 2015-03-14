'use strict';

angular.module('dutyappApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('watchman', {
                parent: 'entity',
                url: '/watchman',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'dutyappApp.watchman.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/watchman/watchmans.html',
                        controller: 'WatchmanController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('watchman');
                        return $translate.refresh();
                    }]
                }
            })
            .state('watchmanDetail', {
                parent: 'entity',
                url: '/watchman/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'dutyappApp.watchman.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/watchman/watchman-detail.html',
                        controller: 'WatchmanDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('watchman');
                        return $translate.refresh();
                    }]
                }
            });
    });
