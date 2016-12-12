(function() {
    'use strict';

    angular
        .module('farmerConnApp')
        .controller('OrderDataController', OrderDataController);

    OrderDataController.$inject = ['$scope', '$state', 'OrderData'];

    function OrderDataController ($scope, $state, OrderData) {
        var vm = this;

        vm.orderData = [];

        loadAll();

        function loadAll() {
            OrderData.query(function(result) {
                vm.orderData = result;
            });
        }
    }
})();
