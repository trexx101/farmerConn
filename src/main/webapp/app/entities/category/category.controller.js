(function() {
    'use strict';

    angular
        .module('farmerConnApp')
        .controller('CategoryController', CategoryController);

    CategoryController.$inject = ['$scope', '$state', 'DataUtils', 'Category'];

    function CategoryController ($scope, $state, DataUtils, Category) {
        var vm = this;

        vm.categories = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;

        loadAll();

        function loadAll() {
            Category.query(function(result) {
                vm.categories = result;
            });
        }
    }
})();
