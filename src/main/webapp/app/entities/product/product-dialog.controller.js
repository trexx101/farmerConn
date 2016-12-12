(function() {
    'use strict';

    angular
        .module('farmerConnApp')
        .controller('ProductDialogController', ProductDialogController);

    ProductDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'DataUtils', 'entity', 'Product', 'Category', 'Supplier'];

    function ProductDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, DataUtils, entity, Product, Category, Supplier) {
        var vm = this;

        vm.product = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.categories = Category.query({filter: 'product-is-null'});
        $q.all([vm.product.$promise, vm.categories.$promise]).then(function() {
            if (!vm.product.category || !vm.product.category.id) {
                return $q.reject();
            }
            return Category.get({id : vm.product.category.id}).$promise;
        }).then(function(category) {
            vm.categories.push(category);
        });
        vm.suppliers = Supplier.query({filter: 'product-is-null'});
        $q.all([vm.product.$promise, vm.suppliers.$promise]).then(function() {
            if (!vm.product.supplier || !vm.product.supplier.id) {
                return $q.reject();
            }
            return Supplier.get({id : vm.product.supplier.id}).$promise;
        }).then(function(supplier) {
            vm.suppliers.push(supplier);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.product.id !== null) {
                Product.update(vm.product, onSaveSuccess, onSaveError);
            } else {
                Product.save(vm.product, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('farmerConnApp:productUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setPicture = function ($file, product) {
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        product.picture = base64Data;
                        product.pictureContentType = $file.type;
                    });
                });
            }
        };

    }
})();
