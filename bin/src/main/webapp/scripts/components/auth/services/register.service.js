'use strict';

angular.module('dutyappApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


