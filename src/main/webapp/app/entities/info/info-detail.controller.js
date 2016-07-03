(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('InfoDetailController', InfoDetailController);

    InfoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Info'];

    function InfoDetailController($scope, $rootScope, $stateParams, entity, Info) {
        var vm = this;
        vm.info = entity;
        vm.load = function (id) {
            Info.get({id: id}, function(result) {
                vm.info = result;
            });
        };
        var unsubscribe = $rootScope.$on('mozhotelsApp:infoUpdate', function(event, result) {
            vm.info = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
