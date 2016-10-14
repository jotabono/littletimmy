(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('recommend-notification', {
            parent: 'entity',
            url: '/recommend-notification?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'littletimmyApp.recommend_notification.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/recommend-notification/recommend-notifications.html',
                    controller: 'Recommend_notificationController',
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
                    $translatePartialLoader.addPart('recommend_notification');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('recommend-notification-detail', {
            parent: 'entity',
            url: '/recommend-notification/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'littletimmyApp.recommend_notification.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/recommend-notification/recommend-notification-detail.html',
                    controller: 'Recommend_notificationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('recommend_notification');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Recommend_notification', function($stateParams, Recommend_notification) {
                    return Recommend_notification.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'recommend-notification',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('recommend-notification-detail.edit', {
            parent: 'recommend-notification-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/recommend-notification/recommend-notification-dialog.html',
                    controller: 'Recommend_notificationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Recommend_notification', function(Recommend_notification) {
                            return Recommend_notification.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('recommend-notification.new', {
            parent: 'recommend-notification',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/recommend-notification/recommend-notification-dialog.html',
                    controller: 'Recommend_notificationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                contenido: null,
                                fechaRecibida: null,
                                leida: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('recommend-notification', null, { reload: 'recommend-notification' });
                }, function() {
                    $state.go('recommend-notification');
                });
            }]
        })
        .state('recommend-notification.edit', {
            parent: 'recommend-notification',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/recommend-notification/recommend-notification-dialog.html',
                    controller: 'Recommend_notificationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Recommend_notification', function(Recommend_notification) {
                            return Recommend_notification.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('recommend-notification', null, { reload: 'recommend-notification' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('recommend-notification.delete', {
            parent: 'recommend-notification',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/recommend-notification/recommend-notification-delete-dialog.html',
                    controller: 'Recommend_notificationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Recommend_notification', function(Recommend_notification) {
                            return Recommend_notification.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('recommend-notification', null, { reload: 'recommend-notification' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
