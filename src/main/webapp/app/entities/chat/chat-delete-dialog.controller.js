(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('ChatDeleteController',ChatDeleteController);

    ChatDeleteController.$inject = ['$uibModalInstance', 'entity', 'Chat'];

    function ChatDeleteController($uibModalInstance, entity, Chat) {
        var vm = this;

        vm.chat = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Chat.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
