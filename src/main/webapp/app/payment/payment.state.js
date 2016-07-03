(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('payment', {
            parent: 'app',
            url: '/payment',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/payment/payment.html',
                    controller: 'PaymentController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('home');
                    return $translate.refresh();
                }]
            }

        });
    }
})();
