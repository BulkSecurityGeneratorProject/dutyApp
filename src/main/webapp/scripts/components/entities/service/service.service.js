'use strict';

angular.module('dutyappApp')
    .factory('Service', function ($resource) {
        return $resource('api/services/:id', {id:'@id'}, {
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
