(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('SettingsController', SettingsController);

    SettingsController.$inject = ['Principal', 'Auth', 'JhiLanguageService', '$translate', 'Upload','$scope', '$timeout', 'DateUtils'];

    function SettingsController (Principal, Auth, JhiLanguageService, $translate, Upload, $scope, $timeout, DateUtils) {
        var vm = this;

        vm.error = null;
        vm.save = save;
        vm.settingsAccount = null;
        vm.success = null;
        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.fechaNacimiento = false;
        vm.openCalendar = openCalendar;

        /**
         * Store the "settings account" in a separate variable, and not in the shared "account" variable.
         */
        var copyAccount = function (account) {
            return {
                activated: account.activated,
                email: account.email,
                firstName: account.firstName,
                langKey: account.langKey,
                lastName: account.lastName,
                login: account.login,
                imagen: account.imagen,
                fecha_nacimiento: account.fecha_nacimiento,
                web_personal: account.web_personal,
                facebook: account.facebook,
                twitter: account.twitter,
                skype: account.skype,
                github: account.github,
                carta_presentacion: account.carta_presentacion,
                correo_alternativo: account.correo_alternativo,
                ciudad: account.ciudad,
                dni: account.dni,
                domicilio: account.domicilio
            };
        };

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function uploadUsingUploadArtwork(file) {
            var pictureName = vm.settingsAccount.login.toLowerCase()+""+vm.settingsAccount.firstName;
            var ext = file.name.split('.').pop();

            pictureName = pictureName.concat("."+ext);

            console.log(pictureName);

            Upload.upload({
                url: 'api/account/update_image',
                data: {file: file, name: pictureName}
            }).then(function () {
                vm.settingsAccount.imagen = "uploads/" + pictureName;
                Auth.updateAccount(vm.settingsAccount).then(function() {
                    vm.error = null;
                    vm.success = 'OK';
                    Principal.identity(true).then(function(account) {
                        vm.settingsAccount = copyAccount(account);
                    });
                    JhiLanguageService.getCurrent().then(function(current) {
                        if (vm.settingsAccount.langKey !== current) {
                            $translate.use(vm.settingsAccount.langKey);
                        }
                    });
                }).catch(function() {
                    vm.success = null;
                    vm.error = 'ERROR';
                });
            }, function (resp) {
                console.log('Error status: ' + resp.status);
            }, function (evt) {
                var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
            });
        }

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }

        Principal.identity().then(function(account) {
            vm.settingsAccount = copyAccount(account);
        });

        function save () {
            if($scope.picFile != undefined){
                uploadUsingUploadArtwork(imagenFile);
            }
            else{
                Auth.updateAccount(vm.settingsAccount).then(function() {
                    vm.error = null;
                    vm.success = 'OK';
                    Principal.identity(true).then(function(account) {
                        vm.settingsAccount = copyAccount(account);
                    });
                    JhiLanguageService.getCurrent().then(function(current) {
                        if (vm.settingsAccount.langKey !== current) {
                            $translate.use(vm.settingsAccount.langKey);
                        }
                    });
                }).catch(function() {
                    vm.success = null;
                    vm.error = 'ERROR';
                });
            };
        }

        var imagenFile = [];

        $scope.$watch('picFile', function(){
            if($scope.picFile != undefined){
                vm.artworkShow($scope.picFile);
                imagenFile = $scope.picFile;
            }
        });

        vm.artworkShow = function (e) {
            var reader = new FileReader();
            reader.onload = function (e) {
                var image;
                image = new Image();
                image.src = e.target.result;
                return image.onload = function () {
                    return $('.image_user').attr("src", this.src);
                };
            };
            return reader.readAsDataURL(e);
        }
    }
})();
