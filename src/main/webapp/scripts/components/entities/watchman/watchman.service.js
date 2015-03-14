'use strict';

angular.module('dutyappApp')
    .factory('Watchman', function ($resource) {
        return $resource('api/watchmans/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
