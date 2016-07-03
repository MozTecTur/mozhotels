(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('instance-activity-type', {
            parent: 'entity',
            url: '/instance-activity-type?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsApp.instanceActivityType.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/instance-activity-type/instance-activity-types.html',
                    controller: 'InstanceActivityTypeController',
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
                    $translatePartialLoader.addPart('instanceActivityType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('instance-activity-type-detail', {
            parent: 'entity',
            url: '/instance-activity-type/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsApp.instanceActivityType.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/instance-activity-type/instance-activity-type-detail.html',
                    controller: 'InstanceActivityTypeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('instanceActivityType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'InstanceActivityType', function($stateParams, InstanceActivityType) {
                    return InstanceActivityType.get({id : $stateParams.id});
                }]
            }
        })
        .state('instance-activity-type.new', {
            parent: 'instance-activity-type',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-activity-type/instance-activity-type-dialog.html',
                    controller: 'InstanceActivityTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                instanceActivityTypeName: null,
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('instance-activity-type', null, { reload: true });
                }, function() {
                    $state.go('instance-activity-type');
                });
            }]
        })
        .state('instance-activity-type.edit', {
            parent: 'instance-activity-type',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-activity-type/instance-activity-type-dialog.html',
                    controller: 'InstanceActivityTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['InstanceActivityType', function(InstanceActivityType) {
                            return InstanceActivityType.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('instance-activity-type', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('instance-activity-type.delete', {
            parent: 'instance-activity-type',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-activity-type/instance-activity-type-delete-dialog.html',
                    controller: 'InstanceActivityTypeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['InstanceActivityType', function(InstanceActivityType) {
                            return InstanceActivityType.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('instance-activity-type', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
