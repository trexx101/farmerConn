(function() {
    'use strict';

    angular
        .module('farmerConnApp')
        .controller('OrdersDetailController', OrdersDetailController);

    OrdersDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Orders', 'Payment', 'OrderData', 'Customers'];

    function OrdersDetailController($scope, $rootScope, $stateParams, previousState, entity, Orders, Payment, OrderData, Customers) {
        var vm = this;

        vm.orders = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('farmerConnApp:ordersUpdate', function(event, result) {
            vm.orders = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
