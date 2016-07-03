(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('instance-facility-type', {
            parent: 'entity',
            url: '/instance-facility-type?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsApp.instanceFacilityType.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/instance-facility-type/instance-facility-types.html',
                    controller: 'InstanceFacilityTypeController',
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
                    $translatePartialLoader.addPart('instanceFacilityType');
                    $translatePartialLoader.addPart('bFacility');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('instance-facility-type-detail', {
            parent: 'entity',
            url: '/instance-facility-type/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsApp.instanceFacilityType.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/instance-facility-type/instance-facility-type-detail.html',
                    controller: 'InstanceFacilityTypeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('instanceFacilityType');
                    $translatePartialLoader.addPart('bFacility');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'InstanceFacilityType', function($stateParams, InstanceFacilityType) {
                    return InstanceFacilityType.get({id : $stateParams.id});
                }]
            }
        })
        .state('instance-facility-type.new', {
            parent: 'instance-facility-type',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-facility-type/instance-facility-type-dialog.html',
                    controller: 'InstanceFacilityTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                instanceFacilityTypeName: null,
                                facility: null,
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('instance-facility-type', null, { reload: true });
                }, function() {
                    $state.go('instance-facility-type');
                });
            }]
        })
        .state('instance-facility-type.edit', {
            parent: 'instance-facility-type',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-facility-type/instance-facility-type-dialog.html',
                    controller: 'InstanceFacilityTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['InstanceFacilityType', function(InstanceFacilityType) {
                            return InstanceFacilityType.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('instance-facility-type', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('instance-facility-type.delete', {
            parent: 'instance-facility-type',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-facility-type/instance-facility-type-delete-dialog.html',
                    controller: 'InstanceFacilityTypeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['InstanceFacilityType', function(InstanceFacilityType) {
                            return InstanceFacilityType.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('instance-facility-type', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
