(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('detail', {
            parent: 'app',
            url: '/detail/{id}',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/detail/detail.html',
                    controller: 'DetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('home');
                    $translatePartialLoader.addPart('instanceReview');
                    $translatePartialLoader.addPart('gGEvaluation');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }],
				entity: ['$stateParams', 'InstanceTur', function($stateParams, InstanceTur) {
                    return InstanceTur.get({id : $stateParams.id});
                }]
            }

        });
    }
})();
