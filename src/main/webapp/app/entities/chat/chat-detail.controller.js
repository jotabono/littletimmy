(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('ChatDetailController', ChatDetailController);

    ChatDetailController.$inject = ['$scope', '$rootScope', '$timeout', '$interval', '$window', 'previousState', 'entity', 'Chat', 'User', 'Messages'];

    function ChatDetailController($scope, $rootScope, $timeout, $interval, $window, previousState, entity, Chat, User, Messages) {
        var vm = this;

        vm.chat = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('littletimmyApp:chatUpdate', function(event, result) {
            vm.chat = result;
        });
        $scope.$on('$destroy', unsubscribe);

        $rootScope.$on('emitUser',function(e,user){
            $rootScope.account = user;
        });

        entity.$promise.then(function(){
            $window.document.title = "Chat: " + vm.chat.name;
            $timeout(function () {
                $("#messagesId").animate({ scrollTop: $('#messagesId').prop("scrollHeight")}, 500);
            }, 500);

            $interval(function() {
                Chat.get({id: vm.chat.id}, function (result) {
                    if(vm.chat.messages.length < result.messages.length){
                        var dif = result.messages.length - vm.chat.messages.length;
                        var start = (result.messages.length - dif);
                        for(var i = start; i < result.messages.length; i++){
                            vm.chat.messages.push(result.messages[i]);
                            scrollChat();
                        }
                    }


                });
            }, 1000);
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

        function scrollChat(){
            $("#messagesId").animate({ scrollTop: $('#messagesId').prop("scrollHeight")}, 500);
        }

        function addMsg(res){
            vm.chat.messages.push(res);
            $scope.msg = "";
            scrollChat();
        }

    }
})();
