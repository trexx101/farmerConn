(function() {
    'use strict';

    angular
        .module('farmerConnApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('supplier', {
            parent: 'entity',
            url: '/supplier',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Suppliers'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/supplier/suppliers.html',
                    controller: 'SupplierController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('supplier-detail', {
            parent: 'entity',
            url: '/supplier/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Supplier'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/supplier/supplier-detail.html',
                    controller: 'SupplierDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Supplier', function($stateParams, Supplier) {
                    return Supplier.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'supplier',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('supplier-detail.edit', {
            parent: 'supplier-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/supplier/supplier-dialog.html',
                    controller: 'SupplierDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Supplier', function(Supplier) {
                            return Supplier.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('supplier.new', {
            parent: 'supplier',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/supplier/supplier-dialog.html',
                    controller: 'SupplierDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                firstName: null,
                                lastName: null,
                                businessName: null,
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
                    $state.go('supplier', null, { reload: 'supplier' });
                }, function() {
                    $state.go('supplier');
                });
            }]
        })
        .state('supplier.edit', {
            parent: 'supplier',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/supplier/supplier-dialog.html',
                    controller: 'SupplierDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Supplier', function(Supplier) {
                            return Supplier.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('supplier', null, { reload: 'supplier' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('supplier.delete', {
            parent: 'supplier',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/supplier/supplier-delete-dialog.html',
                    controller: 'SupplierDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Supplier', function(Supplier) {
                            return Supplier.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('supplier', null, { reload: 'supplier' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
