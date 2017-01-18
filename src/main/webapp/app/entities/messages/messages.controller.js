(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('MessagesController', MessagesController);

    MessagesController.$inject = ['$scope', '$state', 'Messages', 'MessagesSearch'];

    function MessagesController ($scope, $state, Messages, MessagesSearch) {
        var vm = this;
        
        vm.messages = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Messages.query(function(result) {
                vm.messages = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            MessagesSearch.query({query: vm.searchQuery}, function(result) {
                vm.messages = result;
            });
        }    }
})();
