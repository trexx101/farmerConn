'use strict';

describe('Controller Tests', function() {

    describe('Orders Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockOrders, MockPayment, MockOrderData, MockCustomers;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockOrders = jasmine.createSpy('MockOrders');
            MockPayment = jasmine.createSpy('MockPayment');
            MockOrderData = jasmine.createSpy('MockOrderData');
            MockCustomers = jasmine.createSpy('MockCustomers');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Orders': MockOrders,
                'Payment': MockPayment,
                'OrderData': MockOrderData,
                'Customers': MockCustomers
            };
            createController = function() {
                $injector.get('$controller')("OrdersDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'farmerConnApp:ordersUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
