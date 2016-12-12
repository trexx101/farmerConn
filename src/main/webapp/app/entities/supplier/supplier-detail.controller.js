(function() {
    'use strict';

    angular
        .module('farmerConnApp')
        .controller('SupplierDetailController', SupplierDetailController);

    SupplierDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Supplier'];

    function SupplierDetailController($scope, $rootScope, $stateParams, previousState, entity, Supplier) {
        var vm = this;

        vm.supplier = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('farmerConnApp:supplierUpdate', function(event, result) {
            vm.supplier = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
