(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('instance-room-type', {
            parent: 'entity',
            url: '/instance-room-type?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsApp.instanceRoomType.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/instance-room-type/instance-room-types.html',
                    controller: 'InstanceRoomTypeController',
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
                    $translatePartialLoader.addPart('instanceRoomType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('instance-room-type-detail', {
            parent: 'entity',
            url: '/instance-room-type/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsApp.instanceRoomType.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/instance-room-type/instance-room-type-detail.html',
                    controller: 'InstanceRoomTypeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('instanceRoomType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'InstanceRoomType', function($stateParams, InstanceRoomType) {
                    return InstanceRoomType.get({id : $stateParams.id});
                }]
            }
        })
        .state('instance-room-type.new', {
            parent: 'instance-room-type',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-room-type/instance-room-type-dialog.html',
                    controller: 'InstanceRoomTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                instanceRoomTypeName: null,
                                description: null,
                                quantity: null,
                                capacityAdults: null,
                                capacityChildren: null,
                                price: null,
                                photoPrincipal: null,
                                photoPrincipalContentType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('instance-room-type', null, { reload: true });
                }, function() {
                    $state.go('instance-room-type');
                });
            }]
        })
        .state('instance-room-type.edit', {
            parent: 'instance-room-type',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-room-type/instance-room-type-dialog.html',
                    controller: 'InstanceRoomTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['InstanceRoomType', function(InstanceRoomType) {
                            return InstanceRoomType.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('instance-room-type', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('instance-room-type.delete', {
            parent: 'instance-room-type',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-room-type/instance-room-type-delete-dialog.html',
                    controller: 'InstanceRoomTypeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['InstanceRoomType', function(InstanceRoomType) {
                            return InstanceRoomType.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('instance-room-type', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
