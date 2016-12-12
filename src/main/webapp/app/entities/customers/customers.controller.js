(function() {
    'use strict';

    angular
        .module('farmerConnApp')
        .controller('CustomersController', CustomersController);

    CustomersController.$inject = ['$scope', '$state', 'Customers'];

    function CustomersController ($scope, $state, Customers) {
        var vm = this;

        vm.customers = [];

        loadAll();

        function loadAll() {
            Customers.query(function(result) {
                vm.customers = result;
            });
        }
    }
})();
