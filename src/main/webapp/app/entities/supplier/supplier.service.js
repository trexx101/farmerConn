(function() {
    'use strict';
    angular
        .module('farmerConnApp')
        .factory('Supplier', Supplier);

    Supplier.$inject = ['$resource', 'DateUtils'];

    function Supplier ($resource, DateUtils) {
        var resourceUrl =  'api/suppliers/:id';

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
