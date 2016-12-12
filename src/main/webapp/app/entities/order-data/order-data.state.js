(function() {
    'use strict';

    angular
        .module('farmerConnApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('order-data', {
            parent: 'entity',
            url: '/order-data',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'OrderData'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/order-data/order-data.html',
                    controller: 'OrderDataController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('order-data-detail', {
            parent: 'entity',
            url: '/order-data/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'OrderData'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/order-data/order-data-detail.html',
                    controller: 'OrderDataDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'OrderData', function($stateParams, OrderData) {
                    return OrderData.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'order-data',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('order-data-detail.edit', {
            parent: 'order-data-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/order-data/order-data-dialog.html',
                    controller: 'OrderDataDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['OrderData', function(OrderData) {
                            return OrderData.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('order-data.new', {
            parent: 'order-data',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/order-data/order-data-dialog.html',
                    controller: 'OrderDataDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                price: null,
                                quantity: null,
                                shipDate: null,
                                billDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('order-data', null, { reload: 'order-data' });
                }, function() {
                    $state.go('order-data');
                });
            }]
        })
        .state('order-data.edit', {
            parent: 'order-data',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/order-data/order-data-dialog.html',
                    controller: 'OrderDataDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['OrderData', function(OrderData) {
                            return OrderData.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('order-data', null, { reload: 'order-data' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('order-data.delete', {
            parent: 'order-data',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/order-data/order-data-delete-dialog.html',
                    controller: 'OrderDataDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['OrderData', function(OrderData) {
                            return OrderData.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('order-data', null, { reload: 'order-data' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
