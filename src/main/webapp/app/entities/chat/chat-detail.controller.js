(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('ChatDetailController', ChatDetailController);

    ChatDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Chat', 'User', 'Messages'];

    function ChatDetailController($scope, $rootScope, $stateParams, previousState, entity, Chat, User, Messages) {
        var vm = this;

        vm.chat = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('littletimmyApp:chatUpdate', function(event, result) {
            vm.chat = result;
        });
        $scope.$on('$destroy', unsubscribe);

        $rootScope.$on('emitUser',function(e,user){
            $rootScope.account = user;
            console.log($rootScope.account);
        });

        vm.post = post;

        function post(msg) {
            var post = {
                text: msg,
                chat: vm.chat
            }
            if(msg.length >= 1){
                Messages.save(post, addMsg);
            }

        }

        function addMsg(res){
            vm.chat.messages.push(res);
            $scope.msg = "";
        }

    }
})();
