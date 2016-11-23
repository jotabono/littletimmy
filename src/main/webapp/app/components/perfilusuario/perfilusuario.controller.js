(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('PerfilUsuarioController', PerfilUsuarioController);

    PerfilUsuarioController.$inject = ['$scope', '$state', 'userInfo', '$http', 'Auth', 'Principal', '$rootScope', 'userFriends', 'userTrabajo', 'userEstudio', '$window'];

    function PerfilUsuarioController ($scope, $state, userInfo, $http, Auth, Principal, $rootScope, userFriends, userTrabajo, userEstudio, $window) {
        var vm = this;
        vm.user = userInfo;
        vm.estudios = userEstudio;
        vm.estudioactuales = [];
        vm.estudioactual = [];

        vm.trabajos = userTrabajo;
        vm.trabajoactuales = [];
        vm.trabajoactual = [];
        vm.saveContent = saveContent;

        vm.friends = userFriends;


        $scope.scroll = function () {
            $('html, body').stop().animate({
                scrollTop: 0
            }, 500);
        };

        $rootScope.$on('authenticationSuccess',function(){
            Principal.identity().then(function(account) {
                $rootScope.account = account;
            });
        });

        $rootScope.$on('emitUser',function(e,user){
            console.log(user);
            $rootScope.account = user;
        });

        function saveContent(user) {
            console.log(user);
            Auth.updateAccount(user).then(function() {
                vm.error = null;
                vm.success = 'OK';
                Principal.identity(true).then(function(account) {
                    vm.account = account;
                });
            }).catch(function() {
                vm.success = null;
                vm.error = 'ERROR';
            });
        }

        vm.user.$promise.then(function (response) {
            $window.document.title = "Perfil de " + vm.user.firstName + " " + vm.user.lastName;
        });

        vm.estudios.$promise.then(function (response) {
            for(var i=0; i<vm.estudios.length; i++){
                if (vm.estudios[i].actualmente) {
                    vm.estudioactuales.push(vm.estudios[i]);
                }
            }
            vm.estudioactual = vm.estudioactuales[vm.estudioactuales.length-1];
            if (vm.estudioactual == null){
                vm.estudioactual = vm.estudios[vm.estudios.length-1];
            }
        });

        vm.trabajos.$promise.then(function (response) {
            console.log(response);
            for(var i=0; i<vm.trabajos.length; i++){
                if (vm.trabajos[i].trabajo.actualmente) {
                    vm.trabajoactual = vm.trabajos[i].trabajo;
                }
                vm.trabajoactuales.push(vm.trabajos[i].trabajo);
            }
            /*vm.trabajoactual = vm.trabajoactuales[vm.trabajoactuales.length-1];
            if (vm.trabajoactual == null){
                vm.trabajoactual = vm.trabajos[vm.trabajos.length-1];
            }*/
        })

        $(window).on("scroll",function(){
            console.log($(this).scrollTop());
            if($(this).scrollTop() <= 500){
                $('.fixed-user').css("height","0px");
            }
            if($(this).scrollTop() >= 437){
                $('.fixed-user').css("height","65px");
            }
        })

    }
})();
