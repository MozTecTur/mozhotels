(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('InstanceTurTypeDetailController', InstanceTurTypeDetailController);

    InstanceTurTypeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'InstanceTurType', 'InstanceTur'];

    function InstanceTurTypeDetailController($scope, $rootScope, $stateParams, entity, InstanceTurType, InstanceTur) {
        var vm = this;
        vm.instanceTurType = entity;
        vm.load = function (id) {
            InstanceTurType.get({id: id}, function(result) {
                vm.instanceTurType = result;
            });
        };
        var unsubscribe = $rootScope.$on('mozhotelsApp:instanceTurTypeUpdate', function(event, result) {
            vm.instanceTurType = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
