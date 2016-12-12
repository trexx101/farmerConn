(function() {
    'use strict';
    angular
        .module('farmerConnApp')
        .factory('Customers', Customers);

    Customers.$inject = ['$resource', 'DateUtils'];

    function Customers ($resource, DateUtils) {
        var resourceUrl =  'api/customers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateEntered = DateUtils.convertDateTimeFromServer(data.dateEntered);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
