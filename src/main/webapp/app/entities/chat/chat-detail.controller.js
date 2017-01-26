(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('ChatDetailController', ChatDetailController);

    ChatDetailController.$inject = ['$scope', '$rootScope', '$timeout', '$interval', '$window', 'previousState', 'entity', 'Chat', 'User', 'ChatTrackerService', 'Principal'];

    function ChatDetailController($scope, $rootScope, $timeout, $interval, $window, previousState, entity, Chat, User, ChatTrackerService, Principal) {
        var vm = this;

        vm.chat = entity;
        vm.previousState = previousState.name;
        vm.account = [];

        var unsubscribe = $rootScope.$on('littletimmyApp:chatUpdate', function(event, result) {
            vm.chat = result;
        });

        Principal.identity().then(function (account) {
            vm.account = account;
        });

        $scope.$on('$destroy', unsubscribe);

        $rootScope.$on('emitUser',function(e,user){
            $rootScope.account = user;
        });

        entity.$promise.then(function(res){

            ChatTrackerService.connect('chat', res.id);

            ChatTrackerService.receive().then(null, null, function(message) {
                vm.chat.messages.push(message);
                scroll();
            });

            $window.document.title = "Chat: " + vm.chat.name;
            $timeout(function () {
                $("#messagesId").scrollTop($('#messagesId').prop("scrollHeight"));
            }, 10);

        });

        vm.post = post;

        $rootScope.$on('$stateChangeStart', function(event, toState, toParams, fromState, fromParams){
            ChatTrackerService.unsubscribe();
        });

        vm.addMember = addMember;

        function addMember(){
            vm.friends = [];
            User.getFriendsUser({login: vm.account.login}, function(res){
                vm.friends = res;
            });
        }

        function post(msg) {
            var post = {
                text: msg,
                chat: vm.chat
            }
            if(msg.length >= 1){
                ChatTrackerService.sendActivity(vm.chat.id, post);
                $scope.msg = "";
                $('#messageInputId').val("");
            }
        }

        function scrollChat(){
            $("#messagesId").animate({ scrollTop: $('#messagesId').prop("scrollHeight")}, 500);
        }

        function scroll(){
            $timeout(function () {
                $("#messagesId").scrollTop($('#messagesId').prop("scrollHeight"));
            }, 10);
        }

    }
})();
