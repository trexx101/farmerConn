(function() {
    'use strict';
    angular
        .module('farmerConnApp')
        .factory('OrderData', OrderData);

    OrderData.$inject = ['$resource', 'DateUtils'];

    function OrderData ($resource, DateUtils) {
        var resourceUrl =  'api/order-data/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.shipDate = DateUtils.convertDateTimeFromServer(data.shipDate);
                        data.billDate = DateUtils.convertDateTimeFromServer(data.billDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
