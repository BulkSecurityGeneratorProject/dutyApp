'use strict';

angular.module('dutyappApp')
    .controller('IncidentDetailController', function ($scope, $stateParams, Incident, Service, Alert, User, Assign_user) {
        $scope.incident = {};
        $scope.load = function (id) {
            Incident.get({id: id}, function(result) {
              $scope.incident = result;
            });
        };
        $scope.load($stateParams.id);
    });
