(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('PerfilUsuarioController', PerfilUsuarioController);

    PerfilUsuarioController.$inject = ['$scope', 'userInfo', '$stateParams', 'Auth', 'Principal', '$rootScope', 'userTrabajo', 'userEstudio', '$window', 'Friend_user', 'userFriendship', 'userFriends', 'User', '$http'];

    function PerfilUsuarioController ($scope, userInfo, $stateParams, Auth, Principal, $rootScope, userTrabajo, userEstudio, $window, Friend_user, userFriendship, userFriends, User, $http) {
        var vm = this;

        // userFriends
        // userFriendship
        // userFriendship

        vm.user = userInfo;
        vm.estudios = userEstudio;
        vm.estudioactuales = [];
        vm.estudioactual = [];

        vm.trabajos = userTrabajo;
        vm.trabajoactuales = [];
        vm.trabajoactual = [];
        vm.userConnectionPath = [];

        vm.saveContent = saveContent;
        vm.addFriend = addFriend;

        vm.friendUser = userFriendship;
        vm.friends = userFriends;

        $scope.scroll = function () {
            $('html, body').stop().animate({
                scrollTop: 0
            }, 500);
        };

        Principal.identity().then(function(account) {
            $rootScope.account = account;
        });

        function addFriend() {
            if(vm.friendUser.friendship){
                vm.friendUser.friendship = false;
                Friend_user.update(vm.friendUser, function(res){
                    vm.friendUser = res;
                });
            }else{
                if(!vm.friendUser.friendship && (vm.friendUser.friend_from == null || vm.friendUser.friend_to == null)){
                    var newFriend = {
                        "id": null,
                        "friendship_date": new Date(),
                        "friendship": true,
                        "friend_from": vm.user,
                        "friend_to": vm.user
                    }
                    Friend_user.save(newFriend, function(res){
                        vm.friendUser = res;
                    });
                }
                else{
                    vm.friendUser.friendship = true;
                    Friend_user.update(vm.friendUser, function(res){
                        vm.friendUser = res;
                    });
                }
            }
            User.getFriendsUser({login:$stateParams.user},function(res){
                vm.friends = res;
            });
        }

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

            $http({
                method: 'GET',
                url: '/api/users/'+$rootScope.account.login
            }).then(function successCallback(resp) {
                var user = resp.data;
                $http({
                    method: 'GET',
                    url: '/api/users/connectionDegree/from/'+user.id+'/to/'+response.id
                }).then(function successCallback(res) {
                    vm.userConnectionPath = res.data;
                    console.log(res.data);
                });
            });
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
            if($(this).scrollTop() <= 500){
                $('.fixed-user').css("height","0px");
            }
            if($(this).scrollTop() >= 437){
                $('.fixed-user').css("height","65px");
            }
        })

    }
})();
