(function() {
    'use strict';

    angular
        .module('farmerConnApp')
        .controller('ProductDetailController', ProductDetailController);

    ProductDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Product', 'Category', 'Supplier'];

    function ProductDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Product, Category, Supplier) {
        var vm = this;

        vm.product = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('farmerConnApp:productUpdate', function(event, result) {
            vm.product = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
