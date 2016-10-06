(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('EstudiosDeleteController',EstudiosDeleteController);

    EstudiosDeleteController.$inject = ['$uibModalInstance', 'entity', 'Estudios'];

    function EstudiosDeleteController($uibModalInstance, entity, Estudios) {
        var vm = this;
        vm.estudios = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Estudios.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
