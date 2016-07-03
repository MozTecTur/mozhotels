(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('RegionDialogController', RegionDialogController);

    RegionDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Region', 'Province', 'Country'];

    function RegionDialogController ($scope, $stateParams, $uibModalInstance, entity, Region, Province, Country) {
        var vm = this;
        vm.region = entity;
        vm.provinces = Province.query();
        vm.countrys = Country.query();
        vm.load = function(id) {
            Region.get({id : id}, function(result) {
                vm.region = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('mozhotelsApp:regionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.region.id !== null) {
                Region.update(vm.region, onSaveSuccess, onSaveError);
            } else {
                Region.save(vm.region, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
