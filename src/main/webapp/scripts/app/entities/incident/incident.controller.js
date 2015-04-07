'use strict';

angular.module('dutyappApp')
    .controller('IncidentController', function ($scope, Incident, Service, Alert, User, ParseLinks) {
        $scope.incidents = [];
        $scope.services = Service.query();
        $scope.alerts = Alert.query();
        $scope.users = User.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            Incident.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.incidents = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.create = function (id) {
            if (id == null) {
                Incident.save($scope.incident,
                    function () {
                        $scope.loadAll();
                        $('#saveIncidentModal').modal('hide');
                        $scope.clear();
                    });
            } else {
                Incident.update($scope.incident,
                    function () {
                        $scope.loadAll();
                        $('#saveIncidentModal').modal('hide');
                        $scope.clear();
                    });
            }
        };

        $scope.update = function (id) {
            Incident.get({id: id}, function(result) {
                $scope.incident = result;
                $('#saveIncidentModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            Incident.get({id: id}, function(result) {
                $scope.incident = result;
                $('#deleteIncidentConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Incident.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteIncidentConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.incident = {create_time: null, state: null, ack_time: null, resolve_time: null, description: null, detail: null, incident_no: null, id: null};
        };
    });
