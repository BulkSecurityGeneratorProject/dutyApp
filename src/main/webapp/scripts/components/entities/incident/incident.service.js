'use strict';

angular.module('dutyappApp')
    .factory('Incident', function ($resource) {
        return $resource('api/incidents/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.create_time = new Date(data.create_time);
                    data.ack_time = new Date(data.ack_time);
                    data.resolve_time = new Date(data.resolve_time);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
