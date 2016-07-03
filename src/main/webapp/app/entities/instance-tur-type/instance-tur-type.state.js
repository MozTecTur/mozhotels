(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('instance-tur-type', {
            parent: 'entity',
            url: '/instance-tur-type?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsApp.instanceTurType.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/instance-tur-type/instance-tur-types.html',
                    controller: 'InstanceTurTypeController',
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
                    $translatePartialLoader.addPart('instanceTurType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('instance-tur-type-detail', {
            parent: 'entity',
            url: '/instance-tur-type/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsApp.instanceTurType.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/instance-tur-type/instance-tur-type-detail.html',
                    controller: 'InstanceTurTypeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('instanceTurType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'InstanceTurType', function($stateParams, InstanceTurType) {
                    return InstanceTurType.get({id : $stateParams.id});
                }]
            }
        })
        .state('instance-tur-type.new', {
            parent: 'instance-tur-type',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-tur-type/instance-tur-type-dialog.html',
                    controller: 'InstanceTurTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                instanceTurTypeName: null,
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('instance-tur-type', null, { reload: true });
                }, function() {
                    $state.go('instance-tur-type');
                });
            }]
        })
        .state('instance-tur-type.edit', {
            parent: 'instance-tur-type',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-tur-type/instance-tur-type-dialog.html',
                    controller: 'InstanceTurTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['InstanceTurType', function(InstanceTurType) {
                            return InstanceTurType.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('instance-tur-type', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('instance-tur-type.delete', {
            parent: 'instance-tur-type',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-tur-type/instance-tur-type-delete-dialog.html',
                    controller: 'InstanceTurTypeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['InstanceTurType', function(InstanceTurType) {
                            return InstanceTurType.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('instance-tur-type', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
