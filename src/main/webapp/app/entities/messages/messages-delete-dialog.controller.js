(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('MessagesDeleteController',MessagesDeleteController);

    MessagesDeleteController.$inject = ['$uibModalInstance', 'entity', 'Messages'];

    function MessagesDeleteController($uibModalInstance, entity, Messages) {
        var vm = this;

        vm.messages = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Messages.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
