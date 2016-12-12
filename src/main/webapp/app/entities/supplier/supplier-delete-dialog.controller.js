(function() {
    'use strict';

    angular
        .module('farmerConnApp')
        .controller('SupplierDeleteController',SupplierDeleteController);

    SupplierDeleteController.$inject = ['$uibModalInstance', 'entity', 'Supplier'];

    function SupplierDeleteController($uibModalInstance, entity, Supplier) {
        var vm = this;

        vm.supplier = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Supplier.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
