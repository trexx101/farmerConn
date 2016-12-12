(function() {
    'use strict';

    angular
        .module('farmerConnApp')
        .controller('PaymentDetailController', PaymentDetailController);

    PaymentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Payment'];

    function PaymentDetailController($scope, $rootScope, $stateParams, previousState, entity, Payment) {
        var vm = this;

        vm.payment = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('farmerConnApp:paymentUpdate', function(event, result) {
            vm.payment = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
