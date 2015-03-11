'use strict';

angular.module('dutyappApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
