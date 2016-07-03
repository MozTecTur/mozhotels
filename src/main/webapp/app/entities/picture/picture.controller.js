(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('PictureController', PictureController);

    PictureController.$inject = ['$scope', '$state', 'DataUtils', 'Picture', 'PictureSearch'];

    function PictureController ($scope, $state, DataUtils, Picture, PictureSearch) {
        var vm = this;
        vm.pictures = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
        vm.loadAll = function() {
            Picture.query(function(result) {
                vm.pictures = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            PictureSearch.query({query: vm.searchQuery}, function(result) {
                vm.pictures = result;
            });
        };
        vm.loadAll();
        
    }
})();
