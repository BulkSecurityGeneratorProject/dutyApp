'use strict';

angular.module('dutyappApp')
    .controller('WatchmanDetailController', function ($scope, $stateParams, Watchman) {
        $scope.watchman = {};
        $scope.load = function (id) {
            Watchman.get({id: id}, function(result) {
              $scope.watchman = result;
            });
        };
        $scope.load($stateParams.id);
    });
