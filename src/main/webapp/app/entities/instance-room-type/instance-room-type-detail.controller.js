(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('InstanceRoomTypeDetailController', InstanceRoomTypeDetailController);

    InstanceRoomTypeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'DataUtils', 'entity', 'InstanceRoomType', 'Picture', 'InstanceRoomFacility', 'InstanceTur', 'Booking'];

    function InstanceRoomTypeDetailController($scope, $rootScope, $stateParams, DataUtils, entity, InstanceRoomType, Picture, InstanceRoomFacility, InstanceTur, Booking) {
        var vm = this;
        vm.instanceRoomType = entity;
        vm.load = function (id) {
            InstanceRoomType.get({id: id}, function(result) {
                vm.instanceRoomType = result;
            });
        };
        var unsubscribe = $rootScope.$on('mozhotelsApp:instanceRoomTypeUpdate', function(event, result) {
            vm.instanceRoomType = result;
        });
        $scope.$on('$destroy', unsubscribe);

        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
    }
})();
