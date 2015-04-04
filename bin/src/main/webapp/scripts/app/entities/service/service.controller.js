'use strict';

angular.module('dutyappApp')
    .controller('ServiceController', function ($scope, Service, EscalationPolicy, ParseLinks) {
        $scope.success = null;
        $scope.error = null;

        $scope.services = [];
        $scope.escalationpolicys = EscalationPolicy.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            Service.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.services = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.create = function () {
            $scope.error = null;

            Service.update($scope.service,
                function () {
                    $scope.loadAll();
                    $('#saveServiceModal').modal('hide');
                    $scope.clear();
                }).$promise.then(function () {
                    $scope.success = 'OK';
                }).catch(function (response) {
                    $scope.success = null;
                    if (response.status == 400)
                        $scope.success = 'ERROR';
                });
        };

        $scope.update = function (id) {
            Service.get({id: id}, function(result) {
                $scope.service = result;
                $('#saveServiceModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            Service.get({id: id}, function(result) {
                $scope.service = result;
                $('#deleteServiceConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Service.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteServiceConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.service = {user_id: null, service_name: null, api_key: null, service_type: null, is_deleted: null, id: null};
        };
    });
