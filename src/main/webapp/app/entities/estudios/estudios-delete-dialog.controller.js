(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('EstudiosDeleteController',EstudiosDeleteController);

    EstudiosDeleteController.$inject = ['$uibModalInstance', 'entity', 'Estudios'];

    function EstudiosDeleteController($uibModalInstance, entity, Estudios) {
        var vm = this;

        vm.estudios = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Estudios.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
