(function() {
    'use strict';

    angular
        .module('farmerConnApp')
        .controller('SupplierDialogController', SupplierDialogController);

    SupplierDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Supplier'];

    function SupplierDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Supplier) {
        var vm = this;

        vm.supplier = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.supplier.id !== null) {
                Supplier.update(vm.supplier, onSaveSuccess, onSaveError);
            } else {
                Supplier.save(vm.supplier, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('farmerConnApp:supplierUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateEntered = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
