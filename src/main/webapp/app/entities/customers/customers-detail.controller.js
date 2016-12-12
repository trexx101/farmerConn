(function() {
    'use strict';

    angular
        .module('farmerConnApp')
        .controller('CustomersDetailController', CustomersDetailController);

    CustomersDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Customers'];

    function CustomersDetailController($scope, $rootScope, $stateParams, previousState, entity, Customers) {
        var vm = this;

        vm.customers = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('farmerConnApp:customersUpdate', function(event, result) {
            vm.customers = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
