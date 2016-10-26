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
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
    }

})();
