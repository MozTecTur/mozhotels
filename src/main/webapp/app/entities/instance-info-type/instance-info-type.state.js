(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('instance-info-type', {
            parent: 'entity',
            url: '/instance-info-type?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsApp.instanceInfoType.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/instance-info-type/instance-info-types.html',
                    controller: 'InstanceInfoTypeController',
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
                    $translatePartialLoader.addPart('instanceInfoType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('instance-info-type-detail', {
            parent: 'entity',
            url: '/instance-info-type/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsApp.instanceInfoType.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/instance-info-type/instance-info-type-detail.html',
                    controller: 'InstanceInfoTypeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('instanceInfoType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'InstanceInfoType', function($stateParams, InstanceInfoType) {
                    return InstanceInfoType.get({id : $stateParams.id});
                }]
            }
        })
        .state('instance-info-type.new', {
            parent: 'instance-info-type',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-info-type/instance-info-type-dialog.html',
                    controller: 'InstanceInfoTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                instanceInfoTypeName: null,
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('instance-info-type', null, { reload: true });
                }, function() {
                    $state.go('instance-info-type');
                });
            }]
        })
        .state('instance-info-type.edit', {
            parent: 'instance-info-type',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-info-type/instance-info-type-dialog.html',
                    controller: 'InstanceInfoTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['InstanceInfoType', function(InstanceInfoType) {
                            return InstanceInfoType.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('instance-info-type', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('instance-info-type.delete', {
            parent: 'instance-info-type',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-info-type/instance-info-type-delete-dialog.html',
                    controller: 'InstanceInfoTypeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['InstanceInfoType', function(InstanceInfoType) {
                            return InstanceInfoType.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('instance-info-type', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
