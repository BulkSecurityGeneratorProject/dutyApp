'use strict';

angular.module('dutyappApp')
    .controller('AlertController', function ($scope, Alert, Incident, User, ParseLinks) {
        $scope.alerts = [];
        $scope.incidents = Incident.query();
        $scope.users = User.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            Alert.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.alerts = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.create = function () {
            Alert.update($scope.alert,
                function () {
                    $scope.loadAll();
                    $('#saveAlertModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            Alert.get({id: id}, function(result) {
                $scope.alert = result;
                $('#saveAlertModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            Alert.get({id: id}, function(result) {
                $scope.alert = result;
                $('#deleteAlertConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Alert.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteAlertConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.alert = {alert_time: null, id: null};
        };
    });
