(function() {
    'use strict';

    angular
        .module('farmerConnApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('customers', {
            parent: 'entity',
            url: '/customers',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Customers'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/customers/customers.html',
                    controller: 'CustomersController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('customers-detail', {
            parent: 'entity',
            url: '/customers/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Customers'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/customers/customers-detail.html',
                    controller: 'CustomersDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Customers', function($stateParams, Customers) {
                    return Customers.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'customers',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('customers-detail.edit', {
            parent: 'customers-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/customers/customers-dialog.html',
                    controller: 'CustomersDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Customers', function(Customers) {
                            return Customers.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('customers.new', {
            parent: 'customers',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/customers/customers-dialog.html',
                    controller: 'CustomersDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                firstName: null,
                                lastName: null,
                                login: null,
                                email: null,
                                phone: null,
                                address1: null,
                                address2: null,
                                postalCode: null,
                                city: null,
                                stateProvince: null,
                                dateEntered: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('customers', null, { reload: 'customers' });
                }, function() {
                    $state.go('customers');
                });
            }]
        })
        .state('customers.edit', {
            parent: 'customers',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/customers/customers-dialog.html',
                    controller: 'CustomersDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Customers', function(Customers) {
                            return Customers.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('customers', null, { reload: 'customers' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('customers.delete', {
            parent: 'customers',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/customers/customers-delete-dialog.html',
                    controller: 'CustomersDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Customers', function(Customers) {
                            return Customers.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('customers', null, { reload: 'customers' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
