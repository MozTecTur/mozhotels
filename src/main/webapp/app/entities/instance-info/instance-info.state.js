(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('instance-info', {
            parent: 'entity',
            url: '/instance-info?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsApp.instanceInfo.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/instance-info/instance-infos.html',
                    controller: 'InstanceInfoController',
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
                    $translatePartialLoader.addPart('instanceInfo');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('instance-info-detail', {
            parent: 'entity',
            url: '/instance-info/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'mozhotelsApp.instanceInfo.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/instance-info/instance-info-detail.html',
                    controller: 'InstanceInfoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('instanceInfo');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'InstanceInfo', function($stateParams, InstanceInfo) {
                    return InstanceInfo.get({id : $stateParams.id});
                }]
            }
        })
        .state('instance-info.new', {
            parent: 'instance-info',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-info/instance-info-dialog.html',
                    controller: 'InstanceInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                instanceInfoName: null,
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('instance-info', null, { reload: true });
                }, function() {
                    $state.go('instance-info');
                });
            }]
        })
        .state('instance-info.edit', {
            parent: 'instance-info',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-info/instance-info-dialog.html',
                    controller: 'InstanceInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['InstanceInfo', function(InstanceInfo) {
                            return InstanceInfo.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('instance-info', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('instance-info.delete', {
            parent: 'instance-info',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instance-info/instance-info-delete-dialog.html',
                    controller: 'InstanceInfoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['InstanceInfo', function(InstanceInfo) {
                            return InstanceInfo.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('instance-info', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
