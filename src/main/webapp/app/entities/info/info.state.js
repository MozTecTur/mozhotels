(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('info', {
            parent: 'entity',
            url: '/info?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsApp.info.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/info/infos.html',
                    controller: 'InfoController',
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
                    $translatePartialLoader.addPart('info');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('info-detail', {
            parent: 'entity',
            url: '/info/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsApp.info.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/info/info-detail.html',
                    controller: 'InfoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('info');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Info', function($stateParams, Info) {
                    return Info.get({id : $stateParams.id});
                }]
            }
        })
        .state('info.new', {
            parent: 'info',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/info/info-dialog.html',
                    controller: 'InfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                value: null,
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('info', null, { reload: true });
                }, function() {
                    $state.go('info');
                });
            }]
        })
        .state('info.edit', {
            parent: 'info',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/info/info-dialog.html',
                    controller: 'InfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Info', function(Info) {
                            return Info.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('info', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('info.delete', {
            parent: 'info',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/info/info-delete-dialog.html',
                    controller: 'InfoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Info', function(Info) {
                            return Info.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('info', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
