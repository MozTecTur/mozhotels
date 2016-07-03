(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('instance-room-facility', {
            parent: 'entity',
            url: '/instance-room-facility?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsApp.instanceRoomFacility.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/instance-room-facility/instance-room-facilities.html',
                    controller: 'InstanceRoomFacilityController',
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
                    $translatePartialLoader.addPart('instanceRoomFacility');
                    $translatePartialLoader.addPart('bFacility');
                    $translatePartialLoader.addPart('fInstanceArea');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('instance-room-facility-detail', {
            parent: 'entity',
            url: '/instance-room-facility/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsApp.instanceRoomFacility.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/instance-room-facility/instance-room-facility-detail.html',
                    controller: 'InstanceRoomFacilityDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('instanceRoomFacility');
                    $translatePartialLoader.addPart('bFacility');
                    $translatePartialLoader.addPart('fInstanceArea');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'InstanceRoomFacility', function($stateParams, InstanceRoomFacility) {
                    return InstanceRoomFacility.get({id : $stateParams.id});
                }]
            }
        })
        .state('instance-room-facility.new', {
            parent: 'instance-room-facility',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-room-facility/instance-room-facility-dialog.html',
                    controller: 'InstanceRoomFacilityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                instanceRoomFacilityName: null,
                                facility: null,
                                quantity: null,
                                area: null,
                                price: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('instance-room-facility', null, { reload: true });
                }, function() {
                    $state.go('instance-room-facility');
                });
            }]
        })
        .state('instance-room-facility.edit', {
            parent: 'instance-room-facility',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-room-facility/instance-room-facility-dialog.html',
                    controller: 'InstanceRoomFacilityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['InstanceRoomFacility', function(InstanceRoomFacility) {
                            return InstanceRoomFacility.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('instance-room-facility', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('instance-room-facility.delete', {
            parent: 'instance-room-facility',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-room-facility/instance-room-facility-delete-dialog.html',
                    controller: 'InstanceRoomFacilityDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['InstanceRoomFacility', function(InstanceRoomFacility) {
                            return InstanceRoomFacility.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('instance-room-facility', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
