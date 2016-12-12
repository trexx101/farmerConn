(function() {
    'use strict';

    angular
        .module('farmerConnApp')
        .controller('OrderDataDetailController', OrderDataDetailController);

    OrderDataDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'OrderData', 'Orders'];

    function OrderDataDetailController($scope, $rootScope, $stateParams, previousState, entity, OrderData, Orders) {
        var vm = this;

        vm.orderData = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('farmerConnApp:orderDataUpdate', function(event, result) {
            vm.orderData = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
