(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('CountryDialogController', CountryDialogController);

    CountryDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Country', 'Region'];

    function CountryDialogController ($scope, $stateParams, $uibModalInstance, entity, Country, Region) {
        var vm = this;
        vm.country = entity;
        vm.regions = Region.query();
        vm.load = function(id) {
            Country.get({id : id}, function(result) {
                vm.country = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('mozhotelsApp:countryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.country.id !== null) {
                Country.update(vm.country, onSaveSuccess, onSaveError);
            } else {
                Country.save(vm.country, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
