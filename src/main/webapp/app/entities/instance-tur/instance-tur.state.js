(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('instance-tur', {
            parent: 'entity',
            url: '/instance-tur?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsApp.instanceTur.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/instance-tur/instance-turs.html',
                    controller: 'InstanceTurController',
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
                    $translatePartialLoader.addPart('instanceTur');
                    $translatePartialLoader.addPart('cRating');
                    $translatePartialLoader.addPart('eCurrency');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('instance-tur-detail', {
            parent: 'entity',
            url: '/instance-tur/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsApp.instanceTur.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/instance-tur/instance-tur-detail.html',
                    controller: 'InstanceTurDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('instanceTur');
                    $translatePartialLoader.addPart('cRating');
                    $translatePartialLoader.addPart('eCurrency');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'InstanceTur', function($stateParams, InstanceTur) {
                    return InstanceTur.get({id : $stateParams.id});
                }]
            }
        })
        .state('instance-tur.new', {
            parent: 'instance-tur',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-tur/instance-tur-dialog.html',
                    controller: 'InstanceTurDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                instanceTurName: null,
                                description: null,
                                address: null,
                                website: null,
                                email: null,
                                latitude: null,
                                longitude: null,
                                rooms: null,
                                beds: null,
                                floors: null,
                                rating: null,
                                currency: null,
                                contactNumberPrincipal: null,
                                photoPrincipal: null,
                                photoPrincipalContentType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('instance-tur', null, { reload: true });
                }, function() {
                    $state.go('instance-tur');
                });
            }]
        })
        .state('instance-tur.edit', {
            parent: 'instance-tur',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-tur/instance-tur-dialog.html',
                    controller: 'InstanceTurDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['InstanceTur', function(InstanceTur) {
                            return InstanceTur.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('instance-tur', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('instance-tur.delete', {
            parent: 'instance-tur',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-tur/instance-tur-delete-dialog.html',
                    controller: 'InstanceTurDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['InstanceTur', function(InstanceTur) {
                            return InstanceTur.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('instance-tur', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
