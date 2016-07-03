(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('instance-policy', {
            parent: 'entity',
            url: '/instance-policy?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsApp.instancePolicy.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/instance-policy/instance-policies.html',
                    controller: 'InstancePolicyController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('instancePolicy');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('instance-policy-detail', {
            parent: 'entity',
            url: '/instance-policy/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsApp.instancePolicy.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/instance-policy/instance-policy-detail.html',
                    controller: 'InstancePolicyDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('instancePolicy');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'InstancePolicy', function($stateParams, InstancePolicy) {
                    return InstancePolicy.get({id : $stateParams.id});
                }]
            }
        })
        .state('instance-policy.new', {
            parent: 'instance-policy',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-policy/instance-policy-dialog.html',
                    controller: 'InstancePolicyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                instancePolictyName: null,
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('instance-policy', null, { reload: true });
                }, function() {
                    $state.go('instance-policy');
                });
            }]
        })
        .state('instance-policy.edit', {
            parent: 'instance-policy',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-policy/instance-policy-dialog.html',
                    controller: 'InstancePolicyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['InstancePolicy', function(InstancePolicy) {
                            return InstancePolicy.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('instance-policy', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('instance-policy.delete', {
            parent: 'instance-policy',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-policy/instance-policy-delete-dialog.html',
                    controller: 'InstancePolicyDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['InstancePolicy', function(InstancePolicy) {
                            return InstancePolicy.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('instance-policy', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
