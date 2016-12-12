(function() {
    'use strict';

    angular
        .module('farmerConnApp')
        .controller('CustomersDialogController', CustomersDialogController);

    CustomersDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Customers'];

    function CustomersDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Customers) {
        var vm = this;

        vm.customers = entity;
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
            if (vm.customers.id !== null) {
                Customers.update(vm.customers, onSaveSuccess, onSaveError);
            } else {
                Customers.save(vm.customers, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('farmerConnApp:customersUpdate', result);
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
