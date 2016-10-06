(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('estudios', {
            parent: 'entity',
            url: '/estudios',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'littletimmyApp.estudios.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/estudios/estudios.html',
                    controller: 'EstudiosController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('estudios');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('estudios-detail', {
            parent: 'entity',
            url: '/estudios/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'littletimmyApp.estudios.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/estudios/estudios-detail.html',
                    controller: 'EstudiosDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('estudios');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Estudios', function($stateParams, Estudios) {
                    return Estudios.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'estudios',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('estudios-detail.edit', {
            parent: 'estudios-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/estudios/estudios-dialog.html',
                    controller: 'EstudiosDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Estudios', function(Estudios) {
                            return Estudios.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('estudios.new', {
            parent: 'estudios',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/estudios/estudios-dialog.html',
                    controller: 'EstudiosDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                fechaInicio: null,
                                fechaFinal: null,
                                actualmente: null,
                                curso: null,
                                nota: null,
                                descripcion: null,
                                archivos: null,
                                archivosContentType: null,
                                link: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('estudios', null, { reload: 'estudios' });
                }, function() {
                    $state.go('estudios');
                });
            }]
        })
        .state('estudios.edit', {
            parent: 'estudios',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/estudios/estudios-dialog.html',
                    controller: 'EstudiosDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Estudios', function(Estudios) {
                            return Estudios.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('estudios', null, { reload: 'estudios' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('estudios.delete', {
            parent: 'estudios',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/estudios/estudios-delete-dialog.html',
                    controller: 'EstudiosDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Estudios', function(Estudios) {
                            return Estudios.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('estudios', null, { reload: 'estudios' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
