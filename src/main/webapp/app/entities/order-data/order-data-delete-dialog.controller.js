(function() {
    'use strict';

    angular
        .module('farmerConnApp')
        .controller('OrderDataDeleteController',OrderDataDeleteController);

    OrderDataDeleteController.$inject = ['$uibModalInstance', 'entity', 'OrderData'];

    function OrderDataDeleteController($uibModalInstance, entity, OrderData) {
        var vm = this;

        vm.orderData = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            OrderData.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
