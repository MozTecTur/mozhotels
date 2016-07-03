(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('local-tur', {
            parent: 'entity',
            url: '/local-tur?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsApp.localTur.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/local-tur/local-turs.html',
                    controller: 'LocalTurController',
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
                    $translatePartialLoader.addPart('localTur');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('local-tur-detail', {
            parent: 'entity',
            url: '/local-tur/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsApp.localTur.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/local-tur/local-tur-detail.html',
                    controller: 'LocalTurDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('localTur');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'LocalTur', function($stateParams, LocalTur) {
                    return LocalTur.get({id : $stateParams.id});
                }]
            }
        })
        .state('local-tur.new', {
            parent: 'local-tur',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/local-tur/local-tur-dialog.html',
                    controller: 'LocalTurDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                localTurName: null,
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('local-tur', null, { reload: true });
                }, function() {
                    $state.go('local-tur');
                });
            }]
        })
        .state('local-tur.edit', {
            parent: 'local-tur',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/local-tur/local-tur-dialog.html',
                    controller: 'LocalTurDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['LocalTur', function(LocalTur) {
                            return LocalTur.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('local-tur', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('local-tur.delete', {
            parent: 'local-tur',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/local-tur/local-tur-delete-dialog.html',
                    controller: 'LocalTurDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['LocalTur', function(LocalTur) {
                            return LocalTur.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('local-tur', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
