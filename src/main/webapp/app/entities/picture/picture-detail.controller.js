(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('PictureDetailController', PictureDetailController);

    PictureDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'DataUtils', 'entity', 'Picture', 'Province', 'InstanceTur', 'InstanceRoomType'];

    function PictureDetailController($scope, $rootScope, $stateParams, DataUtils, entity, Picture, Province, InstanceTur, InstanceRoomType) {
        var vm = this;
        vm.picture = entity;
        vm.load = function (id) {
            Picture.get({id: id}, function(result) {
                vm.picture = result;
            });
        };
        var unsubscribe = $rootScope.$on('mozhotelsApp:pictureUpdate', function(event, result) {
            vm.picture = result;
        });
        $scope.$on('$destroy', unsubscribe);

        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
    }
})();
