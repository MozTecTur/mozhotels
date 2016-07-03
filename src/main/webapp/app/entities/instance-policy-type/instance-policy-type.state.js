(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('instance-policy-type', {
            parent: 'entity',
            url: '/instance-policy-type?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsApp.instancePolicyType.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/instance-policy-type/instance-policy-types.html',
                    controller: 'InstancePolicyTypeController',
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
                    $translatePartialLoader.addPart('instancePolicyType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('instance-policy-type-detail', {
            parent: 'entity',
            url: '/instance-policy-type/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsApp.instancePolicyType.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/instance-policy-type/instance-policy-type-detail.html',
                    controller: 'InstancePolicyTypeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('instancePolicyType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'InstancePolicyType', function($stateParams, InstancePolicyType) {
                    return InstancePolicyType.get({id : $stateParams.id});
                }]
            }
        })
        .state('instance-policy-type.new', {
            parent: 'instance-policy-type',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-policy-type/instance-policy-type-dialog.html',
                    controller: 'InstancePolicyTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                instancePolicyTypeName: null,
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('instance-policy-type', null, { reload: true });
                }, function() {
                    $state.go('instance-policy-type');
                });
            }]
        })
        .state('instance-policy-type.edit', {
            parent: 'instance-policy-type',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-policy-type/instance-policy-type-dialog.html',
                    controller: 'InstancePolicyTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['InstancePolicyType', function(InstancePolicyType) {
                            return InstancePolicyType.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('instance-policy-type', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('instance-policy-type.delete', {
            parent: 'instance-policy-type',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-policy-type/instance-policy-type-delete-dialog.html',
                    controller: 'InstancePolicyTypeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['InstancePolicyType', function(InstancePolicyType) {
                            return InstancePolicyType.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('instance-policy-type', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
