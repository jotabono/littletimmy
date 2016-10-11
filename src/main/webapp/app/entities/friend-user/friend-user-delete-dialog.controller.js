(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('Friend_userDeleteController',Friend_userDeleteController);

    Friend_userDeleteController.$inject = ['$uibModalInstance', 'entity', 'Friend_user'];

    function Friend_userDeleteController($uibModalInstance, entity, Friend_user) {
        var vm = this;

        vm.friend_user = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Friend_user.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
