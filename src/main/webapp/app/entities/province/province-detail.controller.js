(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('ProvinceDetailController', ProvinceDetailController);

    ProvinceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'DataUtils', 'entity', 'Province', 'LocalTur', 'Picture', 'Region'];

    function ProvinceDetailController($scope, $rootScope, $stateParams, DataUtils, entity, Province, LocalTur, Picture, Region) {
        var vm = this;
        vm.province = entity;
        vm.load = function (id) {
            Province.get({id: id}, function(result) {
                vm.province = result;
            });
        };
        var unsubscribe = $rootScope.$on('mozhotelsApp:provinceUpdate', function(event, result) {
            vm.province = result;
        });
        $scope.$on('$destroy', unsubscribe);

        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
    }
})();
