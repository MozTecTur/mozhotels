(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('PaymentController', PaymentController);

    PaymentController.$inject = ['$scope', 'Principal'];

    function PaymentController ($scope, Principal) {
        var vm = this;

        vm.checkIn = new Date();
        vm.checkOut = new Date();
    }
})();
