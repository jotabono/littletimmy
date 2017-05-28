(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('Friend_userDetailController', Friend_userDetailController);

    Friend_userDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Friend_user', 'User', 'Friendship_notification'];

    function Friend_userDetailController($scope, $rootScope, $stateParams, previousState, entity, Friend_user, User, Friendship_notification) {
        var vm = this;

        vm.friend_user = entity;
        vm.previousState = previousState.name;


        $window.document.title = "Amistad";

        vm.friend_user.$promise.then(function (response) {
            $window.document.title = "Nueva amistad de " + vm.friend_user.friend_from.login;
        });

        var unsubscribe = $rootScope.$on('littletimmyApp:friend_userUpdate', function(event, result) {
            vm.friend_user = result;
        });
        $scope.$on('$destroy', unsubscribe);

        vm.update = update;

        function update(){
            if(!vm.friend_user.friendship){
                vm.friend_user.friendship = true;

                var notFriendship = Friendship_notification.query({});
                notFriendship.$promise.then(function(){
                    for(var i = 0; i < notFriendship.length; i++){
                        if(notFriendship[i].friend_user.id == vm.friend_user.id){
                            notFriendship[i].leida = true;
                            Friendship_notification.update(notFriendship[i]);
                        }
                    }
                });

                Friend_user.update(vm.friend_user, function(res){
                    $rootScope.$broadcast('updateFriendships');
                });
            }
        }
    }
})();
