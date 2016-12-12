(function() {
    'use strict';

    angular
        .module('farmerConnApp')
        .controller('OrdersDeleteController',OrdersDeleteController);

    OrdersDeleteController.$inject = ['$uibModalInstance', 'entity', 'Orders'];

    function OrdersDeleteController($uibModalInstance, entity, Orders) {
        var vm = this;

        vm.orders = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Orders.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
