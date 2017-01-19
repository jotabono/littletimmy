(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('ChatController', ChatController);

    ChatController.$inject = ['$scope', '$state', 'Chat', 'ChatSearch'];

    function ChatController ($scope, $state, Chat, ChatSearch) {
        var vm = this;
        
        vm.chats = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Chat.query(function(result) {
                vm.chats = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ChatSearch.query({query: vm.searchQuery}, function(result) {
                vm.chats = result;
            });
        }    }
})();
