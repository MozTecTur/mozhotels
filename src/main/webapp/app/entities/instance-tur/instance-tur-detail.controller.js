(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('InstanceTurDetailController', InstanceTurDetailController);

    InstanceTurDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'DataUtils', 'entity', 'InstanceTur', 'Picture', 'InstanceContact', 'InstanceFacility', 'InstanceActivity', 'InstanceRoomType', 'InstancePolicy', 'InstanceInfo', 'InstanceReview', 'Booking', 'LocalTur', 'InstanceTurType'];

    function InstanceTurDetailController($scope, $rootScope, $stateParams, DataUtils, entity, InstanceTur, Picture, InstanceContact, InstanceFacility, InstanceActivity, InstanceRoomType, InstancePolicy, InstanceInfo, InstanceReview, Booking, LocalTur, InstanceTurType) {
        var vm = this;
        vm.instanceTur = entity;
        vm.load = function (id) {
            InstanceTur.get({id: id}, function(result) {
                vm.instanceTur = result;
            });
        };
        var unsubscribe = $rootScope.$on('mozhotelsApp:instanceTurUpdate', function(event, result) {
            vm.instanceTur = result;
        });
        $scope.$on('$destroy', unsubscribe);

        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
    }
})();
