'use strict';

angular.module('dutyappApp')
    .controller('WatchmanController', function ($scope, Watchman, ParseLinks) {
        $scope.watchmans = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Watchman.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.watchmans = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.create = function () {
            Watchman.update($scope.watchman,
                function () {
                    $scope.loadAll();
                    $('#saveWatchmanModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            Watchman.get({id: id}, function(result) {
                $scope.watchman = result;
                $('#saveWatchmanModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            Watchman.get({id: id}, function(result) {
                $scope.watchman = result;
                $('#deleteWatchmanConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Watchman.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteWatchmanConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.watchman = {email: null, password: null, id: null};
        };
    });
