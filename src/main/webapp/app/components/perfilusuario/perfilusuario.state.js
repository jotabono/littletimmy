(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('perfil', {
                parent: 'entity',
                url: '/perfil/{user}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: ''
                },
                views: {
                    'content@': {
                        templateUrl: 'app/components/perfilusuario/perfilusuario.html',
                        controller: 'PerfilUsuarioController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    userInfo: ["User", "$stateParams", function(User, $stateParams){
                        return User.get({login:$stateParams.user});
                    }],
                    userFriends: ["User", "$stateParams", function(User, $stateParams){
                        return User.getFriendsUser({login:$stateParams.user});
                    }],
                    userTrabajo: ["User", "$stateParams", function(User, $stateParams){
                        return User.getTrabajosUser({login:$stateParams.user});
                    }],
                    userEstudio: ["User", "$stateParams", function(User, $stateParams){
                        return User.getEstudiosUser({login:$stateParams.user});
                    }],
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('perfil-recomendaciones', {
                parent: 'perfil',
                url: '/trabajo/{id_trabajo}/recomendations',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/components/perfilusuario/recomendaciones.html',
                        controller: 'RecoTrabajoController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            recomendaciones: ["Recomendacion", function(Recomendacion){
                                return Recomendacion.recomendacionesTrabajo({id_trabajo:$stateParams.id_trabajo});
                            }]
                        }
                    });
                }]
            });
    }

})();
