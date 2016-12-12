(function() {
    'use strict';

    angular
        .module('farmerConnApp')
        .controller('OrdersDialogController', OrdersDialogController);

    OrdersDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Orders', 'Payment', 'OrderData', 'Customers'];

    function OrdersDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Orders, Payment, OrderData, Customers) {
        var vm = this;

        vm.orders = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.paymentids = Payment.query({filter: 'orders-is-null'});
        $q.all([vm.orders.$promise, vm.paymentids.$promise]).then(function() {
            if (!vm.orders.paymentId || !vm.orders.paymentId.id) {
                return $q.reject();
            }
            return Payment.get({id : vm.orders.paymentId.id}).$promise;
        }).then(function(paymentId) {
            vm.paymentids.push(paymentId);
        });
        vm.orderdata = OrderData.query();
        vm.customers = Customers.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.orders.id !== null) {
                Orders.update(vm.orders, onSaveSuccess, onSaveError);
            } else {
                Orders.save(vm.orders, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('farmerConnApp:ordersUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.orderDate = false;
        vm.datePickerOpenStatus.shipDate = false;
        vm.datePickerOpenStatus.paymentDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
