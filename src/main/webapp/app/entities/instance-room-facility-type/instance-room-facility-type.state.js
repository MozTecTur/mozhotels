(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('instance-room-facility-type', {
            parent: 'entity',
            url: '/instance-room-facility-type?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsApp.instanceRoomFacilityType.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/instance-room-facility-type/instance-room-facility-types.html',
                    controller: 'InstanceRoomFacilityTypeController',
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
                    $translatePartialLoader.addPart('instanceRoomFacilityType');
                    $translatePartialLoader.addPart('bFacility');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('instance-room-facility-type-detail', {
            parent: 'entity',
            url: '/instance-room-facility-type/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsApp.instanceRoomFacilityType.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/instance-room-facility-type/instance-room-facility-type-detail.html',
                    controller: 'InstanceRoomFacilityTypeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('instanceRoomFacilityType');
                    $translatePartialLoader.addPart('bFacility');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'InstanceRoomFacilityType', function($stateParams, InstanceRoomFacilityType) {
                    return InstanceRoomFacilityType.get({id : $stateParams.id});
                }]
            }
        })
        .state('instance-room-facility-type.new', {
            parent: 'instance-room-facility-type',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-room-facility-type/instance-room-facility-type-dialog.html',
                    controller: 'InstanceRoomFacilityTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                instanceRoomFacilityTypeName: null,
                                facility: null,
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('instance-room-facility-type', null, { reload: true });
                }, function() {
                    $state.go('instance-room-facility-type');
                });
            }]
        })
        .state('instance-room-facility-type.edit', {
            parent: 'instance-room-facility-type',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-room-facility-type/instance-room-facility-type-dialog.html',
                    controller: 'InstanceRoomFacilityTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['InstanceRoomFacilityType', function(InstanceRoomFacilityType) {
                            return InstanceRoomFacilityType.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('instance-room-facility-type', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('instance-room-facility-type.delete', {
            parent: 'instance-room-facility-type',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-room-facility-type/instance-room-facility-type-delete-dialog.html',
                    controller: 'InstanceRoomFacilityTypeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['InstanceRoomFacilityType', function(InstanceRoomFacilityType) {
                            return InstanceRoomFacilityType.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('instance-room-facility-type', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
