(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('friend-user', {
            parent: 'entity',
            url: '/friend-user?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'littletimmyApp.friend_user.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/friend-user/friend-users.html',
                    controller: 'Friend_userController',
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
                    $translatePartialLoader.addPart('friend_user');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('friend-user-detail', {
            parent: 'entity',
            url: '/friend-user/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'littletimmyApp.friend_user.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/friend-user/friend-user-detail.html',
                    controller: 'Friend_userDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('friend_user');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Friend_user', function($stateParams, Friend_user) {
                    return Friend_user.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'friend-user',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('friend-user-detail.edit', {
            parent: 'friend-user-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/friend-user/friend-user-dialog.html',
                    controller: 'Friend_userDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Friend_user', function(Friend_user) {
                            return Friend_user.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('friend-user.new', {
            parent: 'friend-user',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/friend-user/friend-user-dialog.html',
                    controller: 'Friend_userDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                friendship: null,
                                friendship_date: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('friend-user', null, { reload: 'friend-user' });
                }, function() {
                    $state.go('friend-user');
                });
            }]
        })
        .state('friend-user.edit', {
            parent: 'friend-user',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/friend-user/friend-user-dialog.html',
                    controller: 'Friend_userDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Friend_user', function(Friend_user) {
                            return Friend_user.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('friend-user', null, { reload: 'friend-user' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('friend-user.delete', {
            parent: 'friend-user',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/friend-user/friend-user-delete-dialog.html',
                    controller: 'Friend_userDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Friend_user', function(Friend_user) {
                            return Friend_user.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('friend-user', null, { reload: 'friend-user' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
