(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('TrabajoDeleteController',TrabajoDeleteController);

    TrabajoDeleteController.$inject = ['$uibModalInstance', 'entity', 'Trabajo'];

    function TrabajoDeleteController($uibModalInstance, entity, Trabajo) {
        var vm = this;

        vm.trabajo = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Trabajo.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
