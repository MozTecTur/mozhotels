(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('instance-facility', {
            parent: 'entity',
            url: '/instance-facility?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsApp.instanceFacility.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/instance-facility/instance-facilities.html',
                    controller: 'InstanceFacilityController',
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
                    $translatePartialLoader.addPart('instanceFacility');
                    $translatePartialLoader.addPart('fInstanceArea');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('instance-facility-detail', {
            parent: 'entity',
            url: '/instance-facility/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsApp.instanceFacility.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/instance-facility/instance-facility-detail.html',
                    controller: 'InstanceFacilityDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('instanceFacility');
                    $translatePartialLoader.addPart('fInstanceArea');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'InstanceFacility', function($stateParams, InstanceFacility) {
                    return InstanceFacility.get({id : $stateParams.id});
                }]
            }
        })
        .state('instance-facility.new', {
            parent: 'instance-facility',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-facility/instance-facility-dialog.html',
                    controller: 'InstanceFacilityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                instanceFacilityName: null,
                                description: null,
                                quantity: null,
                                area: null,
                                price: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('instance-facility', null, { reload: true });
                }, function() {
                    $state.go('instance-facility');
                });
            }]
        })
        .state('instance-facility.edit', {
            parent: 'instance-facility',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-facility/instance-facility-dialog.html',
                    controller: 'InstanceFacilityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['InstanceFacility', function(InstanceFacility) {
                            return InstanceFacility.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('instance-facility', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('instance-facility.delete', {
            parent: 'instance-facility',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-facility/instance-facility-delete-dialog.html',
                    controller: 'InstanceFacilityDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['InstanceFacility', function(InstanceFacility) {
                            return InstanceFacility.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('instance-facility', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
