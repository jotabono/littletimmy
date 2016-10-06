(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('EstudiosDialogController', EstudiosDialogController);

    EstudiosDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Estudios', 'Centro'];

    function EstudiosDialogController ($scope, $stateParams, $uibModalInstance, DataUtils, entity, Estudios, Centro) {
        var vm = this;
        vm.estudios = entity;
        vm.centros = Centro.query();
        vm.load = function(id) {
            Estudios.get({id : id}, function(result) {
                vm.estudios = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('littletimmyApp:estudiosUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.estudios.id !== null) {
                Estudios.update(vm.estudios, onSaveSuccess, onSaveError);
            } else {
                Estudios.save(vm.estudios, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.fechaInicio = false;
        vm.datePickerOpenStatus.fechaFinal = false;

        vm.setArchivos = function ($file, estudios) {
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        estudios.archivos = base64Data;
                        estudios.archivosContentType = $file.type;
                    });
                });
            }
        };

        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
