(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('CountryDeleteController',CountryDeleteController);

    CountryDeleteController.$inject = ['$uibModalInstance', 'entity', 'Country'];

    function CountryDeleteController($uibModalInstance, entity, Country) {
        var vm = this;
        vm.country = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Country.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
