(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('friendship-notification', {
            parent: 'entity',
            url: '/friendship-notification',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'littletimmyApp.friendship_notification.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/friendship-notification/friendship-notifications.html',
                    controller: 'Friendship_notificationController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('friendship_notification');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('friendship-notification-detail', {
            parent: 'entity',
            url: '/friendship-notification/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'littletimmyApp.friendship_notification.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/friendship-notification/friendship-notification-detail.html',
                    controller: 'Friendship_notificationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('friendship_notification');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Friendship_notification', function($stateParams, Friendship_notification) {
                    return Friendship_notification.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'friendship-notification',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('friendship-notification-detail.edit', {
            parent: 'friendship-notification-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/friendship-notification/friendship-notification-dialog.html',
                    controller: 'Friendship_notificationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Friendship_notification', function(Friendship_notification) {
                            return Friendship_notification.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('friendship-notification.new', {
            parent: 'friendship-notification',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/friendship-notification/friendship-notification-dialog.html',
                    controller: 'Friendship_notificationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                fechaRecibida: null,
                                leida: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('friendship-notification', null, { reload: 'friendship-notification' });
                }, function() {
                    $state.go('friendship-notification');
                });
            }]
        })
        .state('friendship-notification.edit', {
            parent: 'friendship-notification',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/friendship-notification/friendship-notification-dialog.html',
                    controller: 'Friendship_notificationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Friendship_notification', function(Friendship_notification) {
                            return Friendship_notification.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('friendship-notification', null, { reload: 'friendship-notification' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('friendship-notification.delete', {
            parent: 'friendship-notification',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/friendship-notification/friendship-notification-delete-dialog.html',
                    controller: 'Friendship_notificationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Friendship_notification', function(Friendship_notification) {
                            return Friendship_notification.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('friendship-notification', null, { reload: 'friendship-notification' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
