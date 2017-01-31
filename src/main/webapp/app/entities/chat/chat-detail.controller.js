(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('ChatDetailController', ChatDetailController);

    ChatDetailController.$inject = ['$scope', '$rootScope', '$timeout', '$interval', '$window', 'previousState', 'entity', 'Chat', 'User', 'ChatTrackerService', 'Principal', '$mdSidenav'];

    function ChatDetailController($scope, $rootScope, $timeout, $interval, $window, previousState, entity, Chat, User, ChatTrackerService, Principal, $mdSidenav) {
        var vm = this;

        vm.chat = entity;
        vm.previousState = previousState.name;
        vm.account = [];

        var unsubscribe = $rootScope.$on('littletimmyApp:chatUpdate', function(event, result) {
            vm.chat = result;
        });

        Principal.identity().then(function (account) {
            vm.account = account;
            vm.friends = [];
            User.getFriendsUser({login: vm.account.login}, function(res){
                vm.friends = res;
            });
        });

        $scope.$on('$destroy', unsubscribe);

        $rootScope.$on('emitUser',function(e,user){
            $rootScope.account = user;
        });
        $scope.toggleRight = buildToggler('right');
        $scope.isOpenRight = function(){
            return $mdSidenav('right').isOpen();
        };
        function buildToggler(navID) {
            return function () {
                // Component lookup should always be available since we are not using `ng-if`
                $mdSidenav(navID)
                    .toggle()
                    .then(function () {

                    });
            }
        }
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
