(function() {
    'use strict';

    angular
        .module('farmerConnApp')
        .controller('OrderDataDialogController', OrderDataDialogController);

    OrderDataDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'OrderData', 'Orders'];

    function OrderDataDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, OrderData, Orders) {
        var vm = this;

        vm.orderData = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.orders = Orders.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.orderData.id !== null) {
                OrderData.update(vm.orderData, onSaveSuccess, onSaveError);
            } else {
                OrderData.save(vm.orderData, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('farmerConnApp:orderDataUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.shipDate = false;
        vm.datePickerOpenStatus.billDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
