(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('Friend_userDialogController', Friend_userDialogController);

    Friend_userDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Friend_user', 'User'];

    function Friend_userDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Friend_user, User) {
        var vm = this;

        vm.friend_user = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.friend_user.id !== null) {
                Friend_user.update(vm.friend_user, onSaveSuccess, onSaveError);
            } else {
                Friend_user.save(vm.friend_user, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('littletimmyApp:friend_userUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.friendship_date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
