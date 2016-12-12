(function() {
    'use strict';
    angular
        .module('farmerConnApp')
        .factory('Orders', Orders);

    Orders.$inject = ['$resource', 'DateUtils'];

    function Orders ($resource, DateUtils) {
        var resourceUrl =  'api/orders/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.orderDate = DateUtils.convertDateTimeFromServer(data.orderDate);
                        data.shipDate = DateUtils.convertDateTimeFromServer(data.shipDate);
                        data.paymentDate = DateUtils.convertDateTimeFromServer(data.paymentDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
