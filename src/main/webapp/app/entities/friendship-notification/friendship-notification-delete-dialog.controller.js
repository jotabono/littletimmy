(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('Friendship_notificationDeleteController',Friendship_notificationDeleteController);

    Friendship_notificationDeleteController.$inject = ['$uibModalInstance', 'entity', 'Friendship_notification'];

    function Friendship_notificationDeleteController($uibModalInstance, entity, Friendship_notification) {
        var vm = this;

        vm.friendship_notification = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Friendship_notification.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
