(function() {
    'use strict';

    angular
        .module('farmerConnApp')
        .controller('CustomersDeleteController',CustomersDeleteController);

    CustomersDeleteController.$inject = ['$uibModalInstance', 'entity', 'Customers'];

    function CustomersDeleteController($uibModalInstance, entity, Customers) {
        var vm = this;

        vm.customers = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Customers.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
