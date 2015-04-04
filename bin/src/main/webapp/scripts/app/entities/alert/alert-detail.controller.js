'use strict';

angular.module('dutyappApp')
    .controller('AlertDetailController', function ($scope, $stateParams, Alert, Incident, User) {
        $scope.alert = {};
        $scope.load = function (id) {
            Alert.get({id: id}, function(result) {
              $scope.alert = result;
            });
        };
        $scope.load($stateParams.id);
    });
