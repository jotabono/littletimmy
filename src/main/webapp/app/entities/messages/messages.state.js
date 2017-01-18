(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('messages', {
            parent: 'entity',
            url: '/messages',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'littletimmyApp.messages.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/messages/messages.html',
                    controller: 'MessagesController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('messages');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('messages-detail', {
            parent: 'entity',
            url: '/messages/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'littletimmyApp.messages.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/messages/messages-detail.html',
                    controller: 'MessagesDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('messages');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Messages', function($stateParams, Messages) {
                    return Messages.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'messages',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('messages-detail.edit', {
            parent: 'messages-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/messages/messages-dialog.html',
                    controller: 'MessagesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Messages', function(Messages) {
                            return Messages.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('messages.new', {
            parent: 'messages',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/messages/messages-dialog.html',
                    controller: 'MessagesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                text: null,
                                sendDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('messages', null, { reload: 'messages' });
                }, function() {
                    $state.go('messages');
                });
            }]
        })
        .state('messages.edit', {
            parent: 'messages',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/messages/messages-dialog.html',
                    controller: 'MessagesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Messages', function(Messages) {
                            return Messages.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('messages', null, { reload: 'messages' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('messages.delete', {
            parent: 'messages',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/messages/messages-delete-dialog.html',
                    controller: 'MessagesDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Messages', function(Messages) {
                            return Messages.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('messages', null, { reload: 'messages' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
